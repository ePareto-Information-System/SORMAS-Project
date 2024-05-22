package de.symeda.sormas.api.caze;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum NotifiedList {

    ATTENDING,
    COMM_BASED_SURV_VOL,
    HEALTH_WORKER,
    OTHER_COMM_WORKER,
    PARENT_FAM,
    RELIGIOUS_LEADER,
    OTHER;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }

}
