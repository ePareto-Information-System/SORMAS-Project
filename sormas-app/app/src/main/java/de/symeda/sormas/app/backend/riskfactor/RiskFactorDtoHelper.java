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

package de.symeda.sormas.app.backend.riskfactor;

import java.util.ArrayList;
import java.util.List;

import de.symeda.sormas.api.PostResponse;
import de.symeda.sormas.api.activityascase.ActivityAsCaseDto;
import de.symeda.sormas.api.riskfactor.PatientSymptomsPrecedenceDto;
import de.symeda.sormas.api.riskfactor.RiskFactorDto;
import de.symeda.sormas.app.backend.activityascase.ActivityAsCase;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.backend.patientsymptomsprecedence.PatientSymptomsPrecedence;
import de.symeda.sormas.app.backend.patientsymptomsprecedence.PatientSymptomsPrecedenceDtoHelper;
import de.symeda.sormas.app.rest.NoConnectionException;
import retrofit2.Call;

public class RiskFactorDtoHelper extends AdoDtoHelper<RiskFactor, RiskFactorDto> {

	private final PatientSymptomsPrecedenceDtoHelper patientSymptomsPrecedenceDtoHelper;
	public RiskFactorDtoHelper() {
		patientSymptomsPrecedenceDtoHelper = new PatientSymptomsPrecedenceDtoHelper();
	}

	@Override
	protected Class<RiskFactor> getAdoClass() {
		return RiskFactor.class;
	}

	@Override
	protected Class<RiskFactorDto> getDtoClass() {
		return RiskFactorDto.class;
	}

	@Override
	protected Call<List<RiskFactorDto>> pullAllSince(long since, Integer size, String lastSynchronizedUuid)  throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	protected Call<List<RiskFactorDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	protected Call<List<PostResponse>> pushAll(List<RiskFactorDto> epiDataDtos) throws NoConnectionException {
		throw new UnsupportedOperationException("Entity is embedded");
	}

	@Override
	public void fillInnerFromDto(RiskFactor target, RiskFactorDto source) {

		target.setDrinkingWaterSourceOne(source.getDrinkingWaterSourceOne());
		target.setDrinkingWaterSourceTwo(source.getDrinkingWaterSourceTwo());
		target.setDrinkingWaterSourceThree(source.getDrinkingWaterSourceThree());
		target.setDrinkingWaterSourceFour(source.getDrinkingWaterSourceFour());
		target.setNonDrinkingWaterSourceOne(source.getNonDrinkingWaterSourceOne());
		target.setNonDrinkingWaterSourceTwo(source.getNonDrinkingWaterSourceTwo());
		target.setNonDrinkingWaterSourceThree(source.getNonDrinkingWaterSourceThree());
		target.setNonDrinkingWaterSourceFour(source.getNonDrinkingWaterSourceFour());
		target.setFoodItemsOne(source.getFoodItemsOne());
		target.setFoodItemsTwo(source.getFoodItemsTwo());
		target.setFoodItemsThree(source.getFoodItemsThree());
		target.setFoodItemsFour(source.getFoodItemsFour());
		target.setFoodItemsFive(source.getFoodItemsFive());
		target.setFoodItemsSix(source.getFoodItemsSix());
		target.setFoodItemsSeven(source.getFoodItemsSeven());
		target.setFoodItemsEight(source.getFoodItemsEight());
		target.setDrinkingWaterInfectedByVibrio(source.getDrinkingWaterInfectedByVibrio());
		target.setNonDrinkingWaterInfectedByVibrio(source.getNonDrinkingWaterInfectedByVibrio());
		target.setOtherSocialEventDetails(source.getOtherSocialEventDetails());
		target.setFoodItemsInfectedByVibrio(source.getFoodItemsInfectedByVibrio());
		target.setWaterUsedForDrinking(source.getWaterUsedForDrinking());
		target.setThreeDaysPriorToDiseaseWaterSourceOne(source.getThreeDaysPriorToDiseaseWaterSourceOne());
		target.setThreeDaysPriorToDiseaseWaterSourceTwo(source.getThreeDaysPriorToDiseaseWaterSourceTwo());
		target.setThreeDaysPriorToDiseaseWaterSourceThree(source.getThreeDaysPriorToDiseaseWaterSourceThree());
		target.setThreeDaysPriorToDiseaseWaterSourceFour(source.getThreeDaysPriorToDiseaseWaterSourceFour());
		target.setThreeDaysPriorToDiseaseWaterSourceFive(source.getThreeDaysPriorToDiseaseWaterSourceFive());
		target.setThreeDaysPriorToDiseaseFoodItemsOne(source.getThreeDaysPriorToDiseaseFoodItemsOne());
		target.setThreeDaysPriorToDiseaseFoodItemsTwo(source.getThreeDaysPriorToDiseaseFoodItemsTwo());
		target.setThreeDaysPriorToDiseaseFoodItemsThree(source.getThreeDaysPriorToDiseaseFoodItemsThree());
		target.setThreeDaysPriorToDiseaseFoodItemsFour(source.getThreeDaysPriorToDiseaseFoodItemsFour());
		target.setThreeDaysPriorToDiseaseFoodItemsFive(source.getThreeDaysPriorToDiseaseFoodItemsFive());
		target.setThreeDaysPriorToDiseaseAttendAnyFuneral(source.getThreeDaysPriorToDiseaseAttendAnyFuneral());
		target.setThreeDaysPriorToDiseaseAttendAnySocialEvent(source.getThreeDaysPriorToDiseaseAttendAnySocialEvent());

		List<PatientSymptomsPrecedence> patientSymptomsPrecedences = new ArrayList<>();
		if (!source.getPatientSymptomsPrecedence().isEmpty()) {
			for (PatientSymptomsPrecedenceDto patientSymptomsPrecedenceDto : source.getPatientSymptomsPrecedence()) {
				PatientSymptomsPrecedence patientSymptomsPrecedence = patientSymptomsPrecedenceDtoHelper.fillOrCreateFromDto(null, patientSymptomsPrecedenceDto);
				patientSymptomsPrecedence.setRiskFactor(target);
				patientSymptomsPrecedences.add(patientSymptomsPrecedence);
			}
		}
		target.setPatientSymptomsPrecedences(patientSymptomsPrecedences);

	}

	@Override
	public void fillInnerFromAdo(RiskFactorDto target, RiskFactor source) {

		target.setDrinkingWaterSourceOne(source.getDrinkingWaterSourceOne());
		target.setDrinkingWaterSourceTwo(source.getDrinkingWaterSourceTwo());
		target.setDrinkingWaterSourceThree(source.getDrinkingWaterSourceThree());
		target.setDrinkingWaterSourceFour(source.getDrinkingWaterSourceFour());
		target.setNonDrinkingWaterSourceOne(source.getNonDrinkingWaterSourceOne());
		target.setNonDrinkingWaterSourceTwo(source.getNonDrinkingWaterSourceTwo());
		target.setNonDrinkingWaterSourceThree(source.getNonDrinkingWaterSourceThree());
		target.setNonDrinkingWaterSourceFour(source.getNonDrinkingWaterSourceFour());
		target.setFoodItemsOne(source.getFoodItemsOne());
		target.setFoodItemsTwo(source.getFoodItemsTwo());
		target.setFoodItemsThree(source.getFoodItemsThree());
		target.setFoodItemsFour(source.getFoodItemsFour());
		target.setFoodItemsFive(source.getFoodItemsFive());
		target.setFoodItemsSix(source.getFoodItemsSix());
		target.setFoodItemsSeven(source.getFoodItemsSeven());
		target.setFoodItemsEight(source.getFoodItemsEight());
		target.setDrinkingWaterInfectedByVibrio(source.getDrinkingWaterInfectedByVibrio());
		target.setNonDrinkingWaterInfectedByVibrio(source.getNonDrinkingWaterInfectedByVibrio());
		target.setOtherSocialEventDetails(source.getOtherSocialEventDetails());
		target.setFoodItemsInfectedByVibrio(source.getFoodItemsInfectedByVibrio());
		target.setWaterUsedForDrinking(source.getWaterUsedForDrinking());
		target.setThreeDaysPriorToDiseaseWaterSourceOne(source.getThreeDaysPriorToDiseaseWaterSourceOne());
		target.setThreeDaysPriorToDiseaseWaterSourceTwo(source.getThreeDaysPriorToDiseaseWaterSourceTwo());
		target.setThreeDaysPriorToDiseaseWaterSourceThree(source.getThreeDaysPriorToDiseaseWaterSourceThree());
		target.setThreeDaysPriorToDiseaseWaterSourceFour(source.getThreeDaysPriorToDiseaseWaterSourceFour());
		target.setThreeDaysPriorToDiseaseWaterSourceFive(source.getThreeDaysPriorToDiseaseWaterSourceFive());
		target.setThreeDaysPriorToDiseaseFoodItemsOne(source.getThreeDaysPriorToDiseaseFoodItemsOne());
		target.setThreeDaysPriorToDiseaseFoodItemsTwo(source.getThreeDaysPriorToDiseaseFoodItemsTwo());
		target.setThreeDaysPriorToDiseaseFoodItemsThree(source.getThreeDaysPriorToDiseaseFoodItemsThree());
		target.setThreeDaysPriorToDiseaseFoodItemsFour(source.getThreeDaysPriorToDiseaseFoodItemsFour());
		target.setThreeDaysPriorToDiseaseFoodItemsFive(source.getThreeDaysPriorToDiseaseFoodItemsFive());
		target.setThreeDaysPriorToDiseaseAttendAnyFuneral(source.getThreeDaysPriorToDiseaseAttendAnyFuneral());
		target.setThreeDaysPriorToDiseaseAttendAnySocialEvent(source.getThreeDaysPriorToDiseaseAttendAnySocialEvent());

		List<PatientSymptomsPrecedenceDto> patientSymptomsPrecedenceDtos = new ArrayList<>();
		if (!source.getPatientSymptomsPrecedences().isEmpty()) {
			for (PatientSymptomsPrecedence patientSymptomsPrecedence : source.getPatientSymptomsPrecedences()) {
				PatientSymptomsPrecedenceDto  patientSymptomsPrecedenceDto = patientSymptomsPrecedenceDtoHelper.adoToDto(patientSymptomsPrecedence);
				patientSymptomsPrecedenceDtos.add(patientSymptomsPrecedenceDto);
			}
		}
		target.setPatientSymptomsPrecedence(patientSymptomsPrecedenceDtos);

	}

    @Override
    protected long getApproximateJsonSizeInBytes() {
        return 0;
    }
}
