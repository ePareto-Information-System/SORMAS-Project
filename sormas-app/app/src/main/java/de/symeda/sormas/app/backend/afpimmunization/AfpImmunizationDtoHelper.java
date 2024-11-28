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

package de.symeda.sormas.app.backend.afpimmunization;

import java.util.List;

import de.symeda.sormas.api.PostResponse;
import de.symeda.sormas.api.afpimmunization.AfpImmunizationDto;
import de.symeda.sormas.api.riskfactor.RiskFactorDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.rest.NoConnectionException;
import retrofit2.Call;

public class AfpImmunizationDtoHelper extends AdoDtoHelper<AfpImmunization, AfpImmunizationDto> {


	public AfpImmunizationDtoHelper() {
	}

	@Override
	protected Class<AfpImmunization> getAdoClass() {
		return AfpImmunization.class;
	}

	@Override
	protected Class<AfpImmunizationDto> getDtoClass() {
		return AfpImmunizationDto.class;
	}

	@Override
	protected Call<List<AfpImmunizationDto>> pullAllSince(long since, Integer size, String lastSynchronizedUuid)  throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	protected Call<List<AfpImmunizationDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	protected Call<List<PostResponse>> pushAll(List<AfpImmunizationDto> afpImmunizationDtos) throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	public void fillInnerFromDto(AfpImmunization target, AfpImmunizationDto source) {
		target.setTotalNumberDoses(source.getTotalNumberDoses());
		target.setOpvDoseAtBirth(source.getOpvDoseAtBirth());
		target.setSecondDose(source.getSecondDose());
		target.setFourthDose(source.getFourthDose());
		target.setFirstDose(source.getFirstDose());
		target.setThirdDose(source.getThirdDose());
		target.setLastDose(source.getLastDose());
		target.setTotalOpvDosesReceivedThroughSia(source.getTotalOpvDosesReceivedThroughSia());
		target.setTotalOpvDosesReceivedThroughRi(source.getTotalOpvDosesReceivedThroughRi());
		target.setDateLastOpvDosesReceivedThroughSia(source.getDateLastOpvDosesReceivedThroughSia());
		target.setTotalIpvDosesReceivedThroughSia(source.getTotalIpvDosesReceivedThroughSia());
		target.setTotalIpvDosesReceivedThroughRi(source.getTotalIpvDosesReceivedThroughRi());
		target.setDateLastIpvDosesReceivedThroughSia(source.getDateLastIpvDosesReceivedThroughSia());
		target.setSourceRiVaccinationInformation(source.getSourceRiVaccinationInformation());
	}

	@Override
	public void fillInnerFromAdo(AfpImmunizationDto target, AfpImmunization source) {
		target.setTotalNumberDoses(source.getTotalNumberDoses());
		target.setOpvDoseAtBirth(source.getOpvDoseAtBirth());
		target.setSecondDose(source.getSecondDose());
		target.setFourthDose(source.getFourthDose());
		target.setFirstDose(source.getFirstDose());
		target.setThirdDose(source.getThirdDose());
		target.setLastDose(source.getLastDose());
		target.setTotalOpvDosesReceivedThroughSia(source.getTotalOpvDosesReceivedThroughSia());
		target.setTotalOpvDosesReceivedThroughRi(source.getTotalOpvDosesReceivedThroughRi());
		target.setDateLastOpvDosesReceivedThroughSia(source.getDateLastOpvDosesReceivedThroughSia());
		target.setTotalIpvDosesReceivedThroughSia(source.getTotalIpvDosesReceivedThroughSia());
		target.setTotalIpvDosesReceivedThroughRi(source.getTotalIpvDosesReceivedThroughRi());
		target.setDateLastIpvDosesReceivedThroughSia(source.getDateLastIpvDosesReceivedThroughSia());
		target.setSourceRiVaccinationInformation(source.getSourceRiVaccinationInformation());
	}

    @Override
    protected long getApproximateJsonSizeInBytes() {
        return 0;
    }
}
