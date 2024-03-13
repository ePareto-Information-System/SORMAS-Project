package de.symeda.sormas.ui.samples;

import static de.symeda.sormas.ui.utils.CssStyles.*;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;
import static de.symeda.sormas.ui.utils.LayoutUtil.locCss;

import java.util.*;
import java.util.stream.Collectors;

import com.vaadin.ui.*;
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
import de.symeda.sormas.api.caze.*;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.contact.ContactReferenceDto;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.event.EventParticipantReferenceDto;
import de.symeda.sormas.api.event.EventReferenceDto;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.*;
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
import de.symeda.sormas.api.utils.*;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.api.utils.pseudonymization.SampleDispatchMode;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.*;
import de.symeda.sormas.ui.utils.DateFormatHelper;
import de.symeda.sormas.ui.utils.DateTimeField;
import java.util.Date;

public abstract class AbstractSampleForm extends AbstractEditForm<SampleDto> {

	private static final long serialVersionUID = -2323128076462668517L;

	//	protected static final String REPORT_INFORMATION_LOC = "reportInformationLoc";
	protected static final String PATHOGEN_TESTING_INFO_LOC = "pathogenTestingInfoLoc";
	protected static final String SAMPLE_MATERIAL_INFO_LOC = "sampleMaterialInfoLoc";
	protected static final String ADDITIONAL_TESTING_INFO_LOC = "additionalTestingInfoLoc";
	protected static final String PATHOGEN_TESTING_READ_HEADLINE_LOC = "pathogenTestingReadHeadlineLoc";
	protected static final String SAMPLE_MATERIAL_READ_HEADLINE_LOC = "sampleMaterialReadHeadlineLoc";
	protected static final String ADDITIONAL_TESTING_READ_HEADLINE_LOC = "additionalTestingReadHeadlineLoc";
	protected static final String REQUESTED_PATHOGEN_TESTS_READ_LOC = "requestedPathogenTestsReadLoc";
	protected static final String DISTRICT_LABORATORY_HEADLINE_LOC = "districtLaboratoryLoc";
	protected static final String REGIONAL_LABORATORY_HEADLINE_LOC = "regionalLaboratoryLoc";
	protected static final String REFERENCE_LABORATORY_HEADLINE_LOC = "referenceLaboratoryLoc";
	protected static final String STOOL_SPECIMEN_COLLECTION_HEADLINE_LOC = "stoolSpecimenCollectionLoc";
	protected static final String STOOL_SPECIMEN_RESULTS_HEADLINE_LOC = "stoolSpecimenResultsLoc";
	protected static final String FINAL_LAB_RESULTS_HEADLINE_LOC = "finalLabResultsLoc";
	protected static final String FOLLOW_UP_EXAMINATION_HEADLINE_LOC = "followUpExaminationLoc";
	protected static final String LABORATORY_ANTIBIOGRAM_HEADLINE_LOC = "laboratoryAntibiogramHeadlineloc";
	protected static final String LABORATORY_PCR_HEADLINE_LOC = "laboratoryPcrHeadlineloc";
	protected static final String REQUESTED_ADDITIONAL_TESTS_READ_LOC = "requestedAdditionalTestsReadLoc";
	protected static final String REPORT_INFO_LABEL_LOC = "reportInfoLabelLoc";
	protected static final String REFERRED_FROM_BUTTON_LOC = "referredFromButtonLoc";
	private ComboBox lab;
	private Disease disease;
	private ComboBox diseaseField;
	public ComboBox sampleMaterialComboBox;
	public ComboBox sampleSource;
	private ComboBox lab;
	private TextField labDetails;
	private TextField labLocation;
	protected SampleDispatchMode sampleDispatchMode = SampleDispatchMode.REGIONAL_COLDROOM;
	private DateTimeField sampleDateField;
	private DateTimeField laboratorySampleDateReceived;
	OptionGroup sampleTestsField;
	OptionGroup requestedPathogenTestsField;
	OptionGroup requestedSampleMaterialsField;
	private ComboBox testResultField;
	private ComboBox suspectedDisease;
	private DateField dateLabReceivedSpecimen;
	private OptionGroup laboratorySampleCondition;
	private TextField laboratoryFinalResults;
	private DateField dateFormSentToDistrict;
	private DateField dateFormReceivedAtDistrict;
	private DateField dateResultsSentToClinician;

	//@formatter:off
    protected static final String SAMPLE_COMMON_HTML_LAYOUT =
			fluidRowLocs(SampleDto.SAMPLE_PURPOSE) +
            fluidRowLocs(SampleDto.UUID, REPORT_INFO_LABEL_LOC) +
                    fluidRowLocs(SampleDto.CSF_SAMPLE_COLLECTED) +
                    fluidRowLocs(SampleDto.CSF_REASON) +
					fluidRowLocs(SampleDto.SAMPLE_DATE_TIME) +
                    fluidRowLocs(SampleDto.APPEARANCE_OF_CSF) +
                    fluidRowLocs(6,SampleDto.INOCULATION_TIME_TRANSPORT_MEDIA) +
                    fluidRowLocs(SampleDto.SAMPLE_SENT_TO_LAB, SampleDto.REASON_NOT_SENT_TO_LAB) +
                    fluidRowLocs(6,SampleDto.DATE_SAMPLE_SENT_TO_LAB) +
                    fluidRowLocs(SampleDto.LAB, SampleDto.LAB_DETAILS, SampleDto.LAB_LOCATION) +
                    fluidRowLocs(SampleDto.SAMPLE_CONTAINER_USED) +
                    fluidRowLocs(SampleDto.RDT_PERFORMED, SampleDto.RDT_RESULTS) +
                    fluidRowLocs(SampleDto.DISTRICT_NOTIFICATION_DATE, SampleDto.NAME_OF_PERSON, SampleDto.TEL_NUMBER) +
                    fluidRowLocs(SampleDto.DATE_FORM_SENT_TO_DISTRICT, SampleDto.DATE_FORM_RECEIVED_AT_DISTRICT) +
                    fluidRowLocs(SampleDto.DATE_FORM_SENT_TO_REGION, SampleDto.DATE_FORM_RECEIVED_AT_REGION) +
                    fluidRowLocs(SampleDto.DATE_FORM_SENT_TO_NATIONAL, SampleDto.DATE_FORM_RECEIVED_AT_NATIONAL) +

					fluidRowLocs(SampleDto.SUSPECTED_DISEASE, SampleDto.DATE_LAB_RECEIVED_SPECIMEN) +
					fluidRowLocs(6,SampleDto.DATE_RESULTS_RECEIVED_SENT_TO_CLINICIAN) +
                    fluidRowLocs(6,SampleDto.SAMPLE_MATERIAL) +
					fluidRowLocs(SampleDto.FIELD_SAMPLE_ID, REFERRED_FROM_BUTTON_LOC) +
					fluidRowLocs(6, SampleDto.DISEASE) +
					fluidRowLocs(SampleDto.SAMPLE_TESTS) +
					fluidRowLocs(SampleDto.SAMPLE_DISPATCH_MODE) +
					fluidRowLocs(6,SampleDto.SAMPLE_DISPATCH_DATE) +
					fluidRowLocs("", SampleDto.SAMPLE_MATERIAL_TEXT) +
					fluidRowLocs(SampleDto.SAMPLING_REASON, SampleDto.SAMPLING_REASON_DETAILS) +
					fluidRowLocs(SampleDto.SAMPLE_SOURCE, "") +
					//fluidRowLocs(SampleDto.LAB, SampleDto.LAB_DETAILS) +

					fluidRowLocs(6,SampleDto.SAMPLE_MATERIAL) +
					fluidRowLocs(SampleDto.CSF_SAMPLE_COLLECTED) +
					fluidRowLocs(6, SampleDto.CSF_REASON) +
					fluidRowLocs(SampleDto.SAMPLE_DATE_TIME) +
					fluidRowLocs(SampleDto.APPEARANCE_OF_CSF) +
					fluidRowLocs(6,SampleDto.INOCULATION_TIME_TRANSPORT_MEDIA) +
					fluidRowLocs(SampleDto.SAMPLE_SENT_TO_LAB, SampleDto.REASON_NOT_SENT_TO_LAB) +
					fluidRowLocs(6,SampleDto.DATE_SAMPLE_SENT_TO_LAB) +
					fluidRowLocs(SampleDto.LAB, SampleDto.LAB_DETAILS) +
					fluidRowLocs(SampleDto.SAMPLE_CONTAINER_USED) +
					fluidRowLocs(SampleDto.RDT_PERFORMED, SampleDto.RDT_RESULTS) +
					fluidRowLocs(SampleDto.DISTRICT_NOTIFICATION_DATE, SampleDto.NAME_OF_PERSON, SampleDto.TEL_NUMBER) +
					fluidRowLocs(SampleDto.DATE_FORM_SENT_TO_DISTRICT, SampleDto.DATE_FORM_RECEIVED_AT_DISTRICT) +
					fluidRowLocs(SampleDto.DATE_FORM_SENT_TO_REGION, SampleDto.DATE_FORM_RECEIVED_AT_REGION) +
					fluidRowLocs(SampleDto.DATE_FORM_SENT_TO_NATIONAL, SampleDto.DATE_FORM_RECEIVED_AT_NATIONAL) +

					fluidRowLocs(6,SampleDto.LABORATORY_TYPE) +
					fluidRowLocs(DISTRICT_LABORATORY_HEADLINE_LOC) +
					fluidRowLocs(REGIONAL_LABORATORY_HEADLINE_LOC) +
					fluidRowLocs(REFERENCE_LABORATORY_HEADLINE_LOC) +
					locCss(VSPACE_TOP_3, SampleDto.LABORATORY_NAME) +
					fluidRowLocs(SampleDto.LABORATORY_SAMPLE_DATE_RECEIVED, SampleDto.LABORATORY_NUMBER) +
					fluidRowLocs(SampleDto.LABORATORY_SAMPLE_CONTAINER_RECEIVED, SampleDto.LABORATORY_SAMPLE_CONTAINER_OTHER) +
					fluidRowLocs(SampleDto.LABORATORY_SAMPLE_CONDITION) +
					fluidRowLocs(SampleDto.LABORATORY_APPEARANCE_OF_CSF, SampleDto.LABORATORY_TEST_PERFORMED, SampleDto.LABORATORY_TEST_PERFORMED_OTHER) +
					fluidRowLocs(SampleDto.LABORATORY_CYTOLOGY, SampleDto.LABORATORY_GRAM, SampleDto.LABORATORY_GRAM_OTHER) +
					fluidRowLocs(SampleDto.LABORATORY_RDT_PERFORMED, SampleDto.LABORATORY_RDT_RESULTS) +
					fluidRowLocs(6,SampleDto.LABORATORY_LATEX) +
					fluidRowLocs(SampleDto.LABORATORY_CULTURE, SampleDto.LABORATORY_CULTURE_OTHER) +
					fluidRowLocs(SampleDto.LABORATORY_OTHER_TESTS, SampleDto.LABORATORY_OTHER_TESTS_RESULTS) +

					loc(LABORATORY_ANTIBIOGRAM_HEADLINE_LOC) +
					fluidRowLocs(SampleDto.LABORATORY_CEFTRIAXONE, SampleDto.LABORATORY_PENICILLIN_G) +
					fluidRowLocs(SampleDto.LABORATORY_AMOXYCILLIN, SampleDto.LABORATORY_OXACILLIN) +
					fluidRowLocs(SampleDto.LABORATORY_ANTIBIOGRAM_OTHER) +

					loc(LABORATORY_PCR_HEADLINE_LOC) +
					fluidRowLocs(SampleDto.LABORATORY_DATE_PCR_PERFORMED, SampleDto.LABORATORY_PCR_TYPE) +
					fluidRowLocs(SampleDto.LABORATORY_PCR_OPTIONS) +
					fluidRowLocs(SampleDto.LABORATORY_SEROTYPE, SampleDto.LABORATORY_SEROTYPE_TYPE, SampleDto.LABORATORY_SEROTYPE_RESULTS) +
					fluidRowLocs(6,SampleDto.LABORATORY_FINAL_RESULTS) +
					fluidRowLocs(SampleDto.LABORATORY_OBSERVATIONS) +
					fluidRowLocs(6, SampleDto.LABORATORY_DATE_RESULTS_SENT_HEALTH_FACILITY) +
					fluidRowLocs(6, SampleDto.LABORATORY_DATE_RESULTS_SENT_DSD) +
					fluidRowLocs(SampleDto.LABORATORY_FINAL_CLASSIFICATION) +


					fluidRowLocs(SampleDto.IPSAMPLESENT) + fluidRowLocs(SampleDto.IPSAMPLERESULTS, "")+
					loc(SAMPLE_MATERIAL_READ_HEADLINE_LOC) +
					loc(SampleDto.REQUESTED_SAMPLE_MATERIALS) +

					//INFLUENZA
					loc(SampleDto.POSITIVE_VIRAL_CULTURE) +
					loc(SampleDto.POSITIVE_REAL_TIME) +
					loc(SampleDto.FOUR_FOLD_RISE) +
					fluidRowLocs(SampleDto.INFLUENZA_VIRUS, SampleDto.OTHER_INFLUENZA_VIRUS) +
					loc(SampleDto.TREATMENT) +
					loc(SampleDto.STATE_TREATMENT_ADMINISTERED) +

					locCss(VSPACE_TOP_3, SampleDto.PATHOGEN_TESTING_REQUESTED) +
					loc(PATHOGEN_TESTING_READ_HEADLINE_LOC) +
					loc(PATHOGEN_TESTING_INFO_LOC) +
					loc(SampleDto.REQUESTED_PATHOGEN_TESTS) +
					loc(SampleDto.REQUESTED_OTHER_PATHOGEN_TESTS) +
					loc(REQUESTED_PATHOGEN_TESTS_READ_LOC) +

					locCss(VSPACE_TOP_3, SampleDto.ADDITIONAL_TESTING_REQUESTED) +
					loc(ADDITIONAL_TESTING_READ_HEADLINE_LOC) +
					loc(ADDITIONAL_TESTING_INFO_LOC) +
					//loc(SampleDto.REQUESTED_ADDITIONAL_TESTS) +
				//	loc(SampleDto.REQUESTED_OTHER_ADDITIONAL_TESTS) +
					loc(REQUESTED_ADDITIONAL_TESTS_READ_LOC) +

					locCss(VSPACE_TOP_3, SampleDto.SHIPPED) +
					fluidRowLocs(SampleDto.SHIPMENT_DATE, SampleDto.SHIPMENT_DETAILS) +
					locCss(VSPACE_TOP_3, SampleDto.RECEIVED) +
					fluidRowLocs(SampleDto.RECEIVED_DATE, SampleDto.LAB_SAMPLE_ID) +
					fluidRowLocs(SampleDto.SPECIMEN_CONDITION, SampleDto.NO_TEST_POSSIBLE_REASON) +
					fluidRowLocs(SampleDto.COMMENT) +
					fluidRowLocs(SampleDto.PATHOGEN_TEST_RESULT) +
                    fluidRowLocs(SampleDto.SPECIMEN_CONDITION, SampleDto.NO_TEST_POSSIBLE_REASON) +
					fluidRowLocs(CaseDataDto.DELETION_REASON) +
					fluidRowLocs(CaseDataDto.OTHER_DELETION_REASON) +

					//AFP
					loc(STOOL_SPECIMEN_COLLECTION_HEADLINE_LOC) +
					fluidRowLocs(SampleDto.DATE_FIRST_SPECIMEN, SampleDto.DATE_SECOND_SPECIMEN, SampleDto.DATE_SPECIMEN_SENT_NATIONAL_LEVEL) +
					fluidRowLocs(SampleDto.DATE_SPECIMEN_RECEIVED_NATIONAL_LEVEL, SampleDto.DATE_SPECIMEN_SENT_INTERCOUNTY_NATLAB) +

					loc(STOOL_SPECIMEN_RESULTS_HEADLINE_LOC) +
					fluidRowLocs(SampleDto.DATE_SPECIMEN_RECEIVED_INTERCOUNTY_NATLAB, SampleDto.STATUS_SPECIMEN_RECEPTION_AT_LAB) +
					fluidRowLocs(6,SampleDto.DATE_COMBINED_CELL_CULTURE_RESULTS) +


					fluidRowLocs(SampleDto.DATE_SENT_NATIONAL_REG_LAB, SampleDto.DATE_DIFFERENTIATION_SENT_EPI) +
					fluidRowLocs(6,SampleDto.DATE_DIFFERENTIATION_RECEIVED_EPI) +
					fluidRowLocs(SampleDto.DATE_ISOLATE_SENT_SEQUENCING, SampleDto.DATE_SEQ_RESULTS_SENT_PROGRAM) +

					loc(FINAL_LAB_RESULTS_HEADLINE_LOC) +
					fluidRowLocs(SampleDto.W1, SampleDto.W2, SampleDto.W3) +
					fluidRowLocs(SampleDto.SL1, SampleDto.SL2, SampleDto.SL3) +
					fluidRowLocs(SampleDto.DISCORDANT, SampleDto.FINAL_LAB_RESULTS) +

					loc(FOLLOW_UP_EXAMINATION_HEADLINE_LOC) +
					fluidRowLocs(SampleDto.DATE_FOLLOWUP_EXAM, SampleDto.RESIDUAL_ANALYSIS, SampleDto.RESULT_EXAM) +
					fluidRowLocs(6,SampleDto.IMMUNOCOMPROMISED_STATUS_SUSPECTED) +
					fluidRowLocs(6,SampleDto.AFP_FINAL_CLASSIFICATION);


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
		sampleDateField = addField(SampleDto.SAMPLE_DATE_TIME, DateTimeField.class);
		sampleDateField.setInvalidCommitted(false);
		suspectedDisease = addField(SampleDto.SUSPECTED_DISEASE);
		dateLabReceivedSpecimen = addField(SampleDto.DATE_LAB_RECEIVED_SPECIMEN);

		ComboBox diseaseBox = new ComboBox("Diseases");

		for (Disease ahfDisease : Disease.AHF_DISEASES) {
			diseaseBox.addItem(ahfDisease);
		}

		diseaseField = addField(SampleDto.DISEASE, diseaseBox);
		diseaseField.setVisible(false);

		addField(SampleDto.SAMPLE_MATERIAL_TEXT, TextField.class);
		sampleSource =  addField(SampleDto.SAMPLE_SOURCE, ComboBox.class);
		addField(SampleDto.FIELD_SAMPLE_ID, TextField.class);
		addField(SampleDto.FOR_RETEST, OptionGroup.class).setRequired(false);
		addDateField(SampleDto.SHIPMENT_DATE, DateField.class, 7);
		addField(SampleDto.SHIPMENT_DETAILS, TextField.class);
		addField(SampleDto.RECEIVED_DATE, DateField.class);

		lab = addInfrastructureField(SampleDto.LAB);
		lab.addItems(FacadeProvider.getFacilityFacade().getAllActiveLaboratories(true));
		labDetails = addField(SampleDto.LAB_DETAILS, TextField.class);
		labLocation = addField(SampleDto.LAB_LOCATION, TextField.class);
		labDetails.setVisible(false);
		lab.addValueChangeListener(event -> updateLabDetailsVisibility(labDetails, event));

		addField(SampleDto.SPECIMEN_CONDITION, ComboBox.class);
		addField(SampleDto.NO_TEST_POSSIBLE_REASON, TextField.class);
		TextArea comment = addField(SampleDto.COMMENT, TextArea.class);
		comment.setRows(4);
		comment.setDescription(
				I18nProperties.getPrefixDescription(SampleDto.I18N_PREFIX, SampleDto.COMMENT, "") + "\n"
						+ I18nProperties.getDescription(Descriptions.descGdpr));
		CheckBox check = addField(SampleDto.SHIPPED, CheckBox.class);

		addField(SampleDto.RECEIVED, CheckBox.class);

		OptionGroup ipSampleSent = new OptionGroup("YesNo");

		for (YesNo val : YesNo.values()) {
			if (val == YesNo.YES || val == YesNo.NO) {
				ipSampleSent.addItem(val);
			}
		}
		addField(SampleDto.IPSAMPLESENT, ipSampleSent);

		ComboBox ipSampleResults = addField(SampleDto.IPSAMPLERESULTS);
		testResultField = addField(SampleDto.PATHOGEN_TEST_RESULT, ComboBox.class);
		testResultField.removeItem(PathogenTestResultType.NOT_DONE);
		testResultField.removeItem(PathogenTestResultType.INDETERMINATE);

		addFields(SampleDto.SAMPLING_REASON, SampleDto.SAMPLING_REASON_DETAILS);
		FieldHelper.setVisibleWhen(
				getFieldGroup(),
				SampleDto.SAMPLING_REASON_DETAILS,
				SampleDto.SAMPLING_REASON,
				Collections.singletonList(SamplingReason.OTHER_REASON),
				true);

		FieldHelper.setVisibleWhen(getFieldGroup(), SampleDto.IPSAMPLERESULTS, SampleDto.IPSAMPLESENT, Collections.singletonList(YesNo.YES), true);
		laboratorySampleCondition = addField(SampleDto.LABORATORY_SAMPLE_CONDITION, OptionGroup.class);
		laboratoryFinalResults = addField(SampleDto.LABORATORY_FINAL_RESULTS, TextField.class);
		laboratoryFinalResults.setVisible(false);
		dateFormSentToDistrict = addField(SampleDto.DATE_FORM_SENT_TO_DISTRICT, DateField.class);
		dateFormReceivedAtDistrict = addField(SampleDto.DATE_FORM_RECEIVED_AT_DISTRICT, DateField.class);
		dateResultsSentToClinician = addField(SampleDto.DATE_RESULTS_RECEIVED_SENT_TO_CLINICIAN, DateField.class);
		addField(SampleDto.DELETION_REASON);
		addField(SampleDto.OTHER_DELETION_REASON, TextArea.class).setRows(3);
		setVisible(false, SampleDto.DELETION_REASON, SampleDto.OTHER_DELETION_REASON);

		diseaseField.addValueChangeListener((ValueChangeListener) valueChangeEvent -> {
			Disease disease = (Disease) valueChangeEvent.getProperty().getValue();

			switch(disease){
				case EVD:
				case LASSA:
				case DENGUE:
				case CHIKUNGUNYA:
				case YELLOW_FEVER:
				case MARBURG:
				case ZIKA:
					selectAHFTests();
					break;
			}
		});

	}

	protected void defaultValueChangeListener() {

		final NullableOptionGroup samplePurposeField = getField(SampleDto.SAMPLE_PURPOSE);
		final Field<?> receivedField = getField(SampleDto.RECEIVED);
		final Field<?> shippedField = getField(SampleDto.SHIPPED);

		samplePurposeField.setRequired(true);

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

		//disease = getAssociatedDisease();

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

		getDisease();

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

		sampleMaterialComboBox = addField(SampleDto.SAMPLE_MATERIAL);

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

		StringBuilder reportInfoText = new StringBuilder().append(I18nProperties.getString(Strings.reportedOn))
				.append(" ")
				.append(DateFormatHelper.formatLocalDateTime(getValue().getReportDateTime()));
		if (reportingUser != null) {
			reportInfoText.append(" ").append(I18nProperties.getString(Strings.by)).append(" ").append(reportingUser.toString());
		}
		Label reportInfoLabel = new Label(reportInfoText.toString());
		reportInfoLabel.setEnabled(false);
		getContent().addComponent(reportInfoLabel, REPORT_INFO_LABEL_LOC);

		hidePropertiesVisibility();

			switch (disease) {
				case CSM:
					handleCSM();
					break;
				case AFP:
					handleAFP();
					break;
				case AHF:
					handleAHF();
					break;
				case YELLOW_FEVER:
					handleYellowFever();
					break;
				case NEW_INFLUENZA:
					handleNewInfluenza();
					break;
				case MEASLES:
					handleMeasles();
					break;
				default:
			}

		setSampleMaterialTypesForDisease(disease);
    }

	protected abstract Disease getDisease();

	protected void updateLabDetailsVisibility(TextField labDetails, Property.ValueChangeEvent event) {
		if (event.getProperty().getValue() != null
				&& ((FacilityReferenceDto) event.getProperty().getValue()).getUuid().equals(FacilityDto.OTHER_FACILITY_UUID)) {
			labDetails.setVisible(true);
			labDetails.setRequired(isEditableAllowed(labDetails));
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
		requestedPathogenTestsField = addField(SampleDto.REQUESTED_PATHOGEN_TESTS, OptionGroup.class);
		CssStyles.style(requestedPathogenTestsField, CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
		requestedPathogenTestsField.setMultiSelect(true);
		requestedPathogenTestsField.addItems(
				Arrays.stream(PathogenTestType.values())
						.filter(c -> fieldVisibilityCheckers.isVisible(PathogenTestType.class, c.name()))
						.collect(Collectors.toList()));
		requestedPathogenTestsField.removeItem(PathogenTestType.OTHER);
		requestedPathogenTestsField.setCaption(null);

		requestedSampleMaterialsField = addField(SampleDto.REQUESTED_SAMPLE_MATERIALS, OptionGroup.class);
		CssStyles.style(requestedSampleMaterialsField, CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
		requestedSampleMaterialsField.setMultiSelect(true);

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
		getContent().addComponent(pathogenTestsHeading,  PATHOGEN_TESTING_READ_HEADLINE_LOC);

		Label additionalTestsHeading = new Label(I18nProperties.getString(Strings.headingRequestedAdditionalTests));
		CssStyles.style(additionalTestsHeading, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_4);
		getContent().addComponent(additionalTestsHeading, ADDITIONAL_TESTING_READ_HEADLINE_LOC);

		updateRequestedTestFields();

		sampleTestsField = addField(SampleDto.SAMPLE_TESTS, OptionGroup.class);
		sampleTestsField.setVisible(false);
	}
	CheckBox sampleMaterialRequestedField = addField(SampleDto.SAMPLE_MATERIAL_REQUESTED, CheckBox.class);
	Field<?> sampleMaterialTestingField = getField(SampleDto.SAMPLE_MATERIAL_REQUESTED);


	protected void initializeMaterialsMultiSelect( ){

			Label materialMultiSelectInfoLabel = new Label(I18nProperties.getString(Strings.infoSampleMaterialSelection));
			getContent().addComponent(materialMultiSelectInfoLabel, SAMPLE_MATERIAL_INFO_LOC);

			// Yes/No fields for sample materials
			sampleMaterialRequestedField.setWidthUndefined();
			sampleMaterialRequestedField.addValueChangeListener(e -> updateSampleMaterialFields());
			sampleMaterialRequestedField.setCaption("Sample Types");


			// CheckBox groups to select sample Materials
			CssStyles.style(requestedSampleMaterialsField, CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
			requestedSampleMaterialsField.setMultiSelect(true);
			requestedSampleMaterialsField.addItems(
					Arrays.stream(YellowFeverSample.values())
							.filter( c -> fieldVisibilityCheckers.isVisible(YellowFeverSample.class, c.name()))
							.collect(Collectors.toList()));
			requestedSampleMaterialsField.setCaption(null);

	}

	private void selectAHFTests(){

		sampleTestsField.setCaption("Sample Tests");
		sampleTestsField.setVisible(true);
		List<PathogenTestType> validValues = Arrays.asList(PathogenTestType.IGG_SERUM_ANTIBODY, PathogenTestType.IGM_SERUM_ANTIBODY, PathogenTestType.PCR_RT_PCR);
		FieldHelper.updateEnumData(sampleTestsField, validValues);
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
		handleDisease(Disease.AHF, "Noguchi Memorial Institute for Medical Research");
		handleDisease(Disease.AFP, "Noguchi Memorial Institute for Medical Research");

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
	}
	private void handleDisease(Disease targetDisease, String labName) {
		if (disease == targetDisease) {
			setVisibleAndCheckLab(labName, SampleDto.PATHOGEN_TESTING_REQUESTED);
		}
	}

	private void handleCSM() {
		sampleMaterialComboBox.setValue(SampleMaterial.CEREBROSPINAL_FLUID);

		ComboBox laboType = addField(SampleDto.LABORATORY_TYPE, ComboBox.class);
		laboType.setRequired(true);

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

		TextField laboratoryName = addField(SampleDto.LABORATORY_NAME, TextField.class);
		laboratorySampleDateReceived = addField(SampleDto.LABORATORY_SAMPLE_DATE_RECEIVED, DateTimeField.class);
		laboratorySampleDateReceived.setInvalidCommitted(false);
		TextField laboratoryNumber = addField(SampleDto.LABORATORY_NUMBER, TextField.class);
		OptionGroup laboratorySampleContainerReceived = addField(SampleDto.LABORATORY_SAMPLE_CONTAINER_RECEIVED, OptionGroup.class);
		laboratorySampleContainerReceived.setWidthUndefined();
		TextField laboratorySampleContainerOther = addField(SampleDto.LABORATORY_SAMPLE_CONTAINER_OTHER, TextField.class);
		NullableOptionGroup laboratoryAppearanceOfCSF = addField(SampleDto.LABORATORY_APPEARANCE_OF_CSF, NullableOptionGroup.class);
		NullableOptionGroup laboratoryTestPerformed = addField(SampleDto.LABORATORY_TEST_PERFORMED, NullableOptionGroup.class);
		TextField laboratoryTestPerformedOther = addField(SampleDto.LABORATORY_TEST_PERFORMED_OTHER, TextField.class);
		TextField laboratoryCytology = addField(SampleDto.LABORATORY_CYTOLOGY, TextField.class);
		NullableOptionGroup laboratoryGram = addField(SampleDto.LABORATORY_GRAM, NullableOptionGroup.class);
		TextField laboratoryGramOther = addField(SampleDto.LABORATORY_GRAM_OTHER, TextField.class);
		OptionGroup laboratoryRdtPerformed = addField(SampleDto.LABORATORY_RDT_PERFORMED, OptionGroup.class);
		TextField laboratoryRdtResults = addField(SampleDto.LABORATORY_RDT_RESULTS, TextField.class);

		laboratorySampleContainerOther.setVisible(false);
		laboratoryTestPerformedOther.setVisible(false);
		laboratoryGramOther.setVisible(false);
		laboratoryRdtResults.setVisible(false);

		FieldHelper
		         .setVisibleWhen(laboratoryRdtPerformed, Arrays.asList(laboratoryRdtResults), Arrays.asList(YesNo.YES), true);
		 FieldHelper
		         .setVisibleWhen(laboratorySampleContainerReceived, Arrays.asList(laboratorySampleContainerOther), Arrays.asList(SampleContainerUsed.OTHER), true);
		 FieldHelper
		         .setVisibleWhen(laboratoryTestPerformed, Arrays.asList(laboratoryTestPerformedOther), Arrays.asList(LabTest.OTHER), true);
		 FieldHelper
		         .setVisibleWhen(laboratoryGram, Arrays.asList(laboratoryGramOther), Arrays.asList(Gram.OTHER_PATHOGENS), true);


		ComboBox laboratoryLatex = new ComboBox();
		for (LatexCulture latexCulture : LatexCulture.LATEX) {
			laboratoryLatex.addItem(latexCulture);
			laboratoryLatex.setItemCaption(latexCulture, latexCulture.toString());
		}
		addField(SampleDto.LABORATORY_LATEX, laboratoryLatex);

		ComboBox laboratoryCulture = new ComboBox();
		for (LatexCulture latexCulture : LatexCulture.LAB_CULTURE) {
			laboratoryCulture.addItem(latexCulture);
			laboratoryCulture.setItemCaption(latexCulture, latexCulture.toString());
		}
		addField(SampleDto.LABORATORY_CULTURE, laboratoryCulture);
		TextField laboratoryCultureOther = addField(SampleDto.LABORATORY_CULTURE_OTHER, TextField.class);
		TextField laboratoryOtherTests = addField(SampleDto.LABORATORY_OTHER_TESTS, TextField.class);
		TextField laboratoryOtherTestsResults = addField(SampleDto.LABORATORY_OTHER_TESTS_RESULTS, TextField.class);
		ComboBox laboratoryCeftriaxone = addField(SampleDto.LABORATORY_CEFTRIAXONE, ComboBox.class);
		ComboBox laboratoryPenicillinG = addField(SampleDto.LABORATORY_PENICILLIN_G, ComboBox.class);
		ComboBox laboratoryAmoxycillin = addField(SampleDto.LABORATORY_AMOXYCILLIN, ComboBox.class);
		ComboBox laboratoryOxacillin = addField(SampleDto.LABORATORY_OXACILLIN, ComboBox.class);
		OptionGroup laboratoryAntibiogramOther = addField(SampleDto.LABORATORY_ANTIBIOGRAM_OTHER, OptionGroup.class);
		DateField laboratoryDatePcrPerformed = addField(SampleDto.LABORATORY_DATE_PCR_PERFORMED, DateField.class);
		TextField laboratoryPcrType = addField(SampleDto.LABORATORY_PCR_TYPE, TextField.class);

		ComboBox labPcrOptions = new ComboBox();
		for (LatexCulture latexCulture : LatexCulture.LAB_CULTURE) {
			labPcrOptions.addItem(latexCulture);
			labPcrOptions.setItemCaption(latexCulture, latexCulture.toString());
		}
		addField(SampleDto.LABORATORY_PCR_OPTIONS, labPcrOptions);

		TextField laboratorySerotype = addField(SampleDto.LABORATORY_SEROTYPE, TextField.class);
		TextField laboratorySerotypeType = addField(SampleDto.LABORATORY_SEROTYPE_TYPE, TextField.class);
		TextField laboratorySerotypeResults = addField(SampleDto.LABORATORY_SEROTYPE_RESULTS, TextField.class);

		TextArea laboratoryObservations = addField(SampleDto.LABORATORY_OBSERVATIONS, TextArea.class);
		laboratoryObservations.setRows(4);
		laboratoryObservations.setDescription(
				I18nProperties.getPrefixDescription(SampleDto.I18N_PREFIX, SampleDto.LABORATORY_OBSERVATIONS, "") + "\n"
						+ I18nProperties.getDescription(Descriptions.observation));

		DateField laboratoryDateResultsSentHealthFacility = addField(SampleDto.LABORATORY_DATE_RESULTS_SENT_HEALTH_FACILITY, DateField.class);
		DateField laboratoryDateResultsSentDSD = addField(SampleDto.LABORATORY_DATE_RESULTS_SENT_DSD, DateField.class);

		OptionGroup laboratoryFinalClassification = new OptionGroup();
		for (CaseClassification caseClassification : CaseClassification.CASE_CLASSIFY) {
			laboratoryFinalClassification.addItem(caseClassification);
			laboratoryFinalClassification.setItemCaption(caseClassification, caseClassification.toString());
		}
		addField(SampleDto.LABORATORY_FINAL_CLASSIFICATION, laboratoryFinalClassification);

		laboType.addValueChangeListener(e -> {
			LabType labType = (LabType) e.getProperty().getValue();

			districtLaboratory.setVisible(false);
			regionalLaboratory.setVisible(false);
			referenceLaboratory.setVisible(false);

			setVisible(false, SampleDto.LABORATORY_CULTURE, SampleDto.LABORATORY_CULTURE_OTHER, SampleDto.LABORATORY_OTHER_TESTS, SampleDto.LABORATORY_OTHER_TESTS_RESULTS);
			setVisible(false, SampleDto.LABORATORY_CEFTRIAXONE, SampleDto.LABORATORY_PENICILLIN_G, SampleDto.LABORATORY_AMOXYCILLIN, SampleDto.LABORATORY_OXACILLIN, SampleDto.LABORATORY_ANTIBIOGRAM_OTHER);
			setVisible(false, SampleDto.LABORATORY_DATE_PCR_PERFORMED, SampleDto.LABORATORY_PCR_TYPE, SampleDto.LABORATORY_PCR_OPTIONS, SampleDto.LABORATORY_SEROTYPE, SampleDto.LABORATORY_SEROTYPE_TYPE,
					SampleDto.LABORATORY_SEROTYPE_RESULTS, SampleDto.LABORATORY_FINAL_CLASSIFICATION);

			if (labType == LabType.DISTRICT_LAB) {
				districtLaboratory.setVisible(true);
			}
			else if (labType == LabType.REGIONAL_LAB) {
				regionalLaboratory.setVisible(true);
				setVisible(true,SampleDto.LABORATORY_CULTURE, SampleDto.LABORATORY_CULTURE_OTHER, SampleDto.LABORATORY_OTHER_TESTS, SampleDto.LABORATORY_OTHER_TESTS_RESULTS);
				setVisible(true, SampleDto.LABORATORY_CEFTRIAXONE, SampleDto.LABORATORY_PENICILLIN_G, SampleDto.LABORATORY_AMOXYCILLIN, SampleDto.LABORATORY_OXACILLIN, SampleDto.LABORATORY_ANTIBIOGRAM_OTHER);
				setVisible(true, SampleDto.LABORATORY_DATE_PCR_PERFORMED, SampleDto.LABORATORY_PCR_TYPE, SampleDto.LABORATORY_PCR_OPTIONS, SampleDto.LABORATORY_SEROTYPE, SampleDto.LABORATORY_SEROTYPE_TYPE, SampleDto.LABORATORY_SEROTYPE_RESULTS);
			}
			else if (labType == LabType.REFERENCE_LAB) {
				referenceLaboratory.setVisible(true);
				// Set all other fields to visible
				setVisible(true, SampleDto.LABORATORY_CULTURE, SampleDto.LABORATORY_CULTURE_OTHER, SampleDto.LABORATORY_OTHER_TESTS, SampleDto.LABORATORY_OTHER_TESTS_RESULTS);
				setVisible(true, SampleDto.LABORATORY_CEFTRIAXONE, SampleDto.LABORATORY_PENICILLIN_G, SampleDto.LABORATORY_AMOXYCILLIN, SampleDto.LABORATORY_OXACILLIN, SampleDto.LABORATORY_ANTIBIOGRAM_OTHER);
				setVisible(true, SampleDto.LABORATORY_DATE_PCR_PERFORMED, SampleDto.LABORATORY_PCR_TYPE, SampleDto.LABORATORY_PCR_OPTIONS, SampleDto.LABORATORY_SEROTYPE, SampleDto.LABORATORY_SEROTYPE_TYPE,
						SampleDto.LABORATORY_SEROTYPE_RESULTS, SampleDto.LABORATORY_FINAL_CLASSIFICATION);
			}

			setVisible(false, SampleDto.PATHOGEN_TESTING_REQUESTED);

		});
		laboratoryFinalResults.setVisible(true);

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

		setVisible(false, SampleDto.SAMPLE_PURPOSE, SampleDto.REQUESTED_SAMPLE_MATERIALS, SampleDto.FIELD_SAMPLE_ID, SampleDto.SAMPLE_MATERIAL_TEXT, SampleDto.SAMPLE_MATERIAL_REQUESTED, SampleDto.SHIPPED, SampleDto.RECEIVED, SampleDto.COMMENT, SampleDto.SAMPLE_TESTS, SampleDto.DISEASE, SampleDto.SAMPLING_REASON, SampleDto.IPSAMPLESENT, SampleDto.SAMPLE_MATERIAL, SampleDto.PATHOGEN_TEST_RESULT, SampleDto.SAMPLE_SOURCE);

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
				SampleDto.SAMPLE_PURPOSE,SampleDto.SAMPLING_REASON, SampleDto.FIELD_SAMPLE_ID, SampleDto.SAMPLE_MATERIAL_REQUESTED, SampleDto.SHIPPED, SampleDto.RECEIVED, SampleDto.COMMENT, SampleDto.PATHOGEN_TESTING_REQUESTED, SampleDto.REQUESTED_SAMPLE_MATERIALS);

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
		List<SampleMaterial> validValues = Arrays.asList(SampleMaterial.OROPHARYNGEAL_SWAB, SampleMaterial.NP_SWAB, SampleMaterial.SERUM, SampleMaterial.PLASMA);
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
		ComboBox finalClassification = addField(SampleDto.LABORATORY_FINAL_CLASSIFICATION, classificationBox);

		FieldHelper
				.setVisibleWhen(Virus, Arrays.asList(otherVirus), Arrays.asList(InfluenzaVirus.OTHER), true);
		suspectedDisease.setVisible(false);
		labLocation.setVisible(false);
		dateLabReceivedSpecimen.setVisible(false);
		laboratorySampleCondition.setVisible(false);
		dateFormSentToDistrict.setVisible(false);
		dateFormReceivedAtDistrict.setVisible(false);
		dateResultsSentToClinician.setVisible(false);
		dateSpecimenSentToLab.setVisible(false);
	}

	private void addSampleDispatchFields() {
		OptionGroup outcome = new OptionGroup("Sample Dispatch Modes");

		for (SampleDispatchMode sampleDis : SampleDispatchMode.values()) {
			if (sampleDis == SampleDispatchMode.NATIONAL_LAB || sampleDis == SampleDispatchMode.REGIONAL_COLDROOM || sampleDis == SampleDispatchMode.NATIONAL_BY_DISTRICT) {
				outcome.addItem(sampleDis);
			}
		}

		OptionGroup sampleDispatchModeTypes = addField(SampleDto.SAMPLE_DISPATCH_MODE, outcome);
		DateField cardDateField = addField(SampleDto.SAMPLE_DISPATCH_DATE, DateField.class);
		cardDateField.setValue(new Date());

		FieldHelper.setEnabledWhen(
				sampleDispatchModeTypes,
				Arrays.asList(SampleDispatchMode.NATIONAL_LAB, SampleDispatchMode.REGIONAL_COLDROOM, SampleDispatchMode.NATIONAL_BY_DISTRICT),
				Collections.singletonList(cardDateField),
				false);

		setRequired(false, SampleDto.SAMPLE_DATE_TIME, SampleDto.SAMPLE_MATERIAL);
		   sampleMaterialComboBox.setVisible(false);
		   sampleMaterialComboBox.setRequired(false);
	}

	private void handleMeasles() {
		if (disease == Disease.MEASLES) {
			addSampleDispatchFields();
			setVisible(false, SampleDto.SAMPLING_REASON);
			setVisible(true, SampleDto.RECEIVED, SampleDto.REQUESTED_SAMPLE_MATERIALS);
			List<PathogenTestType> measelesPathogenTests = PathogenTestType.getMeaslesTestTypes();
			Arrays.stream(PathogenTestType.values())
					.filter(pathogenTestType -> !measelesPathogenTests.contains(pathogenTestType))
					.forEach(pathogenTestType -> requestedPathogenTestsField.removeItem(pathogenTestType));

		}
	}

	private FacilityReferenceDto findLabByName(List<FacilityReferenceDto> labs, String labName) {
		for (FacilityReferenceDto labItem : labs) {
			if (labName.equals(labItem.getCaption())) {
				return labItem;
			}
		}

		return null;
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

		if (lab != null) {
			List<FacilityReferenceDto> allActiveLaboratories = FacadeProvider.getFacilityFacade().getAllActiveLaboratories(false);
			FacilityReferenceDto facilityLab = findLabByName(allActiveLaboratories, labName);

			if (facilityLab != null) {
				lab.addItems(allActiveLaboratories);
				lab.setValue(facilityLab);
				lab.setReadOnly(true);
				labDetails.setVisible(false);
				labDetails.setRequired(false);
			} else {
				System.out.println("Please add " + labName + " to Facility Configuration");
			}
		} else {
			System.out.println("Lab dropdown is null. Please check your code.");
		}
	}

	private Disease getDiseaseFromCase(String caseUuid) {
		CaseDataDto caseDataDto = FacadeProvider.getCaseFacade().getCaseDataByUuid(caseUuid);
		if (caseDataDto != null) {
			return caseDataDto.getDisease();
		}
		return null;
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

	private void setPropertiesVisibility(){
		setVisible(false,
				SampleDto.SAMPLE_PURPOSE,
				SampleDto.SAMPLING_REASON,
				SampleDto.SAMPLING_REASON_DETAILS,
				SampleDto.IPSAMPLESENT,
				SampleDto.IPSAMPLERESULTS,
				SampleDto.SAMPLE_MATERIAL_REQUESTED,
				SampleDto.REQUESTED_SAMPLE_MATERIALS,
				SampleDto.PATHOGEN_TESTING_REQUESTED,
				SampleDto.REQUESTED_PATHOGEN_TESTS,
				SampleDto.REQUESTED_OTHER_PATHOGEN_TESTS,
				SampleDto.ADDITIONAL_TESTING_REQUESTED,
				SampleDto.REQUESTED_ADDITIONAL_TESTS,
				SampleDto.REQUESTED_OTHER_ADDITIONAL_TESTS,
				SampleDto.SAMPLE_SOURCE,
				SampleDto.FIELD_SAMPLE_ID,
				SampleDto.DISEASE,
				SampleDto.SAMPLE_TESTS,
				SampleDto.COMMENT,
				SampleDto.PATHOGEN_TEST_RESULT,
				SampleDto.SHIPPED,
				SampleDto.SHIPMENT_DATE,
				SampleDto.SHIPMENT_DETAILS,
				SampleDto.RECEIVED,
				SampleDto.RECEIVED_DATE,
				SampleDto.LAB_SAMPLE_ID,
				SampleDto.SPECIMEN_CONDITION,
				SampleDto.NO_TEST_POSSIBLE_REASON,
				CaseDataDto.DELETION_REASON,
				CaseDataDto.OTHER_DELETION_REASON
		);
	}

	private void hidePropertiesVisibility(){
		setVisible(false,
				SampleDto.SAMPLE_PURPOSE,
				SampleDto.SAMPLING_REASON,
				SampleDto.SAMPLING_REASON_DETAILS,
				SampleDto.IPSAMPLESENT,
				SampleDto.IPSAMPLERESULTS,
				SampleDto.SAMPLE_MATERIAL_REQUESTED,
				SampleDto.SAMPLE_MATERIAL_TEXT,
				SampleDto.REQUESTED_SAMPLE_MATERIALS,
				SampleDto.PATHOGEN_TESTING_REQUESTED,
				SampleDto.REQUESTED_PATHOGEN_TESTS,
				SampleDto.REQUESTED_OTHER_PATHOGEN_TESTS,
				SampleDto.ADDITIONAL_TESTING_REQUESTED,
				SampleDto.REQUESTED_ADDITIONAL_TESTS,
				SampleDto.REQUESTED_OTHER_ADDITIONAL_TESTS,
				SampleDto.SAMPLE_SOURCE,
				SampleDto.DISEASE,
				SampleDto.COMMENT,
				SampleDto.PATHOGEN_TEST_RESULT,
				SampleDto.NO_TEST_POSSIBLE_REASON,
				CaseDataDto.DELETION_REASON,
				CaseDataDto.OTHER_DELETION_REASON
		);
	}

	//create a function with a switch statement within to return sampleMaterials types for specific diseases
	public void setSampleMaterialTypesForDisease(Disease disease){
		switch (disease){
			case MEASLES:
				requestedSampleMaterialsField.addItems(
						Arrays.stream(SampleMaterial.getMeaselsMateriealTypes())
								.filter( c -> fieldVisibilityCheckers.isVisible(SampleMaterial.class, c.name()))
								.collect(Collectors.toList()));
				break;
			case YELLOW_FEVER:
				requestedSampleMaterialsField.addItems(
						Arrays.stream(SampleMaterial.getYellowFeverMateriealTypes())
								.filter( c -> fieldVisibilityCheckers.isVisible(SampleMaterial.class, c.name()))
								.collect(Collectors.toList()));
				break;
			default:
				requestedSampleMaterialsField.addItems(
						Arrays.stream(SampleMaterial.values())
								.filter( c -> fieldVisibilityCheckers.isVisible(SampleMaterial.class, c.name()))
								.collect(Collectors.toList()));
				break;
		}
	}

}
