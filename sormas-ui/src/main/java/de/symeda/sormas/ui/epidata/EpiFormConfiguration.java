package de.symeda.sormas.ui.epidata;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.epidata.EpiDataDto;

import java.util.*;

public class EpiFormConfiguration {
    private static final Map<String, Set<Disease>> DISABLED_FIELDS_BY_DISEASE = new HashMap<>();

    static {
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.PATIENT_TRAVELLED_TWO_WEEKS_PRIOR, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.CSM, Disease.AHF, Disease.AFP, Disease.NEW_INFLUENZA)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_ONE, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.CSM, Disease.AHF, Disease.AFP, Disease.NEW_INFLUENZA)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_TWO, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.CSM, Disease.AHF, Disease.AFP, Disease.NEW_INFLUENZA)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_THREE, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.CSM, Disease.AHF, Disease.AFP, Disease.NEW_INFLUENZA)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_FOUR, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.CSM, Disease.AHF, Disease.AFP, Disease.NEW_INFLUENZA)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_ONE, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.CSM, Disease.AHF, Disease.AFP, Disease.NEW_INFLUENZA)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_TWO, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.CSM, Disease.AHF, Disease.AFP, Disease.NEW_INFLUENZA)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_THREE, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.CSM, Disease.AHF, Disease.AFP, Disease.NEW_INFLUENZA)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_FOUR, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.CSM, Disease.AHF, Disease.AFP, Disease.NEW_INFLUENZA)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.PATIENT_VISITED_HEALTH_CARE_FACILITY, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.CSM, Disease.AHF, Disease.AFP, Disease.NEW_INFLUENZA)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.PATIENT_CLOSE_CONTACT_WITH_ARI, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.CSM, Disease.AHF, Disease.AFP, Disease.NEW_INFLUENZA)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.PATIENT_CLOSE_CONTACT_WITH_ARI_CONTACT_SETTINGS, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.CSM, Disease.AHF, Disease.AFP, Disease.NEW_INFLUENZA)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.CSM, Disease.AHF, Disease.AFP, Disease.NEW_INFLUENZA)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE_EXPOSURE_LOCATIONS, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.CSM, Disease.AHF, Disease.AFP, Disease.NEW_INFLUENZA)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE_EXPOSURE_LOCATION_CITY_COUNTRY, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.CSM, Disease.AHF, Disease.AFP, Disease.NEW_INFLUENZA)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.EXPOSURE_DETAILS_KNOWN, new HashSet<>(Arrays.asList(Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,Disease.AHF, Disease.NEW_INFLUENZA, Disease.YELLOW_FEVER, Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.EXPOSURES, new HashSet<>(Arrays.asList(Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,Disease.AHF, Disease.NEW_INFLUENZA, Disease.YELLOW_FEVER, Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.ACTIVITY_AS_CASE_DETAILS_KNOWN, new HashSet<>(Arrays.asList(Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,Disease.AHF, Disease.NEW_INFLUENZA, Disease.CSM, Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.ACTIVITIES_AS_CASE, new HashSet<>(Arrays.asList(Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,Disease.AHF, Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.HIGH_TRANSMISSION_RISK_AREA, new HashSet<>(Arrays.asList(Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,Disease.AHF, Disease.NEW_INFLUENZA, Disease.CSM, Disease.YELLOW_FEVER, Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.LARGE_OUTBREAKS_AREA, new HashSet<>(Arrays.asList(Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,Disease.AHF, Disease.NEW_INFLUENZA, Disease.CSM, Disease.YELLOW_FEVER, Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.AREA_INFECTED_ANIMALS, new HashSet<>(Arrays.asList(Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,Disease.AHF, Disease.NEW_INFLUENZA, Disease.YELLOW_FEVER, Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.CONTACT_WITH_SOURCE_CASE_KNOWN, new HashSet<>(Arrays.asList(Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,Disease.AHF, Disease.NEW_INFLUENZA, Disease.YELLOW_FEVER, Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.CONTACT_SICK_ANIMALS, new HashSet<>(Arrays.asList(Disease.AFP, Disease.CSM, Disease.YELLOW_FEVER, Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.RECENT_TRAVEL_OUTBREAK, new HashSet<>(Arrays.asList(Disease.CSM, Disease.YELLOW_FEVER, Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.CONTACT_SIMILAR_SYMPTOMS, new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.FOODBORNE_ILLNESS)));
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
