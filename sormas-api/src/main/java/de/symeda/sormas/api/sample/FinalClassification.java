package de.symeda.sormas.api.sample;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.i18n.I18nProperties;
import java.util.Arrays;
import java.util.List;

public enum FinalClassification {

    CONFIRMED_MEASLES_BY_LAB,
    CONFIRMED_MEASLES_BY_EPIDEMIOLOGIC_LINK,
    COMPATIBLE_CLINICAL_MEASLES,
    CONFIRMED_RUBELLA_BY_LAB,
    DISCARDED_NON_MEASLES_NON_RUBELLA_BY_LAB,
    SUSPECTED_MEASLES_RUBELLA_WITH_LAB_RESULTS_AND_OR_CASE_CLASSIFICATION_PENDING,
    LAB_CONFIRMED,
    EPIDEMIOLOGICAL,
    COMPATIBLE,
    DISCARDED,
    PENDING,
    NOT_A_CASE,
    SUSPECT,
    PROBABLE,
    CONFIRMED_POLIO,
    NOT_AN_AFP_CASE,
    cVDPV,
    aVDPV,
    iVDPV,
    SERO_TYPE;

    public static final List<FinalClassification> YF_CLASSIFICATION = Arrays.asList( LAB_CONFIRMED, EPIDEMIOLOGICAL, COMPATIBLE, DISCARDED, PENDING);
    public static final List<FinalClassification> CHOLERA_CLASSIFICATION = Arrays.asList( NOT_A_CASE, SUSPECT, PROBABLE, LAB_CONFIRMED, EPIDEMIOLOGICAL, PENDING);

    public static final List<FinalClassification> DEFAULT = Arrays.asList( CONFIRMED_MEASLES_BY_LAB, LAB_CONFIRMED, EPIDEMIOLOGICAL, DISCARDED, PENDING);

    public static final List<FinalClassification> measlesClass = Arrays.asList(CONFIRMED_MEASLES_BY_LAB,
            CONFIRMED_MEASLES_BY_EPIDEMIOLOGIC_LINK,
            COMPATIBLE_CLINICAL_MEASLES,
            CONFIRMED_RUBELLA_BY_LAB,
            DISCARDED_NON_MEASLES_NON_RUBELLA_BY_LAB,
            SUSPECTED_MEASLES_RUBELLA_WITH_LAB_RESULTS_AND_OR_CASE_CLASSIFICATION_PENDING  );
    public static final List<FinalClassification> AFP_CLASSIFICATION = Arrays.asList(CONFIRMED_POLIO, COMPATIBLE, DISCARDED, NOT_AN_AFP_CASE, cVDPV, aVDPV, iVDPV, SERO_TYPE);


    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }

}
