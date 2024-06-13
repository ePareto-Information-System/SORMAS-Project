/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.api.symptoms;

import static de.symeda.sormas.api.CountryHelper.COUNTRY_CODE_GERMANY;
import static de.symeda.sormas.api.CountryHelper.COUNTRY_CODE_SWITZERLAND;
import static de.symeda.sormas.api.Disease.*;

import java.util.Date;
import java.util.Set;

import javax.validation.constraints.Size;

import de.symeda.sormas.api.CountryHelper;
import de.symeda.sormas.api.ImportIgnore;
import de.symeda.sormas.api.caze.CaseOutcome;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.hospitalization.SymptomsList;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.utils.*;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableDto;

@DependingOnFeatureType(featureType = {
	FeatureType.CASE_SURVEILANCE,
	FeatureType.CONTACT_TRACING })
public class SymptomsDto extends PseudonymizableDto {

	private static final long serialVersionUID = 4146526547904182448L;

	public static final String I18N_PREFIX = "Symptoms";

	//Symptoms
	public static final String ABDOMINAL_PAIN = "abdominalPain";
	public static final String ABNORMAL_LUNG_XRAY_FINDINGS = "abnormalLungXrayFindings";
	public static final String ACUTE_RESPIRATORY_DISTRESS_SYNDROME = "acuteRespiratoryDistressSyndrome";
	public static final String AEROPHOBIA = "aerophobia";
	public static final String AGITATION = "agitation";
	public static final String ANOREXIA_APPETITE_LOSS = "anorexiaAppetiteLoss";
	public static final String ANXIETY_STATES = "anxietyStates";
	public static final String ASCENDING_FLACCID_PARALYSIS = "ascendingFlaccidParalysis";
	public static final String BACKACHE = "backache";
	public static final String BEDRIDDEN = "bedridden";
	public static final String BILATERAL_CATARACTS = "bilateralCataracts";
	public static final String BLACKENING_DEATH_OF_TISSUE = "blackeningDeathOfTissue";
	public static final String BLEEDING_VAGINA = "bleedingVagina";
	public static final String BLOODY_BLACK_STOOL = "bloodyBlackStool";
	public static final String BLOOD_IN_STOOL = "bloodInStool";
	public static final String BLOOD_PRESSURE_DIASTOLIC = "bloodPressureDiastolic";
	public static final String BLOOD_PRESSURE_SYSTOLIC = "bloodPressureSystolic";
	public static final String BLOOD_URINE = "bloodUrine";
	public static final String BUBOES_GROIN_ARMPIT_NECK = "buboesGroinArmpitNeck";
	public static final String BULGING_FONTANELLE = "bulgingFontanelle";
	public static final String CHEST_PAIN = "chestPain";
	public static final String CHILLS_SWEATS = "chillsSweats";
	public static final String COMA = "coma";
	public static final String CONGENITAL_GLAUCOMA = "congenitalGlaucoma";
	public static final String CONGENITAL_HEART_DISEASE = "congenitalHeartDisease";
	public static final String CONGENITAL_HEART_DISEASE_DETAILS = "congenitalHeartDiseaseDetails";
	public static final String CONGENITAL_HEART_DISEASE_TYPE = "congenitalHeartDiseaseType";
	public static final String CONJUNCTIVAL_INJECTION = "conjunctivalInjection";
	public static final String CONJUNCTIVITIS = "conjunctivitis";
	public static final String CONVULSION = "convulsion";
	public static final String COUGH = "cough";
	public static final String COUGHING_BLOOD = "coughingBlood";
	public static final String COUGH_WITH_HEAMOPTYSIS = "coughWithHeamoptysis";
	public static final String COUGH_WITH_SPUTUM = "coughWithSputum";
	public static final String DARK_URINE = "darkUrine";
	public static final String DEHYDRATION = "dehydration";
	public static final String DELIRIUM = "delirium";
	public static final String DEVELOPMENTAL_DELAY = "developmentalDelay";
	public static final String DIARRHEA = "diarrhea";
	public static final String DIFFICULTY_SWALLOWING = "difficultySwallow";
	public static final String SKIN_RASH_NEW = "skinRashNew";
	public static final String DIFFICULTY_BREATHING = "difficultyBreathing";
	public static final String DIGESTED_BLOOD_VOMIT = "digestedBloodVomit";
	public static final String DYSPHAGIA = "dysphagia";
	public static final String ERRATIC_BEHAVIOUR = "erraticBehaviour";
	public static final String EXCESS_SALIVATION = "excessSalivation";
	public static final String EXCITATION = "excitation";
	public static final String EYES_BLEEDING = "eyesBleeding";
	public static final String EYE_PAIN_LIGHT_SENSITIVE = "eyePainLightSensitive";
	public static final String FATIGUE_WEAKNESS = "fatigueWeakness";
	public static final String FEVER = "fever";
	public static final String BODY_ACHE = "bodyAche";
	public static final String FLUID_IN_LUNG_CAVITY = "fluidInLungCavity";
	public static final String FLUID_IN_LUNG_CAVITY_AUSCULTATION = "fluidInLungCavityAuscultation";
	public static final String FLUID_IN_LUNG_CAVITY_XRAY = "fluidInLungCavityXray";
	public static final String GLASGOW_COMA_SCALE = "glasgowComaScale";
	public static final String GUMS_BLEEDING = "gumsBleeding";
	public static final String HEADACHE = "headache";
	public static final String HEARINGLOSS = "hearingloss";
	public static final String HEART_RATE = "heartRate";
	public static final String HEIGHT = "height";
	public static final String HICCUPS = "hiccups";
	public static final String HYDROPHOBIA = "hydrophobia";
	public static final String HYPERACTIVITY = "hyperactivity";
	public static final String INABILITY_TO_WALK = "inabilityToWalk";
	public static final String INJECTION_SITE_BLEEDING = "injectionSiteBleeding";
	public static final String INSOMNIA = "insomnia";
	public static final String IN_DRAWING_OF_CHEST_WALL = "inDrawingOfChestWall";
	public static final String JAUNDICE = "jaundice";
	public static final String JAUNDICE_WITHIN_24_HOURS_OF_BIRTH = "jaundiceWithin24HoursOfBirth";
	public static final String JOINT_PAIN = "jointPain";
	public static final String KOPLIKS_SPOTS = "kopliksSpots";
	public static final String LESIONS = "lesions";
	public static final String LESIONS_ALL_OVER_BODY = "lesionsAllOverBody";
	public static final String LESIONS_ARMS = "lesionsArms";
	public static final String LESIONS_DEEP_PROFOUND = "lesionsDeepProfound";
	public static final String LESIONS_FACE = "lesionsFace";
	public static final String LESIONS_GENITALS = "lesionsGenitals";
	public static final String LESIONS_LEGS = "lesionsLegs";
	public static final String LESIONS_ONSET_DATE = "lesionsOnsetDate";
	public static final String LESIONS_PALMS_HANDS = "lesionsPalmsHands";
	public static final String LESIONS_RESEMBLE_IMG1 = "lesionsResembleImg1";
	public static final String LESIONS_RESEMBLE_IMG2 = "lesionsResembleImg2";
	public static final String LESIONS_RESEMBLE_IMG3 = "lesionsResembleImg3";
	public static final String LESIONS_RESEMBLE_IMG4 = "lesionsResembleImg4";
	public static final String LESIONS_SAME_SIZE = "lesionsSameSize";
	public static final String LESIONS_SAME_STATE = "lesionsSameState";
	public static final String LESIONS_SOLES_FEET = "lesionsSolesFeet";
	public static final String LESIONS_THAT_ITCH = "lesionsThatItch";
	public static final String LESIONS_THORAX = "lesionsThorax";
	public static final String LOSS_OF_SMELL = "lossOfSmell";
	public static final String LOSS_OF_TASTE = "lossOfTaste";
	public static final String LOSS_SKIN_TURGOR = "lossSkinTurgor";
	public static final String LYMPHADENOPATHY = "lymphadenopathy";
	public static final String LYMPHADENOPATHY_AXILLARY = "lymphadenopathyAxillary";
	public static final String LYMPHADENOPATHY_CERVICAL = "lymphadenopathyCervical";
	public static final String LYMPHADENOPATHY_INGUINAL = "lymphadenopathyInguinal";
	public static final String MALAISE = "malaise";
	public static final String MENINGOENCEPHALITIS = "meningoencephalitis";
	public static final String MICROCEPHALY = "microcephaly";
	public static final String MID_UPPER_ARM_CIRCUMFERENCE = "midUpperArmCircumference";
	public static final String MUSCLE_PAIN = "musclePain";
	public static final String NAUSEA = "nausea";
	public static final String NECK_STIFFNESS = "neckStiffness";
	public static final String NOSE_BLEEDING = "noseBleeding";
	public static final String OEDEMA_FACE_NECK = "oedemaFaceNeck";
	public static final String OEDEMA_LOWER_EXTREMITY = "oedemaLowerExtremity";
	public static final String ONSET_DATE = "onsetDate";
	public static final String DATE_OF_ONSET = "dateOfOnset";
	public static final String ONSET_SYMPTOM = "onsetSymptom";
	public static final String OPISTHOTONUS = "opisthotonus";
	public static final String ORAL_ULCERS = "oralUlcers";
	public static final String OTHER_HEMORRHAGIC_SYMPTOMS = "otherHemorrhagicSymptoms";
	public static final String OTHER_HEMORRHAGIC_SYMPTOMS_TEXT = "otherHemorrhagicSymptomsText";
	public static final String OTHER_NON_HEMORRHAGIC_SYMPTOMS = "otherNonHemorrhagicSymptoms";
	public static final String OTHER_NON_HEMORRHAGIC_SYMPTOMS_TEXT = "otherNonHemorrhagicSymptomsText";
	public static final String OTITIS_MEDIA = "otitisMedia";
	public static final String PAINFUL_LYMPHADENITIS = "painfulLymphadenitis";
	public static final String PALPABLE_LIVER = "palpableLiver";
	public static final String PALPABLE_SPLEEN = "palpableSpleen";
	public static final String PARALYSIS = "paralysis";
	public static final String PARASTHESIA_AROUND_WOUND = "paresthesiaAroundWound";
	public static final String PARESIS = "paresis";
	public static final String PATIENT_ILL_LOCATION = "patientIllLocation";
	public static final String PHARYNGEAL_ERYTHEMA = "pharyngealErythema";
	public static final String PHARYNGEAL_EXUDATE = "pharyngealExudate";
	public static final String PIGMENTARY_RETINOPATHY = "pigmentaryRetinopathy";
	public static final String PNEUMONIA_CLINICAL_OR_RADIOLOGIC = "pneumoniaClinicalOrRadiologic";
	public static final String PURPURIC_RASH = "purpuricRash";
	public static final String PAPULAR_RASH = "papularRash";
	public static final String MACULAR_RASH = "macularRash";
	public static final String VESICULAR_RASH = "vesicularRash";
	public static final String OTHER_LESION_AREAS = "otherLesionAreas";
	public static final String RADIOLUCENT_BONE_DISEASE = "radiolucentBoneDisease";
	public static final String RAPID_BREATHING = "rapidBreathing";
	public static final String RED_BLOOD_VOMIT = "redBloodVomit";
	public static final String REFUSAL_FEEDOR_DRINK = "refusalFeedorDrink";
	public static final String RESPIRATORY_RATE = "respiratoryRate";
	public static final String RUNNY_NOSE = "runnyNose";
	public static final String SIDE_PAIN = "sidePain";
	public static final String SKIN_BRUISING = "skinBruising";
	public static final String SKIN_RASH = "skinRash";
	public static final String RASHES = "rashes";
	public static final String SKIN_ULCERS = "skinUlcers";
	public static final String SORE_THROAT = "soreThroat";
	public static final String MUSCLE_TONE = "muscleTone";
	public static final String DEEP_TENDON_REFLEX = "deepTendonReflex";
	public static final String MUSCLE_VOLUME = "muscleVolume";
	public static final String SENSORY_LOSS = "sensoryLoss";
	public static final String SPLENOMEGALY = "splenomegaly";
	public static final String STOMACH_BLEEDING = "stomachBleeding";
	public static final String SUNKEN_EYES_FONTANELLE = "sunkenEyesFontanelle";
	public static final String SWOLLEN_GLANDS = "swollenGlands";
	public static final String SYMPTOMATIC = "symptomatic";
	public static final String SYMPTOMS_COMMENTS = "symptomsComments";
	public static final String TEMPERATURE = "temperature";
	public static final String TEMPERATURE_SOURCE = "temperatureSource";
	public static final String THROBOCYTOPENIA = "throbocytopenia";
	public static final String TREMOR = "tremor";
	public static final String UNEXPLAINED_BLEEDING = "unexplainedBleeding";
	public static final String UNILATERAL_CATARACTS = "unilateralCataracts";
	public static final String UPROARIOUSNESS = "uproariousness";
	public static final String VOMITING = "vomiting";
	public static final String WHEEZING = "wheezing";
	public static final String RESPIRATORY_DISEASE_VENTILATION = "respiratoryDiseaseVentilation";
	public static final String FEELING_ILL = "feelingIll";
	public static final String SHIVERING = "shivering";
	public static final String FAST_HEART_RATE = "fastHeartRate";
	public static final String OXYGEN_SATURATION_LOWER_94 = "oxygenSaturationLower94";
	public static final String GENERAL_BODILY_PAINS = "generalBodilyPains";
	public static final String LESIONS_NECK = "lesionsNeck";
	public static final String LESIONS_TRUNK = "lesionsTrunk";

	public static final String WEIGHT = "weight";

	public static final String FEVERISHFEELING = "feverishFeeling";
	public static final String WEAKNESS = "weakness";
	public static final String FATIGUE = "fatigue";
	public static final String COUGH_WITHOUT_SPUTUM = "coughWithoutSputum";
	public static final String BREATHLESSNESS = "breathlessness";
	public static final String CHEST_PRESSURE = "chestPressure";
	public static final String BLUE_LIPS = "blueLips";
	public static final String BLOOD_CIRCULATION_PROBLEMS = "bloodCirculationProblems";
	public static final String PALPITATIONS = "palpitations";
	public static final String DIZZINESS_STANDING_UP = "dizzinessStandingUp";
	public static final String HIGH_OR_LOW_BLOOD_PRESSURE = "highOrLowBloodPressure";
	public static final String URINARY_RETENTION = "urinaryRetention";

	// Complications
	public static final String ALTERED_CONSCIOUSNESS = "alteredConsciousness";
	public static final String CONFUSED_DISORIENTED = "confusedDisoriented";
	public static final String HEMORRHAGIC_SYNDROME = "hemorrhagicSyndrome";
	public static final String HYPERGLYCEMIA = "hyperglycemia";
	public static final String HYPOGLYCEMIA = "hypoglycemia";
	public static final String MENINGEAL_SIGNS = "meningealSigns";
	public static final String OTHER_COMPLICATIONS = "otherComplications";
	public static final String FEVER_BODY_TEMP_GREATER = "feverBodyTempGreater";
	public static final String OTHER_COMPLICATIONS_TEXT = "otherComplicationsText";
	public static final String SEIZURES = "seizures";
	public static final String SEPSIS = "sepsis";
	public static final String SHOCK = "shock";
	public static final String OUTCOME = "outcome";
	public static final String PROVISONAL_DIAGNOSIS = "provisionalDiagnosis";
	public static final String FEVER_ONSET_PARALYSIS = "feverOnsetParalysis";
	public static final String PROGRESSIVE_PARALYSIS = "progressiveParalysis";
	public static final String DATE_ONSET_PARALYSIS = "dateOnsetParalysis";
	public static final String PROGRESSIVE_FLACID_ACUTE = "progressiveFlaccidAcute";
	public static final String ASSYMETRIC = "assymetric";
	public static final String SITE_OF_PARALYSIS = "siteOfParalysis";
	public static final String PARALYSED_LIMB_SENSITIVE_TO_PAIN = "paralysedLimbSensitiveToPain";
	public static final String INJECTION_SITE_BEFORE_ONSET_PARALYSIS = "injectionSiteBeforeOnsetParalysis";
	public static final String RIGHT_INJECTION_SITE = "rightInjectionSite";
	public static final String LEFT_INJECTION_SITE = "leftInjectionSite";
	public static final String TRUEAFP = "trueAfp";
	public static final String DYSPNEA = "dyspnea";
	public static final String TACHYPNEA = "tachypnea";
	public static final String BABY_DIED = "babyDied";
	public static final String AGE_AT_DEATH_DAYS = "ageAtDeathDays";
	public static final String AGE_AT_ONSET_DAYS = "ageAtOnsetDays";
	public static final String BABY_NORMAL_AT_BIRTH = "babyNormalAtBirth";
	public static final String NORMAL_CRY_AND_SUCK = "normalCryAndSuck";
	public static final String STOPPED_SUCKING_AFTER_TWO_DAYS = "stoppedSuckingAfterTwoDays";
	public static final String STIFFNESS = "stiffness";
	public static final String OUTCOME_OTHER = "outcomeOther";


	public static final String NON_VASCULAR = "nonVascular";
	public static final String GENERALIZED_RASH = "generalizedRash";
	public static final String RED_EYES = "redEyes";
	public static final String SWOLLEN_LYMPH_NODES_BEHIND_EARS = "swollenLymphNodesBehindEars";

	public static final String SYMPTOMS_ONGOING = "symptomsOngoing";
	public static final String DURATION_HOURS = "durationHours";
	public static final String YES_NAME_OF_HEALTH_FACILITY = "nameOfHealthFacility";
	public static final String DIZZINESS = "dizziness";
	public static final String EXCESSIVE_SWEATING = "excessiveSweating";
	public static final String NUMBNESS = "numbness";
	public static final String SYMPTOMS_SELECTED = "symptomsSelected";
	public static final String SYMPTOMS_SELECTED_OTHER = "symptomsSelectedOther";
	public static final String DATE_OF_ONSET_RASH = "dateOfOnsetRash";
	public static final String RASH_SYMPTOMS = "rashSymptoms";
	public static final String RASH_SYMPTOMS_OTHER_AREAS = "rashSymptomsOtherAreas";
	public static final String ARE_LESIONS_IN_SAME_STATE = "areLesionsSameState";
	public static final String ARE_LESIONS_SAME_SIZE = "areLesionsSameSize";
	public static final String ARE_LESIONS_DEEP = "areLesionsDeep";
	public static final String ARE_ULCERS_AMONG_LESIONS = "areUlcersAmong";
	public static final String TYPE_OF_RASH = "typeOfRash";
	public static final String PATIENT_HAVE_FEVER = "patientHaveFever";
	public static final String OUTCOME_DATE = "outcomeDate";
	public static final String OUTCOME_PLACE_COMM_VILLAGE = "outcomePlaceCommVillage";
	public static final String HOSPITAL = "hospital";
	public static final String HOSPITAL_NAME_SERVICE = "nameService";
	public static final String PLACE_OF_FUNERAL_NAME_VILLAGE = "placeOfFuneralNameVillage";

	public static final String FIRST_SIGN_OR_SYMPTOMS_BEFORE_WORM = "firstSignOrSymptomsBeforeWorm";
	public static final String FIRST_SIGN_OR_SYMPTOMS_BEFORE_WORM_OTHERS = "firstSignOrSymptomsBeforeWormOthers";
	public static final String EMERGENCE_OF_GUINEA_WORM = "emergenceOfGuineaWorm";
	public static final String NUMBER_OF_WORMS = "numberOfWorms";
	public static final String FIRST_WORM_THIS_YEAR = "firstWormThisYear";
	public static final String DATE_FIRST_WORM_EMERGENCE = "dateFirstWormEmergence";
	public static final String CASE_DETECTED_BEFORE_WORM_EMERGENCE = "caseDetectedBeforeWormEmergence";

	public static final String DIARRHOEA = "diarrhoea";
	public static final String ABDOMINAL_CRAMPS = "abdominalCramps";
	public static final String HEADACHES = "headaches";

	// Fields are declared in the order they should appear in the import template

	public static SymptomsDto build() {
		SymptomsDto symptoms = new SymptomsDto();
		symptoms.setUuid(DataHelper.createUuid());
		return symptoms;
	}

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA,
		SARI,
		YELLOW_FEVER,
		DENGUE,
		PLAGUE,
		POLIO,
		ANTHRAX,
		CORONAVIRUS,
		AHF,
		DENGUE,
		UNDEFINED,
		FOODBORNE_ILLNESS,
		CHOLERA,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState abdominalPain;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		CSM,
		POLIO,
		YELLOW_FEVER,
		AHF,
		DENGUE,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState anorexiaAppetiteLoss;

	@Diseases({
		AFP,
		POLIO,
		YELLOW_FEVER,
		AHF,
		DENGUE,
		UNDEFINED,
		NEONATAL_TETANUS,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState backache;

	@Diseases({
		AFP,
		MONKEYPOX,
		POLIO,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState bedridden;

	@Diseases({
		AFP,
		PLAGUE,
		POLIO,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState blackeningDeathOfTissue;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		POLIO,
		AHF,
		DENGUE,
		UNDEFINED,
		OTHER })
	@DependantOn(UNEXPLAINED_BLEEDING)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState bleedingVagina;

	@Diseases({
		AFP,
		GUINEA_WORM,
		POLIO,
		YELLOW_FEVER,
		ANTHRAX,
		AHF,
		DENGUE,
		UNDEFINED,
		FOODBORNE_ILLNESS,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState bloodInStool;

	private Integer bloodPressureDiastolic;

	private Integer bloodPressureSystolic;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		POLIO,
		AHF,
		DENGUE,
		UNDEFINED,
		OTHER })
	@DependantOn(UNEXPLAINED_BLEEDING)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState bloodUrine;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		POLIO,
		AHF,
		DENGUE,
		UNDEFINED,
		OTHER })
	@DependantOn(UNEXPLAINED_BLEEDING)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState bloodyBlackStool;

	@Diseases({
		AFP,
		PLAGUE,
		POLIO,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState buboesGroinArmpitNeck;

	@Diseases({
		AFP,
		CSM,
		POLIO,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState bulgingFontanelle;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		PLAGUE,
		POLIO,
		ANTHRAX,
		CORONAVIRUS,
		AHF,
		DENGUE,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@HideForCountries
	private SymptomState chestPain;

	@Diseases({
		AFP,
		MONKEYPOX,
		PLAGUE,
		ANTHRAX,
		POLIO,
		CORONAVIRUS,
		UNDEFINED,
		YELLOW_FEVER,
		FOODBORNE_ILLNESS,
		OTHER })
	@Outbreaks
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState chillsSweats;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		MEASLES,
		MONKEYPOX,
		POLIO,
		AHF,
		DENGUE,
		UNDEFINED,
		OTHER })
	@HideForCountries(countries = {
		CountryHelper.COUNTRY_CODE_GERMANY,
		CountryHelper.COUNTRY_CODE_SWITZERLAND })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState conjunctivitis;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		MEASLES,
		MONKEYPOX,
		PLAGUE,
		POLIO,
		ANTHRAX,
		AHF,
		DENGUE,
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@HideForCountries(countries = {
		CountryHelper.COUNTRY_CODE_SWITZERLAND })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState cough;

	@Diseases({
			AFP,
			EVD,
			LASSA,
			NEW_INFLUENZA, SARI,
			MEASLES,
			MONKEYPOX,
			PLAGUE,
			POLIO,
			ANTHRAX,
			AHF,
			DENGUE,
			CORONAVIRUS,
			UNDEFINED,
			OTHER })
	@Outbreaks
	@HideForCountries(countries = {
			CountryHelper.COUNTRY_CODE_SWITZERLAND })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState soreThroat;

	@Diseases({
		CORONAVIRUS })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState coughWithSputum;

	@Diseases({
		CORONAVIRUS })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState coughWithHeamoptysis;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		PLAGUE,
		POLIO,
		AHF,
		DENGUE,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@DependantOn(UNEXPLAINED_BLEEDING)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState coughingBlood;

	@Diseases({
		AFP,
		POLIO,
		YELLOW_FEVER,
		AHF,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState darkUrine;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		POLIO,
		AHF,
		DENGUE,
		UNDEFINED,
		FOODBORNE_ILLNESS,
		CHOLERA,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState dehydration;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		PLAGUE,
		POLIO,
		ANTHRAX,
		CORONAVIRUS,
		AHF,
		UNDEFINED,
		FOODBORNE_ILLNESS,
		OTHER })
	@Outbreaks
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState diarrhea;

	@Diseases({
			AFP,
			EVD,
			LASSA,
			NEW_INFLUENZA, SARI,
			CSM,
			MEASLES,
			PLAGUE,
			POLIO,
			ANTHRAX,
			CORONAVIRUS,
			AHF,
			UNDEFINED,
			FOODBORNE_ILLNESS,
			OTHER })
	@Outbreaks
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState difficultySwallow;

	@Diseases({
			AFP,
			EVD,
			LASSA,
			NEW_INFLUENZA, SARI,
			CSM,
			MEASLES,
			PLAGUE,
			POLIO,
			ANTHRAX,
			CORONAVIRUS,
			AHF,
			UNDEFINED,
			FOODBORNE_ILLNESS,
			OTHER })
	@Outbreaks
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState skinRashNew;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		MEASLES,
		PLAGUE,
		POLIO,
		AHF,
		DENGUE,
		RABIES,
		ANTHRAX,
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState difficultyBreathing;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		POLIO,
		AHF,
		DENGUE,
		UNDEFINED,
		OTHER })
	@DependantOn(UNEXPLAINED_BLEEDING)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState digestedBloodVomit;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		DENGUE,
		MONKEYPOX,
		POLIO,
		AHF,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState eyePainLightSensitive;

	@Diseases({
		AFP,
		POLIO,
		YELLOW_FEVER,
		AHF,
		DENGUE,
		UNDEFINED,
		OTHER })
	@DependantOn(UNEXPLAINED_BLEEDING)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState eyesBleeding;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		YELLOW_FEVER,
		DENGUE,
		MONKEYPOX,
		PLAGUE,
		POLIO,
		AHF,
		RABIES,
		ANTHRAX,
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@HideForCountries(countries = {
		CountryHelper.COUNTRY_CODE_GERMANY,
		CountryHelper.COUNTRY_CODE_SWITZERLAND })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState fatigueWeakness;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		YELLOW_FEVER,
		DENGUE,
		MONKEYPOX,
		PLAGUE,
		POLIO,
		AHF,
		RABIES,
		ANTHRAX,
		CORONAVIRUS,
		UNDEFINED,
		FOODBORNE_ILLNESS,
		CHOLERA,
		OTHER })
	@Outbreaks
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState fever;

	@Diseases({
		AFP,
		LASSA,
		POLIO,
		AHF,
		UNDEFINED,
		OTHER })
	@HideForCountries(countries = {
		CountryHelper.COUNTRY_CODE_GERMANY,
		CountryHelper.COUNTRY_CODE_SWITZERLAND })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState fluidInLungCavity;

	@SymptomGrouping(SymptomGroup.GENERAL)
	private Integer glasgowComaScale;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		POLIO,
		YELLOW_FEVER,
		DENGUE,
		AHF,
		UNDEFINED,
		OTHER })
	@DependantOn(UNEXPLAINED_BLEEDING)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState gumsBleeding;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		YELLOW_FEVER,
		DENGUE,
		MONKEYPOX,
		PLAGUE,
		POLIO,
		AHF,
		RABIES,
		ANTHRAX,
		CORONAVIRUS,
		UNDEFINED,
		FOODBORNE_ILLNESS,
		OTHER })
	@Outbreaks
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState headache;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		POLIO,
		AHF,
		CONGENITAL_RUBELLA,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState hearingloss;
	@Diseases({
		YELLOW_FEVER})
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState bodyAche;

	private Integer heartRate;

	private Integer height;

	@Diseases({
		AFP,
		EVD,
		POLIO,
		AHF,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState hiccups;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		POLIO,
		AHF,
		UNDEFINED,
		OTHER })
	@DependantOn(UNEXPLAINED_BLEEDING)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState injectionSiteBleeding;

	@Diseases({
		AFP,
		YELLOW_FEVER,
		LASSA,
		POLIO,
		AHF,
		CONGENITAL_RUBELLA,
		UNDEFINED,
		FOODBORNE_ILLNESS,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState jaundice;

	@Diseases({
		CONGENITAL_RUBELLA })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private YesNoUnknown jaundiceWithin24HoursOfBirth;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		POLIO,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		DENGUE,
		CORONAVIRUS,
		AHF,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@HideForCountries(countries = {
		CountryHelper.COUNTRY_CODE_GERMANY,
		CountryHelper.COUNTRY_CODE_SWITZERLAND })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState jointPain;

	@Diseases({
		AFP,
		MEASLES,
		POLIO,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState kopliksSpots;

	@Diseases({
		AFP,
		MONKEYPOX,
		ANTHRAX,
		POLIO,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	/** Vesiculopustular rash */
	private SymptomState lesions;

	@Diseases({
		AFP,
		MONKEYPOX,
		POLIO,
		UNDEFINED,
		OTHER })
	@DependantOn(LESIONS)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private Boolean lesionsAllOverBody;

	@Diseases({
		AFP,
		MONKEYPOX,
		ANTHRAX,
		POLIO,
		UNDEFINED,
		OTHER })
	@DependantOn(LESIONS)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private Boolean lesionsArms;

	@Diseases({
		AFP,
		MONKEYPOX,
		ANTHRAX,
		POLIO,
		UNDEFINED,
		OTHER })
	@DependantOn(LESIONS)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState lesionsDeepProfound;

	@Diseases({
		AFP,
		MONKEYPOX,
		POLIO,
		UNDEFINED,
		OTHER })
	@DependantOn(LESIONS)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private Boolean lesionsFace;

	@Diseases({
		AFP,
		MONKEYPOX,
		POLIO,
		UNDEFINED,
		OTHER })
	@DependantOn(LESIONS)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private Boolean lesionsGenitals;

	@Diseases({
		AFP,
		MONKEYPOX,
		POLIO,
		UNDEFINED,
		OTHER })
	@DependantOn(LESIONS)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private Boolean lesionsLegs;

	@Diseases({
		MONKEYPOX,
		ANTHRAX })
	@DependantOn(LESIONS)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private Date lesionsOnsetDate;

	@Diseases({
		AFP,
		MONKEYPOX,
		POLIO,
		UNDEFINED,
		OTHER })
	@DependantOn(LESIONS)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private Boolean lesionsPalmsHands;

	@Diseases({
		MONKEYPOX })
	@DependantOn(LESIONS)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState lesionsResembleImg1;
	@Diseases({
		MONKEYPOX })
	@DependantOn(LESIONS)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState lesionsResembleImg2;

	@Diseases({
		MONKEYPOX })
	@DependantOn(LESIONS)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState lesionsResembleImg3;

	@Diseases({
		MONKEYPOX })
	@DependantOn(LESIONS)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState lesionsResembleImg4;

	@Diseases({
		AFP,
		MONKEYPOX,
		POLIO,
		UNDEFINED,
		OTHER })
	@DependantOn(LESIONS)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState lesionsSameSize;

	@Diseases({
		AFP,
		MONKEYPOX,
		POLIO,
		UNDEFINED,
		OTHER })
	@DependantOn(LESIONS)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState lesionsSameState;

	@Diseases({
		AFP,
		MONKEYPOX,
		POLIO,
		UNDEFINED,
		OTHER })
	@DependantOn(LESIONS)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private Boolean lesionsSolesFeet;

	@Diseases({
		AFP,
		MONKEYPOX,
		POLIO,
		UNDEFINED,
		OTHER })
	@DependantOn(LESIONS)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState lesionsThatItch;

	@Diseases({
		AFP,
		MONKEYPOX,
		POLIO,
		UNDEFINED,
		OTHER })
	@DependantOn(RASHES)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private Boolean lesionsThorax;

	@Diseases({
		AFP,
		LASSA,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState lossSkinTurgor;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountries(countries = {
		CountryHelper.COUNTRY_CODE_GERMANY,
		CountryHelper.COUNTRY_CODE_SWITZERLAND })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState lymphadenopathy;

	@Diseases({
		AFP,
		MONKEYPOX,
		POLIO,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState lymphadenopathyAxillary;

	@Diseases({
		AFP,
		MONKEYPOX,
		POLIO,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState lymphadenopathyCervical;

	@Diseases({
		AFP,
		MONKEYPOX,
		POLIO,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState lymphadenopathyInguinal;

	@Diseases({
		AFP,
		LASSA,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState malaise;

	private Integer midUpperArmCircumference;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		YELLOW_FEVER,
		DENGUE,
		MONKEYPOX,
		PLAGUE,
		POLIO,
		CORONAVIRUS,
		AHF,
		UNDEFINED,
		FOODBORNE_ILLNESS,
		OTHER })
	@Outbreaks
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState musclePain;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		YELLOW_FEVER,
		DENGUE,
		MONKEYPOX,
		PLAGUE,
		AHF,
		POLIO,
		RABIES,
		ANTHRAX,
		CORONAVIRUS,
		UNDEFINED,
		FOODBORNE_ILLNESS,
		OTHER })
	@Outbreaks
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState nausea;

	@Diseases({
		AFP,
		CSM,
		POLIO,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState neckStiffness;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		POLIO,
		YELLOW_FEVER,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@DependantOn(UNEXPLAINED_BLEEDING)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState noseBleeding;

	@Diseases({
		AFP,
		LASSA,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState oedemaFaceNeck;

	@Diseases({
		AFP,
		LASSA,
		POLIO,
		AHF,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState oedemaLowerExtremity;

	@Outbreaks
	private Date onsetDate;

	@Outbreaks
	private Date dateOfOnset;

	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String onsetSymptom;

	@Diseases({
		AFP,
		MONKEYPOX,
		POLIO,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState oralUlcers;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		PLAGUE,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@DependantOn(UNEXPLAINED_BLEEDING)
	@HideForCountries(countries = {
		CountryHelper.COUNTRY_CODE_GERMANY,
		CountryHelper.COUNTRY_CODE_SWITZERLAND })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState otherHemorrhagicSymptoms;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		PLAGUE,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@DependantOn(OTHER_HEMORRHAGIC_SYMPTOMS)
	@SensitiveData
	@SymptomGrouping(SymptomGroup.GENERAL)
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String otherHemorrhagicSymptomsText;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		DENGUE,
		MONKEYPOX,
		PLAGUE,
		AHF,
		CONGENITAL_RUBELLA,
		POLIO,
		RABIES,
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState otherNonHemorrhagicSymptoms;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		DENGUE,
		MONKEYPOX,
		PLAGUE,
		POLIO,
		AHF,
		CONGENITAL_RUBELLA,
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@DependantOn(OTHER_NON_HEMORRHAGIC_SYMPTOMS)
	@SensitiveData
	@SymptomGrouping(SymptomGroup.GENERAL)
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String otherNonHemorrhagicSymptomsText;

	@Diseases({
			AFP,
			GUINEA_WORM,
			NEW_INFLUENZA, SARI,
			MEASLES,
			POLIO,
			UNDEFINED,
			OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState otitisMedia;

	@Diseases({
		AFP,
		PLAGUE,
		POLIO,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState painfulLymphadenitis;

	@Diseases({
		AFP,
		LASSA,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState palpableLiver;

	@Diseases({
		AFP,
		LASSA,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState palpableSpleen;

	@Diseases({
		AFP,
		MONKEYPOX,
		POLIO,
		UNDEFINED,
		OTHER })
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String patientIllLocation;

	@Diseases({
		AFP,
		LASSA,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState pharyngealErythema;

	@Diseases({
		AFP,
		LASSA,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState pharyngealExudate;

	@Diseases({
		AFP,
		CORONAVIRUS,
		DENGUE,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState rapidBreathing;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		DENGUE,
		POLIO,
		AHF,
		UNDEFINED,
		OTHER })
	@DependantOn(UNEXPLAINED_BLEEDING)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState redBloodVomit;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		CSM,
		POLIO,
		AHF, DENGUE,
		RABIES,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState refusalFeedorDrink;

	private Integer respiratoryRate;

	@Diseases({
			AFP,
			GUINEA_WORM,
			NEW_INFLUENZA, SARI,
			MEASLES,
			POLIO,
			CORONAVIRUS,
			UNDEFINED,
			OTHER })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState runnyNose;

	@Diseases({
		AFP,
		LASSA,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState sidePain;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@DependantOn(UNEXPLAINED_BLEEDING)
	@HideForCountries(countries = {
		CountryHelper.COUNTRY_CODE_GERMANY,
		CountryHelper.COUNTRY_CODE_SWITZERLAND })
	private SymptomState skinBruising;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		DENGUE,
		MONKEYPOX,
		POLIO,
		AHF,
		UNDEFINED,
		OTHER,
		CORONAVIRUS })
	@Outbreaks
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	/** Maculopapular rash */
	private SymptomState skinRash;

	@Diseases({
			MEASLES})
	@Outbreaks
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState generalizedRash;

	@Diseases({
			MEASLES
	})
	@Outbreaks
	@HideForCountries
	@SymptomGrouping
	private SymptomState redEyes;

	@Diseases({
			MEASLES
	})
	@Outbreaks
	@HideForCountries
	@SymptomGrouping
	private SymptomState swollenLymphNodesBehindEars;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA,
		CSM,
		MEASLES,
		DENGUE,
		MONKEYPOX,
		POLIO,
		UNSPECIFIED_VHF,
		UNDEFINED,
		OTHER,
		CORONAVIRUS })
	@Outbreaks
	@HideForCountries
	@SymptomGrouping(SymptomGroup.RASH_TYPE)
	private SymptomState papularRash;

	@Diseases({
			AFP,
			GUINEA_WORM,
			MONKEYPOX,
			ANTHRAX,
			POLIO,
			UNDEFINED,
			OTHER })
	@SymptomGrouping(SymptomGroup.RASH)
	/** rashes */
	private SymptomState rashes;

	@Diseases({
			AFP,
			GUINEA_WORM,
			MONKEYPOX,
			ANTHRAX,
			POLIO,
			UNDEFINED,
			OTHER })
	@SymptomGrouping(SymptomGroup.RASH_TYPE)
	private SymptomState vesicularRash;

	@Diseases({
			AFP,
			GUINEA_WORM,
			MONKEYPOX,
			POLIO,
			UNDEFINED,
			OTHER })
	@DependantOn(RASHES)
	@SymptomGrouping(SymptomGroup.RASH_CHARACTERISTICS)
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String otherLesionAreas;

	@Diseases({
		AFP,
		EVD,
		GUINEA_WORM,
		LASSA,
		NEW_INFLUENZA,
		CSM,
		MEASLES,
		DENGUE,
		MONKEYPOX,
		POLIO,
		UNSPECIFIED_VHF,
		UNDEFINED,
		OTHER,
		CORONAVIRUS })
	@SymptomGrouping(SymptomGroup.RASH_TYPE)
	private SymptomState macularRash;

	@Diseases({
			AFP,
			UNDEFINED,
			OTHER })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState muscleTone;

	@Diseases({
			AFP,
			UNDEFINED,
			OTHER })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState deepTendonReflex;

	@Diseases({
			AFP,
			UNDEFINED,
			OTHER })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState muscleVolume;

	@Diseases({
			AFP,
			UNDEFINED,
			OTHER })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState sensoryLoss;


	@Diseases({
		AFP,
		POLIO,
		YELLOW_FEVER,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@DependantOn(UNEXPLAINED_BLEEDING)
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState stomachBleeding;

	@Diseases({
		AFP,
		LASSA,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState sunkenEyesFontanelle;

	@Diseases({
		AFP,
		DENGUE,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState swollenGlands;

	private Boolean symptomatic;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		DENGUE,
		MONKEYPOX,
		PLAGUE,
		POLIO,
		AHF,
		CONGENITAL_RUBELLA,
		CORONAVIRUS,
		NEONATAL_TETANUS,
		UNDEFINED,
		OTHER })
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String symptomsComments;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		YELLOW_FEVER,
		DENGUE,
		MONKEYPOX,
		PLAGUE,
		AHF,
		CONGENITAL_RUBELLA,
		POLIO,
		RABIES,
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@Outbreaks
	private Float temperature;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		YELLOW_FEVER,
		DENGUE,
		MONKEYPOX,
		PLAGUE,
		AHF,
		CONGENITAL_RUBELLA,
		POLIO,
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@Outbreaks
	private TemperatureSource temperatureSource;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState throbocytopenia;

	@Diseases({
		AFP,
		LASSA,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@HideForCountries
	private SymptomState tremor;

	@Diseases({
		CONGENITAL_RUBELLA })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState bilateralCataracts;

	@Diseases({
		CONGENITAL_RUBELLA })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState unilateralCataracts;

	@Diseases({
		CONGENITAL_RUBELLA })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState congenitalGlaucoma;

	@Diseases({
		CONGENITAL_RUBELLA })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState pigmentaryRetinopathy;

	@Diseases({
		CONGENITAL_RUBELLA })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState purpuricRash;

	@Diseases({
		CONGENITAL_RUBELLA })
	@HideForCountries
	private SymptomState microcephaly;

	@Diseases({
		CONGENITAL_RUBELLA })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState developmentalDelay;

	@Diseases({
		CONGENITAL_RUBELLA })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState splenomegaly;

	@Diseases({
		CONGENITAL_RUBELLA })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.NERVOUS_SYSTEM)
	private SymptomState meningoencephalitis;

	@Diseases({
		CONGENITAL_RUBELLA })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState radiolucentBoneDisease;

	@Diseases({
		CONGENITAL_RUBELLA })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState congenitalHeartDisease;

	@Diseases({
		CONGENITAL_RUBELLA })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private CongenitalHeartDiseaseType congenitalHeartDiseaseType;

	@Diseases({
		CONGENITAL_RUBELLA })
	@HideForCountries
	@SensitiveData
	@SymptomGrouping(SymptomGroup.GENERAL)
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String congenitalHeartDiseaseDetails;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		YELLOW_FEVER,
		DENGUE,
		PLAGUE,
		AHF,
		UNDEFINED,
		POLIO,
		OTHER,
		CORONAVIRUS })
	@HideForCountries(countries = {
		CountryHelper.COUNTRY_CODE_GERMANY,
		CountryHelper.COUNTRY_CODE_SWITZERLAND })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState unexplainedBleeding;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		YELLOW_FEVER,
		DENGUE,
		MONKEYPOX,
		PLAGUE,
		ANTHRAX,
		AHF,
		UNDEFINED,
		POLIO,
		CORONAVIRUS,
		FOODBORNE_ILLNESS,
		CHOLERA,
		OTHER })
	@Outbreaks
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState vomiting;

	@Diseases({
		RABIES })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.NERVOUS_SYSTEM)
	private SymptomState hydrophobia;

	@Diseases({
		RABIES })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.NERVOUS_SYSTEM)
	private SymptomState opisthotonus;

	@Diseases({
		RABIES })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.NERVOUS_SYSTEM)
	private SymptomState anxietyStates;

	@Diseases({
		RABIES })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.NERVOUS_SYSTEM)
	private SymptomState delirium;

	@Diseases({
		RABIES })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.NERVOUS_SYSTEM)
	private SymptomState uproariousness;

	@Diseases({
		RABIES })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState paresthesiaAroundWound;

	@Diseases({
		RABIES })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState excessSalivation;

	@Diseases({
		RABIES })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState insomnia;

	@Diseases({
		RABIES })
	@HideForCountries
	private SymptomState paralysis;

	@Diseases({
		RABIES })
	@HideForCountries
	private SymptomState excitation;

	@Diseases({
		RABIES })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.NERVOUS_SYSTEM)
	private SymptomState dysphagia;

	@Diseases({
		RABIES })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.NERVOUS_SYSTEM)
	private SymptomState aerophobia;

	@Diseases({
		RABIES })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.NERVOUS_SYSTEM)
	private SymptomState hyperactivity;

	@Diseases({
		RABIES })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.NERVOUS_SYSTEM)
	private SymptomState paresis;

	@Diseases({
		RABIES })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.NERVOUS_SYSTEM)
	private SymptomState agitation;

	@Diseases({
		RABIES })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.NERVOUS_SYSTEM)
	private SymptomState ascendingFlaccidParalysis;

	@Diseases({
		RABIES })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.NERVOUS_SYSTEM)
	private SymptomState erraticBehaviour;

	@Diseases({
		RABIES,
		CORONAVIRUS })
	@HideForCountries(countries = {
		CountryHelper.COUNTRY_CODE_GERMANY,
		CountryHelper.COUNTRY_CODE_SWITZERLAND })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState coma;

	@Diseases({
		ANTHRAX,
		ANTHRAX,
		FOODBORNE_ILLNESS})
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState convulsion;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountries(countries = {
		CountryHelper.COUNTRY_CODE_GERMANY,
		CountryHelper.COUNTRY_CODE_SWITZERLAND })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState fluidInLungCavityAuscultation;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountries(countries = {
		CountryHelper.COUNTRY_CODE_GERMANY,
		CountryHelper.COUNTRY_CODE_SWITZERLAND })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState fluidInLungCavityXray;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountries(countries = {
		CountryHelper.COUNTRY_CODE_GERMANY,
		CountryHelper.COUNTRY_CODE_SWITZERLAND })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState abnormalLungXrayFindings;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountries(countries = {
		CountryHelper.COUNTRY_CODE_GERMANY,
		CountryHelper.COUNTRY_CODE_SWITZERLAND })
	private SymptomState conjunctivalInjection;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountries(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState acuteRespiratoryDistressSyndrome;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountries(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState pneumoniaClinicalOrRadiologic;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState lossOfTaste;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState lossOfSmell;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountries(countries = {
		CountryHelper.COUNTRY_CODE_GERMANY,
		CountryHelper.COUNTRY_CODE_SWITZERLAND })
	private SymptomState wheezing;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		MONKEYPOX,
		OTHER })
	@HideForCountries(countries = {
		CountryHelper.COUNTRY_CODE_GERMANY,
		CountryHelper.COUNTRY_CODE_SWITZERLAND })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState skinUlcers;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountries(countries = {
		CountryHelper.COUNTRY_CODE_GERMANY,
		CountryHelper.COUNTRY_CODE_SWITZERLAND })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState inabilityToWalk;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountries(countries = {
		CountryHelper.COUNTRY_CODE_GERMANY,
		CountryHelper.COUNTRY_CODE_SWITZERLAND })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState inDrawingOfChestWall;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountriesExcept
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState respiratoryDiseaseVentilation;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountriesExcept(countries = {
		COUNTRY_CODE_GERMANY,
		COUNTRY_CODE_SWITZERLAND })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState feelingIll;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountriesExcept(countries = {
		CountryHelper.COUNTRY_CODE_GERMANY,
		CountryHelper.COUNTRY_CODE_SWITZERLAND })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState fastHeartRate;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountriesExcept
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState oxygenSaturationLower94;

	private Integer weight;

	// complications

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		POLIO,
		AHF, DENGUE,
		RABIES,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@Complication
	@HideForCountries
	private SymptomState alteredConsciousness;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		PLAGUE,
		POLIO,
		AHF, DENGUE,
		RABIES,
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@Complication
	@HideForCountries
	private SymptomState confusedDisoriented;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		POLIO,
		YELLOW_FEVER,
		DENGUE,
		PLAGUE,
		AHF,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@Complication
	@HideForCountries
	private SymptomState hemorrhagicSyndrome;

	@Diseases({
		AFP,
		CSM,
		LASSA,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@Complication
	@HideForCountries
	private SymptomState hyperglycemia;

	@Diseases({
		AFP,
		CSM,
		LASSA,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@Complication
	@HideForCountries
	private SymptomState hypoglycemia;

	@Diseases({
		AFP,
		CSM,
		LASSA,
		POLIO,
		AHF, DENGUE,
		RABIES,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@Complication
	@HideForCountries
	private SymptomState meningealSigns;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		GUINEA_WORM,
		POLIO,
		AHF,
		RABIES,
		YELLOW_FEVER,
		DENGUE,
		PLAGUE,
		ANTHRAX,
		CORONAVIRUS,
		UNDEFINED,
		NEONATAL_TETANUS,
		FOODBORNE_ILLNESS,
		MONKEYPOX,
		OTHER })
	@Complication
	@HideForCountries
	private SymptomState otherComplications;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		POLIO,
		AHF,
		RABIES,
		YELLOW_FEVER,
		DENGUE,
		PLAGUE,
		ANTHRAX,
		CORONAVIRUS,
		UNDEFINED,
		NEONATAL_TETANUS,
		FOODBORNE_ILLNESS,
		MONKEYPOX,
		OTHER })
	@DependantOn(OTHER_COMPLICATIONS)
	@Complication
	@HideForCountries
	@SensitiveData
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String otherComplicationsText;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		POLIO,
		AHF, DENGUE,
		RABIES,
		CORONAVIRUS,
		UNDEFINED,
		NEONATAL_TETANUS,
		OTHER })
	@Outbreaks
	@Complication
	@HideForCountries
	private SymptomState seizures;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@Complication
	@HideForCountries
	private SymptomState sepsis;

	@Diseases({
		AFP,
		EVD,
		LASSA,
		NEW_INFLUENZA, SARI,
		CSM,
		MEASLES,
		PLAGUE,
		ANTHRAX,
		POLIO,
		AHF, DENGUE,
		UNDEFINED,
		OTHER })
	@Outbreaks
	@Complication
	@HideForCountries
	private SymptomState shock;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState feverishFeeling;
	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		FOODBORNE_ILLNESS,
		OTHER })
	@Outbreaks
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState weakness;
	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		CHOLERA,
		AHF,
		OTHER })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState fatigue;
	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState coughWithoutSputum;
	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		AHF, DENGUE,
		OTHER })
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState breathlessness;
	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState chestPressure;
	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState blueLips;
	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState bloodCirculationProblems;
	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState palpitations;
	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState dizzinessStandingUp;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState highOrLowBloodPressure;

	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountriesExcept(countries = CountryHelper.COUNTRY_CODE_SWITZERLAND)
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState urinaryRetention;

	@Diseases({
			MONKEYPOX })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState generalBodilyPains;

	@Diseases({
			AFP,
			GUINEA_WORM,
			MONKEYPOX,
			POLIO,
			UNDEFINED,
			OTHER })
	@DependantOn(RASHES)
	@SymptomGrouping(SymptomGroup.GENERAL)
	private Boolean lesionsNeck;

	@Diseases({
			AFP,
			GUINEA_WORM,
			MONKEYPOX,
			POLIO,
			UNDEFINED,
			OTHER })
	@DependantOn(RASHES)
	@SymptomGrouping(SymptomGroup.GENERAL)
	private Boolean lesionsTrunk;
	@Diseases({
		CORONAVIRUS,
		UNDEFINED,
		OTHER })
	@HideForCountriesExcept(countries = {
		COUNTRY_CODE_GERMANY,
		COUNTRY_CODE_SWITZERLAND })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState shivering;

	@Diseases({
			MEASLES})
	@SymptomGrouping(SymptomGroup.OTHER)
	private SymptomState nonVascular;

	@Diseases({
		FOODBORNE_ILLNESS,
		OTHER })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState dizziness;
	@Diseases({
		FOODBORNE_ILLNESS,
		OTHER })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState excessiveSweating;
	@Diseases({
		FOODBORNE_ILLNESS,
		OTHER })
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState numbness;
	private YesNo feverBodyTempGreater;
	@Outbreaks
	private CaseOutcome outcome;
	private String provisionalDiagnosis;

	private YesNoUnknown feverOnsetParalysis;
	private YesNoUnknown progressiveParalysis;
	private Date dateOnsetParalysis;
	private YesNoUnknown progressiveFlaccidAcute;
	private YesNoUnknown assymetric;
	private YesNo paralysedLimbSensitiveToPain;
	private YesNo injectionSiteBeforeOnsetParalysis;
	private InjectionSite rightInjectionSite;
	private InjectionSite leftInjectionSite;
	private YesNo trueAfp;
	private SymptomState dyspnea;
	private SymptomState tachypnea;
	@Diseases({
			NEONATAL_TETANUS
	})
	@Outbreaks
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState babyDied;
	private Integer ageAtDeathDays;
	private Integer ageAtOnsetDays;
	@Diseases({
			NEONATAL_TETANUS
	})
	@Outbreaks
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState babyNormalAtBirth;
	@Diseases({
			NEONATAL_TETANUS
	})
	@Outbreaks
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState normalCryAndSuck;
	@Diseases({
			NEONATAL_TETANUS
	})
	@Outbreaks
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState stoppedSuckingAfterTwoDays;
	@Diseases({
			NEONATAL_TETANUS
	})
	@Outbreaks
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState stiffness;
	private YesNo symptomsOngoing;
	private DurationHours durationHours;
	private String nameOfHealthFacility;
	private Set<SymptomsList> symptomsSelected;
	private String symptomsSelectedOther;
	private Date dateOfOnsetRash;
	private Set<MpoxRashArea> rashSymptoms;
	private Set<InjectionSite> siteOfParalysis;
	private String rashSymptomsOtherAreas;
	private YesNo areLesionsSameState;
	private YesNo areLesionsSameSize;
	private YesNo areLesionsDeep;
	private YesNo areUlcersAmong;
	private Set<SymptomsList> typeOfRash;
	private String outcomeOther;
	private YesNo patientHaveFever;
	private Date outcomeDate;
	private String outcomePlaceCommVillage;
	private String nameService;
	private String placeOfFuneralNameVillage;

	@Diseases({
			GUINEA_WORM })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private GuineaWormFirstSymptom firstSignOrSymptomsBeforeWorm;
	private String firstSignOrSymptomsBeforeWormOthers;
	@Diseases({
			GUINEA_WORM })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.OTHER)
	private SymptomState emergenceOfGuineaWorm;
	private String numberOfWorms;
	@Diseases({
			GUINEA_WORM })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.OTHER)
	private SymptomState firstWormThisYear;
	private Date dateFirstWormEmergence;
	@Diseases({
			GUINEA_WORM })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.OTHER)
	private SymptomState caseDetectedBeforeWormEmergence;
	@Diseases({
			CHOLERA
			 })
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState diarrhoea;
	@Diseases({
			CHOLERA
	})
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState abdominalCramps;
	@Diseases({
			CHOLERA
	})
	@HideForCountries
	@SymptomGrouping(SymptomGroup.GENERAL)
	private SymptomState headaches;

	@Order(0)
	public Float getTemperature() {
		return temperature;
	}

	@Order(1)
	public TemperatureSource getTemperatureSource() {
		return temperatureSource;
	}

	@Order(20)
	@ImportIgnore
	public Boolean getSymptomatic() {
		return symptomatic;
	}

	@Order(21)
	public String getSymptomsComments() {
		return symptomsComments;
	}

	@Order(22)
	public Date getOnsetDate() {
		return onsetDate;
	}

	@Order(23)
	public String getOnsetSymptom() {
		return onsetSymptom;
	}

	@ImportIgnore
	public String getPatientIllLocation() {
		return patientIllLocation;
	}

	@Order(100)
	public SymptomState getAbdominalPain() {
		return abdominalPain;
	}

	@Order(101)
	public SymptomState getAlteredConsciousness() {
		return alteredConsciousness;
	}

	@Order(102)
	public SymptomState getAnorexiaAppetiteLoss() {
		return anorexiaAppetiteLoss;
	}

	@Order(103)
	public SymptomState getBackache() {
		return backache;
	}

	@Order(104)
	public SymptomState getBedridden() {
		return bedridden;
	}

	@Order(105)
	public SymptomState getBlackeningDeathOfTissue() {
		return blackeningDeathOfTissue;
	}

	@Order(110)
	public SymptomState getBleedingVagina() {
		return bleedingVagina;
	}

	@Order(111)
	public SymptomState getBloodInStool() {
		return bloodInStool;
	}

	public Integer getBloodPressureDiastolic() {
		return bloodPressureDiastolic;
	}

	public Integer getBloodPressureSystolic() {
		return bloodPressureSystolic;
	}

	@Order(112)
	public SymptomState getBloodUrine() {
		return bloodUrine;
	}

	@Order(113)
	public SymptomState getBloodyBlackStool() {
		return bloodyBlackStool;
	}

	@Order(114)
	public SymptomState getBuboesGroinArmpitNeck() {
		return buboesGroinArmpitNeck;
	}

	@Order(115)
	public SymptomState getBulgingFontanelle() {
		return bulgingFontanelle;
	}

	@Order(116)
	public SymptomState getBilateralCataracts() {
		return bilateralCataracts;
	}

	@Order(117)
	public SymptomState getUnilateralCataracts() {
		return unilateralCataracts;
	}

	@Order(120)
	public SymptomState getChestPain() {
		return chestPain;
	}

	@Order(121)
	public SymptomState getChillsSweats() {
		return chillsSweats;
	}

	@Order(122)
	public SymptomState getConfusedDisoriented() {
		return confusedDisoriented;
	}

	@Order(123)
	public SymptomState getCongenitalGlaucoma() {
		return congenitalGlaucoma;
	}

	@Order(124)
	public SymptomState getCongenitalHeartDisease() {
		return congenitalHeartDisease;
	}

	@Order(125)
	public CongenitalHeartDiseaseType getCongenitalHeartDiseaseType() {
		return congenitalHeartDiseaseType;
	}

	@Order(126)
	public String getCongenitalHeartDiseaseDetails() {
		return congenitalHeartDiseaseDetails;
	}

	@Order(127)
	public SymptomState getConjunctivitis() {
		return conjunctivitis;
	}

	@Order(128)
	public SymptomState getCough() {
		return cough;
	}

	@Order(129)
	public SymptomState getCoughingBlood() {
		return coughingBlood;
	}

	@Order(130)
	public SymptomState getDarkUrine() {
		return darkUrine;
	}

	@Order(131)
	public SymptomState getDehydration() {
		return dehydration;
	}

	@Order(132)
	public SymptomState getDevelopmentalDelay() {
		return developmentalDelay;
	}

	@Order(133)
	public SymptomState getDiarrhea() {
		return diarrhea;
	}

	@Order(134)
	public SymptomState getDifficultyBreathing() {
		return difficultyBreathing;
	}

	@Order(135)
	public SymptomState getDigestedBloodVomit() {
		return digestedBloodVomit;
	}

	@Order(136)
	public SymptomState getEyePainLightSensitive() {
		return eyePainLightSensitive;
	}

	@Order(140)
	public SymptomState getEyesBleeding() {
		return eyesBleeding;
	}

	@Order(141)
	public SymptomState getFatigueWeakness() {
		return fatigueWeakness;
	}

	@Order(142)
	public SymptomState getFever() {
		return fever;
	}

	@Order(143)
	public SymptomState getFluidInLungCavity() {
		return fluidInLungCavity;
	}

	@Order(144)
	public SymptomState getFluidInLungCavityAuscultation() {
		return fluidInLungCavityAuscultation;
	}

	@Order(145)
	public SymptomState getFluidInLungCavityXray() {
		return fluidInLungCavityXray;
	}

	public Integer getGlasgowComaScale() {
		return glasgowComaScale;
	}

	@Order(146)
	public SymptomState getGumsBleeding() {
		return gumsBleeding;
	}

	@Order(147)
	public SymptomState getHeadache() {
		return headache;
	}

	@Order(150)
	public SymptomState getHearingloss() {
		return hearingloss;
	}

	public Integer getHeartRate() {
		return heartRate;
	}

	public Integer getHeight() {
		return height;
	}

	@Order(151)
	public SymptomState getHemorrhagicSyndrome() {
		return hemorrhagicSyndrome;
	}

	@Order(152)
	public SymptomState getHiccups() {
		return hiccups;
	}

	@Order(153)
	public SymptomState getHyperglycemia() {
		return hyperglycemia;
	}

	@Order(154)
	public SymptomState getHypoglycemia() {
		return hypoglycemia;
	}

	@Order(155)
	public SymptomState getInjectionSiteBleeding() {
		return injectionSiteBleeding;
	}

	@Order(160)
	public SymptomState getJaundice() {
		return jaundice;
	}

	@Order(161)
	public YesNoUnknown getJaundiceWithin24HoursOfBirth() {
		return jaundiceWithin24HoursOfBirth;
	}

	@Order(162)
	public SymptomState getJointPain() {
		return jointPain;
	}

	@Order(163)
	public SymptomState getKopliksSpots() {
		return kopliksSpots;
	}

	@Order(164)
	public SymptomState getLesions() {
		return lesions;
	}

	@Order(165)
	public Boolean getLesionsAllOverBody() {
		return lesionsAllOverBody;
	}

	@Order(166)
	public Boolean getLesionsArms() {
		return lesionsArms;
	}

	@Order(170)
	public SymptomState getLesionsDeepProfound() {
		return lesionsDeepProfound;
	}

	@Order(171)
	public Boolean getLesionsFace() {
		return lesionsFace;
	}

	@Order(172)
	public Boolean getLesionsGenitals() {
		return lesionsGenitals;
	}

	@Order(173)
	public Boolean getLesionsLegs() {
		return lesionsLegs;
	}

	@Order(174)
	public Date getLesionsOnsetDate() {
		return lesionsOnsetDate;
	}

	@Order(175)
	public Boolean getLesionsPalmsHands() {
		return lesionsPalmsHands;
	}

	public SymptomState getLesionsResembleImg1() {
		return lesionsResembleImg1;
	}

	public SymptomState getLesionsResembleImg2() {
		return lesionsResembleImg2;
	}

	public SymptomState getLesionsResembleImg3() {
		return lesionsResembleImg3;
	}

	public SymptomState getLesionsResembleImg4() {
		return lesionsResembleImg4;
	}

	@Order(180)
	public SymptomState getLesionsSameSize() {
		return lesionsSameSize;
	}

	@Order(181)
	public SymptomState getLesionsSameState() {
		return lesionsSameState;
	}

	@Order(182)
	public Boolean getLesionsSolesFeet() {
		return lesionsSolesFeet;
	}

	@Order(183)
	public SymptomState getLesionsThatItch() {
		return lesionsThatItch;
	}

	@Order(184)
	public Boolean getLesionsThorax() {
		return lesionsThorax;
	}

	@Order(185)
	public SymptomState getLossSkinTurgor() {
		return lossSkinTurgor;
	}

	@Order(190)
	public SymptomState getLymphadenopathyAxillary() {
		return lymphadenopathyAxillary;
	}

	@Order(191)
	public SymptomState getLymphadenopathyCervical() {
		return lymphadenopathyCervical;
	}

	@Order(192)
	public SymptomState getLymphadenopathyInguinal() {
		return lymphadenopathyInguinal;
	}

	@Order(193)
	public SymptomState getMalaise() {
		return malaise;
	}

	@Order(194)
	public SymptomState getMeningealSigns() {
		return meningealSigns;
	}

	@Order(195)
	public SymptomState getMeningoencephalitis() {
		return meningoencephalitis;
	}

	@Order(196)
	public SymptomState getMicrocephaly() {
		return microcephaly;
	}

	@Order(197)
	public Integer getMidUpperArmCircumference() {
		return midUpperArmCircumference;
	}

	@Order(200)
	public SymptomState getMusclePain() {
		return musclePain;
	}

	@Order(201)
	public SymptomState getNausea() {
		return nausea;
	}

	@Order(202)
	public SymptomState getNeckStiffness() {
		return neckStiffness;
	}

	@Order(203)
	public SymptomState getNoseBleeding() {
		return noseBleeding;
	}

	@Order(204)
	public SymptomState getOedemaFaceNeck() {
		return oedemaFaceNeck;
	}

	@Order(205)
	public SymptomState getOedemaLowerExtremity() {
		return oedemaLowerExtremity;
	}

	@Order(210)
	public SymptomState getOralUlcers() {
		return oralUlcers;
	}

	@Order(211)
	public SymptomState getOtherHemorrhagicSymptoms() {
		return otherHemorrhagicSymptoms;
	}

	@Order(212)
	public String getOtherHemorrhagicSymptomsText() {
		return otherHemorrhagicSymptomsText;
	}

	@Order(213)
	public SymptomState getOtherNonHemorrhagicSymptoms() {
		return otherNonHemorrhagicSymptoms;
	}

	@Order(214)
	public String getOtherNonHemorrhagicSymptomsText() {
		return otherNonHemorrhagicSymptomsText;
	}

	@Order(215)
	public SymptomState getOtitisMedia() {
		return otitisMedia;
	}

	@Order(220)
	public SymptomState getPainfulLymphadenitis() {
		return painfulLymphadenitis;
	}

	@Order(221)
	public SymptomState getPalpableLiver() {
		return palpableLiver;
	}

	@Order(222)
	public SymptomState getPalpableSpleen() {
		return palpableSpleen;
	}

	@Order(223)
	public SymptomState getPharyngealErythema() {
		return pharyngealErythema;
	}

	@Order(224)
	public SymptomState getPharyngealExudate() {
		return pharyngealExudate;
	}

	@Order(225)
	public SymptomState getPigmentaryRetinopathy() {
		return pigmentaryRetinopathy;
	}

	@Order(226)
	public SymptomState getPurpuricRash() {
		return purpuricRash;
	}

	@Order(227)
	public SymptomState getRadiolucentBoneDisease() {
		return radiolucentBoneDisease;
	}

	@Order(228)
	public SymptomState getRapidBreathing() {
		return rapidBreathing;
	}

	@Order(230)
	public SymptomState getRedBloodVomit() {
		return redBloodVomit;
	}

	@Order(231)
	public SymptomState getRefusalFeedorDrink() {
		return refusalFeedorDrink;
	}

	public Integer getRespiratoryRate() {
		return respiratoryRate;
	}

	@Order(232)
	public SymptomState getRunnyNose() {
		return runnyNose;
	}

	@Order(233)
	public SymptomState getSeizures() {
		return seizures;
	}

	@Order(234)
	public SymptomState getSepsis() {
		return sepsis;
	}

	@Order(235)
	public SymptomState getShock() {
		return shock;
	}

	@Order(240)
	public SymptomState getSidePain() {
		return sidePain;
	}

	@Order(241)
	public SymptomState getSkinBruising() {
		return skinBruising;
	}

	@Order(242)
	public SymptomState getSkinRash() {
		return skinRash;
	}

	@Order(243)
	public SymptomState getSoreThroat() {
		return soreThroat;
	}

	@Order(244)
	public SymptomState getSplenomegaly() {
		return splenomegaly;
	}

	@Order(245)
	public SymptomState getStomachBleeding() {
		return stomachBleeding;
	}

	@Order(246)
	public SymptomState getSunkenEyesFontanelle() {
		return sunkenEyesFontanelle;
	}

	@Order(250)
	public SymptomState getSwollenGlands() {
		return swollenGlands;
	}

	@Order(251)
	public SymptomState getThrobocytopenia() {
		return throbocytopenia;
	}

	@Order(252)
	public SymptomState getTremor() {
		return tremor;
	}

	@Order(253)
	public SymptomState getUnexplainedBleeding() {
		return unexplainedBleeding;
	}

	@Order(254)
	public SymptomState getVomiting() {
		return vomiting;
	}

	@Order(260)
	public SymptomState getAbnormalLungXrayFindings() {
		return abnormalLungXrayFindings;
	}

	@Order(261)
	public SymptomState getConjunctivalInjection() {
		return conjunctivalInjection;
	}

	@Order(262)
	public SymptomState getAcuteRespiratoryDistressSyndrome() {
		return acuteRespiratoryDistressSyndrome;
	}

	@Order(263)
	public SymptomState getPneumoniaClinicalOrRadiologic() {
		return pneumoniaClinicalOrRadiologic;
	}

	@Order(264)
	public SymptomState getLossOfTaste() {
		return lossOfTaste;
	}

	@Order(265)
	public SymptomState getLossOfSmell() {
		return lossOfSmell;
	}

	@Order(266)
	public SymptomState getCoughWithSputum() {
		return coughWithSputum;
	}

	@Order(267)
	public SymptomState getCoughWithHeamoptysis() {
		return coughWithHeamoptysis;
	}

	@Order(268)
	public SymptomState getLymphadenopathy() {
		return lymphadenopathy;
	}

	@Order(269)
	public SymptomState getWheezing() {
		return wheezing;
	}

	@Order(270)
	public SymptomState getSkinUlcers() {
		return skinUlcers;
	}

	@Order(271)
	public SymptomState getInabilityToWalk() {
		return inabilityToWalk;
	}

	@Order(272)
	public SymptomState getInDrawingOfChestWall() {
		return inDrawingOfChestWall;
	}

	@Order(273)
	public SymptomState getOtherComplications() {
		return otherComplications;
	}

	@Order(274)
	public String getOtherComplicationsText() {
		return otherComplicationsText;
	}

	@Order(300)
	public SymptomState getRespiratoryDiseaseVentilation() {
		return respiratoryDiseaseVentilation;
	}

	@Order(301)
	public SymptomState getFeelingIll() {
		return feelingIll;
	}

	@Order(302)
	public SymptomState getShivering() {
		return shivering;
	}

	@Order(304)
	public SymptomState getFastHeartRate() {
		return fastHeartRate;
	}

	@Order(305)
	public SymptomState getOxygenSaturationLower94() {
		return oxygenSaturationLower94;
	}

	@Order(310)
	public SymptomState getFeverishFeeling() {
		return feverishFeeling;
	}

	@Order(311)
	public SymptomState getWeakness() {
		return weakness;
	}

	@Order(312)
	public SymptomState getFatigue() {
		return fatigue;
	}

	@Order(313)
	public SymptomState getCoughWithoutSputum() {
		return coughWithoutSputum;
	}

	@Order(314)
	public SymptomState getBreathlessness() {
		return breathlessness;
	}

	@Order(315)
	public SymptomState getChestPressure() {
		return chestPressure;
	}

	@Order(316)
	public SymptomState getBlueLips() {
		return blueLips;
	}

	@Order(317)
	public SymptomState getBloodCirculationProblems() {
		return bloodCirculationProblems;
	}

	@Order(318)
	public SymptomState getDizzinessStandingUp() {
		return dizzinessStandingUp;
	}

	@Order(319)
	public SymptomState getHighOrLowBloodPressure() {
		return highOrLowBloodPressure;
	}

	@Order(320)
	public SymptomState getUrinaryRetention() {
		return urinaryRetention;
	}

	@Order(330)
	public Integer getWeight() {
		return weight;
	}

	@Order(331)
	public SymptomState getBodyAche() {return  bodyAche;}

	@Order(332)
	public SymptomState getDizziness() {return  dizziness;}

	@Order(333)
	public SymptomState getExcessiveSweating() {return  excessiveSweating;}

	@Order(334)
	public SymptomState getNumbness() {return  numbness;}
	@Order(335)
	public String getOutcomeOther() { return outcomeOther;}

	@Order(336)
	public SymptomState getDifficultySwallow() {return  difficultySwallow;}

	@Order(337)
	public SymptomState getSkinRashNew() {return skinRashNew;}
	public void setAbdominalPain(SymptomState abdominalPain) {
		this.abdominalPain = abdominalPain;
	}
	public void setOutcome(CaseOutcome outcome) {
		this.outcome = outcome;
	}

	public void setAlteredConsciousness(SymptomState alteredConsciousness) {
		this.alteredConsciousness = alteredConsciousness;
	}

	public void setAnorexiaAppetiteLoss(SymptomState anorexiaAppetiteLoss) {
		this.anorexiaAppetiteLoss = anorexiaAppetiteLoss;
	}

	public void setBackache(SymptomState backache) {
		this.backache = backache;
	}

	public void setBedridden(SymptomState bedridden) {
		this.bedridden = bedridden;
	}

	public void setBlackeningDeathOfTissue(SymptomState blackeningDeathOfTissue) {
		this.blackeningDeathOfTissue = blackeningDeathOfTissue;
	}

	public void setBleedingVagina(SymptomState bleedingVagina) {
		this.bleedingVagina = bleedingVagina;
	}

	public void setBloodInStool(SymptomState bloodInStool) {
		this.bloodInStool = bloodInStool;
	}

	public void setBloodPressureDiastolic(Integer bloodPressureDiastolic) {
		this.bloodPressureDiastolic = bloodPressureDiastolic;
	}

	public void setBloodPressureSystolic(Integer bloodPressureSystolic) {
		this.bloodPressureSystolic = bloodPressureSystolic;
	}

	public void setBloodUrine(SymptomState bloodUrine) {
		this.bloodUrine = bloodUrine;
	}

	public void setBloodyBlackStool(SymptomState bloodyBlackStool) {
		this.bloodyBlackStool = bloodyBlackStool;
	}

	public void setBuboesGroinArmpitNeck(SymptomState buboesGroinArmpitNeck) {
		this.buboesGroinArmpitNeck = buboesGroinArmpitNeck;
	}

	public void setBulgingFontanelle(SymptomState bulgingFontanelle) {
		this.bulgingFontanelle = bulgingFontanelle;
	}

	public void setChestPain(SymptomState chestPain) {
		this.chestPain = chestPain;
	}

	public void setChillsSweats(SymptomState chillsSweats) {
		this.chillsSweats = chillsSweats;
	}

	public void setConfusedDisoriented(SymptomState confusedDisoriented) {
		this.confusedDisoriented = confusedDisoriented;
	}

	public void setConjunctivitis(SymptomState conjunctivitis) {
		this.conjunctivitis = conjunctivitis;
	}

	public void setCough(SymptomState cough) {
		this.cough = cough;
	}

	public void setCoughingBlood(SymptomState coughingBlood) {
		this.coughingBlood = coughingBlood;
	}

	public void setDarkUrine(SymptomState darkUrine) {
		this.darkUrine = darkUrine;
	}

	public void setDehydration(SymptomState dehydration) {
		this.dehydration = dehydration;
	}

	public void setDiarrhea(SymptomState diarrhea) {
		this.diarrhea = diarrhea;
	}
	public void setDifficultySwallow(SymptomState difficultySwallow) {
		this.difficultySwallow = difficultySwallow;
	}

	public void setSkinRashNew(SymptomState skinRashNew) {
		this.skinRashNew = skinRashNew;
	}

	public void setDifficultyBreathing(SymptomState difficultyBreathing) {
		this.difficultyBreathing = difficultyBreathing;
	}

	public void setDigestedBloodVomit(SymptomState digestedBloodVomit) {
		this.digestedBloodVomit = digestedBloodVomit;
	}

	public void setEyePainLightSensitive(SymptomState eyePainLightSensitive) {
		this.eyePainLightSensitive = eyePainLightSensitive;
	}

	public void setEyesBleeding(SymptomState eyesBleeding) {
		this.eyesBleeding = eyesBleeding;
	}

	public void setFatigueWeakness(SymptomState fatigueWeakness) {
		this.fatigueWeakness = fatigueWeakness;
	}

	public void setFever(SymptomState fever) {
		this.fever = fever;
	}

	public void setFluidInLungCavity(SymptomState fluidInLungCavity) {
		this.fluidInLungCavity = fluidInLungCavity;
	}

	public void setGlasgowComaScale(Integer glasgowComaScale) {
		this.glasgowComaScale = glasgowComaScale;
	}

	public void setGumsBleeding(SymptomState gumsBleeding) {
		this.gumsBleeding = gumsBleeding;
	}

	public void setHeadache(SymptomState headache) {
		this.headache = headache;
	}

	public void setHearingloss(SymptomState hearingloss) {
		this.hearingloss = hearingloss;
	}
	public void setBodyAche(SymptomState bodyAche) {
		this.bodyAche = bodyAche;
	}
	public void setDizziness(SymptomState dizziness) {
		this.dizziness = dizziness;
	}

	public void setExcessiveSweating(SymptomState excessiveSweating) {
		this.excessiveSweating = excessiveSweating;
	}

	public void setNumbness(SymptomState numbness) {
		this.numbness = numbness;
	}

	public void setHeartRate(Integer heartRate) {
		this.heartRate = heartRate;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public void setHemorrhagicSyndrome(SymptomState hemorrhagicSyndrome) {
		this.hemorrhagicSyndrome = hemorrhagicSyndrome;
	}

	public void setHiccups(SymptomState hiccups) {
		this.hiccups = hiccups;
	}

	public void setHyperglycemia(SymptomState hyperglycemia) {
		this.hyperglycemia = hyperglycemia;
	}

	public void setHypoglycemia(SymptomState hypoglycemia) {
		this.hypoglycemia = hypoglycemia;
	}

	public void setInjectionSiteBleeding(SymptomState injectionSiteBleeding) {
		this.injectionSiteBleeding = injectionSiteBleeding;
	}

	public void setJaundice(SymptomState jaundice) {
		this.jaundice = jaundice;
	}

	public void setJointPain(SymptomState jointPain) {
		this.jointPain = jointPain;
	}

	public void setKopliksSpots(SymptomState kopliksSpots) {
		this.kopliksSpots = kopliksSpots;
	}

	public void setLesions(SymptomState lesions) {
		this.lesions = lesions;
	}

	public void setLesionsAllOverBody(Boolean lesionsAllOverBody) {
		this.lesionsAllOverBody = lesionsAllOverBody;
	}

	public void setLesionsArms(Boolean lesionsArms) {
		this.lesionsArms = lesionsArms;
	}

	public void setLesionsDeepProfound(SymptomState lesionsDeepProfound) {
		this.lesionsDeepProfound = lesionsDeepProfound;
	}

	public void setLesionsFace(Boolean lesionsFace) {
		this.lesionsFace = lesionsFace;
	}

	public void setLesionsGenitals(Boolean lesionsGenitals) {
		this.lesionsGenitals = lesionsGenitals;
	}

	public void setLesionsLegs(Boolean lesionsLegs) {
		this.lesionsLegs = lesionsLegs;
	}

	public void setLesionsOnsetDate(Date lesionsOnsetDate) {
		this.lesionsOnsetDate = lesionsOnsetDate;
	}

	public void setLesionsPalmsHands(Boolean lesionsPalmsHands) {
		this.lesionsPalmsHands = lesionsPalmsHands;
	}

	public void setLesionsResembleImg1(SymptomState lesionsResembleImg1) {
		this.lesionsResembleImg1 = lesionsResembleImg1;
	}

	public void setLesionsResembleImg2(SymptomState lesionsResembleImg2) {
		this.lesionsResembleImg2 = lesionsResembleImg2;
	}

	public void setLesionsResembleImg3(SymptomState lesionsResembleImg3) {
		this.lesionsResembleImg3 = lesionsResembleImg3;
	}

	public void setLesionsResembleImg4(SymptomState lesionsResembleImg4) {
		this.lesionsResembleImg4 = lesionsResembleImg4;
	}

	public void setLesionsSameSize(SymptomState lesionsSameSize) {
		this.lesionsSameSize = lesionsSameSize;
	}

	public void setLesionsSameState(SymptomState lesionsSameState) {
		this.lesionsSameState = lesionsSameState;
	}

	public void setLesionsSolesFeet(Boolean lesionsSolesFeet) {
		this.lesionsSolesFeet = lesionsSolesFeet;
	}

	public void setLesionsThatItch(SymptomState lesionsThatItch) {
		this.lesionsThatItch = lesionsThatItch;
	}

	public void setLesionsThorax(Boolean lesionsThorax) {
		this.lesionsThorax = lesionsThorax;
	}

	public void setLossSkinTurgor(SymptomState lossSkinTurgor) {
		this.lossSkinTurgor = lossSkinTurgor;
	}

	public void setLymphadenopathyAxillary(SymptomState lymphadenopathyAxillary) {
		this.lymphadenopathyAxillary = lymphadenopathyAxillary;
	}

	public void setLymphadenopathyCervical(SymptomState lymphadenopathyCervical) {
		this.lymphadenopathyCervical = lymphadenopathyCervical;
	}

	public void setLymphadenopathyInguinal(SymptomState lymphadenopathyInguinal) {
		this.lymphadenopathyInguinal = lymphadenopathyInguinal;
	}

	public void setMalaise(SymptomState malaise) {
		this.malaise = malaise;
	}

	public void setMeningealSigns(SymptomState meningealSigns) {
		this.meningealSigns = meningealSigns;
	}

	public void setMidUpperArmCircumference(Integer midUpperArmCircumference) {
		this.midUpperArmCircumference = midUpperArmCircumference;
	}

	public void setMusclePain(SymptomState musclePain) {
		this.musclePain = musclePain;
	}

	public void setNausea(SymptomState nausea) {
		this.nausea = nausea;
	}

	public void setNeckStiffness(SymptomState neckStiffness) {
		this.neckStiffness = neckStiffness;
	}

	public void setNoseBleeding(SymptomState noseBleeding) {
		this.noseBleeding = noseBleeding;
	}

	public void setOedemaFaceNeck(SymptomState oedemaFaceNeck) {
		this.oedemaFaceNeck = oedemaFaceNeck;
	}

	public void setOedemaLowerExtremity(SymptomState oedemaLowerExtremity) {
		this.oedemaLowerExtremity = oedemaLowerExtremity;
	}

	public void setOnsetDate(Date onsetDate) {
		this.onsetDate = onsetDate;
	}
	public void setDateOfOnset(Date dateOfOnset) {
		this.dateOfOnset = dateOfOnset;
	}
	public void setFeverBodyTempGreater(YesNo feverBodyTempGreater) {
		this.feverBodyTempGreater = feverBodyTempGreater;
	}

	public void setOnsetSymptom(String onsetSymptom) {
		this.onsetSymptom = onsetSymptom;
	}

	public void setOralUlcers(SymptomState oralUlcers) {
		this.oralUlcers = oralUlcers;
	}

	public void setOtherHemorrhagicSymptoms(SymptomState otherHemorrhagicSymptoms) {
		this.otherHemorrhagicSymptoms = otherHemorrhagicSymptoms;
	}

	public void setOtherHemorrhagicSymptomsText(String otherHemorrhagicSymptomsText) {
		this.otherHemorrhagicSymptomsText = otherHemorrhagicSymptomsText;
	}

	public void setOtherNonHemorrhagicSymptoms(SymptomState otherNonHemorrhagicSymptoms) {
		this.otherNonHemorrhagicSymptoms = otherNonHemorrhagicSymptoms;
	}

	public void setOtherNonHemorrhagicSymptomsText(String otherNonHemorrhagicSymptomsText) {
		this.otherNonHemorrhagicSymptomsText = otherNonHemorrhagicSymptomsText;
	}

	public void setOtitisMedia(SymptomState otitisMedia) {
		this.otitisMedia = otitisMedia;
	}

	public void setPainfulLymphadenitis(SymptomState painfulLymphadenitis) {
		this.painfulLymphadenitis = painfulLymphadenitis;
	}

	public void setPalpableLiver(SymptomState palpableLiver) {
		this.palpableLiver = palpableLiver;
	}

	public void setPalpableSpleen(SymptomState palpableSpleen) {
		this.palpableSpleen = palpableSpleen;
	}

	public void setPatientIllLocation(String patientIllLocation) {
		this.patientIllLocation = patientIllLocation;
	}

	public void setPharyngealErythema(SymptomState pharyngealErythema) {
		this.pharyngealErythema = pharyngealErythema;
	}

	public void setPharyngealExudate(SymptomState pharyngealExudate) {
		this.pharyngealExudate = pharyngealExudate;
	}

	public void setRapidBreathing(SymptomState rapidBreathing) {
		this.rapidBreathing = rapidBreathing;
	}

	public void setRedBloodVomit(SymptomState redBloodVomit) {
		this.redBloodVomit = redBloodVomit;
	}

	public void setRefusalFeedorDrink(SymptomState refusalFeedorDrink) {
		this.refusalFeedorDrink = refusalFeedorDrink;
	}

	public void setRespiratoryRate(Integer respiratoryRate) {
		this.respiratoryRate = respiratoryRate;
	}

	public void setRunnyNose(SymptomState runnyNose) {
		this.runnyNose = runnyNose;
	}

	public void setSeizures(SymptomState seizures) {
		this.seizures = seizures;
	}

	public void setSepsis(SymptomState sepsis) {
		this.sepsis = sepsis;
	}

	public void setShock(SymptomState shock) {
		this.shock = shock;
	}

	public void setSidePain(SymptomState sidePain) {
		this.sidePain = sidePain;
	}

	public void setSkinBruising(SymptomState skinBruising) {
		this.skinBruising = skinBruising;
	}

	public void setSkinRash(SymptomState skinRash) {
		this.skinRash = skinRash;
	}

	public void setSoreThroat(SymptomState soreThroat) {
		this.soreThroat = soreThroat;
	}

	public void setMuscleTone(SymptomState muscleTone) {
		this.muscleTone = muscleTone;
	}
	public void setDeepTendonReflex(SymptomState deepTendonReflex) {
		this.deepTendonReflex = deepTendonReflex;
	}
	public void setMuscleVolume(SymptomState muscleVolume) {
		this.muscleVolume = muscleVolume;
	}
	public void setSensoryLoss(SymptomState sensoryLoss) {
		this.sensoryLoss = sensoryLoss;
	}

	public void setStomachBleeding(SymptomState stomachBleeding) {
		this.stomachBleeding = stomachBleeding;
	}

	public void setSunkenEyesFontanelle(SymptomState sunkenEyesFontanelle) {
		this.sunkenEyesFontanelle = sunkenEyesFontanelle;
	}

	public void setSwollenGlands(SymptomState swollenGlands) {
		this.swollenGlands = swollenGlands;
	}

	public void setSymptomatic(Boolean symptomatic) {
		this.symptomatic = symptomatic;
	}

	public void setSymptomsComments(String symptomsComments) {
		this.symptomsComments = symptomsComments;
	}

	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}

	public void setTemperatureSource(TemperatureSource temperatureSource) {
		this.temperatureSource = temperatureSource;
	}

	public void setThrobocytopenia(SymptomState throbocytopenia) {
		this.throbocytopenia = throbocytopenia;
	}

	public void setTremor(SymptomState tremor) {
		this.tremor = tremor;
	}

	public void setUnexplainedBleeding(SymptomState unexplainedBleeding) {
		this.unexplainedBleeding = unexplainedBleeding;
	}
	public void setVomiting(SymptomState vomiting) {
		this.vomiting = vomiting;
	}

	public void setConvulsion(SymptomState convulsion) {
		this.convulsion = convulsion;
	}

	public void setJaundiceWithin24HoursOfBirth(YesNoUnknown jaundiceWithin24HoursOfBirth) {
		this.jaundiceWithin24HoursOfBirth = jaundiceWithin24HoursOfBirth;
	}

	public void setBilateralCataracts(SymptomState bilateralCataracts) {
		this.bilateralCataracts = bilateralCataracts;
	}

	public void setUnilateralCataracts(SymptomState unilateralCataracts) {
		this.unilateralCataracts = unilateralCataracts;
	}

	public void setCongenitalGlaucoma(SymptomState congenitalGlaucoma) {
		this.congenitalGlaucoma = congenitalGlaucoma;
	}

	public void setPigmentaryRetinopathy(SymptomState pigmentaryRetinopathy) {
		this.pigmentaryRetinopathy = pigmentaryRetinopathy;
	}

	public void setPurpuricRash(SymptomState purpuricRash) {
		this.purpuricRash = purpuricRash;
	}

	public void setMicrocephaly(SymptomState microcephaly) {
		this.microcephaly = microcephaly;
	}

	public void setDevelopmentalDelay(SymptomState developmentalDelay) {
		this.developmentalDelay = developmentalDelay;
	}

	public void setSplenomegaly(SymptomState splenomegaly) {
		this.splenomegaly = splenomegaly;
	}

	public void setMeningoencephalitis(SymptomState meningoencephalitis) {
		this.meningoencephalitis = meningoencephalitis;
	}

	public void setRadiolucentBoneDisease(SymptomState radiolucentBoneDisease) {
		this.radiolucentBoneDisease = radiolucentBoneDisease;
	}

	public void setCongenitalHeartDisease(SymptomState congenitalHeartDisease) {
		this.congenitalHeartDisease = congenitalHeartDisease;
	}

	public void setCongenitalHeartDiseaseType(CongenitalHeartDiseaseType congenitalHeartDiseaseType) {
		this.congenitalHeartDiseaseType = congenitalHeartDiseaseType;
	}

	public void setCongenitalHeartDiseaseDetails(String congenitalHeartDiseaseDetails) {
		this.congenitalHeartDiseaseDetails = congenitalHeartDiseaseDetails;
	}
	@Order(331)
	public SymptomState getConvulsion() {
		return convulsion;
	}
	@Order(332)
	public SymptomState getHydrophobia() {
		return hydrophobia;
	}

	public void setHydrophobia(SymptomState hydrophobia) {
		this.hydrophobia = hydrophobia;
	}

	@Order(333)
	public SymptomState getOpisthotonus() {
		return opisthotonus;
	}

	public void setOpisthotonus(SymptomState opisthotonus) {
		this.opisthotonus = opisthotonus;
	}

	@Order(334)
	public SymptomState getAnxietyStates() {
		return anxietyStates;
	}

	public void setAnxietyStates(SymptomState anxietyStates) {
		this.anxietyStates = anxietyStates;
	}

	@Order(335)
	public SymptomState getDelirium() {
		return delirium;
	}

	public void setDelirium(SymptomState delirium) {
		this.delirium = delirium;
	}

	@Order(336)
	public SymptomState getUproariousness() {
		return uproariousness;
	}

	public void setUproariousness(SymptomState uproariousness) {
		this.uproariousness = uproariousness;
	}

	@Order(337)
	public SymptomState getParesthesiaAroundWound() {
		return paresthesiaAroundWound;
	}

	public void setParesthesiaAroundWound(SymptomState paresthesiaAroundWound) {
		this.paresthesiaAroundWound = paresthesiaAroundWound;
	}

	@Order(338)
	public SymptomState getExcessSalivation() {
		return excessSalivation;
	}

	public void setExcessSalivation(SymptomState excessSalivation) {
		this.excessSalivation = excessSalivation;
	}

	@Order(339)
	public SymptomState getInsomnia() {
		return insomnia;
	}

	public void setInsomnia(SymptomState insomnia) {
		this.insomnia = insomnia;
	}

	@Order(340)
	public SymptomState getParalysis() {
		return paralysis;
	}

	public void setParalysis(SymptomState paralysis) {
		this.paralysis = paralysis;
	}

	@Order(341)
	public SymptomState getExcitation() {
		return excitation;
	}

	public void setExcitation(SymptomState excitation) {
		this.excitation = excitation;
	}

	@Order(342)
	public SymptomState getDysphagia() {
		return dysphagia;
	}

	public void setDysphagia(SymptomState dysphagia) {
		this.dysphagia = dysphagia;
	}

	@Order(343)
	public SymptomState getAerophobia() {
		return aerophobia;
	}

	public void setAerophobia(SymptomState aerophobia) {
		this.aerophobia = aerophobia;
	}

	@Order(344)
	public SymptomState getHyperactivity() {
		return hyperactivity;
	}

	public void setHyperactivity(SymptomState hyperactivity) {
		this.hyperactivity = hyperactivity;
	}

	@Order(345)
	public SymptomState getParesis() {
		return paresis;
	}

	public void setParesis(SymptomState paresis) {
		this.paresis = paresis;
	}

	@Order(346)
	public SymptomState getAgitation() {
		return agitation;
	}

	public void setAgitation(SymptomState agitation) {
		this.agitation = agitation;
	}

	@Order(347)
	public SymptomState getAscendingFlaccidParalysis() {
		return ascendingFlaccidParalysis;
	}

	public void setAscendingFlaccidParalysis(SymptomState ascendingFlaccidParalysis) {
		this.ascendingFlaccidParalysis = ascendingFlaccidParalysis;
	}

	@Order(348)
	public SymptomState getErraticBehaviour() {
		return erraticBehaviour;
	}

	public void setErraticBehaviour(SymptomState erraticBehaviour) {
		this.erraticBehaviour = erraticBehaviour;
	}

	@Order(349)
	public SymptomState getComa() {
		return coma;
	}

	@Order(350)
	public SymptomState getMuscleTone() {
		return muscleTone;
	}

	@Order(351)
	public SymptomState getDeepTendonReflex() {
		return deepTendonReflex;
	}

	@Order(352)
	public SymptomState getMuscleVolume() {
		return muscleVolume;
	}

	@Order(353)
	public SymptomState getSensoryLoss() {
		return sensoryLoss;
	}
	@Order(354)
	public String getProvisionalDiagnosis() {
		return provisionalDiagnosis;
	}
	@Order(355)
	public Date getDateOfOnset() {
		return dateOfOnset;
	}
	@Order(356)
	public YesNo getFeverBodyTempGreater(){return feverBodyTempGreater;}

	@Order(357)
	public YesNoUnknown getFeverOnsetParalysis(){return feverOnsetParalysis;}
	@Order(358)
	public YesNoUnknown getProgressiveParalysis(){return progressiveParalysis;}
	@Order(359)
	public Date getDateOnsetParalysis(){return dateOnsetParalysis;}
	@Order(360)
	public YesNoUnknown getProgressiveFlaccidAcute(){return progressiveFlaccidAcute;}
	@Order(361)
	public YesNoUnknown getAssymetric(){return  assymetric;}
	@Order(363)
	public YesNo getParalysedLimbSensitiveToPain(){return paralysedLimbSensitiveToPain;}
	@Order(364)
	public YesNo getInjectionSiteBeforeOnsetParalysis(){return injectionSiteBeforeOnsetParalysis;}
	@Order(365)
	public InjectionSite getRightInjectionSite(){return rightInjectionSite;}
	@Order(366)
	public InjectionSite getLeftInjectionSite(){return leftInjectionSite;}
	@Order(367)
	public YesNo getTrueAfp(){return trueAfp;}
	@Order(368)
	public CaseOutcome getOutcome() { return outcome;}
	@Order(369)
	public YesNo getSymptomsOngoing() { return symptomsOngoing;}
	@Order(370)
	public DurationHours getDurationHours() { return durationHours;}
	@Order(371)
	public String getNameOfHealthFacility() { return nameOfHealthFacility;}
	@Order(372)
	@ImportIgnore
	public Set<SymptomsList> getSymptomsSelected() {
		return symptomsSelected;
	}
	@Order(373)
	public Date getDateOfOnsetRash(){
		return dateOfOnsetRash;
	}
	@Order(374)
	public Set<MpoxRashArea> getRashSymptoms(){
		return rashSymptoms;
	}
	@Order(362)
	public Set<InjectionSite> getSiteOfParalysis(){return siteOfParalysis;}
	@Order(375)
	public String getRashSymptomsOtherAreas(){
		return rashSymptomsOtherAreas;
	}
	@Order(376)
	public YesNo getAreLesionsSameState(){
		return areLesionsSameState;
	}
	@Order(377)
	public YesNo getAreLesionsSameSize(){
		return areLesionsSameSize;
	}
	@Order(378)
	public YesNo getAreLesionsDeep(){
		return areLesionsDeep;
	}
	@Order(379)
	public YesNo getAreUlcersAmong(){
		return areUlcersAmong;
	}
	@Order(380)
	public Set<SymptomsList> getTypeOfRash(){
		return typeOfRash;
	}
	@Order(381)
	public String getSymptomsSelectedOther(){
		return symptomsSelectedOther;
	}
	@Order(382)
	public YesNo getPatientHaveFever() {
		return patientHaveFever;
	}
	@Order(383)
	public Date getOutcomeDate() {
		return outcomeDate;
	}
	@Order(384)
	public String getOutcomePlaceCommVillage() {
		return outcomePlaceCommVillage;
	}
	@Order(385)
	public String getNameService() {
		return nameService;
	}
	@Order(386)
	public String getPlaceOfFuneralNameVillage() {
		return placeOfFuneralNameVillage;
	}

	public void setComa(SymptomState coma) {
		this.coma = coma;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public void setFluidInLungCavityAuscultation(SymptomState fluidInLungCavityAuscultation) {
		this.fluidInLungCavityAuscultation = fluidInLungCavityAuscultation;
	}

	public void setFluidInLungCavityXray(SymptomState fluidInLungCavityXray) {
		this.fluidInLungCavityXray = fluidInLungCavityXray;
	}

	public void setAbnormalLungXrayFindings(SymptomState abnormalLungXrayFindings) {
		this.abnormalLungXrayFindings = abnormalLungXrayFindings;
	}

	public void setConjunctivalInjection(SymptomState conjunctivalInjection) {
		this.conjunctivalInjection = conjunctivalInjection;
	}

	public void setAcuteRespiratoryDistressSyndrome(SymptomState acuteRespiratoryDistressSyndrome) {
		this.acuteRespiratoryDistressSyndrome = acuteRespiratoryDistressSyndrome;
	}

	public void setPneumoniaClinicalOrRadiologic(SymptomState pneumoniaClinicalOrRadiologic) {
		this.pneumoniaClinicalOrRadiologic = pneumoniaClinicalOrRadiologic;
	}

	public void setLossOfTaste(SymptomState lossOfTaste) {
		this.lossOfTaste = lossOfTaste;
	}

	public void setLossOfSmell(SymptomState lossOfSmell) {
		this.lossOfSmell = lossOfSmell;
	}

	public void setCoughWithSputum(SymptomState coughWithSputum) {
		this.coughWithSputum = coughWithSputum;
	}

	public void setCoughWithHeamoptysis(SymptomState coughWithHeamoptysis) {
		this.coughWithHeamoptysis = coughWithHeamoptysis;
	}

	public void setLymphadenopathy(SymptomState lymphadenopathy) {
		this.lymphadenopathy = lymphadenopathy;
	}

	public void setWheezing(SymptomState wheezing) {
		this.wheezing = wheezing;
	}

	public void setSkinUlcers(SymptomState skinUlcers) {
		this.skinUlcers = skinUlcers;
	}

	public void setInabilityToWalk(SymptomState inabilityToWalk) {
		this.inabilityToWalk = inabilityToWalk;
	}

	public void setInDrawingOfChestWall(SymptomState inDrawingOfChestWall) {
		this.inDrawingOfChestWall = inDrawingOfChestWall;
	}

	public void setOtherComplications(SymptomState otherComplications) {
		this.otherComplications = otherComplications;
	}

	public void setOtherComplicationsText(String otherComplicationsText) {
		this.otherComplicationsText = otherComplicationsText;
	}

	public void setProvisionalDiagnosis(String provisionalDiagnosis) {
		this.provisionalDiagnosis = provisionalDiagnosis;
	}

	public void setRespiratoryDiseaseVentilation(SymptomState respiratoryDiseaseVentilation) {
		this.respiratoryDiseaseVentilation = respiratoryDiseaseVentilation;
	}

	public void setFeelingIll(SymptomState feelingIll) {
		this.feelingIll = feelingIll;
	}

	public void setShivering(SymptomState shivering) {
		this.shivering = shivering;
	}

	public void setFastHeartRate(SymptomState fastHeartRate) {
		this.fastHeartRate = fastHeartRate;
	}

	public void setOxygenSaturationLower94(SymptomState oxygenSaturationLower94) {
		this.oxygenSaturationLower94 = oxygenSaturationLower94;
	}

	public void setFeverishFeeling(SymptomState feverishFeeling) {
		this.feverishFeeling = feverishFeeling;
	}

	public void setWeakness(SymptomState weakness) {
		this.weakness = weakness;
	}

	public void setFatigue(SymptomState fatigue) {
		this.fatigue = fatigue;
	}

	public void setCoughWithoutSputum(SymptomState coughWithoutSputum) {
		this.coughWithoutSputum = coughWithoutSputum;
	}

	public void setBreathlessness(SymptomState breathlessness) {
		this.breathlessness = breathlessness;
	}

	public void setChestPressure(SymptomState chestPressure) {
		this.chestPressure = chestPressure;
	}

	public void setBlueLips(SymptomState blueLips) {
		this.blueLips = blueLips;
	}

	@Order(350)
	public SymptomState getPalpitations() {
		return palpitations;
	}

	public void setPalpitations(SymptomState palpitations) {
		this.palpitations = palpitations;
	}

	public void setDizzinessStandingUp(SymptomState dizzinessStandingUp) {
		this.dizzinessStandingUp = dizzinessStandingUp;
	}

	public void setHighOrLowBloodPressure(SymptomState highOrLowBloodPressure) {
		this.highOrLowBloodPressure = highOrLowBloodPressure;
	}

	public void setUrinaryRetention(SymptomState urinaryRetention) {
		this.urinaryRetention = urinaryRetention;
	}

	public void setBloodCirculationProblems(SymptomState bloodCirculationProblems) {
		this.bloodCirculationProblems = bloodCirculationProblems;
	}

	@Order(351)
	public SymptomState getGeneralBodilyPains() {
		return generalBodilyPains;
	}

	public void setGeneralBodilyPains(SymptomState generalBodilyPains) {
		this.generalBodilyPains = generalBodilyPains;
	}

	@Order(352)
	public Boolean getLesionsNeck() {
		return lesionsNeck;
	}

	public void setLesionsNeck(Boolean lesionsNeck) {
		this.lesionsNeck = lesionsNeck;
	}

	@Order(353)
	public Boolean getLesionsTrunk() {
		return lesionsTrunk;
	}

	public void setLesionsTrunk(Boolean lesionsTrunk) {
		this.lesionsTrunk = lesionsTrunk;
	}

	@Order(354)
	public SymptomState getRashes() {
		return rashes;
	}

	public void setRashes(SymptomState rashes) {
		this.rashes = rashes;
	}

	@Order(355)
	public SymptomState getPapularRash() {
		return papularRash;
	}

	public void setPapularRash(SymptomState papularRash) {
		this.papularRash = papularRash;
	}

	@Order(356)
	public SymptomState getMacularRash() {
		return macularRash;
	}

	public void setMacularRash(SymptomState macularRash) {
		this.macularRash = macularRash;
	}

	@Order(357)
	public SymptomState getVesicularRash() {
		return vesicularRash;
	}

	public void setVesicularRash(SymptomState vesicularRash) {
		this.vesicularRash = vesicularRash;
	}

	@Order(358)
	public String getOtherLesionAreas() {
		return otherLesionAreas;
	}

	public void setOtherLesionAreas(String otherLesionAreas) {
		this.otherLesionAreas = otherLesionAreas;
	}
	public void setNonVascular(SymptomState nonVascular) {
		this.nonVascular = nonVascular;
	}

	@Order(351)
	public SymptomState getNonVascular() {
		return nonVascular;
	}


	public void setFeverOnsetParalysis(YesNoUnknown feverOnsetParalysis) {
		this.feverOnsetParalysis = feverOnsetParalysis;
	}

	public void setProgressiveParalysis(YesNoUnknown progressiveParalysis) {
		this.progressiveParalysis = progressiveParalysis;
	}

	public void setDateOnsetParalysis(Date dateOnsetParalysis) {
		this.dateOnsetParalysis = dateOnsetParalysis;
	}

	public void setProgressiveFlaccidAcute(YesNoUnknown progressiveFlaccidAcute) {
		this.progressiveFlaccidAcute = progressiveFlaccidAcute;
	}

	public void setAssymetric(YesNoUnknown assymetric) {
		this.assymetric = assymetric;
	}

	public void setSiteOfParalysis(Set<InjectionSite> siteOfParalysis) {
		this.siteOfParalysis = siteOfParalysis;
	}

	public void setParalysedLimbSensitiveToPain(YesNo paralysedLimbSensitiveToPain) {
		this.paralysedLimbSensitiveToPain = paralysedLimbSensitiveToPain;
	}

	public void setInjectionSiteBeforeOnsetParalysis(YesNo injectionSiteBeforeOnsetParalysis) {
		this.injectionSiteBeforeOnsetParalysis = injectionSiteBeforeOnsetParalysis;
	}

	public void setRightInjectionSite(InjectionSite rightInjectionSite) {
		this.rightInjectionSite = rightInjectionSite;
	}

	public void setLeftInjectionSite(InjectionSite leftInjectionSite) {
		this.leftInjectionSite = leftInjectionSite;
	}

	public void setTrueAfp(YesNo trueAfp) {
		this.trueAfp = trueAfp;
	}

	public SymptomState getDyspnea() {
		return dyspnea;
	}
	public void setDyspnea(SymptomState dyspnea) {
		this.dyspnea = dyspnea;
	}
	public SymptomState getTachypnea() {
		return tachypnea;
	}
	public void setTachypnea(SymptomState tachypnea) {
		this.tachypnea = tachypnea;
	}

	public SymptomState getBabyDied() {
		return babyDied;
	}

	public void setBabyDied(SymptomState babyDied) {
		this.babyDied = babyDied;
	}

	public Integer getAgeAtDeathDays() {
		return ageAtDeathDays;
	}

	public void setAgeAtDeathDays(Integer ageAtDeathDays) {
		this.ageAtDeathDays = ageAtDeathDays;
	}

	public Integer getAgeAtOnsetDays() {
		return ageAtOnsetDays;
	}

	public void setAgeAtOnsetDays(Integer ageAtOnsetDays) {
		this.ageAtOnsetDays = ageAtOnsetDays;
	}

	public SymptomState getBabyNormalAtBirth() {
		return babyNormalAtBirth;
	}

	public void setBabyNormalAtBirth(SymptomState babyNormalAtBirth) {
		this.babyNormalAtBirth = babyNormalAtBirth;
	}

	public SymptomState getNormalCryAndSuck() {
		return normalCryAndSuck;
	}

	public void setNormalCryAndSuck(SymptomState normalCryAndSuck) {
		this.normalCryAndSuck = normalCryAndSuck;
	}

	public SymptomState getStoppedSuckingAfterTwoDays() {
		return stoppedSuckingAfterTwoDays;
	}

	public void setStoppedSuckingAfterTwoDays(SymptomState stoppedSuckingAfterTwoDays) {
		this.stoppedSuckingAfterTwoDays = stoppedSuckingAfterTwoDays;
	}

	public SymptomState getStiffness() {
		return stiffness;
	}

	public void setStiffness(SymptomState stiffness) {
		this.stiffness = stiffness;
	}
	public void setSymptomsOngoing(YesNo symptomsOngoing) {
		this.symptomsOngoing = symptomsOngoing;
	}
	public void setDurationHours(DurationHours durationHours) {
		this.durationHours = durationHours;
	}
	public void setNameOfHealthFacility(String nameOfHealthFacility) {
		this.nameOfHealthFacility = nameOfHealthFacility;
	}
	public void setSymptomsSelected(Set<SymptomsList> symptomsSelected) {
		this.symptomsSelected = symptomsSelected;
	}
	public void setDateOfOnsetRash(Date dateOfOnsetRash) {
		this.dateOfOnsetRash = dateOfOnsetRash;
	}
	public void setRashSymptoms(Set<MpoxRashArea> rashSymptoms) {
		this.rashSymptoms = rashSymptoms;
	}
	public void setRashSymptomsOtherAreas(String rashSymptomsOtherAreas) {
		this.rashSymptomsOtherAreas = rashSymptomsOtherAreas;
	}
	public void setAreLesionsSameState(YesNo areLesionsSameState) {
		this.areLesionsSameState = areLesionsSameState;
	}
	public void setAreLesionsSameSize(YesNo areLesionsSameSize) {
		this.areLesionsSameSize = areLesionsSameSize;
	}
	public void setAreLesionsDeep(YesNo areLesionsDeep) {
		this.areLesionsDeep = areLesionsDeep;
	}
	public void setAreUlcersAmong(YesNo areUlcersAmong) {
		this.areUlcersAmong = areUlcersAmong;
	}
	public void setTypeOfRash(Set<SymptomsList> typeOfRash) {
		this.typeOfRash = typeOfRash;
	}
	public void setSymptomsSelectedOther(String symptomsSelectedOther) {
		this.symptomsSelectedOther = symptomsSelectedOther;
	}

	public GuineaWormFirstSymptom getFirstSignOrSymptomsBeforeWorm() {
		return firstSignOrSymptomsBeforeWorm;
	}

	public void setFirstSignOrSymptomsBeforeWorm(GuineaWormFirstSymptom firstSignOrSymptomsBeforeWorm) {
		this.firstSignOrSymptomsBeforeWorm = firstSignOrSymptomsBeforeWorm;
	}

	public String getFirstSignOrSymptomsBeforeWormOthers() {
		return firstSignOrSymptomsBeforeWormOthers;
	}

	public void setFirstSignOrSymptomsBeforeWormOthers(String firstSignOrSymptomsBeforeWormOthers) {
		this.firstSignOrSymptomsBeforeWormOthers = firstSignOrSymptomsBeforeWormOthers;
	}

	public SymptomState getEmergenceOfGuineaWorm() {
		return emergenceOfGuineaWorm;
	}

	public void setEmergenceOfGuineaWorm(SymptomState emergenceOfGuineaWorm) {
		this.emergenceOfGuineaWorm = emergenceOfGuineaWorm;
	}

	public String getNumberOfWorms() {
		return numberOfWorms;
	}

	public void setNumberOfWorms(String numberOfWorms) {
		this.numberOfWorms = numberOfWorms;
	}

	public SymptomState getFirstWormThisYear() {
		return firstWormThisYear;
	}

	public void setFirstWormThisYear(SymptomState firstWormThisYear) {
		this.firstWormThisYear = firstWormThisYear;
	}

	public Date getDateFirstWormEmergence() {
		return dateFirstWormEmergence;
	}

	public void setDateFirstWormEmergence(Date dateFirstWormEmergence) {
		this.dateFirstWormEmergence = dateFirstWormEmergence;
	}

	public SymptomState getCaseDetectedBeforeWormEmergence() {
		return caseDetectedBeforeWormEmergence;
	}

	public void setCaseDetectedBeforeWormEmergence(SymptomState caseDetectedBeforeWormEmergence) {
		this.caseDetectedBeforeWormEmergence = caseDetectedBeforeWormEmergence;
	}

	public SymptomState getDiarrhoea() {
		return diarrhoea;
	}

	public void setDiarrhoea(SymptomState diarrhoea) {
		this.diarrhoea = diarrhoea;
	}

	public SymptomState getAbdominalCramps() {
		return abdominalCramps;
	}

	public void setAbdominalCramps(SymptomState abdominalCramps) {
		this.abdominalCramps = abdominalCramps;
	}

	public SymptomState getHeadaches() {
		return headaches;
	}

	public void setHeadaches(SymptomState headaches) {
		this.headaches = headaches;
	}

	public void setOutcomeOther(String outcomeOther) {
		this.outcomeOther = outcomeOther;
	}
	public SymptomState getGeneralizedRash() {
		return generalizedRash;
	}

	public void setGeneralizedRash(SymptomState generalizedRash) {
		this.generalizedRash = generalizedRash;
	}

	public SymptomState getRedEyes() {
		return redEyes;
	}

	public void setRedEyes(SymptomState redEyes) {
		this.redEyes = redEyes;
	}

	public SymptomState getSwollenLymphNodesBehindEars() {
		return swollenLymphNodesBehindEars;
	}

	public void setSwollenLymphNodesBehindEars(SymptomState swollenLymphNodesBehindEars) {
		this.swollenLymphNodesBehindEars = swollenLymphNodesBehindEars;
	}

	public void setPatientHaveFever(YesNo patientHaveFever) {
		this.patientHaveFever = patientHaveFever;
	}
	public void setOutcomeDate(Date outcomeDate) {
		this.outcomeDate = outcomeDate;
	}

	public void setNameService(String nameService) {
		this.nameService = nameService;
	}

	public void setOutcomePlaceCommVillage(String outcomePlaceCommVillage) {
		this.outcomePlaceCommVillage = outcomePlaceCommVillage;
	}

	public void setPlaceOfFuneralNameVillage(String placeOfFuneralNameVillage) {
		this.placeOfFuneralNameVillage = placeOfFuneralNameVillage;
	}
}
