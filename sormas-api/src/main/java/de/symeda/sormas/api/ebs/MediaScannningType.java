package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum MediaScannningType {
    AUTOMATIC,
    MANUAL;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
