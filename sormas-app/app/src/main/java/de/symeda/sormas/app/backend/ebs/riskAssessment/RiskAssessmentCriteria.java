package de.symeda.sormas.app.backend.ebs.riskAssessment;

import java.io.Serializable;

public class RiskAssessmentCriteria implements Serializable {
    private Long ebsId;

    public void setEbsId(Long ebsId) {
        this.ebsId = ebsId;
    }

    public Long getEbsId() {
        return ebsId;
    }
}
