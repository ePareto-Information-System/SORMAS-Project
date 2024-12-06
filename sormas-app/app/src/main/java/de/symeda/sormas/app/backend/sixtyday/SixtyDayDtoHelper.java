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

package de.symeda.sormas.app.backend.sixtyday;

import java.util.List;

import de.symeda.sormas.api.PostResponse;
import de.symeda.sormas.api.sixtyday.SixtyDayDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.rest.NoConnectionException;
import retrofit2.Call;

public class SixtyDayDtoHelper extends AdoDtoHelper<SixtyDay, SixtyDayDto> {


	public SixtyDayDtoHelper() {
	}

	@Override
	protected Class<SixtyDay> getAdoClass() {
		return SixtyDay.class;
	}

	@Override
	protected Class<SixtyDayDto> getDtoClass() {
		return SixtyDayDto.class;
	}

	@Override
	protected Call<List<SixtyDayDto>> pullAllSince(long since, Integer size, String lastSynchronizedUuid)  throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	protected Call<List<SixtyDayDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	protected Call<List<PostResponse>> pushAll(List<SixtyDayDto> SixtyDayDtos) throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	public void fillInnerFromDto(SixtyDay target, SixtyDayDto source) {
		target.setPersonExamineCase(source.getPersonExamineCase());
		target.setDateOfFollowup(source.getDateOfFollowup());
		target.setDateBirth(source.getDateBirth());
		target.setResidentialLocation(source.getResidentialLocation());
		target.setPatientFound(source.getPatientFound());
		target.setPatientFoundReason(source.getPatientFoundReason());
		target.setLocateChildAttempt(source.getLocateChildAttempt());
		target.setParalysisWeaknessPresent(source.getParalysisWeaknessPresent());
		target.setParalysisWeaknessPresentSite(source.getParalysisWeaknessPresentSite());
		target.setParalyzedPartOther(source.getParalyzedPartOther());
		target.setParalysisWeaknessFloppy(source.getParalysisWeaknessFloppy());
		target.setMuscleToneParalyzedPart(source.getMuscleToneParalyzedPart());
		target.setMuscleToneOtherPartBody(source.getMuscleToneOtherPartBody());
		target.setDeepTendon(source.getDeepTendon());
		target.setMuscleVolume(source.getMuscleVolume());
		target.setSensoryLoss(source.getSensoryLoss());
		target.setProvisionalDiagnosis(source.getProvisionalDiagnosis());
		target.setComments(source.getComments());
		target.setContactDetailsNumber(source.getContactDetailsNumber());
		target.setContactDetailsEmail(source.getContactDetailsEmail());
		target.setSignature(source.getSignature());
		target.setDateSubmissionForms(source.getDateSubmissionForms());
		target.setSpecifyFoodsSources(source.getSpecifyFoodsSources());
		target.setSpecifySources(source.getSpecifySources());
		target.setProductName(source.getProductName());
		target.setBatchNumber(source.getBatchNumber());
		target.setDateOfManufacture(source.getDateOfManufacture());
		target.setExpirationDate(source.getExpirationDate());
		target.setPackageSize(source.getPackageSize());
		target.setPackagingType(source.getPackagingType());
		target.setPackagingTypeOther(source.getPackagingTypeOther());
		target.setPlaceOfPurchase(source.getPlaceOfPurchase());
		target.setNameOfManufacturer(source.getNameOfManufacturer());
		target.setAddress(source.getAddress());
		target.setFoodTel(source.getFoodTel());
	}

	@Override
	public void fillInnerFromAdo(SixtyDayDto target, SixtyDay source) {
		target.setPersonExamineCase(source.getPersonExamineCase());
		target.setDateOfFollowup(source.getDateOfFollowup());
		target.setDateBirth(source.getDateBirth());
		target.setResidentialLocation(source.getResidentialLocation());
		target.setPatientFound(source.getPatientFound());
		target.setPatientFoundReason(source.getPatientFoundReason());
		target.setLocateChildAttempt(source.getLocateChildAttempt());
		target.setParalysisWeaknessPresent(source.getParalysisWeaknessPresent());
		target.setParalysisWeaknessPresentSite(source.getParalysisWeaknessPresentSite());
		target.setParalyzedPartOther(source.getParalyzedPartOther());
		target.setParalysisWeaknessFloppy(source.getParalysisWeaknessFloppy());
		target.setMuscleToneParalyzedPart(source.getMuscleToneParalyzedPart());
		target.setMuscleToneOtherPartBody(source.getMuscleToneOtherPartBody());
		target.setDeepTendon(source.getDeepTendon());
		target.setMuscleVolume(source.getMuscleVolume());
		target.setSensoryLoss(source.getSensoryLoss());
		target.setProvisionalDiagnosis(source.getProvisionalDiagnosis());
		target.setComments(source.getComments());
		target.setContactDetailsNumber(source.getContactDetailsNumber());
		target.setContactDetailsEmail(source.getContactDetailsEmail());
		target.setSignature(source.getSignature());
		target.setDateSubmissionForms(source.getDateSubmissionForms());
		target.setSpecifyFoodsSources(source.getSpecifyFoodsSources());
		target.setSpecifySources(source.getSpecifySources());
		target.setProductName(source.getProductName());
		target.setBatchNumber(source.getBatchNumber());
		target.setDateOfManufacture(source.getDateOfManufacture());
		target.setExpirationDate(source.getExpirationDate());
		target.setPackageSize(source.getPackageSize());
		target.setPackagingType(source.getPackagingType());
		target.setPackagingTypeOther(source.getPackagingTypeOther());
		target.setPlaceOfPurchase(source.getPlaceOfPurchase());
		target.setNameOfManufacturer(source.getNameOfManufacturer());
		target.setAddress(source.getAddress());
		target.setFoodTel(source.getFoodTel());
	}

    @Override
    protected long getApproximateJsonSizeInBytes() {
        return 0;
    }
}
