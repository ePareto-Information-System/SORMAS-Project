package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum BodyPart {
    FACE,
    NECK,
    TRUNK,
    ARMS,
    LEGS,
    PALMS,
    GENITAL_AREA,
    SOLES_OF_THE_FEET,
    ALL_OVER_THE_BODY,
    OTHER;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
