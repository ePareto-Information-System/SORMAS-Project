package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum NormalWasted {
    NORMAL,
    WASTED;
    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
