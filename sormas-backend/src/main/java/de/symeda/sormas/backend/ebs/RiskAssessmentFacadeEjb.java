package de.symeda.sormas.backend.ebs;

import de.symeda.sormas.api.ebs.RiskAssessmentCriteria;
import de.symeda.sormas.api.ebs.RiskAssessmentDto;
import de.symeda.sormas.api.ebs.RiskAssessmentFacade;
import de.symeda.sormas.backend.user.User;
import de.symeda.sormas.backend.user.UserService;
import de.symeda.sormas.backend.util.DtoHelper;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.validation.Valid;
import java.util.*;
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
        target.setAssessmentDate(source.getAssessmentDate());
        target.setAssessmentTime(source.getAssessmentTime());
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
        target.setAssessmentDate(source.getAssessmentDate());
        target.setAssessmentTime(source.getAssessmentTime());
        target.setRiskAssessment(source.getRiskAssessment());
        target.setControlMeasuresComment(source.getControlMeasuresComment());
        target.setSpreadProbabilityComment(source.getSpreadProbabilityComment());
        target.setMorbidityMortalityComment(source.getMorbidityMortalityComment());
        target.setEbs(EbsFacadeEjb.toReferenceDto(source.getEbs()));
        return target;
    }

    public RiskAssessmentDto save(@Valid RiskAssessmentDto dto) {
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

    @Override
    @PermitAll
    public List<RiskAssessmentDto> getAllAfter(Date date) {
        return riskService.getAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public RiskAssessmentDto getRiskByUuid(String uuid, boolean detailedReferences) {
        RiskAssessment risk = riskService.getByUuid(uuid);
        return toDto(risk);
    }

    @Override
    public List<String> getAllActiveUuids() {
        User user = userService.getCurrentUser();
        if (user == null) {
            return Collections.emptyList();
        }

        return riskService.getAllActiveUuids();
    }

    @LocalBean
    @Stateless
    public static class RiskAssessmentFacadeEjbLocal extends RiskAssessmentFacadeEjb {
    }
}
