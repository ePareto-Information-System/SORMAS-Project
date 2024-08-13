package de.symeda.sormas.backend.riskfactor;

import de.symeda.sormas.api.riskfactor.PatientSymptomsPrecedenceDto;
import de.symeda.sormas.api.riskfactor.RiskFactorDto;
import de.symeda.sormas.api.riskfactor.RiskFactorFacade;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.caze.CaseService;
import de.symeda.sormas.backend.infrastructure.community.CommunityService;
import de.symeda.sormas.backend.infrastructure.district.DistrictService;
import de.symeda.sormas.backend.infrastructure.facility.FacilityService;
import de.symeda.sormas.backend.infrastructure.region.RegionService;
import de.symeda.sormas.backend.patientsymptomsprecedence.PatientSymptomsPrecedence;
import de.symeda.sormas.backend.patientsymptomsprecedence.PatientSymptomsPrecedenceService;
import de.symeda.sormas.backend.util.DtoHelper;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @EJB
    private PatientSymptomsPrecedenceService patientSymptomsPrecedenceService;

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
        target.setDateOfSpecimenCollection(source.getDateOfSpecimenCollection());
        target.setTypeOfSpecimenCollection(source.getTypeOfSpecimenCollection());
        target.setInvestigatorName(source.getInvestigatorName());
        target.setInvestigatorTitle(source.getInvestigatorTitle());
        target.setInvestigatorAddress(source.getInvestigatorAddress());
        target.setInvestigatorTel(source.getInvestigatorTel());
        target.setEmail(source.getEmail());

    List<PatientSymptomsPrecedence> patientSymptomsPrecedences = new ArrayList<>();
    for (PatientSymptomsPrecedenceDto patientSymptomsPrecedenceDto : source.getPatientSymptomsPrecedence()) {
        PatientSymptomsPrecedence patientSymptomsPrecedence = patientSymptomsPrecedenceService.getByUuid(patientSymptomsPrecedenceDto.getUuid());
        patientSymptomsPrecedence = fillOrBuildPatientSymptomsPrecedenceEntity(patientSymptomsPrecedenceDto, patientSymptomsPrecedence, checkChangeDate);
        patientSymptomsPrecedence.setRiskFactor(target);
        patientSymptomsPrecedences.add(patientSymptomsPrecedence);
    }
    if (!DataHelper.equalContains(target.getPatientSymptomsPrecedence(), patientSymptomsPrecedences)) {
        // note: DataHelper.equal does not work here, because target.getAddresses may be a PersistentBag when using lazy loading
        target.setChangeDateOfEmbeddedLists(new Date());
    }
    target.getPatientSymptomsPrecedence().clear();
    target.getPatientSymptomsPrecedence().addAll(patientSymptomsPrecedences);

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
        target.setDateOfSpecimenCollection(source.getDateOfSpecimenCollection());
        target.setTypeOfSpecimenCollection(source.getTypeOfSpecimenCollection());
        target.setInvestigatorName(source.getInvestigatorName());
        target.setInvestigatorTitle(source.getInvestigatorTitle());
        target.setInvestigatorAddress(source.getInvestigatorAddress());
        target.setInvestigatorTel(source.getInvestigatorTel());
        target.setEmail(source.getEmail());

        List<PatientSymptomsPrecedenceDto> patientSymptomsPrecedenceDtos = new ArrayList<>();
        for (PatientSymptomsPrecedence patientSymptomsPrecedence : source.getPatientSymptomsPrecedence()) {
            PatientSymptomsPrecedenceDto patientSymptomsPrecedenceDto = toPatientSymptomsPrecedenceDto(patientSymptomsPrecedence);
            patientSymptomsPrecedenceDtos.add(patientSymptomsPrecedenceDto);
        }
        target.setPatientSymptomsPrecedence(patientSymptomsPrecedenceDtos);

        return target;
    }

    public PatientSymptomsPrecedence fillOrBuildPatientSymptomsPrecedenceEntity(PatientSymptomsPrecedenceDto source, PatientSymptomsPrecedence target, boolean checkChangeDate) {
        if (source == null) {
            return null;
        }
        target = DtoHelper.fillOrBuildEntity(source, target, PatientSymptomsPrecedence::new, checkChangeDate);

        target.setName(source.getName());
        target.setContactAddress(source.getContactAddress());
        target.setPhone(source.getPhone());

        return target;
    }

    public static PatientSymptomsPrecedenceDto toPatientSymptomsPrecedenceDto(PatientSymptomsPrecedence source) {

        if (source == null) {
            return null;
        }

        PatientSymptomsPrecedenceDto target = new PatientSymptomsPrecedenceDto();

        DtoHelper.fillDto(target, source);
        target.setName(source.getName());
        target.setContactAddress(source.getContactAddress());
        target.setPhone(source.getPhone());

        return target;
    }

    @LocalBean
    @Stateless
    public static class RiskFactorFacadeEjbLocal extends RiskFactorFacadeEjb {
    }
}
