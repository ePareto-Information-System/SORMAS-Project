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