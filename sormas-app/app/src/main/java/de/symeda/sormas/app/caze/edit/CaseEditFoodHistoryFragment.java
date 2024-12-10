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

import static android.view.View.GONE;

import android.content.res.Resources;
import android.util.Log;
import android.view.ViewGroup;

import androidx.databinding.ObservableArrayList;

import com.googlecode.openbeans.Introspector;
import com.googlecode.openbeans.PropertyDescriptor;

import java.util.List;

import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.foodhistory.FoodHistoryDto;
import de.symeda.sormas.api.utils.EventType;
import de.symeda.sormas.api.utils.FoodSource;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.affectedperson.AffectedPerson;
import de.symeda.sormas.app.backend.caze.Case;
import de.symeda.sormas.app.backend.common.AbstractDomainObject;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.foodhistory.FoodHistory;
import de.symeda.sormas.app.component.Item;
import de.symeda.sormas.app.core.IEntryItemOnClickListener;
import de.symeda.sormas.app.databinding.FragmentCaseEditFoodhistoryLayoutBinding;
import de.symeda.sormas.app.foodhistory.AffectedPersonDialog;
import de.symeda.sormas.app.util.DataUtils;
import de.symeda.sormas.app.util.FieldVisibilityAndAccessHelper;

public class CaseEditFoodHistoryFragment extends BaseEditFragment<FragmentCaseEditFoodhistoryLayoutBinding, FoodHistory, Case> {

    private FoodHistory record;
    private Case caze;
    private List<Item> foodSourceList;
    private List<Item> eventTypeList;
    private IEntryItemOnClickListener onAffectedPersonItemClickListener;

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
        setUpControlListeners(contentBinding);
        setDefaultValues(record);
        contentBinding.setData(record);

        contentBinding.setYesNoClass(YesNo.class);
        contentBinding.foodHistoryFoodSource.initializeSpinner(foodSourceList);
        contentBinding.foodHistoryEventType.initializeSpinner(eventTypeList);
        contentBinding.foodHistoryDateConsumed.initializeDateTimeField(getFragmentManager());

        contentBinding.setAffectedPersonList(getAffectedPersons());
        contentBinding.setAffectedPersonItemClickCallback(onAffectedPersonItemClickListener);
        contentBinding.setAffectedPersonListBindCallback(
                v -> FieldVisibilityAndAccessHelper
                        .setFieldVisibilitiesAndAccesses(AffectedPerson.class, (ViewGroup) v, new FieldVisibilityCheckers(), getFieldAccessCheckers()));

        if (caze.getDisease() != null) {
            super.hideFieldsForDisease(caze.getDisease(), contentBinding.mainContent, FormType.FOOD_HISTORY_EDIT);
        }
    }

    public void setDefaultValues(FoodHistory foodHistoryDto) {
        if (foodHistoryDto == null) {
            return;
        }

        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(FoodHistory.class, AbstractDomainObject.class).getPropertyDescriptors()) {
                if (pd.getWriteMethod() != null && (pd.getReadMethod().getReturnType().equals(YesNo.class))) {
                    try {
                        if (pd.getReadMethod().invoke(foodHistoryDto) == null)
                            pd.getWriteMethod().invoke(foodHistoryDto, YesNo.NO);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onAfterLayoutBinding(FragmentCaseEditFoodhistoryLayoutBinding contentBinding) {
        setFieldVisibilitiesAndAccesses(FoodHistoryDto.class, contentBinding.mainContent);

        if (getActivityRootData() == null) {
            contentBinding.affectedPersonLayout.setVisibility(GONE);
        }

    }
    @Override
    public int getEditLayout() {
        return R.layout.fragment_case_edit_foodhistory_layout;
    }

    private void setUpControlListeners(final FragmentCaseEditFoodhistoryLayoutBinding contentBinding) {

        contentBinding.btnAddAffectedPerson.setOnClickListener(v -> {
            final AffectedPerson affectedPerson = DatabaseHelper.getAffectedPersonDao().build();
            final AffectedPersonDialog dialog =
                    new AffectedPersonDialog(CaseEditActivity.getActiveActivity(), affectedPerson, getActivityRootData(), true);
            dialog.setPositiveCallback(() -> addAffectedPerson(affectedPerson));
            dialog.show();
        });
        onAffectedPersonItemClickListener = (v, item) -> {
            final AffectedPerson affectedPerson = (AffectedPerson) item;
            final AffectedPerson affectedPersonClone = (AffectedPerson) affectedPerson.clone();
            final AffectedPersonDialog dialog =
                    new AffectedPersonDialog(CaseEditActivity.getActiveActivity(), affectedPersonClone, getActivityRootData(), false);
            dialog.setPositiveCallback(() -> {
                record.getAffectedPersons().set(record.getAffectedPersons().indexOf(affectedPerson), affectedPersonClone);
                updateAffectedPersons();
            });
            dialog.setDeleteCallback(() -> {
                removeAffectedPerson(affectedPerson);
                dialog.dismiss();
            });
            dialog.show();
        };
        contentBinding.setAffectedPersonItemClickCallback(onAffectedPersonItemClickListener);

    }

    private void addAffectedPerson(AffectedPerson affectedPerson) {
        record.getAffectedPersons().add(0, affectedPerson);
        updateAffectedPersons();
    }

    private void updateAffectedPersons() {
        getContentBinding().setAffectedPersonList(getAffectedPersons());
    }

    private ObservableArrayList<AffectedPerson> getAffectedPersons() {
        ObservableArrayList<AffectedPerson> affectedPersons = new ObservableArrayList<>();
        affectedPersons.addAll(record.getAffectedPersons());
        return affectedPersons;
    }

    private void removeAffectedPerson(AffectedPerson affectedPerson) {
        record.getAffectedPersons().remove(affectedPerson);
        updateAffectedPersons();
    }

}
