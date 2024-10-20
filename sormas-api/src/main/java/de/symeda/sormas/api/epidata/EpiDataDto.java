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
package de.symeda.sormas.api.epidata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.ImportIgnore;
import de.symeda.sormas.api.activityascase.ActivityAsCaseDto;
import de.symeda.sormas.api.caze.CaseOutcome;
import de.symeda.sormas.api.exposure.ExposureDto;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.riskfactor.DrinkingWaterSource;
import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.utils.*;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableDto;

@DependingOnFeatureType(featureType = {
	FeatureType.CASE_SURVEILANCE,
	FeatureType.CONTACT_TRACING })
public class EpiDataDto extends PseudonymizableDto {

	private static final long serialVersionUID = 6292411396563549093L;

	public static final String I18N_PREFIX = "EpiData";

	public static final String EXPOSURE_DETAILS_KNOWN = "exposureDetailsKnown";
	public static final String ACTIVITY_AS_CASE_DETAILS_KNOWN = "activityAsCaseDetailsKnown";
	public static final String RECENT_TRAVEL_OUTBREAK = "recentTravelOutbreak";
	public static final String CONTACT_SIMILAR_SYMPTOMS = "contactSimilarOutbreak";
	public static final String CONTACT_SICK_ANIMALS = "contactSickAnimals";
	public static final String IF_YES_SPECIFY_SICK_ANIMAL = "ifYesSpecifySick";
	public static final String CONTACT_DEAD_WILD_ANIMALS = "contactDeadAnimals";
	public static final String IF_YES_SPECIFY_DEAD_WILD_ANIMAL = "ifYesSpecifyDead";
	public static final String CONTACT_WITH_SOURCE_CASE_KNOWN = "contactWithSourceCaseKnown";
	public static final String EXPOSURES = "exposures";
	public static final String ACTIVITIES_AS_CASE = "activitiesAsCase";
	public static final String AREA_INFECTED_ANIMALS = "areaInfectedAnimals";
	public static final String HIGH_TRANSMISSION_RISK_AREA = "highTransmissionRiskArea";
	public static final String LARGE_OUTBREAKS_AREA = "largeOutbreaksArea";
	public static final String CHILD_COME_IN_CONTACT_WITH_SYMPTOMS = "childComeInContactWithSymptoms";

	public static final String PREVIOUSLY_VACCINATED_AGAINST_INFLUENZA = "previouslyVaccinatedAgainstInfluenza";
	public static final String YEAR_OF_VACCINATION = "yearOfVaccination";
	public static final String NAME_OF_VACCINE = "nameOfVaccine";
	public static final String PREVIOUSLY_VACCINATED_AGAINST_COVID = "previouslyVaccinatedAgainstCovid";
	public static final String NAME_OF_VACCINE_FOR_COVID = "nameOfVaccineCovid";
	public static final String YEAR_OF_VACCINATION_FOR_COVID = "yearOfVaccinationCovid";
	public static final String PLACES_VISITED_PAST_7DAYS = "placesVisitedPastSevenDays";
	public static final String VISITED_PLACES_CONFIRMED_PANDEMIC = "vistedPlacesConfirmedPandemic";
	public static final String RISK_FACTORS_SEVERE_DISEASE = "riskFactorsSevereDisease";
	public static final String PATIENT_TRAVELLED_TWO_WEEKS_PRIOR = "patientTravelledTwoWeeksPrior";
	public static final String PATIENT_TRAVELLED_IN_COUNTRY_ONE = "patientTravelledInCountryOne";
	public static final String PATIENT_TRAVELLED_IN_COUNTRY_TWO = "patientTravelledInCountryTwo";
	public static final String PATIENT_TRAVELLED_IN_COUNTRY_THREE = "patientTravelledInCountryThree";
	public static final String PATIENT_TRAVELLED_IN_COUNTRY_FOUR = "patientTravelledInCountryFour";
	public static final String PATIENT_TRAVELLED_INTERNATIONAL_ONE = "patientTravelledInternationalOne";
	public static final String PATIENT_TRAVELLED_INTERNATIONAL_TWO = "patientTravelledInternationalTwo";
	public static final String PATIENT_TRAVELLED_INTERNATIONAL_THREE = "patientTravelledInternationalThree";
	public static final String PATIENT_TRAVELLED_INTERNATIONAL_FOUR = "patientTravelledInternationalFour";
	public static final String OTHER_SPECIFY = "otherSpecify";
	public static final String PATIENT_VISITED_HEALTH_CARE_FACILITY = "patientVisitedHealthCareFacility";
	public static final String PATIENT_CLOSE_CONTACT_WITH_ARI = "patientCloseContactWithARI";
	public static final String PATIENT_CLOSE_CONTACT_WITH_ARI_CONTACT_SETTINGS = "patientCloseContactWithARIContactSettings";
	public static final String PATIENT_CONTACT_WITH_CONFIRMED_CASE = "patientContactWithConfirmedCase";
	public static final String PATIENT_CONTACT_WITH_CONFIRMED_CASE_EXPOSURE_LOCATIONS = "patientContactWithConfirmedCaseExposureLocations";
	public static final String PATIENT_CONTACT_WITH_CONFIRMED_CASE_EXPOSURE_LOCATION_CITY_COUNTRY = "patientContactWithConfirmedCaseExposureLocationCityCountry";
	public static final String EXPOSED_TO_RISK_FACTOR = "exposedToRiskFactor";
	public static final String WATER_USED_BY_PATIENT_AFTER_EXPOSURE = "waterUsedByPatientAfterExposure";
	public static final String PERSON_TRAVEL_HISTORY = "personTravelHistories";
	public static final String CONTAMINATION_SOURCES = "contaminationSources";
	public static final String CONTAINMENT_MEASURES = "containmentMeasures";
	public static final String RECEIVED_HEALTH_EDUCATION = "receivedHealthEducation";
	public static final String PATIENT_ENTERED_WATER_SOURCE = "patientEnteredWaterSource";
	public static final String PLACE_MANAGED = "placeManaged";
	public static final String WATER_USED_FOR_DRINKING = "waterUsedForDrinking";
	public static final String WATER_USED_NOT_FOR_DRINKING = "waterUsedNotForDrinking";
	public static final String FOOD_ITEMS = "foodItems";

	public static final String INTL_TRAVEL = "intlTravel";
	public static final String SPECIFY_COUNTRIES = "specifyCountries";
	public static final String DATE_OF_DEPARTURE = "dateOfDeparture";
	public static final String DATE_OF_ARRIVAL = "dateOfArrival";
	public static final String DOMESTIC_TRAVEL = "domesticTravel";
	public static final String SPECIFY_LOCATION = "specifyLocation";
	public static final String DATE_OF_DEPARTURE2 = "dateOfDeparture2";
	public static final String DATE_OF_ARRIVAL2 = "dateOfArrival2";
	public static final String CONTACT_ILL_PERSON = "contactIllPerson";
	public static final String CONTACT_DATE = "contactDate";
	public static final String SPECIFY_ILLNESS = "specifyIllness";
	public static final String PATIENT_TRAVEL_DURING_ILLNESS = "patientTravelDuringIllness";
	public static final String COMM1 = "comm1";
	public static final String HEALTH_CENTER1 = "healthCenter1";
	public static final String COUNTRY1 = "country1";
	public static final String COMM2 = "comm2";
	public static final String HEALTH_CENTER2 = "healthCenter2";
	public static final String COUNTRY2 = "country2";
	public static final String WAS_PATIENT_HOSPITALIZED = "wasPatientHospitalized";
	public static final String IF_YES_WHERE = "ifYesWhere";
	public static final String HOSPITALIZED_DATE1 = "hospitalizedDate1";
	public static final String HOSPITALIZED_DATE2 = "hospitalizedDate2";
	public static final String DID_PATIENT_CONSULT_HEALER = "didPatientConsultHealer";
	public static final String IF_YES_NAME_HEALER = "ifYesNameHealer";
	public static final String COMMUNITY = "community";
	public static final String COUNTRY = "country";
	public static final String WHEN_WHERE_CONTACT_TAKE_PLACE = "whenWhereContactTakePlace";
	public static final String DATE_OF_CONTACT = "dateOfContact";
	public static final String PATIENT_RECEIVE_TRADITIONAL_MEDICINE = "patientReceiveTraditionalMedicine";
	public static final String IF_YES_EXPLAIN = "ifYesExplain";
	public static final String PATIENT_ATTEND_FUNERAL_CEREMONIES = "patientAttendFuneralCeremonies";
	public static final String PATIENT_TRAVEL_ANYTIME_PERIOD_BEFORE_ILL = "patientTravelAnytimePeriodBeforeIll";
	public static final String IF_TRAVEL_YES_WHERE = "ifTravelYesWhere";
	public static final String IF_TRAVEL_START_DATE = "ifYesStartDate";
	public static final String IF_YES_END_DATE = "ifYesEndDate";
	public static final String PATIENT_CONTACT_KNOWN_SUSPECT = "patientContactKnownSuspect";
	public static final String SUSPECT_NAME = "suspectName";
	public static final String SUSPECT_LAST_NAME = "suspectLastName";
	public static final String ID_CASE = "idCase";
	public static final String DURING_CONTACT_SUSPECT_CASE = "duringContactSuspectCase";
	public static final String DATE_OF_DEATH = "dateOfDeath";
	public static final String DATE_OF_LAST_CONTACT_WITH_SUSPECT_CASE = "dateOfLastContactWithSuspectCase";
	public static final String IF_YES_WILD_ANIMAL_LOCATION = "ifYesWildAnimalLocation";
	public static final String IF_YES_WILD_ANIMAL_DATE = "ifYesWildAnimalDate";
	public static final String HISTORY_OF_TRAVEL_OUTSIDE_THE_VILLAGE_TOWN_DISTRICT = "historyOfTravelOutsideTheVillageTownDistrict";
	public static final String HISTORY_OF_TRAVEL_OUTSIDE_THE_VILLAGE_TOWN_DISTRICT_DETAILS = "historyOfTravelOutsideTheVillageTownDistrictDetails";

	public static final String HISTORY_OF_TRAVEL_REGION = "historyOfTravelRegion";
	public static final String HISTORY_OF_TRAVEL_DISTRICT = "historyOfTravelDistrict";
	public static final String HISTORY_OF_TRAVEL_SUB_DISTRICT = "historyOfTravelSubDistrict";
	public static final String HISTORY_OF_TRAVEL_VILLAGE = "historyOfTravelVillage";

	@Enumerated(EnumType.STRING)
	private Disease disease;
	private YesNo exposureDetailsKnown;
	private YesNo activityAsCaseDetailsKnown;
	private YesNo recentTravelOutbreak;
	private YesNo contactSimilarOutbreak;
	private YesNo contactSickAnimals;
	private YesNo contactWithSourceCaseKnown;
	private YesNo highTransmissionRiskArea;
	private YesNo largeOutbreaksArea;
	@Diseases({
		Disease.AFP,
		Disease.GUINEA_WORM,
		Disease.NEW_INFLUENZA,
		Disease.ANTHRAX,
		Disease.POLIO,
		Disease.UNDEFINED,
		Disease.OTHER })
	private YesNo areaInfectedAnimals;

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
	private String suspectLastName;
	private String idCase;
	private CaseOutcome duringContactSuspectCase;
	private Date dateOfDeath;
	private Date dateOfLastContactWithSuspectCase;
	private String ifYesWildAnimalLocation;
	private Date ifYesWildAnimalDate;


	@Valid
	private List<ExposureDto> exposures = new ArrayList<>();

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
	private Set<ContactSetting> patientCloseContactWithARIContactSettings;
	private YesNoUnknown patientContactWithConfirmedCase;
	private Set<ContactSetting> patientContactWithConfirmedCaseExposureLocations;
	private String patientContactWithConfirmedCaseExposureLocationCityCountry;

	private YesNo exposedToRiskFactor;
	private DrinkingWaterSource waterUsedByPatientAfterExposure;

	@Diseases({
			Disease.GUINEA_WORM
	})
	private YesNo receivedHealthEducation;

	@Diseases({
			Disease.GUINEA_WORM
	})
	private YesNo patientEnteredWaterSource;

	@Diseases({
			Disease.GUINEA_WORM
	})
	private PlaceManaged placeManaged;

	@Valid
	private List<ActivityAsCaseDto> activitiesAsCase = new ArrayList<>();
	@Diseases({
			Disease.GUINEA_WORM
	})
	@Valid
	private List<PersonTravelHistoryDto> personTravelHistories = new ArrayList<>();
	@Diseases({
			Disease.GUINEA_WORM
	})
	@Valid
	private List<ContaminationSourceDto> contaminationSources = new ArrayList<>();
	@Diseases({
			Disease.GUINEA_WORM
	})
	@Valid
	private List<ContainmentMeasureDto> containmentMeasures = new ArrayList<>();

	private YesNo waterUsedForDrinking;
	private YesNo waterUsedNotForDrinking;
	private YesNo foodItems;


	public YesNo getExposureDetailsKnown() {
		return exposureDetailsKnown;
	}

	private YesNoUnknown childComeInContactWithSymptoms;

	private YesNo historyOfTravelOutsideTheVillageTownDistrict;
	private String historyOfTravelOutsideTheVillageTownDistrictDetails;
	private RegionReferenceDto historyOfTravelRegion;
	private DistrictReferenceDto historyOfTravelDistrict;
	private CommunityReferenceDto historyOfTravelSubDistrict;
	private String historyOfTravelVillage;


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

	@ImportIgnore
	public List<ExposureDto> getExposures() {
		return exposures;
	}

	public void setExposures(List<ExposureDto> exposures) {
		this.exposures = exposures;
	}

	@ImportIgnore
	public List<ActivityAsCaseDto> getActivitiesAsCase() {
		return activitiesAsCase;
	}


	public void setActivitiesAsCase(List<ActivityAsCaseDto> activitiesAsCase) {
		this.activitiesAsCase = activitiesAsCase;
	}

	@ImportIgnore
	public List<PersonTravelHistoryDto> getPersonTravelHistories() {
		return personTravelHistories;
	}

	public void setPersonTravelHistories(List<PersonTravelHistoryDto> personTravelHistories) {
		this.personTravelHistories = personTravelHistories;
	}

	@ImportIgnore
	public List<ContaminationSourceDto> getContaminationSources() {
		return contaminationSources;
	}

	public void setContaminationSources(List<ContaminationSourceDto> contaminationSources) {
		this.contaminationSources = contaminationSources;
	}

	@ImportIgnore
	public List<ContainmentMeasureDto> getContainmentMeasures() {
		return containmentMeasures;
	}

	public void setContainmentMeasures(List<ContainmentMeasureDto> containmentMeasures) {
		this.containmentMeasures = containmentMeasures;
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

	public Disease getDisease() {
		return disease;
	}
	public void setDisease(Disease disease) {
		this.disease = disease;
	}

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


	public static EpiDataDto build() {

		EpiDataDto epiData = new EpiDataDto();
		epiData.setUuid(DataHelper.createUuid());
		return epiData;
	}

	@Override
	public EpiDataDto clone() throws CloneNotSupportedException {
		EpiDataDto clone = (EpiDataDto) super.clone();
		List<ActivityAsCaseDto> activityAsCaseDtos = new ArrayList<>();
		for (ActivityAsCaseDto activityAsCase : getActivitiesAsCase()) {
			activityAsCaseDtos.add(activityAsCase.clone());
		}
		clone.getActivitiesAsCase().clear();
		clone.getActivitiesAsCase().addAll(activityAsCaseDtos);

		List<ExposureDto> exposureDtos = new ArrayList<>();
		for (ExposureDto exposure : getExposures()) {
			exposureDtos.add(exposure.clone());
		}
		clone.getExposures().clear();
		clone.getExposures().addAll(exposureDtos);

		List<PersonTravelHistoryDto> personTravelHistoryDtos = new ArrayList<>();
		for (PersonTravelHistoryDto personTravelHistory : getPersonTravelHistories()) {
			personTravelHistoryDtos.add(personTravelHistory.clone());
		}
		clone.getPersonTravelHistories().clear();
		clone.getPersonTravelHistories().addAll(personTravelHistoryDtos);

		List<ContaminationSourceDto> contaminationSourcesDtos = new ArrayList<>();
		for (ContaminationSourceDto contaminationSources : getContaminationSources()) {
			contaminationSourcesDtos.add(contaminationSources.clone());
		}
		clone.getPersonTravelHistories().clear();
		clone.getPersonTravelHistories().addAll(personTravelHistoryDtos);

		List<ContainmentMeasureDto> containmentMeasureDtos = new ArrayList<>();
		for (ContainmentMeasureDto containmentMeasure : getContainmentMeasures()) {
			containmentMeasureDtos.add(containmentMeasure.clone());
		}
		clone.getContainmentMeasures().clear();
		clone.getContainmentMeasures().addAll(containmentMeasureDtos);

		return clone;
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

	public YesNoUnknown getChildComeInContactWithSymptoms() {
		return childComeInContactWithSymptoms;
	}
	public void setChildComeInContactWithSymptoms(YesNoUnknown childComeInContactWithSymptoms) {
		this.childComeInContactWithSymptoms = childComeInContactWithSymptoms;
	}
	public void setPatientCloseContactWithARI(YesNoUnknown patientCloseContactWithARI) {
		this.patientCloseContactWithARI = patientCloseContactWithARI;
	}

	public Set<ContactSetting> getPatientCloseContactWithARIContactSettings() {
		return patientCloseContactWithARIContactSettings;
	}

	public void setPatientCloseContactWithARIContactSettings(Set<ContactSetting> patientCloseContactWithARIContactSettings) {
		this.patientCloseContactWithARIContactSettings = patientCloseContactWithARIContactSettings;
	}

	public YesNoUnknown getPatientContactWithConfirmedCase() {
		return patientContactWithConfirmedCase;
	}

	public void setPatientContactWithConfirmedCase(YesNoUnknown patientContactWithConfirmedCase) {
		this.patientContactWithConfirmedCase = patientContactWithConfirmedCase;
	}

	public Set<ContactSetting> getPatientContactWithConfirmedCaseExposureLocations() {
		return patientContactWithConfirmedCaseExposureLocations;
	}

	public void setPatientContactWithConfirmedCaseExposureLocations(Set<ContactSetting> patientContactWithConfirmedCaseExposureLocations) {
		this.patientContactWithConfirmedCaseExposureLocations = patientContactWithConfirmedCaseExposureLocations;
	}

	public String getPatientContactWithConfirmedCaseExposureLocationCityCountry() {
		return patientContactWithConfirmedCaseExposureLocationCityCountry;
	}

	public void setPatientContactWithConfirmedCaseExposureLocationCityCountry(String patientContactWithConfirmedCaseExposureLocationCityCountry) {
		this.patientContactWithConfirmedCaseExposureLocationCityCountry = patientContactWithConfirmedCaseExposureLocationCityCountry;
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
	public YesNoUnknown getPatientVisitedHealthCareFacility() {
		return patientVisitedHealthCareFacility;
	}

	public void setPatientVisitedHealthCareFacility(YesNoUnknown patientVisitedHealthCareFacility) {
		this.patientVisitedHealthCareFacility = patientVisitedHealthCareFacility;
	}

	public YesNoUnknown getPatientCloseContactWithARI() {
		return patientCloseContactWithARI;
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

	public RegionReferenceDto getHistoryOfTravelRegion() {
		return this.historyOfTravelRegion;
	}

	public void setHistoryOfTravelRegion(final RegionReferenceDto historyOfTravelRegion) {
		this.historyOfTravelRegion = historyOfTravelRegion;
	}

	public DistrictReferenceDto getHistoryOfTravelDistrict() {
		return this.historyOfTravelDistrict;
	}

	public void setHistoryOfTravelDistrict(final DistrictReferenceDto historyOfTravelDistrict) {
		this.historyOfTravelDistrict = historyOfTravelDistrict;
	}

	public CommunityReferenceDto getHistoryOfTravelSubDistrict() {
		return this.historyOfTravelSubDistrict;
	}

	public void setHistoryOfTravelSubDistrict(final CommunityReferenceDto historyOfTravelSubDistrict) {
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
