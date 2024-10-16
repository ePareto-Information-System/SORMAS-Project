package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum YellowFeverSample {

    BLOOD,
    SERUM,
    POST_MORTEM_LIVER_SPECIMEN;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }

    public static String toString(YellowFeverSample value, String details) {

        if (value == null) {
            return "";
        }

        return value.toString();
    }

}

