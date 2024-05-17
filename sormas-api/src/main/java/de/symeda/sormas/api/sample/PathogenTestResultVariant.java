package de.symeda.sormas.api.sample;

import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.DataHelper;

public enum PathogenTestResultVariant {
    AH1NI,
    AH3N2,
    VICTORIA,
    YAMAGATA,
    OTHER;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }

    public static String toString(PathogenTestResultVariant value, String details) {
        if (value == null) {
            return "";
        }

        if (value == PathogenTestResultVariant.OTHER) {
            return DataHelper.toStringNullable(details);
        }

        return value.toString();
    }
}
