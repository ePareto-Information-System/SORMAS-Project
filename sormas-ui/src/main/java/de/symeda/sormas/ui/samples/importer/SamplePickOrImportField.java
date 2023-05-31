package de.symeda.sormas.ui.samples.importer;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Label;
import de.symeda.sormas.api.DiseaseHelper;

import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.facility.FacilityHelper;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.person.PersonHelper;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.sample.SampleSelectionDto;
import de.symeda.sormas.ui.samples.SamplePickOrCreateField;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.DateFormatHelper;

import java.util.List;

@SuppressWarnings("serial")
public class SamplePickOrImportField extends SamplePickOrCreateField {

	private CheckBox overrideCheckBox;

	public SamplePickOrImportField(SampleDto newSample, PersonDto importedPerson, List<SampleDto> similarSamples) {
		super(newSample, importedPerson, similarSamples);
	}

	@Override
	protected void addInfoComponent() {

		HorizontalLayout infoLayout = new HorizontalLayout();
		infoLayout.setWidth(100, Unit.PERCENTAGE);
		infoLayout.setSpacing(true);
		Image icon = new Image(null, new ThemeResource("img/info-icon.png"));
		icon.setHeight(35, Unit.PIXELS);
		icon.setWidth(35, Unit.PIXELS);
		infoLayout.addComponent(icon);
		Label infoLabel = new Label(I18nProperties.getString(Strings.infoImportSimilarity));
		infoLayout.addComponent(infoLabel);
		infoLayout.setExpandRatio(infoLabel, 1);
		mainLayout.addComponent(infoLayout);
		CssStyles.style(infoLayout, CssStyles.VSPACE_3);

		// Imported Sample info
		VerticalLayout SampleInfoContainer = new VerticalLayout();
		SampleInfoContainer.setWidth(100, Unit.PERCENTAGE);
		CssStyles
			.style(SampleInfoContainer, CssStyles.BACKGROUND_ROUNDED_CORNERS, CssStyles.BACKGROUND_SUB_CRITERIA, CssStyles.VSPACE_3, "v-scrollable");

		Label newSampleLabel = new Label(I18nProperties.getString(Strings.headingImportedSampleInfo));
		CssStyles.style(newSampleLabel, CssStyles.LABEL_BOLD, CssStyles.VSPACE_4);
		SampleInfoContainer.addComponent(newSampleLabel);

		HorizontalLayout SampleInfoLayout = new HorizontalLayout();
		SampleInfoLayout.setSpacing(true);
		SampleInfoLayout.setSizeUndefined();
		{
			Label diseaseField = new Label();
//			diseaseField.setCaption(I18nProperties.getPrefixCaption(SampleDto.I18N_PREFIX, SampleDto.LAB));
//			diseaseField.setValue(DiseaseHelper.toString(newSample.getDisease(), newSample.getDiseaseDetails()));
//			diseaseField.setWidthUndefined();
//			SampleInfoLayout.addComponent(diseaseField);
//
//			Label reportDateField = new Label();
//			reportDateField.setCaption(I18nProperties.getPrefixCaption(SampleDto.I18N_PREFIX,SampleDto.CREATION_DATE));
//			reportDateField.setValue(DateFormatHelper.formatDate(newSample.getReportDate()));
//			reportDateField.setWidthUndefined();
//			SampleInfoLayout.addComponent(reportDateField);

//			Label responsibleRegionField = new Label();

//			responsibleRegionField.setCaption(I18nProperties.getPrefixCaption(SampleDataDto.I18N_PREFIX, SampleDto.RESPONSIBLE_REGION));
//			responsibleRegionField.setValue(newSample.getResponsibleRegion().toString());
//			responsibleRegionField.setWidthUndefined();
//			SampleInfoLayout.addComponent(responsibleRegionField);

//			Label responsibleDistrictField = new Label();
//			responsibleDistrictField.setCaption(I18nProperties.getPrefixCaption(SampleDto.I18N_PREFIX, SampleDto.));
//			responsibleDistrictField.setValue(newSample.getResponsibleDistrict().toString());
//			responsibleDistrictField.setWidthUndefined();
//			SampleInfoLayout.addComponent(responsibleDistrictField);

//			if (newSample.getRegion() != null) {
//				Label regionField = new Label();
//				regionField.setCaption(I18nProperties.getPrefixCaption(SampleDataDto.I18N_PREFIX, SampleDataDto.REGION));
//				regionField.setValue(newSample.getRegion().toString());
//				regionField.setWidthUndefined();
//				SampleInfoLayout.addComponent(regionField);
//			}
//
//			if (newSample.getDistrict() != null) {
//				Label districtField = new Label();
//				districtField.setCaption(I18nProperties.getPrefixCaption(SampleDataDto.I18N_PREFIX, SampleDataDto.DISTRICT));
//				districtField.setValue(newSample.getDistrict().toString());
//				districtField.setWidthUndefined();
//				SampleInfoLayout.addComponent(districtField);
//			}

//			Label facilityField = new Label();
//			facilityField.setCaption(I18nProperties.getPrefixCaption(SampleDto.I18N_PREFIX, SampleDto.));
//			facilityField.setValue(
//				FacilityHelper.buildFacilityString(
//					null,
//					newSample.getHealthFacility() != null ? newSample.getHealthFacility().toString() : "",
//					newSample.getHealthFacilityDetails()));
//			facilityField.setWidthUndefined();
//			SampleInfoLayout.addComponent(facilityField);

//			Label firstNameField = new Label();
//			firstNameField.setCaption(I18nProperties.getPrefixCaption(PersonDto.I18N_PREFIX, PersonDto.FIRST_NAME));
//			firstNameField.setValue(newPerson.getFirstName());
//			firstNameField.setWidthUndefined();
//			SampleInfoLayout.addComponent(firstNameField);
//
//			Label lastNameField = new Label();
//			lastNameField.setCaption(I18nProperties.getPrefixCaption(PersonDto.I18N_PREFIX, PersonDto.LAST_NAME));
//			lastNameField.setValue(newPerson.getLastName());
//			lastNameField.setWidthUndefined();
//			SampleInfoLayout.addComponent(lastNameField);
//
//			Label ageAndBirthDateField = new Label();
//			ageAndBirthDateField.setCaption(I18nProperties.getCaption(Captions.personAgeAndBirthdate));
//			ageAndBirthDateField.setValue(
//				PersonHelper.getAgeAndBirthdateString(
//					newPerson.getApproximateAge(),
//					newPerson.getApproximateAgeType(),
//					newPerson.getBirthdateDD(),
//					newPerson.getBirthdateMM(),
//					newPerson.getBirthdateYYYY()));
//			ageAndBirthDateField.setWidthUndefined();
//			SampleInfoLayout.addComponent(ageAndBirthDateField);
//
//			Label sexField = new Label();
//			sexField.setCaption(I18nProperties.getPrefixCaption(PersonDto.I18N_PREFIX, PersonDto.SEX));
//			sexField.setValue(newPerson.getSex() != null ? newPerson.getSex().toString() : "");
//			sexField.setWidthUndefined();
//			SampleInfoLayout.addComponent(sexField);
		}

		SampleInfoContainer.addComponent(SampleInfoLayout);
		mainLayout.addComponent(SampleInfoContainer);
	}

	@Override
	protected Component initContent() {

		addInfoComponent();
		addPickSampleComponent();

		overrideCheckBox = new CheckBox();
		overrideCheckBox.setCaption(I18nProperties.getCaption(Captions.SampleImportMergeSample));
		CssStyles.style(overrideCheckBox, CssStyles.VSPACE_3);
		mainLayout.addComponent(overrideCheckBox);

		addAndConfigureGrid();
		addCreateSampleComponent();

		pickSample.addValueChangeListener(e -> {
			if (e.getProperty().getValue() != null) {
				overrideCheckBox.setEnabled(true);
			}
		});

		createSample.addValueChangeListener(e -> {
			if (e.getProperty().getValue() != null) {
				overrideCheckBox.setEnabled(false);
			}
		});

		setInternalValue(super.getInternalValue());
		pickSample.setValue(PICK_Sample);

		return mainLayout;
	}

	public boolean isOverrideSample() {
		return overrideCheckBox.getValue();
	}
}
