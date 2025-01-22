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
import de.symeda.sormas.app.backend.patienttraveldetailsduring.PatientTravelDetailsDuring;
import de.symeda.sormas.app.backend.patienttraveldetailsprior.PatientTravelDetailsPrior;
import de.symeda.sormas.app.backend.riskfactor.RiskFactor;
import de.symeda.sormas.app.component.Item;
import de.symeda.sormas.app.core.IEntryItemOnClickListener;
import de.symeda.sormas.app.databinding.FragmentCaseEditRiskfactorLayoutBinding;
import de.symeda.sormas.app.epidata.PersonTravelHistoryDialog;
import de.symeda.sormas.app.riskfactor.PatientSymptomsPrecedenceDialog;
import de.symeda.sormas.app.riskfactor.PatientTravelDetailsDuringDialog;
import de.symeda.sormas.app.riskfactor.PatientTravelDetailsPriorDialog;
import de.symeda.sormas.app.util.DataUtils;
import de.symeda.sormas.app.util.FieldVisibilityAndAccessHelper;

public class CaseEditRiskFactorFragment extends BaseEditFragment<FragmentCaseEditRiskfactorLayoutBinding, RiskFactor, Case> {

	private RiskFactor record;
	private Case caze;
	private List<Item> listDrinkingWaterSources;
	private IEntryItemOnClickListener onPatientSymptomsPrecedenceItemClickListener;
	private IEntryItemOnClickListener onPatientTravelDetailsDuringItemClickListener;
	private IEntryItemOnClickListener onPatientTravelDetailsPriorItemClickListener;

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

		contentBinding.setPatientTravelDetailsDuringList(getPatientTravelDetailsDurings());
		contentBinding.setPatientTravelDetailsDuringItemClickCallback(onPatientTravelDetailsDuringItemClickListener);
		contentBinding.setPatientTravelDetailsDuringListBindCallback(
				v -> FieldVisibilityAndAccessHelper
						.setFieldVisibilitiesAndAccesses(PatientTravelDetailsDuring.class, (ViewGroup) v, new FieldVisibilityCheckers(), getFieldAccessCheckers()));

		contentBinding.setPatientTravelDetailsPriorList(getPatientTravelDetailsPriors());
		contentBinding.setPatientTravelDetailsPriorItemClickCallback(onPatientTravelDetailsDuringItemClickListener);
		contentBinding.setPatientTravelDetailsPriorListBindCallback(
				v -> FieldVisibilityAndAccessHelper
						.setFieldVisibilitiesAndAccesses(PatientTravelDetailsPrior.class, (ViewGroup) v, new FieldVisibilityCheckers(), getFieldAccessCheckers()));

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
		if (getActivityRootData() == null) {
			contentBinding.patientTravelDetailsDuringLayout.setVisibility(GONE);
		}
		if (getActivityRootData() == null) {
			contentBinding.patientTravelDetailsPriorLayout.setVisibility(GONE);
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
		contentBinding.btnAddPatientTravelDetailsDuring.setOnClickListener(v -> {
			final PatientTravelDetailsDuring patientTravelDetailsDuring = DatabaseHelper.getPatientTravelDetailsDuringDao().build();
			final PatientTravelDetailsDuringDialog dialog =
					new PatientTravelDetailsDuringDialog(CaseEditActivity.getActiveActivity(), patientTravelDetailsDuring, getActivityRootData(), true);
			dialog.setPositiveCallback(() -> addPatientTravelDetailsDuring(patientTravelDetailsDuring));
			dialog.show();
		});
		contentBinding.btnAddPatientTravelDetailsPrior.setOnClickListener(v -> {
			final PatientTravelDetailsPrior patientTravelDetailsPrior = DatabaseHelper.getPatientTravelDetailsPriorDao().build();
			final PatientTravelDetailsPriorDialog dialog =
					new PatientTravelDetailsPriorDialog(CaseEditActivity.getActiveActivity(), patientTravelDetailsPrior, getActivityRootData(), true);
			dialog.setPositiveCallback(() -> addPatientTravelDetailsPrior(patientTravelDetailsPrior));
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

		contentBinding.riskFactorPatientTravelledAnywhere3WeeksPrior.addValueChangedListener(field -> {
			YesNo value = (YesNo) field.getValue();
			contentBinding.patientTravelDetailsPriorLayout.setVisibility(value == YesNo.YES ? VISIBLE : GONE);
			if (value != YesNo.YES) {
				clearPatientTravelDetailsPriors();
			}

			getContentBinding().riskFactorPatientTravelledAnywhere3WeeksPrior.setEnabled(getPatientTravelDetailsPriorList().isEmpty());
		});
		contentBinding.riskFactorPatientTravelledPeriodOfIllness.addValueChangedListener(field -> {
			YesNo value = (YesNo) field.getValue();
			contentBinding.patientTravelDetailsDuringLayout.setVisibility(value == YesNo.YES ? VISIBLE : GONE);
			if (value != YesNo.YES) {
				clearPatientTravelDetailsDurings();
			}

			getContentBinding().riskFactorPatientTravelledPeriodOfIllness.setEnabled(getPatientTravelDetailsDuringList().isEmpty());
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

		onPatientTravelDetailsDuringItemClickListener = (v, item) -> {
			PatientTravelDetailsDuring patientTravelDetailsDuring = (PatientTravelDetailsDuring) item;
			final PatientTravelDetailsDuring patientTravelDetailsDuringClone = (PatientTravelDetailsDuring) patientTravelDetailsDuring.clone();
			final PatientTravelDetailsDuringDialog dialog =
					new PatientTravelDetailsDuringDialog(CaseEditActivity.getActiveActivity(), patientTravelDetailsDuringClone, getActivityRootData(), false);
			dialog.setPositiveCallback(() -> {
				patientTravelDetailsDuring.setDateOfTravel(dialog.getData().getDateOfTravel());
				patientTravelDetailsDuring.setPlaceOfTravel(dialog.getData().getPlaceOfTravel());

				record.getPatientTravelDetailsDurings().set(record.getPatientTravelDetailsDurings().indexOf(patientTravelDetailsDuring), patientTravelDetailsDuringClone);
				updatePatientTravelDetailsDurings();
			});
			dialog.setDeleteCallback(() -> {
				removePatientTravelDetailsDuring(patientTravelDetailsDuring);
				dialog.dismiss();
			});
			dialog.show();
		};
		contentBinding.setPatientTravelDetailsDuringItemClickCallback(onPatientTravelDetailsDuringItemClickListener);

		onPatientTravelDetailsPriorItemClickListener = (v, item) -> {
			PatientTravelDetailsPrior patientTravelDetailsPrior = (PatientTravelDetailsPrior) item;
			final PatientTravelDetailsPrior patientTravelDetailsPriorClone = (PatientTravelDetailsPrior) patientTravelDetailsPrior.clone();
			final PatientTravelDetailsPriorDialog dialog =
					new PatientTravelDetailsPriorDialog(CaseEditActivity.getActiveActivity(), patientTravelDetailsPriorClone, getActivityRootData(), false);
			dialog.setPositiveCallback(() -> {
				patientTravelDetailsPrior.setDateOfTravel(dialog.getData().getDateOfTravel());
				patientTravelDetailsPrior.setPlaceOfTravel(dialog.getData().getPlaceOfTravel());

				record.getPatientTravelDetailsPriors().set(record.getPatientTravelDetailsPriors().indexOf(patientTravelDetailsPrior), patientTravelDetailsPriorClone);
				updatePatientTravelDetailsPriors();
			});
			dialog.setDeleteCallback(() -> {
				removePatientTravelDetailsPrior(patientTravelDetailsPrior);
				dialog.dismiss();
			});
			dialog.show();
		};
		contentBinding.setPatientTravelDetailsPriorItemClickCallback(onPatientTravelDetailsPriorItemClickListener);
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

	//during
	private void addPatientTravelDetailsDuring(PatientTravelDetailsDuring patientTravelDetailsDuring) {
		record.getPatientTravelDetailsDurings().add(0, patientTravelDetailsDuring);
		updatePatientTravelDetailsDurings();
	}
	private void updatePatientTravelDetailsDurings() {
		getContentBinding().setPatientTravelDetailsDuringList(getPatientTravelDetailsDurings());
	}
	private ObservableArrayList<PatientTravelDetailsDuring> getPatientTravelDetailsDurings() {
		ObservableArrayList<PatientTravelDetailsDuring> patientTravelDetailsDurings = new ObservableArrayList<>();
		patientTravelDetailsDurings.addAll(record.getPatientTravelDetailsDurings());
		return patientTravelDetailsDurings;
	}
	private void removePatientTravelDetailsDuring(PatientTravelDetailsDuring patientTravelDetailsDuring) {
		record.getPatientTravelDetailsDurings().remove(patientTravelDetailsDuring);
		updatePatientTravelDetailsDurings();
	}

	private void clearPatientTravelDetailsDurings() {
		record.getPatientTravelDetailsDurings().clear();
		updatePatientTravelDetailsDurings();
	}

	private ObservableArrayList<PatientTravelDetailsDuring> getPatientTravelDetailsDuringList() {
		ObservableArrayList<PatientTravelDetailsDuring> patientTravelDetailsDuring = new ObservableArrayList<>();
		patientTravelDetailsDuring.addAll(record.getPatientTravelDetailsDurings());
		return patientTravelDetailsDuring;
	}

	//prior
	private void addPatientTravelDetailsPrior(PatientTravelDetailsPrior patientTravelDetailsPrior) {
		record.getPatientTravelDetailsPriors().add(0, patientTravelDetailsPrior);
		updatePatientTravelDetailsPriors();
	}
	private void updatePatientTravelDetailsPriors() {
		getContentBinding().setPatientTravelDetailsPriorList(getPatientTravelDetailsPriors());
	}
	private ObservableArrayList<PatientTravelDetailsPrior> getPatientTravelDetailsPriors() {
		ObservableArrayList<PatientTravelDetailsPrior> patientTravelDetailsPriors = new ObservableArrayList<>();
		patientTravelDetailsPriors.addAll(record.getPatientTravelDetailsPriors());
		return patientTravelDetailsPriors;
	}
	private void removePatientTravelDetailsPrior(PatientTravelDetailsPrior patientTravelDetailsPrior) {
		record.getPatientTravelDetailsPriors().remove(patientTravelDetailsPrior);
		updatePatientTravelDetailsPriors();
	}

	private void clearPatientTravelDetailsPriors() {
		record.getPatientTravelDetailsPriors().clear();
		updatePatientTravelDetailsPriors();
	}

	private ObservableArrayList<PatientTravelDetailsPrior> getPatientTravelDetailsPriorList() {
		ObservableArrayList<PatientTravelDetailsPrior> patientTravelDetailsPrior = new ObservableArrayList<>();
		patientTravelDetailsPrior.addAll(record.getPatientTravelDetailsPriors());
		return patientTravelDetailsPrior;
	}
}
