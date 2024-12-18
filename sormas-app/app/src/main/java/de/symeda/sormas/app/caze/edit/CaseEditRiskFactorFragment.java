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
import static android.view.View.VISIBLE;

import android.content.res.Resources;
import android.view.ViewGroup;

import androidx.databinding.ObservableArrayList;

import com.googlecode.openbeans.Introspector;
import com.googlecode.openbeans.PropertyDescriptor;

import java.util.List;

import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.riskfactor.DrinkingWaterSource;
import de.symeda.sormas.api.riskfactor.RiskFactorDto;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.activityascase.ActivityAsCase;
import de.symeda.sormas.app.backend.caze.Case;
import de.symeda.sormas.app.backend.common.AbstractDomainObject;
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

	public static CaseEditRiskFactorFragment newInstance(Case activityRootData) {
		return newInstanceWithFieldCheckers(
				CaseEditRiskFactorFragment.class,
				null,
				activityRootData,
				new FieldVisibilityCheckers(),
				UiFieldAccessCheckers.forSensitiveData(activityRootData.isPseudonymized()));
	}

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

	public void setDefaultValues(RiskFactor riskFactorDto) {
		if (riskFactorDto == null) {
			return;
		}
		try {
			for (PropertyDescriptor pd : Introspector.getBeanInfo(RiskFactor.class, AbstractDomainObject.class).getPropertyDescriptors()) {
				if (pd.getWriteMethod() != null && (pd.getReadMethod().getReturnType().equals(YesNo.class))) {
					try {
						if (pd.getReadMethod().invoke(riskFactorDto) == null)
							pd.getWriteMethod().invoke(riskFactorDto, YesNo.NO);
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
	protected void onAfterLayoutBinding(FragmentCaseEditRiskfactorLayoutBinding contentBinding) {
		setFieldVisibilitiesAndAccesses(RiskFactorDto.class, contentBinding.mainContent);

		if (getActivityRootData() == null) {
			contentBinding.patientSymptomsPrecedenceLayout.setVisibility(GONE);
		}
		contentBinding.riskFactorWaterUsedForDrinking.initializeSpinner(listDrinkingWaterSources);
	}

	@Override
	public int getEditLayout() {
		return R.layout.fragment_case_edit_riskfactor_layout;
	}

	private void setUpControlListeners(final FragmentCaseEditRiskfactorLayoutBinding contentBinding) {
		contentBinding.btnAddPatientSymptomsPrecedence.setOnClickListener(v -> {
			final PatientSymptomsPrecedence patientSymptomsPrecedence = DatabaseHelper.getPatientSymptomsPrecedenceDao().build();
			final PatientSymptomsPrecedenceDialog dialog =
					new PatientSymptomsPrecedenceDialog(CaseEditActivity.getActiveActivity(), patientSymptomsPrecedence, getActivityRootData(), true);
			dialog.setPositiveCallback(() -> addPatientSymptomsPrecedence(patientSymptomsPrecedence));
			dialog.show();
		});
		contentBinding.riskFactorDuring3WeeksPatientContactWithSimilarSymptoms.addValueChangedListener(field -> {
			YesNo value = (YesNo) field.getValue();
			contentBinding.patientSymptomsPrecedenceLayout.setVisibility(value == YesNo.YES ? VISIBLE : GONE);
			if (value != YesNo.YES) {
				clearPatientSymptomsPrecedences();
			}

			getContentBinding().riskFactorDuring3WeeksPatientContactWithSimilarSymptoms.setEnabled(getPatientSymptomsPrecedenceList().isEmpty());
		});

		onPatientSymptomsPrecedenceItemClickListener = (v, item) -> {
			PatientSymptomsPrecedence patientSymptomsPrecedence = (PatientSymptomsPrecedence) item;
			final PatientSymptomsPrecedence patientSymptomsPrecedenceClone = (PatientSymptomsPrecedence) patientSymptomsPrecedence.clone();
			final PatientSymptomsPrecedenceDialog dialog =
					new PatientSymptomsPrecedenceDialog(CaseEditActivity.getActiveActivity(), patientSymptomsPrecedenceClone, getActivityRootData(), false);
			dialog.setPositiveCallback(() -> {
				patientSymptomsPrecedence.setName(dialog.getData().getName());
				patientSymptomsPrecedence.setContactAddress(dialog.getData().getContactAddress());
				patientSymptomsPrecedence.setPhone(dialog.getData().getPhone());

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

	private void addPatientSymptomsPrecedence(PatientSymptomsPrecedence patientSymptomsPrecedence) {
		record.getPatientSymptomsPrecedences().add(0, patientSymptomsPrecedence);
		updatePatientSymptomsPrecedences();
	}
	private void updatePatientSymptomsPrecedences() {
		getContentBinding().setPatientSymptomsPrecedenceList(getPatientSymptomsPrecedences());
	}
	private ObservableArrayList<PatientSymptomsPrecedence> getPatientSymptomsPrecedences() {
		ObservableArrayList<PatientSymptomsPrecedence> patientSymptomsPrecedences = new ObservableArrayList<>();
		patientSymptomsPrecedences.addAll(record.getPatientSymptomsPrecedences());
		return patientSymptomsPrecedences;
	}
	private void removePatientSymptomsPrecedence(PatientSymptomsPrecedence patientSymptomsPrecedence) {
		record.getPatientSymptomsPrecedences().remove(patientSymptomsPrecedence);
		updatePatientSymptomsPrecedences();
	}

	private void clearPatientSymptomsPrecedences() {
		record.getPatientSymptomsPrecedences().clear();
		updatePatientSymptomsPrecedences();
	}

	private ObservableArrayList<PatientSymptomsPrecedence> getPatientSymptomsPrecedenceList() {
		ObservableArrayList<PatientSymptomsPrecedence> patientSymptomsPrecedence = new ObservableArrayList<>();
		patientSymptomsPrecedence.addAll(record.getPatientSymptomsPrecedences());
		return patientSymptomsPrecedence;
	}
}
