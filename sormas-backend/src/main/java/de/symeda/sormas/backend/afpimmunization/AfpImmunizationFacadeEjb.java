package de.symeda.sormas.backend.afpimmunization;

import de.symeda.sormas.api.afpimmunization.AfpImmunizationDto;
import de.symeda.sormas.api.afpimmunization.AfpImmunizationFacade;
import de.symeda.sormas.backend.caze.CaseService;
import de.symeda.sormas.backend.infrastructure.community.CommunityService;
import de.symeda.sormas.backend.infrastructure.district.DistrictService;
import de.symeda.sormas.backend.infrastructure.facility.FacilityService;
import de.symeda.sormas.backend.infrastructure.region.RegionService;
import de.symeda.sormas.backend.util.DtoHelper;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless(name = "AfpImmunizationFacade")
public class AfpImmunizationFacadeEjb implements AfpImmunizationFacade {
    @EJB
    private AfpImmunizationService ImmunizationService;
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

    public AfpImmunization fillOrBuildEntity(AfpImmunizationDto source, AfpImmunization target, boolean checkChangeDate) {

        if (source == null) {
            return null;
        }

        target = DtoHelper.fillOrBuildEntity(source, target, AfpImmunization::new, checkChangeDate);

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


        return target;
    }

    public static AfpImmunizationDto toDto(AfpImmunization afpImmunization) {

        if (afpImmunization == null) {
            return null;
        }

        AfpImmunizationDto target = new AfpImmunizationDto();
        AfpImmunization source = afpImmunization;

        DtoHelper.fillDto(target, source);

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



        return target;
    }


    @LocalBean
    @Stateless
    public static class AfpImmunizationEjbLocal extends AfpImmunizationFacadeEjb {

    }
}
