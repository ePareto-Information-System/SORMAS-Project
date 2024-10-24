package de.symeda.sormas.api.person;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum MaritalStatus {
    SINGLE,
    MARRIED;
    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
