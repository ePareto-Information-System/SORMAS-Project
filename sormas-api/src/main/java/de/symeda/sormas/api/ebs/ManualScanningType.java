package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum ManualScanningType {
    TV,
    RADIO,
    COMMUNITY_INFORMANT,
    NEWSPAPER,
    ONLINE,
    SOCIAL_MEDIA,
    OTHER;
    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }

}
