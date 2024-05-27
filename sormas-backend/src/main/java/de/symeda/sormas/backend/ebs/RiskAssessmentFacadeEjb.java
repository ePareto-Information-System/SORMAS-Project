package de.symeda.sormas.backend.ebs;

import de.symeda.sormas.api.ebs.RiskAssessmentCriteria;
import de.symeda.sormas.api.ebs.RiskAssessmentDto;
import de.symeda.sormas.api.ebs.RiskAssessmentFacade;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.sample.SampleJurisdictionFlagsDto;
import de.symeda.sormas.backend.sample.Sample;
import de.symeda.sormas.backend.user.UserService;
import de.symeda.sormas.backend.util.DtoHelper;
import de.symeda.sormas.backend.util.Pseudonymizer;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless(name = "RiskAssessmentFacade")
public class RiskAssessmentFacadeEjb implements RiskAssessmentFacade {
    @EJB
    private EbsService ebsService;
    @EJB
    private RiskAssessmentService riskService;
    @EJB
    private UserService userService;

    public RiskAssessment fillOrBuildEntity(RiskAssessmentDto source, RiskAssessment target, boolean checkChangeDate) {
        if (source == null) {
            return null;
        }

        target = DtoHelper.fillOrBuildEntity(source, target, RiskAssessment::new, checkChangeDate);
        target.setMorbidityMortality(source.getMorbidityMortality());
        target.setSpreadProbability(source.getSpreadProbability());
        target.setControlMeasures(source.getControlMeasures());
        target.setResponseDate(source.getResponseDate());
        target.setResponseTime(source.getResponseTime());
        target.setRiskAssessment(source.getRiskAssessment());
        target.setControlMeasuresComment(source.getControlMeasuresComment());
        target.setSpreadProbabilityComment(source.getSpreadProbabilityComment());
        target.setMorbidityMortalityComment(source.getMorbidityMortalityComment());
        target.setEbs(ebsService.getByReferenceDto(source.getEbs()));
        return target;
    }

    public RiskAssessmentDto toDto(RiskAssessment riskAssessment) {
        if (riskAssessment == null) {
            return null;
        }
        RiskAssessmentDto target = new RiskAssessmentDto();
        RiskAssessment source = riskAssessment;
        DtoHelper.fillDto(target, source);
        target.setMorbidityMortality(source.getMorbidityMortality());
        target.setSpreadProbability(source.getSpreadProbability());
        target.setControlMeasures(source.getControlMeasures());
        target.setResponseDate(source.getResponseDate());
        target.setResponseTime(source.getResponseTime());
        target.setRiskAssessment(source.getRiskAssessment());
        target.setControlMeasuresComment(source.getControlMeasuresComment());
        target.setSpreadProbabilityComment(source.getSpreadProbabilityComment());
        target.setMorbidityMortalityComment(source.getMorbidityMortalityComment());
        target.setEbs(EbsFacadeEjb.toReferenceDto(source.getEbs()));
        return target;
    }

    public RiskAssessmentDto saveRisk(@Valid RiskAssessmentDto dto) {
        RiskAssessment riskAssessment = riskService.getByUuid(dto.getUuid());

        RiskAssessment ado = fillOrBuildEntity(dto, riskAssessment, true);

        riskService.ensurePersisted(ado);
        return toDto(ado);
    }

    @Override
    public List<RiskAssessmentDto> findBy(RiskAssessmentCriteria criteria) {
        return riskService.findBy(criteria).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<RiskAssessmentDto> getByEbsUuids(List<String> ebsUuids) {
        return riskService.getByEbsUuids(ebsUuids).stream().map(this::toDto).collect(Collectors.toList());
    }

    @LocalBean
    @Stateless
    public static class RiskAssessmentFacadeEjbLocal extends RiskAssessmentFacadeEjb {
    }
}
