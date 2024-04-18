
package de.symeda.sormas.api.caze;

import de.symeda.sormas.api.CountryHelper;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.clinicalcourse.HealthConditionsDto;
import de.symeda.sormas.api.contact.FollowUpStatus;
import de.symeda.sormas.api.contact.QuarantineType;
import de.symeda.sormas.api.disease.DiseaseVariant;
import de.symeda.sormas.api.epidata.EpiDataDto;
import de.symeda.sormas.api.event.EventStatus;
import de.symeda.sormas.api.hospitalization.HospitalizationDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.immunization.ImmunizationDto;
import de.symeda.sormas.api.importexport.*;
import de.symeda.sormas.api.infrastructure.InfrastructureHelper;
import de.symeda.sormas.api.infrastructure.facility.FacilityHelper;
import de.symeda.sormas.api.infrastructure.facility.FacilityType;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.api.person.*;
import de.symeda.sormas.api.person.ApproximateAgeType.ApproximateAgeHelper;
import de.symeda.sormas.api.sample.*;
import de.symeda.sormas.api.symptoms.SymptomsDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.user.UserRoleReferenceDto;
import de.symeda.sormas.api.utils.*;
import de.symeda.sormas.api.utils.pseudonymization.Pseudonymizer;
import de.symeda.sormas.api.utils.pseudonymization.valuepseudonymizers.EmptyValuePseudonymizer;
import de.symeda.sormas.api.utils.pseudonymization.valuepseudonymizers.PostalCodePseudonymizer;
import de.symeda.sormas.api.vaccination.VaccinationDto;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * A DTO class that contains the properties that are exported during a detailed case export. These
 * properties are also those that users can select when creating a custom export configuration.
 * <p>
 * PLEASE NOTE: When the @ExportProperty value of one of these properties changes, it's necessary
 * to replace all occurrences of the former value in all export configurations in the database.
 * Otherwise, existing export configurations will no longer export the property. Also, it is
 * recommended to remove properties that are removed from this file from existing export configurations.
 */
@ExportEntity(CaseDataDto.class)
public class CaseExportDetailedSampleDto implements Serializable {

    private static final long serialVersionUID = 8581579464816945555L;

    public static final String I18N_PREFIX = "CaseExport";

    public static final String ID = "id";
    public static final String COUNTRY = "country";
    public static final String BIRTH_DATE = "birthdate";
    public static final String AGE_GROUP = "ageGroup";
    public static final String INITIAL_DETECTION_PLACE = "initialDetectionPlace";
    public static final String MAX_SOURCE_CASE_CLASSIFICATION = "maxSourceCaseClassification";
    public static final String ASSOCIATED_WITH_OUTBREAK = "associatedWithOutbreak";
    public static final String BURIAL_INFO = "burialInfo";
    public static final String ADDRESS_GPS_COORDINATES = "addressGpsCoordinates";
    public static final String BURIAL_ATTENDED = "burialAttended";
    public static final String TRAVELED = "traveled";
    public static final String TRAVEL_HISTORY = "travelHistory";
    public static final String NUMBER_OF_PRESCRIPTIONS = "numberOfPrescriptions";
    public static final String NUMBER_OF_TREATMENTS = "numberOfTreatments";
    public static final String NUMBER_OF_CLINICAL_VISITS = "numberOfClinicalVisits";
    public static final String SAMPLE_INFORMATION = "sampleInformation";
    public static final String QUARANTINE_INFORMATION = "quarantineInformation";
    public static final String NUMBER_OF_VISITS = "numberOfVisits";
    public static final String LAST_COOPERATIVE_VISIT_SYMPTOMATIC = "lastCooperativeVisitSymptomatic";
    public static final String LAST_COOPERATIVE_VISIT_DATE = "lastCooperativeVisitDate";
    public static final String LAST_COOPERATIVE_VISIT_SYMPTOMS = "lastCooperativeVisitSymptoms";
    public static final String FACILITY = "facility";
    public static final String EVENT_COUNT = "eventCount";
    public static final String LATEST_EVENT_ID = "latestEventId";
    public static final String LATEST_EVENT_STATUS = "latestEventStatus";
    public static final String LATEST_EVENT_TITLE = "latestEventTitle";

    private String country;
    private long id;
    private long personId;
    private long epiDataId;
    private long hospitalizationId;
    private long symptomsId;
    private long healthConditionsId;
    private String uuid;
    private String epidNumber;
    private Disease disease;
    private String diseaseDetails;
    private DiseaseVariant diseaseVariant;
    private String diseaseVariantDetails;
    private String personUuid;
    @PersonalData
    @SensitiveData
    private String firstName;
    @PersonalData
    @SensitiveData
    private String lastName;
    private Salutation salutation;
    @SensitiveData
    private String otherSalutation;
    private Sex sex;
    private YesNoUnknown pregnant;
    private String approximateAge;
    private String ageGroup;
    private BirthDateDto birthdate;
    private Date reportDate;
    private String region;
    private String district;
    @PersonalData
    @SensitiveData
    private String community;
    private FacilityType facilityType;
    @PersonalData
    @SensitiveData
    private String healthFacility;
    @PersonalData
    @SensitiveData
    private String healthFacilityDetails;
    @PersonalData
    @SensitiveData
    private String pointOfEntry;
    @PersonalData
    @SensitiveData
    private String pointOfEntryDetails;
    private CaseClassification caseClassification;
    private YesNoUnknown clinicalConfirmation;
    private YesNoUnknown epidemiologicalConfirmation;
    private YesNoUnknown laboratoryDiagnosticConfirmation;
    private Boolean notACaseReasonNegativeTest;
    private Boolean notACaseReasonPhysicianInformation;
    private Boolean notACaseReasonDifferentPathogen;
    private Boolean notACaseReasonOther;
    private String notACaseReasonDetails;
    private CaseIdentificationSource caseIdentificationSource;
    private ScreeningType screeningType;
    private InvestigationStatus investigationStatus;
    private Date investigatedDate;
    private CaseClassification maxSourceCaseClassification;
    private CaseOutcome outcome;
    private Date outcomeDate;
    private YesNoUnknown sequelae;
    @SensitiveData
    private String sequelaeDetails;
    private YesNoUnknown bloodOrganOrTissueDonated;
    private String associatedWithOutbreak;
    private YesNoUnknown admittedToHealthFacility;
    private Date admissionDate;
    private Date dischargeDate;
    private YesNoUnknown leftAgainstAdvice;
    @SensitiveData
    private String initialDetectionPlace;
    private PresentCondition presentCondition;
    private Date deathDate;
    private BurialInfoDto burialInfo;
    private String addressRegion;
    private String addressDistrict;
    @PersonalData
    @SensitiveData
    private String addressCommunity;
    @PersonalData
    @SensitiveData
    private String city;
    @PersonalData
    @SensitiveData
    private String street;
    @PersonalData
    @SensitiveData
    private String houseNumber;
    @PersonalData
    @SensitiveData
    private String additionalInformation;
    @PersonalData
    @SensitiveData
    @Pseudonymizer(PostalCodePseudonymizer.class)
    private String postalCode;
    @PersonalData
    @SensitiveData
    private String addressGpsCoordinates;
    @PersonalData
    @SensitiveData
    private String facility;
    @PersonalData
    @SensitiveData
    private String facilityDetails;
    @SensitiveData
    private String phone;
    @SensitiveData
    private String phoneOwner;
    @SensitiveData
    private String emailAddress;
    @SensitiveData
    private String otherContactDetails;
    private OccupationType occupationType;
    @SensitiveData
    private String occupationDetails;
    private ArmedForcesRelationType armedForcesRelationType;
    private EducationType educationType;
    @SensitiveData
    private String educationDetails;
    private String travelHistory;
    private boolean traveled;
    private boolean burialAttended;
    private YesNoUnknown contactWithSourceCaseKnown;
    private SymptomsDto symptoms;
    //	private Date onsetDate;
//	private String symptoms;
    private VaccinationStatus vaccinationStatus;
    private String numberOfDoses;
    private VaccinationInfoSource vaccinationInfoSource;
    private Date firstVaccinationDate;
    private Date lastVaccinationDate;
    private Vaccine vaccineName;
    private String otherVaccineName;
    private VaccineManufacturer vaccineManufacturer;
    private String otherVaccineManufacturer;
    private String vaccineInn;
    private String vaccineBatchNumber;
    private String vaccineUniiCode;
    private String vaccineAtcCode;
    private HealthConditionsDto healthConditions;
    private int numberOfPrescriptions;
    private int numberOfTreatments;
    private int numberOfClinicalVisits;
    private Boolean nosocomialOutbreak;
    private InfectionSetting infectionSetting;

    private YesNoUnknown prohibitionToWork;
    private Date prohibitionToWorkFrom;
    private Date prohibitionToWorkUntil;

    private YesNoUnknown reInfection;
    private Date previousInfectionDate;
    private ReinfectionStatus reinfectionStatus;
    private String reinfectionDetails;

    private QuarantineType quarantine;
    @SensitiveData
    private String quarantineTypeDetails;
    private Date quarantineFrom;
    private Date quarantineTo;
    @SensitiveData
    private String quarantineHelpNeeded;
    private boolean quarantineOrderedVerbally;
    private boolean quarantineOrderedOfficialDocument;
    private Date quarantineOrderedVerballyDate;
    private Date quarantineOrderedOfficialDocumentDate;
    private boolean quarantineExtended;
    private boolean quarantineReduced;
    private boolean quarantineOfficialOrderSent;
    private Date quarantineOfficialOrderSentDate;

    private YesNoUnknown postpartum;
    private Trimester trimester;

    private FollowUpStatus followUpStatus;
    private Date followUpUntil;
    private int numberOfVisits;
    private YesNoUnknown lastCooperativeVisitSymptomatic;
    private Date lastCooperativeVisitDate;
    private String lastCooperativeVisitSymptoms;

    private Long eventCount;
    private String latestEventId;
    private String latestEventTitle;
    private EventStatus latestEventStatus;
    private String externalID;
    private String externalToken;
    private String internalToken;

    @PersonalData
    @SensitiveData
    private String birthName;
    private String birthCountry;
    private String citizenship;

    private String responsibleRegion;
    private String responsibleDistrict;
    private String responsibleCommunity;

    @SensitiveData
    @DependingOnUserRight(UserRight.CASE_CLINICIAN_VIEW)
    private String clinicianName;
    @SensitiveData
    @DependingOnUserRight(UserRight.CASE_CLINICIAN_VIEW)
    private String clinicianPhone;
    @SensitiveData
    @DependingOnUserRight(UserRight.CASE_CLINICIAN_VIEW)
    private String clinicianEmail;

    private Long reportingUserId;
    private Long followUpStatusChangeUserId;

    private String reportingUserName;
    private String reportingUserRoles;
    private String followUpStatusChangeUserName;
    private String followUpStatusChangeUserRoles;
    private Date previousQuarantineTo;
    @SensitiveData
    private String quarantineChangeComment;

    private Boolean isInJurisdiction;

    private String sampleUuid;

    private String labSampleId;
    private Date sampleReportDate;
    private  Date sampleDateTime;
    private SampleMaterial sampleMaterial;
    private String sampleMaterialDetails;

    private String sampleMaterialString;
    @EmbeddedPersonalData
    @EmbeddedSensitiveData
    @Pseudonymizer(EmptyValuePseudonymizer.class)
    private SampleExportMaterial sampleSampleExportMaterial;
    private String samplePurpose;
    private SampleSource sampleSource;
    private SamplingReason samplingReason;
    private String samplingReasonDetails;
    private String laboratory;
    private PathogenTestResultType pathogenTestResult;
    private Boolean pathogenTestingRequested;
    private Set<PathogenTestType> requestedPathogenTests;
    private String requestedOtherPathogenTests;
    private Boolean additionalTestingRequested;
    private Set<AdditionalTestType> requestedAdditionalTests;
    private String requestedOtherAdditionalTests;
    private boolean shipped;
    private Date shipmentDate;
    private String shipmentDetails;
    private boolean received;
    private Date receivedDate;
    private SpecimenCondition specimenCondition;
    private String noTestPossibleReason;
    private String comment;

    private String afpAntiBodyDetection;
    private String afpAntigenDetection;
    private String afpRapidTest;
    private String afpCulture;
    private String afpHistopathology;
    private String afpIsolation;
    private String afpIgmSerumAntibody;
    private String afpIggSerumAntibody;
    private String afpIgaSerumAntibody;
    private String afpIncubationTime;
    private String afpIndirectFluorescentAntibody;
    private String afpMicroscopy;
    private String afpNeutralizingAntibodies;
    private String afpPcr;
    private String afpGramStain;
    private String afpLatexAgglutination;
    private String afpCqValueDetection;
    private String afpSeQuencing;
    private String afpDnaMicroArray;
    private String afpOther;

    private String choleraAntiBodyDetection;
    private String choleraAntigenDetection;
    private String choleraRapidTest;
    private String choleraCulture;
    private String choleraHistopathology;
    private String choleraIsolation;
    private String choleraIgmSerumAntibody;
    private String choleraIggSerumAntibody;
    private String choleraIgaSerumAntibody;
    private String choleraIncubationTime;
    private String choleraIndirectFluorescentAntibody;
    private String choleraMicroscopy;
    private String choleraNeutralizingAntibodies;
    private String choleraPcr;
    private String choleraGramStain;
    private String choleraLatexAgglutination;
    private String choleraCqValueDetection;
    private String choleraSeQuencing;
    private String choleraDnaMicroArray;
    private String choleraOther;

    private String congenitalRubellaAntiBodyDetection;
    private String congenitalRubellaAntigenDetection;
    private String congenitalRubellaRapidTest;
    private String congenitalRubellaCulture;
    private String congenitalRubellaHistopathology;
    private String congenitalRubellaIsolation;
    private String congenitalRubellaIgmSerumAntibody;
    private String congenitalRubellaIggSerumAntibody;
    private String congenitalRubellaIgaSerumAntibody;
    private String congenitalRubellaIncubationTime;
    private String congenitalRubellaIndirectFluorescentAntibody;
    private String congenitalRubellaMicroscopy;
    private String congenitalRubellaNeutralizingAntibodies;
    private String congenitalRubellaPcr;
    private String congenitalRubellaGramStain;
    private String congenitalRubellaLatexAgglutination;
    private String congenitalRubellaCqValueDetection;
    private String congenitalRubellaSeQuencing;
    private String congenitalRubellaDnaMicroArray;
    private String congenitalRubellaOther;

    private String csmAntiBodyDetection;
    private String csmAntigenDetection;
    private String csmRapidTest;
    private String csmCulture;
    private String csmHistopathology;
    private String csmIsolation;
    private String csmIgmSerumAntibody;
    private String csmIggSerumAntibody;
    private String csmIgaSerumAntibody;
    private String csmIncubationTime;
    private String csmIndirectFluorescentAntibody;
    private String csmMicroscopy;
    private String csmNeutralizingAntibodies;
    private String csmPcr;
    private String csmGramStain;
    private String csmLatexAgglutination;
    private String csmCqValueDetection;
    private String csmSeQuencing;
    private String csmDnaMicroArray;
    private String csmOther;

    private String dengueAntiBodyDetection;
    private String dengueAntigenDetection;
    private String dengueRapidTest;
    private String dengueCulture;
    private String dengueHistopathology;
    private String dengueIsolation;
    private String dengueIgmSerumAntibody;
    private String dengueIggSerumAntibody;
    private String dengueIgaSerumAntibody;
    private String dengueIncubationTime;
    private String dengueIndirectFluorescentAntibody;
    private String dengueMicroscopy;
    private String dengueNeutralizingAntibodies;
    private String denguePcr;
    private String dengueGramStain;
    private String dengueLatexAgglutination;
    private String dengueCqValueDetection;
    private String dengueSeQuencing;
    private String dengueDnaMicroArray;
    private String dengueOther;

    private String evdAntiBodyDetection;
    private String evdAntigenDetection;
    private String evdRapidTest;
    private String evdCulture;
    private String evdHistopathology;
    private String evdIsolation;
    private String evdIgmSerumAntibody;
    private String evdIggSerumAntibody;
    private String evdIgaSerumAntibody;
    private String evdIncubationTime;
    private String evdIndirectFluorescentAntibody;
    private String evdMicroscopy;
    private String evdNeutralizingAntibodies;
    private String evdPcr;
    private String evdGramStain;
    private String evdLatexAgglutination;
    private String evdCqValueDetection;
    private String evdSeQuencing;
    private String evdDnaMicroArray;
    private String evdOther;
    private String guineaWormAntiBodyDetection;
    private String guineaWormAntigenDetection;
    private String guineaWormRapidTest;
    private String guineaWormCulture;
    private String guineaWormHistopathology;
    private String guineaWormIsolation;
    private String guineaWormIgmSerumAntibody;
    private String guineaWormIggSerumAntibody;
    private String guineaWormIgaSerumAntibody;
    private String guineaWormIncubationTime;
    private String guineaWormIndirectFluorescentAntibody;
    private String guineaWormMicroscopy;
    private String guineaWormNeutralizingAntibodies;
    private String guineaWormPcr;
    private String guineaWormGramStain;
    private String guineaWormLatexAgglutination;
    private String guineaWormCqValueDetection;
    private String guineaWormSeQuencing;
    private String guineaWormDnaMicroArray;
    private String guineaWormOther;
    private String lassaAntiBodyDetection;
    private String lassaAntigenDetection;
    private String lassaRapidTest;
    private String lassaCulture;
    private String lassaHistopathology;
    private String lassaIsolation;
    private String lassaIgmSerumAntibody;
    private String lassaIggSerumAntibody;
    private String lassaIgaSerumAntibody;
    private String lassaIncubationTime;
    private String lassaIndirectFluorescentAntibody;
    private String lassaMicroscopy;
    private String lassaNeutralizingAntibodies;
    private String lassaPcr;
    private String lassaGramStain;
    private String lassaLatexAgglutination;
    private String lassaCqValueDetection;
    private String lassaSeQuencing;
    private String lassaDnaMicroArray;
    private String lassaOther;
    private String measlesAntiBodyDetection;
    private String measlesAntigenDetection;
    private String measlesRapidTest;
    private String measlesCulture;
    private String measlesHistopathology;
    private String measlesIsolation;
    private String measlesIgmSerumAntibody;
    private String measlesIggSerumAntibody;
    private String measlesIgaSerumAntibody;
    private String measlesIncubationTime;
    private String measlesIndirectFluorescentAntibody;
    private String measlesMicroscopy;
    private String measlesNeutralizingAntibodies;
    private String measlesPcr;
    private String measlesGramStain;
    private String measlesLatexAgglutination;
    private String measlesCqValueDetection;
    private String measlesSeQuencing;
    private String measlesDnaMicroArray;
    private String measlesOther;
    private String monkeyPoxAntiBodyDetection;
    private String monkeyPoxAntigenDetection;
    private String monkeyPoxRapidTest;
    private String monkeyPoxCulture;
    private String monkeyPoxHistopathology;
    private String monkeyPoxIsolation;
    private String monkeyPoxIgmSerumAntibody;
    private String monkeyPoxIggSerumAntibody;
    private String monkeyPoxIgaSerumAntibody;
    private String monkeyPoxIncubationTime;
    private String monkeyPoxIndirectFluorescentAntibody;
    private String monkeyPoxMicroscopy;
    private String monkeyPoxNeutralizingAntibodies;
    private String monkeyPoxPcr;
    private String monkeyPoxGramStain;
    private String monkeyPoxLatexAgglutination;
    private String monkeyPoxCqValueDetection;
    private String monkeyPoxSeQuencing;
    private String monkeyPoxDnaMicroArray;
    private String monkeyPoxOther;
    private String monkeypoxAntiBodyDetection;
    private String monkeypoxAntigenDetection;
    private String monkeypoxRapidTest;
    private String monkeypoxCulture;
    private String monkeypoxHistopathology;
    private String monkeypoxIsolation;
    private String monkeypoxIgmSerumAntibody;
    private String monkeypoxIggSerumAntibody;
    private String monkeypoxIgaSerumAntibody;
    private String monkeypoxIncubationTime;
    private String monkeypoxIndirectFluorescentAntibody;
    private String monkeypoxMicroscopy;
    private String monkeypoxNeutralizingAntibodies;
    private String monkeypoxPcr;
    private String monkeypoxGramStain;
    private String monkeypoxLatexAgglutination;
    private String monkeypoxCqValueDetection;
    private String monkeypoxSeQuencing;
    private String monkeypoxDnaMicroArray;
    private String monkeypoxOther;
    private String newInfluenzaAntiBodyDetection;
    private String newInfluenzaAntigenDetection;
    private String newInfluenzaRapidTest;
    private String newInfluenzaCulture;
    private String newInfluenzaHistopathology;
    private String newInfluenzaIsolation;
    private String newInfluenzaIgmSerumAntibody;
    private String newInfluenzaIggSerumAntibody;
    private String newInfluenzaIgaSerumAntibody;
    private String newInfluenzaIncubationTime;
    private String newInfluenzaIndirectFluorescentAntibody;
    private String newInfluenzaMicroscopy;
    private String newInfluenzaNeutralizingAntibodies;
    private String newInfluenzaPcr;
    private String newInfluenzaGramStain;
    private String newInfluenzaLatexAgglutination;
    private String newInfluenzaCqValueDetection;
    private String newInfluenzaSeQuencing;
    private String newInfluenzaDnaMicroArray;
    private String newInfluenzaOther;
    private String plagueAntiBodyDetection;
    private String plagueAntigenDetection;
    private String plagueRapidTest;
    private String plagueCulture;
    private String plagueHistopathology;
    private String plagueIsolation;
    private String plagueIgmSerumAntibody;
    private String plagueIggSerumAntibody;
    private String plagueIgaSerumAntibody;
    private String plagueIncubationTime;
    private String plagueIndirectFluorescentAntibody;
    private String plagueMicroscopy;
    private String plagueNeutralizingAntibodies;
    private String plaguePcr;
    private String plagueGramStain;
    private String plagueLatexAgglutination;
    private String plagueCqValueDetection;
    private String plagueSeQuencing;
    private String plagueDnaMicroArray;
    private String plagueOther;
    private String polioAntiBodyDetection;
    private String polioAntigenDetection;
    private String polioRapidTest;
    private String polioCulture;
    private String polioHistopathology;
    private String polioIsolation;
    private String polioIgmSerumAntibody;
    private String polioIggSerumAntibody;
    private String polioIgaSerumAntibody;
    private String polioIncubationTime;
    private String polioIndirectFluorescentAntibody;
    private String polioMicroscopy;
    private String polioNeutralizingAntibodies;
    private String polioPcr;
    private String polioGramStain;
    private String polioLatexAgglutination;
    private String polioCqValueDetection;
    private String polioSeQuencing;
    private String polioDnaMicroArray;
    private String polioOther;
    private String unspecifiedVhfAntiBodyDetection;
    private String unspecifiedVhfAntigenDetection;
    private String unspecifiedVhfRapidTest;
    private String unspecifiedVhfCulture;
    private String unspecifiedVhfHistopathology;
    private String unspecifiedVhfIsolation;
    private String unspecifiedVhfIgmSerumAntibody;
    private String unspecifiedVhfIggSerumAntibody;
    private String unspecifiedVhfIgaSerumAntibody;
    private String unspecifiedVhfIncubationTime;
    private String unspecifiedVhfIndirectFluorescentAntibody;
    private String unspecifiedVhfMicroscopy;
    private String unspecifiedVhfNeutralizingAntibodies;
    private String unspecifiedVhfPcr;
    private String unspecifiedVhfGramStain;
    private String unspecifiedVhfLatexAgglutination;
    private String unspecifiedVhfCqValueDetection;
    private String unspecifiedVhfSeQuencing;
    private String unspecifiedVhfDnaMicroArray;
    private String unspecifiedVhfOther;
    private String yellowFeverAntiBodyDetection;
    private String yellowFeverAntigenDetection;
    private String yellowFeverRapidTest;
    private String yellowFeverCulture;
    private String yellowFeverHistopathology;
    private String yellowFeverIsolation;
    private String yellowFeverIgmSerumAntibody;
    private String yellowFeverIggSerumAntibody;
    private String yellowFeverIgaSerumAntibody;
    private String yellowFeverIncubationTime;
    private String yellowFeverIndirectFluorescentAntibody;
    private String yellowFeverMicroscopy;
    private String yellowFeverNeutralizingAntibodies;
    private String yellowFeverPcr;
    private String yellowFeverGramStain;
    private String yellowFeverLatexAgglutination;
    private String yellowFeverCqValueDetection;
    private String yellowFeverSeQuencing;
    private String yellowFeverDnaMicroArray;
    private String yellowFeverOther;
    private String rabiesAntiBodyDetection;
    private String rabiesAntigenDetection;
    private String rabiesRapidTest;
    private String rabiesCulture;
    private String rabiesHistopathology;
    private String rabiesIsolation;
    private String rabiesIgmSerumAntibody;
    private String rabiesIggSerumAntibody;
    private String rabiesIgaSerumAntibody;
    private String rabiesIncubationTime;
    private String rabiesIndirectFluorescentAntibody;
    private String rabiesMicroscopy;
    private String rabiesNeutralizingAntibodies;
    private String rabiesPcr;
    private String rabiesGramStain;
    private String rabiesLatexAgglutination;
    private String rabiesCqValueDetection;
    private String rabiesSeQuencing;
    private String rabiesDnaMicroArray;
    private String rabiesOther;
    private String anthraxAntiBodyDetection;
    private String anthraxAntigenDetection;
    private String anthraxRapidTest;
    private String anthraxCulture;
    private String anthraxHistopathology;
    private String anthraxIsolation;
    private String anthraxIgmSerumAntibody;
    private String anthraxIggSerumAntibody;
    private String anthraxIgaSerumAntibody;
    private String anthraxIncubationTime;
    private String anthraxIndirectFluorescentAntibody;
    private String anthraxMicroscopy;
    private String anthraxNeutralizingAntibodies;
    private String anthraxPcr;
    private String anthraxGramStain;
    private String anthraxLatexAgglutination;
    private String anthraxCqValueDetection;
    private String anthraxSeQuencing;
    private String anthraxDnaMicroArray;
    private String anthraxOther;
    private String coronavirusAntiBodyDetection;
    private String coronavirusAntigenDetection;
    private String coronavirusRapidTest;
    private String coronavirusCulture;
    private String coronavirusHistopathology;
    private String coronavirusIsolation;
    private String coronavirusIgmSerumAntibody;
    private String coronavirusIggSerumAntibody;
    private String coronavirusIgaSerumAntibody;
    private String coronavirusIncubationTime;
    private String coronavirusIndirectFluorescentAntibody;
    private String coronavirusMicroscopy;
    private String coronavirusNeutralizingAntibodies;
    private String coronavirusPcr;
    private String coronavirusGramStain;
    private String coronavirusLatexAgglutination;
    private String coronavirusCqValueDetection;
    private String coronavirusSeQuencing;
    private String coronavirusDnaMicroArray;
    private String coronavirusOther;
    private String pneumoniaAntiBodyDetection;
    private String pneumoniaAntigenDetection;
    private String pneumoniaRapidTest;
    private String pneumoniaCulture;
    private String pneumoniaHistopathology;
    private String pneumoniaIsolation;
    private String pneumoniaIgmSerumAntibody;
    private String pneumoniaIggSerumAntibody;
    private String pneumoniaIgaSerumAntibody;
    private String pneumoniaIncubationTime;
    private String pneumoniaIndirectFluorescentAntibody;
    private String pneumoniaMicroscopy;
    private String pneumoniaNeutralizingAntibodies;
    private String pneumoniaPcr;
    private String pneumoniaGramStain;
    private String pneumoniaLatexAgglutination;
    private String pneumoniaCqValueDetection;
    private String pneumoniaSeQuencing;
    private String pneumoniaDnaMicroArray;
    private String pneumoniaOther;
    private String malariaAntiBodyDetection;
    private String malariaAntigenDetection;
    private String malariaRapidTest;
    private String malariaCulture;
    private String malariaHistopathology;
    private String malariaIsolation;
    private String malariaIgmSerumAntibody;
    private String malariaIggSerumAntibody;
    private String malariaIgaSerumAntibody;
    private String malariaIncubationTime;
    private String malariaIndirectFluorescentAntibody;
    private String malariaMicroscopy;
    private String malariaNeutralizingAntibodies;
    private String malariaPcr;
    private String malariaGramStain;
    private String malariaLatexAgglutination;
    private String malariaCqValueDetection;
    private String malariaSeQuencing;
    private String malariaDnaMicroArray;
    private String malariaOther;
    private String typhoidFeverAntiBodyDetection;
    private String typhoidFeverAntigenDetection;
    private String typhoidFeverRapidTest;
    private String typhoidFeverCulture;
    private String typhoidFeverHistopathology;
    private String typhoidFeverIsolation;
    private String typhoidFeverIgmSerumAntibody;
    private String typhoidFeverIggSerumAntibody;
    private String typhoidFeverIgaSerumAntibody;
    private String typhoidFeverIncubationTime;
    private String typhoidFeverIndirectFluorescentAntibody;
    private String typhoidFeverMicroscopy;
    private String typhoidFeverNeutralizingAntibodies;
    private String typhoidFeverPcr;
    private String typhoidFeverGramStain;
    private String typhoidFeverLatexAgglutination;
    private String typhoidFeverCqValueDetection;
    private String typhoidFeverSeQuencing;
    private String typhoidFeverDnaMicroArray;
    private String typhoidFeverOther;
    private String acuteViralHepatitisAntiBodyDetection;
    private String acuteViralHepatitisAntigenDetection;
    private String acuteViralHepatitisRapidTest;
    private String acuteViralHepatitisCulture;
    private String acuteViralHepatitisHistopathology;
    private String acuteViralHepatitisIsolation;
    private String acuteViralHepatitisIgmSerumAntibody;
    private String acuteViralHepatitisIggSerumAntibody;
    private String acuteViralHepatitisIgaSerumAntibody;
    private String acuteViralHepatitisIncubationTime;
    private String acuteViralHepatitisIndirectFluorescentAntibody;
    private String acuteViralHepatitisMicroscopy;
    private String acuteViralHepatitisNeutralizingAntibodies;
    private String acuteViralHepatitisPcr;
    private String acuteViralHepatitisGramStain;
    private String acuteViralHepatitisLatexAgglutination;
    private String acuteViralHepatitisCqValueDetection;
    private String acuteViralHepatitisSeQuencing;
    private String acuteViralHepatitisDnaMicroArray;
    private String acuteViralHepatitisOther;
    //    NON_NEONATAL_TETANUS
    private String nonNeonatalTetanusAntiBodyDetection;
    private String nonNeonatalTetanusAntigenDetection;
    private String nonNeonatalTetanusRapidTest;
    private String nonNeonatalTetanusCulture;
    private String nonNeonatalTetanusHistopathology;
    private String nonNeonatalTetanusIsolation;
    private String nonNeonatalTetanusIgmSerumAntibody;
    private String nonNeonatalTetanusIggSerumAntibody;
    private String nonNeonatalTetanusIgaSerumAntibody;
    private String nonNeonatalTetanusIncubationTime;
    private String nonNeonatalTetanusIndirectFluorescentAntibody;
    private String nonNeonatalTetanusMicroscopy;
    private String nonNeonatalTetanusNeutralizingAntibodies;
    private String nonNeonatalTetanusPcr;
    private String nonNeonatalTetanusGramStain;
    private String nonNeonatalTetanusLatexAgglutination;
    private String nonNeonatalTetanusCqValueDetection;
    private String nonNeonatalTetanusSeQuencing;
    private String nonNeonatalTetanusDnaMicroArray;
    private String nonNeonatalTetanusOther;
    private String hivAntiBodyDetection;
    private String hivAntigenDetection;
    private String hivRapidTest;
    private String hivCulture;
    private String hivHistopathology;
    private String hivIsolation;
    private String hivIgmSerumAntibody;
    private String hivIggSerumAntibody;
    private String hivIgaSerumAntibody;
    private String hivIncubationTime;
    private String hivIndirectFluorescentAntibody;
    private String hivMicroscopy;
    private String hivNeutralizingAntibodies;
    private String hivPcr;
    private String hivGramStain;
    private String hivLatexAgglutination;
    private String hivCqValueDetection;
    private String hivSeQuencing;
    private String hivDnaMicroArray;
    private String hivOther;
    private String schistosomiasisAntiBodyDetection;
    private String schistosomiasisAntigenDetection;
    private String schistosomiasisRapidTest;
    private String schistosomiasisCulture;
    private String schistosomiasisHistopathology;
    private String schistosomiasisIsolation;
    private String schistosomiasisIgmSerumAntibody;
    private String schistosomiasisIggSerumAntibody;
    private String schistosomiasisIgaSerumAntibody;
    private String schistosomiasisIncubationTime;
    private String schistosomiasisIndirectFluorescentAntibody;
    private String schistosomiasisMicroscopy;
    private String schistosomiasisNeutralizingAntibodies;
    private String schistosomiasisPcr;
    private String schistosomiasisGramStain;
    private String schistosomiasisLatexAgglutination;
    private String schistosomiasisCqValueDetection;
    private String schistosomiasisSeQuencing;
    private String schistosomiasisDnaMicroArray;
    private String schistosomiasisOther;
    private String soilTransmittedHelminthsAntiBodyDetection;
    private String soilTransmittedHelminthsAntigenDetection;
    private String soilTransmittedHelminthsRapidTest;
    private String soilTransmittedHelminthsCulture;
    private String soilTransmittedHelminthsHistopathology;
    private String soilTransmittedHelminthsIsolation;
    private String soilTransmittedHelminthsIgmSerumAntibody;
    private String soilTransmittedHelminthsIggSerumAntibody;
    private String soilTransmittedHelminthsIgaSerumAntibody;
    private String soilTransmittedHelminthsIncubationTime;
    private String soilTransmittedHelminthsIndirectFluorescentAntibody;
    private String soilTransmittedHelminthsMicroscopy;
    private String soilTransmittedHelminthsNeutralizingAntibodies;
    private String soilTransmittedHelminthsPcr;
    private String soilTransmittedHelminthsGramStain;
    private String soilTransmittedHelminthsLatexAgglutination;
    private String soilTransmittedHelminthsCqValueDetection;
    private String soilTransmittedHelminthsSeQuencing;
    private String soilTransmittedHelminthsDnaMicroArray;
    private String soilTransmittedHelminthsOther;
    private String trypanosomiasisAntiBodyDetection;
    private String trypanosomiasisAntigenDetection;
    private String trypanosomiasisRapidTest;
    private String trypanosomiasisCulture;
    private String trypanosomiasisHistopathology;
    private String trypanosomiasisIsolation;
    private String trypanosomiasisIgmSerumAntibody;
    private String trypanosomiasisIggSerumAntibody;
    private String trypanosomiasisIgaSerumAntibody;
    private String trypanosomiasisIncubationTime;
    private String trypanosomiasisIndirectFluorescentAntibody;
    private String trypanosomiasisMicroscopy;
    private String trypanosomiasisNeutralizingAntibodies;
    private String trypanosomiasisPcr;
    private String trypanosomiasisGramStain;
    private String trypanosomiasisLatexAgglutination;
    private String trypanosomiasisCqValueDetection;
    private String trypanosomiasisSeQuencing;
    private String trypanosomiasisDnaMicroArray;
    private String trypanosomiasisOther;
    private String diarrheaDehydrationAntiBodyDetection;
    private String diarrheaDehydrationAntigenDetection;
    private String diarrheaDehydrationRapidTest;
    private String diarrheaDehydrationCulture;
    private String diarrheaDehydrationHistopathology;
    private String diarrheaDehydrationIsolation;
    private String diarrheaDehydrationIgmSerumAntibody;
    private String diarrheaDehydrationIggSerumAntibody;
    private String diarrheaDehydrationIgaSerumAntibody;
    private String diarrheaDehydrationIncubationTime;
    private String diarrheaDehydrationIndirectFluorescentAntibody;
    private String diarrheaDehydrationMicroscopy;
    private String diarrheaDehydrationNeutralizingAntibodies;
    private String diarrheaDehydrationPcr;
    private String diarrheaDehydrationGramStain;
    private String diarrheaDehydrationLatexAgglutination;
    private String diarrheaDehydrationCqValueDetection;
    private String diarrheaDehydrationSeQuencing;
    private String diarrheaDehydrationDnaMicroArray;
    private String diarrheaDehydrationOther;
    private String diarrheaBloodAntiBodyDetection;
    private String diarrheaBloodAntigenDetection;
    private String diarrheaBloodRapidTest;
    private String diarrheaBloodCulture;
    private String diarrheaBloodHistopathology;
    private String diarrheaBloodIsolation;
    private String diarrheaBloodIgmSerumAntibody;
    private String diarrheaBloodIggSerumAntibody;
    private String diarrheaBloodIgaSerumAntibody;
    private String diarrheaBloodIncubationTime;
    private String diarrheaBloodIndirectFluorescentAntibody;
    private String diarrheaBloodMicroscopy;
    private String diarrheaBloodNeutralizingAntibodies;
    private String diarrheaBloodPcr;
    private String diarrheaBloodGramStain;
    private String diarrheaBloodLatexAgglutination;
    private String diarrheaBloodCqValueDetection;
    private String diarrheaBloodSeQuencing;
    private String diarrheaBloodDnaMicroArray;
    private String diarrheaBloodOther;
    private String snakeBiteAntiBodyDetection;
    private String snakeBiteAntigenDetection;
    private String snakeBiteRapidTest;
    private String snakeBiteCulture;
    private String snakeBiteHistopathology;
    private String snakeBiteIsolation;
    private String snakeBiteIgmSerumAntibody;
    private String snakeBiteIggSerumAntibody;
    private String snakeBiteIgaSerumAntibody;
    private String snakeBiteIncubationTime;
    private String snakeBiteIndirectFluorescentAntibody;
    private String snakeBiteMicroscopy;
    private String snakeBiteNeutralizingAntibodies;
    private String snakeBitePcr;
    private String snakeBiteGramStain;
    private String snakeBiteLatexAgglutination;
    private String snakeBiteCqValueDetection;
    private String snakeBiteSeQuencing;
    private String snakeBiteDnaMicroArray;
    private String snakeBiteOther;
    private String rubellaAntiBodyDetection;
    private String rubellaAntigenDetection;
    private String rubellaRapidTest;
    private String rubellaCulture;
    private String rubellaHistopathology;
    private String rubellaIsolation;
    private String rubellaIgmSerumAntibody;
    private String rubellaIggSerumAntibody;
    private String rubellaIgaSerumAntibody;
    private String rubellaIncubationTime;
    private String rubellaIndirectFluorescentAntibody;
    private String rubellaMicroscopy;
    private String rubellaNeutralizingAntibodies;
    private String rubellaPcr;
    private String rubellaGramStain;
    private String rubellaLatexAgglutination;
    private String rubellaCqValueDetection;
    private String rubellaSeQuencing;
    private String rubellaDnaMicroArray;
    private String rubellaOther;
    private String tuberculosisAntiBodyDetection;
    private String tuberculosisAntigenDetection;
    private String tuberculosisRapidTest;
    private String tuberculosisCulture;
    private String tuberculosisHistopathology;
    private String tuberculosisIsolation;
    private String tuberculosisIgmSerumAntibody;
    private String tuberculosisIggSerumAntibody;
    private String tuberculosisIgaSerumAntibody;
    private String tuberculosisIncubationTime;
    private String tuberculosisIndirectFluorescentAntibody;
    private String tuberculosisMicroscopy;
    private String tuberculosisNeutralizingAntibodies;
    private String tuberculosisPcr;
    private String tuberculosisGramStain;
    private String tuberculosisLatexAgglutination;
    private String tuberculosisCqValueDetection;
    private String tuberculosisSeQuencing;
    private String tuberculosisDnaMicroArray;
    private String tuberculosisOther;
    private String leprosyAntiBodyDetection;
    private String leprosyAntigenDetection;
    private String leprosyRapidTest;
    private String leprosyCulture;
    private String leprosyHistopathology;
    private String leprosyIsolation;
    private String leprosyIgmSerumAntibody;
    private String leprosyIggSerumAntibody;
    private String leprosyIgaSerumAntibody;
    private String leprosyIncubationTime;
    private String leprosyIndirectFluorescentAntibody;
    private String leprosyMicroscopy;
    private String leprosyNeutralizingAntibodies;
    private String leprosyPcr;
    private String leprosyGramStain;
    private String leprosyLatexAgglutination;
    private String leprosyCqValueDetection;
    private String leprosySeQuencing;
    private String leprosyDnaMicroArray;
    private String leprosyOther;
    private String lymphaticFilariasisAntiBodyDetection;
    private String lymphaticFilariasisAntigenDetection;
    private String lymphaticFilariasisRapidTest;
    private String lymphaticFilariasisCulture;
    private String lymphaticFilariasisHistopathology;
    private String lymphaticFilariasisIsolation;
    private String lymphaticFilariasisIgmSerumAntibody;
    private String lymphaticFilariasisIggSerumAntibody;
    private String lymphaticFilariasisIgaSerumAntibody;
    private String lymphaticFilariasisIncubationTime;
    private String lymphaticFilariasisIndirectFluorescentAntibody;
    private String lymphaticFilariasisMicroscopy;
    private String lymphaticFilariasisNeutralizingAntibodies;
    private String lymphaticFilariasisPcr;
    private String lymphaticFilariasisGramStain;
    private String lymphaticFilariasisLatexAgglutination;
    private String lymphaticFilariasisCqValueDetection;
    private String lymphaticFilariasisSeQuencing;
    private String lymphaticFilariasisDnaMicroArray;
    private String lymphaticFilariasisOther;
    private String buruliUlcerAntiBodyDetection;
    private String buruliUlcerAntigenDetection;
    private String buruliUlcerRapidTest;
    private String buruliUlcerCulture;
    private String buruliUlcerHistopathology;
    private String buruliUlcerIsolation;
    private String buruliUlcerIgmSerumAntibody;
    private String buruliUlcerIggSerumAntibody;
    private String buruliUlcerIgaSerumAntibody;
    private String buruliUlcerIncubationTime;
    private String buruliUlcerIndirectFluorescentAntibody;
    private String buruliUlcerMicroscopy;
    private String buruliUlcerNeutralizingAntibodies;
    private String buruliUlcerPcr;
    private String buruliUlcerGramStain;
    private String buruliUlcerLatexAgglutination;
    private String buruliUlcerCqValueDetection;
    private String buruliUlcerSeQuencing;
    private String buruliUlcerDnaMicroArray;
    private String buruliUlcerOther;
    private String pertussisAntiBodyDetection;
    private String pertussisAntigenDetection;
    private String pertussisRapidTest;
    private String pertussisCulture;
    private String pertussisHistopathology;
    private String pertussisIsolation;
    private String pertussisIgmSerumAntibody;
    private String pertussisIggSerumAntibody;
    private String pertussisIgaSerumAntibody;
    private String pertussisIncubationTime;
    private String pertussisIndirectFluorescentAntibody;
    private String pertussisMicroscopy;
    private String pertussisNeutralizingAntibodies;
    private String pertussisPcr;
    private String pertussisGramStain;
    private String pertussisLatexAgglutination;
    private String pertussisCqValueDetection;
    private String pertussisSeQuencing;
    private String pertussisDnaMicroArray;
    private String pertussisOther;
    private String neonatalAntiBodyDetection;
    private String neonatalAntigenDetection;
    private String neonatalRapidTest;
    private String neonatalCulture;
    private String neonatalHistopathology;
    private String neonatalIsolation;
    private String neonatalIgmSerumAntibody;
    private String neonatalIggSerumAntibody;
    private String neonatalIgaSerumAntibody;
    private String neonatalIncubationTime;
    private String neonatalIndirectFluorescentAntibody;
    private String neonatalMicroscopy;
    private String neonatalNeutralizingAntibodies;
    private String neonatalPcr;
    private String neonatalGramStain;
    private String neonatalLatexAgglutination;
    private String neonatalCqValueDetection;
    private String neonatalSeQuencing;
    private String neonatalDnaMicroArray;
    private String neonatalOther;
    private String onchocerciasisAntiBodyDetection;
    private String onchocerciasisAntigenDetection;
    private String onchocerciasisRapidTest;
    private String onchocerciasisCulture;
    private String onchocerciasisHistopathology;
    private String onchocerciasisIsolation;
    private String onchocerciasisIgmSerumAntibody;
    private String onchocerciasisIggSerumAntibody;
    private String onchocerciasisIgaSerumAntibody;
    private String onchocerciasisIncubationTime;
    private String onchocerciasisIndirectFluorescentAntibody;
    private String onchocerciasisMicroscopy;
    private String onchocerciasisNeutralizingAntibodies;
    private String onchocerciasisPcr;
    private String onchocerciasisGramStain;
    private String onchocerciasisLatexAgglutination;
    private String onchocerciasisCqValueDetection;
    private String onchocerciasisSeQuencing;
    private String onchocerciasisDnaMicroArray;
    private String onchocerciasisOther;
    private String diphtheriaAntiBodyDetection;
    private String diphtheriaAntigenDetection;
    private String diphtheriaRapidTest;
    private String diphtheriaCulture;
    private String diphtheriaHistopathology;
    private String diphtheriaIsolation;
    private String diphtheriaIgmSerumAntibody;
    private String diphtheriaIggSerumAntibody;
    private String diphtheriaIgaSerumAntibody;
    private String diphtheriaIncubationTime;
    private String diphtheriaIndirectFluorescentAntibody;
    private String diphtheriaMicroscopy;
    private String diphtheriaNeutralizingAntibodies;
    private String diphtheriaPcr;
    private String diphtheriaGramStain;
    private String diphtheriaLatexAgglutination;
    private String diphtheriaCqValueDetection;
    private String diphtheriaSeQuencing;
    private String diphtheriaDnaMicroArray;
    private String diphtheriaOther;
    private String trachomaAntiBodyDetection;
    private String trachomaAntigenDetection;
    private String trachomaRapidTest;
    private String trachomaCulture;
    private String trachomaHistopathology;
    private String trachomaIsolation;
    private String trachomaIgmSerumAntibody;
    private String trachomaIggSerumAntibody;
    private String trachomaIgaSerumAntibody;
    private String trachomaIncubationTime;
    private String trachomaIndirectFluorescentAntibody;
    private String trachomaMicroscopy;
    private String trachomaNeutralizingAntibodies;
    private String trachomaPcr;
    private String trachomaGramStain;
    private String trachomaLatexAgglutination;
    private String trachomaCqValueDetection;
    private String trachomaSeQuencing;
    private String trachomaDnaMicroArray;
    private String trachomaOther;
    private String yawsEndemicSyphilisAntiBodyDetection;
    private String yawsEndemicSyphilisAntigenDetection;
    private String yawsEndemicSyphilisRapidTest;
    private String yawsEndemicSyphilisCulture;
    private String yawsEndemicSyphilisHistopathology;
    private String yawsEndemicSyphilisIsolation;
    private String yawsEndemicSyphilisIgmSerumAntibody;
    private String yawsEndemicSyphilisIggSerumAntibody;
    private String yawsEndemicSyphilisIgaSerumAntibody;
    private String yawsEndemicSyphilisIncubationTime;
    private String yawsEndemicSyphilisIndirectFluorescentAntibody;
    private String yawsEndemicSyphilisMicroscopy;
    private String yawsEndemicSyphilisNeutralizingAntibodies;
    private String yawsEndemicSyphilisPcr;
    private String yawsEndemicSyphilisGramStain;
    private String yawsEndemicSyphilisLatexAgglutination;
    private String yawsEndemicSyphilisCqValueDetection;
    private String yawsEndemicSyphilisSeQuencing;
    private String yawsEndemicSyphilisDnaMicroArray;
    private String yawsEndemicSyphilisOther;
    private String maternalDeathsAntiBodyDetection;
    private String maternalDeathsAntigenDetection;
    private String maternalDeathsRapidTest;
    private String maternalDeathsCulture;
    private String maternalDeathsHistopathology;
    private String maternalDeathsIsolation;
    private String maternalDeathsIgmSerumAntibody;
    private String maternalDeathsIggSerumAntibody;
    private String maternalDeathsIgaSerumAntibody;
    private String maternalDeathsIncubationTime;
    private String maternalDeathsIndirectFluorescentAntibody;
    private String maternalDeathsMicroscopy;
    private String maternalDeathsNeutralizingAntibodies;
    private String maternalDeathsPcr;
    private String maternalDeathsGramStain;
    private String maternalDeathsLatexAgglutination;
    private String maternalDeathsCqValueDetection;
    private String maternalDeathsSeQuencing;
    private String maternalDeathsDnaMicroArray;
    private String maternalDeathsOther;
    private String perinatalDeathsAntiBodyDetection;
    private String perinatalDeathsAntigenDetection;
    private String perinatalDeathsRapidTest;
    private String perinatalDeathsCulture;
    private String perinatalDeathsHistopathology;
    private String perinatalDeathsIsolation;
    private String perinatalDeathsIgmSerumAntibody;
    private String perinatalDeathsIggSerumAntibody;
    private String perinatalDeathsIgaSerumAntibody;
    private String perinatalDeathsIncubationTime;
    private String perinatalDeathsIndirectFluorescentAntibody;
    private String perinatalDeathsMicroscopy;
    private String perinatalDeathsNeutralizingAntibodies;
    private String perinatalDeathsPcr;
    private String perinatalDeathsGramStain;
    private String perinatalDeathsLatexAgglutination;
    private String perinatalDeathsCqValueDetection;
    private String perinatalDeathsSeQuencing;
    private String perinatalDeathsDnaMicroArray;
    private String perinatalDeathsOther;
    private String influenzaAAntiBodyDetection;
    private String influenzaAAntigenDetection;
    private String influenzaARapidTest;
    private String influenzaACulture;
    private String influenzaAHistopathology;
    private String influenzaAIsolation;
    private String influenzaAIgmSerumAntibody;
    private String influenzaAIggSerumAntibody;
    private String influenzaAIgaSerumAntibody;
    private String influenzaAIncubationTime;
    private String influenzaAIndirectFluorescentAntibody;
    private String influenzaAMicroscopy;
    private String influenzaANeutralizingAntibodies;
    private String influenzaAPcr;
    private String influenzaAGramStain;
    private String influenzaALatexAgglutination;
    private String influenzaACqValueDetection;
    private String influenzaASeQuencing;
    private String influenzaADnaMicroArray;
    private String influenzaAOther;
    private String influenzaBAntiBodyDetection;
    private String influenzaBAntigenDetection;
    private String influenzaBRapidTest;
    private String influenzaBCulture;
    private String influenzaBHistopathology;
    private String influenzaBIsolation;
    private String influenzaBIgmSerumAntibody;
    private String influenzaBIggSerumAntibody;
    private String influenzaBIgaSerumAntibody;
    private String influenzaBIncubationTime;
    private String influenzaBIndirectFluorescentAntibody;
    private String influenzaBMicroscopy;
    private String influenzaBNeutralizingAntibodies;
    private String influenzaBPcr;
    private String influenzaBGramStain;
    private String influenzaBLatexAgglutination;
    private String influenzaBCqValueDetection;
    private String influenzaBSeQuencing;
    private String influenzaBDnaMicroArray;
    private String influenzaBOther;
    private String hMetapneumovirusAntiBodyDetection;
    private String hMetapneumovirusAntigenDetection;
    private String hMetapneumovirusRapidTest;
    private String hMetapneumovirusCulture;
    private String hMetapneumovirusHistopathology;
    private String hMetapneumovirusIsolation;
    private String hMetapneumovirusIgmSerumAntibody;
    private String hMetapneumovirusIggSerumAntibody;
    private String hMetapneumovirusIgaSerumAntibody;
    private String hMetapneumovirusIncubationTime;
    private String hMetapneumovirusIndirectFluorescentAntibody;
    private String hMetapneumovirusMicroscopy;
    private String hMetapneumovirusNeutralizingAntibodies;
    private String hMetapneumovirusPcr;
    private String hMetapneumovirusGramStain;
    private String hMetapneumovirusLatexAgglutination;
    private String hMetapneumovirusCqValueDetection;
    private String hMetapneumovirusSeQuencing;
    private String hMetapneumovirusDnaMicroArray;
    private String hMetapneumovirusOther;
    private String respiratorySyncytialVirusAntiBodyDetection;
    private String respiratorySyncytialVirusAntigenDetection;
    private String respiratorySyncytialVirusRapidTest;
    private String respiratorySyncytialVirusCulture;
    private String respiratorySyncytialVirusHistopathology;
    private String respiratorySyncytialVirusIsolation;
    private String respiratorySyncytialVirusIgmSerumAntibody;
    private String respiratorySyncytialVirusIggSerumAntibody;
    private String respiratorySyncytialVirusIgaSerumAntibody;
    private String respiratorySyncytialVirusIncubationTime;
    private String respiratorySyncytialVirusIndirectFluorescentAntibody;
    private String respiratorySyncytialVirusMicroscopy;
    private String respiratorySyncytialVirusNeutralizingAntibodies;
    private String respiratorySyncytialVirusPcr;
    private String respiratorySyncytialVirusGramStain;
    private String respiratorySyncytialVirusLatexAgglutination;
    private String respiratorySyncytialVirusCqValueDetection;
    private String respiratorySyncytialVirusSeQuencing;
    private String respiratorySyncytialVirusDnaMicroArray;
    private String respiratorySyncytialVirusOther;
    private String parainfluenza1_4AntiBodyDetection;
    private String parainfluenza1_4AntigenDetection;
    private String parainfluenza1_4RapidTest;
    private String parainfluenza1_4Culture;
    private String parainfluenza1_4Histopathology;
    private String parainfluenza1_4Isolation;
    private String parainfluenza1_4IgmSerumAntibody;
    private String parainfluenza1_4IggSerumAntibody;
    private String parainfluenza1_4IgaSerumAntibody;
    private String parainfluenza1_4IncubationTime;
    private String parainfluenza1_4IndirectFluorescentAntibody;
    private String parainfluenza1_4Microscopy;
    private String parainfluenza1_4NeutralizingAntibodies;
    private String parainfluenza1_4Pcr;
    private String parainfluenza1_4GramStain;
    private String parainfluenza1_4LatexAgglutination;
    private String parainfluenza1_4CqValueDetection;
    private String parainfluenza1_4SeQuencing;
    private String parainfluenza1_4DnaMicroArray;
    private String parainfluenza1_4Other;
    private String adenovirusAntiBodyDetection;
    private String adenovirusAntigenDetection;
    private String adenovirusRapidTest;
    private String adenovirusCulture;
    private String adenovirusHistopathology;
    private String adenovirusIsolation;
    private String adenovirusIgmSerumAntibody;
    private String adenovirusIggSerumAntibody;
    private String adenovirusIgaSerumAntibody;
    private String adenovirusIncubationTime;
    private String adenovirusIndirectFluorescentAntibody;
    private String adenovirusMicroscopy;
    private String adenovirusNeutralizingAntibodies;
    private String adenovirusPcr;
    private String adenovirusGramStain;
    private String adenovirusLatexAgglutination;
    private String adenovirusCqValueDetection;
    private String adenovirusSeQuencing;
    private String adenovirusDnaMicroArray;
    private String adenovirusOther;
    private String rhinovirusAntiBodyDetection;
    private String rhinovirusAntigenDetection;
    private String rhinovirusRapidTest;
    private String rhinovirusCulture;
    private String rhinovirusHistopathology;
    private String rhinovirusIsolation;
    private String rhinovirusIgmSerumAntibody;
    private String rhinovirusIggSerumAntibody;
    private String rhinovirusIgaSerumAntibody;
    private String rhinovirusIncubationTime;
    private String rhinovirusIndirectFluorescentAntibody;
    private String rhinovirusMicroscopy;
    private String rhinovirusNeutralizingAntibodies;
    private String rhinovirusPcr;
    private String rhinovirusGramStain;
    private String rhinovirusLatexAgglutination;
    private String rhinovirusCqValueDetection;
    private String rhinovirusSeQuencing;
    private String rhinovirusDnaMicroArray;
    private String rhinovirusOther;
    private String enterovirusAntiBodyDetection;
    private String enterovirusAntigenDetection;
    private String enterovirusRapidTest;
    private String enterovirusCulture;
    private String enterovirusHistopathology;
    private String enterovirusIsolation;
    private String enterovirusIgmSerumAntibody;
    private String enterovirusIggSerumAntibody;
    private String enterovirusIgaSerumAntibody;
    private String enterovirusIncubationTime;
    private String enterovirusIndirectFluorescentAntibody;
    private String enterovirusMicroscopy;
    private String enterovirusNeutralizingAntibodies;
    private String enterovirusPcr;
    private String enterovirusGramStain;
    private String enterovirusLatexAgglutination;
    private String enterovirusCqValueDetection;
    private String enterovirusSeQuencing;
    private String enterovirusDnaMicroArray;
    private String enterovirusOther;
    private String mPneumoniaeAntiBodyDetection;
    private String mPneumoniaeAntigenDetection;
    private String mPneumoniaeRapidTest;
    private String mPneumoniaeCulture;
    private String mPneumoniaeHistopathology;
    private String mPneumoniaeIsolation;
    private String mPneumoniaeIgmSerumAntibody;
    private String mPneumoniaeIggSerumAntibody;
    private String mPneumoniaeIgaSerumAntibody;
    private String mPneumoniaeIncubationTime;
    private String mPneumoniaeIndirectFluorescentAntibody;
    private String mPneumoniaeMicroscopy;
    private String mPneumoniaeNeutralizingAntibodies;
    private String mPneumoniaePcr;
    private String mPneumoniaeGramStain;
    private String mPneumoniaeLatexAgglutination;
    private String mPneumoniaeCqValueDetection;
    private String mPneumoniaeSeQuencing;
    private String mPneumoniaeDnaMicroArray;
    private String mPneumoniaeOther;
    private String cPneumoniaeAntiBodyDetection;
    private String cPneumoniaeAntigenDetection;
    private String cPneumoniaeRapidTest;
    private String cPneumoniaeCulture;
    private String cPneumoniaeHistopathology;
    private String cPneumoniaeIsolation;
    private String cPneumoniaeIgmSerumAntibody;
    private String cPneumoniaeIggSerumAntibody;
    private String cPneumoniaeIgaSerumAntibody;
    private String cPneumoniaeIncubationTime;
    private String cPneumoniaeIndirectFluorescentAntibody;
    private String cPneumoniaeMicroscopy;
    private String cPneumoniaeNeutralizingAntibodies;
    private String cPneumoniaePcr;
    private String cPneumoniaeGramStain;
    private String cPneumoniaeLatexAgglutination;
    private String cPneumoniaeCqValueDetection;
    private String cPneumoniaeSeQuencing;
    private String cPneumoniaeDnaMicroArray;
    private String cPneumoniaeOther;
    private String ariAntiBodyDetection;
    private String ariAntigenDetection;
    private String ariRapidTest;
    private String ariCulture;
    private String ariHistopathology;
    private String ariIsolation;
    private String ariIgmSerumAntibody;
    private String ariIggSerumAntibody;
    private String ariIgaSerumAntibody;
    private String ariIncubationTime;
    private String ariIndirectFluorescentAntibody;
    private String ariMicroscopy;
    private String ariNeutralizingAntibodies;
    private String ariPcr;
    private String ariGramStain;
    private String ariLatexAgglutination;
    private String ariCqValueDetection;
    private String ariSeQuencing;
    private String ariDnaMicroArray;
    private String ariOther;
    private String chikungunyaAntiBodyDetection;
    private String chikungunyaAntigenDetection;
    private String chikungunyaRapidTest;
    private String chikungunyaCulture;
    private String chikungunyaHistopathology;
    private String chikungunyaIsolation;
    private String chikungunyaIgmSerumAntibody;
    private String chikungunyaIggSerumAntibody;
    private String chikungunyaIgaSerumAntibody;
    private String chikungunyaIncubationTime;
    private String chikungunyaIndirectFluorescentAntibody;
    private String chikungunyaMicroscopy;
    private String chikungunyaNeutralizingAntibodies;
    private String chikungunyaPcr;
    private String chikungunyaGramStain;
    private String chikungunyaLatexAgglutination;
    private String chikungunyaCqValueDetection;
    private String chikungunyaSeQuencing;
    private String chikungunyaDnaMicroArray;
    private String chikungunyaOther;
    private String postImmunizationAdverseEventsMildAntiBodyDetection;
    private String postImmunizationAdverseEventsMildAntigenDetection;
    private String postImmunizationAdverseEventsMildRapidTest;
    private String postImmunizationAdverseEventsMildCulture;
    private String postImmunizationAdverseEventsMildHistopathology;
    private String postImmunizationAdverseEventsMildIsolation;
    private String postImmunizationAdverseEventsMildIgmSerumAntibody;
    private String postImmunizationAdverseEventsMildIggSerumAntibody;
    private String postImmunizationAdverseEventsMildIgaSerumAntibody;
    private String postImmunizationAdverseEventsMildIncubationTime;
    private String postImmunizationAdverseEventsMildIndirectFluorescentAntibody;
    private String postImmunizationAdverseEventsMildMicroscopy;
    private String postImmunizationAdverseEventsMildNeutralizingAntibodies;
    private String postImmunizationAdverseEventsMildPcr;
    private String postImmunizationAdverseEventsMildGramStain;
    private String postImmunizationAdverseEventsMildLatexAgglutination;
    private String postImmunizationAdverseEventsMildCqValueDetection;
    private String postImmunizationAdverseEventsMildSeQuencing;
    private String postImmunizationAdverseEventsMildDnaMicroArray;
    private String postImmunizationAdverseEventsMildOther;
    private String PostImmunizationAdverseEventsSevereAntiBodyDetection;
    private String PostImmunizationAdverseEventsSevereAntigenDetection;
    private String PostImmunizationAdverseEventsSevereRapidTest;
    private String PostImmunizationAdverseEventsSevereCulture;
    private String PostImmunizationAdverseEventsSevereHistopathology;
    private String PostImmunizationAdverseEventsSevereIsolation;
    private String PostImmunizationAdverseEventsSevereIgmSerumAntibody;
    private String PostImmunizationAdverseEventsSevereIggSerumAntibody;
    private String PostImmunizationAdverseEventsSevereIgaSerumAntibody;
    private String PostImmunizationAdverseEventsSevereIncubationTime;
    private String PostImmunizationAdverseEventsSevereIndirectFluorescentAntibody;
    private String PostImmunizationAdverseEventsSevereMicroscopy;
    private String PostImmunizationAdverseEventsSevereNeutralizingAntibodies;
    private String PostImmunizationAdverseEventsSeverePcr;
    private String PostImmunizationAdverseEventsSevereGramStain;
    private String PostImmunizationAdverseEventsSevereLatexAgglutination;
    private String PostImmunizationAdverseEventsSevereCqValueDetection;
    private String PostImmunizationAdverseEventsSevereSeQuencing;
    private String PostImmunizationAdverseEventsSevereDnaMicroArray;
    private String PostImmunizationAdverseEventsSevereOther;
    private String fhaAntiBodyDetection;
    private String fhaAntigenDetection;
    private String fhaRapidTest;
    private String fhaCulture;
    private String fhaHistopathology;
    private String fhaIsolation;
    private String fhaIgmSerumAntibody;
    private String fhaIggSerumAntibody;
    private String fhaIgaSerumAntibody;
    private String fhaIncubationTime;
    private String fhaIndirectFluorescentAntibody;
    private String fhaMicroscopy;
    private String fhaNeutralizingAntibodies;
    private String fhaPcr;
    private String fhaGramStain;
    private String fhaLatexAgglutination;
    private String fhaCqValueDetection;
    private String fhaSeQuencing;
    private String fhaDnaMicroArray;
    private String fhaOther;
    private String otherAntiBodyDetection;
    private String otherAntigenDetection;
    private String otherRapidTest;
    private String otherCulture;
    private String otherHistopathology;
    private String otherIsolation;
    private String otherIgmSerumAntibody;
    private String otherIggSerumAntibody;
    private String otherIgaSerumAntibody;
    private String otherIncubationTime;
    private String otherIndirectFluorescentAntibody;
    private String otherMicroscopy;
    private String otherNeutralizingAntibodies;
    private String otherPcr;
    private String otherGramStain;
    private String otherLatexAgglutination;
    private String otherCqValueDetection;
    private String otherSeQuencing;
    private String otherDnaMicroArray;
    private String otherOther;
    private String undefinedAntiBodyDetection;
    private String undefinedAntigenDetection;
    private String undefinedRapidTest;
    private String undefinedCulture;
    private String undefinedHistopathology;
    private String undefinedIsolation;
    private String undefinedIgmSerumAntibody;
    private String undefinedIggSerumAntibody;
    private String undefinedIgaSerumAntibody;
    private String undefinedIncubationTime;
    private String undefinedIndirectFluorescentAntibody;
    private String undefinedMicroscopy;
    private String undefinedNeutralizingAntibodies;
    private String undefinedPcr;
    private String undefinedGramStain;
    private String undefinedLatexAgglutination;
    private String undefinedCqValueDetection;
    private String undefinedSeQuencing;
    private String undefinedDnaMicroArray;
    private String undefinedOther;

//    @EmbeddedPersonalData
//    @EmbeddedSensitiveData
//    private SampleExportPathogenTest pathogenTest1 = new SampleExportPathogenTest();
//    @EmbeddedPersonalData
//    @EmbeddedSensitiveData
//    private SampleExportPathogenTest pathogenTest2 = new SampleExportPathogenTest();
//    @EmbeddedPersonalData
//    @EmbeddedSensitiveData
//    private SampleExportPathogenTest pathogenTest3 = new SampleExportPathogenTest();
    private String otherPathogenTests;




    public CaseExportDetailedSampleDto() {}
    //@formatter:off
    @SuppressWarnings("unchecked")
    public CaseExportDetailedSampleDto(long id, long personId, Double personAddressLatitude, Double personAddressLongitude, Float personAddressLatLonAcc, long epiDataId, long symptomsId,
                                       long hospitalizationId, long healthConditionsId, String uuid, String epidNumber,
                                       Disease disease, DiseaseVariant diseaseVariant, String diseaseDetails, String diseaseVariantDetails,
                                       String personUuid, String firstName, String lastName, Salutation salutation, String otherSalutation, Sex sex, YesNoUnknown pregnant,
                                       Integer approximateAge, ApproximateAgeType approximateAgeType, Integer birthdateDD, Integer birthdateMM,
                                       Integer birthdateYYYY, Date reportDate, String region, String district, String community,
                                       FacilityType facilityType, String healthFacility, String healthFacilityUuid, String healthFacilityDetails, String pointOfEntry,
                                       String pointOfEntryUuid, String pointOfEntryDetails, CaseClassification caseClassification,
                                       YesNoUnknown clinicalConfirmation, YesNoUnknown epidemiologicalConfirmation, YesNoUnknown laboratoryDiagnosticConfirmation,
                                       Boolean notACaseReasonNegativeTest, Boolean notACaseReasonPhysicianInformation, Boolean notACaseReasonDifferentPathogen, Boolean notACaseReasonOther,
                                       String notACaseReasonDetails, InvestigationStatus investigationStatus, Date investigatedDate,
                                       CaseOutcome outcome, Date outcomeDate,
                                       YesNoUnknown sequelae, String sequelaeDetails,
                                       YesNoUnknown bloodOrganOrTissueDonated,
                                       FollowUpStatus followUpStatus, Date followUpUntil,
                                       Boolean nosocomialOutbreak, InfectionSetting infectionSetting,
                                       YesNoUnknown prohibitionToWork, Date prohibitionToWorkFrom, Date prohibitionToWorkUntil,
                                       YesNoUnknown reInfection, Date previousInfectionDate, ReinfectionStatus reinfectionStatus, Object reinfectionDetails,
                                       // Quarantine
                                       QuarantineType quarantine, String quarantineTypeDetails, Date quarantineFrom, Date quarantineTo,
                                       String quarantineHelpNeeded,
                                       boolean quarantineOrderedVerbally, boolean quarantineOrderedOfficialDocument, Date quarantineOrderedVerballyDate,
                                       Date quarantineOrderedOfficialDocumentDate, boolean quarantineExtended, boolean quarantineReduced,
                                       boolean quarantineOfficialOrderSent, Date quarantineOfficialOrderSentDate,
                                       YesNoUnknown admittedToHealthFacility, Date admissionDate, Date dischargeDate, YesNoUnknown leftAgainstAdvice, PresentCondition presentCondition,
                                       Date deathDate, Date burialDate, BurialConductor burialConductor, String burialPlaceDescription,
                                       String addressRegion, String addressDistrict, String addressCommunity, String city, String street, String houseNumber, String additionalInformation, String postalCode,
                                       String facility, String facilityUuid, String facilityDetails,
                                       String phone, String phoneOwner, String emailAddress, String otherContactDetails, EducationType educationType, String educationDetails,
                                       OccupationType occupationType, String occupationDetails, ArmedForcesRelationType ArmedForcesRelationType, YesNoUnknown contactWithSourceCaseKnown,
                                       //Date onsetDate,
                                       VaccinationStatus vaccinationStatus, YesNoUnknown postpartum, Trimester trimester,
                                       long eventCount, Long prescriptionCount, Long treatmentCount, Long clinicalVisitCount,
                                       String externalID, String externalToken, String internalToken,
                                       String birthName, String birthCountryIsoCode, String birthCountryName, String citizenshipIsoCode, String citizenshipCountryName,
                                       CaseIdentificationSource caseIdentificationSource, ScreeningType screeningType,
                                       // responsible jurisdiction
                                       String responsibleRegion, String responsibleDistrict, String responsibleCommunity,
                                       // clinician
                                       String clinicianName, String clinicianPhone, String clinicianEmail,
                                       // users
                                       Long reportingUserId, Long followUpStatusChangeUserId,
                                       Date previousQuarantineTo, String quarantineChangeComment,
                                       String associatedWithOutbreak, boolean isInJurisdiction
    ) {
        //@formatter:on

        this.id = id;
        this.personId = personId;
        this.addressGpsCoordinates = LocationHelper.buildGpsCoordinatesCaption(personAddressLatitude, personAddressLongitude, personAddressLatLonAcc);
        this.epiDataId = epiDataId;
        this.symptomsId = symptomsId;
        this.hospitalizationId = hospitalizationId;
        this.healthConditionsId = healthConditionsId;
        this.uuid = uuid;
        this.epidNumber = epidNumber;
        this.armedForcesRelationType = ArmedForcesRelationType;
        this.disease = disease;
        this.diseaseDetails = diseaseDetails;
        this.diseaseVariant = diseaseVariant;
        this.diseaseVariantDetails = diseaseVariantDetails;
        this.personUuid = personUuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salutation = salutation;
        this.otherSalutation = otherSalutation;
        this.sex = sex;
        this.pregnant = pregnant;
        this.approximateAge = ApproximateAgeHelper.formatApproximateAge(approximateAge, approximateAgeType);
        this.ageGroup = ApproximateAgeHelper.getAgeGroupFromAge(approximateAge, approximateAgeType);
        this.birthdate = new BirthDateDto(birthdateDD, birthdateMM, birthdateYYYY);
        this.reportDate = reportDate;
        this.region = region;
        this.district = district;
        this.community = community;
        this.caseClassification = caseClassification;
        this.clinicalConfirmation = clinicalConfirmation;
        this.epidemiologicalConfirmation = epidemiologicalConfirmation;
        this.laboratoryDiagnosticConfirmation = laboratoryDiagnosticConfirmation;
        this.notACaseReasonNegativeTest = notACaseReasonNegativeTest;
        this.notACaseReasonPhysicianInformation = notACaseReasonPhysicianInformation;
        this.notACaseReasonDifferentPathogen = notACaseReasonDifferentPathogen;
        this.notACaseReasonOther = notACaseReasonOther;
        this.notACaseReasonDetails = notACaseReasonDetails;
        this.investigationStatus = investigationStatus;
        this.investigatedDate = investigatedDate;
        this.outcome = outcome;
        this.outcomeDate = outcomeDate;
        this.sequelae = sequelae;
        this.sequelaeDetails = sequelaeDetails;
        this.bloodOrganOrTissueDonated = bloodOrganOrTissueDonated;
        this.nosocomialOutbreak = nosocomialOutbreak;
        this.infectionSetting = infectionSetting;
        this.prohibitionToWork = prohibitionToWork;
        this.prohibitionToWorkFrom = prohibitionToWorkFrom;
        this.prohibitionToWorkUntil = prohibitionToWorkUntil;
        this.reInfection = reInfection;
        this.previousInfectionDate = previousInfectionDate;
        this.reinfectionStatus = reinfectionStatus;
        this.reinfectionDetails = DataHelper.buildStringFromTrueValues((Map<ReinfectionDetail, Boolean>) reinfectionDetails);
        this.quarantine = quarantine;
        this.quarantineTypeDetails = quarantineTypeDetails;
        this.quarantineFrom = quarantineFrom;
        this.quarantineTo = quarantineTo;
        this.quarantineHelpNeeded = quarantineHelpNeeded;
        this.quarantineOrderedVerbally = quarantineOrderedVerbally;
        this.quarantineOrderedOfficialDocument = quarantineOrderedOfficialDocument;
        this.quarantineOrderedVerballyDate = quarantineOrderedVerballyDate;
        this.quarantineOrderedOfficialDocumentDate = quarantineOrderedOfficialDocumentDate;
        this.quarantineExtended = quarantineExtended;
        this.quarantineReduced = quarantineReduced;
        this.quarantineOfficialOrderSent = quarantineOfficialOrderSent;
        this.quarantineOfficialOrderSentDate = quarantineOfficialOrderSentDate;
        this.facilityType = facilityType;
        this.healthFacility = FacilityHelper.buildFacilityString(healthFacilityUuid, healthFacility);
        this.healthFacilityDetails = healthFacilityDetails;
        this.pointOfEntry = InfrastructureHelper.buildPointOfEntryString(pointOfEntryUuid, pointOfEntry);
        this.pointOfEntryDetails = pointOfEntryDetails;
        this.admittedToHealthFacility = admittedToHealthFacility;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.leftAgainstAdvice = leftAgainstAdvice;
        this.presentCondition = presentCondition;
        this.deathDate = deathDate;
        this.burialInfo = new BurialInfoDto(burialDate, burialConductor, burialPlaceDescription);
        this.addressRegion = addressRegion;
        this.addressDistrict = addressDistrict;
        this.addressCommunity = addressCommunity;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.additionalInformation = additionalInformation;
        this.postalCode = postalCode;
        this.facility = FacilityHelper.buildFacilityString(facilityUuid, facility);
        this.facilityDetails = facilityDetails;
        this.phone = phone;
        this.phoneOwner = phoneOwner;
        this.emailAddress = emailAddress;
        this.otherContactDetails = otherContactDetails;
        this.educationType = educationType;
        this.educationDetails = educationDetails;
        this.occupationType = occupationType;
        this.occupationDetails = occupationDetails;
        this.contactWithSourceCaseKnown = contactWithSourceCaseKnown;
//		this.onsetDate = onsetDate;
        this.vaccinationStatus = vaccinationStatus;

        this.postpartum = postpartum;
        this.trimester = trimester;
        this.followUpStatus = followUpStatus;
        this.followUpUntil = followUpUntil;

        this.eventCount = eventCount;
        this.numberOfPrescriptions = prescriptionCount != null ? prescriptionCount.intValue() : 0;
        this.numberOfTreatments = treatmentCount != null ? treatmentCount.intValue() : 0;
        this.numberOfClinicalVisits = clinicalVisitCount != null ? clinicalVisitCount.intValue() : 0;

        this.externalID = externalID;
        this.externalToken = externalToken;
        this.internalToken = internalToken;
        this.birthName = birthName;
        this.birthCountry = I18nProperties.getCountryName(birthCountryIsoCode, birthCountryName);
        this.citizenship = I18nProperties.getCountryName(citizenshipIsoCode, citizenshipCountryName);
        this.caseIdentificationSource = caseIdentificationSource;
        this.screeningType = screeningType;

        this.responsibleRegion = responsibleRegion;
        this.responsibleDistrict = responsibleDistrict;
        this.responsibleCommunity = responsibleCommunity;

        this.clinicianName = clinicianName;
        this.clinicianPhone = clinicianPhone;
        this.clinicianEmail = clinicianEmail;

        this.reportingUserId = reportingUserId;
        this.followUpStatusChangeUserId = followUpStatusChangeUserId;

        this.previousQuarantineTo = previousQuarantineTo;
        this.quarantineChangeComment = quarantineChangeComment;

        this.associatedWithOutbreak = associatedWithOutbreak;
        this.isInJurisdiction = isInJurisdiction;
    }


    public CaseReferenceDto toReference() {
        return new CaseReferenceDto(uuid, firstName, lastName);
    }

    public Boolean getInJurisdiction() {
        return isInJurisdiction;
    }

    @Order(0)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(COUNTRY)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCountry() {
        return country;
    }

    @Order(1)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(ID)
    @ExportGroup(ExportGroupType.CORE)
    public long getId() {
        return id;
    }

    public long getPersonId() {
        return personId;
    }

    public long getEpiDataId() {
        return epiDataId;
    }

    public long getSymptomsId() {
        return symptomsId;
    }

    public void setSymptomsId(long symptomsId) {
        this.symptomsId = symptomsId;
    }

    public long getHospitalizationId() {
        return hospitalizationId;
    }

    @Order(2)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.UUID)
    @ExportGroup(ExportGroupType.CORE)
    public String getUuid() {
        return uuid;
    }

    @Order(3)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.EPID_NUMBER)
    @ExportGroup(ExportGroupType.CORE)
    public String getEpidNumber() {
        return epidNumber;
    }

    @Order(4)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.EXTERNAL_ID)
    @ExportGroup(ExportGroupType.CORE)
    public String getExternalID() {
        return externalID;
    }

    @Order(5)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.EXTERNAL_TOKEN)
    @ExportGroup(ExportGroupType.CORE)
    public String getExternalToken() {
        return externalToken;
    }

    @Order(6)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.INTERNAL_TOKEN)
    @ExportGroup(ExportGroupType.CORE)
    public String getInternalToken() {
        return internalToken;
    }

    @Order(7)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.DISEASE)
    @ExportGroup(ExportGroupType.CORE)
    public Disease getDisease() {
        return disease;
    }

    @Order(8)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.DISEASE_DETAILS)
    @ExportGroup(ExportGroupType.CORE)
    public String getDiseaseDetails() {
        return diseaseDetails;
    }

    @Order(9)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.DISEASE_VARIANT)
    @ExportGroup(ExportGroupType.CORE)
    public DiseaseVariant getDiseaseVariant() {
        return diseaseVariant;
    }

    @Order(10)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.DISEASE_VARIANT_DETAILS)
    @ExportGroup(ExportGroupType.CORE)
    public String getDiseaseVariantDetails() {
        return diseaseVariantDetails;
    }

    @Order(11)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.UUID })
    @ExportGroup(ExportGroupType.CORE)
    public String getPersonUuid() {
        return personUuid;
    }

    @Order(12)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.FIRST_NAME })
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getFirstName() {
        return firstName;
    }

    @Order(13)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.LAST_NAME })
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getLastName() {
        return lastName;
    }

    @Order(14)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.SALUTATION })
    @ExportGroup(ExportGroupType.SENSITIVE)
    @HideForCountriesExcept
    public Salutation getSalutation() {
        return salutation;
    }

    @Order(15)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.OTHER_SALUTATION })
    @ExportGroup(ExportGroupType.SENSITIVE)
    @HideForCountriesExcept
    public String getOtherSalutation() {
        return otherSalutation;
    }

    @Order(16)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.SEX })
    @ExportGroup(ExportGroupType.SENSITIVE)
    public Sex getSex() {
        return sex;
    }

    @Order(17)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.PREGNANT)
    @ExportGroup(ExportGroupType.SENSITIVE)
    public YesNoUnknown getPregnant() {
        return pregnant;
    }

    @Order(18)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.TRIMESTER)
    @ExportGroup(ExportGroupType.SENSITIVE)
    public Trimester getTrimester() {
        return trimester;
    }

    @Order(19)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.POSTPARTUM)
    @ExportGroup(ExportGroupType.SENSITIVE)
    public YesNoUnknown getPostpartum() {
        return postpartum;
    }

    @Order(20)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.APPROXIMATE_AGE })
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getApproximateAge() {
        return approximateAge;
    }

    @Order(21)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            AGE_GROUP })
    @ExportGroup(ExportGroupType.PERSON)
    public String getAgeGroup() {
        return ageGroup;
    }

    @Order(22)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty(CaseExportDetailedSampleDto.BIRTH_DATE)
    @ExportGroup(ExportGroupType.SENSITIVE)
    public BirthDateDto getBirthdate() {
        return birthdate;
    }

    @Order(23)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.REPORT_DATE)
    @ExportGroup(ExportGroupType.CORE)
    public Date getReportDate() {
        return reportDate;
    }

    @Order(24)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.RESPONSIBLE_REGION)
    @ExportGroup(ExportGroupType.CORE)
    public String getResponsibleRegion() {
        return responsibleRegion;
    }

    @Order(25)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.RESPONSIBLE_DISTRICT)
    @ExportGroup(ExportGroupType.CORE)
    public String getResponsibleDistrict() {
        return responsibleDistrict;
    }

    @Order(26)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.RESPONSIBLE_COMMUNITY)
    @ExportGroup(ExportGroupType.CORE)
    public String getResponsibleCommunity() {
        return responsibleCommunity;
    }

    @Order(27)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.REGION)
    @ExportGroup(ExportGroupType.CORE)
    public String getRegion() {
        return region;
    }

    @Order(28)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.DISTRICT)
    @ExportGroup(ExportGroupType.CORE)
    public String getDistrict() {
        return district;
    }

    @Order(29)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.COMMUNITY)
    @ExportGroup(ExportGroupType.CORE)
    public String getCommunity() {
        return community;
    }

    @Order(30)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.FACILITY_TYPE)
    @ExportGroup(ExportGroupType.CORE)
    public FacilityType getFacilityType() {
        return facilityType;
    }

    @Order(31)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.HEALTH_FACILITY)
    @ExportGroup(ExportGroupType.CORE)
    public String getHealthFacility() {
        return healthFacility;
    }

    @Order(32)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.HEALTH_FACILITY_DETAILS)
    @ExportGroup(ExportGroupType.CORE)
    public String getHealthFacilityDetails() {
        return healthFacilityDetails;
    }

    @Order(33)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.POINT_OF_ENTRY)
    @ExportGroup(ExportGroupType.CORE)
    public String getPointOfEntry() {
        return pointOfEntry;
    }

    @Order(34)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.POINT_OF_ENTRY_DETAILS)
    @ExportGroup(ExportGroupType.CORE)
    public String getPointOfEntryDetails() {
        return pointOfEntryDetails;
    }

    @Order(35)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(INITIAL_DETECTION_PLACE)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInitialDetectionPlace() {
        return initialDetectionPlace;
    }

    @Order(36)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(CaseDataDto.CASE_CLASSIFICATION)
    @ExportGroup(ExportGroupType.CORE)
    public CaseClassification getCaseClassification() {
        return caseClassification;
    }

    @Order(37)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.CLINICAL_CONFIRMATION)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public YesNoUnknown getClinicalConfirmation() {
        return clinicalConfirmation;
    }

    @Order(38)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.EPIDEMIOLOGICAL_CONFIRMATION)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public YesNoUnknown getEpidemiologicalConfirmation() {
        return epidemiologicalConfirmation;
    }

    @Order(39)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.LABORATORY_DIAGNOSTIC_CONFIRMATION)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public YesNoUnknown getLaboratoryDiagnosticConfirmation() {
        return laboratoryDiagnosticConfirmation;
    }

    @Order(41)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(CaseDataDto.INVESTIGATION_STATUS)
    @ExportGroup(ExportGroupType.CORE)
    public InvestigationStatus getInvestigationStatus() {
        return investigationStatus;
    }

    @Order(42)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(CaseDataDto.INVESTIGATED_DATE)
    @ExportGroup(ExportGroupType.CORE)
    public Date getInvestigatedDate() {
        return investigatedDate;
    }

    @Order(43)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.OUTCOME)
    @ExportGroup(ExportGroupType.CORE)
    public CaseOutcome getOutcome() {
        return outcome;
    }

    @Order(44)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.OUTCOME_DATE)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public Date getOutcomeDate() {
        return outcomeDate;
    }

    @Order(45)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.SEQUELAE)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public YesNoUnknown getSequelae() {
        return sequelae;
    }

    @Order(46)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = CaseDataDto.SEQUELAE_DETAILS, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSequelaeDetails() {
        return sequelaeDetails;
    }

    @Order(47)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.BLOOD_ORGAN_OR_TISSUE_DONATED)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    @HideForCountriesExcept()
    public YesNoUnknown getBloodOrganOrTissueDonated() {
        return bloodOrganOrTissueDonated;
    }

    @Order(48)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = CaseDataDto.NOSOCOMIAL_OUTBREAK, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    @HideForCountriesExcept
    public Boolean getNosocomialOutbreak() {
        return nosocomialOutbreak;
    }

    @Order(49)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = CaseDataDto.NOSOCOMIAL_OUTBREAK, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    @HideForCountriesExcept
    public InfectionSetting getInfectionSetting() {
        return infectionSetting;
    }

    @Order(50)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = CaseDataDto.PROHIBITION_TO_WORK, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    @HideForCountriesExcept
    public YesNoUnknown getProhibitionToWork() {
        return prohibitionToWork;
    }

    @Order(51)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = CaseDataDto.PROHIBITION_TO_WORK, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    @HideForCountriesExcept
    public Date getProhibitionToWorkFrom() {
        return prohibitionToWorkFrom;
    }

    @Order(52)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = CaseDataDto.PROHIBITION_TO_WORK, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    @HideForCountriesExcept
    public Date getProhibitionToWorkUntil() {
        return prohibitionToWorkUntil;
    }

    @Order(53)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = CaseDataDto.RE_INFECTION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    @HideForCountriesExcept
    public YesNoUnknown getReInfection() {
        return reInfection;
    }

    @Order(54)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = CaseDataDto.RE_INFECTION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    @HideForCountriesExcept
    public Date getPreviousInfectionDate() {
        return previousInfectionDate;
    }

    @Order(55)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = CaseDataDto.RE_INFECTION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    @HideForCountriesExcept
    public ReinfectionStatus getReinfectionStatus() {
        return reinfectionStatus;
    }

    @Order(56)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = CaseDataDto.RE_INFECTION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    @HideForCountriesExcept
    public String getReinfectionDetails() {
        return reinfectionDetails;
    }

    @Order(57)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = QUARANTINE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public QuarantineType getQuarantine() {
        return quarantine;
    }

    @Order(58)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = QUARANTINE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getQuarantineTypeDetails() {
        return quarantineTypeDetails;
    }

    @Order(59)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = QUARANTINE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public Date getQuarantineFrom() {
        return quarantineFrom;
    }

    @Order(60)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = QUARANTINE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public Date getQuarantineTo() {
        return quarantineTo;
    }

    @Order(61)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = QUARANTINE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public Date getPreviousQuarantineTo() {
        return previousQuarantineTo;
    }

    @Order(62)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = QUARANTINE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getQuarantineChangeComment() {
        return quarantineChangeComment;
    }

    @Order(63)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = QUARANTINE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getQuarantineHelpNeeded() {
        return quarantineHelpNeeded;
    }

    @Order(64)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = QUARANTINE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    @HideForCountriesExcept(countries = {
            CountryHelper.COUNTRY_CODE_GERMANY,
            CountryHelper.COUNTRY_CODE_SWITZERLAND })
    public boolean isQuarantineOrderedVerbally() {
        return quarantineOrderedVerbally;
    }

    @Order(65)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = QUARANTINE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    @HideForCountriesExcept(countries = {
            CountryHelper.COUNTRY_CODE_GERMANY,
            CountryHelper.COUNTRY_CODE_SWITZERLAND })
    public boolean isQuarantineOrderedOfficialDocument() {
        return quarantineOrderedOfficialDocument;
    }

    @Order(66)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = QUARANTINE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    @HideForCountriesExcept(countries = {
            CountryHelper.COUNTRY_CODE_GERMANY,
            CountryHelper.COUNTRY_CODE_SWITZERLAND })
    public Date getQuarantineOrderedVerballyDate() {
        return quarantineOrderedVerballyDate;
    }

    @Order(67)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = QUARANTINE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    @HideForCountriesExcept(countries = {
            CountryHelper.COUNTRY_CODE_GERMANY,
            CountryHelper.COUNTRY_CODE_SWITZERLAND })
    public Date getQuarantineOrderedOfficialDocumentDate() {
        return quarantineOrderedOfficialDocumentDate;
    }

    @Order(68)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = QUARANTINE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    @HideForCountriesExcept(countries = {
            CountryHelper.COUNTRY_CODE_GERMANY,
            CountryHelper.COUNTRY_CODE_SWITZERLAND })
    public boolean isQuarantineOfficialOrderSent() {
        return quarantineOfficialOrderSent;
    }

    @Order(69)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = QUARANTINE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    @HideForCountriesExcept(countries = {
            CountryHelper.COUNTRY_CODE_GERMANY,
            CountryHelper.COUNTRY_CODE_SWITZERLAND })
    public Date getQuarantineOfficialOrderSentDate() {
        return quarantineOfficialOrderSentDate;
    }

    @Order(70)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = QUARANTINE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public boolean isQuarantineExtended() {
        return quarantineExtended;
    }

    @Order(71)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = QUARANTINE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public boolean isQuarantineReduced() {
        return quarantineReduced;
    }

    @Order(72)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(MAX_SOURCE_CASE_CLASSIFICATION)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public CaseClassification getMaxSourceCaseClassification() {
        return maxSourceCaseClassification;
    }

    @Order(73)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(ASSOCIATED_WITH_OUTBREAK)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAssociatedWithOutbreak() {
        return associatedWithOutbreak;
    }

    public void setMaxSourceCaseClassification(CaseClassification maxSourceCaseClassification) {
        this.maxSourceCaseClassification = maxSourceCaseClassification;
    }

    @Order(74)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(HospitalizationDto.class)
    @ExportProperty({
            CaseDataDto.HOSPITALIZATION,
            HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY })
    @ExportGroup(ExportGroupType.HOSPITALIZATION)
    public YesNoUnknown getAdmittedToHealthFacility() {
        return admittedToHealthFacility;
    }

    @Order(75)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(HospitalizationDto.class)
    @ExportProperty({
            CaseDataDto.HOSPITALIZATION,
            HospitalizationDto.ADMISSION_DATE })
    @ExportGroup(ExportGroupType.HOSPITALIZATION)
    public Date getAdmissionDate() {
        return admissionDate;
    }

    @Order(76)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(HospitalizationDto.class)
    @ExportProperty({
            CaseDataDto.HOSPITALIZATION,
            HospitalizationDto.DISCHARGE_DATE })
    @ExportGroup(ExportGroupType.HOSPITALIZATION)
    public Date getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(Date dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    @Order(77)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(HospitalizationDto.class)
    @ExportProperty({
            CaseDataDto.HOSPITALIZATION,
            HospitalizationDto.LEFT_AGAINST_ADVICE })
    @ExportGroup(ExportGroupType.HOSPITALIZATION)
    public YesNoUnknown getLeftAgainstAdvice() {
        return leftAgainstAdvice;
    }

    public void setLeftAgainstAdvice(YesNoUnknown leftAgainstAdvice) {
        this.leftAgainstAdvice = leftAgainstAdvice;
    }

    @Order(78)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.PRESENT_CONDITION })
    @ExportGroup(ExportGroupType.PERSON)
    public PresentCondition getPresentCondition() {
        return presentCondition;
    }

    @Order(79)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.DEATH_DATE })
    @ExportGroup(ExportGroupType.SENSITIVE)
    public Date getDeathDate() {
        return deathDate;
    }

    @Order(80)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(BURIAL_INFO)
    @ExportGroup(ExportGroupType.SENSITIVE)
    @HideForCountries
    public BurialInfoDto getBurialInfo() {
        return burialInfo;
    }

    @Order(81)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(LocationDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.ADDRESS,
            LocationDto.REGION })
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getAddressRegion() {
        return addressRegion;
    }

    @Order(82)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(LocationDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.ADDRESS,
            LocationDto.DISTRICT })
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getAddressDistrict() {
        return addressDistrict;
    }

    @Order(83)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(LocationDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.ADDRESS,
            LocationDto.COMMUNITY })
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getAddressCommunity() {
        return addressCommunity;
    }

    @Order(84)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(LocationDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.ADDRESS,
            LocationDto.CITY })
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getCity() {
        return city;
    }

    @Order(85)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(LocationDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.ADDRESS,
            LocationDto.STREET })
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getStreet() {
        return street;
    }

    @Order(86)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(LocationDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.ADDRESS,
            LocationDto.HOUSE_NUMBER })
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getHouseNumber() {
        return houseNumber;
    }

    @Order(87)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(LocationDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.ADDRESS,
            LocationDto.ADDITIONAL_INFORMATION })
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    @Order(88)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(LocationDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.ADDRESS,
            LocationDto.POSTAL_CODE })
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getPostalCode() {
        return postalCode;
    }

    @Order(89)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(ADDRESS_GPS_COORDINATES)
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getAddressGpsCoordinates() {
        return addressGpsCoordinates;
    }

    @Order(90)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(LocationDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.ADDRESS,
            LocationDto.FACILITY })
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getFacility() {
        return facility;
    }

    @Order(91)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(LocationDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.ADDRESS,
            LocationDto.FACILITY_DETAILS })
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getFacilityDetails() {
        return facilityDetails;
    }

    @Order(92)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.PHONE })
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getPhone() {
        return phone;
    }

    @Order(93)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.PHONE_OWNER })
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getPhoneOwner() {
        return phoneOwner;
    }

    @Order(94)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.EMAIL_ADDRESS })
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getEmailAddress() {
        return emailAddress;
    }

    @Order(95)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.OTHER_CONTACT_DETAILS })
    @ExportGroup(ExportGroupType.SENSITIVE)
    public String getOtherContactDetails() {
        return otherContactDetails;
    }

    @Order(96)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.EDUCATION_TYPE })
    @ExportGroup(ExportGroupType.PERSON)
    @HideForCountries(countries = {
            CountryHelper.COUNTRY_CODE_GERMANY,
            CountryHelper.COUNTRY_CODE_FRANCE })
    public EducationType getEducationType() {
        return educationType;
    }

    @Order(97)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.EDUCATION_DETAILS })
    @ExportGroup(ExportGroupType.PERSON)
    public String getEducationDetails() {
        return educationDetails;
    }

    @Order(98)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.OCCUPATION_TYPE })
    @ExportGroup(ExportGroupType.PERSON)
    public OccupationType getOccupationType() {
        return occupationType;
    }

    @Order(99)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.OCCUPATION_DETAILS })
    @ExportGroup(ExportGroupType.PERSON)
    public String getOccupationDetails() {
        return occupationDetails;
    }

    @Order(100)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.ARMED_FORCES_RELATION_TYPE })
    @ExportGroup(ExportGroupType.PERSON)
    public ArmedForcesRelationType getArmedForcesRelationType() {
        return armedForcesRelationType;
    }

    @Order(101)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(TRAVELED)
    @ExportGroup(ExportGroupType.EPIDEMIOLOGICAL)
    public boolean isTraveled() {
        return traveled;
    }

    public void setTraveled(boolean traveled) {
        this.traveled = traveled;
    }

    @Order(102)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(TRAVEL_HISTORY)
    @ExportGroup(ExportGroupType.EPIDEMIOLOGICAL)
    public String getTravelHistory() {
        return travelHistory;
    }

    @Order(103)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(BURIAL_ATTENDED)
    @ExportGroup(ExportGroupType.EPIDEMIOLOGICAL)
    public boolean isBurialAttended() {
        return burialAttended;
    }

    public void setBurialAttended(boolean burialAttended) {
        this.burialAttended = burialAttended;
    }

    @Order(104)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportEntity(EpiDataDto.class)
    @ExportProperty({
            CaseDataDto.EPI_DATA,
            EpiDataDto.CONTACT_WITH_SOURCE_CASE_KNOWN })
    @ExportGroup(ExportGroupType.EPIDEMIOLOGICAL)
    public YesNoUnknown getContactWithSourceCaseKnown() {
        return contactWithSourceCaseKnown;
    }

    public void setContactWithSourceCaseKnown(YesNoUnknown contactWithSourceCaseKnown) {
        this.contactWithSourceCaseKnown = contactWithSourceCaseKnown;
    }

    @Order(105)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.VACCINATION_STATUS)
    @ExportGroup(ExportGroupType.VACCINATION)
    public VaccinationStatus getVaccinationStatus() {
        return vaccinationStatus;
    }

    @Order(106)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(ImmunizationDto.NUMBER_OF_DOSES)
    @ExportGroup(ExportGroupType.VACCINATION)
    public String getNumberOfDoses() {
        return numberOfDoses;
    }

    @Order(107)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(VaccinationDto.VACCINATION_INFO_SOURCE)
    @ExportGroup(ExportGroupType.VACCINATION)
    public VaccinationInfoSource getVaccinationInfoSource() {
        return vaccinationInfoSource;
    }

    @Order(108)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(ImmunizationDto.FIRST_VACCINATION_DATE)
    @ExportGroup(ExportGroupType.VACCINATION)
    public Date getFirstVaccinationDate() {
        return firstVaccinationDate;
    }

    @Order(109)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(ImmunizationDto.LAST_VACCINATION_DATE)
    @ExportGroup(ExportGroupType.VACCINATION)
    public Date getLastVaccinationDate() {
        return lastVaccinationDate;
    }

    @Order(110)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(VaccinationDto.VACCINE_NAME)
    @ExportGroup(ExportGroupType.VACCINATION)
    public Vaccine getVaccineName() {
        return vaccineName;
    }

    @Order(111)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(VaccinationDto.OTHER_VACCINE_NAME)
    @ExportGroup(ExportGroupType.VACCINATION)
    public String getOtherVaccineName() {
        return otherVaccineName;
    }

    @Order(112)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(VaccinationDto.VACCINE_MANUFACTURER)
    @ExportGroup(ExportGroupType.VACCINATION)
    public VaccineManufacturer getVaccineManufacturer() {
        return vaccineManufacturer;
    }

    @Order(113)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(VaccinationDto.OTHER_VACCINE_MANUFACTURER)
    @ExportGroup(ExportGroupType.VACCINATION)
    public String getOtherVaccineManufacturer() {
        return otherVaccineManufacturer;
    }

    @Order(114)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(VaccinationDto.VACCINE_INN)
    @ExportGroup(ExportGroupType.VACCINATION)
    public String getVaccineInn() {
        return vaccineInn;
    }

    @Order(115)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(VaccinationDto.VACCINE_BATCH_NUMBER)
    @ExportGroup(ExportGroupType.VACCINATION)
    public String getVaccineBatchNumber() {
        return vaccineBatchNumber;
    }

    @Order(116)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(VaccinationDto.VACCINE_UNII_CODE)
    @ExportGroup(ExportGroupType.VACCINATION)
    public String getVaccineUniiCode() {
        return vaccineUniiCode;
    }

    @Order(117)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(VaccinationDto.VACCINE_ATC_CODE)
    @ExportGroup(ExportGroupType.VACCINATION)
    public String getVaccineAtcCode() {
        return vaccineAtcCode;
    }

//	@Order(97)
//	public Date getOnsetDate() {
//		return onsetDate;
//	}
//
//	@Order(98)
//	public String getSymptoms() {
//		return symptoms;
//	}

    @Order(121)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.SYMPTOMS)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public SymptomsDto getSymptoms() {
        return symptoms;
    }

    //sample data starts form here
    @Order(122)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSampleUuid() {
        return sampleUuid;
    }

    @Order(123)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLabSampleId() {
        return labSampleId;
    }

    @Order(124)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public Date getSampleReportDate() {
        return sampleReportDate;
    }

    @Order(125)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public Date getSampleDateTime() {
        return sampleDateTime;
    }

    //	@Order(126)
//	@ExportTarget(caseExportTypes = {
//			CaseExportType.CASE_SURVEILLANCE })
//	@ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//	@ExportGroup(ExportGroupType.ADDITIONAL)
    public SampleMaterial getSampleMaterial() {
        return sampleMaterial;
    }

    //	@Order(127)
//	@ExportTarget(caseExpor tTypes = {
//			CaseExportType.CASE_SURVEILLANCE })
//	@ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//	@ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSampleMaterialDetails() {
        return sampleMaterialDetails;
    }

    @Order(126)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSampleMaterialString() {
        return sampleMaterialString;
    }

    public SampleExportMaterial getSampleSampleExportMaterial() {
        return sampleSampleExportMaterial;
    }

    @Order(128)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSamplePurpose() {
        return samplePurpose;
    }

    @Order(129)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public SampleSource getSampleSource() {
        return sampleSource;
    }

    @Order(130)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public SamplingReason getSamplingReason() {
        return samplingReason;
    }

    @Order(131)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSamplingReasonDetails() {
        return samplingReasonDetails;
    }

    @Order(132)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLaboratory() {
        return laboratory;
    }

    @Order(134)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public PathogenTestResultType getPathogenTestResult() {
        return pathogenTestResult;
    }

    @Order(135)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public Boolean getPathogenTestingRequested() {
        return pathogenTestingRequested;
    }

    @Order(136)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public Set<PathogenTestType> getRequestedPathogenTests() {
        return requestedPathogenTests;
    }

    @Order(137)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRequestedOtherPathogenTests() {
        return requestedOtherPathogenTests;
    }

    @Order(138)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public Boolean getAdditionalTestingRequested() {
        return additionalTestingRequested;
    }

    @Order(139)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public Set<AdditionalTestType> getRequestedAdditionalTests() {
        return requestedAdditionalTests;
    }

    @Order(140)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRequestedOtherAdditionalTests() {
        return requestedOtherAdditionalTests;
    }

    @Order(141)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public Boolean getShipped() {
        return shipped;
    }

    @Order(142)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public Date getShipmentDate() {
        return shipmentDate;
    }

    @Order(143)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getShipmentDetails() {
        return shipmentDetails;
    }

    @Order(144)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public Boolean getReceived() {
        return received;
    }

    @Order(145)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public Date getReceivedDate() {
        return receivedDate;
    }

    @Order(146)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public SpecimenCondition getSpecimenCondition() {
        return specimenCondition;
    }

    @Order(147)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNoTestPossibleReason() {
        return noTestPossibleReason;
    }

    @Order(148)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getComment() {
        return comment;
    }



//    @Order(149)
//    @ExportTarget(caseExportTypes = {
//            CaseExportType.CASE_SURVEILLANCE })
//    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//    @ExportGroup(ExportGroupType.ADDITIONAL)
//    public String getPathogenTestType1() {
//        return pathogenTest1.testTypeText;
//    }
//
//    @Order(150)
//    @ExportTarget(caseExportTypes = {
//            CaseExportType.CASE_SURVEILLANCE })
//    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//    @ExportGroup(ExportGroupType.ADDITIONAL)
//    public String getPathogenTestDisease1() {
//        return pathogenTest1.disease;
//    }
//
//    @Order(151)
//    @ExportTarget(caseExportTypes = {
//            CaseExportType.CASE_SURVEILLANCE })
//    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//    @ExportGroup(ExportGroupType.ADDITIONAL)
//    public Date getPathogenTestDateTime1() {
//        return pathogenTest1.dateTime;
//    }
//
//    @Order(152)
//    @ExportTarget(caseExportTypes = {
//            CaseExportType.CASE_SURVEILLANCE })
//    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//    @ExportGroup(ExportGroupType.ADDITIONAL)
//    public String getPathogenTestLab1() {
//        return pathogenTest1.lab;
//    }
//
//    @Order(153)
//    @ExportTarget(caseExportTypes = {
//            CaseExportType.CASE_SURVEILLANCE })
//    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//    @ExportGroup(ExportGroupType.ADDITIONAL)
//    public PathogenTestResultType getPathogenTestResult1() {
//        return pathogenTest1.testResult;
//    }
//
//    @Order(154)
//    @ExportTarget(caseExportTypes = {
//            CaseExportType.CASE_SURVEILLANCE })
//    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//    @ExportGroup(ExportGroupType.ADDITIONAL)
//    public Boolean getPathogenTestVerified1() {
//        return pathogenTest1.verified;
//    }
//
//    @Order(155)
//    @ExportTarget(caseExportTypes = {
//            CaseExportType.CASE_SURVEILLANCE })
//    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//    @ExportGroup(ExportGroupType.ADDITIONAL)
//    public String getPathogenTestType2() {
//        return pathogenTest2.testTypeText;
//    }
//
//    @Order(156)
//    @ExportTarget(caseExportTypes = {
//            CaseExportType.CASE_SURVEILLANCE })
//    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//    @ExportGroup(ExportGroupType.ADDITIONAL)
//    public String getPathogenTestDisease2() {
//        return pathogenTest2.disease;
//    }
//
//    @Order(157)
//    @ExportTarget(caseExportTypes = {
//            CaseExportType.CASE_SURVEILLANCE })
//    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//    @ExportGroup(ExportGroupType.ADDITIONAL)
//    public Date getPathogenTestDateTime2() {
//        return pathogenTest2.dateTime;
//    }
//
//    @Order(158)
//    @ExportTarget(caseExportTypes = {
//            CaseExportType.CASE_SURVEILLANCE })
//    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//    @ExportGroup(ExportGroupType.ADDITIONAL)
//    public String getPathogenTestLab2() {
//        return pathogenTest2.lab;
//    }
//
//    @Order(159)
//    @ExportTarget(caseExportTypes = {
//            CaseExportType.CASE_SURVEILLANCE })
//    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//    @ExportGroup(ExportGroupType.ADDITIONAL)
//    public PathogenTestResultType getPathogenTestResult2() {
//        return pathogenTest2.testResult;
//    }
//
//    @Order(160)
//    @ExportTarget(caseExportTypes = {
//            CaseExportType.CASE_SURVEILLANCE })
//    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//    @ExportGroup(ExportGroupType.ADDITIONAL)
//    public Boolean getPathogenTestVerified2() {
//        return pathogenTest2.verified;
//    }
//
//    @Order(161)
//    @ExportTarget(caseExportTypes = {
//            CaseExportType.CASE_SURVEILLANCE })
//    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//    @ExportGroup(ExportGroupType.ADDITIONAL)
//    public String getPathogenTestType3() {
//        return pathogenTest3.testTypeText;
//    }
//
//    @Order(162)
//    @ExportTarget(caseExportTypes = {
//            CaseExportType.CASE_SURVEILLANCE })
//    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//    @ExportGroup(ExportGroupType.ADDITIONAL)
//    public String getPathogenTestDisease3() {
//        return pathogenTest3.disease;
//    }
//
//    @Order(163)
//    @ExportTarget(caseExportTypes = {
//            CaseExportType.CASE_SURVEILLANCE })
//    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//    @ExportGroup(ExportGroupType.ADDITIONAL)
//    public Date getPathogenTestDateTime3() {
//        return pathogenTest3.dateTime;
//    }
//
//    @Order(164)
//    @ExportTarget(caseExportTypes = {
//            CaseExportType.CASE_SURVEILLANCE })
//    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//    @ExportGroup(ExportGroupType.ADDITIONAL)
//    public String getPathogenTestLab3() {
//        return pathogenTest3.lab;
//    }
//
//    @Order(165)
//    @ExportTarget(caseExportTypes = {
//            CaseExportType.CASE_SURVEILLANCE })
//    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//    @ExportGroup(ExportGroupType.ADDITIONAL)
//    public PathogenTestResultType getPathogenTestResult3() {
//        return pathogenTest3.testResult;
//    }
//
//    @Order(166)
//    @ExportTarget(caseExportTypes = {
//            CaseExportType.CASE_SURVEILLANCE })
//    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
//    @ExportGroup(ExportGroupType.ADDITIONAL)
//    public Boolean getPathogenTestVerified3() {
//        return pathogenTest3.verified;
//    }

    @Order(167)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherPathogenTestsDetails() {
        return otherPathogenTests;
    }


    @Order(168)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.HEALTH_CONDITIONS)
    @ExportGroup(ExportGroupType.CLINICAL_COURSE)
    public HealthConditionsDto getHealthConditions() {
        return healthConditions;
    }

    @Order(169)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(NUMBER_OF_PRESCRIPTIONS)
    @ExportGroup(ExportGroupType.THERAPY)
    public int getNumberOfPrescriptions() {
        return numberOfPrescriptions;
    }

    @Order(170)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(NUMBER_OF_TREATMENTS)
    @ExportGroup(ExportGroupType.THERAPY)
    public int getNumberOfTreatments() {
        return numberOfTreatments;
    }

    @Order(171)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(NUMBER_OF_CLINICAL_VISITS)
    @ExportGroup(ExportGroupType.CLINICAL_COURSE)
    public int getNumberOfClinicalVisits() {
        return numberOfClinicalVisits;
    }

    @Order(172)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(CaseDataDto.FOLLOW_UP_STATUS)
    @ExportGroup(ExportGroupType.FOLLOW_UP)
    public FollowUpStatus getFollowUpStatus() {
        return followUpStatus;
    }

    @Order(173)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(CaseDataDto.FOLLOW_UP_UNTIL)
    @ExportGroup(ExportGroupType.FOLLOW_UP)
    public Date getFollowUpUntil() {
        return followUpUntil;
    }

    @Order(174)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(CaseExportDetailedSampleDto.NUMBER_OF_VISITS)
    @ExportGroup(ExportGroupType.FOLLOW_UP)
    public int getNumberOfVisits() {
        return numberOfVisits;
    }

    @Order(175)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(CaseExportDetailedSampleDto.LAST_COOPERATIVE_VISIT_SYMPTOMATIC)
    @ExportGroup(ExportGroupType.FOLLOW_UP)
    public YesNoUnknown getLastCooperativeVisitSymptomatic() {
        return lastCooperativeVisitSymptomatic;
    }

    @Order(176)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(CaseExportDetailedSampleDto.LAST_COOPERATIVE_VISIT_DATE)
    @ExportGroup(ExportGroupType.FOLLOW_UP)
    public Date getLastCooperativeVisitDate() {
        return lastCooperativeVisitDate;
    }

    @Order(177)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(CaseExportDetailedSampleDto.LAST_COOPERATIVE_VISIT_SYMPTOMS)
    @ExportGroup(ExportGroupType.FOLLOW_UP)
    public String getLastCooperativeVisitSymptoms() {
        return lastCooperativeVisitSymptoms;
    }

    @Order(178)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(CaseExportDetailedSampleDto.EVENT_COUNT)
    @ExportGroup(ExportGroupType.EVENT)
    public Long getEventCount() {
        return eventCount;
    }

    @Order(179)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(CaseExportDetailedSampleDto.LATEST_EVENT_ID)
    @ExportGroup(ExportGroupType.EVENT)
    public String getLatestEventId() {
        return latestEventId;
    }

    @Order(180)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(CaseExportDetailedSampleDto.LATEST_EVENT_STATUS)
    @ExportGroup(ExportGroupType.EVENT)
    public EventStatus getLatestEventStatus() {
        return latestEventStatus;
    }

    @Order(181)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(CaseExportDetailedSampleDto.LATEST_EVENT_TITLE)
    @ExportGroup(ExportGroupType.EVENT)
    public String getLatestEventTitle() {
        return latestEventTitle;
    }

    @Order(182)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.BIRTH_NAME })
    @ExportGroup(ExportGroupType.SENSITIVE)
    @HideForCountriesExcept
    public String getBirthName() {
        return birthName;
    }

    @Order(183)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.BIRTH_COUNTRY })
    @ExportGroup(ExportGroupType.SENSITIVE)
    @HideForCountriesExcept
    public String getBirthCountry() {
        return birthCountry;
    }

    @Order(184)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportEntity(PersonDto.class)
    @ExportProperty({
            CaseDataDto.PERSON,
            PersonDto.CITIZENSHIP })
    @ExportGroup(ExportGroupType.SENSITIVE)
    @HideForCountriesExcept
    public String getCitizenship() {
        return citizenship;
    }

    @Order(185)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(CaseDataDto.NOT_A_CASE_REASON_NEGATIVE_TEST)
    @ExportGroup(ExportGroupType.CORE)
    public Boolean getNotACaseReasonNegativeTest() {
        return notACaseReasonNegativeTest;
    }

    @Order(186)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(CaseDataDto.NOT_A_CASE_REASON_PHYSICIAN_INFORMATION)
    @ExportGroup(ExportGroupType.CORE)
    public Boolean getNotACaseReasonPhysicianInformation() {
        return notACaseReasonPhysicianInformation;
    }

    @Order(187)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(CaseDataDto.NOT_A_CASE_REASON_DIFFERENT_PATHOGEN)
    @ExportGroup(ExportGroupType.CORE)
    public Boolean getNotACaseReasonDifferentPathogen() {
        return notACaseReasonDifferentPathogen;
    }

    @Order(188)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(CaseDataDto.NOT_A_CASE_REASON_OTHER)
    @ExportGroup(ExportGroupType.CORE)
    public Boolean getNotACaseReasonOther() {
        return notACaseReasonOther;
    }

    @Order(189)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(CaseDataDto.NOT_A_CASE_REASON_DETAILS)
    @ExportGroup(ExportGroupType.CORE)
    public String getNotACaseReasonDetails() {
        return notACaseReasonDetails;
    }

    @Order(190)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.CASE_IDENTIFICATION_SOURCE)
    @ExportGroup(ExportGroupType.CORE)
    @HideForCountriesExcept
    public CaseIdentificationSource getCaseIdentificationSource() {
        return caseIdentificationSource;
    }

    @Order(191)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.SCREENING_TYPE)
    @ExportGroup(ExportGroupType.CORE)
    @HideForCountriesExcept
    public ScreeningType getScreeningType() {
        return screeningType;
    }

    @Order(192)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.CLINICIAN_NAME)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getClinicianName() {
        return clinicianName;
    }

    @Order(193)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.CLINICIAN_PHONE)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getClinicianPhone() {
        return clinicianPhone;
    }

    @Order(194)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(CaseDataDto.CLINICIAN_EMAIL)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getClinicianEmail() {
        return clinicianEmail;
    }

    public Long getReportingUserId() {
        return reportingUserId;
    }

    public Long getFollowUpStatusChangeUserId() {
        return followUpStatusChangeUserId;
    }

    @Order(195)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = CaseDataDto.REPORTING_USER, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getReportingUserName() {
        return reportingUserName;
    }

    public void setReportingUserName(String reportingUserName) {
        this.reportingUserName = reportingUserName;
    }

    @Order(196)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = CaseDataDto.REPORTING_USER, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getReportingUserRoles() {
        return reportingUserRoles;
    }

    public void setReportingUserRoles(Set<UserRoleReferenceDto> roles) {
        this.reportingUserRoles = StringUtils.join(roles, ", ");
    }

    @Order(197)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = CaseDataDto.FOLLOW_UP_STATUS_CHANGE_USER, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFollowUpStatusChangeUserName() {
        return followUpStatusChangeUserName;
    }

    public void setFollowUpStatusChangeUserName(String followUpStatusChangeUserName) {
        this.followUpStatusChangeUserName = followUpStatusChangeUserName;
    }

    @Order(198)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE,
            CaseExportType.CASE_MANAGEMENT })
    @ExportProperty(value = CaseDataDto.FOLLOW_UP_STATUS_CHANGE_USER, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFollowUpStatusChangeUserRoles() {
        return followUpStatusChangeUserRoles;
    }

    public void setFollowUpStatusChangeUserRoles(Set<UserRoleReferenceDto> roles) {
        this.followUpStatusChangeUserRoles = StringUtils.join(roles, ", ");
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public void setEpiDataId(long epiDataId) {
        this.epiDataId = epiDataId;
    }

    public long getHealthConditionsId() {
        return healthConditionsId;
    }

    public void setHealthConditionsId(long healthConditionsId) {
        this.healthConditionsId = healthConditionsId;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public void setDiseaseDetails(String diseaseDetails) {
        this.diseaseDetails = diseaseDetails;
    }

    public void setDiseaseVariantDetails(String diseaseVariantDetails) {
        this.diseaseVariantDetails = diseaseVariantDetails;
    }

    public void setPersonUuid(String personUuid) {
        this.personUuid = personUuid;
    }

    public void setHospitalizationId(long hospitalizationId) {
        this.hospitalizationId = hospitalizationId;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setEpidNumber(String epidNumber) {
        this.epidNumber = epidNumber;
    }

    public void setDiseaseVariant(DiseaseVariant diseaseVariant) {
        this.diseaseVariant = diseaseVariant;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSalutation(Salutation salutation) {
        this.salutation = salutation;
    }

    public void setOtherSalutation(String otherSalutation) {
        this.otherSalutation = otherSalutation;
    }

    public void setBirthdate(BirthDateDto birthdate) {
        this.birthdate = birthdate;
    }

    public void setFacilityType(FacilityType facilityType) {
        this.facilityType = facilityType;
    }

    public void setHealthFacilityDetails(String healthFacilityDetails) {
        this.healthFacilityDetails = healthFacilityDetails;
    }

    public void setPointOfEntryDetails(String pointOfEntryDetails) {
        this.pointOfEntryDetails = pointOfEntryDetails;
    }

    public void setInvestigatedDate(Date investigatedDate) {
        this.investigatedDate = investigatedDate;
    }

    public void setSequelae(YesNoUnknown sequelae) {
        this.sequelae = sequelae;
    }

    public void setSequelaeDetails(String sequelaeDetails) {
        this.sequelaeDetails = sequelaeDetails;
    }

    public void setBloodOrganOrTissueDonated(YesNoUnknown bloodOrganOrTissueDonated) {
        this.bloodOrganOrTissueDonated = bloodOrganOrTissueDonated;
    }

    public void setBurialInfo(BurialInfoDto burialInfo) {
        this.burialInfo = burialInfo;
    }

    public void setAddressRegion(String addressRegion) {
        this.addressRegion = addressRegion;
    }

    public void setAddressDistrict(String addressDistrict) {
        this.addressDistrict = addressDistrict;
    }

    public void setAddressCommunity(String addressCommunity) {
        this.addressCommunity = addressCommunity;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setFacilityDetails(String facilityDetails) {
        this.facilityDetails = facilityDetails;
    }

    public void setPhoneOwner(String phoneOwner) {
        this.phoneOwner = phoneOwner;
    }

    public void setOccupationType(OccupationType occupationType) {
        this.occupationType = occupationType;
    }

    public void setOccupationDetails(String occupationDetails) {
        this.occupationDetails = occupationDetails;
    }

    public void setEducationType(EducationType educationType) {
        this.educationType = educationType;
    }

    public void setEducationDetails(String educationDetails) {
        this.educationDetails = educationDetails;
    }

    public void setNosocomialOutbreak(Boolean nosocomialOutbreak) {
        this.nosocomialOutbreak = nosocomialOutbreak;
    }

    public void setInfectionSetting(InfectionSetting infectionSetting) {
        this.infectionSetting = infectionSetting;
    }

    public void setProhibitionToWork(YesNoUnknown prohibitionToWork) {
        this.prohibitionToWork = prohibitionToWork;
    }

    public void setProhibitionToWorkFrom(Date prohibitionToWorkFrom) {
        this.prohibitionToWorkFrom = prohibitionToWorkFrom;
    }

    public void setProhibitionToWorkUntil(Date prohibitionToWorkUntil) {
        this.prohibitionToWorkUntil = prohibitionToWorkUntil;
    }

    public void setReInfection(YesNoUnknown reInfection) {
        this.reInfection = reInfection;
    }

    public void setPreviousInfectionDate(Date previousInfectionDate) {
        this.previousInfectionDate = previousInfectionDate;
    }

    public void setReinfectionStatus(ReinfectionStatus reinfectionStatus) {
        this.reinfectionStatus = reinfectionStatus;
    }

    public void setReinfectionDetails(String reinfectionDetails) {
        this.reinfectionDetails = reinfectionDetails;
    }

    public void setQuarantine(QuarantineType quarantine) {
        this.quarantine = quarantine;
    }

    public void setQuarantineTypeDetails(String quarantineTypeDetails) {
        this.quarantineTypeDetails = quarantineTypeDetails;
    }

    public void setQuarantineFrom(Date quarantineFrom) {
        this.quarantineFrom = quarantineFrom;
    }

    public void setQuarantineTo(Date quarantineTo) {
        this.quarantineTo = quarantineTo;
    }

    public void setQuarantineHelpNeeded(String quarantineHelpNeeded) {
        this.quarantineHelpNeeded = quarantineHelpNeeded;
    }

    public void setQuarantineOrderedVerbally(boolean quarantineOrderedVerbally) {
        this.quarantineOrderedVerbally = quarantineOrderedVerbally;
    }

    public void setQuarantineOrderedOfficialDocument(boolean quarantineOrderedOfficialDocument) {
        this.quarantineOrderedOfficialDocument = quarantineOrderedOfficialDocument;
    }

    public void setQuarantineOrderedVerballyDate(Date quarantineOrderedVerballyDate) {
        this.quarantineOrderedVerballyDate = quarantineOrderedVerballyDate;
    }

    public void setQuarantineOrderedOfficialDocumentDate(Date quarantineOrderedOfficialDocumentDate) {
        this.quarantineOrderedOfficialDocumentDate = quarantineOrderedOfficialDocumentDate;
    }

    public void setQuarantineExtended(boolean quarantineExtended) {
        this.quarantineExtended = quarantineExtended;
    }

    public void setQuarantineReduced(boolean quarantineReduced) {
        this.quarantineReduced = quarantineReduced;
    }

    public void setQuarantineOfficialOrderSent(boolean quarantineOfficialOrderSent) {
        this.quarantineOfficialOrderSent = quarantineOfficialOrderSent;
    }

    public void setQuarantineOfficialOrderSentDate(Date quarantineOfficialOrderSentDate) {
        this.quarantineOfficialOrderSentDate = quarantineOfficialOrderSentDate;
    }

    public void setEventCount(Long eventCount) {
        this.eventCount = eventCount;
    }

    public void setBirthName(String birthName) {
        this.birthName = birthName;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public void setResponsibleRegion(String responsibleRegion) {
        this.responsibleRegion = responsibleRegion;
    }

    public void setResponsibleDistrict(String responsibleDistrict) {
        this.responsibleDistrict = responsibleDistrict;
    }

    public void setResponsibleCommunity(String responsibleCommunity) {
        this.responsibleCommunity = responsibleCommunity;
    }

    public void setClinicianName(String clinicianName) {
        this.clinicianName = clinicianName;
    }

    public void setClinicianPhone(String clinicianPhone) {
        this.clinicianPhone = clinicianPhone;
    }

    public void setClinicianEmail(String clinicianEmail) {
        this.clinicianEmail = clinicianEmail;
    }

    public void setReportingUserId(Long reportingUserId) {
        this.reportingUserId = reportingUserId;
    }

    public void setFollowUpStatusChangeUserId(Long followUpStatusChangeUserId) {
        this.followUpStatusChangeUserId = followUpStatusChangeUserId;
    }

    public void setReportingUserRoles(String reportingUserRoles) {
        this.reportingUserRoles = reportingUserRoles;
    }

    public void setFollowUpStatusChangeUserRoles(String followUpStatusChangeUserRoles) {
        this.followUpStatusChangeUserRoles = followUpStatusChangeUserRoles;
    }

    public void setInJurisdiction(Boolean inJurisdiction) {
        isInJurisdiction = inJurisdiction;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public void setPregnant(YesNoUnknown pregnant) {
        this.pregnant = pregnant;
    }

    public void setTrimester(Trimester trimester) {
        this.trimester = trimester;
    }

    public void setPostpartum(YesNoUnknown postpartum) {
        this.postpartum = postpartum;
    }

    public void setApproximateAge(String age) {
        this.approximateAge = age;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    public void setHealthFacility(String healthFacility) {
        this.healthFacility = healthFacility;
    }

    public void setPointOfEntry(String pointOfEntry) {
        this.pointOfEntry = pointOfEntry;
    }

    public void setAdmittedToHealthFacility(YesNoUnknown admittedToHealthFacility) {
        this.admittedToHealthFacility = admittedToHealthFacility;
    }

    public void setCaseClassification(CaseClassification caseClassification) {
        this.caseClassification = caseClassification;
    }

    public void setClinicalConfirmation(YesNoUnknown clinicalConfirmation) {
        this.clinicalConfirmation = clinicalConfirmation;
    }

    public void setEpidemiologicalConfirmation(YesNoUnknown epidemiologicalConfirmation) {
        this.epidemiologicalConfirmation = epidemiologicalConfirmation;
    }

    public void setLaboratoryDiagnosticConfirmation(YesNoUnknown laboratoryDiagnosticConfirmation) {
        this.laboratoryDiagnosticConfirmation = laboratoryDiagnosticConfirmation;
    }

    public void setNotACaseReasonNegativeTest(Boolean notACaseReasonNegativeTest) {
        this.notACaseReasonNegativeTest = notACaseReasonNegativeTest;
    }

    public void setNotACaseReasonPhysicianInformation(Boolean notACaseReasonPhysicianInformation) {
        this.notACaseReasonPhysicianInformation = notACaseReasonPhysicianInformation;
    }

    public void setNotACaseReasonDifferentPathogen(Boolean notACaseReasonDifferentPathogen) {
        this.notACaseReasonDifferentPathogen = notACaseReasonDifferentPathogen;
    }

    public void setNotACaseReasonOther(Boolean notACaseReasonOther) {
        this.notACaseReasonOther = notACaseReasonOther;
    }

    public void setNotACaseReasonDetails(String notACaseReasonDetails) {
        this.notACaseReasonDetails = notACaseReasonDetails;
    }

    public void setInvestigationStatus(InvestigationStatus investigationStatus) {
        this.investigationStatus = investigationStatus;
    }

    public void setPresentCondition(PresentCondition presentCondition) {
        this.presentCondition = presentCondition;
    }

    public void setOutcome(CaseOutcome outcome) {
        this.outcome = outcome;
    }

    public void setOutcomeDate(Date outcomeDate) {
        this.outcomeDate = outcomeDate;
    }

    public void setAssociatedWithOutbreak(String associatedWithOutbreak) {
        this.associatedWithOutbreak = associatedWithOutbreak;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public void setAddressGpsCoordinates(String addressGpsCoordinates) {
        this.addressGpsCoordinates = addressGpsCoordinates;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setArmedForcesRelationType(ArmedForcesRelationType armedForcesRelationType) {
        this.armedForcesRelationType = armedForcesRelationType;
    }

    public void setTravelHistory(String travelHistory) {
        this.travelHistory = travelHistory;
    }

    public void setInitialDetectionPlace(String initialDetectionPlace) {
        this.initialDetectionPlace = initialDetectionPlace;
    }

    public void setVaccinationStatus(VaccinationStatus vaccinationStatus) {
        this.vaccinationStatus = vaccinationStatus;
    }

    public void setNumberOfDoses(String numberOfDoses) {
        this.numberOfDoses = numberOfDoses;
    }

    public void setLastVaccinationDate(Date lastVaccinationDate) {
        this.lastVaccinationDate = lastVaccinationDate;
    }

    public void setVaccinationInfoSource(VaccinationInfoSource vaccinationInfoSource) {
        this.vaccinationInfoSource = vaccinationInfoSource;
    }

    public void setFirstVaccinationDate(Date firstVaccinationDate) {
        this.firstVaccinationDate = firstVaccinationDate;
    }

    public void setVaccineName(Vaccine vaccineName) {
        this.vaccineName = vaccineName;
    }

    public void setOtherVaccineName(String otherVaccineName) {
        this.otherVaccineName = otherVaccineName;
    }

    public void setVaccineManufacturer(VaccineManufacturer vaccineManufacturer) {
        this.vaccineManufacturer = vaccineManufacturer;
    }

    public void setOtherVaccineManufacturer(String otherVaccineManufacturer) {
        this.otherVaccineManufacturer = otherVaccineManufacturer;
    }

    public void setVaccineInn(String vaccineInn) {
        this.vaccineInn = vaccineInn;
    }

    public void setVaccineBatchNumber(String vaccineBatchNumber) {
        this.vaccineBatchNumber = vaccineBatchNumber;
    }

    public void setVaccineUniiCode(String vaccineUniiCode) {
        this.vaccineUniiCode = vaccineUniiCode;
    }

    public void setVaccineAtcCode(String vaccineAtcCode) {
        this.vaccineAtcCode = vaccineAtcCode;
    }

    public void setSymptoms(SymptomsDto symptoms) {
        this.symptoms = symptoms;
    }

    public void setHealthConditions(HealthConditionsDto healthConditions) {
        this.healthConditions = healthConditions;
    }

    public void setNumberOfPrescriptions(int numberOfPrescriptions) {
        this.numberOfPrescriptions = numberOfPrescriptions;
    }

    public void setNumberOfTreatments(int numberOfTreatments) {
        this.numberOfTreatments = numberOfTreatments;
    }

    public void setNumberOfClinicalVisits(int numberOfClinicalVisits) {
        this.numberOfClinicalVisits = numberOfClinicalVisits;
    }

    public void setFollowUpStatus(FollowUpStatus followUpStatus) {
        this.followUpStatus = followUpStatus;
    }

    public void setFollowUpUntil(Date followUpUntil) {
        this.followUpUntil = followUpUntil;
    }

    public void setNumberOfVisits(int numberOfVisits) {
        this.numberOfVisits = numberOfVisits;
    }

    public void setLastCooperativeVisitSymptomatic(YesNoUnknown lastCooperativeVisitSymptomatic) {
        this.lastCooperativeVisitSymptomatic = lastCooperativeVisitSymptomatic;
    }

    public void setLastCooperativeVisitDate(Date lastCooperativeVisitDate) {
        this.lastCooperativeVisitDate = lastCooperativeVisitDate;
    }

    public void setLastCooperativeVisitSymptoms(String lastCooperativeVisitSymptoms) {
        this.lastCooperativeVisitSymptoms = lastCooperativeVisitSymptoms;
    }

    public void setLatestEventId(String latestEventId) {
        this.latestEventId = latestEventId;
    }

    public void setLatestEventTitle(String latestEventTitle) {
        this.latestEventTitle = latestEventTitle;
    }

    public void setLatestEventStatus(EventStatus latestEventStatus) {
        this.latestEventStatus = latestEventStatus;
    }

    public void setExternalID(String externalID) {
        this.externalID = externalID;
    }

    public void setExternalToken(String externalToken) {
        this.externalToken = externalToken;
    }

    public void setInternalToken(String internalToken) {
        this.internalToken = internalToken;
    }

    public void setCaseIdentificationSource(CaseIdentificationSource caseIdentificationSource) {
        this.caseIdentificationSource = caseIdentificationSource;
    }

    public void setScreeningType(ScreeningType screeningType) {
        this.screeningType = screeningType;
    }

    public void setOtherContactDetails(String otherContactDetails) {
        this.otherContactDetails = otherContactDetails;
    }

    public void setPreviousQuarantineTo(Date previousQuarantineTo) {
        this.previousQuarantineTo = previousQuarantineTo;
    }

    public void setQuarantineChangeComment(String quarantineChangeComment) {
        this.quarantineChangeComment = quarantineChangeComment;
    }

    //setOtherPathogenTests
    public void setOtherPathogenTests(String otherPathogenTests) {
        this.otherPathogenTests = otherPathogenTests;
    }

    public void setSampleUuid(String sampleUuid) {this.sampleUuid = sampleUuid;}

    public void setLabSampleId(String labSampleId) {this.labSampleId = labSampleId;}

    public void setSampleReportDate(Date sampleReportDate) {this.sampleReportDate = sampleReportDate;}

//    public SampleExportPathogenTest getPathogenTest1() {
//        return pathogenTest1;
//    }
//
//    public void setPathogenTest1(SampleExportPathogenTest pathogenTest1) {
//        this.pathogenTest1 = pathogenTest1;
//    }
//
//    public SampleExportPathogenTest getPathogenTest2() {
//        return pathogenTest2;
//    }
//
//    public void setPathogenTest2(SampleExportPathogenTest pathogenTest2) {
//        this.pathogenTest2 = pathogenTest2;
//    }
//
//    public SampleExportPathogenTest getPathogenTest3() {
//        return pathogenTest3;
//    }
//
//    public void setPathogenTest3(SampleExportPathogenTest pathogenTest3) {
//        this.pathogenTest3 = pathogenTest3;
//    }


    public void setSampleDateTime(Date sampleDateTime) {this.sampleDateTime = sampleDateTime;}

    public void setSampleMaterial(SampleMaterial sampleMaterial) {this.sampleMaterial = sampleMaterial;}

    public void setSampleMaterialDetails(String sampleMaterialDetails) {this.sampleMaterialDetails = sampleMaterialDetails;}

    public void setSampleMaterialString(String sampleMaterialString) {this.sampleMaterialString = sampleMaterialString;}
    public void setSamplePurpose(String samplePurpose) {this.samplePurpose = samplePurpose;}

    public void setSampleSource(SampleSource sampleSource) {this.sampleSource = sampleSource;}

    public void setSamplingReason(SamplingReason samplingReason) {this.samplingReason = samplingReason;}

    public void setSamplingReasonDetails(String samplingReasonDetails) {this.samplingReasonDetails = samplingReasonDetails;}

    public void setLaboratory(String laboratory) {this.laboratory = laboratory;}

    public void setPathogenTestResult(PathogenTestResultType pathogenTestResult) {this.pathogenTestResult = pathogenTestResult;}

    public void setPathogenTestingRequested(Boolean pathogenTestingRequested) {this.pathogenTestingRequested = pathogenTestingRequested;}

    public void setRequestedPathogenTests(Set<PathogenTestType> requestedPathogenTests) {this.requestedPathogenTests = requestedPathogenTests;}

    public void setRequestedOtherPathogenTests(String requestedOtherPathogenTests) {this.requestedOtherPathogenTests = requestedOtherPathogenTests;}
    public void setAdditionalTestingRequested(Boolean additionalTestingRequested) {this.additionalTestingRequested = additionalTestingRequested;}

    public void setRequestedAdditionalTests(Set<AdditionalTestType> requestedAdditionalTests) {this.requestedAdditionalTests = requestedAdditionalTests;}

    public void setRequestedOtherAdditionalTests(String requestedOtherAdditionalTests) {this.requestedOtherAdditionalTests = requestedOtherAdditionalTests;}

    public void setShipped(Boolean shipped) {this.shipped = shipped;}

    public void setShipmentDate(Date shipmentDate) {this.shipmentDate = shipmentDate;}

    public void setShipmentDetails(String shipmentDetails) {this.shipmentDetails = shipmentDetails;}

    public void setReceived(Boolean received) {this.received = received;}

    public void setReceivedDate(Date receivedDate) {this.receivedDate = receivedDate;}

    public void setSpecimenCondition(SpecimenCondition specimenCondition) {this.specimenCondition = specimenCondition;}

    public void setNoTestPossibleReason(String noTestPossibleReason) {this.noTestPossibleReason = noTestPossibleReason;}

    public void setComment(String comment) {this.comment = comment;}

    public void setAfpAntiBodyDetection(String afpAntiBodyDetection) {
        this.afpAntiBodyDetection = afpAntiBodyDetection;
    }

    public void setAfpAntigenDetection(String afpAntigenDetection) {
        this.afpAntigenDetection = afpAntigenDetection;
    }

    public void setAfpRapidTest(String afpRapidTest) {
        this.afpRapidTest = afpRapidTest;
    }

    public void setAfpCulture(String afpCulture) {
        this.afpCulture = afpCulture;
    }

    public void setAfpHistopathology(String afpHistopathology) {
        this.afpHistopathology = afpHistopathology;
    }

    public void setAfpIsolation(String afpIsolation) {
        this.afpIsolation = afpIsolation;
    }

    public void setAfpIgmSerumAntibody(String afpIgmSerumAntibody) {
        this.afpIgmSerumAntibody = afpIgmSerumAntibody;
    }

    public void setAfpIggSerumAntibody(String afpIggSerumAntibody) {
        this.afpIggSerumAntibody = afpIggSerumAntibody;
    }

    public void setAfpIgaSerumAntibody(String afpIgaSerumAntibody) {
        this.afpIgaSerumAntibody = afpIgaSerumAntibody;
    }

    public void setAfpIncubationTime(String afpIncubationTime) {
        this.afpIncubationTime = afpIncubationTime;
    }

    public void setAfpIndirectFluorescentAntibody(String afpIndirectFluorescentAntibody) {
        this.afpIndirectFluorescentAntibody = afpIndirectFluorescentAntibody;
    }

    public void setAfpMicroscopy(String afpMicroscopy) {
        this.afpMicroscopy = afpMicroscopy;
    }

    public void setAfpNeutralizingAntibodies(String afpNeutralizingAntibodies) {
        this.afpNeutralizingAntibodies = afpNeutralizingAntibodies;
    }

    public void setAfpPcr(String afpPcr) {
        this.afpPcr = afpPcr;
    }

    public void setAfpGramStain(String afpGramStain) {
        this.afpGramStain = afpGramStain;
    }

    public void setAfpLatexAgglutination(String afpLatexAgglutination) {
        this.afpLatexAgglutination = afpLatexAgglutination;
    }

    public void setAfpCqValueDetection(String afpCqValueDetection) {
        this.afpCqValueDetection = afpCqValueDetection;
    }

    public void setAfpSeQuencing(String afpSeQuencing) {
        this.afpSeQuencing = afpSeQuencing;
    }

    public void setAfpDnaMicroArray(String afpDnaMicroArray) {
        this.afpDnaMicroArray = afpDnaMicroArray;
    }

    public void setAfpOther(String afpOther) {
        this.afpOther = afpOther;
    }

    public void setCholeraAntiBodyDetection(String choleraAntiBodyDetection) {
        this.choleraAntiBodyDetection = choleraAntiBodyDetection;
    }

    public void setCholeraAntigenDetection(String choleraAntigenDetection) {
        this.choleraAntigenDetection = choleraAntigenDetection;
    }

    public void setCholeraRapidTest(String choleraRapidTest) {
        this.choleraRapidTest = choleraRapidTest;
    }

    public void setCholeraCulture(String choleraCulture) {
        this.choleraCulture = choleraCulture;
    }

    public void setCholeraHistopathology(String choleraHistopathology) {
        this.choleraHistopathology = choleraHistopathology;
    }

    public void setCholeraIsolation(String choleraIsolation) {
        this.choleraIsolation = choleraIsolation;
    }

    public void setCholeraIgmSerumAntibody(String choleraIgmSerumAntibody) {
        this.choleraIgmSerumAntibody = choleraIgmSerumAntibody;
    }

    public void setCholeraIggSerumAntibody(String choleraIggSerumAntibody) {
        this.choleraIggSerumAntibody = choleraIggSerumAntibody;
    }

    public void setCholeraIgaSerumAntibody(String choleraIgaSerumAntibody) {
        this.choleraIgaSerumAntibody = choleraIgaSerumAntibody;
    }

    public void setCholeraIncubationTime(String choleraIncubationTime) {
        this.choleraIncubationTime = choleraIncubationTime;
    }

    public void setCholeraIndirectFluorescentAntibody(String choleraIndirectFluorescentAntibody) {
        this.choleraIndirectFluorescentAntibody = choleraIndirectFluorescentAntibody;
    }

    public void setCholeraMicroscopy(String choleraMicroscopy) {
        this.choleraMicroscopy = choleraMicroscopy;
    }

    public void setCholeraNeutralizingAntibodies(String choleraNeutralizingAntibodies) {
        this.choleraNeutralizingAntibodies = choleraNeutralizingAntibodies;
    }

    public void setCholeraPcr(String choleraPcr) {
        this.choleraPcr = choleraPcr;
    }

    public void setCholeraGramStain(String choleraGramStain) {
        this.choleraGramStain = choleraGramStain;
    }

    public void setCholeraLatexAgglutination(String choleraLatexAgglutination) {
        this.choleraLatexAgglutination = choleraLatexAgglutination;
    }

    public void setCholeraCqValueDetection(String choleraCqValueDetection) {
        this.choleraCqValueDetection = choleraCqValueDetection;
    }

    public void setCholeraSeQuencing(String choleraSeQuencing) {
        this.choleraSeQuencing = choleraSeQuencing;
    }

    public void setCholeraDnaMicroArray(String choleraDnaMicroArray) {
        this.choleraDnaMicroArray = choleraDnaMicroArray;
    }

    public void setCholeraOther(String choleraOther) {
        this.choleraOther = choleraOther;
    }

    public void setCongenitalRubellaAntiBodyDetection(String congenitalRubellaAntiBodyDetection) {
        this.congenitalRubellaAntiBodyDetection = congenitalRubellaAntiBodyDetection;
    }

    public void setCongenitalRubellaAntigenDetection(String congenitalRubellaAntigenDetection) {
        this.congenitalRubellaAntigenDetection = congenitalRubellaAntigenDetection;
    }

    public void setCongenitalRubellaRapidTest(String congenitalRubellaRapidTest) {
        this.congenitalRubellaRapidTest = congenitalRubellaRapidTest;
    }

    public void setCongenitalRubellaCulture(String congenitalRubellaCulture) {
        this.congenitalRubellaCulture = congenitalRubellaCulture;
    }

    public void setCongenitalRubellaHistopathology(String congenitalRubellaHistopathology) {
        this.congenitalRubellaHistopathology = congenitalRubellaHistopathology;
    }

    public void setCongenitalRubellaIsolation(String congenitalRubellaIsolation) {
        this.congenitalRubellaIsolation = congenitalRubellaIsolation;
    }

    public void setCongenitalRubellaIgmSerumAntibody(String congenitalRubellaIgmSerumAntibody) {
        this.congenitalRubellaIgmSerumAntibody = congenitalRubellaIgmSerumAntibody;
    }

    public void setCongenitalRubellaIggSerumAntibody(String congenitalRubellaIggSerumAntibody) {
        this.congenitalRubellaIggSerumAntibody = congenitalRubellaIggSerumAntibody;
    }

    public void setCongenitalRubellaIgaSerumAntibody(String congenitalRubellaIgaSerumAntibody) {
        this.congenitalRubellaIgaSerumAntibody = congenitalRubellaIgaSerumAntibody;
    }

    public void setCongenitalRubellaIncubationTime(String congenitalRubellaIncubationTime) {
        this.congenitalRubellaIncubationTime = congenitalRubellaIncubationTime;
    }

    public void setCongenitalRubellaIndirectFluorescentAntibody(String congenitalRubellaIndirectFluorescentAntibody) {
        this.congenitalRubellaIndirectFluorescentAntibody = congenitalRubellaIndirectFluorescentAntibody;
    }

    public void setCongenitalRubellaMicroscopy(String congenitalRubellaMicroscopy) {
        this.congenitalRubellaMicroscopy = congenitalRubellaMicroscopy;
    }

    public void setCongenitalRubellaNeutralizingAntibodies(String congenitalRubellaNeutralizingAntibodies) {
        this.congenitalRubellaNeutralizingAntibodies = congenitalRubellaNeutralizingAntibodies;
    }

    public void setCongenitalRubellaPcr(String congenitalRubellaPcr) {
        this.congenitalRubellaPcr = congenitalRubellaPcr;
    }

    public void setCongenitalRubellaGramStain(String congenitalRubellaGramStain) {
        this.congenitalRubellaGramStain = congenitalRubellaGramStain;
    }

    public void setCongenitalRubellaLatexAgglutination(String congenitalRubellaLatexAgglutination) {
        this.congenitalRubellaLatexAgglutination = congenitalRubellaLatexAgglutination;
    }

    public void setCongenitalRubellaCqValueDetection(String congenitalRubellaCqValueDetection) {
        this.congenitalRubellaCqValueDetection = congenitalRubellaCqValueDetection;
    }

    public void setCongenitalRubellaSeQuencing(String congenitalRubellaSeQuencing) {
        this.congenitalRubellaSeQuencing = congenitalRubellaSeQuencing;
    }

    public void setCongenitalRubellaDnaMicroArray(String congenitalRubellaDnaMicroArray) {
        this.congenitalRubellaDnaMicroArray = congenitalRubellaDnaMicroArray;
    }

    public void setCongenitalRubellaOther(String congenitalRubellaOther) {
        this.congenitalRubellaOther = congenitalRubellaOther;
    }

    public void setCsmAntiBodyDetection(String csmAntiBodyDetection) {
        this.csmAntiBodyDetection = csmAntiBodyDetection;
    }

    public void setCsmAntigenDetection(String csmAntigenDetection) {
        this.csmAntigenDetection = csmAntigenDetection;
    }

    public void setCsmRapidTest(String csmRapidTest) {
        this.csmRapidTest = csmRapidTest;
    }

    public void setCsmCulture(String csmCulture) {
        this.csmCulture = csmCulture;
    }

    public void setCsmHistopathology(String csmHistopathology) {
        this.csmHistopathology = csmHistopathology;
    }

    public void setCsmIsolation(String csmIsolation) {
        this.csmIsolation = csmIsolation;
    }

    public void setCsmIgmSerumAntibody(String csmIgmSerumAntibody) {
        this.csmIgmSerumAntibody = csmIgmSerumAntibody;
    }

    public void setCsmIggSerumAntibody(String csmIggSerumAntibody) {
        this.csmIggSerumAntibody = csmIggSerumAntibody;
    }

    public void setCsmIgaSerumAntibody(String csmIgaSerumAntibody) {
        this.csmIgaSerumAntibody = csmIgaSerumAntibody;
    }

    public void setCsmIncubationTime(String csmIncubationTime) {
        this.csmIncubationTime = csmIncubationTime;
    }

    public void setCsmIndirectFluorescentAntibody(String csmIndirectFluorescentAntibody) {
        this.csmIndirectFluorescentAntibody = csmIndirectFluorescentAntibody;
    }

    public void setCsmMicroscopy(String csmMicroscopy) {
        this.csmMicroscopy = csmMicroscopy;
    }

    public void setCsmNeutralizingAntibodies(String csmNeutralizingAntibodies) {
        this.csmNeutralizingAntibodies = csmNeutralizingAntibodies;
    }

    public void setCsmPcr(String csmPcr) {
        this.csmPcr = csmPcr;
    }

    public void setCsmGramStain(String csmGramStain) {
        this.csmGramStain = csmGramStain;
    }

    public void setCsmLatexAgglutination(String csmLatexAgglutination) {
        this.csmLatexAgglutination = csmLatexAgglutination;
    }

    public void setCsmCqValueDetection(String csmCqValueDetection) {
        this.csmCqValueDetection = csmCqValueDetection;
    }

    public void setCsmSeQuencing(String csmSeQuencing) {
        this.csmSeQuencing = csmSeQuencing;
    }

    public void setCsmDnaMicroArray(String csmDnaMicroArray) {
        this.csmDnaMicroArray = csmDnaMicroArray;
    }

    public void setCsmOther(String csmOther) {
        this.csmOther = csmOther;
    }

    public void setDengueAntiBodyDetection(String dengueAntiBodyDetection) {
        this.dengueAntiBodyDetection = dengueAntiBodyDetection;
    }

    public void setDengueAntigenDetection(String dengueAntigenDetection) {
        this.dengueAntigenDetection = dengueAntigenDetection;
    }

    public void setDengueRapidTest(String dengueRapidTest) {
        this.dengueRapidTest = dengueRapidTest;
    }

    public void setDengueCulture(String dengueCulture) {
        this.dengueCulture = dengueCulture;
    }

    public void setDengueHistopathology(String dengueHistopathology) {
        this.dengueHistopathology = dengueHistopathology;
    }

    public void setDengueIsolation(String dengueIsolation) {
        this.dengueIsolation = dengueIsolation;
    }

    public void setDengueIgmSerumAntibody(String dengueIgmSerumAntibody) {
        this.dengueIgmSerumAntibody = dengueIgmSerumAntibody;
    }

    public void setDengueIggSerumAntibody(String dengueIggSerumAntibody) {
        this.dengueIggSerumAntibody = dengueIggSerumAntibody;
    }

    public void setDengueIgaSerumAntibody(String dengueIgaSerumAntibody) {
        this.dengueIgaSerumAntibody = dengueIgaSerumAntibody;
    }

    public void setDengueIncubationTime(String dengueIncubationTime) {
        this.dengueIncubationTime = dengueIncubationTime;
    }

    public void setDengueIndirectFluorescentAntibody(String dengueIndirectFluorescentAntibody) {
        this.dengueIndirectFluorescentAntibody = dengueIndirectFluorescentAntibody;
    }

    public void setDengueMicroscopy(String dengueMicroscopy) {
        this.dengueMicroscopy = dengueMicroscopy;
    }

    public void setDengueNeutralizingAntibodies(String dengueNeutralizingAntibodies) {
        this.dengueNeutralizingAntibodies = dengueNeutralizingAntibodies;
    }

    public void setDenguePcr(String denguePcr) {
        this.denguePcr = denguePcr;
    }

    public void setDengueGramStain(String dengueGramStain) {
        this.dengueGramStain = dengueGramStain;
    }

    public void setDengueLatexAgglutination(String dengueLatexAgglutination) {
        this.dengueLatexAgglutination = dengueLatexAgglutination;
    }

    public void setDengueCqValueDetection(String dengueCqValueDetection) {
        this.dengueCqValueDetection = dengueCqValueDetection;
    }

    public void setDengueSeQuencing(String dengueSeQuencing) {
        this.dengueSeQuencing = dengueSeQuencing;
    }

    public void setDengueDnaMicroArray(String dengueDnaMicroArray) {
        this.dengueDnaMicroArray = dengueDnaMicroArray;
    }

    public void setDengueOther(String dengueOther) {
        this.dengueOther = dengueOther;
    }

    public void setEvdAntiBodyDetection(String evdAntiBodyDetection) {
        this.evdAntiBodyDetection = evdAntiBodyDetection;
    }

    public void setEvdAntigenDetection(String evdAntigenDetection) {
        this.evdAntigenDetection = evdAntigenDetection;
    }

    public void setEvdRapidTest(String evdRapidTest) {
        this.evdRapidTest = evdRapidTest;
    }

    public void setEvdCulture(String evdCulture) {
        this.evdCulture = evdCulture;
    }

    public void setEvdHistopathology(String evdHistopathology) {
        this.evdHistopathology = evdHistopathology;
    }

    public void setEvdIsolation(String evdIsolation) {
        this.evdIsolation = evdIsolation;
    }

    public void setEvdIgmSerumAntibody(String evdIgmSerumAntibody) {
        this.evdIgmSerumAntibody = evdIgmSerumAntibody;
    }

    public void setEvdIggSerumAntibody(String evdIggSerumAntibody) {
        this.evdIggSerumAntibody = evdIggSerumAntibody;
    }

    public void setEvdIgaSerumAntibody(String evdIgaSerumAntibody) {
        this.evdIgaSerumAntibody = evdIgaSerumAntibody;
    }

    public void setEvdIncubationTime(String evdIncubationTime) {
        this.evdIncubationTime = evdIncubationTime;
    }

    public void setEvdIndirectFluorescentAntibody(String evdIndirectFluorescentAntibody) {
        this.evdIndirectFluorescentAntibody = evdIndirectFluorescentAntibody;
    }

    public void setEvdMicroscopy(String evdMicroscopy) {
        this.evdMicroscopy = evdMicroscopy;
    }

    public void setEvdNeutralizingAntibodies(String evdNeutralizingAntibodies) {
        this.evdNeutralizingAntibodies = evdNeutralizingAntibodies;
    }

    public void setEvdPcr(String evdPcr) {
        this.evdPcr = evdPcr;
    }

    public void setEvdGramStain(String evdGramStain) {
        this.evdGramStain = evdGramStain;
    }

    public void setEvdLatexAgglutination(String evdLatexAgglutination) {
        this.evdLatexAgglutination = evdLatexAgglutination;
    }

    public void setEvdCqValueDetection(String evdCqValueDetection) {
        this.evdCqValueDetection = evdCqValueDetection;
    }

    public void setEvdSeQuencing(String evdSeQuencing) {
        this.evdSeQuencing = evdSeQuencing;
    }

    public void setEvdDnaMicroArray(String evdDnaMicroArray) {
        this.evdDnaMicroArray = evdDnaMicroArray;
    }

    public void setEvdOther(String evdOther) {
        this.evdOther = evdOther;
    }

    public void setGuineaWormAntiBodyDetection(String guineaWormAntiBodyDetection) {
        this.guineaWormAntiBodyDetection = guineaWormAntiBodyDetection;
    }

    public void setGuineaWormAntigenDetection(String guineaWormAntigenDetection) {
        this.guineaWormAntigenDetection = guineaWormAntigenDetection;
    }

    public void setGuineaWormRapidTest(String guineaWormRapidTest) {
        this.guineaWormRapidTest = guineaWormRapidTest;
    }

    public void setGuineaWormCulture(String guineaWormCulture) {
        this.guineaWormCulture = guineaWormCulture;
    }

    public void setGuineaWormHistopathology(String guineaWormHistopathology) {
        this.guineaWormHistopathology = guineaWormHistopathology;
    }

    public void setGuineaWormIsolation(String guineaWormIsolation) {
        this.guineaWormIsolation = guineaWormIsolation;
    }

    public void setGuineaWormIgmSerumAntibody(String guineaWormIgmSerumAntibody) {
        this.guineaWormIgmSerumAntibody = guineaWormIgmSerumAntibody;
    }

    public void setGuineaWormIggSerumAntibody(String guineaWormIggSerumAntibody) {
        this.guineaWormIggSerumAntibody = guineaWormIggSerumAntibody;
    }

    public void setGuineaWormIgaSerumAntibody(String guineaWormIgaSerumAntibody) {
        this.guineaWormIgaSerumAntibody = guineaWormIgaSerumAntibody;
    }

    public void setGuineaWormIncubationTime(String guineaWormIncubationTime) {
        this.guineaWormIncubationTime = guineaWormIncubationTime;
    }

    public void setGuineaWormIndirectFluorescentAntibody(String guineaWormIndirectFluorescentAntibody) {
        this.guineaWormIndirectFluorescentAntibody = guineaWormIndirectFluorescentAntibody;
    }

    public void setGuineaWormMicroscopy(String guineaWormMicroscopy) {
        this.guineaWormMicroscopy = guineaWormMicroscopy;
    }

    public void setGuineaWormNeutralizingAntibodies(String guineaWormNeutralizingAntibodies) {
        this.guineaWormNeutralizingAntibodies = guineaWormNeutralizingAntibodies;
    }

    public void setGuineaWormPcr(String guineaWormPcr) {
        this.guineaWormPcr = guineaWormPcr;
    }

    public void setGuineaWormGramStain(String guineaWormGramStain) {
        this.guineaWormGramStain = guineaWormGramStain;
    }

    public void setGuineaWormLatexAgglutination(String guineaWormLatexAgglutination) {
        this.guineaWormLatexAgglutination = guineaWormLatexAgglutination;
    }

    public void setGuineaWormCqValueDetection(String guineaWormCqValueDetection) {
        this.guineaWormCqValueDetection = guineaWormCqValueDetection;
    }

    public void setGuineaWormSeQuencing(String guineaWormSeQuencing) {
        this.guineaWormSeQuencing = guineaWormSeQuencing;
    }

    public void setGuineaWormDnaMicroArray(String guineaWormDnaMicroArray) {
        this.guineaWormDnaMicroArray = guineaWormDnaMicroArray;
    }

    public void setGuineaWormOther(String guineaWormOther) {
        this.guineaWormOther = guineaWormOther;
    }

    public void setLassaAntiBodyDetection(String lassaAntiBodyDetection) {
        this.lassaAntiBodyDetection = lassaAntiBodyDetection;
    }

    public void setLassaAntigenDetection(String lassaAntigenDetection) {
        this.lassaAntigenDetection = lassaAntigenDetection;
    }

    public void setLassaRapidTest(String lassaRapidTest) {
        this.lassaRapidTest = lassaRapidTest;
    }

    public void setLassaCulture(String lassaCulture) {
        this.lassaCulture = lassaCulture;
    }

    public void setLassaHistopathology(String lassaHistopathology) {
        this.lassaHistopathology = lassaHistopathology;
    }

    public void setLassaIsolation(String lassaIsolation) {
        this.lassaIsolation = lassaIsolation;
    }

    public void setLassaIgmSerumAntibody(String lassaIgmSerumAntibody) {
        this.lassaIgmSerumAntibody = lassaIgmSerumAntibody;
    }

    public void setLassaIggSerumAntibody(String lassaIggSerumAntibody) {
        this.lassaIggSerumAntibody = lassaIggSerumAntibody;
    }

    public void setLassaIgaSerumAntibody(String lassaIgaSerumAntibody) {
        this.lassaIgaSerumAntibody = lassaIgaSerumAntibody;
    }

    public void setLassaIncubationTime(String lassaIncubationTime) {
        this.lassaIncubationTime = lassaIncubationTime;
    }

    public void setLassaIndirectFluorescentAntibody(String lassaIndirectFluorescentAntibody) {
        this.lassaIndirectFluorescentAntibody = lassaIndirectFluorescentAntibody;
    }

    public void setLassaMicroscopy(String lassaMicroscopy) {
        this.lassaMicroscopy = lassaMicroscopy;
    }

    public void setLassaNeutralizingAntibodies(String lassaNeutralizingAntibodies) {
        this.lassaNeutralizingAntibodies = lassaNeutralizingAntibodies;
    }

    public void setLassaPcr(String lassaPcr) {
        this.lassaPcr = lassaPcr;
    }

    public void setLassaGramStain(String lassaGramStain) {
        this.lassaGramStain = lassaGramStain;
    }

    public void setLassaLatexAgglutination(String lassaLatexAgglutination) {
        this.lassaLatexAgglutination = lassaLatexAgglutination;
    }

    public void setLassaCqValueDetection(String lassaCqValueDetection) {
        this.lassaCqValueDetection = lassaCqValueDetection;
    }

    public void setLassaSeQuencing(String lassaSeQuencing) {
        this.lassaSeQuencing = lassaSeQuencing;
    }

    public void setLassaDnaMicroArray(String lassaDnaMicroArray) {
        this.lassaDnaMicroArray = lassaDnaMicroArray;
    }

    public void setLassaOther(String lassaOther) {
        this.lassaOther = lassaOther;
    }

    public void setMeaslesAntiBodyDetection(String measlesAntiBodyDetection) {
        this.measlesAntiBodyDetection = measlesAntiBodyDetection;
    }

    public void setMeaslesAntigenDetection(String measlesAntigenDetection) {
        this.measlesAntigenDetection = measlesAntigenDetection;
    }

    public void setMeaslesRapidTest(String measlesRapidTest) {
        this.measlesRapidTest = measlesRapidTest;
    }

    public void setMeaslesCulture(String measlesCulture) {
        this.measlesCulture = measlesCulture;
    }

    public void setMeaslesHistopathology(String measlesHistopathology) {
        this.measlesHistopathology = measlesHistopathology;
    }

    public void setMeaslesIsolation(String measlesIsolation) {
        this.measlesIsolation = measlesIsolation;
    }

    public void setMeaslesIgmSerumAntibody(String measlesIgmSerumAntibody) {
        this.measlesIgmSerumAntibody = measlesIgmSerumAntibody;
    }

    public void setMeaslesIggSerumAntibody(String measlesIggSerumAntibody) {
        this.measlesIggSerumAntibody = measlesIggSerumAntibody;
    }

    public void setMeaslesIgaSerumAntibody(String measlesIgaSerumAntibody) {
        this.measlesIgaSerumAntibody = measlesIgaSerumAntibody;
    }

    public void setMeaslesIncubationTime(String measlesIncubationTime) {
        this.measlesIncubationTime = measlesIncubationTime;
    }

    public void setMeaslesIndirectFluorescentAntibody(String measlesIndirectFluorescentAntibody) {
        this.measlesIndirectFluorescentAntibody = measlesIndirectFluorescentAntibody;
    }

    public void setMeaslesMicroscopy(String measlesMicroscopy) {
        this.measlesMicroscopy = measlesMicroscopy;
    }

    public void setMeaslesNeutralizingAntibodies(String measlesNeutralizingAntibodies) {
        this.measlesNeutralizingAntibodies = measlesNeutralizingAntibodies;
    }

    public void setMeaslesPcr(String measlesPcr) {
        this.measlesPcr = measlesPcr;
    }

    public void setMeaslesGramStain(String measlesGramStain) {
        this.measlesGramStain = measlesGramStain;
    }

    public void setMeaslesLatexAgglutination(String measlesLatexAgglutination) {
        this.measlesLatexAgglutination = measlesLatexAgglutination;
    }

    public void setMeaslesCqValueDetection(String measlesCqValueDetection) {
        this.measlesCqValueDetection = measlesCqValueDetection;
    }

    public void setMeaslesSeQuencing(String measlesSeQuencing) {
        this.measlesSeQuencing = measlesSeQuencing;
    }

    public void setMeaslesDnaMicroArray(String measlesDnaMicroArray) {
        this.measlesDnaMicroArray = measlesDnaMicroArray;
    }

    public void setMeaslesOther(String measlesOther) {
        this.measlesOther = measlesOther;
    }

    public void setMonkeyPoxAntiBodyDetection(String monkeyPoxAntiBodyDetection) {
        this.monkeyPoxAntiBodyDetection = monkeyPoxAntiBodyDetection;
    }

    public void setMonkeyPoxAntigenDetection(String monkeyPoxAntigenDetection) {
        this.monkeyPoxAntigenDetection = monkeyPoxAntigenDetection;
    }

    public void setMonkeyPoxRapidTest(String monkeyPoxRapidTest) {
        this.monkeyPoxRapidTest = monkeyPoxRapidTest;
    }

    public void setMonkeyPoxCulture(String monkeyPoxCulture) {
        this.monkeyPoxCulture = monkeyPoxCulture;
    }

    public void setMonkeyPoxHistopathology(String monkeyPoxHistopathology) {
        this.monkeyPoxHistopathology = monkeyPoxHistopathology;
    }

    public void setMonkeyPoxIsolation(String monkeyPoxIsolation) {
        this.monkeyPoxIsolation = monkeyPoxIsolation;
    }

    public void setMonkeyPoxIgmSerumAntibody(String monkeyPoxIgmSerumAntibody) {
        this.monkeyPoxIgmSerumAntibody = monkeyPoxIgmSerumAntibody;
    }

    public void setMonkeyPoxIggSerumAntibody(String monkeyPoxIggSerumAntibody) {
        this.monkeyPoxIggSerumAntibody = monkeyPoxIggSerumAntibody;
    }

    public void setMonkeyPoxIgaSerumAntibody(String monkeyPoxIgaSerumAntibody) {
        this.monkeyPoxIgaSerumAntibody = monkeyPoxIgaSerumAntibody;
    }

    public void setMonkeyPoxIncubationTime(String monkeyPoxIncubationTime) {
        this.monkeyPoxIncubationTime = monkeyPoxIncubationTime;
    }

    public void setMonkeyPoxIndirectFluorescentAntibody(String monkeyPoxIndirectFluorescentAntibody) {
        this.monkeyPoxIndirectFluorescentAntibody = monkeyPoxIndirectFluorescentAntibody;
    }

    public void setMonkeyPoxMicroscopy(String monkeyPoxMicroscopy) {
        this.monkeyPoxMicroscopy = monkeyPoxMicroscopy;
    }

    public void setMonkeyPoxNeutralizingAntibodies(String monkeyPoxNeutralizingAntibodies) {
        this.monkeyPoxNeutralizingAntibodies = monkeyPoxNeutralizingAntibodies;
    }

    public void setMonkeyPoxPcr(String monkeyPoxPcr) {
        this.monkeyPoxPcr = monkeyPoxPcr;
    }

    public void setMonkeyPoxGramStain(String monkeyPoxGramStain) {
        this.monkeyPoxGramStain = monkeyPoxGramStain;
    }

    public void setMonkeyPoxLatexAgglutination(String monkeyPoxLatexAgglutination) {
        this.monkeyPoxLatexAgglutination = monkeyPoxLatexAgglutination;
    }

    public void setMonkeyPoxCqValueDetection(String monkeyPoxCqValueDetection) {
        this.monkeyPoxCqValueDetection = monkeyPoxCqValueDetection;
    }

    public void setMonkeyPoxSeQuencing(String monkeyPoxSeQuencing) {
        this.monkeyPoxSeQuencing = monkeyPoxSeQuencing;
    }

    public void setMonkeyPoxDnaMicroArray(String monkeyPoxDnaMicroArray) {
        this.monkeyPoxDnaMicroArray = monkeyPoxDnaMicroArray;
    }

    public void setMonkeyPoxOther(String monkeyPoxOther) {
        this.monkeyPoxOther = monkeyPoxOther;
    }

    public void setMonkeypoxAntiBodyDetection(String monkeypoxAntiBodyDetection) {
        this.monkeypoxAntiBodyDetection = monkeypoxAntiBodyDetection;
    }

    public void setMonkeypoxAntigenDetection(String monkeypoxAntigenDetection) {
        this.monkeypoxAntigenDetection = monkeypoxAntigenDetection;
    }

    public void setMonkeypoxRapidTest(String monkeypoxRapidTest) {
        this.monkeypoxRapidTest = monkeypoxRapidTest;
    }

    public void setMonkeypoxCulture(String monkeypoxCulture) {
        this.monkeypoxCulture = monkeypoxCulture;
    }

    public void setMonkeypoxHistopathology(String monkeypoxHistopathology) {
        this.monkeypoxHistopathology = monkeypoxHistopathology;
    }

    public void setMonkeypoxIsolation(String monkeypoxIsolation) {
        this.monkeypoxIsolation = monkeypoxIsolation;
    }

    public void setMonkeypoxIgmSerumAntibody(String monkeypoxIgmSerumAntibody) {
        this.monkeypoxIgmSerumAntibody = monkeypoxIgmSerumAntibody;
    }

    public void setMonkeypoxIggSerumAntibody(String monkeypoxIggSerumAntibody) {
        this.monkeypoxIggSerumAntibody = monkeypoxIggSerumAntibody;
    }

    public void setMonkeypoxIgaSerumAntibody(String monkeypoxIgaSerumAntibody) {
        this.monkeypoxIgaSerumAntibody = monkeypoxIgaSerumAntibody;
    }

    public void setMonkeypoxIncubationTime(String monkeypoxIncubationTime) {
        this.monkeypoxIncubationTime = monkeypoxIncubationTime;
    }

    public void setMonkeypoxIndirectFluorescentAntibody(String monkeypoxIndirectFluorescentAntibody) {
        this.monkeypoxIndirectFluorescentAntibody = monkeypoxIndirectFluorescentAntibody;
    }

    public void setMonkeypoxMicroscopy(String monkeypoxMicroscopy) {
        this.monkeypoxMicroscopy = monkeypoxMicroscopy;
    }

    public void setMonkeypoxNeutralizingAntibodies(String monkeypoxNeutralizingAntibodies) {
        this.monkeypoxNeutralizingAntibodies = monkeypoxNeutralizingAntibodies;
    }

    public void setMonkeypoxPcr(String monkeypoxPcr) {
        this.monkeypoxPcr = monkeypoxPcr;
    }

    public void setMonkeypoxGramStain(String monkeypoxGramStain) {
        this.monkeypoxGramStain = monkeypoxGramStain;
    }

    public void setMonkeypoxLatexAgglutination(String monkeypoxLatexAgglutination) {
        this.monkeypoxLatexAgglutination = monkeypoxLatexAgglutination;
    }

    public void setMonkeypoxCqValueDetection(String monkeypoxCqValueDetection) {
        this.monkeypoxCqValueDetection = monkeypoxCqValueDetection;
    }

    public void setMonkeypoxSeQuencing(String monkeypoxSeQuencing) {
        this.monkeypoxSeQuencing = monkeypoxSeQuencing;
    }

    public void setMonkeypoxDnaMicroArray(String monkeypoxDnaMicroArray) {
        this.monkeypoxDnaMicroArray = monkeypoxDnaMicroArray;
    }

    public void setMonkeypoxOther(String monkeypoxOther) {
        this.monkeypoxOther = monkeypoxOther;
    }

    public void setNewInfluenzaAntiBodyDetection(String newInfluenzaAntiBodyDetection) {
        this.newInfluenzaAntiBodyDetection = newInfluenzaAntiBodyDetection;
    }

    public void setNewInfluenzaAntigenDetection(String newInfluenzaAntigenDetection) {
        this.newInfluenzaAntigenDetection = newInfluenzaAntigenDetection;
    }

    public void setNewInfluenzaRapidTest(String newInfluenzaRapidTest) {
        this.newInfluenzaRapidTest = newInfluenzaRapidTest;
    }

    public void setNewInfluenzaCulture(String newInfluenzaCulture) {
        this.newInfluenzaCulture = newInfluenzaCulture;
    }

    public void setNewInfluenzaHistopathology(String newInfluenzaHistopathology) {
        this.newInfluenzaHistopathology = newInfluenzaHistopathology;
    }

    public void setNewInfluenzaIsolation(String newInfluenzaIsolation) {
        this.newInfluenzaIsolation = newInfluenzaIsolation;
    }

    public void setNewInfluenzaIgmSerumAntibody(String newInfluenzaIgmSerumAntibody) {
        this.newInfluenzaIgmSerumAntibody = newInfluenzaIgmSerumAntibody;
    }

    public void setNewInfluenzaIggSerumAntibody(String newInfluenzaIggSerumAntibody) {
        this.newInfluenzaIggSerumAntibody = newInfluenzaIggSerumAntibody;
    }

    public void setNewInfluenzaIgaSerumAntibody(String newInfluenzaIgaSerumAntibody) {
        this.newInfluenzaIgaSerumAntibody = newInfluenzaIgaSerumAntibody;
    }

    public void setNewInfluenzaIncubationTime(String newInfluenzaIncubationTime) {
        this.newInfluenzaIncubationTime = newInfluenzaIncubationTime;
    }

    public void setNewInfluenzaIndirectFluorescentAntibody(String newInfluenzaIndirectFluorescentAntibody) {
        this.newInfluenzaIndirectFluorescentAntibody = newInfluenzaIndirectFluorescentAntibody;
    }

    public void setNewInfluenzaMicroscopy(String newInfluenzaMicroscopy) {
        this.newInfluenzaMicroscopy = newInfluenzaMicroscopy;
    }

    public void setNewInfluenzaNeutralizingAntibodies(String newInfluenzaNeutralizingAntibodies) {
        this.newInfluenzaNeutralizingAntibodies = newInfluenzaNeutralizingAntibodies;
    }

    public void setNewInfluenzaPcr(String newInfluenzaPcr) {
        this.newInfluenzaPcr = newInfluenzaPcr;
    }

    public void setNewInfluenzaGramStain(String newInfluenzaGramStain) {
        this.newInfluenzaGramStain = newInfluenzaGramStain;
    }

    public void setNewInfluenzaLatexAgglutination(String newInfluenzaLatexAgglutination) {
        this.newInfluenzaLatexAgglutination = newInfluenzaLatexAgglutination;
    }

    public void setNewInfluenzaCqValueDetection(String newInfluenzaCqValueDetection) {
        this.newInfluenzaCqValueDetection = newInfluenzaCqValueDetection;
    }

    public void setNewInfluenzaSeQuencing(String newInfluenzaSeQuencing) {
        this.newInfluenzaSeQuencing = newInfluenzaSeQuencing;
    }

    public void setNewInfluenzaDnaMicroArray(String newInfluenzaDnaMicroArray) {
        this.newInfluenzaDnaMicroArray = newInfluenzaDnaMicroArray;
    }

    public void setNewInfluenzaOther(String newInfluenzaOther) {
        this.newInfluenzaOther = newInfluenzaOther;
    }

    public void setPlagueAntiBodyDetection(String plagueAntiBodyDetection) {
        this.plagueAntiBodyDetection = plagueAntiBodyDetection;
    }

    public void setPlagueAntigenDetection(String plagueAntigenDetection) {
        this.plagueAntigenDetection = plagueAntigenDetection;
    }

    public void setPlagueRapidTest(String plagueRapidTest) {
        this.plagueRapidTest = plagueRapidTest;
    }

    public void setPlagueCulture(String plagueCulture) {
        this.plagueCulture = plagueCulture;
    }

    public void setPlagueHistopathology(String plagueHistopathology) {
        this.plagueHistopathology = plagueHistopathology;
    }

    public void setPlagueIsolation(String plagueIsolation) {
        this.plagueIsolation = plagueIsolation;
    }

    public void setPlagueIgmSerumAntibody(String plagueIgmSerumAntibody) {
        this.plagueIgmSerumAntibody = plagueIgmSerumAntibody;
    }

    public void setPlagueIggSerumAntibody(String plagueIggSerumAntibody) {
        this.plagueIggSerumAntibody = plagueIggSerumAntibody;
    }

    public void setPlagueIgaSerumAntibody(String plagueIgaSerumAntibody) {
        this.plagueIgaSerumAntibody = plagueIgaSerumAntibody;
    }

    public void setPlagueIncubationTime(String plagueIncubationTime) {
        this.plagueIncubationTime = plagueIncubationTime;
    }

    public void setPlagueIndirectFluorescentAntibody(String plagueIndirectFluorescentAntibody) {
        this.plagueIndirectFluorescentAntibody = plagueIndirectFluorescentAntibody;
    }

    public void setPlagueMicroscopy(String plagueMicroscopy) {
        this.plagueMicroscopy = plagueMicroscopy;
    }

    public void setPlagueNeutralizingAntibodies(String plagueNeutralizingAntibodies) {
        this.plagueNeutralizingAntibodies = plagueNeutralizingAntibodies;
    }

    public void setPlaguePcr(String plaguePcr) {
        this.plaguePcr = plaguePcr;
    }

    public void setPlagueGramStain(String plagueGramStain) {
        this.plagueGramStain = plagueGramStain;
    }

    public void setPlagueLatexAgglutination(String plagueLatexAgglutination) {
        this.plagueLatexAgglutination = plagueLatexAgglutination;
    }

    public void setPlagueCqValueDetection(String plagueCqValueDetection) {
        this.plagueCqValueDetection = plagueCqValueDetection;
    }

    public void setPlagueSeQuencing(String plagueSeQuencing) {
        this.plagueSeQuencing = plagueSeQuencing;
    }

    public void setPlagueDnaMicroArray(String plagueDnaMicroArray) {
        this.plagueDnaMicroArray = plagueDnaMicroArray;
    }

    public void setPlagueOther(String plagueOther) {
        this.plagueOther = plagueOther;
    }

    public void setPolioAntiBodyDetection(String polioAntiBodyDetection) {
        this.polioAntiBodyDetection = polioAntiBodyDetection;
    }

    public void setPolioAntigenDetection(String polioAntigenDetection) {
        this.polioAntigenDetection = polioAntigenDetection;
    }

    public void setPolioRapidTest(String polioRapidTest) {
        this.polioRapidTest = polioRapidTest;
    }

    public void setPolioCulture(String polioCulture) {
        this.polioCulture = polioCulture;
    }

    public void setPolioHistopathology(String polioHistopathology) {
        this.polioHistopathology = polioHistopathology;
    }

    public void setPolioIsolation(String polioIsolation) {
        this.polioIsolation = polioIsolation;
    }

    public void setPolioIgmSerumAntibody(String polioIgmSerumAntibody) {
        this.polioIgmSerumAntibody = polioIgmSerumAntibody;
    }

    public void setPolioIggSerumAntibody(String polioIggSerumAntibody) {
        this.polioIggSerumAntibody = polioIggSerumAntibody;
    }

    public void setPolioIgaSerumAntibody(String polioIgaSerumAntibody) {
        this.polioIgaSerumAntibody = polioIgaSerumAntibody;
    }

    public void setPolioIncubationTime(String polioIncubationTime) {
        this.polioIncubationTime = polioIncubationTime;
    }

    public void setPolioIndirectFluorescentAntibody(String polioIndirectFluorescentAntibody) {
        this.polioIndirectFluorescentAntibody = polioIndirectFluorescentAntibody;
    }

    public void setPolioMicroscopy(String polioMicroscopy) {
        this.polioMicroscopy = polioMicroscopy;
    }

    public void setPolioNeutralizingAntibodies(String polioNeutralizingAntibodies) {
        this.polioNeutralizingAntibodies = polioNeutralizingAntibodies;
    }

    public void setPolioPcr(String polioPcr) {
        this.polioPcr = polioPcr;
    }

    public void setPolioGramStain(String polioGramStain) {
        this.polioGramStain = polioGramStain;
    }

    public void setPolioLatexAgglutination(String polioLatexAgglutination) {
        this.polioLatexAgglutination = polioLatexAgglutination;
    }

    public void setPolioCqValueDetection(String polioCqValueDetection) {
        this.polioCqValueDetection = polioCqValueDetection;
    }

    public void setPolioSeQuencing(String polioSeQuencing) {
        this.polioSeQuencing = polioSeQuencing;
    }

    public void setPolioDnaMicroArray(String polioDnaMicroArray) {
        this.polioDnaMicroArray = polioDnaMicroArray;
    }

    public void setPolioOther(String polioOther) {
        this.polioOther = polioOther;
    }

    public void setUnspecifiedVhfAntiBodyDetection(String unspecifiedVhfAntiBodyDetection) {
        this.unspecifiedVhfAntiBodyDetection = unspecifiedVhfAntiBodyDetection;
    }

    public void setUnspecifiedVhfAntigenDetection(String unspecifiedVhfAntigenDetection) {
        this.unspecifiedVhfAntigenDetection = unspecifiedVhfAntigenDetection;
    }

    public void setUnspecifiedVhfRapidTest(String unspecifiedVhfRapidTest) {
        this.unspecifiedVhfRapidTest = unspecifiedVhfRapidTest;
    }

    public void setUnspecifiedVhfCulture(String unspecifiedVhfCulture) {
        this.unspecifiedVhfCulture = unspecifiedVhfCulture;
    }

    public void setUnspecifiedVhfHistopathology(String unspecifiedVhfHistopathology) {
        this.unspecifiedVhfHistopathology = unspecifiedVhfHistopathology;
    }

    public void setUnspecifiedVhfIsolation(String unspecifiedVhfIsolation) {
        this.unspecifiedVhfIsolation = unspecifiedVhfIsolation;
    }

    public void setUnspecifiedVhfIgmSerumAntibody(String unspecifiedVhfIgmSerumAntibody) {
        this.unspecifiedVhfIgmSerumAntibody = unspecifiedVhfIgmSerumAntibody;
    }

    public void setUnspecifiedVhfIggSerumAntibody(String unspecifiedVhfIggSerumAntibody) {
        this.unspecifiedVhfIggSerumAntibody = unspecifiedVhfIggSerumAntibody;
    }

    public void setUnspecifiedVhfIgaSerumAntibody(String unspecifiedVhfIgaSerumAntibody) {
        this.unspecifiedVhfIgaSerumAntibody = unspecifiedVhfIgaSerumAntibody;
    }

    public void setUnspecifiedVhfIncubationTime(String unspecifiedVhfIncubationTime) {
        this.unspecifiedVhfIncubationTime = unspecifiedVhfIncubationTime;
    }

    public void setUnspecifiedVhfIndirectFluorescentAntibody(String unspecifiedVhfIndirectFluorescentAntibody) {
        this.unspecifiedVhfIndirectFluorescentAntibody = unspecifiedVhfIndirectFluorescentAntibody;
    }

    public void setUnspecifiedVhfMicroscopy(String unspecifiedVhfMicroscopy) {
        this.unspecifiedVhfMicroscopy = unspecifiedVhfMicroscopy;
    }

    public void setUnspecifiedVhfNeutralizingAntibodies(String unspecifiedVhfNeutralizingAntibodies) {
        this.unspecifiedVhfNeutralizingAntibodies = unspecifiedVhfNeutralizingAntibodies;
    }

    public void setUnspecifiedVhfPcr(String unspecifiedVhfPcr) {
        this.unspecifiedVhfPcr = unspecifiedVhfPcr;
    }

    public void setUnspecifiedVhfGramStain(String unspecifiedVhfGramStain) {
        this.unspecifiedVhfGramStain = unspecifiedVhfGramStain;
    }

    public void setUnspecifiedVhfLatexAgglutination(String unspecifiedVhfLatexAgglutination) {
        this.unspecifiedVhfLatexAgglutination = unspecifiedVhfLatexAgglutination;
    }

    public void setUnspecifiedVhfCqValueDetection(String unspecifiedVhfCqValueDetection) {
        this.unspecifiedVhfCqValueDetection = unspecifiedVhfCqValueDetection;
    }

    public void setUnspecifiedVhfSeQuencing(String unspecifiedVhfSeQuencing) {
        this.unspecifiedVhfSeQuencing = unspecifiedVhfSeQuencing;
    }

    public void setUnspecifiedVhfDnaMicroArray(String unspecifiedVhfDnaMicroArray) {
        this.unspecifiedVhfDnaMicroArray = unspecifiedVhfDnaMicroArray;
    }

    public void setUnspecifiedVhfOther(String unspecifiedVhfOther) {
        this.unspecifiedVhfOther = unspecifiedVhfOther;
    }

    public void setYellowFeverAntiBodyDetection(String yellowFeverAntiBodyDetection) {
        this.yellowFeverAntiBodyDetection = yellowFeverAntiBodyDetection;
    }

    public void setYellowFeverAntigenDetection(String yellowFeverAntigenDetection) {
        this.yellowFeverAntigenDetection = yellowFeverAntigenDetection;
    }

    public void setYellowFeverRapidTest(String yellowFeverRapidTest) {
        this.yellowFeverRapidTest = yellowFeverRapidTest;
    }

    public void setYellowFeverCulture(String yellowFeverCulture) {
        this.yellowFeverCulture = yellowFeverCulture;
    }

    public void setYellowFeverHistopathology(String yellowFeverHistopathology) {
        this.yellowFeverHistopathology = yellowFeverHistopathology;
    }

    public void setYellowFeverIsolation(String yellowFeverIsolation) {
        this.yellowFeverIsolation = yellowFeverIsolation;
    }

    public void setYellowFeverIgmSerumAntibody(String yellowFeverIgmSerumAntibody) {
        this.yellowFeverIgmSerumAntibody = yellowFeverIgmSerumAntibody;
    }

    public void setYellowFeverIggSerumAntibody(String yellowFeverIggSerumAntibody) {
        this.yellowFeverIggSerumAntibody = yellowFeverIggSerumAntibody;
    }

    public void setYellowFeverIgaSerumAntibody(String yellowFeverIgaSerumAntibody) {
        this.yellowFeverIgaSerumAntibody = yellowFeverIgaSerumAntibody;
    }

    public void setYellowFeverIncubationTime(String yellowFeverIncubationTime) {
        this.yellowFeverIncubationTime = yellowFeverIncubationTime;
    }

    public void setYellowFeverIndirectFluorescentAntibody(String yellowFeverIndirectFluorescentAntibody) {
        this.yellowFeverIndirectFluorescentAntibody = yellowFeverIndirectFluorescentAntibody;
    }

    public void setYellowFeverMicroscopy(String yellowFeverMicroscopy) {
        this.yellowFeverMicroscopy = yellowFeverMicroscopy;
    }

    public void setYellowFeverNeutralizingAntibodies(String yellowFeverNeutralizingAntibodies) {
        this.yellowFeverNeutralizingAntibodies = yellowFeverNeutralizingAntibodies;
    }

    public void setYellowFeverPcr(String yellowFeverPcr) {
        this.yellowFeverPcr = yellowFeverPcr;
    }

    public void setYellowFeverGramStain(String yellowFeverGramStain) {
        this.yellowFeverGramStain = yellowFeverGramStain;
    }

    public void setYellowFeverLatexAgglutination(String yellowFeverLatexAgglutination) {
        this.yellowFeverLatexAgglutination = yellowFeverLatexAgglutination;
    }

    public void setYellowFeverCqValueDetection(String yellowFeverCqValueDetection) {
        this.yellowFeverCqValueDetection = yellowFeverCqValueDetection;
    }

    public void setYellowFeverSeQuencing(String yellowFeverSeQuencing) {
        this.yellowFeverSeQuencing = yellowFeverSeQuencing;
    }

    public void setYellowFeverDnaMicroArray(String yellowFeverDnaMicroArray) {
        this.yellowFeverDnaMicroArray = yellowFeverDnaMicroArray;
    }

    public void setYellowFeverOther(String yellowFeverOther) {
        this.yellowFeverOther = yellowFeverOther;
    }

    public void setRabiesAntiBodyDetection(String rabiesAntiBodyDetection) {
        this.rabiesAntiBodyDetection = rabiesAntiBodyDetection;
    }

    public void setRabiesAntigenDetection(String rabiesAntigenDetection) {
        this.rabiesAntigenDetection = rabiesAntigenDetection;
    }

    public void setRabiesRapidTest(String rabiesRapidTest) {
        this.rabiesRapidTest = rabiesRapidTest;
    }

    public void setRabiesCulture(String rabiesCulture) {
        this.rabiesCulture = rabiesCulture;
    }

    public void setRabiesHistopathology(String rabiesHistopathology) {
        this.rabiesHistopathology = rabiesHistopathology;
    }

    public void setRabiesIsolation(String rabiesIsolation) {
        this.rabiesIsolation = rabiesIsolation;
    }

    public void setRabiesIgmSerumAntibody(String rabiesIgmSerumAntibody) {
        this.rabiesIgmSerumAntibody = rabiesIgmSerumAntibody;
    }

    public void setRabiesIggSerumAntibody(String rabiesIggSerumAntibody) {
        this.rabiesIggSerumAntibody = rabiesIggSerumAntibody;
    }

    public void setRabiesIgaSerumAntibody(String rabiesIgaSerumAntibody) {
        this.rabiesIgaSerumAntibody = rabiesIgaSerumAntibody;
    }

    public void setRabiesIncubationTime(String rabiesIncubationTime) {
        this.rabiesIncubationTime = rabiesIncubationTime;
    }

    public void setRabiesIndirectFluorescentAntibody(String rabiesIndirectFluorescentAntibody) {
        this.rabiesIndirectFluorescentAntibody = rabiesIndirectFluorescentAntibody;
    }

    public void setRabiesMicroscopy(String rabiesMicroscopy) {
        this.rabiesMicroscopy = rabiesMicroscopy;
    }

    public void setRabiesNeutralizingAntibodies(String rabiesNeutralizingAntibodies) {
        this.rabiesNeutralizingAntibodies = rabiesNeutralizingAntibodies;
    }

    public void setRabiesPcr(String rabiesPcr) {
        this.rabiesPcr = rabiesPcr;
    }

    public void setRabiesGramStain(String rabiesGramStain) {
        this.rabiesGramStain = rabiesGramStain;
    }

    public void setRabiesLatexAgglutination(String rabiesLatexAgglutination) {
        this.rabiesLatexAgglutination = rabiesLatexAgglutination;
    }

    public void setRabiesCqValueDetection(String rabiesCqValueDetection) {
        this.rabiesCqValueDetection = rabiesCqValueDetection;
    }

    public void setRabiesSeQuencing(String rabiesSeQuencing) {
        this.rabiesSeQuencing = rabiesSeQuencing;
    }

    public void setRabiesDnaMicroArray(String rabiesDnaMicroArray) {
        this.rabiesDnaMicroArray = rabiesDnaMicroArray;
    }

    public void setRabiesOther(String rabiesOther) {
        this.rabiesOther = rabiesOther;
    }

    public void setAnthraxAntiBodyDetection(String anthraxAntiBodyDetection) {
        this.anthraxAntiBodyDetection = anthraxAntiBodyDetection;
    }

    public void setAnthraxAntigenDetection(String anthraxAntigenDetection) {
        this.anthraxAntigenDetection = anthraxAntigenDetection;
    }

    public void setAnthraxRapidTest(String anthraxRapidTest) {
        this.anthraxRapidTest = anthraxRapidTest;
    }

    public void setAnthraxCulture(String anthraxCulture) {
        this.anthraxCulture = anthraxCulture;
    }

    public void setAnthraxHistopathology(String anthraxHistopathology) {
        this.anthraxHistopathology = anthraxHistopathology;
    }

    public void setAnthraxIsolation(String anthraxIsolation) {
        this.anthraxIsolation = anthraxIsolation;
    }

    public void setAnthraxIgmSerumAntibody(String anthraxIgmSerumAntibody) {
        this.anthraxIgmSerumAntibody = anthraxIgmSerumAntibody;
    }

    public void setAnthraxIggSerumAntibody(String anthraxIggSerumAntibody) {
        this.anthraxIggSerumAntibody = anthraxIggSerumAntibody;
    }

    public void setAnthraxIgaSerumAntibody(String anthraxIgaSerumAntibody) {
        this.anthraxIgaSerumAntibody = anthraxIgaSerumAntibody;
    }

    public void setAnthraxIncubationTime(String anthraxIncubationTime) {
        this.anthraxIncubationTime = anthraxIncubationTime;
    }

    public void setAnthraxIndirectFluorescentAntibody(String anthraxIndirectFluorescentAntibody) {
        this.anthraxIndirectFluorescentAntibody = anthraxIndirectFluorescentAntibody;
    }

    public void setAnthraxMicroscopy(String anthraxMicroscopy) {
        this.anthraxMicroscopy = anthraxMicroscopy;
    }

    public void setAnthraxNeutralizingAntibodies(String anthraxNeutralizingAntibodies) {
        this.anthraxNeutralizingAntibodies = anthraxNeutralizingAntibodies;
    }

    public void setAnthraxPcr(String anthraxPcr) {
        this.anthraxPcr = anthraxPcr;
    }

    public void setAnthraxGramStain(String anthraxGramStain) {
        this.anthraxGramStain = anthraxGramStain;
    }

    public void setAnthraxLatexAgglutination(String anthraxLatexAgglutination) {
        this.anthraxLatexAgglutination = anthraxLatexAgglutination;
    }

    public void setAnthraxCqValueDetection(String anthraxCqValueDetection) {
        this.anthraxCqValueDetection = anthraxCqValueDetection;
    }

    public void setAnthraxSeQuencing(String anthraxSeQuencing) {
        this.anthraxSeQuencing = anthraxSeQuencing;
    }

    public void setAnthraxDnaMicroArray(String anthraxDnaMicroArray) {
        this.anthraxDnaMicroArray = anthraxDnaMicroArray;
    }

    public void setAnthraxOther(String anthraxOther) {
        this.anthraxOther = anthraxOther;
    }

    public void setCoronavirusAntiBodyDetection(String coronavirusAntiBodyDetection) {
        this.coronavirusAntiBodyDetection = coronavirusAntiBodyDetection;
    }

    public void setCoronavirusAntigenDetection(String coronavirusAntigenDetection) {
        this.coronavirusAntigenDetection = coronavirusAntigenDetection;
    }

    public void setCoronavirusRapidTest(String coronavirusRapidTest) {
        this.coronavirusRapidTest = coronavirusRapidTest;
    }

    public void setCoronavirusCulture(String coronavirusCulture) {
        this.coronavirusCulture = coronavirusCulture;
    }

    public void setCoronavirusHistopathology(String coronavirusHistopathology) {
        this.coronavirusHistopathology = coronavirusHistopathology;
    }

    public void setCoronavirusIsolation(String coronavirusIsolation) {
        this.coronavirusIsolation = coronavirusIsolation;
    }

    public void setCoronavirusIgmSerumAntibody(String coronavirusIgmSerumAntibody) {
        this.coronavirusIgmSerumAntibody = coronavirusIgmSerumAntibody;
    }

    public void setCoronavirusIggSerumAntibody(String coronavirusIggSerumAntibody) {
        this.coronavirusIggSerumAntibody = coronavirusIggSerumAntibody;
    }

    public void setCoronavirusIgaSerumAntibody(String coronavirusIgaSerumAntibody) {
        this.coronavirusIgaSerumAntibody = coronavirusIgaSerumAntibody;
    }

    public void setCoronavirusIncubationTime(String coronavirusIncubationTime) {
        this.coronavirusIncubationTime = coronavirusIncubationTime;
    }

    public void setCoronavirusIndirectFluorescentAntibody(String coronavirusIndirectFluorescentAntibody) {
        this.coronavirusIndirectFluorescentAntibody = coronavirusIndirectFluorescentAntibody;
    }

    public void setCoronavirusMicroscopy(String coronavirusMicroscopy) {
        this.coronavirusMicroscopy = coronavirusMicroscopy;
    }

    public void setCoronavirusNeutralizingAntibodies(String coronavirusNeutralizingAntibodies) {
        this.coronavirusNeutralizingAntibodies = coronavirusNeutralizingAntibodies;
    }

    public void setCoronavirusPcr(String coronavirusPcr) {
        this.coronavirusPcr = coronavirusPcr;
    }

    public void setCoronavirusGramStain(String coronavirusGramStain) {
        this.coronavirusGramStain = coronavirusGramStain;
    }

    public void setCoronavirusLatexAgglutination(String coronavirusLatexAgglutination) {
        this.coronavirusLatexAgglutination = coronavirusLatexAgglutination;
    }

    public void setCoronavirusCqValueDetection(String coronavirusCqValueDetection) {
        this.coronavirusCqValueDetection = coronavirusCqValueDetection;
    }

    public void setCoronavirusSeQuencing(String coronavirusSeQuencing) {
        this.coronavirusSeQuencing = coronavirusSeQuencing;
    }

    public void setCoronavirusDnaMicroArray(String coronavirusDnaMicroArray) {
        this.coronavirusDnaMicroArray = coronavirusDnaMicroArray;
    }

    public void setCoronavirusOther(String coronavirusOther) {
        this.coronavirusOther = coronavirusOther;
    }

    public void setPneumoniaAntiBodyDetection(String pneumoniaAntiBodyDetection) {
        this.pneumoniaAntiBodyDetection = pneumoniaAntiBodyDetection;
    }

    public void setPneumoniaAntigenDetection(String pneumoniaAntigenDetection) {
        this.pneumoniaAntigenDetection = pneumoniaAntigenDetection;
    }

    public void setPneumoniaRapidTest(String pneumoniaRapidTest) {
        this.pneumoniaRapidTest = pneumoniaRapidTest;
    }

    public void setPneumoniaCulture(String pneumoniaCulture) {
        this.pneumoniaCulture = pneumoniaCulture;
    }

    public void setPneumoniaHistopathology(String pneumoniaHistopathology) {
        this.pneumoniaHistopathology = pneumoniaHistopathology;
    }

    public void setPneumoniaIsolation(String pneumoniaIsolation) {
        this.pneumoniaIsolation = pneumoniaIsolation;
    }

    public void setPneumoniaIgmSerumAntibody(String pneumoniaIgmSerumAntibody) {
        this.pneumoniaIgmSerumAntibody = pneumoniaIgmSerumAntibody;
    }

    public void setPneumoniaIggSerumAntibody(String pneumoniaIggSerumAntibody) {
        this.pneumoniaIggSerumAntibody = pneumoniaIggSerumAntibody;
    }

    public void setPneumoniaIgaSerumAntibody(String pneumoniaIgaSerumAntibody) {
        this.pneumoniaIgaSerumAntibody = pneumoniaIgaSerumAntibody;
    }

    public void setPneumoniaIncubationTime(String pneumoniaIncubationTime) {
        this.pneumoniaIncubationTime = pneumoniaIncubationTime;
    }

    public void setPneumoniaIndirectFluorescentAntibody(String pneumoniaIndirectFluorescentAntibody) {
        this.pneumoniaIndirectFluorescentAntibody = pneumoniaIndirectFluorescentAntibody;
    }

    public void setPneumoniaMicroscopy(String pneumoniaMicroscopy) {
        this.pneumoniaMicroscopy = pneumoniaMicroscopy;
    }

    public void setPneumoniaNeutralizingAntibodies(String pneumoniaNeutralizingAntibodies) {
        this.pneumoniaNeutralizingAntibodies = pneumoniaNeutralizingAntibodies;
    }

    public void setPneumoniaPcr(String pneumoniaPcr) {
        this.pneumoniaPcr = pneumoniaPcr;
    }

    public void setPneumoniaGramStain(String pneumoniaGramStain) {
        this.pneumoniaGramStain = pneumoniaGramStain;
    }

    public void setPneumoniaLatexAgglutination(String pneumoniaLatexAgglutination) {
        this.pneumoniaLatexAgglutination = pneumoniaLatexAgglutination;
    }

    public void setPneumoniaCqValueDetection(String pneumoniaCqValueDetection) {
        this.pneumoniaCqValueDetection = pneumoniaCqValueDetection;
    }

    public void setPneumoniaSeQuencing(String pneumoniaSeQuencing) {
        this.pneumoniaSeQuencing = pneumoniaSeQuencing;
    }

    public void setPneumoniaDnaMicroArray(String pneumoniaDnaMicroArray) {
        this.pneumoniaDnaMicroArray = pneumoniaDnaMicroArray;
    }

    public void setPneumoniaOther(String pneumoniaOther) {
        this.pneumoniaOther = pneumoniaOther;
    }

    public void setMalariaAntiBodyDetection(String malariaAntiBodyDetection) {
        this.malariaAntiBodyDetection = malariaAntiBodyDetection;
    }

    public void setMalariaAntigenDetection(String malariaAntigenDetection) {
        this.malariaAntigenDetection = malariaAntigenDetection;
    }

    public void setMalariaRapidTest(String malariaRapidTest) {
        this.malariaRapidTest = malariaRapidTest;
    }

    public void setMalariaCulture(String malariaCulture) {
        this.malariaCulture = malariaCulture;
    }

    public void setMalariaHistopathology(String malariaHistopathology) {
        this.malariaHistopathology = malariaHistopathology;
    }

    public void setMalariaIsolation(String malariaIsolation) {
        this.malariaIsolation = malariaIsolation;
    }

    public void setMalariaIgmSerumAntibody(String malariaIgmSerumAntibody) {
        this.malariaIgmSerumAntibody = malariaIgmSerumAntibody;
    }

    public void setMalariaIggSerumAntibody(String malariaIggSerumAntibody) {
        this.malariaIggSerumAntibody = malariaIggSerumAntibody;
    }

    public void setMalariaIgaSerumAntibody(String malariaIgaSerumAntibody) {
        this.malariaIgaSerumAntibody = malariaIgaSerumAntibody;
    }

    public void setMalariaIncubationTime(String malariaIncubationTime) {
        this.malariaIncubationTime = malariaIncubationTime;
    }

    public void setMalariaIndirectFluorescentAntibody(String malariaIndirectFluorescentAntibody) {
        this.malariaIndirectFluorescentAntibody = malariaIndirectFluorescentAntibody;
    }

    public void setMalariaMicroscopy(String malariaMicroscopy) {
        this.malariaMicroscopy = malariaMicroscopy;
    }

    public void setMalariaNeutralizingAntibodies(String malariaNeutralizingAntibodies) {
        this.malariaNeutralizingAntibodies = malariaNeutralizingAntibodies;
    }

    public void setMalariaPcr(String malariaPcr) {
        this.malariaPcr = malariaPcr;
    }

    public void setMalariaGramStain(String malariaGramStain) {
        this.malariaGramStain = malariaGramStain;
    }

    public void setMalariaLatexAgglutination(String malariaLatexAgglutination) {
        this.malariaLatexAgglutination = malariaLatexAgglutination;
    }

    public void setMalariaCqValueDetection(String malariaCqValueDetection) {
        this.malariaCqValueDetection = malariaCqValueDetection;
    }

    public void setMalariaSeQuencing(String malariaSeQuencing) {
        this.malariaSeQuencing = malariaSeQuencing;
    }

    public void setMalariaDnaMicroArray(String malariaDnaMicroArray) {
        this.malariaDnaMicroArray = malariaDnaMicroArray;
    }

    public void setMalariaOther(String malariaOther) {
        this.malariaOther = malariaOther;
    }

    public void setTyphoidFeverAntiBodyDetection(String typhoidFeverAntiBodyDetection) {
        this.typhoidFeverAntiBodyDetection = typhoidFeverAntiBodyDetection;
    }

    public void setTyphoidFeverAntigenDetection(String typhoidFeverAntigenDetection) {
        this.typhoidFeverAntigenDetection = typhoidFeverAntigenDetection;
    }

    public void setTyphoidFeverRapidTest(String typhoidFeverRapidTest) {
        this.typhoidFeverRapidTest = typhoidFeverRapidTest;
    }

    public void setTyphoidFeverCulture(String typhoidFeverCulture) {
        this.typhoidFeverCulture = typhoidFeverCulture;
    }

    public void setTyphoidFeverHistopathology(String typhoidFeverHistopathology) {
        this.typhoidFeverHistopathology = typhoidFeverHistopathology;
    }

    public void setTyphoidFeverIsolation(String typhoidFeverIsolation) {
        this.typhoidFeverIsolation = typhoidFeverIsolation;
    }

    public void setTyphoidFeverIgmSerumAntibody(String typhoidFeverIgmSerumAntibody) {
        this.typhoidFeverIgmSerumAntibody = typhoidFeverIgmSerumAntibody;
    }

    public void setTyphoidFeverIggSerumAntibody(String typhoidFeverIggSerumAntibody) {
        this.typhoidFeverIggSerumAntibody = typhoidFeverIggSerumAntibody;
    }

    public void setTyphoidFeverIgaSerumAntibody(String typhoidFeverIgaSerumAntibody) {
        this.typhoidFeverIgaSerumAntibody = typhoidFeverIgaSerumAntibody;
    }

    public void setTyphoidFeverIncubationTime(String typhoidFeverIncubationTime) {
        this.typhoidFeverIncubationTime = typhoidFeverIncubationTime;
    }

    public void setTyphoidFeverIndirectFluorescentAntibody(String typhoidFeverIndirectFluorescentAntibody) {
        this.typhoidFeverIndirectFluorescentAntibody = typhoidFeverIndirectFluorescentAntibody;
    }

    public void setTyphoidFeverMicroscopy(String typhoidFeverMicroscopy) {
        this.typhoidFeverMicroscopy = typhoidFeverMicroscopy;
    }

    public void setTyphoidFeverNeutralizingAntibodies(String typhoidFeverNeutralizingAntibodies) {
        this.typhoidFeverNeutralizingAntibodies = typhoidFeverNeutralizingAntibodies;
    }

    public void setTyphoidFeverPcr(String typhoidFeverPcr) {
        this.typhoidFeverPcr = typhoidFeverPcr;
    }

    public void setTyphoidFeverGramStain(String typhoidFeverGramStain) {
        this.typhoidFeverGramStain = typhoidFeverGramStain;
    }

    public void setTyphoidFeverLatexAgglutination(String typhoidFeverLatexAgglutination) {
        this.typhoidFeverLatexAgglutination = typhoidFeverLatexAgglutination;
    }

    public void setTyphoidFeverCqValueDetection(String typhoidFeverCqValueDetection) {
        this.typhoidFeverCqValueDetection = typhoidFeverCqValueDetection;
    }

    public void setTyphoidFeverSeQuencing(String typhoidFeverSeQuencing) {
        this.typhoidFeverSeQuencing = typhoidFeverSeQuencing;
    }

    public void setTyphoidFeverDnaMicroArray(String typhoidFeverDnaMicroArray) {
        this.typhoidFeverDnaMicroArray = typhoidFeverDnaMicroArray;
    }

    public void setTyphoidFeverOther(String typhoidFeverOther) {
        this.typhoidFeverOther = typhoidFeverOther;
    }

    public void setAcuteViralHepatitisAntiBodyDetection(String acuteViralHepatitisAntiBodyDetection) {
        this.acuteViralHepatitisAntiBodyDetection = acuteViralHepatitisAntiBodyDetection;
    }

    public void setAcuteViralHepatitisAntigenDetection(String acuteViralHepatitisAntigenDetection) {
        this.acuteViralHepatitisAntigenDetection = acuteViralHepatitisAntigenDetection;
    }

    public void setAcuteViralHepatitisRapidTest(String acuteViralHepatitisRapidTest) {
        this.acuteViralHepatitisRapidTest = acuteViralHepatitisRapidTest;
    }

    public void setAcuteViralHepatitisCulture(String acuteViralHepatitisCulture) {
        this.acuteViralHepatitisCulture = acuteViralHepatitisCulture;
    }

    public void setAcuteViralHepatitisHistopathology(String acuteViralHepatitisHistopathology) {
        this.acuteViralHepatitisHistopathology = acuteViralHepatitisHistopathology;
    }

    public void setAcuteViralHepatitisIsolation(String acuteViralHepatitisIsolation) {
        this.acuteViralHepatitisIsolation = acuteViralHepatitisIsolation;
    }

    public void setAcuteViralHepatitisIgmSerumAntibody(String acuteViralHepatitisIgmSerumAntibody) {
        this.acuteViralHepatitisIgmSerumAntibody = acuteViralHepatitisIgmSerumAntibody;
    }

    public void setAcuteViralHepatitisIggSerumAntibody(String acuteViralHepatitisIggSerumAntibody) {
        this.acuteViralHepatitisIggSerumAntibody = acuteViralHepatitisIggSerumAntibody;
    }

    public void setAcuteViralHepatitisIgaSerumAntibody(String acuteViralHepatitisIgaSerumAntibody) {
        this.acuteViralHepatitisIgaSerumAntibody = acuteViralHepatitisIgaSerumAntibody;
    }

    public void setAcuteViralHepatitisIncubationTime(String acuteViralHepatitisIncubationTime) {
        this.acuteViralHepatitisIncubationTime = acuteViralHepatitisIncubationTime;
    }

    public void setAcuteViralHepatitisIndirectFluorescentAntibody(String acuteViralHepatitisIndirectFluorescentAntibody) {
        this.acuteViralHepatitisIndirectFluorescentAntibody = acuteViralHepatitisIndirectFluorescentAntibody;
    }

    public void setAcuteViralHepatitisMicroscopy(String acuteViralHepatitisMicroscopy) {
        this.acuteViralHepatitisMicroscopy = acuteViralHepatitisMicroscopy;
    }

    public void setAcuteViralHepatitisNeutralizingAntibodies(String acuteViralHepatitisNeutralizingAntibodies) {
        this.acuteViralHepatitisNeutralizingAntibodies = acuteViralHepatitisNeutralizingAntibodies;
    }

    public void setAcuteViralHepatitisPcr(String acuteViralHepatitisPcr) {
        this.acuteViralHepatitisPcr = acuteViralHepatitisPcr;
    }

    public void setAcuteViralHepatitisGramStain(String acuteViralHepatitisGramStain) {
        this.acuteViralHepatitisGramStain = acuteViralHepatitisGramStain;
    }

    public void setAcuteViralHepatitisLatexAgglutination(String acuteViralHepatitisLatexAgglutination) {
        this.acuteViralHepatitisLatexAgglutination = acuteViralHepatitisLatexAgglutination;
    }

    public void setAcuteViralHepatitisCqValueDetection(String acuteViralHepatitisCqValueDetection) {
        this.acuteViralHepatitisCqValueDetection = acuteViralHepatitisCqValueDetection;
    }

    public void setAcuteViralHepatitisSeQuencing(String acuteViralHepatitisSeQuencing) {
        this.acuteViralHepatitisSeQuencing = acuteViralHepatitisSeQuencing;
    }

    public void setAcuteViralHepatitisDnaMicroArray(String acuteViralHepatitisDnaMicroArray) {
        this.acuteViralHepatitisDnaMicroArray = acuteViralHepatitisDnaMicroArray;
    }

    public void setAcuteViralHepatitisOther(String acuteViralHepatitisOther) {
        this.acuteViralHepatitisOther = acuteViralHepatitisOther;
    }

    public void setNonNeonatalTetanusAntiBodyDetection(String nonNeonatalTetanusAntiBodyDetection) {
        this.nonNeonatalTetanusAntiBodyDetection = nonNeonatalTetanusAntiBodyDetection;
    }

    public void setNonNeonatalTetanusAntigenDetection(String nonNeonatalTetanusAntigenDetection) {
        this.nonNeonatalTetanusAntigenDetection = nonNeonatalTetanusAntigenDetection;
    }

    public void setNonNeonatalTetanusRapidTest(String nonNeonatalTetanusRapidTest) {
        this.nonNeonatalTetanusRapidTest = nonNeonatalTetanusRapidTest;
    }

    public void setNonNeonatalTetanusCulture(String nonNeonatalTetanusCulture) {
        this.nonNeonatalTetanusCulture = nonNeonatalTetanusCulture;
    }

    public void setNonNeonatalTetanusHistopathology(String nonNeonatalTetanusHistopathology) {
        this.nonNeonatalTetanusHistopathology = nonNeonatalTetanusHistopathology;
    }

    public void setNonNeonatalTetanusIsolation(String nonNeonatalTetanusIsolation) {
        this.nonNeonatalTetanusIsolation = nonNeonatalTetanusIsolation;
    }

    public void setNonNeonatalTetanusIgmSerumAntibody(String nonNeonatalTetanusIgmSerumAntibody) {
        this.nonNeonatalTetanusIgmSerumAntibody = nonNeonatalTetanusIgmSerumAntibody;
    }

    public void setNonNeonatalTetanusIggSerumAntibody(String nonNeonatalTetanusIggSerumAntibody) {
        this.nonNeonatalTetanusIggSerumAntibody = nonNeonatalTetanusIggSerumAntibody;
    }

    public void setNonNeonatalTetanusIgaSerumAntibody(String nonNeonatalTetanusIgaSerumAntibody) {
        this.nonNeonatalTetanusIgaSerumAntibody = nonNeonatalTetanusIgaSerumAntibody;
    }

    public void setNonNeonatalTetanusIncubationTime(String nonNeonatalTetanusIncubationTime) {
        this.nonNeonatalTetanusIncubationTime = nonNeonatalTetanusIncubationTime;
    }

    public void setNonNeonatalTetanusIndirectFluorescentAntibody(String nonNeonatalTetanusIndirectFluorescentAntibody) {
        this.nonNeonatalTetanusIndirectFluorescentAntibody = nonNeonatalTetanusIndirectFluorescentAntibody;
    }

    public void setNonNeonatalTetanusMicroscopy(String nonNeonatalTetanusMicroscopy) {
        this.nonNeonatalTetanusMicroscopy = nonNeonatalTetanusMicroscopy;
    }

    public void setNonNeonatalTetanusNeutralizingAntibodies(String nonNeonatalTetanusNeutralizingAntibodies) {
        this.nonNeonatalTetanusNeutralizingAntibodies = nonNeonatalTetanusNeutralizingAntibodies;
    }

    public void setNonNeonatalTetanusPcr(String nonNeonatalTetanusPcr) {
        this.nonNeonatalTetanusPcr = nonNeonatalTetanusPcr;
    }

    public void setNonNeonatalTetanusGramStain(String nonNeonatalTetanusGramStain) {
        this.nonNeonatalTetanusGramStain = nonNeonatalTetanusGramStain;
    }

    public void setNonNeonatalTetanusLatexAgglutination(String nonNeonatalTetanusLatexAgglutination) {
        this.nonNeonatalTetanusLatexAgglutination = nonNeonatalTetanusLatexAgglutination;
    }

    public void setNonNeonatalTetanusCqValueDetection(String nonNeonatalTetanusCqValueDetection) {
        this.nonNeonatalTetanusCqValueDetection = nonNeonatalTetanusCqValueDetection;
    }

    public void setNonNeonatalTetanusSeQuencing(String nonNeonatalTetanusSeQuencing) {
        this.nonNeonatalTetanusSeQuencing = nonNeonatalTetanusSeQuencing;
    }

    public void setNonNeonatalTetanusDnaMicroArray(String nonNeonatalTetanusDnaMicroArray) {
        this.nonNeonatalTetanusDnaMicroArray = nonNeonatalTetanusDnaMicroArray;
    }

    public void setNonNeonatalTetanusOther(String nonNeonatalTetanusOther) {
        this.nonNeonatalTetanusOther = nonNeonatalTetanusOther;
    }

    public void setHivAntiBodyDetection(String hivAntiBodyDetection) {
        this.hivAntiBodyDetection = hivAntiBodyDetection;
    }

    public void setHivAntigenDetection(String hivAntigenDetection) {
        this.hivAntigenDetection = hivAntigenDetection;
    }

    public void setHivRapidTest(String hivRapidTest) {
        this.hivRapidTest = hivRapidTest;
    }

    public void setHivCulture(String hivCulture) {
        this.hivCulture = hivCulture;
    }

    public void setHivHistopathology(String hivHistopathology) {
        this.hivHistopathology = hivHistopathology;
    }

    public void setHivIsolation(String hivIsolation) {
        this.hivIsolation = hivIsolation;
    }

    public void setHivIgmSerumAntibody(String hivIgmSerumAntibody) {
        this.hivIgmSerumAntibody = hivIgmSerumAntibody;
    }

    public void setHivIggSerumAntibody(String hivIggSerumAntibody) {
        this.hivIggSerumAntibody = hivIggSerumAntibody;
    }

    public void setHivIgaSerumAntibody(String hivIgaSerumAntibody) {
        this.hivIgaSerumAntibody = hivIgaSerumAntibody;
    }

    public void setHivIncubationTime(String hivIncubationTime) {
        this.hivIncubationTime = hivIncubationTime;
    }

    public void setHivIndirectFluorescentAntibody(String hivIndirectFluorescentAntibody) {
        this.hivIndirectFluorescentAntibody = hivIndirectFluorescentAntibody;
    }

    public void setHivMicroscopy(String hivMicroscopy) {
        this.hivMicroscopy = hivMicroscopy;
    }

    public void setHivNeutralizingAntibodies(String hivNeutralizingAntibodies) {
        this.hivNeutralizingAntibodies = hivNeutralizingAntibodies;
    }

    public void setHivPcr(String hivPcr) {
        this.hivPcr = hivPcr;
    }

    public void setHivGramStain(String hivGramStain) {
        this.hivGramStain = hivGramStain;
    }

    public void setHivLatexAgglutination(String hivLatexAgglutination) {
        this.hivLatexAgglutination = hivLatexAgglutination;
    }

    public void setHivCqValueDetection(String hivCqValueDetection) {
        this.hivCqValueDetection = hivCqValueDetection;
    }

    public void setHivSeQuencing(String hivSeQuencing) {
        this.hivSeQuencing = hivSeQuencing;
    }

    public void setHivDnaMicroArray(String hivDnaMicroArray) {
        this.hivDnaMicroArray = hivDnaMicroArray;
    }

    public void setHivOther(String hivOther) {
        this.hivOther = hivOther;
    }

    public void setSchistosomiasisAntiBodyDetection(String schistosomiasisAntiBodyDetection) {
        this.schistosomiasisAntiBodyDetection = schistosomiasisAntiBodyDetection;
    }

    public void setSchistosomiasisAntigenDetection(String schistosomiasisAntigenDetection) {
        this.schistosomiasisAntigenDetection = schistosomiasisAntigenDetection;
    }

    public void setSchistosomiasisRapidTest(String schistosomiasisRapidTest) {
        this.schistosomiasisRapidTest = schistosomiasisRapidTest;
    }

    public void setSchistosomiasisCulture(String schistosomiasisCulture) {
        this.schistosomiasisCulture = schistosomiasisCulture;
    }

    public void setSchistosomiasisHistopathology(String schistosomiasisHistopathology) {
        this.schistosomiasisHistopathology = schistosomiasisHistopathology;
    }

    public void setSchistosomiasisIsolation(String schistosomiasisIsolation) {
        this.schistosomiasisIsolation = schistosomiasisIsolation;
    }

    public void setSchistosomiasisIgmSerumAntibody(String schistosomiasisIgmSerumAntibody) {
        this.schistosomiasisIgmSerumAntibody = schistosomiasisIgmSerumAntibody;
    }

    public void setSchistosomiasisIggSerumAntibody(String schistosomiasisIggSerumAntibody) {
        this.schistosomiasisIggSerumAntibody = schistosomiasisIggSerumAntibody;
    }

    public void setSchistosomiasisIgaSerumAntibody(String schistosomiasisIgaSerumAntibody) {
        this.schistosomiasisIgaSerumAntibody = schistosomiasisIgaSerumAntibody;
    }

    public void setSchistosomiasisIncubationTime(String schistosomiasisIncubationTime) {
        this.schistosomiasisIncubationTime = schistosomiasisIncubationTime;
    }

    public void setSchistosomiasisIndirectFluorescentAntibody(String schistosomiasisIndirectFluorescentAntibody) {
        this.schistosomiasisIndirectFluorescentAntibody = schistosomiasisIndirectFluorescentAntibody;
    }

    public void setSchistosomiasisMicroscopy(String schistosomiasisMicroscopy) {
        this.schistosomiasisMicroscopy = schistosomiasisMicroscopy;
    }

    public void setSchistosomiasisNeutralizingAntibodies(String schistosomiasisNeutralizingAntibodies) {
        this.schistosomiasisNeutralizingAntibodies = schistosomiasisNeutralizingAntibodies;
    }

    public void setSchistosomiasisPcr(String schistosomiasisPcr) {
        this.schistosomiasisPcr = schistosomiasisPcr;
    }

    public void setSchistosomiasisGramStain(String schistosomiasisGramStain) {
        this.schistosomiasisGramStain = schistosomiasisGramStain;
    }

    public void setSchistosomiasisLatexAgglutination(String schistosomiasisLatexAgglutination) {
        this.schistosomiasisLatexAgglutination = schistosomiasisLatexAgglutination;
    }

    public void setSchistosomiasisCqValueDetection(String schistosomiasisCqValueDetection) {
        this.schistosomiasisCqValueDetection = schistosomiasisCqValueDetection;
    }

    public void setSchistosomiasisSeQuencing(String schistosomiasisSeQuencing) {
        this.schistosomiasisSeQuencing = schistosomiasisSeQuencing;
    }

    public void setSchistosomiasisDnaMicroArray(String schistosomiasisDnaMicroArray) {
        this.schistosomiasisDnaMicroArray = schistosomiasisDnaMicroArray;
    }

    public void setSchistosomiasisOther(String schistosomiasisOther) {
        this.schistosomiasisOther = schistosomiasisOther;
    }

    public void setSoilTransmittedHelminthsAntiBodyDetection(String soilTransmittedHelminthsAntiBodyDetection) {
        this.soilTransmittedHelminthsAntiBodyDetection = soilTransmittedHelminthsAntiBodyDetection;
    }

    public void setSoilTransmittedHelminthsAntigenDetection(String soilTransmittedHelminthsAntigenDetection) {
        this.soilTransmittedHelminthsAntigenDetection = soilTransmittedHelminthsAntigenDetection;
    }

    public void setSoilTransmittedHelminthsRapidTest(String soilTransmittedHelminthsRapidTest) {
        this.soilTransmittedHelminthsRapidTest = soilTransmittedHelminthsRapidTest;
    }

    public void setSoilTransmittedHelminthsCulture(String soilTransmittedHelminthsCulture) {
        this.soilTransmittedHelminthsCulture = soilTransmittedHelminthsCulture;
    }

    public void setSoilTransmittedHelminthsHistopathology(String soilTransmittedHelminthsHistopathology) {
        this.soilTransmittedHelminthsHistopathology = soilTransmittedHelminthsHistopathology;
    }

    public void setSoilTransmittedHelminthsIsolation(String soilTransmittedHelminthsIsolation) {
        this.soilTransmittedHelminthsIsolation = soilTransmittedHelminthsIsolation;
    }

    public void setSoilTransmittedHelminthsIgmSerumAntibody(String soilTransmittedHelminthsIgmSerumAntibody) {
        this.soilTransmittedHelminthsIgmSerumAntibody = soilTransmittedHelminthsIgmSerumAntibody;
    }

    public void setSoilTransmittedHelminthsIggSerumAntibody(String soilTransmittedHelminthsIggSerumAntibody) {
        this.soilTransmittedHelminthsIggSerumAntibody = soilTransmittedHelminthsIggSerumAntibody;
    }

    public void setSoilTransmittedHelminthsIgaSerumAntibody(String soilTransmittedHelminthsIgaSerumAntibody) {
        this.soilTransmittedHelminthsIgaSerumAntibody = soilTransmittedHelminthsIgaSerumAntibody;
    }

    public void setSoilTransmittedHelminthsIncubationTime(String soilTransmittedHelminthsIncubationTime) {
        this.soilTransmittedHelminthsIncubationTime = soilTransmittedHelminthsIncubationTime;
    }

    public void setSoilTransmittedHelminthsIndirectFluorescentAntibody(String soilTransmittedHelminthsIndirectFluorescentAntibody) {
        this.soilTransmittedHelminthsIndirectFluorescentAntibody = soilTransmittedHelminthsIndirectFluorescentAntibody;
    }

    public void setSoilTransmittedHelminthsMicroscopy(String soilTransmittedHelminthsMicroscopy) {
        this.soilTransmittedHelminthsMicroscopy = soilTransmittedHelminthsMicroscopy;
    }

    public void setSoilTransmittedHelminthsNeutralizingAntibodies(String soilTransmittedHelminthsNeutralizingAntibodies) {
        this.soilTransmittedHelminthsNeutralizingAntibodies = soilTransmittedHelminthsNeutralizingAntibodies;
    }

    public void setSoilTransmittedHelminthsPcr(String soilTransmittedHelminthsPcr) {
        this.soilTransmittedHelminthsPcr = soilTransmittedHelminthsPcr;
    }

    public void setSoilTransmittedHelminthsGramStain(String soilTransmittedHelminthsGramStain) {
        this.soilTransmittedHelminthsGramStain = soilTransmittedHelminthsGramStain;
    }

    public void setSoilTransmittedHelminthsLatexAgglutination(String soilTransmittedHelminthsLatexAgglutination) {
        this.soilTransmittedHelminthsLatexAgglutination = soilTransmittedHelminthsLatexAgglutination;
    }

    public void setSoilTransmittedHelminthsCqValueDetection(String soilTransmittedHelminthsCqValueDetection) {
        this.soilTransmittedHelminthsCqValueDetection = soilTransmittedHelminthsCqValueDetection;
    }

    public void setSoilTransmittedHelminthsSeQuencing(String soilTransmittedHelminthsSeQuencing) {
        this.soilTransmittedHelminthsSeQuencing = soilTransmittedHelminthsSeQuencing;
    }

    public void setSoilTransmittedHelminthsDnaMicroArray(String soilTransmittedHelminthsDnaMicroArray) {
        this.soilTransmittedHelminthsDnaMicroArray = soilTransmittedHelminthsDnaMicroArray;
    }

    public void setSoilTransmittedHelminthsOther(String soilTransmittedHelminthsOther) {
        this.soilTransmittedHelminthsOther = soilTransmittedHelminthsOther;
    }

    public void setTrypanosomiasisAntiBodyDetection(String trypanosomiasisAntiBodyDetection) {
        this.trypanosomiasisAntiBodyDetection = trypanosomiasisAntiBodyDetection;
    }

    public void setTrypanosomiasisAntigenDetection(String trypanosomiasisAntigenDetection) {
        this.trypanosomiasisAntigenDetection = trypanosomiasisAntigenDetection;
    }

    public void setTrypanosomiasisRapidTest(String trypanosomiasisRapidTest) {
        this.trypanosomiasisRapidTest = trypanosomiasisRapidTest;
    }

    public void setTrypanosomiasisCulture(String trypanosomiasisCulture) {
        this.trypanosomiasisCulture = trypanosomiasisCulture;
    }

    public void setTrypanosomiasisHistopathology(String trypanosomiasisHistopathology) {
        this.trypanosomiasisHistopathology = trypanosomiasisHistopathology;
    }

    public void setTrypanosomiasisIsolation(String trypanosomiasisIsolation) {
        this.trypanosomiasisIsolation = trypanosomiasisIsolation;
    }

    public void setTrypanosomiasisIgmSerumAntibody(String trypanosomiasisIgmSerumAntibody) {
        this.trypanosomiasisIgmSerumAntibody = trypanosomiasisIgmSerumAntibody;
    }

    public void setTrypanosomiasisIggSerumAntibody(String trypanosomiasisIggSerumAntibody) {
        this.trypanosomiasisIggSerumAntibody = trypanosomiasisIggSerumAntibody;
    }

    public void setTrypanosomiasisIgaSerumAntibody(String trypanosomiasisIgaSerumAntibody) {
        this.trypanosomiasisIgaSerumAntibody = trypanosomiasisIgaSerumAntibody;
    }

    public void setTrypanosomiasisIncubationTime(String trypanosomiasisIncubationTime) {
        this.trypanosomiasisIncubationTime = trypanosomiasisIncubationTime;
    }

    public void setTrypanosomiasisIndirectFluorescentAntibody(String trypanosomiasisIndirectFluorescentAntibody) {
        this.trypanosomiasisIndirectFluorescentAntibody = trypanosomiasisIndirectFluorescentAntibody;
    }

    public void setTrypanosomiasisMicroscopy(String trypanosomiasisMicroscopy) {
        this.trypanosomiasisMicroscopy = trypanosomiasisMicroscopy;
    }

    public void setTrypanosomiasisNeutralizingAntibodies(String trypanosomiasisNeutralizingAntibodies) {
        this.trypanosomiasisNeutralizingAntibodies = trypanosomiasisNeutralizingAntibodies;
    }

    public void setTrypanosomiasisPcr(String trypanosomiasisPcr) {
        this.trypanosomiasisPcr = trypanosomiasisPcr;
    }

    public void setTrypanosomiasisGramStain(String trypanosomiasisGramStain) {
        this.trypanosomiasisGramStain = trypanosomiasisGramStain;
    }

    public void setTrypanosomiasisLatexAgglutination(String trypanosomiasisLatexAgglutination) {
        this.trypanosomiasisLatexAgglutination = trypanosomiasisLatexAgglutination;
    }

    public void setTrypanosomiasisCqValueDetection(String trypanosomiasisCqValueDetection) {
        this.trypanosomiasisCqValueDetection = trypanosomiasisCqValueDetection;
    }

    public void setTrypanosomiasisSeQuencing(String trypanosomiasisSeQuencing) {
        this.trypanosomiasisSeQuencing = trypanosomiasisSeQuencing;
    }

    public void setTrypanosomiasisDnaMicroArray(String trypanosomiasisDnaMicroArray) {
        this.trypanosomiasisDnaMicroArray = trypanosomiasisDnaMicroArray;
    }

    public void setTrypanosomiasisOther(String trypanosomiasisOther) {
        this.trypanosomiasisOther = trypanosomiasisOther;
    }

    public void setDiarrheaDehydrationAntiBodyDetection(String diarrheaDehydrationAntiBodyDetection) {
        this.diarrheaDehydrationAntiBodyDetection = diarrheaDehydrationAntiBodyDetection;
    }

    public void setDiarrheaDehydrationAntigenDetection(String diarrheaDehydrationAntigenDetection) {
        this.diarrheaDehydrationAntigenDetection = diarrheaDehydrationAntigenDetection;
    }

    public void setDiarrheaDehydrationRapidTest(String diarrheaDehydrationRapidTest) {
        this.diarrheaDehydrationRapidTest = diarrheaDehydrationRapidTest;
    }

    public void setDiarrheaDehydrationCulture(String diarrheaDehydrationCulture) {
        this.diarrheaDehydrationCulture = diarrheaDehydrationCulture;
    }

    public void setDiarrheaDehydrationHistopathology(String diarrheaDehydrationHistopathology) {
        this.diarrheaDehydrationHistopathology = diarrheaDehydrationHistopathology;
    }

    public void setDiarrheaDehydrationIsolation(String diarrheaDehydrationIsolation) {
        this.diarrheaDehydrationIsolation = diarrheaDehydrationIsolation;
    }

    public void setDiarrheaDehydrationIgmSerumAntibody(String diarrheaDehydrationIgmSerumAntibody) {
        this.diarrheaDehydrationIgmSerumAntibody = diarrheaDehydrationIgmSerumAntibody;
    }

    public void setDiarrheaDehydrationIggSerumAntibody(String diarrheaDehydrationIggSerumAntibody) {
        this.diarrheaDehydrationIggSerumAntibody = diarrheaDehydrationIggSerumAntibody;
    }

    public void setDiarrheaDehydrationIgaSerumAntibody(String diarrheaDehydrationIgaSerumAntibody) {
        this.diarrheaDehydrationIgaSerumAntibody = diarrheaDehydrationIgaSerumAntibody;
    }

    public void setDiarrheaDehydrationIncubationTime(String diarrheaDehydrationIncubationTime) {
        this.diarrheaDehydrationIncubationTime = diarrheaDehydrationIncubationTime;
    }

    public void setDiarrheaDehydrationIndirectFluorescentAntibody(String diarrheaDehydrationIndirectFluorescentAntibody) {
        this.diarrheaDehydrationIndirectFluorescentAntibody = diarrheaDehydrationIndirectFluorescentAntibody;
    }

    public void setDiarrheaDehydrationMicroscopy(String diarrheaDehydrationMicroscopy) {
        this.diarrheaDehydrationMicroscopy = diarrheaDehydrationMicroscopy;
    }

    public void setDiarrheaDehydrationNeutralizingAntibodies(String diarrheaDehydrationNeutralizingAntibodies) {
        this.diarrheaDehydrationNeutralizingAntibodies = diarrheaDehydrationNeutralizingAntibodies;
    }

    public void setDiarrheaDehydrationPcr(String diarrheaDehydrationPcr) {
        this.diarrheaDehydrationPcr = diarrheaDehydrationPcr;
    }

    public void setDiarrheaDehydrationGramStain(String diarrheaDehydrationGramStain) {
        this.diarrheaDehydrationGramStain = diarrheaDehydrationGramStain;
    }

    public void setDiarrheaDehydrationLatexAgglutination(String diarrheaDehydrationLatexAgglutination) {
        this.diarrheaDehydrationLatexAgglutination = diarrheaDehydrationLatexAgglutination;
    }

    public void setDiarrheaDehydrationCqValueDetection(String diarrheaDehydrationCqValueDetection) {
        this.diarrheaDehydrationCqValueDetection = diarrheaDehydrationCqValueDetection;
    }

    public void setDiarrheaDehydrationSeQuencing(String diarrheaDehydrationSeQuencing) {
        this.diarrheaDehydrationSeQuencing = diarrheaDehydrationSeQuencing;
    }

    public void setDiarrheaDehydrationDnaMicroArray(String diarrheaDehydrationDnaMicroArray) {
        this.diarrheaDehydrationDnaMicroArray = diarrheaDehydrationDnaMicroArray;
    }

    public void setDiarrheaDehydrationOther(String diarrheaDehydrationOther) {
        this.diarrheaDehydrationOther = diarrheaDehydrationOther;
    }

    public void setDiarrheaBloodAntiBodyDetection(String diarrheaBloodAntiBodyDetection) {
        this.diarrheaBloodAntiBodyDetection = diarrheaBloodAntiBodyDetection;
    }

    public void setDiarrheaBloodAntigenDetection(String diarrheaBloodAntigenDetection) {
        this.diarrheaBloodAntigenDetection = diarrheaBloodAntigenDetection;
    }

    public void setDiarrheaBloodRapidTest(String diarrheaBloodRapidTest) {
        this.diarrheaBloodRapidTest = diarrheaBloodRapidTest;
    }

    public void setDiarrheaBloodCulture(String diarrheaBloodCulture) {
        this.diarrheaBloodCulture = diarrheaBloodCulture;
    }

    public void setDiarrheaBloodHistopathology(String diarrheaBloodHistopathology) {
        this.diarrheaBloodHistopathology = diarrheaBloodHistopathology;
    }

    public void setDiarrheaBloodIsolation(String diarrheaBloodIsolation) {
        this.diarrheaBloodIsolation = diarrheaBloodIsolation;
    }

    public void setDiarrheaBloodIgmSerumAntibody(String diarrheaBloodIgmSerumAntibody) {
        this.diarrheaBloodIgmSerumAntibody = diarrheaBloodIgmSerumAntibody;
    }

    public void setDiarrheaBloodIggSerumAntibody(String diarrheaBloodIggSerumAntibody) {
        this.diarrheaBloodIggSerumAntibody = diarrheaBloodIggSerumAntibody;
    }

    public void setDiarrheaBloodIgaSerumAntibody(String diarrheaBloodIgaSerumAntibody) {
        this.diarrheaBloodIgaSerumAntibody = diarrheaBloodIgaSerumAntibody;
    }

    public void setDiarrheaBloodIncubationTime(String diarrheaBloodIncubationTime) {
        this.diarrheaBloodIncubationTime = diarrheaBloodIncubationTime;
    }

    public void setDiarrheaBloodIndirectFluorescentAntibody(String diarrheaBloodIndirectFluorescentAntibody) {
        this.diarrheaBloodIndirectFluorescentAntibody = diarrheaBloodIndirectFluorescentAntibody;
    }

    public void setDiarrheaBloodMicroscopy(String diarrheaBloodMicroscopy) {
        this.diarrheaBloodMicroscopy = diarrheaBloodMicroscopy;
    }

    public void setDiarrheaBloodNeutralizingAntibodies(String diarrheaBloodNeutralizingAntibodies) {
        this.diarrheaBloodNeutralizingAntibodies = diarrheaBloodNeutralizingAntibodies;
    }

    public void setDiarrheaBloodPcr(String diarrheaBloodPcr) {
        this.diarrheaBloodPcr = diarrheaBloodPcr;
    }

    public void setDiarrheaBloodGramStain(String diarrheaBloodGramStain) {
        this.diarrheaBloodGramStain = diarrheaBloodGramStain;
    }

    public void setDiarrheaBloodLatexAgglutination(String diarrheaBloodLatexAgglutination) {
        this.diarrheaBloodLatexAgglutination = diarrheaBloodLatexAgglutination;
    }

    public void setDiarrheaBloodCqValueDetection(String diarrheaBloodCqValueDetection) {
        this.diarrheaBloodCqValueDetection = diarrheaBloodCqValueDetection;
    }

    public void setDiarrheaBloodSeQuencing(String diarrheaBloodSeQuencing) {
        this.diarrheaBloodSeQuencing = diarrheaBloodSeQuencing;
    }

    public void setDiarrheaBloodDnaMicroArray(String diarrheaBloodDnaMicroArray) {
        this.diarrheaBloodDnaMicroArray = diarrheaBloodDnaMicroArray;
    }

    public void setDiarrheaBloodOther(String diarrheaBloodOther) {
        this.diarrheaBloodOther = diarrheaBloodOther;
    }

    public void setSnakeBiteAntiBodyDetection(String snakeBiteAntiBodyDetection) {
        this.snakeBiteAntiBodyDetection = snakeBiteAntiBodyDetection;
    }

    public void setSnakeBiteAntigenDetection(String snakeBiteAntigenDetection) {
        this.snakeBiteAntigenDetection = snakeBiteAntigenDetection;
    }

    public void setSnakeBiteRapidTest(String snakeBiteRapidTest) {
        this.snakeBiteRapidTest = snakeBiteRapidTest;
    }

    public void setSnakeBiteCulture(String snakeBiteCulture) {
        this.snakeBiteCulture = snakeBiteCulture;
    }

    public void setSnakeBiteHistopathology(String snakeBiteHistopathology) {
        this.snakeBiteHistopathology = snakeBiteHistopathology;
    }

    public void setSnakeBiteIsolation(String snakeBiteIsolation) {
        this.snakeBiteIsolation = snakeBiteIsolation;
    }

    public void setSnakeBiteIgmSerumAntibody(String snakeBiteIgmSerumAntibody) {
        this.snakeBiteIgmSerumAntibody = snakeBiteIgmSerumAntibody;
    }

    public void setSnakeBiteIggSerumAntibody(String snakeBiteIggSerumAntibody) {
        this.snakeBiteIggSerumAntibody = snakeBiteIggSerumAntibody;
    }

    public void setSnakeBiteIgaSerumAntibody(String snakeBiteIgaSerumAntibody) {
        this.snakeBiteIgaSerumAntibody = snakeBiteIgaSerumAntibody;
    }

    public void setSnakeBiteIncubationTime(String snakeBiteIncubationTime) {
        this.snakeBiteIncubationTime = snakeBiteIncubationTime;
    }

    public void setSnakeBiteIndirectFluorescentAntibody(String snakeBiteIndirectFluorescentAntibody) {
        this.snakeBiteIndirectFluorescentAntibody = snakeBiteIndirectFluorescentAntibody;
    }

    public void setSnakeBiteMicroscopy(String snakeBiteMicroscopy) {
        this.snakeBiteMicroscopy = snakeBiteMicroscopy;
    }

    public void setSnakeBiteNeutralizingAntibodies(String snakeBiteNeutralizingAntibodies) {
        this.snakeBiteNeutralizingAntibodies = snakeBiteNeutralizingAntibodies;
    }

    public void setSnakeBitePcr(String snakeBitePcr) {
        this.snakeBitePcr = snakeBitePcr;
    }

    public void setSnakeBiteGramStain(String snakeBiteGramStain) {
        this.snakeBiteGramStain = snakeBiteGramStain;
    }

    public void setSnakeBiteLatexAgglutination(String snakeBiteLatexAgglutination) {
        this.snakeBiteLatexAgglutination = snakeBiteLatexAgglutination;
    }

    public void setSnakeBiteCqValueDetection(String snakeBiteCqValueDetection) {
        this.snakeBiteCqValueDetection = snakeBiteCqValueDetection;
    }

    public void setSnakeBiteSeQuencing(String snakeBiteSeQuencing) {
        this.snakeBiteSeQuencing = snakeBiteSeQuencing;
    }

    public void setSnakeBiteDnaMicroArray(String snakeBiteDnaMicroArray) {
        this.snakeBiteDnaMicroArray = snakeBiteDnaMicroArray;
    }

    public void setSnakeBiteOther(String snakeBiteOther) {
        this.snakeBiteOther = snakeBiteOther;
    }

    public void setRubellaAntiBodyDetection(String rubellaAntiBodyDetection) {
        this.rubellaAntiBodyDetection = rubellaAntiBodyDetection;
    }

    public void setRubellaAntigenDetection(String rubellaAntigenDetection) {
        this.rubellaAntigenDetection = rubellaAntigenDetection;
    }

    public void setRubellaRapidTest(String rubellaRapidTest) {
        this.rubellaRapidTest = rubellaRapidTest;
    }

    public void setRubellaCulture(String rubellaCulture) {
        this.rubellaCulture = rubellaCulture;
    }

    public void setRubellaHistopathology(String rubellaHistopathology) {
        this.rubellaHistopathology = rubellaHistopathology;
    }

    public void setRubellaIsolation(String rubellaIsolation) {
        this.rubellaIsolation = rubellaIsolation;
    }

    public void setRubellaIgmSerumAntibody(String rubellaIgmSerumAntibody) {
        this.rubellaIgmSerumAntibody = rubellaIgmSerumAntibody;
    }

    public void setRubellaIggSerumAntibody(String rubellaIggSerumAntibody) {
        this.rubellaIggSerumAntibody = rubellaIggSerumAntibody;
    }

    public void setRubellaIgaSerumAntibody(String rubellaIgaSerumAntibody) {
        this.rubellaIgaSerumAntibody = rubellaIgaSerumAntibody;
    }

    public void setRubellaIncubationTime(String rubellaIncubationTime) {
        this.rubellaIncubationTime = rubellaIncubationTime;
    }

    public void setRubellaIndirectFluorescentAntibody(String rubellaIndirectFluorescentAntibody) {
        this.rubellaIndirectFluorescentAntibody = rubellaIndirectFluorescentAntibody;
    }

    public void setRubellaMicroscopy(String rubellaMicroscopy) {
        this.rubellaMicroscopy = rubellaMicroscopy;
    }

    public void setRubellaNeutralizingAntibodies(String rubellaNeutralizingAntibodies) {
        this.rubellaNeutralizingAntibodies = rubellaNeutralizingAntibodies;
    }

    public void setRubellaPcr(String rubellaPcr) {
        this.rubellaPcr = rubellaPcr;
    }

    public void setRubellaGramStain(String rubellaGramStain) {
        this.rubellaGramStain = rubellaGramStain;
    }

    public void setRubellaLatexAgglutination(String rubellaLatexAgglutination) {
        this.rubellaLatexAgglutination = rubellaLatexAgglutination;
    }

    public void setRubellaCqValueDetection(String rubellaCqValueDetection) {
        this.rubellaCqValueDetection = rubellaCqValueDetection;
    }

    public void setRubellaSeQuencing(String rubellaSeQuencing) {
        this.rubellaSeQuencing = rubellaSeQuencing;
    }

    public void setRubellaDnaMicroArray(String rubellaDnaMicroArray) {
        this.rubellaDnaMicroArray = rubellaDnaMicroArray;
    }

    public void setRubellaOther(String rubellaOther) {
        this.rubellaOther = rubellaOther;
    }

    public void setTuberculosisAntiBodyDetection(String tuberculosisAntiBodyDetection) {
        this.tuberculosisAntiBodyDetection = tuberculosisAntiBodyDetection;
    }

    public void setTuberculosisAntigenDetection(String tuberculosisAntigenDetection) {
        this.tuberculosisAntigenDetection = tuberculosisAntigenDetection;
    }

    public void setTuberculosisRapidTest(String tuberculosisRapidTest) {
        this.tuberculosisRapidTest = tuberculosisRapidTest;
    }

    public void setTuberculosisCulture(String tuberculosisCulture) {
        this.tuberculosisCulture = tuberculosisCulture;
    }

    public void setTuberculosisHistopathology(String tuberculosisHistopathology) {
        this.tuberculosisHistopathology = tuberculosisHistopathology;
    }

    public void setTuberculosisIsolation(String tuberculosisIsolation) {
        this.tuberculosisIsolation = tuberculosisIsolation;
    }

    public void setTuberculosisIgmSerumAntibody(String tuberculosisIgmSerumAntibody) {
        this.tuberculosisIgmSerumAntibody = tuberculosisIgmSerumAntibody;
    }

    public void setTuberculosisIggSerumAntibody(String tuberculosisIggSerumAntibody) {
        this.tuberculosisIggSerumAntibody = tuberculosisIggSerumAntibody;
    }

    public void setTuberculosisIgaSerumAntibody(String tuberculosisIgaSerumAntibody) {
        this.tuberculosisIgaSerumAntibody = tuberculosisIgaSerumAntibody;
    }

    public void setTuberculosisIncubationTime(String tuberculosisIncubationTime) {
        this.tuberculosisIncubationTime = tuberculosisIncubationTime;
    }

    public void setTuberculosisIndirectFluorescentAntibody(String tuberculosisIndirectFluorescentAntibody) {
        this.tuberculosisIndirectFluorescentAntibody = tuberculosisIndirectFluorescentAntibody;
    }

    public void setTuberculosisMicroscopy(String tuberculosisMicroscopy) {
        this.tuberculosisMicroscopy = tuberculosisMicroscopy;
    }

    public void setTuberculosisNeutralizingAntibodies(String tuberculosisNeutralizingAntibodies) {
        this.tuberculosisNeutralizingAntibodies = tuberculosisNeutralizingAntibodies;
    }

    public void setTuberculosisPcr(String tuberculosisPcr) {
        this.tuberculosisPcr = tuberculosisPcr;
    }

    public void setTuberculosisGramStain(String tuberculosisGramStain) {
        this.tuberculosisGramStain = tuberculosisGramStain;
    }

    public void setTuberculosisLatexAgglutination(String tuberculosisLatexAgglutination) {
        this.tuberculosisLatexAgglutination = tuberculosisLatexAgglutination;
    }

    public void setTuberculosisCqValueDetection(String tuberculosisCqValueDetection) {
        this.tuberculosisCqValueDetection = tuberculosisCqValueDetection;
    }

    public void setTuberculosisSeQuencing(String tuberculosisSeQuencing) {
        this.tuberculosisSeQuencing = tuberculosisSeQuencing;
    }

    public void setTuberculosisDnaMicroArray(String tuberculosisDnaMicroArray) {
        this.tuberculosisDnaMicroArray = tuberculosisDnaMicroArray;
    }

    public void setTuberculosisOther(String tuberculosisOther) {
        this.tuberculosisOther = tuberculosisOther;
    }

    public void setLeprosyAntiBodyDetection(String leprosyAntiBodyDetection) {
        this.leprosyAntiBodyDetection = leprosyAntiBodyDetection;
    }

    public void setLeprosyAntigenDetection(String leprosyAntigenDetection) {
        this.leprosyAntigenDetection = leprosyAntigenDetection;
    }

    public void setLeprosyRapidTest(String leprosyRapidTest) {
        this.leprosyRapidTest = leprosyRapidTest;
    }

    public void setLeprosyCulture(String leprosyCulture) {
        this.leprosyCulture = leprosyCulture;
    }

    public void setLeprosyHistopathology(String leprosyHistopathology) {
        this.leprosyHistopathology = leprosyHistopathology;
    }

    public void setLeprosyIsolation(String leprosyIsolation) {
        this.leprosyIsolation = leprosyIsolation;
    }

    public void setLeprosyIgmSerumAntibody(String leprosyIgmSerumAntibody) {
        this.leprosyIgmSerumAntibody = leprosyIgmSerumAntibody;
    }

    public void setLeprosyIggSerumAntibody(String leprosyIggSerumAntibody) {
        this.leprosyIggSerumAntibody = leprosyIggSerumAntibody;
    }

    public void setLeprosyIgaSerumAntibody(String leprosyIgaSerumAntibody) {
        this.leprosyIgaSerumAntibody = leprosyIgaSerumAntibody;
    }

    public void setLeprosyIncubationTime(String leprosyIncubationTime) {
        this.leprosyIncubationTime = leprosyIncubationTime;
    }

    public void setLeprosyIndirectFluorescentAntibody(String leprosyIndirectFluorescentAntibody) {
        this.leprosyIndirectFluorescentAntibody = leprosyIndirectFluorescentAntibody;
    }

    public void setLeprosyMicroscopy(String leprosyMicroscopy) {
        this.leprosyMicroscopy = leprosyMicroscopy;
    }

    public void setLeprosyNeutralizingAntibodies(String leprosyNeutralizingAntibodies) {
        this.leprosyNeutralizingAntibodies = leprosyNeutralizingAntibodies;
    }

    public void setLeprosyPcr(String leprosyPcr) {
        this.leprosyPcr = leprosyPcr;
    }

    public void setLeprosyGramStain(String leprosyGramStain) {
        this.leprosyGramStain = leprosyGramStain;
    }

    public void setLeprosyLatexAgglutination(String leprosyLatexAgglutination) {
        this.leprosyLatexAgglutination = leprosyLatexAgglutination;
    }

    public void setLeprosyCqValueDetection(String leprosyCqValueDetection) {
        this.leprosyCqValueDetection = leprosyCqValueDetection;
    }

    public void setLeprosySeQuencing(String leprosySeQuencing) {
        this.leprosySeQuencing = leprosySeQuencing;
    }

    public void setLeprosyDnaMicroArray(String leprosyDnaMicroArray) {
        this.leprosyDnaMicroArray = leprosyDnaMicroArray;
    }

    public void setLeprosyOther(String leprosyOther) {
        this.leprosyOther = leprosyOther;
    }

    public void setLymphaticFilariasisAntiBodyDetection(String lymphaticFilariasisAntiBodyDetection) {
        this.lymphaticFilariasisAntiBodyDetection = lymphaticFilariasisAntiBodyDetection;
    }

    public void setLymphaticFilariasisAntigenDetection(String lymphaticFilariasisAntigenDetection) {
        this.lymphaticFilariasisAntigenDetection = lymphaticFilariasisAntigenDetection;
    }

    public void setLymphaticFilariasisRapidTest(String lymphaticFilariasisRapidTest) {
        this.lymphaticFilariasisRapidTest = lymphaticFilariasisRapidTest;
    }

    public void setLymphaticFilariasisCulture(String lymphaticFilariasisCulture) {
        this.lymphaticFilariasisCulture = lymphaticFilariasisCulture;
    }

    public void setLymphaticFilariasisHistopathology(String lymphaticFilariasisHistopathology) {
        this.lymphaticFilariasisHistopathology = lymphaticFilariasisHistopathology;
    }

    public void setLymphaticFilariasisIsolation(String lymphaticFilariasisIsolation) {
        this.lymphaticFilariasisIsolation = lymphaticFilariasisIsolation;
    }

    public void setLymphaticFilariasisIgmSerumAntibody(String lymphaticFilariasisIgmSerumAntibody) {
        this.lymphaticFilariasisIgmSerumAntibody = lymphaticFilariasisIgmSerumAntibody;
    }

    public void setLymphaticFilariasisIggSerumAntibody(String lymphaticFilariasisIggSerumAntibody) {
        this.lymphaticFilariasisIggSerumAntibody = lymphaticFilariasisIggSerumAntibody;
    }

    public void setLymphaticFilariasisIgaSerumAntibody(String lymphaticFilariasisIgaSerumAntibody) {
        this.lymphaticFilariasisIgaSerumAntibody = lymphaticFilariasisIgaSerumAntibody;
    }

    public void setLymphaticFilariasisIncubationTime(String lymphaticFilariasisIncubationTime) {
        this.lymphaticFilariasisIncubationTime = lymphaticFilariasisIncubationTime;
    }

    public void setLymphaticFilariasisIndirectFluorescentAntibody(String lymphaticFilariasisIndirectFluorescentAntibody) {
        this.lymphaticFilariasisIndirectFluorescentAntibody = lymphaticFilariasisIndirectFluorescentAntibody;
    }

    public void setLymphaticFilariasisMicroscopy(String lymphaticFilariasisMicroscopy) {
        this.lymphaticFilariasisMicroscopy = lymphaticFilariasisMicroscopy;
    }

    public void setLymphaticFilariasisNeutralizingAntibodies(String lymphaticFilariasisNeutralizingAntibodies) {
        this.lymphaticFilariasisNeutralizingAntibodies = lymphaticFilariasisNeutralizingAntibodies;
    }

    public void setLymphaticFilariasisPcr(String lymphaticFilariasisPcr) {
        this.lymphaticFilariasisPcr = lymphaticFilariasisPcr;
    }

    public void setLymphaticFilariasisGramStain(String lymphaticFilariasisGramStain) {
        this.lymphaticFilariasisGramStain = lymphaticFilariasisGramStain;
    }

    public void setLymphaticFilariasisLatexAgglutination(String lymphaticFilariasisLatexAgglutination) {
        this.lymphaticFilariasisLatexAgglutination = lymphaticFilariasisLatexAgglutination;
    }

    public void setLymphaticFilariasisCqValueDetection(String lymphaticFilariasisCqValueDetection) {
        this.lymphaticFilariasisCqValueDetection = lymphaticFilariasisCqValueDetection;
    }

    public void setLymphaticFilariasisSeQuencing(String lymphaticFilariasisSeQuencing) {
        this.lymphaticFilariasisSeQuencing = lymphaticFilariasisSeQuencing;
    }

    public void setLymphaticFilariasisDnaMicroArray(String lymphaticFilariasisDnaMicroArray) {
        this.lymphaticFilariasisDnaMicroArray = lymphaticFilariasisDnaMicroArray;
    }

    public void setLymphaticFilariasisOther(String lymphaticFilariasisOther) {
        this.lymphaticFilariasisOther = lymphaticFilariasisOther;
    }

    public void setBuruliUlcerAntiBodyDetection(String buruliUlcerAntiBodyDetection) {
        this.buruliUlcerAntiBodyDetection = buruliUlcerAntiBodyDetection;
    }

    public void setBuruliUlcerAntigenDetection(String buruliUlcerAntigenDetection) {
        this.buruliUlcerAntigenDetection = buruliUlcerAntigenDetection;
    }

    public void setBuruliUlcerRapidTest(String buruliUlcerRapidTest) {
        this.buruliUlcerRapidTest = buruliUlcerRapidTest;
    }

    public void setBuruliUlcerCulture(String buruliUlcerCulture) {
        this.buruliUlcerCulture = buruliUlcerCulture;
    }

    public void setBuruliUlcerHistopathology(String buruliUlcerHistopathology) {
        this.buruliUlcerHistopathology = buruliUlcerHistopathology;
    }

    public void setBuruliUlcerIsolation(String buruliUlcerIsolation) {
        this.buruliUlcerIsolation = buruliUlcerIsolation;
    }

    public void setBuruliUlcerIgmSerumAntibody(String buruliUlcerIgmSerumAntibody) {
        this.buruliUlcerIgmSerumAntibody = buruliUlcerIgmSerumAntibody;
    }

    public void setBuruliUlcerIggSerumAntibody(String buruliUlcerIggSerumAntibody) {
        this.buruliUlcerIggSerumAntibody = buruliUlcerIggSerumAntibody;
    }

    public void setBuruliUlcerIgaSerumAntibody(String buruliUlcerIgaSerumAntibody) {
        this.buruliUlcerIgaSerumAntibody = buruliUlcerIgaSerumAntibody;
    }

    public void setBuruliUlcerIncubationTime(String buruliUlcerIncubationTime) {
        this.buruliUlcerIncubationTime = buruliUlcerIncubationTime;
    }

    public void setBuruliUlcerIndirectFluorescentAntibody(String buruliUlcerIndirectFluorescentAntibody) {
        this.buruliUlcerIndirectFluorescentAntibody = buruliUlcerIndirectFluorescentAntibody;
    }

    public void setBuruliUlcerMicroscopy(String buruliUlcerMicroscopy) {
        this.buruliUlcerMicroscopy = buruliUlcerMicroscopy;
    }

    public void setBuruliUlcerNeutralizingAntibodies(String buruliUlcerNeutralizingAntibodies) {
        this.buruliUlcerNeutralizingAntibodies = buruliUlcerNeutralizingAntibodies;
    }

    public void setBuruliUlcerPcr(String buruliUlcerPcr) {
        this.buruliUlcerPcr = buruliUlcerPcr;
    }

    public void setBuruliUlcerGramStain(String buruliUlcerGramStain) {
        this.buruliUlcerGramStain = buruliUlcerGramStain;
    }

    public void setBuruliUlcerLatexAgglutination(String buruliUlcerLatexAgglutination) {
        this.buruliUlcerLatexAgglutination = buruliUlcerLatexAgglutination;
    }

    public void setBuruliUlcerCqValueDetection(String buruliUlcerCqValueDetection) {
        this.buruliUlcerCqValueDetection = buruliUlcerCqValueDetection;
    }

    public void setBuruliUlcerSeQuencing(String buruliUlcerSeQuencing) {
        this.buruliUlcerSeQuencing = buruliUlcerSeQuencing;
    }

    public void setBuruliUlcerDnaMicroArray(String buruliUlcerDnaMicroArray) {
        this.buruliUlcerDnaMicroArray = buruliUlcerDnaMicroArray;
    }

    public void setBuruliUlcerOther(String buruliUlcerOther) {
        this.buruliUlcerOther = buruliUlcerOther;
    }

    public void setPertussisAntiBodyDetection(String pertussisAntiBodyDetection) {
        this.pertussisAntiBodyDetection = pertussisAntiBodyDetection;
    }

    public void setPertussisAntigenDetection(String pertussisAntigenDetection) {
        this.pertussisAntigenDetection = pertussisAntigenDetection;
    }

    public void setPertussisRapidTest(String pertussisRapidTest) {
        this.pertussisRapidTest = pertussisRapidTest;
    }

    public void setPertussisCulture(String pertussisCulture) {
        this.pertussisCulture = pertussisCulture;
    }

    public void setPertussisHistopathology(String pertussisHistopathology) {
        this.pertussisHistopathology = pertussisHistopathology;
    }

    public void setPertussisIsolation(String pertussisIsolation) {
        this.pertussisIsolation = pertussisIsolation;
    }

    public void setPertussisIgmSerumAntibody(String pertussisIgmSerumAntibody) {
        this.pertussisIgmSerumAntibody = pertussisIgmSerumAntibody;
    }

    public void setPertussisIggSerumAntibody(String pertussisIggSerumAntibody) {
        this.pertussisIggSerumAntibody = pertussisIggSerumAntibody;
    }

    public void setPertussisIgaSerumAntibody(String pertussisIgaSerumAntibody) {
        this.pertussisIgaSerumAntibody = pertussisIgaSerumAntibody;
    }

    public void setPertussisIncubationTime(String pertussisIncubationTime) {
        this.pertussisIncubationTime = pertussisIncubationTime;
    }

    public void setPertussisIndirectFluorescentAntibody(String pertussisIndirectFluorescentAntibody) {
        this.pertussisIndirectFluorescentAntibody = pertussisIndirectFluorescentAntibody;
    }

    public void setPertussisMicroscopy(String pertussisMicroscopy) {
        this.pertussisMicroscopy = pertussisMicroscopy;
    }

    public void setPertussisNeutralizingAntibodies(String pertussisNeutralizingAntibodies) {
        this.pertussisNeutralizingAntibodies = pertussisNeutralizingAntibodies;
    }

    public void setPertussisPcr(String pertussisPcr) {
        this.pertussisPcr = pertussisPcr;
    }

    public void setPertussisGramStain(String pertussisGramStain) {
        this.pertussisGramStain = pertussisGramStain;
    }

    public void setPertussisLatexAgglutination(String pertussisLatexAgglutination) {
        this.pertussisLatexAgglutination = pertussisLatexAgglutination;
    }

    public void setPertussisCqValueDetection(String pertussisCqValueDetection) {
        this.pertussisCqValueDetection = pertussisCqValueDetection;
    }

    public void setPertussisSeQuencing(String pertussisSeQuencing) {
        this.pertussisSeQuencing = pertussisSeQuencing;
    }

    public void setPertussisDnaMicroArray(String pertussisDnaMicroArray) {
        this.pertussisDnaMicroArray = pertussisDnaMicroArray;
    }

    public void setPertussisOther(String pertussisOther) {
        this.pertussisOther = pertussisOther;
    }

    public void setNeonatalAntiBodyDetection(String neonatalAntiBodyDetection) {
        this.neonatalAntiBodyDetection = neonatalAntiBodyDetection;
    }

    public void setNeonatalAntigenDetection(String neonatalAntigenDetection) {
        this.neonatalAntigenDetection = neonatalAntigenDetection;
    }

    public void setNeonatalRapidTest(String neonatalRapidTest) {
        this.neonatalRapidTest = neonatalRapidTest;
    }

    public void setNeonatalCulture(String neonatalCulture) {
        this.neonatalCulture = neonatalCulture;
    }

    public void setNeonatalHistopathology(String neonatalHistopathology) {
        this.neonatalHistopathology = neonatalHistopathology;
    }

    public void setNeonatalIsolation(String neonatalIsolation) {
        this.neonatalIsolation = neonatalIsolation;
    }

    public void setNeonatalIgmSerumAntibody(String neonatalIgmSerumAntibody) {
        this.neonatalIgmSerumAntibody = neonatalIgmSerumAntibody;
    }

    public void setNeonatalIggSerumAntibody(String neonatalIggSerumAntibody) {
        this.neonatalIggSerumAntibody = neonatalIggSerumAntibody;
    }

    public void setNeonatalIgaSerumAntibody(String neonatalIgaSerumAntibody) {
        this.neonatalIgaSerumAntibody = neonatalIgaSerumAntibody;
    }

    public void setNeonatalIncubationTime(String neonatalIncubationTime) {
        this.neonatalIncubationTime = neonatalIncubationTime;
    }

    public void setNeonatalIndirectFluorescentAntibody(String neonatalIndirectFluorescentAntibody) {
        this.neonatalIndirectFluorescentAntibody = neonatalIndirectFluorescentAntibody;
    }

    public void setNeonatalMicroscopy(String neonatalMicroscopy) {
        this.neonatalMicroscopy = neonatalMicroscopy;
    }

    public void setNeonatalNeutralizingAntibodies(String neonatalNeutralizingAntibodies) {
        this.neonatalNeutralizingAntibodies = neonatalNeutralizingAntibodies;
    }

    public void setNeonatalPcr(String neonatalPcr) {
        this.neonatalPcr = neonatalPcr;
    }

    public void setNeonatalGramStain(String neonatalGramStain) {
        this.neonatalGramStain = neonatalGramStain;
    }

    public void setNeonatalLatexAgglutination(String neonatalLatexAgglutination) {
        this.neonatalLatexAgglutination = neonatalLatexAgglutination;
    }

    public void setNeonatalCqValueDetection(String neonatalCqValueDetection) {
        this.neonatalCqValueDetection = neonatalCqValueDetection;
    }

    public void setNeonatalSeQuencing(String neonatalSeQuencing) {
        this.neonatalSeQuencing = neonatalSeQuencing;
    }

    public void setNeonatalDnaMicroArray(String neonatalDnaMicroArray) {
        this.neonatalDnaMicroArray = neonatalDnaMicroArray;
    }

    public void setNeonatalOther(String neonatalOther) {
        this.neonatalOther = neonatalOther;
    }

    public void setOnchocerciasisAntiBodyDetection(String onchocerciasisAntiBodyDetection) {
        this.onchocerciasisAntiBodyDetection = onchocerciasisAntiBodyDetection;
    }

    public void setOnchocerciasisAntigenDetection(String onchocerciasisAntigenDetection) {
        this.onchocerciasisAntigenDetection = onchocerciasisAntigenDetection;
    }

    public void setOnchocerciasisRapidTest(String onchocerciasisRapidTest) {
        this.onchocerciasisRapidTest = onchocerciasisRapidTest;
    }

    public void setOnchocerciasisCulture(String onchocerciasisCulture) {
        this.onchocerciasisCulture = onchocerciasisCulture;
    }

    public void setOnchocerciasisHistopathology(String onchocerciasisHistopathology) {
        this.onchocerciasisHistopathology = onchocerciasisHistopathology;
    }

    public void setOnchocerciasisIsolation(String onchocerciasisIsolation) {
        this.onchocerciasisIsolation = onchocerciasisIsolation;
    }

    public void setOnchocerciasisIgmSerumAntibody(String onchocerciasisIgmSerumAntibody) {
        this.onchocerciasisIgmSerumAntibody = onchocerciasisIgmSerumAntibody;
    }

    public void setOnchocerciasisIggSerumAntibody(String onchocerciasisIggSerumAntibody) {
        this.onchocerciasisIggSerumAntibody = onchocerciasisIggSerumAntibody;
    }

    public void setOnchocerciasisIgaSerumAntibody(String onchocerciasisIgaSerumAntibody) {
        this.onchocerciasisIgaSerumAntibody = onchocerciasisIgaSerumAntibody;
    }

    public void setOnchocerciasisIncubationTime(String onchocerciasisIncubationTime) {
        this.onchocerciasisIncubationTime = onchocerciasisIncubationTime;
    }

    public void setOnchocerciasisIndirectFluorescentAntibody(String onchocerciasisIndirectFluorescentAntibody) {
        this.onchocerciasisIndirectFluorescentAntibody = onchocerciasisIndirectFluorescentAntibody;
    }

    public void setOnchocerciasisMicroscopy(String onchocerciasisMicroscopy) {
        this.onchocerciasisMicroscopy = onchocerciasisMicroscopy;
    }

    public void setOnchocerciasisNeutralizingAntibodies(String onchocerciasisNeutralizingAntibodies) {
        this.onchocerciasisNeutralizingAntibodies = onchocerciasisNeutralizingAntibodies;
    }

    public void setOnchocerciasisPcr(String onchocerciasisPcr) {
        this.onchocerciasisPcr = onchocerciasisPcr;
    }

    public void setOnchocerciasisGramStain(String onchocerciasisGramStain) {
        this.onchocerciasisGramStain = onchocerciasisGramStain;
    }

    public void setOnchocerciasisLatexAgglutination(String onchocerciasisLatexAgglutination) {
        this.onchocerciasisLatexAgglutination = onchocerciasisLatexAgglutination;
    }

    public void setOnchocerciasisCqValueDetection(String onchocerciasisCqValueDetection) {
        this.onchocerciasisCqValueDetection = onchocerciasisCqValueDetection;
    }

    public void setOnchocerciasisSeQuencing(String onchocerciasisSeQuencing) {
        this.onchocerciasisSeQuencing = onchocerciasisSeQuencing;
    }

    public void setOnchocerciasisDnaMicroArray(String onchocerciasisDnaMicroArray) {
        this.onchocerciasisDnaMicroArray = onchocerciasisDnaMicroArray;
    }

    public void setOnchocerciasisOther(String onchocerciasisOther) {
        this.onchocerciasisOther = onchocerciasisOther;
    }

    public void setDiphtheriaAntiBodyDetection(String diphtheriaAntiBodyDetection) {
        this.diphtheriaAntiBodyDetection = diphtheriaAntiBodyDetection;
    }

    public void setDiphtheriaAntigenDetection(String diphtheriaAntigenDetection) {
        this.diphtheriaAntigenDetection = diphtheriaAntigenDetection;
    }

    public void setDiphtheriaRapidTest(String diphtheriaRapidTest) {
        this.diphtheriaRapidTest = diphtheriaRapidTest;
    }

    public void setDiphtheriaCulture(String diphtheriaCulture) {
        this.diphtheriaCulture = diphtheriaCulture;
    }

    public void setDiphtheriaHistopathology(String diphtheriaHistopathology) {
        this.diphtheriaHistopathology = diphtheriaHistopathology;
    }

    public void setDiphtheriaIsolation(String diphtheriaIsolation) {
        this.diphtheriaIsolation = diphtheriaIsolation;
    }

    public void setDiphtheriaIgmSerumAntibody(String diphtheriaIgmSerumAntibody) {
        this.diphtheriaIgmSerumAntibody = diphtheriaIgmSerumAntibody;
    }

    public void setDiphtheriaIggSerumAntibody(String diphtheriaIggSerumAntibody) {
        this.diphtheriaIggSerumAntibody = diphtheriaIggSerumAntibody;
    }

    public void setDiphtheriaIgaSerumAntibody(String diphtheriaIgaSerumAntibody) {
        this.diphtheriaIgaSerumAntibody = diphtheriaIgaSerumAntibody;
    }

    public void setDiphtheriaIncubationTime(String diphtheriaIncubationTime) {
        this.diphtheriaIncubationTime = diphtheriaIncubationTime;
    }

    public void setDiphtheriaIndirectFluorescentAntibody(String diphtheriaIndirectFluorescentAntibody) {
        this.diphtheriaIndirectFluorescentAntibody = diphtheriaIndirectFluorescentAntibody;
    }

    public void setDiphtheriaMicroscopy(String diphtheriaMicroscopy) {
        this.diphtheriaMicroscopy = diphtheriaMicroscopy;
    }

    public void setDiphtheriaNeutralizingAntibodies(String diphtheriaNeutralizingAntibodies) {
        this.diphtheriaNeutralizingAntibodies = diphtheriaNeutralizingAntibodies;
    }

    public void setDiphtheriaPcr(String diphtheriaPcr) {
        this.diphtheriaPcr = diphtheriaPcr;
    }

    public void setDiphtheriaGramStain(String diphtheriaGramStain) {
        this.diphtheriaGramStain = diphtheriaGramStain;
    }

    public void setDiphtheriaLatexAgglutination(String diphtheriaLatexAgglutination) {
        this.diphtheriaLatexAgglutination = diphtheriaLatexAgglutination;
    }

    public void setDiphtheriaCqValueDetection(String diphtheriaCqValueDetection) {
        this.diphtheriaCqValueDetection = diphtheriaCqValueDetection;
    }

    public void setDiphtheriaSeQuencing(String diphtheriaSeQuencing) {
        this.diphtheriaSeQuencing = diphtheriaSeQuencing;
    }

    public void setDiphtheriaDnaMicroArray(String diphtheriaDnaMicroArray) {
        this.diphtheriaDnaMicroArray = diphtheriaDnaMicroArray;
    }

    public void setDiphtheriaOther(String diphtheriaOther) {
        this.diphtheriaOther = diphtheriaOther;
    }

    public void setTrachomaAntiBodyDetection(String trachomaAntiBodyDetection) {
        this.trachomaAntiBodyDetection = trachomaAntiBodyDetection;
    }

    public void setTrachomaAntigenDetection(String trachomaAntigenDetection) {
        this.trachomaAntigenDetection = trachomaAntigenDetection;
    }

    public void setTrachomaRapidTest(String trachomaRapidTest) {
        this.trachomaRapidTest = trachomaRapidTest;
    }

    public void setTrachomaCulture(String trachomaCulture) {
        this.trachomaCulture = trachomaCulture;
    }

    public void setTrachomaHistopathology(String trachomaHistopathology) {
        this.trachomaHistopathology = trachomaHistopathology;
    }

    public void setTrachomaIsolation(String trachomaIsolation) {
        this.trachomaIsolation = trachomaIsolation;
    }

    public void setTrachomaIgmSerumAntibody(String trachomaIgmSerumAntibody) {
        this.trachomaIgmSerumAntibody = trachomaIgmSerumAntibody;
    }

    public void setTrachomaIggSerumAntibody(String trachomaIggSerumAntibody) {
        this.trachomaIggSerumAntibody = trachomaIggSerumAntibody;
    }

    public void setTrachomaIgaSerumAntibody(String trachomaIgaSerumAntibody) {
        this.trachomaIgaSerumAntibody = trachomaIgaSerumAntibody;
    }

    public void setTrachomaIncubationTime(String trachomaIncubationTime) {
        this.trachomaIncubationTime = trachomaIncubationTime;
    }

    public void setTrachomaIndirectFluorescentAntibody(String trachomaIndirectFluorescentAntibody) {
        this.trachomaIndirectFluorescentAntibody = trachomaIndirectFluorescentAntibody;
    }

    public void setTrachomaMicroscopy(String trachomaMicroscopy) {
        this.trachomaMicroscopy = trachomaMicroscopy;
    }

    public void setTrachomaNeutralizingAntibodies(String trachomaNeutralizingAntibodies) {
        this.trachomaNeutralizingAntibodies = trachomaNeutralizingAntibodies;
    }

    public void setTrachomaPcr(String trachomaPcr) {
        this.trachomaPcr = trachomaPcr;
    }

    public void setTrachomaGramStain(String trachomaGramStain) {
        this.trachomaGramStain = trachomaGramStain;
    }

    public void setTrachomaLatexAgglutination(String trachomaLatexAgglutination) {
        this.trachomaLatexAgglutination = trachomaLatexAgglutination;
    }

    public void setTrachomaCqValueDetection(String trachomaCqValueDetection) {
        this.trachomaCqValueDetection = trachomaCqValueDetection;
    }

    public void setTrachomaSeQuencing(String trachomaSeQuencing) {
        this.trachomaSeQuencing = trachomaSeQuencing;
    }

    public void setTrachomaDnaMicroArray(String trachomaDnaMicroArray) {
        this.trachomaDnaMicroArray = trachomaDnaMicroArray;
    }

    public void setTrachomaOther(String trachomaOther) {
        this.trachomaOther = trachomaOther;
    }

    public void setYawsEndemicSyphilisAntiBodyDetection(String yawsEndemicSyphilisAntiBodyDetection) {
        this.yawsEndemicSyphilisAntiBodyDetection = yawsEndemicSyphilisAntiBodyDetection;
    }

    public void setYawsEndemicSyphilisAntigenDetection(String yawsEndemicSyphilisAntigenDetection) {
        this.yawsEndemicSyphilisAntigenDetection = yawsEndemicSyphilisAntigenDetection;
    }

    public void setYawsEndemicSyphilisRapidTest(String yawsEndemicSyphilisRapidTest) {
        this.yawsEndemicSyphilisRapidTest = yawsEndemicSyphilisRapidTest;
    }

    public void setYawsEndemicSyphilisCulture(String yawsEndemicSyphilisCulture) {
        this.yawsEndemicSyphilisCulture = yawsEndemicSyphilisCulture;
    }

    public void setYawsEndemicSyphilisHistopathology(String yawsEndemicSyphilisHistopathology) {
        this.yawsEndemicSyphilisHistopathology = yawsEndemicSyphilisHistopathology;
    }

    public void setYawsEndemicSyphilisIsolation(String yawsEndemicSyphilisIsolation) {
        this.yawsEndemicSyphilisIsolation = yawsEndemicSyphilisIsolation;
    }

    public void setYawsEndemicSyphilisIgmSerumAntibody(String yawsEndemicSyphilisIgmSerumAntibody) {
        this.yawsEndemicSyphilisIgmSerumAntibody = yawsEndemicSyphilisIgmSerumAntibody;
    }

    public void setYawsEndemicSyphilisIggSerumAntibody(String yawsEndemicSyphilisIggSerumAntibody) {
        this.yawsEndemicSyphilisIggSerumAntibody = yawsEndemicSyphilisIggSerumAntibody;
    }

    public void setYawsEndemicSyphilisIgaSerumAntibody(String yawsEndemicSyphilisIgaSerumAntibody) {
        this.yawsEndemicSyphilisIgaSerumAntibody = yawsEndemicSyphilisIgaSerumAntibody;
    }

    public void setYawsEndemicSyphilisIncubationTime(String yawsEndemicSyphilisIncubationTime) {
        this.yawsEndemicSyphilisIncubationTime = yawsEndemicSyphilisIncubationTime;
    }

    public void setYawsEndemicSyphilisIndirectFluorescentAntibody(String yawsEndemicSyphilisIndirectFluorescentAntibody) {
        this.yawsEndemicSyphilisIndirectFluorescentAntibody = yawsEndemicSyphilisIndirectFluorescentAntibody;
    }

    public void setYawsEndemicSyphilisMicroscopy(String yawsEndemicSyphilisMicroscopy) {
        this.yawsEndemicSyphilisMicroscopy = yawsEndemicSyphilisMicroscopy;
    }

    public void setYawsEndemicSyphilisNeutralizingAntibodies(String yawsEndemicSyphilisNeutralizingAntibodies) {
        this.yawsEndemicSyphilisNeutralizingAntibodies = yawsEndemicSyphilisNeutralizingAntibodies;
    }

    public void setYawsEndemicSyphilisPcr(String yawsEndemicSyphilisPcr) {
        this.yawsEndemicSyphilisPcr = yawsEndemicSyphilisPcr;
    }

    public void setYawsEndemicSyphilisGramStain(String yawsEndemicSyphilisGramStain) {
        this.yawsEndemicSyphilisGramStain = yawsEndemicSyphilisGramStain;
    }

    public void setYawsEndemicSyphilisLatexAgglutination(String yawsEndemicSyphilisLatexAgglutination) {
        this.yawsEndemicSyphilisLatexAgglutination = yawsEndemicSyphilisLatexAgglutination;
    }

    public void setYawsEndemicSyphilisCqValueDetection(String yawsEndemicSyphilisCqValueDetection) {
        this.yawsEndemicSyphilisCqValueDetection = yawsEndemicSyphilisCqValueDetection;
    }

    public void setYawsEndemicSyphilisSeQuencing(String yawsEndemicSyphilisSeQuencing) {
        this.yawsEndemicSyphilisSeQuencing = yawsEndemicSyphilisSeQuencing;
    }

    public void setYawsEndemicSyphilisDnaMicroArray(String yawsEndemicSyphilisDnaMicroArray) {
        this.yawsEndemicSyphilisDnaMicroArray = yawsEndemicSyphilisDnaMicroArray;
    }

    public void setYawsEndemicSyphilisOther(String yawsEndemicSyphilisOther) {
        this.yawsEndemicSyphilisOther = yawsEndemicSyphilisOther;
    }

    public void setMaternalDeathsAntiBodyDetection(String maternalDeathsAntiBodyDetection) {
        this.maternalDeathsAntiBodyDetection = maternalDeathsAntiBodyDetection;
    }

    public void setMaternalDeathsAntigenDetection(String maternalDeathsAntigenDetection) {
        this.maternalDeathsAntigenDetection = maternalDeathsAntigenDetection;
    }

    public void setMaternalDeathsRapidTest(String maternalDeathsRapidTest) {
        this.maternalDeathsRapidTest = maternalDeathsRapidTest;
    }

    public void setMaternalDeathsCulture(String maternalDeathsCulture) {
        this.maternalDeathsCulture = maternalDeathsCulture;
    }

    public void setMaternalDeathsHistopathology(String maternalDeathsHistopathology) {
        this.maternalDeathsHistopathology = maternalDeathsHistopathology;
    }

    public void setMaternalDeathsIsolation(String maternalDeathsIsolation) {
        this.maternalDeathsIsolation = maternalDeathsIsolation;
    }

    public void setMaternalDeathsIgmSerumAntibody(String maternalDeathsIgmSerumAntibody) {
        this.maternalDeathsIgmSerumAntibody = maternalDeathsIgmSerumAntibody;
    }

    public void setMaternalDeathsIggSerumAntibody(String maternalDeathsIggSerumAntibody) {
        this.maternalDeathsIggSerumAntibody = maternalDeathsIggSerumAntibody;
    }

    public void setMaternalDeathsIgaSerumAntibody(String maternalDeathsIgaSerumAntibody) {
        this.maternalDeathsIgaSerumAntibody = maternalDeathsIgaSerumAntibody;
    }

    public void setMaternalDeathsIncubationTime(String maternalDeathsIncubationTime) {
        this.maternalDeathsIncubationTime = maternalDeathsIncubationTime;
    }

    public void setMaternalDeathsIndirectFluorescentAntibody(String maternalDeathsIndirectFluorescentAntibody) {
        this.maternalDeathsIndirectFluorescentAntibody = maternalDeathsIndirectFluorescentAntibody;
    }

    public void setMaternalDeathsMicroscopy(String maternalDeathsMicroscopy) {
        this.maternalDeathsMicroscopy = maternalDeathsMicroscopy;
    }

    public void setMaternalDeathsNeutralizingAntibodies(String maternalDeathsNeutralizingAntibodies) {
        this.maternalDeathsNeutralizingAntibodies = maternalDeathsNeutralizingAntibodies;
    }

    public void setMaternalDeathsPcr(String maternalDeathsPcr) {
        this.maternalDeathsPcr = maternalDeathsPcr;
    }

    public void setMaternalDeathsGramStain(String maternalDeathsGramStain) {
        this.maternalDeathsGramStain = maternalDeathsGramStain;
    }

    public void setMaternalDeathsLatexAgglutination(String maternalDeathsLatexAgglutination) {
        this.maternalDeathsLatexAgglutination = maternalDeathsLatexAgglutination;
    }

    public void setMaternalDeathsCqValueDetection(String maternalDeathsCqValueDetection) {
        this.maternalDeathsCqValueDetection = maternalDeathsCqValueDetection;
    }

    public void setMaternalDeathsSeQuencing(String maternalDeathsSeQuencing) {
        this.maternalDeathsSeQuencing = maternalDeathsSeQuencing;
    }

    public void setMaternalDeathsDnaMicroArray(String maternalDeathsDnaMicroArray) {
        this.maternalDeathsDnaMicroArray = maternalDeathsDnaMicroArray;
    }

    public void setMaternalDeathsOther(String maternalDeathsOther) {
        this.maternalDeathsOther = maternalDeathsOther;
    }

    public void setPerinatalDeathsAntiBodyDetection(String perinatalDeathsAntiBodyDetection) {
        this.perinatalDeathsAntiBodyDetection = perinatalDeathsAntiBodyDetection;
    }

    public void setPerinatalDeathsAntigenDetection(String perinatalDeathsAntigenDetection) {
        this.perinatalDeathsAntigenDetection = perinatalDeathsAntigenDetection;
    }

    public void setPerinatalDeathsRapidTest(String perinatalDeathsRapidTest) {
        this.perinatalDeathsRapidTest = perinatalDeathsRapidTest;
    }

    public void setPerinatalDeathsCulture(String perinatalDeathsCulture) {
        this.perinatalDeathsCulture = perinatalDeathsCulture;
    }

    public void setPerinatalDeathsHistopathology(String perinatalDeathsHistopathology) {
        this.perinatalDeathsHistopathology = perinatalDeathsHistopathology;
    }

    public void setPerinatalDeathsIsolation(String perinatalDeathsIsolation) {
        this.perinatalDeathsIsolation = perinatalDeathsIsolation;
    }

    public void setPerinatalDeathsIgmSerumAntibody(String perinatalDeathsIgmSerumAntibody) {
        this.perinatalDeathsIgmSerumAntibody = perinatalDeathsIgmSerumAntibody;
    }

    public void setPerinatalDeathsIggSerumAntibody(String perinatalDeathsIggSerumAntibody) {
        this.perinatalDeathsIggSerumAntibody = perinatalDeathsIggSerumAntibody;
    }

    public void setPerinatalDeathsIgaSerumAntibody(String perinatalDeathsIgaSerumAntibody) {
        this.perinatalDeathsIgaSerumAntibody = perinatalDeathsIgaSerumAntibody;
    }

    public void setPerinatalDeathsIncubationTime(String perinatalDeathsIncubationTime) {
        this.perinatalDeathsIncubationTime = perinatalDeathsIncubationTime;
    }

    public void setPerinatalDeathsIndirectFluorescentAntibody(String perinatalDeathsIndirectFluorescentAntibody) {
        this.perinatalDeathsIndirectFluorescentAntibody = perinatalDeathsIndirectFluorescentAntibody;
    }

    public void setPerinatalDeathsMicroscopy(String perinatalDeathsMicroscopy) {
        this.perinatalDeathsMicroscopy = perinatalDeathsMicroscopy;
    }

    public void setPerinatalDeathsNeutralizingAntibodies(String perinatalDeathsNeutralizingAntibodies) {
        this.perinatalDeathsNeutralizingAntibodies = perinatalDeathsNeutralizingAntibodies;
    }

    public void setPerinatalDeathsPcr(String perinatalDeathsPcr) {
        this.perinatalDeathsPcr = perinatalDeathsPcr;
    }

    public void setPerinatalDeathsGramStain(String perinatalDeathsGramStain) {
        this.perinatalDeathsGramStain = perinatalDeathsGramStain;
    }

    public void setPerinatalDeathsLatexAgglutination(String perinatalDeathsLatexAgglutination) {
        this.perinatalDeathsLatexAgglutination = perinatalDeathsLatexAgglutination;
    }

    public void setPerinatalDeathsCqValueDetection(String perinatalDeathsCqValueDetection) {
        this.perinatalDeathsCqValueDetection = perinatalDeathsCqValueDetection;
    }

    public void setPerinatalDeathsSeQuencing(String perinatalDeathsSeQuencing) {
        this.perinatalDeathsSeQuencing = perinatalDeathsSeQuencing;
    }

    public void setPerinatalDeathsDnaMicroArray(String perinatalDeathsDnaMicroArray) {
        this.perinatalDeathsDnaMicroArray = perinatalDeathsDnaMicroArray;
    }

    public void setPerinatalDeathsOther(String perinatalDeathsOther) {
        this.perinatalDeathsOther = perinatalDeathsOther;
    }

    public void setInfluenzaAAntiBodyDetection(String influenzaAAntiBodyDetection) {
        this.influenzaAAntiBodyDetection = influenzaAAntiBodyDetection;
    }

    public void setInfluenzaAAntigenDetection(String influenzaAAntigenDetection) {
        this.influenzaAAntigenDetection = influenzaAAntigenDetection;
    }

    public void setInfluenzaARapidTest(String influenzaARapidTest) {
        this.influenzaARapidTest = influenzaARapidTest;
    }

    public void setInfluenzaACulture(String influenzaACulture) {
        this.influenzaACulture = influenzaACulture;
    }

    public void setInfluenzaAHistopathology(String influenzaAHistopathology) {
        this.influenzaAHistopathology = influenzaAHistopathology;
    }

    public void setInfluenzaAIsolation(String influenzaAIsolation) {
        this.influenzaAIsolation = influenzaAIsolation;
    }

    public void setInfluenzaAIgmSerumAntibody(String influenzaAIgmSerumAntibody) {
        this.influenzaAIgmSerumAntibody = influenzaAIgmSerumAntibody;
    }

    public void setInfluenzaAIggSerumAntibody(String influenzaAIggSerumAntibody) {
        this.influenzaAIggSerumAntibody = influenzaAIggSerumAntibody;
    }

    public void setInfluenzaAIgaSerumAntibody(String influenzaAIgaSerumAntibody) {
        this.influenzaAIgaSerumAntibody = influenzaAIgaSerumAntibody;
    }

    public void setInfluenzaAIncubationTime(String influenzaAIncubationTime) {
        this.influenzaAIncubationTime = influenzaAIncubationTime;
    }

    public void setInfluenzaAIndirectFluorescentAntibody(String influenzaAIndirectFluorescentAntibody) {
        this.influenzaAIndirectFluorescentAntibody = influenzaAIndirectFluorescentAntibody;
    }

    public void setInfluenzaAMicroscopy(String influenzaAMicroscopy) {
        this.influenzaAMicroscopy = influenzaAMicroscopy;
    }

    public void setInfluenzaANeutralizingAntibodies(String influenzaANeutralizingAntibodies) {
        this.influenzaANeutralizingAntibodies = influenzaANeutralizingAntibodies;
    }

    public void setInfluenzaAPcr(String influenzaAPcr) {
        this.influenzaAPcr = influenzaAPcr;
    }

    public void setInfluenzaAGramStain(String influenzaAGramStain) {
        this.influenzaAGramStain = influenzaAGramStain;
    }

    public void setInfluenzaALatexAgglutination(String influenzaALatexAgglutination) {
        this.influenzaALatexAgglutination = influenzaALatexAgglutination;
    }

    public void setInfluenzaACqValueDetection(String influenzaACqValueDetection) {
        this.influenzaACqValueDetection = influenzaACqValueDetection;
    }

    public void setInfluenzaASeQuencing(String influenzaASeQuencing) {
        this.influenzaASeQuencing = influenzaASeQuencing;
    }

    public void setInfluenzaADnaMicroArray(String influenzaADnaMicroArray) {
        this.influenzaADnaMicroArray = influenzaADnaMicroArray;
    }

    public void setInfluenzaAOther(String influenzaAOther) {
        this.influenzaAOther = influenzaAOther;
    }

    public void setInfluenzaBAntiBodyDetection(String influenzaBAntiBodyDetection) {
        this.influenzaBAntiBodyDetection = influenzaBAntiBodyDetection;
    }

    public void setInfluenzaBAntigenDetection(String influenzaBAntigenDetection) {
        this.influenzaBAntigenDetection = influenzaBAntigenDetection;
    }

    public void setInfluenzaBRapidTest(String influenzaBRapidTest) {
        this.influenzaBRapidTest = influenzaBRapidTest;
    }

    public void setInfluenzaBCulture(String influenzaBCulture) {
        this.influenzaBCulture = influenzaBCulture;
    }

    public void setInfluenzaBHistopathology(String influenzaBHistopathology) {
        this.influenzaBHistopathology = influenzaBHistopathology;
    }

    public void setInfluenzaBIsolation(String influenzaBIsolation) {
        this.influenzaBIsolation = influenzaBIsolation;
    }

    public void setInfluenzaBIgmSerumAntibody(String influenzaBIgmSerumAntibody) {
        this.influenzaBIgmSerumAntibody = influenzaBIgmSerumAntibody;
    }

    public void setInfluenzaBIggSerumAntibody(String influenzaBIggSerumAntibody) {
        this.influenzaBIggSerumAntibody = influenzaBIggSerumAntibody;
    }

    public void setInfluenzaBIgaSerumAntibody(String influenzaBIgaSerumAntibody) {
        this.influenzaBIgaSerumAntibody = influenzaBIgaSerumAntibody;
    }

    public void setInfluenzaBIncubationTime(String influenzaBIncubationTime) {
        this.influenzaBIncubationTime = influenzaBIncubationTime;
    }

    public void setInfluenzaBIndirectFluorescentAntibody(String influenzaBIndirectFluorescentAntibody) {
        this.influenzaBIndirectFluorescentAntibody = influenzaBIndirectFluorescentAntibody;
    }

    public void setInfluenzaBMicroscopy(String influenzaBMicroscopy) {
        this.influenzaBMicroscopy = influenzaBMicroscopy;
    }

    public void setInfluenzaBNeutralizingAntibodies(String influenzaBNeutralizingAntibodies) {
        this.influenzaBNeutralizingAntibodies = influenzaBNeutralizingAntibodies;
    }

    public void setInfluenzaBPcr(String influenzaBPcr) {
        this.influenzaBPcr = influenzaBPcr;
    }

    public void setInfluenzaBGramStain(String influenzaBGramStain) {
        this.influenzaBGramStain = influenzaBGramStain;
    }

    public void setInfluenzaBLatexAgglutination(String influenzaBLatexAgglutination) {
        this.influenzaBLatexAgglutination = influenzaBLatexAgglutination;
    }

    public void setInfluenzaBCqValueDetection(String influenzaBCqValueDetection) {
        this.influenzaBCqValueDetection = influenzaBCqValueDetection;
    }

    public void setInfluenzaBSeQuencing(String influenzaBSeQuencing) {
        this.influenzaBSeQuencing = influenzaBSeQuencing;
    }

    public void setInfluenzaBDnaMicroArray(String influenzaBDnaMicroArray) {
        this.influenzaBDnaMicroArray = influenzaBDnaMicroArray;
    }

    public void setInfluenzaBOther(String influenzaBOther) {
        this.influenzaBOther = influenzaBOther;
    }

    public void sethMetapneumovirusAntiBodyDetection(String hMetapneumovirusAntiBodyDetection) {
        this.hMetapneumovirusAntiBodyDetection = hMetapneumovirusAntiBodyDetection;
    }

    public void sethMetapneumovirusAntigenDetection(String hMetapneumovirusAntigenDetection) {
        this.hMetapneumovirusAntigenDetection = hMetapneumovirusAntigenDetection;
    }

    public void sethMetapneumovirusRapidTest(String hMetapneumovirusRapidTest) {
        this.hMetapneumovirusRapidTest = hMetapneumovirusRapidTest;
    }

    public void sethMetapneumovirusCulture(String hMetapneumovirusCulture) {
        this.hMetapneumovirusCulture = hMetapneumovirusCulture;
    }

    public void sethMetapneumovirusHistopathology(String hMetapneumovirusHistopathology) {
        this.hMetapneumovirusHistopathology = hMetapneumovirusHistopathology;
    }

    public void sethMetapneumovirusIsolation(String hMetapneumovirusIsolation) {
        this.hMetapneumovirusIsolation = hMetapneumovirusIsolation;
    }

    public void sethMetapneumovirusIgmSerumAntibody(String hMetapneumovirusIgmSerumAntibody) {
        this.hMetapneumovirusIgmSerumAntibody = hMetapneumovirusIgmSerumAntibody;
    }

    public void sethMetapneumovirusIggSerumAntibody(String hMetapneumovirusIggSerumAntibody) {
        this.hMetapneumovirusIggSerumAntibody = hMetapneumovirusIggSerumAntibody;
    }

    public void sethMetapneumovirusIgaSerumAntibody(String hMetapneumovirusIgaSerumAntibody) {
        this.hMetapneumovirusIgaSerumAntibody = hMetapneumovirusIgaSerumAntibody;
    }

    public void sethMetapneumovirusIncubationTime(String hMetapneumovirusIncubationTime) {
        this.hMetapneumovirusIncubationTime = hMetapneumovirusIncubationTime;
    }

    public void sethMetapneumovirusIndirectFluorescentAntibody(String hMetapneumovirusIndirectFluorescentAntibody) {
        this.hMetapneumovirusIndirectFluorescentAntibody = hMetapneumovirusIndirectFluorescentAntibody;
    }

    public void sethMetapneumovirusMicroscopy(String hMetapneumovirusMicroscopy) {
        this.hMetapneumovirusMicroscopy = hMetapneumovirusMicroscopy;
    }

    public void sethMetapneumovirusNeutralizingAntibodies(String hMetapneumovirusNeutralizingAntibodies) {
        this.hMetapneumovirusNeutralizingAntibodies = hMetapneumovirusNeutralizingAntibodies;
    }

    public void sethMetapneumovirusPcr(String hMetapneumovirusPcr) {
        this.hMetapneumovirusPcr = hMetapneumovirusPcr;
    }

    public void sethMetapneumovirusGramStain(String hMetapneumovirusGramStain) {
        this.hMetapneumovirusGramStain = hMetapneumovirusGramStain;
    }

    public void sethMetapneumovirusLatexAgglutination(String hMetapneumovirusLatexAgglutination) {
        this.hMetapneumovirusLatexAgglutination = hMetapneumovirusLatexAgglutination;
    }

    public void sethMetapneumovirusCqValueDetection(String hMetapneumovirusCqValueDetection) {
        this.hMetapneumovirusCqValueDetection = hMetapneumovirusCqValueDetection;
    }

    public void sethMetapneumovirusSeQuencing(String hMetapneumovirusSeQuencing) {
        this.hMetapneumovirusSeQuencing = hMetapneumovirusSeQuencing;
    }

    public void sethMetapneumovirusDnaMicroArray(String hMetapneumovirusDnaMicroArray) {
        this.hMetapneumovirusDnaMicroArray = hMetapneumovirusDnaMicroArray;
    }

    public void sethMetapneumovirusOther(String hMetapneumovirusOther) {
        this.hMetapneumovirusOther = hMetapneumovirusOther;
    }

    public void setRespiratorySyncytialVirusAntiBodyDetection(String respiratorySyncytialVirusAntiBodyDetection) {
        this.respiratorySyncytialVirusAntiBodyDetection = respiratorySyncytialVirusAntiBodyDetection;
    }

    public void setRespiratorySyncytialVirusAntigenDetection(String respiratorySyncytialVirusAntigenDetection) {
        this.respiratorySyncytialVirusAntigenDetection = respiratorySyncytialVirusAntigenDetection;
    }

    public void setRespiratorySyncytialVirusRapidTest(String respiratorySyncytialVirusRapidTest) {
        this.respiratorySyncytialVirusRapidTest = respiratorySyncytialVirusRapidTest;
    }

    public void setRespiratorySyncytialVirusCulture(String respiratorySyncytialVirusCulture) {
        this.respiratorySyncytialVirusCulture = respiratorySyncytialVirusCulture;
    }

    public void setRespiratorySyncytialVirusHistopathology(String respiratorySyncytialVirusHistopathology) {
        this.respiratorySyncytialVirusHistopathology = respiratorySyncytialVirusHistopathology;
    }

    public void setRespiratorySyncytialVirusIsolation(String respiratorySyncytialVirusIsolation) {
        this.respiratorySyncytialVirusIsolation = respiratorySyncytialVirusIsolation;
    }

    public void setRespiratorySyncytialVirusIgmSerumAntibody(String respiratorySyncytialVirusIgmSerumAntibody) {
        this.respiratorySyncytialVirusIgmSerumAntibody = respiratorySyncytialVirusIgmSerumAntibody;
    }

    public void setRespiratorySyncytialVirusIggSerumAntibody(String respiratorySyncytialVirusIggSerumAntibody) {
        this.respiratorySyncytialVirusIggSerumAntibody = respiratorySyncytialVirusIggSerumAntibody;
    }

    public void setRespiratorySyncytialVirusIgaSerumAntibody(String respiratorySyncytialVirusIgaSerumAntibody) {
        this.respiratorySyncytialVirusIgaSerumAntibody = respiratorySyncytialVirusIgaSerumAntibody;
    }

    public void setRespiratorySyncytialVirusIncubationTime(String respiratorySyncytialVirusIncubationTime) {
        this.respiratorySyncytialVirusIncubationTime = respiratorySyncytialVirusIncubationTime;
    }

    public void setRespiratorySyncytialVirusIndirectFluorescentAntibody(String respiratorySyncytialVirusIndirectFluorescentAntibody) {
        this.respiratorySyncytialVirusIndirectFluorescentAntibody = respiratorySyncytialVirusIndirectFluorescentAntibody;
    }

    public void setRespiratorySyncytialVirusMicroscopy(String respiratorySyncytialVirusMicroscopy) {
        this.respiratorySyncytialVirusMicroscopy = respiratorySyncytialVirusMicroscopy;
    }

    public void setRespiratorySyncytialVirusNeutralizingAntibodies(String respiratorySyncytialVirusNeutralizingAntibodies) {
        this.respiratorySyncytialVirusNeutralizingAntibodies = respiratorySyncytialVirusNeutralizingAntibodies;
    }

    public void setRespiratorySyncytialVirusPcr(String respiratorySyncytialVirusPcr) {
        this.respiratorySyncytialVirusPcr = respiratorySyncytialVirusPcr;
    }

    public void setRespiratorySyncytialVirusGramStain(String respiratorySyncytialVirusGramStain) {
        this.respiratorySyncytialVirusGramStain = respiratorySyncytialVirusGramStain;
    }

    public void setRespiratorySyncytialVirusLatexAgglutination(String respiratorySyncytialVirusLatexAgglutination) {
        this.respiratorySyncytialVirusLatexAgglutination = respiratorySyncytialVirusLatexAgglutination;
    }

    public void setRespiratorySyncytialVirusCqValueDetection(String respiratorySyncytialVirusCqValueDetection) {
        this.respiratorySyncytialVirusCqValueDetection = respiratorySyncytialVirusCqValueDetection;
    }

    public void setRespiratorySyncytialVirusSeQuencing(String respiratorySyncytialVirusSeQuencing) {
        this.respiratorySyncytialVirusSeQuencing = respiratorySyncytialVirusSeQuencing;
    }

    public void setRespiratorySyncytialVirusDnaMicroArray(String respiratorySyncytialVirusDnaMicroArray) {
        this.respiratorySyncytialVirusDnaMicroArray = respiratorySyncytialVirusDnaMicroArray;
    }

    public void setRespiratorySyncytialVirusOther(String respiratorySyncytialVirusOther) {
        this.respiratorySyncytialVirusOther = respiratorySyncytialVirusOther;
    }

    public void setParainfluenza1_4AntiBodyDetection(String parainfluenza1_4AntiBodyDetection) {
        this.parainfluenza1_4AntiBodyDetection = parainfluenza1_4AntiBodyDetection;
    }

    public void setParainfluenza1_4AntigenDetection(String parainfluenza1_4AntigenDetection) {
        this.parainfluenza1_4AntigenDetection = parainfluenza1_4AntigenDetection;
    }

    public void setParainfluenza1_4RapidTest(String parainfluenza1_4RapidTest) {
        this.parainfluenza1_4RapidTest = parainfluenza1_4RapidTest;
    }

    public void setParainfluenza1_4Culture(String parainfluenza1_4Culture) {
        this.parainfluenza1_4Culture = parainfluenza1_4Culture;
    }

    public void setParainfluenza1_4Histopathology(String parainfluenza1_4Histopathology) {
        this.parainfluenza1_4Histopathology = parainfluenza1_4Histopathology;
    }

    public void setParainfluenza1_4Isolation(String parainfluenza1_4Isolation) {
        this.parainfluenza1_4Isolation = parainfluenza1_4Isolation;
    }

    public void setParainfluenza1_4IgmSerumAntibody(String parainfluenza1_4IgmSerumAntibody) {
        this.parainfluenza1_4IgmSerumAntibody = parainfluenza1_4IgmSerumAntibody;
    }

    public void setParainfluenza1_4IggSerumAntibody(String parainfluenza1_4IggSerumAntibody) {
        this.parainfluenza1_4IggSerumAntibody = parainfluenza1_4IggSerumAntibody;
    }

    public void setParainfluenza1_4IgaSerumAntibody(String parainfluenza1_4IgaSerumAntibody) {
        this.parainfluenza1_4IgaSerumAntibody = parainfluenza1_4IgaSerumAntibody;
    }

    public void setParainfluenza1_4IncubationTime(String parainfluenza1_4IncubationTime) {
        this.parainfluenza1_4IncubationTime = parainfluenza1_4IncubationTime;
    }

    public void setParainfluenza1_4IndirectFluorescentAntibody(String parainfluenza1_4IndirectFluorescentAntibody) {
        this.parainfluenza1_4IndirectFluorescentAntibody = parainfluenza1_4IndirectFluorescentAntibody;
    }

    public void setParainfluenza1_4Microscopy(String parainfluenza1_4Microscopy) {
        this.parainfluenza1_4Microscopy = parainfluenza1_4Microscopy;
    }

    public void setParainfluenza1_4NeutralizingAntibodies(String parainfluenza1_4NeutralizingAntibodies) {
        this.parainfluenza1_4NeutralizingAntibodies = parainfluenza1_4NeutralizingAntibodies;
    }

    public void setParainfluenza1_4Pcr(String parainfluenza1_4Pcr) {
        this.parainfluenza1_4Pcr = parainfluenza1_4Pcr;
    }

    public void setParainfluenza1_4GramStain(String parainfluenza1_4GramStain) {
        this.parainfluenza1_4GramStain = parainfluenza1_4GramStain;
    }

    public void setParainfluenza1_4LatexAgglutination(String parainfluenza1_4LatexAgglutination) {
        this.parainfluenza1_4LatexAgglutination = parainfluenza1_4LatexAgglutination;
    }

    public void setParainfluenza1_4CqValueDetection(String parainfluenza1_4CqValueDetection) {
        this.parainfluenza1_4CqValueDetection = parainfluenza1_4CqValueDetection;
    }

    public void setParainfluenza1_4SeQuencing(String parainfluenza1_4SeQuencing) {
        this.parainfluenza1_4SeQuencing = parainfluenza1_4SeQuencing;
    }

    public void setParainfluenza1_4DnaMicroArray(String parainfluenza1_4DnaMicroArray) {
        this.parainfluenza1_4DnaMicroArray = parainfluenza1_4DnaMicroArray;
    }

    public void setParainfluenza1_4Other(String parainfluenza1_4Other) {
        this.parainfluenza1_4Other = parainfluenza1_4Other;
    }

    public void setAdenovirusAntiBodyDetection(String adenovirusAntiBodyDetection) {
        this.adenovirusAntiBodyDetection = adenovirusAntiBodyDetection;
    }

    public void setAdenovirusAntigenDetection(String adenovirusAntigenDetection) {
        this.adenovirusAntigenDetection = adenovirusAntigenDetection;
    }

    public void setAdenovirusRapidTest(String adenovirusRapidTest) {
        this.adenovirusRapidTest = adenovirusRapidTest;
    }

    public void setAdenovirusCulture(String adenovirusCulture) {
        this.adenovirusCulture = adenovirusCulture;
    }

    public void setAdenovirusHistopathology(String adenovirusHistopathology) {
        this.adenovirusHistopathology = adenovirusHistopathology;
    }

    public void setAdenovirusIsolation(String adenovirusIsolation) {
        this.adenovirusIsolation = adenovirusIsolation;
    }

    public void setAdenovirusIgmSerumAntibody(String adenovirusIgmSerumAntibody) {
        this.adenovirusIgmSerumAntibody = adenovirusIgmSerumAntibody;
    }

    public void setAdenovirusIggSerumAntibody(String adenovirusIggSerumAntibody) {
        this.adenovirusIggSerumAntibody = adenovirusIggSerumAntibody;
    }

    public void setAdenovirusIgaSerumAntibody(String adenovirusIgaSerumAntibody) {
        this.adenovirusIgaSerumAntibody = adenovirusIgaSerumAntibody;
    }

    public void setAdenovirusIncubationTime(String adenovirusIncubationTime) {
        this.adenovirusIncubationTime = adenovirusIncubationTime;
    }

    public void setAdenovirusIndirectFluorescentAntibody(String adenovirusIndirectFluorescentAntibody) {
        this.adenovirusIndirectFluorescentAntibody = adenovirusIndirectFluorescentAntibody;
    }

    public void setAdenovirusMicroscopy(String adenovirusMicroscopy) {
        this.adenovirusMicroscopy = adenovirusMicroscopy;
    }

    public void setAdenovirusNeutralizingAntibodies(String adenovirusNeutralizingAntibodies) {
        this.adenovirusNeutralizingAntibodies = adenovirusNeutralizingAntibodies;
    }

    public void setAdenovirusPcr(String adenovirusPcr) {
        this.adenovirusPcr = adenovirusPcr;
    }

    public void setAdenovirusGramStain(String adenovirusGramStain) {
        this.adenovirusGramStain = adenovirusGramStain;
    }

    public void setAdenovirusLatexAgglutination(String adenovirusLatexAgglutination) {
        this.adenovirusLatexAgglutination = adenovirusLatexAgglutination;
    }

    public void setAdenovirusCqValueDetection(String adenovirusCqValueDetection) {
        this.adenovirusCqValueDetection = adenovirusCqValueDetection;
    }

    public void setAdenovirusSeQuencing(String adenovirusSeQuencing) {
        this.adenovirusSeQuencing = adenovirusSeQuencing;
    }

    public void setAdenovirusDnaMicroArray(String adenovirusDnaMicroArray) {
        this.adenovirusDnaMicroArray = adenovirusDnaMicroArray;
    }

    public void setAdenovirusOther(String adenovirusOther) {
        this.adenovirusOther = adenovirusOther;
    }

    public void setRhinovirusAntiBodyDetection(String rhinovirusAntiBodyDetection) {
        this.rhinovirusAntiBodyDetection = rhinovirusAntiBodyDetection;
    }

    public void setRhinovirusAntigenDetection(String rhinovirusAntigenDetection) {
        this.rhinovirusAntigenDetection = rhinovirusAntigenDetection;
    }

    public void setRhinovirusRapidTest(String rhinovirusRapidTest) {
        this.rhinovirusRapidTest = rhinovirusRapidTest;
    }

    public void setRhinovirusCulture(String rhinovirusCulture) {
        this.rhinovirusCulture = rhinovirusCulture;
    }

    public void setRhinovirusHistopathology(String rhinovirusHistopathology) {
        this.rhinovirusHistopathology = rhinovirusHistopathology;
    }

    public void setRhinovirusIsolation(String rhinovirusIsolation) {
        this.rhinovirusIsolation = rhinovirusIsolation;
    }

    public void setRhinovirusIgmSerumAntibody(String rhinovirusIgmSerumAntibody) {
        this.rhinovirusIgmSerumAntibody = rhinovirusIgmSerumAntibody;
    }

    public void setRhinovirusIggSerumAntibody(String rhinovirusIggSerumAntibody) {
        this.rhinovirusIggSerumAntibody = rhinovirusIggSerumAntibody;
    }

    public void setRhinovirusIgaSerumAntibody(String rhinovirusIgaSerumAntibody) {
        this.rhinovirusIgaSerumAntibody = rhinovirusIgaSerumAntibody;
    }

    public void setRhinovirusIncubationTime(String rhinovirusIncubationTime) {
        this.rhinovirusIncubationTime = rhinovirusIncubationTime;
    }

    public void setRhinovirusIndirectFluorescentAntibody(String rhinovirusIndirectFluorescentAntibody) {
        this.rhinovirusIndirectFluorescentAntibody = rhinovirusIndirectFluorescentAntibody;
    }

    public void setRhinovirusMicroscopy(String rhinovirusMicroscopy) {
        this.rhinovirusMicroscopy = rhinovirusMicroscopy;
    }

    public void setRhinovirusNeutralizingAntibodies(String rhinovirusNeutralizingAntibodies) {
        this.rhinovirusNeutralizingAntibodies = rhinovirusNeutralizingAntibodies;
    }

    public void setRhinovirusPcr(String rhinovirusPcr) {
        this.rhinovirusPcr = rhinovirusPcr;
    }

    public void setRhinovirusGramStain(String rhinovirusGramStain) {
        this.rhinovirusGramStain = rhinovirusGramStain;
    }

    public void setRhinovirusLatexAgglutination(String rhinovirusLatexAgglutination) {
        this.rhinovirusLatexAgglutination = rhinovirusLatexAgglutination;
    }

    public void setRhinovirusCqValueDetection(String rhinovirusCqValueDetection) {
        this.rhinovirusCqValueDetection = rhinovirusCqValueDetection;
    }

    public void setRhinovirusSeQuencing(String rhinovirusSeQuencing) {
        this.rhinovirusSeQuencing = rhinovirusSeQuencing;
    }

    public void setRhinovirusDnaMicroArray(String rhinovirusDnaMicroArray) {
        this.rhinovirusDnaMicroArray = rhinovirusDnaMicroArray;
    }

    public void setRhinovirusOther(String rhinovirusOther) {
        this.rhinovirusOther = rhinovirusOther;
    }

    public void setEnterovirusAntiBodyDetection(String enterovirusAntiBodyDetection) {
        this.enterovirusAntiBodyDetection = enterovirusAntiBodyDetection;
    }

    public void setEnterovirusAntigenDetection(String enterovirusAntigenDetection) {
        this.enterovirusAntigenDetection = enterovirusAntigenDetection;
    }

    public void setEnterovirusRapidTest(String enterovirusRapidTest) {
        this.enterovirusRapidTest = enterovirusRapidTest;
    }

    public void setEnterovirusCulture(String enterovirusCulture) {
        this.enterovirusCulture = enterovirusCulture;
    }

    public void setEnterovirusHistopathology(String enterovirusHistopathology) {
        this.enterovirusHistopathology = enterovirusHistopathology;
    }

    public void setEnterovirusIsolation(String enterovirusIsolation) {
        this.enterovirusIsolation = enterovirusIsolation;
    }

    public void setEnterovirusIgmSerumAntibody(String enterovirusIgmSerumAntibody) {
        this.enterovirusIgmSerumAntibody = enterovirusIgmSerumAntibody;
    }

    public void setEnterovirusIggSerumAntibody(String enterovirusIggSerumAntibody) {
        this.enterovirusIggSerumAntibody = enterovirusIggSerumAntibody;
    }

    public void setEnterovirusIgaSerumAntibody(String enterovirusIgaSerumAntibody) {
        this.enterovirusIgaSerumAntibody = enterovirusIgaSerumAntibody;
    }

    public void setEnterovirusIncubationTime(String enterovirusIncubationTime) {
        this.enterovirusIncubationTime = enterovirusIncubationTime;
    }

    public void setEnterovirusIndirectFluorescentAntibody(String enterovirusIndirectFluorescentAntibody) {
        this.enterovirusIndirectFluorescentAntibody = enterovirusIndirectFluorescentAntibody;
    }

    public void setEnterovirusMicroscopy(String enterovirusMicroscopy) {
        this.enterovirusMicroscopy = enterovirusMicroscopy;
    }

    public void setEnterovirusNeutralizingAntibodies(String enterovirusNeutralizingAntibodies) {
        this.enterovirusNeutralizingAntibodies = enterovirusNeutralizingAntibodies;
    }

    public void setEnterovirusPcr(String enterovirusPcr) {
        this.enterovirusPcr = enterovirusPcr;
    }

    public void setEnterovirusGramStain(String enterovirusGramStain) {
        this.enterovirusGramStain = enterovirusGramStain;
    }

    public void setEnterovirusLatexAgglutination(String enterovirusLatexAgglutination) {
        this.enterovirusLatexAgglutination = enterovirusLatexAgglutination;
    }

    public void setEnterovirusCqValueDetection(String enterovirusCqValueDetection) {
        this.enterovirusCqValueDetection = enterovirusCqValueDetection;
    }

    public void setEnterovirusSeQuencing(String enterovirusSeQuencing) {
        this.enterovirusSeQuencing = enterovirusSeQuencing;
    }

    public void setEnterovirusDnaMicroArray(String enterovirusDnaMicroArray) {
        this.enterovirusDnaMicroArray = enterovirusDnaMicroArray;
    }

    public void setEnterovirusOther(String enterovirusOther) {
        this.enterovirusOther = enterovirusOther;
    }

    public void setmPneumoniaeAntiBodyDetection(String mPneumoniaeAntiBodyDetection) {
        this.mPneumoniaeAntiBodyDetection = mPneumoniaeAntiBodyDetection;
    }

    public void setmPneumoniaeAntigenDetection(String mPneumoniaeAntigenDetection) {
        this.mPneumoniaeAntigenDetection = mPneumoniaeAntigenDetection;
    }

    public void setmPneumoniaeRapidTest(String mPneumoniaeRapidTest) {
        this.mPneumoniaeRapidTest = mPneumoniaeRapidTest;
    }

    public void setmPneumoniaeCulture(String mPneumoniaeCulture) {
        this.mPneumoniaeCulture = mPneumoniaeCulture;
    }

    public void setmPneumoniaeHistopathology(String mPneumoniaeHistopathology) {
        this.mPneumoniaeHistopathology = mPneumoniaeHistopathology;
    }

    public void setmPneumoniaeIsolation(String mPneumoniaeIsolation) {
        this.mPneumoniaeIsolation = mPneumoniaeIsolation;
    }

    public void setmPneumoniaeIgmSerumAntibody(String mPneumoniaeIgmSerumAntibody) {
        this.mPneumoniaeIgmSerumAntibody = mPneumoniaeIgmSerumAntibody;
    }

    public void setmPneumoniaeIggSerumAntibody(String mPneumoniaeIggSerumAntibody) {
        this.mPneumoniaeIggSerumAntibody = mPneumoniaeIggSerumAntibody;
    }

    public void setmPneumoniaeIgaSerumAntibody(String mPneumoniaeIgaSerumAntibody) {
        this.mPneumoniaeIgaSerumAntibody = mPneumoniaeIgaSerumAntibody;
    }

    public void setmPneumoniaeIncubationTime(String mPneumoniaeIncubationTime) {
        this.mPneumoniaeIncubationTime = mPneumoniaeIncubationTime;
    }

    public void setmPneumoniaeIndirectFluorescentAntibody(String mPneumoniaeIndirectFluorescentAntibody) {
        this.mPneumoniaeIndirectFluorescentAntibody = mPneumoniaeIndirectFluorescentAntibody;
    }

    public void setmPneumoniaeMicroscopy(String mPneumoniaeMicroscopy) {
        this.mPneumoniaeMicroscopy = mPneumoniaeMicroscopy;
    }

    public void setmPneumoniaeNeutralizingAntibodies(String mPneumoniaeNeutralizingAntibodies) {
        this.mPneumoniaeNeutralizingAntibodies = mPneumoniaeNeutralizingAntibodies;
    }

    public void setmPneumoniaePcr(String mPneumoniaePcr) {
        this.mPneumoniaePcr = mPneumoniaePcr;
    }

    public void setmPneumoniaeGramStain(String mPneumoniaeGramStain) {
        this.mPneumoniaeGramStain = mPneumoniaeGramStain;
    }

    public void setmPneumoniaeLatexAgglutination(String mPneumoniaeLatexAgglutination) {
        this.mPneumoniaeLatexAgglutination = mPneumoniaeLatexAgglutination;
    }

    public void setmPneumoniaeCqValueDetection(String mPneumoniaeCqValueDetection) {
        this.mPneumoniaeCqValueDetection = mPneumoniaeCqValueDetection;
    }

    public void setmPneumoniaeSeQuencing(String mPneumoniaeSeQuencing) {
        this.mPneumoniaeSeQuencing = mPneumoniaeSeQuencing;
    }

    public void setmPneumoniaeDnaMicroArray(String mPneumoniaeDnaMicroArray) {
        this.mPneumoniaeDnaMicroArray = mPneumoniaeDnaMicroArray;
    }

    public void setmPneumoniaeOther(String mPneumoniaeOther) {
        this.mPneumoniaeOther = mPneumoniaeOther;
    }

    public void setcPneumoniaeAntiBodyDetection(String cPneumoniaeAntiBodyDetection) {
        this.cPneumoniaeAntiBodyDetection = cPneumoniaeAntiBodyDetection;
    }

    public void setcPneumoniaeAntigenDetection(String cPneumoniaeAntigenDetection) {
        this.cPneumoniaeAntigenDetection = cPneumoniaeAntigenDetection;
    }

    public void setcPneumoniaeRapidTest(String cPneumoniaeRapidTest) {
        this.cPneumoniaeRapidTest = cPneumoniaeRapidTest;
    }

    public void setcPneumoniaeCulture(String cPneumoniaeCulture) {
        this.cPneumoniaeCulture = cPneumoniaeCulture;
    }

    public void setcPneumoniaeHistopathology(String cPneumoniaeHistopathology) {
        this.cPneumoniaeHistopathology = cPneumoniaeHistopathology;
    }

    public void setcPneumoniaeIsolation(String cPneumoniaeIsolation) {
        this.cPneumoniaeIsolation = cPneumoniaeIsolation;
    }

    public void setcPneumoniaeIgmSerumAntibody(String cPneumoniaeIgmSerumAntibody) {
        this.cPneumoniaeIgmSerumAntibody = cPneumoniaeIgmSerumAntibody;
    }

    public void setcPneumoniaeIggSerumAntibody(String cPneumoniaeIggSerumAntibody) {
        this.cPneumoniaeIggSerumAntibody = cPneumoniaeIggSerumAntibody;
    }

    public void setcPneumoniaeIgaSerumAntibody(String cPneumoniaeIgaSerumAntibody) {
        this.cPneumoniaeIgaSerumAntibody = cPneumoniaeIgaSerumAntibody;
    }

    public void setcPneumoniaeIncubationTime(String cPneumoniaeIncubationTime) {
        this.cPneumoniaeIncubationTime = cPneumoniaeIncubationTime;
    }

    public void setcPneumoniaeIndirectFluorescentAntibody(String cPneumoniaeIndirectFluorescentAntibody) {
        this.cPneumoniaeIndirectFluorescentAntibody = cPneumoniaeIndirectFluorescentAntibody;
    }

    public void setcPneumoniaeMicroscopy(String cPneumoniaeMicroscopy) {
        this.cPneumoniaeMicroscopy = cPneumoniaeMicroscopy;
    }

    public void setcPneumoniaeNeutralizingAntibodies(String cPneumoniaeNeutralizingAntibodies) {
        this.cPneumoniaeNeutralizingAntibodies = cPneumoniaeNeutralizingAntibodies;
    }

    public void setcPneumoniaePcr(String cPneumoniaePcr) {
        this.cPneumoniaePcr = cPneumoniaePcr;
    }

    public void setcPneumoniaeGramStain(String cPneumoniaeGramStain) {
        this.cPneumoniaeGramStain = cPneumoniaeGramStain;
    }

    public void setcPneumoniaeLatexAgglutination(String cPneumoniaeLatexAgglutination) {
        this.cPneumoniaeLatexAgglutination = cPneumoniaeLatexAgglutination;
    }

    public void setcPneumoniaeCqValueDetection(String cPneumoniaeCqValueDetection) {
        this.cPneumoniaeCqValueDetection = cPneumoniaeCqValueDetection;
    }

    public void setcPneumoniaeSeQuencing(String cPneumoniaeSeQuencing) {
        this.cPneumoniaeSeQuencing = cPneumoniaeSeQuencing;
    }

    public void setcPneumoniaeDnaMicroArray(String cPneumoniaeDnaMicroArray) {
        this.cPneumoniaeDnaMicroArray = cPneumoniaeDnaMicroArray;
    }

    public void setcPneumoniaeOther(String cPneumoniaeOther) {
        this.cPneumoniaeOther = cPneumoniaeOther;
    }

    public void setAriAntiBodyDetection(String ariAntiBodyDetection) {
        this.ariAntiBodyDetection = ariAntiBodyDetection;
    }

    public void setAriAntigenDetection(String ariAntigenDetection) {
        this.ariAntigenDetection = ariAntigenDetection;
    }

    public void setAriRapidTest(String ariRapidTest) {
        this.ariRapidTest = ariRapidTest;
    }

    public void setAriCulture(String ariCulture) {
        this.ariCulture = ariCulture;
    }

    public void setAriHistopathology(String ariHistopathology) {
        this.ariHistopathology = ariHistopathology;
    }

    public void setAriIsolation(String ariIsolation) {
        this.ariIsolation = ariIsolation;
    }

    public void setAriIgmSerumAntibody(String ariIgmSerumAntibody) {
        this.ariIgmSerumAntibody = ariIgmSerumAntibody;
    }

    public void setAriIggSerumAntibody(String ariIggSerumAntibody) {
        this.ariIggSerumAntibody = ariIggSerumAntibody;
    }

    public void setAriIgaSerumAntibody(String ariIgaSerumAntibody) {
        this.ariIgaSerumAntibody = ariIgaSerumAntibody;
    }

    public void setAriIncubationTime(String ariIncubationTime) {
        this.ariIncubationTime = ariIncubationTime;
    }

    public void setAriIndirectFluorescentAntibody(String ariIndirectFluorescentAntibody) {
        this.ariIndirectFluorescentAntibody = ariIndirectFluorescentAntibody;
    }

    public void setAriMicroscopy(String ariMicroscopy) {
        this.ariMicroscopy = ariMicroscopy;
    }

    public void setAriNeutralizingAntibodies(String ariNeutralizingAntibodies) {
        this.ariNeutralizingAntibodies = ariNeutralizingAntibodies;
    }

    public void setAriPcr(String ariPcr) {
        this.ariPcr = ariPcr;
    }

    public void setAriGramStain(String ariGramStain) {
        this.ariGramStain = ariGramStain;
    }

    public void setAriLatexAgglutination(String ariLatexAgglutination) {
        this.ariLatexAgglutination = ariLatexAgglutination;
    }

    public void setAriCqValueDetection(String ariCqValueDetection) {
        this.ariCqValueDetection = ariCqValueDetection;
    }

    public void setAriSeQuencing(String ariSeQuencing) {
        this.ariSeQuencing = ariSeQuencing;
    }

    public void setAriDnaMicroArray(String ariDnaMicroArray) {
        this.ariDnaMicroArray = ariDnaMicroArray;
    }

    public void setAriOther(String ariOther) {
        this.ariOther = ariOther;
    }

    public void setChikungunyaAntiBodyDetection(String chikungunyaAntiBodyDetection) {
        this.chikungunyaAntiBodyDetection = chikungunyaAntiBodyDetection;
    }

    public void setChikungunyaAntigenDetection(String chikungunyaAntigenDetection) {
        this.chikungunyaAntigenDetection = chikungunyaAntigenDetection;
    }

    public void setChikungunyaRapidTest(String chikungunyaRapidTest) {
        this.chikungunyaRapidTest = chikungunyaRapidTest;
    }

    public void setChikungunyaCulture(String chikungunyaCulture) {
        this.chikungunyaCulture = chikungunyaCulture;
    }

    public void setChikungunyaHistopathology(String chikungunyaHistopathology) {
        this.chikungunyaHistopathology = chikungunyaHistopathology;
    }

    public void setChikungunyaIsolation(String chikungunyaIsolation) {
        this.chikungunyaIsolation = chikungunyaIsolation;
    }

    public void setChikungunyaIgmSerumAntibody(String chikungunyaIgmSerumAntibody) {
        this.chikungunyaIgmSerumAntibody = chikungunyaIgmSerumAntibody;
    }

    public void setChikungunyaIggSerumAntibody(String chikungunyaIggSerumAntibody) {
        this.chikungunyaIggSerumAntibody = chikungunyaIggSerumAntibody;
    }

    public void setChikungunyaIgaSerumAntibody(String chikungunyaIgaSerumAntibody) {
        this.chikungunyaIgaSerumAntibody = chikungunyaIgaSerumAntibody;
    }

    public void setChikungunyaIncubationTime(String chikungunyaIncubationTime) {
        this.chikungunyaIncubationTime = chikungunyaIncubationTime;
    }

    public void setChikungunyaIndirectFluorescentAntibody(String chikungunyaIndirectFluorescentAntibody) {
        this.chikungunyaIndirectFluorescentAntibody = chikungunyaIndirectFluorescentAntibody;
    }

    public void setChikungunyaMicroscopy(String chikungunyaMicroscopy) {
        this.chikungunyaMicroscopy = chikungunyaMicroscopy;
    }

    public void setChikungunyaNeutralizingAntibodies(String chikungunyaNeutralizingAntibodies) {
        this.chikungunyaNeutralizingAntibodies = chikungunyaNeutralizingAntibodies;
    }

    public void setChikungunyaPcr(String chikungunyaPcr) {
        this.chikungunyaPcr = chikungunyaPcr;
    }

    public void setChikungunyaGramStain(String chikungunyaGramStain) {
        this.chikungunyaGramStain = chikungunyaGramStain;
    }

    public void setChikungunyaLatexAgglutination(String chikungunyaLatexAgglutination) {
        this.chikungunyaLatexAgglutination = chikungunyaLatexAgglutination;
    }

    public void setChikungunyaCqValueDetection(String chikungunyaCqValueDetection) {
        this.chikungunyaCqValueDetection = chikungunyaCqValueDetection;
    }

    public void setChikungunyaSeQuencing(String chikungunyaSeQuencing) {
        this.chikungunyaSeQuencing = chikungunyaSeQuencing;
    }

    public void setChikungunyaDnaMicroArray(String chikungunyaDnaMicroArray) {
        this.chikungunyaDnaMicroArray = chikungunyaDnaMicroArray;
    }

    public void setChikungunyaOther(String chikungunyaOther) {
        this.chikungunyaOther = chikungunyaOther;
    }

    public void setPostImmunizationAdverseEventsMildAntiBodyDetection(String postImmunizationAdverseEventsMildAntiBodyDetection) {
        this.postImmunizationAdverseEventsMildAntiBodyDetection = postImmunizationAdverseEventsMildAntiBodyDetection;
    }

    public void setPostImmunizationAdverseEventsMildAntigenDetection(String postImmunizationAdverseEventsMildAntigenDetection) {
        this.postImmunizationAdverseEventsMildAntigenDetection = postImmunizationAdverseEventsMildAntigenDetection;
    }

    public void setPostImmunizationAdverseEventsMildRapidTest(String postImmunizationAdverseEventsMildRapidTest) {
        this.postImmunizationAdverseEventsMildRapidTest = postImmunizationAdverseEventsMildRapidTest;
    }

    public void setPostImmunizationAdverseEventsMildCulture(String postImmunizationAdverseEventsMildCulture) {
        this.postImmunizationAdverseEventsMildCulture = postImmunizationAdverseEventsMildCulture;
    }

    public void setPostImmunizationAdverseEventsMildHistopathology(String postImmunizationAdverseEventsMildHistopathology) {
        this.postImmunizationAdverseEventsMildHistopathology = postImmunizationAdverseEventsMildHistopathology;
    }

    public void setPostImmunizationAdverseEventsMildIsolation(String postImmunizationAdverseEventsMildIsolation) {
        this.postImmunizationAdverseEventsMildIsolation = postImmunizationAdverseEventsMildIsolation;
    }

    public void setPostImmunizationAdverseEventsMildIgmSerumAntibody(String postImmunizationAdverseEventsMildIgmSerumAntibody) {
        this.postImmunizationAdverseEventsMildIgmSerumAntibody = postImmunizationAdverseEventsMildIgmSerumAntibody;
    }

    public void setPostImmunizationAdverseEventsMildIggSerumAntibody(String postImmunizationAdverseEventsMildIggSerumAntibody) {
        this.postImmunizationAdverseEventsMildIggSerumAntibody = postImmunizationAdverseEventsMildIggSerumAntibody;
    }

    public void setPostImmunizationAdverseEventsMildIgaSerumAntibody(String postImmunizationAdverseEventsMildIgaSerumAntibody) {
        this.postImmunizationAdverseEventsMildIgaSerumAntibody = postImmunizationAdverseEventsMildIgaSerumAntibody;
    }

    public void setPostImmunizationAdverseEventsMildIncubationTime(String postImmunizationAdverseEventsMildIncubationTime) {
        this.postImmunizationAdverseEventsMildIncubationTime = postImmunizationAdverseEventsMildIncubationTime;
    }

    public void setPostImmunizationAdverseEventsMildIndirectFluorescentAntibody(String postImmunizationAdverseEventsMildIndirectFluorescentAntibody) {
        this.postImmunizationAdverseEventsMildIndirectFluorescentAntibody = postImmunizationAdverseEventsMildIndirectFluorescentAntibody;
    }

    public void setPostImmunizationAdverseEventsMildMicroscopy(String postImmunizationAdverseEventsMildMicroscopy) {
        this.postImmunizationAdverseEventsMildMicroscopy = postImmunizationAdverseEventsMildMicroscopy;
    }

    public void setPostImmunizationAdverseEventsMildNeutralizingAntibodies(String postImmunizationAdverseEventsMildNeutralizingAntibodies) {
        this.postImmunizationAdverseEventsMildNeutralizingAntibodies = postImmunizationAdverseEventsMildNeutralizingAntibodies;
    }

    public void setPostImmunizationAdverseEventsMildPcr(String postImmunizationAdverseEventsMildPcr) {
        this.postImmunizationAdverseEventsMildPcr = postImmunizationAdverseEventsMildPcr;
    }

    public void setPostImmunizationAdverseEventsMildGramStain(String postImmunizationAdverseEventsMildGramStain) {
        this.postImmunizationAdverseEventsMildGramStain = postImmunizationAdverseEventsMildGramStain;
    }

    public void setPostImmunizationAdverseEventsMildLatexAgglutination(String postImmunizationAdverseEventsMildLatexAgglutination) {
        this.postImmunizationAdverseEventsMildLatexAgglutination = postImmunizationAdverseEventsMildLatexAgglutination;
    }

    public void setPostImmunizationAdverseEventsMildCqValueDetection(String postImmunizationAdverseEventsMildCqValueDetection) {
        this.postImmunizationAdverseEventsMildCqValueDetection = postImmunizationAdverseEventsMildCqValueDetection;
    }

    public void setPostImmunizationAdverseEventsMildSeQuencing(String postImmunizationAdverseEventsMildSeQuencing) {
        this.postImmunizationAdverseEventsMildSeQuencing = postImmunizationAdverseEventsMildSeQuencing;
    }

    public void setPostImmunizationAdverseEventsMildDnaMicroArray(String postImmunizationAdverseEventsMildDnaMicroArray) {
        this.postImmunizationAdverseEventsMildDnaMicroArray = postImmunizationAdverseEventsMildDnaMicroArray;
    }

    public void setPostImmunizationAdverseEventsMildOther(String postImmunizationAdverseEventsMildOther) {
        this.postImmunizationAdverseEventsMildOther = postImmunizationAdverseEventsMildOther;
    }

    public void setPostImmunizationAdverseEventsSevereAntiBodyDetection(String PostImmunizationAdverseEventsSevereAntiBodyDetection) {
        this.PostImmunizationAdverseEventsSevereAntiBodyDetection = PostImmunizationAdverseEventsSevereAntiBodyDetection;
    }

    public void setPostImmunizationAdverseEventsSevereAntigenDetection(String PostImmunizationAdverseEventsSevereAntigenDetection) {
        this.PostImmunizationAdverseEventsSevereAntigenDetection = PostImmunizationAdverseEventsSevereAntigenDetection;
    }

    public void setPostImmunizationAdverseEventsSevereRapidTest(String PostImmunizationAdverseEventsSevereRapidTest) {
        this.PostImmunizationAdverseEventsSevereRapidTest = PostImmunizationAdverseEventsSevereRapidTest;
    }

    public void setPostImmunizationAdverseEventsSevereCulture(String PostImmunizationAdverseEventsSevereCulture) {
        this.PostImmunizationAdverseEventsSevereCulture = PostImmunizationAdverseEventsSevereCulture;
    }

    public void setPostImmunizationAdverseEventsSevereHistopathology(String PostImmunizationAdverseEventsSevereHistopathology) {
        this.PostImmunizationAdverseEventsSevereHistopathology = PostImmunizationAdverseEventsSevereHistopathology;
    }

    public void setPostImmunizationAdverseEventsSevereIsolation(String PostImmunizationAdverseEventsSevereIsolation) {
        this.PostImmunizationAdverseEventsSevereIsolation = PostImmunizationAdverseEventsSevereIsolation;
    }

    public void setPostImmunizationAdverseEventsSevereIgmSerumAntibody(String PostImmunizationAdverseEventsSevereIgmSerumAntibody) {
        this.PostImmunizationAdverseEventsSevereIgmSerumAntibody = PostImmunizationAdverseEventsSevereIgmSerumAntibody;
    }

    public void setPostImmunizationAdverseEventsSevereIggSerumAntibody(String PostImmunizationAdverseEventsSevereIggSerumAntibody) {
        this.PostImmunizationAdverseEventsSevereIggSerumAntibody = PostImmunizationAdverseEventsSevereIggSerumAntibody;
    }

    public void setPostImmunizationAdverseEventsSevereIgaSerumAntibody(String PostImmunizationAdverseEventsSevereIgaSerumAntibody) {
        this.PostImmunizationAdverseEventsSevereIgaSerumAntibody = PostImmunizationAdverseEventsSevereIgaSerumAntibody;
    }

    public void setPostImmunizationAdverseEventsSevereIncubationTime(String PostImmunizationAdverseEventsSevereIncubationTime) {
        this.PostImmunizationAdverseEventsSevereIncubationTime = PostImmunizationAdverseEventsSevereIncubationTime;
    }

    public void setPostImmunizationAdverseEventsSevereIndirectFluorescentAntibody(String PostImmunizationAdverseEventsSevereIndirectFluorescentAntibody) {
        this.PostImmunizationAdverseEventsSevereIndirectFluorescentAntibody = PostImmunizationAdverseEventsSevereIndirectFluorescentAntibody;
    }

    public void setPostImmunizationAdverseEventsSevereMicroscopy(String PostImmunizationAdverseEventsSevereMicroscopy) {
        this.PostImmunizationAdverseEventsSevereMicroscopy = PostImmunizationAdverseEventsSevereMicroscopy;
    }

    public void setPostImmunizationAdverseEventsSevereNeutralizingAntibodies(String PostImmunizationAdverseEventsSevereNeutralizingAntibodies) {
        this.PostImmunizationAdverseEventsSevereNeutralizingAntibodies = PostImmunizationAdverseEventsSevereNeutralizingAntibodies;
    }

    public void setPostImmunizationAdverseEventsSeverePcr(String PostImmunizationAdverseEventsSeverePcr) {
        this.PostImmunizationAdverseEventsSeverePcr = PostImmunizationAdverseEventsSeverePcr;
    }

    public void setPostImmunizationAdverseEventsSevereGramStain(String PostImmunizationAdverseEventsSevereGramStain) {
        this.PostImmunizationAdverseEventsSevereGramStain = PostImmunizationAdverseEventsSevereGramStain;
    }

    public void setPostImmunizationAdverseEventsSevereLatexAgglutination(String PostImmunizationAdverseEventsSevereLatexAgglutination) {
        this.PostImmunizationAdverseEventsSevereLatexAgglutination = PostImmunizationAdverseEventsSevereLatexAgglutination;
    }

    public void setPostImmunizationAdverseEventsSevereCqValueDetection(String PostImmunizationAdverseEventsSevereCqValueDetection) {
        this.PostImmunizationAdverseEventsSevereCqValueDetection = PostImmunizationAdverseEventsSevereCqValueDetection;
    }

    public void setPostImmunizationAdverseEventsSevereSeQuencing(String PostImmunizationAdverseEventsSevereSeQuencing) {
        this.PostImmunizationAdverseEventsSevereSeQuencing = PostImmunizationAdverseEventsSevereSeQuencing;
    }

    public void setPostImmunizationAdverseEventsSevereDnaMicroArray(String PostImmunizationAdverseEventsSevereDnaMicroArray) {
        this.PostImmunizationAdverseEventsSevereDnaMicroArray = PostImmunizationAdverseEventsSevereDnaMicroArray;
    }

    public void setPostImmunizationAdverseEventsSevereOther(String PostImmunizationAdverseEventsSevereOther) {
        this.PostImmunizationAdverseEventsSevereOther = PostImmunizationAdverseEventsSevereOther;
    }

    public void setFhaAntiBodyDetection(String fhaAntiBodyDetection) {
        this.fhaAntiBodyDetection = fhaAntiBodyDetection;
    }

    public void setFhaAntigenDetection(String fhaAntigenDetection) {
        this.fhaAntigenDetection = fhaAntigenDetection;
    }

    public void setFhaRapidTest(String fhaRapidTest) {
        this.fhaRapidTest = fhaRapidTest;
    }

    public void setFhaCulture(String fhaCulture) {
        this.fhaCulture = fhaCulture;
    }

    public void setFhaHistopathology(String fhaHistopathology) {
        this.fhaHistopathology = fhaHistopathology;
    }

    public void setFhaIsolation(String fhaIsolation) {
        this.fhaIsolation = fhaIsolation;
    }

    public void setFhaIgmSerumAntibody(String fhaIgmSerumAntibody) {
        this.fhaIgmSerumAntibody = fhaIgmSerumAntibody;
    }

    public void setFhaIggSerumAntibody(String fhaIggSerumAntibody) {
        this.fhaIggSerumAntibody = fhaIggSerumAntibody;
    }

    public void setFhaIgaSerumAntibody(String fhaIgaSerumAntibody) {
        this.fhaIgaSerumAntibody = fhaIgaSerumAntibody;
    }

    public void setFhaIncubationTime(String fhaIncubationTime) {
        this.fhaIncubationTime = fhaIncubationTime;
    }

    public void setFhaIndirectFluorescentAntibody(String fhaIndirectFluorescentAntibody) {
        this.fhaIndirectFluorescentAntibody = fhaIndirectFluorescentAntibody;
    }

    public void setFhaMicroscopy(String fhaMicroscopy) {
        this.fhaMicroscopy = fhaMicroscopy;
    }

    public void setFhaNeutralizingAntibodies(String fhaNeutralizingAntibodies) {
        this.fhaNeutralizingAntibodies = fhaNeutralizingAntibodies;
    }

    public void setFhaPcr(String fhaPcr) {
        this.fhaPcr = fhaPcr;
    }

    public void setFhaGramStain(String fhaGramStain) {
        this.fhaGramStain = fhaGramStain;
    }

    public void setFhaLatexAgglutination(String fhaLatexAgglutination) {
        this.fhaLatexAgglutination = fhaLatexAgglutination;
    }

    public void setFhaCqValueDetection(String fhaCqValueDetection) {
        this.fhaCqValueDetection = fhaCqValueDetection;
    }

    public void setFhaSeQuencing(String fhaSeQuencing) {
        this.fhaSeQuencing = fhaSeQuencing;
    }

    public void setFhaDnaMicroArray(String fhaDnaMicroArray) {
        this.fhaDnaMicroArray = fhaDnaMicroArray;
    }

    public void setFhaOther(String fhaOther) {
        this.fhaOther = fhaOther;
    }

    public void setOtherAntiBodyDetection(String otherAntiBodyDetection) {
        this.otherAntiBodyDetection = otherAntiBodyDetection;
    }

    public void setOtherAntigenDetection(String otherAntigenDetection) {
        this.otherAntigenDetection = otherAntigenDetection;
    }

    public void setOtherRapidTest(String otherRapidTest) {
        this.otherRapidTest = otherRapidTest;
    }

    public void setOtherCulture(String otherCulture) {
        this.otherCulture = otherCulture;
    }

    public void setOtherHistopathology(String otherHistopathology) {
        this.otherHistopathology = otherHistopathology;
    }

    public void setOtherIsolation(String otherIsolation) {
        this.otherIsolation = otherIsolation;
    }

    public void setOtherIgmSerumAntibody(String otherIgmSerumAntibody) {
        this.otherIgmSerumAntibody = otherIgmSerumAntibody;
    }

    public void setOtherIggSerumAntibody(String otherIggSerumAntibody) {
        this.otherIggSerumAntibody = otherIggSerumAntibody;
    }

    public void setOtherIgaSerumAntibody(String otherIgaSerumAntibody) {
        this.otherIgaSerumAntibody = otherIgaSerumAntibody;
    }

    public void setOtherIncubationTime(String otherIncubationTime) {
        this.otherIncubationTime = otherIncubationTime;
    }

    public void setOtherIndirectFluorescentAntibody(String otherIndirectFluorescentAntibody) {
        this.otherIndirectFluorescentAntibody = otherIndirectFluorescentAntibody;
    }

    public void setOtherMicroscopy(String otherMicroscopy) {
        this.otherMicroscopy = otherMicroscopy;
    }

    public void setOtherNeutralizingAntibodies(String otherNeutralizingAntibodies) {
        this.otherNeutralizingAntibodies = otherNeutralizingAntibodies;
    }

    public void setOtherPcr(String otherPcr) {
        this.otherPcr = otherPcr;
    }

    public void setOtherGramStain(String otherGramStain) {
        this.otherGramStain = otherGramStain;
    }

    public void setOtherLatexAgglutination(String otherLatexAgglutination) {
        this.otherLatexAgglutination = otherLatexAgglutination;
    }

    public void setOtherCqValueDetection(String otherCqValueDetection) {
        this.otherCqValueDetection = otherCqValueDetection;
    }

    public void setOtherSeQuencing(String otherSeQuencing) {
        this.otherSeQuencing = otherSeQuencing;
    }

    public void setOtherDnaMicroArray(String otherDnaMicroArray) {
        this.otherDnaMicroArray = otherDnaMicroArray;
    }

    public void setOtherOther(String otherOther) {
        this.otherOther = otherOther;
    }

    public void setUndefinedAntiBodyDetection(String undefinedAntiBodyDetection) {
        this.undefinedAntiBodyDetection = undefinedAntiBodyDetection;
    }

    public void setUndefinedAntigenDetection(String undefinedAntigenDetection) {
        this.undefinedAntigenDetection = undefinedAntigenDetection;
    }

    public void setUndefinedRapidTest(String undefinedRapidTest) {
        this.undefinedRapidTest = undefinedRapidTest;
    }

    public void setUndefinedCulture(String undefinedCulture) {
        this.undefinedCulture = undefinedCulture;
    }

    public void setUndefinedHistopathology(String undefinedHistopathology) {
        this.undefinedHistopathology = undefinedHistopathology;
    }

    public void setUndefinedIsolation(String undefinedIsolation) {
        this.undefinedIsolation = undefinedIsolation;
    }

    public void setUndefinedIgmSerumAntibody(String undefinedIgmSerumAntibody) {
        this.undefinedIgmSerumAntibody = undefinedIgmSerumAntibody;
    }

    public void setUndefinedIggSerumAntibody(String undefinedIggSerumAntibody) {
        this.undefinedIggSerumAntibody = undefinedIggSerumAntibody;
    }

    public void setUndefinedIgaSerumAntibody(String undefinedIgaSerumAntibody) {
        this.undefinedIgaSerumAntibody = undefinedIgaSerumAntibody;
    }

    public void setUndefinedIncubationTime(String undefinedIncubationTime) {
        this.undefinedIncubationTime = undefinedIncubationTime;
    }

    public void setUndefinedIndirectFluorescentAntibody(String undefinedIndirectFluorescentAntibody) {
        this.undefinedIndirectFluorescentAntibody = undefinedIndirectFluorescentAntibody;
    }

    public void setUndefinedMicroscopy(String undefinedMicroscopy) {
        this.undefinedMicroscopy = undefinedMicroscopy;
    }

    public void setUndefinedNeutralizingAntibodies(String undefinedNeutralizingAntibodies) {
        this.undefinedNeutralizingAntibodies = undefinedNeutralizingAntibodies;
    }

    public void setUndefinedPcr(String undefinedPcr) {
        this.undefinedPcr = undefinedPcr;
    }

    public void setUndefinedGramStain(String undefinedGramStain) {
        this.undefinedGramStain = undefinedGramStain;
    }

    public void setUndefinedLatexAgglutination(String undefinedLatexAgglutination) {
        this.undefinedLatexAgglutination = undefinedLatexAgglutination;
    }

    public void setUndefinedCqValueDetection(String undefinedCqValueDetection) {
        this.undefinedCqValueDetection = undefinedCqValueDetection;
    }

    public void setUndefinedSeQuencing(String undefinedSeQuencing) {
        this.undefinedSeQuencing = undefinedSeQuencing;
    }

    public void setUndefinedDnaMicroArray(String undefinedDnaMicroArray) {
        this.undefinedDnaMicroArray = undefinedDnaMicroArray;
    }

    public void setUndefinedOther(String undefinedOther) {
        this.undefinedOther = undefinedOther;
    }

    @Order(149)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpAntiBodyDetection() {
        return afpAntiBodyDetection;
    }

    @Order(150)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpAntigenDetection() {
        return afpAntigenDetection;
    }

    @Order(151)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpRapidTest() {
        return afpRapidTest;
    }
    @Order(152)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpCulture() {
        return afpCulture;
    }

    @Order(153)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpHistopathology() {
        return afpHistopathology;
    }

    @Order(154)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpIsolation() {
        return afpIsolation;
    }

    @Order(155)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpIgmSerumAntibody() {
        return afpIgmSerumAntibody;
    }

    @Order(156)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpIggSerumAntibody() {
        return afpIggSerumAntibody;
    }

    @Order(157)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpIgaSerumAntibody() {
        return afpIgaSerumAntibody;
    }

    @Order(158)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpIncubationTime() {
        return afpIncubationTime;
    }

    @Order(159)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpIndirectFluorescentAntibody() {
        return afpIndirectFluorescentAntibody;
    }

    @Order(160)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpMicroscopy() {
        return afpMicroscopy;
    }

    @Order(161)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpNeutralizingAntibodies() {
        return afpNeutralizingAntibodies;
    }

    @Order(162)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpPcr() {
        return afpPcr;
    }

    @Order(163)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpGramStain() {
        return afpGramStain;
    }

    @Order(164)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpLatexAgglutination() {
        return afpLatexAgglutination;
    }

    @Order(165)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpCqValueDetection() {
        return afpCqValueDetection;
    }

    @Order(166)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpSeQuencing() {
        return afpSeQuencing;
    }

    @Order(167)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpDnaMicroArray() {
        return afpDnaMicroArray;
    }

    @Order(168)
    @ExportTarget(caseExportTypes = {
            CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAfpOther() {
        return afpOther;
    }

    @Order(169)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraAntiBodyDetection() {
        return choleraAntiBodyDetection;
    }

    @Order(170)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraAntigenDetection() {
        return choleraAntigenDetection;
    }

    @Order(171)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraRapidTest() {
        return choleraRapidTest;
    }

    @Order(172)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraCulture() {
        return choleraCulture;
    }

    @Order(173)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraHistopathology() {
        return choleraHistopathology;
    }

    @Order(174)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraIsolation() {
        return choleraIsolation;
    }

    @Order(175)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraIgmSerumAntibody() {
        return choleraIgmSerumAntibody;
    }

    @Order(176)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraIggSerumAntibody() {
        return choleraIggSerumAntibody;
    }

    @Order(177)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraIgaSerumAntibody() {
        return choleraIgaSerumAntibody;
    }

    @Order(178)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraIncubationTime() {
        return choleraIncubationTime;
    }

    @Order(179)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraIndirectFluorescentAntibody() {
        return choleraIndirectFluorescentAntibody;
    }

    @Order(180)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraMicroscopy() {
        return choleraMicroscopy;
    }

    @Order(181)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraNeutralizingAntibodies() {
        return choleraNeutralizingAntibodies;
    }

    @Order(182)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraPcr() {
        return choleraPcr;
    }

    @Order(183)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraGramStain() {
        return choleraGramStain;
    }

    @Order(184)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraLatexAgglutination() {
        return choleraLatexAgglutination;
    }

    @Order(185)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraCqValueDetection() {
        return choleraCqValueDetection;
    }

    @Order(186)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraSeQuencing() {
        return choleraSeQuencing;
    }

    @Order(187)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraDnaMicroArray() {
        return choleraDnaMicroArray;
    }

    @Order(188)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCholeraOther() {
        return choleraOther;
    }

    @Order(189)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaAntiBodyDetection() {
        return congenitalRubellaAntiBodyDetection;
    }

    @Order(190)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaAntigenDetection() {
        return congenitalRubellaAntigenDetection;
    }

    @Order(191)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaRapidTest() {
        return congenitalRubellaRapidTest;
    }

    @Order(192)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaCulture() {
        return congenitalRubellaCulture;
    }

    @Order(193)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaHistopathology() {
        return congenitalRubellaHistopathology;
    }

    @Order(194)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaIsolation() {
        return congenitalRubellaIsolation;
    }

    @Order(195)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaIgmSerumAntibody() {
        return congenitalRubellaIgmSerumAntibody;
    }

    @Order(196)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaIggSerumAntibody() {
        return congenitalRubellaIggSerumAntibody;
    }

    @Order(197)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaIgaSerumAntibody() {
        return congenitalRubellaIgaSerumAntibody;
    }

    @Order(198)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaIncubationTime() {
        return congenitalRubellaIncubationTime;
    }

    @Order(199)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaIndirectFluorescentAntibody() {
        return congenitalRubellaIndirectFluorescentAntibody;
    }

    @Order(200)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaMicroscopy() {
        return congenitalRubellaMicroscopy;
    }

    @Order(201)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaNeutralizingAntibodies() {
        return congenitalRubellaNeutralizingAntibodies;
    }

    @Order(202)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaPcr() {
        return congenitalRubellaPcr;
    }

    @Order(203)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaGramStain() {
        return congenitalRubellaGramStain;
    }

    @Order(204)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaLatexAgglutination() {
        return congenitalRubellaLatexAgglutination;
    }

    @Order(205)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaCqValueDetection() {
        return congenitalRubellaCqValueDetection;
    }

    @Order(206)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaSeQuencing() {
        return congenitalRubellaSeQuencing;
    }

    @Order(207)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaDnaMicroArray() {
        return congenitalRubellaDnaMicroArray;
    }

    @Order(208)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCongenitalRubellaOther() {
        return congenitalRubellaOther;
    }

    @Order(209)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmAntiBodyDetection() {
        return csmAntiBodyDetection;
    }

    @Order(210)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmAntigenDetection() {
        return csmAntigenDetection;
    }

    @Order(211)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmRapidTest() {
        return csmRapidTest;
    }

    @Order(212)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmCulture() {
        return csmCulture;
    }

    @Order(213)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmHistopathology() {
        return csmHistopathology;
    }

    @Order(214)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmIsolation() {
        return csmIsolation;
    }

    @Order(215)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmIgmSerumAntibody() {
        return csmIgmSerumAntibody;
    }

    @Order(216)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmIggSerumAntibody() {
        return csmIggSerumAntibody;
    }

    @Order(217)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmIgaSerumAntibody() {
        return csmIgaSerumAntibody;
    }

    @Order(218)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmIncubationTime() {
        return csmIncubationTime;
    }

    @Order(219)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmIndirectFluorescentAntibody() {
        return csmIndirectFluorescentAntibody;
    }

    @Order(220)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmMicroscopy() {
        return csmMicroscopy;
    }

    @Order(221)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmNeutralizingAntibodies() {
        return csmNeutralizingAntibodies;
    }

    @Order(222)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmPcr() {
        return csmPcr;
    }

    @Order(223)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmGramStain() {
        return csmGramStain;
    }

    @Order(224)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmLatexAgglutination() {
        return csmLatexAgglutination;
    }

    @Order(225)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmCqValueDetection() {
        return csmCqValueDetection;
    }

    @Order(226)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmSeQuencing() {
        return csmSeQuencing;
    }

    @Order(227)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmDnaMicroArray() {
        return csmDnaMicroArray;
    }

    @Order(228)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCsmOther() {
        return csmOther;
    }

    @Order(229)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDengueAntiBodyDetection() {
        return dengueAntiBodyDetection;
    }

    @Order(230)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDengueAntigenDetection() {
        return dengueAntigenDetection;
    }

    @Order(231)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDengueRapidTest() {
        return dengueRapidTest;
    }

    @Order(232)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDengueCulture() {
        return dengueCulture;
    }

    @Order(233)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDengueHistopathology() {
        return dengueHistopathology;
    }

    @Order(234)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDengueIsolation() {
        return dengueIsolation;
    }

    @Order(235)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDengueIgmSerumAntibody() {
        return dengueIgmSerumAntibody;
    }

    @Order(236)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDengueIggSerumAntibody() {
        return dengueIggSerumAntibody;
    }

    @Order(237)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDengueIgaSerumAntibody() {
        return dengueIgaSerumAntibody;
    }

    @Order(238)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDengueIncubationTime() {
        return dengueIncubationTime;
    }

    @Order(239)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDengueIndirectFluorescentAntibody() {
        return dengueIndirectFluorescentAntibody;
    }

    @Order(240)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDengueMicroscopy() {
        return dengueMicroscopy;
    }

    @Order(241)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDengueNeutralizingAntibodies() {
        return dengueNeutralizingAntibodies;
    }

    @Order(242)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDenguePcr() {
        return denguePcr;
    }

    @Order(243)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDengueGramStain() {
        return dengueGramStain;
    }

    @Order(244)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDengueLatexAgglutination() {
        return dengueLatexAgglutination;
    }

    @Order(245)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDengueCqValueDetection() {
        return dengueCqValueDetection;
    }

    @Order(246)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDengueSeQuencing() {
        return dengueSeQuencing;
    }

    @Order(247)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDengueDnaMicroArray() {
        return dengueDnaMicroArray;
    }

    @Order(248)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDengueOther() {
        return dengueOther;
    }

    @Order(249)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdAntiBodyDetection() {
        return evdAntiBodyDetection;
    }

    @Order(250)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdAntigenDetection() {
        return evdAntigenDetection;
    }

    @Order(251)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdRapidTest() {
        return evdRapidTest;
    }

    @Order(252)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdCulture() {
        return evdCulture;
    }

    @Order(253)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdHistopathology() {
        return evdHistopathology;
    }

    @Order(254)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdIsolation() {
        return evdIsolation;
    }

    @Order(255)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdIgmSerumAntibody() {
        return evdIgmSerumAntibody;
    }

    @Order(256)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdIggSerumAntibody() {
        return evdIggSerumAntibody;
    }

    @Order(257)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdIgaSerumAntibody() {
        return evdIgaSerumAntibody;
    }

    @Order(258)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdIncubationTime() {
        return evdIncubationTime;
    }

    @Order(259)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdIndirectFluorescentAntibody() {
        return evdIndirectFluorescentAntibody;
    }

    @Order(260)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdMicroscopy() {
        return evdMicroscopy;
    }

    @Order(261)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdNeutralizingAntibodies() {
        return evdNeutralizingAntibodies;
    }

    @Order(262)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdPcr() {
        return evdPcr;
    }

    @Order(263)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdGramStain() {
        return evdGramStain;
    }

    @Order(264)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdLatexAgglutination() {
        return evdLatexAgglutination;
    }

    @Order(265)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdCqValueDetection() {
        return evdCqValueDetection;
    }

    @Order(266)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdSeQuencing() {
        return evdSeQuencing;
    }

    @Order(267)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdDnaMicroArray() {
        return evdDnaMicroArray;
    }

    @Order(268)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEvdOther() {
        return evdOther;
    }
    @Order(269)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormAntiBodyDetection() {
        return guineaWormAntiBodyDetection;
    }

    @Order(270)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormAntigenDetection() {
        return guineaWormAntigenDetection;
    }

    @Order(271)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormRapidTest() {
        return guineaWormRapidTest;
    }

    @Order(272)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormCulture() {
        return guineaWormCulture;
    }

    @Order(273)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormHistopathology() {
        return guineaWormHistopathology;
    }

    @Order(274)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormIsolation() {
        return guineaWormIsolation;
    }

    @Order(275)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormIgmSerumAntibody() {
        return guineaWormIgmSerumAntibody;
    }

    @Order(276)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormIggSerumAntibody() {
        return guineaWormIggSerumAntibody;
    }

    @Order(277)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormIgaSerumAntibody() {
        return guineaWormIgaSerumAntibody;
    }

    @Order(278)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormIncubationTime() {
        return guineaWormIncubationTime;
    }

    @Order(279)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormIndirectFluorescentAntibody() {
        return guineaWormIndirectFluorescentAntibody;
    }

    @Order(280)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormMicroscopy() {
        return guineaWormMicroscopy;
    }

    @Order(281)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormNeutralizingAntibodies() {
        return guineaWormNeutralizingAntibodies;
    }

    @Order(282)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormPcr() {
        return guineaWormPcr;
    }

    @Order(283)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormGramStain() {
        return guineaWormGramStain;
    }

    @Order(284)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormLatexAgglutination() {
        return guineaWormLatexAgglutination;
    }

    @Order(285)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormCqValueDetection() {
        return guineaWormCqValueDetection;
    }

    @Order(286)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormSeQuencing() {
        return guineaWormSeQuencing;
    }

    @Order(287)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormDnaMicroArray() {
        return guineaWormDnaMicroArray;
    }

    @Order(288)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getGuineaWormOther() {
        return guineaWormOther;
    }

    @Order(289)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaAntiBodyDetection() {
        return lassaAntiBodyDetection;
    }

    @Order(290)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaAntigenDetection() {
        return lassaAntigenDetection;
    }

    @Order(291)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaRapidTest() {
        return lassaRapidTest;
    }

    @Order(292)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaCulture() {
        return lassaCulture;
    }

    @Order(293)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaHistopathology() {
        return lassaHistopathology;
    }

    @Order(294)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaIsolation() {
        return lassaIsolation;
    }

    @Order(295)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaIgmSerumAntibody() {
        return lassaIgmSerumAntibody;
    }

    @Order(296)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaIggSerumAntibody() {
        return lassaIggSerumAntibody;
    }

    @Order(297)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaIgaSerumAntibody() {
        return lassaIgaSerumAntibody;
    }

    @Order(298)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaIncubationTime() {
        return lassaIncubationTime;
    }

    @Order(299)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaIndirectFluorescentAntibody() {
        return lassaIndirectFluorescentAntibody;
    }

    @Order(300)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaMicroscopy() {
        return lassaMicroscopy;
    }

    @Order(301)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaNeutralizingAntibodies() {
        return lassaNeutralizingAntibodies;
    }

    @Order(302)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaPcr() {
        return lassaPcr;
    }

    @Order(303)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaGramStain() {
        return lassaGramStain;
    }

    @Order(304)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaLatexAgglutination() {
        return lassaLatexAgglutination;
    }

    @Order(305)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaCqValueDetection() {
        return lassaCqValueDetection;
    }

    @Order(306)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaSeQuencing() {
        return lassaSeQuencing;
    }

    @Order(307)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaDnaMicroArray() {
        return lassaDnaMicroArray;
    }

    @Order(308)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLassaOther() {
        return lassaOther;
    }

    @Order(309)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesAntiBodyDetection() {
        return measlesAntiBodyDetection;
    }

    @Order(310)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesAntigenDetection() {
        return measlesAntigenDetection;
    }

    @Order(311)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesRapidTest() {
        return measlesRapidTest;
    }

    @Order(312)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesCulture() {
        return measlesCulture;
    }

    @Order(313)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesHistopathology() {
        return measlesHistopathology;
    }

    @Order(314)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesIsolation() {
        return measlesIsolation;
    }

    @Order(315)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesIgmSerumAntibody() {
        return measlesIgmSerumAntibody;
    }

    @Order(316)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesIggSerumAntibody() {
        return measlesIggSerumAntibody;
    }

    @Order(317)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesIgaSerumAntibody() {
        return measlesIgaSerumAntibody;
    }

    @Order(318)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesIncubationTime() {
        return measlesIncubationTime;
    }

    @Order(319)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesIndirectFluorescentAntibody() {
        return measlesIndirectFluorescentAntibody;
    }

    @Order(320)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesMicroscopy() {
        return measlesMicroscopy;
    }

    @Order(321)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesNeutralizingAntibodies() {
        return measlesNeutralizingAntibodies;
    }

    @Order(322)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesPcr() {
        return measlesPcr;
    }

    @Order(323)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesGramStain() {
        return measlesGramStain;
    }

    @Order(324)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesLatexAgglutination() {
        return measlesLatexAgglutination;
    }

    @Order(325)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesCqValueDetection() {
        return measlesCqValueDetection;
    }

    @Order(326)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesSeQuencing() {
        return measlesSeQuencing;
    }

    @Order(327)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesDnaMicroArray() {
        return measlesDnaMicroArray;
    }

    @Order(328)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMeaslesOther() {
        return measlesOther;
    }

    @Order(329)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxAntiBodyDetection() {
        return monkeyPoxAntiBodyDetection;
    }

    @Order(330)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxAntigenDetection() {
        return monkeyPoxAntigenDetection;
    }

    @Order(331)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxRapidTest() {
        return monkeyPoxRapidTest;
    }

    @Order(332)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxCulture() {
        return monkeyPoxCulture;
    }

    @Order(333)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxHistopathology() {
        return monkeyPoxHistopathology;
    }

    @Order(334)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxIsolation() {
        return monkeyPoxIsolation;
    }

    @Order(335)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxIgmSerumAntibody() {
        return monkeyPoxIgmSerumAntibody;
    }

    @Order(336)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxIggSerumAntibody() {
        return monkeyPoxIggSerumAntibody;
    }

    @Order(337)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxIgaSerumAntibody() {
        return monkeyPoxIgaSerumAntibody;
    }

    @Order(338)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxIncubationTime() {
        return monkeyPoxIncubationTime;
    }

    @Order(339)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxIndirectFluorescentAntibody() {
        return monkeyPoxIndirectFluorescentAntibody;
    }

    @Order(340)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxMicroscopy() {
        return monkeyPoxMicroscopy;
    }

    @Order(341)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxNeutralizingAntibodies() {
        return monkeyPoxNeutralizingAntibodies;
    }

    @Order(342)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxPcr() {
        return monkeyPoxPcr;
    }

    @Order(343)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxGramStain() {
        return monkeyPoxGramStain;
    }

    @Order(344)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxLatexAgglutination() {
        return monkeyPoxLatexAgglutination;
    }

    @Order(345)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxCqValueDetection() {
        return monkeyPoxCqValueDetection;
    }

    @Order(346)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxSeQuencing() {
        return monkeyPoxSeQuencing;
    }

    @Order(347)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxDnaMicroArray() {
        return monkeyPoxDnaMicroArray;
    }

    @Order(348)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMonkeyPoxOther() {
        return monkeyPoxOther;
    }

    public String getMonkeypoxAntiBodyDetection() {
        return monkeypoxAntiBodyDetection;
    }

    public String getMonkeypoxAntigenDetection() {
        return monkeypoxAntigenDetection;
    }

    public String getMonkeypoxRapidTest() {
        return monkeypoxRapidTest;
    }

    public String getMonkeypoxCulture() {
        return monkeypoxCulture;
    }

    public String getMonkeypoxHistopathology() {
        return monkeypoxHistopathology;
    }

    public String getMonkeypoxIsolation() {
        return monkeypoxIsolation;
    }

    public String getMonkeypoxIgmSerumAntibody() {
        return monkeypoxIgmSerumAntibody;
    }

    public String getMonkeypoxIggSerumAntibody() {
        return monkeypoxIggSerumAntibody;
    }

    public String getMonkeypoxIgaSerumAntibody() {
        return monkeypoxIgaSerumAntibody;
    }

    public String getMonkeypoxIncubationTime() {
        return monkeypoxIncubationTime;
    }

    public String getMonkeypoxIndirectFluorescentAntibody() {
        return monkeypoxIndirectFluorescentAntibody;
    }

    public String getMonkeypoxMicroscopy() {
        return monkeypoxMicroscopy;
    }

    public String getMonkeypoxNeutralizingAntibodies() {
        return monkeypoxNeutralizingAntibodies;
    }

    public String getMonkeypoxPcr() {
        return monkeypoxPcr;
    }

    public String getMonkeypoxGramStain() {
        return monkeypoxGramStain;
    }

    public String getMonkeypoxLatexAgglutination() {
        return monkeypoxLatexAgglutination;
    }

    public String getMonkeypoxCqValueDetection() {
        return monkeypoxCqValueDetection;
    }

    public String getMonkeypoxSeQuencing() {
        return monkeypoxSeQuencing;
    }

    public String getMonkeypoxDnaMicroArray() {
        return monkeypoxDnaMicroArray;
    }

    public String getMonkeypoxOther() {
        return monkeypoxOther;
    }

    @Order(349)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaAntiBodyDetection() {
        return newInfluenzaAntiBodyDetection;
    }

    @Order(350)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaAntigenDetection() {
        return newInfluenzaAntigenDetection;
    }

    @Order(351)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaRapidTest() {
        return newInfluenzaRapidTest;
    }

    @Order(352)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaCulture() {
        return newInfluenzaCulture;
    }

    @Order(353)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaHistopathology() {
        return newInfluenzaHistopathology;
    }

    @Order(354)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaIsolation() {
        return newInfluenzaIsolation;
    }

    @Order(355)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaIgmSerumAntibody() {
        return newInfluenzaIgmSerumAntibody;
    }

    @Order(356)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaIggSerumAntibody() {
        return newInfluenzaIggSerumAntibody;
    }

    @Order(357)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaIgaSerumAntibody() {
        return newInfluenzaIgaSerumAntibody;
    }

    @Order(358)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaIncubationTime() {
        return newInfluenzaIncubationTime;
    }

    @Order(359)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaIndirectFluorescentAntibody() {
        return newInfluenzaIndirectFluorescentAntibody;
    }

    @Order(360)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaMicroscopy() {
        return newInfluenzaMicroscopy;
    }

    @Order(361)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaNeutralizingAntibodies() {
        return newInfluenzaNeutralizingAntibodies;
    }

    @Order(362)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaPcr() {
        return newInfluenzaPcr;
    }

    @Order(363)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaGramStain() {
        return newInfluenzaGramStain;
    }

    @Order(364)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaLatexAgglutination() {
        return newInfluenzaLatexAgglutination;
    }

    @Order(365)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaCqValueDetection() {
        return newInfluenzaCqValueDetection;
    }

    @Order(366)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaSeQuencing() {
        return newInfluenzaSeQuencing;
    }

    @Order(367)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaDnaMicroArray() {
        return newInfluenzaDnaMicroArray;
    }

    @Order(368)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNewInfluenzaOther() {
        return newInfluenzaOther;
    }

    @Order(369)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlagueAntiBodyDetection() {
        return plagueAntiBodyDetection;
    }

    @Order(370)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlagueAntigenDetection() {
        return plagueAntigenDetection;
    }

    @Order(371)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlagueRapidTest() {
        return plagueRapidTest;
    }

    @Order(372)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlagueCulture() {
        return plagueCulture;
    }

    @Order(373)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlagueHistopathology() {
        return plagueHistopathology;
    }

    @Order(374)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlagueIsolation() {
        return plagueIsolation;
    }

    @Order(375)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlagueIgmSerumAntibody() {
        return plagueIgmSerumAntibody;
    }

    @Order(376)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlagueIggSerumAntibody() {
        return plagueIggSerumAntibody;
    }

    @Order(377)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlagueIgaSerumAntibody() {
        return plagueIgaSerumAntibody;
    }

    @Order(378)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlagueIncubationTime() {
        return plagueIncubationTime;
    }

    @Order(379)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlagueIndirectFluorescentAntibody() {
        return plagueIndirectFluorescentAntibody;
    }

    @Order(380)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlagueMicroscopy() {
        return plagueMicroscopy;
    }

    @Order(381)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlagueNeutralizingAntibodies() {
        return plagueNeutralizingAntibodies;
    }

    @Order(382)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlaguePcr() {
        return plaguePcr;
    }

    @Order(383)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlagueGramStain() {
        return plagueGramStain;
    }

    @Order(384)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlagueLatexAgglutination() {
        return plagueLatexAgglutination;
    }

    @Order(385)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlagueCqValueDetection() {
        return plagueCqValueDetection;
    }

    @Order(386)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlagueSeQuencing() {
        return plagueSeQuencing;
    }

    @Order(387)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlagueDnaMicroArray() {
        return plagueDnaMicroArray;
    }

    @Order(388)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPlagueOther() {
        return plagueOther;
    }

    @Order(389)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioAntiBodyDetection() {
        return polioAntiBodyDetection;
    }

    @Order(390)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioAntigenDetection() {
        return polioAntigenDetection;
    }

    @Order(391)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioRapidTest() {
        return polioRapidTest;
    }

    @Order(392)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioCulture() {
        return polioCulture;
    }

    @Order(393)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioHistopathology() {
        return polioHistopathology;
    }

    @Order(394)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioIsolation() {
        return polioIsolation;
    }

    @Order(395)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioIgmSerumAntibody() {
        return polioIgmSerumAntibody;
    }

    @Order(396)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioIggSerumAntibody() {
        return polioIggSerumAntibody;
    }

    @Order(397)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioIgaSerumAntibody() {
        return polioIgaSerumAntibody;
    }

    @Order(398)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioIncubationTime() {
        return polioIncubationTime;
    }

    @Order(399)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioIndirectFluorescentAntibody() {
        return polioIndirectFluorescentAntibody;
    }

    @Order(400)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioMicroscopy() {
        return polioMicroscopy;
    }

    @Order(401)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioNeutralizingAntibodies() {
        return polioNeutralizingAntibodies;
    }

    @Order(402)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioPcr() {
        return polioPcr;
    }

    @Order(403)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioGramStain() {
        return polioGramStain;
    }

    @Order(404)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioLatexAgglutination() {
        return polioLatexAgglutination;
    }

    @Order(405)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioCqValueDetection() {
        return polioCqValueDetection;
    }

    @Order(406)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioSeQuencing() {
        return polioSeQuencing;
    }

    @Order(407)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioDnaMicroArray() {
        return polioDnaMicroArray;
    }

    @Order(408)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPolioOther() {
        return polioOther;
    }

    @Order(409)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfAntiBodyDetection() {
        return unspecifiedVhfAntiBodyDetection;
    }

    @Order(410)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfAntigenDetection() {
        return unspecifiedVhfAntigenDetection;
    }

    @Order(411)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfRapidTest() {
        return unspecifiedVhfRapidTest;
    }

    @Order(412)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfCulture() {
        return unspecifiedVhfCulture;
    }

    @Order(413)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfHistopathology() {
        return unspecifiedVhfHistopathology;
    }

    @Order(414)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfIsolation() {
        return unspecifiedVhfIsolation;
    }

    @Order(415)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfIgmSerumAntibody() {
        return unspecifiedVhfIgmSerumAntibody;
    }

    @Order(416)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfIggSerumAntibody() {
        return unspecifiedVhfIggSerumAntibody;
    }

    @Order(417)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfIgaSerumAntibody() {
        return unspecifiedVhfIgaSerumAntibody;
    }

    @Order(418)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfIncubationTime() {
        return unspecifiedVhfIncubationTime;
    }

    @Order(419)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfIndirectFluorescentAntibody() {
        return unspecifiedVhfIndirectFluorescentAntibody;
    }

    @Order(420)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfMicroscopy() {
        return unspecifiedVhfMicroscopy;
    }

    @Order(421)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfNeutralizingAntibodies() {
        return unspecifiedVhfNeutralizingAntibodies;
    }

    @Order(422)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfPcr() {
        return unspecifiedVhfPcr;
    }

    @Order(423)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfGramStain() {
        return unspecifiedVhfGramStain;
    }

    @Order(424)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfLatexAgglutination() {
        return unspecifiedVhfLatexAgglutination;
    }

    @Order(425)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfCqValueDetection() {
        return unspecifiedVhfCqValueDetection;
    }

    @Order(426)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfSeQuencing() {
        return unspecifiedVhfSeQuencing;
    }

    @Order(427)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfDnaMicroArray() {
        return unspecifiedVhfDnaMicroArray;
    }

    @Order(428)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUnspecifiedVhfOther() {
        return unspecifiedVhfOther;
    }

    @Order(429)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverAntiBodyDetection() {
        return yellowFeverAntiBodyDetection;
    }

    @Order(430)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverAntigenDetection() {
        return yellowFeverAntigenDetection;
    }

    @Order(431)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverRapidTest() {
        return yellowFeverRapidTest;
    }

    @Order(432)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverCulture() {
        return yellowFeverCulture;
    }

    @Order(433)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverHistopathology() {
        return yellowFeverHistopathology;
    }

    @Order(434)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverIsolation() {
        return yellowFeverIsolation;
    }

    @Order(435)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverIgmSerumAntibody() {
        return yellowFeverIgmSerumAntibody;
    }

    @Order(436)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverIggSerumAntibody() {
        return yellowFeverIggSerumAntibody;
    }

    @Order(437)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverIgaSerumAntibody() {
        return yellowFeverIgaSerumAntibody;
    }

    @Order(438)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverIncubationTime() {
        return yellowFeverIncubationTime;
    }

    @Order(439)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverIndirectFluorescentAntibody() {
        return yellowFeverIndirectFluorescentAntibody;
    }

    @Order(440)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverMicroscopy() {
        return yellowFeverMicroscopy;
    }

    @Order(441)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverNeutralizingAntibodies() {
        return yellowFeverNeutralizingAntibodies;
    }

    @Order(442)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverPcr() {
        return yellowFeverPcr;
    }

    @Order(443)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverGramStain() {
        return yellowFeverGramStain;
    }

    @Order(444)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverLatexAgglutination() {
        return yellowFeverLatexAgglutination;
    }

    @Order(445)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverCqValueDetection() {
        return yellowFeverCqValueDetection;
    }

    @Order(446)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverSeQuencing() {
        return yellowFeverSeQuencing;
    }

    @Order(447)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverDnaMicroArray() {
        return yellowFeverDnaMicroArray;
    }

    @Order(448)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYellowFeverOther() {
        return yellowFeverOther;
    }

    @Order(449)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesAntiBodyDetection() {
        return rabiesAntiBodyDetection;
    }

    @Order(450)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesAntigenDetection() {
        return rabiesAntigenDetection;
    }

    @Order(451)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesRapidTest() {
        return rabiesRapidTest;
    }

    @Order(452)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesCulture() {
        return rabiesCulture;
    }

    @Order(453)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesHistopathology() {
        return rabiesHistopathology;
    }

    @Order(454)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesIsolation() {
        return rabiesIsolation;
    }

    @Order(455)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesIgmSerumAntibody() {
        return rabiesIgmSerumAntibody;
    }

    @Order(456)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesIggSerumAntibody() {
        return rabiesIggSerumAntibody;
    }

    @Order(457)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesIgaSerumAntibody() {
        return rabiesIgaSerumAntibody;
    }

    @Order(458)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesIncubationTime() {
        return rabiesIncubationTime;
    }

    @Order(459)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesIndirectFluorescentAntibody() {
        return rabiesIndirectFluorescentAntibody;
    }

    @Order(460)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesMicroscopy() {
        return rabiesMicroscopy;
    }

    @Order(461)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesNeutralizingAntibodies() {
        return rabiesNeutralizingAntibodies;
    }

    @Order(462)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesPcr() {
        return rabiesPcr;
    }

    @Order(463)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesGramStain() {
        return rabiesGramStain;
    }

    @Order(464)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesLatexAgglutination() {
        return rabiesLatexAgglutination;
    }

    @Order(465)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesCqValueDetection() {
        return rabiesCqValueDetection;
    }

    @Order(466)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesSeQuencing() {
        return rabiesSeQuencing;
    }

    @Order(467)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesDnaMicroArray() {
        return rabiesDnaMicroArray;
    }

    @Order(468)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRabiesOther() {
        return rabiesOther;
    }

    @Order(469)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxAntiBodyDetection() {
        return anthraxAntiBodyDetection;
    }

    @Order(470)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxAntigenDetection() {
        return anthraxAntigenDetection;
    }

    @Order(471)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxRapidTest() {
        return anthraxRapidTest;
    }

    @Order(472)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxCulture() {
        return anthraxCulture;
    }

    @Order(473)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxHistopathology() {
        return anthraxHistopathology;
    }

    @Order(474)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxIsolation() {
        return anthraxIsolation;
    }

    @Order(475)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxIgmSerumAntibody() {
        return anthraxIgmSerumAntibody;
    }

    @Order(476)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxIggSerumAntibody() {
        return anthraxIggSerumAntibody;
    }

    @Order(477)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxIgaSerumAntibody() {
        return anthraxIgaSerumAntibody;
    }

    @Order(478)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxIncubationTime() {
        return anthraxIncubationTime;
    }

    @Order(479)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxIndirectFluorescentAntibody() {
        return anthraxIndirectFluorescentAntibody;
    }

    @Order(480)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxMicroscopy() {
        return anthraxMicroscopy;
    }

    @Order(481)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxNeutralizingAntibodies() {
        return anthraxNeutralizingAntibodies;
    }

    @Order(482)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxPcr() {
        return anthraxPcr;
    }

    @Order(483)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxGramStain() {
        return anthraxGramStain;
    }

    @Order(484)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxLatexAgglutination() {
        return anthraxLatexAgglutination;
    }

    @Order(485)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxCqValueDetection() {
        return anthraxCqValueDetection;
    }

    @Order(486)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxSeQuencing() {
        return anthraxSeQuencing;
    }

    @Order(487)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxDnaMicroArray() {
        return anthraxDnaMicroArray;
    }

    @Order(488)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAnthraxOther() {
        return anthraxOther;
    }

    @Order(489)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusAntiBodyDetection() {
        return coronavirusAntiBodyDetection;
    }

    @Order(490)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusAntigenDetection() {
        return coronavirusAntigenDetection;
    }

    @Order(491)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusRapidTest() {
        return coronavirusRapidTest;
    }

    @Order(492)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusCulture() {
        return coronavirusCulture;
    }

    @Order(493)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusHistopathology() {
        return coronavirusHistopathology;
    }

    @Order(494)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusIsolation() {
        return coronavirusIsolation;
    }

    @Order(495)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusIgmSerumAntibody() {
        return coronavirusIgmSerumAntibody;
    }

    @Order(496)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusIggSerumAntibody() {
        return coronavirusIggSerumAntibody;
    }

    @Order(497)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusIgaSerumAntibody() {
        return coronavirusIgaSerumAntibody;
    }

    @Order(498)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusIncubationTime() {
        return coronavirusIncubationTime;
    }

    @Order(499)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusIndirectFluorescentAntibody() {
        return coronavirusIndirectFluorescentAntibody;
    }

    @Order(500)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusMicroscopy() {
        return coronavirusMicroscopy;
    }

    @Order(501)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusNeutralizingAntibodies() {
        return coronavirusNeutralizingAntibodies;
    }

    @Order(502)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusPcr() {
        return coronavirusPcr;
    }

    @Order(503)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusGramStain() {
        return coronavirusGramStain;
    }

    @Order(504)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusLatexAgglutination() {
        return coronavirusLatexAgglutination;
    }

    @Order(505)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusCqValueDetection() {
        return coronavirusCqValueDetection;
    }

    @Order(506)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusSeQuencing() {
        return coronavirusSeQuencing;
    }

    @Order(507)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusDnaMicroArray() {
        return coronavirusDnaMicroArray;
    }

    @Order(508)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getCoronavirusOther() {
        return coronavirusOther;
    }

    @Order(509)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaAntiBodyDetection() {
        return pneumoniaAntiBodyDetection;
    }

    @Order(510)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaAntigenDetection() {
        return pneumoniaAntigenDetection;
    }

    @Order(511)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaRapidTest() {
        return pneumoniaRapidTest;
    }

    @Order(512)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaCulture() {
        return pneumoniaCulture;
    }

    @Order(513)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaHistopathology() {
        return pneumoniaHistopathology;
    }

    @Order(514)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaIsolation() {
        return pneumoniaIsolation;
    }

    @Order(515)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaIgmSerumAntibody() {
        return pneumoniaIgmSerumAntibody;
    }

    @Order(516)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaIggSerumAntibody() {
        return pneumoniaIggSerumAntibody;
    }

    @Order(517)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaIgaSerumAntibody() {
        return pneumoniaIgaSerumAntibody;
    }

    @Order(518)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaIncubationTime() {
        return pneumoniaIncubationTime;
    }

    @Order(519)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaIndirectFluorescentAntibody() {
        return pneumoniaIndirectFluorescentAntibody;
    }

    @Order(520)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaMicroscopy() {
        return pneumoniaMicroscopy;
    }

    @Order(521)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaNeutralizingAntibodies() {
        return pneumoniaNeutralizingAntibodies;
    }

    @Order(522)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaPcr() {
        return pneumoniaPcr;
    }

    @Order(523)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaGramStain() {
        return pneumoniaGramStain;
    }

    @Order(524)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaLatexAgglutination() {
        return pneumoniaLatexAgglutination;
    }

    @Order(525)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaCqValueDetection() {
        return pneumoniaCqValueDetection;
    }

    @Order(526)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaSeQuencing() {
        return pneumoniaSeQuencing;
    }

    @Order(527)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaDnaMicroArray() {
        return pneumoniaDnaMicroArray;
    }

    @Order(528)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPneumoniaOther() {
        return pneumoniaOther;
    }

    @Order(529)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaAntiBodyDetection() {
        return malariaAntiBodyDetection;
    }

    @Order(530)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaAntigenDetection() {
        return malariaAntigenDetection;
    }

    @Order(531)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaRapidTest() {
        return malariaRapidTest;
    }

    @Order(532)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaCulture() {
        return malariaCulture;
    }

    @Order(533)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaHistopathology() {
        return malariaHistopathology;
    }

    @Order(534)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaIsolation() {
        return malariaIsolation;
    }

    @Order(535)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaIgmSerumAntibody() {
        return malariaIgmSerumAntibody;
    }

    @Order(536)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaIggSerumAntibody() {
        return malariaIggSerumAntibody;
    }

    @Order(537)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaIgaSerumAntibody() {
        return malariaIgaSerumAntibody;
    }

    @Order(538)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaIncubationTime() {
        return malariaIncubationTime;
    }

    @Order(539)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaIndirectFluorescentAntibody() {
        return malariaIndirectFluorescentAntibody;
    }

    @Order(540)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaMicroscopy() {
        return malariaMicroscopy;
    }

    @Order(541)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaNeutralizingAntibodies() {
        return malariaNeutralizingAntibodies;
    }

    @Order(542)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaPcr() {
        return malariaPcr;
    }

    @Order(543)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaGramStain() {
        return malariaGramStain;
    }

    @Order(544)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaLatexAgglutination() {
        return malariaLatexAgglutination;
    }

    @Order(545)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaCqValueDetection() {
        return malariaCqValueDetection;
    }

    @Order(546)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaSeQuencing() {
        return malariaSeQuencing;
    }

    @Order(547)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaDnaMicroArray() {
        return malariaDnaMicroArray;
    }

    @Order(548)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMalariaOther() {
        return malariaOther;
    }

    @Order(549)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverAntiBodyDetection() {
        return typhoidFeverAntiBodyDetection;
    }

    @Order(550)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverAntigenDetection() {
        return typhoidFeverAntigenDetection;
    }

    @Order(551)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverRapidTest() {
        return typhoidFeverRapidTest;
    }

    @Order(552)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverCulture() {
        return typhoidFeverCulture;
    }

    @Order(553)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverHistopathology() {
        return typhoidFeverHistopathology;
    }

    @Order(554)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverIsolation() {
        return typhoidFeverIsolation;
    }

    @Order(555)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverIgmSerumAntibody() {
        return typhoidFeverIgmSerumAntibody;
    }

    @Order(556)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverIggSerumAntibody() {
        return typhoidFeverIggSerumAntibody;
    }

    @Order(557)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverIgaSerumAntibody() {
        return typhoidFeverIgaSerumAntibody;
    }

    @Order(558)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverIncubationTime() {
        return typhoidFeverIncubationTime;
    }

    @Order(559)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverIndirectFluorescentAntibody() {
        return typhoidFeverIndirectFluorescentAntibody;
    }

    @Order(560)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverMicroscopy() {
        return typhoidFeverMicroscopy;
    }

    @Order(561)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverNeutralizingAntibodies() {
        return typhoidFeverNeutralizingAntibodies;
    }

    @Order(562)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverPcr() {
        return typhoidFeverPcr;
    }

    @Order(563)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverGramStain() {
        return typhoidFeverGramStain;
    }

    @Order(564)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverLatexAgglutination() {
        return typhoidFeverLatexAgglutination;
    }

    @Order(565)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverCqValueDetection() {
        return typhoidFeverCqValueDetection;
    }

    @Order(566)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverSeQuencing() {
        return typhoidFeverSeQuencing;
    }

    @Order(567)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverDnaMicroArray() {
        return typhoidFeverDnaMicroArray;
    }

    @Order(568)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTyphoidFeverOther() {
        return typhoidFeverOther;
    }

    @Order(569)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisAntiBodyDetection() {
        return acuteViralHepatitisAntiBodyDetection;
    }

    @Order(570)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisAntigenDetection() {
        return acuteViralHepatitisAntigenDetection;
    }

    @Order(571)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisRapidTest() {
        return acuteViralHepatitisRapidTest;
    }

    @Order(572)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisCulture() {
        return acuteViralHepatitisCulture;
    }

    @Order(573)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisHistopathology() {
        return acuteViralHepatitisHistopathology;
    }

    @Order(574)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisIsolation() {
        return acuteViralHepatitisIsolation;
    }

    @Order(575)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisIgmSerumAntibody() {
        return acuteViralHepatitisIgmSerumAntibody;
    }

    @Order(576)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisIggSerumAntibody() {
        return acuteViralHepatitisIggSerumAntibody;
    }

    @Order(577)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisIgaSerumAntibody() {
        return acuteViralHepatitisIgaSerumAntibody;
    }

    @Order(578)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisIncubationTime() {
        return acuteViralHepatitisIncubationTime;
    }

    @Order(579)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisIndirectFluorescentAntibody() {
        return acuteViralHepatitisIndirectFluorescentAntibody;
    }

    @Order(580)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisMicroscopy() {
        return acuteViralHepatitisMicroscopy;
    }

    @Order(581)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisNeutralizingAntibodies() {
        return acuteViralHepatitisNeutralizingAntibodies;
    }

    @Order(582)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisPcr() {
        return acuteViralHepatitisPcr;
    }

    @Order(583)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisGramStain() {
        return acuteViralHepatitisGramStain;
    }

    @Order(584)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisLatexAgglutination() {
        return acuteViralHepatitisLatexAgglutination;
    }

    @Order(585)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisCqValueDetection() {
        return acuteViralHepatitisCqValueDetection;
    }

    @Order(586)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisSeQuencing() {
        return acuteViralHepatitisSeQuencing;
    }

    @Order(587)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisDnaMicroArray() {
        return acuteViralHepatitisDnaMicroArray;
    }

    @Order(588)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAcuteViralHepatitisOther() {
        return acuteViralHepatitisOther;
    }

    @Order(589)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusAntiBodyDetection() {
        return nonNeonatalTetanusAntiBodyDetection;
    }

    @Order(590)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusAntigenDetection() {
        return nonNeonatalTetanusAntigenDetection;
    }

    @Order(591)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusRapidTest() {
        return nonNeonatalTetanusRapidTest;
    }

    @Order(592)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusCulture() {
        return nonNeonatalTetanusCulture;
    }

    @Order(593)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusHistopathology() {
        return nonNeonatalTetanusHistopathology;
    }

    @Order(594)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusIsolation() {
        return nonNeonatalTetanusIsolation;
    }

    @Order(595)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusIgmSerumAntibody() {
        return nonNeonatalTetanusIgmSerumAntibody;
    }

    @Order(596)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusIggSerumAntibody() {
        return nonNeonatalTetanusIggSerumAntibody;
    }

    @Order(597)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusIgaSerumAntibody() {
        return nonNeonatalTetanusIgaSerumAntibody;
    }

    @Order(598)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusIncubationTime() {
        return nonNeonatalTetanusIncubationTime;
    }

    @Order(599)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusIndirectFluorescentAntibody() {
        return nonNeonatalTetanusIndirectFluorescentAntibody;
    }

    @Order(600)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusMicroscopy() {
        return nonNeonatalTetanusMicroscopy;
    }

    @Order(601)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusNeutralizingAntibodies() {
        return nonNeonatalTetanusNeutralizingAntibodies;
    }

    @Order(602)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusPcr() {
        return nonNeonatalTetanusPcr;
    }

    @Order(603)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusGramStain() {
        return nonNeonatalTetanusGramStain;
    }

    @Order(604)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusLatexAgglutination() {
        return nonNeonatalTetanusLatexAgglutination;
    }

    @Order(605)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusCqValueDetection() {
        return nonNeonatalTetanusCqValueDetection;
    }

    @Order(606)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusSeQuencing() {
        return nonNeonatalTetanusSeQuencing;
    }

    @Order(607)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusDnaMicroArray() {
        return nonNeonatalTetanusDnaMicroArray;
    }

    @Order(608)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNonNeonatalTetanusOther() {
        return nonNeonatalTetanusOther;
    }

    @Order(609)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivAntiBodyDetection() {
        return hivAntiBodyDetection;
    }

    @Order(610)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivAntigenDetection() {
        return hivAntigenDetection;
    }

    @Order(611)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivRapidTest() {
        return hivRapidTest;
    }

    @Order(612)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivCulture() {
        return hivCulture;
    }

    @Order(613)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivHistopathology() {
        return hivHistopathology;
    }

    @Order(614)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivIsolation() {
        return hivIsolation;
    }

    @Order(615)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivIgmSerumAntibody() {
        return hivIgmSerumAntibody;
    }

    @Order(616)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivIggSerumAntibody() {
        return hivIggSerumAntibody;
    }

    @Order(617)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivIgaSerumAntibody() {
        return hivIgaSerumAntibody;
    }

    @Order(618)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivIncubationTime() {
        return hivIncubationTime;
    }

    @Order(619)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivIndirectFluorescentAntibody() {
        return hivIndirectFluorescentAntibody;
    }

    @Order(620)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivMicroscopy() {
        return hivMicroscopy;
    }

    @Order(621)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivNeutralizingAntibodies() {
        return hivNeutralizingAntibodies;
    }

    @Order(622)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivPcr() {
        return hivPcr;
    }

    @Order(623)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivGramStain() {
        return hivGramStain;
    }

    @Order(624)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivLatexAgglutination() {
        return hivLatexAgglutination;
    }

    @Order(625)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivCqValueDetection() {
        return hivCqValueDetection;
    }

    @Order(626)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivSeQuencing() {
        return hivSeQuencing;
    }

    @Order(627)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivDnaMicroArray() {
        return hivDnaMicroArray;
    }

    @Order(628)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getHivOther() {
        return hivOther;
    }

    @Order(629)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisAntiBodyDetection() {
        return schistosomiasisAntiBodyDetection;
    }

    @Order(630)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisAntigenDetection() {
        return schistosomiasisAntigenDetection;
    }

    @Order(631)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisRapidTest() {
        return schistosomiasisRapidTest;
    }

    @Order(632)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisCulture() {
        return schistosomiasisCulture;
    }

    @Order(633)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisHistopathology() {
        return schistosomiasisHistopathology;
    }

    @Order(634)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisIsolation() {
        return schistosomiasisIsolation;
    }

    @Order(635)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisIgmSerumAntibody() {
        return schistosomiasisIgmSerumAntibody;
    }

    @Order(636)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisIggSerumAntibody() {
        return schistosomiasisIggSerumAntibody;
    }

    @Order(637)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisIgaSerumAntibody() {
        return schistosomiasisIgaSerumAntibody;
    }

    @Order(638)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisIncubationTime() {
        return schistosomiasisIncubationTime;
    }

    @Order(639)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisIndirectFluorescentAntibody() {
        return schistosomiasisIndirectFluorescentAntibody;
    }

    @Order(640)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisMicroscopy() {
        return schistosomiasisMicroscopy;
    }

    @Order(641)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisNeutralizingAntibodies() {
        return schistosomiasisNeutralizingAntibodies;
    }

    @Order(642)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisPcr() {
        return schistosomiasisPcr;
    }

    @Order(643)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisGramStain() {
        return schistosomiasisGramStain;
    }

    @Order(644)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisLatexAgglutination() {
        return schistosomiasisLatexAgglutination;
    }

    @Order(645)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisCqValueDetection() {
        return schistosomiasisCqValueDetection;
    }

    @Order(646)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisSeQuencing() {
        return schistosomiasisSeQuencing;
    }

    @Order(647)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisDnaMicroArray() {
        return schistosomiasisDnaMicroArray;
    }

    @Order(648)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSchistosomiasisOther() {
        return schistosomiasisOther;
    }

    @Order(649)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsAntiBodyDetection() {
        return soilTransmittedHelminthsAntiBodyDetection;
    }

    @Order(650)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsAntigenDetection() {
        return soilTransmittedHelminthsAntigenDetection;
    }

    @Order(651)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsRapidTest() {
        return soilTransmittedHelminthsRapidTest;
    }

    @Order(652)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsCulture() {
        return soilTransmittedHelminthsCulture;
    }

    @Order(653)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsHistopathology() {
        return soilTransmittedHelminthsHistopathology;
    }

    @Order(654)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsIsolation() {
        return soilTransmittedHelminthsIsolation;
    }

    @Order(655)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsIgmSerumAntibody() {
        return soilTransmittedHelminthsIgmSerumAntibody;
    }

    @Order(656)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsIggSerumAntibody() {
        return soilTransmittedHelminthsIggSerumAntibody;
    }

    @Order(657)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsIgaSerumAntibody() {
        return soilTransmittedHelminthsIgaSerumAntibody;
    }

    @Order(658)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsIncubationTime() {
        return soilTransmittedHelminthsIncubationTime;
    }

    @Order(659)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsIndirectFluorescentAntibody() {
        return soilTransmittedHelminthsIndirectFluorescentAntibody;
    }

    @Order(660)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsMicroscopy() {
        return soilTransmittedHelminthsMicroscopy;
    }

    @Order(661)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsNeutralizingAntibodies() {
        return soilTransmittedHelminthsNeutralizingAntibodies;
    }

    @Order(662)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsPcr() {
        return soilTransmittedHelminthsPcr;
    }

    @Order(663)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsGramStain() {
        return soilTransmittedHelminthsGramStain;
    }

    @Order(664)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsLatexAgglutination() {
        return soilTransmittedHelminthsLatexAgglutination;
    }

    @Order(665)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsCqValueDetection() {
        return soilTransmittedHelminthsCqValueDetection;
    }

    @Order(666)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsSeQuencing() {
        return soilTransmittedHelminthsSeQuencing;
    }

    @Order(667)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsDnaMicroArray() {
        return soilTransmittedHelminthsDnaMicroArray;
    }

    @Order(668)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSoilTransmittedHelminthsOther() {
        return soilTransmittedHelminthsOther;
    }

    @Order(669)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisAntiBodyDetection() {
        return trypanosomiasisAntiBodyDetection;
    }

    @Order(670)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisAntigenDetection() {
        return trypanosomiasisAntigenDetection;
    }

    @Order(671)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisRapidTest() {
        return trypanosomiasisRapidTest;
    }

    @Order(672)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisCulture() {
        return trypanosomiasisCulture;
    }

    @Order(673)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisHistopathology() {
        return trypanosomiasisHistopathology;
    }

    @Order(674)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisIsolation() {
        return trypanosomiasisIsolation;
    }

    @Order(675)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisIgmSerumAntibody() {
        return trypanosomiasisIgmSerumAntibody;
    }

    @Order(676)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisIggSerumAntibody() {
        return trypanosomiasisIggSerumAntibody;
    }

    @Order(677)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisIgaSerumAntibody() {
        return trypanosomiasisIgaSerumAntibody;
    }

    @Order(678)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisIncubationTime() {
        return trypanosomiasisIncubationTime;
    }

    @Order(679)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisIndirectFluorescentAntibody() {
        return trypanosomiasisIndirectFluorescentAntibody;
    }

    @Order(680)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisMicroscopy() {
        return trypanosomiasisMicroscopy;
    }

    @Order(681)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisNeutralizingAntibodies() {
        return trypanosomiasisNeutralizingAntibodies;
    }

    @Order(682)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisPcr() {
        return trypanosomiasisPcr;
    }

    @Order(683)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisGramStain() {
        return trypanosomiasisGramStain;
    }

    @Order(684)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisLatexAgglutination() {
        return trypanosomiasisLatexAgglutination;
    }

    @Order(685)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisCqValueDetection() {
        return trypanosomiasisCqValueDetection;
    }

    @Order(686)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisSeQuencing() {
        return trypanosomiasisSeQuencing;
    }

    @Order(687)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisDnaMicroArray() {
        return trypanosomiasisDnaMicroArray;
    }

    @Order(688)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrypanosomiasisOther() {
        return trypanosomiasisOther;
    }

    @Order(689)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationAntiBodyDetection() {
        return diarrheaDehydrationAntiBodyDetection;
    }

    @Order(690)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationAntigenDetection() {
        return diarrheaDehydrationAntigenDetection;
    }

    @Order(691)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationRapidTest() {
        return diarrheaDehydrationRapidTest;
    }

    @Order(692)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationCulture() {
        return diarrheaDehydrationCulture;
    }

    @Order(693)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationHistopathology() {
        return diarrheaDehydrationHistopathology;
    }

    @Order(694)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationIsolation() {
        return diarrheaDehydrationIsolation;
    }

    @Order(695)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationIgmSerumAntibody() {
        return diarrheaDehydrationIgmSerumAntibody;
    }

    @Order(696)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationIggSerumAntibody() {
        return diarrheaDehydrationIggSerumAntibody;
    }

    @Order(697)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationIgaSerumAntibody() {
        return diarrheaDehydrationIgaSerumAntibody;
    }

    @Order(698)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationIncubationTime() {
        return diarrheaDehydrationIncubationTime;
    }

    @Order(699)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationIndirectFluorescentAntibody() {
        return diarrheaDehydrationIndirectFluorescentAntibody;
    }

    @Order(700)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationMicroscopy() {
        return diarrheaDehydrationMicroscopy;
    }

    @Order(701)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationNeutralizingAntibodies() {
        return diarrheaDehydrationNeutralizingAntibodies;
    }

    @Order(702)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationPcr() {
        return diarrheaDehydrationPcr;
    }

    @Order(703)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationGramStain() {
        return diarrheaDehydrationGramStain;
    }

    @Order(704)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationLatexAgglutination() {
        return diarrheaDehydrationLatexAgglutination;
    }

    @Order(705)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationCqValueDetection() {
        return diarrheaDehydrationCqValueDetection;
    }

    @Order(706)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationSeQuencing() {
        return diarrheaDehydrationSeQuencing;
    }

    @Order(707)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationDnaMicroArray() {
        return diarrheaDehydrationDnaMicroArray;
    }

    @Order(708)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaDehydrationOther() {
        return diarrheaDehydrationOther;
    }

    @Order(709)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodAntiBodyDetection() {
        return diarrheaBloodAntiBodyDetection;
    }

    @Order(710)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodAntigenDetection() {
        return diarrheaBloodAntigenDetection;
    }

    @Order(711)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodRapidTest() {
        return diarrheaBloodRapidTest;
    }

    @Order(712)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodCulture() {
        return diarrheaBloodCulture;
    }

    @Order(713)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodHistopathology() {
        return diarrheaBloodHistopathology;
    }

    @Order(714)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodIsolation() {
        return diarrheaBloodIsolation;
    }

    @Order(715)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodIgmSerumAntibody() {
        return diarrheaBloodIgmSerumAntibody;
    }

    @Order(716)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodIggSerumAntibody() {
        return diarrheaBloodIggSerumAntibody;
    }

    @Order(717)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodIgaSerumAntibody() {
        return diarrheaBloodIgaSerumAntibody;
    }

    @Order(718)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodIncubationTime() {
        return diarrheaBloodIncubationTime;
    }

    @Order(719)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodIndirectFluorescentAntibody() {
        return diarrheaBloodIndirectFluorescentAntibody;
    }

    @Order(720)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodMicroscopy() {
        return diarrheaBloodMicroscopy;
    }

    @Order(721)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodNeutralizingAntibodies() {
        return diarrheaBloodNeutralizingAntibodies;
    }

    @Order(722)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodPcr() {
        return diarrheaBloodPcr;
    }

    @Order(723)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodGramStain() {
        return diarrheaBloodGramStain;
    }

    @Order(724)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodLatexAgglutination() {
        return diarrheaBloodLatexAgglutination;
    }

    @Order(725)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodCqValueDetection() {
        return diarrheaBloodCqValueDetection;
    }

    @Order(726)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodSeQuencing() {
        return diarrheaBloodSeQuencing;
    }

    @Order(727)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodDnaMicroArray() {
        return diarrheaBloodDnaMicroArray;
    }

    @Order(728)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiarrheaBloodOther() {
        return diarrheaBloodOther;
    }

    @Order(729)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBiteAntiBodyDetection() {
        return snakeBiteAntiBodyDetection;
    }

    @Order(730)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBiteAntigenDetection() {
        return snakeBiteAntigenDetection;
    }

    @Order(731)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBiteRapidTest() {
        return snakeBiteRapidTest;
    }

    @Order(732)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBiteCulture() {
        return snakeBiteCulture;
    }

    @Order(733)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBiteHistopathology() {
        return snakeBiteHistopathology;
    }

    @Order(734)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBiteIsolation() {
        return snakeBiteIsolation;
    }

    @Order(735)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBiteIgmSerumAntibody() {
        return snakeBiteIgmSerumAntibody;
    }

    @Order(736)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBiteIggSerumAntibody() {
        return snakeBiteIggSerumAntibody;
    }

    @Order(737)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBiteIgaSerumAntibody() {
        return snakeBiteIgaSerumAntibody;
    }

    @Order(738)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBiteIncubationTime() {
        return snakeBiteIncubationTime;
    }

    @Order(739)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBiteIndirectFluorescentAntibody() {
        return snakeBiteIndirectFluorescentAntibody;
    }

    @Order(740)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBiteMicroscopy() {
        return snakeBiteMicroscopy;
    }

    @Order(741)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBiteNeutralizingAntibodies() {
        return snakeBiteNeutralizingAntibodies;
    }

    @Order(742)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBitePcr() {
        return snakeBitePcr;
    }

    @Order(743)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBiteGramStain() {
        return snakeBiteGramStain;
    }

    @Order(744)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBiteLatexAgglutination() {
        return snakeBiteLatexAgglutination;
    }

    @Order(745)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBiteCqValueDetection() {
        return snakeBiteCqValueDetection;
    }

    @Order(746)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBiteSeQuencing() {
        return snakeBiteSeQuencing;
    }

    @Order(747)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBiteDnaMicroArray() {
        return snakeBiteDnaMicroArray;
    }

    @Order(748)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getSnakeBiteOther() {
        return snakeBiteOther;
    }

    @Order(749)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaAntiBodyDetection() {
        return rubellaAntiBodyDetection;
    }

    @Order(750)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaAntigenDetection() {
        return rubellaAntigenDetection;
    }

    @Order(751)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaRapidTest() {
        return rubellaRapidTest;
    }

    @Order(752)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaCulture() {
        return rubellaCulture;
    }

    @Order(753)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaHistopathology() {
        return rubellaHistopathology;
    }

    @Order(754)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaIsolation() {
        return rubellaIsolation;
    }

    @Order(755)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaIgmSerumAntibody() {
        return rubellaIgmSerumAntibody;
    }

    @Order(756)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaIggSerumAntibody() {
        return rubellaIggSerumAntibody;
    }

    @Order(757)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaIgaSerumAntibody() {
        return rubellaIgaSerumAntibody;
    }

    @Order(758)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaIncubationTime() {
        return rubellaIncubationTime;
    }

    @Order(759)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaIndirectFluorescentAntibody() {
        return rubellaIndirectFluorescentAntibody;
    }

    @Order(760)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaMicroscopy() {
        return rubellaMicroscopy;
    }

    @Order(761)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaNeutralizingAntibodies() {
        return rubellaNeutralizingAntibodies;
    }

    @Order(762)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaPcr() {
        return rubellaPcr;
    }

    @Order(763)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaGramStain() {
        return rubellaGramStain;
    }

    @Order(764)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaLatexAgglutination() {
        return rubellaLatexAgglutination;
    }

    @Order(765)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaCqValueDetection() {
        return rubellaCqValueDetection;
    }

    @Order(766)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaSeQuencing() {
        return rubellaSeQuencing;
    }

    @Order(767)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaDnaMicroArray() {
        return rubellaDnaMicroArray;
    }

    @Order(768)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRubellaOther() {
        return rubellaOther;
    }

    @Order(769)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisAntiBodyDetection() {
        return tuberculosisAntiBodyDetection;
    }

    @Order(770)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisAntigenDetection() {
        return tuberculosisAntigenDetection;
    }

    @Order(771)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisRapidTest() {
        return tuberculosisRapidTest;
    }

    @Order(772)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisCulture() {
        return tuberculosisCulture;
    }

    @Order(773)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisHistopathology() {
        return tuberculosisHistopathology;
    }

    @Order(774)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisIsolation() {
        return tuberculosisIsolation;
    }

    @Order(775)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisIgmSerumAntibody() {
        return tuberculosisIgmSerumAntibody;
    }

    @Order(776)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisIggSerumAntibody() {
        return tuberculosisIggSerumAntibody;
    }

    @Order(777)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisIgaSerumAntibody() {
        return tuberculosisIgaSerumAntibody;
    }

    @Order(778)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisIncubationTime() {
        return tuberculosisIncubationTime;
    }

    @Order(779)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisIndirectFluorescentAntibody() {
        return tuberculosisIndirectFluorescentAntibody;
    }

    @Order(780)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisMicroscopy() {
        return tuberculosisMicroscopy;
    }

    @Order(781)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisNeutralizingAntibodies() {
        return tuberculosisNeutralizingAntibodies;
    }

    @Order(782)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisPcr() {
        return tuberculosisPcr;
    }

    @Order(783)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisGramStain() {
        return tuberculosisGramStain;
    }

    @Order(784)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisLatexAgglutination() {
        return tuberculosisLatexAgglutination;
    }

    @Order(785)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisCqValueDetection() {
        return tuberculosisCqValueDetection;
    }

    @Order(786)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisSeQuencing() {
        return tuberculosisSeQuencing;
    }

    @Order(787)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisDnaMicroArray() {
        return tuberculosisDnaMicroArray;
    }

    @Order(788)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTuberculosisOther() {
        return tuberculosisOther;
    }
    
    @Order(789)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosyAntiBodyDetection() {
        return leprosyAntiBodyDetection;
    }

    @Order(790)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosyAntigenDetection() {
        return leprosyAntigenDetection;
    }

    @Order(791)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosyRapidTest() {
        return leprosyRapidTest;
    }

    @Order(792)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosyCulture() {
        return leprosyCulture;
    }

    @Order(793)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosyHistopathology() {
        return leprosyHistopathology;
    }

    @Order(794)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosyIsolation() {
        return leprosyIsolation;
    }

    @Order(795)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosyIgmSerumAntibody() {
        return leprosyIgmSerumAntibody;
    }

    @Order(796)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosyIggSerumAntibody() {
        return leprosyIggSerumAntibody;
    }

    @Order(797)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosyIgaSerumAntibody() {
        return leprosyIgaSerumAntibody;
    }

    @Order(798)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosyIncubationTime() {
        return leprosyIncubationTime;
    }

    @Order(799)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosyIndirectFluorescentAntibody() {
        return leprosyIndirectFluorescentAntibody;
    }

    @Order(800)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosyMicroscopy() {
        return leprosyMicroscopy;
    }

    @Order(801)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosyNeutralizingAntibodies() {
        return leprosyNeutralizingAntibodies;
    }

    @Order(802)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosyPcr() {
        return leprosyPcr;
    }

    @Order(803)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosyGramStain() {
        return leprosyGramStain;
    }

    @Order(804)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosyLatexAgglutination() {
        return leprosyLatexAgglutination;
    }

    @Order(805)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosyCqValueDetection() {
        return leprosyCqValueDetection;
    }

    @Order(806)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosySeQuencing() {
        return leprosySeQuencing;
    }

    @Order(807)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosyDnaMicroArray() {
        return leprosyDnaMicroArray;
    }

    @Order(808)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLeprosyOther() {
        return leprosyOther;
    }

    @Order(809)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisAntiBodyDetection() {
        return lymphaticFilariasisAntiBodyDetection;
    }

    @Order(810)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisAntigenDetection() {
        return lymphaticFilariasisAntigenDetection;
    }

    @Order(811)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisRapidTest() {
        return lymphaticFilariasisRapidTest;
    }

    @Order(812)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisCulture() {
        return lymphaticFilariasisCulture;
    }

    @Order(813)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisHistopathology() {
        return lymphaticFilariasisHistopathology;
    }

    @Order(814)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisIsolation() {
        return lymphaticFilariasisIsolation;
    }

    @Order(815)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisIgmSerumAntibody() {
        return lymphaticFilariasisIgmSerumAntibody;
    }

    @Order(816)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisIggSerumAntibody() {
        return lymphaticFilariasisIggSerumAntibody;
    }

    @Order(817)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisIgaSerumAntibody() {
        return lymphaticFilariasisIgaSerumAntibody;
    }

    @Order(818)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisIncubationTime() {
        return lymphaticFilariasisIncubationTime;
    }

    @Order(819)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisIndirectFluorescentAntibody() {
        return lymphaticFilariasisIndirectFluorescentAntibody;
    }

    @Order(820)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisMicroscopy() {
        return lymphaticFilariasisMicroscopy;
    }

    @Order(821)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisNeutralizingAntibodies() {
        return lymphaticFilariasisNeutralizingAntibodies;
    }

    @Order(822)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisPcr() {
        return lymphaticFilariasisPcr;
    }

    @Order(823)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisGramStain() {
        return lymphaticFilariasisGramStain;
    }

    @Order(824)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisLatexAgglutination() {
        return lymphaticFilariasisLatexAgglutination;
    }

    @Order(825)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisCqValueDetection() {
        return lymphaticFilariasisCqValueDetection;
    }

    @Order(826)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisSeQuencing() {
        return lymphaticFilariasisSeQuencing;
    }

    @Order(827)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisDnaMicroArray() {
        return lymphaticFilariasisDnaMicroArray;
    }

    @Order(828)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getLymphaticFilariasisOther() {
        return lymphaticFilariasisOther;
    }

    @Order(829)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerAntiBodyDetection() {
        return buruliUlcerAntiBodyDetection;
    }

    @Order(830)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerAntigenDetection() {
        return buruliUlcerAntigenDetection;
    }

    @Order(831)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerRapidTest() {
        return buruliUlcerRapidTest;
    }

    @Order(832)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerCulture() {
        return buruliUlcerCulture;
    }

    @Order(833)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerHistopathology() {
        return buruliUlcerHistopathology;
    }

    @Order(834)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerIsolation() {
        return buruliUlcerIsolation;
    }

    @Order(835)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerIgmSerumAntibody() {
        return buruliUlcerIgmSerumAntibody;
    }

    @Order(836)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerIggSerumAntibody() {
        return buruliUlcerIggSerumAntibody;
    }

    @Order(837)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerIgaSerumAntibody() {
        return buruliUlcerIgaSerumAntibody;
    }

    @Order(838)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerIncubationTime() {
        return buruliUlcerIncubationTime;
    }

    @Order(839)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerIndirectFluorescentAntibody() {
        return buruliUlcerIndirectFluorescentAntibody;
    }

    @Order(840)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerMicroscopy() {
        return buruliUlcerMicroscopy;
    }

    @Order(841)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerNeutralizingAntibodies() {
        return buruliUlcerNeutralizingAntibodies;
    }

    @Order(842)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerPcr() {
        return buruliUlcerPcr;
    }

    @Order(843)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerGramStain() {
        return buruliUlcerGramStain;
    }

    @Order(844)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerLatexAgglutination() {
        return buruliUlcerLatexAgglutination;
    }

    @Order(845)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerCqValueDetection() {
        return buruliUlcerCqValueDetection;
    }

    @Order(846)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerSeQuencing() {
        return buruliUlcerSeQuencing;
    }

    @Order(847)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerDnaMicroArray() {
        return buruliUlcerDnaMicroArray;
    }

    @Order(848)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getBuruliUlcerOther() {
        return buruliUlcerOther;
    }

    @Order(849)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisAntiBodyDetection() {
        return pertussisAntiBodyDetection;
    }

    @Order(850)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisAntigenDetection() {
        return pertussisAntigenDetection;
    }

    @Order(851)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisRapidTest() {
        return pertussisRapidTest;
    }

    @Order(852)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisCulture() {
        return pertussisCulture;
    }

    @Order(853)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisHistopathology() {
        return pertussisHistopathology;
    }

    @Order(854)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisIsolation() {
        return pertussisIsolation;
    }

    @Order(855)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisIgmSerumAntibody() {
        return pertussisIgmSerumAntibody;
    }

    @Order(856)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisIggSerumAntibody() {
        return pertussisIggSerumAntibody;
    }

    @Order(857)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisIgaSerumAntibody() {
        return pertussisIgaSerumAntibody;
    }

    @Order(858)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisIncubationTime() {
        return pertussisIncubationTime;
    }

    @Order(859)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisIndirectFluorescentAntibody() {
        return pertussisIndirectFluorescentAntibody;
    }

    @Order(860)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisMicroscopy() {
        return pertussisMicroscopy;
    }

    @Order(861)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisNeutralizingAntibodies() {
        return pertussisNeutralizingAntibodies;
    }

    @Order(862)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisPcr() {
        return pertussisPcr;
    }

    @Order(863)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisGramStain() {
        return pertussisGramStain;
    }

    @Order(864)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisLatexAgglutination() {
        return pertussisLatexAgglutination;
    }

    @Order(865)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisCqValueDetection() {
        return pertussisCqValueDetection;
    }

    @Order(866)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisSeQuencing() {
        return pertussisSeQuencing;
    }

    @Order(867)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisDnaMicroArray() {
        return pertussisDnaMicroArray;
    }

    @Order(868)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPertussisOther() {
        return pertussisOther;
    }

    @Order(869)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalAntiBodyDetection() {
        return neonatalAntiBodyDetection;
    }

    @Order(870)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalAntigenDetection() {
        return neonatalAntigenDetection;
    }

    @Order(871)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalRapidTest() {
        return neonatalRapidTest;
    }

    @Order(872)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalCulture() {
        return neonatalCulture;
    }

    @Order(873)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalHistopathology() {
        return neonatalHistopathology;
    }

    @Order(874)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalIsolation() {
        return neonatalIsolation;
    }

    @Order(875)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalIgmSerumAntibody() {
        return neonatalIgmSerumAntibody;
    }

    @Order(876)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalIggSerumAntibody() {
        return neonatalIggSerumAntibody;
    }

    @Order(877)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalIgaSerumAntibody() {
        return neonatalIgaSerumAntibody;
    }

    @Order(878)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalIncubationTime() {
        return neonatalIncubationTime;
    }

    @Order(879)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalIndirectFluorescentAntibody() {
        return neonatalIndirectFluorescentAntibody;
    }

    @Order(880)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalMicroscopy() {
        return neonatalMicroscopy;
    }

    @Order(881)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalNeutralizingAntibodies() {
        return neonatalNeutralizingAntibodies;
    }

    @Order(882)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalPcr() {
        return neonatalPcr;
    }

    @Order(883)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalGramStain() {
        return neonatalGramStain;
    }

    @Order(884)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalLatexAgglutination() {
        return neonatalLatexAgglutination;
    }

    @Order(885)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalCqValueDetection() {
        return neonatalCqValueDetection;
    }

    @Order(886)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalSeQuencing() {
        return neonatalSeQuencing;
    }

    @Order(887)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalDnaMicroArray() {
        return neonatalDnaMicroArray;
    }

    @Order(888)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getNeonatalOther() {
        return neonatalOther;
    }

    @Order(889)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisAntiBodyDetection() {
        return onchocerciasisAntiBodyDetection;
    }

    @Order(890)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisAntigenDetection() {
        return onchocerciasisAntigenDetection;
    }

    @Order(891)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisRapidTest() {
        return onchocerciasisRapidTest;
    }

    @Order(892)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisCulture() {
        return onchocerciasisCulture;
    }

    @Order(893)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisHistopathology() {
        return onchocerciasisHistopathology;
    }

    @Order(894)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisIsolation() {
        return onchocerciasisIsolation;
    }

    @Order(895)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisIgmSerumAntibody() {
        return onchocerciasisIgmSerumAntibody;
    }

    @Order(896)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisIggSerumAntibody() {
        return onchocerciasisIggSerumAntibody;
    }

    @Order(897)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisIgaSerumAntibody() {
        return onchocerciasisIgaSerumAntibody;
    }

    @Order(898)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisIncubationTime() {
        return onchocerciasisIncubationTime;
    }

    @Order(899)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisIndirectFluorescentAntibody() {
        return onchocerciasisIndirectFluorescentAntibody;
    }

    @Order(900)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisMicroscopy() {
        return onchocerciasisMicroscopy;
    }

    @Order(901)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisNeutralizingAntibodies() {
        return onchocerciasisNeutralizingAntibodies;
    }

    @Order(902)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisPcr() {
        return onchocerciasisPcr;
    }

    @Order(903)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisGramStain() {
        return onchocerciasisGramStain;
    }

    @Order(904)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisLatexAgglutination() {
        return onchocerciasisLatexAgglutination;
    }

    @Order(905)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisCqValueDetection() {
        return onchocerciasisCqValueDetection;
    }

    @Order(906)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisSeQuencing() {
        return onchocerciasisSeQuencing;
    }

    @Order(907)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisDnaMicroArray() {
        return onchocerciasisDnaMicroArray;
    }

    @Order(908)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOnchocerciasisOther() {
        return onchocerciasisOther;
    }

    @Order(909)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaAntiBodyDetection() {
        return diphtheriaAntiBodyDetection;
    }

    @Order(910)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaAntigenDetection() {
        return diphtheriaAntigenDetection;
    }

    @Order(911)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaRapidTest() {
        return diphtheriaRapidTest;
    }

    @Order(912)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaCulture() {
        return diphtheriaCulture;
    }

    @Order(913)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaHistopathology() {
        return diphtheriaHistopathology;
    }

    @Order(914)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaIsolation() {
        return diphtheriaIsolation;
    }

    @Order(915)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaIgmSerumAntibody() {
        return diphtheriaIgmSerumAntibody;
    }

    @Order(916)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaIggSerumAntibody() {
        return diphtheriaIggSerumAntibody;
    }

    @Order(917)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaIgaSerumAntibody() {
        return diphtheriaIgaSerumAntibody;
    }

    @Order(918)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaIncubationTime() {
        return diphtheriaIncubationTime;
    }

    @Order(919)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaIndirectFluorescentAntibody() {
        return diphtheriaIndirectFluorescentAntibody;
    }

    @Order(920)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaMicroscopy() {
        return diphtheriaMicroscopy;
    }

    @Order(921)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaNeutralizingAntibodies() {
        return diphtheriaNeutralizingAntibodies;
    }

    @Order(922)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaPcr() {
        return diphtheriaPcr;
    }

    @Order(923)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaGramStain() {
        return diphtheriaGramStain;
    }

    @Order(924)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaLatexAgglutination() {
        return diphtheriaLatexAgglutination;
    }

    @Order(925)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaCqValueDetection() {
        return diphtheriaCqValueDetection;
    }

    @Order(926)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaSeQuencing() {
        return diphtheriaSeQuencing;
    }

    @Order(927)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaDnaMicroArray() {
        return diphtheriaDnaMicroArray;
    }

    @Order(928)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getDiphtheriaOther() {
        return diphtheriaOther;
    }

    @Order(929)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaAntiBodyDetection() {
        return trachomaAntiBodyDetection;
    }

    @Order(930)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaAntigenDetection() {
        return trachomaAntigenDetection;
    }

    @Order(931)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaRapidTest() {
        return trachomaRapidTest;
    }

    @Order(932)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaCulture() {
        return trachomaCulture;
    }

    @Order(933)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaHistopathology() {
        return trachomaHistopathology;
    }

    @Order(934)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaIsolation() {
        return trachomaIsolation;
    }

    @Order(935)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaIgmSerumAntibody() {
        return trachomaIgmSerumAntibody;
    }

    @Order(936)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaIggSerumAntibody() {
        return trachomaIggSerumAntibody;
    }

    @Order(937)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaIgaSerumAntibody() {
        return trachomaIgaSerumAntibody;
    }

    @Order(938)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaIncubationTime() {
        return trachomaIncubationTime;
    }

    @Order(939)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaIndirectFluorescentAntibody() {
        return trachomaIndirectFluorescentAntibody;
    }

    @Order(940)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaMicroscopy() {
        return trachomaMicroscopy;
    }

    @Order(941)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaNeutralizingAntibodies() {
        return trachomaNeutralizingAntibodies;
    }

    @Order(942)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaPcr() {
        return trachomaPcr;
    }

    @Order(943)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaGramStain() {
        return trachomaGramStain;
    }

    @Order(944)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaLatexAgglutination() {
        return trachomaLatexAgglutination;
    }

    @Order(945)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaCqValueDetection() {
        return trachomaCqValueDetection;
    }

    @Order(946)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaSeQuencing() {
        return trachomaSeQuencing;
    }

    @Order(947)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaDnaMicroArray() {
        return trachomaDnaMicroArray;
    }

    @Order(948)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getTrachomaOther() {
        return trachomaOther;
    }

    @Order(949)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisAntiBodyDetection() {
        return yawsEndemicSyphilisAntiBodyDetection;
    }

    @Order(950)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisAntigenDetection() {
        return yawsEndemicSyphilisAntigenDetection;
    }

    @Order(951)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisRapidTest() {
        return yawsEndemicSyphilisRapidTest;
    }

    @Order(952)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisCulture() {
        return yawsEndemicSyphilisCulture;
    }

    @Order(953)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisHistopathology() {
        return yawsEndemicSyphilisHistopathology;
    }

    @Order(954)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisIsolation() {
        return yawsEndemicSyphilisIsolation;
    }

    @Order(955)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisIgmSerumAntibody() {
        return yawsEndemicSyphilisIgmSerumAntibody;
    }

    @Order(956)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisIggSerumAntibody() {
        return yawsEndemicSyphilisIggSerumAntibody;
    }

    @Order(957)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisIgaSerumAntibody() {
        return yawsEndemicSyphilisIgaSerumAntibody;
    }

    @Order(958)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisIncubationTime() {
        return yawsEndemicSyphilisIncubationTime;
    }

    @Order(959)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisIndirectFluorescentAntibody() {
        return yawsEndemicSyphilisIndirectFluorescentAntibody;
    }

    @Order(960)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisMicroscopy() {
        return yawsEndemicSyphilisMicroscopy;
    }

    @Order(961)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisNeutralizingAntibodies() {
        return yawsEndemicSyphilisNeutralizingAntibodies;
    }

    @Order(962)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisPcr() {
        return yawsEndemicSyphilisPcr;
    }

    @Order(963)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisGramStain() {
        return yawsEndemicSyphilisGramStain;
    }

    @Order(964)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisLatexAgglutination() {
        return yawsEndemicSyphilisLatexAgglutination;
    }

    @Order(965)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisCqValueDetection() {
        return yawsEndemicSyphilisCqValueDetection;
    }

    @Order(966)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisSeQuencing() {
        return yawsEndemicSyphilisSeQuencing;
    }

    @Order(967)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisDnaMicroArray() {
        return yawsEndemicSyphilisDnaMicroArray;
    }

    @Order(968)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getYawsEndemicSyphilisOther() {
        return yawsEndemicSyphilisOther;
    }

    @Order(969)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsAntiBodyDetection() {
        return maternalDeathsAntiBodyDetection;
    }

    @Order(970)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsAntigenDetection() {
        return maternalDeathsAntigenDetection;
    }

    @Order(971)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsRapidTest() {
        return maternalDeathsRapidTest;
    }

    @Order(972)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsCulture() {
        return maternalDeathsCulture;
    }

    @Order(973)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsHistopathology() {
        return maternalDeathsHistopathology;
    }

    @Order(974)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsIsolation() {
        return maternalDeathsIsolation;
    }

    @Order(975)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsIgmSerumAntibody() {
        return maternalDeathsIgmSerumAntibody;
    }

    @Order(976)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsIggSerumAntibody() {
        return maternalDeathsIggSerumAntibody;
    }

    @Order(977)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsIgaSerumAntibody() {
        return maternalDeathsIgaSerumAntibody;
    }

    @Order(978)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsIncubationTime() {
        return maternalDeathsIncubationTime;
    }

    @Order(979)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsIndirectFluorescentAntibody() {
        return maternalDeathsIndirectFluorescentAntibody;
    }

    @Order(980)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsMicroscopy() {
        return maternalDeathsMicroscopy;
    }

    @Order(981)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsNeutralizingAntibodies() {
        return maternalDeathsNeutralizingAntibodies;
    }

    @Order(982)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsPcr() {
        return maternalDeathsPcr;
    }

    @Order(983)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsGramStain() {
        return maternalDeathsGramStain;
    }

    @Order(984)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsLatexAgglutination() {
        return maternalDeathsLatexAgglutination;
    }

    @Order(985)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsCqValueDetection() {
        return maternalDeathsCqValueDetection;
    }

    @Order(986)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsSeQuencing() {
        return maternalDeathsSeQuencing;
    }

    @Order(987)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsDnaMicroArray() {
        return maternalDeathsDnaMicroArray;
    }

    @Order(988)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getMaternalDeathsOther() {
        return maternalDeathsOther;
    }

    @Order(989)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsAntiBodyDetection() {
        return perinatalDeathsAntiBodyDetection;
    }

    @Order(990)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsAntigenDetection() {
        return perinatalDeathsAntigenDetection;
    }

    @Order(991)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsRapidTest() {
        return perinatalDeathsRapidTest;
    }

    @Order(992)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsCulture() {
        return perinatalDeathsCulture;
    }

    @Order(993)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsHistopathology() {
        return perinatalDeathsHistopathology;
    }

    @Order(994)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsIsolation() {
        return perinatalDeathsIsolation;
    }

    @Order(995)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsIgmSerumAntibody() {
        return perinatalDeathsIgmSerumAntibody;
    }

    @Order(996)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsIggSerumAntibody() {
        return perinatalDeathsIggSerumAntibody;
    }

    @Order(997)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsIgaSerumAntibody() {
        return perinatalDeathsIgaSerumAntibody;
    }

    @Order(998)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsIncubationTime() {
        return perinatalDeathsIncubationTime;
    }

    @Order(999)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsIndirectFluorescentAntibody() {
        return perinatalDeathsIndirectFluorescentAntibody;
    }

    @Order(1000)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsMicroscopy() {
        return perinatalDeathsMicroscopy;
    }

    @Order(1001)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsNeutralizingAntibodies() {
        return perinatalDeathsNeutralizingAntibodies;
    }

    @Order(1002)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsPcr() {
        return perinatalDeathsPcr;
    }

    @Order(1003)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsGramStain() {
        return perinatalDeathsGramStain;
    }

    @Order(1004)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsLatexAgglutination() {
        return perinatalDeathsLatexAgglutination;
    }

    @Order(1005)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsCqValueDetection() {
        return perinatalDeathsCqValueDetection;
    }

    @Order(1006)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsSeQuencing() {
        return perinatalDeathsSeQuencing;
    }

    @Order(1007)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsDnaMicroArray() {
        return perinatalDeathsDnaMicroArray;
    }

    @Order(1008)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPerinatalDeathsOther() {
        return perinatalDeathsOther;
    }

    @Order(1009)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaAAntiBodyDetection() {
        return influenzaAAntiBodyDetection;
    }

    @Order(1010)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaAAntigenDetection() {
        return influenzaAAntigenDetection;
    }

    @Order(1011)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaARapidTest() {
        return influenzaARapidTest;
    }

    @Order(1012)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaACulture() {
        return influenzaACulture;
    }

    @Order(1013)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaAHistopathology() {
        return influenzaAHistopathology;
    }

    @Order(1014)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaAIsolation() {
        return influenzaAIsolation;
    }

    @Order(1015)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaAIgmSerumAntibody() {
        return influenzaAIgmSerumAntibody;
    }

    @Order(1016)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaAIggSerumAntibody() {
        return influenzaAIggSerumAntibody;
    }

    @Order(1017)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaAIgaSerumAntibody() {
        return influenzaAIgaSerumAntibody;
    }

    @Order(1018)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaAIncubationTime() {
        return influenzaAIncubationTime;
    }

    @Order(1019)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaAIndirectFluorescentAntibody() {
        return influenzaAIndirectFluorescentAntibody;
    }

    @Order(1020)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaAMicroscopy() {
        return influenzaAMicroscopy;
    }

    @Order(1021)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaANeutralizingAntibodies() {
        return influenzaANeutralizingAntibodies;
    }

    @Order(1022)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaAPcr() {
        return influenzaAPcr;
    }

    @Order(1023)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaAGramStain() {
        return influenzaAGramStain;
    }

    @Order(1024)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaALatexAgglutination() {
        return influenzaALatexAgglutination;
    }

    @Order(1025)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaACqValueDetection() {
        return influenzaACqValueDetection;
    }

    @Order(1026)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaASeQuencing() {
        return influenzaASeQuencing;
    }

    @Order(1027)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaADnaMicroArray() {
        return influenzaADnaMicroArray;
    }

    @Order(1028)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaAOther() {
        return influenzaAOther;
    }

    @Order(1029)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBAntiBodyDetection() {
        return influenzaBAntiBodyDetection;
    }

    @Order(1030)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBAntigenDetection() {
        return influenzaBAntigenDetection;
    }

    @Order(1031)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBRapidTest() {
        return influenzaBRapidTest;
    }

    @Order(1032)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBCulture() {
        return influenzaBCulture;
    }

    @Order(1033)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBHistopathology() {
        return influenzaBHistopathology;
    }

    @Order(1034)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBIsolation() {
        return influenzaBIsolation;
    }

    @Order(1035)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBIgmSerumAntibody() {
        return influenzaBIgmSerumAntibody;
    }

    @Order(1036)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBIggSerumAntibody() {
        return influenzaBIggSerumAntibody;
    }

    @Order(1037)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBIgaSerumAntibody() {
        return influenzaBIgaSerumAntibody;
    }

    @Order(1038)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBIncubationTime() {
        return influenzaBIncubationTime;
    }

    @Order(1039)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBIndirectFluorescentAntibody() {
        return influenzaBIndirectFluorescentAntibody;
    }

    @Order(1040)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBMicroscopy() {
        return influenzaBMicroscopy;
    }

    @Order(1041)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBNeutralizingAntibodies() {
        return influenzaBNeutralizingAntibodies;
    }

    @Order(1042)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBPcr() {
        return influenzaBPcr;
    }

    @Order(1043)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBGramStain() {
        return influenzaBGramStain;
    }

    @Order(1044)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBLatexAgglutination() {
        return influenzaBLatexAgglutination;
    }

    @Order(1045)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBCqValueDetection() {
        return influenzaBCqValueDetection;
    }

    @Order(1046)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBSeQuencing() {
        return influenzaBSeQuencing;
    }

    @Order(1047)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBDnaMicroArray() {
        return influenzaBDnaMicroArray;
    }

    @Order(1048)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getInfluenzaBOther() {
        return influenzaBOther;
    }

    @Order(1049)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusAntiBodyDetection() {
        return hMetapneumovirusAntiBodyDetection;
    }

    @Order(1050)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusAntigenDetection() {
        return hMetapneumovirusAntigenDetection;
    }

    @Order(1051)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusRapidTest() {
        return hMetapneumovirusRapidTest;
    }

    @Order(1052)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusCulture() {
        return hMetapneumovirusCulture;
    }

    @Order(1053)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusHistopathology() {
        return hMetapneumovirusHistopathology;
    }

    @Order(1054)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusIsolation() {
        return hMetapneumovirusIsolation;
    }

    @Order(1055)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusIgmSerumAntibody() {
        return hMetapneumovirusIgmSerumAntibody;
    }

    @Order(1056)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusIggSerumAntibody() {
        return hMetapneumovirusIggSerumAntibody;
    }

    @Order(1057)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusIgaSerumAntibody() {
        return hMetapneumovirusIgaSerumAntibody;
    }

    @Order(1058)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusIncubationTime() {
        return hMetapneumovirusIncubationTime;
    }

    @Order(1059)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusIndirectFluorescentAntibody() {
        return hMetapneumovirusIndirectFluorescentAntibody;
    }

    @Order(1060)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusMicroscopy() {
        return hMetapneumovirusMicroscopy;
    }

    @Order(1061)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusNeutralizingAntibodies() {
        return hMetapneumovirusNeutralizingAntibodies;
    }

    @Order(1062)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusPcr() {
        return hMetapneumovirusPcr;
    }

    @Order(1063)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusGramStain() {
        return hMetapneumovirusGramStain;
    }

    @Order(1064)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusLatexAgglutination() {
        return hMetapneumovirusLatexAgglutination;
    }

    @Order(1065)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusCqValueDetection() {
        return hMetapneumovirusCqValueDetection;
    }

    @Order(1066)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusSeQuencing() {
        return hMetapneumovirusSeQuencing;
    }

    @Order(1067)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusDnaMicroArray() {
        return hMetapneumovirusDnaMicroArray;
    }

    @Order(1068)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String gethMetapneumovirusOther() {
        return hMetapneumovirusOther;
    }

    @Order(1069)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusAntiBodyDetection() {
        return respiratorySyncytialVirusAntiBodyDetection;
    }

    @Order(1070)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusAntigenDetection() {
        return respiratorySyncytialVirusAntigenDetection;
    }

    @Order(1071)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusRapidTest() {
        return respiratorySyncytialVirusRapidTest;
    }

    @Order(1072)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusCulture() {
        return respiratorySyncytialVirusCulture;
    }

    @Order(1073)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusHistopathology() {
        return respiratorySyncytialVirusHistopathology;
    }

    @Order(1074)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusIsolation() {
        return respiratorySyncytialVirusIsolation;
    }

    @Order(1075)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusIgmSerumAntibody() {
        return respiratorySyncytialVirusIgmSerumAntibody;
    }

    @Order(1076)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusIggSerumAntibody() {
        return respiratorySyncytialVirusIggSerumAntibody;
    }

    @Order(1077)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusIgaSerumAntibody() {
        return respiratorySyncytialVirusIgaSerumAntibody;
    }

    @Order(1078)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusIncubationTime() {
        return respiratorySyncytialVirusIncubationTime;
    }

    @Order(1079)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusIndirectFluorescentAntibody() {
        return respiratorySyncytialVirusIndirectFluorescentAntibody;
    }

    @Order(1080)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusMicroscopy() {
        return respiratorySyncytialVirusMicroscopy;
    }

    @Order(1081)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusNeutralizingAntibodies() {
        return respiratorySyncytialVirusNeutralizingAntibodies;
    }

    @Order(1082)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusPcr() {
        return respiratorySyncytialVirusPcr;
    }

    @Order(1083)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusGramStain() {
        return respiratorySyncytialVirusGramStain;
    }

    @Order(1084)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusLatexAgglutination() {
        return respiratorySyncytialVirusLatexAgglutination;
    }

    @Order(1085)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusCqValueDetection() {
        return respiratorySyncytialVirusCqValueDetection;
    }

    @Order(1086)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusSeQuencing() {
        return respiratorySyncytialVirusSeQuencing;
    }

    @Order(1087)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusDnaMicroArray() {
        return respiratorySyncytialVirusDnaMicroArray;
    }

    @Order(1088)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRespiratorySyncytialVirusOther() {
        return respiratorySyncytialVirusOther;
    }

    @Order(1089)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4AntiBodyDetection() {
        return parainfluenza1_4AntiBodyDetection;
    }

    @Order(1090)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4AntigenDetection() {
        return parainfluenza1_4AntigenDetection;
    }

    @Order(1091)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4RapidTest() {
        return parainfluenza1_4RapidTest;
    }

    @Order(1092)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4Culture() {
        return parainfluenza1_4Culture;
    }

    @Order(1093)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4Histopathology() {
        return parainfluenza1_4Histopathology;
    }

    @Order(1094)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4Isolation() {
        return parainfluenza1_4Isolation;
    }

    @Order(1095)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4IgmSerumAntibody() {
        return parainfluenza1_4IgmSerumAntibody;
    }

    @Order(1096)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4IggSerumAntibody() {
        return parainfluenza1_4IggSerumAntibody;
    }

    @Order(1097)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4IgaSerumAntibody() {
        return parainfluenza1_4IgaSerumAntibody;
    }

    @Order(1098)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4IncubationTime() {
        return parainfluenza1_4IncubationTime;
    }

    @Order(1099)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4IndirectFluorescentAntibody() {
        return parainfluenza1_4IndirectFluorescentAntibody;
    }

    @Order(1100)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4Microscopy() {
        return parainfluenza1_4Microscopy;
    }

    @Order(1101)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4NeutralizingAntibodies() {
        return parainfluenza1_4NeutralizingAntibodies;
    }

    @Order(1102)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4Pcr() {
        return parainfluenza1_4Pcr;
    }

    @Order(1103)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4GramStain() {
        return parainfluenza1_4GramStain;
    }

    @Order(1104)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4LatexAgglutination() {
        return parainfluenza1_4LatexAgglutination;
    }

    @Order(1105)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4CqValueDetection() {
        return parainfluenza1_4CqValueDetection;
    }

    @Order(1106)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4SeQuencing() {
        return parainfluenza1_4SeQuencing;
    }

    @Order(1107)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4DnaMicroArray() {
        return parainfluenza1_4DnaMicroArray;
    }

    @Order(1108)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getParainfluenza1_4Other() {
        return parainfluenza1_4Other;
    }

    @Order(1109)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusAntiBodyDetection() {
        return adenovirusAntiBodyDetection;
    }

    @Order(1110)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusAntigenDetection() {
        return adenovirusAntigenDetection;
    }

    @Order(1111)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusRapidTest() {
        return adenovirusRapidTest;
    }

    @Order(1112)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusCulture() {
        return adenovirusCulture;
    }

    @Order(1113)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusHistopathology() {
        return adenovirusHistopathology;
    }

    @Order(1114)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusIsolation() {
        return adenovirusIsolation;
    }

    @Order(1115)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusIgmSerumAntibody() {
        return adenovirusIgmSerumAntibody;
    }

    @Order(1116)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusIggSerumAntibody() {
        return adenovirusIggSerumAntibody;
    }

    @Order(1117)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusIgaSerumAntibody() {
        return adenovirusIgaSerumAntibody;
    }

    @Order(1118)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusIncubationTime() {
        return adenovirusIncubationTime;
    }

    @Order(1119)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusIndirectFluorescentAntibody() {
        return adenovirusIndirectFluorescentAntibody;
    }

    @Order(1120)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusMicroscopy() {
        return adenovirusMicroscopy;
    }

    @Order(1121)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusNeutralizingAntibodies() {
        return adenovirusNeutralizingAntibodies;
    }

    @Order(1122)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusPcr() {
        return adenovirusPcr;
    }

    @Order(1123)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusGramStain() {
        return adenovirusGramStain;
    }

    @Order(1124)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusLatexAgglutination() {
        return adenovirusLatexAgglutination;
    }

    @Order(1125)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusCqValueDetection() {
        return adenovirusCqValueDetection;
    }

    @Order(1126)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusSeQuencing() {
        return adenovirusSeQuencing;
    }

    @Order(1127)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusDnaMicroArray() {
        return adenovirusDnaMicroArray;
    }

    @Order(1128)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAdenovirusOther() {
        return adenovirusOther;
    }

    @Order(1129)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusAntiBodyDetection() {
        return rhinovirusAntiBodyDetection;
    }

    @Order(1130)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusAntigenDetection() {
        return rhinovirusAntigenDetection;
    }

    @Order(1131)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusRapidTest() {
        return rhinovirusRapidTest;
    }

    @Order(1132)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusCulture() {
        return rhinovirusCulture;
    }

    @Order(1133)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusHistopathology() {
        return rhinovirusHistopathology;
    }

    @Order(1134)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusIsolation() {
        return rhinovirusIsolation;
    }

    @Order(1135)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusIgmSerumAntibody() {
        return rhinovirusIgmSerumAntibody;
    }

    @Order(1136)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusIggSerumAntibody() {
        return rhinovirusIggSerumAntibody;
    }

    @Order(1137)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusIgaSerumAntibody() {
        return rhinovirusIgaSerumAntibody;
    }

    @Order(1138)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusIncubationTime() {
        return rhinovirusIncubationTime;
    }

    @Order(1139)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusIndirectFluorescentAntibody() {
        return rhinovirusIndirectFluorescentAntibody;
    }

    @Order(1140)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusMicroscopy() {
        return rhinovirusMicroscopy;
    }

    @Order(1141)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusNeutralizingAntibodies() {
        return rhinovirusNeutralizingAntibodies;
    }

    @Order(1142)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusPcr() {
        return rhinovirusPcr;
    }

    @Order(1143)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusGramStain() {
        return rhinovirusGramStain;
    }

    @Order(1144)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusLatexAgglutination() {
        return rhinovirusLatexAgglutination;
    }

    @Order(1145)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusCqValueDetection() {
        return rhinovirusCqValueDetection;
    }

    @Order(1146)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusSeQuencing() {
        return rhinovirusSeQuencing;
    }

    @Order(1147)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusDnaMicroArray() {
        return rhinovirusDnaMicroArray;
    }

    @Order(1148)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getRhinovirusOther() {
        return rhinovirusOther;
    }

    @Order(1149)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusAntiBodyDetection() {
        return enterovirusAntiBodyDetection;
    }

    @Order(1150)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusAntigenDetection() {
        return enterovirusAntigenDetection;
    }

    @Order(1151)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusRapidTest() {
        return enterovirusRapidTest;
    }

    @Order(1152)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusCulture() {
        return enterovirusCulture;
    }

    @Order(1153)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusHistopathology() {
        return enterovirusHistopathology;
    }

    @Order(1154)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusIsolation() {
        return enterovirusIsolation;
    }

    @Order(1155)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusIgmSerumAntibody() {
        return enterovirusIgmSerumAntibody;
    }

    @Order(1156)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusIggSerumAntibody() {
        return enterovirusIggSerumAntibody;
    }

    @Order(1157)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusIgaSerumAntibody() {
        return enterovirusIgaSerumAntibody;
    }

    @Order(1158)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusIncubationTime() {
        return enterovirusIncubationTime;
    }

    @Order(1159)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusIndirectFluorescentAntibody() {
        return enterovirusIndirectFluorescentAntibody;
    }

    @Order(1160)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusMicroscopy() {
        return enterovirusMicroscopy;
    }

    @Order(1161)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusNeutralizingAntibodies() {
        return enterovirusNeutralizingAntibodies;
    }

    @Order(1162)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusPcr() {
        return enterovirusPcr;
    }

    @Order(1163)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusGramStain() {
        return enterovirusGramStain;
    }

    @Order(1164)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusLatexAgglutination() {
        return enterovirusLatexAgglutination;
    }

    @Order(1165)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusCqValueDetection() {
        return enterovirusCqValueDetection;
    }

    @Order(1166)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusSeQuencing() {
        return enterovirusSeQuencing;
    }

    @Order(1167)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusDnaMicroArray() {
        return enterovirusDnaMicroArray;
    }

    @Order(1168)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getEnterovirusOther() {
        return enterovirusOther;
    }

    @Order(1169)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaeAntiBodyDetection() {
        return mPneumoniaeAntiBodyDetection;
    }

    @Order(1170)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaeAntigenDetection() {
        return mPneumoniaeAntigenDetection;
    }

    @Order(1171)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaeRapidTest() {
        return mPneumoniaeRapidTest;
    }

    @Order(1172)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaeCulture() {
        return mPneumoniaeCulture;
    }

    @Order(1173)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaeHistopathology() {
        return mPneumoniaeHistopathology;
    }

    @Order(1174)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaeIsolation() {
        return mPneumoniaeIsolation;
    }

    @Order(1175)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaeIgmSerumAntibody() {
        return mPneumoniaeIgmSerumAntibody;
    }

    @Order(1176)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaeIggSerumAntibody() {
        return mPneumoniaeIggSerumAntibody;
    }

    @Order(1177)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaeIgaSerumAntibody() {
        return mPneumoniaeIgaSerumAntibody;
    }

    @Order(1178)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaeIncubationTime() {
        return mPneumoniaeIncubationTime;
    }

    @Order(1179)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaeIndirectFluorescentAntibody() {
        return mPneumoniaeIndirectFluorescentAntibody;
    }

    @Order(1180)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaeMicroscopy() {
        return mPneumoniaeMicroscopy;
    }

    @Order(1181)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaeNeutralizingAntibodies() {
        return mPneumoniaeNeutralizingAntibodies;
    }

    @Order(1182)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaePcr() {
        return mPneumoniaePcr;
    }

    @Order(1183)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaeGramStain() {
        return mPneumoniaeGramStain;
    }

    @Order(1184)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaeLatexAgglutination() {
        return mPneumoniaeLatexAgglutination;
    }

    @Order(1185)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaeCqValueDetection() {
        return mPneumoniaeCqValueDetection;
    }

    @Order(1186)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaeSeQuencing() {
        return mPneumoniaeSeQuencing;
    }

    @Order(1187)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaeDnaMicroArray() {
        return mPneumoniaeDnaMicroArray;
    }

    @Order(1188)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getmPneumoniaeOther() {
        return mPneumoniaeOther;
    }

    @Order(1189)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaeAntiBodyDetection() {
        return cPneumoniaeAntiBodyDetection;
    }

    @Order(1190)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaeAntigenDetection() {
        return cPneumoniaeAntigenDetection;
    }

    @Order(1191)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaeRapidTest() {
        return cPneumoniaeRapidTest;
    }

    @Order(1192)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaeCulture() {
        return cPneumoniaeCulture;
    }

    @Order(1193)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaeHistopathology() {
        return cPneumoniaeHistopathology;
    }

    @Order(1194)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaeIsolation() {
        return cPneumoniaeIsolation;
    }

    @Order(1195)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaeIgmSerumAntibody() {
        return cPneumoniaeIgmSerumAntibody;
    }

    @Order(1196)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaeIggSerumAntibody() {
        return cPneumoniaeIggSerumAntibody;
    }

    @Order(1197)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaeIgaSerumAntibody() {
        return cPneumoniaeIgaSerumAntibody;
    }

    @Order(1198)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaeIncubationTime() {
        return cPneumoniaeIncubationTime;
    }

    @Order(1199)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaeIndirectFluorescentAntibody() {
        return cPneumoniaeIndirectFluorescentAntibody;
    }

    @Order(1200)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaeMicroscopy() {
        return cPneumoniaeMicroscopy;
    }

    @Order(1201)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaeNeutralizingAntibodies() {
        return cPneumoniaeNeutralizingAntibodies;
    }

    @Order(1202)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaePcr() {
        return cPneumoniaePcr;
    }

    @Order(1203)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaeGramStain() {
        return cPneumoniaeGramStain;
    }

    @Order(1204)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaeLatexAgglutination() {
        return cPneumoniaeLatexAgglutination;
    }

    @Order(1205)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaeCqValueDetection() {
        return cPneumoniaeCqValueDetection;
    }

    @Order(1206)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaeSeQuencing() {
        return cPneumoniaeSeQuencing;
    }

    @Order(1207)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaeDnaMicroArray() {
        return cPneumoniaeDnaMicroArray;
    }

    @Order(1208)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getcPneumoniaeOther() {
        return cPneumoniaeOther;
    }

    @Order(1209)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriAntiBodyDetection() {
        return ariAntiBodyDetection;
    }

    @Order(1210)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriAntigenDetection() {
        return ariAntigenDetection;
    }

    @Order(1211)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriRapidTest() {
        return ariRapidTest;
    }

    @Order(1212)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriCulture() {
        return ariCulture;
    }

    @Order(1213)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriHistopathology() {
        return ariHistopathology;
    }

    @Order(1214)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriIsolation() {
        return ariIsolation;
    }

    @Order(1215)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriIgmSerumAntibody() {
        return ariIgmSerumAntibody;
    }

    @Order(1216)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriIggSerumAntibody() {
        return ariIggSerumAntibody;
    }

    @Order(1217)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriIgaSerumAntibody() {
        return ariIgaSerumAntibody;
    }

    @Order(1218)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriIncubationTime() {
        return ariIncubationTime;
    }

    @Order(1219)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriIndirectFluorescentAntibody() {
        return ariIndirectFluorescentAntibody;
    }

    @Order(1220)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriMicroscopy() {
        return ariMicroscopy;
    }

    @Order(1221)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriNeutralizingAntibodies() {
        return ariNeutralizingAntibodies;
    }

    @Order(1222)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriPcr() {
        return ariPcr;
    }

    @Order(1223)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriGramStain() {
        return ariGramStain;
    }

    @Order(1224)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriLatexAgglutination() {
        return ariLatexAgglutination;
    }

    @Order(1225)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriCqValueDetection() {
        return ariCqValueDetection;
    }

    @Order(1226)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriSeQuencing() {
        return ariSeQuencing;
    }

    @Order(1227)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriDnaMicroArray() {
        return ariDnaMicroArray;
    }

    @Order(1228)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getAriOther() {
        return ariOther;
    }

    @Order(1229)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaAntiBodyDetection() {
        return chikungunyaAntiBodyDetection;
    }

    @Order(1230)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaAntigenDetection() {
        return chikungunyaAntigenDetection;
    }

    @Order(1231)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaRapidTest() {
        return chikungunyaRapidTest;
    }

    @Order(1232)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaCulture() {
        return chikungunyaCulture;
    }

    @Order(1233)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaHistopathology() {
        return chikungunyaHistopathology;
    }

    @Order(1234)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaIsolation() {
        return chikungunyaIsolation;
    }

    @Order(1235)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaIgmSerumAntibody() {
        return chikungunyaIgmSerumAntibody;
    }

    @Order(1236)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaIggSerumAntibody() {
        return chikungunyaIggSerumAntibody;
    }

    @Order(1237)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaIgaSerumAntibody() {
        return chikungunyaIgaSerumAntibody;
    }

    @Order(1238)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaIncubationTime() {
        return chikungunyaIncubationTime;
    }

    @Order(1239)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaIndirectFluorescentAntibody() {
        return chikungunyaIndirectFluorescentAntibody;
    }

    @Order(1240)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaMicroscopy() {
        return chikungunyaMicroscopy;
    }

    @Order(1241)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaNeutralizingAntibodies() {
        return chikungunyaNeutralizingAntibodies;
    }

    @Order(1242)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaPcr() {
        return chikungunyaPcr;
    }

    @Order(1243)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaGramStain() {
        return chikungunyaGramStain;
    }

    @Order(1244)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaLatexAgglutination() {
        return chikungunyaLatexAgglutination;
    }

    @Order(1245)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaCqValueDetection() {
        return chikungunyaCqValueDetection;
    }

    @Order(1246)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaSeQuencing() {
        return chikungunyaSeQuencing;
    }

    @Order(1247)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaDnaMicroArray() {
        return chikungunyaDnaMicroArray;
    }

    @Order(1248)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getChikungunyaOther() {
        return chikungunyaOther;
    }

    @Order(1249)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildAntiBodyDetection() {
        return postImmunizationAdverseEventsMildAntiBodyDetection;
    }

    @Order(1250)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildAntigenDetection() {
        return postImmunizationAdverseEventsMildAntigenDetection;
    }

    @Order(1251)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildRapidTest() {
        return postImmunizationAdverseEventsMildRapidTest;
    }

    @Order(1252)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildCulture() {
        return postImmunizationAdverseEventsMildCulture;
    }

    @Order(1253)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildHistopathology() {
        return postImmunizationAdverseEventsMildHistopathology;
    }

    @Order(1254)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildIsolation() {
        return postImmunizationAdverseEventsMildIsolation;
    }

    @Order(1255)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildIgmSerumAntibody() {
        return postImmunizationAdverseEventsMildIgmSerumAntibody;
    }

    @Order(1256)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildIggSerumAntibody() {
        return postImmunizationAdverseEventsMildIggSerumAntibody;
    }

    @Order(1257)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildIgaSerumAntibody() {
        return postImmunizationAdverseEventsMildIgaSerumAntibody;
    }

    @Order(1258)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildIncubationTime() {
        return postImmunizationAdverseEventsMildIncubationTime;
    }

    @Order(1259)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildIndirectFluorescentAntibody() {
        return postImmunizationAdverseEventsMildIndirectFluorescentAntibody;
    }

    @Order(1260)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildMicroscopy() {
        return postImmunizationAdverseEventsMildMicroscopy;
    }

    @Order(1261)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildNeutralizingAntibodies() {
        return postImmunizationAdverseEventsMildNeutralizingAntibodies;
    }

    @Order(1262)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildPcr() {
        return postImmunizationAdverseEventsMildPcr;
    }

    @Order(1263)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildGramStain() {
        return postImmunizationAdverseEventsMildGramStain;
    }

    @Order(1264)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildLatexAgglutination() {
        return postImmunizationAdverseEventsMildLatexAgglutination;
    }

    @Order(1265)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildCqValueDetection() {
        return postImmunizationAdverseEventsMildCqValueDetection;
    }

    @Order(1266)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildSeQuencing() {
        return postImmunizationAdverseEventsMildSeQuencing;
    }

    @Order(1267)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildDnaMicroArray() {
        return postImmunizationAdverseEventsMildDnaMicroArray;
    }

    @Order(1268)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsMildOther() {
        return postImmunizationAdverseEventsMildOther;
    }

    @Order(1269)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSevereAntiBodyDetection() {
        return PostImmunizationAdverseEventsSevereAntiBodyDetection;
    }

    @Order(1270)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSevereAntigenDetection() {
        return PostImmunizationAdverseEventsSevereAntigenDetection;
    }

    @Order(1271)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSevereRapidTest() {
        return PostImmunizationAdverseEventsSevereRapidTest;
    }

    @Order(1272)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSevereCulture() {
        return PostImmunizationAdverseEventsSevereCulture;
    }

    @Order(1273)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSevereHistopathology() {
        return PostImmunizationAdverseEventsSevereHistopathology;
    }

    @Order(1274)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSevereIsolation() {
        return PostImmunizationAdverseEventsSevereIsolation;
    }

    @Order(1275)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSevereIgmSerumAntibody() {
        return PostImmunizationAdverseEventsSevereIgmSerumAntibody;
    }

    @Order(1276)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSevereIggSerumAntibody() {
        return PostImmunizationAdverseEventsSevereIggSerumAntibody;
    }

    @Order(1277)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSevereIgaSerumAntibody() {
        return PostImmunizationAdverseEventsSevereIgaSerumAntibody;
    }

    @Order(1278)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSevereIncubationTime() {
        return PostImmunizationAdverseEventsSevereIncubationTime;
    }

    @Order(1279)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSevereIndirectFluorescentAntibody() {
        return PostImmunizationAdverseEventsSevereIndirectFluorescentAntibody;
    }

    @Order(1280)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSevereMicroscopy() {
        return PostImmunizationAdverseEventsSevereMicroscopy;
    }

    @Order(1281)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSevereNeutralizingAntibodies() {
        return PostImmunizationAdverseEventsSevereNeutralizingAntibodies;
    }

    @Order(1282)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSeverePcr() {
        return PostImmunizationAdverseEventsSeverePcr;
    }

    @Order(1283)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSevereGramStain() {
        return PostImmunizationAdverseEventsSevereGramStain;
    }

    @Order(1284)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSevereLatexAgglutination() {
        return PostImmunizationAdverseEventsSevereLatexAgglutination;
    }

    @Order(1285)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSevereCqValueDetection() {
        return PostImmunizationAdverseEventsSevereCqValueDetection;
    }

    @Order(1286)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSevereSeQuencing() {
        return PostImmunizationAdverseEventsSevereSeQuencing;
    }

    @Order(1287)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSevereDnaMicroArray() {
        return PostImmunizationAdverseEventsSevereDnaMicroArray;
    }

    @Order(1288)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getPostImmunizationAdverseEventsSevereOther() {
        return PostImmunizationAdverseEventsSevereOther;
    }

    @Order(1289)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaAntiBodyDetection() {
        return fhaAntiBodyDetection;
    }

    @Order(1290)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaAntigenDetection() {
        return fhaAntigenDetection;
    }

    @Order(1291)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaRapidTest() {
        return fhaRapidTest;
    }

    @Order(1292)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaCulture() {
        return fhaCulture;
    }

    @Order(1293)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaHistopathology() {
        return fhaHistopathology;
    }

    @Order(1294)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaIsolation() {
        return fhaIsolation;
    }

    @Order(1295)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaIgmSerumAntibody() {
        return fhaIgmSerumAntibody;
    }

    @Order(1296)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaIggSerumAntibody() {
        return fhaIggSerumAntibody;
    }

    @Order(1297)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaIgaSerumAntibody() {
        return fhaIgaSerumAntibody;
    }

    @Order(1298)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaIncubationTime() {
        return fhaIncubationTime;
    }

    @Order(1299)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaIndirectFluorescentAntibody() {
        return fhaIndirectFluorescentAntibody;
    }

    @Order(1300)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaMicroscopy() {
        return fhaMicroscopy;
    }

    @Order(1301)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaNeutralizingAntibodies() {
        return fhaNeutralizingAntibodies;
    }

    @Order(1302)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaPcr() {
        return fhaPcr;
    }

    @Order(1303)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaGramStain() {
        return fhaGramStain;
    }

    @Order(1304)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaLatexAgglutination() {
        return fhaLatexAgglutination;
    }

    @Order(1305)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaCqValueDetection() {
        return fhaCqValueDetection;
    }

    @Order(1306)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaSeQuencing() {
        return fhaSeQuencing;
    }

    @Order(1307)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaDnaMicroArray() {
        return fhaDnaMicroArray;
    }

    @Order(1308)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getFhaOther() {
        return fhaOther;
    }

    @Order(1309)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherAntiBodyDetection() {
        return otherAntiBodyDetection;
    }

    @Order(1310)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherAntigenDetection() {
        return otherAntigenDetection;
    }

    @Order(1311)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherRapidTest() {
        return otherRapidTest;
    }

    @Order(1312)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherCulture() {
        return otherCulture;
    }

    @Order(1313)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherHistopathology() {
        return otherHistopathology;
    }

    @Order(1314)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherIsolation() {
        return otherIsolation;
    }

    @Order(1315)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherIgmSerumAntibody() {
        return otherIgmSerumAntibody;
    }

    @Order(1316)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherIggSerumAntibody() {
        return otherIggSerumAntibody;
    }

    @Order(1317)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherIgaSerumAntibody() {
        return otherIgaSerumAntibody;
    }

    @Order(1318)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherIncubationTime() {
        return otherIncubationTime;
    }

    @Order(1319)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherIndirectFluorescentAntibody() {
        return otherIndirectFluorescentAntibody;
    }

    @Order(1320)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherMicroscopy() {
        return otherMicroscopy;
    }

    @Order(1321)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherNeutralizingAntibodies() {
        return otherNeutralizingAntibodies;
    }

    @Order(1322)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherPcr() {
        return otherPcr;
    }

    @Order(1323)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherGramStain() {
        return otherGramStain;
    }

    @Order(1324)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherLatexAgglutination() {
        return otherLatexAgglutination;
    }

    @Order(1325)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherCqValueDetection() {
        return otherCqValueDetection;
    }

    @Order(1326)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherSeQuencing() {
        return otherSeQuencing;
    }

    @Order(1327)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherDnaMicroArray() {
        return otherDnaMicroArray;
    }

    @Order(1328)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getOtherOther() {
        return otherOther;
    }

    @Order(1329)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedAntiBodyDetection() {
        return undefinedAntiBodyDetection;
    }

    @Order(1330)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedAntigenDetection() {
        return undefinedAntigenDetection;
    }

    @Order(1331)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedRapidTest() {
        return undefinedRapidTest;
    }

    @Order(1332)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedCulture() {
        return undefinedCulture;
    }

    @Order(1333)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedHistopathology() {
        return undefinedHistopathology;
    }

    @Order(1334)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedIsolation() {
        return undefinedIsolation;
    }

    @Order(1335)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedIgmSerumAntibody() {
        return undefinedIgmSerumAntibody;
    }

    @Order(1336)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedIggSerumAntibody() {
        return undefinedIggSerumAntibody;
    }

    @Order(1337)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedIgaSerumAntibody() {
        return undefinedIgaSerumAntibody;
    }

    @Order(1338)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedIncubationTime() {
        return undefinedIncubationTime;
    }

    @Order(1339)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedIndirectFluorescentAntibody() {
        return undefinedIndirectFluorescentAntibody;
    }

    @Order(1340)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedMicroscopy() {
        return undefinedMicroscopy;
    }

    @Order(1341)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedNeutralizingAntibodies() {
        return undefinedNeutralizingAntibodies;
    }

    @Order(1342)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedPcr() {
        return undefinedPcr;
    }

    @Order(1343)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedGramStain() {
        return undefinedGramStain;
    }

    @Order(1344)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedLatexAgglutination() {
        return undefinedLatexAgglutination;
    }

    @Order(1345)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedCqValueDetection() {
        return undefinedCqValueDetection;
    }

    @Order(1346)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedSeQuencing() {
        return undefinedSeQuencing;
    }

    @Order(1347)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedDnaMicroArray() {
        return undefinedDnaMicroArray;
    }

    @Order(1348)
    @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    @ExportGroup(ExportGroupType.ADDITIONAL)
    public String getUndefinedOther() {
        return undefinedOther;
    }

    // @Order(1349)
    // @ExportTarget(caseExportTypes = { CaseExportType.CASE_SURVEILLANCE })
    // @ExportProperty(value = SAMPLE_INFORMATION, combined = true)
    // @ExportGroup(ExportGroupType.ADDITIONAL)
    // public String getOtherPathogenTests() {
    //     return otherPathogenTests;
    // }

    public static class SampleExportMaterial implements Serializable {

        private SampleMaterial sampleMaterial;
        @SensitiveData
        private String sampleMaterialDetails;

        public SampleExportMaterial(SampleMaterial sampleMaterial, String sampleMaterialDetails) {
            this.sampleMaterial = sampleMaterial;
            this.sampleMaterialDetails = sampleMaterialDetails;
        }

        public String formatString() {
            return SampleMaterial.toString(sampleMaterial, sampleMaterialDetails);
        }
    }


    public static class SampleExportPathogenTest implements Serializable {

        private PathogenTestType testType;
        @SensitiveData
        private String testTypeText;
        private String disease;
        private Date dateTime;
        private String lab;
        private PathogenTestResultType testResult;
        private Boolean verified;

        public SampleExportPathogenTest() {
        }

        public SampleExportPathogenTest(
                String testTypeText,
                String disease,
                Date dateTime,
                String lab,
                PathogenTestResultType testResult,
                Boolean verified) {
            this.testType = testType;
            this.testTypeText = testTypeText;
            this.disease = disease;
            this.dateTime = dateTime;
            this.lab = lab;
            this.testResult = testResult;
            this.verified = verified;
        }

        public String formatType() {
            return PathogenTestType.toString(testType, testTypeText);
        }

        public String formatString() {
            StringBuilder sb = new StringBuilder();
            sb.append(DateHelper.formatDateForExport(dateTime)).append(" (");
            String type = formatType();
            if (type.length() > 0) {
                sb.append(type).append(", ");
            }

            sb.append(disease).append(", ").append(testResult).append(")");

            return sb.toString();
        }
    }
}
