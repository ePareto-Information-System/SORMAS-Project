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

import java.util.List;

import de.symeda.sormas.api.ebs.RiskAssesment;
import de.symeda.sormas.api.ebs.RiskAssessmentDto;
import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.backend.ebs.riskAssessment.RiskAssessment;
import de.symeda.sormas.app.component.Item;
import de.symeda.sormas.app.component.dialog.InfoDialog;
import de.symeda.sormas.app.databinding.FragmentRiskAssessmentEditLayoutBinding;
import de.symeda.sormas.app.util.DataUtils;


public class RiskAssessmentEditFragment extends BaseEditFragment<FragmentRiskAssessmentEditLayoutBinding, RiskAssessment, RiskAssessment> {

	public static final String TAG = RiskAssessmentEditFragment.class.getSimpleName();

	private RiskAssessment record;
	private List<Item> assessment;
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
		assessment = DataUtils.getEnumItems(RiskAssesment.class,true);
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
		contentBinding.riskAssessmentRiskAssessment.initializeSpinner(assessment);
		setFieldVisibilitiesAndAccesses(RiskAssessmentDto.class, contentBinding.mainContent);
		contentBinding.riskAssessmentMorbidityMortality.setOnCheckedChangeListener((buttonView, isChecked) -> updateRiskAssessment(contentBinding));
		contentBinding.riskAssessmentSpreadProbability.setOnCheckedChangeListener((buttonView, isChecked) -> updateRiskAssessment(contentBinding));
		contentBinding.riskAssessmentControlMeasures.setOnCheckedChangeListener((buttonView, isChecked) -> updateRiskAssessment(contentBinding));
		contentBinding.riskAssessmentRiskAssessment.addValueChangedListener(e->{
			if (e.getValue() == RiskAssesment.VERY_HIGH){
				contentBinding.riskAssessmentRiskAssessment.setBackgroundResource(R.drawable.background_risk_very_high_assessment);
			}if (e.getValue() == RiskAssesment.HIGH){
				contentBinding.riskAssessmentRiskAssessment.setBackgroundResource(R.drawable.background_risk_high_assessment);
			}if (e.getValue() == RiskAssesment.MEDIUM){
				contentBinding.riskAssessmentRiskAssessment.setBackgroundResource(R.drawable.background_risk_moderate_assessment);
			}if (e.getValue() == RiskAssesment.LOW){
				contentBinding.riskAssessmentRiskAssessment.setBackgroundResource(R.drawable.background_risk_low_assessment);
			}if (e.getValue() == null) {
                contentBinding.riskAssessmentRiskAssessment.initializeSpinner(assessment);
            }

		});
	}

	private void updateRiskAssessment(FragmentRiskAssessmentEditLayoutBinding binding) {
		String morbidityMortalityYes = binding.riskAssessmentMorbidityMortality.getValue()  == null ? "NO" : binding.riskAssessmentMorbidityMortality.getValue().toString() ;
		String spreadProbabilityYes = binding.riskAssessmentSpreadProbability.getValue()  == null ? "NO" : binding.riskAssessmentSpreadProbability.getValue().toString() ;
		String controlMeasuresYes = binding.riskAssessmentControlMeasures.getValue() == null ? "NO" : binding.riskAssessmentControlMeasures.getValue().toString() ;

		// Check conditions based on the checked values
		if (morbidityMortalityYes.equals("YES") && spreadProbabilityYes.equals("YES") && controlMeasuresYes.equals("NO")) {
			binding.riskAssessmentRiskAssessment.setValue(RiskAssesment.VERY_HIGH);
			binding.riskAssessmentRiskAssessment.setBackgroundResource(R.drawable.background_risk_very_high_assessment);

		} else if ((morbidityMortalityYes.equals("YES") && spreadProbabilityYes.equals("YES") && controlMeasuresYes.equals("YES")) ||
				(morbidityMortalityYes.equals("NO") && spreadProbabilityYes.equals("YES") && controlMeasuresYes.equals("NO")) ||
				(morbidityMortalityYes.equals("YES") && spreadProbabilityYes.equals("NO") && controlMeasuresYes.equals("NO"))) {
			binding.riskAssessmentRiskAssessment.setValue(RiskAssesment.HIGH);
			binding.riskAssessmentRiskAssessment.setBackgroundResource(R.drawable.background_risk_high_assessment);
		} else if ((morbidityMortalityYes.equals("NO") && spreadProbabilityYes.equals("NO") && controlMeasuresYes.equals("NO")) ||
				(morbidityMortalityYes.equals("YES") && spreadProbabilityYes.equals("NO") && controlMeasuresYes.equals("YES")) ||
				(morbidityMortalityYes.equals("NO") && spreadProbabilityYes.equals("YES") && controlMeasuresYes.equals("YES"))) {
			binding.riskAssessmentRiskAssessment.setValue(RiskAssesment.MEDIUM);
			binding.riskAssessmentRiskAssessment.setBackgroundResource(R.drawable.background_risk_moderate_assessment);
		} else if (morbidityMortalityYes.equals("NO") && spreadProbabilityYes.equals("NO") && controlMeasuresYes.equals("YES")) {
			binding.riskAssessmentRiskAssessment.setValue(RiskAssesment.LOW);
			binding.riskAssessmentRiskAssessment.setBackgroundResource(R.drawable.background_risk_low_assessment);
		}
		else {
			binding.riskAssessmentRiskAssessment.setValue(RiskAssesment.LOW);
			binding.riskAssessmentRiskAssessment.setBackgroundResource(R.drawable.background_risk_low_assessment);
		}
	}

	@Override
	public int getEditLayout() {
		return R.layout.fragment_risk_assessment_edit_layout;
	}
}
