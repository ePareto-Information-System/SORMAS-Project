package de.symeda.sormas.api.utils.pseudonymization;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum SampleDispatchMode {
    REGIONAL_COLDROOM,
    NATIONAL_LAB,
    NATIONAL_BY_DISTRICT;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
