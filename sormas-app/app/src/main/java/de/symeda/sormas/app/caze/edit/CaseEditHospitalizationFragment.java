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
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.ObservableArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.hospitalization.HospitalizationDto;
import de.symeda.sormas.api.hospitalization.HospitalizationReasonType;
import de.symeda.sormas.api.hospitalization.PreviousHospitalizationDto;
import java.util.List;

import de.symeda.sormas.api.hospitalization.SymptomsList;
import de.symeda.sormas.api.sample.PathogenTestType;
import de.symeda.sormas.api.utils.DurationHours;
import de.symeda.sormas.api.utils.InpatOutpat;
import de.symeda.sormas.api.utils.MildModerateSevereCritical;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.symeda.sormas.api.caze.CaseOutcome;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.caze.Case;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.hospitalization.Hospitalization;
import de.symeda.sormas.app.backend.hospitalization.PreviousHospitalization;
import de.symeda.sormas.app.backend.location.Location;
import de.symeda.sormas.app.backend.region.Community;
import de.symeda.sormas.app.backend.region.District;
import de.symeda.sormas.app.backend.region.Region;
import de.symeda.sormas.app.component.Item;
import de.symeda.sormas.app.core.IEntryItemOnClickListener;
import de.symeda.sormas.app.databinding.FragmentCaseEditHospitalizationLayoutBinding;
import de.symeda.sormas.app.util.DataUtils;
import de.symeda.sormas.app.util.FieldVisibilityAndAccessHelper;
import de.symeda.sormas.app.util.DataUtils;
//import de.symeda.sormas.app.util.InfrastructureHelper;
import de.symeda.sormas.app.util.InfrastructureDaoHelper;
import de.symeda.sormas.app.util.InfrastructureFieldsDependencyHandler;

public class CaseEditHospitalizationFragment extends BaseEditFragment<FragmentCaseEditHospitalizationLayoutBinding, Hospitalization, Case> {

	private Hospitalization record;
	private Case caze;
	private List<Item> patientCondition;

	private IEntryItemOnClickListener onPrevHosItemClickListener;
	private List<Item> outcomeList;
	private List<Item> inpatientOutpatientList;
	private Disease disease;


	// Static methods

	public static CaseEditHospitalizationFragment newInstance(Case activityRootData) {
		return newInstanceWithFieldCheckers(
			CaseEditHospitalizationFragment.class,
			null,
			activityRootData,
			new FieldVisibilityCheckers(),
			UiFieldAccessCheckers.forSensitiveData(activityRootData.isPseudonymized()));
	}

	// Instance methods

	private void setUpControlListeners() {
		onPrevHosItemClickListener = (v, item) -> {
			final PreviousHospitalization previousHospitalization = (PreviousHospitalization) item;
			final PreviousHospitalization previousHospitalizationClone = (PreviousHospitalization) previousHospitalization.clone();
			final PreviousHospitalizationDialog dialog =
				new PreviousHospitalizationDialog(CaseEditActivity.getActiveActivity(), previousHospitalizationClone, false);

			dialog.setPositiveCallback(() -> {
				record.getPreviousHospitalizations()
					.set(record.getPreviousHospitalizations().indexOf(previousHospitalization), previousHospitalizationClone);
				updatePreviousHospitalizations();
			});

			dialog.setDeleteCallback(() -> removePreviousHospitalization(previousHospitalization));

			dialog.show();
		};

		getContentBinding().btnAddPrevHosp.setOnClickListener(v -> {
			final PreviousHospitalization previousHospitalization = DatabaseHelper.getPreviousHospitalizationDao().build();
			final PreviousHospitalizationDialog dialog =
				new PreviousHospitalizationDialog(CaseEditActivity.getActiveActivity(), previousHospitalization, true);

			dialog.setPositiveCallback(() -> addPreviousHospitalization(previousHospitalization));

			dialog.setDeleteCallback(() -> removePreviousHospitalization(previousHospitalization));

			dialog.show();
		});
	}

	private ObservableArrayList<PreviousHospitalization> getPreviousHospitalizations() {
		ObservableArrayList<PreviousHospitalization> newPreHospitalizations = new ObservableArrayList<>();
		newPreHospitalizations.addAll(record.getPreviousHospitalizations());
		return newPreHospitalizations;
	}

	private void clearPreviousHospitalizations() {
		record.getPreviousHospitalizations().clear();
		updatePreviousHospitalizations();
	}

	private void removePreviousHospitalization(PreviousHospitalization item) {
		record.getPreviousHospitalizations().remove(item);
		updatePreviousHospitalizations();
	}

	private void updatePreviousHospitalizations() {
		getContentBinding().setPreviousHospitalizationList(getPreviousHospitalizations());

		verifyPrevHospitalizationStatus();
	}

	private void addPreviousHospitalization(PreviousHospitalization item) {
		record.getPreviousHospitalizations().add(0, item);
		updatePreviousHospitalizations();
	}

	private void verifyPrevHospitalizationStatus() {
		YesNo hospitalizedPreviously = record.getHospitalizedPreviously();
		if (hospitalizedPreviously == YesNo.YES && getPreviousHospitalizations().size() <= 0) {
			getContentBinding().caseHospitalizationHospitalizedPreviously.enableWarningState(R.string.validation_soft_add_list_entry);
		} else {
			getContentBinding().caseHospitalizationHospitalizedPreviously.disableWarningState();
		}

		getContentBinding().caseHospitalizationHospitalizedPreviously.setEnabled(getPreviousHospitalizations().size() == 0);
	}

	// Overrides

	@Override
	protected String getSubHeadingTitle() {
		Resources r = getResources();
		return r.getString(R.string.caption_case_hospitalization);
	}

	@Override
	public Hospitalization getPrimaryData() {
		return record;
	}

	@Override
	protected void prepareFragmentData() {
		caze = getActivityRootData();
		record = caze.getHospitalization();
		patientCondition = DataUtils.getEnumItems(MildModerateSevereCritical.class, true);
		outcomeList = DataUtils.getEnumItems(CaseOutcome.class, true);
		inpatientOutpatientList = DataUtils.getEnumItems(InpatOutpat.class, true);
		disease = caze.getDisease();
	}

	@Override
	public void onLayoutBinding(final FragmentCaseEditHospitalizationLayoutBinding contentBinding) {
		setUpControlListeners();

		CaseValidator.initializeHospitalizationValidation(contentBinding, caze);
		contentBinding.setYesNoClass(YesNo.class);
		contentBinding.setSymptomsListClass(SymptomsList.class);

		List<Item> hospitalizationReasons = DataUtils.getEnumItems(HospitalizationReasonType.class, true);
		List<Item> durationList = DataUtils.getEnumItems(DurationHours.class, true);
		List<Item> initialPlaceOfRegions = InfrastructureDaoHelper.loadRegionsByServerCountry();
		List<Item> initialPlaceOfDistricts = InfrastructureDaoHelper.loadDistricts(record.getSoughtRegion());
		List<Item> initialPlaceOfCommunities = InfrastructureDaoHelper.loadCommunities(record.getSoughtDistrict());
		List<Item> initialPlaceOfFacilities =
				InfrastructureDaoHelper.loadFacilities(record.getSoughtDistrict(), record.getSoughtCommunity(), null);

		contentBinding.setData(record);
		contentBinding.setCaze(caze);
		contentBinding.setYesNoClass(YesNo.class);
//		contentBinding.setPatientCondition(patientCondition);
		contentBinding.setPreviousHospitalizationList(getPreviousHospitalizations());
		contentBinding.setPrevHosItemClickCallback(onPrevHosItemClickListener);
		getContentBinding().setPreviousHospitalizationBindCallback(this::setFieldVisibilitiesAndAccesses);
		contentBinding.caseHospitalizationHospitalizationReason.initializeSpinner(hospitalizationReasons);
		contentBinding.caseHospitalizationDurationHours.initializeSpinner(durationList);
		contentBinding.setYesNoUnknownClass(YesNoUnknown.class);

		Set<SymptomsList> symptomList = Arrays.stream(SymptomsList.FoodBorne())
				.filter(c -> c != null)
				.filter(c -> fieldVisibilityCheckers.isVisible(SymptomsList.class, c.name()))
				.collect(Collectors.toSet());

		List<Item> compatibleItems = DataUtils.toItems(new ArrayList<>(symptomList));
		compatibleItems.removeIf(item -> item == null || item.toString().isEmpty());

		contentBinding.caseHospitalizationSymptomsSelected.initializeCheckBoxGroup(compatibleItems);

		contentBinding.caseHospitalizationHospitalizedPreviously.addValueChangedListener(field -> {
			YesNo value = (YesNo) field.getValue();
			contentBinding.prevHospitalizationsLayout.setVisibility(value == YesNo.YES ? View.VISIBLE : View.GONE);
			if (value != YesNo.YES) {
				clearPreviousHospitalizations();
			}

			verifyPrevHospitalizationStatus();
		});
		InfrastructureFieldsDependencyHandler.instance.initializeFacilityFields(
				record,
				contentBinding.caseHospitalizationSoughtRegion,
				initialPlaceOfRegions,
				record.getSoughtRegion(),
				contentBinding.caseHospitalizationSoughtDistrict,
				initialPlaceOfDistricts,
				record.getSoughtDistrict(),
				contentBinding.caseHospitalizationSoughtCommunity,
				initialPlaceOfCommunities,
				record.getSoughtCommunity(),
				null,
				null,
				null,
				null,
				null,
				null,
				contentBinding.caseHospitalizationNameOfFacility,
				initialPlaceOfFacilities,
				record.getNameOfFacility(),
				null,
				false);

		if (disease == Disease.MONKEYPOX) {
			/*Region responsibleRegion = caze.getResponsibleRegion();
			District responsibleDistrict = caze.getResponsibleDistrict();
			Community responsibleCommunity = caze.getResponsibleCommunity();

			Region locationTypeRegion = record.getLocationType() != null ? record.getLocationType().getRegion() : null;
			District locationTypeDistrict = record.getLocationType() != null ? record.getLocationType().getDistrict() : null;
			Community locationTypeCommunity = record.getLocationType() != null ? record.getLocationType().getCommunity() : null;

			InfrastructureFieldsDependencyHandler.instance.initializeRegionFields(
					contentBinding.locationTypeRegion,
					initialPlaceOfRegions,
					responsibleRegion != null ? responsibleRegion : locationTypeRegion,
					contentBinding.locationTypeDistrict,
					initialPlaceOfDistricts,
					responsibleDistrict != null ? responsibleDistrict : locationTypeDistrict,
					contentBinding.locationTypeCommunity,
					initialPlaceOfCommunities,
					responsibleCommunity != null ? responsibleCommunity : locationTypeCommunity
			);*/
//			if (record.getLocationType() == null){
//				record.setLocationType(new Location());
//			}

			InfrastructureFieldsDependencyHandler.instance.initializeRegionFields(
					contentBinding.locationTypeRegion,
					initialPlaceOfRegions,
					record.getLocationType().getRegion(),
					contentBinding.locationTypeDistrict,
					initialPlaceOfDistricts,
					record.getLocationType().getDistrict(),
					contentBinding.locationTypeCommunity,
					initialPlaceOfCommunities,
					record.getLocationType().getCommunity());


			if (contentBinding.caseHospitalizationAdmittedToHealthFacilityNew.getValue() == null) {
				contentBinding.locationTypeRegion.setVisibility(View.GONE);
				contentBinding.locationTypeDistrict.setVisibility(View.GONE);
				contentBinding.locationTypeCommunity.setVisibility(View.GONE);
				contentBinding.caseHospitalizationNameOfFacility.setVisibility(View.GONE);
				contentBinding.caseHospitalizationAdmissionDate.setVisibility(View.GONE);
				contentBinding.caseHospitalizationHospitalRecordNumber.setVisibility(View.GONE);
			} else {
				int visibility = (contentBinding.caseHospitalizationAdmittedToHealthFacilityNew.getValue() == YesNo.YES ? View.VISIBLE : View.GONE);
				contentBinding.locationTypeRegion.setVisibility(visibility);
				contentBinding.locationTypeDistrict.setVisibility(visibility);
				contentBinding.locationTypeCommunity.setVisibility(visibility);
				contentBinding.caseHospitalizationNameOfFacility.setVisibility(visibility);
				contentBinding.caseHospitalizationAdmissionDate.setVisibility(visibility);
				contentBinding.caseHospitalizationHospitalRecordNumber.setVisibility(visibility);
			}

			contentBinding.caseHospitalizationAdmittedToHealthFacilityNew.addValueChangedListener(field -> {
				int visibility = (field.getValue() == YesNo.YES ? View.VISIBLE : View.GONE);
				contentBinding.locationTypeRegion.setVisibility(visibility);
				contentBinding.locationTypeDistrict.setVisibility(visibility);
				contentBinding.locationTypeCommunity.setVisibility(visibility);
				contentBinding.caseHospitalizationNameOfFacility.setVisibility(visibility);
				contentBinding.caseHospitalizationAdmissionDate.setVisibility(visibility);
				contentBinding.caseHospitalizationHospitalRecordNumber.setVisibility(visibility);
			});

		}


		if (disease != null) {
			hideFieldsForDisease(disease, contentBinding.mainContent, FormType.HOSPITALIZATION_EDIT);
		}
	}

	@Override
	protected void onAfterLayoutBinding(FragmentCaseEditHospitalizationLayoutBinding contentBinding) {
		setFieldVisibilitiesAndAccesses(HospitalizationDto.class, contentBinding.mainContent);

		InfrastructureDaoHelper
			.initializeHealthFacilityDetailsFieldVisibility(contentBinding.caseDataHealthFacility, contentBinding.caseDataHealthFacilityDetails);

		// Initialize ControlDateFields
		contentBinding.caseHospitalizationAdmissionDate.initializeDateField(getFragmentManager());
		contentBinding.caseHospitalizationDateOfDeath.initializeDateField(getFragmentManager());
		contentBinding.caseHospitalizationDischargeDate.initializeDateField(getFragmentManager());
		contentBinding.caseHospitalizationIntensiveCareUnitStart.initializeDateField(getFragmentManager());
		contentBinding.caseHospitalizationIntensiveCareUnitEnd.initializeDateField(getFragmentManager());
		contentBinding.caseHospitalizationIsolationDate.initializeDateField(getFragmentManager());
		contentBinding.caseHospitalizationDateFirstSeen.initializeDateField(getFragmentManager());
		contentBinding.caseHospitalizationNotifyDistrictDate.initializeDateField(getFragmentManager());
		contentBinding.caseHospitalizationPatientConditionOnAdmission.initializeSpinner(patientCondition);
		contentBinding.caseHospitalizationDateFormSentToDistrict.initializeDateField(getFragmentManager());
		contentBinding.caseHospitalizationTerminationDateHospitalStay.initializeDateField(getFragmentManager());
		contentBinding.caseDataOutcome.initializeSpinner(outcomeList);
		contentBinding.caseHospitalizationSelectInpatientOutpatient.initializeSpinner(inpatientOutpatientList);
		contentBinding.caseHospitalizationDiseaseOnsetDate.initializeDateField(getFragmentManager());
		contentBinding.caseHospitalizationOnsetOfSymptomDatetime.initializeDateTimeField(getFragmentManager());
		contentBinding.caseHospitalizationDateOfVisitHospital.initializeDateField(getFragmentManager());

		contentBinding.caseDataHealthFacility.setCaption("Hospital Name");

		verifyPrevHospitalizationStatus();

		switch (disease){
			case AHF:
				handleAHF();
				break;
			case NEW_INFLUENZA:
				handleILI();
				break;
			case CSM:
				handleCSM();
				break;
			case IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS:
				handleIDSR();
				break;
			case MEASLES:
				handleMeasles();
				break;
			case GUINEA_WORM:
				handleGuineaWorm();
				contentBinding.caseHospitalizationAdmittedToHealthFacility.addValueChangedListener(field -> handleGuineaWorm());
				break;
			case YELLOW_FEVER:
				handleYF();
				break;
			default:
		}
	}

	@Override
	public int getEditLayout() {
		return R.layout.fragment_case_edit_hospitalization_layout;
	}

	private void setFieldVisibilitiesAndAccesses(View view) {
		FieldVisibilityAndAccessHelper.setFieldVisibilitiesAndAccesses(
			PreviousHospitalizationDto.class,
			(ViewGroup) view,
			new FieldVisibilityCheckers(),
			getFieldAccessCheckers());

	}

	private void handleHospitalizationVisibility(List<View> viewsToToggle) {
		// Initialize visibility based on the current value
		YesNo admittedToFacility = (YesNo) getContentBinding().caseHospitalizationAdmittedToHealthFacility.getValue();
		int initialVisibility = (admittedToFacility == YesNo.YES ? VISIBLE : GONE);
		for (View view : viewsToToggle) {
			view.setVisibility(initialVisibility);
		}

		// Add listener to update visibility when the value changes
		getContentBinding().caseHospitalizationAdmittedToHealthFacility.addValueChangedListener(field -> {
			int visibility = (field.getValue() == YesNo.YES ? VISIBLE : GONE);
			for (View view : viewsToToggle) {
				view.setVisibility(visibility);
			}
		});
	}

	private void handleAHF() {
		getContentBinding().caseHospitalizationAdmissionDate.setCaption("DATE OF ADMISSION IF APPLICABLE");
		getContentBinding().caseHospitalizationDischargeDate.setCaption("DATE OF DISCHARGE OR TRANSFER");
		handleHospitalizationVisibility(Arrays.asList(
				getContentBinding().caseHospitalizationAdmissionDate,
				getContentBinding().caseHospitalizationDischargeDate,
				getContentBinding().caseHospitalizationDateOfDeath
		));
	}


	private void handleILI() {
		getContentBinding().caseHospitalizationAdmittedToHealthFacility.setCaption("WAS THE PATIENT ADMITTED AT THE FACILITY (IN-PATIENT)?");
		getContentBinding().caseHospitalizationAdmissionDate.setCaption("DATE OF ADMISSION (IN-PATIENT)");
		getContentBinding().caseHospitalizationDischargeDate.setCaption("DATE PERSON DISCHARGED FROM HOSPITAL");
		handleHospitalizationVisibility(Arrays.asList(
				getContentBinding().caseHospitalizationAdmissionDate,
				getContentBinding().caseHospitalizationDischargeDate,
				getContentBinding().caseHospitalizationTerminationDateHospitalStay,
				getContentBinding().caseHospitalizationIntensiveCareUnit
		));
	}

	private void handleCSM(){
		getContentBinding().caseHospitalizationDateFirstSeen.setCaption("DATE SEEN AT HEALTH FACILITY:");
	}

	private void handleIDSR(){
		getContentBinding().caseHospitalizationDateFirstSeen.setCaption("DATE SEEN AT HEALTH FACILITY:");
	}

	private void handleMeasles() {
		// Add value change listener for inpatient/outpatient
		getContentBinding().caseHospitalizationSelectInpatientOutpatient.addValueChangedListener(field -> {
			InpatOutpat value = (InpatOutpat) field.getValue();
			if (value == InpatOutpat.INPATIENT) {
				getContentBinding().caseHospitalizationAdmissionDate.setVisibility(VISIBLE);
				getContentBinding().caseHospitalizationDischargeDate.setVisibility(VISIBLE);
			} else {
				getContentBinding().caseHospitalizationAdmissionDate.setVisibility(GONE);
				getContentBinding().caseHospitalizationDischargeDate.setVisibility(GONE);
			}
		});

		// Trigger the logic manually for the first time
		InpatOutpat initialInpatOutpatValue = (InpatOutpat) getContentBinding().caseHospitalizationSelectInpatientOutpatient.getValue();
		if (initialInpatOutpatValue == InpatOutpat.INPATIENT) {
			getContentBinding().caseHospitalizationAdmissionDate.setVisibility(VISIBLE);
			getContentBinding().caseHospitalizationDischargeDate.setVisibility(VISIBLE);
		} else {
			getContentBinding().caseHospitalizationAdmissionDate.setVisibility(GONE);
			getContentBinding().caseHospitalizationDischargeDate.setVisibility(GONE);
		}

		// Add value change listener for seen at a health facility
		getContentBinding().caseHospitalizationSeenAtAHealthFacility.addValueChangedListener(field -> {
			YesNoUnknown value = (YesNoUnknown) field.getValue();
			if (value == YesNoUnknown.YES) {
				getContentBinding().caseHospitalizationDateFirstSeen.setVisibility(VISIBLE);
			} else {
				getContentBinding().caseHospitalizationDateFirstSeen.setVisibility(GONE);
			}
		});

		// Trigger the logic manually for the first time
		YesNoUnknown initialSeenAtHealthFacilityValue = (YesNoUnknown) getContentBinding().caseHospitalizationSeenAtAHealthFacility.getValue();
		if (initialSeenAtHealthFacilityValue == YesNoUnknown.YES) {
			getContentBinding().caseHospitalizationDateFirstSeen.setVisibility(VISIBLE);
		} else {
			getContentBinding().caseHospitalizationDateFirstSeen.setVisibility(GONE);
		}
	}

	public void handleGuineaWorm() {
		 YesNo caseHospitalizationAdmittedToHealthFacilityYesNo = (YesNo) getContentBinding().caseHospitalizationAdmittedToHealthFacility.getValue();
		 if (caseHospitalizationAdmittedToHealthFacilityYesNo == YesNo.YES) {
			 getContentBinding().caseHospitalizationAdmissionDate.setVisibility(VISIBLE);
			 getContentBinding().caseHospitalizationDischargeDate.setVisibility(VISIBLE);
			 getContentBinding().caseHospitalizationHospitalRecordNumber.setVisibility(VISIBLE);
		 } else {
			 getContentBinding().caseHospitalizationAdmissionDate.setVisibility(GONE);
			 getContentBinding().caseHospitalizationDischargeDate.setVisibility(GONE);
			 getContentBinding().caseHospitalizationHospitalRecordNumber.setVisibility(GONE);
		 }
	}

	private void handleYF(){
		getContentBinding().caseHospitalizationDateFirstSeen.setCaption("DATE SEEN AT HEALTH FACILITY");
	}




}
