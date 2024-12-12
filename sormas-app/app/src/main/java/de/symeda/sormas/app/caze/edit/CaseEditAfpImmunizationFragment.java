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
import de.symeda.sormas.api.utils.CardRecall;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.afpimmunization.AfpImmunization;
import de.symeda.sormas.app.backend.caze.Case;
import de.symeda.sormas.app.component.Item;
import de.symeda.sormas.app.databinding.FragmentCaseEditAfpImmunizationLayoutBinding;
import de.symeda.sormas.app.databinding.FragmentCaseEditAfpImmunizationLayoutBindingImpl;
import de.symeda.sormas.app.util.DataUtils;

public class CaseEditAfpImmunizationFragment extends BaseEditFragment<FragmentCaseEditAfpImmunizationLayoutBinding, AfpImmunization, Case> {

	private AfpImmunization record;
	private Case caze;
	private List<Item> cardRecallList;

	// Static methods

	public static CaseEditAfpImmunizationFragment newInstance(Case activityRootData) {
		return newInstanceWithFieldCheckers(
			CaseEditAfpImmunizationFragment.class,
			null,
			activityRootData,
			new FieldVisibilityCheckers(),
			UiFieldAccessCheckers.forSensitiveData(activityRootData.isPseudonymized()));
	}

	// Instance methods

	private void setUpControlListeners() {

	}

	// Overrides

	@Override
	protected String getSubHeadingTitle() {
		Resources r = getResources();
		return r.getString(R.string.caption_case_afp_immunization);
	}

	@Override
	public AfpImmunization getPrimaryData() {
		return record;
	}

	@Override
	protected void prepareFragmentData() {
		caze = getActivityRootData();
		record = caze.getAfpImmunization();
	}

	@Override
	public void onLayoutBinding(final FragmentCaseEditAfpImmunizationLayoutBinding contentBinding) {
		setUpControlListeners();
		contentBinding.setData(record);
		contentBinding.setCaze(caze);
		contentBinding.setYesNoClass(YesNo.class);
		contentBinding.afpImmunizationOpvDoseAtBirth.initializeDateField(getFragmentManager());
		contentBinding.afpImmunizationFirstDose.initializeDateField(getFragmentManager());
		contentBinding.afpImmunizationSecondDose.initializeDateField(getFragmentManager());
		contentBinding.afpImmunizationThirdDose.initializeDateField(getFragmentManager());
		contentBinding.afpImmunizationFourthDose.initializeDateField(getFragmentManager());
		contentBinding.afpImmunizationLastDose.initializeDateField(getFragmentManager());
		contentBinding.afpImmunizationDateLastOpvDosesReceivedThroughSia.initializeDateField(getFragmentManager());
		cardRecallList = DataUtils.getEnumItems(CardRecall.class);
		contentBinding.afpImmunizationDateLastIpvDosesReceivedThroughSia.initializeDateField(getFragmentManager());
		contentBinding.afpImmunizationSourceRiVaccinationInformation.initializeSpinner(cardRecallList);
	}

	@Override
	protected void onAfterLayoutBinding(FragmentCaseEditAfpImmunizationLayoutBinding contentBinding) {
		setFieldVisibilitiesAndAccesses(AfpImmunization.class, contentBinding.mainContent);

		if (caze.getDisease() != null) {
			super.hideFieldsForDisease(caze.getDisease(), contentBinding.mainContent, FormType.AFP_IMMUNIZATION_EDIT);
		}
	}

	@Override
	public int getEditLayout() {
		return R.layout.fragment_case_edit_afp_immunization_layout;
	}
}
