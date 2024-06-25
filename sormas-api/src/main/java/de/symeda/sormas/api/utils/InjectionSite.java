package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.hospitalization.SymptomsList;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.infrastructure.facility.FacilityType;

import java.util.Arrays;
import java.util.List;

public enum InjectionSite {

    LEFT_ARM,
    LEFT_BUTTOCKS,
    LEFT_FOREARM,
    LEFT_LEG,
    LEFT_THIGH,
    RIGHT_ARM,
    RIGHT_BUTTOCKS,
    RIGHT_FOREARM,
    RIGHT_LEG,
    RIGHT_THIGH;

    public static InjectionSite[] ParalysisSite() {
        return new InjectionSite[] {
                LEFT_ARM, RIGHT_ARM, LEFT_LEG, RIGHT_LEG
        };
    }

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
