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

package de.symeda.sormas.app.ebs.edit;

import android.content.res.Resources;

import de.symeda.sormas.api.ebs.RiskAssessmentDto;
import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.backend.ebs.riskAssessment.RiskAssessment;
import de.symeda.sormas.app.databinding.FragmentRiskAssessmentEditLayoutBinding;


public class RiskAssessmentEditFragment extends BaseEditFragment<FragmentRiskAssessmentEditLayoutBinding, RiskAssessment, RiskAssessment> {

	public static final String TAG = RiskAssessmentEditFragment.class.getSimpleName();

	private RiskAssessment record;
	// Static methods

	public static RiskAssessmentEditFragment newInstance(Ebs activityRootData) {
		return newInstanceWithFieldCheckers(
			RiskAssessmentEditFragment.class,
			null,
			activityRootData,
			null,
			UiFieldAccessCheckers.forSensitiveData(activityRootData.isPseudonymized()));
	}
	public static RiskAssessmentEditFragment newInstance(RiskAssessment activityRootData) {
		return newInstanceWithFieldCheckers(
			RiskAssessmentEditFragment.class,
			null,
			activityRootData,
			null,
			UiFieldAccessCheckers.forSensitiveData(activityRootData.isPseudonymized()));
	}

	// Overrides

	@Override
	protected String getSubHeadingTitle() {
		Resources r = getResources();
		return r.getString(R.string.caption_risk_assessment);
	}

	@Override
	public RiskAssessment getPrimaryData() {
		return record;
	}

	@Override
	protected void prepareFragmentData() {
		record = getActivityRootData();
	}

	@Override
	public void onLayoutBinding(final FragmentRiskAssessmentEditLayoutBinding contentBinding) {
		contentBinding.setData(record);
		contentBinding.setYesNoClass(YesNo.class);
	}

	@Override
	protected void onAfterLayoutBinding(FragmentRiskAssessmentEditLayoutBinding contentBinding) {
		// Initialize ControlDateFields
		contentBinding.riskAssessmentAssessmentDate.initializeDateField(getFragmentManager());
		setFieldVisibilitiesAndAccesses(RiskAssessmentDto.class, contentBinding.mainContent);
	}

	@Override
	public int getEditLayout() {
		return R.layout.fragment_risk_assessment_edit_layout;
	}
}
