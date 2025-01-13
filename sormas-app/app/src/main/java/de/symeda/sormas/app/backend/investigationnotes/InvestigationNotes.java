package de.symeda.sormas.app.backend.investigationnotes;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.Entity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import de.symeda.sormas.app.backend.common.EmbeddedAdo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;

@Entity(name = InvestigationNotes.TABLE_NAME)
@DatabaseTable(tableName = InvestigationNotes.TABLE_NAME)
@EmbeddedAdo
public class InvestigationNotes extends PseudonymizableAdo {

    private static final long serialVersionUID = -8294812479501735785L;
    public static final String TABLE_NAME = "investigationnotes";
    public static final String I18N_PREFIX = "InvestigationNotes";

    @Column(length = CHARACTER_LIMIT_DEFAULT)
    private String investigationNotesData;
    @Column(length = CHARACTER_LIMIT_DEFAULT)
    private String suspectedDiagnosis;
    @Column(length = CHARACTER_LIMIT_DEFAULT)
    private String confirmedDiagnosis;
    @Column(length = CHARACTER_LIMIT_DEFAULT)
    private String investigatedBy;
    @Column(length = CHARACTER_LIMIT_DEFAULT)
    private String investigatorSignature;
    @DatabaseField(dataType = DataType.DATE_LONG)
    private Date investigatorDate;
    @Column(length = CHARACTER_LIMIT_DEFAULT)
    private String surname;
    @Column(length = CHARACTER_LIMIT_DEFAULT)
    private String firstName;
    @Column(length = CHARACTER_LIMIT_DEFAULT)
    private String middleName;
    @Column(length = CHARACTER_LIMIT_DEFAULT)
    private String telNo;
    @DatabaseField(dataType = DataType.DATE_LONG)
    private Date dateOfCompletionOfForm;
    @Column(length = CHARACTER_LIMIT_DEFAULT)
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

    @Override
    public String getI18nPrefix() {
        return I18N_PREFIX;
    }
}
