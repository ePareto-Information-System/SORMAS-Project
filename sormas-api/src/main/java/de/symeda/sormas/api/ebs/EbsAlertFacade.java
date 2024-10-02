package de.symeda.sormas.api.ebs;

import javax.ejb.Remote;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Remote
public interface EbsAlertFacade {
    EbsAlertDto saveAlert(@Valid EbsAlertDto dto);
    List<EbsAlertDto> findBy(RiskAssessmentCriteria criteria);
    List<EbsAlertDto> getAllAfter(Date date);
    EbsAlertDto getAlertByUuid(String uuid, boolean detailedReferences);
    List<String> getAllActiveUuids();
}
