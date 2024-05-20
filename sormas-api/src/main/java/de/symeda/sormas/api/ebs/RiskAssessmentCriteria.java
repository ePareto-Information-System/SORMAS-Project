package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.utils.criteria.BaseCriteria;
import de.symeda.sormas.api.utils.criteria.CriteriaDateType;
import de.symeda.sormas.api.utils.criteria.CriteriaWithDateType;

import java.io.Serializable;

public class RiskAssessmentCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = -4649293670201029461L;

    public static final String EBS = "ebs";


    private EbsReferenceDto ebs;

    public EbsReferenceDto getEbs() {
        return ebs;
    }

    public RiskAssessmentCriteria Ebs(EbsReferenceDto ebs) {
        this.ebs = ebs;
        return this;
    }
}
