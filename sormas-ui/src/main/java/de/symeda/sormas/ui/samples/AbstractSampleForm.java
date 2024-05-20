package de.symeda.sormas.ui.samples;

import static de.symeda.sormas.ui.utils.CssStyles.HSPACE_RIGHT_4;
import static de.symeda.sormas.ui.utils.CssStyles.VSPACE_3;
import static de.symeda.sormas.ui.utils.CssStyles.VSPACE_4;
import static de.symeda.sormas.ui.utils.CssStyles.VSPACE_NONE;
import static de.symeda.sormas.ui.utils.CssStyles.VSPACE_TOP_3;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;
import static de.symeda.sormas.ui.utils.LayoutUtil.locCss;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Validator;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.contact.ContactReferenceDto;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.event.EventParticipantReferenceDto;
import de.symeda.sormas.api.event.EventReferenceDto;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.Descriptions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.infrastructure.facility.FacilityDto;
import de.symeda.sormas.api.infrastructure.facility.FacilityReferenceDto;
import de.symeda.sormas.api.sample.AdditionalTestType;
import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.PathogenTestType;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.sample.SampleMaterial;
import de.symeda.sormas.api.sample.SamplePurpose;
import de.symeda.sormas.api.sample.SampleReferenceDto;
import de.symeda.sormas.api.sample.SamplingReason;
import de.symeda.sormas.api.sample.SpecimenCondition;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.DateComparisonValidator;
import de.symeda.sormas.ui.utils.DateFormatHelper;
import de.symeda.sormas.ui.utils.DateTimeField;
import de.symeda.sormas.ui.utils.FieldHelper;
import de.symeda.sormas.ui.utils.NullableOptionGroup;

public abstract class AbstractSampleForm extends AbstractEditForm<SampleDto> {

	private static final long serialVersionUID = -2323128076462668517L;

//	protected static final String REPORT_INFORMATION_LOC = "reportInformationLoc";
	protected static final String PATHOGEN_TESTING_INFO_LOC = "pathogenTestingInfoLoc";
	protected static final String ADDITIONAL_TESTING_INFO_LOC = "additionalTestingInfoLoc";
	protected static final String PATHOGEN_TESTING_READ_HEADLINE_LOC = "pathogenTestingReadHeadlineLoc";
	protected static final String ADDITIONAL_TESTING_READ_HEADLINE_LOC = "additionalTestingReadHeadlineLoc";
	protected static final String REQUESTED_PATHOGEN_TESTS_READ_LOC = "requestedPathogenTestsReadLoc";
	protected static final String REQUESTED_ADDITIONAL_TESTS_READ_LOC = "requestedAdditionalTestsReadLoc";
	protected static final String REPORT_INFO_LABEL_LOC = "reportInfoLabelLoc";
	protected static final String REFERRED_FROM_BUTTON_LOC = "referredFromButtonLoc";
	private ComboBox lab;

	//@formatter:off
    protected static final String SAMPLE_COMMON_HTML_LAYOUT =
            fluidRowLocs(SampleDto.UUID, REPORT_INFO_LABEL_LOC) +
                    fluidRowLocs(SampleDto.SAMPLE_PURPOSE) +
                    fluidRowLocs(SampleDto.SAMPLE_DATE_TIME, SampleDto.SAMPLE_MATERIAL) +
                    fluidRowLocs("", SampleDto.SAMPLE_MATERIAL_TEXT) +
                    fluidRowLocs(SampleDto.SAMPLING_REASON, SampleDto.SAMPLING_REASON_DETAILS) +
                    fluidRowLocs(SampleDto.SAMPLE_SOURCE, REFERRED_FROM_BUTTON_LOC) +
                     fluidRowLocs(SampleDto.FIELD_SAMPLE_ID, SampleDto.FOR_RETEST) +
                    fluidRowLocs(SampleDto.LAB, SampleDto.LAB_DETAILS) +

					locCss(VSPACE_TOP_3, SampleDto.PATHOGEN_TESTING_REQUESTED) +
					loc(PATHOGEN_TESTING_READ_HEADLINE_LOC) +
					loc(PATHOGEN_TESTING_INFO_LOC) +
					loc(SampleDto.REQUESTED_PATHOGEN_TESTS) +
					loc(SampleDto.REQUESTED_OTHER_PATHOGEN_TESTS) +
					loc(REQUESTED_PATHOGEN_TESTS_READ_LOC) +

					locCss(VSPACE_TOP_3, SampleDto.ADDITIONAL_TESTING_REQUESTED) +
					loc(ADDITIONAL_TESTING_READ_HEADLINE_LOC) +
					loc(ADDITIONAL_TESTING_INFO_LOC) +
					loc(SampleDto.REQUESTED_ADDITIONAL_TESTS) +
					loc(SampleDto.REQUESTED_OTHER_ADDITIONAL_TESTS) +
					loc(REQUESTED_ADDITIONAL_TESTS_READ_LOC) +

					locCss(VSPACE_TOP_3, SampleDto.SHIPPED) +
					fluidRowLocs(SampleDto.SHIPMENT_DATE, SampleDto.SHIPMENT_DETAILS) +

					locCss(VSPACE_TOP_3, SampleDto.RECEIVED) +
					fluidRowLocs(SampleDto.RECEIVED_DATE, SampleDto.LAB_SAMPLE_ID) +

					fluidRowLocs(SampleDto.SPECIMEN_CONDITION, SampleDto.NO_TEST_POSSIBLE_REASON) +
					fluidRowLocs(SampleDto.COMMENT) +
					fluidRowLocs(SampleDto.PATHOGEN_TEST_RESULT) +
					fluidRowLocs(CaseDataDto.DELETION_REASON) +
					fluidRowLocs(CaseDataDto.OTHER_DELETION_REASON);
	//@formatter:on

	protected AbstractSampleForm(Class<SampleDto> type, String propertyI18nPrefix, Disease disease, UiFieldAccessCheckers fieldAccessCheckers) {
		super(
				type,
				propertyI18nPrefix,
				true,
				FieldVisibilityCheckers.withDisease(disease).andWithCountry(FacadeProvider.getConfigFacade().getCountryLocale()),
				fieldAccessCheckers);
	}

	protected void addCommonFields() {

		final NullableOptionGroup samplePurpose = addField(SampleDto.SAMPLE_PURPOSE, NullableOptionGroup.class);
		addField(SampleDto.UUID).setReadOnly(true);
		addField(SampleDto.REPORTING_USER).setReadOnly(true);
		samplePurpose.addValueChangeListener(e -> updateRequestedTestFields());
		addField(SampleDto.LAB_SAMPLE_ID, TextField.class);
		final DateTimeField sampleDateField = addField(SampleDto.SAMPLE_DATE_TIME, DateTimeField.class);
		sampleDateField.setInvalidCommitted(false);
		addField(SampleDto.SAMPLE_MATERIAL, ComboBox.class);
		addField(SampleDto.SAMPLE_MATERIAL_TEXT, TextField.class);
		addField(SampleDto.SAMPLE_SOURCE, ComboBox.class);
		addField(SampleDto.FIELD_SAMPLE_ID, TextField.class);
		addField(SampleDto.FOR_RETEST, OptionGroup.class).setRequired(true);
		addDateField(SampleDto.SHIPMENT_DATE, DateField.class, 7);
		addField(SampleDto.SHIPMENT_DETAILS, TextField.class);
		addField(SampleDto.RECEIVED_DATE, DateField.class);
		//final ComboBox lab = addInfrastructureField(SampleDto.LAB);
		lab = addInfrastructureField(SampleDto.LAB);
		//lab.addItems(FacadeProvider.getFacilityFacade().getAllActiveLaboratories(true));
		final TextField labDetails = addField(SampleDto.LAB_DETAILS, TextField.class);
		labDetails.setVisible(false);
		lab.addValueChangeListener(event -> updateLabDetailsVisibility(labDetails, event));

		addField(SampleDto.SPECIMEN_CONDITION, ComboBox.class);
		addField(SampleDto.NO_TEST_POSSIBLE_REASON, TextField.class);
		TextArea comment = addField(SampleDto.COMMENT, TextArea.class);
		comment.setRows(4);
		comment.setDescription(
				I18nProperties.getPrefixDescription(SampleDto.I18N_PREFIX, SampleDto.COMMENT, "") + "\n"
						+ I18nProperties.getDescription(Descriptions.descGdpr));
		addField(SampleDto.SHIPPED, CheckBox.class);
		addField(SampleDto.RECEIVED, CheckBox.class);

		ComboBox testResultField = addField(SampleDto.PATHOGEN_TEST_RESULT, ComboBox.class);
		testResultField.removeItem(PathogenTestResultType.NOT_DONE);

		addFields(SampleDto.SAMPLING_REASON, SampleDto.SAMPLING_REASON_DETAILS);
		FieldHelper.setVisibleWhen(
				getFieldGroup(),
				SampleDto.SAMPLING_REASON_DETAILS,
				SampleDto.SAMPLING_REASON,
				Collections.singletonList(SamplingReason.OTHER_REASON),
				true);

		addField(SampleDto.DELETION_REASON);
		addField(SampleDto.OTHER_DELETION_REASON, TextArea.class).setRows(3);
		setVisible(false, SampleDto.DELETION_REASON, SampleDto.OTHER_DELETION_REASON);

	}

	protected void defaultValueChangeListener() {

		final NullableOptionGroup samplePurposeField = (NullableOptionGroup) getField(SampleDto.SAMPLE_PURPOSE);
		final Field<?> receivedField = getField(SampleDto.RECEIVED);
		final Field<?> shippedField = getField(SampleDto.SHIPPED);

		samplePurposeField.setRequired(true);

		Disease disease = null;
		final CaseReferenceDto associatedCase = getValue().getAssociatedCase();
		final ContactReferenceDto associatedContact = getValue().getAssociatedContact();
		final EventParticipantReferenceDto associatedEventParticipant = getValue().getAssociatedEventParticipant();

		if (associatedCase != null && UserProvider.getCurrent().hasAllUserRights(UserRight.CASE_VIEW)) {
			disease = getDiseaseFromCase(associatedCase.getUuid());
		} else if (associatedContact != null && UserProvider.getCurrent().hasAllUserRights(UserRight.CONTACT_VIEW)) {
			disease = getDiseaseFromContact(associatedContact.getUuid());
		} else if (associatedEventParticipant != null && UserProvider.getCurrent().hasAllUserRights(UserRight.EVENT_VIEW)) {
			EventReferenceDto eventReferenceDto = FacadeProvider.getEventParticipantFacade().getEventParticipantByUuid(associatedEventParticipant.getUuid()).getEvent();
			if (eventReferenceDto != null) {
				disease = getDiseaseFromEvent(eventReferenceDto.getUuid());
			}
		}

		FieldHelper.setVisibleWhen(
				getFieldGroup(),
				Arrays.asList(SampleDto.RECEIVED_DATE, SampleDto.LAB_SAMPLE_ID, SampleDto.SPECIMEN_CONDITION),
				SampleDto.RECEIVED,
				Arrays.asList(true),
				true);
		FieldHelper.setEnabledWhen(
				getFieldGroup(),
				receivedField,
				Arrays.asList(true),
				Arrays.asList(SampleDto.RECEIVED_DATE, SampleDto.LAB_SAMPLE_ID, SampleDto.SPECIMEN_CONDITION),
				true);

		if (disease != Disease.NEW_INFLUENZA) {
			getField(SampleDto.SAMPLE_SOURCE).setVisible(false);
		}

		if(disease != null) {
			lab.addItems(FacadeProvider.getFacilityFacade().getAllActiveFacilityByDisease(disease.getName()));
			FacilityReferenceDto labValue = (FacilityReferenceDto) lab.getValue();
			if(labValue != null) {
				lab.removeItem(labValue);
				lab.addItem(labValue);
				lab.setValue(labValue);
			}
		}

		UserReferenceDto reportingUser = getValue().getReportingUser();
		if (UserProvider.getCurrent().hasUserRight(UserRight.SAMPLE_EDIT_NOT_OWNED)
				|| (reportingUser != null && UserProvider.getCurrent().getUuid().equals(reportingUser.getUuid()))) {
			FieldHelper.setVisibleWhen(
					getFieldGroup(),
					Arrays.asList(SampleDto.SHIPMENT_DATE, SampleDto.SHIPMENT_DETAILS),
					SampleDto.SHIPPED,
					Arrays.asList(true),
					true);
			FieldHelper.setEnabledWhen(
					getFieldGroup(),
					shippedField,
					Arrays.asList(true),
					Arrays.asList(SampleDto.SHIPMENT_DATE, SampleDto.SHIPMENT_DETAILS),
					true);
			FieldHelper.setRequiredWhen(
					getFieldGroup(),
					SampleDto.SAMPLE_PURPOSE,
					Arrays.asList(SampleDto.LAB),
					Arrays.asList(SamplePurpose.EXTERNAL, null));
			setRequired(true, SampleDto.SAMPLE_DATE_TIME, SampleDto.SAMPLE_MATERIAL);
		} else {
			getField(SampleDto.SAMPLE_DATE_TIME).setEnabled(false);
			getField(SampleDto.SAMPLE_MATERIAL).setEnabled(false);
			getField(SampleDto.SAMPLE_MATERIAL_TEXT).setEnabled(false);
			getField(SampleDto.LAB).setEnabled(false);
			shippedField.setEnabled(false);
			getField(SampleDto.SHIPMENT_DATE).setEnabled(false);
			getField(SampleDto.SHIPMENT_DETAILS).setEnabled(false);
			getField(SampleDto.SAMPLE_SOURCE).setEnabled(false);
		}

		// Initialize referral and report information
		// VerticalLayout reportInfoLayout = new VerticalLayout();
		StringBuilder reportInfoText = new StringBuilder().append(I18nProperties.getString(Strings.reportedOn))
				.append(" ")
				.append(DateFormatHelper.formatLocalDateTime(getValue().getReportDateTime()));
		if (reportingUser != null) {
			reportInfoText.append(" ").append(I18nProperties.getString(Strings.by)).append(" ").append(reportingUser.toString());
		}
		Label reportInfoLabel = new Label(reportInfoText.toString());
		reportInfoLabel.setEnabled(false);
		// reportInfoLayout.addComponent(reportInfoLabel);
		getContent().addComponent(reportInfoLabel);

		SampleReferenceDto referredFromRef = FacadeProvider.getSampleFacade().getReferredFrom(getValue().getUuid());
		if (referredFromRef != null) {
			SampleDto referredFrom = FacadeProvider.getSampleFacade().getSampleByUuid(referredFromRef.getUuid());
			FacilityReferenceDto referredFromLab = referredFrom.getLab();
			String referredButtonCaption = referredFromLab == null
				? I18nProperties.getCaption(Captions.sampleReferredFromInternal) + " ("
					+ DateFormatHelper.formatLocalDateTime(referredFrom.getSampleDateTime()) + ")"
				: I18nProperties.getCaption(Captions.sampleReferredFrom) + " " + referredFromLab.toString();
			Button referredButton = ButtonHelper.createButton(
				"referredFrom",
				referredButtonCaption,
				event -> ControllerProvider.getSampleController().navigateToData(referredFrom.getUuid()),
				ValoTheme.BUTTON_LINK,
				VSPACE_NONE);

			getContent().addComponent(referredButton);
		}

		// getContent().addComponent(reportInfoLayout, REPORT_INFORMATION_LOC);

	}

	protected void updateLabDetailsVisibility(TextField labDetails, Property.ValueChangeEvent event) {
		if (event.getProperty().getValue() != null
				&& ((FacilityReferenceDto) event.getProperty().getValue()).getUuid().equals(FacilityDto.OTHER_FACILITY_UUID)) {
			labDetails.setVisible(true);
			labDetails.setRequired(true);
		} else {
			labDetails.setVisible(false);
			labDetails.setRequired(false);
			labDetails.clear();
		}
	}

	protected void addValidators() {
		// Validators
		final DateTimeField sampleDateField = (DateTimeField) getField(SampleDto.SAMPLE_DATE_TIME);
		final DateField shipmentDate = (DateField) getField(SampleDto.SHIPMENT_DATE);
		final DateField receivedDate = (DateField) getField(SampleDto.RECEIVED_DATE);

		sampleDateField.addValidator(
				new DateComparisonValidator(
						sampleDateField,
						shipmentDate,
						true,
						false,
						I18nProperties.getValidationError(Validations.beforeDate, sampleDateField.getCaption(), shipmentDate.getCaption())));
		sampleDateField.addValidator(
				new DateComparisonValidator(
						sampleDateField,
						receivedDate,
						true,
						false,
						I18nProperties.getValidationError(Validations.beforeDate, sampleDateField.getCaption(), receivedDate.getCaption())));
		shipmentDate.addValidator(
				new DateComparisonValidator(
						shipmentDate,
						sampleDateField,
						false,
						false,
						I18nProperties.getValidationError(Validations.afterDate, shipmentDate.getCaption(), sampleDateField.getCaption())));
		shipmentDate.addValidator(
				new DateComparisonValidator(
						shipmentDate,
						receivedDate,
						true,
						false,
						I18nProperties.getValidationError(Validations.beforeDate, shipmentDate.getCaption(), receivedDate.getCaption())));
		receivedDate.addValidator(
				new DateComparisonValidator(
						receivedDate,
						sampleDateField,
						false,
						false,
						I18nProperties.getValidationError(Validations.afterDate, receivedDate.getCaption(), sampleDateField.getCaption())));
		receivedDate.addValidator(
				new DateComparisonValidator(
						receivedDate,
						shipmentDate,
						false,
						false,
						I18nProperties.getValidationError(Validations.afterDate, receivedDate.getCaption(), shipmentDate.getCaption())));

		addValidators(SampleDto.FIELD_SAMPLE_ID, new FieldSampleIdValidator());

		List<AbstractField<Date>> validatedFields = Arrays.asList(sampleDateField, shipmentDate, receivedDate);
		validatedFields.forEach(field -> field.addValueChangeListener(r -> {
			validatedFields.forEach(otherField -> {
				otherField.setValidationVisible(!otherField.isValid());
			});
		}));
	}

	protected void setVisibilities() {

		FieldHelper
				.setVisibleWhen(getFieldGroup(), SampleDto.SAMPLE_MATERIAL_TEXT, SampleDto.SAMPLE_MATERIAL, Arrays.asList(SampleMaterial.OTHER), true);
		FieldHelper.setVisibleWhen(
				getFieldGroup(),
				SampleDto.NO_TEST_POSSIBLE_REASON,
				SampleDto.SPECIMEN_CONDITION,
				Arrays.asList(SpecimenCondition.NOT_ADEQUATE),
				true);
		FieldHelper.setRequiredWhen(
				getFieldGroup(),
				SampleDto.SAMPLE_MATERIAL,
				Arrays.asList(SampleDto.SAMPLE_MATERIAL_TEXT),
				Arrays.asList(SampleMaterial.OTHER));
		FieldHelper.setRequiredWhen(
				getFieldGroup(),
				SampleDto.SPECIMEN_CONDITION,
				Arrays.asList(SampleDto.NO_TEST_POSSIBLE_REASON),
				Arrays.asList(SpecimenCondition.NOT_ADEQUATE));
		FieldHelper.setVisibleWhen(
				getFieldGroup(),
				Arrays.asList(SampleDto.LAB, SampleDto.SHIPPED, SampleDto.RECEIVED),
				SampleDto.SAMPLE_PURPOSE,
				Arrays.asList(SamplePurpose.EXTERNAL, null),
				true);
	}

	protected void initializeRequestedTestFields() {

		// Information texts for users that can edit the requested tests
		Label requestedPathogenInfoLabel = new Label(I18nProperties.getString(Strings.infoSamplePathogenTesting));
		getContent().addComponent(requestedPathogenInfoLabel, PATHOGEN_TESTING_INFO_LOC);
		Label requestedAdditionalInfoLabel = new Label(I18nProperties.getString(Strings.infoSampleAdditionalTesting));
		getContent().addComponent(requestedAdditionalInfoLabel, ADDITIONAL_TESTING_INFO_LOC);

		// Yes/No fields for requesting pathogen/additional tests
		CheckBox pathogenTestingRequestedField = addField(SampleDto.PATHOGEN_TESTING_REQUESTED, CheckBox.class);
		pathogenTestingRequestedField.setWidthUndefined();
		pathogenTestingRequestedField.addValueChangeListener(e -> updateRequestedTestFields());

		CheckBox additionalTestingRequestedField = addField(SampleDto.ADDITIONAL_TESTING_REQUESTED, CheckBox.class);
		additionalTestingRequestedField.setWidthUndefined();
		additionalTestingRequestedField.addValueChangeListener(e -> updateRequestedTestFields());

		// CheckBox groups to select the requested pathogen/additional tests
		OptionGroup requestedPathogenTestsField = addField(SampleDto.REQUESTED_PATHOGEN_TESTS, OptionGroup.class);
		CssStyles.style(requestedPathogenTestsField, CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
		requestedPathogenTestsField.setMultiSelect(true);
		requestedPathogenTestsField.addItems(
			Arrays.stream(PathogenTestType.values())
				.filter(c -> fieldVisibilityCheckers.isVisible(PathogenTestType.class, c.name()))
				.collect(Collectors.toList()));
		requestedPathogenTestsField.removeItem(PathogenTestType.OTHER);
		requestedPathogenTestsField.setCaption(null);

		OptionGroup requestedAdditionalTestsField = addField(SampleDto.REQUESTED_ADDITIONAL_TESTS, OptionGroup.class);
		CssStyles.style(requestedAdditionalTestsField, CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
		requestedAdditionalTestsField.setMultiSelect(true);
		requestedAdditionalTestsField.addItems((Object[]) AdditionalTestType.values());
		requestedAdditionalTestsField.setCaption(null);

		// Text fields to type in other tests
		TextField requestedOtherPathogenTests = addField(SampleDto.REQUESTED_OTHER_PATHOGEN_TESTS, TextField.class);
		TextField requestedOtherAdditionalTests = addField(SampleDto.REQUESTED_OTHER_ADDITIONAL_TESTS, TextField.class);

		// header for read view
		Label pathogenTestsHeading = new Label(I18nProperties.getString(Strings.headingRequestedPathogenTests));
		CssStyles.style(pathogenTestsHeading, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_4);
		getContent().addComponent(pathogenTestsHeading, PATHOGEN_TESTING_READ_HEADLINE_LOC);

		Label additionalTestsHeading = new Label(I18nProperties.getString(Strings.headingRequestedAdditionalTests));
		CssStyles.style(additionalTestsHeading, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_4);
		getContent().addComponent(additionalTestsHeading, ADDITIONAL_TESTING_READ_HEADLINE_LOC);

		updateRequestedTestFields();
	}

	private void updateRequestedTestFields() {

		boolean showRequestFields = getField(SampleDto.SAMPLE_PURPOSE).getValue() != SamplePurpose.INTERNAL;
		UserReferenceDto reportingUser = getValue() != null ? getValue().getReportingUser() : null;
		boolean canEditRequest = showRequestFields
				&& (UserProvider.getCurrent().hasUserRight(UserRight.SAMPLE_EDIT_NOT_OWNED)
				|| reportingUser != null && UserProvider.getCurrent().getUuid().equals(reportingUser.getUuid()));
		boolean canOnlyReadRequests = !canEditRequest && showRequestFields;
		boolean canUseAdditionalTests = UserProvider.getCurrent().hasUserRight(UserRight.ADDITIONAL_TEST_VIEW)
				&& FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.ADDITIONAL_TESTS);

		Field<?> pathogenTestingField = getField(SampleDto.PATHOGEN_TESTING_REQUESTED);
		pathogenTestingField.setVisible(canEditRequest);
		if (!showRequestFields) {
			pathogenTestingField.clear();
		}

		Field<?> additionalTestingField = getField(SampleDto.ADDITIONAL_TESTING_REQUESTED);
		additionalTestingField.setVisible(canEditRequest && canUseAdditionalTests);
		if (!showRequestFields) {
			additionalTestingField.clear();
		}

		boolean pathogenTestsRequested = Boolean.TRUE.equals(pathogenTestingField.getValue());
		setVisible(pathogenTestsRequested, SampleDto.REQUESTED_PATHOGEN_TESTS, SampleDto.REQUESTED_OTHER_PATHOGEN_TESTS);
		getContent().getComponent(PATHOGEN_TESTING_INFO_LOC).setVisible(pathogenTestsRequested);

		boolean additionalTestsRequested = Boolean.TRUE.equals(additionalTestingField.getValue());
		setVisible(additionalTestsRequested, SampleDto.REQUESTED_ADDITIONAL_TESTS, SampleDto.REQUESTED_OTHER_ADDITIONAL_TESTS);
		getContent().getComponent(ADDITIONAL_TESTING_INFO_LOC).setVisible(additionalTestsRequested);

		getContent().getComponent(PATHOGEN_TESTING_READ_HEADLINE_LOC).setVisible(canOnlyReadRequests);
		getContent().getComponent(ADDITIONAL_TESTING_READ_HEADLINE_LOC).setVisible(canOnlyReadRequests && canUseAdditionalTests);

		handleDisease(Disease.YELLOW_FEVER, "National Public Health Reference Laboratory");
		handleDisease(Disease.AHF, "NMIMR");
		handleDisease(Disease.DENGUE, "NMIMR");
		handleDisease(Disease.AFP, "NMIMR");
		handleDisease(Disease.NEW_INFLUENZA, "NMIMR");
		handleDiseaseField(Disease.NEW_INFLUENZA, Disease.CSM, Disease.SARI, Disease.FOODBORNE_ILLNESS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS);

		if (getValue() != null && canOnlyReadRequests) {
			CssLayout requestedPathogenTestsLayout = new CssLayout();
			CssStyles.style(requestedPathogenTestsLayout, VSPACE_3);
			for (PathogenTestType testType : getValue().getRequestedPathogenTests()) {
				Label testLabel = new Label(testType.toString());
				testLabel.setWidthUndefined();
				CssStyles.style(testLabel, CssStyles.LABEL_ROUNDED_CORNERS, CssStyles.LABEL_BACKGROUND_FOCUS_LIGHT, VSPACE_4, HSPACE_RIGHT_4);
				requestedPathogenTestsLayout.addComponent(testLabel);
			}
			getContent().addComponent(requestedPathogenTestsLayout, REQUESTED_PATHOGEN_TESTS_READ_LOC);
		} else {
			getContent().removeComponent(REQUESTED_PATHOGEN_TESTS_READ_LOC);
		}

		if (getValue() != null && canOnlyReadRequests && canUseAdditionalTests) {
			CssLayout requestedAdditionalTestsLayout = new CssLayout();
			CssStyles.style(requestedAdditionalTestsLayout, VSPACE_3);
			for (AdditionalTestType testType : getValue().getRequestedAdditionalTests()) {
				Label testLabel = new Label(testType.toString());
				testLabel.setWidthUndefined();
				CssStyles.style(testLabel, CssStyles.LABEL_ROUNDED_CORNERS, CssStyles.LABEL_BACKGROUND_FOCUS_LIGHT, VSPACE_4, HSPACE_RIGHT_4);
				requestedAdditionalTestsLayout.addComponent(testLabel);
			}
			getContent().addComponent(requestedAdditionalTestsLayout, REQUESTED_ADDITIONAL_TESTS_READ_LOC);
		} else {
			getContent().removeComponent(REQUESTED_ADDITIONAL_TESTS_READ_LOC);
		}
	}

	class FieldSampleIdValidator implements Validator {

		private static final long serialVersionUID = 1L;

		@Override
		public void validate(Object value) throws InvalidValueException {
			if (value == null || value.equals(""))
				return;

			if (!ControllerProvider.getSampleController().isFieldSampleIdUnique(getValue().getUuid(), (String) value))
				throw new InvalidValueException(I18nProperties.getString(Strings.messageFieldSampleIdExist));
		}
	private void disableField(String field) {
		setVisible(false, field);
	}

	private void handleCSM() {

		OptionGroup csfSampleCollected = addField(SampleDto.CSF_SAMPLE_COLLECTED, OptionGroup.class);
		ComboBox csfReason = addField(SampleDto.CSF_REASON, ComboBox.class);
		NullableOptionGroup appearanceOfCsf = addField(SampleDto.APPEARANCE_OF_CSF, NullableOptionGroup.class);
		addField(SampleDto.INOCULATION_TIME_TRANSPORT_MEDIA, DateField.class);
		OptionGroup sampleSentToLab = addField(SampleDto.SAMPLE_SENT_TO_LAB, OptionGroup.class);
		TextField reasonNotSent = addField(SampleDto.REASON_NOT_SENT_TO_LAB, TextField.class);
		DateField dateSampleSentToLab = addField(SampleDto.DATE_SAMPLE_SENT_TO_LAB, DateField.class);
		NullableOptionGroup sampleContainerUsed = addField(SampleDto.SAMPLE_CONTAINER_USED, NullableOptionGroup.class);
		OptionGroup rdtPerformed = addField(SampleDto.RDT_PERFORMED, OptionGroup.class);
		addField(SampleDto.RDT_RESULTS, TextField.class);
		addField(SampleDto.DISTRICT_NOTIFICATION_DATE, DateField.class);
		addField(SampleDto.NAME_OF_PERSON, TextField.class);
		addField(SampleDto.TEL_NUMBER, TextField.class);

		addField(SampleDto.DATE_FORM_SENT_TO_REGION, DateField.class);
		addField(SampleDto.DATE_FORM_RECEIVED_AT_REGION, DateField.class);
		addField(SampleDto.DATE_FORM_SENT_TO_NATIONAL, DateField.class);
		addField(SampleDto.DATE_FORM_RECEIVED_AT_NATIONAL, DateField.class);

		csfReason.setVisible(false);
		reasonNotSent.setVisible(false);
		dateSampleSentToLab.setVisible(false);
		sampleDateField.setVisible(false);
		suspectedDisease.setVisible(false);
		labLocation.setVisible(false);
		dateSpecimenSentToLab.setVisible(false);
		dateLabReceivedSpecimen.setVisible(false);
		dateResultsSentToClinician.setVisible(false);
		sampleMaterialComboBox.setVisible(false);


		FieldHelper
				.setVisibleWhen(csfSampleCollected, Arrays.asList(sampleDateField), Arrays.asList(YesNo.YES), true);
		FieldHelper
				.setVisibleWhen(sampleSentToLab, Arrays.asList(reasonNotSent), Arrays.asList(YesNo.NO), true);
		FieldHelper
				.setVisibleWhen(sampleSentToLab, Arrays.asList(dateSampleSentToLab), Arrays.asList(YesNo.YES), true);
		FieldHelper
				.setVisibleWhen(csfSampleCollected, Arrays.asList(csfReason), Arrays.asList(YesNo.NO), true);

		setPropertiesVisibility();

		Label districtLaboratory = new Label(I18nProperties.getString(Strings.headingDistrictLaboratory));
		CssStyles.style(districtLaboratory, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_4);
		getContent().addComponent(districtLaboratory, DISTRICT_LABORATORY_HEADLINE_LOC);
		districtLaboratory.setVisible(false);

		Label regionalLaboratory = new Label(I18nProperties.getString(Strings.headingRegionalLaboratory));
		CssStyles.style(regionalLaboratory, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_4);
		getContent().addComponent(regionalLaboratory, REGIONAL_LABORATORY_HEADLINE_LOC);
		regionalLaboratory.setVisible(false);

		Label referenceLaboratory = new Label(I18nProperties.getString(Strings.headingReferenceLaboratory));
		CssStyles.style(referenceLaboratory, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_4);
		getContent().addComponent(referenceLaboratory, REFERENCE_LABORATORY_HEADLINE_LOC);
		referenceLaboratory.setVisible(false);

		laboratorySampleDateReceived = addField(SampleDto.LABORATORY_SAMPLE_DATE_RECEIVED, DateTimeField.class);
		laboratorySampleDateReceived.setInvalidCommitted(false);

		laboratorySampleContainerOther.setVisible(false);
	}

	private void handleAFP() {

		Label stoolSpecimenCollection = new Label(I18nProperties.getString(Strings.headingStoolSpecimenCollection));
		CssStyles.style(stoolSpecimenCollection, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_4);
		getContent().addComponent(stoolSpecimenCollection, STOOL_SPECIMEN_COLLECTION_HEADLINE_LOC);
		stoolSpecimenCollection.setVisible(false);

		Label stoolSpecimenResults = new Label(I18nProperties.getString(Strings.headingStoolSpecimenResults));
		CssStyles.style(stoolSpecimenResults, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_4);
		getContent().addComponent(stoolSpecimenResults, STOOL_SPECIMEN_RESULTS_HEADLINE_LOC);
		stoolSpecimenResults.setVisible(false);

		Label followUpExamination = new Label(I18nProperties.getString(Strings.headingFollowUpExamination));
		CssStyles.style(followUpExamination, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_4);
		getContent().addComponent(followUpExamination, FOLLOW_UP_EXAMINATION_HEADLINE_LOC);
		followUpExamination.setVisible(false);

		stoolSpecimenCollection.setVisible(true);
		DateField dateFirstSpecimen = addField(SampleDto.DATE_FIRST_SPECIMEN, DateField.class);
		DateField dateSecondSpecimen = addField(SampleDto.DATE_SECOND_SPECIMEN, DateField.class);
		DateField dateSpecimenSentNationalLevel = addField(SampleDto.DATE_SPECIMEN_SENT_NATIONAL_LEVEL, DateField.class);
		DateField dateSpecimenReceivedNationalLevel = addField(SampleDto.DATE_SPECIMEN_RECEIVED_NATIONAL_LEVEL, DateField.class);
		DateField dateSpecimenSentInter = addField(SampleDto.DATE_SPECIMEN_SENT_INTERCOUNTY_NATLAB, DateField.class);
		DateField dateSpecimenReceivedInter = addField(SampleDto.DATE_SPECIMEN_RECEIVED_INTERCOUNTY_NATLAB, DateField.class);

		stoolSpecimenResults.setVisible(true);
		OptionGroup statusSpecimenReceptionAtLab = addField(SampleDto.STATUS_SPECIMEN_RECEPTION_AT_LAB, OptionGroup.class);
		DateField dateCombinedCellCultureResults = addField(SampleDto.DATE_COMBINED_CELL_CULTURE_RESULTS, DateField.class);
		OptionGroup w1 = addField(SampleDto.W1, OptionGroup.class);
		OptionGroup w2 = addField(SampleDto.W2, OptionGroup.class);
		OptionGroup w3 = addField(SampleDto.W3, OptionGroup.class);
		OptionGroup s1 = addField(SampleDto.SL1, OptionGroup.class);
		OptionGroup s2 = addField(SampleDto.SL2, OptionGroup.class);
		OptionGroup s3 = addField(SampleDto.SL3, OptionGroup.class);
		NullableOptionGroup discordant = addField(SampleDto.DISCORDANT, NullableOptionGroup.class);

		followUpExamination.setVisible(true);
		DateField dateFollowUpExam = addField(SampleDto.DATE_FOLLOWUP_EXAM, DateField.class);
		NullableOptionGroup residualAnalysis = addField(SampleDto.RESIDUAL_ANALYSIS, NullableOptionGroup.class);
		residualAnalysis.removeItem(InjectionSite.RIGHT_FOREARM);
		residualAnalysis.removeItem(InjectionSite.RIGHT_BUTTOCKS);
		residualAnalysis.removeItem(InjectionSite.RIGHT_THIGH);
		residualAnalysis.removeItem(InjectionSite.LEFT_FOREARM);
		residualAnalysis.removeItem(InjectionSite.LEFT_BUTTOCKS);
		residualAnalysis.removeItem(InjectionSite.LEFT_THIGH);
		ComboBox resultExam = addField(SampleDto.RESULT_EXAM, ComboBox.class);

		DateField dateSentNationalRegLab = addField(SampleDto.DATE_SENT_NATIONAL_REG_LAB, DateField.class);
		DateField dateDifferentiationSentEpi = addField(SampleDto.DATE_DIFFERENTIATION_SENT_EPI, DateField.class);
		DateField dateDifferentiationReceivedEpi = addField(SampleDto.DATE_DIFFERENTIATION_RECEIVED_EPI, DateField.class);
		DateField dateIsolateSentSequencing = addField(SampleDto.DATE_ISOLATE_SENT_SEQUENCING, DateField.class);
		DateField dateSeqResultsSentProgram = addField(SampleDto.DATE_SEQ_RESULTS_SENT_PROGRAM, DateField.class);
		NullableOptionGroup finalLabResults = addField(SampleDto.FINAL_LAB_RESULTS, NullableOptionGroup.class);
		NullableOptionGroup immunocompromisedStatusSuspected = addField(SampleDto.IMMUNOCOMPROMISED_STATUS_SUSPECTED, NullableOptionGroup.class);
		ComboBox afpFinalClassification = addField(SampleDto.AFP_FINAL_CLASSIFICATION, ComboBox.class);

		setRequired(false, SampleDto.SAMPLE_PURPOSE);

		setVisible(false, SampleDto.SAMPLE_PURPOSE, SampleDto.REQUESTED_SAMPLE_MATERIALS, SampleDto.FIELD_SAMPLE_ID, SampleDto.SAMPLE_MATERIAL_TEXT, SampleDto.SAMPLE_MATERIAL_REQUESTED, SampleDto.COMMENT, SampleDto.SAMPLE_TESTS, SampleDto.DISEASE, SampleDto.SAMPLING_REASON, SampleDto.IPSAMPLESENT, SampleDto.SAMPLE_MATERIAL, SampleDto.PATHOGEN_TEST_RESULT, SampleDto.SAMPLE_SOURCE);

		suspectedDisease.setVisible(false);
		labLocation.setVisible(false);
		dateLabReceivedSpecimen.setVisible(false);
		laboratorySampleCondition.setVisible(false);
		dateFormSentToDistrict.setVisible(false);
		dateFormReceivedAtDistrict.setVisible(false);
		dateResultsSentToClinician.setVisible(false);
		dateSpecimenSentToLab.setVisible(false);

	}

	private void handleAHF(){
		setVisible(false,SampleDto.SAMPLE_SOURCE,
				SampleDto.SAMPLE_PURPOSE,SampleDto.SAMPLING_REASON, SampleDto.FIELD_SAMPLE_ID, SampleDto.SAMPLE_MATERIAL_REQUESTED, SampleDto.COMMENT, SampleDto.PATHOGEN_TESTING_REQUESTED, SampleDto.REQUESTED_SAMPLE_MATERIALS);

		suspectedDisease.setVisible(false);
		labLocation.setVisible(false);
		dateLabReceivedSpecimen.setVisible(false);
		laboratorySampleCondition.setVisible(false);
		dateFormSentToDistrict.setVisible(false);
		dateFormReceivedAtDistrict.setVisible(false);
		dateResultsSentToClinician.setVisible(false);
		dateSpecimenSentToLab.setVisible(false);

		diseaseField.setVisible(true);
		List<SampleMaterial> validValues = Arrays.asList(SampleMaterial.WHOLE_BLOOD, SampleMaterial.PLASMA_SERUM);
		FieldHelper.updateEnumData(sampleMaterialComboBox, validValues);

	}

	private void handleYellowFever(){

		addSampleDispatchFields();

		setRequired(false, SampleDto.SAMPLE_DATE_TIME, SampleDto.SAMPLE_MATERIAL);

		sampleMaterialComboBox.setVisible(false);
		suspectedDisease.setVisible(false);
		labLocation.setVisible(false);
		dateLabReceivedSpecimen.setVisible(false);
		laboratorySampleCondition.setVisible(false);
		dateFormSentToDistrict.setVisible(false);
		dateFormReceivedAtDistrict.setVisible(false);
		dateResultsSentToClinician.setVisible(false);
		dateSpecimenSentToLab.setVisible(false);
		setVisible(false, SampleDto.SAMPLE_SOURCE, SampleDto.SAMPLING_REASON, SampleDto.SAMPLE_MATERIAL_TEXT);

		setVisible(true, SampleDto.IPSAMPLESENT, SampleDto.IPSAMPLERESULTS, SampleDto.REQUESTED_SAMPLE_MATERIALS);
		testResultField.setVisible(true);

	}
	private void handleNewInfluenza(){

		setPropertiesVisibility();
		/*List<SampleMaterial> validValues = Arrays.asList(SampleMaterial.OROPHARYNGEAL_SWAB, SampleMaterial.NP_SWAB, SampleMaterial.SERUM, SampleMaterial.PLASMA);
		FieldHelper.updateEnumData(sampleMaterialComboBox, validValues);

		OptionGroup positiveviral = addField(SampleDto.POSITIVE_VIRAL_CULTURE, OptionGroup.class);
		OptionGroup positiveRealTime = addField(SampleDto.POSITIVE_REAL_TIME, OptionGroup.class);
		OptionGroup foldRise = addField(SampleDto.FOUR_FOLD_RISE, OptionGroup.class);
		ComboBox Virus = addField(SampleDto.INFLUENZA_VIRUS, ComboBox.class);
		TextField otherVirus = addField(SampleDto.OTHER_INFLUENZA_VIRUS, TextField.class);
		otherVirus.setVisible(false);
		TextField treatment = addField(SampleDto.TREATMENT, TextField.class);
		TextField stateTreatment = addField(SampleDto.STATE_TREATMENT_ADMINISTERED, TextField.class);


		ComboBox classificationBox = new ComboBox("Final Classification");

		for (CaseClassification validValuesForInfluenza : CaseClassification.CASE_CLASSIFY_INFLUENZA) {
			classificationBox.addItem(validValuesForInfluenza);
		}
		ComboBox finalClassification = addField(SampleDto.LABORATORY_FINAL_CLASSIFICATION, classificationBox);*/

		sampleMaterialComboBox.setVisible(false);
		suspectedDisease.setVisible(false);
		labLocation.setVisible(false);
		dateLabReceivedSpecimen.setVisible(false);
		laboratorySampleCondition.setVisible(false);
		dateFormSentToDistrict.setVisible(false);
		dateFormReceivedAtDistrict.setVisible(false);
		dateResultsSentToClinician.setVisible(false);
		dateSpecimenSentToLab.setVisible(false);
		pathogenTestingRequestedField.setVisible(false);
		setVisible(true, SampleDto.REQUESTED_SAMPLE_MATERIALS);
	}
	
	private Disease getDiseaseFromCase(String caseUuid) {
		CaseDataDto caseDataDto = FacadeProvider.getCaseFacade().getCaseDataByUuid(caseUuid);
		if (caseDataDto != null) {
			return caseDataDto.getDisease();
		}
		return null;
	}

	private void setVisibleAndCheckLab(String labName, String...fieldsToHide) {
		setVisible(false, fieldsToHide);
		List<FacilityReferenceDto> allActiveLaboratories = FacadeProvider.getFacilityFacade().getAllActiveLaboratories(false);
		FacilityReferenceDto facilityLab = findLabByName(allActiveLaboratories, labName);

		if (lab != null) {
			if (facilityLab != null) {
				lab.addItems(allActiveLaboratories);
				lab.setValue(facilityLab);
//				lab.setReadOnly(true);
				labDetails.setVisible(false);
				labDetails.setRequired(false);
			} else {
				System.out.println("Please add " + labName + " to Facility Configuration");
			}
		} else {
			System.out.println("Lab dropdown is null. Please contact system admin.");
		}
	}


	private Disease getDiseaseFromContact(String contactUuid) {
		ContactDto contactDto = FacadeProvider.getContactFacade().getByUuid(contactUuid);
		if (contactDto != null) {
			return contactDto.getDisease();
		}
		return null;
	}

	private Disease getDiseaseFromEvent(String eventUuid) {
		EventDto eventDto = FacadeProvider.getEventFacade().getByUuid(eventUuid);
		if (eventDto != null) {
			return eventDto.getDisease();
		}
		return null;
	}
}
