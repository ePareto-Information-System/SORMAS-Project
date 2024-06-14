package de.symeda.sormas.ui.epidata;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.epidata.EpiDataDto;

import java.util.*;

public class EpiFormConfiguration {
    private static final Map<String, Set<Disease>> DISABLED_FIELDS_BY_DISEASE = new HashMap<>();

    static {
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.EXPOSURE_DETAILS_KNOWN, new HashSet<>(Arrays.asList(Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,Disease.AHF)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.EXPOSURES, new HashSet<>(Arrays.asList(Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,Disease.AHF)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.ACTIVITY_AS_CASE_DETAILS_KNOWN, new HashSet<>(Arrays.asList(Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,Disease.AHF)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.ACTIVITIES_AS_CASE, new HashSet<>(Arrays.asList(Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,Disease.AHF)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.HIGH_TRANSMISSION_RISK_AREA, new HashSet<>(Arrays.asList(Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,Disease.AHF)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.LARGE_OUTBREAKS_AREA, new HashSet<>(Arrays.asList(Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,Disease.AHF)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.AREA_INFECTED_ANIMALS, new HashSet<>(Arrays.asList(Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,Disease.AHF)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.CONTACT_WITH_SOURCE_CASE_KNOWN, new HashSet<>(Arrays.asList(Disease.AFP, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS,Disease.AHF)));
        DISABLED_FIELDS_BY_DISEASE.put(EpiDataDto.CONTACT_SICK_ANIMALS, new HashSet<>(Arrays.asList(Disease.AFP)));
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
