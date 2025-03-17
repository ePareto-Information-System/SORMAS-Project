package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.criteria.CriteriaDateType;

public enum NewEbsDateType implements
        CriteriaDateType {
    REPORT,
    TRIAGE_DECISION,
    VERIFIED_DATE,
    ASSESSMENT_DATE,
    ALERT_DATE;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
