package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum CardOrHistory {
    CARD,
    HISTORY;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }

    public static CardOrHistory valueOf(Boolean value) {

        if (value == null) {
            return null;
        } else if (Boolean.TRUE.equals(value)) {
            return CARD;
        } else {
            return HISTORY;
        }
    }
}
