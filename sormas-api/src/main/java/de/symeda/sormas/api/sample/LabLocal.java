package de.symeda.sormas.api.sample;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum LabLocal {

    IN_COUNTRY,
    OUTSIDE_COUNTRY;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
