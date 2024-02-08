package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum AFPClassification {
    CONFIRMED_POLIO,
    COMPATIBLE,
    DISCARDED,
    NOT_AN_AFP_CASE,
    cVDPV,
    aVDPV,
    iVDPV,
    SERO_TYPE;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
