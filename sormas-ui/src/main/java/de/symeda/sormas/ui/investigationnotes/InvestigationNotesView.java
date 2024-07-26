package de.symeda.sormas.ui.investigationnotes;

import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.caze.AbstractCaseView;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;

public class InvestigationNotesView extends AbstractCaseView {

    public static final String VIEW_NAME = ROOT_VIEW_NAME + "/Investigation-Notes";

    public InvestigationNotesView() {
        super(VIEW_NAME, true);
    }

    @Override
    protected void initView(String params) {

        CommitDiscardWrapperComponent<InvestigationNotesForm> caseInvestigationNotesComponent =
                ControllerProvider.getCaseController().getInvestigationNotesComponent(getCaseRef().getUuid(), getViewMode(),true );
        setSubComponent(caseInvestigationNotesComponent);
        setEditPermission(caseInvestigationNotesComponent);
    }
}
