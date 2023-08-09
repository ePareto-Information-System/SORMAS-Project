/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package de.symeda.sormas.ui.caze.importer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.data.provider.*;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import de.symeda.sormas.api.caze.*;
import de.symeda.sormas.api.externalsurveillancetool.ExternalSurveillanceToolRuntimeException;
import de.symeda.sormas.api.importexport.*;
import de.symeda.sormas.api.person.PersonNameDto;
import de.symeda.sormas.api.utils.PickMerge;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.caze.CasesView;
import de.symeda.sormas.ui.caze.CasesViewConfiguration;
import de.symeda.sormas.ui.caze.MergeImportedCasesGrid;
import de.symeda.sormas.ui.importer.*;
import de.symeda.sormas.ui.importer.ImportLineResult;
import de.symeda.sormas.ui.utils.CaseDownloadUtil;
import org.apache.commons.lang3.ArrayUtils;

import com.opencsv.exceptions.CsvValidationException;
import com.vaadin.server.Sizeable.Unit;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.caseimport.CaseImportEntities;
import de.symeda.sormas.api.caze.caseimport.CaseImportFacade;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.person.PersonFacade;
import de.symeda.sormas.api.user.UserDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent.DiscardListener;
import de.symeda.sormas.ui.utils.VaadinUiUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Data importer that is used to import cases and associated samples.
 * This importer adds the following logic:
 * <p>
 * - Check the database for similar cases and, if at least one is found, execute the
 * similarityCallback received by the calling class.
 * - The import will wait for the similarityCallback to be resolved before it is continued
 * - Based on the results of the similarityCallback, an existing case might be overridden by
 * the data in the CSV file
 * - Save the person and case to the database (unless the case was skipped or the import
 * was canceled)
 */
public class CaseImporter extends DataImporter {

    private UI currentUI;
    private final CaseImportFacade caseImportFacade;

    private final PersonFacade personFacade;
    private final CaseFacade caseFacade;

    private boolean showAllDuplicatesAfter;

    private boolean displayRecordsFromExcel;
    private List<String> duplicateSimilarCaseIds;
    private List<String> duplicateSimilarPersonIds;
    private MergeImportedCasesGrid mergeImportedCasesGrid;
    private final ExportConfigurationDto detailedExportConfiguration;
    private final CasesViewConfiguration viewConfiguration;
    private ImportExportUtils.PropertyNameFilter errorReportConsumer;
    private ImportResultStatus importResult;

    public CaseImporter(File inputFile, boolean hasEntityClassRow, UserDto currentUser, ValueSeparator csvSeparator) throws IOException {
        super(inputFile, hasEntityClassRow, currentUser, csvSeparator);

        caseImportFacade = FacadeProvider.getCaseImportFacade();
        personFacade = FacadeProvider.getPersonFacade();
        caseFacade = FacadeProvider.getCaseFacade();
        mergeImportedCasesGrid = new MergeImportedCasesGrid();
        detailedExportConfiguration = buildDetailedExportConfiguration();
        viewConfiguration = ViewModelProviders.of(CasesView.class).get(CasesViewConfiguration.class);
    }

    @Override
    public void startImport(Consumer<StreamResource> addErrorReportToLayoutCallback, UI currentUI, boolean duplicatesPossible)
            throws IOException, CsvValidationException {

        this.currentUI = currentUI;
        super.startImport(addErrorReportToLayoutCallback, currentUI, duplicatesPossible);
    }


    public void startImport(Consumer<StreamResource> addErrorReportToLayoutCallback, UI currentUI, boolean duplicatesPossible, boolean showAllDuplicatesAfter)
            throws IOException, CsvValidationException {

        this.showAllDuplicatesAfter = showAllDuplicatesAfter;
        if (showAllDuplicatesAfter) {
            this.duplicateSimilarCaseIds = new ArrayList<>();
            this.duplicateSimilarPersonIds = new ArrayList<>();
            this.onRunImportComplete = (ImportResultStatus result) -> {
                handleDuplicatesMerge();
            };
        }
        this.startImport(addErrorReportToLayoutCallback, currentUI, duplicatesPossible);
    }


    public void startImportToDisplayOnModal(Consumer<StreamResource> addErrorReportToLayoutCallback, UI currentUI, boolean displayRecordsFromExcel)
            throws IOException, CsvValidationException {

        this.displayRecordsFromExcel = displayRecordsFromExcel;

        ImportProgressLayout progressLayout = this.getImportProgressLayoutMergePick(currentUI, displayRecordsFromExcel);

        importedLineCallback = progressLayout::updateProgressMergedDuplicate;

        Window window = VaadinUiUtil.createPopupWindow();
        window.setCaption(I18nProperties.getString(Strings.headingDataImport));
        window.setWidth(800, Unit.PIXELS);
        window.setContent(progressLayout);
        window.setClosable(false);
        currentUI.addWindow(window);

        Thread importThread = new Thread(() -> {
            try {
                currentUI.setPollInterval(300);
                I18nProperties.setUserLanguage(currentUser.getLanguage());
                FacadeProvider.getI18nFacade().setUserLanguage(currentUser.getLanguage());

                importResult = runImportToPopulateModal();

                // Display a window presenting the import result
                currentUI.access(() -> {
                    window.setClosable(true);
                    progressLayout.makeClosable(window::close);

                    if (importResult == ImportResultStatus.COMPLETED) {
                        progressLayout.displaySuccessIcon();
                        progressLayout.setInfoLabelText(I18nProperties.getString(Strings.messageImportSuccessful));

                        importResult = performMergeOrPickActionBasedOnUsersChoiceOnExcel();

                    } else if (importResult == ImportResultStatus.COMPLETED_WITH_ERRORS) {
                        progressLayout.displayWarningIcon();
                        progressLayout.setInfoLabelText(I18nProperties.getString(Strings.messageImportPartiallySuccessful));
                    } else if (importResult == ImportResultStatus.CANCELED) {
                        progressLayout.displaySuccessIcon();
                        progressLayout.setInfoLabelText(I18nProperties.getString(Strings.messageImportCanceled));
                    } else {
                        progressLayout.displayWarningIcon();
                        progressLayout.setInfoLabelText(I18nProperties.getString(Strings.messageImportCanceledErrors));
                    }

                    window.addCloseListener(e -> {
                        if (importResult == ImportResultStatus.COMPLETED_WITH_ERRORS || importResult == ImportResultStatus.CANCELED_WITH_ERRORS) {
                            StreamResource streamResource = createErrorReportStreamResource();
                            errorReportConsumer.accept(String.valueOf(streamResource));
                        }

//						if (onRunImportComplete != null) {
//							onRunImportComplete.accept(importResult);
//						}
                    });

                    currentUI.setPollInterval(-1);
                });
            } catch (InvalidColumnException e) {
                currentUI.access(() -> {
                    window.setClosable(true);
                    progressLayout.makeClosable(window::close);
                    progressLayout.displayErrorIcon();
                    progressLayout.setInfoLabelText(String.format(I18nProperties.getString(Strings.messageImportInvalidColumn), e.getColumnName()));
                    currentUI.setPollInterval(-1);
                });
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                currentUI.access(() -> {
                    window.setClosable(true);
                    progressLayout.makeClosable(window::close);
                    progressLayout.displayErrorIcon();
                    progressLayout.setInfoLabelText(I18nProperties.getString(Strings.messageImportFailedFull));
                    currentUI.setPollInterval(-1);
                });
            }
        });

        importThread.start();
        //this.startImport(addErrorReportToLayoutCallback, currentUI, duplicatesPossible);
    }

    private ImportResultStatus performMergeOrPickActionBasedOnUsersChoiceOnExcel() {

        ImportResultStatus resultStatus = null;
        try {
            int total = readImportFileLength(inputFile);
            if (total == 0) {
                new Notification("Please make Sure You have Records on the Sheet", Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
                resultStatus = ImportResultStatus.CANCELED_WITH_ERRORS;
            }
            //checking if records are two pairs parent and child
            if ((total) % 2 == 0) {
                ImportLineResult importLineResult = importMergeOrPickRecordFromExportedDuplicatesCsvFile();

//                    if (importedLineCallback != null) {
//                        importedLineCallback.accept(importLineResult);
//                    }

            } else {
                new Notification("Please make Sure Record(s) have Parent and Child Pairs", Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
                resultStatus = ImportResultStatus.CANCELED_WITH_ERRORS;

            }
            resultStatus = resultStatus.COMPLETED;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return resultStatus;
    }


    private Set<String> getSelectedRows() {
        MergeImportedCasesGrid exportMergeGrid = this.mergeImportedCasesGrid;
//		return exportMergeGrid.asMultiSelect().getSelectedItems().stream().map(CaseIndexDto::getUuid).collect(Collectors.toSet());
        return this.viewConfiguration.isInEagerMode()
                ? exportMergeGrid.asMultiSelect().getSelectedItems().stream().map(CaseIndexDto::getUuid).collect(Collectors.toSet())
                : Collections.emptySet();
    }

    private ExportConfigurationDto buildDetailedExportConfiguration() {
        ExportConfigurationDto config = ExportConfigurationDto.build(UserProvider.getCurrent().getUserReference(), ExportType.CASE);

        config.setProperties(
                ImportExportUtils.getCaseExportProperties(CaseDownloadUtil::getPropertyCaption, true, true)
                        .stream()
                        .map(ExportPropertyMetaInfo::getPropertyId)
                        .collect(Collectors.toSet()));
        return config;
    }

    protected void handleDuplicatesMerge() {


        System.out.println("The case uuids founds ------------------: " + duplicateSimilarCaseIds.size());
        System.out.println("The person uuids founds------------------: " + duplicateSimilarPersonIds.size());

        if (duplicateSimilarCaseIds.isEmpty() && duplicateSimilarPersonIds.isEmpty()) {
            return;
        }
//		RegionReferenceDto region = currentUser.getRegion();
//		System.out.println(currentUser.getRegion());
        CaseCriteria criteria = new CaseCriteria();

        //Date going back in time
        //LocalDate beginningLocalDate = LocalDate.of(1, 1, 1);
        //Date date = Date.from(beginningLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

       //LocalDate currentDate = LocalDate.now();

        // Calculate the last day of the previous month
        //LocalDate lastDayOfPreviousMonth = currentDate.minusMonths(1).withDayOfMonth(currentDate.minusMonths(1).lengthOfMonth());

        // Create a Date object from the LocalDate
       // Date date = Date.from(lastDayOfPreviousMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());


        int currentYear = LocalDate.now().getYear();

        // Calculate the last day of the last month of the previous year
        LocalDate lastDayOfLastMonthOfPreviousYear = LocalDate.of(currentYear - 1, Month.DECEMBER, 1)
                .withDayOfMonth(Month.DECEMBER.maxLength());

        // Create a Date object from the LocalDate
        Date date = Date.from(lastDayOfLastMonthOfPreviousYear.atStartOfDay(ZoneId.systemDefault()).toInstant());


        criteria.creationDateFrom(date)
                .creationDateTo(new Date())
                .setRegion(UserProvider.getCurrent().getUser().getRegion());

        criteria.setCaseUuids(duplicateSimilarCaseIds);
        criteria.setPersonsUuids(duplicateSimilarPersonIds);

        criteria.caseLike("");
        criteria.eventLike("");
        criteria.setReportingUserLike("");

        mergeImportedCasesGrid.setCriteria(criteria);
        mergeImportedCasesGrid.reload();


        VerticalLayout mergeLayout = new VerticalLayout();

//		StreamResource exportStreamResource = CaseDownloadUtil.createCaseExportResourceDuplicates(
//				mergeImportedCasesGrid.getCriteria(),
//				this::getSelectedRows,
//				CaseExportType.CASE_SURVEILLANCE,
//				detailedExportConfiguration,false);
//
//		mergeImportedCasesGrid.addExportButton(
//				exportStreamResource,
//				mergeLayout,
//				VaadinIcons.FILE_TEXT,
//				Captions.exportDownloadMergeFile,
//				Strings.infoDetailedExport);

        Button exportButton = createExportButton(mergeImportedCasesGrid);

        mergeLayout.addComponent(exportButton);

        mergeLayout.addComponent(mergeImportedCasesGrid);
        if (mergeImportedCasesGrid.dataCount > 0) {

            currentUI.accessSynchronously(() -> {
                final CommitDiscardWrapperComponent<VerticalLayout> component =
                        new CommitDiscardWrapperComponent<VerticalLayout>(mergeLayout);
                component.getDiscardButton().setVisible(false);
                component.getCommitButton().setCaption(I18nProperties.getCaption(Captions.actionClose));
                component.addCommitListener(() -> {
                    //closed
                });

                VaadinUiUtil.showModalPopupWindow(component, I18nProperties.getCaption(Captions.caseMergeDuplicates));

            });

        }


    }


    public ImportLineResult importMergeOrPickRecordFromExportedDuplicatesCsvFile() {
        ImportLineResult importLineResult = null;
        LinkedHashMap<CaseMDDataDto, CaseMDDataDto>parentChildMap = new LinkedHashMap<>(entities.getParentChildMap());
        List<Map.Entry<CaseMDDataDto, CaseMDDataDto>> entries =new CopyOnWriteArrayList<>(parentChildMap.entrySet());

        int totalSize = parentChildMap.size();
        int currentIteration = 1;
        boolean showNotification = false;
        CaseMDDataDto targetMergeParentNode = null;
        CaseMDDataDto targetMergeChildNode = null;
        CaseMDDataDto targetPickParentNodePick=null;
        CaseMDDataDto targetPickChildNodePick=null;

        boolean showNotificationDontHaveEmptyCells = false;
        boolean showNotificationAlreadyMerged = false;

            for (Map.Entry<CaseMDDataDto, CaseMDDataDto> entry : entries) {

                CaseMDDataDto parent = entry.getKey();
                CaseMDDataDto child = entry.getValue();
                System.out.println(parent + " - " + child);

                if (child != null) {

                    LinkedHashMap<CaseDataDto, CaseDataDto> casePairFromDb = FacadeProvider.getCaseFacade()
                            .getCaseDataByParentAndChildUuid(parent.getCaseId(), child.getCaseId());

                    PickMerge pickMergeParent = getPickMergeValue(parent.getActions());
                    PickMerge pickMergeChild = getPickMergeValue(child.getActions());

                    if ((pickMergeParent == null && pickMergeChild == null) || (pickMergeParent != null && pickMergeChild != null)) {
                        // Check if it is the last iteration
                        if (currentIteration == totalSize) {
                            // This is the last iteration
                            showNotification = true;
                        }
                        importLineResult = ImportLineResult.ERROR;
                    }

                    CaseDataDto parentDto = null;
                    CaseDataDto childDto = null;
                    if (!casePairFromDb.isEmpty()) {
                        Map.Entry<CaseDataDto, CaseDataDto> firstEntry = casePairFromDb.entrySet().iterator().next();
                        parentDto = firstEntry.getKey();
                        childDto = firstEntry.getValue();
                    } else {
                        parentChildMap.remove(parent);
                        importLineResult = ImportLineResult.ERROR;
                        if (importedLineCallback != null) {
                            importedLineCallback.accept(importLineResult);
                        }
                        continue;
                    }

                    if (isCaseAlreadyMerged(parentDto, childDto)) {
                        if (currentIteration == totalSize) {
                            // This is the last iteration
                            showNotificationAlreadyMerged = true;
                        }
                        importLineResult = ImportLineResult.SKIPPED;
                        //skip
                    }

                    if (parentDto == null && childDto == null) {
                        if (currentIteration == totalSize) {
                            // This is the last iteration
                            showNotificationDontHaveEmptyCells = true;
                        }
                        importLineResult = ImportLineResult.ERROR;
                    }
                    if (pickMergeParent == null) {
                        pickMergeParent = PickMerge.CANCEL;
                    }
                    if (pickMergeChild == null) {
                        pickMergeChild = PickMerge.CANCEL;
                    }

                    if (pickMergeChild.equals(PickMerge.CANCEL) && pickMergeParent.equals("Hide")) {
                        importLineResult = ImportLineResult.SKIPPED;
                        //skip
                    }
                    if (pickMergeParent.equals(PickMerge.CANCEL) && pickMergeChild.equals("Hide")) {
                        importLineResult = ImportLineResult.SKIPPED;
                        //skip
                    }

                    if (pickMergeParent.equals(PickMerge.PICK) && pickMergeChild.equals(PickMerge.CANCEL) && parentDto != null) {
                        importLineResult = pick(parentDto, childDto);

                        if (importLineResult == ImportLineResult.PICKED) {
                            //remove parent and child of that particular iteration
                            parentChildMap.remove(parent);
                        }
                    }

                    if (pickMergeChild.equals(PickMerge.PICK) && pickMergeParent.equals(PickMerge.CANCEL) && childDto != null) {
                        importLineResult = pick(childDto, parentDto);

                        if (importLineResult == ImportLineResult.PICKED) {
                            //remove parent and child of that particular iteration
                            parentChildMap.remove(parent);
                        }
                    }

                    if (pickMergeParent.equals(PickMerge.MERGE) && pickMergeChild.equals(PickMerge.CANCEL) && parentDto != null) {
                        importLineResult = merge(parentDto, childDto);

                        if (importLineResult == ImportLineResult.MERGED) {
                            // Remove parent and its children
                            removeChildrenByParent(parentChildMap, child);

                        }
                    }

                    if (pickMergeChild.equals(PickMerge.MERGE) && pickMergeParent.equals(PickMerge.CANCEL) && childDto != null) {
                        importLineResult = merge(childDto, parentDto);

                        if (importLineResult == ImportLineResult.MERGED) {
                            // Treat child as a parent and remove the children of that parent as well as the parent itself
                            removeChildrenByParent(parentChildMap, parent);
                        }

                    }

                    if (importedLineCallback != null) {
                        importedLineCallback.accept(importLineResult);
                    }

                }


            if (showNotification || showNotificationDontHaveEmptyCells || showNotificationAlreadyMerged) {
                break; // Exit the outer loop since the notification is shown
            }
            currentIteration++;

        }

        if (showNotification) {
            new Notification("Please choose one of the following: Pick, Merge, or Hide For either a Parent or Child Record", Notification.Type.ERROR_MESSAGE)
                    .show(Page.getCurrent());
        }

        if (showNotificationDontHaveEmptyCells) {
            new Notification("Please ensure that your excel sheets do not have empty cells. " +
                    "Make sure to select the white area, right click, and delete.", Notification.Type.ERROR_MESSAGE)
                    .show(Page.getCurrent());
        }

        if (showNotificationAlreadyMerged) {
            new Notification("Sorry one or more of the records have already been merged", Notification.Type.ERROR_MESSAGE)
                    .show(Page.getCurrent());
        }

        return importLineResult;
    }



    private void removeParentsByChild(LinkedHashMap<CaseMDDataDto, CaseMDDataDto> parentChildMap, CaseMDDataDto child) {
        Iterator<Map.Entry<CaseMDDataDto, CaseMDDataDto>> iterator = parentChildMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<CaseMDDataDto, CaseMDDataDto> entry = iterator.next();
            CaseMDDataDto parent = entry.getKey();

            if (child.getCaseId().equals(child.getCaseId())) {
                iterator.remove();
                removeChildrenByParent(parentChildMap, parent);
            }
        }
    }

    private static void removeChildrenByParent(LinkedHashMap<CaseMDDataDto, CaseMDDataDto> parentChildMap, CaseMDDataDto targetedKey) {
        parentChildMap.entrySet().removeIf(entry ->
                entry.getKey().getCaseId().equals(targetedKey.getCaseId()) || entry.getValue().getCaseId().equals(targetedKey.getCaseId())
        );
    }




//    public void removeNodesFromParentChildMap(ConcurrentHashMap<CaseMDDataDto, List<CaseMDDataDto>> parentChildMap, CaseMDDataDto removedNode) {
//        if (parentChildMap.size()<1){
//            return;
//        }
//
//        parentChildMap.forEach((parent, children) -> {
//            children.removeIf(child -> child.getCaseId().equals(removedNode.getCaseId())); // Remove the specified node from the children list
//        });
//
//        parentChildMap.entrySet().removeIf(entry -> {
//            if (entry.getKey().getCaseId().equals(removedNode.getCaseId())) {
//                return true; // Remove the parent and its children from the map
//            } else {
//                List<CaseMDDataDto> children = entry.getValue();
//                children.removeIf(child -> child.getCaseId().equals(removedNode.getCaseId())); // Remove the specified node from the children list
//                return children.isEmpty(); // Remove the parent if it has no remaining children
//            }
//        });
//
//
//    }




    private PickMerge getPickMergeValue(String action) {
        if (action != null) {
            String actionUpperCase = StringUtils.toRootUpperCase(action).toString();
            if (actionUpperCase.equals("PICK") || actionUpperCase.equals("MERGE") || actionUpperCase.equals("HIDE")) {
                return PickMerge.valueOf(actionUpperCase);
            }
        }
        return null;
    }

    private boolean isCaseAlreadyMerged(CaseDataDto parentDto, CaseDataDto childDto) {
        return (parentDto == null && childDto != null) || (parentDto != null && childDto == null);
    }


    protected ImportLineResult merge(CaseDataDto targetedCase, CaseDataDto caseToMergeAndDelete) {
        caseFacade.mergeCase(targetedCase.getUuid(), caseToMergeAndDelete.getUuid());
        boolean deletePerformed = deleteCaseAsDuplicate(targetedCase, caseToMergeAndDelete);

        if (deletePerformed && caseFacade.isDeleted(caseToMergeAndDelete.getUuid())) {
            //reload();
            System.out.println(I18nProperties.getString(Strings.messageCasesMerged));

            new Notification(I18nProperties.getString(Strings.messageCasesMerged), Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
            return ImportLineResult.MERGED;

        } else {

            System.out.println(I18nProperties.getString(Strings.errorCaseMerging));

            new Notification(I18nProperties.getString(Strings.errorCaseMerging), Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());

            return ImportLineResult.ERROR;

        }
    }


    protected ImportLineResult pick(CaseDataDto targetedCase, CaseDataDto caseToDelete) {
        boolean deletePerformed = deleteCaseAsDuplicate(targetedCase, caseToDelete);

        if (deletePerformed && caseFacade.isDeleted(caseToDelete.getUuid())) {
            //reload();
            System.out.println(I18nProperties.getString(Strings.messageCaseDuplicateDeleted));
            new Notification(I18nProperties.getString(Strings.messageCaseDuplicateDeleted), Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
            return ImportLineResult.PICKED;
        } else {
            System.out.println(I18nProperties.getString(Strings.errorCaseDuplicateDeletion));
            new Notification(I18nProperties.getString(Strings.errorCaseDuplicateDeletion), Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
            return ImportLineResult.ERROR;

        }
    }

    @SuppressWarnings("unchecked")
    private boolean deleteCaseAsDuplicate(CaseDataDto caze, CaseDataDto caseToMergeAndDelete) {
        try {
            caseFacade.deleteCaseAsDuplicate(caseToMergeAndDelete.getUuid(), caze.getUuid());
        } catch (ExternalSurveillanceToolRuntimeException e) {
            return false;
        }

        return true;
    }

    private Button createExportButton(Grid<CaseIndexDto> grid) {
        Button exportButton = new Button("Export to CSV");
        StreamResource.StreamSource streamSource = () -> {
            try {
                String csvContent = generateCsvContent((grid));
                return new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        };
        StreamResource resource = new StreamResource(streamSource, "sormas_merge_duplicate_import_data.csv");
        FileDownloader downloader = new FileDownloader(resource);
        downloader.extend(exportButton);
        return exportButton;
    }


    private static <T> String generateCsvContent(Grid<T> grid) {
        List<String> headers = grid.getColumns().stream()
                .map(column -> column.getCaption())
                .collect(Collectors.toList());

        StringBuilder csvContent = new StringBuilder();
        csvContent.append("CaseMDData,CaseMDData,CaseMDData,CaseMDData,CaseMDData,CaseMDData,CaseMDData,CaseMDData,CaseMDData,CaseMDData,CaseMDData,CaseMDData,CaseMDData\n");

        csvContent.append("caseId,disease,caseClassification,firstName,lastName,approximateAgeBirthdate,sex,responsibleDistrict,healthFacility,reportDate,creationDate,completeness,actions\n");

        String[] headerItems = headers.toArray(new String[0]);
        headerItems[0] = "##" + headerItems[0];

        csvContent.append(String.join(",", headerItems)).append("\n");

        DataProvider<T, ?> dataProvider = grid.getDataProvider();
        if (dataProvider instanceof HierarchicalDataProvider) {
            HierarchicalDataProvider<T, ?> hierarchicalDataProvider = (HierarchicalDataProvider<T, ?>) dataProvider;
            T parentItem = grid.getSelectedItems().stream().findFirst().orElse(null);
            Stream<T> descendants = fetchAllDescendants(parentItem, hierarchicalDataProvider);

            descendants.forEach(item -> {
                List<String> rowValues = grid.getColumns().stream()
                        .map(column -> getColumnValue(column, item))
                        .collect(Collectors.toList());
                csvContent.append(String.join(",", rowValues)).append("\n");
            });
        } else {
            // Non-hierarchical data provider
            List<T> items = dataProvider.fetch(new Query<>()).collect(Collectors.toList());

            for (T item : items) {
                List<String> rowValues = grid.getColumns().stream()
                        .map(column -> getColumnValue(column, item))
                        .collect(Collectors.toList());
                csvContent.append(String.join(",", rowValues)).append("\n");
            }
        }

        return csvContent.toString();
    }


    private static <T> String getColumnValue(Grid.Column<T, ?> column, T item) {
        Object value = column.getValueProvider().apply(item);
        if (value != null) {
            // Format the value as needed
            if (value instanceof Date) {
                // Example formatting: yyyy-MM-dd
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                value = dateFormat.format((Date) value);
            } else if (value instanceof AgeAndBirthDateDto) {
                // Example formatting: "40 (22/1/1987)"
                AgeAndBirthDateDto ageAndBirthDateDto = (AgeAndBirthDateDto) value;
                value = ageAndBirthDateDto.getAge() + " (" + ageAndBirthDateDto.getDateOfBirthDD() + "/" + ageAndBirthDateDto.getDateOfBirthMM() + "/" + ageAndBirthDateDto.getDateOfBirthYYYY() + ")";
            } else if (value instanceof Link) {
                Link link = (Link) value;
                String sourceURL = ((ExternalResource) link.getResource()).getURL();

                int startIndex = sourceURL.lastIndexOf("/") + 1;
                int endIndex = sourceURL.length();
                String extractedValue = sourceURL.substring(startIndex, endIndex);

                value = link.getCaption();
            } else if (value instanceof Label) {
                Label lable = (Label) value;
                value = lable.getValue();
            } else if (value instanceof HorizontalLayout) {
                HorizontalLayout horizontalLayout = (HorizontalLayout) value;
                StringBuilder sb = new StringBuilder();
                for (Component component : horizontalLayout) {
                    if (component instanceof Button) {
                        Button button = (Button) component;
                        sb.append(button.getCaption()).append(";");
                    }
                }
                return sb.toString().trim();
            } else {
                // Return the value as string if no specific formatting is applied
                return String.valueOf(value);
            }
        }
        return String.valueOf(value);
    }


    private static <T> Stream<T> fetchAllDescendants(T parent, HierarchicalDataProvider<T, ?> dataProvider) {
        List<T> children = getChildren(parent, dataProvider);
        if (children.isEmpty()) {
            return Stream.empty();
        }

        return children.stream()
                .flatMap(child -> Stream.concat(Stream.of(child), fetchAllDescendants(child, dataProvider)));
    }

    private static <T> List<T> getChildren(T parent, HierarchicalDataProvider<T, ?> dataProvider) {
        List<T> children = new ArrayList<>();
        dataProvider.fetchChildren(new HierarchicalQuery<>(null, parent))
                .forEach(children::add);
        return children;
    }


    @Override
    protected ImportLineResult importDataFromCsvLine(
            String[] values,
            String[] entityClasses,
            String[] entityProperties,
            String[][] entityPropertyPaths,
            boolean firstLine)
            throws IOException, InvalidColumnException, InterruptedException {

        // regenerate the UUID to prevent overwrite in case of export and import of the same entities
        int uuidIndex = ArrayUtils.indexOf(entityProperties, CaseDataDto.UUID);
        int existingCase = ArrayUtils.indexOf(entityProperties, CaseDataDto.NEW_EXISTING);

        if (uuidIndex >= 0) {
            if (existingCase == -1) {
                values[uuidIndex] = DataHelper.createUuid();
            }
        }

        ImportLineResultDto<CaseImportEntities> importResult =
                caseImportFacade.importCaseData(values, entityClasses, entityProperties, entityPropertyPaths, !firstLine);

        if (importResult.isError()) {
            writeImportError(values, importResult.getMessage());
            return ImportLineResult.ERROR;
        } else if (importResult.isMerge()) {
            CaseImportEntities entities = importResult.getImportEntities();
            CaseDataDto importCase = entities.getCaze();
            PersonDto importPerson = entities.getPerson();
            List<PersonNameDto> similarPersons = entities.getSimilarPersons();

            if (showAllDuplicatesAfter) {
//				duplicateSimilarPersonIds.add(entities.getSimilarPersons().get(0).getUuid() );
                similarPersons.forEach(personNameDto -> {
                    duplicateSimilarPersonIds.add(personNameDto.getUuid());
                });
//				duplicateSimilarCaseIds.add(importCase.getUuid());
                List<CaseDataDto> listSimilarCases = caseFacade.getByPersonUuids(duplicateSimilarPersonIds);
                listSimilarCases.forEach(
                        listSimilarCase ->
                                duplicateSimilarCaseIds.add(listSimilarCase.getUuid())
                );
            } else {
                duplicateSimilarCaseIds.add(importCase.getUuid());
                duplicateSimilarPersonIds.add(importPerson.getUuid());
            }

            return ImportLineResult.DUPLICATE;
        } else if (importResult.isDuplicate()) {
            CaseImportEntities entities = importResult.getImportEntities();
            CaseDataDto importCase = entities.getCaze();
            PersonDto importPerson = entities.getPerson();

            String selectedPersonUuid = null;
            String selectedCaseUuid = null;

            CaseImportConsumer consumer = new CaseImportConsumer();
            ImportSimilarityResultOption resultOption = null;

            if (showAllDuplicatesAfter) {
                duplicateSimilarPersonIds.add(entities.getSimilarPersons().get(0).getUuid());
            } else {
                CaseImportLock personSelectLock = new CaseImportLock();
                // We need to pause the current thread to prevent the import from continuing until the user has acted
                synchronized (personSelectLock) {
                    // Call the logic that allows the user to handle the similarity; once this has been done, the LOCK should be notified
                    // to allow the importer to resume
                    handlePersonSimilarity(
                            importPerson,
                            result -> consumer.onImportResult(result, personSelectLock),
                            (person, similarityResultOption) -> new CaseImportSimilarityResult(person, null, similarityResultOption),
                            Strings.infoSelectOrCreatePersonForImport,
                            currentUI);
                    try {
                        if (!personSelectLock.wasNotified) {
                            personSelectLock.wait();
                        }
                    } catch (InterruptedException e) {
                        logger.error("InterruptedException when trying to perform LOCK.wait() in case import: " + e.getMessage());
                        throw e;
                    }

                    if (consumer.result != null) {
                        resultOption = consumer.result.getResultOption();
                    }

                    // If the user picked an existing person, override the case person with it
                    if (ImportSimilarityResultOption.PICK.equals(resultOption)) {
                        selectedPersonUuid = consumer.result.getMatchingPerson().getUuid();
                        // Reset the importResult option for case selection
                        resultOption = null;
                    }
                }
            }

            if (ImportSimilarityResultOption.SKIP.equals(resultOption)) {
                return ImportLineResult.SKIPPED;
            } else {
                final CaseImportLock caseSelectLock = new CaseImportLock();
                synchronized (caseSelectLock) {
                    // Retrieve all similar cases from the database
                    CaseSimilarityCriteria criteria =
                            CaseSimilarityCriteria.forCase(importCase, selectedPersonUuid != null ? selectedPersonUuid : importPerson.getUuid());

                    List<CaseSelectionDto> similarCases = caseFacade.getSimilarCases(criteria);

                    if (similarCases.size() > 0) {
                        // Call the logic that allows the user to handle the similarity; once this has been done, the LOCK should be notified
                        // to allow the importer to resume
                        if (selectedPersonUuid != null) {
                            importPerson = personFacade.getPersonByUuid(selectedPersonUuid);
                        }

                        // handleCaseSimilarity(
                        // 	new CaseImportSimilarityInput(importCase, importPerson, similarCases),
                        // 	result -> consumer.onImportResult(result, caseSelectLock));
                        if (showAllDuplicatesAfter) {
                            duplicateSimilarCaseIds.add(similarCases.get(0).getUuid());
                        } else {
                            handleCaseSimilarity(
                                    new CaseImportSimilarityInput(importCase, importPerson, similarCases),
                                    result -> consumer.onImportResult(result, caseSelectLock));
                        }

                        try {
                            if (!caseSelectLock.wasNotified) {
                                caseSelectLock.wait();
                            }
                        } catch (InterruptedException e) {
                            logger.error("InterruptedException when trying to perform LOCK.wait() in case import: " + e.getMessage());
                            throw e;
                        }

                        if (consumer.result != null) {
                            resultOption = consumer.result.getResultOption();
                        }

                        // If the user chose to override an existing case with the imported case, insert the new data into the existing case and associate the imported samples with it
                        if (resultOption == ImportSimilarityResultOption.OVERRIDE
                                && consumer.result != null
                                && consumer.result.getMatchingCase() != null) {
                            selectedCaseUuid = consumer.result.getMatchingCase().getUuid();
                        }
                    }
                }
            }

            if (resultOption == ImportSimilarityResultOption.SKIP) {
                consumer.result = null;
                return ImportLineResult.SKIPPED;
            } else if (resultOption == ImportSimilarityResultOption.PICK) {
                consumer.result = null;
                return ImportLineResult.DUPLICATE;
            } else if (resultOption == ImportSimilarityResultOption.CANCEL) {
                cancelImport();
                return ImportLineResult.SKIPPED;
            } else {
                ImportLineResultDto<CaseImportEntities> saveResult;
                if (selectedPersonUuid != null || selectedCaseUuid != null) {
                    saveResult =
                            caseImportFacade.updateCaseWithImportData(selectedPersonUuid, selectedCaseUuid, values, entityClasses, entityPropertyPaths, false);
                } else {
                    saveResult = caseImportFacade.saveImportedEntities(entities, false);
                }

                if (saveResult.isError()) {
                    writeImportError(values, importResult.getMessage());
                    return ImportLineResult.ERROR;
                }
            }
        }

        return ImportLineResult.SUCCESS;
    }


//	@Override
//	protected ImportLineResult importDataFromCsvLine(
//		String[] values,
//		String[] entityClasses,
//		String[] entityProperties,
//		String[][] entityPropertyPaths,
//		boolean firstLine)
//		throws IOException, InvalidColumnException, InterruptedException {
//
//		// regenerate the UUID to prevent overwrite in case of export and import of the same entities
//		setValueUuid(values, entityProperties, DataHelper.createUuid());
//
//		ImportLineResultDto<CaseImportEntities> importResult =
//			caseImportFacade.importCaseData(values, entityClasses, entityProperties, entityPropertyPaths, !firstLine);
//
//		if (importResult.isError()) {
//			writeImportError(values, importResult.getMessage());
//			return ImportLineResult.ERROR;
//		}
//
//		else if (importResult.isDuplicate()) {
//			CaseImportEntities entities = importResult.getImportEntities();
//			CaseDataDto importCase = entities.getCaze();
//			PersonDto importPerson = entities.getPerson();
//
//			String selectedPersonUuid = null;
//			String selectedCaseUuid = null;
//
//			CaseImportConsumer consumer = new CaseImportConsumer();
//			ImportSimilarityResultOption resultOption = null;
//
////			CaseImportLock personSelectLock = new CaseImportLock();
//			// We need to pause the current thread to prevent the import from continuing until the user has acted
////			synchronized (personSelectLock) {
////				// Call the logic that allows the user to handle the similarity; once this has been done, the LOCK should be notified
////				// to allow the importer to resume
////				handlePersonSimilarity(
////					importPerson,
////					result -> consumer.onImportResult(result, personSelectLock),
////					(person, similarityResultOption) -> new CaseImportSimilarityResult(person, null, similarityResultOption),
////					Strings.infoSelectOrCreatePersonForImport,
////					currentUI);
////
////				try {
////					if (!personSelectLock.wasNotified) {
////						personSelectLock.wait();
////					}
////				} catch (InterruptedException e) {
////					logger.error("InterruptedException when trying to perform LOCK.wait() in case import: " + e.getMessage());
////					throw e;
////				}
////
////				if (consumer.result != null) {
////					resultOption = consumer.result.getResultOption();
////				}
////
////				// If the user picked an existing person, override the case person with it
////				if (ImportSimilarityResultOption.PICK.equals(resultOption)) {
////					selectedPersonUuid = consumer.result.getMatchingPerson().getUuid();
////					// Reset the importResult option for case selection
////					resultOption = null;
////				}
////			}
//
////			if (ImportSimilarityResultOption.SKIP.equals(resultOption)) {
////				return ImportLineResult.SKIPPED;
////			} else {
////				final CaseImportLock caseSelectLock = new CaseImportLock();
////				synchronized (caseSelectLock) {
////					// Retrieve all similar cases from the database
////					CaseSimilarityCriteria criteria =
////						CaseSimilarityCriteria.forCase(importCase, selectedPersonUuid != null ? selectedPersonUuid : importPerson.getUuid());
////
////					List<CaseSelectionDto> similarCases = caseFacade.getSimilarCases(criteria);
////
////					if (similarCases.size() > 0) {
////						// Call the logic that allows the user to handle the similarity; once this has been done, the LOCK should be notified
////						// to allow the importer to resume
////						if (selectedPersonUuid != null) {
////							importPerson = personFacade.getPersonByUuid(selectedPersonUuid);
////						}
////
//////						handleCaseSimilarity(
//////							new CaseImportSimilarityInput(importCase, importPerson, similarCases),
//////							result -> consumer.onImportResult(result, caseSelectLock));
////
////						try {
////							if (!caseSelectLock.wasNotified) {
////								caseSelectLock.wait();
////							}
////						} catch (InterruptedException e) {
////							logger.error("InterruptedException when trying to perform LOCK.wait() in case import: " + e.getMessage());
////							throw e;
////						}
////
////						if (consumer.result != null) {
////							resultOption = consumer.result.getResultOption();
////						}
////
////						// If the user chose to override an existing case with the imported case, insert the new data into the existing case and associate the imported samples with it
////						if (resultOption == ImportSimilarityResultOption.OVERRIDE
////							&& consumer.result != null
////							&& consumer.result.getMatchingCase() != null) {
////							selectedCaseUuid = consumer.result.getMatchingCase().getUuid();
////							setValueUuid(values, entityProperties, selectedCaseUuid);
////						}
////					}
////				}
////			}
//
//			if (resultOption == ImportSimilarityResultOption.SKIP) {
//				consumer.result = null;
//				return ImportLineResult.SKIPPED;
//			} else if (resultOption == ImportSimilarityResultOption.PICK) {
//				consumer.result = null;
//				return ImportLineResult.DUPLICATE;
//			} else if (resultOption == ImportSimilarityResultOption.CANCEL) {
//				cancelImport();
//				return ImportLineResult.SKIPPED;
//			} else {
//				ImportLineResultDto<CaseImportEntities> saveResult;
//				boolean skipPersonValidation = selectedPersonUuid != null;
//				if (selectedPersonUuid != null || selectedCaseUuid != null) {
//					saveResult = caseImportFacade.updateCaseWithImportData(
//						selectedPersonUuid,
//						selectedCaseUuid,
//						values,
//						entityClasses,
//						entityPropertyPaths,
//						skipPersonValidation);
//				} else {
//					saveResult = caseImportFacade.saveImportedEntities(entities, skipPersonValidation);
//				}
//
//				if (saveResult.isError()) {
//					writeImportError(values, importResult.getMessage());
//					return ImportLineResult.ERROR;
//				}
//			}
//		}
//
//		return ImportLineResult.SUCCESS;
//	}

    private void setValueUuid(String[] values, String[] entityProperties, String uuid) {
        int uuidIndex = ArrayUtils.indexOf(entityProperties, CaseDataDto.UUID);
        if (uuidIndex >= 0) {
            values[uuidIndex] = uuid;
        }
    }

    /**
     * Presents a popup window to the user that allows them to deal with detected potentially duplicate cases.
     * By passing the desired result to the resultConsumer, the importer decided how to proceed with the import process.
     */
    protected void handleCaseSimilarity(CaseImportSimilarityInput input, Consumer<CaseImportSimilarityResult> resultConsumer) {
        currentUI.accessSynchronously(() -> {
            CasePickOrImportField pickOrImportField = new CasePickOrImportField(input.getCaze(), input.getPerson(), input.getSimilarCases());
            pickOrImportField.setWidth(1024, Unit.PIXELS);

            final CommitDiscardWrapperComponent<CasePickOrImportField> component = new CommitDiscardWrapperComponent<>(pickOrImportField);

            component.addCommitListener(() -> {
                CaseSelectionDto pickedCase = pickOrImportField.getValue();
                if (pickedCase != null) {
                    if (pickOrImportField.isOverrideCase()) {
                        resultConsumer.accept(new CaseImportSimilarityResult(null, pickedCase, ImportSimilarityResultOption.OVERRIDE));
                    } else {
                        resultConsumer.accept(new CaseImportSimilarityResult(null, pickedCase, ImportSimilarityResultOption.PICK));
                    }
                } else {
                    resultConsumer.accept(new CaseImportSimilarityResult(null, null, ImportSimilarityResultOption.CREATE));
                }
            });

            DiscardListener discardListener =
                    () -> resultConsumer.accept(new CaseImportSimilarityResult(null, null, ImportSimilarityResultOption.CANCEL));
            component.addDiscardListener(discardListener);
            component.getDiscardButton().setCaption(I18nProperties.getCaption(Captions.actionCancel));
            component.getCommitButton().setCaption(I18nProperties.getCaption(Captions.actionConfirm));
            component.getCommitButton().setEnabled(false);

            Button skipButton = ButtonHelper.createButton(Captions.actionSkip, e -> {
                component.removeDiscardListener(discardListener);
                component.discard();
                resultConsumer.accept(new CaseImportSimilarityResult(null, null, ImportSimilarityResultOption.SKIP));
            });
            component.getButtonsPanel().addComponentAsFirst(skipButton);

            pickOrImportField.setSelectionChangeCallback((commitAllowed) -> component.getCommitButton().setEnabled(commitAllowed));

            VaadinUiUtil.showModalPopupWindow(component, I18nProperties.getString(Strings.headingPickOrCreateCase));
        });
    }

    private class CaseImportConsumer {

        protected CaseImportSimilarityResult result;

        private void onImportResult(CaseImportSimilarityResult result, CaseImportLock LOCK) {
            this.result = result;
            synchronized (LOCK) {
                LOCK.notify();
                LOCK.wasNotified = true;
            }
        }
    }

    private class CaseImportLock {

        protected boolean wasNotified = false;
    }
}
