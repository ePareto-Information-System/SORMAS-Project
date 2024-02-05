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

package de.symeda.sormas.app.sample.edit;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jcajce.provider.asymmetric.util.IESUtil;

import com.google.android.gms.common.api.CommonStatusCodes;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.facility.FacilityDto;
import de.symeda.sormas.api.person.Sex;
import de.symeda.sormas.api.sample.AdditionalTestType;
import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.PathogenTestType;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.sample.SampleMaterial;
import de.symeda.sormas.api.sample.SamplePurpose;
import de.symeda.sormas.api.sample.SampleSource;
import de.symeda.sormas.api.sample.SamplingReason;
import de.symeda.sormas.api.sample.SpecimenCondition;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.config.ConfigProvider;
import de.symeda.sormas.app.backend.facility.Facility;
import de.symeda.sormas.app.backend.sample.AdditionalTest;
import de.symeda.sormas.app.backend.sample.PathogenTest;
import de.symeda.sormas.app.backend.sample.Sample;
import de.symeda.sormas.app.barcode.BarcodeActivity;
import de.symeda.sormas.app.component.Item;
import de.symeda.sormas.app.databinding.FragmentSampleEditLayoutBinding;
import de.symeda.sormas.app.sample.read.SampleReadActivity;
import de.symeda.sormas.app.util.DataUtils;
import de.symeda.sormas.app.util.DiseaseConfigurationCache;

public class SampleEditFragment extends BaseEditFragment<FragmentSampleEditLayoutBinding, Sample, Sample> {

	private Sample record;
	private Sample referredSample;
	private PathogenTest mostRecentTest;
	private AdditionalTest mostRecentAdditionalTests;

	// Enum lists

	private List<Item> sampleMaterialList;
	private List<Item> sampleSourceList;
	private List<Facility> labList;
	private List<Item> samplePurposeList;
	private List<Item> samplingReasonList;
	private List<String> requestedPathogenTests = new ArrayList<>();
	private List<String> requestedAdditionalTests = new ArrayList<>();
	private List<Item> finalTestResults;
	private List<Item> diseaseList;
	//private List<Item> labResultList;
	private Disease disease;

	public static SampleEditFragment newInstance(Sample activityRootData) {
		return newInstanceWithFieldCheckers(
			SampleEditFragment.class,
			null,
			activityRootData,
			FieldVisibilityCheckers.withDisease(getDiseaseOfAssociatedEntity(activityRootData)).andWithCountry(ConfigProvider.getServerCountryCode()),
			UiFieldAccessCheckers.forSensitiveData(activityRootData.isPseudonymized()),
			UserRight.SAMPLE_EDIT);
	}

	private void setUpControlListeners(FragmentSampleEditLayoutBinding contentBinding) {
		if (!StringUtils.isEmpty(record.getReferredToUuid())) {
			contentBinding.sampleReferredToUuid.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					if (referredSample != null) {
						// Activity needs to be destroyed because it is only resumed, not created otherwise
						// and therefore the record uuid is not changed
						if (getActivity() != null) {
							getActivity().finish();
						}
						SampleReadActivity.startActivity(getActivity(), record.getUuid());
					}
				}
			});
		}
	}

	private void setUpFieldVisibilities(final FragmentSampleEditLayoutBinding contentBinding) {
		// Most recent test layout
		if (!record.isReceived() || record.getSpecimenCondition() != SpecimenCondition.ADEQUATE) {
			contentBinding.mostRecentTestLayout.setVisibility(GONE);
		} else {
			if (mostRecentTest != null) {
				contentBinding.noRecentTest.setVisibility(GONE);
			}
		}

		// Most recent additional tests layout
		if (ConfigProvider.hasUserRight(UserRight.ADDITIONAL_TEST_VIEW)
			&& !DatabaseHelper.getFeatureConfigurationDao().isFeatureDisabled(FeatureType.ADDITIONAL_TESTS)) {
			if (!record.isReceived()
				|| record.getSpecimenCondition() != SpecimenCondition.ADEQUATE
				|| !record.getAdditionalTestingRequested()
				|| mostRecentAdditionalTests == null) {
				contentBinding.mostRecentAdditionalTestsLayout.setVisibility(GONE);
			} else {
				if (!mostRecentAdditionalTests.hasArterialVenousGasValue()) {
					contentBinding.mostRecentAdditionalTests.arterialVenousGasLayout.setVisibility(GONE);
				}
			}
		} else {
			contentBinding.mostRecentAdditionalTestsLayout.setVisibility(GONE);
		}

		if (record.getId() == null) {
			contentBinding.samplePathogenTestResult.setVisibility(GONE);
		}
	}

	// Overrides

	@Override
	protected String getSubHeadingTitle() {
		return getResources().getString(R.string.caption_sample_information);
	}

	@Override
	public Sample getPrimaryData() {
		return record;
	}

	@Override
	protected void prepareFragmentData() {
		record = getActivityRootData();

		List<Disease> diseases = DiseaseConfigurationCache.getInstance().getAllDiseases(true, true, true);
		diseaseList = DataUtils.toItems(diseases);
		if (record.getDisease() != null && !diseases.contains(record.getDisease())) {
			diseaseList.add(DataUtils.toItem(record.getDisease()));
		}

		if (record.getId() != null) {
			mostRecentTest = DatabaseHelper.getSampleTestDao().queryMostRecentBySample(record);
			if (ConfigProvider.hasUserRight(UserRight.ADDITIONAL_TEST_VIEW)
				&& !DatabaseHelper.getFeatureConfigurationDao().isFeatureDisabled(FeatureType.ADDITIONAL_TESTS)) {
				mostRecentAdditionalTests = DatabaseHelper.getAdditionalTestDao().queryMostRecentBySample(record);
			}
		}
		if (!StringUtils.isEmpty(record.getReferredToUuid())) {
			referredSample = DatabaseHelper.getSampleDao().queryUuid(record.getReferredToUuid());
		} else {
			referredSample = null;
		}

		sampleMaterialList = DataUtils.getEnumItems(SampleMaterial.class, true, getFieldVisibilityCheckers());
		sampleSourceList = DataUtils.getEnumItems(SampleSource.class, true);
		labList = DatabaseHelper.getFacilityDao().getActiveLaboratories(true);
		samplePurposeList = DataUtils.getEnumItems(SamplePurpose.class, true);
		samplingReasonList = DataUtils.getEnumItems(SamplingReason.class, true, getFieldVisibilityCheckers());
		//labResultList = DataUtils.getEnumItems(PathogenTestResultType.class, true);

		for (PathogenTestType pathogenTest : record.getRequestedPathogenTests()) {
			requestedPathogenTests.clear();
			if (pathogenTest != PathogenTestType.OTHER) {
				requestedPathogenTests.add(pathogenTest.toString());
			}
		}
		if (ConfigProvider.hasUserRight(UserRight.ADDITIONAL_TEST_VIEW)
			&& !DatabaseHelper.getFeatureConfigurationDao().isFeatureDisabled(FeatureType.ADDITIONAL_TESTS)) {
			requestedAdditionalTests.clear();
			for (AdditionalTestType additionalTest : record.getRequestedAdditionalTests()) {
				requestedAdditionalTests.add(additionalTest.toString());
			}
		}

		if (record.getId() != null) {
			if (DatabaseHelper.getSampleTestDao()
				.queryBySample(record)
				.stream()
				.allMatch(pathogenTest -> pathogenTest.getTestResult() == PathogenTestResultType.PENDING)) {
				finalTestResults = DataUtils.toItems(Arrays.asList(PathogenTestResultType.values()));
			} else {
				finalTestResults = DataUtils.toItems(
					Arrays.stream(PathogenTestResultType.values())
						.filter(type -> type != PathogenTestResultType.NOT_DONE)
						.collect(Collectors.toList()));
			}
		}
	}

	@Override
	public void onLayoutBinding(FragmentSampleEditLayoutBinding contentBinding) {
		setUpControlListeners(contentBinding);

		contentBinding.setData(record);
		contentBinding.setPathogenTest(mostRecentTest);
		contentBinding.setAdditionalTest(mostRecentAdditionalTests);
		contentBinding.setReferredSample(referredSample);

		SampleValidator.initializeSampleValidation(contentBinding);

		contentBinding.setPathogenTestTypeClass(PathogenTestType.class);
		contentBinding.setAdditionalTestTypeClass(AdditionalTestType.class);
	}

	@Override
	public void onAfterLayoutBinding(final FragmentSampleEditLayoutBinding contentBinding) {
		super.onAfterLayoutBinding(contentBinding);
		setFieldVisibilitiesAndAccesses(SampleDto.class, contentBinding.mainContent);
		setUpFieldVisibilities(contentBinding);

		Disease selectedDisease = getDiseaseOfAssociatedEntity(record);

		// Initialize ControlSpinnerFields
		contentBinding.sampleSampleMaterial.initializeSpinner(sampleMaterialList);
		contentBinding.sampleSampleSource.initializeSpinner(sampleSourceList);
		contentBinding.samplePurpose.setEnabled(referredSample == null || record.getSamplePurpose() != SamplePurpose.EXTERNAL);
		contentBinding.sampleLab.initializeSpinner(DataUtils.toItems(labList), field -> {
			Facility laboratory = (Facility) field.getValue();
			if (laboratory != null && laboratory.getUuid().equals(FacilityDto.OTHER_FACILITY_UUID)) {
				contentBinding.sampleLabDetails.setVisibility(View.VISIBLE);
			} else {
				contentBinding.sampleLabDetails.hideField(true);
			}
		});
		if (finalTestResults != null) {
			contentBinding.samplePathogenTestResult.initializeSpinner(finalTestResults);
			if (contentBinding.samplePathogenTestResult.getValue() == null) {
				contentBinding.samplePathogenTestResult.setValue(PathogenTestResultType.PENDING);
			}
		}

		contentBinding.samplePurpose.initializeSpinner(samplePurposeList, field -> {
			SamplePurpose samplePurpose = (SamplePurpose) field.getValue();
			if (SamplePurpose.EXTERNAL == samplePurpose) {
				contentBinding.externalSampleFieldsLayout.setVisibility(VISIBLE);
				contentBinding.samplePathogenTestingRequested
					.setVisibility(ConfigProvider.getUser().equals(record.getReportingUser()) ? VISIBLE : GONE);
				contentBinding.sampleAdditionalTestingRequested
					.setVisibility(ConfigProvider.getUser().equals(record.getReportingUser()) ? VISIBLE : GONE);

				handleDisease(Disease.YELLOW_FEVER, "National Public Health and Reference Lab");
				handleDisease(Disease.AHF, "Noguchi Memorial Institute for Medical Research");
				handleDisease(Disease.AFP, "Noguchi Memorial Institute for Medical Research");

				switch (selectedDisease) {
					case CSM:
						handleCSM();
						break;
					case AHF:
						handleAHF();
						break;
					case AFP:
						handleAFP();
						break;
					case YELLOW_FEVER:
						handleYellowFever();

					default:
						break;
				}

			} else {
				contentBinding.sampleShipped.setValue(null);
				contentBinding.sampleShipmentDate.setValue(null);
				contentBinding.sampleShipmentDetails.setValue(null);
				contentBinding.externalSampleFieldsLayout.setVisibility(GONE);
				contentBinding.samplePathogenTestingRequested.setVisibility(GONE);
				contentBinding.sampleAdditionalTestingRequested.setVisibility(GONE);
			}
		});

		contentBinding.sampleSamplingReason.initializeSpinner(samplingReasonList);

		// Initialize ControlDateFields and ControlDateTimeFields
		contentBinding.sampleSampleDateTime.initializeDateTimeField(getFragmentManager());
		contentBinding.sampleShipmentDate.initializeDateField(getFragmentManager());

		// Initialize on clicks
		contentBinding.buttonScanFieldSampleId.setOnClickListener((View v) -> {
			Intent intent = new Intent(getContext(), BarcodeActivity.class);
			startActivityForResult(intent, BarcodeActivity.RC_BARCODE_CAPTURE);
		});

		// Disable fields the user doesn't have access to - this involves almost all fields when
		// the user is not the one that originally reported the sample
		if (!ConfigProvider.getUser().equals(record.getReportingUser())) {
			contentBinding.sampleSampleDateTime.setEnabled(false);
			contentBinding.sampleSampleMaterial.setEnabled(false);
			contentBinding.sampleSampleMaterialText.setEnabled(false);
			contentBinding.sampleSampleSource.setEnabled(false);
			contentBinding.sampleLab.setEnabled(false);
			contentBinding.sampleLabDetails.setEnabled(false);
			contentBinding.sampleShipped.setEnabled(false);
			contentBinding.sampleShipmentDate.setEnabled(false);
			contentBinding.sampleShipmentDetails.setEnabled(false);
			contentBinding.samplePurpose.setEnabled(false);
			contentBinding.sampleReceived.setEnabled(false);
			contentBinding.sampleLabSampleID.setEnabled(false);
			contentBinding.sampleSpecimenCondition.setEnabled(false);
			contentBinding.samplePathogenTestingRequested.setVisibility(GONE);
			contentBinding.sampleRequestedPathogenTests.setVisibility(GONE);
			contentBinding.sampleAdditionalTestingRequested.setVisibility(GONE);
			contentBinding.sampleRequestedAdditionalTests.setVisibility(GONE);

			if (!requestedPathogenTests.isEmpty()) {
				contentBinding.sampleRequestedPathogenTestsTags.setTags(requestedPathogenTests);
				if (StringUtils.isEmpty(record.getRequestedOtherPathogenTests())) {
					contentBinding.sampleRequestedOtherPathogenTests.setVisibility(GONE);
				}
			} else {
				contentBinding.sampleRequestedPathogenTestsTags.setVisibility(GONE);
				contentBinding.sampleRequestedOtherPathogenTests.setVisibility(GONE);
			}

			if (ConfigProvider.hasUserRight(UserRight.ADDITIONAL_TEST_VIEW)
				&& !DatabaseHelper.getFeatureConfigurationDao().isFeatureDisabled(FeatureType.ADDITIONAL_TESTS)) {
				if (!requestedAdditionalTests.isEmpty()) {
					contentBinding.sampleRequestedAdditionalTestsTags.setTags(requestedAdditionalTests);
					if (StringUtils.isEmpty(record.getRequestedOtherAdditionalTests())) {
						contentBinding.sampleRequestedOtherAdditionalTests.setVisibility(GONE);
					}
				} else {
					contentBinding.sampleRequestedAdditionalTestsTags.setVisibility(GONE);
					contentBinding.sampleRequestedOtherAdditionalTests.setVisibility(GONE);
				}
			}

			if (requestedPathogenTests.isEmpty() && requestedAdditionalTests.isEmpty()) {
				contentBinding.pathogenTestingDivider.setVisibility(GONE);
			}
		} else {
			contentBinding.sampleRequestedPathogenTestsTags.setVisibility(GONE);
			contentBinding.sampleRequestedPathogenTests.removeItem(PathogenTestType.OTHER);
			contentBinding.sampleRequestedAdditionalTestsTags.setVisibility(GONE);
		}

		if (!ConfigProvider.hasUserRight(UserRight.ADDITIONAL_TEST_VIEW)
			&& !DatabaseHelper.getFeatureConfigurationDao().isFeatureDisabled(FeatureType.ADDITIONAL_TESTS)) {
			contentBinding.additionalTestingLayout.setVisibility(GONE);
			}

	}

	private void handleDisease(Disease targetDisease, String labName) {
		Disease selectedDisease = getDiseaseOfAssociatedEntity(record);

		if (selectedDisease == targetDisease) {
			checkLabName(labName);
		}
	}

	private void checkLabName(String labName){
		if(labList != null){
			List<Facility>allActiveLaboratories = DatabaseHelper.getFacilityDao().getActiveLaboratories(false);
			Facility facilitylab = findLabByName(allActiveLaboratories, labName);

			if(facilitylab != null){
				labList.addAll(allActiveLaboratories);
				getContentBinding().sampleLab.setValue(facilitylab);
			} else {
				System.out.println("Please add " + labName + " to Facility Configuration");
			}
		} else {
			System.out.println("Lab dropdown is null. Please contact the administrator.");
		}
	}

	private Facility findLabByName(List<Facility> labs, String labName) {
		for (Facility labItem : labs) {
			if (labName.equals(labItem.getName())) {
				return labItem;
			}
		}
		return null;
	}

	private void handleCSM() {
		getContentBinding().samplePurpose.setVisibility(GONE);
		getContentBinding().samplePurpose.setVisibility(GONE);
	}

	private void handleAFP() {
		getContentBinding().samplePurpose.setVisibility(GONE);
		getContentBinding().samplePurpose.setRequired(false);
	}

	private void handleAHF(){
		getContentBinding().sampleSampleSource.setVisibility(GONE);
		getContentBinding().sampleSamplingReason.setVisibility(GONE);
		getContentBinding().sampleSamplingReason.setVisibility(GONE);
		getContentBinding().sampleSamplingReasonDetails.setVisibility(GONE);
		getContentBinding().sampleSamplingReasonDetails.setVisibility(GONE);
	}

	private void handleYellowFever(){
		getContentBinding().sampleSampleDateTime.setVisibility(GONE);
		getContentBinding().sampleSampleMaterial.setVisibility(GONE);
		getContentBinding().sampleSamplingReason.setVisibility(GONE);
		getContentBinding().samplePathogenTestingRequested.setVisibility(GONE);

		getContentBinding().sampleSampleDateTime.setRequired(false);
		getContentBinding().sampleSampleMaterial.setRequired(false);

		//Sample IP Sent
		getContentBinding().sampleIpSent.setEnabled(true);
		getContentBinding().sampleIpResults.setEnabled(true);

		getContentBinding().sampleMaterialTypeForYellowFever.setEnabled(true);
		getContentBinding().sampleYellowFeverSampleTypes.setEnumClass(SampleMaterial.class);

		List<Item> labResultList = new ArrayList<>();
		labResultList.add(new Item("", null));

		for (PathogenTestResultType resultType : PathogenTestResultType.values()) {
			if (resultType == PathogenTestResultType.PENDING || resultType == PathogenTestResultType.POSITIVE || resultType == PathogenTestResultType.NEGATIVE) {
				Item item = new Item(resultType.toString(), resultType);
				labResultList.add(item);
			}
		}

		getContentBinding().sampleLabResults.initializeSpinner(labResultList);

	}


	@Override
	public int getEditLayout() {
		return R.layout.fragment_sample_edit_layout;
	}

	@Override
	public boolean isShowSaveAction() {
		return ConfigProvider.hasUserRight(UserRight.SAMPLE_EDIT);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		if (requestCode == BarcodeActivity.RC_BARCODE_CAPTURE) {
			if (resultCode == CommonStatusCodes.SUCCESS && data != null) {
				getContentBinding().sampleFieldSampleID.setValue(data.getStringExtra(BarcodeActivity.BARCODE_RESULT));
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	protected static Disease getDiseaseOfAssociatedEntity(Sample sample) {
		if (sample.getAssociatedCase() != null) {
			return sample.getAssociatedCase().getDisease();
		} else if (sample.getAssociatedContact() != null) {
			return sample.getAssociatedContact().getDisease();
		} else if (sample.getAssociatedEventParticipant() != null) {
			return sample.getAssociatedEventParticipant().getEvent().getDisease();
		} else {
			return null;
		}
	}
}
