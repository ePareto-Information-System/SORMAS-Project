package de.symeda.sormas.api.sample;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum IpSampleTestType {
    IGM,
    PCR,
    PRNT;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }

}
