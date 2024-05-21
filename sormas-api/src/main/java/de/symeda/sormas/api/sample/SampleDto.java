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
package de.symeda.sormas.api.sample;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.utils.*;

import java.security.Timestamp;
import java.util.Date;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.symeda.sormas.api.ImportIgnore;
import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.common.DeletionReason;
import de.symeda.sormas.api.contact.ContactReferenceDto;
import de.symeda.sormas.api.event.EventParticipantReferenceDto;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.infrastructure.facility.FacilityReferenceDto;
import de.symeda.sormas.api.sormastosormas.SormasToSormasShareableDto;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DependingOnFeatureType;
import de.symeda.sormas.api.utils.FieldConstraints;
import de.symeda.sormas.api.utils.SensitiveData;
import de.symeda.sormas.api.utils.pseudonymization.SampleDispatchMode;

@DependingOnFeatureType(featureType = FeatureType.SAMPLES_LAB)
public class SampleDto extends SormasToSormasShareableDto {

	private static final long serialVersionUID = -6975445672442728938L;

	public static final long APPROXIMATE_JSON_SIZE_IN_BYTES = 6210;

	public static final String I18N_PREFIX = "Sample";

	public static final String ASSOCIATED_CASE = "associatedCase";
	public static final String LAB_SAMPLE_ID = "labSampleID";
	public static final String FIELD_SAMPLE_ID = "fieldSampleID";
	public static final String SAMPLE_DATE_TIME = "sampleDateTime";
	public static final String REPORT_DATE_TIME = "reportDateTime";
	public static final String REPORTING_USER = "reportingUser";
	public static final String SAMPLE_MATERIAL = "sampleMaterial";
	public static final String SAMPLE_MATERIAL_TEXT = "sampleMaterialText";
	public static final String LAB = "lab";
	public static final String LAB_DETAILS = "labDetails";
	public static final String SAMPLE_PURPOSE = "samplePurpose";
	public static final String CSF_SAMPLE_COLLECTED = "csfSampleCollected";
	public static final String CSF_REASON = "csfReason";
	public static final String APPEARANCE_OF_CSF = "appearanceOfCsf";
	public static final String INOCULATION_TIME_TRANSPORT_MEDIA = "inoculationTimeTransportMedia";
	public static final String SAMPLE_SENT_TO_LAB = "sampleSentToLab";
	public static final String DATE_SAMPLE_SENT_TO_LAB = "dateSampleSentToLab";
	public static final String SAMPLE_CONTAINER_USED = "sampleContainerUsed";
	public static final String RDT_PERFORMED = "rdtPerformed";
	public static final String RDT_RESULTS = "rdtResults";
	public static final String DISTRICT_NOTIFICATION_DATE = "districtNotificationDate";
	public static final String NAME_OF_PERSON = "nameOfPerson";
	public static final String TEL_NUMBER = "telNumber";
	public static final String DATE_FORM_SENT_TO_DISTRICT = "dateFormSentToDistrict";
	public static final String DATE_FORM_RECEIVED_AT_DISTRICT = "dateFormReceivedAtDistrict";
	public static final String DATE_RESULTS_RECEIVED_SENT_TO_CLINICIAN = "dateResultsSentToClinician";
	public static final String DATE_SPECIMEN_SENT_TO_LAB = "dateSpecimenSentToLab";
	public static final String DATE_FORM_SENT_TO_REGION = "dateFormSentToRegion";
	public static final String DATE_FORM_RECEIVED_AT_REGION = "dateFormReceivedAtRegion";
	public static final String DATE_FORM_SENT_TO_NATIONAL = "dateFormSentToNational";
	public static final String DATE_FORM_RECEIVED_AT_NATIONAL = "dateFormReceivedAtNational";
	public static final String REASON_NOT_SENT_TO_LAB = "reasonNotSentToLab";


	public static final String SHIPMENT_DATE = "shipmentDate";
	public static final String SHIPMENT_DETAILS = "shipmentDetails";
	public static final String RECEIVED_DATE = "receivedDate";
	public static final String SPECIMEN_CONDITION = "specimenCondition";
	public static final String NO_TEST_POSSIBLE_REASON = "noTestPossibleReason";
	public static final String COMMENT = "comment";
	public static final String SAMPLE_SOURCE = "sampleSource";
	public static final String REFERRED_TO = "referredTo";
	public static final String SHIPPED = "shipped";
	public static final String RECEIVED = "received";
	public static final String PATHOGEN_TESTING_REQUESTED = "pathogenTestingRequested";
	public static final String SAMPLE_MATERIAL_REQUESTED = "sampleMaterialTestingRequested";
	public static final String ADDITIONAL_TESTING_REQUESTED = "additionalTestingRequested";
	public static final String REQUESTED_PATHOGEN_TESTS = "requestedPathogenTests";
	public static final String REQUESTED_SAMPLE_MATERIALS = "requestedSampleMaterials";
	public static final String SAMPLE_TESTS = "sampleTests";
//	public static final String SAMPLE_DISEASE_TESTS = "sampleDiseaseTests";
	public static final String SAMPLE_DISPATCH_MODE = "sampleDispatchMode";
	public static final String SAMPLE_DISPATCH_DATE = "sampleDispatchDate";
	public static final String REQUESTED_ADDITIONAL_TESTS = "requestedAdditionalTests";
	public static final String PATHOGEN_TEST_RESULT = "pathogenTestResult";
	public static final String REQUESTED_OTHER_PATHOGEN_TESTS = "requestedOtherPathogenTests";
	public static final String REQUESTED_OTHER_ADDITIONAL_TESTS = "requestedOtherAdditionalTests";
	public static final String SAMPLING_REASON = "samplingReason";
	public static final String SAMPLING_REASON_DETAILS = "samplingReasonDetails";
	public static final String DELETION_REASON = "deletionReason";
	public static final String OTHER_DELETION_REASON = "otherDeletionReason";
	public static final String SUSPECTED_DISEASE = "suspectedDisease";
	public static final String DATE_LAB_RECEIVED_SPECIMEN = "dateLabReceivedSpecimen";
	public static final String LAB_LOCATION = "labLocation";
	public static final String IPSAMPLESENT = "ipSampleSent";
	public static final String IPSAMPLERESULTS = "ipSampleResults";
	public static final String DISEASE = "disease";



	public static final String LABORATORY_NAME = "laboratoryName";
	public static final String LABORATORY_SAMPLE_DATE_RECEIVED = "laboratorySampleDateReceived";
	public static final String LABORATORY_NUMBER = "laboratoryNumber";
	public static final String LABORATORY_SAMPLE_CONTAINER_RECEIVED = "laboratorySampleContainerReceived";
	public static final String LABORATORY_TYPE = "laboratoryType";
	public static final String LABORATORY_SAMPLE_CONTAINER_OTHER = "laboratorySampleContainerOther";
	public static final String LABORATORY_SAMPLE_CONDITION = "laboratorySampleCondition";
	public static final String LABORATORY_APPEARANCE_OF_CSF = "laboratoryAppearanceOfCSF";
	public static final String LABORATORY_TEST_PERFORMED = "laboratoryTestPerformed";
	public static final String LABORATORY_TEST_PERFORMED_OTHER = "laboratoryTestPerformedOther";
	public static final String LABORATORY_CYTOLOGY = "laboratoryCytology";
	public static final String LABORATORY_GRAM = "laboratoryGram";
	public static final String LABORATORY_GRAM_OTHER = "laboratoryGramOther";
	public static final String LABORATORY_RDT_PERFORMED = "laboratoryRdtPerformed";
	public static final String LABORATORY_RDT_RESULTS = "laboratoryRdtResults";
	public static final String LABORATORY_LATEX = "laboratoryLatex";
	public static final String LABORATORY_CULTURE = "laboratoryCulture";
	public static final String LABORATORY_CULTURE_OTHER = "laboratoryCultureOther";
	public static final String LABORATORY_OTHER_TESTS = "laboratoryOtherTests";
	public static final String LABORATORY_OTHER_TESTS_RESULTS = "laboratoryOtherTestsResults";
	public static final String LABORATORY_CEFTRIAXONE = "laboratoryCeftriaxone";
	public static final String LABORATORY_PENICILLIN_G = "laboratoryPenicillinG";
	public static final String LABORATORY_AMOXYCILLIN = "laboratoryAmoxycillin";
	public static final String LABORATORY_OXACILLIN = "laboratoryOxacillin";
	public static final String LABORATORY_ANTIBIOGRAM_OTHER = "laboratoryAntibiogramOther";
	public static final String LABORATORY_DATE_PCR_PERFORMED = "laboratoryDatePcrPerformed";
	public static final String LABORATORY_PCR_TYPE = "laboratoryPcrType";
	public static final String LABORATORY_PCR_OPTIONS = "laboratoryPcrOptions";
	public static final String LABORATORY_SEROTYPE = "laboratorySerotype";
	public static final String LABORATORY_SEROTYPE_TYPE = "laboratorySerotypeType";
	public static final String LABORATORY_SEROTYPE_RESULTS = "laboratorySerotypeResults";
	public static final String LABORATORY_FINAL_RESULTS = "laboratoryFinalResults";
	public static final String LABORATORY_OBSERVATIONS = "laboratoryObservations";
	public static final String LABORATORY_DATE_RESULTS_SENT_HEALTH_FACILITY = "laboratoryDateResultsSentHealthFacility";
	public static final String LABORATORY_DATE_RESULTS_SENT_DSD = "laboratoryDateResultsSentDSD";
	public static final String LABORATORY_FINAL_CLASSIFICATION = "laboratoryFinalClassification";

	//AFP

	public static final String DATE_FIRST_SPECIMEN = "dateFirstSpecimen";
	public static final String DATE_SECOND_SPECIMEN = "dateSecondSpecimen";
	public static final String DATE_SPECIMEN_SENT_NATIONAL_LEVEL = "dateSpecimenSentNationalLevel";
	public static final String DATE_SPECIMEN_RECEIVED_NATIONAL_LEVEL = "dateSpecimenReceivedNationalLevel";
	public static final String DATE_SPECIMEN_SENT_INTERCOUNTY_NATLAB = "dateSpecimenSentInter";
	public static final String DATE_SPECIMEN_RECEIVED_INTERCOUNTY_NATLAB = "dateSpecimenReceivedInter";
	public static final String STATUS_SPECIMEN_RECEPTION_AT_LAB = "statusSpecimenReceptionAtLab";
	public static final String DATE_COMBINED_CELL_CULTURE_RESULTS = "dateCombinedCellCultureResults";
	public static final String W1 = "w1";
	public static final String W2 = "w2";
	public static final String W3 = "w3";
	public static final String DISCORDANT = "discordant";
	public static final String SL1 = "sL1";
	public static final String SL2 = "sL2";
	public static final String SL3 = "sL3";
	public static final String DATE_FOLLOWUP_EXAM = "dateFollowUpExam";
	public static final String RESIDUAL_ANALYSIS = "residualAnalysis";
	public static final String RESULT_EXAM = "resultExam";
	public static final String DATE_SENT_NATIONAL_REG_LAB = "dateSentToNationalRegLab";
	public static final String DATE_DIFFERENTIATION_SENT_EPI = "dateDifferentiationSentToEpi";
	public static final String DATE_DIFFERENTIATION_RECEIVED_EPI = "dateDifferentiationReceivedFromEpi";
	public static final String DATE_ISOLATE_SENT_SEQUENCING = "dateIsolateSentForSequencing";
	public static final String DATE_SEQ_RESULTS_SENT_PROGRAM = "dateSeqResultsSentToProgram";
	public static final String FINAL_LAB_RESULTS = "finalLabResults";
	public static final String IMMUNOCOMPROMISED_STATUS_SUSPECTED = "immunocompromisedStatusSuspected";
	public static final String AFP_FINAL_CLASSIFICATION = "afpFinalClassification";
	public static final String POSITIVE_VIRAL_CULTURE = "positiveViralCulture";
	public static final String POSITIVE_REAL_TIME = "positiveRealTime";
	public static final String FOUR_FOLD_RISE = "fourFoldRise";
	public static final String INFLUENZA_VIRUS = "influenzaVirus";
	public static final String OTHER_TYPE = "otherInfluenzaVirus";
	public static final String TREATMENT = "treatment";
	public static final String STATE_TREATMENT_ADMINISTERED = "stateTreatmentAdministered";

	private CaseReferenceDto associatedCase;
	private ContactReferenceDto associatedContact;
	private EventParticipantReferenceDto associatedEventParticipant;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String labSampleID;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String fieldSampleID;
	private YesNoUnknown forRetest;
//	@NotNull(message = Validations.requiredField)
	private Date sampleDateTime;

	@NotNull(message = Validations.validReportDateTime)
	private Date reportDateTime;
	private UserReferenceDto reportingUser;
	@SensitiveData
	@Min(value = -90, message = Validations.numberTooSmall)
	@Max(value = 90, message = Validations.numberTooBig)
	private Double reportLat;
	@SensitiveData
	@Min(value = -180, message = Validations.numberTooSmall)
	@Max(value = 180, message = Validations.numberTooBig)
	private Double reportLon;

	private Float reportLatLonAccuracy;

	private SampleMaterial sampleMaterial;
	private Disease disease;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String sampleMaterialText;

	private SamplePurpose samplePurpose;

	private FacilityReferenceDto lab;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String labDetails;
	private Date shipmentDate;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String shipmentDetails;
	private Date receivedDate;
	private SpecimenCondition specimenCondition;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String noTestPossibleReason;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_BIG, message = Validations.textTooLong)
	private String comment;
	private SampleSource sampleSource;
	private SampleReferenceDto referredTo;
	private boolean shipped;
/*	private boolean sampleMaterialTypeForYF;
	private Boolean sampleDiseaseTests;*/
	private boolean received;
	private PathogenTestResultType pathogenTestResult;

	private Boolean pathogenTestingRequested;
	private Boolean sampleMaterialTestingRequested;
	private Boolean additionalTestingRequested;
	private Set<PathogenTestType> requestedPathogenTests;
	private Set<SampleMaterial> requestedSampleMaterials;
	private PathogenTestType sampleTests;
	private Set<AdditionalTestType> requestedAdditionalTests;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String requestedOtherPathogenTests;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String requestedOtherAdditionalTests;

	private SamplingReason samplingReason;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_TEXT, message = Validations.textTooLong)
	private String samplingReasonDetails;

	private boolean deleted;
	private DeletionReason deletionReason;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_TEXT, message = Validations.textTooLong)
	private String otherDeletionReason;

	private YesNo ipSampleSent;
	private IpResult ipSampleResults;
	private SampleDispatchMode sampleDispatchMode;
	private Date sampleDispatchDate;

	private YesNo csfSampleCollected;
	private YesNo rdtPerformed;
	private YesNo sampleSentToLab;

	private CsfReason csfReason;
	private CsfAppearance appearanceOfCsf;
	private SampleContainerUsed sampleContainerUsed;

	private String rdtResults;
	private String reasonNotSentToLab;
	private String nameOfPerson;
	private String telNumber;

	private Date inoculationTimeTransportMedia;
	private Date districtNotificationDate;
	private Date dateSampleSentToLab;
	private Date dateFormSentToDistrict;
	private Date dateFormReceivedAtDistrict;
	private Date dateResultsSentToClinician;
	private Date dateSpecimenSentToLab;
	private Date dateFormSentToRegion;
	private Date dateFormReceivedAtRegion;
	private Date dateFormSentToNational;
	private Date dateFormReceivedAtNational;


	private String laboratoryName;
	private String laboratoryNumber;
	private String laboratorySerotype;
	private String laboratorySerotypeType;
	private String laboratorySerotypeResults;
	private String laboratoryFinalResults;
	private String laboratoryObservations;
	private String laboratorySampleContainerOther;
	private String laboratoryTestPerformedOther;
	private String laboratoryCytology;
	private String laboratoryGramOther;
	private String laboratoryCultureOther;
	private String laboratoryOtherTests;
	private String laboratoryOtherTestsResults;
	private String laboratoryRdtResults;
	private CaseClassification laboratoryFinalClassification;
	private String laboratoryPcrType;

	private SampleContainerUsed laboratorySampleContainerReceived;
	private SpecimenCondition laboratorySampleCondition;
	private CsfAppearance laboratoryAppearanceOfCSF;
	private LabTest laboratoryTestPerformed;
	private Gram laboratoryGram;
	private YesNo laboratoryRdtPerformed;
	private LatexCulture laboratoryLatex;
	private LatexCulture laboratoryCulture;
	private LatexCulture laboratoryPcrOptions;
	private Antibiogram laboratoryCeftriaxone;
	private Antibiogram laboratoryPenicillinG;
	private Antibiogram laboratoryAmoxycillin;
	private Antibiogram laboratoryOxacillin;
	private Antibiogram laboratoryAntibiogramOther;
	private LabType laboratoryType;

	private Date laboratoryDateResultsSentHealthFacility;
	private Date laboratoryDateResultsSentDSD;
	private Date laboratorySampleDateReceived;
	private Date laboratoryDatePcrPerformed;

	//AFP
	private Date dateSentToNationalRegLab;
	private Date dateDifferentiationSentToEpi;
	private Date dateDifferentiationReceivedFromEpi;
	private Date dateIsolateSentForSequencing;
	private Date dateSeqResultsSentToProgram;

	private PosNeg finalLabResults;
	private YesNoUnknown immunocompromisedStatusSuspected;
	private AFPClassification afpFinalClassification;

	private Date dateFirstSpecimen;
	private Date dateSecondSpecimen;
	private Date dateSpecimenSentNationalLevel;
	private Date dateSpecimenReceivedNationalLevel;
	private Date dateSpecimenSentInter;
	private Date dateSpecimenReceivedInter;
	private SpecimenCondition statusSpecimenReceptionAtLab;
	private Date dateCombinedCellCultureResults;
	private YesNo w1;
	private YesNo w2;
	private YesNo w3;
	private YesNoUnknown discordant;
	private YesNo sL1;
	private YesNo sL2;
	private YesNo sL3;
	private Date dateFollowUpExam;
	private InjectionSite residualAnalysis;
	private AfpResult resultExam;
	private YesNo positiveViralCulture;
	private YesNo positiveRealTime;
	private YesNo fourFoldRise;
	private InfluenzaVirus influenzaVirus;
	private String otherInfluenzaVirus;
	private String treatment;
	private String stateTreatmentAdministered;
	private Disease suspectedDisease;
	private String labLocation;
	private Date dateLabReceivedSpecimen;



	public YesNo getCsfSampleCollected() {
		return csfSampleCollected;
	}
	public void setCsfSampleCollected(YesNo csfSampleCollected) {
		this.csfSampleCollected = csfSampleCollected;
	}
	public YesNo getRdtPerformed() {
		return rdtPerformed;
	}
	public void setRdtPerformed(YesNo rdtPerformed) {
		this.rdtPerformed = rdtPerformed;
	}
	public YesNo getSampleSentToLab() {
		return sampleSentToLab;
	}
	public void setSampleSentToLab(YesNo sampleSentToLab) {
		this.sampleSentToLab = sampleSentToLab;
	}

	public CsfReason getCsfReason() {
		return csfReason;
	}
	public void setCsfReason(CsfReason csfReason) {
		this.csfReason = csfReason;
	}
	public CsfAppearance getAppearanceOfCsf() {return appearanceOfCsf; }
	public void setAppearanceOfCsf(CsfAppearance appearanceOfCsf) {
		this.appearanceOfCsf = appearanceOfCsf;
	}
	public SampleContainerUsed getSampleContainerUsed() {
		return sampleContainerUsed;
	}
	public void setSampleContainerUsed(SampleContainerUsed sampleContainerUsed) {
		this.sampleContainerUsed = sampleContainerUsed;
	}

	public String getRdtResults() {
		return rdtResults;
	}

	public void setRdtResults(String rdtResults) {
		this.rdtResults = rdtResults;
	}

	public String getReasonNotSentToLab() {
		return reasonNotSentToLab;
	}

	public void setReasonNotSentToLab(String reasonNotSentToLab) {
		this.reasonNotSentToLab = reasonNotSentToLab;
	}
	public String getNameOfPerson() {
		return nameOfPerson;
	}

	public void setNameOfPerson(String nameOfPerson) {
		this.nameOfPerson = nameOfPerson;
	}
	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	public Date getInoculationTimeTransportMedia() {
		return inoculationTimeTransportMedia;
	}

	public void setInoculationTimeTransportMedia(Date inoculationTimeTransportMedia) {
		this.inoculationTimeTransportMedia = inoculationTimeTransportMedia;
	}

	public Date getDistrictNotificationDate() {
		return districtNotificationDate;
	}

	public void setDistrictNotificationDate(Date districtNotificationDate) {
		this.districtNotificationDate = districtNotificationDate;
	}

	public Date getDateSampleSentToLab() {
		return dateSampleSentToLab;
	}

	public void setDateSampleSentToLab(Date dateSampleSentToLab) {
		this.dateSampleSentToLab = dateSampleSentToLab;
	}

	public Date getDateFormSentToDistrict() {
		return dateFormSentToDistrict;
	}

	public void setDateFormSentToDistrict(Date dateFormSentToDistrict) {
		this.dateFormSentToDistrict = dateFormSentToDistrict;
	}

	public Date getDateFormReceivedAtDistrict() {
		return dateFormReceivedAtDistrict;
	}

	public void setDateFormReceivedAtDistrict(Date dateFormReceivedAtDistrict) {
		this.dateFormReceivedAtDistrict = dateFormReceivedAtDistrict;
	}

	public Date getDateFormSentToRegion() {
		return dateFormSentToRegion;
	}

	public void setDateFormSentToRegion(Date dateFormSentToRegion) {
		this.dateFormSentToRegion = dateFormSentToRegion;
	}

	public Date getDateFormReceivedAtRegion() {
		return dateFormReceivedAtRegion;
	}

	public void setDateFormReceivedAtRegion(Date dateFormReceivedAtRegion) {
		this.dateFormReceivedAtRegion = dateFormReceivedAtRegion;
	}

	public Date getDateFormSentToNational() {
		return dateFormSentToNational;
	}

	public void setDateFormSentToNational(Date dateFormSentToNational) {
		this.dateFormSentToNational = dateFormSentToNational;
	}

	public Date getDateFormReceivedAtNational() {
		return dateFormReceivedAtNational;
	}

	public void setDateFormReceivedAtNational(Date dateFormReceivedAtNational) {
		this.dateFormReceivedAtNational = dateFormReceivedAtNational;
	}

	public Date getSampleDispatchDate() {
		return sampleDispatchDate;
	}

	public void setSampleDispatchDate(Date sampleDispatchDate) {
		this.sampleDispatchDate = sampleDispatchDate;
	}

	public YesNo getIpSampleSent() {
		return ipSampleSent;
	}

	public void setIpSampleSent(YesNo ipSampleSent) {
		this.ipSampleSent = ipSampleSent;
	}

	public SampleDispatchMode getSampleDispatchMode() {
		return sampleDispatchMode;
	}
	public void setSampleDispatchMode(SampleDispatchMode sampleDispatchMode) {
		this.sampleDispatchMode = sampleDispatchMode;
	}
	public IpResult getIpSampleResults(){
		return ipSampleResults;
	}

	public void setIpSampleResults(IpResult ipSampleResults) {
		this.ipSampleResults = ipSampleResults;
	}

	public Disease getDisease(){
		return disease;
	}
	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	@ImportIgnore
	public CaseReferenceDto getAssociatedCase() {
		return associatedCase;
	}

	public void setAssociatedCase(CaseReferenceDto associatedCase) {
		this.associatedCase = associatedCase;
	}

	@ImportIgnore
	public ContactReferenceDto getAssociatedContact() {
		return associatedContact;
	}

	public void setAssociatedContact(ContactReferenceDto associatedContact) {
		this.associatedContact = associatedContact;
	}

	@ImportIgnore
	public EventParticipantReferenceDto getAssociatedEventParticipant() {
		return associatedEventParticipant;
	}

	public void setAssociatedEventParticipant(EventParticipantReferenceDto associatedEventParticipant) {
		this.associatedEventParticipant = associatedEventParticipant;
	}

	public String getLabSampleID() {
		return labSampleID;
	}

	public void setLabSampleID(String labSampleID) {
		this.labSampleID = labSampleID;
	}

	public String getFieldSampleID() {
		return fieldSampleID;
	}

	public void setFieldSampleID(String fieldSampleID) {
		this.fieldSampleID = fieldSampleID;
	}

	public YesNoUnknown getForRetest() {
		return forRetest;
	}

	public void setForRetest(YesNoUnknown forRetest) {
		this.forRetest = forRetest;
	}

	public Date getSampleDateTime() {
		return sampleDateTime;
	}

	public void setSampleDateTime(Date sampleDateTime) {
		this.sampleDateTime = sampleDateTime;
	}

	public Date getReportDateTime() {
		return reportDateTime;
	}

	public void setReportDateTime(Date reportDateTime) {
		this.reportDateTime = reportDateTime;
	}

	@Override
	public UserReferenceDto getReportingUser() {
		return reportingUser;
	}

	@Override
	public void setReportingUser(UserReferenceDto reportingUser) {
		this.reportingUser = reportingUser;
	}

	public SampleMaterial getSampleMaterial() {
		return sampleMaterial;
	}

	public void setSampleMaterial(SampleMaterial sampleMaterial) {
		this.sampleMaterial = sampleMaterial;
	}

	public String getSampleMaterialText() {
		return sampleMaterialText;
	}

	public void setSampleMaterialText(String sampleMaterialText) {
		this.sampleMaterialText = sampleMaterialText;
	}

	public SamplePurpose getSamplePurpose() {
		return samplePurpose;
	}

	public void setSamplePurpose(SamplePurpose samplePurpose) {
		this.samplePurpose = samplePurpose;
	}

	public FacilityReferenceDto getLab() {
		return lab;
	}

	public void setLab(FacilityReferenceDto lab) {
		this.lab = lab;
	}

	public String getLabDetails() {
		return labDetails;
	}

	public void setLabDetails(String labDetails) {
		this.labDetails = labDetails;
	}

	public Date getShipmentDate() {
		return shipmentDate;
	}

	public void setShipmentDate(Date shipmentDate) {
		this.shipmentDate = shipmentDate;
	}

	public String getShipmentDetails() {
		return shipmentDetails;
	}

	public void setShipmentDetails(String shipmentDetails) {
		this.shipmentDetails = shipmentDetails;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public SpecimenCondition getSpecimenCondition() {
		return specimenCondition;
	}

	public void setSpecimenCondition(SpecimenCondition specimenCondition) {
		this.specimenCondition = specimenCondition;
	}

	public String getNoTestPossibleReason() {
		return noTestPossibleReason;
	}

	public void setNoTestPossibleReason(String noTestPossibleReason) {
		this.noTestPossibleReason = noTestPossibleReason;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public SampleSource getSampleSource() {
		return sampleSource;
	}

	public void setSampleSource(SampleSource sampleSource) {
		this.sampleSource = sampleSource;
	}

	@ImportIgnore
	public SampleReferenceDto getReferredTo() {
		return referredTo;
	}

	public void setReferredTo(SampleReferenceDto referredTo) {
		this.referredTo = referredTo;
	}

	public boolean isShipped() {
		return shipped;
	}

	public void setShipped(Boolean shipped) {
		this.shipped = shipped;
	}

	public boolean isReceived() {
		return received;
	}

	public void setReceived(boolean received) {
		this.received = received;
	}

	public PathogenTestResultType getPathogenTestResult() {
		return pathogenTestResult;
	}

	public void setPathogenTestResult(PathogenTestResultType pathogenTestResult) {
		this.pathogenTestResult = pathogenTestResult;
	}

	@ImportIgnore
	public Boolean getPathogenTestingRequested() {
		return pathogenTestingRequested;
	}

	public void setPathogenTestingRequested(Boolean pathogenTestingRequested) {
		this.pathogenTestingRequested = pathogenTestingRequested;
	}

	@ImportIgnore
	public Boolean getSampleMaterialTestingRequested() {
		return sampleMaterialTestingRequested;
	}

	public void setSampleMaterialTestingRequested(Boolean sampleMaterialTestingRequested) {
		this.sampleMaterialTestingRequested = sampleMaterialTestingRequested;
	}
	/*@ImportIgnore
	public boolean isYellowFeverSampleType() {
		return sampleMaterialTypeForYF;
	}

	public void setYellowFeverSampleType(boolean sampleMaterialTypeForYF) {
		this.sampleMaterialTypeForYF = sampleMaterialTypeForYF;
	}

	@ImportIgnore
	public Boolean isDiseaseSampleTests() {
		return sampleDiseaseTests;
	}

	public void setDiseaseSampleTests(Boolean sampleDiseaseTests) {
		this.sampleDiseaseTests = sampleDiseaseTests;
	}*/

	@ImportIgnore
	public Boolean getAdditionalTestingRequested() {
		return additionalTestingRequested;
	}

	public void setAdditionalTestingRequested(Boolean additionalTestingRequested) {
		this.additionalTestingRequested = additionalTestingRequested;
	}

	@ImportIgnore
	public Set<PathogenTestType> getRequestedPathogenTests() {
		return requestedPathogenTests;
	}

	public void setRequestedPathogenTests(Set<PathogenTestType> requestedPathogenTests) {
		this.requestedPathogenTests = requestedPathogenTests;
	}
	@ImportIgnore
	public Set<SampleMaterial> getRequestedSampleMaterials() {
		return requestedSampleMaterials;
	}

	public void setRequestedSampleMaterials(Set<SampleMaterial> requestedSampleMaterials) {
		this.requestedSampleMaterials = requestedSampleMaterials;
	}
	public PathogenTestType getSampleTests() {
		return sampleTests;
	}

	public void setSampleTests(PathogenTestType sampleTests) {
		this.sampleTests = sampleTests;
	}

	@ImportIgnore
	public Set<AdditionalTestType> getRequestedAdditionalTests() {
		return requestedAdditionalTests;
	}

	public void setRequestedAdditionalTests(Set<AdditionalTestType> requestedAdditionalTests) {
		this.requestedAdditionalTests = requestedAdditionalTests;
	}

	@ImportIgnore
	public String getRequestedOtherPathogenTests() {
		return requestedOtherPathogenTests;
	}

	public void setRequestedOtherPathogenTests(String requestedOtherPathogenTests) {
		this.requestedOtherPathogenTests = requestedOtherPathogenTests;
	}

	@ImportIgnore
	public String getRequestedOtherAdditionalTests() {
		return requestedOtherAdditionalTests;
	}

	public void setRequestedOtherAdditionalTests(String requestedOtherAdditionalTests) {
		this.requestedOtherAdditionalTests = requestedOtherAdditionalTests;
	}

	public SamplingReason getSamplingReason() {
		return samplingReason;
	}

	public void setSamplingReason(SamplingReason samplingReason) {
		this.samplingReason = samplingReason;
	}

	public String getSamplingReasonDetails() {
		return samplingReasonDetails;
	}

	public void setSamplingReasonDetails(String samplingReasonDetails) {
		this.samplingReasonDetails = samplingReasonDetails;
	}

	public static SampleDto build(UserReferenceDto userRef, CaseReferenceDto caseRef) {

		final SampleDto sampleDto = getSampleDto(userRef);
		sampleDto.setAssociatedCase(caseRef);
		return sampleDto;
	}

	public static SampleDto build(UserReferenceDto userRef, EventParticipantReferenceDto eventParticipantRef) {

		final SampleDto sampleDto = getSampleDto(userRef);
		sampleDto.setAssociatedEventParticipant(eventParticipantRef);
		return sampleDto;
	}

	public static SampleDto build(UserReferenceDto userRef, ContactReferenceDto contactRef) {

		final SampleDto sampleDto = getSampleDto(userRef);
		sampleDto.setAssociatedContact(contactRef);
		return sampleDto;
	}

	private static SampleDto getSampleDto(UserReferenceDto userRef) {

		SampleDto sample = new SampleDto();
		sample.setUuid(DataHelper.createUuid());

		sample.setReportingUser(userRef);
		sample.setReportDateTime(new Date());
		sample.setPathogenTestResult(PathogenTestResultType.PENDING);

		return sample;
	}

	public static SampleDto buildReferralDto(UserReferenceDto userRef, SampleDto referredSample) {

		final SampleDto sample;
		final CaseReferenceDto associatedCase = referredSample.getAssociatedCase();
		final ContactReferenceDto associatedContact = referredSample.getAssociatedContact();
		final EventParticipantReferenceDto associatedEventParticipant = referredSample.getAssociatedEventParticipant();
		if (associatedCase != null) {
			sample = build(userRef, associatedCase);
		} else if (associatedContact != null) {
			sample = build(userRef, associatedContact);
		} else {
			sample = build(userRef, associatedEventParticipant);
		}
		migrateAttributesOfPhysicalSample(referredSample, sample);

		return sample;
	}

	/**
	 * The physical sample is neither the source, nor the target. This method is about migrating the attributes that belong to the real
	 * (physical) sample out there in the labs.
	 * Source and target should both refer to the physical sample, but have different values for some attributes. For example, the
	 * specimenCondition may be different in source and target.
	 * In one lab (source), the specimenCondition may be ADEQUATE. But then during transport to another lab (target) the specimenCondition
	 * can change to NOT_ADEQUATE.
	 *
	 * In contrast, the attributes of the physical sample don't change (e.g. samplingReason) and thus should be migrated when a sample
	 * referral is created in SORMAS.
	 */
	private static void migrateAttributesOfPhysicalSample(SampleDto source, SampleDto target) {
	target.setSampleDateTime(source.getSampleDateTime());
		target.setSampleMaterial(source.getSampleMaterial());
		target.setSampleMaterialText(source.getSampleMaterialText());
		target.setSampleSource(source.getSampleSource());
		target.setPathogenTestingRequested(source.getPathogenTestingRequested());
		target.setSampleMaterialTestingRequested(source.getSampleMaterialTestingRequested());
		target.setAdditionalTestingRequested(source.getAdditionalTestingRequested());
		target.setRequestedPathogenTests(source.getRequestedPathogenTests());
		target.setRequestedSampleMaterials(source.getRequestedSampleMaterials());
		target.setSampleTests(source.getSampleTests());
		target.setRequestedAdditionalTests(source.getRequestedAdditionalTests());
		target.setFieldSampleID(source.getFieldSampleID());
		target.setSamplingReason(source.getSamplingReason());
		target.setSamplingReasonDetails(source.getSamplingReasonDetails());
		target.setSamplePurpose(source.getSamplePurpose());

		target.setCsfSampleCollected(source.getCsfSampleCollected());
		target.setRdtPerformed(source.getRdtPerformed());
		target.setSampleSentToLab(source.getSampleSentToLab());
		target.setCsfReason(source.getCsfReason());
		target.setAppearanceOfCsf(source.getAppearanceOfCsf());
		target.setSampleContainerUsed(source.getSampleContainerUsed());
		target.setRdtResults(source.getRdtResults());
		target.setReasonNotSentToLab(source.getReasonNotSentToLab());
		target.setNameOfPerson(source.getNameOfPerson());
		target.setTelNumber(source.getTelNumber());
		target.setInoculationTimeTransportMedia(source.getInoculationTimeTransportMedia());
		target.setDistrictNotificationDate(source.getDistrictNotificationDate());
		target.setDateSampleSentToLab(source.getDateSampleSentToLab());
		target.setDateFormSentToDistrict(source.getDateFormSentToDistrict());
		target.setDateFormReceivedAtDistrict(source.getDateFormReceivedAtDistrict());
		target.setDateFormSentToRegion(source.getDateFormSentToRegion());
		target.setDateFormReceivedAtRegion(source.getDateFormReceivedAtRegion());
		target.setDateFormSentToNational(source.getDateFormSentToNational());
		target.setDateFormReceivedAtNational(source.getDateFormReceivedAtNational());

		target.setLaboratoryName(source.getLaboratoryName());
		target.setLaboratoryNumber(source.getLaboratoryNumber());
		target.setLaboratorySerotype(source.getLaboratorySerotype());
		target.setLaboratorySerotypeType(source.getLaboratorySerotypeType());
		target.setLaboratorySerotypeResults(source.getLaboratorySerotypeResults());
		target.setLaboratoryFinalResults(source.getLaboratoryFinalResults());
		target.setLaboratoryObservations(source.getLaboratoryObservations());
		target.setLaboratorySampleContainerOther(source.getLaboratorySampleContainerOther());
		target.setLaboratoryTestPerformedOther(source.getLaboratoryTestPerformedOther());
		target.setLaboratoryCytology(source.getLaboratoryCytology());
		target.setLaboratoryGramOther(source.getLaboratoryGramOther());
		target.setLaboratoryCultureOther(source.getLaboratoryCultureOther());
		target.setLaboratoryOtherTests(source.getLaboratoryOtherTests());
		target.setLaboratoryOtherTestsResults(source.getLaboratoryOtherTestsResults());
		target.setLaboratoryRdtResults(source.getLaboratoryRdtResults());
		target.setLaboratoryFinalClassification(source.getLaboratoryFinalClassification());
		target.setLaboratoryPcrType(source.getLaboratoryPcrType());
		target.setLaboratorySampleContainerReceived(source.getLaboratorySampleContainerReceived());
		target.setLaboratorySampleCondition(source.getLaboratorySampleCondition());
		target.setLaboratoryAppearanceOfCSF(source.getLaboratoryAppearanceOfCSF());
		target.setLaboratoryTestPerformed(source.getLaboratoryTestPerformed());
		target.setLaboratoryGram(source.getLaboratoryGram());
		target.setLaboratoryRdtPerformed(source.getLaboratoryRdtPerformed());
		target.setLaboratoryLatex(source.getLaboratoryLatex());
		target.setLaboratoryCulture(source.getLaboratoryCulture());
		target.setLaboratoryPcrOptions(source.getLaboratoryPcrOptions());
		target.setLaboratoryCeftriaxone(source.getLaboratoryCeftriaxone());
		target.setLaboratoryPenicillinG(source.getLaboratoryPenicillinG());
		target.setLaboratoryAmoxycillin(source.getLaboratoryAmoxycillin());
		target.setLaboratoryOxacillin(source.getLaboratoryOxacillin());
		target.setLaboratoryAntibiogramOther(source.getLaboratoryAntibiogramOther());
		target.setLaboratoryType(source.getLaboratoryType());
		target.setLaboratoryDateResultsSentHealthFacility(source.getLaboratoryDateResultsSentHealthFacility());
		target.setLaboratoryDateResultsSentDSD(source.getLaboratoryDateResultsSentDSD());
		target.setLaboratorySampleDateReceived(source.getLaboratorySampleDateReceived());
		target.setLaboratoryDatePcrPerformed(source.getLaboratoryDatePcrPerformed());
		target.setDateSentToNationalRegLab(source.getDateSentToNationalRegLab());
		target.setDateDifferentiationSentToEpi(source.getDateDifferentiationSentToEpi());
		target.setDateDifferentiationReceivedFromEpi(source.getDateDifferentiationReceivedFromEpi());
		target.setDateIsolateSentForSequencing(source.getDateIsolateSentForSequencing());
		target.setDateSeqResultsSentToProgram(source.getDateSeqResultsSentToProgram());
		target.setFinalLabResults(source.getFinalLabResults());
		target.setImmunocompromisedStatusSuspected(source.getImmunocompromisedStatusSuspected());
		target.setAfpFinalClassification(source.getAfpFinalClassification());
		target.setDateFirstSpecimen(source.getDateFirstSpecimen());
		target.setDateSecondSpecimen(source.getDateSecondSpecimen());
		target.setDateSpecimenSentNationalLevel(source.getDateSpecimenSentNationalLevel());
		target.setDateSpecimenReceivedNationalLevel(source.getDateSpecimenReceivedNationalLevel());
		target.setDateSpecimenSentInter(source.getDateSpecimenSentInter());
		target.setDateSpecimenReceivedInter(source.getDateSpecimenReceivedInter());
		target.setStatusSpecimenReceptionAtLab(source.getStatusSpecimenReceptionAtLab());
		target.setDateCombinedCellCultureResults(source.getDateCombinedCellCultureResults());
		target.setW1(source.getW1());
		target.setW2(source.getW2());
		target.setW3(source.getW3());
		target.setDiscordant(source.getDiscordant());
		target.setsL1(source.getsL1());
		target.setsL2(source.getsL2());
		target.setsL3(source.getsL3());
		target.setDateFollowUpExam(source.getDateFollowUpExam());
		target.setResidualAnalysis(source.getResidualAnalysis());
		target.setResultExam(source.getResultExam());
		target.setPositiveViralCulture(source.getPositiveViralCulture());
		target.setPositiveRealTime(source.getPositiveRealTime());
		target.setFourFoldRise(source.getFourFoldRise());
		target.setOtherInfluenzaVirus(source.getOtherInfluenzaVirus());
		target.setInfluenzaVirus(source.getInfluenzaVirus());
		target.setTreatment(source.getTreatment());
		target.setStateTreatmentAdministered(source.getStateTreatmentAdministered());
		target.setSuspectedDisease(source.getSuspectedDisease());
		target.setLabLocation(source.getLabLocation());
		target.setDateLabReceivedSpecimen(source.getDateLabReceivedSpecimen());
		target.setDateResultsSentToClinician(source.getDateResultsSentToClinician());
		target.setDateSpecimenSentToLab(source.getDateSpecimenSentToLab());





	}

	@ImportIgnore
	public Double getReportLat() {
		return reportLat;
	}

	public void setReportLat(Double reportLat) {
		this.reportLat = reportLat;
	}

	@ImportIgnore
	public Double getReportLon() {
		return reportLon;
	}

	public void setReportLon(Double reportLon) {
		this.reportLon = reportLon;
	}

	@ImportIgnore
	public Float getReportLatLonAccuracy() {
		return reportLatLonAccuracy;
	}

	public void setReportLatLonAccuracy(Float reportLatLonAccuracy) {
		this.reportLatLonAccuracy = reportLatLonAccuracy;
	}

	public SampleReferenceDto toReference() {
		return new SampleReferenceDto(getUuid());
	}

	@Override
	public SampleDto clone() throws CloneNotSupportedException {
		return (SampleDto) super.clone();
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public DeletionReason getDeletionReason() {
		return deletionReason;
	}

	public void setDeletionReason(DeletionReason deletionReason) {
		this.deletionReason = deletionReason;
	}

	public String getOtherDeletionReason() {
		return otherDeletionReason;
	}

	public void setOtherDeletionReason(String otherDeletionReason) {
		this.otherDeletionReason = otherDeletionReason;
	}

	public String getLaboratoryName() {
		return laboratoryName;
	}

	public void setLaboratoryName(String laboratoryName) {
		this.laboratoryName = laboratoryName;
	}

	public String getLaboratoryNumber() {
		return laboratoryNumber;
	}

	public void setLaboratoryNumber(String laboratoryNumber) {
		this.laboratoryNumber = laboratoryNumber;
	}

	public String getLaboratorySerotype() {
		return laboratorySerotype;
	}

	public void setLaboratorySerotype(String laboratorySerotype) {
		this.laboratorySerotype = laboratorySerotype;
	}

	public String getLaboratorySerotypeType() {
		return laboratorySerotypeType;
	}

	public void setLaboratorySerotypeType(String laboratorySerotypeType) {
		this.laboratorySerotypeType = laboratorySerotypeType;
	}

	public String getLaboratorySerotypeResults() {
		return laboratorySerotypeResults;
	}

	public void setLaboratorySerotypeResults(String laboratorySerotypeResults) {
		this.laboratorySerotypeResults = laboratorySerotypeResults;
	}

	public String getLaboratoryFinalResults() {
		return laboratoryFinalResults;
	}

	public void setLaboratoryFinalResults(String laboratoryFinalResults) {
		this.laboratoryFinalResults = laboratoryFinalResults;
	}

	public String getLaboratoryObservations() {
		return laboratoryObservations;
	}

	public void setLaboratoryObservations(String laboratoryObservations) {
		this.laboratoryObservations = laboratoryObservations;
	}

	public String getLaboratorySampleContainerOther() {
		return laboratorySampleContainerOther;
	}

	public void setLaboratorySampleContainerOther(String laboratorySampleContainerOther) {
		this.laboratorySampleContainerOther = laboratorySampleContainerOther;
	}

	public String getLaboratoryTestPerformedOther() {
		return laboratoryTestPerformedOther;
	}

	public void setLaboratoryTestPerformedOther(String laboratoryTestPerformedOther) {
		this.laboratoryTestPerformedOther = laboratoryTestPerformedOther;
	}

	public String getLaboratoryCytology() {
		return laboratoryCytology;
	}

	public void setLaboratoryCytology(String laboratoryCytology) {
		this.laboratoryCytology = laboratoryCytology;
	}

	public String getLaboratoryGramOther() {
		return laboratoryGramOther;
	}

	public void setLaboratoryGramOther(String laboratoryGramOther) {
		this.laboratoryGramOther = laboratoryGramOther;
	}

	public String getLaboratoryCultureOther() {
		return laboratoryCultureOther;
	}

	public void setLaboratoryCultureOther(String laboratoryCultureOther) {
		this.laboratoryCultureOther = laboratoryCultureOther;
	}

	public String getLaboratoryOtherTests() {
		return laboratoryOtherTests;
	}

	public void setLaboratoryOtherTests(String laboratoryOtherTests) {
		this.laboratoryOtherTests = laboratoryOtherTests;
	}

	public String getLaboratoryOtherTestsResults() {
		return laboratoryOtherTestsResults;
	}

	public void setLaboratoryOtherTestsResults(String laboratoryOtherTestsResults) {
		this.laboratoryOtherTestsResults = laboratoryOtherTestsResults;
	}

	public String getLaboratoryRdtResults() {
		return laboratoryRdtResults;
	}

	public void setLaboratoryRdtResults(String laboratoryRdtResults) {
		this.laboratoryRdtResults = laboratoryRdtResults;
	}

	public CaseClassification getLaboratoryFinalClassification() {
		return laboratoryFinalClassification;
	}

	public void setLaboratoryFinalClassification(CaseClassification laboratoryFinalClassification) {
		this.laboratoryFinalClassification = laboratoryFinalClassification;
	}

	public String getLaboratoryPcrType() {
		return laboratoryPcrType;
	}

	public void setLaboratoryPcrType(String laboratoryPcrType) {
		this.laboratoryPcrType = laboratoryPcrType;
	}

	public SampleContainerUsed getLaboratorySampleContainerReceived() {
		return laboratorySampleContainerReceived;
	}

	public void setLaboratorySampleContainerReceived(SampleContainerUsed laboratorySampleContainerReceived) {
		this.laboratorySampleContainerReceived = laboratorySampleContainerReceived;
	}

	public SpecimenCondition getLaboratorySampleCondition() {
		return laboratorySampleCondition;
	}

	public void setLaboratorySampleCondition(SpecimenCondition laboratorySampleCondition) {
		this.laboratorySampleCondition = laboratorySampleCondition;
	}

	public CsfAppearance getLaboratoryAppearanceOfCSF() {
		return laboratoryAppearanceOfCSF;
	}

	public void setLaboratoryAppearanceOfCSF(CsfAppearance laboratoryAppearanceOfCSF) {
		this.laboratoryAppearanceOfCSF = laboratoryAppearanceOfCSF;
	}

	public LabTest getLaboratoryTestPerformed() {
		return laboratoryTestPerformed;
	}

	public void setLaboratoryTestPerformed(LabTest laboratoryTestPerformed) {
		this.laboratoryTestPerformed = laboratoryTestPerformed;
	}

	public Gram getLaboratoryGram() {
		return laboratoryGram;
	}

	public void setLaboratoryGram(Gram laboratoryGram) {
		this.laboratoryGram = laboratoryGram;
	}

	public YesNo getLaboratoryRdtPerformed() {
		return laboratoryRdtPerformed;
	}

	public void setLaboratoryRdtPerformed(YesNo laboratoryRdtPerformed) {
		this.laboratoryRdtPerformed = laboratoryRdtPerformed;
	}

	public LatexCulture getLaboratoryLatex() {
		return laboratoryLatex;
	}

	public void setLaboratoryLatex(LatexCulture laboratoryLatex) {
		this.laboratoryLatex = laboratoryLatex;
	}

	public LatexCulture getLaboratoryCulture() {
		return laboratoryCulture;
	}

	public void setLaboratoryCulture(LatexCulture laboratoryCulture) {
		this.laboratoryCulture = laboratoryCulture;
	}

	public LatexCulture getLaboratoryPcrOptions() {
		return laboratoryPcrOptions;
	}

	public void setLaboratoryPcrOptions(LatexCulture laboratoryPcrOptions) {
		this.laboratoryPcrOptions = laboratoryPcrOptions;
	}

	public Antibiogram getLaboratoryCeftriaxone() {
		return laboratoryCeftriaxone;
	}

	public void setLaboratoryCeftriaxone(Antibiogram laboratoryCeftriaxone) {
		this.laboratoryCeftriaxone = laboratoryCeftriaxone;
	}

	public Antibiogram getLaboratoryPenicillinG() {
		return laboratoryPenicillinG;
	}

	public void setLaboratoryPenicillinG(Antibiogram laboratoryPenicillinG) {
		this.laboratoryPenicillinG = laboratoryPenicillinG;
	}

	public Antibiogram getLaboratoryAmoxycillin() {
		return laboratoryAmoxycillin;
	}

	public void setLaboratoryAmoxycillin(Antibiogram laboratoryAmoxycillin) {
		this.laboratoryAmoxycillin = laboratoryAmoxycillin;
	}

	public Antibiogram getLaboratoryOxacillin() {
		return laboratoryOxacillin;
	}

	public void setLaboratoryOxacillin(Antibiogram laboratoryOxacillin) {
		this.laboratoryOxacillin = laboratoryOxacillin;
	}

	public Antibiogram getLaboratoryAntibiogramOther() {
		return laboratoryAntibiogramOther;
	}

	public void setLaboratoryAntibiogramOther(Antibiogram laboratoryAntibiogramOther) {
		this.laboratoryAntibiogramOther = laboratoryAntibiogramOther;
	}

	public LabType getLaboratoryType() {
		return laboratoryType;
	}

	public void setLaboratoryType(LabType laboratoryType) {
		this.laboratoryType = laboratoryType;
	}
	public Date getLaboratoryDateResultsSentHealthFacility() {
		return laboratoryDateResultsSentHealthFacility;
	}

	public void setLaboratoryDateResultsSentHealthFacility(Date laboratoryDateResultsSentHealthFacility) {
		this.laboratoryDateResultsSentHealthFacility = laboratoryDateResultsSentHealthFacility;
	}

	public Date getLaboratoryDateResultsSentDSD() {
		return laboratoryDateResultsSentDSD;
	}

	public void setLaboratoryDateResultsSentDSD(Date laboratoryDateResultsSentDSD) {
		this.laboratoryDateResultsSentDSD = laboratoryDateResultsSentDSD;
	}

	public Date getLaboratorySampleDateReceived() {
		return laboratorySampleDateReceived;
	}

	public void setLaboratorySampleDateReceived(Date laboratorySampleDateReceived) {
		this.laboratorySampleDateReceived = laboratorySampleDateReceived;
	}

	public Date getLaboratoryDatePcrPerformed() {
		return laboratoryDatePcrPerformed;
	}

	public void setLaboratoryDatePcrPerformed(Date laboratoryDatePcrPerformed) {
		this.laboratoryDatePcrPerformed = laboratoryDatePcrPerformed;
	}

	public Date getDateSentToNationalRegLab() {
		return dateSentToNationalRegLab;
	}

	public void setDateSentToNationalRegLab(Date dateSentToNationalRegLab) {
		this.dateSentToNationalRegLab = dateSentToNationalRegLab;
	}

	public Date getDateDifferentiationSentToEpi() {
		return dateDifferentiationSentToEpi;
	}

	public void setDateDifferentiationSentToEpi(Date dateDifferentiationSentToEpi) {
		this.dateDifferentiationSentToEpi = dateDifferentiationSentToEpi;
	}

	public Date getDateDifferentiationReceivedFromEpi() {
		return dateDifferentiationReceivedFromEpi;
	}

	public void setDateDifferentiationReceivedFromEpi(Date dateDifferentiationReceivedFromEpi) {
		this.dateDifferentiationReceivedFromEpi = dateDifferentiationReceivedFromEpi;
	}

	public Date getDateIsolateSentForSequencing() {
		return dateIsolateSentForSequencing;
	}

	public void setDateIsolateSentForSequencing(Date dateIsolateSentForSequencing) {
		this.dateIsolateSentForSequencing = dateIsolateSentForSequencing;
	}

	public Date getDateSeqResultsSentToProgram() {
		return dateSeqResultsSentToProgram;
	}

	public void setDateSeqResultsSentToProgram(Date dateSeqResultsSentToProgram) {
		this.dateSeqResultsSentToProgram = dateSeqResultsSentToProgram;
	}

	public PosNeg getFinalLabResults() {
		return finalLabResults;
	}

	public void setFinalLabResults(PosNeg finalLabResults) {
		this.finalLabResults = finalLabResults;
	}

	public YesNoUnknown getImmunocompromisedStatusSuspected() {
		return immunocompromisedStatusSuspected;
	}

	public void setImmunocompromisedStatusSuspected(YesNoUnknown immunocompromisedStatusSuspected) {
		this.immunocompromisedStatusSuspected = immunocompromisedStatusSuspected;
	}

	public AFPClassification getAfpFinalClassification() {
		return afpFinalClassification;
	}

	public void setAfpFinalClassification(AFPClassification afpFinalClassification) {
		this.afpFinalClassification = afpFinalClassification;
	}

	public Date getDateFirstSpecimen() {
		return dateFirstSpecimen;
	}

	public void setDateFirstSpecimen(Date dateFirstSpecimen) {
		this.dateFirstSpecimen = dateFirstSpecimen;
	}

	public Date getDateSecondSpecimen() {
		return dateSecondSpecimen;
	}

	public void setDateSecondSpecimen(Date dateSecondSpecimen) {
		this.dateSecondSpecimen = dateSecondSpecimen;
	}

	public Date getDateSpecimenSentNationalLevel() {
		return dateSpecimenSentNationalLevel;
	}

	public void setDateSpecimenSentNationalLevel(Date dateSpecimenSentNationalLevel) {
		this.dateSpecimenSentNationalLevel = dateSpecimenSentNationalLevel;
	}

	public Date getDateSpecimenReceivedNationalLevel() {
		return dateSpecimenReceivedNationalLevel;
	}

	public void setDateSpecimenReceivedNationalLevel(Date dateSpecimenReceivedNationalLevel) {
		this.dateSpecimenReceivedNationalLevel = dateSpecimenReceivedNationalLevel;
	}

	public Date getDateSpecimenSentInter() {
		return dateSpecimenSentInter;
	}

	public void setDateSpecimenSentInter(Date dateSpecimenSentInter) {
		this.dateSpecimenSentInter = dateSpecimenSentInter;
	}

	public Date getDateSpecimenReceivedInter() {
		return dateSpecimenReceivedInter;
	}

	public void setDateSpecimenReceivedInter(Date dateSpecimenReceivedInter) {
		this.dateSpecimenReceivedInter = dateSpecimenReceivedInter;
	}

	public SpecimenCondition getStatusSpecimenReceptionAtLab() {
		return statusSpecimenReceptionAtLab;
	}

	public void setStatusSpecimenReceptionAtLab(SpecimenCondition statusSpecimenReceptionAtLab) {
		this.statusSpecimenReceptionAtLab = statusSpecimenReceptionAtLab;
	}

	public Date getDateCombinedCellCultureResults() {
		return dateCombinedCellCultureResults;
	}

	public void setDateCombinedCellCultureResults(Date dateCombinedCellCultureResults) {
		this.dateCombinedCellCultureResults = dateCombinedCellCultureResults;
	}

	public YesNo getW1() {
		return w1;
	}

	public void setW1(YesNo w1) {
		this.w1 = w1;
	}

	public YesNo getW2() {
		return w2;
	}

	public void setW2(YesNo w2) {
		this.w2 = w2;
	}

	public YesNo getW3() {
		return w3;
	}

	public void setW3(YesNo w3) {
		this.w3 = w3;
	}

	public YesNoUnknown getDiscordant() {
		return discordant;
	}

	public void setDiscordant(YesNoUnknown discordant) {
		this.discordant = discordant;
	}

	public YesNo getsL1() {
		return sL1;
	}

	public void setsL1(YesNo sL1) {
		this.sL1 = sL1;
	}

	public YesNo getsL2() {
		return sL2;
	}

	public void setsL2(YesNo sL2) {
		this.sL2 = sL2;
	}

	public YesNo getsL3() {
		return sL3;
	}

	public void setsL3(YesNo sL3) {
		this.sL3 = sL3;
	}

	public Date getDateFollowUpExam() {
		return dateFollowUpExam;
	}

	public void setDateFollowUpExam(Date dateFollowUpExam) {
		this.dateFollowUpExam = dateFollowUpExam;
	}

	public InjectionSite getResidualAnalysis() {
		return residualAnalysis;
	}

	public void setResidualAnalysis(InjectionSite residualAnalysis) {
		this.residualAnalysis = residualAnalysis;
	}

	public AfpResult getResultExam() {
		return resultExam;
	}

	public void setResultExam(AfpResult resultExam) {
		this.resultExam = resultExam;
	}

	public YesNo getPositiveViralCulture() {
		return positiveViralCulture;
	}

	public void setPositiveViralCulture(YesNo positiveViralCulture) {
		this.positiveViralCulture = positiveViralCulture;
	}

	public YesNo getPositiveRealTime() {
		return positiveRealTime;
	}

	public void setPositiveRealTime(YesNo positiveRealTime) {
		this.positiveRealTime = positiveRealTime;
	}

	public YesNo getFourFoldRise() {
		return fourFoldRise;
	}

	public void setFourFoldRise(YesNo fourFoldRise) {
		this.fourFoldRise = fourFoldRise;
	}

	public InfluenzaVirus getInfluenzaVirus() {
		return influenzaVirus;
	}

	public void setInfluenzaVirus(InfluenzaVirus influenzaVirus) {
		this.influenzaVirus = influenzaVirus;
	}

	public String getOtherInfluenzaVirus() {
		return otherInfluenzaVirus;
	}

	public void setOtherInfluenzaVirus(String otherInfluenzaVirus) {
		this.otherInfluenzaVirus = otherInfluenzaVirus;
	}

	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	public String getStateTreatmentAdministered() {
		return stateTreatmentAdministered;
	}

	public void setStateTreatmentAdministered(String stateTreatmentAdministered) {
		this.stateTreatmentAdministered = stateTreatmentAdministered;
	}

	public Disease getSuspectedDisease() {
		return suspectedDisease;
	}

	public void setSuspectedDisease(Disease suspectedDisease) {
		this.suspectedDisease = suspectedDisease;
	}

	public String getLabLocation() {
		return labLocation;
	}

	public void setLabLocation(String labLocation) {
		this.labLocation = labLocation;
	}

	public Date getDateLabReceivedSpecimen() {
		return dateLabReceivedSpecimen;
	}

	public void setDateLabReceivedSpecimen(Date dateLabReceivedSpecimen) {
		this.dateLabReceivedSpecimen = dateLabReceivedSpecimen;
	}

	public Date getDateResultsSentToClinician() {
		return dateResultsSentToClinician;
	}

	public void setDateResultsSentToClinician(Date dateResultsSentToClinician) {
		this.dateResultsSentToClinician = dateResultsSentToClinician;
	}

	public Date getDateSpecimenSentToLab() {
		return dateSpecimenSentToLab;
	}

	public void setDateSpecimenSentToLab(Date dateSpecimenSentToLab) {
		this.dateSpecimenSentToLab = dateSpecimenSentToLab;
	}

}
