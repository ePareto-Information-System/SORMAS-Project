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
import de.symeda.sormas.api.utils.InjectionSite;
import de.symeda.sormas.api.utils.NormalWasted;
import de.symeda.sormas.api.utils.ParalysisSite;
import de.symeda.sormas.api.utils.SymptomLevel;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.caze.Case;
import de.symeda.sormas.app.backend.sixtyday.SixtyDay;
import de.symeda.sormas.app.component.Item;
import de.symeda.sormas.app.databinding.FragmentCaseEditSixtydayLayoutBinding;
import de.symeda.sormas.app.util.DataUtils;

public class CaseEditSixtyDayFragment extends BaseEditFragment<FragmentCaseEditSixtydayLayoutBinding, SixtyDay, Case> {

	private SixtyDay record;
	private Case caze;
	private List<Item> cardRecallList;

	// Static methods

	public static CaseEditSixtyDayFragment newInstance(Case activityRootData) {
		return newInstanceWithFieldCheckers(
			CaseEditSixtyDayFragment.class,
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
		return r.getString(R.string.caption_case_sixy_day);
	}

	@Override
	public SixtyDay getPrimaryData() {
		return record;
	}

	@Override
	protected void prepareFragmentData() {
		caze = getActivityRootData();
		record = caze.getSixtyDay();
	}

	@Override
	public void onLayoutBinding(final FragmentCaseEditSixtydayLayoutBinding contentBinding) {
		setUpControlListeners();
		contentBinding.setData(record);
		contentBinding.setCaze(caze);
		contentBinding.setYesNoClass(YesNo.class);
		contentBinding.setInjectionSiteClass(InjectionSite.class);
		contentBinding.setSymptomLevelClass(SymptomLevel.class);
		contentBinding.setNormalWastedClass(NormalWasted.class);
		contentBinding.setParalysisSiteClass(ParalysisSite.class);
		contentBinding.sixtyDayDateOfFollowup.initializeDateField(getFragmentManager());
		contentBinding.sixtyDayDateBirth.initializeDateField(getFragmentManager());
		contentBinding.sixtyDayDateSubmissionForms.initializeDateField(getFragmentManager());
	}

	@Override
	protected void onAfterLayoutBinding(FragmentCaseEditSixtydayLayoutBinding contentBinding) {
		setFieldVisibilitiesAndAccesses(SixtyDay.class, contentBinding.mainContent);

//		if (caze.getDisease() != null) {
//			super.hideFieldsForDisease(caze.getDisease(), contentBinding.mainContent, FormType.RISK_FACTOR_EDIT);
//		}
	}

	@Override
	public int getEditLayout() {
		return R.layout.fragment_case_edit_sixtyday_layout;
	}
}
