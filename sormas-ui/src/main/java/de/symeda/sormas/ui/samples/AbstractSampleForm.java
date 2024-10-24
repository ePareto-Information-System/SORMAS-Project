package de.symeda.sormas.ui.samples;

import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Validator;
import com.vaadin.v7.ui.*;
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
import de.symeda.sormas.api.i18n.*;
import de.symeda.sormas.api.infrastructure.facility.FacilityDto;
import de.symeda.sormas.api.infrastructure.facility.FacilityReferenceDto;
import de.symeda.sormas.api.sample.*;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.InjectionSite;
import de.symeda.sormas.api.utils.SampleContainerUsed;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.api.utils.pseudonymization.SampleDispatchMode;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.*;

import java.util.*;
import java.util.stream.Collectors;

import static de.symeda.sormas.ui.utils.CssStyles.*;
import static de.symeda.sormas.ui.utils.LayoutUtil.*;

public abstract class AbstractSampleForm extends AbstractEditForm<SampleDto> {

    private static final long serialVersionUID = -2323128076462668517L;

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
	protected static final String NATIONAL_SECRETARIAT_ONLY = "nationalSecretariatOnly";
	protected static final String OTHER_INFORMATION_HEADLINE_LOC = "otherInformationHeadlineLoc";
//	headingSpecimenHandling
	protected static final String HEADING_SPECIMEN_HANDLING = "headingSpecimenHandling";


	private Disease disease;
	public ComboBox sampleMaterialComboBox;
	public ComboBox sampleSource;

	private TextField labLocation;
	private TextField labSampleId;
	private DateTimeField sampleDateTimeField;
	private NullableOptionGroup hasSampleBeenCollected;
	OptionGroup requestedSampleMaterialsField;
	private ComboBox testResultField;
	private ComboBox suspectedDisease;
	private DateField dateLabReceivedSpecimen;
	private OptionGroup laboratorySampleCondition;
	private DateField dateFormSentToDistrict;
	private DateField dateFormReceivedAtDistrict;
	private DateField dateResultsSentToClinician;
	private DateField dateSpecimenSentToLab;
	private CheckBox pathogenTestingRequestedField;
	private OptionGroup laboratorySampleContainerReceived;
	private TextField laboratorySampleContainerOther;
	private DateField dateSampleSentToLab;
	private DateField laboratoryDateResultsSentDSD;
	private DateField dateSurveillanceSentResultsToDistrict;
	private DateField dateFormSentToHigherLevel;
	private TextField personCompletingForm;
	OptionGroup requestedPathogenTestsField;
	private CheckBox sampleReceived;
	private DateTimeField sampleReceivedDate;
    private ComboBox sampleSpecimenCondition;
    private OptionGroup ipSampleTestResults;


	//@formatter:off
    protected static final String SAMPLE_COMMON_HTML_LAYOUT =
			fluidRowLocs(SampleDto.SAMPLE_PURPOSE) +
            fluidRowLocs(SampleDto.UUID, REPORT_INFO_LABEL_LOC) +
                    fluidRowLocs(SampleDto.CSF_SAMPLE_COLLECTED) +
                    fluidRowLocs(6, SampleDto.CSF_REASON) +
					fluidRowLocs(6, SampleDto.HAS_SAMPLE_BEEN_COLLECTED) +
					fluidRowLocs(SampleDto.SAMPLE_DATE_TIME) +
                    fluidRowLocs(SampleDto.APPEARANCE_OF_CSF) +
                    fluidRowLocs(6,SampleDto.INOCULATION_TIME_TRANSPORT_MEDIA) +
                    fluidRowLocs(SampleDto.SAMPLE_SENT_TO_LAB, SampleDto.REASON_NOT_SENT_TO_LAB) +
                    fluidRowLocs(6,SampleDto.DATE_SAMPLE_SENT_TO_LAB) +
                    fluidRowLocs(SampleDto.LAB, SampleDto.LAB_DETAILS, SampleDto.LAB_LOCATION) +
                    fluidRowLocs(SampleDto.SAMPLE_CONTAINER_USED, SampleDto.CONTAINER_OTHER) +
                    fluidRowLocs(SampleDto.RDT_PERFORMED, SampleDto.RDT_RESULTS) +
                    fluidRowLocs(SampleDto.DISTRICT_NOTIFICATION_DATE, SampleDto.NAME_OF_PERSON, SampleDto.TEL_NUMBER) +
                    fluidRowLocs(SampleDto.DATE_FORM_SENT_TO_DISTRICT, SampleDto.DATE_FORM_RECEIVED_AT_DISTRICT) +
                    fluidRowLocs(SampleDto.DATE_FORM_SENT_TO_REGION, SampleDto.DATE_FORM_RECEIVED_AT_REGION) +
                    fluidRowLocs(SampleDto.DATE_FORM_SENT_TO_NATIONAL, SampleDto.DATE_FORM_RECEIVED_AT_NATIONAL) +
					fluidRowLocs(SampleDto.SUSPECTED_DISEASE, SampleDto.DATE_LAB_RECEIVED_SPECIMEN) +
					fluidRowLocs(6,SampleDto.DATE_RESULTS_RECEIVED_SENT_TO_CLINICIAN) +
					fluidRowLocs(6,SampleDto.DATE_SPECIMEN_SENT_TO_LAB) +
					fluidRowLocs(SampleDto.SPECIMEN_SAVED_AND_PRESEVED_IN_ALCOHOL, SampleDto.SPECIMEN_SAVED_AND_PRESEVED_IN_ALCOHOL_WHY) +
					fluidRowLocs(SampleDto.SENT_FOR_CONFIRMATION_NATIONAL, SampleDto.SENT_FOR_CONFIRMATION_NATIONAL_DATE, SampleDto.SENT_FOR_CONFIRMATION_TO) +
					fluidRowLocs(SampleDto.DATE_RESULT_RECEIVED_NATIONAL, SampleDto.USE_OF_CLOTH_FILTER, SampleDto.FREQUENCY_OF_CHANGING_FILTERS) +
					fluidRowLocs(SampleDto.REMARKS) +
                    fluidRowLocs(6,SampleDto.SAMPLE_MATERIAL) +
					fluidRowLocs(SampleDto.FIELD_SAMPLE_ID, REFERRED_FROM_BUTTON_LOC) +
					fluidRowLocs("", SampleDto.SAMPLE_MATERIAL_TEXT) +
					fluidRowLocs(SampleDto.SAMPLING_REASON, SampleDto.SAMPLING_REASON_DETAILS) +
					fluidRowLocs(SampleDto.SAMPLE_SOURCE, "") +
					fluidRowLocs(6,SampleDto.OTHER_TYPE) +

					fluidRowLocs(SampleDto.SAMPLE_DISPATCH_MODE) +
					fluidRowLocs(6,SampleDto.SAMPLE_DISPATCH_DATE) +
					fluidRowLocs(6,SampleDto.LABORATORY_TYPE) +
					fluidRowLocs(DISTRICT_LABORATORY_HEADLINE_LOC) +
					fluidRowLocs(REGIONAL_LABORATORY_HEADLINE_LOC) +
					fluidRowLocs(REFERENCE_LABORATORY_HEADLINE_LOC) +
					locCss(VSPACE_TOP_3, SampleDto.LABORATORY_NAME) +

					fluidRowLocs(SampleDto.LABORATORY_TEST_PERFORMED, SampleDto.LABORATORY_TEST_PERFORMED_OTHER) +
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
					fluidRowLocs(6, SampleDto.LABORATORY_FINAL_RESULTS) +
					fluidRowLocs(SampleDto.LABORATORY_OBSERVATIONS) +
					fluidRowLocs(6, SampleDto.LABORATORY_DATE_RESULTS_SENT_HEALTH_FACILITY) +
					fluidRowLocs(6, SampleDto.LABORATORY_DATE_RESULTS_SENT_DSD) +

					//INFLUENZA
					loc(SampleDto.POSITIVE_VIRAL_CULTURE) +
					loc(SampleDto.POSITIVE_REAL_TIME) +
					loc(SampleDto.FOUR_FOLD_RISE) +
					fluidRowLocs(6,SampleDto.INFLUENZA_VIRUS) +
					loc(SampleDto.TREATMENT) +
					loc(SampleDto.STATE_TREATMENT_ADMINISTERED) +

					fluidRowLocs(6, SampleDto.LABORATORY_FINAL_CLASSIFICATION) +
					fluidRowLocs(SampleDto.DATE_SURVEILLANCE_SENT_RESULTS_TO_DISTRICT, SampleDto.DATE_FORM_SENT_TO_HIGHER_LEVEL, SampleDto.PERSON_COMPLETING_FORM) +

					loc(SAMPLE_MATERIAL_READ_HEADLINE_LOC) +
					loc(SampleDto.REQUESTED_SAMPLE_MATERIALS) +
//					locCss(VSPACE_TOP_3, SampleDto.PATHOGEN_TESTING_REQUESTED) +
					loc(PATHOGEN_TESTING_READ_HEADLINE_LOC) +
					loc(PATHOGEN_TESTING_INFO_LOC) +
					loc(SampleDto.REQUESTED_PATHOGEN_TESTS) +
					loc(SampleDto.REQUESTED_OTHER_PATHOGEN_TESTS) +
					loc(REQUESTED_PATHOGEN_TESTS_READ_LOC) +

//					locCss(VSPACE_TOP_3, SampleDto.ADDITIONAL_TESTING_REQUESTED) +
					loc(ADDITIONAL_TESTING_READ_HEADLINE_LOC) +
					loc(ADDITIONAL_TESTING_INFO_LOC) +
					//loc(SampleDto.REQUESTED_ADDITIONAL_TESTS) +
//					loc(SampleDto.REQUESTED_OTHER_ADDITIONAL_TESTS) +
					loc(REQUESTED_ADDITIONAL_TESTS_READ_LOC) +

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
					fluidRowLocs(6,SampleDto.AFP_FINAL_CLASSIFICATION) +

					locCss(VSPACE_TOP_3, SampleDto.SHIPPED) +
					fluidRowLocs(SampleDto.SHIPMENT_DATE, SampleDto.SHIPMENT_DETAILS) +
					locCss(VSPACE_TOP_3, SampleDto.RECEIVED) +
					fluidRowLocs(SampleDto.RECEIVED_DATE, SampleDto.LABORATORY_NUMBER) +
					fluidRowLocs(SampleDto.LABORATORY_SAMPLE_CONTAINER_RECEIVED, SampleDto.LABORATORY_SAMPLE_CONTAINER_OTHER) +
					fluidRowLocs(6,SampleDto.LAB_SAMPLE_ID) +
					fluidRowLocs(SampleDto.SPECIMEN_CONDITION, SampleDto.NO_TEST_POSSIBLE_REASON) +

					fluidRowLocs(6,SampleDto.LABORATORY_APPEARANCE_OF_CSF) +
					fluidRowLocs(SampleDto.COMMENT) +

					fluidRowLocs(6,SampleDto.PATHOGEN_TEST_RESULT) +
					fluidRowLocs(6,SampleDto.IPSAMPLESENT) +
					fluidRowLocs(SampleDto.IPSAMPLE_TEST_RESULTS) +
					fluidRowLocs(SampleDto.SELECTED_RESULT_IGM, SampleDto.SELECTED_RESULT_IGM_DATE)+
					fluidRowLocs(SampleDto.SELECTED_RESULT_PCR, SampleDto.SELECTED_RESULT_PCR_DATE)+
					fluidRowLocs(SampleDto.SELECTED_RESULT_PRNT, SampleDto.INPUT_VALUE_PRNT, SampleDto.SELECTED_RESULT_PRNT_DATE)+

					fluidRowLocs(CaseDataDto.DELETION_REASON) +
					fluidRowLocs(CaseDataDto.OTHER_DELETION_REASON);


    protected static final String GUINEA_WORM_LAYOUT =
				fluidRowLocs(SampleDto.UUID, REPORT_INFO_LABEL_LOC) +
                fluidRowLocs(SampleDto.SAMPLE_DATE_TIME) +
				fluidRowLocs(SampleDto.LAB_LOCAL) +
				fluidRowLocs(6, SampleDto.LAB_LOCAL_DETAILS) +
				fluidRowLocs(SampleDto.LAB, SampleDto.LAB_DETAILS) +
//				fluidRowLocs(6, SampleDto.SAMPLE_MATERIAL) +
				loc(HEADING_SPECIMEN_HANDLING) +
				fluidRowLocs(SampleDto.SPECIMEN_SAVED_AND_PRESEVED_IN_ALCOHOL) +
				fluidRowLocs(SampleDto.SPECIMEN_SAVED_AND_PRESEVED_IN_ALCOHOL_WHY) +
				fluidRowLocs(SampleDto.DATE_SPECIMEN_SENT_TO_REGION, SampleDto.DATE_SPECIMEN_RECEIVED_AT_REGION) +
				fluidRowLocs(8, SampleDto.NAME_OF_PERSON_WHO_RECEIVED_SPECIMEN_AT_REGION) +
				fluidRowLocs(SampleDto.DATE_SPECIMEN_SENT_TO_NATIONAL, SampleDto.DATE_SPECIMEN_RECEIVED_AT_NATIONAL) +
				fluidRowLocs(8, SampleDto.NAME_OF_PERSON_WHO_RECEIVED_SPECIMEN_AT_NATIONAL) +
				loc(NATIONAL_SECRETARIAT_ONLY) +
				fluidRowLocs(SampleDto.SENT_FOR_CONFIRMATION_NATIONAL, SampleDto.SENT_FOR_CONFIRMATION_NATIONAL_DATE, SampleDto.SENT_FOR_CONFIRMATION_TO) +
				fluidRowLocs(6, SampleDto.DATE_RESULT_RECEIVED_NATIONAL) +
				loc(OTHER_INFORMATION_HEADLINE_LOC) +
				fluidRowLocs(SampleDto.USE_OF_CLOTH_FILTER, SampleDto.FREQUENCY_OF_CHANGING_FILTERS) +
				fluidRowLocs(SampleDto.REMARKS) +
//				locCss(VSPACE_TOP_3, SampleDto.SHIPPED) +
//				fluidRowLocs(SampleDto.SHIPMENT_DATE, SampleDto.SHIPMENT_DETAILS) +
//				locCss(VSPACE_TOP_3, SampleDto.RECEIVED) +
//				fluidRowLocs(SampleDto.RECEIVED_DATE, SampleDto.SPECIMEN_CONDITION) +
				fluidRowLocs(SampleDto.CONFIRMED_AS_GUINEA_WORM) +
				fluidRowLocs(SampleDto.PATHOGEN_TEST_RESULT);
				
	protected static final String MEASLES_LAYOUT =
			fluidRowLocs(SampleDto.UUID, REPORT_INFO_LABEL_LOC) +
			fluidRowLocs(SampleDto.SAMPLE_DATE_TIME) +
			fluidRowLocs(SampleDto.LAB, SampleDto.LAB_DETAILS) +
			fluidRowLocs(6, SampleDto.SAMPLE_MATERIAL) +
			fluidRowLocs(SampleDto.DATE_FORM_SENT_TO_HIGHER_LEVEL, SampleDto.PERSON_COMPLETING_FORM) +
			locCss(VSPACE_TOP_3, SampleDto.SHIPPED) +
			fluidRowLocs(SampleDto.SHIPMENT_DATE, SampleDto.SHIPMENT_DETAILS) +
			locCss(VSPACE_TOP_3, SampleDto.RECEIVED) +
			fluidRowLocs(6, SampleDto.RECEIVED_DATE) +
			fluidRowLocs(SampleDto.PATHOGEN_TEST_RESULT);
			
	protected static final String CHOLERA_HTML_LAYOUT =
					fluidRowLocs(SampleDto.UUID, REPORT_INFO_LABEL_LOC) +
					fluidRowLocs(SampleDto.SAMPLE_DATE_TIME) +
					fluidRowLocs(SampleDto.LAB, SampleDto.LAB_DETAILS) +
							fluidRowLocs(6, SampleDto.SAMPLE_MATERIAL, 6, SampleDto.SAMPLE_MATERIAL_TEXT) +
							fluidRowLocs(6, SampleDto.FIELD_SAMPLE_ID) +
					fluidRowLocs(SampleDto.PATHOGEN_TESTING_REQUESTED) +
					loc(SampleDto.REQUESTED_PATHOGEN_TESTS) +
					loc(SampleDto.REQUESTED_OTHER_PATHOGEN_TESTS) +
					locCss(VSPACE_TOP_3, SampleDto.SHIPPED) +
					fluidRowLocs(SampleDto.SHIPMENT_DATE, SampleDto.SHIPMENT_DETAILS) +
					locCss(VSPACE_TOP_3, SampleDto.RECEIVED) +
					fluidRowLocs(6, SampleDto.RECEIVED_DATE, 6, SampleDto.SPECIMEN_CONDITION) +
					fluidRowLocs("", SampleDto.PATHOGEN_TEST_RESULT);



    //@formatter:on

	protected AbstractSampleForm(Class<SampleDto> type, String propertyI18nPrefix, Disease disease, UiFieldAccessCheckers fieldAccessCheckers) {
		super(
			type,
			propertyI18nPrefix,
			true,
			FieldVisibilityCheckers.withDisease(disease).andWithCountry(FacadeProvider.getConfigFacade().getCountryLocale()),
			fieldAccessCheckers,
			disease);
	}

    protected void addCommonFields() {
    TextField labDetails;

    TextField laboratoryFinalResults;
  	ComboBox lab;

		disease = getCaseDisease();

//	-- dateSurveillanceSentResultsToDistrict, dateFormSentToHigherLevel, personCompletingForm
		dateSurveillanceSentResultsToDistrict = addField(SampleDto.DATE_SURVEILLANCE_SENT_RESULTS_TO_DISTRICT, DateField.class);
		dateFormSentToHigherLevel = addField(SampleDto.DATE_FORM_SENT_TO_HIGHER_LEVEL, DateField.class);
		personCompletingForm = addField(SampleDto.PERSON_COMPLETING_FORM, TextField.class);
		setVisible(false, SampleDto.DATE_SURVEILLANCE_SENT_RESULTS_TO_DISTRICT, SampleDto.DATE_FORM_SENT_TO_HIGHER_LEVEL, SampleDto.PERSON_COMPLETING_FORM);

		final NullableOptionGroup samplePurpose = addField(SampleDto.SAMPLE_PURPOSE, NullableOptionGroup.class);
		addField(SampleDto.UUID).setReadOnly(true);
		addField(SampleDto.REPORTING_USER).setReadOnly(true);
		samplePurpose.addValueChangeListener(e -> updateRequestedTestFields());
		labSampleId = addField(SampleDto.LAB_SAMPLE_ID, TextField.class);
		labSampleId.setVisible(false);
		hasSampleBeenCollected = addField(SampleDto.HAS_SAMPLE_BEEN_COLLECTED, NullableOptionGroup.class);
		sampleDateTimeField = addField(SampleDto.SAMPLE_DATE_TIME, DateTimeField.class);
		sampleDateTimeField.setInvalidCommitted(false);
		suspectedDisease = addField(SampleDto.SUSPECTED_DISEASE);
		dateLabReceivedSpecimen = addField(SampleDto.DATE_LAB_RECEIVED_SPECIMEN);
		hasSampleBeenCollected.setVisible(false);

        addField(SampleDto.SAMPLE_MATERIAL_TEXT, TextField.class);
        sampleSource = addField(SampleDto.SAMPLE_SOURCE, ComboBox.class);
        addField(SampleDto.FIELD_SAMPLE_ID, TextField.class);
        addDateField(SampleDto.SHIPMENT_DATE, DateField.class, 7);
        addField(SampleDto.SHIPMENT_DETAILS, TextField.class);
        sampleReceivedDate = addField(SampleDto.RECEIVED_DATE, DateTimeField.class);

        addField(SampleDto.LABORATORY_NUMBER, TextField.class);
        laboratorySampleContainerReceived = addField(SampleDto.LABORATORY_SAMPLE_CONTAINER_RECEIVED, OptionGroup.class);
        laboratorySampleContainerReceived.setWidthUndefined();
        laboratorySampleContainerOther = addField(SampleDto.LABORATORY_SAMPLE_CONTAINER_OTHER, TextField.class);
		addField(SampleDto.LABORATORY_APPEARANCE_OF_CSF, NullableOptionGroup.class);

        setVisible(false, SampleDto.LABORATORY_NUMBER, SampleDto.LABORATORY_SAMPLE_CONTAINER_RECEIVED, SampleDto.LABORATORY_SAMPLE_CONTAINER_OTHER, SampleDto.LABORATORY_APPEARANCE_OF_CSF);

		laboratoryDateResultsSentDSD = addField(SampleDto.LABORATORY_DATE_RESULTS_SENT_DSD, DateField.class);
		laboratoryDateResultsSentDSD.setVisible(false);
		dateSampleSentToLab = addField(SampleDto.DATE_SAMPLE_SENT_TO_LAB, DateField.class);
		dateSampleSentToLab.setVisible(false);

		disease = getCaseDisease();
		lab = addInfrastructureField(SampleDto.LAB);
		List<FacilityReferenceDto> allActiveLabs;
		disease = getCaseDisease();
		if (disease != null) {
			allActiveLabs = FacadeProvider.getFacilityFacade().getAllActiveFacilityByDisease(disease.getName());
			if (allActiveLabs.isEmpty()) {
				allActiveLabs = FacadeProvider.getFacilityFacade().getAllActiveLaboratories(true);
			}
		} else {
			allActiveLabs = FacadeProvider.getFacilityFacade().getAllActiveLaboratories(true);
		}
		lab.addItems(allActiveLabs);
		labDetails = addField(SampleDto.LAB_DETAILS, TextField.class);
		labLocation = addField(SampleDto.LAB_LOCATION, TextField.class);
		labDetails.setVisible(false);
		lab.addValueChangeListener(event -> updateLabDetailsVisibility(labDetails, event));

        sampleSpecimenCondition = addField(SampleDto.SPECIMEN_CONDITION, ComboBox.class);
        addField(SampleDto.NO_TEST_POSSIBLE_REASON, TextField.class);
        TextArea comment = addField(SampleDto.COMMENT, TextArea.class);
        comment.setRows(4);
        comment.setDescription(
                I18nProperties.getPrefixDescription(SampleDto.I18N_PREFIX, SampleDto.COMMENT, "") + "\n"
                        + I18nProperties.getDescription(Descriptions.descGdpr));
		addField(SampleDto.SHIPPED, CheckBox.class);
        sampleReceived = addField(SampleDto.RECEIVED, CheckBox.class);

        sampleSpecimenCondition.setVisible(false);

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


        laboratorySampleCondition = addField(SampleDto.LABORATORY_SAMPLE_CONDITION, OptionGroup.class);
        laboratoryFinalResults = addField(SampleDto.LABORATORY_FINAL_RESULTS, TextField.class);
        laboratoryFinalResults.setVisible(false);
        dateFormSentToDistrict = addField(SampleDto.DATE_FORM_SENT_TO_DISTRICT, DateField.class);
        dateFormReceivedAtDistrict = addField(SampleDto.DATE_FORM_RECEIVED_AT_DISTRICT, DateField.class);
        dateResultsSentToClinician = addField(SampleDto.DATE_RESULTS_RECEIVED_SENT_TO_CLINICIAN, DateField.class);
        dateSpecimenSentToLab = addField(SampleDto.DATE_SPECIMEN_SENT_TO_LAB, DateField.class);
        dateSpecimenSentToLab.setVisible(false);
        addField(SampleDto.DELETION_REASON);
        addField(SampleDto.OTHER_DELETION_REASON, TextArea.class).setRows(3);
        setVisible(false, SampleDto.DELETION_REASON, SampleDto.OTHER_DELETION_REASON);

		addField(SampleDto.SPECIMEN_SAVED_AND_PRESEVED_IN_ALCOHOL, NullableOptionGroup.class);
		addField(SampleDto.SPECIMEN_SAVED_AND_PRESEVED_IN_ALCOHOL_WHY, TextArea.class).setRows(4);
		addField(SampleDto.SENT_FOR_CONFIRMATION_NATIONAL, NullableOptionGroup.class);
		addDateField(SampleDto.SENT_FOR_CONFIRMATION_NATIONAL_DATE, DateField.class, 7);
		setVisible(false, SampleDto.SENT_FOR_CONFIRMATION_NATIONAL_DATE);
		addField(SampleDto.SENT_FOR_CONFIRMATION_TO, TextField.class);
		addDateField(SampleDto.DATE_RESULT_RECEIVED_NATIONAL, DateField.class, 7);
		addField(SampleDto.USE_OF_CLOTH_FILTER, NullableOptionGroup.class);
		addField(SampleDto.FREQUENCY_OF_CHANGING_FILTERS, ComboBox.class);
		addField(SampleDto.REMARKS, TextArea.class).setRows(3);
		setVisible(false,
				SampleDto.SPECIMEN_SAVED_AND_PRESEVED_IN_ALCOHOL,
				SampleDto.SPECIMEN_SAVED_AND_PRESEVED_IN_ALCOHOL_WHY,
				SampleDto.SENT_FOR_CONFIRMATION_NATIONAL,
				SampleDto.SENT_FOR_CONFIRMATION_TO,
				SampleDto.DATE_RESULT_RECEIVED_NATIONAL,
				SampleDto.USE_OF_CLOTH_FILTER,
				SampleDto.FREQUENCY_OF_CHANGING_FILTERS,
				SampleDto.REMARKS);

    }

    protected void defaultValueChangeListener() {

		final NullableOptionGroup samplePurposeField = getField(SampleDto.SAMPLE_PURPOSE);
		final Field<?> receivedField = getField(SampleDto.RECEIVED);
		final Field<?> shippedField = getField(SampleDto.SHIPPED);

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

        samplePurposeField.setRequired(true);
        sampleMaterialComboBox = addField(SampleDto.SAMPLE_MATERIAL);
        UserReferenceDto reportingUser = getValue().getReportingUser();

        if (disease == Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS) {
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
                getField(SampleDto.SHIPMENT_DETAILS).setEnabled(false);
                getField(SampleDto.SAMPLE_SOURCE).setEnabled(false);
            }
        } else {
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

        if (disease == Disease.CSM) {
            laboratorySampleContainerOther.setVisible(false);
            FieldHelper.setVisibleWhen(
                    getFieldGroup(),
                    Arrays.asList(SampleDto.RECEIVED_DATE, SampleDto.LAB_SAMPLE_ID, SampleDto.SPECIMEN_CONDITION, SampleDto.LABORATORY_NUMBER, SampleDto.LABORATORY_SAMPLE_CONTAINER_RECEIVED, SampleDto.LABORATORY_APPEARANCE_OF_CSF),
                    SampleDto.RECEIVED,
                    Arrays.asList(true),
                    true);
            FieldHelper.setEnabledWhen(
                    getFieldGroup(),
                    receivedField,
                    Arrays.asList(true),
                    Arrays.asList(SampleDto.RECEIVED_DATE, SampleDto.LAB_SAMPLE_ID, SampleDto.SPECIMEN_CONDITION, SampleDto.LABORATORY_NUMBER, SampleDto.LABORATORY_SAMPLE_CONTAINER_RECEIVED, SampleDto.LABORATORY_APPEARANCE_OF_CSF),
                    true);
            FieldHelper.setVisibleWhen(laboratorySampleContainerReceived, Arrays.asList(laboratorySampleContainerOther), Arrays.asList(SampleContainerUsed.OTHER), true);
        }
		else if (disease == Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS) {
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
        } else {
            FieldHelper.setVisibleWhen(
                    getFieldGroup(),
					Arrays.asList(SampleDto.RECEIVED_DATE),
                    SampleDto.RECEIVED,
					Arrays.asList(true),
                    true);
            FieldHelper.setEnabledWhen(
                    getFieldGroup(),
                    receivedField,
                    Arrays.asList(true),
                    Arrays.asList(SampleDto.RECEIVED_DATE),
                    true);
        }

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
				case CORONAVIRUS:
					handleCoronaVirus();
					break;
				case FOODBORNE_ILLNESS:
					handleFBI();
                    break;
				case IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS:
					handleIDSR();
					break;
				case GUINEA_WORM:
					handleGuineaWorm();
				case CHOLERA:
					handleCholera();
					break;
				case MONKEYPOX:
					handleMpox();
					break;
				default:
			}
		hideFieldsForSelectedDisease(disease);
        setSampleMaterialTypesForDisease(disease);
    }


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
        final DateTimeField receivedDate = getField(SampleDto.RECEIVED_DATE);

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
        validatedFields.forEach(field -> field.addValueChangeListener(r -> validatedFields.forEach(otherField -> otherField.setValidationVisible(!otherField.isValid()))));
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

				if (disease != Disease.GUINEA_WORM) {
						FieldHelper.setVisibleWhen(
								getFieldGroup(),
								Arrays.asList(SampleDto.LAB),
								SampleDto.SAMPLE_PURPOSE,
								Arrays.asList(SamplePurpose.EXTERNAL, null),
								true);
				} else {

					FieldHelper.setVisibleWhen(
							getFieldGroup(),
							Arrays.asList(SampleDto.LAB_LOCAL_DETAILS),
							SampleDto.LAB_LOCAL,
							Arrays.asList(LabLocal.OUTSIDE_COUNTRY),
							true);

					FieldHelper.setVisibleWhen(
							getFieldGroup(),
							Arrays.asList(SampleDto.LAB),
							SampleDto.LAB_LOCAL,
							Arrays.asList(LabLocal.IN_COUNTRY),
							true);
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

    protected void initializeRequestedTestFields() {

        // Information texts for users that can edit the requested tests
        Label requestedPathogenInfoLabel = new Label(I18nProperties.getString(Strings.infoSamplePathogenTesting));
        getContent().addComponent(requestedPathogenInfoLabel, PATHOGEN_TESTING_INFO_LOC);
        Label requestedAdditionalInfoLabel = new Label(I18nProperties.getString(Strings.infoSampleAdditionalTesting));
        getContent().addComponent(requestedAdditionalInfoLabel, ADDITIONAL_TESTING_INFO_LOC);

        // Yes/No fields for requesting pathogen/additional tests
        pathogenTestingRequestedField = addField(SampleDto.PATHOGEN_TESTING_REQUESTED, CheckBox.class);
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

        ipSampleTestResults = addField(SampleDto.IPSAMPLE_TEST_RESULTS, OptionGroup.class);
        CssStyles.style(ipSampleTestResults, CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
        ipSampleTestResults.setMultiSelect(true);

        ipSampleTestResults.setVisible(false);

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

		Label nationalSecretariatOnly = new Label(I18nProperties.getString(Strings.headingNationalSecretariatOnly));
		CssStyles.style(nationalSecretariatOnly, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_TOP_5, H3);
		getContent().addComponent(nationalSecretariatOnly, NATIONAL_SECRETARIAT_ONLY);

		Label otherInformation = new Label(I18nProperties.getString(Strings.headingOtherInformation));
		CssStyles.style(otherInformation, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_TOP_5, H3);
		getContent().addComponent(otherInformation, OTHER_INFORMATION_HEADLINE_LOC);

		Label headingSpecimenHandling = new Label(I18nProperties.getString(Strings.headingSpecimenHandling));
		CssStyles.style(headingSpecimenHandling, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_TOP_5, H3);
		getContent().addComponent(headingSpecimenHandling, HEADING_SPECIMEN_HANDLING);

        updateRequestedTestFields();


    }
    CheckBox sampleMaterialRequestedField = addField(SampleDto.SAMPLE_MATERIAL_REQUESTED, CheckBox.class);
    Field<?> sampleMaterialTestingField = getField(SampleDto.SAMPLE_MATERIAL_REQUESTED);

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

        handleDiseaseField(Disease.NEW_INFLUENZA, Disease.CSM, Disease.SARI, Disease.FOODBORNE_ILLNESS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, Disease.AHF, Disease.MONKEYPOX);

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


    private void disableField(String field) {
        setVisible(false, field);
    }

    private void handleCSM() {

        OptionGroup csfSampleCollected = addField(SampleDto.CSF_SAMPLE_COLLECTED, OptionGroup.class);
        ComboBox csfReason = addField(SampleDto.CSF_REASON, ComboBox.class);
        addField(SampleDto.APPEARANCE_OF_CSF, NullableOptionGroup.class);
        addField(SampleDto.INOCULATION_TIME_TRANSPORT_MEDIA, DateField.class);
        OptionGroup sampleSentToLab = addField(SampleDto.SAMPLE_SENT_TO_LAB, OptionGroup.class);
        TextField reasonNotSent = addField(SampleDto.REASON_NOT_SENT_TO_LAB, TextField.class);
        NullableOptionGroup sampleContainerUsed = addField(SampleDto.SAMPLE_CONTAINER_USED, NullableOptionGroup.class);
        TextField otherContainer = addField(SampleDto.CONTAINER_OTHER, TextField.class);
        addField(SampleDto.RDT_PERFORMED, OptionGroup.class);
        addField(SampleDto.RDT_RESULTS, TextField.class);
        addField(SampleDto.DISTRICT_NOTIFICATION_DATE, DateField.class);
        addField(SampleDto.NAME_OF_PERSON, TextField.class);
        addField(SampleDto.TEL_NUMBER, TextField.class);

        addField(SampleDto.DATE_FORM_SENT_TO_REGION, DateField.class);
        addField(SampleDto.DATE_FORM_RECEIVED_AT_REGION, DateField.class);
        addField(SampleDto.DATE_FORM_SENT_TO_NATIONAL, DateField.class);
        addField(SampleDto.DATE_FORM_RECEIVED_AT_NATIONAL, DateField.class);

		setFieldsVisible(false,	csfReason, reasonNotSent, dateSampleSentToLab, sampleDateTimeField, suspectedDisease, labLocation, dateSpecimenSentToLab, dateLabReceivedSpecimen, dateResultsSentToClinician, sampleMaterialComboBox, otherContainer);

		setVisible(true, SampleDto.DATE_SAMPLE_SENT_TO_LAB);

        FieldHelper
                .setVisibleWhen(csfSampleCollected, Arrays.asList(sampleDateTimeField), Arrays.asList(YesNo.YES), true);
        FieldHelper
                .setVisibleWhen(sampleSentToLab, Arrays.asList(reasonNotSent), Arrays.asList(YesNo.NO), true);
        FieldHelper
                .setVisibleWhen(sampleSentToLab, Arrays.asList(dateSampleSentToLab), Arrays.asList(YesNo.YES), true);
        FieldHelper
                .setVisibleWhen(csfSampleCollected, Arrays.asList(csfReason), Arrays.asList(YesNo.NO), true);
        FieldHelper
                .setVisibleWhen(sampleContainerUsed, Arrays.asList(otherContainer), Arrays.asList(SampleContainerUsed.OTHER), true);

    }

    private void handleAFP() {

        Label stoolSpecimenCollection = new Label(I18nProperties.getString(Strings.headingStoolSpecimenCollection));
        CssStyles.style(stoolSpecimenCollection, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_4);
        getContent().addComponent(stoolSpecimenCollection, STOOL_SPECIMEN_COLLECTION_HEADLINE_LOC);

        Label stoolSpecimenResults = new Label(I18nProperties.getString(Strings.headingStoolSpecimenResults));
        CssStyles.style(stoolSpecimenResults, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_4);
        getContent().addComponent(stoolSpecimenResults, STOOL_SPECIMEN_RESULTS_HEADLINE_LOC);

        Label followUpExamination = new Label(I18nProperties.getString(Strings.headingFollowUpExamination));
        CssStyles.style(followUpExamination, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_4);
        getContent().addComponent(followUpExamination, FOLLOW_UP_EXAMINATION_HEADLINE_LOC);

        addField(SampleDto.DATE_FIRST_SPECIMEN, DateField.class);
		addField(SampleDto.DATE_SECOND_SPECIMEN, DateField.class);
        addField(SampleDto.DATE_SPECIMEN_SENT_NATIONAL_LEVEL, DateField.class);
		addField(SampleDto.DATE_SPECIMEN_RECEIVED_NATIONAL_LEVEL, DateField.class);
        addField(SampleDto.DATE_SPECIMEN_SENT_INTERCOUNTY_NATLAB, DateField.class);
        addField(SampleDto.DATE_SPECIMEN_RECEIVED_INTERCOUNTY_NATLAB, DateField.class);

        addField(SampleDto.STATUS_SPECIMEN_RECEPTION_AT_LAB, OptionGroup.class);
        addField(SampleDto.DATE_COMBINED_CELL_CULTURE_RESULTS, DateField.class);
        addField(SampleDto.W1, OptionGroup.class);
        addField(SampleDto.W2, OptionGroup.class);
        addField(SampleDto.W3, OptionGroup.class);
        addField(SampleDto.SL1, OptionGroup.class);
        addField(SampleDto.SL2, OptionGroup.class);
        addField(SampleDto.SL3, OptionGroup.class);
        addField(SampleDto.DISCORDANT, NullableOptionGroup.class);

        addField(SampleDto.DATE_FOLLOWUP_EXAM, DateField.class);
        NullableOptionGroup residualAnalysis = addField(SampleDto.RESIDUAL_ANALYSIS, NullableOptionGroup.class);

		List<InjectionSite> paralysisSite = Arrays.asList(InjectionSite.LEFT_ARM, InjectionSite.LEFT_LEG, InjectionSite.RIGHT_ARM, InjectionSite.RIGHT_LEG);
		FieldHelper.updateEnumData(residualAnalysis, paralysisSite);

        addField(SampleDto.RESULT_EXAM, ComboBox.class);
        addField(SampleDto.DATE_SENT_NATIONAL_REG_LAB, DateField.class);
		addField(SampleDto.DATE_DIFFERENTIATION_SENT_EPI, DateField.class);
        addField(SampleDto.DATE_DIFFERENTIATION_RECEIVED_EPI, DateField.class);
        addField(SampleDto.DATE_ISOLATE_SENT_SEQUENCING, DateField.class);
        addField(SampleDto.DATE_SEQ_RESULTS_SENT_PROGRAM, DateField.class);
        addField(SampleDto.FINAL_LAB_RESULTS, NullableOptionGroup.class);
        addField(SampleDto.IMMUNOCOMPROMISED_STATUS_SUSPECTED, NullableOptionGroup.class);
        ComboBox afpFinalClassification = addField(SampleDto.AFP_FINAL_CLASSIFICATION, ComboBox.class);
		FieldHelper.updateEnumData(afpFinalClassification, FinalClassification.AFP_CLASSIFICATION);

        setRequired(false, SampleDto.SAMPLE_PURPOSE);
        hideCommonProperties();

    }

    private void handleAHF() {
        hideCommonProperties();
		sampleDateTimeField.setVisible(false);

        List<SampleMaterial> validValues = Arrays.asList(SampleMaterial.WHOLE_BLOOD, SampleMaterial.PLASMA_SERUM, SampleMaterial.SALIVA, SampleMaterial.URINE, SampleMaterial.BIOPSY, SampleMaterial.CARDIAC, SampleMaterial.BLOOD_ANTI_COAGULANT, SampleMaterial.OTHER);
        FieldHelper.updateEnumData(sampleMaterialComboBox, validValues);

        hasSampleBeenCollected.setVisible(true);
        FieldHelper.setVisibleWhen(hasSampleBeenCollected, Arrays.asList(sampleDateTimeField), Arrays.asList(YesNo.YES), true);

    }

    private void handleYellowFever() {
        TextField inputValuePrnt;
        ComboBox ipSampleSent;
        NullableOptionGroup selectedResultIGM;
        DateField selectedResultIGMDate;
        NullableOptionGroup selectedResultPcr;
        DateField selectedResultPcrDate;
        NullableOptionGroup selectedResultPrnt;
        DateField selectedResultPrntDate;

        addSampleDispatchFields();

        setRequired(false, SampleDto.SAMPLE_DATE_TIME, SampleDto.SAMPLE_MATERIAL);

        hideCommonProperties();
        sampleMaterialComboBox.setVisible(false);

        setVisible(true, SampleDto.REQUESTED_SAMPLE_MATERIALS);
        testResultField.setVisible(true);

        ipSampleSent = addField(SampleDto.IPSAMPLESENT, ComboBox.class);

        sampleReceived.addValueChangeListener((ValueChangeListener) valueChangeEvent -> FieldHelper.setVisibleWhen(sampleReceived, Arrays.asList(sampleReceivedDate, labSampleId, sampleSpecimenCondition), Arrays.asList(Boolean.TRUE), true));

        if(sampleReceived.getValue().equals(Boolean.TRUE)){
            FieldHelper.setVisibleWhen(sampleReceived, Arrays.asList(sampleReceivedDate, labSampleId, sampleSpecimenCondition), Arrays.asList(Boolean.TRUE), true);
        }

        List<IpSampleTestType> values =  Arrays.stream(IpSampleTestType.values())
                .filter(c -> fieldVisibilityCheckers.isVisible(IpSampleTestType.class, c.name()))
                .collect(Collectors.toList());

        ipSampleTestResults.addItems(values);

        selectedResultIGM = addField(SampleDto.SELECTED_RESULT_IGM, NullableOptionGroup.class);
        selectedResultIGMDate = addField(SampleDto.SELECTED_RESULT_IGM_DATE, DateField.class);

        selectedResultPrnt = addField(SampleDto.SELECTED_RESULT_PRNT, NullableOptionGroup.class);
        inputValuePrnt = addField(SampleDto.INPUT_VALUE_PRNT, TextField.class);
        selectedResultPrntDate = addField(SampleDto.SELECTED_RESULT_PRNT_DATE, DateField.class);

        selectedResultPcr = addField(SampleDto.SELECTED_RESULT_PCR, NullableOptionGroup.class);
        selectedResultPcrDate = addField(SampleDto.SELECTED_RESULT_PCR_DATE, DateField.class);

        setVisible(false, SampleDto.SELECTED_RESULT_IGM, SampleDto.SELECTED_RESULT_IGM_DATE, SampleDto.SELECTED_RESULT_PRNT, SampleDto.SELECTED_RESULT_PRNT_DATE, SampleDto.SELECTED_RESULT_PCR, SampleDto.SELECTED_RESULT_PCR_DATE, SampleDto.INPUT_VALUE_PRNT);

        FieldHelper.setVisibleWhen(ipSampleSent, Arrays.asList(ipSampleTestResults), Arrays.asList(YesNo.YES), true);

        //Event Listener for when sample result type is selected
        ipSampleTestResults.addValueChangeListener(event -> {
            Set<IpSampleTestType> selectedResults = (Set<IpSampleTestType>) event.getProperty().getValue();

            boolean elisa = selectedResults != null && selectedResults.contains(IpSampleTestType.IGM);
            selectedResultIGM.setVisible(elisa);
            selectedResultIGMDate.setVisible(elisa);

            boolean pcr = selectedResults != null && selectedResults.contains(IpSampleTestType.PCR);
            selectedResultPcr.setVisible(pcr);
            selectedResultPcr.removeItem(PosNegEq.EQU);
            selectedResultPcrDate.setVisible(pcr);

            boolean prnt = selectedResults != null && selectedResults.contains(IpSampleTestType.PRNT);
            selectedResultPrnt.setVisible(prnt);
            selectedResultPrnt.removeItem(PosNegEq.EQU);
            selectedResultPrntDate.setVisible(prnt);

        });

// Check the value on load and set visibility accordingly for ipSampleTestResults
        Set<IpSampleTestType> selectedResults = (Set<IpSampleTestType>) ipSampleTestResults.getValue();

        boolean elisa = selectedResults != null && selectedResults.contains(IpSampleTestType.IGM);
        selectedResultIGM.setVisible(elisa);
        selectedResultIGMDate.setVisible(elisa);

        boolean pcr = selectedResults != null && selectedResults.contains(IpSampleTestType.PCR);
        selectedResultPcr.setVisible(pcr);
        selectedResultPcr.removeItem(PosNegEq.EQU);
        selectedResultPcrDate.setVisible(pcr);

        boolean prnt = selectedResults != null && selectedResults.contains(IpSampleTestType.PRNT);
        selectedResultPrnt.setVisible(prnt);
        selectedResultPrnt.removeItem(PosNegEq.EQU);
        selectedResultPrntDate.setVisible(prnt);

        FieldHelper.setVisibleWhen(selectedResultPrnt, Arrays.asList(inputValuePrnt), Arrays.asList(PosNegEq.POSITIVE), true);


    }

    private void handleNewInfluenza() {

        hideCommonProperties();
        pathogenTestingRequestedField.setVisible(false);

        List<SampleMaterial> validValues = Arrays.asList(SampleMaterial.NASOPHARYNGEAL_SWAB, SampleMaterial.OROPHARYNGEAL_SWAB, SampleMaterial.ORO_NASO, SampleMaterial.SERA, SampleMaterial.PLASMA, SampleMaterial.OTHER);
        FieldHelper.updateEnumData(sampleMaterialComboBox, validValues);

        TextField otherType = addField(SampleDto.OTHER_TYPE, TextField.class);
        otherType.setVisible(false);
        otherType.setCaption("Specify for other type");

        sampleMaterialComboBox.addValueChangeListener((ValueChangeListener) valueChangeEvent -> otherType.setVisible(sampleMaterialComboBox.getValue() != null && sampleMaterialComboBox.getValue() == SampleMaterial.OTHER));
    }


    private void handleFBI() {
        setRequired(false, SampleDto.SAMPLE_DATE_TIME, SampleDto.SAMPLE_MATERIAL);
    }

	public void handleCholera() {
		List<SampleMaterial> choleraSampleMaterials = Arrays.asList(SampleMaterial.getCholeraMateriealTypes());
		FieldHelper.updateEnumData(sampleMaterialComboBox, choleraSampleMaterials);

		FieldHelper
				.setVisibleWhen(getFieldGroup(), SampleDto.SAMPLE_MATERIAL_TEXT, SampleDto.SAMPLE_MATERIAL, Arrays.asList(SampleMaterial.OTHER), true);

		List<PathogenTestType> choleraPathogenTests = PathogenTestType.getCholeraPathogenTests();
		Arrays.stream(PathogenTestType.values())
				.filter(pathogenTestType -> !choleraPathogenTests.contains(pathogenTestType))
				.forEach(pathogenTestType -> requestedPathogenTestsField.removeItem(pathogenTestType));

		setVisible(true, SampleDto.PATHOGEN_TESTING_REQUESTED);
		FieldHelper.setVisibleWhen(
				getFieldGroup(),
				Arrays.asList(SampleDto.REQUESTED_PATHOGEN_TESTS),
				SampleDto.PATHOGEN_TESTING_REQUESTED,
				Arrays.asList(true),
				true
		);

		FieldHelper.setVisibleWhen(
				getFieldGroup(),
				Arrays.asList(SampleDto.REQUESTED_OTHER_PATHOGEN_TESTS),
				SampleDto.PATHOGEN_TESTING_REQUESTED,
				Arrays.asList(PathogenTestType.OTHER),
				true
		);

	}


    private void handleIDSR() {

        List<SampleMaterial> validValues = Arrays.asList(SampleMaterial.WHOLE_BLOOD, SampleMaterial.PLASMA, SampleMaterial.SERUM, SampleMaterial.ASPIRATE, SampleMaterial.CEREBROSPINAL_FLUID, SampleMaterial.PUS, SampleMaterial.SALIVA, SampleMaterial.BIOPSY, SampleMaterial.STOOL, SampleMaterial.URETHRAL, SampleMaterial.URINE, SampleMaterial.SPUTUM, SampleMaterial.FOOD_WATER);
        FieldHelper.updateEnumData(sampleMaterialComboBox, validValues);

        suspectedDisease.setRequired(true);
		sampleDateTimeField.setRequired(true);

    }

    private void handleMpox(){
        hideCommonProperties();
        sampleMaterialComboBox.setVisible(false);

        setVisible(true, SampleDto.REQUESTED_SAMPLE_MATERIALS);

    }

    private void addSampleDispatchFields() {
        OptionGroup outcome = new OptionGroup("Sample Dispatch Modes");

        for (SampleDispatchMode sampleDis : SampleDispatchMode.values()) {
            if (sampleDis == SampleDispatchMode.NATIONAL_LAB || sampleDis == SampleDispatchMode.REGIONAL_COLDROOM || sampleDis == SampleDispatchMode.NATIONAL_BY_DISTRICT) {
                outcome.addItem(sampleDis);
            }
        }

    addField(SampleDto.SAMPLE_DISPATCH_MODE, outcome);

		setRequired(false, SampleDto.SAMPLE_DATE_TIME, SampleDto.SAMPLE_MATERIAL);
		   sampleMaterialComboBox.setVisible(false);
		   sampleMaterialComboBox.setRequired(false);
	}

	private void handleMeasles() {
			List<PathogenTestType> measelesPathogenTests = PathogenTestType.getMeaslesTestTypes();
			Arrays.stream(PathogenTestType.values())
					.filter(pathogenTestType -> !measelesPathogenTests.contains(pathogenTestType))
					.forEach(pathogenTestType -> requestedPathogenTestsField.removeItem(pathogenTestType));
			setVisible(false, SampleDto.FIELD_SAMPLE_ID, SampleDto.SAMPLING_REASON, SampleDto.SAMPLE_SOURCE, SampleDto.LAB_LOCATION, SampleDto.DATE_FORM_SENT_TO_DISTRICT, SampleDto.SUSPECTED_DISEASE, SampleDto.DATE_RESULTS_RECEIVED_SENT_TO_CLINICIAN);

			laboratoryDateResultsSentDSD.setVisible(true);
			setVisible(true, SampleDto.DATE_FORM_RECEIVED_AT_DISTRICT);
			dateFormReceivedAtDistrict.setVisible(true);
			dateFormSentToDistrict.setVisible(false);

		dateSurveillanceSentResultsToDistrict.setVisible(true);
		dateFormSentToHigherLevel.setVisible(true);
		personCompletingForm.setVisible(true);

	}

	private void handleCoronaVirus() {
		if (disease == Disease.CORONAVIRUS) {
			setVisible(true, SampleDto.PATHOGEN_TEST_RESULT, SampleDto.COMMENT, SampleDto.FIELD_SAMPLE_ID);
			setVisible(false, SampleDto.SUSPECTED_DISEASE, SampleDto.DATE_LAB_RECEIVED_SPECIMEN, SampleDto.DATE_RESULTS_RECEIVED_SENT_TO_CLINICIAN, SampleDto.DATE_SPECIMEN_SENT_TO_LAB);
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

    private void hideCommonProperties(){
        suspectedDisease.setVisible(false);
        labLocation.setVisible(false);
        dateLabReceivedSpecimen.setVisible(false);
        laboratorySampleCondition.setVisible(false);
        dateFormSentToDistrict.setVisible(false);
        dateFormReceivedAtDistrict.setVisible(false);
        dateResultsSentToClinician.setVisible(false);
        dateSpecimenSentToLab.setVisible(false);
    }

    private void hidePropertiesVisibility() {
        setVisible(false,
                SampleDto.SAMPLE_PURPOSE,
                SampleDto.SAMPLING_REASON,
                SampleDto.SAMPLING_REASON_DETAILS,
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
                SampleDto.COMMENT,
                SampleDto.PATHOGEN_TEST_RESULT,
                SampleDto.NO_TEST_POSSIBLE_REASON,
                CaseDataDto.DELETION_REASON,
                CaseDataDto.OTHER_DELETION_REASON
        );
    }

    //create a function with a switch statement within to return sampleMaterials types for specific diseases
    public void setSampleMaterialTypesForDisease(Disease disease) {
        switch (disease) {
            case MEASLES:
                requestedSampleMaterialsField.addItems(
                        Arrays.stream(SampleMaterial.getMeaselsMateriealTypes())
                                .filter(c -> fieldVisibilityCheckers.isVisible(SampleMaterial.class, c.name()))
                                .collect(Collectors.toList()));
                break;
            case YELLOW_FEVER:
                 List<SampleMaterial> values1 = Arrays.stream(SampleMaterial.getYellowFeverMateriealTypes())
                        .filter(c -> fieldVisibilityCheckers.isVisible(SampleMaterial.class, c.name()))
                        .collect(Collectors.toList());

                requestedSampleMaterialsField.addItems(values1);
				break;
            case MONKEYPOX:
                requestedSampleMaterialsField.addItems(
                        Arrays.stream(SampleMaterial.getNewInfluenzaType())
                                .filter(c -> fieldVisibilityCheckers.isVisible(SampleMaterial.class, c.name()))
                                .collect(Collectors.toList()));
                break;
            default:
                requestedSampleMaterialsField.addItems(
                        Arrays.stream(SampleMaterial.values())
                                .filter(c -> fieldVisibilityCheckers.isVisible(SampleMaterial.class, c.name()))
                                .collect(Collectors.toList()));
                break;
        }
    }

    private void handleDiseaseField(Disease... targetDiseases) {
        for (Disease targetDisease : targetDiseases) {
            if (disease == targetDisease) {
                setVisible(false, SampleDto.PATHOGEN_TESTING_REQUESTED, SampleDto.ADDITIONAL_TESTING_REQUESTED);
            }
        }
    }

    public void hideFieldsForSelectedDisease(Disease disease) {
        Set<String> disabledFields = SampleFormConfiguration.getDisabledFieldsForDisease(disease);
        for (String field : disabledFields) {
            disableField(field);
        }
    }

	public void handleGuineaWorm() {
		addFields(
				SampleDto.DATE_SPECIMEN_SENT_TO_REGION,
				SampleDto.NAME_OF_PERSON_WHO_RECEIVED_SPECIMEN_AT_REGION,
				SampleDto.DATE_SPECIMEN_RECEIVED_AT_REGION,
				SampleDto.DATE_SPECIMEN_SENT_TO_NATIONAL,
				SampleDto.NAME_OF_PERSON_WHO_RECEIVED_SPECIMEN_AT_NATIONAL,
				SampleDto.DATE_SPECIMEN_RECEIVED_AT_NATIONAL
		);

		addField(SampleDto.CONFIRMED_AS_GUINEA_WORM, NullableOptionGroup.class);

		setVisible(true,
				SampleDto.SPECIMEN_SAVED_AND_PRESEVED_IN_ALCOHOL,
				SampleDto.SENT_FOR_CONFIRMATION_NATIONAL,
				SampleDto.USE_OF_CLOTH_FILTER,
				SampleDto.REMARKS);

		FieldHelper.setVisibleWhen(
				getField(SampleDto.SPECIMEN_SAVED_AND_PRESEVED_IN_ALCOHOL),
				Arrays.asList(getField(SampleDto.SPECIMEN_SAVED_AND_PRESEVED_IN_ALCOHOL_WHY)),
				Arrays.asList(YesNo.YES),
				true);

		FieldHelper.setVisibleWhen(
				getFieldGroup(),
				Arrays.asList(SampleDto.SENT_FOR_CONFIRMATION_TO, SampleDto.SENT_FOR_CONFIRMATION_NATIONAL_DATE, SampleDto.DATE_RESULT_RECEIVED_NATIONAL),
				SampleDto.SENT_FOR_CONFIRMATION_NATIONAL,
				Arrays.asList(YesNo.YES),
				true);

		FieldHelper.setVisibleWhen(
				getFieldGroup(),
				Arrays.asList(SampleDto.FREQUENCY_OF_CHANGING_FILTERS),
				SampleDto.USE_OF_CLOTH_FILTER,
				Arrays.asList(YesNo.YES),
				true);

		FieldHelper.setVisibleWhen(
				getFieldGroup(),
				Arrays.asList(SampleDto.SPECIMEN_SAVED_AND_PRESEVED_IN_ALCOHOL_WHY),
				SampleDto.SPECIMEN_SAVED_AND_PRESEVED_IN_ALCOHOL,
				Arrays.asList(YesNo.NO),
				true);



	}
}
