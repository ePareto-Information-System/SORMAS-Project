/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.app.backend.symptoms;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.apache.commons.lang3.StringUtils;

import de.symeda.sormas.api.caze.CaseOutcome;
import de.symeda.sormas.api.caze.Trimester;
import de.symeda.sormas.api.hospitalization.SymptomsList;
import de.symeda.sormas.api.symptoms.CongenitalHeartDiseaseType;
import de.symeda.sormas.api.symptoms.GuineaWormFirstSymptom;
import de.symeda.sormas.api.symptoms.SymptomState;
import de.symeda.sormas.api.symptoms.TemperatureSource;
import de.symeda.sormas.api.utils.BodyPart;
import de.symeda.sormas.api.utils.DurationHours;
import de.symeda.sormas.api.utils.InjectionSite;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.app.backend.clinicalcourse.HealthConditions;
import de.symeda.sormas.app.backend.common.EmbeddedAdo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;

@Entity(name = Symptoms.TABLE_NAME)
@DatabaseTable(tableName = Symptoms.TABLE_NAME)
@EmbeddedAdo
public class Symptoms extends PseudonymizableAdo {

	private static final long serialVersionUID = 392886645668778670L;

	public static final String TABLE_NAME = "symptoms";
	public static final String I18N_PREFIX = "Symptoms";

	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date onsetDate;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String onsetSymptom;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String symptomsComments;
	@DatabaseField
	private String otherLesionAreas;
	@DatabaseField
	private Boolean symptomatic;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientIllLocation;

	@Deprecated
	@DatabaseField(columnName = "illLocation_id")
	private Long illLocationId;
	@Deprecated
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date illLocationFrom;
	@Deprecated
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date illLocationTo;

	@Column(columnDefinition = "float8")
	private Float temperature;
	@Enumerated(EnumType.STRING)
	private TemperatureSource temperatureSource;
	@Column
	private Integer bloodPressureSystolic;
	@Column
	private Integer bloodPressureDiastolic;
	@Column
	private Integer heartRate;
	@Column
	private Integer respiratoryRate;
	@Column
	private Integer weight;
	@Column
	private Integer height;
	@Column
	private Integer midUpperArmCircumference;
	@Column
	private Integer glasgowComaScale;

	@Enumerated(EnumType.STRING)
	private SymptomState fever;
	@Enumerated(EnumType.STRING)
	private SymptomState vomiting;
	@Enumerated(EnumType.STRING)
	private SymptomState diarrhea;
	@Enumerated(EnumType.STRING)
	private SymptomState bloodInStool;
	@Enumerated(EnumType.STRING)
	private SymptomState nausea;
	@Enumerated(EnumType.STRING)
	private SymptomState abdominalPain;
	@Enumerated(EnumType.STRING)
	private SymptomState headache;
	@Enumerated(EnumType.STRING)
	private SymptomState musclePain;
	@Enumerated(EnumType.STRING)
	private SymptomState fatigueWeakness;
	@Enumerated(EnumType.STRING)
	private SymptomState unexplainedBleeding;
	@Enumerated(EnumType.STRING)
	private SymptomState gumsBleeding;
	@Enumerated(EnumType.STRING)
	private SymptomState injectionSiteBleeding;
	@Enumerated(EnumType.STRING)
	private SymptomState noseBleeding;
	@Enumerated(EnumType.STRING)
	private SymptomState bloodyBlackStool;
	@Enumerated(EnumType.STRING)
	private SymptomState redBloodVomit;
	@Enumerated(EnumType.STRING)
	private SymptomState digestedBloodVomit;
	@Enumerated(EnumType.STRING)
	private SymptomState coughingBlood;
	@Enumerated(EnumType.STRING)
	private SymptomState bleedingVagina;
	@Enumerated(EnumType.STRING)
	private SymptomState skinBruising;
	@Enumerated(EnumType.STRING)
	private SymptomState bloodUrine;
	@Enumerated(EnumType.STRING)
	private SymptomState skinRash;
	@Enumerated(EnumType.STRING)
	private SymptomState neckStiffness;
	@Enumerated(EnumType.STRING)
	private SymptomState soreThroat;
	@Enumerated(EnumType.STRING)
	private SymptomState cough;
	@Enumerated(EnumType.STRING)
	private SymptomState coughWithSputum;
	@Enumerated(EnumType.STRING)
	private SymptomState coughWithHeamoptysis;
	@Enumerated(EnumType.STRING)
	private SymptomState runnyNose;
	@Enumerated(EnumType.STRING)
	private SymptomState difficultyBreathing;
	@Enumerated(EnumType.STRING)
	private SymptomState chestPain;
	@Enumerated(EnumType.STRING)
	private SymptomState conjunctivitis;
	@Enumerated(EnumType.STRING)
	private SymptomState eyePainLightSensitive;
	@Enumerated(EnumType.STRING)
	private SymptomState kopliksSpots;
	@Enumerated(EnumType.STRING)
	private SymptomState throbocytopenia;
	@Enumerated(EnumType.STRING)
	private SymptomState otitisMedia;
	@Enumerated(EnumType.STRING)
	private SymptomState hearingloss;
	@Enumerated(EnumType.STRING)
	private SymptomState dehydration;
	@Enumerated(EnumType.STRING)
	private SymptomState anorexiaAppetiteLoss;
	@Enumerated(EnumType.STRING)
	private SymptomState refusalFeedorDrink;
	@Enumerated(EnumType.STRING)
	private SymptomState jointPain;
	@Enumerated(EnumType.STRING)
	private SymptomState hiccups;
	@Enumerated(EnumType.STRING)
	private SymptomState backache;
	@Enumerated(EnumType.STRING)
	private SymptomState eyesBleeding;
	@Enumerated(EnumType.STRING)
	private SymptomState jaundice;
	@Enumerated(EnumType.STRING)
	private YesNoUnknown jaundiceWithin24HoursOfBirth;
	@Enumerated(EnumType.STRING)
	private SymptomState darkUrine;
	@Enumerated(EnumType.STRING)
	private SymptomState stomachBleeding;
	@Enumerated(EnumType.STRING)
	private SymptomState rapidBreathing;
	@Enumerated(EnumType.STRING)
	private SymptomState swollenGlands;
	@Column
	@Deprecated
	private SymptomState cutaneousEruption;
	@Enumerated(EnumType.STRING)
	private SymptomState lesions;
	@Enumerated(EnumType.STRING)
	private SymptomState rashes;
	@Enumerated(EnumType.STRING)
	private SymptomState vesicularRash;
	@Enumerated(EnumType.STRING)
	private SymptomState macularRash;
	@Enumerated(EnumType.STRING)
	private SymptomState papularRash;
	@Enumerated(EnumType.STRING)
	private SymptomState lesionsThatItch;
	@Enumerated(EnumType.STRING)
	private SymptomState lesionsSameState;
	@Enumerated(EnumType.STRING)
	private SymptomState lesionsSameSize;
	@Enumerated(EnumType.STRING)
	private SymptomState lesionsDeepProfound;
	@DatabaseField
	private Boolean lesionsFace;
	@DatabaseField
	private Boolean lesionsLegs;
	@DatabaseField
	private Boolean lesionsSolesFeet;
	@DatabaseField
	private Boolean lesionsPalmsHands;
	@DatabaseField
	private Boolean lesionsThorax;
	@DatabaseField
	private Boolean lesionsArms;
	@DatabaseField
	private Boolean lesionsGenitals;
	@DatabaseField
	private Boolean lesionsAllOverBody;
	@Enumerated(EnumType.STRING)
	private SymptomState lesionsResembleImg1;
	@Enumerated(EnumType.STRING)
	private SymptomState lesionsResembleImg2;
	@Enumerated(EnumType.STRING)
	private SymptomState lesionsResembleImg3;
	@Enumerated(EnumType.STRING)
	private SymptomState lesionsResembleImg4;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date lesionsOnsetDate;
	@Enumerated(EnumType.STRING)
	private SymptomState lymphadenopathy;
	@Enumerated(EnumType.STRING)
	private SymptomState lymphadenopathyInguinal;
	@Enumerated(EnumType.STRING)
	private SymptomState lymphadenopathyAxillary;
	@Enumerated(EnumType.STRING)
	private SymptomState lymphadenopathyCervical;
	@Enumerated(EnumType.STRING)
	private SymptomState chillsSweats;
	@Enumerated(EnumType.STRING)
	private SymptomState bedridden;
	@Enumerated(EnumType.STRING)
	private SymptomState oralUlcers;
	@Enumerated(EnumType.STRING)
	private SymptomState painfulLymphadenitis;
	@Enumerated(EnumType.STRING)
	private SymptomState buboesGroinArmpitNeck;
	@Enumerated(EnumType.STRING)
	private SymptomState blackeningDeathOfTissue;
	@Enumerated(EnumType.STRING)
	private SymptomState bulgingFontanelle;
	@Enumerated(EnumType.STRING)
	private SymptomState pharyngealErythema;
	@Enumerated(EnumType.STRING)
	private SymptomState pharyngealExudate;
	@Enumerated(EnumType.STRING)
	private SymptomState oedemaFaceNeck;
	@Enumerated(EnumType.STRING)
	private SymptomState oedemaLowerExtremity;
	@Enumerated(EnumType.STRING)
	private SymptomState lossSkinTurgor;
	@Enumerated(EnumType.STRING)
	private SymptomState palpableLiver;
	@Enumerated(EnumType.STRING)
	private SymptomState palpableSpleen;
	@Enumerated(EnumType.STRING)
	private SymptomState malaise;
	@Enumerated(EnumType.STRING)
	private SymptomState sunkenEyesFontanelle;
	@Enumerated(EnumType.STRING)
	private SymptomState sidePain;
	@Enumerated(EnumType.STRING)
	private SymptomState fluidInLungCavity;
	@Enumerated(EnumType.STRING)
	private SymptomState tremor;

	@Enumerated(EnumType.STRING)
	private SymptomState hydrophobia;
	@Enumerated(EnumType.STRING)
	private SymptomState opisthotonus;
	@Enumerated(EnumType.STRING)
	private SymptomState anxietyStates;
	@Enumerated(EnumType.STRING)
	private SymptomState delirium;
	@Enumerated(EnumType.STRING)
	private SymptomState uproariousness;
	@Enumerated(EnumType.STRING)
	private SymptomState paresthesiaAroundWound;
	@Enumerated(EnumType.STRING)
	private SymptomState excessSalivation;
	@Enumerated(EnumType.STRING)
	private SymptomState insomnia;
	@Enumerated(EnumType.STRING)
	private SymptomState paralysis;
	@Enumerated(EnumType.STRING)
	private SymptomState excitation;
	@Enumerated(EnumType.STRING)
	private SymptomState dysphagia;
	@Enumerated(EnumType.STRING)
	private SymptomState aerophobia;
	@Enumerated(EnumType.STRING)
	private SymptomState hyperactivity;
	@Enumerated(EnumType.STRING)
	private SymptomState paresis;
	@Enumerated(EnumType.STRING)
	private SymptomState agitation;
	@Enumerated(EnumType.STRING)
	private SymptomState ascendingFlaccidParalysis;
	@Enumerated(EnumType.STRING)
	private SymptomState erraticBehaviour;
	@Enumerated(EnumType.STRING)
	private SymptomState coma;
	@Enumerated(EnumType.STRING)
	private SymptomState respiratoryDiseaseVentilation;
	@DatabaseField(dataType = DataType.ENUM_STRING, columnName = "generalSignsOfDisease")
	private SymptomState feelingIll;
	@Enumerated(EnumType.STRING)
	private SymptomState fastHeartRate;
	@Enumerated(EnumType.STRING)
	private SymptomState oxygenSaturationLower94;

	@Enumerated(EnumType.STRING)
	private SymptomState convulsion;
	@Enumerated(EnumType.STRING)
	private SymptomState otherHemorrhagicSymptoms;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String otherHemorrhagicSymptomsText;
	@Enumerated(EnumType.STRING)
	private SymptomState otherNonHemorrhagicSymptoms;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String otherNonHemorrhagicSymptomsText;

	// complications
	@Enumerated(EnumType.STRING)
	private SymptomState alteredConsciousness;
	@Enumerated(EnumType.STRING)
	private SymptomState confusedDisoriented;
	@Enumerated(EnumType.STRING)
	private SymptomState hemorrhagicSyndrome;
	@Enumerated(EnumType.STRING)
	private SymptomState hyperglycemia;
	@Enumerated(EnumType.STRING)
	private SymptomState hypoglycemia;
	@Enumerated(EnumType.STRING)
	private SymptomState meningealSigns;
	@Enumerated(EnumType.STRING)
	private SymptomState seizures;
	@Enumerated(EnumType.STRING)
	private SymptomState sepsis;
	@Enumerated(EnumType.STRING)
	private SymptomState shock;
	@Enumerated(EnumType.STRING)
	private SymptomState bilateralCataracts;
	@Enumerated(EnumType.STRING)
	private SymptomState unilateralCataracts;
	@Enumerated(EnumType.STRING)
	private SymptomState congenitalGlaucoma;
	@Enumerated(EnumType.STRING)
	private SymptomState pigmentaryRetinopathy;
	@Enumerated(EnumType.STRING)
	private SymptomState purpuricRash;
	@Enumerated(EnumType.STRING)
	private SymptomState microcephaly;
	@Enumerated(EnumType.STRING)
	private SymptomState developmentalDelay;
	@Enumerated(EnumType.STRING)
	private SymptomState splenomegaly;
	@Enumerated(EnumType.STRING)
	private SymptomState meningoencephalitis;
	@Enumerated(EnumType.STRING)
	private SymptomState radiolucentBoneDisease;
	@Enumerated(EnumType.STRING)
	private SymptomState congenitalHeartDisease;
	@Enumerated(EnumType.STRING)
	private CongenitalHeartDiseaseType congenitalHeartDiseaseType;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String congenitalHeartDiseaseDetails;
	@Enumerated(EnumType.STRING)
	private SymptomState fluidInLungCavityAuscultation;
	@Enumerated(EnumType.STRING)
	private SymptomState fluidInLungCavityXray;
	@Enumerated(EnumType.STRING)
	private SymptomState abnormalLungXrayFindings;
	@Enumerated(EnumType.STRING)
	private SymptomState conjunctivalInjection;
	@Enumerated(EnumType.STRING)
	private SymptomState acuteRespiratoryDistressSyndrome;
	@Enumerated(EnumType.STRING)
	private SymptomState pneumoniaClinicalOrRadiologic;
	@Enumerated(EnumType.STRING)
	private SymptomState lossOfTaste;
	@Enumerated(EnumType.STRING)
	private SymptomState lossOfSmell;
	@Enumerated(EnumType.STRING)
	private SymptomState wheezing;
	@Enumerated(EnumType.STRING)
	private SymptomState skinUlcers;
	@Enumerated(EnumType.STRING)
	private SymptomState inabilityToWalk;
	@Enumerated(EnumType.STRING)
	private SymptomState inDrawingOfChestWall;
	@Enumerated(EnumType.STRING)
	private SymptomState otherComplications;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String otherComplicationsText;

	@Enumerated(EnumType.STRING)
	private SymptomState feverishFeeling;
	@Enumerated(EnumType.STRING)
	private SymptomState weakness;
	@Enumerated(EnumType.STRING)
	private SymptomState fatigue;
	@Enumerated(EnumType.STRING)
	private SymptomState coughWithoutSputum;
	@Enumerated(EnumType.STRING)
	private SymptomState breathlessness;
	@Enumerated(EnumType.STRING)
	private SymptomState chestPressure;
	@Enumerated(EnumType.STRING)
	private SymptomState blueLips;
	@Enumerated(EnumType.STRING)
	private SymptomState bloodCirculationProblems;
	@Enumerated(EnumType.STRING)
	private SymptomState palpitations;
	@Enumerated(EnumType.STRING)
	private SymptomState dizzinessStandingUp;
	@Enumerated(EnumType.STRING)
	private SymptomState highOrLowBloodPressure;
	@Enumerated(EnumType.STRING)
	private SymptomState urinaryRetention;
	@Enumerated(EnumType.STRING)
	private SymptomState shivering;
	@Enumerated(EnumType.STRING)
	private SymptomState  generalBodilyPains;
	@DatabaseField
	private Boolean lesionsTrunk;
	@DatabaseField
	private Boolean lesionsNeck;
	@Enumerated(EnumType.STRING)
	private SymptomState abdominalCramps;

	private Date dateOfOnset;
	private String provisionalDiagnosis;
	private Date dateOnsetParalysis;
	private String requestedSiteOfParalysisString;
	private String firstSignOrSymptomsBeforeWormOthers;
	private String numberOfWorms;
	private String requestedSymptomsSelectedString;
	private Date dateOfOnsetRash;
	private String requestedRashSymptomsString;
	private String rashSymptomsOtherAreas;
	private String typeOfRashString;
	private String symptomsSelectedOther;
	private String outcomeOther;
	private Date outcomeDate;
	private String outcomePlaceCommVillage;
	private String nameService;
	private String placeOfFuneralNameVillage;
	private String injectionSiteString;
	private DurationHours durationHours;
	private Integer ageAtDeathDays;
	private Integer ageAtOnsetDays;
	private Set<SymptomsList> symptomsSelected;
	private Set<BodyPart> rashSymptoms;
	private Set<InjectionSite> siteOfParalysis;
	private SymptomsList typeOfRash;
	private Set<InjectionSite> injectionSite;
	private String nameOfHealthFacility;
	private Date dateFirstWormEmergence;

	@Enumerated(EnumType.STRING)
	private CaseOutcome outcome;

	@Enumerated(EnumType.STRING)
	private YesNo feverBodyTempGreater;

	@Enumerated(EnumType.STRING)
	private SymptomState difficultySwallow;

	@Enumerated(EnumType.STRING)
	private SymptomState skinRashNew;

	@Enumerated(EnumType.STRING)
	private SymptomState generalizedRash;

	@Enumerated(EnumType.STRING)
	private SymptomState redEyes;

	@Enumerated(EnumType.STRING)
	private SymptomState swollenLymphNodesBehindEars;

	@Enumerated(EnumType.STRING)
	private SymptomState muscleTone;

	@Enumerated(EnumType.STRING)
	private SymptomState deepTendonReflex;

	@Enumerated(EnumType.STRING)
	private SymptomState muscleVolume;

	@Enumerated(EnumType.STRING)
	private SymptomState sensoryLoss;

	@Enumerated(EnumType.STRING)
	private SymptomState nonVascular;

	@Enumerated(EnumType.STRING)
	private YesNoUnknown feverOnsetParalysis;

	@Enumerated(EnumType.STRING)
	private YesNoUnknown progressiveParalysis;

	@Enumerated(EnumType.STRING)
	private YesNoUnknown progressiveFlaccidAcute;

	@Enumerated(EnumType.STRING)
	private YesNoUnknown assymetric;

	@Enumerated(EnumType.STRING)
	private YesNo paralysedLimbSensitiveToPain;

	@Enumerated(EnumType.STRING)
	private YesNo injectionSiteBeforeOnsetParalysis;

	@Enumerated(EnumType.STRING)
	private YesNo trueAfp;

	@Enumerated(EnumType.STRING)
	private SymptomState dyspnea;

	@Enumerated(EnumType.STRING)
	private SymptomState tachypnea;

	@Enumerated(EnumType.STRING)
	private SymptomState diarrhoea;

	@Enumerated(EnumType.STRING)
	private SymptomState headaches;

	@Enumerated(EnumType.STRING)
	private SymptomState babyDied;

	@Enumerated(EnumType.STRING)
	private SymptomState babyNormalAtBirth;

	@Enumerated(EnumType.STRING)
	private SymptomState normalCryAndSuck;

	@Enumerated(EnumType.STRING)
	private SymptomState stoppedSuckingAfterTwoDays;

	@Enumerated(EnumType.STRING)
	private SymptomState stiffness;

	@Enumerated(EnumType.STRING)
	private GuineaWormFirstSymptom firstSignOrSymptomsBeforeWorm;

	@Enumerated(EnumType.STRING)
	private SymptomState emergenceOfGuineaWorm;

	@Enumerated(EnumType.STRING)
	private SymptomState firstWormThisYear;

	@Enumerated(EnumType.STRING)
	private SymptomState caseDetectedBeforeWormEmergence;

	@Enumerated(EnumType.STRING)
	private SymptomState bodyAche;

	@Enumerated(EnumType.STRING)
	private SymptomState dizziness;

	@Enumerated(EnumType.STRING)
	private SymptomState excessiveSweating;

	@Enumerated(EnumType.STRING)
	private SymptomState numbness;

	@Enumerated(EnumType.STRING)
	private YesNo symptomsOngoing;

	@Enumerated(EnumType.STRING)
	private YesNo areLesionsSameState;

	@Enumerated(EnumType.STRING)
	private YesNo areLesionsSameSize;

	@Enumerated(EnumType.STRING)
	private YesNo areLesionsDeep;

	@Enumerated(EnumType.STRING)
	private YesNo areUlcersAmong;

	@Enumerated(EnumType.STRING)
	private YesNo patientHaveFever;

	@Enumerated(EnumType.STRING)
	private SymptomState abnormalLungAuscultation;

	private HealthConditions healthConditions;

	@Enumerated(EnumType.STRING)
	private Trimester trimester;

	@Enumerated(EnumType.STRING)
	private YesNo postpartum;

	@Enumerated(EnumType.STRING)
	private YesNo pregnant;


	@Override
	public String getI18nPrefix() {
		return I18N_PREFIX;
	}

	public Date getOnsetDate() {
		return onsetDate;
	}

	public void setOnsetDate(Date onsetDate) {
		this.onsetDate = onsetDate;
	}

	public Float getTemperature() {
		return temperature;
	}

	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}

	public TemperatureSource getTemperatureSource() {
		return temperatureSource;
	}

	public void setTemperatureSource(TemperatureSource temperatureSource) {
		this.temperatureSource = temperatureSource;
	}

	public Integer getBloodPressureSystolic() {
		return bloodPressureSystolic;
	}

	public void setBloodPressureSystolic(Integer bloodPressureSystolic) {
		this.bloodPressureSystolic = bloodPressureSystolic;
	}

	public Integer getBloodPressureDiastolic() {
		return bloodPressureDiastolic;
	}

	public void setBloodPressureDiastolic(Integer bloodPressureDiastolic) {
		this.bloodPressureDiastolic = bloodPressureDiastolic;
	}

	public Integer getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(Integer heartRate) {
		this.heartRate = heartRate;
	}

	public SymptomState getFever() {
		return fever;
	}

	public void setFever(SymptomState fever) {
		this.fever = fever;
	}

	public SymptomState getDiarrhea() {
		return diarrhea;
	}

	public void setDiarrhea(SymptomState diarrhea) {
		this.diarrhea = diarrhea;
	}

	public SymptomState getAnorexiaAppetiteLoss() {
		return anorexiaAppetiteLoss;
	}

	public void setAnorexiaAppetiteLoss(SymptomState anorexiaAppetiteLoss) {
		this.anorexiaAppetiteLoss = anorexiaAppetiteLoss;
	}

	public SymptomState getAbdominalPain() {
		return abdominalPain;
	}

	public void setAbdominalPain(SymptomState abdominalPain) {
		this.abdominalPain = abdominalPain;
	}

	public SymptomState getChestPain() {
		return chestPain;
	}

	public void setChestPain(SymptomState chestPain) {
		this.chestPain = chestPain;
	}

	public SymptomState getMusclePain() {
		return musclePain;
	}

	public void setMusclePain(SymptomState musclePain) {
		this.musclePain = musclePain;
	}

	public SymptomState getJointPain() {
		return jointPain;
	}

	public void setJointPain(SymptomState jointPain) {
		this.jointPain = jointPain;
	}

	public SymptomState getHeadache() {
		return headache;
	}

	public void setHeadache(SymptomState headache) {
		this.headache = headache;
	}

	public SymptomState getCough() {
		return cough;
	}

	public void setCough(SymptomState cough) {
		this.cough = cough;
	}

	public SymptomState getDifficultyBreathing() {
		return difficultyBreathing;
	}

	public void setDifficultyBreathing(SymptomState difficultyBreathing) {
		this.difficultyBreathing = difficultyBreathing;
	}

	public SymptomState getSoreThroat() {
		return soreThroat;
	}

	public void setSoreThroat(SymptomState soreThroat) {
		this.soreThroat = soreThroat;
	}

	public SymptomState getConjunctivitis() {
		return conjunctivitis;
	}

	public void setConjunctivitis(SymptomState conjunctivitis) {
		this.conjunctivitis = conjunctivitis;
	}

	public SymptomState getSkinRash() {
		return skinRash;
	}

	public void setSkinRash(SymptomState skinRash) {
		this.skinRash = skinRash;
	}
	public SymptomState getVesicularRash() {
		return vesicularRash;
	}

	public void setVesicularRash(SymptomState vesicularRash) {
		this.vesicularRash = vesicularRash;
	}
	public SymptomState getMacularRash() {
		return macularRash;
	}

	public void setMacularRash(SymptomState macularRash){
		this.macularRash = macularRash;
	}
	public SymptomState getPapularRash() {
		return papularRash;
	}

	public void setPapularRash(SymptomState papularRash){
		this.papularRash = papularRash;
	}

	public SymptomState getHiccups() {
		return hiccups;
	}

	public void setHiccups(SymptomState hiccups) {
		this.hiccups = hiccups;
	}

	public SymptomState getEyePainLightSensitive() {
		return eyePainLightSensitive;
	}

	public void setEyePainLightSensitive(SymptomState eyePainLightSensitive) {
		this.eyePainLightSensitive = eyePainLightSensitive;
	}

	public SymptomState getConfusedDisoriented() {
		return confusedDisoriented;
	}

	public void setConfusedDisoriented(SymptomState confusedDisoriented) {
		this.confusedDisoriented = confusedDisoriented;
	}

	public SymptomState getUnexplainedBleeding() {
		return unexplainedBleeding;
	}

	public void setUnexplainedBleeding(SymptomState unexplainedBleeding) {
		this.unexplainedBleeding = unexplainedBleeding;
	}

	public SymptomState getGumsBleeding() {
		return gumsBleeding;
	}

	public void setGumsBleeding(SymptomState gumsBleeding) {
		this.gumsBleeding = gumsBleeding;
	}

	public SymptomState getInjectionSiteBleeding() {
		return injectionSiteBleeding;
	}

	public void setInjectionSiteBleeding(SymptomState injectionSiteBleeding) {
		this.injectionSiteBleeding = injectionSiteBleeding;
	}

	public SymptomState getDigestedBloodVomit() {
		return digestedBloodVomit;
	}

	public void setDigestedBloodVomit(SymptomState digestedBloodVomit) {
		this.digestedBloodVomit = digestedBloodVomit;
	}

	public SymptomState getBleedingVagina() {
		return bleedingVagina;
	}

	public void setBleedingVagina(SymptomState bleedingVagina) {
		this.bleedingVagina = bleedingVagina;
	}

	public SymptomState getDehydration() {
		return dehydration;
	}

	public void setDehydration(SymptomState dehydration) {
		this.dehydration = dehydration;
	}

	public SymptomState getFatigueWeakness() {
		return fatigueWeakness;
	}

	public void setFatigueWeakness(SymptomState fatigueWeakness) {
		this.fatigueWeakness = fatigueWeakness;
	}

	public SymptomState getKopliksSpots() {
		return kopliksSpots;
	}

	public void setKopliksSpots(SymptomState kopliksSpots) {
		this.kopliksSpots = kopliksSpots;
	}

	public SymptomState getNausea() {
		return nausea;
	}

	public void setNausea(SymptomState nausea) {
		this.nausea = nausea;
	}

	public SymptomState getNeckStiffness() {
		return neckStiffness;
	}

	public void setNeckStiffness(SymptomState neckStiffness) {
		this.neckStiffness = neckStiffness;
	}

	public String getOnsetSymptom() {
		return onsetSymptom;
	}

	public void setOnsetSymptom(String onsetSymptom) {
		this.onsetSymptom = onsetSymptom;
	}

	public SymptomState getOtitisMedia() {
		return otitisMedia;
	}

	public void setOtitisMedia(SymptomState otitisMedia) {
		this.otitisMedia = otitisMedia;
	}

	public SymptomState getRefusalFeedorDrink() {
		return refusalFeedorDrink;
	}

	public void setRefusalFeedorDrink(SymptomState refusalFeedorDrink) {
		this.refusalFeedorDrink = refusalFeedorDrink;
	}

	public SymptomState getRunnyNose() {
		return runnyNose;
	}

	public void setRunnyNose(SymptomState runnyNose) {
		this.runnyNose = runnyNose;
	}

	public Boolean getSymptomatic() {
		return symptomatic;
	}

	public void setSymptomatic(Boolean symptomatic) {
		this.symptomatic = symptomatic;
	}

	public SymptomState getVomiting() {
		return vomiting;
	}

	public void setVomiting(SymptomState vomiting) {
		this.vomiting = vomiting;
	}

	public SymptomState getOtherHemorrhagicSymptoms() {
		return otherHemorrhagicSymptoms;
	}

	public void setOtherHemorrhagicSymptoms(SymptomState otherHemorrhagicSymptoms) {
		this.otherHemorrhagicSymptoms = otherHemorrhagicSymptoms;
	}

	public String getOtherHemorrhagicSymptomsText() {
		return otherHemorrhagicSymptomsText;
	}

	public void setOtherHemorrhagicSymptomsText(String otherHemorrhagicSymptomsText) {
		this.otherHemorrhagicSymptomsText = otherHemorrhagicSymptomsText;
	}

	public SymptomState getOtherNonHemorrhagicSymptoms() {
		return otherNonHemorrhagicSymptoms;
	}

	public void setOtherNonHemorrhagicSymptoms(SymptomState otherNonHemorrhagicSymptoms) {
		this.otherNonHemorrhagicSymptoms = otherNonHemorrhagicSymptoms;
	}

	public String getOtherNonHemorrhagicSymptomsText() {
		return otherNonHemorrhagicSymptomsText;
	}

	public void setOtherNonHemorrhagicSymptomsText(String otherNonHemorrhagicSymptomsText) {
		this.otherNonHemorrhagicSymptomsText = otherNonHemorrhagicSymptomsText;
	}

	public String getSymptomsComments() {
		return symptomsComments;
	}

	public void setSymptomsComments(String symptomsComments) {
		this.symptomsComments = symptomsComments;
	}

	public SymptomState getBloodInStool() {
		return bloodInStool;
	}

	public void setBloodInStool(SymptomState bloodInStool) {
		this.bloodInStool = bloodInStool;
	}

	public SymptomState getNoseBleeding() {
		return noseBleeding;
	}

	public void setNoseBleeding(SymptomState noseBleeding) {
		this.noseBleeding = noseBleeding;
	}

	public SymptomState getBloodyBlackStool() {
		return bloodyBlackStool;
	}

	public void setBloodyBlackStool(SymptomState bloodyBlackStool) {
		this.bloodyBlackStool = bloodyBlackStool;
	}

	public SymptomState getRedBloodVomit() {
		return redBloodVomit;
	}

	public void setRedBloodVomit(SymptomState redBloodVomit) {
		this.redBloodVomit = redBloodVomit;
	}

	public SymptomState getCoughingBlood() {
		return coughingBlood;
	}

	public void setCoughingBlood(SymptomState coughingBlood) {
		this.coughingBlood = coughingBlood;
	}

	public SymptomState getSkinBruising() {
		return skinBruising;
	}

	public void setSkinBruising(SymptomState skinBruising) {
		this.skinBruising = skinBruising;
	}

	public SymptomState getBloodUrine() {
		return bloodUrine;
	}

	public void setBloodUrine(SymptomState bloodUrine) {
		this.bloodUrine = bloodUrine;
	}

	public SymptomState getThrobocytopenia() {
		return throbocytopenia;
	}

	public void setThrobocytopenia(SymptomState throbocytopenia) {
		this.throbocytopenia = throbocytopenia;
	}

	public SymptomState getHearingloss() {
		return hearingloss;
	}

	public void setHearingloss(SymptomState hearingloss) {
		this.hearingloss = hearingloss;
	}

	public SymptomState getShock() {
		return shock;
	}

	public void setShock(SymptomState shock) {
		this.shock = shock;
	}

	public SymptomState getSeizures() {
		return seizures;
	}

	public void setSeizures(SymptomState seizures) {
		this.seizures = seizures;
	}

	public SymptomState getAlteredConsciousness() {
		return alteredConsciousness;
	}

	public void setAlteredConsciousness(SymptomState alteredConsciousness) {
		this.alteredConsciousness = alteredConsciousness;
	}

	public SymptomState getBackache() {
		return backache;
	}

	public void setBackache(SymptomState backache) {
		this.backache = backache;
	}

	public SymptomState getEyesBleeding() {
		return eyesBleeding;
	}

	public void setEyesBleeding(SymptomState eyesBleeding) {
		this.eyesBleeding = eyesBleeding;
	}

	public SymptomState getJaundice() {
		return jaundice;
	}

	public void setJaundice(SymptomState jaundice) {
		this.jaundice = jaundice;
	}

	public SymptomState getDarkUrine() {
		return darkUrine;
	}

	public void setDarkUrine(SymptomState darkUrine) {
		this.darkUrine = darkUrine;
	}

	public SymptomState getStomachBleeding() {
		return stomachBleeding;
	}

	public void setStomachBleeding(SymptomState stomachBleeding) {
		this.stomachBleeding = stomachBleeding;
	}

	public SymptomState getRapidBreathing() {
		return rapidBreathing;
	}

	public void setRapidBreathing(SymptomState rapidBreathing) {
		this.rapidBreathing = rapidBreathing;
	}

	public SymptomState getSwollenGlands() {
		return swollenGlands;
	}

	public void setSwollenGlands(SymptomState swollenGlands) {
		this.swollenGlands = swollenGlands;
	}

	public SymptomState getLesions() {
		return lesions;
	}

	public void setLesions(SymptomState lesions) {
		this.lesions = lesions;
	}
	public SymptomState getRashes() {
		return rashes;
	}

	public void setRashes(SymptomState rashes) {
		this.rashes = rashes;
	}

	public SymptomState getLesionsThatItch() {
		return lesionsThatItch;
	}

	public void setLesionsThatItch(SymptomState lesionsThatItch) {
		this.lesionsThatItch = lesionsThatItch;
	}

	public SymptomState getLesionsSameState() {
		return lesionsSameState;
	}

	public void setLesionsSameState(SymptomState lesionsSameState) {
		this.lesionsSameState = lesionsSameState;
	}

	public SymptomState getLesionsSameSize() {
		return lesionsSameSize;
	}

	public void setLesionsSameSize(SymptomState lesionsSameSize) {
		this.lesionsSameSize = lesionsSameSize;
	}

	public SymptomState getLesionsDeepProfound() {
		return lesionsDeepProfound;
	}

	public void setLesionsDeepProfound(SymptomState lesionsDeepProfound) {
		this.lesionsDeepProfound = lesionsDeepProfound;
	}

	public Boolean getLesionsFace() {
		return lesionsFace;
	}

	public void setLesionsFace(Boolean lesionsFace) {
		this.lesionsFace = lesionsFace;
	}

	public Boolean getLesionsLegs() {
		return lesionsLegs;
	}

	public void setLesionsLegs(Boolean lesionsLegs) {
		this.lesionsLegs = lesionsLegs;
	}

	public Boolean getLesionsSolesFeet() {
		return lesionsSolesFeet;
	}

	public void setLesionsSolesFeet(Boolean lesionsSolesFeet) {
		this.lesionsSolesFeet = lesionsSolesFeet;
	}

	public Boolean getLesionsPalmsHands() {
		return lesionsPalmsHands;
	}

	public void setLesionsPalmsHands(Boolean lesionsPalmsHands) {
		this.lesionsPalmsHands = lesionsPalmsHands;
	}

	public Boolean getLesionsThorax() {
		return lesionsThorax;
	}

	public void setLesionsThorax(Boolean lesionsThorax) {
		this.lesionsThorax = lesionsThorax;
	}

	public Boolean getLesionsArms() {
		return lesionsArms;
	}

	public void setLesionsArms(Boolean lesionsArms) {
		this.lesionsArms = lesionsArms;
	}

	public Boolean getLesionsGenitals() {
		return lesionsGenitals;
	}

	public void setLesionsGenitals(Boolean lesionsGenitals) {
		this.lesionsGenitals = lesionsGenitals;
	}

	public Boolean getLesionsAllOverBody() {
		return lesionsAllOverBody;
	}

	public void setLesionsAllOverBody(Boolean lesionsAllOverBody) {
		this.lesionsAllOverBody = lesionsAllOverBody;
	}

	public SymptomState getLesionsResembleImg1() {
		return lesionsResembleImg1;
	}

	public void setLesionsResembleImg1(SymptomState lesionsResembleImg1) {
		this.lesionsResembleImg1 = lesionsResembleImg1;
	}

	public SymptomState getLesionsResembleImg2() {
		return lesionsResembleImg2;
	}

	public void setLesionsResembleImg2(SymptomState lesionsResembleImg2) {
		this.lesionsResembleImg2 = lesionsResembleImg2;
	}

	public SymptomState getLesionsResembleImg3() {
		return lesionsResembleImg3;
	}

	public void setLesionsResembleImg3(SymptomState lesionsResembleImg3) {
		this.lesionsResembleImg3 = lesionsResembleImg3;
	}

	public SymptomState getLesionsResembleImg4() {
		return lesionsResembleImg4;
	}

	public void setLesionsResembleImg4(SymptomState lesionsResembleImg4) {
		this.lesionsResembleImg4 = lesionsResembleImg4;
	}

	public Date getLesionsOnsetDate() {
		return lesionsOnsetDate;
	}

	public void setLesionsOnsetDate(Date lesionsOnsetDate) {
		this.lesionsOnsetDate = lesionsOnsetDate;
	}

	public SymptomState getLymphadenopathyInguinal() {
		return lymphadenopathyInguinal;
	}

	public void setLymphadenopathyInguinal(SymptomState lymphadenopathyInguinal) {
		this.lymphadenopathyInguinal = lymphadenopathyInguinal;
	}

	public SymptomState getLymphadenopathyAxillary() {
		return lymphadenopathyAxillary;
	}

	public void setLymphadenopathyAxillary(SymptomState lymphadenopathyAxillary) {
		this.lymphadenopathyAxillary = lymphadenopathyAxillary;
	}

	public SymptomState getLymphadenopathyCervical() {
		return lymphadenopathyCervical;
	}

	public void setLymphadenopathyCervical(SymptomState lymphadenopathyCervical) {
		this.lymphadenopathyCervical = lymphadenopathyCervical;
	}

	public SymptomState getChillsSweats() {
		return chillsSweats;
	}

	public void setChillsSweats(SymptomState chillsSweats) {
		this.chillsSweats = chillsSweats;
	}

	public SymptomState getBedridden() {
		return bedridden;
	}

	public void setBedridden(SymptomState bedridden) {
		this.bedridden = bedridden;
	}

	public SymptomState getOralUlcers() {
		return oralUlcers;
	}

	public void setOralUlcers(SymptomState oralUlcers) {
		this.oralUlcers = oralUlcers;
	}

	public SymptomState getPainfulLymphadenitis() {
		return painfulLymphadenitis;
	}

	public void setPainfulLymphadenitis(SymptomState painfulLymphadenitis) {
		this.painfulLymphadenitis = painfulLymphadenitis;
	}

	public SymptomState getBuboesGroinArmpitNeck() {
		return buboesGroinArmpitNeck;
	}

	public void setBuboesGroinArmpitNeck(SymptomState buboesGroinArmpitNeck) {
		this.buboesGroinArmpitNeck = buboesGroinArmpitNeck;
	}

	public SymptomState getBlackeningDeathOfTissue() {
		return blackeningDeathOfTissue;
	}

	public void setBlackeningDeathOfTissue(SymptomState blackeningDeathOfTissue) {
		this.blackeningDeathOfTissue = blackeningDeathOfTissue;
	}

	public SymptomState getBulgingFontanelle() {
		return bulgingFontanelle;
	}

	public void setBulgingFontanelle(SymptomState bulgingFontanelle) {
		this.bulgingFontanelle = bulgingFontanelle;
	}

	public SymptomState getPharyngealErythema() {
		return pharyngealErythema;
	}

	public void setPharyngealErythema(SymptomState pharyngealErythema) {
		this.pharyngealErythema = pharyngealErythema;
	}

	public SymptomState getPharyngealExudate() {
		return pharyngealExudate;
	}

	public void setPharyngealExudate(SymptomState pharyngealExudate) {
		this.pharyngealExudate = pharyngealExudate;
	}

	public SymptomState getOedemaFaceNeck() {
		return oedemaFaceNeck;
	}

	public void setOedemaFaceNeck(SymptomState oedemaFaceNeck) {
		this.oedemaFaceNeck = oedemaFaceNeck;
	}

	public SymptomState getOedemaLowerExtremity() {
		return oedemaLowerExtremity;
	}

	public void setOedemaLowerExtremity(SymptomState oedemaLowerExtremity) {
		this.oedemaLowerExtremity = oedemaLowerExtremity;
	}

	public SymptomState getLossSkinTurgor() {
		return lossSkinTurgor;
	}

	public void setLossSkinTurgor(SymptomState lossSkinTurgor) {
		this.lossSkinTurgor = lossSkinTurgor;
	}

	public SymptomState getPalpableLiver() {
		return palpableLiver;
	}

	public void setPalpableLiver(SymptomState palpableLiver) {
		this.palpableLiver = palpableLiver;
	}

	public SymptomState getPalpableSpleen() {
		return palpableSpleen;
	}

	public void setPalpableSpleen(SymptomState palpableSpleen) {
		this.palpableSpleen = palpableSpleen;
	}

	public SymptomState getMalaise() {
		return malaise;
	}

	public void setMalaise(SymptomState malaise) {
		this.malaise = malaise;
	}

	public SymptomState getSunkenEyesFontanelle() {
		return sunkenEyesFontanelle;
	}

	public void setSunkenEyesFontanelle(SymptomState sunkenEyesFontanelle) {
		this.sunkenEyesFontanelle = sunkenEyesFontanelle;
	}

	public SymptomState getSidePain() {
		return sidePain;
	}

	public void setSidePain(SymptomState sidePain) {
		this.sidePain = sidePain;
	}

	public SymptomState getFluidInLungCavity() {
		return fluidInLungCavity;
	}

	public void setFluidInLungCavity(SymptomState fluidInLungCavity) {
		this.fluidInLungCavity = fluidInLungCavity;
	}

	public SymptomState getTremor() {
		return tremor;
	}

	public void setTremor(SymptomState tremor) {
		this.tremor = tremor;
	}

	public SymptomState getHydrophobia() {
		return hydrophobia;
	}

	public void setHydrophobia(SymptomState hydrophobia) {
		this.hydrophobia = hydrophobia;
	}

	public SymptomState getOpisthotonus() {
		return opisthotonus;
	}

	public void setOpisthotonus(SymptomState opisthotonus) {
		this.opisthotonus = opisthotonus;
	}

	public SymptomState getAnxietyStates() {
		return anxietyStates;
	}

	public void setAnxietyStates(SymptomState anxietyStates) {
		this.anxietyStates = anxietyStates;
	}

	public SymptomState getDelirium() {
		return delirium;
	}

	public void setDelirium(SymptomState delirium) {
		this.delirium = delirium;
	}

	public SymptomState getUproariousness() {
		return uproariousness;
	}

	public void setUproariousness(SymptomState uproariousness) {
		this.uproariousness = uproariousness;
	}

	public SymptomState getParesthesiaAroundWound() {
		return paresthesiaAroundWound;
	}

	public void setParesthesiaAroundWound(SymptomState paresthesiaAroundWound) {
		this.paresthesiaAroundWound = paresthesiaAroundWound;
	}

	public SymptomState getExcessSalivation() {
		return excessSalivation;
	}

	public void setExcessSalivation(SymptomState excessSalivation) {
		this.excessSalivation = excessSalivation;
	}

	public SymptomState getInsomnia() {
		return insomnia;
	}

	public void setInsomnia(SymptomState insomnia) {
		this.insomnia = insomnia;
	}

	public SymptomState getParalysis() {
		return paralysis;
	}

	public void setParalysis(SymptomState paralysis) {
		this.paralysis = paralysis;
	}

	public SymptomState getExcitation() {
		return excitation;
	}

	public void setExcitation(SymptomState excitation) {
		this.excitation = excitation;
	}

	public SymptomState getDysphagia() {
		return dysphagia;
	}

	public void setDysphagia(SymptomState dysphagia) {
		this.dysphagia = dysphagia;
	}

	public SymptomState getAerophobia() {
		return aerophobia;
	}

	public void setAerophobia(SymptomState aerophobia) {
		this.aerophobia = aerophobia;
	}

	public SymptomState getHyperactivity() {
		return hyperactivity;
	}

	public void setHyperactivity(SymptomState hyperactivity) {
		this.hyperactivity = hyperactivity;
	}

	public SymptomState getParesis() {
		return paresis;
	}

	public void setParesis(SymptomState paresis) {
		this.paresis = paresis;
	}

	public SymptomState getAgitation() {
		return agitation;
	}

	public void setAgitation(SymptomState agitation) {
		this.agitation = agitation;
	}

	public SymptomState getAscendingFlaccidParalysis() {
		return ascendingFlaccidParalysis;
	}

	public void setAscendingFlaccidParalysis(SymptomState ascendingFlaccidParalysis) {
		this.ascendingFlaccidParalysis = ascendingFlaccidParalysis;
	}

	public SymptomState getErraticBehaviour() {
		return erraticBehaviour;
	}

	public void setErraticBehaviour(SymptomState erraticBehaviour) {
		this.erraticBehaviour = erraticBehaviour;
	}

	public SymptomState getComa() {
		return coma;
	}

	public void setComa(SymptomState coma) {
		this.coma = coma;
	}

	public SymptomState getConvulsion() {
		return convulsion;
	}

	public void setConvulsion(SymptomState convulsion) {
		this.convulsion = convulsion;
	}

	public String getPatientIllLocation() {
		return patientIllLocation;
	}

	public void setPatientIllLocation(String patientIllLocation) {
		this.patientIllLocation = patientIllLocation;
	}

	public SymptomState getMeningealSigns() {
		return meningealSigns;
	}

	public void setMeningealSigns(SymptomState meningealSigns) {
		this.meningealSigns = meningealSigns;
	}

	public Integer getRespiratoryRate() {
		return respiratoryRate;
	}

	public void setRespiratoryRate(Integer respiratoryRate) {
		this.respiratoryRate = respiratoryRate;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getMidUpperArmCircumference() {
		return midUpperArmCircumference;
	}

	public void setMidUpperArmCircumference(Integer midUpperArmCircumference) {
		this.midUpperArmCircumference = midUpperArmCircumference;
	}

	public Integer getGlasgowComaScale() {
		return glasgowComaScale;
	}

	public void setGlasgowComaScale(Integer glasgowComaScale) {
		this.glasgowComaScale = glasgowComaScale;
	}

	public SymptomState getHemorrhagicSyndrome() {
		return hemorrhagicSyndrome;
	}

	public void setHemorrhagicSyndrome(SymptomState hemorrhagicSyndrome) {
		this.hemorrhagicSyndrome = hemorrhagicSyndrome;
	}

	public SymptomState getHyperglycemia() {
		return hyperglycemia;
	}

	public void setHyperglycemia(SymptomState hyperglycemia) {
		this.hyperglycemia = hyperglycemia;
	}

	public SymptomState getHypoglycemia() {
		return hypoglycemia;
	}

	public void setHypoglycemia(SymptomState hypoglycemia) {
		this.hypoglycemia = hypoglycemia;
	}

	public SymptomState getSepsis() {
		return sepsis;
	}

	public void setSepsis(SymptomState sepsis) {
		this.sepsis = sepsis;
	}

	public YesNoUnknown getJaundiceWithin24HoursOfBirth() {
		return jaundiceWithin24HoursOfBirth;
	}

	public void setJaundiceWithin24HoursOfBirth(YesNoUnknown jaundiceWithin24HoursOfBirth) {
		this.jaundiceWithin24HoursOfBirth = jaundiceWithin24HoursOfBirth;
	}

	public SymptomState getBilateralCataracts() {
		return bilateralCataracts;
	}

	public void setBilateralCataracts(SymptomState bilateralCataracts) {
		this.bilateralCataracts = bilateralCataracts;
	}

	public SymptomState getUnilateralCataracts() {
		return unilateralCataracts;
	}

	public void setUnilateralCataracts(SymptomState unilateralCataracts) {
		this.unilateralCataracts = unilateralCataracts;
	}

	public SymptomState getCongenitalGlaucoma() {
		return congenitalGlaucoma;
	}

	public void setCongenitalGlaucoma(SymptomState congenitalGlaucoma) {
		this.congenitalGlaucoma = congenitalGlaucoma;
	}

	public SymptomState getPigmentaryRetinopathy() {
		return pigmentaryRetinopathy;
	}

	public void setPigmentaryRetinopathy(SymptomState pigmentaryRetinopathy) {
		this.pigmentaryRetinopathy = pigmentaryRetinopathy;
	}

	public SymptomState getPurpuricRash() {
		return purpuricRash;
	}

	public void setPurpuricRash(SymptomState purpuricRash) {
		this.purpuricRash = purpuricRash;
	}

	public SymptomState getMicrocephaly() {
		return microcephaly;
	}

	public void setMicrocephaly(SymptomState microcephaly) {
		this.microcephaly = microcephaly;
	}

	public SymptomState getDevelopmentalDelay() {
		return developmentalDelay;
	}

	public void setDevelopmentalDelay(SymptomState developmentalDelay) {
		this.developmentalDelay = developmentalDelay;
	}

	public SymptomState getSplenomegaly() {
		return splenomegaly;
	}

	public void setSplenomegaly(SymptomState splenomegaly) {
		this.splenomegaly = splenomegaly;
	}

	public SymptomState getMeningoencephalitis() {
		return meningoencephalitis;
	}

	public void setMeningoencephalitis(SymptomState meningoencephalitis) {
		this.meningoencephalitis = meningoencephalitis;
	}

	public SymptomState getRadiolucentBoneDisease() {
		return radiolucentBoneDisease;
	}

	public void setRadiolucentBoneDisease(SymptomState radiolucentBoneDisease) {
		this.radiolucentBoneDisease = radiolucentBoneDisease;
	}

	public SymptomState getCongenitalHeartDisease() {
		return congenitalHeartDisease;
	}

	public void setCongenitalHeartDisease(SymptomState congenitalHeartDisease) {
		this.congenitalHeartDisease = congenitalHeartDisease;
	}

	public CongenitalHeartDiseaseType getCongenitalHeartDiseaseType() {
		return congenitalHeartDiseaseType;
	}

	public void setCongenitalHeartDiseaseType(CongenitalHeartDiseaseType congenitalHeartDiseaseType) {
		this.congenitalHeartDiseaseType = congenitalHeartDiseaseType;
	}

	public String getCongenitalHeartDiseaseDetails() {
		return congenitalHeartDiseaseDetails;
	}

	public void setCongenitalHeartDiseaseDetails(String congenitalHeartDiseaseDetails) {
		this.congenitalHeartDiseaseDetails = congenitalHeartDiseaseDetails;
	}

	public SymptomState getFluidInLungCavityAuscultation() {
		return fluidInLungCavityAuscultation;
	}

	public void setFluidInLungCavityAuscultation(SymptomState fluidInLungCavityAuscultation) {
		this.fluidInLungCavityAuscultation = fluidInLungCavityAuscultation;
	}

	public SymptomState getFluidInLungCavityXray() {
		return fluidInLungCavityXray;
	}

	public void setFluidInLungCavityXray(SymptomState fluidInLungCavityXray) {
		this.fluidInLungCavityXray = fluidInLungCavityXray;
	}

	public SymptomState getAbnormalLungXrayFindings() {
		return abnormalLungXrayFindings;
	}

	public void setAbnormalLungXrayFindings(SymptomState abnormalLungXrayFindings) {
		this.abnormalLungXrayFindings = abnormalLungXrayFindings;
	}

	public SymptomState getConjunctivalInjection() {
		return conjunctivalInjection;
	}

	public void setConjunctivalInjection(SymptomState conjunctivalInjection) {
		this.conjunctivalInjection = conjunctivalInjection;
	}

	public SymptomState getAcuteRespiratoryDistressSyndrome() {
		return acuteRespiratoryDistressSyndrome;
	}

	public void setAcuteRespiratoryDistressSyndrome(SymptomState acuteRespiratoryDistressSyndrome) {
		this.acuteRespiratoryDistressSyndrome = acuteRespiratoryDistressSyndrome;
	}

	public SymptomState getPneumoniaClinicalOrRadiologic() {
		return pneumoniaClinicalOrRadiologic;
	}

	public void setPneumoniaClinicalOrRadiologic(SymptomState pneumoniaClinicalOrRadiologic) {
		this.pneumoniaClinicalOrRadiologic = pneumoniaClinicalOrRadiologic;
	}

	public SymptomState getLossOfTaste() {
		return lossOfTaste;
	}

	public void setLossOfTaste(SymptomState lossOfTaste) {
		this.lossOfTaste = lossOfTaste;
	}

	public SymptomState getLossOfSmell() {
		return lossOfSmell;
	}

	public void setLossOfSmell(SymptomState lossOfSmell) {
		this.lossOfSmell = lossOfSmell;
	}

	public SymptomState getCoughWithSputum() {
		return coughWithSputum;
	}

	public void setCoughWithSputum(SymptomState coughWithSputum) {
		this.coughWithSputum = coughWithSputum;
	}

	public SymptomState getCoughWithHeamoptysis() {
		return coughWithHeamoptysis;
	}

	public void setCoughWithHeamoptysis(SymptomState coughWithHeamoptysis) {
		this.coughWithHeamoptysis = coughWithHeamoptysis;
	}

	public SymptomState getLymphadenopathy() {
		return lymphadenopathy;
	}

	public void setLymphadenopathy(SymptomState lymphadenopathy) {
		this.lymphadenopathy = lymphadenopathy;
	}

	public SymptomState getWheezing() {
		return wheezing;
	}

	public void setWheezing(SymptomState wheezing) {
		this.wheezing = wheezing;
	}

	public SymptomState getSkinUlcers() {
		return skinUlcers;
	}

	public void setSkinUlcers(SymptomState skinUlcers) {
		this.skinUlcers = skinUlcers;
	}

	public SymptomState getInabilityToWalk() {
		return inabilityToWalk;
	}

	public void setInabilityToWalk(SymptomState inabilityToWalk) {
		this.inabilityToWalk = inabilityToWalk;
	}

	public SymptomState getInDrawingOfChestWall() {
		return inDrawingOfChestWall;
	}

	public void setInDrawingOfChestWall(SymptomState inDrawingOfChestWall) {
		this.inDrawingOfChestWall = inDrawingOfChestWall;
	}

	public SymptomState getOtherComplications() {
		return otherComplications;
	}

	public void setOtherComplications(SymptomState otherComplications) {
		this.otherComplications = otherComplications;
	}

	public String getOtherComplicationsText() {
		return otherComplicationsText;
	}

	public void setOtherComplicationsText(String otherComplicationsText) {
		this.otherComplicationsText = otherComplicationsText;
	}

	public SymptomState getRespiratoryDiseaseVentilation() {
		return respiratoryDiseaseVentilation;
	}

	public void setRespiratoryDiseaseVentilation(SymptomState respiratoryDiseaseVentilation) {
		this.respiratoryDiseaseVentilation = respiratoryDiseaseVentilation;
	}

	public SymptomState getFeelingIll() {
		return feelingIll;
	}

	public void setFeelingIll(SymptomState feelingIll) {
		this.feelingIll = feelingIll;
	}

	public SymptomState getFastHeartRate() {
		return fastHeartRate;
	}

	public void setFastHeartRate(SymptomState fastHeartRate) {
		this.fastHeartRate = fastHeartRate;
	}

	public SymptomState getOxygenSaturationLower94() {
		return oxygenSaturationLower94;
	}

	public void setOxygenSaturationLower94(SymptomState oxygenSaturationLower94) {
		this.oxygenSaturationLower94 = oxygenSaturationLower94;
	}

	public SymptomState getFeverishFeeling() {
		return feverishFeeling;
	}

	public void setFeverishFeeling(SymptomState feverishFeeling) {
		this.feverishFeeling = feverishFeeling;
	}

	public SymptomState getWeakness() {
		return weakness;
	}

	public void setWeakness(SymptomState weakness) {
		this.weakness = weakness;
	}

	public SymptomState getFatigue() {
		return fatigue;
	}

	public void setFatigue(SymptomState fatigue) {
		this.fatigue = fatigue;
	}

	public SymptomState getCoughWithoutSputum() {
		return coughWithoutSputum;
	}

	public void setCoughWithoutSputum(SymptomState coughWithoutSputum) {
		this.coughWithoutSputum = coughWithoutSputum;
	}

	public SymptomState getBreathlessness() {
		return breathlessness;
	}

	public void setBreathlessness(SymptomState breathlessness) {
		this.breathlessness = breathlessness;
	}

	public SymptomState getChestPressure() {
		return chestPressure;
	}

	public void setChestPressure(SymptomState chestPressure) {
		this.chestPressure = chestPressure;
	}

	public SymptomState getBlueLips() {
		return blueLips;
	}

	public void setBlueLips(SymptomState blueLips) {
		this.blueLips = blueLips;
	}

	public SymptomState getBloodCirculationProblems() {
		return bloodCirculationProblems;
	}

	public void setBloodCirculationProblems(SymptomState bloodCirculationProblems) {
		this.bloodCirculationProblems = bloodCirculationProblems;
	}

	public SymptomState getPalpitations() {
		return palpitations;
	}

	public void setPalpitations(SymptomState palpitations) {
		this.palpitations = palpitations;
	}

	public SymptomState getDizzinessStandingUp() {
		return dizzinessStandingUp;
	}

	public void setDizzinessStandingUp(SymptomState dizzinessStandingUp) {
		this.dizzinessStandingUp = dizzinessStandingUp;
	}

	public SymptomState getHighOrLowBloodPressure() {
		return highOrLowBloodPressure;
	}

	public void setHighOrLowBloodPressure(SymptomState highOrLowBloodPressure) {
		this.highOrLowBloodPressure = highOrLowBloodPressure;
	}

	public SymptomState getUrinaryRetention() {
		return urinaryRetention;
	}

	public void setUrinaryRetention(SymptomState urinaryRetention) {
		this.urinaryRetention = urinaryRetention;
	}

	public SymptomState getShivering() {
		return shivering;
	}

	public void setShivering(SymptomState shivering) {
		this.shivering = shivering;
	}

	public SymptomState  getGeneralBodilyPains() {
		return generalBodilyPains;
	}

	public void setGeneralBodilyPains(SymptomState  generalBodilyPains) {
		this.generalBodilyPains = generalBodilyPains;
	}
	public Boolean getLesionsTrunk() {
		return lesionsTrunk;
	}

	public void setLesionsTrunk(Boolean lesionsTrunk) {
		this.lesionsTrunk = lesionsTrunk;
	}
	public Boolean getLesionsNeck() {
		return lesionsNeck;
	}

	public void setLesionsNeck(Boolean lesionsNeck) {
		this.lesionsNeck = lesionsNeck;
	}

	public String getOtherLesionAreas() {
		return otherLesionAreas;
	}

	public void setOtherLesionAreas(String otherLesionAreas) {
		this.otherLesionAreas = otherLesionAreas;
	}

	public SymptomState getAbdominalCramps() {
		return abdominalCramps;
	}

	public void setAbdominalCramps(SymptomState abdominalCramps) {
		this.abdominalCramps = abdominalCramps;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateOfOnset() {
		return dateOfOnset;
	}

	public void setDateOfOnset(Date dateOfOnset) {
		this.dateOfOnset = dateOfOnset;
	}
	@Enumerated(EnumType.STRING)
	public YesNo getFeverBodyTempGreater() {
		return feverBodyTempGreater;
	}

	public void setFeverBodyTempGreater(YesNo feverBodyTempGreater) {
		this.feverBodyTempGreater = feverBodyTempGreater;
	}
	@Enumerated(EnumType.STRING)
	public SymptomState getDifficultySwallow() {
		return difficultySwallow;
	}

	public void setDifficultySwallow(SymptomState difficultySwallow) {
		this.difficultySwallow = difficultySwallow;
	}

	@Enumerated(EnumType.STRING)
	public SymptomState getSkinRashNew() {
		return skinRashNew;
	}

	public void setSkinRashNew(SymptomState skinRashNew) {
		this.skinRashNew = skinRashNew;
	}

	@Enumerated(EnumType.STRING)
	public SymptomState getMuscleTone() {
		return muscleTone;
	}

	public void setMuscleTone(SymptomState muscleTone) {
		this.muscleTone = muscleTone;
	}
	@Enumerated(EnumType.STRING)
	public SymptomState getDeepTendonReflex() {
		return deepTendonReflex;
	}

	public void setDeepTendonReflex(SymptomState deepTendonReflex) {
		this.deepTendonReflex = deepTendonReflex;
	}

	@Enumerated(EnumType.STRING)
	public SymptomState getMuscleVolume() {
		return muscleVolume;
	}

	public void setMuscleVolume(SymptomState muscleVolume) {
		this.muscleVolume = muscleVolume;
	}
	@Enumerated(EnumType.STRING)
	public SymptomState getSensoryLoss() {
		return sensoryLoss;
	}

	public void setSensoryLoss(SymptomState sensoryLoss) {
		this.sensoryLoss = sensoryLoss;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getProvisionalDiagnosis() {
		return provisionalDiagnosis;
	}

	public void setProvisionalDiagnosis(String provisionalDiagnosis) {
		this.provisionalDiagnosis = provisionalDiagnosis;
	}

	@Enumerated
	public SymptomState getNonVascular() {
		return nonVascular;
	}

	public void setNonVascular(SymptomState nonVascular) {
		this.nonVascular = nonVascular;
	}

	public YesNoUnknown getFeverOnsetParalysis() {
		return feverOnsetParalysis;
	}

	public void setFeverOnsetParalysis(YesNoUnknown feverOnsetParalysis) {
		this.feverOnsetParalysis = feverOnsetParalysis;
	}

	public YesNoUnknown getProgressiveParalysis() {
		return progressiveParalysis;
	}

	public void setProgressiveParalysis(YesNoUnknown progressiveParalysis) {
		this.progressiveParalysis = progressiveParalysis;
	}

	public Date getDateOnsetParalysis() {
		return dateOnsetParalysis;
	}

	public void setDateOnsetParalysis(Date dateOnsetParalysis) {
		this.dateOnsetParalysis = dateOnsetParalysis;
	}

	public YesNoUnknown getProgressiveFlaccidAcute() {
		return progressiveFlaccidAcute;
	}

	public void setProgressiveFlaccidAcute(YesNoUnknown progressiveFlaccidAcute) {
		this.progressiveFlaccidAcute = progressiveFlaccidAcute;
	}

	public YesNoUnknown getAssymetric() {
		return assymetric;
	}

	public void setAssymetric(YesNoUnknown assymetric) {
		this.assymetric = assymetric;
	}

	public YesNo getParalysedLimbSensitiveToPain() {
		return paralysedLimbSensitiveToPain;
	}

	public void setParalysedLimbSensitiveToPain(YesNo paralysedLimbSensitiveToPain) {
		this.paralysedLimbSensitiveToPain = paralysedLimbSensitiveToPain;
	}

	public YesNo getInjectionSiteBeforeOnsetParalysis() {
		return injectionSiteBeforeOnsetParalysis;
	}

	public void setInjectionSiteBeforeOnsetParalysis(YesNo injectionSiteBeforeOnsetParalysis) {
		this.injectionSiteBeforeOnsetParalysis = injectionSiteBeforeOnsetParalysis;
	}

	public YesNo getTrueAfp() {
		return trueAfp;
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
	public SymptomState getBodyAche() {
		return bodyAche;
	}

	public void setBodyAche(SymptomState bodyAche) {
		this.bodyAche = bodyAche;
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
	public SymptomState getDizziness() {
		return dizziness;
	}

	public void setDizziness(SymptomState dizziness) {
		this.dizziness = dizziness;
	}
	public SymptomState getExcessiveSweating() {
		return excessiveSweating;
	}

	public void setExcessiveSweating(SymptomState excessiveSweating) {
		this.excessiveSweating = excessiveSweating;
	}
	public SymptomState getNumbness() {
		return numbness;
	}
	public void setNumbness(SymptomState numbness) {
		this.numbness = numbness;
	}
	public String getOutcomeOther() { return outcomeOther;}
	public void setOutcomeOther(String outcomeOther) {
		this.outcomeOther = outcomeOther;
	}
	public YesNo getSymptomsOngoing(){
		return symptomsOngoing;
	}

	public void setSymptomsOngoing(YesNo symptomsOngoing){
		this.symptomsOngoing = symptomsOngoing;
	}

	public DurationHours getDurationHours(){
		return durationHours;
	}

	public void setDurationHours(DurationHours durationHours){
		this.durationHours = durationHours;
	}

	public String getNameOfHealthFacility(){
		return nameOfHealthFacility;
	}

	public void setNameOfHealthFacility(String nameOfHealthFacility){
		this.nameOfHealthFacility = nameOfHealthFacility;
	}

	@Transient
	public Set<SymptomsList> getSymptomsSelected() {
		if (symptomsSelected == null) {
			if (StringUtils.isEmpty(requestedSymptomsSelectedString)) {
				symptomsSelected = new HashSet<>();
			} else {
				symptomsSelected =
						Arrays.stream(requestedSymptomsSelectedString.split(",")).map(SymptomsList::valueOf).collect(Collectors.toSet());
			}
		}
		return symptomsSelected;
	}

	public void setSymptomsSelected(Set<SymptomsList> symptomsSelected) {
		this.symptomsSelected = symptomsSelected;

		if (this.symptomsSelected == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		symptomsSelected.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		requestedSymptomsSelectedString = sb.toString();
	}

	public String getRequestedSymptomsSelectedString() {
		return requestedSymptomsSelectedString;
	}

	public void setRequestedSymptomsSelectedString(String requestedSymptomsSelectedString) {
		this.requestedSymptomsSelectedString = requestedSymptomsSelectedString;
		symptomsSelected = null;
	}
	public Date getDateOfOnsetRash(){
		return dateOfOnsetRash;
	}
	public void setDateOfOnsetRash(Date dateOfOnsetRash) {
		this.dateOfOnsetRash = dateOfOnsetRash;
	}

	@Transient
	public Set<BodyPart> getRashSymptoms() {
		if (rashSymptoms == null) {
			if (StringUtils.isEmpty(requestedRashSymptomsString)) {
				rashSymptoms = new HashSet<>();
			} else {
				rashSymptoms =
						Arrays.stream(requestedRashSymptomsString.split(",")).map(BodyPart::valueOf).collect(Collectors.toSet());
			}
		}
		return rashSymptoms;
	}
	public void setRashSymptoms(Set<BodyPart> rashSymptoms) {
		this.rashSymptoms = rashSymptoms;

		if (this.rashSymptoms == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		rashSymptoms.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		requestedRashSymptomsString = sb.toString();
	}

	@Transient
	public Set<InjectionSite> getSiteOfParalysis() {
		if (siteOfParalysis == null) {
			if (StringUtils.isEmpty(requestedSiteOfParalysisString)) {
				siteOfParalysis = new HashSet<>();
			} else {
				siteOfParalysis =
						Arrays.stream(requestedSiteOfParalysisString.split(",")).map(InjectionSite::valueOf).collect(Collectors.toSet());
			}
		}
		return siteOfParalysis;
	}

	public void setSiteOfParalysis(Set<InjectionSite> siteOfParalysis) {
		this.siteOfParalysis = siteOfParalysis;

		if (this.siteOfParalysis == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		siteOfParalysis.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		requestedSiteOfParalysisString = sb.toString();
	}

	public String getRashSymptomsOtherAreas(){
		return rashSymptomsOtherAreas;
	}
	public void setRashSymptomsOtherAreas(String rashSymptomsOtherAreas) {
		this.rashSymptomsOtherAreas = rashSymptomsOtherAreas;
	}

	public YesNo getAreLesionsSameState(){
		return areLesionsSameState;
	}
	public void setAreLesionsSameState(YesNo areLesionsSameState) {
		this.areLesionsSameState = areLesionsSameState;
	}
	public YesNo getAreLesionsSameSize(){
		return areLesionsSameSize;
	}
	public void setAreLesionsSameSize(YesNo areLesionsSameSize) {
		this.areLesionsSameSize = areLesionsSameSize;
	}
	public YesNo getAreLesionsDeep(){
		return areLesionsDeep;
	}
	public void setAreLesionsDeep(YesNo areLesionsDeep) {
		this.areLesionsDeep = areLesionsDeep;
	}
	public YesNo getAreUlcersAmong(){
		return areUlcersAmong;
	}
	public void setAreUlcersAmong(YesNo areUlcersAmong) {
		this.areUlcersAmong = areUlcersAmong;
	}

	public SymptomsList getTypeOfRash() {
		return typeOfRash;
	}
	public void setTypeOfRash(SymptomsList typeOfRash) {
		this.typeOfRash = typeOfRash;
	}

	public String getSymptomsSelectedOther(){
		return symptomsSelectedOther;
	}
	public void setSymptomsSelectedOther(String symptomsSelectedOther) {
		this.symptomsSelectedOther = symptomsSelectedOther;
	}

	public String getRequestedRashSymptomsString() {
		return requestedRashSymptomsString;
	}

	public void setRequestedRashSymptomsString(String requestedRashSymptomsString) {
		this.requestedRashSymptomsString = requestedRashSymptomsString;
		rashSymptoms = null;
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
	@Enumerated(EnumType.STRING)
	public SymptomState getDiarrhoea() {
		return diarrhoea;
	}

	public void setDiarrhoea(SymptomState diarrhoea) {
		this.diarrhoea = diarrhoea;
	}

	@Enumerated(EnumType.STRING)
	public SymptomState getHeadaches() {
		return headaches;
	}

	public void setHeadaches(SymptomState headaches) {
		this.headaches = headaches;
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
	public String getRequestedSiteOfParalysisString() {
		return requestedSiteOfParalysisString;
	}

	public void setRequestedSiteOfParalysisString(String requestedSiteOfParalysisString) {
		this.requestedSiteOfParalysisString = requestedSiteOfParalysisString;
		siteOfParalysis = null;
	}

	public YesNo getPatientHaveFever() {
		return patientHaveFever;
	}
	public void setPatientHaveFever(YesNo patientHaveFever) {
		this.patientHaveFever = patientHaveFever;
	}
	public Date getOutcomeDate() {
		return outcomeDate;
	}
	public void setOutcomeDate(Date outcomeDate) {
		this.outcomeDate = outcomeDate;
	}
	public String getNameService() {
		return nameService;
	}
	public void setNameService(String nameService) {
		this.nameService = nameService;
	}

	public String getOutcomePlaceCommVillage() {
		return outcomePlaceCommVillage;
	}
	public void setOutcomePlaceCommVillage(String outcomePlaceCommVillage) {
		this.outcomePlaceCommVillage = outcomePlaceCommVillage;
	}
	public String getPlaceOfFuneralNameVillage() {
		return placeOfFuneralNameVillage;
	}
	public void setPlaceOfFuneralNameVillage(String placeOfFuneralNameVillage) {
		this.placeOfFuneralNameVillage = placeOfFuneralNameVillage;
	}

	@Transient
	public Set<InjectionSite> getInjectionSite() {
		if (injectionSite == null) {
			if (StringUtils.isEmpty(injectionSiteString)) {
				injectionSite = new HashSet<>();
			} else {
				injectionSite =
						Arrays.stream(injectionSiteString.split(",")).map(InjectionSite::valueOf).collect(Collectors.toSet());
			}
		}
		return injectionSite;
	}

	public void setInjectionSite(Set<InjectionSite> injectionSite) {
		this.injectionSite = injectionSite;

		if (this.injectionSite == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		injectionSite.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		injectionSiteString = sb.toString();
	}

	public String getInjectionSiteString() {
		return injectionSiteString;
	}

	public void setInjectionSiteString(String injectionSiteString) {
		this.injectionSiteString = injectionSiteString;
		injectionSite = null;
	}

	public SymptomState getAbnormalLungAuscultation() {
		return abnormalLungAuscultation;
	}

	public void setAbnormalLungAuscultation(SymptomState abnormalLungAuscultation) {
		this.abnormalLungAuscultation = abnormalLungAuscultation;
	}

	/*@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public HealthConditions getHealthConditions() {
		return healthConditions;
	}

	public void setHealthConditions(HealthConditions healthConditions) {
		this.healthConditions = healthConditions;
	}*/

	public Trimester getTrimester() {
		return trimester;
	}

	public void setTrimester(Trimester trimester) {
		this.trimester = trimester;
	}

	public YesNo getPostpartum() {
		return postpartum;
	}

	public void setPostpartum(YesNo postpartum) {
		this.postpartum = postpartum;
	}

	public YesNo getPregnant() {
		return pregnant;
	}

	public void setPregnant(YesNo pregnant) {
		this.pregnant = pregnant;
	}

	public CaseOutcome getOutcome() {
		return outcome;
	}
	public void setOutcome(CaseOutcome outcome) {
		this.outcome = outcome;
	}
}
