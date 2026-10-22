package de.symeda.sormas.api.caze.samplesexport;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import de.symeda.sormas.api.Disease;
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

/** Shared disease-disabled DTO fields used by forms and disease exports. */
public final class DiseaseSpecificExportFieldConfiguration {

	private static final Map<Class<?>, Map<String, Set<Disease>>> DISABLED_FIELDS = new HashMap<>();
	private static final Map<Disease, Set<String>> CASE_LAYOUT_FIELDS = new HashMap<>();
	private static final Map<Disease, Set<String>> SAMPLE_LAYOUT_FIELDS = new HashMap<>();
	private static final Map<Disease, Set<String>> PATHOGEN_TEST_LAYOUT_FIELDS = new HashMap<>();
	private static final Map<Class<?>, Map<Disease, Set<String>>> ENTITY_LAYOUT_FIELDS = new HashMap<>();

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
	 * form layout. A DTO without an explicit allow-list remains governed by its
	 * disease annotations and disabled-field configuration.
	 */
	public static boolean isLayoutField(Class<?> dtoClass, String property, Disease disease) {
		Set<String> fields = getLayoutFields(dtoClass, disease);
		return fields == null || fields.contains(property);
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
