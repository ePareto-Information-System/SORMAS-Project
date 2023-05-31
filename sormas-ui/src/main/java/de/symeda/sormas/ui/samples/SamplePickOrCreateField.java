package de.symeda.sormas.ui.samples;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.CustomField;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;

import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.facility.FacilityHelper;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.person.PersonHelper;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.sample.SampleSelectionDto;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.DateFormatHelper;

import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("serial")
public class SamplePickOrCreateField extends CustomField<SampleSelectionDto> {

	public static final String CREATE_Sample = "createSample";
	public static final String PICK_Sample = "pickSample";

	protected List<SampleDto> similarSamples;
	protected SampleSelectionGrid grid;
	protected OptionGroup pickSample;
	protected OptionGroup createSample;
	protected Consumer<Boolean> selectionChangeCallback;
	protected SampleDto newSample;
	protected PersonDto newPerson;

	protected VerticalLayout mainLayout;

	public SamplePickOrCreateField(SampleDto newSample, PersonDto newPerson, List<SampleDto> similarSamples) {

		this.similarSamples = similarSamples;
		this.newSample = newSample;
		this.newPerson = newPerson;

		mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setSizeUndefined();
		mainLayout.setWidth(100, Unit.PERCENTAGE);
	}

	protected void addInfoComponent() {

		HorizontalLayout infoLayout = new HorizontalLayout();
		infoLayout.setWidth(100, Unit.PERCENTAGE);
		infoLayout.setSpacing(true);
		Image icon = new Image(null, new ThemeResource("img/info-icon.png"));
		icon.setHeight(35, Unit.PIXELS);
		icon.setWidth(35, Unit.PIXELS);
		infoLayout.addComponent(icon);
		Label infoLabel = new Label(I18nProperties.getString(Strings.infoPickOrCreateSample));
		infoLabel.setContentMode(ContentMode.HTML);
		infoLayout.addComponent(infoLabel);
		infoLayout.setExpandRatio(infoLabel, 1);
		mainLayout.addComponent(infoLayout);
		CssStyles.style(infoLayout, CssStyles.VSPACE_3);
		// Imported Sample info
		VerticalLayout SampleInfoContainer = new VerticalLayout();
		SampleInfoContainer.setWidth(100, Unit.PERCENTAGE);
		CssStyles
			.style(SampleInfoContainer, CssStyles.BACKGROUND_ROUNDED_CORNERS, CssStyles.BACKGROUND_SUB_CRITERIA, CssStyles.VSPACE_3, "v-scrollable");

		Label newSampleLabel = new Label(I18nProperties.getString(Strings.infoPickOrCreateSampleNewSample));
		CssStyles.style(newSampleLabel, CssStyles.LABEL_BOLD, CssStyles.VSPACE_4);
		SampleInfoContainer.addComponent(newSampleLabel);

		HorizontalLayout SampleInfoLayout = new HorizontalLayout();
		SampleInfoLayout.setSpacing(true);
		SampleInfoLayout.setSizeUndefined();
		{
			Label firstNameField = new Label();
			firstNameField.setCaption(I18nProperties.getPrefixCaption(PersonDto.I18N_PREFIX, PersonDto.FIRST_NAME));
			firstNameField.setValue(newPerson.getFirstName());
			firstNameField.setWidthUndefined();
			SampleInfoLayout.addComponent(firstNameField);

			Label lastNameField = new Label();
			lastNameField.setCaption(I18nProperties.getPrefixCaption(PersonDto.I18N_PREFIX, PersonDto.LAST_NAME));
			lastNameField.setValue(newPerson.getLastName());
			lastNameField.setWidthUndefined();
			SampleInfoLayout.addComponent(lastNameField);

			Label ageAndBirthDateField = new Label();
			ageAndBirthDateField.setCaption(I18nProperties.getCaption(Captions.personAgeAndBirthdate));
			ageAndBirthDateField.setValue(
				PersonHelper.getAgeAndBirthdateString(
					newPerson.getApproximateAge(),
					newPerson.getApproximateAgeType(),
					newPerson.getBirthdateDD(),
					newPerson.getBirthdateMM(),
					newPerson.getBirthdateYYYY()));
			ageAndBirthDateField.setWidthUndefined();
			SampleInfoLayout.addComponent(ageAndBirthDateField);

//			Label responsibleDistrictField = new Label();
//			responsibleDistrictField.setCaption(I18nProperties.getPrefixCaption(SampleDto.I18N_PREFIX, SampleDto.RESPONSIBLE_DISTRICT));
//			responsibleDistrictField.setValue(newSample.getResponsibleDistrict() != null ? newSample.getResponsibleDistrict().toString() : "");
//			responsibleDistrictField.setWidthUndefined();
//			SampleInfoLayout.addComponent(responsibleDistrictField);
//
//			if (newSample.getDistrict() != null) {
//				Label districtField = new Label();
//				districtField.setCaption(I18nProperties.getPrefixCaption(SampleDto.I18N_PREFIX, SampleDto.DISTRICT));
//				districtField.setValue(newSample.getDistrict().toString());
//				districtField.setWidthUndefined();
//				SampleInfoLayout.addComponent(districtField);
//			}
//
//			Label facilityField = new Label();
//			facilityField.setCaption(I18nProperties.getPrefixCaption(SampleDto.I18N_PREFIX, SampleDto.HEALTH_FACILITY));
//			facilityField.setValue(
//				FacilityHelper.buildFacilityString(
//					null,
//					newSample.getHealthFacility() != null ? newSample.getHealthFacility().toString() : "",
//					newSample.getHealthFacilityDetails()));
//			facilityField.setWidthUndefined();
//			SampleInfoLayout.addComponent(facilityField);
//
//			Label reportDateField = new Label();
//			reportDateField.setCaption(I18nProperties.getPrefixCaption(SampleDto.I18N_PREFIX, SampleDto.REPORT_DATE));
//			reportDateField.setValue(DateFormatHelper.formatDate(newSample.getReportDate()));
//			reportDateField.setWidthUndefined();
//			SampleInfoLayout.addComponent(reportDateField);
//
//			Label sexField = new Label();
//			sexField.setCaption(I18nProperties.getPrefixCaption(PersonDto.I18N_PREFIX, PersonDto.SEX));
//			sexField.setValue(newPerson.getSex() != null ? newPerson.getSex().toString() : "");
//			sexField.setWidthUndefined();
//			SampleInfoLayout.addComponent(sexField);
//
//			Label classificationField = new Label();
//			classificationField.setCaption(I18nProperties.getPrefixCaption(SampleDto.I18N_PREFIX, SampleDto.Sample_CLASSIFICATION));
//			classificationField.setValue(newSample.getSampleClassification().toString());
//			classificationField.setWidthUndefined();
//			SampleInfoLayout.addComponent(classificationField);
//
//			Label outcomeField = new Label();
//			outcomeField.setCaption(I18nProperties.getPrefixCaption(SampleDto.I18N_PREFIX, SampleDto.OUTCOME));
//			outcomeField.setValue(newSample.getOutcome().toString());
//			outcomeField.setWidthUndefined();
//			SampleInfoLayout.addComponent(outcomeField);

		}

		SampleInfoContainer.addComponent(SampleInfoLayout);
		mainLayout.addComponent(SampleInfoContainer);
	}

	protected void addPickSampleComponent() {

		pickSample = new OptionGroup(null);
		pickSample.addItem(PICK_Sample);
		pickSample.setItemCaption(PICK_Sample, I18nProperties.getCaption(Captions.SamplePickSample));
		CssStyles.style(pickSample, CssStyles.VSPACE_NONE);
		pickSample.addValueChangeListener(e -> {
			if (e.getProperty().getValue() != null) {
				createSample.setValue(null);
				grid.setEnabled(true);
				if (similarSamples.size() == 1) {
					pickSample.setValue(PICK_Sample);
					grid.select(similarSamples.get(0));
				}
				if (selectionChangeCallback != null) {
					selectionChangeCallback.accept(grid.getSelectedRow() != null);
				}
			}
		});
		mainLayout.addComponent(pickSample);
	}

	protected void addAndConfigureGrid() {

		initGrid();

		grid.setEnabled(false);
		grid.addSelectionListener(e -> {
			if (e.getSelected().size() > 0) {
				createSample.setValue(null);
			}
		});
		mainLayout.addComponent(grid);

		grid.addSelectionListener(e -> {
			if (selectionChangeCallback != null) {
				selectionChangeCallback.accept(!e.getSelected().isEmpty());
			}
		});
	}

	protected void addCreateSampleComponent() {

		createSample = new OptionGroup(null);
		createSample.addItem(CREATE_Sample);
		createSample.setItemCaption(CREATE_Sample, I18nProperties.getCaption(Captions.SampleCreateSample));
		createSample.addValueChangeListener(e -> {
			if (e.getProperty().getValue() != null) {
				pickSample.setValue(null);
				grid.select(null);
				grid.setEnabled(false);
				if (selectionChangeCallback != null) {
					selectionChangeCallback.accept(true);
				}
			}
		});
		mainLayout.addComponent(createSample);
	}

	@Override
	protected Component initContent() {

		addInfoComponent();
		addPickSampleComponent();
		addAndConfigureGrid();
		addCreateSampleComponent();

		grid.setEnabled(false);

		return mainLayout;
	}

	private void initGrid() {
		if (grid == null) {
			grid = new SampleSelectionGrid(similarSamples);
		}
	}

	@Override
	protected void setInternalValue(SampleSelectionDto newValue) {

		super.setInternalValue(newValue);

		if (pickSample != null) {
			pickSample.setValue(PICK_Sample);
		}

		if (newValue != null) {
			grid.select(newValue);
		}
	}

	@Override
	protected SampleSelectionDto getInternalValue() {

		if (grid != null) {
			SampleSelectionDto value = (SampleSelectionDto) grid.getSelectedRow();
			return value;
		}

		return super.getInternalValue();
	}

	@Override
	public Class<? extends SampleSelectionDto> getType() {
		return SampleSelectionDto.class;
	}

	public void setSelectionChangeCallback(Consumer<Boolean> callback) {
		this.selectionChangeCallback = callback;
	}
}
