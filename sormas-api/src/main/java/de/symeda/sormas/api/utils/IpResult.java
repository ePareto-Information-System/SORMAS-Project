package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum IpResult {
    IgM,
    PRNT,
    PCR;
    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
