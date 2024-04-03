package de.symeda.sormas.ui.riskfactor;

import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.caze.AbstractCaseView;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;

public class RiskFactorView extends AbstractCaseView {

        public static final String VIEW_NAME = ROOT_VIEW_NAME + "/riskFactor";

        public RiskFactorView() {
            super(VIEW_NAME, true);
        }

        @Override
        protected void initView(String params) {
            CommitDiscardWrapperComponent<RiskFactorForm> caseRiskFactorComponent =
                    ControllerProvider.getCaseController().getRiskFactorComponent(getCaseRef().getUuid(), getViewMode(),true );
            setSubComponent(caseRiskFactorComponent);
            setEditPermission(caseRiskFactorComponent);
        }

}
