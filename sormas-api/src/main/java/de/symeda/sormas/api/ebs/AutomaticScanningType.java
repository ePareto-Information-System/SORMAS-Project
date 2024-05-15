package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum AutomaticScanningType {
    TALKWALKER,
    EIOS,
    MELTWATER,
    OTHER;
    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }

}
