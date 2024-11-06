package de.symeda.sormas.app.backend.ebs.riskAssessment;

import java.io.Serializable;

import de.symeda.sormas.api.ebs.RiskAssesment;

public class RiskAssessmentCriteria implements Serializable {
    private Long ebsId;
    private RiskAssesment riskAssesment;

    public void setEbsId(Long ebsId) {
        this.ebsId = ebsId;
    }

    public Long getEbsId() {
        return ebsId;
    }

    public void setRiskAssesment(RiskAssesment riskAssesment){
        this.riskAssesment = riskAssesment;
    }
    public RiskAssesment getRiskAssesment(){
        return riskAssesment;
    }

    public RiskAssessmentCriteria riskAssessment(RiskAssesment riskAssesment){
        this.riskAssesment = riskAssesment;
        return this;
    }
}
