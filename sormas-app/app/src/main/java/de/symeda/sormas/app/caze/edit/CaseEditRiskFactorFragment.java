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
import de.symeda.sormas.api.riskfactor.DrinkingWaterSource;
import de.symeda.sormas.api.riskfactor.RiskFactorDto;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.caze.Case;
import de.symeda.sormas.app.backend.riskfactor.RiskFactor;
import de.symeda.sormas.app.component.Item;
import de.symeda.sormas.app.databinding.FragmentCaseEditRiskfactorLayoutBinding;
import de.symeda.sormas.app.util.DataUtils;

public class CaseEditRiskFactorFragment extends BaseEditFragment<FragmentCaseEditRiskfactorLayoutBinding, RiskFactor, Case> {

	private RiskFactor record;
	private Case caze;
	private List<Item> listDrinkingWaterSources;

	// Static methods

	public static CaseEditRiskFactorFragment newInstance(Case activityRootData) {
		return newInstanceWithFieldCheckers(
			CaseEditRiskFactorFragment.class,
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
		return r.getString(R.string.caption_case_riskfactor);
	}

	@Override
	public RiskFactor getPrimaryData() {
		return record;
	}

	@Override
	protected void prepareFragmentData() {
		caze = getActivityRootData();
		record = caze.getRiskFactor();
		listDrinkingWaterSources = DataUtils.getEnumItems(DrinkingWaterSource.class, true);
	}

	@Override
	public void onLayoutBinding(final FragmentCaseEditRiskfactorLayoutBinding contentBinding) {
		setUpControlListeners();
		contentBinding.setData(record);
		contentBinding.setCaze(caze);
		contentBinding.setYesNoClass(YesNo.class);
		if (caze.getDisease() != null) {
			super.hideFieldsForDisease(caze.getDisease(), contentBinding.mainContent, FormType.RISK_FACTOR_EDIT);
		}
	}

	@Override
	protected void onAfterLayoutBinding(FragmentCaseEditRiskfactorLayoutBinding contentBinding) {
		setFieldVisibilitiesAndAccesses(RiskFactorDto.class, contentBinding.mainContent);
		contentBinding.riskFactorWaterUsedForDrinking.initializeSpinner(listDrinkingWaterSources);
	}

	@Override
	public int getEditLayout() {
		return R.layout.fragment_case_edit_riskfactor_layout;
	}
}
