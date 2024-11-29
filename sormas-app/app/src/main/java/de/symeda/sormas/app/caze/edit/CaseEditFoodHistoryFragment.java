/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package de.symeda.sormas.app.caze.edit;

import android.content.res.Resources;

import java.util.List;

import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.foodhistory.FoodHistoryDto;
import de.symeda.sormas.api.utils.EventType;
import de.symeda.sormas.api.utils.FoodSource;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.caze.Case;
import de.symeda.sormas.app.backend.foodhistory.FoodHistory;
import de.symeda.sormas.app.component.Item;
import de.symeda.sormas.app.databinding.FragmentCaseEditFoodhistoryLayoutBinding;
import de.symeda.sormas.app.util.DataUtils;

public class CaseEditFoodHistoryFragment extends BaseEditFragment<FragmentCaseEditFoodhistoryLayoutBinding, FoodHistory, Case> {

    private List<Item> foodSourceList;
    private List<Item> eventTypeList;

    private FoodHistory record;
    private Case caze;

    public static CaseEditFoodHistoryFragment newInstance(Case activityRootData) {
        return newInstanceWithFieldCheckers(
                CaseEditFoodHistoryFragment.class,
                null,
                activityRootData,
                new FieldVisibilityCheckers(),
                UiFieldAccessCheckers.forSensitiveData(activityRootData.isPseudonymized()));
    }

    @Override
    protected String getSubHeadingTitle() {
        Resources r = getResources();
        return r.getString(R.string.caption_case_food_history);
    }
    @Override
    public FoodHistory getPrimaryData() {
        return record;
    }
    @Override
    protected void prepareFragmentData() {
        caze = getActivityRootData();
        record = caze.getFoodHistory();
        foodSourceList = DataUtils.getEnumItems(FoodSource.class, true);
        eventTypeList = DataUtils.getEnumItems(EventType.class, true);
    }

    @Override
    public void onLayoutBinding(final FragmentCaseEditFoodhistoryLayoutBinding contentBinding) {
        contentBinding.setData(record);
        contentBinding.setCaze(caze);
        contentBinding.setYesNoClass(YesNo.class);

        contentBinding.foodHistoryFoodSource.initializeSpinner(foodSourceList);
        contentBinding.foodHistoryEventType.initializeSpinner(eventTypeList);
        contentBinding.foodHistoryDateConsumed.initializeDateTimeField(getFragmentManager());

        if (caze.getDisease() != null) {
            super.hideFieldsForDisease(caze.getDisease(), contentBinding.mainContent, FormType.FOOD_HISTORY_EDIT);
        }
    }

    @Override
    protected void onAfterLayoutBinding(FragmentCaseEditFoodhistoryLayoutBinding contentBinding) {
        setFieldVisibilitiesAndAccesses(FoodHistoryDto.class, contentBinding.mainContent);

    }
    @Override
    public int getEditLayout() {
        return R.layout.fragment_case_edit_foodhistory_layout;
    }

}
