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

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import de.symeda.sormas.api.activityascase.ActivityAsCaseDto;
import de.symeda.sormas.api.epidata.*;
import de.symeda.sormas.api.exposure.ExposureDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.FacadeHelper;
import de.symeda.sormas.backend.activityascase.ActivityAsCase;
import de.symeda.sormas.backend.activityascase.ActivityAsCaseService;
import de.symeda.sormas.backend.contact.ContactFacadeEjb;
import de.symeda.sormas.backend.contact.ContactService;
import de.symeda.sormas.backend.containmentmeasure.ContainmentMeasure;
import de.symeda.sormas.backend.containmentmeasure.ContainmentMeasureService;
import de.symeda.sormas.backend.contaminationsource.ContaminationSource;
import de.symeda.sormas.backend.contaminationsource.ContaminationSourceService;
import de.symeda.sormas.backend.exposure.Exposure;
import de.symeda.sormas.backend.exposure.ExposureService;
import de.symeda.sormas.backend.infrastructure.community.CommunityFacadeEjb;
import de.symeda.sormas.backend.infrastructure.community.CommunityService;
import de.symeda.sormas.backend.infrastructure.district.DistrictFacadeEjb;
import de.symeda.sormas.backend.infrastructure.district.DistrictService;
import de.symeda.sormas.backend.infrastructure.region.RegionFacadeEjb;
import de.symeda.sormas.backend.infrastructure.region.RegionService;
import de.symeda.sormas.backend.location.LocationFacadeEjb;
import de.symeda.sormas.backend.location.LocationFacadeEjb.LocationFacadeEjbLocal;
import de.symeda.sormas.backend.persontravelhistory.PersonTravelHistory;
import de.symeda.sormas.backend.persontravelhistory.PersonTravelHistoryService;
import de.symeda.sormas.backend.user.UserFacadeEjb;
import de.symeda.sormas.backend.user.UserService;
import de.symeda.sormas.backend.util.DtoHelper;

@Stateless(name = "EpiDataFacade")
public class EpiDataFacadeEjb implements EpiDataFacade {

	@EJB
	private ExposureService exposureService;
	@EJB
	private ActivityAsCaseService activityAsCaseService;
	@EJB
	private LocationFacadeEjbLocal locationFacade;
	@EJB
	private ContactService contactService;
	@EJB
	private UserService userService;
	@EJB
	private RegionService regionService;
	@EJB
	private DistrictService districtService;
	@EJB
	private CommunityService communityService;
	@EJB
	private PersonTravelHistoryService personTravelHistoryService;
	@EJB
	private ContaminationSourceService contaminationSourceService;
	@EJB
	private ContainmentMeasureService containmentMeasureService;


	@EJB
	private EpiDataService service;

	public EpiData fillOrBuildEntity(EpiDataDto source, EpiData target, boolean checkChangeDate) {
		if (source == null) {
			return null;
		}

		target = DtoHelper.fillOrBuildEntity(source, target, EpiData::new, checkChangeDate);

		target.setExposureDetailsKnown(source.getExposureDetailsKnown());
		target.setActivityAsCaseDetailsKnown(source.getActivityAsCaseDetailsKnown());
		target.setRecentTravelOutbreak(source.getRecentTravelOutbreak());
		target.setContactSimilarOutbreak(source.getContactSimilarOutbreak());
		target.setContactSickAnimals(source.getContactSickAnimals());
		target.setContactWithSourceCaseKnown(source.getContactWithSourceCaseKnown());
		target.setHighTransmissionRiskArea(source.getHighTransmissionRiskArea());
		target.setLargeOutbreaksArea(source.getLargeOutbreaksArea());
		target.setAreaInfectedAnimals(source.getAreaInfectedAnimals());
		target.setDisease(source.getDisease());
		target.setPreviouslyVaccinatedAgainstInfluenza(source.getPreviouslyVaccinatedAgainstInfluenza());
		target.setYearOfVaccination(source.getYearOfVaccination());
		target.setPlacesVisitedPastSevenDays(source.getPlacesVisitedPastSevenDays());
		target.setVistedPlacesConfirmedPandemic(source.getVistedPlacesConfirmedPandemic());
		target.setRiskFactorsSevereDisease(source.getRiskFactorsSevereDisease());
		target.setOtherSpecify(source.getOtherSpecify());
		target.setIntlTravel(source.getIntlTravel());
		target.setSpecifyCountries(source.getSpecifyCountries());
		target.setDateOfDeparture(source.getDateOfDeparture());
		target.setDateOfArrival(source.getDateOfArrival());
		target.setDomesticTravel(source.getDomesticTravel());
		target.setSpecifyLocation(source.getSpecifyLocation());
		target.setDateOfDeparture2(source.getDateOfDeparture2());
		target.setDateOfArrival2(source.getDateOfArrival2());
		target.setContactIllPerson(source.getContactIllPerson());
		target.setContactDate(source.getContactDate());
		target.setSpecifyIllness(source.getSpecifyIllness());
		target.setNameOfAffectedPerson(source.getNameOfAffectedPerson());
		target.setNameOfAffectedPerson2(source.getNameOfAffectedPerson2());
		target.setNameOfAffectedPerson3(source.getNameOfAffectedPerson3());
		target.setNameOfAffectedPerson4(source.getNameOfAffectedPerson4());
		target.setTelNo(source.getTelNo());
		target.setTelNo2(source.getTelNo2());
		target.setTelNo3(source.getTelNo3());
		target.setTelNo4(source.getTelNo4());
		target.setDateTime(source.getDateTime());
		target.setDateTime2(source.getDateTime2());
		target.setDateTime3(source.getDateTime3());
		target.setDateTime4(source.getDateTime4());
		target.setAge(source.getAge());
		target.setAge2(source.getAge2());
		target.setAge3(source.getAge3());
		target.setAge4(source.getAge4());
		target.setSuspectedFood(source.getSuspectedFood());
		target.setDateConsumed(source.getDateConsumed());
		target.setFoodSource(source.getFoodSource());
		target.setEventType(source.getEventType());
		target.setEventOtherSpecify(source.getEventOtherSpecify());
		target.setBreakfast(source.getBreakfast());
		target.setTotalNoPersons(source.getTotalNoPersons());
		target.setFoodConsumed(source.getFoodConsumed());
		target.setSourceOfFood(source.getSourceOfFood());
		target.setConsumedAtPlace(source.getConsumedAtPlace());
		target.setLunch(source.getLunch());
		target.setTotalNoPersonsL1(source.getTotalNoPersonsL1());
		target.setFoodConsumedL1(source.getFoodConsumedL1());
		target.setSourceOfFoodL1(source.getSourceOfFoodL1());
		target.setConsumedAtPlaceL1(source.getConsumedAtPlaceL1());
		target.setSupper(source.getSupper());
		target.setTotalNoPersonsS1(source.getTotalNoPersonsS1());
		target.setFoodConsumedS1(source.getFoodConsumedS1());
		target.setSourceOfFoodsS1(source.getSourceOfFoodsS1());
		target.setConsumedAtPlaceS1(source.getConsumedAtPlaceS1());

		target.setBreakfast2(source.getBreakfast2());
		target.setTotalNoPersons2(source.getTotalNoPersons2());
		target.setFoodConsumed2(source.getFoodConsumed2());
		target.setSourceOfFood2(source.getSourceOfFood2());
		target.setConsumedAtPlace2(source.getConsumedAtPlace2());
		target.setLunchL2(source.getLunchL2());
		target.setTotalNoPersonsL2(source.getTotalNoPersonsL2());
		target.setFoodConsumedL2(source.getFoodConsumedL2());
		target.setSourceOfFoodL2(source.getSourceOfFoodL2());
		target.setConsumedAtPlaceL2(source.getConsumedAtPlaceL2());
		target.setSupperS2(source.getSupperS2());
		target.setTotalNoPersonsS2(source.getTotalNoPersonsS2());
		target.setFoodConsumedS2(source.getFoodConsumedS2());
		target.setSourceOfFoodS2(source.getSourceOfFoodS2());
		target.setConsumedAtPlaceS2(source.getConsumedAtPlaceS2());
		target.setBreakfast3(source.getBreakfast3());
		target.setTotalNoPersons3(source.getTotalNoPersons3());
		target.setFoodConsumed3(source.getFoodConsumed3());
		target.setSourceOfFood3(source.getSourceOfFood3());
		target.setConsumedAtPlace3(source.getConsumedAtPlace3());
		target.setLunchL3(source.getLunchL3());
		target.setTotalNoPersonsL3(source.getTotalNoPersonsL3());
		target.setFoodConsumedL3(source.getFoodConsumedL3());
		target.setSourceOfFoodL3(source.getSourceOfFoodL3());
		target.setConsumedAtPlaceL3(source.getConsumedAtPlaceL3());
		target.setSupperS3(source.getSupperS3());
		target.setTotalNoPersonsS3(source.getTotalNoPersonsS3());
		target.setFoodConsumedS3(source.getFoodConsumedS3());
		target.setSourceOfFoodS3(source.getSourceOfFoodS3());
		target.setConsumedAtPlaceS3(source.getConsumedAtPlaceS3());




		List<Exposure> exposures = new ArrayList<>();
		for (ExposureDto exposureDto : source.getExposures()) {
			Exposure exposure = exposureService.getByUuid(exposureDto.getUuid());
			exposure = fillOrBuildExposureEntity(exposureDto, exposure, checkChangeDate);
			exposure.setEpiData(target);
			exposures.add(exposure);
		}
		if (!DataHelper.equalContains(target.getExposures(), exposures)) {
			// note: DataHelper.equal does not work here, because target.getAddresses may be a PersistentBag when using lazy loading
			target.setChangeDateOfEmbeddedLists(new Date());
		}
		target.getExposures().clear();
		target.getExposures().addAll(exposures);

		List<ActivityAsCase> activitiesAsCase = new ArrayList<>();
		for (ActivityAsCaseDto activityAsCaseDto : source.getActivitiesAsCase()) {
			ActivityAsCase activityAsCase = activityAsCaseService.getByUuid(activityAsCaseDto.getUuid());
			activityAsCase = fillOrBuildActivityAsCaseEntity(activityAsCaseDto, activityAsCase, checkChangeDate);
			activityAsCase.setEpiData(target);
			activitiesAsCase.add(activityAsCase);
		}
		if (!DataHelper.equalContains(target.getActivitiesAsCase(), activitiesAsCase)) {
			// note: DataHelper.equal does not work here, because target.getAddresses may be a PersistentBag when using lazy loading
			target.setChangeDateOfEmbeddedLists(new Date());
		}
		target.getActivitiesAsCase().clear();
		target.getActivitiesAsCase().addAll(activitiesAsCase);
		target.setChildComeInContactWithSymptoms(source.getChildComeInContactWithSymptoms());


		List<PersonTravelHistory> personTravelHistories = new ArrayList<>();
		for (PersonTravelHistoryDto personTravelHistoryDto : source.getPersonTravelHistories()) {
			PersonTravelHistory personTravelHistory = personTravelHistoryService.getByUuid(personTravelHistoryDto.getUuid());
			personTravelHistory = fillOrBuildPersonTravelHistoryEntity(personTravelHistoryDto, personTravelHistory, checkChangeDate);
			personTravelHistory.setEpiData(target);
			personTravelHistories.add(personTravelHistory);
		}
		if (!DataHelper.equalContains(target.getPersonTravelHistories(), personTravelHistories)) {
			// note: DataHelper.equal does not work here, because target.getAddresses may be a PersistentBag when using lazy loading
			target.setChangeDateOfEmbeddedLists(new Date());
		}
		target.getPersonTravelHistories().clear();
		target.getPersonTravelHistories().addAll(personTravelHistories);

		List<ContaminationSource> contaminationSources = new ArrayList<>();
		for (ContaminationSourceDto contaminationSourceDto : source.getContaminationSources()) {
			ContaminationSource contaminationSource = contaminationSourceService.getByUuid(contaminationSourceDto.getUuid());
			contaminationSource = fillOrBuildContaminationSourceEntity(contaminationSourceDto, contaminationSource, checkChangeDate);
			contaminationSource.setEpiData(target);
			contaminationSources.add(contaminationSource);
		}
		if (!DataHelper.equalContains(target.getContaminationSources(), contaminationSources)) {
			// note: DataHelper.equal does not work here, because target.getAddresses may be a PersistentBag when using lazy loading
			target.setChangeDateOfEmbeddedLists(new Date());
		}

		target.getContaminationSources().clear();
		target.getContaminationSources().addAll(contaminationSources);

		List<ContainmentMeasure> containmentMeasures = new ArrayList<>();
		for (ContainmentMeasureDto containmentMeasureDto : source.getContainmentMeasures()) {
			ContainmentMeasure containmentMeasure = containmentMeasureService.getByUuid(containmentMeasureDto.getUuid());
			containmentMeasure = fillOrBuildContainmentMeasureEntity(containmentMeasureDto, containmentMeasure, checkChangeDate);
			containmentMeasure.setEpiData(target);
			containmentMeasures.add(containmentMeasure);
		}
		if (!DataHelper.equalContains(target.getContainmentMeasures(), containmentMeasures)) {
			// note: DataHelper.equal does not work here, because target.getAddresses may be a PersistentBag when using lazy loading
			target.setChangeDateOfEmbeddedLists(new Date());
		}
		target.getContainmentMeasures().clear();
		target.getContainmentMeasures().addAll(containmentMeasures);


		target.setPatientTravelledTwoWeeksPrior(source.getPatientTravelledTwoWeeksPrior());
		target.setPatientTravelledInCountryOne(source.getPatientTravelledInCountryOne());
		target.setPatientTravelledInCountryTwo(source.getPatientTravelledInCountryTwo());
		target.setPatientTravelledInCountryThree(source.getPatientTravelledInCountryThree());
		target.setPatientTravelledInternationalOne(source.getPatientTravelledInternationalOne());
		target.setPatientTravelledInternationalTwo(source.getPatientTravelledInternationalTwo());
		target.setPatientTravelledInternationalThree(source.getPatientTravelledInternationalThree());
		target.setPatientTravelledInternationalFour(source.getPatientTravelledInternationalFour());
		target.setPatientVisitedHealthCareFacility(source.getPatientVisitedHealthCareFacility());
		target.setPatientCloseContactWithARI(source.getPatientCloseContactWithARI());
		target.setPatientCloseContactWithARIContactSettings(source.getPatientCloseContactWithARIContactSettings());
		target.setPatientContactWithConfirmedCase(source.getPatientContactWithConfirmedCase());
		target.setPatientContactWithConfirmedCaseExposureLocations(source.getPatientContactWithConfirmedCaseExposureLocations());
		target.setPatientContactWithConfirmedCaseExposureLocationCityCountry(source.getPatientContactWithConfirmedCaseExposureLocationCityCountry());
		target.setExposedToRiskFactor(source.getExposedToRiskFactor());
		target.setWaterUsedByPatientAfterExposure(source.getWaterUsedByPatientAfterExposure());
		target.setReceivedHealthEducation(source.getReceivedHealthEducation());
		target.setPatientEnteredWaterSource(source.getPatientEnteredWaterSource());
		target.setPlaceManaged(source.getPlaceManaged());

		return target;
	}


	public Exposure fillOrBuildExposureEntity(ExposureDto source, Exposure target, boolean checkChangeDate) {
		boolean targetWasNull = isNull(target);
		if (source == null) {
			return null;
		}

		target = DtoHelper.fillOrBuildEntity(source, target, Exposure::new, checkChangeDate);

		if (targetWasNull) {
			FacadeHelper.setUuidIfDtoExists(target.getLocation(), source.getLocation());
		}

		target.setAnimalCondition(source.getAnimalCondition());
		target.setTypeOfAnimal(source.getTypeOfAnimal());
		target.setTypeOfAnimalDetails(source.getTypeOfAnimalDetails());
		target.setAnimalContactType(source.getAnimalContactType());
		target.setAnimalContactTypeDetails(source.getAnimalContactTypeDetails());
		target.setAnimalMarket(source.getAnimalMarket());
		target.setAnimalVaccinated(source.getAnimalVaccinated());
		target.setContactToBodyFluids(source.getContactToBodyFluids());
		target.setContactToCase(contactService.getByReferenceDto(source.getContactToCase()));
		target.setDeceasedPersonIll(source.getDeceasedPersonIll());
		target.setDeceasedPersonName(source.getDeceasedPersonName());
		target.setDeceasedPersonRelation(source.getDeceasedPersonRelation());
		target.setDescription(source.getDescription());
		target.setEatingRawAnimalProducts(source.getEatingRawAnimalProducts());
		target.setEndDate(source.getEndDate());
		target.setExposureType(source.getExposureType());
		target.setExposureTypeDetails(source.getExposureTypeDetails());
		target.setGatheringDetails(source.getGatheringDetails());
		target.setGatheringType(source.getGatheringType());
		target.setHabitationDetails(source.getHabitationDetails());
		target.setHabitationType(source.getHabitationType());
		target.setHandlingAnimals(source.getHandlingAnimals());
		target.setHandlingSamples(source.getHandlingSamples());
		target.setIndoors(source.getIndoors());
		target.setLocation(locationFacade.fillOrBuildEntity(source.getLocation(), target.getLocation(), checkChangeDate));
		target.setLongFaceToFaceContact(source.getLongFaceToFaceContact());
		target.setOtherProtectiveMeasures(source.getOtherProtectiveMeasures());
		target.setProtectiveMeasuresDetails(source.getProtectiveMeasuresDetails());
		target.setOutdoors(source.getOutdoors());
		target.setPercutaneous(source.getPercutaneous());
		target.setPhysicalContactDuringPreparation(source.getPhysicalContactDuringPreparation());
		target.setPhysicalContactWithBody(source.getPhysicalContactWithBody());
		target.setReportingUser(userService.getByReferenceDto(source.getReportingUser()));
		target.setProbableInfectionEnvironment(source.isProbableInfectionEnvironment());
		target.setShortDistance(source.getShortDistance());
		target.setStartDate(source.getStartDate());
		target.setWearingMask(source.getWearingMask());
		target.setWearingPpe(source.getWearingPpe());
		target.setTypeOfPlace(source.getTypeOfPlace());
		target.setTypeOfPlaceDetails(source.getTypeOfPlaceDetails());
		target.setMeansOfTransport(source.getMeansOfTransport());
		target.setMeansOfTransportDetails(source.getMeansOfTransportDetails());
		target.setConnectionNumber(source.getConnectionNumber());
		target.setSeatNumber(source.getSeatNumber());
		target.setWorkEnvironment(source.getWorkEnvironment());
		target.setBodyOfWater(source.getBodyOfWater());
		target.setWaterSource(source.getWaterSource());
		target.setWaterSourceDetails(source.getWaterSourceDetails());
		target.setProphylaxis(source.getProphylaxis());
		target.setProphylaxisDate(source.getProphylaxisDate());
		target.setRiskArea(source.getRiskArea());
		target.setExposureRole(source.getExposureRole());
		target.setLargeAttendanceNumber(source.getLargeAttendanceNumber());

		return target;
	}

	public ActivityAsCase fillOrBuildActivityAsCaseEntity(ActivityAsCaseDto source, ActivityAsCase target, boolean checkChangeDate) {
		boolean targetWasNull = isNull(target);
		if (source == null) {
			return null;
		}

		target = DtoHelper.fillOrBuildEntity(source, target, ActivityAsCase::new, checkChangeDate);

		if (targetWasNull) {
			FacadeHelper.setUuidIfDtoExists(target.getLocation(), source.getLocation());
		}

		target.setReportingUser(userService.getByReferenceDto(source.getReportingUser()));
		target.setStartDate(source.getStartDate());
		target.setEndDate(source.getEndDate());
		target.setDescription(source.getDescription());
		target.setActivityAsCaseType(source.getActivityAsCaseType());
		target.setActivityAsCaseTypeDetails(source.getActivityAsCaseTypeDetails());
		target.setLocation(locationFacade.fillOrBuildEntity(source.getLocation(), target.getLocation(), checkChangeDate));
		target.setRole(source.getRole());

		target.setTypeOfPlace(source.getTypeOfPlace());
		target.setTypeOfPlaceDetails(source.getTypeOfPlaceDetails());
		target.setMeansOfTransport(source.getMeansOfTransport());
		target.setMeansOfTransportDetails(source.getMeansOfTransportDetails());
		target.setConnectionNumber(source.getConnectionNumber());
		target.setSeatNumber(source.getSeatNumber());
		target.setWorkEnvironment(source.getWorkEnvironment());

		target.setGatheringType(source.getGatheringType());
		target.setGatheringDetails(source.getGatheringDetails());
		target.setHabitationType(source.getHabitationType());
		target.setHabitationDetails(source.getHabitationDetails());

		return target;
	}

	//fillOrBuildPersonTravelHistoryEntity
	public PersonTravelHistory fillOrBuildPersonTravelHistoryEntity(PersonTravelHistoryDto source, PersonTravelHistory target, boolean checkChangeDate) {
		if (source == null) {
			return null;
		}

		target = DtoHelper.fillOrBuildEntity(source, target, PersonTravelHistory::new, checkChangeDate);

		target.setTravelPeriodType(source.getTravelPeriodType());
		target.setDateFrom(source.getDateFrom());
		target.setDateTo(source.getDateTo());
		target.setVillage(source.getVillage());
		target.setSubDistrict(communityService.getByReferenceDto(source.getSubDistrict()));
		target.setDistrict(districtService.getByReferenceDto(source.getDistrict()));
		target.setRegion(regionService.getByReferenceDto(source.getRegion()));

		return target;
	}

	public ContaminationSource fillOrBuildContaminationSourceEntity(ContaminationSourceDto source, ContaminationSource target, boolean checkChangeDate) {
		if (source == null) {
			return null;
		}

		target = DtoHelper.fillOrBuildEntity(source, target, ContaminationSource::new, checkChangeDate);

		target.setContaminationType(source.getContaminationType());
		target.setName(source.getName());
		target.setLongitude(source.getLongitude());
		target.setLatitude(source.getLatitude());
		target.setType(source.getType());
		target.setSource(source.getSource());
		target.setTreatedWithAbate(source.getTreatedWithAbate());
		target.setAbateTreatmentDate(source.getAbateTreatmentDate());

		return target;
	}

	public ContainmentMeasure fillOrBuildContainmentMeasureEntity(ContainmentMeasureDto source, ContainmentMeasure target, boolean checkChangeDate) {
		if (source == null) {
			return null;
		}

		target = DtoHelper.fillOrBuildEntity(source, target, ContainmentMeasure::new, checkChangeDate);

		target.setLocationOfWorm(source.getLocationOfWorm());
		target.setDateWormDetectedEmergence(source.getDateWormDetectedEmergence());
		target.setDateWormDetectBySupervisor(source.getDateWormDetectBySupervisor());
		target.setDateConfirmed(source.getDateConfirmed());
		target.setDateOfGuineaWormExpelled(source.getDateOfGuineaWormExpelled());
		target.setRegularBandaging(source.getRegularBandaging());
		target.setCompletelyExtracted(source.getCompletelyExtracted());

		return target;
	}


	public static EpiDataDto toDto(EpiData epiData) {

		if (epiData == null) {
			return null;
		}

		EpiDataDto target = new EpiDataDto();
		EpiData source = epiData;

		DtoHelper.fillDto(target, source);

		target.setExposureDetailsKnown(source.getExposureDetailsKnown());
		target.setActivityAsCaseDetailsKnown(source.getActivityAsCaseDetailsKnown());
		target.setRecentTravelOutbreak(source.getRecentTravelOutbreak());
		target.setContactSimilarOutbreak(source.getContactSimilarOutbreak());
		target.setContactSickAnimals(source.getContactSickAnimals());
		target.setContactWithSourceCaseKnown(source.getContactWithSourceCaseKnown());
		target.setHighTransmissionRiskArea(source.getHighTransmissionRiskArea());
		target.setLargeOutbreaksArea(source.getLargeOutbreaksArea());
		target.setAreaInfectedAnimals(source.getAreaInfectedAnimals());
		target.setDisease(source.getDisease());
		target.setPreviouslyVaccinatedAgainstInfluenza(source.getPreviouslyVaccinatedAgainstInfluenza());
		target.setYearOfVaccination(source.getYearOfVaccination());
		target.setPlacesVisitedPastSevenDays(source.getPlacesVisitedPastSevenDays());
		target.setVistedPlacesConfirmedPandemic(source.getVistedPlacesConfirmedPandemic());
		target.setRiskFactorsSevereDisease(source.getRiskFactorsSevereDisease());
		target.setOtherSpecify(source.getOtherSpecify());
		target.setIntlTravel(source.getIntlTravel());
		target.setSpecifyCountries(source.getSpecifyCountries());
		target.setDateOfDeparture(source.getDateOfDeparture());
		target.setDateOfArrival(source.getDateOfArrival());
		target.setDomesticTravel(source.getDomesticTravel());
		target.setSpecifyLocation(source.getSpecifyLocation());
		target.setDateOfDeparture2(source.getDateOfDeparture2());
		target.setDateOfArrival2(source.getDateOfArrival2());
		target.setContactIllPerson(source.getContactIllPerson());
		target.setContactDate(source.getContactDate());
		target.setSpecifyIllness(source.getSpecifyIllness());
		target.setNameOfAffectedPerson(source.getNameOfAffectedPerson());
		target.setNameOfAffectedPerson2(source.getNameOfAffectedPerson2());
		target.setNameOfAffectedPerson3(source.getNameOfAffectedPerson3());
		target.setNameOfAffectedPerson4(source.getNameOfAffectedPerson4());
		target.setTelNo(source.getTelNo());
		target.setTelNo2(source.getTelNo2());
		target.setTelNo3(source.getTelNo3());
		target.setTelNo4(source.getTelNo4());
		target.setDateTime(source.getDateTime());
		target.setDateTime2(source.getDateTime2());
		target.setDateTime3(source.getDateTime3());
		target.setDateTime4(source.getDateTime4());
		target.setAge(source.getAge());
		target.setAge2(source.getAge2());
		target.setAge3(source.getAge3());
		target.setAge4(source.getAge4());
		target.setSuspectedFood(source.getSuspectedFood());
		target.setDateConsumed(source.getDateConsumed());
		target.setFoodSource(source.getFoodSource());
		target.setEventType(source.getEventType());
		target.setEventOtherSpecify(source.getEventOtherSpecify());
		target.setBreakfast(source.getBreakfast());
		target.setTotalNoPersons(source.getTotalNoPersons());
		target.setFoodConsumed(source.getFoodConsumed());
		target.setSourceOfFood(source.getSourceOfFood());
		target.setConsumedAtPlace(source.getConsumedAtPlace());
		target.setLunch(source.getLunch());
		target.setTotalNoPersonsL1(source.getTotalNoPersonsL1());
		target.setFoodConsumedL1(source.getFoodConsumedL1());
		target.setSourceOfFoodL1(source.getSourceOfFoodL1());
		target.setConsumedAtPlaceL1(source.getConsumedAtPlaceL1());
		target.setSupper(source.getSupper());
		target.setTotalNoPersonsS1(source.getTotalNoPersonsS1());
		target.setFoodConsumedS1(source.getFoodConsumedS1());
		target.setSourceOfFoodsS1(source.getSourceOfFoodsS1());
		target.setConsumedAtPlaceS1(source.getConsumedAtPlaceS1());

		target.setBreakfast2(source.getBreakfast2());
		target.setTotalNoPersons2(source.getTotalNoPersons2());
		target.setFoodConsumed2(source.getFoodConsumed2());
		target.setSourceOfFood2(source.getSourceOfFood2());
		target.setConsumedAtPlace2(source.getConsumedAtPlace2());
		target.setLunchL2(source.getLunchL2());
		target.setTotalNoPersonsL2(source.getTotalNoPersonsL2());
		target.setFoodConsumedL2(source.getFoodConsumedL2());
		target.setSourceOfFoodL2(source.getSourceOfFoodL2());
		target.setConsumedAtPlaceL2(source.getConsumedAtPlaceL2());
		target.setSupperS2(source.getSupperS2());
		target.setTotalNoPersonsS2(source.getTotalNoPersonsS2());
		target.setFoodConsumedS2(source.getFoodConsumedS2());
		target.setSourceOfFoodS2(source.getSourceOfFoodS2());
		target.setConsumedAtPlaceS2(source.getConsumedAtPlaceS2());
		target.setBreakfast3(source.getBreakfast3());
		target.setTotalNoPersons3(source.getTotalNoPersons3());
		target.setFoodConsumed3(source.getFoodConsumed3());
		target.setSourceOfFood3(source.getSourceOfFood3());
		target.setConsumedAtPlace3(source.getConsumedAtPlace3());
		target.setLunchL3(source.getLunchL3());
		target.setTotalNoPersonsL3(source.getTotalNoPersonsL3());
		target.setFoodConsumedL3(source.getFoodConsumedL3());
		target.setSourceOfFoodL3(source.getSourceOfFoodL3());
		target.setConsumedAtPlaceL3(source.getConsumedAtPlaceL3());
		target.setSupperS3(source.getSupperS3());
		target.setTotalNoPersonsS3(source.getTotalNoPersonsS3());
		target.setFoodConsumedS3(source.getFoodConsumedS3());
		target.setSourceOfFoodS3(source.getSourceOfFoodS3());
		target.setConsumedAtPlaceS3(source.getConsumedAtPlaceS3());

		List<ExposureDto> exposureDtos = new ArrayList<>();
		for (Exposure exposure : source.getExposures()) {
			ExposureDto exposureDto = toExposureDto(exposure);
			exposureDtos.add(exposureDto);
		}
		target.setExposures(exposureDtos);

		List<ActivityAsCaseDto> activityAsCaseDtos = new ArrayList<>();
		for (ActivityAsCase activityAsCase : source.getActivitiesAsCase()) {
			ActivityAsCaseDto activityAsCaseDto = toActivityAsCaseDto(activityAsCase);
			activityAsCaseDtos.add(activityAsCaseDto);
		}
		target.setActivitiesAsCase(activityAsCaseDtos);
		target.setChildComeInContactWithSymptoms(source.getChildComeInContactWithSymptoms());

		List<PersonTravelHistoryDto> personTravelHistoryDtos = new ArrayList<>();
		for (PersonTravelHistory personTravelHistory : source.getPersonTravelHistories()) {
			PersonTravelHistoryDto personTravelHistoryDto = toPersonTravelHistoryDto(personTravelHistory);
			personTravelHistoryDtos.add(personTravelHistoryDto);
		}
		target.setPersonTravelHistories(personTravelHistoryDtos);

		List<ContaminationSourceDto> contaminationSourceDtos = new ArrayList<>();
		for (ContaminationSource contaminationSource : source.getContaminationSources()) {
			ContaminationSourceDto contaminationSourceDto = toContaminationSourceDto(contaminationSource);
			contaminationSourceDtos.add(contaminationSourceDto);
		}
		target.setContaminationSources(contaminationSourceDtos);

		List<ContainmentMeasureDto> containmentMeasureDtos = new ArrayList<>();
		for (ContainmentMeasure containmentMeasure : source.getContainmentMeasures()) {
			ContainmentMeasureDto containmentMeasureDto = toContainmentMeasureDto(containmentMeasure);
			containmentMeasureDtos.add(containmentMeasureDto);
		}
		target.setContainmentMeasures(containmentMeasureDtos);



		target.setPatientTravelledTwoWeeksPrior(source.getPatientTravelledTwoWeeksPrior());
		target.setPatientTravelledInCountryOne(source.getPatientTravelledInCountryOne());
		target.setPatientTravelledInCountryTwo(source.getPatientTravelledInCountryTwo());
		target.setPatientTravelledInCountryThree(source.getPatientTravelledInCountryThree());
		target.setPatientTravelledInternationalOne(source.getPatientTravelledInternationalOne());
		target.setPatientTravelledInternationalTwo(source.getPatientTravelledInternationalTwo());
		target.setPatientTravelledInternationalThree(source.getPatientTravelledInternationalThree());
		target.setPatientTravelledInternationalFour(source.getPatientTravelledInternationalFour());
		target.setPatientVisitedHealthCareFacility(source.getPatientVisitedHealthCareFacility());
		target.setPatientCloseContactWithARI(source.getPatientCloseContactWithARI());
		target.setPatientCloseContactWithARIContactSettings(source.getPatientCloseContactWithARIContactSettings());
		target.setPatientContactWithConfirmedCase(source.getPatientContactWithConfirmedCase());
		target.setPatientContactWithConfirmedCaseExposureLocations(source.getPatientContactWithConfirmedCaseExposureLocations());
		target.setPatientContactWithConfirmedCaseExposureLocationCityCountry(source.getPatientContactWithConfirmedCaseExposureLocationCityCountry());
		target.setExposedToRiskFactor(source.getExposedToRiskFactor());
		target.setWaterUsedByPatientAfterExposure(source.getWaterUsedByPatientAfterExposure());
		target.setReceivedHealthEducation(source.getReceivedHealthEducation());
		target.setPatientEnteredWaterSource(source.getPatientEnteredWaterSource());
		target.setPlaceManaged(source.getPlaceManaged());

		return target;
	}

	public static ExposureDto toExposureDto(Exposure source) {

		if (source == null) {
			return null;
		}

		ExposureDto target = new ExposureDto();

		DtoHelper.fillDto(target, source);

		target.setAnimalCondition(source.getAnimalCondition());
		target.setTypeOfAnimal(source.getTypeOfAnimal());
		target.setTypeOfAnimalDetails(source.getTypeOfAnimalDetails());
		target.setAnimalContactType(source.getAnimalContactType());
		target.setAnimalContactTypeDetails(source.getAnimalContactTypeDetails());
		target.setAnimalMarket(source.getAnimalMarket());
		target.setAnimalVaccinated(source.getAnimalVaccinated());
		target.setContactToBodyFluids(source.getContactToBodyFluids());
		target.setContactToCase(ContactFacadeEjb.toReferenceDto(source.getContactToCase()));
		target.setDeceasedPersonIll(source.getDeceasedPersonIll());
		target.setDeceasedPersonName(source.getDeceasedPersonName());
		target.setDeceasedPersonRelation(source.getDeceasedPersonRelation());
		target.setDescription(source.getDescription());
		target.setEatingRawAnimalProducts(source.getEatingRawAnimalProducts());
		target.setEndDate(source.getEndDate());
		target.setExposureType(source.getExposureType());
		target.setExposureTypeDetails(source.getExposureTypeDetails());
		target.setGatheringDetails(source.getGatheringDetails());
		target.setGatheringType(source.getGatheringType());
		target.setHabitationDetails(source.getHabitationDetails());
		target.setHabitationType(source.getHabitationType());
		target.setHandlingAnimals(source.getHandlingAnimals());
		target.setHandlingSamples(source.getHandlingSamples());
		target.setIndoors(source.getIndoors());
		target.setLocation(LocationFacadeEjb.toDto(source.getLocation()));
		target.setLongFaceToFaceContact(source.getLongFaceToFaceContact());
		target.setOtherProtectiveMeasures(source.getOtherProtectiveMeasures());
		target.setProtectiveMeasuresDetails(source.getProtectiveMeasuresDetails());
		target.setOutdoors(source.getOutdoors());
		target.setPercutaneous(source.getPercutaneous());
		target.setPhysicalContactDuringPreparation(source.getPhysicalContactDuringPreparation());
		target.setPhysicalContactWithBody(source.getPhysicalContactWithBody());
		target.setReportingUser(UserFacadeEjb.toReferenceDto(source.getReportingUser()));
		target.setProbableInfectionEnvironment(source.isProbableInfectionEnvironment());
		target.setShortDistance(source.getShortDistance());
		target.setStartDate(source.getStartDate());
		target.setWearingMask(source.getWearingMask());
		target.setWearingPpe(source.getWearingPpe());
		target.setTypeOfPlace(source.getTypeOfPlace());
		target.setTypeOfPlaceDetails(source.getTypeOfPlaceDetails());
		target.setMeansOfTransport(source.getMeansOfTransport());
		target.setMeansOfTransportDetails(source.getMeansOfTransportDetails());
		target.setConnectionNumber(source.getConnectionNumber());
		target.setSeatNumber(source.getSeatNumber());
		target.setWorkEnvironment(source.getWorkEnvironment());
		target.setBodyOfWater(source.getBodyOfWater());
		target.setWaterSource(source.getWaterSource());
		target.setWaterSourceDetails(source.getWaterSourceDetails());
		target.setProphylaxis(source.getProphylaxis());
		target.setProphylaxisDate(source.getProphylaxisDate());
		target.setRiskArea(source.getRiskArea());
		target.setExposureRole(source.getExposureRole());
		target.setLargeAttendanceNumber(source.getLargeAttendanceNumber());

		return target;
	}

	public static ActivityAsCaseDto toActivityAsCaseDto(ActivityAsCase source) {

		if (source == null) {
			return null;
		}

		ActivityAsCaseDto target = new ActivityAsCaseDto();

		DtoHelper.fillDto(target, source);

		target.setReportingUser(UserFacadeEjb.toReferenceDto(source.getReportingUser()));
		target.setStartDate(source.getStartDate());
		target.setEndDate(source.getEndDate());
		target.setDescription(source.getDescription());
		target.setActivityAsCaseType(source.getActivityAsCaseType());
		target.setActivityAsCaseTypeDetails(source.getActivityAsCaseTypeDetails());
		target.setLocation(LocationFacadeEjb.toDto(source.getLocation()));
		target.setRole(source.getRole());

		target.setTypeOfPlace(source.getTypeOfPlace());
		target.setTypeOfPlaceDetails(source.getTypeOfPlaceDetails());
		target.setMeansOfTransport(source.getMeansOfTransport());
		target.setMeansOfTransportDetails(source.getMeansOfTransportDetails());
		target.setConnectionNumber(source.getConnectionNumber());
		target.setSeatNumber(source.getSeatNumber());
		target.setWorkEnvironment(source.getWorkEnvironment());

		target.setGatheringType(source.getGatheringType());
		target.setGatheringDetails(source.getGatheringDetails());
		target.setHabitationType(source.getHabitationType());
		target.setHabitationDetails(source.getHabitationDetails());

		return target;
	}

	public EpiData fromDto(EpiDataDto source, boolean checkChangeDate) {

		if (source == null) {
			return null;
		}

		EpiData target = DtoHelper.fillOrBuildEntity(source, service.getByUuid(source.getUuid()), EpiData::new, checkChangeDate);

		target.setExposureDetailsKnown(source.getExposureDetailsKnown());
		target.setActivityAsCaseDetailsKnown(source.getActivityAsCaseDetailsKnown());
		target.setContactWithSourceCaseKnown(source.getContactWithSourceCaseKnown());
		target.setHighTransmissionRiskArea(source.getHighTransmissionRiskArea());
		target.setLargeOutbreaksArea(source.getLargeOutbreaksArea());
		target.setAreaInfectedAnimals(source.getAreaInfectedAnimals());

		List<Exposure> exposures = new ArrayList<>();
		for (ExposureDto exposureDto : source.getExposures()) {
			Exposure exposure = fromExposureDto(exposureDto, checkChangeDate);
			exposure.setEpiData(target);
			exposures.add(exposure);
		}
		if (!DataHelper.equal(target.getExposures(), exposures)) {
			target.setChangeDateOfEmbeddedLists(new Date());
		}
		target.getExposures().clear();
		target.getExposures().addAll(exposures);

		List<ActivityAsCase> activitiesAsCase = new ArrayList<>();
		for (ActivityAsCaseDto activityAsCaseDto : source.getActivitiesAsCase()) {
			ActivityAsCase activityAsCase = fromActivityAsCaseDto(activityAsCaseDto, checkChangeDate);
			activityAsCase.setEpiData(target);
			activitiesAsCase.add(activityAsCase);
		}
		if (!DataHelper.equal(target.getActivitiesAsCase(), activitiesAsCase)) {
			target.setChangeDateOfEmbeddedLists(new Date());
		}
		target.getActivitiesAsCase().clear();
		target.getActivitiesAsCase().addAll(activitiesAsCase);

		return target;
	}

	public ActivityAsCase fromActivityAsCaseDto(ActivityAsCaseDto source, boolean checkChangeDate) {

		if (source == null) {
			return null;
		}

		ActivityAsCase target =
				DtoHelper.fillOrBuildEntity(source, activityAsCaseService.getByUuid(source.getUuid()), ActivityAsCase::new, checkChangeDate);

		target.setReportingUser(userService.getByReferenceDto(source.getReportingUser()));
		target.setStartDate(source.getStartDate());
		target.setEndDate(source.getEndDate());
		target.setDescription(source.getDescription());
		target.setActivityAsCaseType(source.getActivityAsCaseType());
		target.setActivityAsCaseTypeDetails(source.getActivityAsCaseTypeDetails());
		target.setLocation(locationFacade.fromDto(source.getLocation(), checkChangeDate));
		target.setRole(source.getRole());

		target.setTypeOfPlace(source.getTypeOfPlace());
		target.setTypeOfPlaceDetails(source.getTypeOfPlaceDetails());
		target.setMeansOfTransport(source.getMeansOfTransport());
		target.setMeansOfTransportDetails(source.getMeansOfTransportDetails());
		target.setConnectionNumber(source.getConnectionNumber());
		target.setSeatNumber(source.getSeatNumber());
		target.setWorkEnvironment(source.getWorkEnvironment());

		target.setGatheringType(source.getGatheringType());
		target.setGatheringDetails(source.getGatheringDetails());
		target.setHabitationType(source.getHabitationType());
		target.setHabitationDetails(source.getHabitationDetails());

		return target;
	}

	public Exposure fromExposureDto(ExposureDto source, boolean checkChangeDate) {

		if (source == null) {
			return null;
		}

		Exposure target = DtoHelper.fillOrBuildEntity(source, exposureService.getByUuid(source.getUuid()), Exposure::new, checkChangeDate);

		target.setAnimalCondition(source.getAnimalCondition());
		target.setTypeOfAnimal(source.getTypeOfAnimal());
		target.setTypeOfAnimalDetails(source.getTypeOfAnimalDetails());
		target.setAnimalContactType(source.getAnimalContactType());
		target.setAnimalContactTypeDetails(source.getAnimalContactTypeDetails());
		target.setAnimalMarket(source.getAnimalMarket());
		target.setAnimalVaccinated(source.getAnimalVaccinated());
		target.setContactToBodyFluids(source.getContactToBodyFluids());
		target.setContactToCase(contactService.getByReferenceDto(source.getContactToCase()));
		target.setDeceasedPersonIll(source.getDeceasedPersonIll());
		target.setDeceasedPersonName(source.getDeceasedPersonName());
		target.setDeceasedPersonRelation(source.getDeceasedPersonRelation());
		target.setDescription(source.getDescription());
		target.setEatingRawAnimalProducts(source.getEatingRawAnimalProducts());
		target.setEndDate(source.getEndDate());
		target.setExposureType(source.getExposureType());
		target.setExposureTypeDetails(source.getExposureTypeDetails());
		target.setGatheringDetails(source.getGatheringDetails());
		target.setGatheringType(source.getGatheringType());
		target.setHabitationDetails(source.getHabitationDetails());
		target.setHabitationType(source.getHabitationType());
		target.setHandlingAnimals(source.getHandlingAnimals());
		target.setHandlingSamples(source.getHandlingSamples());
		target.setIndoors(source.getIndoors());
		target.setLocation(locationFacade.fromDto(source.getLocation(), checkChangeDate));
		target.setLongFaceToFaceContact(source.getLongFaceToFaceContact());
		target.setOtherProtectiveMeasures(source.getOtherProtectiveMeasures());
		target.setProtectiveMeasuresDetails(source.getProtectiveMeasuresDetails());
		target.setOutdoors(source.getOutdoors());
		target.setPercutaneous(source.getPercutaneous());
		target.setPhysicalContactDuringPreparation(source.getPhysicalContactDuringPreparation());
		target.setPhysicalContactWithBody(source.getPhysicalContactWithBody());
		target.setReportingUser(userService.getByReferenceDto(source.getReportingUser()));
		target.setProbableInfectionEnvironment(source.isProbableInfectionEnvironment());
		target.setShortDistance(source.getShortDistance());
		target.setStartDate(source.getStartDate());
		target.setWearingMask(source.getWearingMask());
		target.setWearingPpe(source.getWearingPpe());
		target.setTypeOfPlace(source.getTypeOfPlace());
		target.setTypeOfPlaceDetails(source.getTypeOfPlaceDetails());
		target.setMeansOfTransport(source.getMeansOfTransport());
		target.setMeansOfTransportDetails(source.getMeansOfTransportDetails());
		target.setConnectionNumber(source.getConnectionNumber());
		target.setSeatNumber(source.getSeatNumber());
		target.setWorkEnvironment(source.getWorkEnvironment());
		target.setBodyOfWater(source.getBodyOfWater());
		target.setWaterSource(source.getWaterSource());
		target.setWaterSourceDetails(source.getWaterSourceDetails());
		target.setProphylaxis(source.getProphylaxis());
		target.setProphylaxisDate(source.getProphylaxisDate());
		target.setRiskArea(source.getRiskArea());
		target.setExposureRole(source.getExposureRole());
		target.setLargeAttendanceNumber(source.getLargeAttendanceNumber());

		return target;
	}

	public static PersonTravelHistoryDto toPersonTravelHistoryDto(PersonTravelHistory source) {

		if (source == null) {
			return null;
		}

		PersonTravelHistoryDto target = new PersonTravelHistoryDto();

		DtoHelper.fillDto(target, source);

		target.setTravelPeriodType(source.getTravelPeriodType());
		target.setDateFrom(source.getDateFrom());
		target.setDateTo(source.getDateTo());
		target.setVillage(source.getVillage());
		target.setSubDistrict(CommunityFacadeEjb.toReferenceDto(source.getSubDistrict()));
		target.setDistrict(DistrictFacadeEjb.toReferenceDto(source.getDistrict()));
		target.setRegion(RegionFacadeEjb.toReferenceDto(source.getRegion()));

		return target;
	}

	public static ContaminationSourceDto toContaminationSourceDto(ContaminationSource source) {

		if (source == null) {
			return null;
		}

		ContaminationSourceDto target = new ContaminationSourceDto();

		DtoHelper.fillDto(target, source);
		target.setName(source.getName());
		target.setContaminationType(source.getContaminationType());
		target.setLongitude(source.getLongitude());
		target.setLatitude(source.getLatitude());
		target.setType(source.getType());
		target.setSource(source.getSource());
		target.setTreatedWithAbate(source.getTreatedWithAbate());
		target.setAbateTreatmentDate(source.getAbateTreatmentDate());

		return target;
	}

	public static ContainmentMeasureDto toContainmentMeasureDto(ContainmentMeasure source) {

		if (source == null) {
			return null;
		}

		ContainmentMeasureDto target = new ContainmentMeasureDto();

		DtoHelper.fillDto(target, source);
		target.setLocationOfWorm(source.getLocationOfWorm());
		target.setDateWormDetectedEmergence(source.getDateWormDetectedEmergence());
		target.setDateWormDetectBySupervisor(source.getDateWormDetectBySupervisor());
		target.setDateConfirmed(source.getDateConfirmed());
		target.setDateOfGuineaWormExpelled(source.getDateOfGuineaWormExpelled());
		target.setRegularBandaging(source.getRegularBandaging());
		target.setCompletelyExtracted(source.getCompletelyExtracted());

		return target;
	}

	@LocalBean
	@Stateless
	public static class EpiDataFacadeEjbLocal extends EpiDataFacadeEjb {
	}
}
