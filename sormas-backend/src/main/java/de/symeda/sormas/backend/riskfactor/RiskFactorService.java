package de.symeda.sormas.backend.riskfactor;

import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.common.BaseAdoService;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class RiskFactorService extends BaseAdoService<RiskFactor> {
    public RiskFactorService() {
        super(RiskFactor.class);
    }

    public RiskFactor createRiskFactor() {
        RiskFactor riskFactor = new RiskFactor();
        riskFactor.setUuid(DataHelper.createUuid());
        return riskFactor;
    }
}
