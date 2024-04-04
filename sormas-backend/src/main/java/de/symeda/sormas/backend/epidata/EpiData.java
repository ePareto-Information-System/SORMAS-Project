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
import de.symeda.sormas.api.riskfactor.DrinkingWaterSource;
import de.symeda.sormas.api.utils.RiskFactorInfluenza;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.backend.activityascase.ActivityAsCase;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.common.NotExposedToApi;
import de.symeda.sormas.backend.exposure.Exposure;
import org.apache.commons.lang3.StringUtils;

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

	private List<Exposure> exposures = new ArrayList<>();
	private List<ActivityAsCase> activitiesAsCase = new ArrayList<>();
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

}
