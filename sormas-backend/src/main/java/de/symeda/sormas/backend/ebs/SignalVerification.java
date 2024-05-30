package de.symeda.sormas.backend.ebs;

import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.backend.common.AbstractDomainObject;

import javax.persistence.Entity;
import java.util.Date;

@Entity(name = "signalVerification")
public class SignalVerification extends AbstractDomainObject {

    private static final long serialVersionUID = 1L;
    public static final long APPROXIMATE_JSON_SIZE_IN_BYTES = 13356;

    public static final String I18N_PREFIX = "SignalVerification";

    public static final String TABLE_NAME = "signalVerification";


    public static final String VERIFICATION_SENT = "verificationSent";
    public static final String VERIFIED = "verified";
    public static final String VERIFICATION_SENT_DATE = "verificationSentDate";
    public static final String VERIFICATION_COMPLETE_DATE = "verificationCompleteDate";
    public static final String DATE_OF_OCCURRENCE = "dateOfOccurrence";
    public static final String NUMBER_OF_PERSON_ANIMAL = "numberOfPersonAnimal";
    public static final String NUMBER_OF_DEATH = "numberOfDeath";
    public static final String DESCRIPTION = "description";
    public static final String WHY_NOT_VERIFY = "whyNotVerify";

    private YesNo verificationSent;
    private YesNo verified;
    private Date verificationSentDate;
    private Date verificationCompleteDate;
    private Date dateOfOccurrence;
    private String numberOfPersonAnimal;
    private String numberOfDeath;
    private String description;
    private String whyNotVerify;

    public YesNo getVerificationSent() {
        return verificationSent;
    }

    public void setVerificationSent(YesNo verificationSent) {
        this.verificationSent = verificationSent;
    }

    public YesNo getVerified() {
        return verified;
    }

    public void setVerified(YesNo verified) {
        this.verified = verified;
    }

    public Date getVerificationSentDate() {
        return verificationSentDate;
    }

    public void setVerificationSentDate(Date verificationSentDate) {
        this.verificationSentDate = verificationSentDate;
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
}
