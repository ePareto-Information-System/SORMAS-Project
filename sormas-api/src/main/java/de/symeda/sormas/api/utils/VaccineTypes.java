package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum VaccineTypes {
    MenAC,
    MenACW,
    MenACWY,
    MenA_CONJUNATE,
    PCV13_1,
    PCV13_2,
    PCV13_3,
    HIB_1,
    HIB_2,
    HIB_3;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
