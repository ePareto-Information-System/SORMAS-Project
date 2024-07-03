package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum ExamResult {
    RESIDUAL_FLACCID_PARALYSIS,
    NO_RESIDUAL_PARALYSIS,
    LOST_FOLLOW_UP,
    DIED_BEFORE_FOLLOW_UP,
    RESIDUAL_SPASTIC_PARALYSIS;
    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
