package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum ParalysisSite {
    LEFT_ARM,
    RIGHT_ARM,
    LEFT_LEG,
    RIGHT_LEG,
    OTHER;
    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
