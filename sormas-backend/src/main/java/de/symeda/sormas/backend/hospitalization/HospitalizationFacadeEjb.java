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
package de.symeda.sormas.backend.hospitalization;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import de.symeda.sormas.api.hospitalization.HospitalizationDto;
import de.symeda.sormas.api.hospitalization.HospitalizationFacade;
import de.symeda.sormas.api.hospitalization.PreviousHospitalizationDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.caze.CaseService;
import de.symeda.sormas.backend.infrastructure.community.CommunityFacadeEjb;
import de.symeda.sormas.backend.infrastructure.community.CommunityService;
import de.symeda.sormas.backend.infrastructure.district.DistrictFacadeEjb;
import de.symeda.sormas.backend.infrastructure.district.DistrictService;
import de.symeda.sormas.backend.infrastructure.facility.FacilityFacadeEjb;
import de.symeda.sormas.backend.infrastructure.facility.FacilityService;
import de.symeda.sormas.backend.infrastructure.region.RegionFacadeEjb;
import de.symeda.sormas.backend.infrastructure.region.RegionService;
import de.symeda.sormas.backend.util.DtoHelper;

@Stateless(name = "HospitalizationFacade")
public class HospitalizationFacadeEjb implements HospitalizationFacade {

	@EJB
	private RegionService regionService;
	@EJB
	private DistrictService districtService;
	@EJB
	private CommunityService communityService;
	@EJB
	private FacilityService facilityService;
	@EJB
	private PreviousHospitalizationService previousHospitalizationService;

	@EJB
	private HospitalizationService service;
	@EJB
	private PreviousHospitalizationService prevHospService;
	@EJB
	private CaseService caseService;


	public Hospitalization fillOrBuildEntity(HospitalizationDto source, Hospitalization target, boolean checkChangeDate) {
		if (source == null) {
			return null;
		}

		target = DtoHelper.fillOrBuildEntity(source, target, Hospitalization::new, checkChangeDate);

		target.setAdmittedToHealthFacility(source.getAdmittedToHealthFacility());
		target.setAdmittedToHealthFacilityNew(source.getAdmittedToHealthFacilityNew());
		target.setAdmissionDate(source.getAdmissionDate());
		target.setDischargeDate(source.getDischargeDate());
		target.setHospitalizedPreviously(source.getHospitalizedPreviously());
		target.setIsolated(source.getIsolated());
		target.setIsolationDate(source.getIsolationDate());
		target.setLeftAgainstAdvice(source.getLeftAgainstAdvice());
		target.setHospitalizationReason(source.getHospitalizationReason());
		target.setOtherHospitalizationReason(source.getOtherHospitalizationReason());
		target.setHealthFacilityRecordNumber(source.getHealthFacilityRecordNumber());

		List<PreviousHospitalization> previousHospitalizations = new ArrayList<>();
		for (PreviousHospitalizationDto prevDto : source.getPreviousHospitalizations()) {
			//prevHospitalization will be present in 1st level cache based on #10214
			PreviousHospitalization prevHosp = previousHospitalizationService.getByUuid(prevDto.getUuid());
			prevHosp = fillOrBuildEntity(prevDto, prevHosp, checkChangeDate);
			prevHosp.setHospitalization(target);
			previousHospitalizations.add(prevHosp);
		}
		if (!DataHelper.equalContains(target.getPreviousHospitalizations(), previousHospitalizations)) {
			// note: DataHelper.equal does not work here, because target.getAddresses may be a PersistentBag when using lazy loading
			target.setChangeDateOfEmbeddedLists(new Date());
		}
		target.getPreviousHospitalizations().clear();
		target.getPreviousHospitalizations().addAll(previousHospitalizations);
		target.setIntensiveCareUnit(source.getIntensiveCareUnit());
		target.setIntensiveCareUnitStart(source.getIntensiveCareUnitStart());
		target.setIntensiveCareUnitEnd(source.getIntensiveCareUnitEnd());
		target.setPatientConditionOnAdmission(source.getPatientConditionOnAdmission());
		target.setDescription(source.getDescription());
		target.setHealthFacilityRecordNumber(source.getHealthFacilityRecordNumber());
		target.setDiseaseOnsetDate(source.getDiseaseOnsetDate());
		target.setPatientHospitalizedOrDetained(source.getPatientHospitalizedOrDetained());
		target.setNotifyDistrictDate(source.getNotifyDistrictDate());
		target.setDateFirstSeen(source.getDateFirstSeen());
		target.setTerminationDateHospitalStay(source.getTerminationDateHospitalStay());
		target.setPlace(source.getPlace());
		target.setDurationMonths(source.getDurationMonths());
		target.setDurationDays(source.getDurationDays());
		target.setPlace2(source.getPlace2());
		target.setDurationMonths2(source.getDurationMonths2());
		target.setDurationDays2(source.getDurationDays2());
		target.setInvestigatorName(source.getInvestigatorName());
		target.setInvestigatorTitle(source.getInvestigatorTitle());
		target.setInvestigatorUnit(source.getInvestigatorUnit());
		target.setInvestigatorAddress(source.getInvestigatorAddress());
		target.setInvestigatorTel(source.getInvestigatorTel());
		target.setIntensiveCareUnitEnd(source.getIntensiveCareUnitEnd());
		target.setWasPatientAdmitted(source.getWasPatientAdmitted());
		target.setHospitalRecordNumber(source.getHospitalRecordNumber());
		target.setPatientVentilated(source.getPatientVentilated());
		target.setDateFormSentToDistrict(source.getDateFormSentToDistrict());
		target.setSoughtMedicalAttention(source.getSoughtMedicalAttention());
		target.setNameOfFacility(source.getNameOfFacility());
		target.setLocationAddress(source.getLocationAddress());
		target.setDateOfVisitHospital(source.getDateOfVisitHospital());
		target.setPhysicianName(source.getPhysicianName());
		target.setPhysicianNumber(source.getPhysicianNumber());
		target.setLabTestConducted(source.getLabTestConducted());
		target.setTypeOfSample(source.getTypeOfSample());
		target.setAgentIdentified(source.getAgentIdentified());
		target.setSymptomsSelected(source.getSymptomsSelected());
		target.setOtherSymptomSelected(source.getOtherSymptomSelected());
		target.setOnsetOfSymptomDatetime(source.getOnsetOfSymptomDatetime());
		target.setSymptomsOngoing(source.getSymptomsOngoing());
		target.setDurationHours(source.getDurationHours());
		target.setSelectInpatientOutpatient(source.getSelectInpatientOutpatient());
		target.setReceptionDate(source.getReceptionDate());
		target.setMemberFamilyHelpingPatient(source.getMemberFamilyHelpingPatient());
		target.setDateOfDeath(source.getDateOfDeath());


		return target;
	}

	public PreviousHospitalization fillOrBuildEntity(PreviousHospitalizationDto source, PreviousHospitalization target, boolean checkChangeDate) {
		if (source == null) {
			return null;
		}

		target = DtoHelper.fillOrBuildEntity(source, target, PreviousHospitalization::new, checkChangeDate);

		if (!DataHelper.isSame(target.getRegion(), source.getRegion())) {
			target.setRegion(regionService.getByReferenceDto(source.getRegion()));
		}

		target.setAdmittedToHealthFacility(source.getAdmittedToHealthFacility());
		target.setAdmissionDate(source.getAdmissionDate());
		target.setDischargeDate(source.getDischargeDate());

		target.setRegion(regionService.getByReferenceDto(source.getRegion()));

		target.setDistrict(districtService.getByReferenceDto(source.getDistrict()));
		target.setCommunity(communityService.getByReferenceDto(source.getCommunity()));
		target.setHealthFacility(facilityService.getByReferenceDto(source.getHealthFacility()));
		target.setHealthFacilityDetails(source.getHealthFacilityDetails());
		target.setIsolated(source.getIsolated());
		target.setIsolationDate(source.getIsolationDate());
		target.setDescription(source.getDescription());
		target.setHospitalizationReason(source.getHospitalizationReason());
		target.setOtherHospitalizationReason(source.getOtherHospitalizationReason());
		target.setIntensiveCareUnit(source.getIntensiveCareUnit());
		target.setIntensiveCareUnitStart(source.getIntensiveCareUnitStart());
		target.setIntensiveCareUnitEnd(source.getIntensiveCareUnitEnd());
		target.setIntensiveCareUnitEnd(source.getIntensiveCareUnitEnd());
		target.setWasPatientAdmitted(source.getWasPatientAdmitted());

		return target;
	}

	public static HospitalizationDto toDto(Hospitalization hospitalization) {

		if (hospitalization == null) {
			return null;
		}

		HospitalizationDto target = new HospitalizationDto();
		Hospitalization source = hospitalization;

		DtoHelper.fillDto(target, source);

		target.setAdmittedToHealthFacility(source.getAdmittedToHealthFacility());
		target.setAdmittedToHealthFacilityNew(source.getAdmittedToHealthFacilityNew());
		target.setAdmissionDate(source.getAdmissionDate());
		target.setDischargeDate(source.getDischargeDate());
		target.setHospitalizedPreviously(source.getHospitalizedPreviously());
		target.setIsolated(source.getIsolated());
		target.setIsolationDate(source.getIsolationDate());
		target.setLeftAgainstAdvice(source.getLeftAgainstAdvice());
		target.setHospitalizationReason(source.getHospitalizationReason());
		target.setOtherHospitalizationReason(source.getOtherHospitalizationReason());
		target.setHealthFacilityRecordNumber(source.getHealthFacilityRecordNumber());

		List<PreviousHospitalizationDto> previousHospitalizations = new ArrayList<>();
		for (PreviousHospitalization prevDto : source.getPreviousHospitalizations()) {
			PreviousHospitalizationDto prevHosp = toDto(prevDto);
			previousHospitalizations.add(prevHosp);
		}
		target.setPreviousHospitalizations(previousHospitalizations);
		target.setIntensiveCareUnit(source.getIntensiveCareUnit());
		target.setIntensiveCareUnitStart(source.getIntensiveCareUnitStart());
		target.setIntensiveCareUnitEnd(source.getIntensiveCareUnitEnd());
		target.setPatientConditionOnAdmission(source.getPatientConditionOnAdmission());
		target.setDescription(source.getDescription());
		target.setDiseaseOnsetDate(source.getDiseaseOnsetDate());
		target.setPatientHospitalizedOrDetained(source.getPatientHospitalizedOrDetained());
		target.setPlace(source.getPlace());
		target.setDurationMonths(source.getDurationMonths());
		target.setDurationDays(source.getDurationDays());
		target.setPlace2(source.getPlace2());
		target.setDurationMonths2(source.getDurationMonths2());
		target.setDurationDays2(source.getDurationDays2());
		target.setInvestigatorName(source.getInvestigatorName());
		target.setInvestigatorTitle(source.getInvestigatorTitle());
		target.setInvestigatorUnit(source.getInvestigatorUnit());
		target.setInvestigatorAddress(source.getInvestigatorAddress());
		target.setInvestigatorTel(source.getInvestigatorTel());
		target.setNotifyDistrictDate(source.getNotifyDistrictDate());
		target.setDateFirstSeen(source.getDateFirstSeen());
		target.setTerminationDateHospitalStay(source.getTerminationDateHospitalStay());
		target.setSeenAtAHealthFacility(source.getSeenAtAHealthFacility());
		target.setWasPatientAdmitted(source.getWasPatientAdmitted());
		target.setHospitalRecordNumber(source.getHospitalRecordNumber());
		target.setPatientVentilated(source.getPatientVentilated());
		target.setDateFormSentToDistrict(source.getDateFormSentToDistrict());
		target.setSoughtMedicalAttention(source.getSoughtMedicalAttention());
		target.setNameOfFacility(source.getNameOfFacility());
		target.setLocationAddress(source.getLocationAddress());
		target.setDateOfVisitHospital(source.getDateOfVisitHospital());
		target.setPhysicianName(source.getPhysicianName());
		target.setPhysicianNumber(source.getPhysicianNumber());
		target.setLabTestConducted(source.getLabTestConducted());
		target.setTypeOfSample(source.getTypeOfSample());
		target.setAgentIdentified(source.getAgentIdentified());
		target.setSymptomsSelected(source.getSymptomsSelected());
		target.setOtherSymptomSelected(source.getOtherSymptomSelected());
		target.setOnsetOfSymptomDatetime(source.getOnsetOfSymptomDatetime());
		target.setSymptomsOngoing(source.getSymptomsOngoing());
		target.setDurationHours(source.getDurationHours());
		target.setSelectInpatientOutpatient(source.getSelectInpatientOutpatient());
		target.setReceptionDate(source.getReceptionDate());
		target.setMemberFamilyHelpingPatient(source.getMemberFamilyHelpingPatient());
		target.setDateOfDeath(source.getDateOfDeath());


		return target;
	}

	public static PreviousHospitalizationDto toDto(PreviousHospitalization source) {

		if (source == null) {
			return null;
		}

		PreviousHospitalizationDto target = new PreviousHospitalizationDto();

		DtoHelper.fillDto(target, source);

		target.setAdmittedToHealthFacility(source.getAdmittedToHealthFacility());
		target.setAdmissionDate(source.getAdmissionDate());
		target.setDischargeDate(source.getDischargeDate());
		target.setRegion(RegionFacadeEjb.toReferenceDto(source.getRegion()));
		target.setDistrict(DistrictFacadeEjb.toReferenceDto(source.getDistrict()));
		target.setCommunity(CommunityFacadeEjb.toReferenceDto(source.getCommunity()));
		target.setHealthFacility(FacilityFacadeEjb.toReferenceDto(source.getHealthFacility()));
		target.setHealthFacilityDetails(source.getHealthFacilityDetails());
		target.setIsolated(source.getIsolated());
		target.setIsolationDate(source.getIsolationDate());
		target.setDescription(source.getDescription());
		target.setHospitalizationReason(source.getHospitalizationReason());
		target.setOtherHospitalizationReason(source.getOtherHospitalizationReason());
		target.setIntensiveCareUnit(source.getIntensiveCareUnit());
		target.setIntensiveCareUnitStart(source.getIntensiveCareUnitStart());
		target.setIntensiveCareUnitEnd(source.getIntensiveCareUnitEnd());
		target.setHealthFacilityRecordNumber(source.getHealthFacilityRecordNumber());

		target.setSeenAtAHealthFacility(source.getSeenAtAHealthFacility());
		target.setWasPatientAdmitted(source.getWasPatientAdmitted());
		return target;
	}

	public Hospitalization fromDto(HospitalizationDto source, boolean checkChangeDate) {

		if (source == null) {
			return null;
		}

		Hospitalization target = DtoHelper.fillOrBuildEntity(source, service.getByUuid(source.getUuid()), Hospitalization::new, checkChangeDate);

		target.setAdmittedToHealthFacility(source.getAdmittedToHealthFacility());
		target.setAdmissionDate(source.getAdmissionDate());
		target.setDischargeDate(source.getDischargeDate());
		target.setHospitalizedPreviously(source.getHospitalizedPreviously());
		target.setIsolated(source.getIsolated());
		target.setIsolationDate(source.getIsolationDate());
		target.setLeftAgainstAdvice(source.getLeftAgainstAdvice());
		target.setHospitalizationReason(source.getHospitalizationReason());
		target.setOtherHospitalizationReason(source.getOtherHospitalizationReason());
		target.setHealthFacilityRecordNumber(source.getHealthFacilityRecordNumber());

		List<PreviousHospitalization> previousHospitalizations = new ArrayList<>();
		for (PreviousHospitalizationDto prevDto : source.getPreviousHospitalizations()) {
			PreviousHospitalization prevHosp = fromDto(prevDto, checkChangeDate);
			prevHosp.setHospitalization(target);
			previousHospitalizations.add(prevHosp);
		}
		if (!DataHelper.equal(target.getPreviousHospitalizations(), previousHospitalizations)) {
			target.setChangeDateOfEmbeddedLists(new Date());
		}
		target.getPreviousHospitalizations().clear();
		target.getPreviousHospitalizations().addAll(previousHospitalizations);
		target.setIntensiveCareUnit(source.getIntensiveCareUnit());
		target.setIntensiveCareUnitStart(source.getIntensiveCareUnitStart());
		target.setIntensiveCareUnitEnd(source.getIntensiveCareUnitEnd());
		target.setPatientConditionOnAdmission(source.getPatientConditionOnAdmission());
		target.setDescription(source.getDescription());
		target.setHealthFacilityRecordNumber(source.getHealthFacilityRecordNumber());

		return target;
	}

	public PreviousHospitalization fromDto(PreviousHospitalizationDto source, boolean checkChangeDate) {

		if (source == null) {
			return null;
		}

		PreviousHospitalization target =
				DtoHelper.fillOrBuildEntity(source, prevHospService.getByUuid(source.getUuid()), PreviousHospitalization::new, checkChangeDate);

		target.setAdmittedToHealthFacility(source.getAdmittedToHealthFacility());
		target.setAdmissionDate(source.getAdmissionDate());
		target.setDischargeDate(source.getDischargeDate());
		target.setRegion(regionService.getByReferenceDto(source.getRegion()));
		target.setDistrict(districtService.getByReferenceDto(source.getDistrict()));
		target.setCommunity(communityService.getByReferenceDto(source.getCommunity()));
		target.setHealthFacility(facilityService.getByReferenceDto(source.getHealthFacility()));
		target.setHealthFacilityDetails(source.getHealthFacilityDetails());
		target.setIsolated(source.getIsolated());
		target.setIsolationDate(source.getIsolationDate());
		target.setDescription(source.getDescription());
		target.setHospitalizationReason(source.getHospitalizationReason());
		target.setOtherHospitalizationReason(source.getOtherHospitalizationReason());
		target.setIntensiveCareUnit(source.getIntensiveCareUnit());
		target.setIntensiveCareUnitStart(source.getIntensiveCareUnitStart());
		target.setIntensiveCareUnitEnd(source.getIntensiveCareUnitEnd());

		return target;
	}
    @LocalBean
	@Stateless
	public static class HospitalizationFacadeEjbLocal extends HospitalizationFacadeEjb {

	}
}
