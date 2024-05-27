package de.symeda.sormas.backend.ebs;

import de.symeda.sormas.api.ebs.RiskAssesment;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.backend.common.AbstractDomainObject;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity(name = "riskAssessment")
public class RiskAssessment extends AbstractDomainObject {

    public static final String DELETED = "deleted";
    private static final long serialVersionUID = 1L;
    public static final long APPROXIMATE_JSON_SIZE_IN_BYTES = 13356;

    public static final String I18N_PREFIX = "RiskAssessment";

    public static final String TABLE_NAME = "riskAssessment";


    public static String MORBIDITY_MORTALITY = "morbidityMortality";

    public static String SPREAD_PROBABILITY = "spreadProbability";

    public static String CONTROL_MEASURES = "controlMeasures";

    public static String RISK_ASSESSMENT = "riskAssessment";

    public static String RESPONSE_DATE = "responseDate";

    public static String RESPONSE_TIME = "responseTime";

    public static String MORBIDITY_MORTALITY_COMMENT = "morbidityMortalityComment";

    public static String SPREAD_PROBABILITY_COMMENT = "spreadProbabilityComment";

    public static String CONTROL_MEASURES_COMMENT = "controlMeasuresComment";

    public static final String EBS = "ebs";


    private YesNo morbidityMortality;
    private YesNo spreadProbability;
    private YesNo controlMeasures;
    private RiskAssesment riskAssessment;
    private Date responseDate;
    private String responseTime;
    private Ebs ebs;
    private String morbidityMortalityComment;
    private String spreadProbabilityComment;
    private String controlMeasuresComment;

    public YesNo getMorbidityMortality() {
        return morbidityMortality;
    }

    public void setMorbidityMortality(YesNo morbidityMortality) {
        this.morbidityMortality = morbidityMortality;
    }

    public YesNo getSpreadProbability() {
        return spreadProbability;
    }

    public void setSpreadProbability(YesNo spreadProbability) {
        this.spreadProbability = spreadProbability;
    }

    public YesNo getControlMeasures() {
        return controlMeasures;
    }

    public void setControlMeasures(YesNo controlMeasures) {
        this.controlMeasures = controlMeasures;
    }

    public RiskAssesment getRiskAssessment() {
        return riskAssessment;
    }

    public void setRiskAssessment(RiskAssesment riskAssessment) {
        this.riskAssessment = riskAssessment;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getControlMeasuresComment() {
        return controlMeasuresComment;
    }

    public void setControlMeasuresComment(String controlMeasuresComment) {
        this.controlMeasuresComment = controlMeasuresComment;
    }

    public String getSpreadProbabilityComment() {
        return spreadProbabilityComment;
    }

    public void setSpreadProbabilityComment(String spreadProbabilityComment) {
        this.spreadProbabilityComment = spreadProbabilityComment;
    }

    public String getMorbidityMortalityComment() {
        return morbidityMortalityComment;
    }

    public void setMorbidityMortalityComment(String morbidityMortalityComment) {
        this.morbidityMortalityComment = morbidityMortalityComment;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    public Ebs getEbs() {
        return ebs;
    }

    public void setEbs(Ebs ebs) {
        this.ebs = ebs;
    }
}
