package de.symeda.sormas.backend.ebs;

import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.common.BaseAdoService;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class RiskAssessmentService extends BaseAdoService<RiskAssessment> {

        public RiskAssessmentService() {super(RiskAssessment.class);}

        public RiskAssessment createRiskAssessment() {

            RiskAssessment riskAssessment = new RiskAssessment();
            riskAssessment.setUuid(DataHelper.createUuid());
            return riskAssessment;
        }
}
