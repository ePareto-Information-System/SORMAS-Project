package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum YesNo {
    YES,
    NO;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }

    public static YesNo valueOf(Boolean value) {

        if (value == null) {
            return null;
        } else if (Boolean.TRUE.equals(value)) {
            return YES;
        } else {
            return NO;
        }
    }
}
