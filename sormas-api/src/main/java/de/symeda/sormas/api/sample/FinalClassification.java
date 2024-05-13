package de.symeda.sormas.api.sample;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum FinalClassification {

    CONFIRMED_MEASLES_BY_LAB,
    CONFIRMED_MEASLES_BY_EPIDEMIOLOGIC_LINK,
    COMPATIBLE_CLINICAL_MEASLES,
    CONFIRMED_RUBELLA_BY_LAB,
    DISCARDED_NON_MEASLES_NON_RUBELLA_BY_LAB,
    SUSPECTED_MEASLES_RUBELLA_WITH_LAB_RESULTS_AND_OR_CASE_CLASSIFICATION_PENDING;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
