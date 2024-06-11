package de.symeda.sormas.ui.caze;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseDataDto;

import java.util.*;

import static de.symeda.sormas.ui.caze.CaseDataForm.*;

public class CaseFormConfiguration {

    private static final Map<String, Set<Disease>> DISABLED_FIELDS_BY_DISEASE = new HashMap<>();

    static {
        // Associate diseases with fields
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.CLASSIFICATION_DATE, new HashSet<>(Arrays.asList(Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.CLASSIFICATION_USER, new HashSet<>(Arrays.asList(Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.CLINICAL_CONFIRMATION, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.EPIDEMIOLOGICAL_CONFIRMATION, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.NOT_A_CASE_REASON_NEGATIVE_TEST, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.NOT_A_CASE_REASON_PHYSICIAN_INFORMATION, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.NOT_A_CASE_REASON_DIFFERENT_PATHOGEN, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.NOT_A_CASE_REASON_OTHER, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.NOT_A_CASE_REASON_DETAILS, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.INVESTIGATION_STATUS, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.INVESTIGATED_DATE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.EPID_NUMBER, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(ASSIGN_NEW_EPID_NUMBER_LOC, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.NOTIFIED_BY, new HashSet<>(Arrays.asList(Disease.AFP)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.DATE_OF_NOTIFICATION, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.DATE_OF_INVESTIGATION, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.EXTERNAL_ID, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.EXTERNAL_TOKEN, new HashSet<>(Arrays.asList(Disease.MEASLES)));
        DISABLED_FIELDS_BY_DISEASE.put(EXTERNAL_TOKEN_WARNING_LOC, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.CASE_ID_ISM, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.INTERNAL_TOKEN, new HashSet<>(Arrays.asList(Disease.MEASLES)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.DISEASE, new HashSet<>(Arrays.asList(Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.DISEASE_DETAILS, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.PLAGUE_TYPE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.DENGUE_FEVER_TYPE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.RABIES_TYPE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.DISEASE_VARIANT, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.DISEASE_VARIANT_DETAILS, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.RE_INFECTION, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(REINFECTION_INFO_LOC, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.REINFECTION_STATUS, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.PREVIOUS_INFECTION_DATE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.OUTCOME, new HashSet<>(Arrays.asList(Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, Disease.AHF)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.OUTCOME_DATE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.SEQUELAE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.SEQUELAE_DETAILS, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.CASE_IDENTIFICATION_SOURCE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.SCREENING_TYPE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.CASE_ORIGIN, new HashSet<>(Arrays.asList(Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.CASE_TRANSMISSION_CLASSIFICATION, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(RESPONSIBLE_JURISDICTION_HEADING_LOC, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.RESPONSIBLE_REGION, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.RESPONSIBLE_DISTRICT, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.RESPONSIBLE_COMMUNITY, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.DONT_SHARE_WITH_REPORTING_TOOL, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(DIFFERENT_PLACE_OF_STAY_JURISDICTION, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.HOME_ADDRESS_RECREATIONAL, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(PLACE_OF_STAY_HEADING_LOC, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(FACILITY_OR_HOME_LOC, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.REGION, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.DISTRICT, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.COMMUNITY, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(TYPE_GROUP_LOC, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.FACILITY_TYPE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.HEALTH_FACILITY, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.HEALTH_FACILITY_DETAILS, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.POINT_OF_ENTRY, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.POINT_OF_ENTRY_DETAILS, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CASE_REFER_POINT_OF_ENTRY_BTN_LOC, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.NOSOCOMIAL_OUTBREAK, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.INFECTION_SETTING, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.SHARED_TO_COUNTRY, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.PROHIBITION_TO_WORK, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.PROHIBITION_TO_WORK_FROM, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.PROHIBITION_TO_WORK_UNTIL, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE_HOME_POSSIBLE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE_HOME_POSSIBLE_COMMENT, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE_HOME_SUPPLY_ENSURED, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE_HOME_SUPPLY_ENSURED_COMMENT, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE, new HashSet<>(Arrays.asList(Disease.MEASLES)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE_FROM, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE_TO, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE_CHANGE_COMMENT, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.PREVIOUS_QUARANTINE_TO, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE_EXTENDED, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE_REDUCED, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE_TYPE_DETAILS, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE_ORDERED_VERBALLY, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE_ORDERED_VERBALLY_DATE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE_ORDERED_OFFICIAL_DOCUMENT, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE_ORDERED_OFFICIAL_DOCUMENT_DATE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE_OFFICIAL_ORDER_SENT, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE_OFFICIAL_ORDER_SENT_DATE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE_HELP_NEEDED, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.WAS_IN_QUARANTINE_BEFORE_ISOLATION, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE_REASON_BEFORE_ISOLATION, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.QUARANTINE_REASON_BEFORE_ISOLATION_DETAILS, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.END_OF_ISOLATION_REASON, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.END_OF_ISOLATION_REASON_DETAILS, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.REPORT_LAT, new HashSet<>(Arrays.asList(Disease.CSM)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.REPORT_LON, new HashSet<>(Arrays.asList(Disease.CSM)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.REPORT_LAT_LON_ACCURACY, new HashSet<>(Arrays.asList(Disease.CSM)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.HEALTH_CONDITIONS, new HashSet<>(Arrays.asList(Disease.MEASLES)));

        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.BLOOD_ORGAN_OR_TISSUE_DONATED, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.PREGNANT, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.CSM, Disease.MEASLES, Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.POSTPARTUM, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.CSM, Disease.MEASLES, Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.TRIMESTER, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.VACCINATION_STATUS, new HashSet<>(Arrays.asList(Disease.AHF, Disease.DENGUE, Disease.AFP)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.VACCINATION_TYPE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.VACCINE_TYPE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.NUMBER_OF_DOSES, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.AHF, Disease.DENGUE, Disease.CSM, Disease.AFP, Disease.NEW_INFLUENZA, Disease.SARI, Disease.MEASLES, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.VACCINATION_DATE, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.CSM, Disease.AFP, Disease.NEW_INFLUENZA, Disease.SARI, Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.SMALLPOX_VACCINATION_RECEIVED, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.SMALLPOX_VACCINATION_SCAR, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.SMALLPOX_LAST_VACCINATION_DATE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.CLINICIAN_NAME, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.NOTIFYING_CLINIC, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.NOTIFYING_CLINIC_DETAILS, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.CLINICIAN_PHONE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.CLINICIAN_EMAIL, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.CONTACT_TRACING_FIRST_CONTACT_TYPE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.CONTACT_TRACING_FIRST_CONTACT_DATE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.FOLLOW_UP_STATUS, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.FOLLOW_UP_STATUS_CHANGE_DATE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.FOLLOW_UP_STATUS_CHANGE_USER, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.FOLLOW_UP_UNTIL, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.FOLLOW_UP_COMMENT, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.SURVEILLANCE_OFFICER, new HashSet<>(Arrays.asList(Disease.MEASLES)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.REPORTING_OFFICER_NAME, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER,Disease.AFP, Disease.NEW_INFLUENZA, Disease.SARI,Disease.DENGUE, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.REPORTING_OFFICER_TITLE, new HashSet<>(Arrays.asList(Disease.AFP, Disease.NEW_INFLUENZA, Disease.SARI,Disease.AHF, Disease.DENGUE, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.FUNCTION_OF_REPORTING_OFFICER, new HashSet<>(Arrays.asList(Disease.AFP, Disease.NEW_INFLUENZA, Disease.SARI,Disease.AHF, Disease.DENGUE, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.REPORTING_OFFICER_CONTACT_PHONE, new HashSet<>(Arrays.asList(Disease.AFP, Disease.NEW_INFLUENZA,Disease.SARI, Disease.AHF, Disease.DENGUE, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.REPORTING_OFFICER_EMAIL, new HashSet<>(Arrays.asList(Disease.CSM, Disease.AFP, Disease.NEW_INFLUENZA, Disease.SARI,Disease.AHF, Disease.DENGUE, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS)));
        DISABLED_FIELDS_BY_DISEASE.put(PAPER_FORM_DATES_LOC, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.DATE_FORM_RECEIVED_AT_DISTRICT, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.DATE_FORM_RECEIVED_AT_REGION, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.DATE_FORM_RECEIVED_AT_NATIONAL, new HashSet<>(Arrays.asList(Disease.NEW_INFLUENZA)));
        DISABLED_FIELDS_BY_DISEASE.put(GENERAL_COMMENT_LOC, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.ADDITIONAL_DETAILS, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.DELETION_REASON, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.OTHER_DELETION_REASON, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.INFORMATION_GIVEN_BY, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.FAMILY_LINK_WITH_PATIENT, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.LAST_VACCINATION_DATE, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.AFP, Disease.AHF, Disease.DENGUE, Disease.NEW_INFLUENZA, Disease.SARI, Disease.CSM, Disease.CORONAVIRUS, Disease.AFP, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(CaseDataDto.NOTIFIED_BY_LIST, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.AFP, Disease.AHF, Disease.CORONAVIRUS, Disease.NEW_INFLUENZA, Disease.CHOLERA, Disease.CSM, Disease.NON_NEONATAL_TETANUS, Disease.AFP, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX, Disease.MEASLES, Disease.GUINEA_WORM)));
    }


    public static Set<String> getDisabledFieldsForDisease(Disease disease) {
        Set<String> disabledFields = new HashSet<>();
        for (Map.Entry<String, Set<Disease>> entry : DISABLED_FIELDS_BY_DISEASE.entrySet()) {
            if (entry.getValue().contains(disease)) {
                disabledFields.add(entry.getKey());
            }
        }
        return disabledFields;
    }

}
