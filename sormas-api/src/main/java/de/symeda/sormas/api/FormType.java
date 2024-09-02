package de.symeda.sormas.api;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum FormType {
    CASE_CREATE,
    CASE_EDIT,
    PERSON_EDIT;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
