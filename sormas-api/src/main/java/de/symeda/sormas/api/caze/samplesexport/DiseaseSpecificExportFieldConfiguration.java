package de.symeda.sormas.api.caze.samplesexport;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.afpimmunization.AfpImmunizationDto;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.epidata.EpiDataDto;
import de.symeda.sormas.api.foodhistory.FoodHistoryDto;
import de.symeda.sormas.api.hospitalization.HospitalizationDto;
import de.symeda.sormas.api.investigationnotes.InvestigationNotesDto;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.riskfactor.RiskFactorDto;
import de.symeda.sormas.api.sample.PathogenTestDto;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.sixtyday.SixtyDayDto;
import de.symeda.sormas.api.symptoms.SymptomsDto;
import de.symeda.sormas.api.utils.SymptomGroup;
import de.symeda.sormas.api.utils.SymptomGrouping;

/** Shared disease-disabled DTO fields used by forms and disease exports. */
public final class DiseaseSpecificExportFieldConfiguration {

	private static final Map<Class<?>, Map<String, Set<Disease>>> DISABLED_FIELDS = new HashMap<>();
	private static final Map<Disease, Set<String>> CASE_LAYOUT_FIELDS = new HashMap<>();
	private static final Map<Disease, Set<String>> SAMPLE_LAYOUT_FIELDS = new HashMap<>();
	private static final Map<Disease, Set<String>> PATHOGEN_TEST_LAYOUT_FIELDS = new HashMap<>();
	private static final Map<Class<?>, Map<Disease, Set<String>>> ENTITY_LAYOUT_FIELDS = new HashMap<>();
	private static final Set<Disease> DEFAULT_SYMPTOM_LAYOUT_DISEASES = Collections.unmodifiableSet(
		EnumSet.of(
			Disease.YELLOW_FEVER,
			Disease.CSM,
			Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,
			Disease.CORONAVIRUS,
			Disease.NEW_INFLUENZA,
			Disease.UNSPECIFIED_VHF));

	static {
		disable(CaseDataDto.class, CaseDataDto.CLASSIFICATION_DATE, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX);
		disable(CaseDataDto.class, CaseDataDto.CLASSIFICATION_USER, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX);
		disable(CaseDataDto.class, CaseDataDto.NOTIFIED_BY, Disease.AFP);
		disable(CaseDataDto.class, CaseDataDto.EXTERNAL_TOKEN, Disease.MEASLES);
		disable(CaseDataDto.class, CaseDataDto.INTERNAL_TOKEN, Disease.MEASLES);
		disable(CaseDataDto.class, CaseDataDto.DISEASE, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX);
		disable(CaseDataDto.class, CaseDataDto.OUTCOME, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, Disease.UNSPECIFIED_VHF);
		disable(CaseDataDto.class, CaseDataDto.CASE_ORIGIN, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX);
		disable(CaseDataDto.class, CaseDataDto.REGION, Disease.MONKEYPOX);
		disable(CaseDataDto.class, CaseDataDto.DISTRICT, Disease.MONKEYPOX);
		disable(CaseDataDto.class, CaseDataDto.COMMUNITY, Disease.MONKEYPOX);
		disable(CaseDataDto.class, CaseDataDto.QUARANTINE, Disease.MEASLES);
		disable(CaseDataDto.class, CaseDataDto.REPORT_LAT, Disease.CSM);
		disable(CaseDataDto.class, CaseDataDto.REPORT_LON, Disease.CSM);
		disable(CaseDataDto.class, CaseDataDto.REPORT_LAT_LON_ACCURACY, Disease.CSM);
		disable(
			CaseDataDto.class,
			CaseDataDto.HEALTH_CONDITIONS,
			Disease.YELLOW_FEVER,
			Disease.NEONATAL_TETANUS,
			Disease.CSM,
			Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,
			Disease.MEASLES,
			Disease.CORONAVIRUS,
			Disease.CHOLERA,
			Disease.AFP,
			Disease.GUINEA_WORM,
			Disease.UNSPECIFIED_VHF,
			Disease.NEW_INFLUENZA,
			Disease.MONKEYPOX,
			Disease.FOODBORNE_ILLNESS);
		disable(CaseDataDto.class, CaseDataDto.PREGNANT, Disease.YELLOW_FEVER, Disease.CSM, Disease.MEASLES, Disease.MONKEYPOX);
		disable(CaseDataDto.class, CaseDataDto.POSTPARTUM, Disease.YELLOW_FEVER, Disease.CSM, Disease.MEASLES, Disease.MONKEYPOX);
		disable(CaseDataDto.class, CaseDataDto.VACCINATION_STATUS, Disease.UNSPECIFIED_VHF, Disease.AFP);
		disable(CaseDataDto.class, CaseDataDto.NUMBER_OF_DOSES, Disease.YELLOW_FEVER, Disease.UNSPECIFIED_VHF, Disease.AFP, Disease.CSM, Disease.NEW_INFLUENZA, Disease.MEASLES, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX);
		disable(CaseDataDto.class, CaseDataDto.VACCINATION_DATE, Disease.YELLOW_FEVER, Disease.CSM, Disease.AFP, Disease.NEW_INFLUENZA, Disease.FOODBORNE_ILLNESS);
		disable(CaseDataDto.class, CaseDataDto.SMALLPOX_VACCINATION_RECEIVED, Disease.MONKEYPOX);
		disable(CaseDataDto.class, CaseDataDto.SURVEILLANCE_OFFICER, Disease.MEASLES);
		disable(CaseDataDto.class, CaseDataDto.REPORTING_OFFICER_NAME, Disease.YELLOW_FEVER, Disease.AFP, Disease.NEW_INFLUENZA, Disease.FOODBORNE_ILLNESS);
		disable(CaseDataDto.class, CaseDataDto.REPORTING_OFFICER_TITLE, Disease.AFP, Disease.NEW_INFLUENZA, Disease.UNSPECIFIED_VHF, Disease.FOODBORNE_ILLNESS);
		disable(CaseDataDto.class, CaseDataDto.FUNCTION_OF_REPORTING_OFFICER, Disease.AFP, Disease.NEW_INFLUENZA, Disease.UNSPECIFIED_VHF, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS);
		disable(CaseDataDto.class, CaseDataDto.REPORTING_OFFICER_CONTACT_PHONE, Disease.AFP, Disease.NEW_INFLUENZA, Disease.UNSPECIFIED_VHF, Disease.FOODBORNE_ILLNESS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS);
		disable(CaseDataDto.class, CaseDataDto.REPORTING_OFFICER_EMAIL, Disease.CSM, Disease.AFP, Disease.NEW_INFLUENZA, Disease.UNSPECIFIED_VHF, Disease.FOODBORNE_ILLNESS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS);
		disable(CaseDataDto.class, CaseDataDto.DATE_FORM_RECEIVED_AT_NATIONAL, Disease.NEW_INFLUENZA);
		disable(CaseDataDto.class, CaseDataDto.VACCINATION_ROUTINE_DATE, Disease.YELLOW_FEVER, Disease.AFP, Disease.UNSPECIFIED_VHF, Disease.NEW_INFLUENZA, Disease.CSM);
		disable(CaseDataDto.class, CaseDataDto.LAST_VACCINATION_DATE, Disease.YELLOW_FEVER, Disease.AFP, Disease.UNSPECIFIED_VHF, Disease.NEW_INFLUENZA, Disease.CSM, Disease.CORONAVIRUS, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX);
		disable(CaseDataDto.class, CaseDataDto.NOTIFIED_BY_LIST, Disease.YELLOW_FEVER, Disease.AFP, Disease.UNSPECIFIED_VHF, Disease.CORONAVIRUS, Disease.NEW_INFLUENZA, Disease.CHOLERA, Disease.CSM, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX, Disease.MEASLES, Disease.GUINEA_WORM);
		disable(CaseDataDto.class, CaseDataDto.INVESTIGATION_OFFICER_ADDRESS, Disease.YELLOW_FEVER, Disease.AFP, Disease.UNSPECIFIED_VHF, Disease.CORONAVIRUS, Disease.NEW_INFLUENZA, Disease.CHOLERA, Disease.CSM, Disease.FOODBORNE_ILLNESS, Disease.MEASLES, Disease.GUINEA_WORM);

		disable(LocationDto.class, LocationDto.ADDRESS_TYPE, Disease.UNSPECIFIED_VHF, Disease.AFP);
		disable(LocationDto.class, LocationDto.ADDRESS_TYPE_DETAILS, Disease.AFP);
		disable(LocationDto.class, LocationDto.REGION, Disease.UNSPECIFIED_VHF, Disease.NEW_INFLUENZA, Disease.FOODBORNE_ILLNESS);
		disable(LocationDto.class, LocationDto.DISTRICT, Disease.UNSPECIFIED_VHF, Disease.NEW_INFLUENZA, Disease.FOODBORNE_ILLNESS);
		disable(LocationDto.class, LocationDto.COMMUNITY, Disease.UNSPECIFIED_VHF, Disease.NEW_INFLUENZA, Disease.FOODBORNE_ILLNESS);
		disable(LocationDto.class, LocationDto.STREET, Disease.NEW_INFLUENZA, Disease.AFP, Disease.FOODBORNE_ILLNESS);
		disable(LocationDto.class, LocationDto.ADDITIONAL_INFORMATION, Disease.UNSPECIFIED_VHF);
		disable(LocationDto.class, LocationDto.HOUSE_NUMBER, Disease.UNSPECIFIED_VHF);
		disable(LocationDto.class, LocationDto.CITY, Disease.CSM, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS);
		disable(LocationDto.class, LocationDto.AREA_TYPE, Disease.CSM, Disease.UNSPECIFIED_VHF);
		disable(LocationDto.class, LocationDto.POSTAL_CODE, Disease.UNSPECIFIED_VHF, Disease.NEW_INFLUENZA, Disease.FOODBORNE_ILLNESS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS);
		disable(LocationDto.class, LocationDto.CONTACT_PERSON_FIRST_NAME, Disease.AFP);

		disable(HospitalizationDto.class, HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY, Disease.AFP, Disease.CSM);
		disable(HospitalizationDto.class, HospitalizationDto.ADMISSION_DATE, Disease.UNSPECIFIED_VHF);
		disable(HospitalizationDto.class, HospitalizationDto.DISCHARGE_DATE, Disease.AFP, Disease.CSM, Disease.UNSPECIFIED_VHF);
		disable(HospitalizationDto.class, HospitalizationDto.LEFT_AGAINST_ADVICE, Disease.AFP, Disease.CSM, Disease.UNSPECIFIED_VHF, Disease.NEW_INFLUENZA);
		disable(HospitalizationDto.class, HospitalizationDto.NOTIFY_DISTRICT_DATE, Disease.AFP, Disease.CSM, Disease.UNSPECIFIED_VHF, Disease.NEW_INFLUENZA, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX);
		disable(HospitalizationDto.class, HospitalizationDto.DATE_FORM_SENT_TO_DISTRICT, Disease.AFP, Disease.CSM, Disease.UNSPECIFIED_VHF, Disease.YELLOW_FEVER, Disease.NEW_INFLUENZA, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX);
		disable(HospitalizationDto.class, HospitalizationDto.INTENSIVE_CARE_UNIT, Disease.AFP, Disease.CSM);
		disable(HospitalizationDto.class, HospitalizationDto.ISOLATED, Disease.AFP, Disease.CSM, Disease.NEW_INFLUENZA);
		disable(HospitalizationDto.class, HospitalizationDto.DESCRIPTION, Disease.CSM, Disease.NEW_INFLUENZA);
		disable(HospitalizationDto.class, HospitalizationDto.HOSPITALIZATION_YES_NO, Disease.AFP, Disease.CSM, Disease.UNSPECIFIED_VHF, Disease.YELLOW_FEVER, Disease.NEW_INFLUENZA, Disease.NEONATAL_TETANUS, Disease.MONKEYPOX, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, Disease.CHOLERA, Disease.MEASLES, Disease.CORONAVIRUS, Disease.GUINEA_WORM);

		disable(EpiDataDto.class, EpiDataDto.EXPOSURE_DETAILS_KNOWN, Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, Disease.UNSPECIFIED_VHF, Disease.NEW_INFLUENZA, Disease.YELLOW_FEVER, Disease.FOODBORNE_ILLNESS);
		disable(EpiDataDto.class, EpiDataDto.EXPOSURES, Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, Disease.UNSPECIFIED_VHF, Disease.NEW_INFLUENZA, Disease.YELLOW_FEVER, Disease.FOODBORNE_ILLNESS);
		disable(EpiDataDto.class, EpiDataDto.ACTIVITY_AS_CASE_DETAILS_KNOWN, Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, Disease.UNSPECIFIED_VHF, Disease.NEW_INFLUENZA, Disease.CSM, Disease.FOODBORNE_ILLNESS);
		disable(EpiDataDto.class, EpiDataDto.ACTIVITIES_AS_CASE, Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, Disease.UNSPECIFIED_VHF, Disease.FOODBORNE_ILLNESS);
		disable(EpiDataDto.class, EpiDataDto.HIGH_TRANSMISSION_RISK_AREA, Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, Disease.UNSPECIFIED_VHF, Disease.NEW_INFLUENZA, Disease.CSM, Disease.YELLOW_FEVER, Disease.FOODBORNE_ILLNESS);
		disable(EpiDataDto.class, EpiDataDto.LARGE_OUTBREAKS_AREA, Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, Disease.UNSPECIFIED_VHF, Disease.NEW_INFLUENZA, Disease.CSM, Disease.YELLOW_FEVER, Disease.FOODBORNE_ILLNESS);
		disable(EpiDataDto.class, EpiDataDto.AREA_INFECTED_ANIMALS, Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, Disease.UNSPECIFIED_VHF, Disease.NEW_INFLUENZA, Disease.YELLOW_FEVER, Disease.FOODBORNE_ILLNESS);
		disable(EpiDataDto.class, EpiDataDto.CONTACT_WITH_SOURCE_CASE_KNOWN, Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, Disease.UNSPECIFIED_VHF, Disease.NEW_INFLUENZA, Disease.YELLOW_FEVER, Disease.FOODBORNE_ILLNESS);
		disable(EpiDataDto.class, EpiDataDto.CONTACT_SICK_ANIMALS, Disease.AFP, Disease.CSM, Disease.YELLOW_FEVER, Disease.FOODBORNE_ILLNESS);
		disable(EpiDataDto.class, EpiDataDto.RECENT_TRAVEL_OUTBREAK, Disease.CSM, Disease.YELLOW_FEVER, Disease.FOODBORNE_ILLNESS);
		disable(EpiDataDto.class, EpiDataDto.CONTACT_SIMILAR_SYMPTOMS, Disease.YELLOW_FEVER, Disease.FOODBORNE_ILLNESS);
		disable(EpiDataDto.class, EpiDataDto.PATIENT_TRAVELLED_TWO_WEEKS_PRIOR, Disease.YELLOW_FEVER, Disease.CSM, Disease.UNSPECIFIED_VHF, Disease.AFP, Disease.NEW_INFLUENZA);
		disable(EpiDataDto.class, EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_ONE, Disease.YELLOW_FEVER, Disease.CSM, Disease.UNSPECIFIED_VHF, Disease.AFP, Disease.NEW_INFLUENZA);
		disable(EpiDataDto.class, EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_TWO, Disease.YELLOW_FEVER, Disease.CSM, Disease.UNSPECIFIED_VHF, Disease.AFP, Disease.NEW_INFLUENZA);
		disable(EpiDataDto.class, EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_THREE, Disease.YELLOW_FEVER, Disease.CSM, Disease.UNSPECIFIED_VHF, Disease.AFP, Disease.NEW_INFLUENZA);
		disable(EpiDataDto.class, EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_FOUR, Disease.YELLOW_FEVER, Disease.CSM, Disease.UNSPECIFIED_VHF, Disease.AFP, Disease.NEW_INFLUENZA);
		disable(EpiDataDto.class, EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_ONE, Disease.YELLOW_FEVER, Disease.CSM, Disease.UNSPECIFIED_VHF, Disease.AFP, Disease.NEW_INFLUENZA);
		disable(EpiDataDto.class, EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_TWO, Disease.YELLOW_FEVER, Disease.CSM, Disease.UNSPECIFIED_VHF, Disease.AFP, Disease.NEW_INFLUENZA);
		disable(EpiDataDto.class, EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_THREE, Disease.YELLOW_FEVER, Disease.CSM, Disease.UNSPECIFIED_VHF, Disease.AFP, Disease.NEW_INFLUENZA);
		disable(EpiDataDto.class, EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_FOUR, Disease.YELLOW_FEVER, Disease.CSM, Disease.UNSPECIFIED_VHF, Disease.AFP, Disease.NEW_INFLUENZA);
		disable(EpiDataDto.class, EpiDataDto.PATIENT_VISITED_HEALTH_CARE_FACILITY, Disease.YELLOW_FEVER, Disease.CSM, Disease.UNSPECIFIED_VHF, Disease.AFP, Disease.NEW_INFLUENZA);
		disable(EpiDataDto.class, EpiDataDto.PATIENT_CLOSE_CONTACT_WITH_ARI, Disease.YELLOW_FEVER, Disease.CSM, Disease.UNSPECIFIED_VHF, Disease.AFP, Disease.NEW_INFLUENZA);
		disable(EpiDataDto.class, EpiDataDto.PATIENT_CLOSE_CONTACT_WITH_ARI_CONTACT_SETTINGS, Disease.YELLOW_FEVER, Disease.CSM, Disease.UNSPECIFIED_VHF, Disease.AFP, Disease.NEW_INFLUENZA);
		disable(EpiDataDto.class, EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE, Disease.YELLOW_FEVER, Disease.CSM, Disease.UNSPECIFIED_VHF, Disease.AFP, Disease.NEW_INFLUENZA);
		disable(EpiDataDto.class, EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE_EXPOSURE_LOCATIONS, Disease.YELLOW_FEVER, Disease.CSM, Disease.UNSPECIFIED_VHF, Disease.AFP, Disease.NEW_INFLUENZA);
		disable(EpiDataDto.class, EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE_EXPOSURE_LOCATION_CITY_COUNTRY, Disease.YELLOW_FEVER, Disease.CSM, Disease.UNSPECIFIED_VHF, Disease.AFP, Disease.NEW_INFLUENZA);

		disable(SampleDto.class, SampleDto.PATHOGEN_TESTING_REQUESTED, Disease.NEW_INFLUENZA, Disease.CSM, Disease.UNSPECIFIED_VHF);
		disable(SampleDto.class, SampleDto.SAMPLE_PURPOSE, Disease.AFP, Disease.UNSPECIFIED_VHF);
		disable(SampleDto.class, SampleDto.REQUESTED_SAMPLE_MATERIALS, Disease.AFP, Disease.UNSPECIFIED_VHF);
		disable(SampleDto.class, SampleDto.SAMPLE_MATERIAL_TEXT, Disease.AFP, Disease.YELLOW_FEVER);
		disable(SampleDto.class, SampleDto.SAMPLE_MATERIAL_REQUESTED, Disease.AFP, Disease.UNSPECIFIED_VHF);
		disable(SampleDto.class, SampleDto.COMMENT, Disease.AFP, Disease.UNSPECIFIED_VHF);
		disable(SampleDto.class, SampleDto.SAMPLING_REASON, Disease.AFP, Disease.UNSPECIFIED_VHF, Disease.YELLOW_FEVER);
		disable(SampleDto.class, SampleDto.SAMPLE_MATERIAL, Disease.AFP, Disease.FOODBORNE_ILLNESS);
		disable(SampleDto.class, SampleDto.PATHOGEN_TEST_RESULT, Disease.AFP);
		disable(SampleDto.class, SampleDto.SAMPLE_SOURCE, Disease.AFP, Disease.UNSPECIFIED_VHF, Disease.YELLOW_FEVER);
		disable(SampleDto.class, SampleDto.SAMPLE_DATE_TIME, Disease.AFP, Disease.FOODBORNE_ILLNESS);
		disable(SampleDto.class, SampleDto.SUSPECTED_DISEASE, Disease.FOODBORNE_ILLNESS);
		disable(SampleDto.class, SampleDto.LAB_LOCATION, Disease.FOODBORNE_ILLNESS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS);
		disable(SampleDto.class, SampleDto.DATE_LAB_RECEIVED_SPECIMEN, Disease.FOODBORNE_ILLNESS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS);
		disable(SampleDto.class, SampleDto.LABORATORY_SAMPLE_CONDITION, Disease.FOODBORNE_ILLNESS);
		disable(SampleDto.class, SampleDto.DATE_FORM_SENT_TO_DISTRICT, Disease.FOODBORNE_ILLNESS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS);
		disable(SampleDto.class, SampleDto.DATE_FORM_RECEIVED_AT_DISTRICT, Disease.FOODBORNE_ILLNESS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS);
		disable(SampleDto.class, SampleDto.DATE_RESULTS_RECEIVED_SENT_TO_CLINICIAN, Disease.FOODBORNE_ILLNESS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS);
		disable(SampleDto.class, SampleDto.ADDITIONAL_TESTING_REQUESTED, Disease.FOODBORNE_ILLNESS);
		disable(SampleDto.class, SampleDto.SPECIMEN_CONDITION, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS);

		disable(PersonDto.class, PersonDto.PRESENT_CONDITION, Disease.FOODBORNE_ILLNESS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, Disease.NEW_INFLUENZA, Disease.UNSPECIFIED_VHF, Disease.YELLOW_FEVER);
		disable(PersonDto.class, PersonDto.PASSPORT_NUMBER, Disease.AFP);
		disable(PersonDto.class, PersonDto.NUMBER_OF_PEOPLE, Disease.CSM, Disease.NEW_INFLUENZA, Disease.AFP, Disease.YELLOW_FEVER, Disease.UNSPECIFIED_VHF);
		disable(PersonDto.class, PersonDto.NUMBER_OF_OTHER_CONTACTS, Disease.CSM, Disease.NEW_INFLUENZA, Disease.AFP, Disease.YELLOW_FEVER, Disease.UNSPECIFIED_VHF);
		disable(PersonDto.class, PersonDto.OCCUPATION_DETAILS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, Disease.UNSPECIFIED_VHF);
		disable(PersonDto.class, PersonDto.EDUCATION_TYPE, Disease.AFP, Disease.CSM, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, Disease.UNSPECIFIED_VHF, Disease.FOODBORNE_ILLNESS);
		disable(PersonDto.class, PersonDto.BIRTH_NAME, Disease.AFP);
		disable(PersonDto.class, PersonDto.MOTHERS_NAME, Disease.FOODBORNE_ILLNESS);
		disable(PersonDto.class, PersonDto.FATHERS_NAME, Disease.FOODBORNE_ILLNESS);
		disable(PersonDto.class, PersonDto.NAMES_OF_GUARDIANS, Disease.AFP);
		disable(PersonDto.class, PersonDto.BIRTH_COUNTRY, Disease.UNSPECIFIED_VHF, Disease.AFP);
		disable(PersonDto.class, PersonDto.ADDITIONAL_DETAILS, Disease.AFP);
		disable(PersonDto.class, PersonDto.MARRIAGE_STATUS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS);

		disable(RiskFactorDto.class, RiskFactorDto.DRINKING_WATER_SOURCE_ONE, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.DRINKING_WATER_SOURCE_TWO, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.DRINKING_WATER_SOURCE_THREE, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.DRINKING_WATER_SOURCE_FOUR, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.NON_DRINKING_WATER_SOURCE_ONE, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.NON_DRINKING_WATER_SOURCE_TWO, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.NON_DRINKING_WATER_SOURCE_THREE, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.NON_DRINKING_WATER_SOURCE_FOUR, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.FOOD_ITEMS_ONE, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.FOOD_ITEMS_TWO, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.FOOD_ITEMS_THREE, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.FOOD_ITEMS_FOUR, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.FOOD_ITEMS_FIVE, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.FOOD_ITEMS_SIX, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.FOOD_ITEMS_SEVEN, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.FOOD_ITEMS_EIGHT, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.DRINKING_WATER_INFECTED_BY_VIBRIO, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.NON_DRINKING_WATER_INFECTED_BY_VIBRIO, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.FOOD_ITEMS_INFECTED_BY_VIBRIO, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.WATER_USED_FOR_DRINKING, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_ONE, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_TWO, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_THREE, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_FOUR, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_FIVE, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_ONE, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_TWO, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_THREE, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_FOUR, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_FIVE, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_ATTEND_ANY_FUNERAL, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_ATTEND_ANY_SOCIAL_EVENT, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.OTHER_SOCIAL_EVENT_DETAILS, Disease.MONKEYPOX);
		disable(RiskFactorDto.class, RiskFactorDto.PATIENT_SYMPTOMS_PRECEDENCE, Disease.CHOLERA);
		disable(RiskFactorDto.class, RiskFactorDto.PATIENT_TRAVEL_DETAILS_PRIOR, Disease.CHOLERA);
		disable(RiskFactorDto.class, RiskFactorDto.PATIENT_TRAVEL_DETAILS_DURING, Disease.CHOLERA);

		configureCaseLayouts();
		configureSampleLayouts();
		configurePathogenTestLayouts();
		configureDiseaseEntityLayouts();
	}

	private DiseaseSpecificExportFieldConfiguration() {
	}

	private static void disable(Class<?> dtoClass, String property, Disease... diseases) {
		Map<String, Set<Disease>> classConfiguration = DISABLED_FIELDS.computeIfAbsent(dtoClass, key -> new HashMap<String, Set<Disease>>());
		Set<Disease> disabledDiseases = classConfiguration.computeIfAbsent(property, key -> EnumSet.noneOf(Disease.class));
		Collections.addAll(disabledDiseases, diseases);
	}

	public static boolean isDisabled(Class<?> dtoClass, String property, Disease disease) {
		return getDisabledFields(dtoClass, disease).contains(property);
	}

	public static Set<String> getDisabledFields(Class<?> dtoClass, Disease disease) {
		Map<String, Set<Disease>> classConfiguration = DISABLED_FIELDS.get(dtoClass);
		if (classConfiguration == null || disease == null) {
			return Collections.emptySet();
		}
		Set<String> disabled = new HashSet<>();
		for (Map.Entry<String, Set<Disease>> entry : classConfiguration.entrySet()) {
			if (entry.getValue().contains(disease)) {
				disabled.add(entry.getKey());
			}
		}
		return disabled;
	}

	/**
	 * Returns whether a DTO property occurs in the selected disease's explicit
	 * form layout. Missing allow-lists exclude the field.
	 */
	public static boolean isLayoutField(Class<?> dtoClass, String property, Disease disease) {
		Set<String> fields = getLayoutFields(dtoClass, disease);
		return fields != null && fields.contains(property);
	}

	public static boolean hasExplicitLayout(Class<?> dtoClass, Disease disease) {
		return getLayoutFields(dtoClass, disease) != null;
	}

	/** Shared symptom-group HTML still needs @Diseases / country filtering. Dedicated layouts do not. */
	public static boolean applyAnnotationVisibility(Class<?> dtoClass, Disease disease) {
		return dtoClass == SymptomsDto.class && DEFAULT_SYMPTOM_LAYOUT_DISEASES.contains(disease);
	}

	/** Returns the zero-based form layout position, or {@code null} for DTOs without an explicit layout. */
	public static Integer getLayoutOrder(Class<?> dtoClass, String property, Disease disease) {
		Set<String> fields = getLayoutFields(dtoClass, disease);
		if (fields == null) {
			return null;
		}
		int index = 0;
		for (String field : fields) {
			if (field.equals(property)) {
				return index;
			}
			index++;
		}
		return Integer.MAX_VALUE;
	}

	private static Set<String> getLayoutFields(Class<?> dtoClass, Disease disease) {
		Map<Disease, Set<String>> layouts;
		if (dtoClass == CaseDataDto.class) {
			layouts = CASE_LAYOUT_FIELDS;
		} else if (dtoClass == SampleDto.class) {
			layouts = SAMPLE_LAYOUT_FIELDS;
		} else if (dtoClass == PathogenTestDto.class) {
			layouts = PATHOGEN_TEST_LAYOUT_FIELDS;
		} else {
			layouts = ENTITY_LAYOUT_FIELDS.get(dtoClass);
			if (layouts == null) {
				return null;
			}
		}
		return layouts.get(disease);
	}

	private static void configureDiseaseEntityLayouts() {
		configurePersonLayouts();
		configureLocationLayouts();
		configureHospitalizationLayouts();
		configureSymptomsLayouts();
		configureEpiDataLayouts();
		configureRiskFactorAndAfpLayouts();

		allowEntityFields(
			SixtyDayDto.class,
			Disease.AFP,
			SixtyDayDto.UUID,
			SixtyDayDto.PERSON_EXAMINE_CASE,
			SixtyDayDto.DATE_OF_FOLLOWUP,
			SixtyDayDto.DATE_BIRTH,
			SixtyDayDto.RESIDENTIAL_LOCATION,
			SixtyDayDto.PATIENT_FOUND,
			SixtyDayDto.PATIENT_FOUND_REASON,
			SixtyDayDto.LOCATE_CHILD_ATTEMPT,
			SixtyDayDto.PARALYSIS_WEAKNESS_PRESENT,
			SixtyDayDto.PARALYSIS_WEAKNESS_PRESENT_SITE,
			SixtyDayDto.PARALYZED_PART_OTHER,
			SixtyDayDto.PARALYSIS_WEAKNESS_FLOPPY,
			SixtyDayDto.PARALYZED_PART,
			SixtyDayDto.OTHER_PART_BODY,
			SixtyDayDto.DEEP_TENDON_REFLEX_SELECTION,
			SixtyDayDto.MUSCLE_VOLUME_SELECTION,
			SixtyDayDto.SENSORY_LOSS_SELECTION,
			SixtyDayDto.PROVISIONAL_DIAGNOSIS,
			SixtyDayDto.COMMENTS,
			SixtyDayDto.CONTACT_DETAILS_NUMBER,
			SixtyDayDto.CONTACT_DETAILS_EMAIL,
			SixtyDayDto.SIGNATURE);

		allowEntityFields(
			SixtyDayDto.class,
			Disease.FOODBORNE_ILLNESS,
			SixtyDayDto.UUID,
			SixtyDayDto.FOOD_AVAILABLE_TESTING,
			SixtyDayDto.LAB_TEST_CONDUCTED,
			SixtyDayDto.SPECIFY_FOODS_SOURCES,
			SixtyDayDto.SPECIFY_SOURCES,
			SixtyDayDto.PRODUCT_NAME,
			SixtyDayDto.BATCH_NUMBER,
			SixtyDayDto.BARCODE,
			SixtyDayDto.DATE_OF_MANUFACTURE,
			SixtyDayDto.EXPIRATION_DATE,
			SixtyDayDto.PACKAGE_SIZE,
			SixtyDayDto.PACKAGING_TYPE,
			SixtyDayDto.PACKAGING_TYPE_OTHER,
			SixtyDayDto.PLACE_OF_PURCHASE,
			SixtyDayDto.NAME_OF_MANUFACTURER,
			SixtyDayDto.ADDRESS,
			SixtyDayDto.FOOD_TEL);

		allowEntityFields(
			FoodHistoryDto.class,
			Disease.FOODBORNE_ILLNESS,
			FoodHistoryDto.UUID,
			FoodHistoryDto.AFFECTED_PERSONS,
			FoodHistoryDto.NUMBER_OF_PEOPLE_ATE_IMPLICATED_FOOD,
			FoodHistoryDto.NUMBER_AFFECTED,
			FoodHistoryDto.SUSPECTED_FOOD,
			FoodHistoryDto.DATE_CONSUMED,
			FoodHistoryDto.FOOD_SOURCE,
			FoodHistoryDto.FOOD_SOURCE_OTHER,
			FoodHistoryDto.EVENT_TYPE,
			FoodHistoryDto.EVENT_OTHER_SPECIFY,
			FoodHistoryDto.BREAKFAST,
			FoodHistoryDto.TOTAL_NO_PERSONS,
			FoodHistoryDto.FOOD_CONSUMED,
			FoodHistoryDto.SOURCE_OF_FOOD,
			FoodHistoryDto.CONSUMED_AT_PLACE,
			FoodHistoryDto.LUNCH,
			FoodHistoryDto.TOTAL_NO_PERSONS_L1,
			FoodHistoryDto.FOOD_CONSUMED_L1,
			FoodHistoryDto.SOURCE_OF_FOOD_L1,
			FoodHistoryDto.CONSUMED_AT_PLACE_L1,
			FoodHistoryDto.SUPPER,
			FoodHistoryDto.TOTAL_NO_PERSONS_S1,
			FoodHistoryDto.FOOD_CONSUMED_S1,
			FoodHistoryDto.SOURCE_OF_FOODS_S1,
			FoodHistoryDto.CONSUMED_AT_PLACE_S1,
			FoodHistoryDto.BREAKFAST2,
			FoodHistoryDto.TOTAL_NO_PERSONS2,
			FoodHistoryDto.FOOD_CONSUMED2,
			FoodHistoryDto.SOURCE_OF_FOOD2,
			FoodHistoryDto.CONSUMED_AT_PLACE2,
			FoodHistoryDto.LUNCH_L2,
			FoodHistoryDto.TOTAL_NO_PERSONS_L2,
			FoodHistoryDto.FOOD_CONSUMED_L2,
			FoodHistoryDto.SOURCE_OF_FOOD_L2,
			FoodHistoryDto.CONSUMED_AT_PLACE_L2,
			FoodHistoryDto.SUPPER_S2,
			FoodHistoryDto.TOTAL_NO_PERSONS_S2,
			FoodHistoryDto.FOOD_CONSUMED_S2,
			FoodHistoryDto.SOURCE_OF_FOOD_S2,
			FoodHistoryDto.CONSUMED_AT_PLACE_S2,
			FoodHistoryDto.BREAKFAST3,
			FoodHistoryDto.TOTAL_NO_PERSONS3,
			FoodHistoryDto.FOOD_CONSUMED3,
			FoodHistoryDto.SOURCE_OF_FOOD3,
			FoodHistoryDto.CONSUMED_AT_PLACE3,
			FoodHistoryDto.LUNCH_L3,
			FoodHistoryDto.TOTAL_NO_PERSONS_L3,
			FoodHistoryDto.FOOD_CONSUMED_L3,
			FoodHistoryDto.SOURCE_OF_FOOD_L3,
			FoodHistoryDto.CONSUMED_AT_PLACE_L3,
			FoodHistoryDto.SUPPER_S3,
			FoodHistoryDto.TOTAL_NO_PERSONS_S3,
			FoodHistoryDto.FOOD_CONSUMED_S3,
			FoodHistoryDto.SOURCE_OF_FOOD_S3,
			FoodHistoryDto.CONSUMED_AT_PLACE_S3);

		allowEntityFields(
			InvestigationNotesDto.class,
			Disease.FOODBORNE_ILLNESS,
			InvestigationNotesDto.UUID,
			InvestigationNotesDto.SURNAME,
			InvestigationNotesDto.FIRSTNAME,
			InvestigationNotesDto.MIDDLENAME,
			InvestigationNotesDto.TEL_NO,
			InvestigationNotesDto.DATE_OF_COMPLETION_OF_FORM,
			InvestigationNotesDto.NAME_OF_HEALTH_FACILITY,
			InvestigationNotesDto.INVESTIGATION_NOTES,
			InvestigationNotesDto.SUSPECTED_DIAGNOSIS,
			InvestigationNotesDto.CONFIRMED_DIAGNOSIS,
			InvestigationNotesDto.INVESTIGATED_BY,
			InvestigationNotesDto.INVESTIGATOR_SIGNATURE,
			InvestigationNotesDto.INVESTIGATOR_DATE);
	}

	private static void configureCaseLayouts() {
		allowCaseFields(
			Disease.GUINEA_WORM,
			CaseDataDto.UUID,
			CaseDataDto.REPORTING_USER,
			CaseDataDto.CASE_CLASSIFICATION,
			CaseDataDto.HEALTH_FACILITY,
			CaseDataDto.HEALTH_FACILITY_DETAILS,
			CaseDataDto.REPORTING_VILLAGE,
			CaseDataDto.REPORTING_ZONE,
			CaseDataDto.RESPONSIBLE_REGION,
			CaseDataDto.RESPONSIBLE_DISTRICT,
			CaseDataDto.REPORT_DATE,
			CaseDataDto.REPORTING_OFFICER_NAME,
			CaseDataDto.REPORTING_OFFICER_TITLE,
			CaseDataDto.DATE_OF_INVESTIGATION,
			CaseDataDto.INVESTIGATION_OFFICER_NAME,
			CaseDataDto.INVESTIGATION_OFFICER_POSITION,
			CaseDataDto.FORM_COMPLETED_BY_NAME,
			CaseDataDto.FORM_COMPLETED_BY_POSITION,
			CaseDataDto.FORM_COMPLETED_BY_CELL_PHONE_NO,
			CaseDataDto.SURVEILLANCE_OFFICER);

		allowCaseFields(
			Disease.MONKEYPOX,
			CaseDataDto.UUID,
			CaseDataDto.REPORTING_USER,
			CaseDataDto.EPID_NUMBER,
			CaseDataDto.CASE_CLASSIFICATION,
			CaseDataDto.RESPONSIBLE_REGION,
			CaseDataDto.RESPONSIBLE_DISTRICT,
			CaseDataDto.RESPONSIBLE_COMMUNITY,
			CaseDataDto.HEALTH_FACILITY,
			CaseDataDto.HEALTH_FACILITY_DETAILS,
			CaseDataDto.NOTIFIED_BY,
			CaseDataDto.DATE_OF_NOTIFICATION,
			CaseDataDto.DATE_OF_INVESTIGATION,
			CaseDataDto.REPORTING_OFFICER_NAME,
			CaseDataDto.REPORTING_OFFICER_TITLE,
			CaseDataDto.INVESTIGATION_OFFICER_ADDRESS,
			CaseDataDto.REPORTING_OFFICER_CONTACT_PHONE,
			CaseDataDto.REPORTING_OFFICER_EMAIL);

		allowCaseFields(Disease.MEASLES, ghanaCaseCore(
			CaseDataDto.NOTIFIED_BY_LIST,
			CaseDataDto.NOTIFIED_OTHER,
			CaseDataDto.INVESTIGATION_STATUS,
			CaseDataDto.INVESTIGATED_DATE,
			CaseDataDto.REPORT_LAT,
			CaseDataDto.REPORT_LON,
			CaseDataDto.REPORT_LAT_LON_ACCURACY,
			CaseDataDto.VACCINATION_TYPE,
			CaseDataDto.NUMBER_OF_DOSES,
			CaseDataDto.VACCINATION_ROUTINE,
			CaseDataDto.VACCINATION_DATE,
			CaseDataDto.DATE_FORM_RECEIVED_AT_DISTRICT));

		allowCaseFields(Disease.YELLOW_FEVER, ghanaCaseCore(
			CaseDataDto.VACCINATION_TYPE,
			CaseDataDto.NUMBER_OF_DOSES,
			CaseDataDto.VACCINATION_DATE));
		CASE_LAYOUT_FIELDS.get(Disease.YELLOW_FEVER).remove(CaseDataDto.REPORTING_OFFICER_NAME);

		allowCaseFields(Disease.CSM, ghanaCaseCore(
			CaseDataDto.VACCINE_TYPE,
			CaseDataDto.NUMBER_OF_DOSES,
			CaseDataDto.VACCINATION_DATE));
		CASE_LAYOUT_FIELDS.get(Disease.CSM).remove(CaseDataDto.REPORTING_OFFICER_EMAIL);

		allowCaseFields(Disease.CORONAVIRUS, ghanaCaseCore(
			CaseDataDto.CASE_TRANSMISSION_CLASSIFICATION,
			CaseDataDto.NUMBER_OF_DOSES,
			CaseDataDto.VACCINATION_DATE,
			CaseDataDto.SECOND_VACCINATION_DATE));

		allowCaseFields(
			Disease.CHOLERA,
			ghanaCaseCore(
				CaseDataDto.VACCINATION_TYPE,
				CaseDataDto.NUMBER_OF_DOSES,
				CaseDataDto.VACCINATION_DATE,
				CaseDataDto.OTHER_NOTES_AND_OBSERVATIONS,
				CaseDataDto.DATE_LATEST_UPDATE_RECORD,
				CaseDataDto.NUMBER_OF_PEOPLE_IN_SAME_HOUSEHOLD));

		allowCaseFields(Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, ghanaCaseCore(
			CaseDataDto.NUMBER_OF_DOSES,
			CaseDataDto.VACCINATION_DATE));
		CASE_LAYOUT_FIELDS.get(Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS).remove(CaseDataDto.FUNCTION_OF_REPORTING_OFFICER);
		CASE_LAYOUT_FIELDS.get(Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS).remove(CaseDataDto.REPORTING_OFFICER_CONTACT_PHONE);
		CASE_LAYOUT_FIELDS.get(Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS).remove(CaseDataDto.REPORTING_OFFICER_EMAIL);

		allowCaseFields(
			Disease.AFP,
			CaseDataDto.UUID,
			CaseDataDto.REPORT_DATE,
			CaseDataDto.REPORTING_USER,
			CaseDataDto.CASE_CLASSIFICATION,
			CaseDataDto.CLASSIFICATION_DATE,
			CaseDataDto.CLASSIFICATION_USER,
			CaseDataDto.INVESTIGATION_STATUS,
			CaseDataDto.INVESTIGATED_DATE,
			CaseDataDto.EPID_NUMBER,
			CaseDataDto.DISEASE,
			CaseDataDto.CASE_ORIGIN,
			CaseDataDto.RESPONSIBLE_REGION,
			CaseDataDto.RESPONSIBLE_DISTRICT,
			CaseDataDto.RESPONSIBLE_COMMUNITY,
			CaseDataDto.HEALTH_FACILITY,
			CaseDataDto.HEALTH_FACILITY_DETAILS,
			CaseDataDto.NOTIFIED_BY_LIST,
			CaseDataDto.DATE_OF_NOTIFICATION,
			CaseDataDto.DATE_OF_INVESTIGATION,
			CaseDataDto.DATE_FORM_RECEIVED_AT_DISTRICT);

		allowCaseFields(
			Disease.NEW_INFLUENZA,
			CaseDataDto.UUID,
			CaseDataDto.REPORT_DATE,
			CaseDataDto.REPORTING_USER,
			CaseDataDto.CASE_CLASSIFICATION,
			CaseDataDto.CLASSIFICATION_DATE,
			CaseDataDto.CLASSIFICATION_USER,
			CaseDataDto.INVESTIGATION_STATUS,
			CaseDataDto.INVESTIGATED_DATE,
			CaseDataDto.EPID_NUMBER,
			CaseDataDto.DISEASE,
			CaseDataDto.CASE_ORIGIN,
			CaseDataDto.RESPONSIBLE_REGION,
			CaseDataDto.RESPONSIBLE_DISTRICT,
			CaseDataDto.RESPONSIBLE_COMMUNITY,
			CaseDataDto.HEALTH_FACILITY,
			CaseDataDto.HEALTH_FACILITY_DETAILS,
			CaseDataDto.VACCINATION_STATUS,
			CaseDataDto.DATE_FORM_RECEIVED_AT_DISTRICT);

		allowCaseFields(
			Disease.FOODBORNE_ILLNESS,
			CaseDataDto.UUID,
			CaseDataDto.REPORT_DATE,
			CaseDataDto.REPORTING_USER,
			CaseDataDto.CASE_CLASSIFICATION,
			CaseDataDto.INVESTIGATION_STATUS,
			CaseDataDto.INVESTIGATED_DATE,
			CaseDataDto.EPID_NUMBER,
			CaseDataDto.RESPONSIBLE_REGION,
			CaseDataDto.RESPONSIBLE_DISTRICT,
			CaseDataDto.RESPONSIBLE_COMMUNITY,
			CaseDataDto.HEALTH_FACILITY,
			CaseDataDto.HEALTH_FACILITY_DETAILS,
			CaseDataDto.VACCINATION_STATUS,
			CaseDataDto.DATE_FORM_RECEIVED_AT_DISTRICT,
			CaseDataDto.DATE_OF_INVESTIGATION);

		allowCaseFields(
			Disease.UNSPECIFIED_VHF,
			CaseDataDto.UUID,
			CaseDataDto.REPORT_DATE,
			CaseDataDto.REPORTING_USER,
			CaseDataDto.CASE_CLASSIFICATION,
			CaseDataDto.CLASSIFICATION_DATE,
			CaseDataDto.CLASSIFICATION_USER,
			CaseDataDto.INVESTIGATION_STATUS,
			CaseDataDto.INVESTIGATED_DATE,
			CaseDataDto.EPID_NUMBER,
			CaseDataDto.DISEASE,
			CaseDataDto.CASE_ORIGIN,
			CaseDataDto.RESPONSIBLE_REGION,
			CaseDataDto.RESPONSIBLE_DISTRICT,
			CaseDataDto.RESPONSIBLE_COMMUNITY,
			CaseDataDto.HEALTH_FACILITY,
			CaseDataDto.HEALTH_FACILITY_DETAILS,
			CaseDataDto.NOTIFIED_BY_LIST,
			CaseDataDto.DATE_OF_INVESTIGATION,
			CaseDataDto.MOBILE_TEAM_NO,
			CaseDataDto.INFORMATION_GIVEN_BY,
			CaseDataDto.FAMILY_LINK_WITH_PATIENT,
			CaseDataDto.NAME_OF_VILLAGE_PERSON_GOT_ILL,
			CaseDataDto.DATE_FORM_RECEIVED_AT_DISTRICT);

		allowCaseFields(
			Disease.NEONATAL_TETANUS,
			CaseDataDto.UUID,
			CaseDataDto.REPORT_DATE,
			CaseDataDto.REPORTING_USER,
			CaseDataDto.CASE_CLASSIFICATION,
			CaseDataDto.CLASSIFICATION_DATE,
			CaseDataDto.CLASSIFICATION_USER,
			CaseDataDto.INVESTIGATION_STATUS,
			CaseDataDto.INVESTIGATED_DATE,
			CaseDataDto.EPID_NUMBER,
			CaseDataDto.DISEASE,
			CaseDataDto.CASE_ORIGIN,
			CaseDataDto.RESPONSIBLE_REGION,
			CaseDataDto.RESPONSIBLE_DISTRICT,
			CaseDataDto.RESPONSIBLE_COMMUNITY,
			CaseDataDto.HEALTH_FACILITY,
			CaseDataDto.HEALTH_FACILITY_DETAILS,
			CaseDataDto.NOTIFIED_BY,
			CaseDataDto.DATE_OF_NOTIFICATION,
			CaseDataDto.DATE_OF_INVESTIGATION,
			CaseDataDto.MOTHER_VACCINATED_WITH_TT,
			CaseDataDto.MOTHER_HAVE_CARD,
			CaseDataDto.MOTHER_NUMBER_OF_DOSES,
			CaseDataDto.MOTHER_VACCINATION_STATUS,
			CaseDataDto.MOTHER_TT_DATE_ONE,
			CaseDataDto.MOTHER_TT_DATE_TWO,
			CaseDataDto.MOTHER_TT_DATE_THREE,
			CaseDataDto.MOTHER_TT_DATE_FOUR,
			CaseDataDto.MOTHER_TT_DATE_FIVE,
			CaseDataDto.MOTHER_LAST_DOSE_DATE,
			CaseDataDto.SEEN_IN_OPD,
			CaseDataDto.ADMITTED_IN_OPD,
			CaseDataDto.MOTHER_GIVEN_PROTECTIVE_DOSE_TT,
			CaseDataDto.MOTHER_GIVEN_PROTECTIVE_DOSE_TT_DATE,
			CaseDataDto.SUPPLEMENTAL_IMMUNIZATION,
			CaseDataDto.SUPPLEMENTAL_IMMUNIZATION_DETAILS,
			CaseDataDto.DATE_FORM_RECEIVED_AT_DISTRICT);
	}

	private static void configureSampleLayouts() {
		allowSampleFields(
			Disease.CSM,
			SampleDto.UUID,
			SampleDto.CSF_SAMPLE_COLLECTED,
			SampleDto.CSF_REASON,
			SampleDto.SAMPLE_DATE_TIME,
			SampleDto.APPEARANCE_OF_CSF,
			SampleDto.INOCULATION_TIME_TRANSPORT_MEDIA,
			SampleDto.SAMPLE_SENT_TO_LAB,
			SampleDto.REASON_NOT_SENT_TO_LAB,
			SampleDto.DATE_SAMPLE_SENT_TO_LAB,
			SampleDto.LAB,
			SampleDto.LAB_DETAILS,
			SampleDto.SAMPLE_CONTAINER_USED,
			SampleDto.CONTAINER_OTHER,
			SampleDto.RDT_PERFORMED,
			SampleDto.RDT_RESULTS,
			SampleDto.DISTRICT_NOTIFICATION_DATE,
			SampleDto.NAME_OF_PERSON,
			SampleDto.TEL_NUMBER,
			SampleDto.DATE_FORM_SENT_TO_DISTRICT,
			SampleDto.DATE_FORM_RECEIVED_AT_DISTRICT,
			SampleDto.DATE_FORM_SENT_TO_REGION,
			SampleDto.DATE_FORM_RECEIVED_AT_REGION,
			SampleDto.DATE_FORM_SENT_TO_NATIONAL,
			SampleDto.DATE_FORM_RECEIVED_AT_NATIONAL,
			SampleDto.FIELD_SAMPLE_ID,
			SampleDto.SHIPPED,
			SampleDto.SHIPMENT_DATE,
			SampleDto.SHIPMENT_DETAILS,
			SampleDto.RECEIVED,
			SampleDto.RECEIVED_DATE,
			SampleDto.LAB_SAMPLE_ID,
			SampleDto.SPECIMEN_CONDITION,
			SampleDto.NO_TEST_POSSIBLE_REASON,
			SampleDto.LABORATORY_NUMBER,
			SampleDto.LABORATORY_SAMPLE_CONTAINER_RECEIVED,
			SampleDto.LABORATORY_SAMPLE_CONTAINER_OTHER,
			SampleDto.LABORATORY_APPEARANCE_OF_CSF);

		allowSampleFields(
			Disease.AFP,
			SampleDto.UUID,
			SampleDto.LAB,
			SampleDto.LAB_DETAILS,
			SampleDto.FIELD_SAMPLE_ID,
			SampleDto.DATE_FIRST_SPECIMEN,
			SampleDto.DATE_SECOND_SPECIMEN,
			SampleDto.DATE_SPECIMEN_SENT_NATIONAL_LEVEL,
			SampleDto.DATE_SPECIMEN_RECEIVED_NATIONAL_LEVEL,
			SampleDto.DATE_SPECIMEN_SENT_INTERCOUNTY_NATLAB,
			SampleDto.DATE_SPECIMEN_RECEIVED_INTERCOUNTY_NATLAB,
			SampleDto.STATUS_SPECIMEN_RECEPTION_AT_LAB,
			SampleDto.DATE_COMBINED_CELL_CULTURE_RESULTS,
			SampleDto.W1,
			SampleDto.W2,
			SampleDto.W3,
			SampleDto.SL1,
			SampleDto.SL2,
			SampleDto.SL3,
			SampleDto.DISCORDANT,
			SampleDto.FINAL_LAB_RESULTS,
			SampleDto.DATE_FOLLOWUP_EXAM,
			SampleDto.RESIDUAL_ANALYSIS,
			SampleDto.RESULT_EXAM,
			SampleDto.IMMUNOCOMPROMISED_STATUS_SUSPECTED,
			SampleDto.AFP_FINAL_CLASSIFICATION,
			SampleDto.DATE_SENT_NATIONAL_REG_LAB,
			SampleDto.DATE_DIFFERENTIATION_SENT_EPI,
			SampleDto.DATE_DIFFERENTIATION_RECEIVED_EPI,
			SampleDto.DATE_ISOLATE_SENT_SEQUENCING,
			SampleDto.DATE_SEQ_RESULTS_SENT_PROGRAM,
			SampleDto.SHIPPED,
			SampleDto.SHIPMENT_DATE,
			SampleDto.SHIPMENT_DETAILS,
			SampleDto.RECEIVED,
			SampleDto.RECEIVED_DATE,
			SampleDto.LAB_SAMPLE_ID,
			SampleDto.SPECIMEN_CONDITION,
			SampleDto.NO_TEST_POSSIBLE_REASON);

		allowSampleFields(
			Disease.YELLOW_FEVER,
			SampleDto.UUID,
			SampleDto.SAMPLE_DATE_TIME,
			SampleDto.LAB,
			SampleDto.LAB_DETAILS,
			SampleDto.FIELD_SAMPLE_ID,
			SampleDto.REQUESTED_SAMPLE_MATERIALS,
			SampleDto.SAMPLE_DISPATCH_MODE,
			SampleDto.SHIPPED,
			SampleDto.SHIPMENT_DATE,
			SampleDto.SHIPMENT_DETAILS,
			SampleDto.RECEIVED,
			SampleDto.RECEIVED_DATE,
			SampleDto.LAB_SAMPLE_ID,
			SampleDto.SPECIMEN_CONDITION,
			SampleDto.NO_TEST_POSSIBLE_REASON,
			SampleDto.PATHOGEN_TEST_RESULT,
			SampleDto.IPSAMPLESENT,
			SampleDto.IPSAMPLE_TEST_RESULTS,
			SampleDto.SELECTED_RESULT_IGM,
			SampleDto.SELECTED_RESULT_IGM_DATE,
			SampleDto.SELECTED_RESULT_PCR,
			SampleDto.SELECTED_RESULT_PCR_DATE,
			SampleDto.SELECTED_RESULT_PRNT,
			SampleDto.INPUT_VALUE_PRNT,
			SampleDto.SELECTED_RESULT_PRNT_DATE);

		allowCommonSampleFields(Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, SampleDto.SUSPECTED_DISEASE);
		allowCommonSampleFields(Disease.CORONAVIRUS, SampleDto.COMMENT, SampleDto.PATHOGEN_TEST_RESULT);
		allowCommonSampleFields(Disease.NEW_INFLUENZA, SampleDto.OTHER_TYPE);
		allowCommonSampleFields(Disease.MONKEYPOX);

		allowSampleFields(
			Disease.GUINEA_WORM,
			SampleDto.UUID,
			SampleDto.SAMPLE_DATE_TIME,
			SampleDto.LAB_LOCAL,
			SampleDto.LAB_LOCAL_DETAILS,
			SampleDto.LAB,
			SampleDto.LAB_DETAILS,
			SampleDto.FIELD_SAMPLE_ID,
			SampleDto.SPECIMEN_SAVED_AND_PRESEVED_IN_ALCOHOL,
			SampleDto.SPECIMEN_SAVED_AND_PRESEVED_IN_ALCOHOL_WHY,
			SampleDto.DATE_SPECIMEN_SENT_TO_REGION,
			SampleDto.DATE_SPECIMEN_RECEIVED_AT_REGION,
			SampleDto.NAME_OF_PERSON_WHO_RECEIVED_SPECIMEN_AT_REGION,
			SampleDto.DATE_SPECIMEN_SENT_TO_NATIONAL,
			SampleDto.DATE_SPECIMEN_RECEIVED_AT_NATIONAL,
			SampleDto.NAME_OF_PERSON_WHO_RECEIVED_SPECIMEN_AT_NATIONAL,
			SampleDto.SENT_FOR_CONFIRMATION_NATIONAL,
			SampleDto.SENT_FOR_CONFIRMATION_NATIONAL_DATE,
			SampleDto.SENT_FOR_CONFIRMATION_TO,
			SampleDto.DATE_RESULT_RECEIVED_NATIONAL,
			SampleDto.USE_OF_CLOTH_FILTER,
			SampleDto.FREQUENCY_OF_CHANGING_FILTERS,
			SampleDto.REMARKS,
			SampleDto.CONFIRMED_AS_GUINEA_WORM,
			SampleDto.PATHOGEN_TEST_RESULT);

		allowSampleFields(
			Disease.MEASLES,
			SampleDto.UUID,
			SampleDto.SAMPLE_DATE_TIME,
			SampleDto.LAB,
			SampleDto.LAB_DETAILS,
			SampleDto.FIELD_SAMPLE_ID,
			SampleDto.SAMPLE_MATERIAL,
			SampleDto.SAMPLE_MATERIAL_TEXT,
			SampleDto.DATE_FORM_SENT_TO_HIGHER_LEVEL,
			SampleDto.PERSON_COMPLETING_FORM,
			SampleDto.SHIPPED,
			SampleDto.SHIPMENT_DATE,
			SampleDto.SHIPMENT_DETAILS,
			SampleDto.RECEIVED,
			SampleDto.RECEIVED_DATE,
			SampleDto.LAB_SAMPLE_ID,
			SampleDto.SPECIMEN_CONDITION,
			SampleDto.NO_TEST_POSSIBLE_REASON,
			SampleDto.PATHOGEN_TEST_RESULT);

		allowSampleFields(
			Disease.CHOLERA,
			SampleDto.UUID,
			SampleDto.SAMPLE_DATE_TIME,
			SampleDto.LAB,
			SampleDto.LAB_DETAILS,
			SampleDto.SAMPLE_MATERIAL,
			SampleDto.SAMPLE_MATERIAL_TEXT,
			SampleDto.FIELD_SAMPLE_ID,
			SampleDto.PATHOGEN_TESTING_REQUESTED,
			SampleDto.REQUESTED_PATHOGEN_TESTS,
			SampleDto.REQUESTED_OTHER_PATHOGEN_TESTS,
			SampleDto.SHIPPED,
			SampleDto.SHIPMENT_DATE,
			SampleDto.SHIPMENT_DETAILS,
			SampleDto.RECEIVED,
			SampleDto.RECEIVED_DATE,
			SampleDto.LAB_SAMPLE_ID,
			SampleDto.SPECIMEN_CONDITION,
			SampleDto.PATHOGEN_TEST_RESULT);

		allowSampleFields(
			Disease.UNSPECIFIED_VHF,
			SampleDto.UUID,
			SampleDto.HAS_SAMPLE_BEEN_COLLECTED,
			SampleDto.SAMPLE_DATE_TIME,
			SampleDto.LAB,
			SampleDto.LAB_DETAILS,
			SampleDto.SAMPLE_MATERIAL,
			SampleDto.SAMPLE_MATERIAL_TEXT,
			SampleDto.FIELD_SAMPLE_ID,
			SampleDto.SHIPPED,
			SampleDto.SHIPMENT_DATE,
			SampleDto.SHIPMENT_DETAILS,
			SampleDto.RECEIVED,
			SampleDto.RECEIVED_DATE,
			SampleDto.LAB_SAMPLE_ID,
			SampleDto.SPECIMEN_CONDITION,
			SampleDto.PATHOGEN_TEST_RESULT);
	}

	private static void configurePathogenTestLayouts() {
		Set<String> common = setOf(
			PathogenTestDto.UUID,
			PathogenTestDto.TEST_TYPE,
			PathogenTestDto.TEST_TYPE_TEXT,
			PathogenTestDto.TEST_DATE_TIME,
			PathogenTestDto.LAB,
			PathogenTestDto.LAB_DETAILS,
			PathogenTestDto.TESTED_DISEASE,
			PathogenTestDto.TESTED_DISEASE_VARIANT,
			PathogenTestDto.TESTED_DISEASE_DETAILS,
			PathogenTestDto.TESTED_DISEASE_VARIANT_DETAILS,
			PathogenTestDto.TEST_RESULT,
			PathogenTestDto.CQ_VALUE,
			PathogenTestDto.TEST_RESULT_VERIFIED,
			PathogenTestDto.TEST_RESULT_TEXT,
			PathogenTestDto.OTHER_NOTES_AND_OBSERVATIONS);

		for (Disease disease : DiseaseSpecificCaseExportSchema.getSupportedDiseases()) {
			if (DiseaseSpecificCaseExportSchema.hasSamples(disease)) {
				PATHOGEN_TEST_LAYOUT_FIELDS.put(disease, new LinkedHashSet<>(common));
			}
		}

		allowPathogenTestFields(
			Disease.CSM,
			PathogenTestDto.SEROTYPE,
			PathogenTestDto.LABORATORY_TYPE,
			PathogenTestDto.LABORATORY_CYTOLOGY_LEUCOCYTES,
			PathogenTestDto.LABORATORY_CYTOLOGY_PMN,
			PathogenTestDto.LABORATORY_CYTOLOGY_LYMPH,
			PathogenTestDto.LABORATORY_GRAM,
			PathogenTestDto.LABORATORY_GRAM_OTHER,
			PathogenTestDto.LABORATORY_RDT_PERFORMED,
			PathogenTestDto.LABORATORY_RDT_RESULTS,
			PathogenTestDto.LABORATORY_LATEX,
			PathogenTestDto.OTHER_TEST,
			PathogenTestDto.DATE_SENT_REPORTING_HEALTH_FACILITY,
			PathogenTestDto.DATE_SAMPLE_SENT_REGREF_LAB,
			PathogenTestDto.LABORATORY_CULTURE,
			PathogenTestDto.LABORATORY_CULTURE_OTHER,
			PathogenTestDto.LABORATORY_OTHER_TESTS,
			PathogenTestDto.LABORATORY_CEFTRIAXONE,
			PathogenTestDto.LABORATORY_PENICILLIN_G,
			PathogenTestDto.LABORATORY_AMOXYCILLIN,
			PathogenTestDto.LABORATORY_OXACILLIN,
			PathogenTestDto.LABORATORY_ANTIBIOGRAM_OTHER,
			PathogenTestDto.DATE_SAMPLE_SENT_REF_LAB,
			PathogenTestDto.LABORATORY_DATE_PCR_PERFORMED,
			PathogenTestDto.LABORATORY_PCR_TYPE,
			PathogenTestDto.LABORATORY_PCR_OPTIONS,
			PathogenTestDto.LABORATORY_SEROTYPE,
			PathogenTestDto.LABORATORY_OBSERVATIONS,
			PathogenTestDto.LABORATORY_DATE_RESULTS_SENT_DSD,
			PathogenTestDto.LABORATORY_FINAL_CLASSIFICATION,
			PathogenTestDto.LABORATORY_FINAL_RESULTS);
		PATHOGEN_TEST_LAYOUT_FIELDS.get(Disease.CSM).remove(PathogenTestDto.CQ_VALUE);
		PATHOGEN_TEST_LAYOUT_FIELDS.get(Disease.CSM).remove(PathogenTestDto.TEST_RESULT_TEXT);

		allowPathogenTestFields(
			Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,
			PathogenTestDto.LAB_LOCATION,
			PathogenTestDto.DATE_LAB_RESULTS_SENT_DISTRICT,
			PathogenTestDto.DATE_LAB_RESULTS_SENT_CLINICIAN,
			PathogenTestDto.DATE_DISTRICT_RECEIVED_LAB_RESULTS);
		PATHOGEN_TEST_LAYOUT_FIELDS.get(Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS)
			.remove(PathogenTestDto.TEST_DATE_TIME);

		allowPathogenTestFields(
			Disease.MEASLES,
			PathogenTestDto.VIRUS_DETECTION_GENOTYPE,
			PathogenTestDto.LABORATORY_DATE_RESULTS_SENT_DSD,
			PathogenTestDto.DATE_SURVEILLANCE_SENT_RESULTS_TO_DISTRICT,
			PathogenTestDto.DATE_DISTRICT_RECEIVED_LAB_RESULTS,
			PathogenTestDto.FINAL_CLASSIFICATION);
		allowPathogenTestFields(
			Disease.YELLOW_FEVER,
			PathogenTestDto.DATE_LAB_RESULTS_SENT_DISTRICT,
			PathogenTestDto.DATE_DISTRICT_RECEIVED_LAB_RESULTS,
			PathogenTestDto.VIRUS_DETECTION_GENOTYPE,
			PathogenTestDto.FINAL_CLASSIFICATION);
		allowPathogenTestFields(
			Disease.CHOLERA,
			PathogenTestDto.VIBRIO_CHOLERAE_IDENTIFIED_IN_STOOLS,
			PathogenTestDto.DRUGS_SENSITIVE_TO_VIBRIO_STRAIN,
			PathogenTestDto.DRUGS_RESISTANT_TO_VIBRIO_STRAIN,
			PathogenTestDto.FINAL_CLASSIFICATION);
		allowPathogenTestFields(
			Disease.NEW_INFLUENZA,
			PathogenTestDto.TEST_RESULT_VARIANT,
			PathogenTestDto.VARIANT_OTHER_SPECIFY,
			PathogenTestDto.SECOND_TESTED_DISEASE,
			PathogenTestDto.TEST_RESULT_FOR_SECOND_DISEASE,
			PathogenTestDto.THIRD_PATHOGEN_TESTED,
			PathogenTestDto.TEST_RESULT_FOR_THIRD_PATHOGEN,
			PathogenTestDto.POSITIVE_SUBTYPES);
		allowPathogenTestFields(
			Disease.UNSPECIFIED_VHF,
			PathogenTestDto.SAMPLE_TESTS,
			PathogenTestDto.SAMPLE_TEST_RESULT_PCR,
			PathogenTestDto.SAMPLE_TEST_RESULT_PCR_DATE,
			PathogenTestDto.SAMPLE_TEST_RESULT_ANTIGEN,
			PathogenTestDto.SAMPLE_TEST_RESULT_ANTIGEN_DATE,
			PathogenTestDto.SAMPLE_TEST_RESULT_IGM,
			PathogenTestDto.SAMPLE_TEST_RESULT_IGM_DATE,
			PathogenTestDto.SAMPLE_TEST_RESULT_IGG,
			PathogenTestDto.SAMPLE_TEST_RESULT_IGG_DATE,
			PathogenTestDto.SAMPLE_TEST_RESULT_IMMUNO,
			PathogenTestDto.SAMPLE_TEST_RESULT_IMMUNO_DATE,
			PathogenTestDto.POSITIVE_SUBTYPES);
		PATHOGEN_TEST_LAYOUT_FIELDS.get(Disease.UNSPECIFIED_VHF).remove(PathogenTestDto.TEST_TYPE);
		PATHOGEN_TEST_LAYOUT_FIELDS.get(Disease.UNSPECIFIED_VHF).remove(PathogenTestDto.TEST_TYPE_TEXT);
		PATHOGEN_TEST_LAYOUT_FIELDS.get(Disease.UNSPECIFIED_VHF).remove(PathogenTestDto.CQ_VALUE);
		PATHOGEN_TEST_LAYOUT_FIELDS.get(Disease.UNSPECIFIED_VHF).remove(PathogenTestDto.TEST_RESULT_TEXT);
	}

	private static void allowSampleFields(Disease disease, String... fields) {
		SAMPLE_LAYOUT_FIELDS.put(disease, setOf(fields));
	}

	private static void allowCommonSampleFields(Disease disease, String... extraFields) {
		Set<String> fields = setOf(
			SampleDto.UUID,
			SampleDto.SAMPLE_DATE_TIME,
			SampleDto.LAB,
			SampleDto.LAB_DETAILS,
			SampleDto.FIELD_SAMPLE_ID,
			SampleDto.SAMPLE_MATERIAL,
			SampleDto.SAMPLE_MATERIAL_TEXT,
			SampleDto.SHIPPED,
			SampleDto.SHIPMENT_DATE,
			SampleDto.SHIPMENT_DETAILS,
			SampleDto.RECEIVED,
			SampleDto.RECEIVED_DATE,
			SampleDto.LAB_SAMPLE_ID,
			SampleDto.SPECIMEN_CONDITION,
			SampleDto.NO_TEST_POSSIBLE_REASON);
		fields.addAll(Arrays.asList(extraFields));
		SAMPLE_LAYOUT_FIELDS.put(disease, fields);
	}

	private static void allowCaseFields(Disease disease, String... fields) {
		CASE_LAYOUT_FIELDS.put(disease, setOf(fields));
	}

	private static String[] ghanaCaseCore(String... extraFields) {
		List<String> fields = new ArrayList<>(
			Arrays.asList(
				CaseDataDto.UUID,
				CaseDataDto.REPORT_DATE,
				CaseDataDto.REPORTING_USER,
				CaseDataDto.CASE_CLASSIFICATION,
				CaseDataDto.CLASSIFICATION_DATE,
				CaseDataDto.CLASSIFICATION_USER,
				CaseDataDto.EPID_NUMBER,
				CaseDataDto.DISEASE,
				CaseDataDto.CASE_ORIGIN,
				CaseDataDto.RESPONSIBLE_REGION,
				CaseDataDto.RESPONSIBLE_DISTRICT,
				CaseDataDto.RESPONSIBLE_COMMUNITY,
				CaseDataDto.HEALTH_FACILITY,
				CaseDataDto.HEALTH_FACILITY_DETAILS,
				CaseDataDto.VACCINATION_STATUS,
				CaseDataDto.REPORTING_OFFICER_NAME,
				CaseDataDto.REPORTING_OFFICER_TITLE,
				CaseDataDto.FUNCTION_OF_REPORTING_OFFICER,
				CaseDataDto.REPORTING_OFFICER_CONTACT_PHONE,
				CaseDataDto.REPORTING_OFFICER_EMAIL));
		fields.addAll(Arrays.asList(extraFields));
		return fields.toArray(new String[0]);
	}

	private static String[] personCore(String... extraFields) {
		List<String> fields = new ArrayList<>(
			Arrays.asList(
				PersonDto.UUID,
				PersonDto.FIRST_NAME,
				PersonDto.LAST_NAME,
				PersonDto.OTHER_NAME,
				PersonDto.BIRTH_DATE_YYYY,
				PersonDto.BIRTH_DATE_MM,
				PersonDto.BIRTH_DATE_DD,
				PersonDto.APPROXIMATE_AGE,
				PersonDto.APPROXIMATE_AGE_TYPE,
				PersonDto.APPROXIMATE_AGE_REFERENCE_DATE,
				PersonDto.SEX));
		fields.addAll(Arrays.asList(extraFields));
		return fields.toArray(new String[0]);
	}

	private static void configurePersonLayouts() {
		allowEntityFields(
			PersonDto.class,
			Disease.GUINEA_WORM,
			PersonDto.UUID,
			PersonDto.FIRST_NAME,
			PersonDto.LAST_NAME,
			PersonDto.OTHER_NAME,
			PersonDto.FATHERS_NAME,
			PersonDto.APPROXIMATE_AGE,
			PersonDto.APPROXIMATE_AGE_TYPE,
			PersonDto.SEX,
			PersonDto.PASSPORT_NUMBER,
			PersonDto.OCCUPATION_DETAILS,
			PersonDto.ETHNICITY,
			PersonDto.PLACE_OF_RESIDENCE_SAME_AS_REPORTING_VILLAGE,
			PersonDto.RESIDENCE_SINCE_WHEN_IN_MONTHS,
			PersonDto.PLACE_STAYED_TEN_TO_FOURTEEN_MONTHS_REGION,
			PersonDto.PLACE_STAYED_TEN_TO_FOURTEEN_MONTHS_DISTRICT,
			PersonDto.PLACE_STAYED_TEN_TO_FOURTEEN_MONTHS_COMMUNITY,
			PersonDto.PLACE_STAYED_TEN_TO_FOURTEEN_MONTHS_ZONE,
			PersonDto.PLACE_STAYED_TEN_TO_FOURTEEN_MONTHS_VILLAGE);

		allowEntityFields(
			PersonDto.class,
			Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,
			personCore(
				PersonDto.TEL_NUMBER,
				PersonDto.PASSPORT_NUMBER,
				PersonDto.APPLICABLE,
				PersonDto.MOTHERS_NAME,
				PersonDto.FATHERS_NAME));

		allowEntityFields(
			PersonDto.class,
			Disease.CSM,
			personCore(
				PersonDto.MARRIAGE_STATUS,
				PersonDto.PRESENT_CONDITION,
				PersonDto.OCCUPATION_DETAILS,
				PersonDto.MOTHERS_NAME,
				PersonDto.FATHERS_NAME,
				PersonDto.PERSON_CONTACT_DETAILS));

		allowEntityFields(
			PersonDto.class,
			Disease.YELLOW_FEVER,
			personCore(
				PersonDto.MARRIAGE_STATUS,
				PersonDto.NATIONAL_HEALTH_ID,
				PersonDto.GHANA_CARD,
				PersonDto.EDUCATION_TYPE,
				PersonDto.EDUCATION_DETAILS,
				PersonDto.OCCUPATION_DETAILS,
				PersonDto.MOTHERS_NAME,
				PersonDto.FATHERS_NAME,
				PersonDto.PERSON_CONTACT_DETAILS));

		allowEntityFields(
			PersonDto.class,
			Disease.MONKEYPOX,
			personCore(
				PersonDto.PASSPORT_NUMBER,
				PersonDto.NATIONALITY,
				PersonDto.ETHNICITY,
				PersonDto.OCCUPATION_DETAILS));

		allowEntityFields(
			PersonDto.class,
			Disease.MEASLES,
			personCore(PersonDto.MARRIAGE_STATUS, PersonDto.PERSON_CONTACT_DETAILS));

		allowEntityFields(
			PersonDto.class,
			Disease.CHOLERA,
			personCore(PersonDto.MARRIAGE_STATUS, PersonDto.PRESENT_CONDITION, PersonDto.PERSON_CONTACT_DETAILS));

		allowEntityFields(
			PersonDto.class,
			Disease.CORONAVIRUS,
			personCore(PersonDto.PRESENT_CONDITION, PersonDto.PERSON_CONTACT_DETAILS, PersonDto.OCCUPATION_TYPE, PersonDto.OCCUPATION_DETAILS));

		allowEntityFields(
			PersonDto.class,
			Disease.NEW_INFLUENZA,
			personCore(
				PersonDto.OCCUPATION_DETAILS,
				PersonDto.PERSON_CONTACT_DETAILS,
				PersonDto.INVESTIGATOR_NAME,
				PersonDto.INVESTIGATOR_TITLE,
				PersonDto.INVESTIGATOR_UNIT,
				PersonDto.INVESTIGATOR_ADDRESS,
				PersonDto.INVESTIGATOR_TEL));

		allowEntityFields(
			PersonDto.class,
			Disease.AFP,
			personCore(
				PersonDto.HOME_ADDRESS_RECREATIONAL,
				PersonDto.PLACE,
				PersonDto.DURATION_MONTHS,
				PersonDto.DURATION_DAYS,
				PersonDto.PLACE2,
				PersonDto.DURATION_MONTHS2,
				PersonDto.DURATION_DAYS2,
				PersonDto.PLACE3,
				PersonDto.DURATION_MONTHS3,
				PersonDto.DURATION_DAYS3,
				PersonDto.PLACE4,
				PersonDto.DURATION_MONTHS4,
				PersonDto.DURATION_DAYS4,
				PersonDto.INVESTIGATOR_NAME,
				PersonDto.INVESTIGATOR_TITLE,
				PersonDto.INVESTIGATOR_UNIT,
				PersonDto.INVESTIGATOR_ADDRESS,
				PersonDto.INVESTIGATOR_TEL));

		allowEntityFields(
			PersonDto.class,
			Disease.FOODBORNE_ILLNESS,
			personCore(PersonDto.TEL_NUMBER, PersonDto.OCCUPATION_DETAILS));

		allowEntityFields(
			PersonDto.class,
			Disease.UNSPECIFIED_VHF,
			personCore(
				PersonDto.HEAD_HOUSEHOLD,
				PersonDto.ETHNICITY,
				PersonDto.NATIONALITY,
				PersonDto.PROFESSION_OF_PATIENT,
				PersonDto.PROFESSION_OF_PATIENT_OTHER,
				PersonDto.NAME_HEALTH_FACILITY,
				PersonDto.SERVICE,
				PersonDto.QUALIFICATION));

		allowEntityFields(
			PersonDto.class,
			Disease.NEONATAL_TETANUS,
			personCore(
				PersonDto.LOCATION_OF_BIRTH,
				PersonDto.BIRTH_IN_INSTITUTION,
				PersonDto.PLACE_OF_BIRTH_REGION,
				PersonDto.PLACE_OF_BIRTH_DISTRICT,
				PersonDto.PLACE_OF_BIRTH_COMMUNITY,
				PersonDto.PLACE_OF_BIRTH_FACILITY,
				PersonDto.PLACE_OF_BIRTH_FACILITY_DETAILS,
				PersonDto.RECEIVED_ANTENATAL_CARE,
				PersonDto.PRENATAL_TOTAL_VISITS,
				PersonDto.ATTENDED_BY_TRAINED_TBA,
				PersonDto.ATTENDED_BY_TRAINED_TBA_MIDWIFE_NAME,
				PersonDto.ATTENDED_BY_DOCTOR_NURSE,
				PersonDto.CUT_CORD_WITH_STERILE_BLADE,
				PersonDto.CORD_TREATED_WITH_ANYTHING,
				PersonDto.CORD_TREATED_WITH_ANYTHING_WHERE));
	}

	private static void configureLocationLayouts() {
		allowEntityFields(
			LocationDto.class,
			Disease.GUINEA_WORM,
			LocationDto.UUID,
			LocationDto.VILLAGE,
			LocationDto.ZONE,
			LocationDto.REGION,
			LocationDto.DISTRICT,
			LocationDto.COMMUNITY,
			LocationDto.AREA_TYPE,
			LocationDto.LAND_MARK);

		allowEntityFields(
			LocationDto.class,
			Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,
			LocationDto.UUID,
			LocationDto.REGION,
			LocationDto.DISTRICT,
			LocationDto.COMMUNITY,
			LocationDto.HOUSE_NUMBER,
			LocationDto.AREA_TYPE);

		allowEntityFields(
			LocationDto.class,
			Disease.CSM,
			LocationDto.UUID,
			LocationDto.REGION,
			LocationDto.DISTRICT,
			LocationDto.COMMUNITY,
			LocationDto.HOUSE_NUMBER,
			LocationDto.POSTAL_CODE,
			LocationDto.LAND_MARK,
			LocationDto.LATITUDE,
			LocationDto.LONGITUDE,
			LocationDto.LAT_LON_ACCURACY);

		allowEntityFields(
			LocationDto.class,
			Disease.YELLOW_FEVER,
			LocationDto.UUID,
			LocationDto.REGION,
			LocationDto.DISTRICT,
			LocationDto.COMMUNITY,
			LocationDto.HOUSE_NUMBER,
			LocationDto.CITY,
			LocationDto.AREA_TYPE,
			LocationDto.POSTAL_CODE,
			LocationDto.LAND_MARK,
			LocationDto.LATITUDE,
			LocationDto.LONGITUDE);

		allowEntityFields(
			LocationDto.class,
			Disease.MONKEYPOX,
			LocationDto.UUID,
			LocationDto.REGION,
			LocationDto.DISTRICT,
			LocationDto.LOCALITY,
			LocationDto.CITY,
			LocationDto.LATITUDE,
			LocationDto.LONGITUDE);

		allowEntityFields(
			LocationDto.class,
			Disease.MEASLES,
			LocationDto.UUID,
			LocationDto.REGION,
			LocationDto.DISTRICT,
			LocationDto.COMMUNITY,
			LocationDto.CITY,
			LocationDto.AREA_TYPE,
			LocationDto.POSTAL_CODE,
			LocationDto.LAND_MARK,
			LocationDto.LATITUDE,
			LocationDto.LONGITUDE,
			LocationDto.LAT_LON_ACCURACY);

		allowEntityFields(
			LocationDto.class,
			Disease.CHOLERA,
			LocationDto.UUID,
			LocationDto.REGION,
			LocationDto.DISTRICT,
			LocationDto.COMMUNITY,
			LocationDto.POSTAL_CODE,
			LocationDto.VILLAGE,
			LocationDto.LATITUDE,
			LocationDto.LONGITUDE);

		allowEntityFields(
			LocationDto.class,
			Disease.CORONAVIRUS,
			LocationDto.UUID,
			LocationDto.REGION,
			LocationDto.DISTRICT,
			LocationDto.COMMUNITY,
			LocationDto.POSTAL_CODE,
			LocationDto.LATITUDE,
			LocationDto.LONGITUDE);

		allowEntityFields(
			LocationDto.class,
			Disease.NEW_INFLUENZA,
			LocationDto.UUID,
			LocationDto.LOCALITY,
			LocationDto.CITY,
			LocationDto.HOUSE_NUMBER,
			LocationDto.ADDITIONAL_INFORMATION,
			LocationDto.LATITUDE,
			LocationDto.LONGITUDE);

		allowEntityFields(
			LocationDto.class,
			Disease.AFP,
			LocationDto.UUID,
			LocationDto.REGION,
			LocationDto.DISTRICT,
			LocationDto.COMMUNITY,
			LocationDto.ADDITIONAL_INFORMATION,
			LocationDto.HOUSE_NUMBER,
			LocationDto.CITY,
			LocationDto.AREA_TYPE,
			LocationDto.POSTAL_CODE,
			LocationDto.LATITUDE,
			LocationDto.LONGITUDE);

		allowEntityFields(
			LocationDto.class,
			Disease.FOODBORNE_ILLNESS,
			LocationDto.UUID,
			LocationDto.LOCALITY,
			LocationDto.HOUSE_NUMBER,
			LocationDto.CITY,
			LocationDto.AREA_TYPE,
			LocationDto.LATITUDE,
			LocationDto.LONGITUDE);

		allowEntityFields(
			LocationDto.class,
			Disease.UNSPECIFIED_VHF,
			LocationDto.UUID,
			LocationDto.STREET,
			LocationDto.HOUSE_NUMBER,
			LocationDto.CITY,
			LocationDto.LATITUDE,
			LocationDto.LONGITUDE);

		allowEntityFields(
			LocationDto.class,
			Disease.NEONATAL_TETANUS,
			LocationDto.UUID,
			LocationDto.REGION,
			LocationDto.DISTRICT,
			LocationDto.COMMUNITY,
			LocationDto.POSTAL_CODE,
			LocationDto.NEAREST_HEALTH_FACILITY_TO_VILLAGE);
	}

	private static void configureHospitalizationLayouts() {
		allowEntityFields(
			HospitalizationDto.class,
			Disease.GUINEA_WORM,
			HospitalizationDto.UUID,
			HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY,
			HospitalizationDto.HOSPITAL_RECORD_NUMBER,
			HospitalizationDto.ADMISSION_DATE,
			HospitalizationDto.DISCHARGE_DATE);

		allowEntityFields(
			HospitalizationDto.class,
			Disease.MEASLES,
			HospitalizationDto.UUID,
			HospitalizationDto.SELECT_INPATIENT_OUTPATIENT,
			HospitalizationDto.SEEN_AT_A_HEALTH_FACILITY,
			HospitalizationDto.ADMISSION_DATE,
			HospitalizationDto.DATE_FIRST_SEEN_HOSPITAL_FOR_DISEASE,
			HospitalizationDto.DISCHARGE_DATE);

		allowEntityFields(
			HospitalizationDto.class,
			Disease.MONKEYPOX,
			HospitalizationDto.UUID,
			HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY_NEW,
			HospitalizationDto.LOCATION_TYPE,
			HospitalizationDto.SOUGHT_REGION,
			HospitalizationDto.SOUGHT_DISTRICT,
			HospitalizationDto.SOUGHT_COMMUNITY,
			HospitalizationDto.NAME_OF_FACILITY,
			HospitalizationDto.HOSPITAL_RECORD_NUMBER,
			HospitalizationDto.ADMISSION_DATE);

		allowEntityFields(
			HospitalizationDto.class,
			Disease.CHOLERA,
			HospitalizationDto.UUID,
			HospitalizationDto.HOSPITAL_RECORD_NUMBER);

		allowEntityFields(
			HospitalizationDto.class,
			Disease.YELLOW_FEVER,
			HospitalizationDto.UUID,
			HospitalizationDto.SELECT_INPATIENT_OUTPATIENT,
			HospitalizationDto.DATE_FIRST_SEEN_HOSPITAL_FOR_DISEASE,
			HospitalizationDto.ADMISSION_DATE,
			HospitalizationDto.DISCHARGE_DATE);

		allowEntityFields(
			HospitalizationDto.class,
			Disease.CSM,
			HospitalizationDto.UUID,
			HospitalizationDto.HOSPITAL_RECORD_NUMBER,
			HospitalizationDto.DATE_FIRST_SEEN_HOSPITAL_FOR_DISEASE,
			HospitalizationDto.DISEASE_ONSET_DATE,
			HospitalizationDto.PATIENT_HOSPITALIZED_DETAINED,
			HospitalizationDto.ADMISSION_DATE);

		allowEntityFields(
			HospitalizationDto.class,
			Disease.AFP,
			HospitalizationDto.UUID,
			HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY_NEW,
			HospitalizationDto.HOSPITAL_RECORD_NUMBER,
			HospitalizationDto.DATE_FIRST_SEEN_HOSPITAL_FOR_DISEASE,
			HospitalizationDto.ADMISSION_DATE);

		allowEntityFields(
			HospitalizationDto.class,
			Disease.UNSPECIFIED_VHF,
			HospitalizationDto.UUID,
			HospitalizationDto.HOSPITAL_RECORD_NUMBER,
			HospitalizationDto.ISOLATED,
			HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY_NEW,
			HospitalizationDto.DATE_OF_DEATH,
			HospitalizationDto.MEMBER_FAMILY_HELPING_PATIENT,
			HospitalizationDto.ADMISSION_DATE,
			HospitalizationDto.DISCHARGE_DATE);

		allowEntityFields(
			HospitalizationDto.class,
			Disease.NEW_INFLUENZA,
			HospitalizationDto.UUID,
			HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY_NEW,
			HospitalizationDto.ADMISSION_DATE,
			HospitalizationDto.DISCHARGE_DATE,
			HospitalizationDto.TERMINATION_DATE_HOSPITAL_STAY,
			HospitalizationDto.INTENSIVE_CARE_UNIT);

		allowEntityFields(
			HospitalizationDto.class,
			Disease.CORONAVIRUS,
			HospitalizationDto.UUID,
			HospitalizationDto.HOSPITAL_RECORD_NUMBER,
			HospitalizationDto.PATIENT_VENTILATED,
			HospitalizationDto.INTENSIVE_CARE_UNIT,
			HospitalizationDto.HOSPITALIZED_PREVIOUSLY,
			HospitalizationDto.PREVIOUS_HOSPITALIZATIONS,
			HospitalizationDto.ISOLATED,
			HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY,
			HospitalizationDto.ADMISSION_DATE,
			HospitalizationDto.DISCHARGE_DATE,
			HospitalizationDto.LEFT_AGAINST_ADVICE);

		allowEntityFields(
			HospitalizationDto.class,
			Disease.NEONATAL_TETANUS,
			HospitalizationDto.UUID,
			HospitalizationDto.HOSPITAL_RECORD_NUMBER,
			HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY,
			HospitalizationDto.ADMISSION_DATE);

		allowEntityFields(
			HospitalizationDto.class,
			Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,
			HospitalizationDto.UUID,
			HospitalizationDto.SELECT_INPATIENT_OUTPATIENT,
			HospitalizationDto.DATE_FIRST_SEEN_HOSPITAL_FOR_DISEASE,
			HospitalizationDto.ADMISSION_DATE,
			HospitalizationDto.DISCHARGE_DATE);

		allowEntityFields(
			HospitalizationDto.class,
			Disease.FOODBORNE_ILLNESS,
			HospitalizationDto.UUID,
			HospitalizationDto.SYMPTOMS_SELECTED,
			HospitalizationDto.OTHER_SYMPTOM_SELECTED,
			HospitalizationDto.ONSET_OF_SYMPTOM_DATETIME,
			HospitalizationDto.SYMPTOMS_ONGOING,
			HospitalizationDto.DURATION_HOURS,
			HospitalizationDto.SOUGHT_MEDICAL_ATTENTION,
			HospitalizationDto.LOCATION_TYPE,
			HospitalizationDto.SOUGHT_REGION,
			HospitalizationDto.SOUGHT_DISTRICT,
			HospitalizationDto.SOUGHT_COMMUNITY,
			HospitalizationDto.NAME_OF_FACILITY,
			HospitalizationDto.DATE_OF_VISIT_HOSPITAL,
			HospitalizationDto.HOSPITALIZATION_YES_NO,
			HospitalizationDto.PHYSICIAN_NAME,
			HospitalizationDto.PHYSICIAN_NUMBER,
			HospitalizationDto.LAB_TEST_CONDUCTED,
			HospitalizationDto.TYPE_OF_SAMPLE,
			HospitalizationDto.AGENT_IDENTIFIED);
	}

	private static void configureSymptomsLayouts() {
		allowEntityFields(
			SymptomsDto.class,
			Disease.MEASLES,
			SymptomsDto.UUID,
			SymptomsDto.FEVER,
			SymptomsDto.GENERALIZED_RASH,
			SymptomsDto.COUGH,
			SymptomsDto.RUNNY_NOSE,
			SymptomsDto.RED_EYES,
			SymptomsDto.SWOLLEN_LYMPH_NODES_BEHIND_EARS,
			SymptomsDto.JOINT_PAIN,
			SymptomsDto.LESIONS_ONSET_DATE,
			SymptomsDto.OTHER_COMPLICATIONS,
			SymptomsDto.OTHER_COMPLICATIONS_TEXT,
			SymptomsDto.OUTCOME);

		allowEntityFields(
			SymptomsDto.class,
			Disease.CHOLERA,
			SymptomsDto.UUID,
			SymptomsDto.DIARRHEA,
			SymptomsDto.VOMITING,
			SymptomsDto.DEHYDRATION,
			SymptomsDto.ABDOMINAL_PAIN,
			SymptomsDto.ABDOMINAL_CRAMPS,
			SymptomsDto.FEVER,
			SymptomsDto.HEADACHE,
			SymptomsDto.FATIGUE_WEAKNESS,
			SymptomsDto.ONSET_SYMPTOM,
			SymptomsDto.ONSET_DATE,
			SymptomsDto.OUTCOME);

		allowEntityFields(
			SymptomsDto.class,
			Disease.GUINEA_WORM,
			SymptomsDto.UUID,
			SymptomsDto.FIRST_SIGN_OR_SYMPTOMS_BEFORE_WORM,
			SymptomsDto.FIRST_SIGN_OR_SYMPTOMS_BEFORE_WORM_OTHERS,
			SymptomsDto.EMERGENCE_OF_GUINEA_WORM,
			SymptomsDto.NUMBER_OF_WORMS,
			SymptomsDto.FIRST_WORM_THIS_YEAR,
			SymptomsDto.DATE_FIRST_WORM_EMERGENCE,
			SymptomsDto.CASE_DETECTED_BEFORE_WORM_EMERGENCE);

		allowEntityFields(
			SymptomsDto.class,
			Disease.NEONATAL_TETANUS,
			SymptomsDto.UUID,
			SymptomsDto.SEIZURES,
			SymptomsDto.OTHER_COMPLICATIONS,
			SymptomsDto.BABY_DIED,
			SymptomsDto.OTHER_COMPLICATIONS_TEXT,
			SymptomsDto.AGE_AT_DEATH_DAYS,
			SymptomsDto.AGE_AT_ONSET_DAYS,
			SymptomsDto.BABY_NORMAL_AT_BIRTH,
			SymptomsDto.NORMAL_CRY_AND_SUCK,
			SymptomsDto.STOPPED_SUCKING_AFTER_TWO_DAYS,
			SymptomsDto.BACKACHE,
			SymptomsDto.STIFFNESS,
			SymptomsDto.SYMPTOMS_COMMENTS,
			SymptomsDto.ONSET_SYMPTOM,
			SymptomsDto.ONSET_DATE,
			SymptomsDto.OUTCOME);

		allowEntityFields(
			SymptomsDto.class,
			Disease.AFP,
			SymptomsDto.UUID,
			SymptomsDto.FEVER_ONSET_PARALYSIS,
			SymptomsDto.PROGRESSIVE_PARALYSIS,
			SymptomsDto.PROGRESSIVE_FLACID_ACUTE,
			SymptomsDto.ASSYMETRIC,
			SymptomsDto.DATE_ONSET_PARALYSIS,
			SymptomsDto.SITE_OF_PARALYSIS,
			SymptomsDto.PARALYSED_LIMB_SENSITIVE_TO_PAIN,
			SymptomsDto.INJECTION_SITE_BEFORE_ONSET_PARALYSIS,
			SymptomsDto.INJECTION_SITE,
			SymptomsDto.PROVISONAL_DIAGNOSIS,
			SymptomsDto.TRUEAFP);

		allowEntityFields(
			SymptomsDto.class,
			Disease.MONKEYPOX,
			SymptomsDto.UUID,
			SymptomsDto.SYMPTOMS_SELECTED,
			SymptomsDto.SYMPTOMS_SELECTED_OTHER,
			SymptomsDto.ONSET_DATE,
			SymptomsDto.DATE_OF_ONSET_RASH,
			SymptomsDto.RASH_SYMPTOMS,
			SymptomsDto.RASH_SYMPTOMS_OTHER_AREAS,
			SymptomsDto.ARE_LESIONS_IN_SAME_STATE,
			SymptomsDto.ARE_LESIONS_SAME_SIZE,
			SymptomsDto.ARE_LESIONS_DEEP,
			SymptomsDto.ARE_ULCERS_AMONG_LESIONS,
			SymptomsDto.TYPE_OF_RASH,
			SymptomsDto.OUTCOME,
			SymptomsDto.DATE_OF_DEATH,
			SymptomsDto.PLACE_OF_DEATH);

		allowDefaultSymptomLayout(Disease.YELLOW_FEVER, SymptomsDto.TEMPERATURE, SymptomsDto.TEMPERATURE_SOURCE);
		allowDefaultSymptomLayout(Disease.CSM);
		allowDefaultSymptomLayout(Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS);
		allowDefaultSymptomLayout(
			Disease.CORONAVIRUS,
			SymptomsDto.TEMPERATURE,
			SymptomsDto.TEMPERATURE_SOURCE,
			SymptomsDto.PREGNANT,
			SymptomsDto.POSTPARTUM,
			SymptomsDto.DIABETES,
			SymptomsDto.HIV,
			SymptomsDto.HIV_ART,
			SymptomsDto.CHRONIC_LIVER_DISEASE,
			SymptomsDto.CHRONIC_KIDNEY_DISEASE,
			SymptomsDto.CARDIOVASCULAR_DISEASE_INCLUDING_HYPERTENSION,
			SymptomsDto.LUNG_DISEASE,
			SymptomsDto.STROKE,
			SymptomsDto.CANCER);
		allowDefaultSymptomLayout(
			Disease.NEW_INFLUENZA,
			SymptomsDto.FEVER_BODY_TEMP_GREATER,
			SymptomsDto.TEMPERATURE,
			SymptomsDto.TEMPERATURE_SOURCE,
			SymptomsDto.DATE_OF_ONSET,
			SymptomsDto.OUTCOME_OTHER);
		allowDefaultSymptomLayout(
			Disease.UNSPECIFIED_VHF,
			SymptomsDto.PATIENT_HAVE_FEVER,
			SymptomsDto.DATE_OF_ONSET,
			SymptomsDto.OUTCOME_DATE,
			SymptomsDto.OUTCOME_PLACE_COMM_VILLAGE,
			SymptomsDto.HOSPITAL_NAME_SERVICE,
			SymptomsDto.PLACE_OF_FUNERAL_NAME_VILLAGE);
	}

	private static void allowDefaultSymptomLayout(Disease disease, String... extraFields) {
		Set<String> fields = new LinkedHashSet<>();
		fields.add(EntityDto.UUID);
		fields.add(SymptomsDto.ONSET_DATE);
		fields.add(SymptomsDto.ONSET_SYMPTOM);
		fields.add(SymptomsDto.OUTCOME);
		fields.add(SymptomsDto.SYMPTOMS_COMMENTS);
		for (Field field : SymptomsDto.class.getDeclaredFields()) {
			SymptomGrouping grouping = field.getAnnotation(SymptomGrouping.class);
			if (grouping == null) {
				continue;
			}
			SymptomGroup group = grouping.value();
			if (group == SymptomGroup.GENERAL
				|| group == SymptomGroup.RESPIRATORY
				|| group == SymptomGroup.GASTROINTESTINAL
				|| group == SymptomGroup.OTHER) {
				fields.add(field.getName());
			}
		}
		fields.addAll(Arrays.asList(extraFields));
		allowEntityFields(SymptomsDto.class, disease, fields.toArray(new String[0]));
	}

	private static void configureEpiDataLayouts() {
		allowEntityFields(
			EpiDataDto.class,
			Disease.MEASLES,
			EpiDataDto.UUID,
			EpiDataDto.HISTORY_OF_TRAVEL_OUTSIDE_THE_VILLAGE_TOWN_DISTRICT,
			EpiDataDto.HISTORY_OF_TRAVEL_REGION,
			EpiDataDto.HISTORY_OF_TRAVEL_DISTRICT,
			EpiDataDto.HISTORY_OF_TRAVEL_SUB_DISTRICT,
			EpiDataDto.HISTORY_OF_TRAVEL_VILLAGE);

		allowEntityFields(
			EpiDataDto.class,
			Disease.CORONAVIRUS,
			EpiDataDto.UUID,
			EpiDataDto.PATIENT_TRAVELLED_TWO_WEEKS_PRIOR,
			EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_ONE,
			EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_TWO,
			EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_THREE,
			EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_FOUR,
			EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_ONE,
			EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_TWO,
			EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_THREE,
			EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_FOUR,
			EpiDataDto.PATIENT_VISITED_HEALTH_CARE_FACILITY,
			EpiDataDto.PATIENT_CLOSE_CONTACT_WITH_ARI,
			EpiDataDto.PATIENT_CLOSE_CONTACT_WITH_ARI_CONTACT_SETTINGS,
			EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE,
			EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE_EXPOSURE_LOCATIONS,
			EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE_EXPOSURE_LOCATION_CITY_COUNTRY);

		allowEntityFields(
			EpiDataDto.class,
			Disease.CHOLERA,
			EpiDataDto.UUID,
			EpiDataDto.EXPOSED_TO_RISK_FACTOR,
			EpiDataDto.WATER_USED_FOR_DRINKING,
			EpiDataDto.WATER_USED_NOT_FOR_DRINKING,
			EpiDataDto.FOOD_ITEMS,
			EpiDataDto.WATER_USED_BY_PATIENT_AFTER_EXPOSURE);

		allowEntityFields(
			EpiDataDto.class,
			Disease.GUINEA_WORM,
			EpiDataDto.UUID,
			EpiDataDto.PERSON_TRAVEL_HISTORY,
			EpiDataDto.CONTAMINATION_SOURCES,
			EpiDataDto.CONTAINMENT_MEASURES,
			EpiDataDto.RECEIVED_HEALTH_EDUCATION,
			EpiDataDto.PATIENT_ENTERED_WATER_SOURCE,
			EpiDataDto.PLACE_MANAGED);

		allowEntityFields(
			EpiDataDto.class,
			Disease.NEW_INFLUENZA,
			EpiDataDto.UUID,
			EpiDataDto.PREVIOUSLY_VACCINATED_AGAINST_INFLUENZA,
			EpiDataDto.NAME_OF_VACCINE,
			EpiDataDto.YEAR_OF_VACCINATION,
			EpiDataDto.PREVIOUSLY_VACCINATED_AGAINST_COVID,
			EpiDataDto.NAME_OF_VACCINE_FOR_COVID,
			EpiDataDto.YEAR_OF_VACCINATION_FOR_COVID,
			EpiDataDto.VISITED_PLACES_CONFIRMED_PANDEMIC,
			EpiDataDto.PLACES_VISITED_PAST_7DAYS,
			EpiDataDto.RISK_FACTORS_SEVERE_DISEASE,
			EpiDataDto.OTHER_SPECIFY,
			EpiDataDto.CONTACT_SICK_ANIMALS,
			EpiDataDto.IF_YES_SPECIFY_SICK_ANIMAL,
			EpiDataDto.CONTACT_DEAD_WILD_ANIMALS,
			EpiDataDto.IF_YES_SPECIFY_DEAD_WILD_ANIMAL,
			EpiDataDto.CONTACT_SIMILAR_SYMPTOMS,
			EpiDataDto.RECENT_TRAVEL_OUTBREAK);

		allowEntityFields(
			EpiDataDto.class,
			Disease.YELLOW_FEVER,
			EpiDataDto.UUID,
			EpiDataDto.ACTIVITY_AS_CASE_DETAILS_KNOWN,
			EpiDataDto.ACTIVITIES_AS_CASE);

		allowEntityFields(
			EpiDataDto.class,
			Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,
			EpiDataDto.UUID,
			EpiDataDto.CONTACT_SICK_ANIMALS,
			EpiDataDto.CONTACT_SIMILAR_SYMPTOMS,
			EpiDataDto.RECENT_TRAVEL_OUTBREAK);

		allowEntityFields(
			EpiDataDto.class,
			Disease.CSM,
			EpiDataDto.UUID,
			EpiDataDto.CONTACT_SIMILAR_SYMPTOMS,
			EpiDataDto.EXPOSURE_DETAILS_KNOWN,
			EpiDataDto.EXPOSURES);

		allowEntityFields(
			EpiDataDto.class,
			Disease.AFP,
			EpiDataDto.UUID,
			EpiDataDto.CONTACT_SIMILAR_SYMPTOMS,
			EpiDataDto.RECENT_TRAVEL_OUTBREAK);

		allowEntityFields(
			EpiDataDto.class,
			Disease.FOODBORNE_ILLNESS,
			EpiDataDto.UUID,
			EpiDataDto.INTL_TRAVEL,
			EpiDataDto.SPECIFY_COUNTRIES,
			EpiDataDto.DATE_OF_DEPARTURE,
			EpiDataDto.DATE_OF_ARRIVAL,
			EpiDataDto.DOMESTIC_TRAVEL,
			EpiDataDto.SPECIFY_LOCATION,
			EpiDataDto.DATE_OF_DEPARTURE2,
			EpiDataDto.DATE_OF_ARRIVAL2,
			EpiDataDto.CONTACT_ILL_PERSON,
			EpiDataDto.CONTACT_DATE,
			EpiDataDto.SPECIFY_ILLNESS);

		allowEntityFields(
			EpiDataDto.class,
			Disease.UNSPECIFIED_VHF,
			EpiDataDto.UUID,
			EpiDataDto.PATIENT_TRAVEL_DURING_ILLNESS,
			EpiDataDto.COMM1,
			EpiDataDto.HEALTH_CENTER1,
			EpiDataDto.COUNTRY1,
			EpiDataDto.COMM2,
			EpiDataDto.HEALTH_CENTER2,
			EpiDataDto.COUNTRY2,
			EpiDataDto.WAS_PATIENT_HOSPITALIZED,
			EpiDataDto.IF_YES_WHERE,
			EpiDataDto.HOSPITALIZED_DATE1,
			EpiDataDto.HOSPITALIZED_DATE2,
			EpiDataDto.DID_PATIENT_CONSULT_HEALER,
			EpiDataDto.IF_YES_NAME_HEALER,
			EpiDataDto.COMMUNITY,
			EpiDataDto.COUNTRY,
			EpiDataDto.WHEN_WHERE_CONTACT_TAKE_PLACE,
			EpiDataDto.DATE_OF_CONTACT,
			EpiDataDto.PATIENT_RECEIVE_TRADITIONAL_MEDICINE,
			EpiDataDto.IF_YES_EXPLAIN,
			EpiDataDto.PATIENT_ATTEND_FUNERAL_CEREMONIES,
			EpiDataDto.PATIENT_TRAVEL_ANYTIME_PERIOD_BEFORE_ILL,
			EpiDataDto.IF_TRAVEL_YES_WHERE,
			EpiDataDto.IF_TRAVEL_START_DATE,
			EpiDataDto.IF_YES_END_DATE,
			EpiDataDto.PATIENT_CONTACT_KNOWN_SUSPECT,
			EpiDataDto.SUSPECT_LAST_NAME,
			EpiDataDto.SUSPECT_NAME,
			EpiDataDto.ID_CASE,
			EpiDataDto.DURING_CONTACT_SUSPECT_CASE,
			EpiDataDto.DATE_OF_DEATH,
			EpiDataDto.DATE_OF_LAST_CONTACT_WITH_SUSPECT_CASE,
			EpiDataDto.CONTACT_SICK_ANIMALS,
			EpiDataDto.IF_YES_SPECIFY_SICK_ANIMAL,
			EpiDataDto.CONTACT_DEAD_WILD_ANIMALS,
			EpiDataDto.IF_YES_SPECIFY_DEAD_WILD_ANIMAL,
			EpiDataDto.IF_YES_WILD_ANIMAL_LOCATION,
			EpiDataDto.IF_YES_WILD_ANIMAL_DATE,
			EpiDataDto.CONTACT_SIMILAR_SYMPTOMS,
			EpiDataDto.RECENT_TRAVEL_OUTBREAK);
	}

	private static void configureRiskFactorAndAfpLayouts() {
		allowEntityFields(
			RiskFactorDto.class,
			Disease.CHOLERA,
			RiskFactorDto.UUID,
			RiskFactorDto.WATER_USED_FOR_DRINKING,
			RiskFactorDto.DRINKING_WATER_SOURCE_ONE,
			RiskFactorDto.DRINKING_WATER_SOURCE_TWO,
			RiskFactorDto.DRINKING_WATER_SOURCE_THREE,
			RiskFactorDto.DRINKING_WATER_SOURCE_FOUR,
			RiskFactorDto.NON_DRINKING_WATER_SOURCE_ONE,
			RiskFactorDto.NON_DRINKING_WATER_SOURCE_TWO,
			RiskFactorDto.NON_DRINKING_WATER_SOURCE_THREE,
			RiskFactorDto.NON_DRINKING_WATER_SOURCE_FOUR,
			RiskFactorDto.FOOD_ITEMS_ONE,
			RiskFactorDto.FOOD_ITEMS_TWO,
			RiskFactorDto.FOOD_ITEMS_THREE,
			RiskFactorDto.FOOD_ITEMS_FOUR,
			RiskFactorDto.FOOD_ITEMS_FIVE,
			RiskFactorDto.FOOD_ITEMS_SIX,
			RiskFactorDto.FOOD_ITEMS_SEVEN,
			RiskFactorDto.FOOD_ITEMS_EIGHT,
			RiskFactorDto.DRINKING_WATER_INFECTED_BY_VIBRIO,
			RiskFactorDto.NON_DRINKING_WATER_INFECTED_BY_VIBRIO,
			RiskFactorDto.FOOD_ITEMS_INFECTED_BY_VIBRIO,
			RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_ONE,
			RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_TWO,
			RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_THREE,
			RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_FOUR,
			RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_FIVE,
			RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_ONE,
			RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_TWO,
			RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_THREE,
			RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_FOUR,
			RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_FIVE,
			RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_ATTEND_ANY_FUNERAL,
			RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_ATTEND_ANY_SOCIAL_EVENT,
			RiskFactorDto.OTHER_SOCIAL_EVENT_DETAILS);

		allowEntityFields(
			RiskFactorDto.class,
			Disease.MONKEYPOX,
			RiskFactorDto.UUID,
			RiskFactorDto.PATIENT_SPOX_VACCINATION_SCAR_PRESENT,
			RiskFactorDto.PATIENT_TRAVELLED_ANYWHERE_3WEEKS_PRIOR,
			RiskFactorDto.PATIENT_TRAVEL_DETAILS_PRIOR,
			RiskFactorDto.PATIENT_TRAVELLED_PERIOD_OF_ILLNESS,
			RiskFactorDto.PATIENT_TRAVEL_DETAILS_DURING,
			RiskFactorDto.OTHER_PLACES,
			RiskFactorDto.DURING_3WEEKS_PATIENT_CONTACT_WITH_SIMILAR_SYMPTOMS,
			RiskFactorDto.PATIENT_SYMPTOMS_PRECEDENCE,
			RiskFactorDto.DATE_OF_CONTACT_WITH_ILL_PERSON,
			RiskFactorDto.PATIENT_TOUCH_DOMESTIC_WILD_ANIMAL,
			RiskFactorDto.PATIENT_TOUCH_DOMESTIC_WILD_ANIMAL_IF_YES);

		allowEntityFields(
			AfpImmunizationDto.class,
			Disease.AFP,
			AfpImmunizationDto.UUID,
			AfpImmunizationDto.TOTAL_NUMBER_DOSES,
			AfpImmunizationDto.OPV_DOSE_AT_BIRTH,
			AfpImmunizationDto.SECOND,
			AfpImmunizationDto.FOURTH,
			AfpImmunizationDto.FIRST,
			AfpImmunizationDto.THIRD,
			AfpImmunizationDto.LAST_DOSE,
			AfpImmunizationDto.TOTAL_OPV_DOSES_RECEIVED_THROUGH_SIA,
			AfpImmunizationDto.TOTAL_OPV_DOSES_RECEIVED_THROUGH_RI,
			AfpImmunizationDto.DATE_LAST_OPV_DOSES_RECEIVED_THROUGH_SIA,
			AfpImmunizationDto.TOTAL_IPV_DOSES_RECEIVED_THROUGH_SIA,
			AfpImmunizationDto.TOTAL_IPV_DOSES_RECEIVED_THROUGH_RI,
			AfpImmunizationDto.DATE_LAST_IPV_DOSES_RECEIVED_THROUGH_SIA,
			AfpImmunizationDto.SOURCE_RI_VACCINATION_INFORMATION);
	}

	private static void allowPathogenTestFields(Disease disease, String... fields) {
		PATHOGEN_TEST_LAYOUT_FIELDS.get(disease).addAll(Arrays.asList(fields));
	}

	private static void allowEntityFields(Class<?> dtoClass, Disease disease, String... fields) {
		ENTITY_LAYOUT_FIELDS.computeIfAbsent(dtoClass, key -> new HashMap<Disease, Set<String>>()).put(disease, setOf(fields));
	}

	private static Set<String> setOf(String... fields) {
		return new LinkedHashSet<>(Arrays.asList(fields));
	}
}
