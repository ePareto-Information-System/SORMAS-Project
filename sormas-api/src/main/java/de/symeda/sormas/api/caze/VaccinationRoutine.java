package de.symeda.sormas.api.caze;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum VaccinationRoutine {
    MR1,
    MR2,
    SIA;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
