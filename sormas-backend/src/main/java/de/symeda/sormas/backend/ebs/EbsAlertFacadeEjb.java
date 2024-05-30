package de.symeda.sormas.backend.ebs;

import de.symeda.sormas.api.ebs.EbsAlertDto;
import de.symeda.sormas.api.ebs.EbsAlertFacade;
import de.symeda.sormas.api.ebs.RiskAssessmentCriteria;
import de.symeda.sormas.api.ebs.RiskAssessmentDto;
import de.symeda.sormas.backend.util.DtoHelper;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;
import java.util.stream.Collectors;
@Stateless(name = "EbsAlertFacade")
public class EbsAlertFacadeEjb implements EbsAlertFacade {
    @EJB
    private EbsService ebsService;
    @EJB
    private EbsAlertService ebsAlertService;

    public EbsAlert fillOrBuildEntity(EbsAlertDto source, EbsAlert target, boolean checkChangeDate) {
        if (source == null) {
            return null;
        }

        target = DtoHelper.fillOrBuildEntity(source, target, EbsAlert::new, checkChangeDate);
        target.setResponseDate(source.getResponseDate());
        target.setResponseStatus(source.getResponseStatus());
        target.setAlertUsed(source.getAlertUsed());
        target.setDetailsAlertUsed(source.getDetailsAlertUsed());
        target.setDetailsResponseActivities(source.getDetailsResponseActivities());
        target.setDetailsGiven(source.getDetailsGiven());
        target.setEbs(ebsService.getByReferenceDto(source.getEbs()));
        target.setActionInitiated(source.getActionInitiated());
        return target;
    }

    public EbsAlertDto toDto(EbsAlert ebsAlert) {
        if (ebsAlert == null) {
            return null;
        }
        EbsAlertDto target = new EbsAlertDto();
        EbsAlert source = ebsAlert;
        DtoHelper.fillDto(target, source);
        target.setActionInitiated(source.getActionInitiated());
        target.setResponseDate(source.getResponseDate());
        target.setResponseStatus(source.getResponseStatus());
        target.setAlertUsed(source.getAlertUsed());
        target.setDetailsAlertUsed(source.getDetailsAlertUsed());
        target.setDetailsResponseActivities(source.getDetailsResponseActivities());
        target.setDetailsGiven(source.getDetailsGiven());
        target.setEbs(EbsFacadeEjb.toReferenceDto(source.getEbs()));
        return target;
    }

    public EbsAlertDto saveAlert(EbsAlertDto dto) {
        EbsAlert ebsAlert = ebsAlertService.getByUuid(dto.getUuid());

        EbsAlert ado = fillOrBuildEntity(dto, ebsAlert, true);
        ebsAlertService.ensurePersisted(ado);
        return toDto(ado);
    }

    @Override
    public List<EbsAlertDto> findBy(RiskAssessmentCriteria criteria) {
        return ebsAlertService.findBy(criteria).stream().map(this::toDto).collect(Collectors.toList());
    }

    @LocalBean
    @Stateless
    public static class EbsAlertFacadeEjbLocal extends EbsAlertFacadeEjb {
    }
}
