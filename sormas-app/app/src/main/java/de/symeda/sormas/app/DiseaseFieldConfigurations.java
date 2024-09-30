package de.symeda.sormas.app;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;

public class DiseaseFieldConfigurations {
    private static final Map<DiseaseFormTypeKey, List<Integer>> diseaseFieldConfigurations = new HashMap<>();

    static {
        // Yellow Fever Fields
        diseaseFieldConfigurations.put(new DiseaseFormTypeKey(Disease.YELLOW_FEVER, FormType.CASE_CREATE),
                Arrays.asList(
                        R.id.caseData_disease, R.id.caseData_caseOrigin, R.id.caseData_epidNumber, R.id.date_fields_layout, R.id.caseData_headingCaseResponsibleJurisidction,
                        R.id.region_district_fields_layout, R.id.caseData_responsibleCommunity, R.id.caseData_healthFacility, R.id.caseData_healthFacilityDetails,
                        R.id.name_fields_layout, R.id.caseData_otherName, R.id.person_birthdateLabel, R.id.birthday_fields_layout, R.id.person_sex, R.id.person_ghanaCard,
                        R.id.person_nationalHealthId, R.id.person_phone
                )
        );

        diseaseFieldConfigurations.put(new DiseaseFormTypeKey(Disease.YELLOW_FEVER, FormType.CASE_EDIT),
                Arrays.asList(
                        R.id.case_uuid_investigationStatus_fields_layout, R.id.caseData_reportDate, R.id.caseData_reportingUser, R.id.classifying_fields_layout, R.id.caseData_caseClassification,R.id.caseData_epidNumber, R.id.caseData_disease, R.id.caseData_caseOrigin, R.id.caseData_headingCaseResponsibleJurisidction, R.id.region_district_fields_layout, R.id.caseData_responsibleCommunity, R.id.caseData_healthFacility, R.id.caseData_healthFacilityDetails, R.id.caseData_headingAdditionalMedicalInformation, R.id.caseData_vaccinationStatus, R.id.caseData_vaccinationType, R.id.caseData_numberOfDoses, R.id.caseData_reportingOfficerTitle, R.id.caseData_functionOfReportingOfficer, R.id.caseData_reportingOfficerContactPhone, R.id.caseData_reportingOfficerEmail
                )
        );

        diseaseFieldConfigurations.put(new DiseaseFormTypeKey(Disease.YELLOW_FEVER, FormType.PERSON_LOCATION_EDIT),
                Arrays.asList(
                        R.id.location_region, R.id.location_district,R.id.location_community, R.id.location_houseNumber, R.id.location_city, R.id.location_areaType, R.id.location_postalCode, R.id.location_landMark, R.id.pick_gps_coordinates, R.id.lat_lon_coordinates, R.id.location_latLonAccuracy
                )
        );

        diseaseFieldConfigurations.put(new DiseaseFormTypeKey(Disease.YELLOW_FEVER, FormType.SYMPTOMS_EDIT),
                Arrays.asList(
                        R.id.symptoms_temperatureAndSource, R.id.symptoms_description, R.id.btns, R.id.symptoms_backache, R.id.symptoms_bodyAche, R.id.symptoms_chillsSweats, R.id.symptoms_fatigueWeakness, R.id.symptoms_fever, R.id.symptoms_headache, R.id.symptoms_jaundice, R.id.symptoms_vomiting, R.id.symptoms_nausea, R.id.symptoms_otherComplications, R.id.symptoms_otherComplicationsText, R.id.symptoms_onsetDate, R.id.symptoms_outcome
                )
        );
    }

    public static List<Integer> getFields(Disease disease, FormType formType) {
        return diseaseFieldConfigurations.getOrDefault(new DiseaseFormTypeKey(disease, formType), Arrays.asList());
    }
}
