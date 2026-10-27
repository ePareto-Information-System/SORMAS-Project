package de.symeda.sormas.ui.configuration.infrastructure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseExportDetailedSampleDto;
import de.symeda.sormas.api.caze.samplesexport.DiseaseSpecificCaseExportSchema;
import de.symeda.sormas.api.caze.samplesexport.DiseaseSpecificCaseExportSchema.Block;
import de.symeda.sormas.api.caze.samplesexport.DiseaseSpecificCaseExportSchema.Column;
import de.symeda.sormas.api.caze.samplesexport.DiseaseSpecificExportFieldDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.ui.configuration.AbstractConfigurationView;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;

public class DiseaseSpecificExportFieldsView extends AbstractConfigurationView {

	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = ROOT_VIEW_NAME + "/diseaseSpecificExportFields";

	private final ComboBox<Disease> diseaseSelector;
	private final VerticalLayout fieldsLayout;
	private final Label activeFieldCount;
	private final Map<String, CheckBox> checkBoxes = new LinkedHashMap<>();
	private final FileDownloader configurationDownloader;
	private ByteArrayOutputStream importBuffer;

	public DiseaseSpecificExportFieldsView() {
		super(VIEW_NAME);

		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		content.setSpacing(true);
		content.setSizeFull();

		Label information = new Label(I18nProperties.getString(Strings.infoDiseaseSpecificExportFieldConfiguration));
		information.setWidth(100, Unit.PERCENTAGE);
		content.addComponent(information);

		diseaseSelector = new ComboBox<>(I18nProperties.getCaption(Captions.diseaseSpecificExportFieldsDisease));
		diseaseSelector.setId("diseaseSpecificExportDisease");
		diseaseSelector.setEmptySelectionAllowed(false);
		diseaseSelector.setWidth(400, Unit.PIXELS);
		List<Disease> diseases = new ArrayList<>(DiseaseSpecificCaseExportSchema.getSupportedDiseases());
		diseases.sort(Comparator.comparing(Disease::toShortString));
		diseaseSelector.setItems(diseases);
		diseaseSelector.setItemCaptionGenerator(Disease::toShortString);
		diseaseSelector.addValueChangeListener(event -> loadFields(event.getValue()));
		content.addComponent(diseaseSelector);

		HorizontalLayout selectionActions = new HorizontalLayout();
		selectionActions.setMargin(false);
		selectionActions.setSpacing(true);
		Button selectAll = ButtonHelper.createButton(Captions.actionSelectAll, event -> setAll(true), ValoTheme.BUTTON_LINK);
		selectAll.setId("diseaseSpecificExportSelectAll");
		Button deselectAll = ButtonHelper.createButton(Captions.actionDeselectAll, event -> setAll(false), ValoTheme.BUTTON_LINK);
		deselectAll.setId("diseaseSpecificExportDeselectAll");
		Button export = ButtonHelper.createIconButton(Captions.export, VaadinIcons.DOWNLOAD, null, ValoTheme.BUTTON_LINK);
		export.setId("diseaseSpecificExportConfigurationExport");
		StreamResource initialResource = new StreamResource(() -> new ByteArrayInputStream(new byte[0]), "disease-specific-export-fields.csv");
		configurationDownloader = new FileDownloader(initialResource);
		configurationDownloader.extend(export);
		Upload upload = new Upload("", (fileName, mimeType) -> {
			importBuffer = new ByteArrayOutputStream();
			return importBuffer;
		});
		upload.setId("diseaseSpecificExportConfigurationImport");
		upload.setButtonCaption(I18nProperties.getCaption(Captions.actionImport));
		upload.setAcceptMimeTypes("text/csv,application/csv,application/vnd.ms-excel,text/plain");
		upload.addSucceededListener(event -> importConfiguration());
		activeFieldCount = new Label();
		activeFieldCount.setId("diseaseSpecificExportActiveCount");
		selectionActions.addComponents(selectAll, deselectAll, export, upload, activeFieldCount);
		content.addComponent(selectionActions);

		fieldsLayout = new VerticalLayout();
		fieldsLayout.setMargin(true);
		fieldsLayout.setSpacing(false);
		Panel fieldsPanel = new Panel(fieldsLayout);
		fieldsPanel.setSizeFull();
		content.addComponent(fieldsPanel);
		content.setExpandRatio(fieldsPanel, 1);

		HorizontalLayout saveActions = new HorizontalLayout();
		saveActions.setMargin(false);
		saveActions.setSpacing(true);
		Button discard = ButtonHelper.createButton(Captions.actionDiscard, event -> loadFields(diseaseSelector.getValue()));
		discard.setId("diseaseSpecificExportDiscard");
		Button save = ButtonHelper.createButton(Captions.actionSave, event -> save(), ValoTheme.BUTTON_PRIMARY);
		save.setId("diseaseSpecificExportSave");
		saveActions.addComponents(discard, save);
		content.addComponent(saveActions);

		addComponent(content);
		setExpandRatio(content, 1);
		if (!diseases.isEmpty()) {
			diseaseSelector.setValue(diseases.get(0));
		}
	}

	private void loadFields(Disease disease) {
		fieldsLayout.removeAllComponents();
		checkBoxes.clear();
		if (disease == null) {
			updateActiveCount();
			return;
		}
		updateDownloadResource(disease);

		List<DiseaseSpecificExportFieldDto> fields = FacadeProvider.getDiseaseSpecificExportConfigurationFacade().getFields(disease);
		List<String> propertyPaths = fields.stream().map(DiseaseSpecificExportFieldDto::getPropertyPath).collect(Collectors.toList());
		List<Column> columns = DiseaseSpecificCaseExportSchema.build(
			disease,
			FacadeProvider.getConfigFacade().getCountryLocale(),
			propertyPaths);
		Map<String, Column> columnsByPath = columns.stream().collect(Collectors.toMap(Column::getPropertyPath, column -> column));
		Map<Block, List<DiseaseSpecificExportFieldDto>> fieldsByBlock = new LinkedHashMap<>();
		for (DiseaseSpecificExportFieldDto field : fields) {
			fieldsByBlock.computeIfAbsent(columnsByPath.get(field.getPropertyPath()).getBlock(), key -> new ArrayList<>()).add(field);
		}

		for (Map.Entry<Block, List<DiseaseSpecificExportFieldDto>> group : fieldsByBlock.entrySet()) {
			Label groupHeading = new Label(I18nProperties.getCaption("DiseaseSpecificExportBlock." + group.getKey().name()));
			groupHeading.addStyleName(CssStyles.H3);
			fieldsLayout.addComponent(groupHeading);
			for (DiseaseSpecificExportFieldDto field : group.getValue()) {
				Column column = columnsByPath.get(field.getPropertyPath());
				String caption = I18nProperties.findPrefixCaption(
					column.getProperty(),
					CaseExportDetailedSampleDto.I18N_PREFIX,
					column.getBlock().getI18nPrefix(),
					CaseDataDto.I18N_PREFIX);
				CheckBox checkBox = new CheckBox(caption, field.isActive());
				checkBox.setDescription(field.getPropertyPath());
				checkBox.setId("diseaseSpecificExportField-" + field.getPropertyPath());
				checkBox.addValueChangeListener(event -> updateActiveCount());
				checkBoxes.put(field.getPropertyPath(), checkBox);
				fieldsLayout.addComponent(checkBox);
			}
		}
		updateActiveCount();
	}

	private void updateDownloadResource(Disease disease) {
		StreamResource resource = new StreamResource(
			() -> new ByteArrayInputStream(FacadeProvider.getDiseaseSpecificExportConfigurationFacade().exportFields(disease)),
			"disease-specific-export-fields-" + disease.name() + ".csv");
		resource.setMIMEType("text/csv");
		configurationDownloader.setFileDownloadResource(resource);
	}

	private void importConfiguration() {
		if (importBuffer == null) {
			return;
		}
		try {
			FacadeProvider.getDiseaseSpecificExportConfigurationFacade().importFields(diseaseSelector.getValue(), importBuffer.toByteArray());
			loadFields(diseaseSelector.getValue());
			Notification.show(
				null,
				I18nProperties.getString(Strings.messageDiseaseSpecificExportFieldConfigurationImported),
				Notification.Type.TRAY_NOTIFICATION);
		} catch (RuntimeException e) {
			Notification.show(
				I18nProperties.getString(Strings.errorDiseaseSpecificExportFieldConfigurationImport),
				e.getMessage(),
				Notification.Type.ERROR_MESSAGE);
		} finally {
			importBuffer = null;
		}
	}

	private void setAll(boolean active) {
		checkBoxes.values().forEach(checkBox -> checkBox.setValue(active));
		updateActiveCount();
	}

	private void updateActiveCount() {
		long active = checkBoxes.values().stream().filter(CheckBox::getValue).count();
		activeFieldCount.setValue(String.format(
			I18nProperties.getString(Strings.diseaseSpecificExportActiveFieldCount),
			active,
			checkBoxes.size()));
	}

	private void save() {
		Set<String> activePaths = checkBoxes.entrySet()
			.stream()
			.filter(entry -> entry.getValue().getValue())
			.map(Map.Entry::getKey)
			.collect(Collectors.toCollection(LinkedHashSet::new));
		if (activePaths.isEmpty()) {
			Notification.show(
				null,
				I18nProperties.getString(Strings.validationDiseaseSpecificExportAtLeastOneField),
				Notification.Type.ERROR_MESSAGE);
			return;
		}

		try {
			FacadeProvider.getDiseaseSpecificExportConfigurationFacade().saveActiveFields(diseaseSelector.getValue(), activePaths);
			loadFields(diseaseSelector.getValue());
			Notification.show(
				null,
				I18nProperties.getString(Strings.messageDiseaseSpecificExportFieldConfigurationSaved),
				Notification.Type.TRAY_NOTIFICATION);
		} catch (RuntimeException e) {
			Notification.show(null, e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}
}
