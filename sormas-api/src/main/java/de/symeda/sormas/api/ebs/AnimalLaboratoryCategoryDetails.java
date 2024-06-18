package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum AnimalLaboratoryCategoryDetails {
    SUDDEN_UNEXPECTED,
    POSITIVE_TEST,
    DETECTION_UNKNOWN;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
