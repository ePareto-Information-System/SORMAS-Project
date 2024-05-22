package de.symeda.sormas.api.ebs;

import javax.ejb.Remote;
import javax.validation.Valid;
import java.util.List;

@Remote
public interface EbsAlertFacade {
    EbsAlertDto saveAlert(@Valid EbsAlertDto dto);
    List<EbsAlertDto> findBy(RiskAssessmentCriteria criteria);
}
