package de.symeda.sormas.api.infrastructure.facility;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum DhimsFacility {

    CHPS_COMPOUND,
    MATERNITY_HOME,
    CLINIC,
    HEALTH_CENTRE,
    POLYCLINIC,
    HOSPITAL;


    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }

    public String getUuid() {
        return null;
    }
}
