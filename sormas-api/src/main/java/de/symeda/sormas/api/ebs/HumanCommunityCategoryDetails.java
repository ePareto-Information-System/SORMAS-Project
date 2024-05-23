package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum HumanCommunityCategoryDetails {
    RESPIRATORY_SYMPTOMS,
    SEVERE_ILLNESS,
    SEVERE_DIARRHOEA,
    ORIFICES,
    UNABLE_TO_WALK,
    OTC,
    HANGING_WORM;
    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
