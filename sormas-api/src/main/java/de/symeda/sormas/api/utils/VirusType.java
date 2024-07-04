package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum VirusType {

    AH5N1,
    AH3N2,
    B_VIRUS,
    NEGATIVE,
    PENDING,
    OTHER;
    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
