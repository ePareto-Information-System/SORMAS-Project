package de.symeda.sormas.api.caze;

import java.io.Serializable;
import java.util.Date;

public class CaseMDDataDto implements Serializable {
    private static final long serialVersionUID = -3544971830146580773L;

    private String caseId;
    private String disease;
    private String caseClassification;
    private String firstName;
    private String lastName;
    private String responsibleDistrict;
    private String sex;
    private String approximateAgeBirthdate;
    private String healthFacility;
    private String dateOfReport;
    private String creationDate;
    private String completeness;
    private String actions;

    // Constructors, getters, and setters

    public CaseMDDataDto() {
    }

    public CaseMDDataDto(String caseId, String disease, String caseClassification, String firstName, String lastName,
                         String approximateAgeBirthdate, String sex, String responsibleDistrict, String healthFacility,
                         String dateOfReport, String creationDate, String completeness, String actions) {
        this.caseId = caseId;
        this.disease = disease;
        this.caseClassification = caseClassification;
        this.firstName = firstName;
        this.lastName = lastName;

        this.approximateAgeBirthdate = approximateAgeBirthdate;
        this.sex = sex;
        this.responsibleDistrict = responsibleDistrict;
        this.healthFacility = healthFacility;
        this.dateOfReport = dateOfReport;
        this.creationDate = creationDate;
        this.completeness = completeness;
        this.actions = actions;
    }

    // Getters and setters

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getCaseClassification() {
        return caseClassification;
    }

    public void setCaseClassification(String caseClassification) {
        this.caseClassification = caseClassification;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getApproximateAgeBirthdate() {
        return approximateAgeBirthdate;
    }

    public void setApproximateAgeBirthdate(String approximateAgeBirthdate) {
        this.approximateAgeBirthdate = approximateAgeBirthdate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getResponsibleDistrict() {
        return responsibleDistrict;
    }

    public void setResponsibleDistrict(String responsibleDistrict) {
        this.responsibleDistrict = responsibleDistrict;
    }

    public String getHealthFacility() {
        return healthFacility;
    }

    public void setHealthFacility(String healthFacility) {
        this.healthFacility = healthFacility;
    }

    public String getDateOfReport() {
        return dateOfReport;
    }

    public void setDateOfReport(String dateOfReport) {
        this.dateOfReport = dateOfReport;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCompleteness() {
        return completeness;
    }

    public void setCompleteness(String completeness) {
        this.completeness = completeness;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }
}

