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

package de.symeda.sormas.app.caze.read;


import android.content.res.Resources;
import android.os.Bundle;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.app.BaseReadFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.caze.Case;
import de.symeda.sormas.app.backend.sixtyday.SixtyDay;
import de.symeda.sormas.app.databinding.FragmentCaseReadSixtydayLayoutBinding;
import de.symeda.sormas.app.util.DataUtils;

public class CaseReadSixtyDayFragment extends BaseReadFragment<FragmentCaseReadSixtydayLayoutBinding, SixtyDay, Case> {

	public static final String TAG = CaseReadSixtyDayFragment.class.getSimpleName();

	private Case caze;
	private SixtyDay record;
//	private MildModerateSevereCritical patientCondition;
	private Disease disease;

	// Static methods

	public static CaseReadSixtyDayFragment newInstance(Case activityRootData) {
		return newInstanceWithFieldCheckers(
			CaseReadSixtyDayFragment.class,
			null,
			activityRootData,
			new FieldVisibilityCheckers(),
			UiFieldAccessCheckers.forSensitiveData(activityRootData.isPseudonymized()));
	}

	// Overrides

	@Override
	protected void prepareFragmentData(Bundle savedInstanceState) {
		caze = getActivityRootData();
		record = caze.getSixtyDay();
		disease = caze.getDisease();
//		patientCondition = record.getPatientConditionOnAdmission();
	}

	@Override
	public void onLayoutBinding(FragmentCaseReadSixtydayLayoutBinding contentBinding) {
		contentBinding.setData(record);
		contentBinding.setCaze(caze);

	}

	@Override
	public void onAfterLayoutBinding(FragmentCaseReadSixtydayLayoutBinding contentBinding) {
		if (record.getParalysisWeaknessPresentSite() != null) {
			String paralysisWeaknessPresentSite = DataUtils.getEnumListString(record.getParalysisWeaknessPresentSite());
			contentBinding.sixtyDayParalysisWeaknessPresentSiteString.setValue(paralysisWeaknessPresentSite);
		}
//		if (disease != null) {
//			super.hideFieldsForDisease(disease, contentBinding.mainContent, FormType.SIXTY_DAY_FOLLOW_UP_EDIT);
//		}
	}

	@Override
	protected String getSubHeadingTitle() {
		Resources r = getResources();
		if (caze.getDisease() == Disease.FOODBORNE_ILLNESS) {
			return r.getString(R.string.caption_case_food_sample_testing);
		} else {
			return r.getString(R.string.caption_case_sixy_day);
		}
	}

	@Override
	public SixtyDay getPrimaryData() {
		return record;
	}

	@Override
	public int getReadLayout() {
		return R.layout.fragment_case_read_sixtyday_layout;
	}
}
