package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum CategoryDetails {
    RESPIRATORY_SYSMPTOMS,
    SEVERE_ILLNESS,
    SEVERE_DIARRHOEA,
    ORIFICES,
    UNABLE_TO_WALK,
    OTC,
    PREGNANT_WOMAN,
    HANGING_WORM;
    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
