package de.symeda.sormas.ui.sixtydayfollowup;

import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.caze.AbstractCaseView;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;


@SuppressWarnings("serial")
public class SixtyDayFollowupView extends AbstractCaseView {

    public static final String VIEW_NAME = ROOT_VIEW_NAME + "/60-Days";

    public SixtyDayFollowupView() {
        super(VIEW_NAME, true);
    }

    @Override
    protected void initView(String params) {

        CommitDiscardWrapperComponent<SixtyDayForm> sixtyDayForm =
                ControllerProvider.getCaseController().getSixtyDayComponent(getCaseRef().getUuid(), getViewMode());
        setSubComponent(sixtyDayForm);
        setCaseEditPermission(sixtyDayForm);
    }
}
