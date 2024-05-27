package de.symeda.sormas.api.person;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum Profession {
    MINER,
    HOUSE_WIFE,
    HUNTER,
    CHILDREN,
    PUPIL,
    FARMER,
    OTHERS;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
