package de.symeda.sormas.backend.ebs;

import de.symeda.sormas.api.ebs.RiskAssessmentDto;
import de.symeda.sormas.api.ebs.RiskAssessmentFacade;
import de.symeda.sormas.backend.util.DtoHelper;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless(name = "RiskAssessmentFacade")
public class RiskAssessmentFacadeEjb implements RiskAssessmentFacade {

    public RiskAssessment fillOrBuildEntity(RiskAssessmentDto source, RiskAssessment target, boolean checkChangeDate) {
        if (source == null) {
            return null;
        }

        target = DtoHelper.fillOrBuildEntity(source, target, RiskAssessment::new, checkChangeDate);
        target.setMorbidityMortality(source.getMorbidityMortality());
        target.setSpreadProbability(source.getSpreadProbability());
        target.setControlMeasures(source.getControlMeasures());
        return target;
    }

    public static RiskAssessmentDto toDto(RiskAssessment riskAssessment) {
        if (riskAssessment == null) {
            return null;
        }
        RiskAssessmentDto target = new RiskAssessmentDto();
        RiskAssessment source = riskAssessment;
        DtoHelper.fillDto(target, source);
        target.setMorbidityMortality(source.getMorbidityMortality());
        target.setSpreadProbability(source.getSpreadProbability());
        target.setControlMeasures(source.getControlMeasures());
        return target;
    }

    @LocalBean
    @Stateless
    public static class RiskAssessmentFacadeEjbLocal extends RiskAssessmentFacadeEjb {
    }
}
