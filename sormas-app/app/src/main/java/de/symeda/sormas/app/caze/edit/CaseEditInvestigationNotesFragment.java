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

import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.investigationnotes.InvestigationNotesDto;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.caze.Case;
import de.symeda.sormas.app.backend.investigationnotes.InvestigationNotes;
import de.symeda.sormas.app.databinding.FragmentCaseEditInvestigationnotesLayoutBinding;

public class CaseEditInvestigationNotesFragment extends BaseEditFragment<FragmentCaseEditInvestigationnotesLayoutBinding, InvestigationNotes, Case> {

    private InvestigationNotes record;
    private Case caze;

    public static CaseEditInvestigationNotesFragment newInstance(Case activityRootData) {
        return newInstanceWithFieldCheckers(
                CaseEditInvestigationNotesFragment.class,
                null,
                activityRootData,
                new FieldVisibilityCheckers(),
                UiFieldAccessCheckers.forSensitiveData(activityRootData.isPseudonymized()));
    }

    @Override
    protected String getSubHeadingTitle() {
        Resources r = getResources();
        return r.getString(R.string.caption_case_investigation_notes);
    }

    @Override
    public InvestigationNotes getPrimaryData() {
        return record;
    }

    @Override
    protected void prepareFragmentData() {
        caze = getActivityRootData();
        record = caze.getInvestigationNotes();
    }

    @Override
    public void onLayoutBinding(final FragmentCaseEditInvestigationnotesLayoutBinding contentBinding) {
        contentBinding.setData(record);
        contentBinding.setCaze(caze);
        contentBinding.investigationNotesInvestigatorDate.initializeDateField(getFragmentManager());
        contentBinding.investigationNotesDateOfCompletionOfForm.initializeDateField(getFragmentManager());

        if (caze.getDisease() != null) {
            super.hideFieldsForDisease(caze.getDisease(), contentBinding.mainContent, FormType.INVESTIGATION_NOTES_EDIT);
        }
    }

    @Override
    protected void onAfterLayoutBinding(FragmentCaseEditInvestigationnotesLayoutBinding contentBinding) {
        setFieldVisibilitiesAndAccesses(InvestigationNotesDto.class, contentBinding.mainContent);
    }

    @Override
    public int getEditLayout() {
        return R.layout.fragment_case_edit_investigationnotes_layout;
    }
}
