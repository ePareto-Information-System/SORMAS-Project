package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum ResponseStatus {
    NOT_STARTED,
    ON_GOING,
    COMPLETED;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
