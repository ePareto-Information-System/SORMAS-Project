package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum PersonReporting {
    COMMUNITY_SURVEILLANCE_VOLUNTEER,
    COMMUNITY_ANIMAL_HEALTH_WORKER,
    OTC_CHEMICAL_WORKER,
    COMMUNITY_MEMBER,
    PUBLIC_HEALTHCARE,
    PRIVATE_HEALTH,
    REFERENCE_LABORATORY,
    GENERAL_PUBLIC_INFORMANT,
    INSTITUTIONAL_INFORMANT,
    PERSON_DISTRICT,
    PERSON_REGION,
    PERSON_NATIONAL,
    OTHER;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }

}
