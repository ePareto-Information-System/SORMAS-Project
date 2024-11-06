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

import static android.view.View.GONE;

import static de.symeda.sormas.app.core.notification.NotificationType.ERROR;

import android.view.View;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


import de.symeda.sormas.api.ebs.AutomaticScanningType;
import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.ebs.ManualScanningType;
import de.symeda.sormas.api.ebs.MediaScannningType;
import de.symeda.sormas.api.ebs.PersonReporting;
import de.symeda.sormas.api.utils.ValidationException;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.app.BaseActivity;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.config.ConfigProvider;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.backend.ebs.signalVerification.SignalVerification;
import de.symeda.sormas.app.backend.location.Location;
import de.symeda.sormas.app.backend.ebs.triaging.Triaging;
import de.symeda.sormas.app.component.Item;
import de.symeda.sormas.app.component.dialog.LocationDialog;
import de.symeda.sormas.app.component.validation.FragmentValidator;
import de.symeda.sormas.app.component.validation.ValidationHelper;
import de.symeda.sormas.app.core.notification.NotificationHelper;
import de.symeda.sormas.app.databinding.FragmentEbsEditLayoutBinding;
import de.symeda.sormas.app.util.DataUtils;
import de.symeda.sormas.app.util.InfrastructureDaoHelper;
import de.symeda.sormas.app.util.InfrastructureFieldsDependencyHandler;

public class EbsEditFragment extends BaseEditFragment<FragmentEbsEditLayoutBinding, Ebs, Ebs> {

	private Ebs record;

	private List<Item> sourceInformation;
	private List<Item> categoryOfInformant;
	private List<Item> manualScanningType;
	private List<Item> automaticScanningType;
	private List<Item> initialRegions;
	private List<Item> initialDistricts;
	private List<Item> initialCommunities;
	private List<Item> mediaScanningType;


	public static EbsEditFragment newInstance(Ebs activityRootData) {
		EbsEditFragment fragment = newInstanceWithFieldCheckers(
				EbsEditFragment.class,
				null,
				activityRootData,
				FieldVisibilityCheckers.withCountry(ConfigProvider.getServerCountryCode()),
				UiFieldAccessCheckers.getDefault(activityRootData.isPseudonymized()));

		return fragment;
	}
	public static EbsEditFragment newInstance(Triaging activityRootData) {
		EbsEditFragment fragment = newInstanceWithFieldCheckers(
				EbsEditFragment.class,
				null,
				activityRootData,
				FieldVisibilityCheckers.withCountry(ConfigProvider.getServerCountryCode()),
				UiFieldAccessCheckers.getDefault(activityRootData.isPseudonymized()));

		return fragment;
	}
	public static EbsEditFragment newInstance(SignalVerification activityRootData) {
		EbsEditFragment fragment = newInstanceWithFieldCheckers(
				EbsEditFragment.class,
				null,
				activityRootData,
				FieldVisibilityCheckers.withCountry(ConfigProvider.getServerCountryCode()),
				UiFieldAccessCheckers.getDefault(activityRootData.isPseudonymized()));

		return fragment;
	}

	private void setUpControlListeners(final FragmentEbsEditLayoutBinding contentBinding) {
		contentBinding.ebsEbsLatitude.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				openAddressPopup(contentBinding);
			}
		});
	}

	private void openAddressPopup(final FragmentEbsEditLayoutBinding contentBinding) {
		final Location location = record.getEbsLocation();
		final Location locationClone = (Location) location.clone();
		final LocationDialog locationDialog = new LocationDialog(BaseActivity.getActiveActivity(), locationClone, false, null);
		locationDialog.show();
		if (DatabaseHelper.getEventDao().hasAnyEventParticipantWithoutJurisdiction(record.getUuid())) {
			locationDialog.getContentBinding().locationRegion.setRequired(true);
			locationDialog.getContentBinding().locationDistrict.setRequired(true);
			locationDialog.getContentBinding().locationCountry.setEnabled(false);
		} else {
			locationDialog.setRequiredFieldsBasedOnCountry();
		}
		locationDialog.setPositiveCallback(() -> {
			try {
				FragmentValidator.validate(getContext(), locationDialog.getContentBinding());
				contentBinding.ebsEbsLatitude.setValue(locationClone);
				record.setEbsLocation(locationClone);

				locationDialog.dismiss();
			} catch (ValidationException e) {
				NotificationHelper.showDialogNotification(locationDialog, ERROR, e.getMessage());
			}
		});
	}


	// Overrides

	@Override
	protected String getSubHeadingTitle() {
		return getResources().getString(R.string.caption_ebs_information);
	}

	@Override
	public Ebs getPrimaryData() {
		return record;
	}

	@Override
	public boolean isShowSaveAction() {
		return record != null;
	}

	@Override
	protected void prepareFragmentData() {
		record = getActivityRootData();
		sourceInformation = DataUtils.getEnumItems(EbsSourceType.class,true);
		categoryOfInformant = DataUtils.getEnumItems(PersonReporting.class,true);
		manualScanningType = DataUtils.getEnumItems(ManualScanningType.class,true);
		automaticScanningType = DataUtils.getEnumItems(AutomaticScanningType.class, true);
		mediaScanningType = DataUtils.getEnumItems(MediaScannningType.class, true);
		initialRegions = InfrastructureDaoHelper.loadRegionsByServerCountry();
		initialDistricts = InfrastructureDaoHelper.loadDistricts(record.getEbsLocation().getRegion());
		initialCommunities = InfrastructureDaoHelper.loadCommunities(record.getEbsLocation().getDistrict());

	}

	@Override
	public void onLayoutBinding(final FragmentEbsEditLayoutBinding contentBinding) {
		setUpControlListeners(contentBinding);

		contentBinding.setData(record);
		contentBinding.setAutomaticScanningTypeClass(AutomaticScanningType.class);
		contentBinding.setManualScanningTypeClass(ManualScanningType.class);
		contentBinding.setMediaScannningTypeClass(MediaScannningType.class);
		ValidationHelper.initPhoneNumberValidator(contentBinding.ebsInformantTel);
		ValidationHelper.initPhoneNumberValidator(contentBinding.ebsPersonPhone);
		InfrastructureFieldsDependencyHandler.instance.initializeRegionFields(
				contentBinding.ebsRegion,
				initialRegions,
				record.getEbsLocation().getRegion(),
				contentBinding.ebsDistrict,
				initialDistricts,
				record.getEbsLocation().getDistrict(),
				contentBinding.ebsCommunity,
				initialCommunities,
				record.getEbsLocation().getCommunity()
		);
	}

	@Override
	protected void onAfterLayoutBinding(FragmentEbsEditLayoutBinding contentBinding) {
		super.onAfterLayoutBinding(contentBinding);
		contentBinding.ebsSourceInformation.initializeSpinner(sourceInformation);
		contentBinding.ebsAutomaticScanningType.initializeSpinner(automaticScanningType);
		contentBinding.ebsManualScanningType.initializeSpinner(manualScanningType);
		contentBinding.ebsScanningType.initializeSpinner(mediaScanningType);
		contentBinding.ebsReportDateTime.initializeDateField(getFragmentManager());
		contentBinding.ebsDateOnset.initializeDateField(getFragmentManager());
		contentBinding.ebsInformantName.setVisibility(GONE);
		contentBinding.ebsCommunity.setCaption("Sub District");
		contentBinding.ebsSourceInformation.addValueChangedListener(e -> {
			List<PersonReporting> itemsToAdd;

			Object value = contentBinding.ebsSourceInformation.getValue();
			if (value == null || !(value instanceof EbsSourceType)){ contentBinding.ebsInformantName.setVisibility(GONE); return;}
			EbsSourceType sourceType = (EbsSourceType) value;

			switch (sourceType) {
				case CEBS:
					itemsToAdd = Arrays.asList(
							PersonReporting.COMMUNITY_SURVEILLANCE_VOLUNTEER,
							PersonReporting.COMMUNITY_ANIMAL_HEALTH_WORKER,
							PersonReporting.OTC_CHEMICAL_WORKER,
							PersonReporting.COMMUNITY_MEMBER,
							PersonReporting.OTHER
					);
					contentBinding.ebsInformantName.setVisibility(View.VISIBLE);
					break;
				case HEBS:
					itemsToAdd = Arrays.asList(
							PersonReporting.PUBLIC_HEALTHCARE,
							PersonReporting.PRIVATE_HEALTH,
							PersonReporting.REFERENCE_LABORATORY,
							PersonReporting.OTHER
					);
					contentBinding.ebsInformantName.setVisibility(View.VISIBLE);
					break;
				case MEDIA_NEWS:
					itemsToAdd = Arrays.asList(
							PersonReporting.PERSON_DISTRICT,
							PersonReporting.PERSON_REGION,
							PersonReporting.PERSON_NATIONAL,
							PersonReporting.OTHER
					);
					contentBinding.ebsInformantName.setVisibility(GONE);
					break;
				case HOTLINE_PERSON:
					itemsToAdd = Arrays.asList(
							PersonReporting.GENERAL_PUBLIC_INFORMANT,
							PersonReporting.INSTITUTIONAL_INFORMANT,
							PersonReporting.OTHER
					);
					contentBinding.ebsInformantName.setVisibility(View.VISIBLE);
					break;
				default:
					itemsToAdd = Collections.emptyList();
					break;
			}

			List<Item> filteredInformant = categoryOfInformant.stream()
					.filter(item -> itemsToAdd.toString().contains(item.toString()))
					.collect(Collectors.toList());

			contentBinding.ebsCategoryOfInformant.initializeSpinner(filteredInformant);
			contentBinding.ebsInformantName.setVisibility((e.getValue() == EbsSourceType.MEDIA_NEWS) ? GONE : View.VISIBLE);
		});
		contentBinding.ebsCategoryOfInformant.addValueChangedListener(e->{
			List<MediaScannningType> itemsToAdd;
			if (e.getValue() != PersonReporting.PERSON_NATIONAL) {
				itemsToAdd = Arrays.asList(
						MediaScannningType.MANUAL
				);
            }else if (e.getValue() == PersonReporting.PERSON_NATIONAL){
				itemsToAdd = Arrays.asList(
						MediaScannningType.MANUAL,
						MediaScannningType.AUTOMATIC
				);
			}else {
				itemsToAdd = Collections.emptyList();
			}
			List<Item> filteredInformant = mediaScanningType.stream()
					.filter(item -> itemsToAdd.toString().contains(item.toString()))
					.collect(Collectors.toList());

			contentBinding.ebsScanningType.initializeSpinner(filteredInformant);
		});
	}

	@Override
	public int getEditLayout() {
		return R.layout.fragment_ebs_edit_layout;
	}
}
