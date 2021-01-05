package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum AFPFacilityOptions {
    Hospital,
    CHPS,
    Polyclinic,
    HC_CR;
    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }

    }