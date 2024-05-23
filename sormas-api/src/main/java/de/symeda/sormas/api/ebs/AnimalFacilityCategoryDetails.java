package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum AnimalFacilityCategoryDetails {
    HUMAN_ILLNESS,
    SEVERE_ILLNESS;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
