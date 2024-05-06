package de.symeda.sormas.api.epidata;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum PlaceManaged {

    CCC,
    HOME,
    HEALTH_CENTER,
    HOSPITAL;


    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
