package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.event.TypeOfPlace;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.sample.PathogenTestType;

import java.util.Arrays;
import java.util.List;

public enum LatexCulture {

    NMA,
    NMC,
    NMW,
    NMW_Y,
    NMB,
    NMB_ECOLIK1,
    S_PNEUMONIAE,
    HIB,
    STREPB,
    NEGATIVE,
    NMY,
    NMX,
    NM_INDETERMINATE,
    H_INFLUENZAE_INDETERMINATE,
    OTHER_GERMS,
    CONTAMINATED,
    OTHER_TEST;

    public static final List<LatexCulture> LATEX = Arrays.asList(
            NMA,
            NMC,
            NMW_Y,
            NMB_ECOLIK1,
            S_PNEUMONIAE,
            HIB,
            STREPB,
            NEGATIVE);
    public static final List<LatexCulture> LAB_CULTURE = Arrays.asList(
            NMA,
            NMC,
            NMW,
            NMY,
            NMB,
            NMX,
            NM_INDETERMINATE,
            S_PNEUMONIAE,
            HIB,
            H_INFLUENZAE_INDETERMINATE,
            STREPB,
            OTHER_GERMS,
            CONTAMINATED,
            NEGATIVE);


    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
