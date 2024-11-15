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

package de.symeda.sormas.app.backend.epidata;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import org.apache.commons.lang3.StringUtils;
import de.symeda.sormas.api.epidata.ContactSetting;
import de.symeda.sormas.api.epidata.PlaceManaged;
import de.symeda.sormas.api.riskfactor.DrinkingWaterSource;
import de.symeda.sormas.api.sample.PathogenTestType;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.symeda.sormas.api.caze.CaseOutcome;
import de.symeda.sormas.api.utils.RiskFactorCondition;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.app.backend.activityascase.ActivityAsCase;
import de.symeda.sormas.app.backend.common.EmbeddedAdo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.containmentmeasure.ContainmentMeasure;
import de.symeda.sormas.app.backend.contaminationsource.ContaminationSource;
import de.symeda.sormas.app.backend.exposure.Exposure;
import de.symeda.sormas.app.backend.persontravelhistory.PersonTravelHistory;
import de.symeda.sormas.app.backend.region.Community;
import de.symeda.sormas.app.backend.region.District;
import de.symeda.sormas.app.backend.region.Region;

@Entity(name = EpiData.TABLE_NAME)
@DatabaseTable(tableName = EpiData.TABLE_NAME)
@EmbeddedAdo
public class EpiData extends PseudonymizableAdo {

	private static final long serialVersionUID = -8294812479501735785L;

	public static final String TABLE_NAME = "epidata";
	public static final String I18N_PREFIX = "EpiData";

	@Enumerated(EnumType.STRING)
	private YesNo exposureDetailsKnown;
	@Enumerated(EnumType.STRING)
	private YesNo activityAsCaseDetailsKnown;
	@Enumerated(EnumType.STRING)
	private YesNo recentTravelOutbreak;
	@Enumerated(EnumType.STRING)
	private YesNo contactSimilarOutbreak;
	@Enumerated(EnumType.STRING)
	private YesNo contactSickAnimals;
	@Enumerated(EnumType.STRING)
	private YesNo contactDeadAnimals;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String ifYesSpecifyDead;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String ifYesSpecifySick;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String ifYesWildAnimalLocation;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date ifYesWildAnimalDate;
	@Enumerated(EnumType.STRING)
	private YesNo contactWithSourceCaseKnown;
	@Enumerated(EnumType.STRING)
	private YesNo highTransmissionRiskArea;
	@Enumerated(EnumType.STRING)
	private YesNo largeOutbreaksArea;
	@Enumerated(EnumType.STRING)
	private YesNo areaInfectedAnimals;

	private YesNo patientTravelDuringIllness;
	private String comm1;
	private String healthCenter1;
	private String country1;
	private String comm2;
	private String healthCenter2;
	private String country2;
	private YesNo wasPatientHospitalized;
	private String ifYesWhere;
	private Date hospitalizedDate1;
	private Date hospitalizedDate2;
	private YesNo didPatientConsultHealer;
	private String ifYesNameHealer;
	private String community;
	private String country;
	private String whenWhereContactTakePlace;
	private Date dateOfContact;
	private YesNo patientReceiveTraditionalMedicine;
	private String ifYesExplain;
	private YesNo patientAttendFuneralCeremonies;
	private YesNo patientTravelAnytimePeriodBeforeIll;
	private String ifTravelYesWhere;
	private Date ifYesStartDate;
	private Date ifYesEndDate;
	private YesNo patientContactKnownSuspect;
	private String suspectName;
	private String suspectLastName;
	private String idCase;
	private CaseOutcome duringContactSuspectCase;
	private Date dateOfDeath;
	private Date dateOfLastContactWithSuspectCase;

	private List<Exposure> exposures = new ArrayList<>();

	private List<ActivityAsCase> activitiesAsCase = new ArrayList<>();
	private List<PersonTravelHistory> personTravelHistories = new ArrayList<>();
	private List<ContainmentMeasure> containmentMeasures = new ArrayList<>();
	private List<ContaminationSource> contaminationSources = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	private YesNo receivedHealthEducation;

	@Enumerated(EnumType.STRING)
	private YesNo patientEnteredWaterSource;

	@Enumerated(EnumType.STRING)
	private PlaceManaged placeManaged;

	@Enumerated(EnumType.STRING)
	private YesNoUnknown patientTravelledTwoWeeksPrior;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientTravelledInCountryOne;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientTravelledInCountryTwo;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientTravelledInCountryThree;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientTravelledInCountryFour;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientTravelledInternationalOne;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientTravelledInternationalTwo;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientTravelledInternationalThree;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientTravelledInternationalFour;

	@Enumerated(EnumType.STRING)
	private YesNoUnknown patientVisitedHealthCareFacility;

	@Enumerated(EnumType.STRING)
	private YesNoUnknown patientCloseContactWithARI;

	@Enumerated(EnumType.STRING)
	private YesNoUnknown patientContactWithConfirmedCase;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientContactWithConfirmedCaseExposureLocationCityCountry;
	public YesNo getExposureDetailsKnown() {
		return exposureDetailsKnown;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientCloseContactWithARIContactSettingsString;

	@Transient
	private Set<ContactSetting> patientCloseContactWithARIContactSettings;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientContactWithConfirmedCaseExposureLocationsString;

	@Transient
	private Set<ContactSetting> patientContactWithConfirmedCaseExposureLocations;

	@Enumerated(EnumType.STRING)
	private YesNo historyOfTravelOutsideTheVillageTownDistrict;

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Region historyOfTravelRegion;

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private District historyOfTravelDistrict;

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Community historyOfTravelSubDistrict;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String historyOfTravelVillage;

	@Enumerated(EnumType.STRING)
	private YesNo exposedToRiskFactor;

	@Enumerated(EnumType.STRING)
	private DrinkingWaterSource waterUsedByPatientAfterExposure;

	@Enumerated(EnumType.STRING)
	private YesNo waterUsedForDrinking;

	@Enumerated(EnumType.STRING)
	private YesNo waterUsedNotForDrinking;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private YesNo foodItems;
	private YesNo previouslyVaccinatedAgainstInfluenza;
	private YesNo previouslyVaccinatedAgainstCovid;
	private Integer yearOfVaccination;
	private Integer yearOfVaccinationCovid;
	private String nameOfVaccine;
	private String nameOfVaccineCovid;
	private String placesVisitedPastSevenDays;
	private YesNo vistedPlacesConfirmedPandemic;
	private RiskFactorCondition riskFactorsSevereDisease;
	private String otherSpecify;

	public void setExposureDetailsKnown(YesNo exposureDetailsKnown) {
		this.exposureDetailsKnown = exposureDetailsKnown;
	}

	public YesNo getActivityAsCaseDetailsKnown() {
		return activityAsCaseDetailsKnown;
	}

	public void setActivityAsCaseDetailsKnown(YesNo activityAsCaseDetailsKnown) {
		this.activityAsCaseDetailsKnown = activityAsCaseDetailsKnown;
	}

	public YesNo getRecentTravelOutbreak() {
		return recentTravelOutbreak;
	}

	public void setRecentTravelOutbreak(YesNo recentTravelOutbreak) {
		this.recentTravelOutbreak = recentTravelOutbreak;
	}

	public YesNo getContactSimilarOutbreak() {
		return contactSimilarOutbreak;
	}

	public void setContactSimilarOutbreak(YesNo contactSimilarOutbreak) {
		this.contactSimilarOutbreak = contactSimilarOutbreak;
	}

	public YesNo getContactSickAnimals() {
		return contactSickAnimals;
	}

	public void setContactSickAnimals(YesNo contactSickAnimals) {
		this.contactSickAnimals = contactSickAnimals;
	}

	public YesNo getContactDeadAnimals() {
		return contactDeadAnimals;
	}

	public void setContactDeadAnimals(YesNo contactDeadAnimals) {
		this.contactDeadAnimals = contactDeadAnimals;
	}

	public String getIfYesSpecifyDead() {
		return ifYesSpecifyDead;
	}

	public void setIfYesSpecifyDead(String ifYesSpecifyDead) {
		this.ifYesSpecifyDead = ifYesSpecifyDead;
	}

	public String getIfYesSpecifySick() {
		return ifYesSpecifySick;
	}

	public void setIfYesSpecifySick(String ifYesSpecifySick) {
		this.ifYesSpecifySick = ifYesSpecifySick;
	}

	public String getIfYesWildAnimalLocation() {
		return ifYesWildAnimalLocation;
	}

	public void setIfYesWildAnimalLocation(String ifYesWildAnimalLocation) {
		this.ifYesWildAnimalLocation = ifYesWildAnimalLocation;
	}

	public Date getIfYesWildAnimalDate() {
		return ifYesWildAnimalDate;
	}

	public void setIfYesWildAnimalDate(Date ifYesWildAnimalDate) {
		this.ifYesWildAnimalDate = ifYesWildAnimalDate;
	}

	public YesNo getContactWithSourceCaseKnown() {
		return contactWithSourceCaseKnown;
	}

	public void setContactWithSourceCaseKnown(YesNo contactWithSourceCaseKnown) {
		this.contactWithSourceCaseKnown = contactWithSourceCaseKnown;
	}

	public YesNo getHighTransmissionRiskArea() {
		return highTransmissionRiskArea;
	}

	public void setHighTransmissionRiskArea(YesNo highTransmissionRiskArea) {
		this.highTransmissionRiskArea = highTransmissionRiskArea;
	}

	public YesNo getLargeOutbreaksArea() {
		return largeOutbreaksArea;
	}

	public void setLargeOutbreaksArea(YesNo largeOutbreaksArea) {
		this.largeOutbreaksArea = largeOutbreaksArea;
	}

	public YesNo getAreaInfectedAnimals() {
		return areaInfectedAnimals;
	}

	public void setAreaInfectedAnimals(YesNo areaInfectedAnimals) {
		this.areaInfectedAnimals = areaInfectedAnimals;
	}

	public List<Exposure> getExposures() {
		return exposures;
	}

	public void setExposures(List<Exposure> exposures) {
		this.exposures = exposures;
	}

	public List<ActivityAsCase> getActivitiesAsCase() {
		return activitiesAsCase;
	}

	public void setActivitiesAsCase(List<ActivityAsCase> activitiesAsCase) {
		this.activitiesAsCase = activitiesAsCase;
	}

	public List<PersonTravelHistory> getPersonTravelHistories() {
		return personTravelHistories;
	}

	public void setPersonTravelHistories(List<PersonTravelHistory> personTravelHistories) {
		this.personTravelHistories = personTravelHistories;
	}

	public List<ContainmentMeasure> getContainmentMeasures() {
		return containmentMeasures;
	}

	public void setContainmentMeasures(List<ContainmentMeasure> containmentMeasures) {
		this.containmentMeasures = containmentMeasures;
	}

	public List<ContaminationSource> getContaminationSources() {
		return contaminationSources;
	}

	public void setContaminationSources(List<ContaminationSource> contaminationSources) {
		this.contaminationSources = contaminationSources;
	}

	public YesNo getReceivedHealthEducation() {
		return receivedHealthEducation;
	}

	public void setReceivedHealthEducation(YesNo receivedHealthEducation) {
		this.receivedHealthEducation = receivedHealthEducation;
	}

	public YesNo getPatientEnteredWaterSource() {
		return patientEnteredWaterSource;
	}

	public void setPatientEnteredWaterSource(YesNo patientEnteredWaterSource) {
		this.patientEnteredWaterSource = patientEnteredWaterSource;
	}

	public PlaceManaged getPlaceManaged() {
		return placeManaged;
	}

	public void setPlaceManaged(PlaceManaged placeManaged) {
		this.placeManaged = placeManaged;
	}

	public YesNoUnknown getPatientTravelledTwoWeeksPrior() {
		return patientTravelledTwoWeeksPrior;
	}

	public void setPatientTravelledTwoWeeksPrior(YesNoUnknown patientTravelledTwoWeeksPrior) {
		this.patientTravelledTwoWeeksPrior = patientTravelledTwoWeeksPrior;
	}

	public String getPatientTravelledInCountryOne() {
		return patientTravelledInCountryOne;
	}

	public void setPatientTravelledInCountryOne(String patientTravelledInCountryOne) {
		this.patientTravelledInCountryOne = patientTravelledInCountryOne;
	}

	public String getPatientTravelledInCountryTwo() {
		return patientTravelledInCountryTwo;
	}

	public void setPatientTravelledInCountryTwo(String patientTravelledInCountryTwo) {
		this.patientTravelledInCountryTwo = patientTravelledInCountryTwo;
	}

	public String getPatientTravelledInCountryThree() {
		return patientTravelledInCountryThree;
	}

	public void setPatientTravelledInCountryThree(String patientTravelledInCountryThree) {
		this.patientTravelledInCountryThree = patientTravelledInCountryThree;
	}

	public String getPatientTravelledInCountryFour() {
		return patientTravelledInCountryFour;
	}

	public void setPatientTravelledInCountryFour(String patientTravelledInCountryFour) {
		this.patientTravelledInCountryFour = patientTravelledInCountryFour;
	}

	public String getPatientTravelledInternationalOne() {
		return patientTravelledInternationalOne;
	}

	public void setPatientTravelledInternationalOne(String patientTravelledInternationalOne) {
		this.patientTravelledInternationalOne = patientTravelledInternationalOne;
	}

	public String getPatientTravelledInternationalTwo() {
		return patientTravelledInternationalTwo;
	}

	public void setPatientTravelledInternationalTwo(String patientTravelledInternationalTwo) {
		this.patientTravelledInternationalTwo = patientTravelledInternationalTwo;
	}

	public String getPatientTravelledInternationalThree() {
		return patientTravelledInternationalThree;
	}

	public void setPatientTravelledInternationalThree(String patientTravelledInternationalThree) {
		this.patientTravelledInternationalThree = patientTravelledInternationalThree;
	}

	public String getPatientTravelledInternationalFour() {
		return patientTravelledInternationalFour;
	}

	public void setPatientTravelledInternationalFour(String patientTravelledInternationalFour) {
		this.patientTravelledInternationalFour = patientTravelledInternationalFour;
	}

	public YesNoUnknown getPatientVisitedHealthCareFacility() {
		return patientVisitedHealthCareFacility;
	}

	public void setPatientVisitedHealthCareFacility(YesNoUnknown patientVisitedHealthCareFacility) {
		this.patientVisitedHealthCareFacility = patientVisitedHealthCareFacility;
	}

	public YesNoUnknown getPatientCloseContactWithARI() {
		return patientCloseContactWithARI;
	}

	public void setPatientCloseContactWithARI(YesNoUnknown patientCloseContactWithARI) {
		this.patientCloseContactWithARI = patientCloseContactWithARI;
	}

	public YesNoUnknown getPatientContactWithConfirmedCase() {
		return patientContactWithConfirmedCase;
	}

	public void setPatientContactWithConfirmedCase(YesNoUnknown patientContactWithConfirmedCase) {
		this.patientContactWithConfirmedCase = patientContactWithConfirmedCase;
	}

	public String getPatientContactWithConfirmedCaseExposureLocationCityCountry() {
		return patientContactWithConfirmedCaseExposureLocationCityCountry;
	}

	public void setPatientContactWithConfirmedCaseExposureLocationCityCountry(String patientContactWithConfirmedCaseExposureLocationCityCountry) {
		this.patientContactWithConfirmedCaseExposureLocationCityCountry = patientContactWithConfirmedCaseExposureLocationCityCountry;
	}

	public String getPatientCloseContactWithARIContactSettingsString() {
		return patientCloseContactWithARIContactSettingsString;
	}

	public void setPatientCloseContactWithARIContactSettingsString(String patientCloseContactWithARIContactSettingsString) {
		this.patientCloseContactWithARIContactSettingsString = patientCloseContactWithARIContactSettingsString;
		patientCloseContactWithARIContactSettings = null;
	}

	public String getPatientContactWithConfirmedCaseExposureLocationsString() {
		return patientContactWithConfirmedCaseExposureLocationsString;
	}

	public void setPatientContactWithConfirmedCaseExposureLocationsString(String patientContactWithConfirmedCaseExposureLocationsString) {
		this.patientContactWithConfirmedCaseExposureLocationsString = patientContactWithConfirmedCaseExposureLocationsString;
		patientContactWithConfirmedCaseExposureLocations = null;
	}

	public YesNo getHistoryOfTravelOutsideTheVillageTownDistrict() {
		return historyOfTravelOutsideTheVillageTownDistrict;
	}

	public void setHistoryOfTravelOutsideTheVillageTownDistrict(YesNo historyOfTravelOutsideTheVillageTownDistrict) {
		this.historyOfTravelOutsideTheVillageTownDistrict = historyOfTravelOutsideTheVillageTownDistrict;
	}

	public Region getHistoryOfTravelRegion() {
		return historyOfTravelRegion;
	}

	public void setHistoryOfTravelRegion(Region historyOfTravelRegion) {
		this.historyOfTravelRegion = historyOfTravelRegion;
	}

	public District getHistoryOfTravelDistrict() {
		return historyOfTravelDistrict;
	}

	public void setHistoryOfTravelDistrict(District historyOfTravelDistrict) {
		this.historyOfTravelDistrict = historyOfTravelDistrict;
	}

	public Community getHistoryOfTravelSubDistrict() {
		return historyOfTravelSubDistrict;
	}

	public void setHistoryOfTravelSubDistrict(Community historyOfTravelSubDistrict) {
		this.historyOfTravelSubDistrict = historyOfTravelSubDistrict;
	}

	public String getHistoryOfTravelVillage() {
		return historyOfTravelVillage;
	}

	public void setHistoryOfTravelVillage(String historyOfTravelVillage) {
		this.historyOfTravelVillage = historyOfTravelVillage;
	}

	public YesNo getExposedToRiskFactor() {
		return exposedToRiskFactor;
	}

	public void setExposedToRiskFactor(YesNo exposedToRiskFactor) {
		this.exposedToRiskFactor = exposedToRiskFactor;
	}

	public DrinkingWaterSource getWaterUsedByPatientAfterExposure() {
		return waterUsedByPatientAfterExposure;
	}

	public void setWaterUsedByPatientAfterExposure(DrinkingWaterSource waterUsedByPatientAfterExposure) {
		this.waterUsedByPatientAfterExposure = waterUsedByPatientAfterExposure;
	}

	public YesNo getWaterUsedForDrinking() {
		return waterUsedForDrinking;
	}

	public void setWaterUsedForDrinking(YesNo waterUsedForDrinking) {
		this.waterUsedForDrinking = waterUsedForDrinking;
	}

	public YesNo getWaterUsedNotForDrinking() {
		return waterUsedNotForDrinking;
	}

	public void setWaterUsedNotForDrinking(YesNo waterUsedNotForDrinking) {
		this.waterUsedNotForDrinking = waterUsedNotForDrinking;
	}

	public YesNo getFoodItems() {
		return foodItems;
	}

	public void setFoodItems(YesNo foodItems) {
		this.foodItems = foodItems;
	}

	public void setPatientCloseContactWithARIContactSettings(Set<ContactSetting> patientCloseContactWithARIContactSettings) {
		this.patientCloseContactWithARIContactSettings = patientCloseContactWithARIContactSettings;

		if (this.patientCloseContactWithARIContactSettings == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		for (ContactSetting contactSetting : patientCloseContactWithARIContactSettings) {
			sb.append(contactSetting.name());
			sb.append(",");
		}
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		patientCloseContactWithARIContactSettingsString = sb.toString();
	}

	@Transient
	public Set<ContactSetting> getPatientCloseContactWithARIContactSettings() {
		if (patientCloseContactWithARIContactSettings == null) {
			patientCloseContactWithARIContactSettings = new HashSet<>();
			if (!StringUtils.isEmpty(patientCloseContactWithARIContactSettingsString)) {
				String[] contactSettingsTypes = patientCloseContactWithARIContactSettingsString.split(",");
				for (String contactSettingsType : contactSettingsTypes) {
					patientCloseContactWithARIContactSettings.add(ContactSetting.valueOf(contactSettingsType));
				}
			}
		}
		return patientCloseContactWithARIContactSettings;
	}

	public void setPatientContactWithConfirmedCaseExposureLocations(Set<ContactSetting> patientContactWithConfirmedCaseExposureLocations) {
		this.patientContactWithConfirmedCaseExposureLocations = patientContactWithConfirmedCaseExposureLocations;

		if (this.patientContactWithConfirmedCaseExposureLocations == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		for (ContactSetting contactSetting : patientContactWithConfirmedCaseExposureLocations) {
			sb.append(contactSetting.name());
			sb.append(",");
		}
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		patientContactWithConfirmedCaseExposureLocationsString = sb.toString();
	}

	@Transient
	public Set<ContactSetting> getPatientContactWithConfirmedCaseExposureLocations() {
		if (patientContactWithConfirmedCaseExposureLocations == null) {
			patientContactWithConfirmedCaseExposureLocations = new HashSet<>();
			if (!StringUtils.isEmpty(patientContactWithConfirmedCaseExposureLocationsString)) {
				String[] contactSettingsTypes = patientContactWithConfirmedCaseExposureLocationsString.split(",");
				for (String contactSettingsType : contactSettingsTypes) {
					patientContactWithConfirmedCaseExposureLocations.add(ContactSetting.valueOf(contactSettingsType));
				}
			}
		}
		return patientContactWithConfirmedCaseExposureLocations;
	}
	public YesNo getPatientTravelDuringIllness() {
		return patientTravelDuringIllness;
	}

	public void setPatientTravelDuringIllness(YesNo patientTravelDuringIllness) {
		this.patientTravelDuringIllness = patientTravelDuringIllness;
	}

	public String getComm1() {
		return comm1;
	}

	public void setComm1(String comm1) {
		this.comm1 = comm1;
	}

	public String getHealthCenter1() {
		return healthCenter1;
	}

	public void setHealthCenter1(String healthCenter1) {
		this.healthCenter1 = healthCenter1;
	}

	public String getCountry1() {
		return country1;
	}

	public void setCountry1(String country1) {
		this.country1 = country1;
	}

	public String getComm2() {
		return comm2;
	}

	public void setComm2(String comm2) {
		this.comm2 = comm2;
	}

	public String getHealthCenter2() {
		return healthCenter2;
	}

	public void setHealthCenter2(String healthCenter2) {
		this.healthCenter2 = healthCenter2;
	}

	public String getCountry2() {
		return country2;
	}

	public void setCountry2(String country2) {
		this.country2 = country2;
	}

	public YesNo getWasPatientHospitalized() {
		return wasPatientHospitalized;
	}

	public void setWasPatientHospitalized(YesNo wasPatientHospitalized) {
		this.wasPatientHospitalized = wasPatientHospitalized;
	}

	public String getIfYesWhere() {
		return ifYesWhere;
	}

	public void setIfYesWhere(String ifYesWhere) {
		this.ifYesWhere = ifYesWhere;
	}

	public Date getHospitalizedDate1() {
		return hospitalizedDate1;
	}

	public void setHospitalizedDate1(Date hospitalizedDate1) {
		this.hospitalizedDate1 = hospitalizedDate1;
	}

	public Date getHospitalizedDate2() {
		return hospitalizedDate2;
	}

	public void setHospitalizedDate2(Date hospitalizedDate2) {
		this.hospitalizedDate2 = hospitalizedDate2;
	}

	public YesNo getDidPatientConsultHealer() {
		return didPatientConsultHealer;
	}

	public void setDidPatientConsultHealer(YesNo didPatientConsultHealer) {
		this.didPatientConsultHealer = didPatientConsultHealer;
	}

	public String getIfYesNameHealer() {
		return ifYesNameHealer;
	}

	public void setIfYesNameHealer(String ifYesNameHealer) {
		this.ifYesNameHealer = ifYesNameHealer;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getWhenWhereContactTakePlace() {
		return whenWhereContactTakePlace;
	}

	public void setWhenWhereContactTakePlace(String whenWhereContactTakePlace) {
		this.whenWhereContactTakePlace = whenWhereContactTakePlace;
	}

	public Date getDateOfContact() {
		return dateOfContact;
	}

	public void setDateOfContact(Date dateOfContact) {
		this.dateOfContact = dateOfContact;
	}

	public YesNo getPatientReceiveTraditionalMedicine() {
		return patientReceiveTraditionalMedicine;
	}

	public void setPatientReceiveTraditionalMedicine(YesNo patientReceiveTraditionalMedicine) {
		this.patientReceiveTraditionalMedicine = patientReceiveTraditionalMedicine;
	}

	public String getIfYesExplain() {
		return ifYesExplain;
	}

	public void setIfYesExplain(String ifYesExplain) {
		this.ifYesExplain = ifYesExplain;
	}

	public YesNo getPatientAttendFuneralCeremonies() {
		return patientAttendFuneralCeremonies;
	}

	public void setPatientAttendFuneralCeremonies(YesNo patientAttendFuneralCeremonies) {
		this.patientAttendFuneralCeremonies = patientAttendFuneralCeremonies;
	}

	public YesNo getPatientTravelAnytimePeriodBeforeIll() {
		return patientTravelAnytimePeriodBeforeIll;
	}

	public void setPatientTravelAnytimePeriodBeforeIll(YesNo patientTravelAnytimePeriodBeforeIll) {
		this.patientTravelAnytimePeriodBeforeIll = patientTravelAnytimePeriodBeforeIll;
	}

	public String getIfTravelYesWhere() {
		return ifTravelYesWhere;
	}

	public void setIfTravelYesWhere(String ifTravelYesWhere) {
		this.ifTravelYesWhere = ifTravelYesWhere;
	}

	public Date getIfYesStartDate() {
		return ifYesStartDate;
	}

	public void setIfYesStartDate(Date ifYesStartDate) {
		this.ifYesStartDate = ifYesStartDate;
	}

	public Date getIfYesEndDate() {
		return ifYesEndDate;
	}

	public void setIfYesEndDate(Date ifYesEndDate) {
		this.ifYesEndDate = ifYesEndDate;
	}
	public YesNo getPatientContactKnownSuspect() {
		return patientContactKnownSuspect;
	}

	public void setPatientContactKnownSuspect(YesNo patientContactKnownSuspect) {
		this.patientContactKnownSuspect = patientContactKnownSuspect;
	}

	public String getSuspectName() {
		return suspectName;
	}

	public void setSuspectName(String suspectName) {
		this.suspectName = suspectName;
	}

	public String getSuspectLastName() {
		return suspectLastName;
	}

	public void setSuspectLastName(String suspectLastName) {
		this.suspectLastName = suspectLastName;
	}

	public String getIdCase() {
		return idCase;
	}

	public void setIdCase(String idCase) {
		this.idCase = idCase;
	}

	public CaseOutcome getDuringContactSuspectCase() {
		return duringContactSuspectCase;
	}

	public void setDuringContactSuspectCase(CaseOutcome duringContactSuspectCase) {
		this.duringContactSuspectCase = duringContactSuspectCase;
	}

	public Date getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(Date dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

	public Date getDateOfLastContactWithSuspectCase() {
		return dateOfLastContactWithSuspectCase;
	}

	public void setDateOfLastContactWithSuspectCase(Date dateOfLastContactWithSuspectCase) {
		this.dateOfLastContactWithSuspectCase = dateOfLastContactWithSuspectCase;
	}
	public YesNo getPreviouslyVaccinatedAgainstInfluenza() {
		return previouslyVaccinatedAgainstInfluenza;
	}

	public void setPreviouslyVaccinatedAgainstInfluenza(YesNo previouslyVaccinatedAgainstInfluenza) {
		this.previouslyVaccinatedAgainstInfluenza = previouslyVaccinatedAgainstInfluenza;
	}
	public String getNameOfVaccine() {
		return nameOfVaccine;
	}
	public void setNameOfVaccine(String nameOfVaccine) {
		this.nameOfVaccine = nameOfVaccine;
	}
	public Integer getYearOfVaccination() {
		return yearOfVaccination;
	}

	public void setYearOfVaccination(Integer yearOfVaccination) {
		this.yearOfVaccination = yearOfVaccination;
	}
	public YesNo getPreviouslyVaccinatedAgainstCovid() {
		return previouslyVaccinatedAgainstCovid;
	}

	public void setPreviouslyVaccinatedAgainstCovid(YesNo previouslyVaccinatedAgainstCovid) {
		this.previouslyVaccinatedAgainstCovid = previouslyVaccinatedAgainstCovid;
	}
	public String getNameOfVaccineCovid() {
		return nameOfVaccineCovid;
	}

	public void setNameOfVaccineCovid(String nameOfVaccineCovid) {
		this.nameOfVaccineCovid = nameOfVaccineCovid;
	}
	public Integer getYearOfVaccinationCovid() {
		return yearOfVaccinationCovid;
	}

	public void setYearOfVaccinationCovid(Integer yearOfVaccinationCovid) {
		this.yearOfVaccinationCovid = yearOfVaccinationCovid;
	}
	public YesNo getVistedPlacesConfirmedPandemic() {
		return vistedPlacesConfirmedPandemic;
	}

	public void setVistedPlacesConfirmedPandemic(YesNo vistedPlacesConfirmedPandemic) {
		this.vistedPlacesConfirmedPandemic = vistedPlacesConfirmedPandemic;
	}
	public String getPlacesVisitedPastSevenDays() {
		return placesVisitedPastSevenDays;
	}

	public void setPlacesVisitedPastSevenDays(String placesVisitedPastSevenDays) {
		this.placesVisitedPastSevenDays = placesVisitedPastSevenDays;
	}
	public RiskFactorCondition getRiskFactorsSevereDisease() {
		return riskFactorsSevereDisease;
	}

	public void setRiskFactorsSevereDisease(RiskFactorCondition riskFactorsSevereDisease) {
		this.riskFactorsSevereDisease = riskFactorsSevereDisease;
	}

	public String getOtherSpecify() {
		return otherSpecify;
	}

	public void setOtherSpecify(String otherSpecify) {
		this.otherSpecify = otherSpecify;
	}





	@Override
	public String getI18nPrefix() {
		return I18N_PREFIX;
	}



}
