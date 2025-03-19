/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2020 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.app.epidata;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static de.symeda.sormas.app.epidata.EpiDataFragmentHelper.getDiseaseOfCaseOrContact;
import static de.symeda.sormas.app.epidata.EpiDataFragmentHelper.getEpiDataOfCaseOrContact;
import static de.symeda.sormas.app.epidata.EpiDataFragmentHelper.getIsCaseInstance;

import android.content.res.Resources;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.ObservableArrayList;

import com.googlecode.openbeans.Introspector;
import com.googlecode.openbeans.PropertyDescriptor;

import java.util.Calendar;
import java.util.List;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.activityascase.ActivityAsCaseDto;
import de.symeda.sormas.api.epidata.ContactSetting;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.caze.CaseOutcome;
import de.symeda.sormas.api.epidata.EpiDataDto;
import de.symeda.sormas.api.epidata.PlaceManaged;
import de.symeda.sormas.api.exposure.ExposureDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.riskfactor.DrinkingWaterSource;
import de.symeda.sormas.api.person.Sex;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.RiskFactorCondition;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.activityascase.ActivityAsCase;
import de.symeda.sormas.app.backend.caze.Case;
import de.symeda.sormas.app.backend.common.AbstractDomainObject;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.contact.Contact;
import de.symeda.sormas.app.backend.containmentmeasure.ContainmentMeasure;
import de.symeda.sormas.app.backend.contaminationsource.ContaminationSource;
import de.symeda.sormas.app.backend.epidata.EpiData;
import de.symeda.sormas.app.backend.exposure.Exposure;
import de.symeda.sormas.app.backend.persontravelhistory.PersonTravelHistory;
import de.symeda.sormas.app.caze.edit.CaseEditActivity;
import de.symeda.sormas.app.component.Item;
import de.symeda.sormas.app.component.controls.ControlPropertyField;
import de.symeda.sormas.app.contact.edit.ContactEditActivity;
import de.symeda.sormas.app.core.IEntryItemOnClickListener;
import de.symeda.sormas.app.databinding.FragmentEditEpidLayoutBinding;
import de.symeda.sormas.app.util.DataUtils;
import de.symeda.sormas.app.util.FieldVisibilityAndAccessHelper;
import de.symeda.sormas.app.util.InfrastructureDaoHelper;
import de.symeda.sormas.app.util.InfrastructureFieldsDependencyHandler;

public class EpidemiologicalDataEditFragment extends BaseEditFragment<FragmentEditEpidLayoutBinding, EpiData, PseudonymizableAdo> {

	public static final String TAG = EpidemiologicalDataEditFragment.class.getSimpleName();

	private EpiData record;
	private Disease caseDisease;
	private static Disease exportedCaseDisease;
	private IEntryItemOnClickListener onExposureItemClickListener;
	private IEntryItemOnClickListener onActivityAsCaseItemClickListener;
	private IEntryItemOnClickListener onPersonTravelHistoryItemClickListener;
	private IEntryItemOnClickListener onContainmentMeasureItemClickListener;
	private IEntryItemOnClickListener onContaminationSourceItemClickListener;

	private List<Item> initialRegionsList;
	private List<Item> initialDistrictsList;
	private List<Item> initialCommunitiesList;
	private List<Item> listDrinkingWaterSources;
	private List<Item> yearList;
	private List<Item> riskFactorConditionList;

	private List<Item> outcomeList;
	private boolean isCaseInstance;

	// Static methods

	public static EpidemiologicalDataEditFragment newInstance(PseudonymizableAdo activityRootData) {
		return newInstanceWithFieldCheckers(
			EpidemiologicalDataEditFragment.class,
			null,
			activityRootData,
			FieldVisibilityCheckers.withDisease(getDiseaseOfCaseOrContact(activityRootData)),
			UiFieldAccessCheckers.forSensitiveData(activityRootData.isPseudonymized()));
	}

	private void setUpControlListeners(final FragmentEditEpidLayoutBinding contentBinding) {
		isCaseInstance = getIsCaseInstance(getActivityRootData());
		onExposureItemClickListener = (v, item) -> {
			final Exposure exposure = (Exposure) item;
			final Exposure exposureClone = (Exposure) exposure.clone();
			final ExposureDialog dialog = new ExposureDialog(CaseEditActivity.getActiveActivity(), exposureClone, getActivityRootData(), false);

			dialog.setPositiveCallback(() -> {
				record.getExposures().set(record.getExposures().indexOf(exposure), exposureClone);
				updateExposures();
			});

			dialog.setDeleteCallback(() -> {
				removeExposure(exposure);
				dialog.dismiss();
			});

			dialog.show();
		};

		contentBinding.btnAddExposure.setOnClickListener(v -> {
			final Exposure exposure = DatabaseHelper.getExposureDao().build();
			final ExposureDialog dialog = new ExposureDialog(CaseEditActivity.getActiveActivity(), exposure, getActivityRootData(), true);

			dialog.setPositiveCallback(() -> addExposure(exposure));
			dialog.show();
		});

		contentBinding.epiDataExposureDetailsKnown.addValueChangedListener(field -> {
			YesNo value = (YesNo) field.getValue();
			contentBinding.exposuresLayout.setVisibility(value == YesNo.YES ? VISIBLE : GONE);
			if (value != YesNo.YES) {
				clearExposures();
			}

			getContentBinding().epiDataExposureDetailsKnown.setEnabled(getExposureList().isEmpty());
		});

		onActivityAsCaseItemClickListener = (v, item) -> {
			final ActivityAsCase activityAsCase = (ActivityAsCase) item;
			final ActivityAsCase activityAsCaseClone = (ActivityAsCase) activityAsCase.clone();
			final ActivityAsCaseDialog dialog =
				new ActivityAsCaseDialog(CaseEditActivity.getActiveActivity(), activityAsCaseClone, getActivityRootData(), false);

			dialog.setPositiveCallback(() -> {
				record.getActivitiesAsCase().set(record.getActivitiesAsCase().indexOf(activityAsCase), activityAsCaseClone);
				updateActivitiesAsCase();
			});

			dialog.setDeleteCallback(() -> {
				removeActivityAsCase(activityAsCase);
				dialog.dismiss();
			});

			dialog.show();
		};

		contentBinding.btnAddActivityascase.setOnClickListener(v -> {
			final ActivityAsCase activityAsCase = DatabaseHelper.getActivityAsCaseDao().build();
			final ActivityAsCaseDialog dialog =
				new ActivityAsCaseDialog(CaseEditActivity.getActiveActivity(), activityAsCase, getActivityRootData(), true);

			dialog.setPositiveCallback(() -> addActivityAsCase(activityAsCase));
			dialog.show();
		});

		contentBinding.epiDataActivityAsCaseDetailsKnown.addValueChangedListener(field -> {
			YesNo value = (YesNo) field.getValue();
			contentBinding.activityascaseLayout.setVisibility(value == YesNo.YES ? VISIBLE : GONE);
			if (value != YesNo.YES) {
				clearActivitiesAsCase();
			}

			getContentBinding().epiDataActivityAsCaseDetailsKnown.setEnabled(getActivityAsCaseList().isEmpty());
		});

		contentBinding.btnAddPersonTravelHistory.setOnClickListener(v -> {
			final PersonTravelHistory personTravelHistory = DatabaseHelper.getPersonTravelHistoryDao().build();
			final PersonTravelHistoryDialog dialog =
				new PersonTravelHistoryDialog(isCaseInstance ? CaseEditActivity.getActiveActivity() : ContactEditActivity.getActiveActivity(), personTravelHistory, getActivityRootData(), true);
			dialog.setPositiveCallback(() -> addPersonTravelHistory(personTravelHistory));
			dialog.show();
		});

		onPersonTravelHistoryItemClickListener = (v, item) -> {
			final PersonTravelHistory personTravelHistory = (PersonTravelHistory) item;
			final PersonTravelHistory personTravelHistoryClone = (PersonTravelHistory) personTravelHistory.clone();
			final PersonTravelHistoryDialog dialog =
				new PersonTravelHistoryDialog(isCaseInstance ? CaseEditActivity.getActiveActivity() : ContactEditActivity.getActiveActivity(), personTravelHistoryClone, getActivityRootData(), false);
			dialog.setPositiveCallback(() -> {
				record.getPersonTravelHistories().set(record.getPersonTravelHistories().indexOf(personTravelHistory), personTravelHistoryClone);
				updatePersonTravelHistories();
			});
			dialog.setDeleteCallback(() -> {
				removePersonTravelHistory(personTravelHistory);
				dialog.dismiss();
			});
			dialog.show();
		};

		contentBinding.setPersonTravelHistoryItemClickCallback(onPersonTravelHistoryItemClickListener);


//		ContainmentMeasure
		contentBinding.btnAddContainmentMeasure.setOnClickListener(v -> {
			final ContainmentMeasure containmentMeasure = DatabaseHelper.getContainmentMeasureDao().build();
			final ContainmentMeasureDialog dialog =
				new ContainmentMeasureDialog(isCaseInstance ? CaseEditActivity.getActiveActivity() : ContactEditActivity.getActiveActivity(), containmentMeasure, getActivityRootData(), true);
			dialog.setPositiveCallback(() -> addContainmentMeasure(containmentMeasure));
			dialog.show();
		});

		onContainmentMeasureItemClickListener = (v, item) -> {
			final ContainmentMeasure containmentMeasure = (ContainmentMeasure) item;
			final ContainmentMeasure containmentMeasureClone = (ContainmentMeasure) containmentMeasure.clone();
			final ContainmentMeasureDialog dialog =
				new ContainmentMeasureDialog(isCaseInstance ? CaseEditActivity.getActiveActivity() : ContactEditActivity.getActiveActivity(), containmentMeasureClone, getActivityRootData(), false);
			dialog.setPositiveCallback(() -> {
				record.getContainmentMeasures().set(record.getContainmentMeasures().indexOf(containmentMeasure), containmentMeasureClone);
				updateContainmentMeasures();
			});
			dialog.setDeleteCallback(() -> {
				removeContainmentMeasure(containmentMeasure);
				dialog.dismiss();
			});
			dialog.show();
		};
		contentBinding.setContainmentMeasureItemClickCallback(onContainmentMeasureItemClickListener);

//		ContaminationSource
		contentBinding.btnAddContaminationSource.setOnClickListener(v -> {
			final ContaminationSource contaminationSource = DatabaseHelper.getContaminationSourceDao().build();
			final ContaminationSourceDialog dialog =
				new ContaminationSourceDialog(isCaseInstance ? CaseEditActivity.getActiveActivity() : ContactEditActivity.getActiveActivity(), contaminationSource, getActivityRootData(), true);
			dialog.setPositiveCallback(() -> addContaminationSource(contaminationSource));
			dialog.show();
		});

		onContaminationSourceItemClickListener = (v, item) -> {
			final ContaminationSource contaminationSource = (ContaminationSource) item;
			final ContaminationSource contaminationSourceClone = (ContaminationSource) contaminationSource.clone();
			final ContaminationSourceDialog dialog =
				new ContaminationSourceDialog(isCaseInstance ? CaseEditActivity.getActiveActivity() : ContactEditActivity.getActiveActivity(), contaminationSourceClone, getActivityRootData(), false);
			dialog.setPositiveCallback(() -> {
				record.getContaminationSources().set(record.getContaminationSources().indexOf(contaminationSource), contaminationSourceClone);
				updateContaminationSources();
			});
			dialog.setDeleteCallback(() -> {
				removeContaminationSource(contaminationSource);
				dialog.dismiss();
			});
			dialog.show();
		};

		contentBinding.setContaminationSourceItemClickCallback(onContaminationSourceItemClickListener);

	}

	public static Disease getExportedCaseDisease() {
		return exportedCaseDisease;
	}

	public void setExportedCaseDisease(Disease exportedCaseDisease) {
		EpidemiologicalDataEditFragment.exportedCaseDisease = exportedCaseDisease;
	}

	private ObservableArrayList<Exposure> getExposureList() {
		ObservableArrayList<Exposure> exposures = new ObservableArrayList<>();
		exposures.addAll(record.getExposures());
		return exposures;
	}

	private void clearExposures() {
		record.getExposures().clear();
		updateExposures();
	}

	private void removeExposure(Exposure exposure) {
		record.getExposures().remove(exposure);
		updateExposures();
	}

	private void updateExposures() {
		getContentBinding().setExposureList(getExposureList());
		getContentBinding().epiDataExposureDetailsKnown.setEnabled(getExposureList().isEmpty());
		updateAddExposuresButtonVisibility();
	}

	private void addExposure(Exposure exposure) {
		record.getExposures().add(0, exposure);
		updateExposures();
	}

	private ObservableArrayList<ActivityAsCase> getActivityAsCaseList() {
		ObservableArrayList<ActivityAsCase> activitiesAsCase = new ObservableArrayList<>();
		activitiesAsCase.addAll(record.getActivitiesAsCase());
		return activitiesAsCase;
	}

	private void clearActivitiesAsCase() {
		record.getActivitiesAsCase().clear();
		updateActivitiesAsCase();
	}

	private void removeActivityAsCase(ActivityAsCase activityAsCase) {
		record.getActivitiesAsCase().remove(activityAsCase);
		updateActivitiesAsCase();
	}

	private void updateActivitiesAsCase() {
		getContentBinding().setActivityAsCaseList(getActivityAsCaseList());
		getContentBinding().epiDataActivityAsCaseDetailsKnown.setEnabled(getActivityAsCaseList().isEmpty());
//		updateAddActivitiesAsCaseButtonVisibility();
	}
	private void addActivityAsCase(ActivityAsCase activityAsCase) {
		record.getActivitiesAsCase().add(0, activityAsCase);
		updateActivitiesAsCase();
	}

	private ObservableArrayList<PersonTravelHistory> getPersonTravelHistories() {
		ObservableArrayList<PersonTravelHistory> personTravelHistories = new ObservableArrayList<>();
		personTravelHistories.addAll(record.getPersonTravelHistories());
		return personTravelHistories;
	}

	private void updatePersonTravelHistories() {
		getContentBinding().setPersonTravelHistoryList(getPersonTravelHistories());
	}

	private void addPersonTravelHistory(PersonTravelHistory personTravelHistory) {
		record.getPersonTravelHistories().add(0, personTravelHistory);
		updatePersonTravelHistories();
	}

	private void removePersonTravelHistory(PersonTravelHistory personTravelHistory) {
		record.getPersonTravelHistories().remove(personTravelHistory);
		updatePersonTravelHistories();
	}

	private ObservableArrayList<ContainmentMeasure> getContainmentMeasures() {
		ObservableArrayList<ContainmentMeasure> containmentMeasures = new ObservableArrayList<>();
		containmentMeasures.addAll(record.getContainmentMeasures());
		return containmentMeasures;
	}

	private void updateContainmentMeasures() {
		getContentBinding().setContainmentMeasureList(getContainmentMeasures());
	}

	private void addContainmentMeasure(ContainmentMeasure containmentMeasure) {
		record.getContainmentMeasures().add(0, containmentMeasure);
		updateContainmentMeasures();
	}

	private void removeContainmentMeasure(ContainmentMeasure containmentMeasure) {
		record.getContainmentMeasures().remove(containmentMeasure);
		updateContainmentMeasures();
	}

	private ObservableArrayList<ContaminationSource> getContaminationSources() {
		ObservableArrayList<ContaminationSource> contaminationSources = new ObservableArrayList<>();
		contaminationSources.addAll(record.getContaminationSources());
		return contaminationSources;
	}

	private void updateContaminationSources() {
		getContentBinding().setContaminationSourceList(getContaminationSources());
	}

	private void addContaminationSource(ContaminationSource contaminationSource) {
		record.getContaminationSources().add(0, contaminationSource);
		updateContaminationSources();
	}

	private void removeContaminationSource(ContaminationSource contaminationSource) {
		record.getContaminationSources().remove(contaminationSource);
		updateContaminationSources();
	}




	@Override
	protected String getSubHeadingTitle() {
		Resources r = getResources();
		return r.getString(R.string.caption_case_epidemiological_data);
	}

	@Override
	public EpiData getPrimaryData() {
		return record;
	}

	@Override
	protected void prepareFragmentData() {
		record = getEpiDataOfCaseOrContact(getActivityRootData());
		caseDisease = getDiseaseOfCaseOrContact(getActivityRootData());
		setExportedCaseDisease(caseDisease);
		initialRegionsList = InfrastructureDaoHelper.loadRegionsByServerCountry();
		initialDistrictsList = InfrastructureDaoHelper.loadDistricts(record.getHistoryOfTravelRegion());
		initialCommunitiesList = InfrastructureDaoHelper.loadCommunities(record.getHistoryOfTravelDistrict());
		listDrinkingWaterSources = DataUtils.getEnumItems(DrinkingWaterSource.class, true);
		outcomeList = DataUtils.getEnumItems(CaseOutcome.class, true);
		yearList = DataUtils.toItems(DateHelper.getYearsToNow(), true);
		riskFactorConditionList = DataUtils.getEnumItems(RiskFactorCondition.class, true);
	}

	@Override
	public void onLayoutBinding(final FragmentEditEpidLayoutBinding contentBinding) {
		setUpControlListeners(contentBinding);

		setDefaultValues(record);
		contentBinding.setData(record);
		contentBinding.setYesNoClass(YesNo.class);
		contentBinding.setPlaceManagedClass(PlaceManaged.class);
		contentBinding.setYesNoUnknownClass(YesNoUnknown.class);
		contentBinding.setContactSettingClass(ContactSetting.class);
		contentBinding.setExposureList(getExposureList());
		contentBinding.setExposureItemClickCallback(onExposureItemClickListener);
		contentBinding.setExposureListBindCallback(
			v -> FieldVisibilityAndAccessHelper
				.setFieldVisibilitiesAndAccesses(ExposureDto.class, (ViewGroup) v, new FieldVisibilityCheckers(), getFieldAccessCheckers()));

		contentBinding.setActivityAsCaseList(getActivityAsCaseList());
		contentBinding.setActivityAsCaseItemClickCallback(onActivityAsCaseItemClickListener);
		contentBinding.setActivityAsCaseListBindCallback(
			v -> FieldVisibilityAndAccessHelper
				.setFieldVisibilitiesAndAccesses(ActivityAsCaseDto.class, (ViewGroup) v, new FieldVisibilityCheckers(), getFieldAccessCheckers()));
		
		contentBinding.setPersonTravelHistoryList(getPersonTravelHistories());
		contentBinding.setPersonTravelHistoryItemClickCallback(onPersonTravelHistoryItemClickListener);
		contentBinding.setPersonTravelHistoryListBindCallback(
			v -> FieldVisibilityAndAccessHelper
				.setFieldVisibilitiesAndAccesses(PersonTravelHistory.class, (ViewGroup) v, new FieldVisibilityCheckers(), getFieldAccessCheckers()));

		contentBinding.setContainmentMeasureList(getContainmentMeasures());
		contentBinding.setContainmentMeasureItemClickCallback(onContainmentMeasureItemClickListener);
		contentBinding.setContainmentMeasureListBindCallback(
			v -> FieldVisibilityAndAccessHelper
				.setFieldVisibilitiesAndAccesses(ContainmentMeasure.class, (ViewGroup) v, new FieldVisibilityCheckers(), getFieldAccessCheckers()));

		contentBinding.setContaminationSourceList(getContaminationSources());
		contentBinding.setContaminationSourceItemClickCallback(onContaminationSourceItemClickListener);
		contentBinding.setContaminationSourceListBindCallback(
			v -> FieldVisibilityAndAccessHelper
				.setFieldVisibilitiesAndAccesses(ContaminationSource.class, (ViewGroup) v, new FieldVisibilityCheckers(), getFieldAccessCheckers()));

		InfrastructureFieldsDependencyHandler.instance.initializeRegionFields(
			contentBinding.epiDataHistoryOfTravelRegion,
			initialRegionsList,
			record.getHistoryOfTravelRegion(),
			contentBinding.epiDataHistoryOfTravelDistrict,
			initialDistrictsList,
			record.getHistoryOfTravelDistrict(),
			contentBinding.epiDataHistoryOfTravelSubDistrict,
			initialCommunitiesList,
			record.getHistoryOfTravelSubDistrict()
		);
		contentBinding.epiDataWaterUsedByPatientAfterExposure.initializeSpinner(listDrinkingWaterSources);
		contentBinding.epiDataYearOfVaccination.initializeSpinner(yearList);
		contentBinding.epiDataYearOfVaccinationCovid.initializeSpinner(yearList);
		contentBinding.epiDataRiskFactorsSevereDisease.initializeSpinner(riskFactorConditionList);

		int year = Calendar.getInstance().get(Calendar.YEAR);
		contentBinding.epiDataYearOfVaccination.setSelectionOnOpen(year - 35);
		contentBinding.epiDataYearOfVaccinationCovid.setSelectionOnOpen(year - 35);

		if (caseDisease == Disease.AHF){
			handleAHF();
		}

		if (caseDisease != null) {
			super.hideFieldsForDisease(caseDisease, contentBinding.mainContent, FormType.EPIDEMIOLOGICAL_EDIT);
		}

	}

	public void setDefaultValues(EpiData epiDataDto) {
		if (epiDataDto == null) {
			return;
		}
		try {
			for (PropertyDescriptor pd : Introspector.getBeanInfo(EpiData.class, AbstractDomainObject.class).getPropertyDescriptors()) {
				if (pd.getWriteMethod() != null && (pd.getReadMethod().getReturnType().equals(YesNoUnknown.class))) {
					try {
						if (pd.getReadMethod().invoke(epiDataDto) == null)
							pd.getWriteMethod().invoke(epiDataDto, YesNoUnknown.NO);
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
	public void onAfterLayoutBinding(FragmentEditEpidLayoutBinding contentBinding) {
		setFieldVisibilitiesAndAccesses(EpiDataDto.class, contentBinding.mainContent);
		contentBinding.epiDataExposureDetailsKnown.setEnabled(getExposureList().isEmpty());
		contentBinding.epiDataActivityAsCaseDetailsKnown.setEnabled(getActivityAsCaseList().isEmpty());
		contentBinding.epiDataIfYesWildAnimalDate.initializeDateField(getFragmentManager());
		contentBinding.epiDataHospitalizedDate1.initializeDateField(getFragmentManager());
		contentBinding.epiDataHospitalizedDate2.initializeDateField(getFragmentManager());
		contentBinding.epiDataDateOfContact.initializeDateField(getFragmentManager());
		contentBinding.epiDataIfYesStartDate.initializeDateField(getFragmentManager());
		contentBinding.epiDataIfYesEndDate.initializeDateField(getFragmentManager());
		contentBinding.epiDataDateOfLastContactWithSuspectCase.initializeDateField(getFragmentManager());
		contentBinding.epiDataDateOfDeath.initializeDateField(getFragmentManager());
		contentBinding.epiDataDateOfDeparture.initializeDateField(getFragmentManager());
		contentBinding.epiDataDateOfArrival.initializeDateField(getFragmentManager());
		contentBinding.epiDataDateOfDeparture2.initializeDateField(getFragmentManager());
		contentBinding.epiDataDateOfArrival2.initializeDateField(getFragmentManager());
		contentBinding.epiDataContactDate.initializeDateField(getFragmentManager());

		if (!(getActivityRootData() instanceof Case) && !(getActivityRootData() instanceof Contact)) {
			contentBinding.epiDataContactWithSourceCaseKnown.setVisibility(GONE);
			contentBinding.sourceContactsHeading.setVisibility(GONE);
			contentBinding.exposureInvestigationInfo.setText(Html.fromHtml(I18nProperties.getString(Strings.infoExposureInvestigationContacts)));
			contentBinding.activityascaseInvestigationInfo.setText(Html.fromHtml(I18nProperties.getString(Strings.infoActivityAsCaseInvestigation)));
			contentBinding.activityascaseLayout.setVisibility(GONE);
			contentBinding.epiDataActivityAsCaseDetailsKnown.setVisibility(GONE);
		}
		YesNo  epiDataHistoryOfTravelOutsideTheVillageTownDistrictValue = (YesNo) contentBinding.epiDataHistoryOfTravelOutsideTheVillageTownDistrict.getValue();
		contentBinding.headingNameOfPlaceOfTravel.setVisibility(epiDataHistoryOfTravelOutsideTheVillageTownDistrictValue == YesNo.YES ? VISIBLE : GONE);
		contentBinding.epiDataHistoryOfTravelOutsideTheVillageTownDistrict.addValueChangedListener(field -> {
				YesNo value = (YesNo) field.getValue();
				contentBinding.headingNameOfPlaceOfTravel.setVisibility(value == YesNo.YES ? VISIBLE : GONE);
			});

		if(caseDisease == Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS){
			contentBinding.epiDataContactSickAnimals.addValueChangedListener( field -> {
				YesNo value = (YesNo) field.getValue();
				if(value == YesNo.YES){
					contentBinding.epiDataIfYesSpecifySick.setVisibility(GONE);
				}
			});
		}
	}

	@Override
	public int getEditLayout() {
		return R.layout.fragment_edit_epid_layout;
	}

	@Override
	public boolean isShowSaveAction() {
		return true;
	}

	@Override
	public boolean isShowNewAction() {
		return false;
	}

	private void updateAddExposuresButtonVisibility() {
		if (getActivityRootData() instanceof Contact && !getExposureList().isEmpty()) {
			getContentBinding().btnAddExposure.setVisibility(GONE);
		} else {
			getContentBinding().btnAddExposure.setVisibility(VISIBLE);
		}
	}

	private void updateAddActivitiesAsCaseButtonVisibility() {
		if (getActivityRootData() instanceof Contact) {
			getContentBinding().btnAddActivityascase.setVisibility(View.GONE);
		} else if (getActivityRootData() instanceof Case && !getActivityAsCaseList().isEmpty()) {
			getContentBinding().btnAddActivityascase.setVisibility(View.VISIBLE);
		}
	}

	private void handleAHF(){
		List<Item<CaseOutcome>> itemsToRemove = List.of(
				new Item<>(CaseOutcome.OTHER.toString(), CaseOutcome.OTHER),
				new Item<>(CaseOutcome.UNKNOWN.toString(), CaseOutcome.UNKNOWN),
				new Item<>(CaseOutcome.REFERRED.toString(), CaseOutcome.REFERRED),
				new Item<>(CaseOutcome.REFERRED.toString(), CaseOutcome.REFERRED),
				new Item<>(CaseOutcome.NO_OUTCOME.toString(), CaseOutcome.NO_OUTCOME),
				new Item<>(CaseOutcome.ON_TREATMENT.toString(), CaseOutcome.ON_TREATMENT)
		);

		getContentBinding().epiDataDuringContactSuspectCase.initializeSpinner(outcomeList);
		outcomeList.removeAll(itemsToRemove);
	}
}
