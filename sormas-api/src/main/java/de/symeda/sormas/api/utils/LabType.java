package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum LabType {
    DISTRICT_LAB,
    REGIONAL_LAB,
    REFERENCE_LAB;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
