package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.sample.PathogenTestType;

import java.util.Arrays;
import java.util.List;

public enum CsfReason {
    LACK_OF_LP_KIT,
    LACK_OF_SKILL,
    LP_CONTRAINDICATED,
    OTHER;


    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }

    public static String toString(CsfReason value, String details) {
        if (value == null) {
            return "";
        }

        if (value == CsfReason.OTHER) {
            return DataHelper.toStringNullable(details);
        }

        return value.toString();
    }
}
