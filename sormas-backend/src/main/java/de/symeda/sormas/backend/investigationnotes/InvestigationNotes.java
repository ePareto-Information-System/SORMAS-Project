package de.symeda.sormas.backend.investigationnotes;

import de.symeda.sormas.backend.common.AbstractDomainObject;

import javax.persistence.Entity;
import java.util.Date;
@Entity
public class InvestigationNotes extends AbstractDomainObject {
    private static final long serialVersionUID = -8294812479501735786L;

    public static final String TABLE_NAME = "investigationnotes";
    public static final String INVESTIGATION_NOTES = "investigationNotesData";
    public static final String SUSPECTED_DIAGNOSIS = "suspectedDiagnosis";
    public static final String CONFIRMED_DIAGNOSIS = "confirmedDiagnosis";
    public static final String INVESTIGATED_BY = "investigatedBy";
    public static final String INVESTIGATOR_SIGNATURE = "investigatorSignature";
    public static final String INVESTIGATOR_DATE = "investigatorDate";

    private String investigationNotesData;
    private String suspectedDiagnosis;
    private String confirmedDiagnosis;
    private String investigatedBy;
    private String investigatorSignature;
    private Date investigatorDate;
    private String surname;
    private String firstName;
    private String middleName;
    private String telNo;
    private Date dateOfCompletionOfForm;
    private String nameOfHealthFacility;

    public String getInvestigationNotesData() {
        return investigationNotesData;
    }

    public void setInvestigationNotesData(String investigationNotesData) {
        this.investigationNotesData = investigationNotesData;
    }

    public String getSuspectedDiagnosis() {
        return suspectedDiagnosis;
    }

    public void setSuspectedDiagnosis(String suspectedDiagnosis) {
        this.suspectedDiagnosis = suspectedDiagnosis;
    }

    public String getConfirmedDiagnosis() {
        return confirmedDiagnosis;
    }

    public void setConfirmedDiagnosis(String confirmedDiagnosis) {
        this.confirmedDiagnosis = confirmedDiagnosis;
    }

    public String getInvestigatedBy() {
        return investigatedBy;
    }

    public void setInvestigatedBy(String investigatedBy) {
        this.investigatedBy = investigatedBy;
    }

    public String getInvestigatorSignature() {
        return investigatorSignature;
    }

    public void setInvestigatorSignature(String investigatorSignature) {
        this.investigatorSignature = investigatorSignature;
    }
    public Date getInvestigatorDate() {
        return investigatorDate;
    }

    public void setInvestigatorDate(Date investigatorDate) {
        this.investigatorDate = investigatorDate;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public Date getDateOfCompletionOfForm() {
        return dateOfCompletionOfForm;
    }

    public void setDateOfCompletionOfForm(Date dateOfCompletionOfForm) {
        this.dateOfCompletionOfForm = dateOfCompletionOfForm;
    }

    public String getNameOfHealthFacility() {
        return nameOfHealthFacility;
    }

    public void setNameOfHealthFacility(String nameOfHealthFacility) {
        this.nameOfHealthFacility = nameOfHealthFacility;
    }
}
