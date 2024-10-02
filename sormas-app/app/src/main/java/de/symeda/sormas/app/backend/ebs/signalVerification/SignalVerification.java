package de.symeda.sormas.app.backend.ebs.signalVerification;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.app.backend.common.EmbeddedAdo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;

@Entity(name = SignalVerification.TABLE_NAME)
@DatabaseTable(tableName = SignalVerification.TABLE_NAME)
@EmbeddedAdo
public class SignalVerification extends PseudonymizableAdo {
    public static final String TABLE_NAME = "signalVerification";
    public static final String I18N_PREFIX = "signalVerification";

    private static final long serialVersionUID = 1L;
    public static final long APPROXIMATE_JSON_SIZE_IN_BYTES = 13356;

    @DatabaseField
    private YesNo verificationSent;
    @DatabaseField
    private SignalOutcome verified;
    @DatabaseField(dataType = DataType.DATE_LONG)
    private Date verificationCompleteDate;
    @DatabaseField(dataType = DataType.DATE_LONG)
    private Date dateOfOccurrence;
    @DatabaseField
    private String numberOfPersonAnimal;
    @DatabaseField
    private String numberOfDeath;
    @DatabaseField
    private String numberOfPersonCases;
    @DatabaseField
    private String numberOfDeathPerson;
    @DatabaseField
    private String description;
    @DatabaseField
    private String whyNotVerify;

    public YesNo getVerificationSent() {
        return verificationSent;
    }

    public void setVerificationSent(YesNo verificationSent) {
        this.verificationSent = verificationSent;
    }
    @Override
    public String getI18nPrefix() {
        return I18N_PREFIX;
    }

    @Enumerated(EnumType.STRING)
    public SignalOutcome getVerified() {
        return verified;
    }

    public void setVerified(SignalOutcome verified) {
        this.verified = verified;
    }

    public Date getVerificationCompleteDate() {
        return verificationCompleteDate;
    }

    public void setVerificationCompleteDate(Date verificationCompleteDate) {
        this.verificationCompleteDate = verificationCompleteDate;
    }

    public Date getDateOfOccurrence() {
        return dateOfOccurrence;
    }

    public void setDateOfOccurrence(Date dateOfOccurrence) {
        this.dateOfOccurrence = dateOfOccurrence;
    }

    public String getNumberOfPersonAnimal() {
        return numberOfPersonAnimal;
    }

    public void setNumberOfPersonAnimal(String numberOfPersonAnimal) {
        this.numberOfPersonAnimal = numberOfPersonAnimal;
    }

    public String getNumberOfDeath() {
        return numberOfDeath;
    }

    public void setNumberOfDeath(String numberOfDeath) {
        this.numberOfDeath = numberOfDeath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWhyNotVerify() {
        return whyNotVerify;
    }

    public void setWhyNotVerify(String whyNotVerify) {
        this.whyNotVerify = whyNotVerify;
    }

    public String getNumberOfPersonCases() {
        return numberOfPersonCases;
    }

    public void setNumberOfPersonCases(String numberOfPersonCases) {
        this.numberOfPersonCases = numberOfPersonCases;
    }

    public String getNumberOfDeathPerson() {
        return numberOfDeathPerson;
    }

    public void setNumberOfDeathPerson(String numberOfDeathPerson) {
        this.numberOfDeathPerson = numberOfDeathPerson;
    }
}
