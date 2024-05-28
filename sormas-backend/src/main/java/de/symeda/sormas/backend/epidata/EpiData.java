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
package de.symeda.sormas.backend.epidata;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;

//import de.symeda.auditlog.api.Audited;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.epidata.ContactSetting;
import de.symeda.sormas.api.epidata.PlaceManaged;
import de.symeda.sormas.api.riskfactor.DrinkingWaterSource;
import de.symeda.sormas.api.utils.RiskFactorInfluenza;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.api.caze.CaseOutcome;
import de.symeda.sormas.api.utils.*;
import de.symeda.sormas.backend.activityascase.ActivityAsCase;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.common.NotExposedToApi;
import de.symeda.sormas.backend.containmentmeasure.ContainmentMeasure;
import de.symeda.sormas.backend.contaminationsource.ContaminationSource;
import de.symeda.sormas.backend.exposure.Exposure;
import de.symeda.sormas.backend.persontravelhistory.PersonTravelHistory;
import org.apache.commons.lang3.StringUtils;
import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.infrastructure.district.District;
import de.symeda.sormas.backend.infrastructure.region.Region;

@Entity
public class EpiData extends AbstractDomainObject {

	private static final long serialVersionUID = -8294812479501735785L;

	public static final String TABLE_NAME = "epidata";

	public static final String CONTACT_WITH_SOURCE_CASE_KNOWN = "contactWithSourceCaseKnown";
	public static final String EXPOSURES = "exposures";
	public static final String ACTIVITIES_AS_CASE = "activitiesAsCase";

	private YesNo exposureDetailsKnown;
	private YesNo activityAsCaseDetailsKnown;
	private YesNo recentTravelOutbreak;
	private YesNo contactSimilarOutbreak;
	private YesNo contactSickAnimals;
	private YesNo contactWithSourceCaseKnown;
	private YesNo highTransmissionRiskArea;
	private YesNo largeOutbreaksArea;
	private YesNo areaInfectedAnimals;
	private Disease disease;
	private YesNo previouslyVaccinatedAgainstInfluenza;
	private Date yearOfVaccination;
	private String placesVisitedPastSevenDays;
	private YesNo vistedPlacesConfirmedPandemic;
	private RiskFactorInfluenza riskFactorsSevereDisease;
	private String otherSpecify;
	private YesNo intlTravel;
	private String specifyCountries;
	private Date dateOfDeparture;
	private Date dateOfArrival;
	private YesNo domesticTravel;
	private String specifyLocation;
	private Date dateOfDeparture2;
	private Date dateOfArrival2;
	private YesNo contactIllPerson;
	private Date contactDate;
	private String specifyIllness;
	private String nameOfAffectedPerson;
	private String nameOfAffectedPerson2;
	private String nameOfAffectedPerson3;
	private String nameOfAffectedPerson4;
	private String telNo;
	private String telNo2;
	private String telNo3;
	private String telNo4;
	private Date dateTime;
	private Date dateTime2;
	private Date dateTime3;
	private Date dateTime4;
	private String age;
	private String age2;
	private String age3;
	private String age4;
	private String suspectedFood;
	private Date dateConsumed;
	private FoodSource foodSource;
	private EventType eventType;
	private String eventOtherSpecify;
	private YesNo breakfast;
	private String totalNoPersons;
	private String foodConsumed;
	private String sourceOfFood;
	private YesNo consumedAtPlace;
	private YesNo lunch;
	private String totalNoPersonsL1;
	private String foodConsumedL1;
	private String sourceOfFoodL1;
	private YesNo consumedAtPlaceL1;
	private YesNo supper;
	private String totalNoPersonsS1;
	private String foodConsumedS1;
	private String sourceOfFoodsS1;
	private YesNo consumedAtPlaceS1;
	private YesNo breakfast2;
	private String totalNoPersons2;
	private String foodConsumed2;
	private String sourceOfFood2;
	private YesNo consumedAtPlace2;
	private YesNo lunchL2;
	private String totalNoPersonsL2;
	private String foodConsumedL2;
	private String sourceOfFoodL2;
	private YesNo consumedAtPlaceL2;
	private YesNo supperS2;
	private String totalNoPersonsS2;
	private String foodConsumedS2;
	private String sourceOfFoodS2;
	private YesNo consumedAtPlaceS2;
	private YesNo breakfast3;
	private String totalNoPersons3;
	private String foodConsumed3;
	private String sourceOfFood3;
	private YesNo consumedAtPlace3;
	private YesNo lunchL3;
	private String totalNoPersonsL3;
	private String foodConsumedL3;
	private String sourceOfFoodL3;
	private YesNo consumedAtPlaceL3;
	private YesNo supperS3;
	private String totalNoPersonsS3;
	private String foodConsumedS3;
	private String sourceOfFoodS3;
	private YesNo consumedAtPlaceS3;
	private String nameOfVaccine;
	private YesNo previouslyVaccinatedAgainstCovid;
	private Date yearOfVaccinationCovid;
	private String nameOfVaccineCovid;
	private String ifYesSpecifySick;
	private YesNo contactDeadAnimals;
	private String ifYesSpecifyDead;
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
	private String idCase;
	private CaseOutcome duringContactSuspectCase;
	private Date dateOfDeath;
	private Date dateOfLastContactWithSuspectCase;
	private String ifYesWildAnimalLocation;
	private Date ifYesWildAnimalDate;

	private List<Exposure> exposures = new ArrayList<>();
	private List<ActivityAsCase> activitiesAsCase = new ArrayList<>();
	private List<PersonTravelHistory> personTravelHistories = new ArrayList<>();
	private List<ContaminationSource> contaminationSources = new ArrayList<>();
	private List<ContainmentMeasure> containmentMeasures = new ArrayList<>();

	@NotExposedToApi
	private Date changeDateOfEmbeddedLists;
	private YesNoUnknown childComeInContactWithSymptoms;
	private YesNoUnknown patientTravelledTwoWeeksPrior;
	private String patientTravelledInCountryOne;
	private String patientTravelledInCountryTwo;
	private String patientTravelledInCountryThree;
	private String patientTravelledInCountryFour;
	private String patientTravelledInternationalOne;
	private String patientTravelledInternationalTwo;
	private String patientTravelledInternationalThree;
	private String patientTravelledInternationalFour;
	private YesNoUnknown patientVisitedHealthCareFacility;
	private YesNoUnknown patientCloseContactWithARI;
	private Set<ContactSetting>  patientCloseContactWithARIContactSettings;
	private String patientCloseContactWithARIContactSettingsString;
	private YesNoUnknown patientContactWithConfirmedCase;
	private Set<ContactSetting> patientContactWithConfirmedCaseExposureLocations;
	private String patientContactWithConfirmedCaseExposureLocationsString;
	private String patientContactWithConfirmedCaseExposureLocationCityCountry;
	private YesNo exposedToRiskFactor;
	private DrinkingWaterSource waterUsedByPatientAfterExposure;

	private YesNo receivedHealthEducation;
	private YesNo patientEnteredWaterSource;
	private PlaceManaged placeManaged;
	private YesNo vibrioCholeraeIdentifiedInStools;
	private String drugsSensitiveToVibrioStrain;
	private String drugsResistantToVibrioStrain;


	private YesNo historyOfTravelOutsideTheVillageTownDistrict;
	private String historyOfTravelOutsideTheVillageTownDistrictDetails;
	private Region historyOfTravelRegion;
	private District historyOfTravelDistrict;
	private Community historyOfTravelSubDistrict;
	private String historyOfTravelVillage;


	@Enumerated(EnumType.STRING)
	public YesNo getExposureDetailsKnown() {
		return exposureDetailsKnown;
	}

	public void setExposureDetailsKnown(YesNo exposureDetailsKnown) {
		this.exposureDetailsKnown = exposureDetailsKnown;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getActivityAsCaseDetailsKnown() {
		return activityAsCaseDetailsKnown;
	}

	public void setActivityAsCaseDetailsKnown(YesNo activityAsCaseDetailsKnown) {
		this.activityAsCaseDetailsKnown = activityAsCaseDetailsKnown;
	}
	@Enumerated(EnumType.STRING)
	public YesNo getRecentTravelOutbreak() {
		return recentTravelOutbreak;
	}

	public void setRecentTravelOutbreak(YesNo recentTravelOutbreak) {
		this.recentTravelOutbreak = recentTravelOutbreak;
	}
	@Enumerated(EnumType.STRING)
	public YesNo getContactSimilarOutbreak() {
		return contactSimilarOutbreak;
	}

	public void setContactSimilarOutbreak(YesNo contactSimilarOutbreak) {
		this.contactSimilarOutbreak = contactSimilarOutbreak;
	}
	@Enumerated(EnumType.STRING)
	public YesNo getContactSickAnimals() {
		return contactSickAnimals;
	}

	public void setContactSickAnimals(YesNo contactSickAnimals) {
		this.contactSickAnimals = contactSickAnimals;
	}
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = Exposure.EPI_DATA)
	public List<Exposure> getExposures() {
		return exposures;
	}

	public void setExposures(List<Exposure> exposures) {
		this.exposures = exposures;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = Exposure.EPI_DATA)
	public List<ActivityAsCase> getActivitiesAsCase() {
		return activitiesAsCase;
	}

	public void setActivitiesAsCase(List<ActivityAsCase> activitiesAsCase) {
		this.activitiesAsCase = activitiesAsCase;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = PersonTravelHistory.EPI_DATA)
	public List<PersonTravelHistory> getPersonTravelHistories() {
		return personTravelHistories;
	}

	public void setPersonTravelHistories(List<PersonTravelHistory> personTravelHistories) {
		this.personTravelHistories = personTravelHistories;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = ContaminationSource.EPI_DATA)
	public List<ContaminationSource> getContaminationSources() {
		return contaminationSources;
	}

	public void setContaminationSources(List<ContaminationSource> contaminationSources) {
		this.contaminationSources = contaminationSources;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = ContainmentMeasure.EPI_DATA)
	public List<ContainmentMeasure> getContainmentMeasures() {
		return containmentMeasures;
	}

	public void setContainmentMeasures(List<ContainmentMeasure> containmentMeasures) {
		this.containmentMeasures = containmentMeasures;
	}

	/**
	 * This change date has to be set whenever exposures are modified
	 */
	public Date getChangeDateOfEmbeddedLists() {
		return changeDateOfEmbeddedLists;
	}

	public void setChangeDateOfEmbeddedLists(Date changeDateOfEmbeddedLists) {
		this.changeDateOfEmbeddedLists = changeDateOfEmbeddedLists;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getAreaInfectedAnimals() {
		return areaInfectedAnimals;
	}

	public void setAreaInfectedAnimals(YesNo areaInfectedAnimals) {
		this.areaInfectedAnimals = areaInfectedAnimals;
	}
	public Disease getDisease() {
		return disease;
	}
	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getHighTransmissionRiskArea() {
		return highTransmissionRiskArea;
	}

	public void setHighTransmissionRiskArea(YesNo highTransmissionRiskArea) {
		this.highTransmissionRiskArea = highTransmissionRiskArea;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getLargeOutbreaksArea() {
		return largeOutbreaksArea;
	}

	public void setLargeOutbreaksArea(YesNo largeOutbreaksArea) {
		this.largeOutbreaksArea = largeOutbreaksArea;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getContactWithSourceCaseKnown() {
		return contactWithSourceCaseKnown;
	}

	public void setContactWithSourceCaseKnown(YesNo contactWithSourceCaseKnown) {
		this.contactWithSourceCaseKnown = contactWithSourceCaseKnown;
	}

	public YesNoUnknown getChildComeInContactWithSymptoms() {
		return childComeInContactWithSymptoms;
	}

	public void setChildComeInContactWithSymptoms(YesNoUnknown childComeInContactWithSymptoms) {
		this.childComeInContactWithSymptoms = childComeInContactWithSymptoms;
	}
	
	@Enumerated(EnumType.STRING)
	public YesNo getPreviouslyVaccinatedAgainstInfluenza() {
		return previouslyVaccinatedAgainstInfluenza;
	}

	public void setPreviouslyVaccinatedAgainstInfluenza(YesNo previouslyVaccinatedAgainstInfluenza) {
		this.previouslyVaccinatedAgainstInfluenza = previouslyVaccinatedAgainstInfluenza;
	}

	public Date getYearOfVaccination() {
		return yearOfVaccination;
	}

	public void setYearOfVaccination(Date yearOfVaccination) {
		this.yearOfVaccination = yearOfVaccination;
	}

	public String getPlacesVisitedPastSevenDays() {
		return placesVisitedPastSevenDays;
	}

	public void setPlacesVisitedPastSevenDays(String placesVisitedPastSevenDays) {
		this.placesVisitedPastSevenDays = placesVisitedPastSevenDays;
	}
	@Enumerated(EnumType.STRING)
	public YesNo getVistedPlacesConfirmedPandemic() {
		return vistedPlacesConfirmedPandemic;
	}

	public void setVistedPlacesConfirmedPandemic(YesNo vistedPlacesConfirmedPandemic) {
		this.vistedPlacesConfirmedPandemic = vistedPlacesConfirmedPandemic;
	}

	public RiskFactorInfluenza getRiskFactorsSevereDisease() {
		return riskFactorsSevereDisease;
	}

	public void setRiskFactorsSevereDisease(RiskFactorInfluenza riskFactorsSevereDisease) {
		this.riskFactorsSevereDisease = riskFactorsSevereDisease;
	}

	public String getOtherSpecify() {
		return otherSpecify;
	}

	public void setOtherSpecify(String otherSpecify) {
		this.otherSpecify = otherSpecify;
	}

	@Enumerated(EnumType.STRING)
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

	public void setPatientTravelledInternationalOne(String PatientTravelledInternationalOne) {
		this.patientTravelledInternationalOne = PatientTravelledInternationalOne;
	}

	public String getPatientTravelledInternationalTwo() {
		return patientTravelledInternationalTwo;
	}

	public void setPatientTravelledInternationalTwo(String PatientTravelledInternationalTwo) {
		this.patientTravelledInternationalTwo = PatientTravelledInternationalTwo;
	}

	public String getPatientTravelledInternationalThree() {
		return patientTravelledInternationalThree;
	}

	public void setPatientTravelledInternationalThree(String PatientTravelledInternationalThree) {
		this.patientTravelledInternationalThree = PatientTravelledInternationalThree;
	}

	public String getPatientTravelledInternationalFour() {
		return patientTravelledInternationalFour;
	}

	public void setPatientTravelledInternationalFour(String PatientTravelledInternationalFour) {
		this.patientTravelledInternationalFour = PatientTravelledInternationalFour;
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
	@Transient
	public Set<ContactSetting> getPatientCloseContactWithARIContactSettings() {
		if (patientCloseContactWithARIContactSettings == null) {
			if (StringUtils.isEmpty(patientCloseContactWithARIContactSettingsString)) {
				patientCloseContactWithARIContactSettings = new HashSet<>();

			} else {
				patientCloseContactWithARIContactSettings = Arrays.stream(patientCloseContactWithARIContactSettingsString.split(","))
						.map(ContactSetting::valueOf).collect(Collectors.toSet());
			}
		}

		return patientCloseContactWithARIContactSettings;
	}

	public void setPatientCloseContactWithARIContactSettings(Set<ContactSetting> patientCloseContactWithARIContactSettings) {
		this.patientCloseContactWithARIContactSettings = patientCloseContactWithARIContactSettings;

		if (this.patientCloseContactWithARIContactSettings == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		patientCloseContactWithARIContactSettings.forEach(contactSetting -> {
			sb.append(contactSetting.name());
			sb.append(",");
		});

		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}

		patientCloseContactWithARIContactSettingsString = sb.toString();
	}

	public String getPatientCloseContactWithARIContactSettingsString() {
		return patientCloseContactWithARIContactSettingsString;
	}

	public void setPatientCloseContactWithARIContactSettingsString(String patientCloseContactWithARIContactSettingsString) {
		this.patientCloseContactWithARIContactSettingsString = patientCloseContactWithARIContactSettingsString;
	}

	public YesNoUnknown getPatientContactWithConfirmedCase() {
		return patientContactWithConfirmedCase;
	}

	public void setPatientContactWithConfirmedCase(YesNoUnknown patientContactWithConfirmedCase) {
		this.patientContactWithConfirmedCase = patientContactWithConfirmedCase;
	}
	@Transient
	public Set<ContactSetting> getPatientContactWithConfirmedCaseExposureLocations() {
		if (patientContactWithConfirmedCaseExposureLocations == null) {
			if(StringUtils.isEmpty(patientContactWithConfirmedCaseExposureLocationsString)) {
				patientContactWithConfirmedCaseExposureLocations = new HashSet<>();

			} else {
				patientContactWithConfirmedCaseExposureLocations = Arrays.stream(patientContactWithConfirmedCaseExposureLocationsString.split(","))
						.map(ContactSetting::valueOf)
						.collect(Collectors.toSet());
			}
		}

		return patientContactWithConfirmedCaseExposureLocations;
	}

	public void setPatientContactWithConfirmedCaseExposureLocations(Set<ContactSetting> patientContactWithConfirmedCaseExposureLocations) {
		this.patientContactWithConfirmedCaseExposureLocations = patientContactWithConfirmedCaseExposureLocations;

		if (this.patientContactWithConfirmedCaseExposureLocations == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		patientContactWithConfirmedCaseExposureLocations.forEach(contactSetting -> {
			sb.append(contactSetting.name());
			sb.append(",");
		});

		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}

		patientContactWithConfirmedCaseExposureLocationsString = sb.toString();

	}

	public String getPatientContactWithConfirmedCaseExposureLocationsString() {
		return patientContactWithConfirmedCaseExposureLocationsString;
	}

	public void setPatientContactWithConfirmedCaseExposureLocationsString(String patientContactWithConfirmedCaseExposureLocationsString) {
		this.patientContactWithConfirmedCaseExposureLocationsString = patientContactWithConfirmedCaseExposureLocationsString;
	}

	public String getPatientContactWithConfirmedCaseExposureLocationCityCountry() {
		return patientContactWithConfirmedCaseExposureLocationCityCountry;
	}

	public void setPatientContactWithConfirmedCaseExposureLocationCityCountry(String patientContactWithConfirmedCaseExposureLocationCityCountry) {
		this.patientContactWithConfirmedCaseExposureLocationCityCountry = patientContactWithConfirmedCaseExposureLocationCityCountry;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getExposedToRiskFactor() {
		return exposedToRiskFactor;
	}

	public void setExposedToRiskFactor(YesNo exposedToRiskFactor) {
		this.exposedToRiskFactor = exposedToRiskFactor;
	}

	@Enumerated(EnumType.STRING)
	public DrinkingWaterSource getWaterUsedByPatientAfterExposure() {
		return waterUsedByPatientAfterExposure;
	}

	public void setWaterUsedByPatientAfterExposure(DrinkingWaterSource waterUsedByPatientAfterExposure) {
		this.waterUsedByPatientAfterExposure = waterUsedByPatientAfterExposure;
	}
	public YesNo getIntlTravel() {
		return intlTravel;
	}

	public void setIntlTravel(YesNo intlTravel) {
		this.intlTravel = intlTravel;
	}

	public String getSpecifyCountries() {
		return specifyCountries;
	}

	public void setSpecifyCountries(String specifyCountries) {
		this.specifyCountries = specifyCountries;
	}

	public Date getDateOfDeparture() {
		return dateOfDeparture;
	}

	public void setDateOfDeparture(Date dateOfDeparture) {
		this.dateOfDeparture = dateOfDeparture;
	}

	public Date getDateOfArrival() {
		return dateOfArrival;
	}

	public void setDateOfArrival(Date dateOfArrival) {
		this.dateOfArrival = dateOfArrival;
	}

	public YesNo getDomesticTravel() {
		return domesticTravel;
	}

	public void setDomesticTravel(YesNo domesticTravel) {
		this.domesticTravel = domesticTravel;
	}

	public String getSpecifyLocation() {
		return specifyLocation;
	}

	public void setSpecifyLocation(String specifyLocation) {
		this.specifyLocation = specifyLocation;
	}

	public Date getDateOfDeparture2() {
		return dateOfDeparture2;
	}

	public void setDateOfDeparture2(Date dateOfDeparture2) {
		this.dateOfDeparture2 = dateOfDeparture2;
	}

	public Date getDateOfArrival2() {
		return dateOfArrival2;
	}

	public void setDateOfArrival2(Date dateOfArrival2) {
		this.dateOfArrival2 = dateOfArrival2;
	}

	public YesNo getContactIllPerson() {
		return contactIllPerson;
	}

	public void setContactIllPerson(YesNo contactIllPerson) {
		this.contactIllPerson = contactIllPerson;
	}

	public Date getContactDate() {
		return contactDate;
	}

	public void setContactDate(Date contactDate) {
		this.contactDate = contactDate;
	}

	public String getSpecifyIllness() {
		return specifyIllness;
	}

	public void setSpecifyIllness(String specifyIllness) {
		this.specifyIllness = specifyIllness;
	}

	public String getNameOfAffectedPerson() {
		return nameOfAffectedPerson;
	}

	public void setNameOfAffectedPerson(String nameOfAffectedPerson) {
		this.nameOfAffectedPerson = nameOfAffectedPerson;
	}

	public String getNameOfAffectedPerson2() {
		return nameOfAffectedPerson2;
	}

	public void setNameOfAffectedPerson2(String nameOfAffectedPerson2) {
		this.nameOfAffectedPerson2 = nameOfAffectedPerson2;
	}
	public String getNameOfAffectedPerson3() {
		return nameOfAffectedPerson3;
	}

	public void setNameOfAffectedPerson3(String nameOfAffectedPerson3) {
		this.nameOfAffectedPerson3 = nameOfAffectedPerson3;
	}
	public String getNameOfAffectedPerson4() {
		return nameOfAffectedPerson4;
	}

	public void setNameOfAffectedPerson4(String nameOfAffectedPerson4) {
		this.nameOfAffectedPerson4 = nameOfAffectedPerson4;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getTelNo2() {
		return telNo2;
	}

	public void setTelNo2(String telNo2) {
		this.telNo2 = telNo2;
	}
	public String getTelNo3() {
		return telNo3;
	}

	public void setTelNo3(String telNo3) {
		this.telNo3 = telNo3;
	}
	public String getTelNo4() {
		return telNo4;
	}

	public void setTelNo4(String telNo4) {
		this.telNo4 = telNo4;
	}
	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public Date getDateTime2() {
		return dateTime2;
	}

	public void setDateTime2(Date dateTime2) {
		this.dateTime2 = dateTime2;
	}

	public Date getDateTime3() {
		return dateTime3;
	}

	public void setDateTime3(Date dateTime3) {
		this.dateTime3 = dateTime3;
	}

	public Date getDateTime4() {
		return dateTime4;
	}

	public void setDateTime4(Date dateTime4) {
		this.dateTime4 = dateTime4;
	}
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	public String getAge2() {
		return age2;
	}

	public void setAge2(String age2) {
		this.age2 = age;
	}
	public String getAge3() {
		return age3;
	}

	public void setAge3(String age3) {
		this.age3 = age3;
	}
	public String getAge4() {
		return age4;
	}

	public void setAge4(String age4) {
		this.age4 = age4;
	}

	public String getSuspectedFood() {
		return suspectedFood;
	}

	public void setSuspectedFood(String suspectedFood) {
		this.suspectedFood = suspectedFood;
	}

	public Date getDateConsumed() {
		return dateConsumed;
	}

	public void setDateConsumed(Date dateConsumed) {
		this.dateConsumed = dateConsumed;
	}

	public FoodSource getFoodSource() {
		return foodSource;
	}

	public void setFoodSource(FoodSource foodSource) {
		this.foodSource = foodSource;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public String getEventOtherSpecify() {
		return eventOtherSpecify;
	}

	public void setEventOtherSpecify(String eventOtherSpecify) {
		this.eventOtherSpecify = eventOtherSpecify;
	}

	public YesNo getBreakfast() {
		return breakfast;
	}

	public void setBreakfast(YesNo breakfast) {
		this.breakfast = breakfast;
	}

	public String getTotalNoPersons() {
		return totalNoPersons;
	}

	public void setTotalNoPersons(String totalNoPersons) {
		this.totalNoPersons = totalNoPersons;
	}

	public String getFoodConsumed() {
		return foodConsumed;
	}

	public void setFoodConsumed(String foodConsumed) {
		this.foodConsumed = foodConsumed;
	}

	public String getSourceOfFood() {
		return sourceOfFood;
	}

	public void setSourceOfFood(String sourceOfFood) {
		this.sourceOfFood = sourceOfFood;
	}

	public YesNo getConsumedAtPlace() {
		return consumedAtPlace;
	}

	public void setConsumedAtPlace(YesNo consumedAtPlace) {
		this.consumedAtPlace = consumedAtPlace;
	}

	public YesNo getLunch() {
		return lunch;
	}

	public void setLunch(YesNo lunch) {
		this.lunch = lunch;
	}

	public String getTotalNoPersonsL1() {
		return totalNoPersonsL1;
	}

	public void setTotalNoPersonsL1(String totalNoPersonsL1) {
		this.totalNoPersonsL1 = totalNoPersonsL1;
	}

	public String getFoodConsumedL1() {
		return foodConsumedL1;
	}

	public void setFoodConsumedL1(String foodConsumedL1) {
		this.foodConsumedL1 = foodConsumedL1;
	}

	public String getSourceOfFoodL1() {
		return sourceOfFoodL1;
	}

	public void setSourceOfFoodL1(String sourceOfFoodL1) {
		this.sourceOfFoodL1 = sourceOfFoodL1;
	}

	public YesNo getConsumedAtPlaceL1() {
		return consumedAtPlaceL1;
	}

	public void setConsumedAtPlaceL1(YesNo consumedAtPlaceL1) {
		this.consumedAtPlaceL1 = consumedAtPlaceL1;
	}

	public YesNo getSupper() {
		return supper;
	}

	public void setSupper(YesNo supper) {
		this.supper = supper;
	}

	public String getTotalNoPersonsS1() {
		return totalNoPersonsS1;
	}

	public void setTotalNoPersonsS1(String totalNoPersonsS1) {
		this.totalNoPersonsS1 = totalNoPersonsS1;
	}

	public String getFoodConsumedS1() {
		return foodConsumedS1;
	}

	public void setFoodConsumedS1(String foodConsumedS1) {
		this.foodConsumedS1 = foodConsumedS1;
	}

	public String getSourceOfFoodsS1() {
		return sourceOfFoodsS1;
	}

	public void setSourceOfFoodsS1(String sourceOfFoodsS1) {
		this.sourceOfFoodsS1 = sourceOfFoodsS1;
	}

	public YesNo getConsumedAtPlaceS1() {
		return consumedAtPlaceS1;
	}

	public void setConsumedAtPlaceS1(YesNo consumedAtPlaceS1) {
		this.consumedAtPlaceS1 = consumedAtPlaceS1;
	}

	public YesNo getBreakfast2() {
		return breakfast2;
	}

	public void setBreakfast2(YesNo breakfast2) {
		this.breakfast2 = breakfast2;
	}

	public String getTotalNoPersons2() {
		return totalNoPersons2;
	}

	public void setTotalNoPersons2(String totalNoPersons2) {
		this.totalNoPersons2 = totalNoPersons2;
	}

	public String getFoodConsumed2() {
		return foodConsumed2;
	}

	public void setFoodConsumed2(String foodConsumed2) {
		this.foodConsumed2 = foodConsumed2;
	}

	public String getSourceOfFood2() {
		return sourceOfFood2;
	}

	public void setSourceOfFood2(String sourceOfFood2) {
		this.sourceOfFood2 = sourceOfFood2;
	}

	public YesNo getConsumedAtPlace2() {
		return consumedAtPlace2;
	}

	public void setConsumedAtPlace2(YesNo consumedAtPlace2) {
		this.consumedAtPlace2 = consumedAtPlace2;
	}

	public YesNo getLunchL2() {
		return lunchL2;
	}

	public void setLunchL2(YesNo lunchL2) {
		this.lunchL2 = lunchL2;
	}

	public String getTotalNoPersonsL2() {
		return totalNoPersonsL2;
	}

	public void setTotalNoPersonsL2(String totalNoPersonsL2) {
		this.totalNoPersonsL2 = totalNoPersonsL2;
	}

	public String getFoodConsumedL2() {
		return foodConsumedL2;
	}

	public void setFoodConsumedL2(String foodConsumedL2) {
		this.foodConsumedL2 = foodConsumedL2;
	}

	public String getSourceOfFoodL2() {
		return sourceOfFoodL2;
	}

	public void setSourceOfFoodL2(String sourceOfFoodL2) {
		this.sourceOfFoodL2 = sourceOfFoodL2;
	}

	public YesNo getConsumedAtPlaceL2() {
		return consumedAtPlaceL2;
	}

	public void setConsumedAtPlaceL2(YesNo consumedAtPlaceL2) {
		this.consumedAtPlaceL2 = consumedAtPlaceL2;
	}

	public YesNo getSupperS2() {
		return supperS2;
	}

	public void setSupperS2(YesNo supperS2) {
		this.supperS2 = supperS2;
	}

	public String getTotalNoPersonsS2() {
		return totalNoPersonsS2;
	}

	public void setTotalNoPersonsS2(String totalNoPersonsS2) {
		this.totalNoPersonsS2 = totalNoPersonsS2;
	}

	public String getFoodConsumedS2() {
		return foodConsumedS2;
	}

	public void setFoodConsumedS2(String foodConsumedS2) {
		this.foodConsumedS2 = foodConsumedS2;
	}

	public String getSourceOfFoodS2() {
		return sourceOfFoodS2;
	}

	public void setSourceOfFoodS2(String sourceOfFoodS2) {
		this.sourceOfFoodS2 = sourceOfFoodS2;
	}

	public YesNo getConsumedAtPlaceS2() {
		return consumedAtPlaceS2;
	}

	public void setConsumedAtPlaceS2(YesNo consumedAtPlaceS2) {
		this.consumedAtPlaceS2 = consumedAtPlaceS2;
	}

	public YesNo getBreakfast3() {
		return breakfast3;
	}

	public void setBreakfast3(YesNo breakfast3) {
		this.breakfast3 = breakfast3;
	}

	public String getTotalNoPersons3() {
		return totalNoPersons3;
	}

	public void setTotalNoPersons3(String totalNoPersons3) {
		this.totalNoPersons3 = totalNoPersons3;
	}

	public String getFoodConsumed3() {
		return foodConsumed3;
	}

	public void setFoodConsumed3(String foodConsumed3) {
		this.foodConsumed3 = foodConsumed3;
	}

	public String getSourceOfFood3() {
		return sourceOfFood3;
	}

	public void setSourceOfFood3(String sourceOfFood3) {
		this.sourceOfFood3 = sourceOfFood3;
	}

	public YesNo getConsumedAtPlace3() {
		return consumedAtPlace3;
	}

	public void setConsumedAtPlace3(YesNo consumedAtPlace3) {
		this.consumedAtPlace3 = consumedAtPlace3;
	}

	public YesNo getLunchL3() {
		return lunchL3;
	}

	public void setLunchL3(YesNo lunchL3) {
		this.lunchL3 = lunchL3;
	}

	public String getTotalNoPersonsL3() {
		return totalNoPersonsL3;
	}

	public void setTotalNoPersonsL3(String totalNoPersonsL3) {
		this.totalNoPersonsL3 = totalNoPersonsL3;
	}

	public String getFoodConsumedL3() {
		return foodConsumedL3;
	}

	public void setFoodConsumedL3(String foodConsumedL3) {
		this.foodConsumedL3 = foodConsumedL3;
	}

	public String getSourceOfFoodL3() {
		return sourceOfFoodL3;
	}

	public void setSourceOfFoodL3(String sourceOfFoodL3) {
		this.sourceOfFoodL3 = sourceOfFoodL3;
	}

	public YesNo getConsumedAtPlaceL3() {
		return consumedAtPlaceL3;
	}

	public void setConsumedAtPlaceL3(YesNo consumedAtPlaceL3) {
		this.consumedAtPlaceL3 = consumedAtPlaceL3;
	}

	public YesNo getSupperS3() {
		return supperS3;
	}

	public void setSupperS3(YesNo supperS3) {
		this.supperS3 = supperS3;
	}

	public String getTotalNoPersonsS3() {
		return totalNoPersonsS3;
	}

	public void setTotalNoPersonsS3(String totalNoPersonsS3) {
		this.totalNoPersonsS3 = totalNoPersonsS3;
	}

	public String getFoodConsumedS3() {
		return foodConsumedS3;
	}

	public void setFoodConsumedS3(String foodConsumedS3) {
		this.foodConsumedS3 = foodConsumedS3;
	}

	public String getSourceOfFoodS3() {
		return sourceOfFoodS3;
	}

	public void setSourceOfFoodS3(String sourceOfFoodS3) {
		this.sourceOfFoodS3 = sourceOfFoodS3;
	}

	public YesNo getConsumedAtPlaceS3() {
		return consumedAtPlaceS3;
	}

	public void setConsumedAtPlaceS3(YesNo consumedAtPlaceS3) {
		this.consumedAtPlaceS3 = consumedAtPlaceS3;
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
	public String getNameOfVaccine() {
		return nameOfVaccine;
	}

	public void setNameOfVaccine(String nameOfVaccine) {
		this.nameOfVaccine = nameOfVaccine;
	}

	public YesNo getPreviouslyVaccinatedAgainstCovid() {
		return previouslyVaccinatedAgainstCovid;
	}

	public void setPreviouslyVaccinatedAgainstCovid(YesNo previouslyVaccinatedAgainstCovid) {
		this.previouslyVaccinatedAgainstCovid = previouslyVaccinatedAgainstCovid;
	}

	public Date getYearOfVaccinationCovid() {
		return yearOfVaccinationCovid;
	}

	public void setYearOfVaccinationCovid(Date yearOfVaccinationCovid) {
		this.yearOfVaccinationCovid = yearOfVaccinationCovid;
	}

	public String getNameOfVaccineCovid() {
		return nameOfVaccineCovid;
	}

	public void setNameOfVaccineCovid(String nameOfVaccineCovid) {
		this.nameOfVaccineCovid = nameOfVaccineCovid;
	}

	public String getIfYesSpecifySick() {
		return ifYesSpecifySick;
	}

	public void setIfYesSpecifySick(String ifYesSpecifySick) {
		this.ifYesSpecifySick = ifYesSpecifySick;
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

	public YesNo getHistoryOfTravelOutsideTheVillageTownDistrict() {
		return this.historyOfTravelOutsideTheVillageTownDistrict;
	}

	public void setHistoryOfTravelOutsideTheVillageTownDistrict(final YesNo historyOfTravelOutsideTheVillageTownDistrict) {
		this.historyOfTravelOutsideTheVillageTownDistrict = historyOfTravelOutsideTheVillageTownDistrict;
	}

	public String getHistoryOfTravelOutsideTheVillageTownDistrictDetails() {
		return this.historyOfTravelOutsideTheVillageTownDistrictDetails;
	}

	public void setHistoryOfTravelOutsideTheVillageTownDistrictDetails(final String historyOfTravelOutsideTheVillageTownDistrictDetails) {
		this.historyOfTravelOutsideTheVillageTownDistrictDetails = historyOfTravelOutsideTheVillageTownDistrictDetails;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public Region getHistoryOfTravelRegion() {
		return this.historyOfTravelRegion;
	}

	public void setHistoryOfTravelRegion(final Region historyOfTravelRegion) {
		this.historyOfTravelRegion = historyOfTravelRegion;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public District getHistoryOfTravelDistrict() {
		return this.historyOfTravelDistrict;
	}

	public void setHistoryOfTravelDistrict(final District historyOfTravelDistrict) {
		this.historyOfTravelDistrict = historyOfTravelDistrict;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public Community getHistoryOfTravelSubDistrict() {
		return this.historyOfTravelSubDistrict;
	}

	public void setHistoryOfTravelSubDistrict(final Community historyOfTravelSubDistrict) {
		this.historyOfTravelSubDistrict = historyOfTravelSubDistrict;
	}

	public String getHistoryOfTravelVillage() {
		return this.historyOfTravelVillage;
	}

	public void setHistoryOfTravelVillage(final String historyOfTravelVillage) {
		this.historyOfTravelVillage = historyOfTravelVillage;
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
}
