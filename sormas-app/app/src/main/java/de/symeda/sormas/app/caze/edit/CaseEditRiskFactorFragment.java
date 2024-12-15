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
import android.view.ViewGroup;

import androidx.databinding.ObservableArrayList;

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
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.patientsymptomsprecedence.PatientSymptomsPrecedence;
import de.symeda.sormas.app.backend.riskfactor.RiskFactor;
import de.symeda.sormas.app.component.Item;
import de.symeda.sormas.app.core.IEntryItemOnClickListener;
import de.symeda.sormas.app.databinding.FragmentCaseEditRiskfactorLayoutBinding;
import de.symeda.sormas.app.epidata.PersonTravelHistoryDialog;
import de.symeda.sormas.app.riskfactor.PatientSymptomsPrecedenceDialog;
import de.symeda.sormas.app.util.DataUtils;
import de.symeda.sormas.app.util.FieldVisibilityAndAccessHelper;

public class CaseEditRiskFactorFragment extends BaseEditFragment<FragmentCaseEditRiskfactorLayoutBinding, RiskFactor, Case> {

	private RiskFactor record;
	private Case caze;
	private List<Item> listDrinkingWaterSources;
	private IEntryItemOnClickListener onPatientSymptomsPrecedenceItemClickListener;

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

	private void setUpControlListeners(final FragmentCaseEditRiskfactorLayoutBinding contentBinding) {

		contentBinding.btnAddPatientSymptomsPrecedence.setOnClickListener(v -> {
			final PatientSymptomsPrecedence patientSymptomsPrecedence = DatabaseHelper.getPatientSymptomsPrecedenceDao().build();
			final PatientSymptomsPrecedenceDialog dialog =
					new PatientSymptomsPrecedenceDialog(CaseEditActivity.getActiveActivity(), patientSymptomsPrecedence, getActivityRootData(), true);
			dialog.setPositiveCallback(() -> addPatientSymptomsPrecedence(patientSymptomsPrecedence));
			dialog.show();
		});
		onPatientSymptomsPrecedenceItemClickListener = (v, item) -> {
			final PatientSymptomsPrecedence patientSymptomsPrecedence = (PatientSymptomsPrecedence) item;
			final PatientSymptomsPrecedence patientSymptomsPrecedenceClone = (PatientSymptomsPrecedence) patientSymptomsPrecedence.clone();
			final PatientSymptomsPrecedenceDialog dialog =
					new PatientSymptomsPrecedenceDialog(CaseEditActivity.getActiveActivity(), patientSymptomsPrecedenceClone, getActivityRootData(), false);
			dialog.setPositiveCallback(() -> {
				record.getPatientSymptomsPrecedences().set(record.getPatientSymptomsPrecedences().indexOf(patientSymptomsPrecedence), patientSymptomsPrecedenceClone);
				updatePatientSymptomsPrecedences();
			});
			dialog.setDeleteCallback(() -> {
				removePatientSymptomsPrecedence(patientSymptomsPrecedence);
				dialog.dismiss();
			});
			dialog.show();
		};
		contentBinding.setPatientSymptomsPrecedenceItemClickCallback(onPatientSymptomsPrecedenceItemClickListener);

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
		setUpControlListeners(contentBinding);
		contentBinding.setData(record);
		contentBinding.setCaze(caze);
		contentBinding.setYesNoClass(YesNo.class);
		contentBinding.riskFactorDateOfContactWithIllPerson.initializeDateField(getFragmentManager());

		contentBinding.setPatientSymptomsPrecedenceList(getPatientSymptomsPrecedences());
		contentBinding.setPatientSymptomsPrecedenceItemClickCallback(onPatientSymptomsPrecedenceItemClickListener);
		contentBinding.setPatientSymptomsPrecedenceListBindCallback(
				v -> FieldVisibilityAndAccessHelper
						.setFieldVisibilitiesAndAccesses(PatientSymptomsPrecedence.class, (ViewGroup) v, new FieldVisibilityCheckers(), getFieldAccessCheckers()));

		if (caze.getDisease() != null) {
			super.hideFieldsForDisease(caze.getDisease(), contentBinding.mainContent, FormType.RISK_FACTOR_EDIT);
		}
	}

	@Override
	protected void onAfterLayoutBinding(FragmentCaseEditRiskfactorLayoutBinding contentBinding) {
		setFieldVisibilitiesAndAccesses(RiskFactorDto.class, contentBinding.mainContent);
		contentBinding.riskFactorWaterUsedForDrinking.initializeSpinner(listDrinkingWaterSources);
	}

	private ObservableArrayList<PatientSymptomsPrecedence> getPatientSymptomsPrecedences() {
		ObservableArrayList<PatientSymptomsPrecedence> patientSymptomsPrecedences = new ObservableArrayList<>();
		patientSymptomsPrecedences.addAll(record.getPatientSymptomsPrecedences());
		return patientSymptomsPrecedences;
	}
	private void updatePatientSymptomsPrecedences() {
		getContentBinding().setPatientSymptomsPrecedenceList(getPatientSymptomsPrecedences());
	}
	private void addPatientSymptomsPrecedence(PatientSymptomsPrecedence patientSymptomsPrecedence) {
		record.getPatientSymptomsPrecedences().add(0, patientSymptomsPrecedence);
		updatePatientSymptomsPrecedences();
	}
	private void removePatientSymptomsPrecedence(PatientSymptomsPrecedence patientSymptomsPrecedence) {
		record.getPatientSymptomsPrecedences().remove(patientSymptomsPrecedence);
		updatePatientSymptomsPrecedences();
	}

	@Override
	public int getEditLayout() {
		return R.layout.fragment_case_edit_riskfactor_layout;
	}
}
