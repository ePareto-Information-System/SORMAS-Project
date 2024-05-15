package de.symeda.sormas.backend.riskfactor;

import de.symeda.sormas.api.riskfactor.RiskFactorDto;
import de.symeda.sormas.api.riskfactor.RiskFactorFacade;
import de.symeda.sormas.backend.caze.CaseService;
import de.symeda.sormas.backend.infrastructure.community.CommunityService;
import de.symeda.sormas.backend.infrastructure.district.DistrictService;
import de.symeda.sormas.backend.infrastructure.facility.FacilityService;
import de.symeda.sormas.backend.infrastructure.region.RegionService;
import de.symeda.sormas.backend.util.DtoHelper;
import de.symeda.sormas.backend.sixtyday.SixtyDayService;
import de.symeda.sormas.backend.util.DtoHelper;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless(name = "RiskFactorFacade")
public class RiskFactorFacadeEjb implements RiskFactorFacade {

    @EJB
    private RiskFactorService service;
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

public RiskFactor fillOrBuildEntity(RiskFactorDto source, RiskFactor target, boolean checkChangeDate) {
        if (source == null) {
            return null;
        }
        target = DtoHelper.fillOrBuildEntity(source, target, RiskFactor::new, checkChangeDate);

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
        target.setOtherSocialEventDetails(source.getOtherSocialEventDetails());
        target.setPatientSpoxVaccinationScarPresent(source.getPatientSpoxVaccinationScarPresent());
        target.setPatientTravelledAnywhere3WeeksPrior(source.getPatientTravelledAnywhere3WeeksPrior());
        target.setPatientTravelled3WeeksIfYesIndicate(source.getPatientTravelled3WeeksIfYesIndicate());
        target.setPatientTravelledPeriodOfIllness(source.getPatientTravelledPeriodOfIllness());
        target.setPatientTravelledIllnessIfYesIndicate(source.getPatientTravelledIllnessIfYesIndicate());
        target.setOtherPlaces(source.getOtherPlaces());
        target.setDuring3WeeksPatientContactWithSimilarSymptoms(source.getDuring3WeeksPatientContactWithSimilarSymptoms());
        target.setDuring3WeeksPatientContactWithSimilarSymptomsIfYes(source.getDuring3WeeksPatientContactWithSimilarSymptomsIfYes());
        target.setDateOfContactWithIllPerson(source.getDateOfContactWithIllPerson());
        target.setPatientTouchDomesticWildAnimal(source.getPatientTouchDomesticWildAnimal());
        target.setPatientTouchDomesticWildAnimalIfYes(source.getPatientTouchDomesticWildAnimalIfYes());
        target.setStatusOfPatient(source.getStatusOfPatient());
        target.setDateOfDeath(source.getDateOfDeath());
        target.setPlaceOfDeath(source.getPlaceOfDeath());
        target.setDateOfSpecimenCollection(source.getDateOfSpecimenCollection());
        target.setTypeOfSpecimenCollection(source.getTypeOfSpecimenCollection());
        target.setInvestigatorName(source.getInvestigatorName());
        target.setInvestigatorTitle(source.getInvestigatorTitle());
        target.setInvestigatorAddress(source.getInvestigatorAddress());
        target.setInvestigatorTel(source.getInvestigatorTel());
        target.setEmail(source.getEmail());

    return target;
    }

    public static RiskFactorDto toDto(RiskFactor riskFactor) {
        if (riskFactor == null) {
            return null;
        }
        RiskFactorDto target = new RiskFactorDto();
        RiskFactor source = riskFactor;
        DtoHelper.fillDto(target, source);

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
        target.setOtherSocialEventDetails(source.getOtherSocialEventDetails());
        target.setPatientSpoxVaccinationScarPresent(source.getPatientSpoxVaccinationScarPresent());
        target.setPatientTravelledAnywhere3WeeksPrior(source.getPatientTravelledAnywhere3WeeksPrior());
        target.setPatientTravelled3WeeksIfYesIndicate(source.getPatientTravelled3WeeksIfYesIndicate());
        target.setPatientTravelledPeriodOfIllness(source.getPatientTravelledPeriodOfIllness());
        target.setPatientTravelledIllnessIfYesIndicate(source.getPatientTravelledIllnessIfYesIndicate());
        target.setOtherPlaces(source.getOtherPlaces());
        target.setDuring3WeeksPatientContactWithSimilarSymptoms(source.getDuring3WeeksPatientContactWithSimilarSymptoms());
        target.setDuring3WeeksPatientContactWithSimilarSymptomsIfYes(source.getDuring3WeeksPatientContactWithSimilarSymptomsIfYes());
        target.setDateOfContactWithIllPerson(source.getDateOfContactWithIllPerson());
        target.setPatientTouchDomesticWildAnimal(source.getPatientTouchDomesticWildAnimal());
        target.setPatientTouchDomesticWildAnimalIfYes(source.getPatientTouchDomesticWildAnimalIfYes());
        target.setStatusOfPatient(source.getStatusOfPatient());
        target.setDateOfDeath(source.getDateOfDeath());
        target.setPlaceOfDeath(source.getPlaceOfDeath());
        target.setDateOfSpecimenCollection(source.getDateOfSpecimenCollection());
        target.setTypeOfSpecimenCollection(source.getTypeOfSpecimenCollection());
        target.setInvestigatorName(source.getInvestigatorName());
        target.setInvestigatorTitle(source.getInvestigatorTitle());
        target.setInvestigatorAddress(source.getInvestigatorAddress());
        target.setInvestigatorTel(source.getInvestigatorTel());
        target.setEmail(source.getEmail());


        return target;
    }

    @LocalBean
    @Stateless
    public static class RiskFactorFacadeEjbLocal extends RiskFactorFacadeEjb {
    }
}
