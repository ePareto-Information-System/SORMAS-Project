package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum DurationHours {
    LESS_THAN_12HRS,
    TWELVE_24HRS,
    MORE_THAN_24HRS;
    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
