package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum SampleContainerUsed {

    DRY_TUBE,
    TRANS_ISOLATE,
    CRYOTUBE,
    OTHER;
    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
