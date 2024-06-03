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
import de.symeda.sormas.api.epidata.EpiDataDto;
import de.symeda.sormas.api.epidata.EpiDataFacade;
import de.symeda.sormas.api.exposure.ExposureDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.FacadeHelper;
import de.symeda.sormas.backend.activityascase.ActivityAsCase;
import de.symeda.sormas.backend.activityascase.ActivityAsCaseService;
import de.symeda.sormas.backend.contact.ContactFacadeEjb;
import de.symeda.sormas.backend.contact.ContactService;
import de.symeda.sormas.backend.exposure.Exposure;
import de.symeda.sormas.backend.exposure.ExposureService;
import de.symeda.sormas.backend.location.LocationFacadeEjb;
import de.symeda.sormas.backend.location.LocationFacadeEjb.LocationFacadeEjbLocal;
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

		target.setNameOfVaccine(source.getNameOfVaccine());
		target.setPreviouslyVaccinatedAgainstCovid(source.getPreviouslyVaccinatedAgainstCovid());
		target.setYearOfVaccinationCovid(source.getYearOfVaccinationCovid());
		target.setNameOfVaccineCovid(source.getNameOfVaccineCovid());
		target.setIfYesSpecifySick(source.getIfYesSpecifySick());
		target.setContactDeadAnimals(source.getContactDeadAnimals());
		target.setIfYesSpecifyDead(source.getIfYesSpecifyDead());
		target.setPatientTravelDuringIllness(source.getPatientTravelDuringIllness());
		target.setComm1(source.getComm1());
		target.setHealthCenter1(source.getHealthCenter1());
		target.setCountry1(source.getCountry1());
		target.setComm2(source.getComm2());
		target.setHealthCenter2(source.getHealthCenter2());
		target.setCountry2(source.getCountry2());
		target.setWasPatientHospitalized(source.getWasPatientHospitalized());
		target.setIfYesWhere(source.getIfYesWhere());
		target.setHospitalizedDate1(source.getHospitalizedDate1());
		target.setHospitalizedDate2(source.getHospitalizedDate2());
		target.setDidPatientConsultHealer(source.getDidPatientConsultHealer());
		target.setIfYesNameHealer(source.getIfYesNameHealer());
		target.setCommunity(source.getCommunity());
		target.setCountry(source.getCountry());
		target.setWhenWhereContactTakePlace(source.getWhenWhereContactTakePlace());
		target.setDateOfContact(source.getDateOfContact());
		target.setPatientReceiveTraditionalMedicine(source.getPatientReceiveTraditionalMedicine());
		target.setIfYesExplain(source.getIfYesExplain());
		target.setPatientAttendFuneralCeremonies(source.getPatientAttendFuneralCeremonies());
		target.setPatientTravelAnytimePeriodBeforeIll(source.getPatientTravelAnytimePeriodBeforeIll());
		target.setIfTravelYesWhere(source.getIfTravelYesWhere());
		target.setIfYesStartDate(source.getIfYesStartDate());
		target.setIfYesEndDate(source.getIfYesEndDate());
		target.setPatientContactKnownSuspect(source.getPatientContactKnownSuspect());
		target.setSuspectName(source.getSuspectName());
		target.setIdCase(source.getIdCase());
		target.setDuringContactSuspectCase(source.getDuringContactSuspectCase());
		target.setDateOfDeath(source.getDateOfDeath());
		target.setDateOfLastContactWithSuspectCase(source.getDateOfLastContactWithSuspectCase());
		target.setIfYesWildAnimalLocation(source.getIfYesWildAnimalLocation());
		target.setIfYesWildAnimalDate(source.getIfYesWildAnimalDate());
		target.setSuspectLastName(source.getSuspectLastName());





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


		target.setNameOfVaccine(source.getNameOfVaccine());
		target.setPreviouslyVaccinatedAgainstCovid(source.getPreviouslyVaccinatedAgainstCovid());
		target.setYearOfVaccinationCovid(source.getYearOfVaccinationCovid());
		target.setNameOfVaccineCovid(source.getNameOfVaccineCovid());
		target.setIfYesSpecifySick(source.getIfYesSpecifySick());
		target.setContactDeadAnimals(source.getContactDeadAnimals());
		target.setIfYesSpecifyDead(source.getIfYesSpecifyDead());
		target.setPatientTravelDuringIllness(source.getPatientTravelDuringIllness());
		target.setComm1(source.getComm1());
		target.setHealthCenter1(source.getHealthCenter1());
		target.setCountry1(source.getCountry1());
		target.setComm2(source.getComm2());
		target.setHealthCenter2(source.getHealthCenter2());
		target.setCountry2(source.getCountry2());
		target.setWasPatientHospitalized(source.getWasPatientHospitalized());
		target.setIfYesWhere(source.getIfYesWhere());
		target.setHospitalizedDate1(source.getHospitalizedDate1());
		target.setHospitalizedDate2(source.getHospitalizedDate2());
		target.setDidPatientConsultHealer(source.getDidPatientConsultHealer());
		target.setIfYesNameHealer(source.getIfYesNameHealer());
		target.setCommunity(source.getCommunity());
		target.setCountry(source.getCountry());
		target.setWhenWhereContactTakePlace(source.getWhenWhereContactTakePlace());
		target.setDateOfContact(source.getDateOfContact());
		target.setPatientReceiveTraditionalMedicine(source.getPatientReceiveTraditionalMedicine());
		target.setIfYesExplain(source.getIfYesExplain());
		target.setPatientAttendFuneralCeremonies(source.getPatientAttendFuneralCeremonies());
		target.setPatientTravelAnytimePeriodBeforeIll(source.getPatientTravelAnytimePeriodBeforeIll());
		target.setIfTravelYesWhere(source.getIfTravelYesWhere());
		target.setIfYesStartDate(source.getIfYesStartDate());
		target.setIfYesEndDate(source.getIfYesEndDate());
		target.setPatientContactKnownSuspect(source.getPatientContactKnownSuspect());
		target.setSuspectName(source.getSuspectName());
		target.setIdCase(source.getIdCase());
		target.setDuringContactSuspectCase(source.getDuringContactSuspectCase());
		target.setDateOfDeath(source.getDateOfDeath());
		target.setDateOfLastContactWithSuspectCase(source.getDateOfLastContactWithSuspectCase());
		target.setIfYesWildAnimalLocation(source.getIfYesWildAnimalLocation());
		target.setIfYesWildAnimalDate(source.getIfYesWildAnimalDate());
		target.setSuspectLastName(source.getSuspectLastName());

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

	@LocalBean
	@Stateless
	public static class EpiDataFacadeEjbLocal extends EpiDataFacadeEjb {
	}
}
