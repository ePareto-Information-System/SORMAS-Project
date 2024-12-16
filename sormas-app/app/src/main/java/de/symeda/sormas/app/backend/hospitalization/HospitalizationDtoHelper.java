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

package de.symeda.sormas.app.backend.hospitalization;

import java.util.ArrayList;
import java.util.List;

import de.symeda.sormas.api.PostResponse;
import de.symeda.sormas.api.hospitalization.HospitalizationDto;
import de.symeda.sormas.api.hospitalization.PreviousHospitalizationDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.facility.Facility;
import de.symeda.sormas.app.backend.facility.FacilityDtoHelper;
import de.symeda.sormas.app.backend.location.Location;
import de.symeda.sormas.app.backend.location.LocationDtoHelper;
import de.symeda.sormas.app.backend.region.Community;
import de.symeda.sormas.app.backend.region.CommunityDtoHelper;
import de.symeda.sormas.app.backend.region.District;
import de.symeda.sormas.app.backend.region.DistrictDtoHelper;
import de.symeda.sormas.app.backend.region.Region;
import de.symeda.sormas.app.backend.region.RegionDtoHelper;
import de.symeda.sormas.app.rest.NoConnectionException;
import retrofit2.Call;

public class HospitalizationDtoHelper extends AdoDtoHelper<Hospitalization, HospitalizationDto> {

	private LocationDtoHelper locationDtoHelper = new LocationDtoHelper();

	private PreviousHospitalizationDtoHelper previousHospitalizationDtoHelper;

	public HospitalizationDtoHelper() {
		previousHospitalizationDtoHelper = new PreviousHospitalizationDtoHelper();
	}

	@Override
	protected Class<Hospitalization> getAdoClass() {
		return Hospitalization.class;
	}

	@Override
	protected Class<HospitalizationDto> getDtoClass() {
		return HospitalizationDto.class;
	}

	@Override
	protected Call<List<HospitalizationDto>> pullAllSince(long since, Integer size, String lastSynchronizedUuid)  throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	protected Call<List<HospitalizationDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	protected Call<List<PostResponse>> pushAll(List<HospitalizationDto> hospitalizationDtos) throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	public void fillInnerFromDto(Hospitalization a, HospitalizationDto b) {

		a.setAdmittedToHealthFacility(b.getAdmittedToHealthFacility());
		a.setAdmissionDate(b.getAdmissionDate());
		a.setDischargeDate(b.getDischargeDate());
		a.setIsolated(b.getIsolated());
		a.setIsolationDate(b.getIsolationDate());
		a.setDescription(b.getDescription());
		a.setLeftAgainstAdvice(b.getLeftAgainstAdvice());
		a.setIntensiveCareUnit(b.getIntensiveCareUnit());
		a.setIntensiveCareUnitStart(b.getIntensiveCareUnitStart());
		a.setIntensiveCareUnitEnd(b.getIntensiveCareUnitEnd());
		a.setHospitalizedPreviously(b.getHospitalizedPreviously());
		a.setPatientConditionOnAdmission(b.getPatientConditionOnAdmission());
		a.setHospitalizationReason(b.getHospitalizationReason());
		a.setOtherHospitalizationReason(b.getOtherHospitalizationReason());
		a.setHealthFacilityRecordNumber(b.getHealthFacilityRecordNumber());
		a.setSelectInpatientOutpatient(b.getSelectInpatientOutpatient());
		a.setDateFirstSeen(b.getDateFirstSeen());
		a.setDiseaseOnsetDate(b.getDiseaseOnsetDate());
		a.setNotifyDistrictDate(b.getNotifyDistrictDate());
		a.setDateFormSentToDistrict(b.getDateFormSentToDistrict());
		a.setAdmittedToHealthFacilityNew(b.getAdmittedToHealthFacilityNew());
		a.setMemberFamilyHelpingPatient(b.getMemberFamilyHelpingPatient());
		a.setDateOfDeath(b.getDateOfDeath());


		// It would be better to merge with the existing hospitalizations
		List<PreviousHospitalization> previousHospitalizations = new ArrayList<>();
		if (!b.getPreviousHospitalizations().isEmpty()) {
			for (PreviousHospitalizationDto prevHospDto : b.getPreviousHospitalizations()) {
				PreviousHospitalization prevHosp = previousHospitalizationDtoHelper.fillOrCreateFromDto(null, prevHospDto);
				prevHosp.setHospitalization(a);
				previousHospitalizations.add(prevHosp);
			}
		}
		a.setPreviousHospitalizations(previousHospitalizations);
		a.setHospitalRecordNumber(b.getHospitalRecordNumber());
		a.setPatientVentilated(b.getPatientVentilated());
		a.setDateFormSentToDistrict(b.getDateFormSentToDistrict());
		a.setSeenAtAHealthFacility(b.getSeenAtAHealthFacility());
		a.setWasPatientAdmitted(b.getWasPatientAdmitted());

		a.setTerminationDateHospitalStay(b.getTerminationDateHospitalStay());
		a.setSymptomsSelected(b.getSymptomsSelected());
		a.setOtherSymptomSelected(b.getOtherSymptomSelected());
		a.setOnsetOfSymptomDatetime(b.getOnsetOfSymptomDatetime());
		a.setSymptomsOngoing(b.getSymptomsOngoing());
		a.setDurationHours(b.getDurationHours());
		a.setSoughtMedicalAttention(b.getSoughtMedicalAttention());
		a.setSoughtRegion(DatabaseHelper.getRegionDao().getByReferenceDto(b.getSoughtRegion()));
		a.setSoughtDistrict(DatabaseHelper.getDistrictDao().getByReferenceDto(b.getSoughtDistrict()));
		a.setSoughtCommunity(DatabaseHelper.getCommunityDao().getByReferenceDto(b.getSoughtCommunity()));
		a.setNameOfFacility(DatabaseHelper.getFacilityDao().getByReferenceDto(b.getNameOfFacility()));
		a.setDateOfVisitHospital(b.getDateOfVisitHospital());
		a.setHospitalizationYesNo(b.getHospitalizationYesNo());
		a.setPhysicianName(b.getPhysicianName());
		a.setPhysicianNumber(b.getPhysicianNumber());
		a.setLabTestConducted(b.getLabTestConducted());
		a.setTypeOfSample(b.getTypeOfSample());
		a.setAgentIdentified(b.getAgentIdentified());
		a.setLocationType(locationDtoHelper.fillOrCreateFromDto(a.getLocationType(), b.getLocationType()));

	}

	@Override
	public void fillInnerFromAdo(HospitalizationDto a, Hospitalization b) {

		a.setAdmittedToHealthFacility(b.getAdmittedToHealthFacility());
		a.setAdmissionDate(b.getAdmissionDate());
		a.setDischargeDate(b.getDischargeDate());
		a.setIsolated(b.getIsolated());
		a.setIsolationDate(b.getIsolationDate());
		a.setDescription(b.getDescription());
		a.setLeftAgainstAdvice(b.getLeftAgainstAdvice());
		a.setIntensiveCareUnit(b.getIntensiveCareUnit());
		a.setIntensiveCareUnitStart(b.getIntensiveCareUnitStart());
		a.setIntensiveCareUnitEnd(b.getIntensiveCareUnitEnd());
		a.setHospitalizedPreviously(b.getHospitalizedPreviously());
		a.setPatientConditionOnAdmission(b.getPatientConditionOnAdmission());
		a.setHospitalizationReason(b.getHospitalizationReason());
		a.setOtherHospitalizationReason(b.getOtherHospitalizationReason());
		a.setHealthFacilityRecordNumber(b.getHealthFacilityRecordNumber());
		a.setSelectInpatientOutpatient(b.getSelectInpatientOutpatient());
		a.setDateFirstSeen(b.getDateFirstSeen());
		a.setDiseaseOnsetDate(b.getDiseaseOnsetDate());
		a.setNotifyDistrictDate(b.getNotifyDistrictDate());
		a.setDateFormSentToDistrict(b.getDateFormSentToDistrict());
		a.setAdmittedToHealthFacilityNew(b.getAdmittedToHealthFacilityNew());
		a.setMemberFamilyHelpingPatient(b.getMemberFamilyHelpingPatient());
		a.setDateOfDeath(b.getDateOfDeath());

		List<PreviousHospitalizationDto> previousHospitalizationDtos = new ArrayList<>();
		for (PreviousHospitalization prevHosp : b.getPreviousHospitalizations()) {
			PreviousHospitalizationDto prevHospDto = previousHospitalizationDtoHelper.adoToDto(prevHosp);
			previousHospitalizationDtos.add(prevHospDto);
		}
		a.setPreviousHospitalizations(previousHospitalizationDtos);
		a.setHospitalRecordNumber(b.getHospitalRecordNumber());
		a.setPatientVentilated(b.getPatientVentilated());
		a.setDateFormSentToDistrict(b.getDateFormSentToDistrict());
		a.setSeenAtAHealthFacility(b.getSeenAtAHealthFacility());
		a.setWasPatientAdmitted(b.getWasPatientAdmitted());

		a.setTerminationDateHospitalStay(b.getTerminationDateHospitalStay());
		a.setSymptomsSelected(b.getSymptomsSelected());
		a.setOtherSymptomSelected(b.getOtherSymptomSelected());
		a.setOnsetOfSymptomDatetime(b.getOnsetOfSymptomDatetime());
		a.setSymptomsOngoing(b.getSymptomsOngoing());
		a.setDurationHours(b.getDurationHours());
		a.setSoughtMedicalAttention(b.getSoughtMedicalAttention());
		if (b.getSoughtRegion() != null) {
			Region region = DatabaseHelper.getRegionDao().queryForId(b.getSoughtRegion().getId());
			a.setSoughtRegion(RegionDtoHelper.toReferenceDto(region));
		} else {
			a.setSoughtRegion(null);
		}

		if (b.getSoughtDistrict() != null) {
			District district = DatabaseHelper.getDistrictDao().queryForId(b.getSoughtDistrict().getId());
			a.setSoughtDistrict(DistrictDtoHelper.toReferenceDto(district));
		} else {
			a.setSoughtDistrict(null);
		}

		if (b.getSoughtCommunity() != null) {
			Community community = DatabaseHelper.getCommunityDao().queryForId(b.getSoughtCommunity().getId());
			a.setSoughtCommunity(CommunityDtoHelper.toReferenceDto(community));
		} else {
			a.setSoughtCommunity(null);
		}

		if (b.getNameOfFacility() != null) {
			Facility facility = DatabaseHelper.getFacilityDao().queryForId(b.getNameOfFacility().getId());
			a.setNameOfFacility(FacilityDtoHelper.toReferenceDto(facility));
		} else {
			a.setNameOfFacility(null);
		}
		a.setDateOfVisitHospital(b.getDateOfVisitHospital());
		a.setHospitalizationYesNo(b.getHospitalizationYesNo());
		a.setPhysicianName(b.getPhysicianName());
		a.setPhysicianNumber(b.getPhysicianNumber());
		a.setLabTestConducted(b.getLabTestConducted());
		a.setTypeOfSample(b.getTypeOfSample());
		a.setAgentIdentified(b.getAgentIdentified());

		if (b.getLocationType() != null) {
			Location location = DatabaseHelper.getLocationDao().queryForId(b.getLocationType().getId());
			a.setLocationType(locationDtoHelper.adoToDto(location));
		} else {
			b.setLocationType(null);
		}
	}

    @Override
    protected long getApproximateJsonSizeInBytes() {
        return 0;
    }
}
