package de.symeda.sormas.api.sixtyday;

import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.hospitalization.HospitalizationDto;
import de.symeda.sormas.api.symptoms.SymptomState;
import de.symeda.sormas.api.utils.*;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableDto;

import java.util.Date;

@DependingOnFeatureType(featureType = FeatureType.CASE_SURVEILANCE)
public class SixtyDayDto extends PseudonymizableDto {

    private static final long serialVersionUID = 4846215199480684367L;

    public static final String I18N_PREFIX = "CaseSixtyDayFollowup";
    public static final String PERSON_EXAMINE_CASE = "personExamineCase";
    public static final String DATE_OF_FOLLOWUP = "dateOfFollowup";
    public static final String DATE_BIRTH = "dateBirth";
    public static final String RESIDENTIAL_LOCATION = "residentialLocation";
    public static final String PATIENT_FOUND = "patientFound";
    public static final String PATIENT_FOUND_REASON = "patientFoundReason";
    public static final String LOCATE_CHILD_ATTEMPT = "locateChildAttempt";
    public static final String PARALYSIS_WEAKNESS_PRESENT = "paralysisWeaknessPresent";
    public static final String PARALYSIS_WEAKNESS_PRESENT_SITE = "paralysisWeaknessPresentSite";
    public static final String PARALYZED_PART_OTHER = "paralyzedPartOther";
    public static final String PARALYSIS_WEAKNESS_FLOPPY = "paralysisWeaknessFloppy";
    public static final String PARALYZED_PART = "muscleToneParalyzedPart";
    public static final String OTHER_PART_BODY = "muscleToneOtherPartBody";
    public static final String DEEP_TENDON_REFLEX_SELECTION = "deepTendon";
    public static final String MUSCLE_VOLUME_SELECTION = "muscleVolume";
    public static final String SENSORY_LOSS_SELECTION = "sensoryLoss";
    public static final String PROVISIONAL_DIAGNOSIS = "provisionalDiagnosis";
    public static final String COMMENTS = "comments";
    public static final String CONTACT_DETAILS_NUMBER = "contactDetailsNumber";
    public static final String CONTACT_DETAILS_EMAIL = "contactDetailsEmail";
    public static final String SIGNATURE = "signature";
    public static final String DATE_SUBMISSION_FORMS = "dateSubmissionForms";

    private String personExamineCase;
    private Date dateOfFollowup;
    private Date dateBirth;
    private String residentialLocation;
    private YesNo patientFound;
    private String patientFoundReason;
    private String locateChildAttempt;
    private YesNo paralysisWeaknessPresent;
    private ParalysisSite paralysisWeaknessPresentSite;
    private String paralyzedPartOther;
    private YesNo paralysisWeaknessFloppy;
    private SymptomLevel muscleToneParalyzedPart;
    private SymptomLevel muscleToneOtherPartBody;
    private SymptomLevel deepTendon;
    private NormalWasted muscleVolume;
    private YesNo sensoryLoss;
    private String provisionalDiagnosis;
    private String comments;
    private String contactDetailsNumber;
    private String contactDetailsEmail;
    private String signature;
    private Date dateSubmissionForms;

    public static SixtyDayDto build() {
        SixtyDayDto sixtyDayDto = new SixtyDayDto();
        sixtyDayDto.setUuid(DataHelper.createUuid());
        return sixtyDayDto;
    }

    public String getPersonExamineCase() {
        return personExamineCase;
    }

    public void setPersonExamineCase(String personExamineCase) {
        this.personExamineCase = personExamineCase;
    }

    public Date getDateOfFollowup() {
        return dateOfFollowup;
    }

    public void setDateOfFollowup(Date dateOfFollowup) {
        this.dateOfFollowup = dateOfFollowup;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getResidentialLocation() {
        return residentialLocation;
    }

    public void setResidentialLocation(String residentialLocation) {
        this.residentialLocation = residentialLocation;
    }

    public YesNo getPatientFound() {
        return patientFound;
    }

    public void setPatientFound(YesNo patientFound) {
        this.patientFound = patientFound;
    }

    public String getPatientFoundReason() {
        return patientFoundReason;
    }

    public void setPatientFoundReason(String patientFoundReason) {
        this.patientFoundReason = patientFoundReason;
    }

    public String getLocateChildAttempt() {
        return locateChildAttempt;
    }

    public void setLocateChildAttempt(String locateChildAttempt) {
        this.locateChildAttempt = locateChildAttempt;
    }

    public YesNo getParalysisWeaknessPresent() {
        return paralysisWeaknessPresent;
    }

    public void setParalysisWeaknessPresent(YesNo paralysisWeaknessPresent) {
        this.paralysisWeaknessPresent = paralysisWeaknessPresent;
    }
    public ParalysisSite getParalysisWeaknessPresentSite() {
        return paralysisWeaknessPresentSite;
    }

    public void setParalysisWeaknessPresentSite(ParalysisSite paralysisWeaknessPresentSite) {
        this.paralysisWeaknessPresentSite = paralysisWeaknessPresentSite;
    }

    public String getParalyzedPartOther() {
        return paralyzedPartOther;
    }

    public void setParalyzedPartOther(String paralyzedPartOther) {
        this.paralyzedPartOther = paralyzedPartOther;
    }

    public YesNo getParalysisWeaknessFloppy() {
        return paralysisWeaknessFloppy;
    }

    public void setParalysisWeaknessFloppy(YesNo paralysisWeaknessFloppy) {
        this.paralysisWeaknessFloppy = paralysisWeaknessFloppy;
    }

    public SymptomLevel getMuscleToneParalyzedPart() {
        return muscleToneParalyzedPart;
    }

    public void setMuscleToneParalyzedPart(SymptomLevel muscleToneParalyzedPart) {
        this.muscleToneParalyzedPart = muscleToneParalyzedPart;
    }

    public SymptomLevel getMuscleToneOtherPartBody() {
        return muscleToneOtherPartBody;
    }

    public void setMuscleToneOtherPartBody(SymptomLevel muscleToneOtherPartBody) {
        this.muscleToneOtherPartBody = muscleToneOtherPartBody;
    }

    public SymptomLevel getDeepTendon() {
        return deepTendon;
    }

    public void setDeepTendon(SymptomLevel deepTendon) {
        this.deepTendon = deepTendon;
    }

    public NormalWasted getMuscleVolume() {
        return muscleVolume;
    }

    public void setMuscleVolume(NormalWasted muscleVolume) {
        this.muscleVolume = muscleVolume;
    }

    public YesNo getSensoryLoss() {
        return sensoryLoss;
    }

    public void setSensoryLoss(YesNo sensoryLoss) {
        this.sensoryLoss = sensoryLoss;
    }

    public String getProvisionalDiagnosis() {
        return provisionalDiagnosis;
    }

    public void setProvisionalDiagnosis(String provisionalDiagnosis) {
        this.provisionalDiagnosis = provisionalDiagnosis;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getContactDetailsNumber() {
        return contactDetailsNumber;
    }

    public void setContactDetailsNumber(String contactDetailsNumber) {
        this.contactDetailsNumber = contactDetailsNumber;
    }

    public String getContactDetailsEmail() {
        return contactDetailsEmail;
    }

    public void setContactDetailsEmail(String contactDetailsEmail) {
        this.contactDetailsEmail = contactDetailsEmail;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Date getDateSubmissionForms() {
        return dateSubmissionForms;
    }

    public void setDateSubmissionForms(Date dateSubmissionForms) {
        this.dateSubmissionForms = dateSubmissionForms;
    }


}
