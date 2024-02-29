package de.symeda.sormas.api.epidata;

import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.DataHelper;

public enum ContactSetting {
    HEALTH_CARE_SETTING,
    FAMILY_SETTING,
    WORKPLACE,
    OTHER;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }

    public static String toString(ContactSetting value, String details) {
        if (value == null) {
            return "";
        }

        if (value == OTHER) {
            return DataHelper.toStringNullable(details);
        }

        return value.toString();
    }
}
