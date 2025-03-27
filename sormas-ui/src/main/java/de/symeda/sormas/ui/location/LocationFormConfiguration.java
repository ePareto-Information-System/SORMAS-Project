package de.symeda.sormas.ui.location;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.location.LocationDto;

import java.util.*;


public class LocationFormConfiguration {

    private static final Map<String, Set<Disease>> DISABLED_FIELDS_BY_DISEASE = new HashMap<>();

    static {
        DISABLED_FIELDS_BY_DISEASE.put(LocationDto.ADDRESS_TYPE, new HashSet<>(Arrays.asList(Disease.AHF, Disease.AFP)));
        DISABLED_FIELDS_BY_DISEASE.put(LocationDto.ADDRESS_TYPE_DETAILS, new HashSet<>(Arrays.asList(Disease.AFP)));
        DISABLED_FIELDS_BY_DISEASE.put(LocationDto.REGION, new HashSet<>(Arrays.asList(Disease.AHF, Disease.NEW_INFLUENZA, Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(LocationDto.DISTRICT, new HashSet<>(Arrays.asList(Disease.AHF, Disease.NEW_INFLUENZA, Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(LocationDto.COMMUNITY, new HashSet<>(Arrays.asList(Disease.AHF, Disease.NEW_INFLUENZA, Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(LocationDto.STREET, new HashSet<>(Arrays.asList(Disease.NEW_INFLUENZA, Disease.AFP, Disease.FOODBORNE_ILLNESS)));
        DISABLED_FIELDS_BY_DISEASE.put(LocationDto.ADDITIONAL_INFORMATION, new HashSet<>(Arrays.asList(Disease.AHF)));
        DISABLED_FIELDS_BY_DISEASE.put(LocationDto.HOUSE_NUMBER, new HashSet<>(Arrays.asList(Disease.AHF)));
        DISABLED_FIELDS_BY_DISEASE.put(LocationDto.CITY, new HashSet<>(Arrays.asList(Disease.CSM, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS)));
        DISABLED_FIELDS_BY_DISEASE.put(LocationDto.AREA_TYPE, new HashSet<>(Arrays.asList(Disease.CSM, Disease.AHF)));
        DISABLED_FIELDS_BY_DISEASE.put(LocationDto.POSTAL_CODE, new HashSet<>(Arrays.asList(Disease.AHF, Disease.NEW_INFLUENZA, Disease.FOODBORNE_ILLNESS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS)));
        DISABLED_FIELDS_BY_DISEASE.put(LocationDto.CONTACT_PERSON_FIRST_NAME, new HashSet<>(Arrays.asList(Disease.AFP)));
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
