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
package de.symeda.sormas.app.backend.foodhistory;

import java.util.List;
import de.symeda.sormas.api.PostResponse;
import de.symeda.sormas.api.foodhistory.FoodHistoryDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.rest.NoConnectionException;
import retrofit2.Call;

public class FoodHistoryDtoHelper extends AdoDtoHelper<FoodHistory, FoodHistoryDto> {

    public FoodHistoryDtoHelper(){
    }

    @Override
    protected Class<FoodHistory> getAdoClass() {
        return FoodHistory.class;
    }
    @Override
    protected Class<FoodHistoryDto> getDtoClass() {
        return FoodHistoryDto.class;
    }
    @Override
    protected Call<List<FoodHistoryDto>> pullAllSince(long since, Integer size, String lastSynchronizedUuid)  throws NoConnectionException {
        throw new UnsupportedOperationException("Entity is embedded");
    }
    @Override
    protected Call<List<FoodHistoryDto>> pullByUuids(List<String> uuids) throws NoConnectionException {
        throw new UnsupportedOperationException("Entity is embedded");
    }
    @Override
    protected Call<List<PostResponse>> pushAll(List<FoodHistoryDto> epiDataDtos) throws NoConnectionException {
        throw new UnsupportedOperationException("Entity is embedded");
    }

    @Override
    public void fillInnerFromDto(FoodHistory target, FoodHistoryDto source) {

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
        target.setNumberOfPeopleAteImplicatedFood(source.getNumberOfPeopleAteImplicatedFood());
        target.setNumberAffected(source.getNumberAffected());
    }

    @Override
    public void fillInnerFromAdo(FoodHistoryDto target, FoodHistory source) {

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
        target.setNumberOfPeopleAteImplicatedFood(source.getNumberOfPeopleAteImplicatedFood());
        target.setNumberAffected(source.getNumberAffected());
    }

    @Override
    protected long getApproximateJsonSizeInBytes() {
        return 0;
    }
}
