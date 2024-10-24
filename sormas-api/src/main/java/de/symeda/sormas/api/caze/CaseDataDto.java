/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2022 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.api.caze;

import static de.symeda.sormas.api.CountryHelper.COUNTRY_CODE_FRANCE;
import static de.symeda.sormas.api.CountryHelper.COUNTRY_CODE_GERMANY;
import static de.symeda.sormas.api.CountryHelper.COUNTRY_CODE_SWITZERLAND;
import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_BIG;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.caseimport.MotherVaccinationStatus;
import de.symeda.sormas.api.afpimmunization.AfpImmunizationDto;
import de.symeda.sormas.api.foodhistory.FoodHistoryDto;
import de.symeda.sormas.api.infrastructure.facility.DhimsFacility;
import de.symeda.sormas.api.person.Sex;
import de.symeda.sormas.api.riskfactor.RiskFactorDto;
import de.symeda.sormas.api.sixtyday.SixtyDayDto;
import de.symeda.sormas.api.user.UserDto;
import de.symeda.sormas.api.utils.*;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.ImportIgnore;
import de.symeda.sormas.api.activityascase.ActivityAsCaseDto;
import de.symeda.sormas.api.caze.maternalhistory.MaternalHistoryDto;
import de.symeda.sormas.api.caze.porthealthinfo.PortHealthInfoDto;
import de.symeda.sormas.api.clinicalcourse.ClinicalCourseDto;
import de.symeda.sormas.api.clinicalcourse.HealthConditionsDto;
import de.symeda.sormas.api.common.DeletionReason;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.contact.FollowUpStatus;
import de.symeda.sormas.api.contact.QuarantineType;
import de.symeda.sormas.api.disease.DiseaseVariant;
import de.symeda.sormas.api.epidata.EpiDataDto;
import de.symeda.sormas.api.event.EventParticipantDto;
import de.symeda.sormas.api.exposure.ExposureDto;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.hospitalization.HospitalizationDto;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.facility.FacilityReferenceDto;
import de.symeda.sormas.api.infrastructure.facility.FacilityType;
import de.symeda.sormas.api.infrastructure.pointofentry.PointOfEntryReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.person.PersonReferenceDto;
import de.symeda.sormas.api.sormastosormas.S2SIgnoreProperty;
import de.symeda.sormas.api.sormastosormas.SormasToSormasConfig;
import de.symeda.sormas.api.sormastosormas.SormasToSormasShareableDto;
import de.symeda.sormas.api.symptoms.SymptomsDto;
import de.symeda.sormas.api.therapy.TherapyDto;
import de.symeda.sormas.api.travelentry.TravelEntryDto;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.pseudonymization.Pseudonymizer;
import de.symeda.sormas.api.utils.pseudonymization.valuepseudonymizers.LatitudePseudonymizer;
import de.symeda.sormas.api.utils.pseudonymization.valuepseudonymizers.LongitudePseudonymizer;

@DependingOnFeatureType(featureType = FeatureType.CASE_SURVEILANCE)
public class CaseDataDto extends SormasToSormasShareableDto implements Serializable{

	private static final long serialVersionUID = 5007131477733638086L;
	private static final long MILLISECONDS_30_DAYS = 30L * 24L * 60L * 60L * 1000L;

	public static final long APPROXIMATE_JSON_SIZE_IN_BYTES = 123458;

	public static final String I18N_PREFIX = "CaseData";

	public static final String CASE_CLASSIFICATION = "caseClassification";
	public static final String CASE_IDENTIFICATION_SOURCE = "caseIdentificationSource";
	public static final String SCREENING_TYPE = "screeningType";
	public static final String CLASSIFICATION_USER = "classificationUser";
	public static final String CLASSIFICATION_DATE = "classificationDate";
	public static final String CLASSIFICATION_COMMENT = "classificationComment";
	public static final String CLASSIFIED_BY = "classifiedBy";
	public static final String CLINICAL_CONFIRMATION = "clinicalConfirmation";
	public static final String EPIDEMIOLOGICAL_CONFIRMATION = "epidemiologicalConfirmation";
	public static final String LABORATORY_DIAGNOSTIC_CONFIRMATION = "laboratoryDiagnosticConfirmation";
	public static final String INVESTIGATION_STATUS = "investigationStatus";
	public static final String VACCINATION_ROUTINE= "vaccinationRoutine";
	public static final String VACCINATION_ROUTINE_DATE = "vaccinationRoutineDate";
	public static final String PERSON = "person";
	public static final String PATIENT_CLIENT = "patientClient";
	public static final String DEMOGRAPHIC = "demographicDetails";
	public static final String DISEASE = "disease";
	public static final String DISEASE_VARIANT = "diseaseVariant";
	public static final String DISEASE_DETAILS = "diseaseDetails";
	public static final String DISEASE_VARIANT_DETAILS = "diseaseVariantDetails";
	public static final String PLAGUE_TYPE = "plagueType";
	public static final String DENGUE_FEVER_TYPE = "dengueFeverType";
	public static final String IDSR_DIAGNOSIS = "idsrDiagnosis";
	public static final String SPECIFY_EVENT_DIAGNOSIS = "specifyEventDiagnosis";
	public static final String RABIES_TYPE = "rabiesType";
	public static final String RESPONSIBLE_REGION = "responsibleRegion";
	public static final String RESPONSIBLE_DISTRICT = "responsibleDistrict";
	public static final String RESPONSIBLE_COMMUNITY = "responsibleCommunity";
	public static final String REGION = "region";
	public static final String DISTRICT = "district";
	public static final String COMMUNITY = "community";
	public static final String HEALTH_FACILITY = "healthFacility";
	public static final String HOME_ADDRESS_RECREATIONAL = "homeAddressRecreational";
	public static final String HEALTH_FACILITY_DETAILS = "healthFacilityDetails";
	public static final String MOBILE_TEAM_NO = "mobileTeamNo";
	public static final String INFORMATION_GIVEN_BY = "informationGivenBy";
	public static final String FAMILY_LINK_WITH_PATIENT = "familyLinkWithPatient";
	public static final String REPORTING_USER = "reportingUser";
	public static final String REPORT_DATE = "reportDate";
	public static final String INVESTIGATED_DATE = "investigatedDate";
	public static final String DATE_FORM_SENT_TO_DISTRICT = "dateFormSentToDistrict";
	public static final String DATE_FORM_RECEIVED_AT_DISTRICT = "districtLevelDate";
	public static final String DATE_FORM_RECEIVED_AT_REGION = "regionLevelDate";
	public static final String DATE_FORM_RECEIVED_AT_NATIONAL = "nationalLevelDate";
	public static final String SURVEILLANCE_OFFICER = "surveillanceOfficer";
	public static final String SYMPTOMS = "symptoms";
	public static final String HOSPITALIZATION = "hospitalization";
	public static final String RISK_FACTOR = "riskFactor";
	public static final String RISK_FACTOR_NAME = "riskFactorName";
	public static final String ILLNESS_INFO = "illnessInformation";
	public static final String SIXTY_DAY = "sixtyDay";
	public static final String AFP_IMMUNIZATION = "afpImmunization";
	public static final String FOOD_HISTORY_TAB = "foodHistory";
	public static final String FOOD_SAMPLE_TESTING = "foodSampleTesting";
	public static final String EPI_DATA = "epiData";
	public static final String THERAPY = "therapy";
	public static final String CLINICAL_COURSE = "clinicalCourse";
	public static final String MATERNAL_HISTORY = "maternalHistory";
	public static final String PORT_HEALTH_INFO = "portHealthInfo";
	public static final String HEALTH_CONDITIONS = "healthConditions";
	public static final String PREGNANT = "pregnant";
	public static final String IPSAMPLESENT = "ipSampleSent";
	public static final String IPSAMPLERESULTS = "ipSampleResults";
	public static final String VACCINATION_STATUS = "vaccinationStatus";
	public static final String VACCINATION_TYPE = "vaccinationType";
	public static final String VACCINE_TYPE = "vaccineType";
	public static final String NUMBER_OF_DOSES = "numberOfDoses";
	public static final String VACCINATION_DATE = "vaccinationDate";
	public static final String LAST_VACCINATION_DATE = "lastVaccinationDate";
	public static final String SMALLPOX_VACCINATION_SCAR = "smallpoxVaccinationScar";
	public static final String SMALLPOX_VACCINATION_RECEIVED = "smallpoxVaccinationReceived";
	public static final String SMALLPOX_LAST_VACCINATION_DATE = "smallpoxLastVaccinationDate";
	public static final String EPID_NUMBER = "epidNumber";
	public static final String NOTIFIED_BY = "notifiedBy";
	public static final String DATE_OF_NOTIFICATION = "dateOfNotification";
	public static final String NOTIFIED_BY_LIST = "notifiedByList";
	public static final String NOTIFIED_OTHER = "notifiedOther";
	public static final String DATE_OF_INVESTIGATION = "dateOfInvestigation";
	public static final String REPORT_LAT = "reportLat";
	public static final String REPORT_LON = "reportLon";
	public static final String REPORT_LAT_LON_ACCURACY = "reportLatLonAccuracy";
	public static final String REPORTING_OFFICER_NAME = "reportingOfficerName";
	public static final String REPORTING_OFFICER_TITLE = "reportingOfficerTitle";
	public static final String FUNCTION_OF_REPORTING_OFFICER = "functionOfReportingOfficer";
	public static final String REPORTING_OFFICER_CONTACT_PHONE = "reportingOfficerContactPhone";
	public static final String REPORTING_OFFICER_EMAIL = "reportingOfficerEmail";
	public static final String OUTCOME = "outcome";
	public static final String OUTCOME_DATE = "outcomeDate";
	public static final String SEQUELAE = "sequelae";
	public static final String SEQUELAE_DETAILS = "sequelaeDetails";
	public static final String CLINICIAN_NAME = "clinicianName";
	public static final String CLINICIAN_PHONE = "clinicianPhone";
	public static final String CLINICIAN_EMAIL = "clinicianEmail";
	public static final String NOTIFYING_CLINIC = "notifyingClinic";
	public static final String NOTIFYING_CLINIC_DETAILS = "notifyingClinicDetails";
	public static final String CASE_ORIGIN = "caseOrigin";
	public static final String POINT_OF_ENTRY = "pointOfEntry";
	public static final String POINT_OF_ENTRY_DETAILS = "pointOfEntryDetails";
	public static final String HOSPITAL_NAME = "hospitalName";
	public static final String ADDITIONAL_DETAILS = "additionalDetails";
	public static final String EXTERNAL_ID = "externalID";
	public static final String EXTERNAL_TOKEN = "externalToken";
	public static final String INTERNAL_TOKEN = "internalToken";
	public static final String SHARED_TO_COUNTRY = "sharedToCountry";
	public static final String NOSOCOMIAL_OUTBREAK = "nosocomialOutbreak";
	public static final String INFECTION_SETTING = "infectionSetting";
	public static final String QUARANTINE = "quarantine";
	public static final String QUARANTINE_TYPE_DETAILS = "quarantineTypeDetails";
	public static final String QUARANTINE_FROM = "quarantineFrom";
	public static final String QUARANTINE_TO = "quarantineTo";
	public static final String QUARANTINE_HELP_NEEDED = "quarantineHelpNeeded";
	public static final String QUARANTINE_ORDERED_VERBALLY = "quarantineOrderedVerbally";
	public static final String QUARANTINE_ORDERED_OFFICIAL_DOCUMENT = "quarantineOrderedOfficialDocument";
	public static final String QUARANTINE_ORDERED_VERBALLY_DATE = "quarantineOrderedVerballyDate";
	public static final String QUARANTINE_ORDERED_OFFICIAL_DOCUMENT_DATE = "quarantineOrderedOfficialDocumentDate";
	public static final String QUARANTINE_HOME_POSSIBLE = "quarantineHomePossible";
	public static final String QUARANTINE_HOME_POSSIBLE_COMMENT = "quarantineHomePossibleComment";
	public static final String QUARANTINE_HOME_SUPPLY_ENSURED = "quarantineHomeSupplyEnsured";
	public static final String QUARANTINE_HOME_SUPPLY_ENSURED_COMMENT = "quarantineHomeSupplyEnsuredComment";
	public static final String QUARANTINE_EXTENDED = "quarantineExtended";
	public static final String QUARANTINE_REDUCED = "quarantineReduced";
	public static final String QUARANTINE_OFFICIAL_ORDER_SENT = "quarantineOfficialOrderSent";
	public static final String QUARANTINE_OFFICIAL_ORDER_SENT_DATE = "quarantineOfficialOrderSentDate";
	public static final String POSTPARTUM = "postpartum";
	public static final String TRIMESTER = "trimester";
	public static final String OVERWRITE_FOLLOW_UP_UNTIL = "overwriteFollowUpUntil";
	public static final String FOLLOW_UP_STATUS = "followUpStatus";
	public static final String FOLLOW_UP_COMMENT = "followUpComment";
	public static final String FOLLOW_UP_UNTIL = "followUpUntil";
	public static final String VISITS = "visits";
	public static final String FACILITY_TYPE = "facilityType";
	public static final String DHIMS_FACILITY_TYPE = "dhimsFacilityType";
	public static final String AFP_FACILITY_OPTIONS = "afpFacilityOptions";
	public static final String CASE_ID_ISM = "caseIdIsm";
	public static final String CONTACT_TRACING_FIRST_CONTACT_TYPE = "contactTracingFirstContactType";
	public static final String CONTACT_TRACING_FIRST_CONTACT_DATE = "contactTracingFirstContactDate";
	public static final String WAS_IN_QUARANTINE_BEFORE_ISOLATION = "wasInQuarantineBeforeIsolation";
	public static final String QUARANTINE_REASON_BEFORE_ISOLATION = "quarantineReasonBeforeIsolation";
	public static final String QUARANTINE_REASON_BEFORE_ISOLATION_DETAILS = "quarantineReasonBeforeIsolationDetails";
	public static final String END_OF_ISOLATION_REASON = "endOfIsolationReason";
	public static final String END_OF_ISOLATION_REASON_DETAILS = "endOfIsolationReasonDetails";
	public static final String CASE_TRANSMISSION_CLASSIFICATION = "caseTransmissionClassification";
	public static final String SECOND_VACCINATION_DATE = "secondVaccinationDate";
	public static final String OTHERCASEOUTCOMEDETAILS = "specifyOtherOutcome";

	public static final String PROHIBITION_TO_WORK = "prohibitionToWork";
	public static final String PROHIBITION_TO_WORK_FROM = "prohibitionToWorkFrom";
	public static final String PROHIBITION_TO_WORK_UNTIL = "prohibitionToWorkUntil";

	public static final String RE_INFECTION = "reInfection";
	public static final String PREVIOUS_INFECTION_DATE = "previousInfectionDate";
	public static final String REINFECTION_STATUS = "reinfectionStatus";

	public static final String BLOOD_ORGAN_OR_TISSUE_DONATED = "bloodOrganOrTissueDonated";

	public static final String NOT_A_CASE_REASON_NEGATIVE_TEST = "notACaseReasonNegativeTest";
	public static final String NOT_A_CASE_REASON_PHYSICIAN_INFORMATION = "notACaseReasonPhysicianInformation";
	public static final String NOT_A_CASE_REASON_DIFFERENT_PATHOGEN = "notACaseReasonDifferentPathogen";
	public static final String NOT_A_CASE_REASON_OTHER = "notACaseReasonOther";
	public static final String NOT_A_CASE_REASON_DETAILS = "notACaseReasonDetails";
	public static final String FOLLOW_UP_STATUS_CHANGE_DATE = "followUpStatusChangeDate";
	public static final String FOLLOW_UP_STATUS_CHANGE_USER = "followUpStatusChangeUser";
	public static final String DONT_SHARE_WITH_REPORTING_TOOL = "dontShareWithReportingTool";
	public static final String CASE_REFERENCE_DEFINITION = "caseReferenceDefinition";
	public static final String PREVIOUS_QUARANTINE_TO = "previousQuarantineTo";
	public static final String QUARANTINE_CHANGE_COMMENT = "quarantineChangeComment";

	public static final String EXTERNAL_DATA = "externalData";
	public static final String DELETION_REASON = "deletionReason";
	public static final String OTHER_DELETION_REASON = "otherDeletionReason";

	public static final String NEW_EXISTING = "existingCase";

	public static final String MOTHER_VACCINATED_WITH_TT = "motherVaccinatedWithTT";
	public static final String MOTHER_HAVE_CARD = "motherHaveCard";
	public static final String MOTHER_NUMBER_OF_DOSES = "motherNumberOfDoses";
	public static final String MOTHER_VACCINATION_STATUS = "motherVaccinationStatus";
	public static final String MOTHER_TT_DATE_ONE = "motherTTDateOne";
	public static final String MOTHER_TT_DATE_TWO = "motherTTDateTwo";
	public static final String MOTHER_TT_DATE_THREE = "motherTTDateThree";
	public static final String MOTHER_TT_DATE_FOUR = "motherTTDateFour";
	public static final String MOTHER_TT_DATE_FIVE = "motherTTDateFive";
	public static final String MOTHER_LAST_DOSE_DATE = "motherLastDoseDate";

	public static final String SEEN_IN_OPD = "seenInOPD";
	public static final String ADMITTED_IN_OPD = "admittedInOPD";
	public static final String MOTHER_GIVEN_PROTECTIVE_DOSE_TT = "motherGivenProtectiveDoseTT";
	public static final String MOTHER_GIVEN_PROTECTIVE_DOSE_TT_DATE = "motherGivenProtectiveDoseTTDate";
	public static final String SUPPLEMENTAL_IMMUNIZATION = "supplementalImmunization";
	public static final String SUPPLEMENTAL_IMMUNIZATION_DETAILS = "supplementalImmunizationDetails";

	public static final String ADDRESS_MPOX = "addressMpox";
	public static final String VILLAGE = "village";
	public static final String CITY = "city";
	public static final String NATIONALITY = "nationality";
	public static final String ETHNICITY = "ethnicity";
	public static final String OCCUPATION = "occupation";
	public static final String DISTRICT_OF_RESIDENCE = "districtOfResidence";
	public static final String PATIENT_FIRST_NAME = "patientFirstName";
	public static final String PATIENT_LAST_NAME = "patientLastName";
	public static final String PATIENT_OTHER_NAMES = "patientOtherNames";
	public static final String PATIENT_DOB_DD = "patientDobDD";
	public static final String PATIENT_DOB_MM = "patientDobMM";
	public static final String PATIENT_DOB_YY = "patientDobYY";
	public static final String PATIENT_AGE_YEAR = "patientAgeYear";
	public static final String PATIENT_AGE_MONTH = "patientAgeMonth";
	public static final String PATIENT_SEX = "patientSex";
	public static final String REPORTING_VILLAGE = "reportingVillage";
	public static final String REPORTING_ZONE = "reportingZone";
	public static final String OTHER_NOTES_AND_OBSERVATIONS = "otherNotesAndObservations";
	public static final String DATE_LATEST_UPDATE_RECORD = "dateLatestUpdateRecord";
	public static final String NUMBER_OF_PEOPLE_IN_SAME_HOUSEHOLD = "numberOfPeopleInSameHousehold";
	public static final String INVESTIGATION_OFFICER_NAME = "investigationOfficerName";
	public static final String INVESTIGATION_OFFICER_POSITION = "investigationOfficerPosition";
	public static final String FORM_COMPLETED_BY_NAME = "formCompletedByName";
	public static final String FORM_COMPLETED_BY_POSITION = "formCompletedByPosition";
	public static final String FORM_COMPLETED_BY_CELL_PHONE_NO = "formCompletedByCellPhoneNo";
	public static final String NAME_OF_VILLAGE_PERSON_GOT_ILL = "nameOfVillagePersonGotIll";
	public static final String AGE_YEAR_MONTH = "ageYearMonth";

	// Fields are declared in the order they should appear in the import template

	private String patientFirstName;
	private String patientLastName;
	private String patientOtherNames;
	private Integer patientDobDD;
	private Integer patientDobMM;
	private Integer patientDobYY;
	private Integer patientAgeYear;
	private Integer patientAgeMonth;
	private Sex patientSex;
	@Outbreaks
	@NotNull(message = Validations.validDisease)
	private Disease disease;
	private DiseaseVariant diseaseVariant;
	@Outbreaks
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String diseaseDetails;
	@Outbreaks
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String diseaseVariantDetails;
	@Diseases({
		Disease.PLAGUE })
	@Outbreaks
	private PlagueType plagueType;
	@Diseases({
		Disease.DENGUE })
	@Outbreaks
	private DengueFeverType dengueFeverType;
	@Diseases({
			Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS })
	@Outbreaks
	private IdsrType idsrDiagnosis;
	@Diseases({
		Disease.RABIES })
	@Outbreaks
	private RabiesType rabiesType;
	@NotNull(message = Validations.validPerson)
	@EmbeddedPersonalData
	private PersonReferenceDto person;
	@Outbreaks
	@HideForCountries(countries = {
		COUNTRY_CODE_GERMANY,
		COUNTRY_CODE_SWITZERLAND })
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String epidNumber;
	@Outbreaks
//	@NotNull(message = Validations.validReportDateTime)
	private Date reportDate;
	@Outbreaks
	private UserReferenceDto reportingUser;
	@HideForCountries(countries = {
		COUNTRY_CODE_FRANCE,
		COUNTRY_CODE_GERMANY,
		COUNTRY_CODE_SWITZERLAND })
	private Date regionLevelDate;
	@HideForCountries(countries = {
		COUNTRY_CODE_FRANCE,
		COUNTRY_CODE_GERMANY,
		COUNTRY_CODE_SWITZERLAND })
	private Date nationalLevelDate;
	@Outbreaks
	@HideForCountries(countries = {
		COUNTRY_CODE_FRANCE,
		COUNTRY_CODE_GERMANY,
		COUNTRY_CODE_SWITZERLAND })
	private Date districtLevelDate;
	private Date dateFormSentToDistrict;
	@Outbreaks
	private CaseClassification caseClassification;
	@HideForCountriesExcept
	private CaseIdentificationSource caseIdentificationSource;
	@HideForCountriesExcept
	private ScreeningType screeningType;
	@Outbreaks
	private UserReferenceDto classificationUser;
	@Outbreaks
	private Date classificationDate;
	@Outbreaks
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String classificationComment;

	private YesNoUnknown clinicalConfirmation;
	private YesNoUnknown epidemiologicalConfirmation;
	private YesNoUnknown laboratoryDiagnosticConfirmation;

	@Outbreaks
	private InvestigationStatus investigationStatus;
	@Outbreaks
	private Date investigatedDate;
	@Outbreaks
	private CaseOutcome outcome;
	@Outbreaks
	private Date outcomeDate;
	private YesNoUnknown sequelae;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String sequelaeDetails;
	@NotNull(message = Validations.validResponsibleRegion)
	private RegionReferenceDto responsibleRegion;
	@NotNull(message = Validations.validResponsibleDistrict)
	private DistrictReferenceDto responsibleDistrict;
	@Outbreaks
	@PersonalData
	@SensitiveData
	private CommunityReferenceDto responsibleCommunity;

	@Outbreaks
	private RegionReferenceDto region;
	@Outbreaks
	private DistrictReferenceDto district;
	@Outbreaks
	@PersonalData
	@SensitiveData
	private CommunityReferenceDto community;
	@PersonalData(mandatoryField = true)
	@SensitiveData(mandatoryField = true)
	private FacilityType facilityType;
	private DhimsFacility dhimsFacilityType;
	private AFPFacilityOptions afpFacilityOptions;

	@Outbreaks
	@PersonalData(mandatoryField = true)
	@SensitiveData(mandatoryField = true)
	private FacilityReferenceDto healthFacility;
	@Outbreaks
	@PersonalData
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String healthFacilityDetails;
	private String mobileTeamNo;
	private String informationGivenBy;
	private String familyLinkWithPatient;

	@Valid
	private HealthConditionsDto healthConditions;

	private YesNo pregnant;
	private YesNoUnknown ipSampleSent;
	private Disease ipSampleResults;
	@Diseases({
		Disease.AFP,
		Disease.GUINEA_WORM,
		Disease.MEASLES,
		Disease.POLIO,
		Disease.YELLOW_FEVER,
		Disease.CSM,
		Disease.RABIES,
		Disease.AHF,
		Disease.DENGUE,
		Disease.ANTHRAX,
		Disease.CORONAVIRUS,
		Disease.OTHER })
	@Outbreaks
	private VaccinationStatus vaccinationStatus;

	private VaccinationRoutine vaccinationRoutine;

	@Diseases({
		Disease.MONKEYPOX })
	private YesNoUnknown smallpoxVaccinationScar;
	@Diseases({
		Disease.MONKEYPOX })
	private YesNoUnknown smallpoxVaccinationReceived;
	@Diseases({
		Disease.MONKEYPOX })
	private Date smallpoxLastVaccinationDate;
	@Outbreaks
	@SensitiveData
	private UserReferenceDto surveillanceOfficer;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	@DependingOnUserRight(UserRight.CASE_CLINICIAN_VIEW)
	private String clinicianName;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	@DependingOnUserRight(UserRight.CASE_CLINICIAN_VIEW)
	private String clinicianPhone;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	@DependingOnUserRight(UserRight.CASE_CLINICIAN_VIEW)
	private String clinicianEmail;

	private String reportingOfficerName;
	private String reportingOfficerTitle;
	private String functionOfReportingOfficer;
	private String reportingOfficerContactPhone;
	private String reportingOfficerEmail;
	private String homeAddressRecreational;
	private String hospitalName;
	private Date vaccinationRoutineDate;
	@Diseases({
		Disease.CONGENITAL_RUBELLA })
	private HospitalWardType notifyingClinic;
	@Diseases({
		Disease.CONGENITAL_RUBELLA })
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String notifyingClinicDetails;
	@Deprecated
	@SensitiveData
	private UserReferenceDto caseOfficer;
	@SensitiveData
	@Pseudonymizer(LatitudePseudonymizer.class)
	@Min(value = -90, message = Validations.numberTooSmall)
	@Max(value = 90, message = Validations.numberTooBig)
	private Double reportLat;
	@SensitiveData
	@Pseudonymizer(LongitudePseudonymizer.class)
	@Min(value = -180, message = Validations.numberTooSmall)
	@Max(value = 180, message = Validations.numberTooBig)
	private Double reportLon;
	private Float reportLatLonAccuracy;
	@Valid
	private HospitalizationDto hospitalization;
	@Valid
	private SixtyDayDto sixtyDay;
	@Valid
	private AfpImmunizationDto afpImmunization;
	@Valid
	private FoodHistoryDto foodHistory;
	@Valid
	private RiskFactorDto riskFactor;
	@Valid
	private SymptomsDto symptoms;
	@Valid
	private EpiDataDto epiData;
	@Valid
	private TherapyDto therapy;
	@Valid
	private ClinicalCourseDto clinicalCourse;
	@Valid
	private MaternalHistoryDto maternalHistory;
	@Size(max = 32, message = Validations.textTooLong)
	private String creationVersion;
	@SensitiveData
	@Valid
	private PortHealthInfoDto portHealthInfo;
	private CaseOrigin caseOrigin;
	@PersonalData(mandatoryField = true)
	@SensitiveData(mandatoryField = true)
	private PointOfEntryReferenceDto pointOfEntry;
	@PersonalData
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String pointOfEntryDetails;
	@S2SIgnoreProperty(configProperty = SormasToSormasConfig.SORMAS2SORMAS_IGNORE_ADDITIONAL_DETAILS)
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_TEXT, message = Validations.textTooLong)
	private String additionalDetails;
	@HideForCountriesExcept(countries = {
		COUNTRY_CODE_GERMANY,
		COUNTRY_CODE_SWITZERLAND })
	@S2SIgnoreProperty(configProperty = SormasToSormasConfig.SORMAS2SORMAS_IGNORE_EXTERNAL_ID)
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String externalID;
	@S2SIgnoreProperty(configProperty = SormasToSormasConfig.SORMAS2SORMAS_IGNORE_EXTERNAL_TOKEN)
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String externalToken;
	@S2SIgnoreProperty(configProperty = SormasToSormasConfig.SORMAS2SORMAS_IGNORE_INTERNAL_TOKEN)
	@Size(max = FieldConstraints.CHARACTER_LIMIT_TEXT, message = Validations.textTooLong)
	private String internalToken;
	private boolean sharedToCountry;
	@HideForCountriesExcept
	private boolean nosocomialOutbreak;
	@HideForCountriesExcept
	private InfectionSetting infectionSetting;
	private QuarantineType quarantine;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String quarantineTypeDetails;
	private Date quarantineFrom;
	private Date quarantineTo;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String quarantineHelpNeeded;
	@HideForCountriesExcept(countries = {
		COUNTRY_CODE_GERMANY,
		COUNTRY_CODE_SWITZERLAND })
	private boolean quarantineOrderedVerbally;
	@HideForCountriesExcept(countries = {
		COUNTRY_CODE_GERMANY,
		COUNTRY_CODE_SWITZERLAND })
	private boolean quarantineOrderedOfficialDocument;
	@HideForCountriesExcept(countries = {
		COUNTRY_CODE_GERMANY,
		COUNTRY_CODE_SWITZERLAND })
	private Date quarantineOrderedVerballyDate;
	@HideForCountriesExcept(countries = {
		COUNTRY_CODE_GERMANY,
		COUNTRY_CODE_SWITZERLAND })
	private Date quarantineOrderedOfficialDocumentDate;
	@HideForCountriesExcept
	private YesNoUnknown quarantineHomePossible;
	@HideForCountriesExcept
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String quarantineHomePossibleComment;
	@HideForCountriesExcept
	private YesNoUnknown quarantineHomeSupplyEnsured;
	@HideForCountriesExcept
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String quarantineHomeSupplyEnsuredComment;
	private boolean quarantineExtended;
	private boolean quarantineReduced;
	@HideForCountriesExcept(countries = {
		COUNTRY_CODE_GERMANY,
		COUNTRY_CODE_SWITZERLAND })
	private boolean quarantineOfficialOrderSent;
	@HideForCountriesExcept(countries = {
		COUNTRY_CODE_GERMANY,
		COUNTRY_CODE_SWITZERLAND })
	private Date quarantineOfficialOrderSentDate;
	private YesNo postpartum;
	private Trimester trimester;
	private FollowUpStatus followUpStatus;
	@SensitiveData
	@Size(max = CHARACTER_LIMIT_BIG, message = Validations.textTooLong)
	private String followUpComment;
	private Date followUpUntil;
	private boolean overwriteFollowUpUntil;
	private VaccineTypes vaccineType;
	private String numberOfDoses;

	@HideForCountriesExcept(countries = COUNTRY_CODE_SWITZERLAND)
	private Integer caseIdIsm;
	@HideForCountriesExcept(countries = COUNTRY_CODE_SWITZERLAND)
	private ContactTracingContactType contactTracingFirstContactType;
	@HideForCountriesExcept(countries = COUNTRY_CODE_SWITZERLAND)
	private Date contactTracingFirstContactDate;
	@HideForCountriesExcept(countries = COUNTRY_CODE_SWITZERLAND)
	private YesNoUnknown wasInQuarantineBeforeIsolation;
	@HideForCountriesExcept(countries = COUNTRY_CODE_SWITZERLAND)
	private QuarantineReason quarantineReasonBeforeIsolation;
	@HideForCountriesExcept(countries = COUNTRY_CODE_SWITZERLAND)
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String quarantineReasonBeforeIsolationDetails;
	@HideForCountriesExcept(countries = COUNTRY_CODE_SWITZERLAND)
	private EndOfIsolationReason endOfIsolationReason;
	@HideForCountriesExcept(countries = COUNTRY_CODE_SWITZERLAND)
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String endOfIsolationReasonDetails;
	private TransmissionClassification caseTransmissionClassification;
	private String specifyOtherOutcome;
	// private String otherCaseOutcomeDetails;

	@HideForCountriesExcept
	private YesNoUnknown prohibitionToWork;
	@HideForCountriesExcept
	private Date prohibitionToWorkFrom;
	@HideForCountriesExcept
	private Date prohibitionToWorkUntil;

	private YesNoUnknown reInfection;

	private Date previousInfectionDate;

	private ReinfectionStatus reinfectionStatus;

	private Map<ReinfectionDetail, Boolean> reinfectionDetails;

	@HideForCountriesExcept
	private YesNoUnknown bloodOrganOrTissueDonated;

	@HideForCountriesExcept
	private boolean notACaseReasonNegativeTest;

	@HideForCountriesExcept
	private boolean notACaseReasonPhysicianInformation;

	@HideForCountriesExcept
	private boolean notACaseReasonDifferentPathogen;

	@HideForCountriesExcept
	private boolean notACaseReasonOther;

	@HideForCountriesExcept
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_TEXT, message = Validations.textTooLong)
	private String notACaseReasonDetails;
	private Date followUpStatusChangeDate;
	private UserReferenceDto followUpStatusChangeUser;

	private boolean dontShareWithReportingTool;

	@HideForCountriesExcept
	private CaseReferenceDefinition caseReferenceDefinition;

	private Date previousQuarantineTo;
	@SensitiveData
	@Size(max = CHARACTER_LIMIT_BIG, message = Validations.textTooLong)
	private String quarantineChangeComment;

	private Map<String, String> externalData;
	private boolean deleted;
	private DeletionReason deletionReason;
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_TEXT, message = Validations.textTooLong)
	private String otherDeletionReason;

	@Outbreaks
	private CardOrHistory vaccinationType;
	private Date vaccinationDate;
	private Date lastVaccinationDate;

	private String notifiedBy;
	private Date dateOfNotification;
	private NotifiedList notifiedByList;
	private String notifiedOther;
	private Date dateOfInvestigation;
	private  Date secondVaccinationDate;
	private String reportingVillage;
	private String reportingZone;
	private String investigationOfficerName;
	private String investigationOfficerPosition;
	private String formCompletedByName;
	private String formCompletedByPosition;
	private String formCompletedByCellPhoneNo;

	private YesNoUnknown motherVaccinatedWithTT;
	private YesNoUnknown motherHaveCard;
	private String motherNumberOfDoses;
	private MotherVaccinationStatus motherVaccinationStatus;
	private Date motherTTDateOne;
	private Date motherTTDateTwo;
	private Date motherTTDateThree;
	private Date motherTTDateFour;
	private Date motherTTDateFive;
	private Date motherLastDoseDate;
	private YesNoUnknown seenInOPD;
	private YesNoUnknown admittedInOPD;
	private YesNoUnknown motherGivenProtectiveDoseTT;
	private Date motherGivenProtectiveDoseTTDate;
	private YesNoUnknown supplementalImmunization;
	private String supplementalImmunizationDetails;
	private String addressMpox;
	private String village;
	private String city;
	private String nationality;
	private String ethnicity;
	private String occupation;
	private String districtOfResidence;
	private String specifyEventDiagnosis;
	private String nameOfVillagePersonGotIll;

	private String otherNotesAndObservations;
	private Date dateLatestUpdateRecord;
	private Integer numberOfPeopleInSameHousehold;

	public static CaseDataDto build(PersonReferenceDto person, Disease disease) {
		return build(person, disease, HealthConditionsDto.build());
	}

	public static CaseDataDto build(PersonReferenceDto person, Disease disease, HealthConditionsDto healthConditions) {
		CaseDataDto caze = new CaseDataDto();
		caze.setUuid(DataHelper.createUuid());
		caze.setPerson(person);
		caze.setHospitalization(HospitalizationDto.build());
		caze.setSixtyDay(SixtyDayDto.build());
		caze.setAfpImmunization(AfpImmunizationDto.build());
		caze.setFoodHistory(FoodHistoryDto.build());
		caze.setRiskFactor(RiskFactorDto.build());
		caze.setEpiData(EpiDataDto.build());
		caze.setSymptoms(SymptomsDto.build());
		caze.setTherapy(TherapyDto.build());
		caze.setHealthConditions(healthConditions);
		caze.setClinicalCourse(ClinicalCourseDto.build());
		caze.setMaternalHistory(MaternalHistoryDto.build());
		caze.setPortHealthInfo(PortHealthInfoDto.build());
		caze.setDisease(disease);
		caze.setInvestigationStatus(InvestigationStatus.PENDING);
		caze.setCaseClassification(CaseClassification.SUSPECT);
		caze.setOutcome(CaseOutcome.NO_OUTCOME);
		caze.setCaseOrigin(CaseOrigin.IN_COUNTRY);
		// TODO This is a workaround for transferring the followup comment while converting a contact to a case. This can be removed if the followup for cases is implemented in the mobile app
		caze.setFollowUpStatus(FollowUpStatus.NO_FOLLOW_UP);

		UserDto currentUser = FacadeProvider.getUserFacade().getCurrentUser();
		if (currentUser != null) {
			caze.setHealthFacility(currentUser.getHealthFacility());
		}
		return caze;
	}

	/**
	 *
	 * @param contact
	 *            leads to the returned case
	 * @return dto that contains the contacts information. If the contact has one exposure, this marked as the probable infection
	 *         environment.
	 */
	public static CaseDataDto buildFromContact(ContactDto contact) {

		HealthConditionsDto healthConditionsClone = null;
		try {
			healthConditionsClone = (HealthConditionsDto) contact.getHealthConditions().clone();
			healthConditionsClone.setUuid(DataHelper.createUuid());
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		CaseDataDto cazeData = CaseDataDto.build(contact.getPerson(), contact.getDisease(), healthConditionsClone);
		copyEpiData(contact, cazeData);
		List<ExposureDto> exposures = cazeData.getEpiData().getExposures();
		if (exposures.size() == 1) {
			exposures.get(0).setProbableInfectionEnvironment(true);
			exposures.get(0).setContactToCase(contact.toReference());
		}
		return cazeData;
	}

	public static CaseDataDto buildFromUnrelatedContact(ContactDto contact, Disease disease) {

		CaseDataDto cazeData = CaseDataDto.build(contact.getPerson(), disease);
		copyEpiData(contact, cazeData);
		return cazeData;
	}

	private static void copyEpiData(ContactDto contact, CaseDataDto cazeData) {
		try {
			EpiDataDto epiDataClone = contact.getEpiData().clone();
			epiDataClone.setUuid(cazeData.getEpiData().getUuid());
			for (ActivityAsCaseDto activityAsCase : epiDataClone.getActivitiesAsCase()) {
				activityAsCase.setUuid(DataHelper.createUuid());
				activityAsCase.getLocation().setUuid(DataHelper.createUuid());
			}
			for (ExposureDto exposure : epiDataClone.getExposures()) {
				exposure.setUuid(DataHelper.createUuid());
				exposure.getLocation().setUuid(DataHelper.createUuid());
			}
			cazeData.setEpiData(epiDataClone);
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		cazeData.setFollowUpComment(contact.getFollowUpComment());
	}

	public static CaseDataDto buildFromEventParticipant(EventParticipantDto eventParticipant, PersonDto person, Disease eventDisease) {

		CaseDataDto caseData = CaseDataDto.build(eventParticipant.getPerson().toReference(), eventDisease);

		updateCaseOutcome(caseData, person, eventDisease, eventParticipant.getCreationDate());

		return caseData;
	}

	public static CaseDataDto buildFromTravelEntry(TravelEntryDto travelEntry, PersonDto person) {

		CaseDataDto caseData = CaseDataDto.build(person.toReference(), travelEntry.getDisease());

		caseData.setCaseOrigin(CaseOrigin.POINT_OF_ENTRY);
		caseData.setDiseaseVariant(travelEntry.getDiseaseVariant());
		caseData.setDiseaseDetails(travelEntry.getDiseaseVariantDetails());
		caseData.setResponsibleRegion(travelEntry.getResponsibleRegion());
		caseData.setResponsibleDistrict(travelEntry.getResponsibleDistrict());
		caseData.setResponsibleCommunity(travelEntry.getResponsibleCommunity());
		caseData.setPointOfEntry(travelEntry.getPointOfEntry());
		caseData.setPointOfEntryDetails(travelEntry.getPointOfEntryDetails());
		caseData.setReportDate(travelEntry.getReportDate());

		updateCaseOutcome(caseData, person, travelEntry.getDisease(), travelEntry.getReportDate());

		return caseData;
	}

	private static void updateCaseOutcome(CaseDataDto caseData, PersonDto person, Disease disease, Date creationDate) {
		if (person.getPresentCondition() != null
			&& person.getPresentCondition().isDeceased()
			&& disease == person.getCauseOfDeathDisease()
			&& person.getDeathDate() != null
			&& Math.abs(person.getDeathDate().getTime() - creationDate.getTime()) <= MILLISECONDS_30_DAYS) {
			caseData.setOutcome(CaseOutcome.DECEASED);
			caseData.setOutcomeDate(person.getDeathDate());
		}
	}

	public CaseReferenceDto toReference() {
		return new CaseReferenceDto(getUuid(), getPerson().getFirstName(), getPerson().getLastName(), getPerson().getOtherName());
	}

	/**
	 * Returns true if the case is an original point of entry case and has not yet
	 * been assigned a health facility.
	 */
	public boolean checkIsUnreferredPortHealthCase() {
		return caseOrigin == CaseOrigin.POINT_OF_ENTRY && healthFacility == null;
	}

	@Override
	public UserReferenceDto getReportingUser() {
		return reportingUser;
	}

	@Override
	public void setReportingUser(UserReferenceDto reportingUser) {
		this.reportingUser = reportingUser;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public PersonReferenceDto getPerson() {
		return person;
	}

	public void setPerson(PersonReferenceDto personDto) {
		this.person = personDto;
	}

	public CaseClassification getCaseClassification() {
		return caseClassification;
	}

	public void setCaseClassification(CaseClassification caseClassification) {
		this.caseClassification = caseClassification;
	}

	public CaseIdentificationSource getCaseIdentificationSource() {
		return caseIdentificationSource;
	}

	public void setCaseIdentificationSource(CaseIdentificationSource caseIdentificationSource) {
		this.caseIdentificationSource = caseIdentificationSource;
	}

	public ScreeningType getScreeningType() {
		return screeningType;
	}

	public void setScreeningType(ScreeningType screeningType) {
		this.screeningType = screeningType;
	}

	public UserReferenceDto getClassificationUser() {
		return classificationUser;
	}

	public void setClassificationUser(UserReferenceDto classificationUser) {
		this.classificationUser = classificationUser;
	}

	public Date getClassificationDate() {
		return classificationDate;
	}

	public void setClassificationDate(Date classificationDate) {
		this.classificationDate = classificationDate;
	}

	public String getClassificationComment() {
		return classificationComment;
	}

	public void setClassificationComment(String classificationComment) {
		this.classificationComment = classificationComment;
	}

	public YesNoUnknown getClinicalConfirmation() {
		return clinicalConfirmation;
	}

	public void setClinicalConfirmation(YesNoUnknown clinicalConfirmation) {
		this.clinicalConfirmation = clinicalConfirmation;
	}

	public YesNoUnknown getEpidemiologicalConfirmation() {
		return epidemiologicalConfirmation;
	}

	public void setEpidemiologicalConfirmation(YesNoUnknown epidemiologicalConfirmation) {
		this.epidemiologicalConfirmation = epidemiologicalConfirmation;
	}

	public YesNoUnknown getLaboratoryDiagnosticConfirmation() {
		return laboratoryDiagnosticConfirmation;
	}

	public void setLaboratoryDiagnosticConfirmation(YesNoUnknown laboratoryDiagnosticConfirmation) {
		this.laboratoryDiagnosticConfirmation = laboratoryDiagnosticConfirmation;
	}

	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	public DiseaseVariant getDiseaseVariant() {
		return diseaseVariant;
	}

	public void setDiseaseVariant(DiseaseVariant diseaseVariant) {
		this.diseaseVariant = diseaseVariant;
	}

	public String getDiseaseDetails() {
		return diseaseDetails;
	}

	public void setDiseaseDetails(String diseaseDetails) {
		this.diseaseDetails = diseaseDetails;
	}

	public String getDiseaseVariantDetails() {
		return diseaseVariantDetails;
	}

	public void setDiseaseVariantDetails(String diseaseVariantDetails) {
		this.diseaseVariantDetails = diseaseVariantDetails;
	}

	public PlagueType getPlagueType() {
		return plagueType;
	}

	public void setPlagueType(PlagueType plagueType) {
		this.plagueType = plagueType;
	}

	public DengueFeverType getDengueFeverType() {
		return dengueFeverType;
	}

	public void setDengueFeverType(DengueFeverType dengueFeverType) {
		this.dengueFeverType = dengueFeverType;
	}

	public IdsrType getIdsrDiagnosis() {
		return idsrDiagnosis;
	}

	public void setIdsrDiagnosis(IdsrType idsrDiagnosis) {
		this.idsrDiagnosis = idsrDiagnosis;
	}

	public RabiesType getRabiesType() {
		return rabiesType;
	}

	public void setRabiesType(RabiesType rabiesType) {
		this.rabiesType = rabiesType;
	}

	public FacilityReferenceDto getHealthFacility() {
		return healthFacility;
	}

	public void setHealthFacility(FacilityReferenceDto healthFacility) {
		this.healthFacility = healthFacility;
	}

	public String getHealthFacilityDetails() {
		return healthFacilityDetails;
	}

	public void setHealthFacilityDetails(String healthFacilityDetails) {
		this.healthFacilityDetails = healthFacilityDetails;
	}

	public Date getInvestigatedDate() {
		return investigatedDate;
	}

	public void setInvestigatedDate(Date investigatedDate) {
		this.investigatedDate = investigatedDate;
	}

	public Date getRegionLevelDate() {
		return regionLevelDate;
	}

	public void setRegionLevelDate(Date regionLevelDate) {
		this.regionLevelDate = regionLevelDate;
	}

	public Date getNationalLevelDate() {
		return nationalLevelDate;
	}

	public void setNationalLevelDate(Date nationalLevelDate) {
		this.nationalLevelDate = nationalLevelDate;
	}

	public Date getDistrictLevelDate() {
		return districtLevelDate;
	}

	public void setDistrictLevelDate(Date districtLevelDate) {
		this.districtLevelDate = districtLevelDate;
	}

	public Date getDateFormSentToDistrict() {
		return dateFormSentToDistrict;
	}

	public void setDateFormSentToDistrict(Date dateFormSentToDistrict) {
		this.dateFormSentToDistrict = dateFormSentToDistrict;
	}

	public UserReferenceDto getSurveillanceOfficer() {
		return surveillanceOfficer;
	}

	public void setSurveillanceOfficer(UserReferenceDto surveillanceOfficer) {
		this.surveillanceOfficer = surveillanceOfficer;
	}

	public String getClinicianName() {
		return clinicianName;
	}

	public void setClinicianName(String clinicianName) {
		this.clinicianName = clinicianName;
	}

	public String getClinicianPhone() {
		return clinicianPhone;
	}

	public void setClinicianPhone(String clinicianPhone) {
		this.clinicianPhone = clinicianPhone;
	}

	public String getClinicianEmail() {
		return clinicianEmail;
	}

	public void setClinicianEmail(String clinicianEmail) {
		this.clinicianEmail = clinicianEmail;
	}

	public String getReportingOfficerTitle() {return reportingOfficerTitle;}

	public void setReportingOfficerTitle(String reportingOfficerTitle) {
		this.reportingOfficerTitle = reportingOfficerTitle;
	}

	public String getReportingOfficerName() {return reportingOfficerName;}

	public void setReportingOfficerName(String reportingOfficerName) {
		this.reportingOfficerName = reportingOfficerName;
	}
	public String getFunctionOfReportingOfficer() {return functionOfReportingOfficer;}

	public void setFunctionOfReportingOfficer(String functionOfReportingOfficer) {
		this.functionOfReportingOfficer = functionOfReportingOfficer;
	}
	public String getReportingOfficerContactPhone() {return reportingOfficerContactPhone;}

	public void setReportingOfficerContactPhone(String reportingOfficerContactPhone) {
		this.reportingOfficerContactPhone = reportingOfficerContactPhone;
	}
	public String getReportingOfficerEmail() {return reportingOfficerEmail;}

	public void setReportingOfficerEmail(String reportingOfficerEmail) {
		this.reportingOfficerEmail = reportingOfficerEmail;
	}

	@Deprecated
	public UserReferenceDto getCaseOfficer() {
		return caseOfficer;
	}

	@Deprecated
	public void setCaseOfficer(UserReferenceDto caseOfficer) {
		this.caseOfficer = caseOfficer;
	}

	public SymptomsDto getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(SymptomsDto symptoms) {
		this.symptoms = symptoms;
	}

	public RegionReferenceDto getResponsibleRegion() {
		return responsibleRegion;
	}

	public void setResponsibleRegion(RegionReferenceDto responsibleRegion) {
		this.responsibleRegion = responsibleRegion;
	}

	public DistrictReferenceDto getResponsibleDistrict() {
		return responsibleDistrict;
	}

	public void setResponsibleDistrict(DistrictReferenceDto responsibleDistrict) {
		this.responsibleDistrict = responsibleDistrict;
	}

	public CommunityReferenceDto getResponsibleCommunity() {
		return responsibleCommunity;
	}

	public void setResponsibleCommunity(CommunityReferenceDto responsibleCommunity) {
		this.responsibleCommunity = responsibleCommunity;
	}

	public RegionReferenceDto getRegion() {
		return region;
	}

	public void setRegion(RegionReferenceDto region) {
		this.region = region;
	}

	public DistrictReferenceDto getDistrict() {
		return district;
	}

	public void setDistrict(DistrictReferenceDto district) {
		this.district = district;
	}

	public CommunityReferenceDto getCommunity() {
		return community;
	}

	public void setCommunity(CommunityReferenceDto community) {
		this.community = community;
	}

	public InvestigationStatus getInvestigationStatus() {
		return investigationStatus;
	}

	public void setInvestigationStatus(InvestigationStatus investigationStatus) {
		this.investigationStatus = investigationStatus;
	}

	public HospitalizationDto getHospitalization() {
		return hospitalization;
	}

	public void setHospitalization(HospitalizationDto hospitalization) {
		this.hospitalization = hospitalization;
	}

	public SixtyDayDto getSixtyDay() {
		return sixtyDay;
	}

	public void setSixtyDay(SixtyDayDto sixtyDay) {
		this.sixtyDay = sixtyDay;
	}

	public AfpImmunizationDto getAfpImmunization() {
		return afpImmunization;
	}
	public void setAfpImmunization(AfpImmunizationDto afpImmunization) {
		this.afpImmunization = afpImmunization;
	}

	public FoodHistoryDto getFoodHistory() {
		return foodHistory;
	}
	public void setFoodHistory(FoodHistoryDto foodHistory) {
		this.foodHistory = foodHistory;
	}

	public RiskFactorDto getRiskFactor() {
		return riskFactor;
	}

	public void setRiskFactor(RiskFactorDto riskFactor) {
		this.riskFactor = riskFactor;
	}

	public EpiDataDto getEpiData() {
		return epiData;
	}

	public void setEpiData(EpiDataDto epiData) {
		this.epiData = epiData;
	}

	public TherapyDto getTherapy() {
		return therapy;
	}

	public void setTherapy(TherapyDto therapy) {
		this.therapy = therapy;
	}

	public ClinicalCourseDto getClinicalCourse() {
		return clinicalCourse;
	}

	public void setClinicalCourse(ClinicalCourseDto clinicalCourse) {
		this.clinicalCourse = clinicalCourse;
	}

	public MaternalHistoryDto getMaternalHistory() {
		return maternalHistory;
	}

	public void setMaternalHistory(MaternalHistoryDto maternalHistory) {
		this.maternalHistory = maternalHistory;
	}

	public PortHealthInfoDto getPortHealthInfo() {
		return portHealthInfo;
	}

	public void setPortHealthInfo(PortHealthInfoDto portHealthInfo) {
		this.portHealthInfo = portHealthInfo;
	}

	public YesNo getPregnant() {
		return pregnant;
	}

	public void setPregnant(YesNo pregnant) {
		this.pregnant = pregnant;
	}

	public YesNoUnknown getIpSampleSent() {
		return ipSampleSent;
	}

	public void setIpSampleSent(YesNoUnknown ipSampleSent) {
		this.ipSampleSent = ipSampleSent;
	}

	public Disease getIpSampleResults(){
		return ipSampleResults;
	}

	public void setIpSampleResults(Disease ipSampleResults) {
		this.ipSampleResults = ipSampleResults;
	}

	public VaccinationStatus getVaccinationStatus() {
		return vaccinationStatus;
	}

	public void setVaccinationStatus(VaccinationStatus vaccinationStatus) {
		this.vaccinationStatus = vaccinationStatus;
	}

	public CardOrHistory getVaccinationType() {
		return vaccinationType;
	}

	public void setVaccinationType(CardOrHistory vaccinationType) {
		this.vaccinationType = vaccinationType;
	}

	public Date getVaccinationDate() {
		return vaccinationDate;
	}

	public void setVaccinationDate(Date vaccinationDate) {
		this.vaccinationDate = vaccinationDate;
	}

	public YesNoUnknown getSmallpoxVaccinationScar() {
		return smallpoxVaccinationScar;
	}

	public void setSmallpoxVaccinationScar(YesNoUnknown smallpoxVaccinationScar) {
		this.smallpoxVaccinationScar = smallpoxVaccinationScar;
	}

	public YesNoUnknown getSmallpoxVaccinationReceived() {
		return smallpoxVaccinationReceived;
	}

	public void setSmallpoxVaccinationReceived(YesNoUnknown smallpoxVaccinationReceived) {
		this.smallpoxVaccinationReceived = smallpoxVaccinationReceived;
	}

	public Date getSmallpoxLastVaccinationDate() {
		return smallpoxLastVaccinationDate;
	}

	public void setSmallpoxLastVaccinationDate(Date smallpoxLastVaccinationDate) {
		this.smallpoxLastVaccinationDate = smallpoxLastVaccinationDate;
	}

	public String getEpidNumber() {
		return epidNumber;
	}

	public void setEpidNumber(String epidNumber) {
		this.epidNumber = epidNumber;
	}

	public Double getReportLat() {
		return reportLat;
	}

	public void setReportLat(Double reportLat) {
		this.reportLat = reportLat;
	}

	public Double getReportLon() {
		return reportLon;
	}

	public void setReportLon(Double reportLon) {
		this.reportLon = reportLon;
	}

	public Float getReportLatLonAccuracy() {
		return reportLatLonAccuracy;
	}

	public void setReportLatLonAccuracy(Float reportLatLonAccuracy) {
		this.reportLatLonAccuracy = reportLatLonAccuracy;
	}

	public CaseOutcome getOutcome() {
		return outcome;
	}

	public void setOutcome(CaseOutcome outcome) {
		this.outcome = outcome;
	}

	public Date getOutcomeDate() {
		return outcomeDate;
	}

	public void setOutcomeDate(Date outcomeDate) {
		this.outcomeDate = outcomeDate;
	}

	public YesNoUnknown getSequelae() {
		return sequelae;
	}

	public void setSequelae(YesNoUnknown sequelae) {
		this.sequelae = sequelae;
	}

	public String getSequelaeDetails() {
		return sequelaeDetails;
	}

	public void setSequelaeDetails(String sequelaeDetails) {
		this.sequelaeDetails = sequelaeDetails;
	}

	public HospitalWardType getNotifyingClinic() {
		return notifyingClinic;
	}

	public void setNotifyingClinic(HospitalWardType notifyingClinic) {
		this.notifyingClinic = notifyingClinic;
	}

	public String getNotifyingClinicDetails() {
		return notifyingClinicDetails;
	}

	public void setNotifyingClinicDetails(String notifyingClinicDetails) {
		this.notifyingClinicDetails = notifyingClinicDetails;
	}

	@ImportIgnore
	public String getCreationVersion() {
		return creationVersion;
	}

	public void setCreationVersion(String creationVersion) {
		this.creationVersion = creationVersion;
	}

	public CaseOrigin getCaseOrigin() {
		return caseOrigin;
	}

	public void setCaseOrigin(CaseOrigin caseOrigin) {
		this.caseOrigin = caseOrigin;
	}

	public PointOfEntryReferenceDto getPointOfEntry() {
		return pointOfEntry;
	}

	public void setPointOfEntry(PointOfEntryReferenceDto pointOfEntry) {
		this.pointOfEntry = pointOfEntry;
	}

	public String getPointOfEntryDetails() {
		return pointOfEntryDetails;
	}

	public void setPointOfEntryDetails(String pointOfEntryDetails) {
		this.pointOfEntryDetails = pointOfEntryDetails;
	}

	public String getAdditionalDetails() {
		return additionalDetails;
	}

	public void setAdditionalDetails(String additionalDetails) {
		this.additionalDetails = additionalDetails;
	}

	public String getExternalID() {
		return externalID;
	}

	public void setExternalID(String externalID) {
		this.externalID = externalID;
	}

	public String getExternalToken() {
		return externalToken;
	}

	public void setExternalToken(String externalToken) {
		this.externalToken = externalToken;
	}

	public String getInternalToken() {
		return internalToken;
	}

	public void setInternalToken(String internalToken) {
		this.internalToken = internalToken;
	}

	public boolean isSharedToCountry() {
		return sharedToCountry;
	}

	public void setSharedToCountry(boolean sharedToCountry) {
		this.sharedToCountry = sharedToCountry;
	}

	public boolean isNosocomialOutbreak() {
		return nosocomialOutbreak;
	}

	public void setNosocomialOutbreak(boolean nosocomialOutbreak) {
		this.nosocomialOutbreak = nosocomialOutbreak;
	}

	public InfectionSetting getInfectionSetting() {
		return infectionSetting;
	}

	public void setInfectionSetting(InfectionSetting infectionSetting) {
		this.infectionSetting = infectionSetting;
	}

	public QuarantineType getQuarantine() {
		return quarantine;
	}

	public void setQuarantine(QuarantineType quarantine) {
		this.quarantine = quarantine;
	}

	public String getQuarantineTypeDetails() {
		return quarantineTypeDetails;
	}

	public void setQuarantineTypeDetails(String quarantineTypeDetails) {
		this.quarantineTypeDetails = quarantineTypeDetails;
	}

	public Date getQuarantineFrom() {
		return quarantineFrom;
	}

	public void setQuarantineFrom(Date quarantineFrom) {
		this.quarantineFrom = quarantineFrom;
	}

	public Date getQuarantineTo() {
		return quarantineTo;
	}

	public void setQuarantineTo(Date quarantineTo) {
		this.quarantineTo = quarantineTo;
	}

	public String getQuarantineHelpNeeded() {
		return quarantineHelpNeeded;
	}

	public void setQuarantineHelpNeeded(String quarantineHelpNeeded) {
		this.quarantineHelpNeeded = quarantineHelpNeeded;
	}

	public boolean isQuarantineOrderedVerbally() {
		return quarantineOrderedVerbally;
	}

	public void setQuarantineOrderedVerbally(boolean quarantineOrderedVerbally) {
		this.quarantineOrderedVerbally = quarantineOrderedVerbally;
	}

	public boolean isQuarantineOrderedOfficialDocument() {
		return quarantineOrderedOfficialDocument;
	}

	public void setQuarantineOrderedOfficialDocument(boolean quarantineOrderedOfficialDocument) {
		this.quarantineOrderedOfficialDocument = quarantineOrderedOfficialDocument;
	}

	public Date getQuarantineOrderedVerballyDate() {
		return quarantineOrderedVerballyDate;
	}

	public void setQuarantineOrderedVerballyDate(Date quarantineOrderedVerballyDate) {
		this.quarantineOrderedVerballyDate = quarantineOrderedVerballyDate;
	}

	public Date getQuarantineOrderedOfficialDocumentDate() {
		return quarantineOrderedOfficialDocumentDate;
	}

	public void setQuarantineOrderedOfficialDocumentDate(Date quarantineOrderedOfficialDocumentDate) {
		this.quarantineOrderedOfficialDocumentDate = quarantineOrderedOfficialDocumentDate;
	}

	public YesNoUnknown getQuarantineHomePossible() {
		return quarantineHomePossible;
	}

	public void setQuarantineHomePossible(YesNoUnknown quarantineHomePossible) {
		this.quarantineHomePossible = quarantineHomePossible;
	}

	public String getQuarantineHomePossibleComment() {
		return quarantineHomePossibleComment;
	}

	public void setQuarantineHomePossibleComment(String quarantineHomePossibleComment) {
		this.quarantineHomePossibleComment = quarantineHomePossibleComment;
	}

	public YesNoUnknown getQuarantineHomeSupplyEnsured() {
		return quarantineHomeSupplyEnsured;
	}

	public void setQuarantineHomeSupplyEnsured(YesNoUnknown quarantineHomeSupplyEnsured) {
		this.quarantineHomeSupplyEnsured = quarantineHomeSupplyEnsured;
	}

	public String getQuarantineHomeSupplyEnsuredComment() {
		return quarantineHomeSupplyEnsuredComment;
	}

	public void setQuarantineHomeSupplyEnsuredComment(String quarantineHomeSupplyEnsuredComment) {
		this.quarantineHomeSupplyEnsuredComment = quarantineHomeSupplyEnsuredComment;
	}

	public boolean isQuarantineExtended() {
		return quarantineExtended;
	}

	public void setQuarantineExtended(boolean quarantineExtended) {
		this.quarantineExtended = quarantineExtended;
	}

	public boolean isQuarantineReduced() {
		return quarantineReduced;
	}

	public void setQuarantineReduced(boolean quarantineReduced) {
		this.quarantineReduced = quarantineReduced;
	}

	public boolean isQuarantineOfficialOrderSent() {
		return quarantineOfficialOrderSent;
	}

	public void setQuarantineOfficialOrderSent(boolean quarantineOfficialOrderSent) {
		this.quarantineOfficialOrderSent = quarantineOfficialOrderSent;
	}

	public Date getQuarantineOfficialOrderSentDate() {
		return quarantineOfficialOrderSentDate;
	}

	public void setQuarantineOfficialOrderSentDate(Date quarantineOfficialOrderSentDate) {
		this.quarantineOfficialOrderSentDate = quarantineOfficialOrderSentDate;
	}

	public YesNo getPostpartum() {
		return postpartum;
	}

	public void setPostpartum(YesNo postpartum) {
		this.postpartum = postpartum;
	}

	public Trimester getTrimester() {
		return trimester;
	}

	public void setTrimester(Trimester trimester) {
		this.trimester = trimester;
	}

	public VaccineTypes getVaccineType() {
		return vaccineType;
	}

	public void setVaccineType(VaccineTypes vaccineType) {
		this.vaccineType = vaccineType;
	}

	public String getNumberOfDoses() {
		return numberOfDoses;
	}

	public void setNumberOfDoses(String numberOfDoses) {
		this.numberOfDoses = numberOfDoses;
	}

	public FollowUpStatus getFollowUpStatus() {
		return followUpStatus;
	}

	public void setFollowUpStatus(FollowUpStatus followUpStatus) {
		this.followUpStatus = followUpStatus;
	}

	public String getFollowUpComment() {
		return followUpComment;
	}

	public void setFollowUpComment(String followUpComment) {
		this.followUpComment = followUpComment;
	}

	public Date getFollowUpUntil() {
		return followUpUntil;
	}

	public void setFollowUpUntil(Date followUpUntil) {
		this.followUpUntil = followUpUntil;
	}

	public boolean isOverwriteFollowUpUntil() {
		return overwriteFollowUpUntil;
	}

	public void setOverwriteFollowUpUntil(boolean overwriteFollowUpUntil) {
		this.overwriteFollowUpUntil = overwriteFollowUpUntil;
	}

	public FacilityType getFacilityType() {
		return facilityType;
	}

	public void setFacilityType(FacilityType facilityType) {
		this.facilityType = facilityType;
	}

	public DhimsFacility getDhimsFacilityType() {
		return dhimsFacilityType;
	}
	public void setDhimsFacilityType(DhimsFacility dhimsFacilityType) {
		this.dhimsFacilityType = dhimsFacilityType;
	}

	public AFPFacilityOptions getAfpFacilityOptions(){return afpFacilityOptions;}
	public void setAfpFacilityOptions(AFPFacilityOptions afpFacilityOptions){this.afpFacilityOptions = afpFacilityOptions;}

	public Integer getCaseIdIsm() {
		return caseIdIsm;
	}

	public void setCaseIdIsm(Integer caseIdIsm) {
		this.caseIdIsm = caseIdIsm;
	}

	public ContactTracingContactType getContactTracingFirstContactType() {
		return contactTracingFirstContactType;
	}

	public void setContactTracingFirstContactType(ContactTracingContactType contactTracingFirstContactType) {
		this.contactTracingFirstContactType = contactTracingFirstContactType;
	}

	public Date getContactTracingFirstContactDate() {
		return contactTracingFirstContactDate;
	}

	public void setContactTracingFirstContactDate(Date contactTracingFirstContactDate) {
		this.contactTracingFirstContactDate = contactTracingFirstContactDate;
	}

	public YesNoUnknown getWasInQuarantineBeforeIsolation() {
		return wasInQuarantineBeforeIsolation;
	}

	public void setWasInQuarantineBeforeIsolation(YesNoUnknown wasInQuarantineBeforeIsolation) {
		this.wasInQuarantineBeforeIsolation = wasInQuarantineBeforeIsolation;
	}

	public QuarantineReason getQuarantineReasonBeforeIsolation() {
		return quarantineReasonBeforeIsolation;
	}

	public void setQuarantineReasonBeforeIsolation(QuarantineReason quarantineReasonBeforeIsolation) {
		this.quarantineReasonBeforeIsolation = quarantineReasonBeforeIsolation;
	}

	public String getQuarantineReasonBeforeIsolationDetails() {
		return quarantineReasonBeforeIsolationDetails;
	}

	public void setQuarantineReasonBeforeIsolationDetails(String quarantineReasonBeforeIsolationDetails) {
		this.quarantineReasonBeforeIsolationDetails = quarantineReasonBeforeIsolationDetails;
	}

	public EndOfIsolationReason getEndOfIsolationReason() {
		return endOfIsolationReason;
	}

	public void setEndOfIsolationReason(EndOfIsolationReason endOfIsolationReason) {
		this.endOfIsolationReason = endOfIsolationReason;
	}

	public String getEndOfIsolationReasonDetails() {
		return endOfIsolationReasonDetails;
	}

	public void setEndOfIsolationReasonDetails(String endOfIsolationReasonDetails) {
		this.endOfIsolationReasonDetails = endOfIsolationReasonDetails;
	}

	public YesNoUnknown getProhibitionToWork() {
		return prohibitionToWork;
	}

	public void setProhibitionToWork(YesNoUnknown prohibitionToWork) {
		this.prohibitionToWork = prohibitionToWork;
	}

	public Date getProhibitionToWorkFrom() {
		return prohibitionToWorkFrom;
	}

	public void setProhibitionToWorkFrom(Date prohibitionToWorkFrom) {
		this.prohibitionToWorkFrom = prohibitionToWorkFrom;
	}

	public Date getProhibitionToWorkUntil() {
		return prohibitionToWorkUntil;
	}

	public void setProhibitionToWorkUntil(Date prohibitionToWorkUntil) {
		this.prohibitionToWorkUntil = prohibitionToWorkUntil;
	}

	public YesNoUnknown getReInfection() {
		return reInfection;
	}

	public void setReInfection(YesNoUnknown reInfection) {
		this.reInfection = reInfection;
	}

	public Date getPreviousInfectionDate() {
		return previousInfectionDate;
	}

	public void setPreviousInfectionDate(Date previousInfectionDate) {
		this.previousInfectionDate = previousInfectionDate;
	}

	@ImportIgnore
	public ReinfectionStatus getReinfectionStatus() {
		return reinfectionStatus;
	}

	public void setReinfectionStatus(ReinfectionStatus reinfectionStatus) {
		this.reinfectionStatus = reinfectionStatus;
	}

	@ImportIgnore
	public Map<ReinfectionDetail, Boolean> getReinfectionDetails() {
		return reinfectionDetails;
	}

	public void setReinfectionDetails(Map<ReinfectionDetail, Boolean> reinfectionDetails) {
		this.reinfectionDetails = reinfectionDetails;
	}

	public YesNoUnknown getBloodOrganOrTissueDonated() {
		return bloodOrganOrTissueDonated;
	}

	public void setBloodOrganOrTissueDonated(YesNoUnknown bloodOrganOrTissueDonated) {
		this.bloodOrganOrTissueDonated = bloodOrganOrTissueDonated;
	}


	// public String getOtherCaseOutcomeDetails() {
	// 	return otherCaseOutcomeDetails;
	// }
	public String getSpecifyOtherOutcome() {
		return specifyOtherOutcome;
	}

	public void setSpecifyOtherOutcome(String specifyOtherOutcome) {
		this.specifyOtherOutcome = specifyOtherOutcome;
	}

	public boolean isNotACaseReasonNegativeTest() {
		return notACaseReasonNegativeTest;
	}

	public void setNotACaseReasonNegativeTest(boolean notACaseReasonNegativeTest) {
		this.notACaseReasonNegativeTest = notACaseReasonNegativeTest;
	}

	public boolean isNotACaseReasonPhysicianInformation() {
		return notACaseReasonPhysicianInformation;
	}

	public void setNotACaseReasonPhysicianInformation(boolean notACaseReasonPhysicianInformation) {
		this.notACaseReasonPhysicianInformation = notACaseReasonPhysicianInformation;
	}

	public boolean isNotACaseReasonDifferentPathogen() {
		return notACaseReasonDifferentPathogen;
	}

	public void setNotACaseReasonDifferentPathogen(boolean notACaseReasonDifferentPathogen) {
		this.notACaseReasonDifferentPathogen = notACaseReasonDifferentPathogen;
	}

	public boolean isNotACaseReasonOther() {
		return notACaseReasonOther;
	}

	public void setNotACaseReasonOther(boolean notACaseReasonOther) {
		this.notACaseReasonOther = notACaseReasonOther;
	}

	public String getNotACaseReasonDetails() {
		return notACaseReasonDetails;
	}

	public void setNotACaseReasonDetails(String notACaseReasonDetails) {
		this.notACaseReasonDetails = notACaseReasonDetails;
	}

	public Date getFollowUpStatusChangeDate() {
		return followUpStatusChangeDate;
	}

	public void setFollowUpStatusChangeDate(Date followUpStatusChangeDate) {
		this.followUpStatusChangeDate = followUpStatusChangeDate;
	}

	public UserReferenceDto getFollowUpStatusChangeUser() {
		return followUpStatusChangeUser;
	}

	public void setFollowUpStatusChangeUser(UserReferenceDto followUpStatusChangeUser) {
		this.followUpStatusChangeUser = followUpStatusChangeUser;
	}

	public boolean hasResponsibleJurisdiction() {
		return responsibleRegion != null || responsibleDistrict != null || responsibleCommunity != null;
	}

	public boolean isDontShareWithReportingTool() {
		return dontShareWithReportingTool;
	}

	public void setDontShareWithReportingTool(boolean dontShareWithReportingTool) {
		this.dontShareWithReportingTool = dontShareWithReportingTool;
	}

	public CaseReferenceDefinition getCaseReferenceDefinition() {
		return caseReferenceDefinition;
	}

	public void setCaseReferenceDefinition(CaseReferenceDefinition caseReferenceDefinition) {
		this.caseReferenceDefinition = caseReferenceDefinition;
	}

	public Date getPreviousQuarantineTo() {
		return previousQuarantineTo;
	}

	public void setPreviousQuarantineTo(Date previousQuarantineTo) {
		this.previousQuarantineTo = previousQuarantineTo;
	}

	public String getQuarantineChangeComment() {
		return quarantineChangeComment;
	}

	public void setQuarantineChangeComment(String quarantineChangeComment) {
		this.quarantineChangeComment = quarantineChangeComment;
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

	public Map<String, String> getExternalData() {
		return externalData;
	}

	public void setExternalData(Map<String, String> externalData) {
		this.externalData = externalData;
	}

	public HealthConditionsDto getHealthConditions() {
		return healthConditions;
	}

	public void setHealthConditions(HealthConditionsDto healthConditions) {
		this.healthConditions = healthConditions;
	}

	@JsonIgnore
	public String i18nPrefix() {
		return I18N_PREFIX;
	}

	@Override
	public String toString() {
		return super.toString() + (StringUtils.isNotBlank(this.getExternalID()) ? " - " + this.getExternalID() : StringUtils.EMPTY);
	}
		public TransmissionClassification getCaseTransmissionClassification() {
		return caseTransmissionClassification;
	}

	public void setCaseTransmissionClassification(TransmissionClassification caseTransmissionClassification) {
		this.caseTransmissionClassification = caseTransmissionClassification;
	}

	public VaccinationRoutine getVaccinationRoutine() {
		return vaccinationRoutine;
	}

	public void setVaccinationRoutine(VaccinationRoutine vaccinationRoutine) {
		this.vaccinationRoutine = vaccinationRoutine;
	}

	public Date getVaccinationRoutineDate() {
		return vaccinationRoutineDate;
	}

	public void setVaccinationRoutineDate(Date vaccinationRoutineDate) {
		this.vaccinationRoutineDate = vaccinationRoutineDate;
	}
	public String getHomeAddressRecreational() {
		return homeAddressRecreational;
	}

	public void setHomeAddressRecreational(String homeAddressRecreational) {
		this.homeAddressRecreational = homeAddressRecreational;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getNotifiedBy() {
		return notifiedBy;
	}

	public void setNotifiedBy(String notifiedBy) {
		this.notifiedBy = notifiedBy;
	}

	public Date getDateOfNotification() {
		return dateOfNotification;
	}

	public void setDateOfNotification(Date dateOfNotification) {
		this.dateOfNotification = dateOfNotification;
	}

	public Date getDateOfInvestigation() {
		return dateOfInvestigation;
	}

	public void setDateOfInvestigation(Date dateOfInvestigation) {
		this.dateOfInvestigation = dateOfInvestigation;
	}

	public Date getLastVaccinationDate() {
		return lastVaccinationDate;
	}

	public void setLastVaccinationDate(Date lastVaccinationDate) {
		this.lastVaccinationDate = lastVaccinationDate;
	}

	public YesNoUnknown getMotherVaccinatedWithTT() {
		return motherVaccinatedWithTT;
	}

	public void setMotherVaccinatedWithTT(YesNoUnknown motherVaccinatedWithTT) {
		this.motherVaccinatedWithTT = motherVaccinatedWithTT;
	}

	public YesNoUnknown getMotherHaveCard() {
		return motherHaveCard;
	}

	public void setMotherHaveCard(YesNoUnknown motherHaveCard) {
		this.motherHaveCard = motherHaveCard;
	}

	public String getMotherNumberOfDoses() {
		return motherNumberOfDoses;
	}

	public void setMotherNumberOfDoses(String motherNumberOfDoses) {
		this.motherNumberOfDoses = motherNumberOfDoses;
	}

	public MotherVaccinationStatus getMotherVaccinationStatus() {
		return motherVaccinationStatus;
	}

	public void setMotherVaccinationStatus(MotherVaccinationStatus motherVaccinationStatus) {
		this.motherVaccinationStatus = motherVaccinationStatus;
	}

	public Date getMotherTTDateOne() {
		return motherTTDateOne;
	}

	public void setMotherTTDateOne(Date motherTTDateOne) {
		this.motherTTDateOne = motherTTDateOne;
	}

	public Date getMotherTTDateTwo() {
		return motherTTDateTwo;
	}

	public void setMotherTTDateTwo(Date motherTTDateTwo) {
		this.motherTTDateTwo = motherTTDateTwo;
	}

	public Date getMotherTTDateThree() {
		return motherTTDateThree;
	}

	public void setMotherTTDateThree(Date motherTTDateThree) {
		this.motherTTDateThree = motherTTDateThree;
	}

	public Date getMotherTTDateFour() {
		return motherTTDateFour;
	}

	public void setMotherTTDateFour(Date motherTTDateFour) {
		this.motherTTDateFour = motherTTDateFour;
	}

	public Date getMotherTTDateFive() {
		return motherTTDateFive;
	}

	public void setMotherTTDateFive(Date motherTTDateFive) {
		this.motherTTDateFive = motherTTDateFive;
	}

	public Date getMotherLastDoseDate() {
		return motherLastDoseDate;
	}
	public YesNoUnknown getSeenInOPD() {
		return seenInOPD;
	}

	public void setSeenInOPD(YesNoUnknown seenInOPD) {
		this.seenInOPD = seenInOPD;
	}

	public YesNoUnknown getAdmittedInOPD() {
		return admittedInOPD;
	}

	public void setAdmittedInOPD(YesNoUnknown admittedInOPD) {
		this.admittedInOPD = admittedInOPD;
	}

	public YesNoUnknown getMotherGivenProtectiveDoseTT() {
		return motherGivenProtectiveDoseTT;
	}

	public void setMotherGivenProtectiveDoseTT(YesNoUnknown motherGivenProtectiveDoseTT) {
		this.motherGivenProtectiveDoseTT = motherGivenProtectiveDoseTT;
	}

	public Date getMotherGivenProtectiveDoseTTDate() {
		return motherGivenProtectiveDoseTTDate;
	}

	public void setMotherGivenProtectiveDoseTTDate(Date motherGivenProtectiveDoseTTDate) {
		this.motherGivenProtectiveDoseTTDate = motherGivenProtectiveDoseTTDate;
	}

	public YesNoUnknown getSupplementalImmunization() {
		return supplementalImmunization;
	}

	public void setSupplementalImmunization(YesNoUnknown supplementalImmunization) {
		this.supplementalImmunization = supplementalImmunization;
	}

	public String getSupplementalImmunizationDetails() {
		return supplementalImmunizationDetails;
	}

	public void setSupplementalImmunizationDetails(String supplementalImmunizationDetails) {
		this.supplementalImmunizationDetails = supplementalImmunizationDetails;
	}

	public void setMotherLastDoseDate(Date motherLastDoseDate) {
		this.motherLastDoseDate = motherLastDoseDate;
	}

	public Date getSecondVaccinationDate() {
		return secondVaccinationDate;
	}
	public void setSecondVaccinationDate(Date secondVaccinationDate) {
		this.secondVaccinationDate = secondVaccinationDate;
	}
	public String getAddressMpox() {
		return addressMpox;
	}

	public void setAddressMpox(String addressMpox) {
		this.addressMpox = addressMpox;
	}

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getDistrictOfResidence() {
		return districtOfResidence;
	}

	public void setDistrictOfResidence(String districtOfResidence) {
		this.districtOfResidence = districtOfResidence;
	}

	public String getReportingVillage() {
		return reportingVillage;
	}

	public void setReportingVillage(String reportingVillage) {
		this.reportingVillage = reportingVillage;
	}

	public String getReportingZone() {
		return reportingZone;
	}

	public void setReportingZone(String reportingZone) {
		this.reportingZone = reportingZone;
	}

	public String getOtherNotesAndObservations() {
		return otherNotesAndObservations;
	}

	public void setOtherNotesAndObservations(String otherNotesAndObservations) {
		this.otherNotesAndObservations = otherNotesAndObservations;
	}

	public Date getDateLatestUpdateRecord() {
		return dateLatestUpdateRecord;
	}

	public void setDateLatestUpdateRecord(Date dateLatestUpdateRecord) {
		this.dateLatestUpdateRecord = dateLatestUpdateRecord;
	}

	public Integer getNumberOfPeopleInSameHousehold() {
		return numberOfPeopleInSameHousehold;
	}

	public void setNumberOfPeopleInSameHousehold(Integer numberOfPeopleInSameHousehold) {
		this.numberOfPeopleInSameHousehold = numberOfPeopleInSameHousehold;
	}
	public String getSpecifyEventDiagnosis() {
		return specifyEventDiagnosis;
	}

	public void setSpecifyEventDiagnosis(String specifyEventDiagnosis) {
		this.specifyEventDiagnosis = specifyEventDiagnosis;
	}

	public String getInvestigationOfficerName() {
		return investigationOfficerName;
	}

	public void setInvestigationOfficerName(String investigationOfficerName) {
		this.investigationOfficerName = investigationOfficerName;
	}

	public String getInvestigationOfficerPosition() {
		return investigationOfficerPosition;
	}

	public void setInvestigationOfficerPosition(String investigationOfficerPosition) {
		this.investigationOfficerPosition = investigationOfficerPosition;
	}

	public String getFormCompletedByName() {
		return this.formCompletedByName;
	}

	public void setFormCompletedByName(String formCompletedByName) {
		this.formCompletedByName = formCompletedByName;
	}

	public String getFormCompletedByPosition() {
		return this.formCompletedByPosition;
	}

	public void setFormCompletedByPosition(String formCompletedByPosition) {
		this.formCompletedByPosition = formCompletedByPosition;
	}

	public String getFormCompletedByCellPhoneNo() {
		return this.formCompletedByCellPhoneNo;
	}

	public void setFormCompletedByCellPhoneNo(String formCompletedByCellPhoneNo) {
		this.formCompletedByCellPhoneNo = formCompletedByCellPhoneNo;
	}
	public NotifiedList getNotifiedByList() {
		return notifiedByList;
	}

	public void setNotifiedByList(NotifiedList notifiedByList) {
		this.notifiedByList = notifiedByList;
	}

	public String getNotifiedOther() {
		return notifiedOther;
	}

	public void setNotifiedOther(String notifiedOther) {
		this.notifiedOther = notifiedOther;
	}

	public String getMobileTeamNo() {
		return mobileTeamNo;
	}

	public void setMobileTeamNo(String mobileTeamNo) {
		this.mobileTeamNo = mobileTeamNo;
	}

	public String getInformationGivenBy() {
		return informationGivenBy;
	}

	public void setInformationGivenBy(String informationGivenBy) {
		this.informationGivenBy = informationGivenBy;
	}

	public String getFamilyLinkWithPatient() {
		return familyLinkWithPatient;
	}

	public void setFamilyLinkWithPatient(String familyLinkWithPatient) {
		this.familyLinkWithPatient = familyLinkWithPatient;
	}

	public String getNameOfVillagePersonGotIll() {
		return nameOfVillagePersonGotIll;
	}

	public void setNameOfVillagePersonGotIll(String nameOfVillagePersonGotIll) {
		this.nameOfVillagePersonGotIll = nameOfVillagePersonGotIll;
	}

	public String getPatientOtherNames() {
		return patientOtherNames;
	}

	public void setPatientOtherNames(String patientOtherNames) {
		this.patientOtherNames = patientOtherNames;
	}

	public Integer getPatientDobDD() {
		return patientDobDD;
	}

	public void setPatientDobDD(Integer patientDobDD) {
		this.patientDobDD = patientDobDD;
	}

	public Integer getPatientDobMM() {
		return patientDobMM;
	}

	public void setPatientDobMM(Integer patientDobMM) {
		this.patientDobMM = patientDobMM;
	}

	public Integer getPatientDobYY() {
		return patientDobYY;
	}

	public void setPatientDobYY(Integer patientDobYY) {
		this.patientDobYY = patientDobYY;
	}

	public Integer getPatientAgeYear() {
		return patientAgeYear;
	}

	public void setPatientAgeYear(Integer patientAgeYear) {
		this.patientAgeYear = patientAgeYear;
	}

	public Integer getPatientAgeMonth() {
		return patientAgeMonth;
	}

	public void setPatientAgeMonth(Integer patientAgeMonth) {
		this.patientAgeMonth = patientAgeMonth;
	}

	public Sex getPatientSex() {
		return patientSex;
	}

	public void setPatientSex(Sex patientSex) {
		this.patientSex = patientSex;
	}

	public String getPatientFirstName() {
		return patientFirstName;
	}

	public void setPatientFirstName(String patientFirstName) {
		this.patientFirstName = patientFirstName;
	}

	public String getPatientLastName() {
		return patientLastName;
	}

	public void setPatientLastName(String patientLastName) {
		this.patientLastName = patientLastName;
	}
}
