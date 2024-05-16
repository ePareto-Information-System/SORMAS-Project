package de.symeda.sormas.ui.person;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.person.PersonDto;

import java.util.*;

import static de.symeda.sormas.ui.person.PersonEditForm.*;

public class PersonFormConfiguration {

    private static final Map<String, Set<Disease>> DISABLED_FIELDS_BY_DISEASE = new HashMap<>();

    static {
        // Associate diseases with fields
            DISABLED_FIELDS_BY_DISEASE.put(PERSON_INFORMATION_HEADING_LOC, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.UUID, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.FIRST_NAME, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.LAST_NAME, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.OTHER_NAME, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.SALUTATION, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.OTHER_SALUTATION, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.BIRTH_DATE_YYYY, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.BIRTH_DATE_MM, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.BIRTH_DATE_DD, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.APPROXIMATE_AGE, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.APPROXIMATE_AGE_TYPE, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.APPROXIMATE_AGE_REFERENCE_DATE, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.PLACE_OF_BIRTH_REGION, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.PLACE_OF_BIRTH_DISTRICT, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.PLACE_OF_BIRTH_COMMUNITY, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.PLACE_OF_BIRTH_FACILITY_TYPE, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.PLACE_OF_BIRTH_FACILITY, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.PLACE_OF_BIRTH_FACILITY_DETAILS, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.GESTATION_AGE_AT_BIRTH, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.BIRTH_WEIGHT, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.SEX, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.PRESENT_CONDITION, new HashSet<>(Arrays.asList(Disease.FOODBORNE_ILLNESS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS, Disease.NEW_INFLUENZA)));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.DEATH_DATE, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.CAUSE_OF_DEATH, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.CAUSE_OF_DEATH_DISEASE, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.CAUSE_OF_DEATH_DETAILS, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.DEATH_PLACE_TYPE, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.DEATH_PLACE_DESCRIPTION, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.BURIAL_DATE, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.BURIAL_CONDUCTOR, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.BURIAL_PLACE_DESCRIPTION, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.NATIONAL_HEALTH_ID, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.GHANA_CARD, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.PASSPORT_NUMBER, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.NUMBER_OF_PEOPLE, new HashSet<>(Arrays.asList(Disease.CSM, Disease.NEW_INFLUENZA, Disease.SARI, Disease.AFP, Disease.YELLOW_FEVER)));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.NUMBER_OF_OTHER_CONTACTS, new HashSet<>(Arrays.asList(Disease.CSM, Disease.NEW_INFLUENZA, Disease.SARI, Disease.AFP, Disease.YELLOW_FEVER)));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.HAS_COVID_APP, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.COVID_CODE_DELIVERED, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(OCCUPATION_HEADER, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.OCCUPATION_DETAILS, new HashSet<>(Arrays.asList(Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS)));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.OCCUPATION_TYPE, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.ARMED_FORCES_RELATION_TYPE, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.EDUCATION_TYPE, new HashSet<>(Arrays.asList(Disease.AFP, Disease.CSM, Disease.FOODBORNE_ILLNESS, Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS)));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.EDUCATION_DETAILS, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(ADDRESS_HEADER, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.HOME_ADDRESS_RECREATIONAL, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.ADDRESS, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(CONTACT_INFORMATION_HEADER, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.BIRTH_NAME, new HashSet<>(Arrays.asList(Disease.AFP)));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.NICKNAME, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.MOTHERS_MAIDEN_NAME, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.MOTHERS_NAME, new HashSet<>(Arrays.asList(Disease.FOODBORNE_ILLNESS)));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.FATHERS_NAME, new HashSet<>(Arrays.asList(Disease.FOODBORNE_ILLNESS)));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.NAMES_OF_GUARDIANS, new HashSet<>(Arrays.asList(Disease.AFP)));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.BIRTH_COUNTRY, new HashSet<>(Arrays.asList(Disease.AHF, Disease.DENGUE, Disease.AFP)));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.CITIZENSHIP, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.PERSON_CONTACT_DETAILS, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(GENERAL_COMMENT_LOC, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.ADDITIONAL_DETAILS, new HashSet<>(Arrays.asList(Disease.AFP)));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.ADDITIONAL_PLACES_STAYED, new HashSet<>(Arrays.asList()));
            DISABLED_FIELDS_BY_DISEASE.put(PersonDto.MARRIAGE_STATUS, new HashSet<>(Arrays.asList(Disease.CSM, Disease.NEW_INFLUENZA, Disease.SARI, Disease.AFP, Disease.YELLOW_FEVER, Disease.AHF, Disease.DENGUE, Disease.CORONAVIRUS, Disease.MEASLES)));

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
