package de.symeda.sormas.api.sixtyday;

import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.epidata.EpiDataDto;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.hospitalization.HospitalizationDto;
import de.symeda.sormas.api.symptoms.SymptomState;
import de.symeda.sormas.api.utils.*;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableDto;

import java.util.Date;
import java.util.Set;

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

    public static final String FOOD_AVAILABLE_TESTING = "foodAvailableTesting";
    public static final String LAB_TEST_CONDUCTED = "labTestConducted";
    public static final String SPECIFY_FOODS_SOURCES = "specifyFoodsSources";
    public static final String SPECIFY_SOURCES = "specifySources";
    public static final String PRODUCT_NAME = "productName";
    public static final String BATCH_NUMBER = "batchNumber";
    public static final String DATE_OF_MANUFACTURE = "dateOfManufacture";
    public static final String EXPIRATION_DATE = "expirationDate";
    public static final String PACKAGE_SIZE = "packageSize";
    public static final String PACKAGING_TYPE = "packagingType";
    public static final String PACKAGING_TYPE_OTHER = "packagingTypeOther";
    public static final String PLACE_OF_PURCHASE = "placeOfPurchase";
    public static final String NAME_OF_MANUFACTURER = "nameOfManufacturer";
    public static final String ADDRESS = "address";
    public static final String FOOD_TEL = "foodTel";
    public static final String INVESTIGATION_NOTES = "investigationNotes";
    public static final String SUSPECTED_DIAGNOSIS = "suspectedDiagnosis";
    public static final String CONFIRMED_DIAGNOSIS = "confirmedDiagnosis";
    public static final String INVESTIGATED_BY = "investigatedBy";
    public static final String INVESTIGATOR_SIGNATURE = "investigatorSignature";
    public static final String INVESTIGATOR_DATE = "investigatorDate";
    public static final String SURNAME = "surname";
    public static final String FIRSTNAME = "firstName";
    public static final String MIDDLENAME = "middleName";
    public static final String TEL_NO = "telNo";
    public static final String DATE_OF_COMPLETION_OF_FORM = "dateOfCompletionOfForm";
    public static final String NAME_OF_HEALTH_FACILITY = "nameOfHealthFacility";

    private String personExamineCase;
    private Date dateOfFollowup;
    private Date dateBirth;
    private String residentialLocation;
    private YesNo patientFound;
    private String patientFoundReason;
    private String locateChildAttempt;
    private YesNo paralysisWeaknessPresent;
    private Set<ParalysisSite> paralysisWeaknessPresentSite;
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

    private YesNo foodAvailableTesting;
    private YesNo labTestConducted;
    private String specifyFoodsSources;
    private String specifySources;
    private String productName;
    private String batchNumber;
    private Date dateOfManufacture;
    private Date expirationDate;
    private String packageSize;
    private PackagingType packagingType;
    private String packagingTypeOther;
    private String placeOfPurchase;
    private String nameOfManufacturer;
    private String address;
    private String foodTel;
    private String investigationNotes;
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

    public static SixtyDayDto build() {
        SixtyDayDto sixtyDayDto  = new SixtyDayDto();
        sixtyDayDto .setUuid(DataHelper.createUuid());
        return sixtyDayDto ;
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
    public Set<ParalysisSite> getParalysisWeaknessPresentSite() {
        return paralysisWeaknessPresentSite;
    }

    public void setParalysisWeaknessPresentSite(Set<ParalysisSite> paralysisWeaknessPresentSite) {
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

    public YesNo getFoodAvailableTesting() {
        return foodAvailableTesting;
    }

    public void setFoodAvailableTesting(YesNo foodAvailableTesting) {
        this.foodAvailableTesting = foodAvailableTesting;
    }
    public YesNo getLabTestConducted() {
        return labTestConducted;
    }

    public void setLabTestConducted(YesNo labTestConducted) {
        this.labTestConducted = labTestConducted;
    }
    public String getSpecifyFoodsSources() {
        return specifyFoodsSources;
    }

    public void setSpecifyFoodsSources(String specifyFoodsSources) {
        this.specifyFoodsSources = specifyFoodsSources;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Date getDateOfManufacture() {
        return dateOfManufacture;
    }

    public void setDateOfManufacture(Date dateOfManufacture) {
        this.dateOfManufacture = dateOfManufacture;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(String packageSize) {
        this.packageSize = packageSize;
    }

    public PackagingType getPackagingType() {
        return packagingType;
    }

    public void setPackagingType(PackagingType packagingType) {
        this.packagingType = packagingType;
    }

    public String getPackagingTypeOther() {
        return packagingTypeOther;
    }

    public void setPackagingTypeOther(String packagingTypeOther) {
        this.packagingTypeOther = packagingTypeOther;
    }

    public String getPlaceOfPurchase() {
        return placeOfPurchase;
    }

    public void setPlaceOfPurchase(String placeOfPurchase) {
        this.placeOfPurchase = placeOfPurchase;
    }

    public String getNameOfManufacturer() {
        return nameOfManufacturer;
    }

    public void setNameOfManufacturer(String nameOfManufacturer) {
        this.nameOfManufacturer = nameOfManufacturer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFoodTel() {
        return foodTel;
    }

    public void setFoodTel(String foodTel) {
        this.foodTel = foodTel;
    }

    public String getInvestigationNotes() {
        return investigationNotes;
    }

    public void setInvestigationNotes(String investigationNotes) {
        this.investigationNotes = investigationNotes;
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

    public String getSpecifySources() {
        return specifySources;
    }

    public void setSpecifySources(String specifySources) {
        this.specifySources = specifySources;
    }
}
