package de.symeda.sormas.api.riskfactor;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum DrinkingWaterSource {
    TAP_WATER,
    BOREHOLE,
    UNPROTECTED_WELL,
    PROTECTED_WELL,
    RIVER,
    DAM,
    LAKE,
    POND,
    SACHET_WATER;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
