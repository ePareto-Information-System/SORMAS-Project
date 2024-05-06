package de.symeda.sormas.api.sample;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum FilterChangingFrequency {
    RARELY,
    SOMETIMES,
    ALWAYS,
    NEVER;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }

}
