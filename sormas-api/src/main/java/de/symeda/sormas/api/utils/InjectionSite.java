package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.hospitalization.SymptomsList;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.infrastructure.facility.FacilityType;

import java.util.Arrays;
import java.util.List;

public enum InjectionSite {

    RIGHT_ARM,
    RIGHT_FOREARM,
    RIGHT_BUTTOCKS,
    RIGHT_THIGH,
    RIGHT_LEG,
    LEFT_ARM,
    LEFT_FOREARM,
    LEFT_BUTTOCKS,
    LEFT_THIGH,
    LEFT_LEG;

    public static final List<InjectionSite> InjectionSiteLeft = Arrays.asList(
            LEFT_ARM,
            LEFT_FOREARM,
            LEFT_BUTTOCKS,
            LEFT_THIGH,
            LEFT_LEG);

    public static final List<InjectionSite> InjectionSiteRight = Arrays.asList(
            RIGHT_ARM,
            RIGHT_FOREARM,
            RIGHT_BUTTOCKS,
            RIGHT_THIGH,
            RIGHT_LEG);

    public static InjectionSite[] ParalysisSite() {
        return new InjectionSite[] {
                LEFT_ARM, RIGHT_ARM, LEFT_LEG, RIGHT_LEG
        };
    }

    public static final List<InjectionSite> RemoveParalysisSite = Arrays.asList(
            RIGHT_FOREARM,
            RIGHT_BUTTOCKS,
            RIGHT_THIGH,
            LEFT_FOREARM,
            LEFT_BUTTOCKS,
            LEFT_THIGH);
    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
