package de.symeda.sormas.api.caze;

import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.PathogenTestType;
import de.symeda.sormas.api.utils.SensitiveData;

import java.io.Serializable;
import java.util.Date;

public class EmbeddedPathogenExportDto implements Serializable {

    private String uuid;
    private PathogenTestType testType;
    @SensitiveData
    private String testTypeText;
    private String disease;
    private Date dateTime;
    private String lab;
    private PathogenTestResultType testResult;
    private Boolean verified;

    public EmbeddedPathogenExportDto() {
    }

    public EmbeddedPathogenExportDto(String uuid, PathogenTestType testType, String testTypeText, String disease, Date dateTime, String lab, PathogenTestResultType testResult, Boolean verified) {
        this.uuid = uuid;
        this.testType = testType;
        this.testTypeText = testTypeText;
        this.disease = disease;
        this.dateTime = dateTime;
        this.lab = lab;
        this.testResult = testResult;
        this.verified = verified;
    }

    public String formatString() {
        StringBuilder sb = new StringBuilder();

        sb.append(testTypeText).append(" (").append(disease).append(") ");
        sb.append(dateTime).append(" (");
        if (lab != null && lab.length() > 0) {
            sb.append(lab).append(", ");
        }
        sb.append(testResult).append(")");

        return sb.toString();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public PathogenTestType getTestType() {
        return testType;
    }

    public void setTestType(PathogenTestType testType) {
        this.testType = testType;
    }

    public String getTestTypeText() {
        return testTypeText;
    }

    public void setTestTypeText(String testTypeText) {
        this.testTypeText = testTypeText;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public PathogenTestResultType getTestResult() {
        return testResult;
    }

    public void setTestResult(PathogenTestResultType testResult) {
        this.testResult = testResult;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}
