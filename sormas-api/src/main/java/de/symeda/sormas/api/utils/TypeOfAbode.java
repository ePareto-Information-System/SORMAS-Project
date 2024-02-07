package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

import java.util.Arrays;
import java.util.List;

public enum TypeOfAbode {
    DHIMS_FACILITY,
    HOME;

    public static final List<TypeOfAbode> FOR_DHIMS_CASES = Arrays.asList(DHIMS_FACILITY, HOME);
    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
