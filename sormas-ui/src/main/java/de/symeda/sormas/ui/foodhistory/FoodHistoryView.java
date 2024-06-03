package de.symeda.sormas.ui.foodhistory;

import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.caze.AbstractCaseView;
import de.symeda.sormas.ui.utils.CommitDiscardWrapperComponent;

public class FoodHistoryView extends AbstractCaseView {

    public static final String VIEW_NAME = ROOT_VIEW_NAME + "/Food-History";

    public FoodHistoryView() {
        super(VIEW_NAME, true);
    }

    @Override
    protected void initView(String params) {

        CommitDiscardWrapperComponent<FoodHistoryForm> caseFoodHistoryComponent =
                ControllerProvider.getCaseController().getFoodHistoryComponent(getCaseRef().getUuid(), getViewMode(),true );
        setSubComponent(caseFoodHistoryComponent);
        setEditPermission(caseFoodHistoryComponent);
    }
}
