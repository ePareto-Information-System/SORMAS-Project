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
import de.symeda.sormas.api.utils.RiskFactorCondition;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.api.caze.CaseOutcome;
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
	private Integer yearOfVaccination;
	private String placesVisitedPastSevenDays;
	private YesNo vistedPlacesConfirmedPandemic;
	private RiskFactorCondition riskFactorsSevereDisease;
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
	private String nameOfVaccine;
	private YesNo previouslyVaccinatedAgainstCovid;
	private Integer yearOfVaccinationCovid;
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
	private String suspectLastName;

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
	private YesNo waterUsedForDrinking;
	private YesNo waterUsedNotForDrinking;
	private YesNo foodItems;


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
	
	@Enumerated(EnumType.STRING)
	public YesNo getPreviouslyVaccinatedAgainstInfluenza() {
		return previouslyVaccinatedAgainstInfluenza;
	}

	public void setPreviouslyVaccinatedAgainstInfluenza(YesNo previouslyVaccinatedAgainstInfluenza) {
		this.previouslyVaccinatedAgainstInfluenza = previouslyVaccinatedAgainstInfluenza;
	}

	public Integer getYearOfVaccination() {
		return yearOfVaccination;
	}

	public void setYearOfVaccination(Integer yearOfVaccination) {
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

	public Integer getYearOfVaccinationCovid() {
		return yearOfVaccinationCovid;
	}

	public void setYearOfVaccinationCovid(Integer yearOfVaccinationCovid) {
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
	public YesNo getWaterUsedForDrinking() {
		return this.waterUsedForDrinking;
	}

	public void setWaterUsedForDrinking(final YesNo waterUsedForDrinking) {
		this.waterUsedForDrinking = waterUsedForDrinking;
	}

	public YesNo getWaterUsedNotForDrinking() {
		return this.waterUsedNotForDrinking;
	}

	public void setWaterUsedNotForDrinking(final YesNo waterUsedNotForDrinking) {
		this.waterUsedNotForDrinking = waterUsedNotForDrinking;
	}

	public YesNo getFoodItems() {
		return this.foodItems;
	}

	public void setFoodItems(final YesNo foodItems) {
		this.foodItems = foodItems;
	}

	public String getSuspectLastName() {
		return suspectLastName;
	}

	public void setSuspectLastName(String suspectLastName) {
		this.suspectLastName = suspectLastName;
	}
}
