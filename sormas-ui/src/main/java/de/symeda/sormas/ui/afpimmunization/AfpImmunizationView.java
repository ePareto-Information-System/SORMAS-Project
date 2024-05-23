package de.symeda.sormas.ui.afpimmunization;

import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.caze.AbstractCaseView;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;

public class AfpImmunizationView extends AbstractCaseView {

    public static final String VIEW_NAME = ROOT_VIEW_NAME + "/AFP-Immunization";

    public AfpImmunizationView() {
        super(VIEW_NAME, true);
    }

    @Override
    protected void initView(String params) {

        CommitDiscardWrapperComponent<AfpImmunizationForm> caseAfpImmunizationComponent =
                ControllerProvider.getCaseController().getAfpImmunizationComponent(getCaseRef().getUuid(), getViewMode(),true );
        setSubComponent(caseAfpImmunizationComponent);
        setEditPermission(caseAfpImmunizationComponent);
    }
}
