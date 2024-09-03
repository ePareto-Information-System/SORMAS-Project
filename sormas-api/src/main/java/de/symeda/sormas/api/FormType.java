package de.symeda.sormas.api;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum FormType {
    CASE_CREATE,
    CASE_EDIT,
    PERSON_EDIT,
    HOSPITALIZATION_EDIT,
    SYMPTOMS_EDIT,
    EPIDEMIOLOGICAL_EDIT,
    RISK_FACTOR_EDIT,
    FOOD_HISTORY_EDIT,
    SIXTY_DAY_FOLLOW_UP_EDIT,
    AFP_IMMUNIZATION_EDIT,
    FOOD_SAMPLE_TESTING_EDIT,
    PERSON_LOCATION_EDIT,
    SAMPLE_CREATE,
    SAMPLE_EDIT,
    PATHOGEN_TEST_CREATE,
    PATHOGEN_TEST_EDIT;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
