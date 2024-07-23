package de.symeda.sormas.ui.hospitalization;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.hospitalization.HospitalizationDto;

import java.util.*;

import static de.symeda.sormas.ui.hospitalization.HospitalizationForm.HEALTH_FACILITY;
import static de.symeda.sormas.ui.hospitalization.HospitalizationForm.HEALTH_FACILITY_DISTRICT;

public class HospitalizationFormConfiguration {
    private static final Map<String, Set<Disease>> DISABLED_FIELDS_BY_DISEASE = new HashMap<>();

    static {
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY, new HashSet<>(Arrays.asList(Disease.AFP, Disease.CSM)));
        DISABLED_FIELDS_BY_DISEASE.put(HEALTH_FACILITY, new HashSet<>(Arrays.asList(Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(HEALTH_FACILITY_DISTRICT, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.HOSPITAL_RECORD_NUMBER, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY_NEW, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.PHYSICIAN_NAME, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.DATE_FIRST_SEEN_HOSPITAL_FOR_DISEASE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.TERMINATION_DATE_HOSPITAL_STAY, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.ADMISSION_DATE, new HashSet<>(Arrays.asList(Disease.AHF)));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.DISCHARGE_DATE, new HashSet<>(Arrays.asList(Disease.AFP, Disease.CSM, Disease.AHF)));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.LEFT_AGAINST_ADVICE, new HashSet<>(Arrays.asList(Disease.AFP, Disease.CSM, Disease.AHF, Disease.NEW_INFLUENZA)));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.NOTIFY_DISTRICT_DATE, new HashSet<>(Arrays.asList(Disease.AFP, Disease.CSM, Disease.AHF, Disease.NEW_INFLUENZA, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.DATE_FORM_SENT_TO_DISTRICT, new HashSet<>(Arrays.asList(Disease.AFP, Disease.CSM, Disease.AHF, Disease.YELLOW_FEVER, Disease.NEW_INFLUENZA, Disease.FOODBORNE_ILLNESS, Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.HOSPITALIZATION_REASON, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.OTHER_HOSPITALIZATION_REASON, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.INTENSIVE_CARE_UNIT, new HashSet<>(Arrays.asList(Disease.AFP, Disease.CSM)));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.INTENSIVE_CARE_UNIT_START, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.INTENSIVE_CARE_UNIT_END, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.ISOLATED, new HashSet<>(Arrays.asList(Disease.AFP, Disease.CSM, Disease.NEW_INFLUENZA)));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.ISOLATION_DATE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.DESCRIPTION, new HashSet<>(Arrays.asList(Disease.CSM, Disease.NEW_INFLUENZA)));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.HOSPITALIZED_PREVIOUSLY, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.PREVIOUS_HOSPITALIZATIONS, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.DISEASE_ONSET_DATE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.PATIENT_HOSPITALIZED_DETAINED, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.SOUGHT_MEDICAL_ATTENTION, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.NAME_OF_FACILITY, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.LOCATION_ADDRESS, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.DATE_OF_VISIT_HOSPITAL, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.PHYSICIAN_NUMBER, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.LAB_TEST_CONDUCTED, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.TYPE_OF_SAMPLE, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.AGENT_IDENTIFIED, new HashSet<>(Arrays.asList()));
        DISABLED_FIELDS_BY_DISEASE.put(HospitalizationDto.HOSPITALIZATION_YES_NO, new HashSet<>(Arrays.asList(Disease.AFP, Disease.CSM, Disease.AHF, Disease.YELLOW_FEVER, Disease.NEW_INFLUENZA, Disease.NEONATAL_TETANUS, Disease.MONKEYPOX, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, Disease.CHOLERA, Disease.MEASLES, Disease.CORONAVIRUS, Disease.GUINEA_WORM)));

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
