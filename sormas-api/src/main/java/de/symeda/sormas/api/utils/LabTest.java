package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum LabTest {

    CYTOLOGY,
    GRAM_STAIN,
    LATEX,
    RDT;
    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
