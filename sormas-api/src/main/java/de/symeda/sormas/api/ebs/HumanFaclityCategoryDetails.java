package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum HumanFaclityCategoryDetails {
    INCREASE_IN_ADMISSION,
    INCREASE_IN_SEVERE,
    AMONGST_HEALTH,
    ACUTE_ONSET;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
    }
