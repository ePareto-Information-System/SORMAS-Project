package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum FoodSource {
    SCHOOL_CANTEEN,
    OFFICE_CANTEEN,
    RESTAURANT,
    CHOPBAR,
    STREET_VENDED_FOOD,
    HOME,
    OTHER;
    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }

}
