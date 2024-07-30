package de.symeda.sormas.backend.foodhistory;

import de.symeda.sormas.api.epidata.ContainmentMeasureDto;
import de.symeda.sormas.api.foodhistory.AffectedPersonDto;
import de.symeda.sormas.api.foodhistory.FoodHistoryDto;
import de.symeda.sormas.api.foodhistory.FoodHistoryFacade;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.affectedperson.AffectedPerson;
import de.symeda.sormas.backend.affectedperson.AffectedPersonService;
import de.symeda.sormas.backend.caze.CaseService;
import de.symeda.sormas.backend.containmentmeasure.ContainmentMeasure;
import de.symeda.sormas.backend.infrastructure.community.CommunityService;
import de.symeda.sormas.backend.infrastructure.district.DistrictService;
import de.symeda.sormas.backend.infrastructure.facility.FacilityService;
import de.symeda.sormas.backend.infrastructure.region.RegionService;
import de.symeda.sormas.backend.util.DtoHelper;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless(name = "FoodHistoryFacade")
public class FoodHistoryFacadeEjb implements FoodHistoryFacade {
    @EJB
    private FoodHistoryService foodHistoryService;
    @EJB
    private CaseService caseService;
    @EJB
    private RegionService regionService;
    @EJB
    private DistrictService districtService;
    @EJB
    private CommunityService communityService;
    @EJB
    private FacilityService facilityService;
    @EJB
    private AffectedPersonService affectedPersonService;

    public FoodHistory fillOrBuildEntity(FoodHistoryDto source, FoodHistory target, boolean checkChangeDate) {

        if (source == null) {
            return null;
        }

        target = DtoHelper.fillOrBuildEntity(source, target, FoodHistory::new, checkChangeDate);

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

        List<AffectedPerson> affectedPersons = new ArrayList<>();
        for (AffectedPersonDto affectedPersonDto : source.getAffectedPersons()) {
            AffectedPerson affectedPerson = affectedPersonService.getByUuid(affectedPersonDto.getUuid());
            affectedPerson = fillOrBuildAffectedPersonEntity(affectedPersonDto, affectedPerson, checkChangeDate);
            affectedPerson.setFoodHistory(target);
            affectedPersons.add(affectedPerson);
        }
        if (!DataHelper.equalContains(target.getAffectedPersons(), affectedPersons)) {
            // note: DataHelper.equal does not work here, because target.getAddresses may be a PersistentBag when using lazy loading
            target.setChangeDateOfEmbeddedLists(new Date());
        }
        target.getAffectedPersons().clear();
        target.getAffectedPersons().addAll(affectedPersons);

        return target;
    }

    public static FoodHistoryDto toDto(FoodHistory foodHistory) {

        if (foodHistory == null) {
            return null;
        }

        FoodHistoryDto target = new FoodHistoryDto();
        FoodHistory source = foodHistory;

        DtoHelper.fillDto(target, source);

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

        List<AffectedPersonDto> affectedPersonDtos = new ArrayList<>();
        for (AffectedPerson affectedPerson : source.getAffectedPersons()) {
            AffectedPersonDto affectedPersonDto = toAffectedPersonDto(affectedPerson);
            affectedPersonDtos.add(affectedPersonDto);
        }
        target.setAffectedPersons(affectedPersonDtos);

        return target;
    }

    public AffectedPerson fillOrBuildAffectedPersonEntity(AffectedPersonDto source, AffectedPerson target, boolean checkChangeDate) {
        if (source == null) {
            return null;
        }
        target = DtoHelper.fillOrBuildEntity(source, target, AffectedPerson::new, checkChangeDate);

        target.setNameOfAffectedPerson(source.getNameOfAffectedPerson());
        target.setTelNo(source.getTelNo());
        target.setDateTime(source.getDateTime());
        target.setAge(source.getAge());

        return target;
    }

    public static AffectedPersonDto toAffectedPersonDto(AffectedPerson source) {

        if (source == null) {
            return null;
        }

        AffectedPersonDto target = new AffectedPersonDto();

        DtoHelper.fillDto(target, source);
        target.setNameOfAffectedPerson(source.getNameOfAffectedPerson());
        target.setTelNo(source.getTelNo());
        target.setDateTime(source.getDateTime());
        target.setAge(source.getAge());

        return target;
    }

    @LocalBean
    @Stateless
    public static class FoodHistoryEjbLocal extends FoodHistoryFacadeEjb {

    }
}
