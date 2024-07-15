package de.symeda.sormas.ui.samples;


import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.sample.SampleDto;

import java.util.*;

public class SampleFormConfiguration {
    private static final Map<String, Set<Disease>> DISABLED_FIELDS_BY_DISEASE = new HashMap<>();
    static {
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.PATHOGEN_TESTING_REQUESTED, new HashSet<>(Arrays.asList(Disease.NEW_INFLUENZA, Disease.CSM, Disease.AHF)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.SAMPLE_PURPOSE, new HashSet<>(Arrays.asList(Disease.AFP, Disease.AHF)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.REQUESTED_SAMPLE_MATERIALS, new HashSet<>(Arrays.asList(Disease.AFP, Disease.AHF)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.FIELD_SAMPLE_ID, new HashSet<>(Arrays.asList(Disease.AFP, Disease.AHF, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, Disease.CSM, Disease.NEW_INFLUENZA, Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.SAMPLE_MATERIAL_TEXT, new HashSet<>(Arrays.asList(Disease.AFP, Disease.YELLOW_FEVER)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.SAMPLE_MATERIAL_REQUESTED, new HashSet<>(Arrays.asList(Disease.AFP, Disease.AHF)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.COMMENT, new HashSet<>(Arrays.asList(Disease.AFP, Disease.AHF)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.SAMPLING_REASON, new HashSet<>(Arrays.asList(Disease.AFP, Disease.AHF, Disease.YELLOW_FEVER)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.SAMPLE_MATERIAL, new HashSet<>(Arrays.asList(Disease.AFP, Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.PATHOGEN_TEST_RESULT, new HashSet<>(Arrays.asList(Disease.AFP)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.SAMPLE_SOURCE, new HashSet<>(Arrays.asList(Disease.AFP, Disease.AHF, Disease.YELLOW_FEVER)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.SAMPLE_DATE_TIME, new HashSet<>(Arrays.asList(Disease.AFP, Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.SUSPECTED_DISEASE, new HashSet<>(Arrays.asList(Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.LAB_LOCATION, new HashSet<>(Arrays.asList(Disease.FOODBORNE_ILLNESS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.DATE_LAB_RECEIVED_SPECIMEN, new HashSet<>(Arrays.asList(Disease.FOODBORNE_ILLNESS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.LABORATORY_SAMPLE_CONDITION, new HashSet<>(Arrays.asList(Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.DATE_FORM_SENT_TO_DISTRICT, new HashSet<>(Arrays.asList(Disease.FOODBORNE_ILLNESS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.DATE_FORM_RECEIVED_AT_DISTRICT, new HashSet<>(Arrays.asList(Disease.FOODBORNE_ILLNESS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.DATE_RESULTS_RECEIVED_SENT_TO_CLINICIAN, new HashSet<>(Arrays.asList(Disease.FOODBORNE_ILLNESS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.ADDITIONAL_TESTING_REQUESTED, new HashSet<>(Arrays.asList(Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.SPECIMEN_CONDITION, new HashSet<>(Arrays.asList(Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS)));

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
