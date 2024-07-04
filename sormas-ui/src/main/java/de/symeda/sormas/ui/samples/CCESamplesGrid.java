/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.ui.samples;

import static java.util.Objects.nonNull;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.DataProviderListener;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.renderers.DateRenderer;

import de.symeda.sormas.api.*;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.sample.*;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.utils.BooleanRenderer;
import de.symeda.sormas.ui.utils.DateFormatHelper;
import de.symeda.sormas.ui.utils.FieldAccessColumnStyleGenerator;
import de.symeda.sormas.ui.utils.FilteredGrid;
import de.symeda.sormas.ui.utils.ShowDetailsListener;
import de.symeda.sormas.ui.utils.UuidRenderer;
import de.symeda.sormas.ui.utils.ViewConfiguration;

@SuppressWarnings("serial")
public class CCESamplesGrid extends FilteredGrid<SampleIndexDto, SampleCriteria> {

    private static final String PATHOGEN_TEST_RESULT = Captions.Sample_pathogenTestResult;
    private static final String DISEASE_SHORT = Captions.columnDiseaseShort;
    private static final String LAST_PATHOGEN_TEST = Captions.columnLastPathogenTest;
    private static final String REQUESTED_SAMPLE_MATERIALS_COLUMN = "requestedSampleMaterialsColumn";

    private DataProviderListener<SampleIndexDto> dataProviderListener;

    @SuppressWarnings("unchecked")
    public CCESamplesGrid(SampleCriteria criteria) {
        super(SampleIndexDto.class);
        setSizeFull();

        ViewConfiguration viewConfiguration = ViewModelProviders.of(SamplesView.class).get(ViewConfiguration.class);
        setInEagerMode(viewConfiguration.isInEagerMode());

        if (isInEagerMode() && UserProvider.getCurrent().hasUserRight(UserRight.PERFORM_BULK_OPERATIONS_CASE_SAMPLES)) {
            setCriteria(criteria);
            setEagerDataProvider();
        } else {
            setLazyDataProvider();
            setCriteria(criteria);
        }

        addEditColumn(e -> ControllerProvider.getSampleController().navigateToData(e.getUuid()));

        Column<SampleIndexDto, String> diseaseShortColumn =
                addColumn(sample -> DiseaseHelper.toString(sample.getDisease(), sample.getDiseaseDetails()));
        diseaseShortColumn.setId(DISEASE_SHORT);
        diseaseShortColumn.setSortProperty(SampleIndexDto.DISEASE);

        Column<SampleIndexDto, String> pathogenTestResultColumn = addColumn(sample -> {
            if (sample.getPathogenTestResult() != null) {
                return sample.getPathogenTestResult().toString();
            } else if (sample.getSpecimenCondition() == SpecimenCondition.NOT_ADEQUATE) {
                return I18nProperties.getCaption(Captions.sampleSpecimenNotAdequate);
            } else {
                return "";
            }
        });
        pathogenTestResultColumn.setId(PATHOGEN_TEST_RESULT);
        pathogenTestResultColumn.setSortProperty(SampleIndexDto.PATHOGEN_TEST_RESULT);

        Column<SampleIndexDto, String> lastPathogenTestColumn = addColumn(sample -> {
            PathogenTestType type = sample.getTypeOfLastTest();
            Float cqValue = sample.getLastTestCqValue();
            String text = null;
            if (type != null) {
                text = type.toString();
                if (cqValue != null) {
                    text += " (" + cqValue + ")";
                }
            }
            return text;
        });

        Column<SampleIndexDto, String> requestedSampleMaterialsColumn = addColumn(sample -> sample.getFormattedRequestedSampleMaterials());
        requestedSampleMaterialsColumn.setId(REQUESTED_SAMPLE_MATERIALS_COLUMN);
        requestedSampleMaterialsColumn.setCaption("Type of Sample");

        lastPathogenTestColumn.setId(LAST_PATHOGEN_TEST);
        lastPathogenTestColumn.setSortable(false);

        if (isMonkeyPoxPresent(criteria)) {
            setColumns(
                    SampleIndexDto.UUID,
                    REQUESTED_SAMPLE_MATERIALS_COLUMN,
                    PATHOGEN_TEST_RESULT,
                    SampleIndexDto.RECEIVED,
                    SampleIndexDto.RECEIVED_DATE,
                    SampleIndexDto.LAB,
                    SampleIndexDto.SAMPLE_PURPOSE,
                    SampleIndexDto.PATHOGEN_TEST_COUNT);
        } else {
            setColumns(
                    SampleIndexDto.UUID,
                    SampleIndexDto.SAMPLE_MATERIAL,
                    PATHOGEN_TEST_RESULT,
                    SampleIndexDto.RECEIVED,
                    SampleIndexDto.RECEIVED_DATE,
                    SampleIndexDto.LAB,
                    SampleIndexDto.SAMPLE_PURPOSE,
                    SampleIndexDto.PATHOGEN_TEST_COUNT);
        }

        //((Column<SampleIndexDto, Date>) getColumn(SampleIndexDto.SHIPMENT_DATE)).setRenderer(new DateRenderer(DateFormatHelper.getDateFormat()));
        ((Column<SampleIndexDto, Date>) getColumn(SampleIndexDto.RECEIVED_DATE)).setRenderer(new DateRenderer(DateFormatHelper.getDateFormat()));
        //((Column<SampleIndexDto, Boolean>) getColumn(SampleIndexDto.SHIPPED)).setRenderer(new BooleanRenderer());
        ((Column<SampleIndexDto, String>) getColumn(SampleIndexDto.RECEIVED)).setRenderer(new BooleanRenderer());
        ((Column<SampleIndexDto, String>) getColumn(SampleIndexDto.LAB)).setMaximumWidth(200);
        //((Column<SampleIndexDto, String>) getColumn(SampleIndexDto.ADDITIONAL_TESTING_STATUS)).setSortable(false);
        ((Column<SampleIndexDto, Integer>) getColumn(SampleIndexDto.PATHOGEN_TEST_COUNT)).setSortable(false);

        ((Column<SampleIndexDto, String>) getColumn(SampleIndexDto.UUID)).setRenderer(new UuidRenderer());
        addItemClickListener(
                new ShowDetailsListener<>(SampleIndexDto.UUID, e -> ControllerProvider.getSampleController().navigateToData(e.getUuid())));

        if (nonNull(UserProvider.getCurrent()) && UserProvider.getCurrent().hasLaboratoryOrExternalLaboratoryJurisdictionLevel()) {
            removeColumn(SampleIndexDto.SHIPMENT_DATE);
        } else {
            removeColumn(SampleIndexDto.RECEIVED_DATE);
        }

//        if (!UserProvider.getCurrent().hasUserRight(UserRight.CASE_VIEW)) {
//            removeColumn(SampleIndexDto.ASSOCIATED_CASE);
//        }
//
//        if (!UserProvider.getCurrent().hasUserRight(UserRight.CONTACT_VIEW)) {
//            removeColumn(SampleIndexDto.ASSOCIATED_CONTACT);
//        }
//
//        if (!UserProvider.getCurrent().hasUserRight(UserRight.EVENT_VIEW)) {
//            removeColumn(SampleIndexDto.ASSOCIATED_EVENT_PARTICIPANT);
//        }



        for (Column<SampleIndexDto, ?> column : getColumns()) {
            column.setCaption(I18nProperties.getPrefixCaption(SampleIndexDto.I18N_PREFIX, column.getId(), column.getCaption()));

            column.setStyleGenerator(FieldAccessColumnStyleGenerator.getDefault(getBeanType(), column.getId()));

        }
    }

    private boolean isMonkeyPoxPresent(SampleCriteria criteria) {
        List<SampleDto> samples = FacadeProvider.getSampleFacade().getSamplesByCriteria(criteria);

        for (SampleDto sample : samples) {
            CaseDataDto caseDataDto = FacadeProvider.getCaseFacade().getCaseDataByUuid(sample.getAssociatedCase().getUuid());

            if (Disease.MONKEYPOX.equals(caseDataDto.getDisease())) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldShowEpidNumber() {
        ConfigFacade configFacade = FacadeProvider.getConfigFacade();
        return !configFacade.isConfiguredCountry(CountryHelper.COUNTRY_CODE_GERMANY)
                && !configFacade.isConfiguredCountry(CountryHelper.COUNTRY_CODE_SWITZERLAND);
    }

    public void reload() {
        if (getSelectionModel().isUserSelectionAllowed()) {
            deselectAll();
        }

        if (ViewModelProviders.of(SamplesView.class).get(ViewConfiguration.class).isInEagerMode()) {
            setEagerDataProvider();
        }

        getDataProvider().refreshAll();
    }

    public void setLazyDataProvider() {
        DataProvider<SampleIndexDto, SampleCriteria> dataProvider = DataProvider.fromFilteringCallbacks(
                query -> FacadeProvider.getSampleFacade()
                        .getIndexList(
                                query.getFilter().orElse(null),
                                query.getOffset(),
                                query.getLimit(),
                                query.getSortOrders()
                                        .stream()
                                        .map(sortOrder -> new SortProperty(sortOrder.getSorted(), sortOrder.getDirection() == SortDirection.ASCENDING))
                                        .collect(Collectors.toList()))
                        .stream(),
                query -> (int) FacadeProvider.getSampleFacade().count(query.getFilter().orElse(null)));
        setDataProvider(dataProvider);
        setSelectionMode(SelectionMode.NONE);
    }

    public void setEagerDataProvider() {
        ListDataProvider<SampleIndexDto> dataProvider =
                DataProvider.fromStream(FacadeProvider.getSampleFacade().getIndexList(getCriteria(), null, null, null).stream());
        setDataProvider(dataProvider);
        setSelectionMode(SelectionMode.MULTI);

        if (dataProviderListener != null) {
            dataProvider.addDataProviderListener(dataProviderListener);
        }
    }

    public void setDataProviderListener(DataProviderListener<SampleIndexDto> dataProviderListener) {
        this.dataProviderListener = dataProviderListener;
    }

}