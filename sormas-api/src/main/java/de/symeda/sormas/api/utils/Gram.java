package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum Gram {
    GPD,
    GND,
    GPB,
    OTHER_PATHOGENS,
    NO_ORGANISM_SEEN;
    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
