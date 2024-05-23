package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum AnimalCommunityCategoryDetails {
    ANIMAL_DEATH,
    SUDDEN_DEATH,
    HUMAN_ILLNESS,
    SUDDEN_INFLUX;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
