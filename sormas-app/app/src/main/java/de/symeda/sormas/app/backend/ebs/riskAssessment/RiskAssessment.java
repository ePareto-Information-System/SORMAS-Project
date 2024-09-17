package de.symeda.sormas.app.backend.ebs.riskAssessment;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import de.symeda.sormas.api.ebs.RiskAssesment;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.ebs.Ebs;

@Entity(name = RiskAssessment.TABLE_NAME)
@DatabaseTable(tableName = RiskAssessment.TABLE_NAME)
public class RiskAssessment extends PseudonymizableAdo {
    private static final long serialVersionUID = 1L;
    public static final long APPROXIMATE_JSON_SIZE_IN_BYTES = 13356;

    public static final String I18N_PREFIX = "RiskAssessment";
    public static final String TABLE_NAME = "riskAssessment";


    @DatabaseField
    private YesNo morbidityMortality;
    @DatabaseField
    private YesNo spreadProbability;
    @DatabaseField
    private YesNo controlMeasures;
    @DatabaseField
    private RiskAssesment riskAssessment;
    @DatabaseField(dataType = DataType.DATE_LONG)
    private Date assessmentDate;
    @DatabaseField
    private String assessmentTime;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Ebs ebs;
    @DatabaseField
    private String morbidityMortalityComment;
    @DatabaseField
    private String spreadProbabilityComment;
    @DatabaseField
    private String controlMeasuresComment;


    @Override
    public String getI18nPrefix() {
        return I18N_PREFIX;
    }

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

    public Date getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(Date responseDate) {
        this.assessmentDate = responseDate;
    }

    public String getAssessmentTime() {
        return assessmentTime;
    }

    public void setAssessmentTime(String responseTime) {
        this.assessmentTime = responseTime;
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