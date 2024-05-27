package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.ImportIgnore;
import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.exposure.ExposureDto;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableDto;

import java.util.Date;

public class RiskAssessmentDto extends PseudonymizableDto {

    private static final long serialVersionUID = 2430932452606853497L;

    public static final long APPROXIMATE_JSON_SIZE_IN_BYTES = 13356;

    public static final String I18N_PREFIX = "RiskAssessment";

    public static String MORBIDITY_MORTALITY = "morbidityMortality";

    public static String MORBIDITY_MORTALITY_COMMENT = "morbidityMortalityComment";

    public static String SPREAD_PROBABILITY = "spreadProbability";

    public static String SPREAD_PROBABILITY_COMMENT = "spreadProbabilityComment";

    public static String CONTROL_MEASURES = "controlMeasures";

    public static String CONTROL_MEASURES_COMMENT = "controlMeasuresComment";

    public static String RISK_ASSESSMENT = "riskAssessment";

    public static String RESPONSE_DATE = "responseDate";

    public static String RESPONSE_TIME = "responseTime";

    public static String INFO = "info";
    public static String EBS = "ebs";


    private YesNo morbidityMortality;
    private String morbidityMortalityComment;
    private YesNo spreadProbability;
    private String spreadProbabilityComment;
    private YesNo controlMeasures;
    private String controlMeasuresComment;
    private RiskAssesment riskAssessment;
    private Date responseDate;
    private String responseTime;
    private EbsReferenceDto ebs;

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

    @ImportIgnore
    public  EbsReferenceDto getEbs() {
        return ebs;
    }

    public void setEbs(EbsReferenceDto ebs) {
        this.ebs = ebs;
    }

    private static RiskAssessmentDto getRiskassessmentDto() {

        RiskAssessmentDto riskAssessmentDto = new RiskAssessmentDto();
        riskAssessmentDto.setUuid(DataHelper.createUuid());

        return riskAssessmentDto;
    }

    public static RiskAssessmentDto build( EbsReferenceDto ebsRef) {

        final RiskAssessmentDto riskAssessmentDto = getRiskassessmentDto();
        riskAssessmentDto.setEbs(ebsRef);
        return riskAssessmentDto;
    }
}
