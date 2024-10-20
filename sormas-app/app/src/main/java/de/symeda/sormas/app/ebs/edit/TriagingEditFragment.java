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

import android.view.View;
import android.widget.PopupWindow;

import androidx.core.app.NotificationCompat;

import org.apache.tapestry.wml.Go;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.symeda.sormas.api.ebs.AnimalCommunityCategoryDetails;
import de.symeda.sormas.api.ebs.AnimalFacilityCategoryDetails;
import de.symeda.sormas.api.ebs.AnimalLaboratoryCategoryDetails;
import de.symeda.sormas.api.ebs.CategoryDetailsLevel;
import de.symeda.sormas.api.ebs.EbsTriagingDecision;
import de.symeda.sormas.api.ebs.EnvironmentalCategoryDetails;
import de.symeda.sormas.api.ebs.HumanCommunityCategoryDetails;
import de.symeda.sormas.api.ebs.HumanFaclityCategoryDetails;
import de.symeda.sormas.api.ebs.HumanLaboratoryCategoryDetails;
import de.symeda.sormas.api.ebs.OutComeSupervisor;
import de.symeda.sormas.api.ebs.POE;
import de.symeda.sormas.api.ebs.SignalCategory;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.config.ConfigProvider;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.backend.ebs.signalVerification.SignalVerification;
import de.symeda.sormas.app.backend.ebs.triaging.Triaging;
import de.symeda.sormas.app.component.Item;
import de.symeda.sormas.app.component.dialog.ConfirmationDialog;
import de.symeda.sormas.app.component.dialog.ConfirmationInputDialog;
import de.symeda.sormas.app.databinding.FragmentTriagingEditLayoutBinding;
import de.symeda.sormas.app.rest.SynchronizeDataAsync;
import de.symeda.sormas.app.triaging.edit.TriagingEditActivity;
import de.symeda.sormas.app.util.Callback;
import de.symeda.sormas.app.util.DataUtils;

public class TriagingEditFragment extends BaseEditFragment<FragmentTriagingEditLayoutBinding, Triaging, Triaging> {

	private Triaging record;

	private List<Item> signalCategory;
	private List<Item> categoryDetailsLevel;
	private List<Item> humanCommunityCategoryDetails;
	private List<Item> humanLaboratoryCategoryDetails;
	private List<Item> humanFacilityCategoryDetails;
	private List<Item> animalCommunityCategoryDetails;
	private List<Item> animalLaboratoryCategoryDetails;
	private List<Item> animalFacilityCategoryDetails;
	private List<Item> environmentCategoryDetails;
	private List<Item> poeCategoryDetails;
	private List<Item> triagingDecision;
	private List<Item> triagingOutComeSupervisor;


	public static TriagingEditFragment newInstance(Ebs activityRootData) {
		TriagingEditFragment fragment = newInstanceWithFieldCheckers(
				TriagingEditFragment.class,
				null,
				activityRootData,
				FieldVisibilityCheckers.withCountry(ConfigProvider.getServerCountryCode()),
				UiFieldAccessCheckers.getDefault(activityRootData.isPseudonymized()));

		return fragment;
	}
	public static TriagingEditFragment newInstance(Triaging activityRootData) {
		TriagingEditFragment fragment = newInstanceWithFieldCheckers(
				TriagingEditFragment.class,
				null,
				activityRootData,
				FieldVisibilityCheckers.withCountry(ConfigProvider.getServerCountryCode()),
				UiFieldAccessCheckers.getDefault(activityRootData.isPseudonymized()));

		return fragment;
	}
	public static TriagingEditFragment newInstance(SignalVerification activityRootData) {
		TriagingEditFragment fragment = newInstanceWithFieldCheckers(
				TriagingEditFragment.class,
				null,
				activityRootData,
				FieldVisibilityCheckers.withCountry(ConfigProvider.getServerCountryCode()),
				UiFieldAccessCheckers.getDefault(activityRootData.isPseudonymized()));

		return fragment;
	}


	// Overrides

	@Override
	protected String getSubHeadingTitle() {
		return getResources().getString(R.string.caption_triaging_information);
	}

	@Override
	public Triaging getPrimaryData() {
		return record;
	}

	@Override
	public boolean isShowSaveAction() {
		return record != null;
	}

	@Override
	protected void prepareFragmentData() {
		record = getActivityRootData();
		signalCategory = DataUtils.getEnumItems(SignalCategory.class,true);
		categoryDetailsLevel = DataUtils.getEnumItems(CategoryDetailsLevel.class,true);
		humanCommunityCategoryDetails = DataUtils.getEnumItems(HumanCommunityCategoryDetails.class,true);
		humanLaboratoryCategoryDetails = DataUtils.getEnumItems(HumanLaboratoryCategoryDetails.class,true);
		humanFacilityCategoryDetails = DataUtils.getEnumItems(HumanFaclityCategoryDetails.class,true);
		humanFacilityCategoryDetails = DataUtils.getEnumItems(HumanFaclityCategoryDetails.class,true);
		humanFacilityCategoryDetails = DataUtils.getEnumItems(HumanFaclityCategoryDetails.class,true);
		animalCommunityCategoryDetails = DataUtils.getEnumItems(AnimalCommunityCategoryDetails.class,true);
		animalLaboratoryCategoryDetails = DataUtils.getEnumItems(AnimalLaboratoryCategoryDetails.class,true);
		animalFacilityCategoryDetails = DataUtils.getEnumItems(AnimalFacilityCategoryDetails.class,true);
		environmentCategoryDetails = DataUtils.getEnumItems(EnvironmentalCategoryDetails.class,true);
		poeCategoryDetails = DataUtils.getEnumItems(POE.class,true);
		triagingDecision = DataUtils.getEnumItems(EbsTriagingDecision.class,true);
		triagingOutComeSupervisor = DataUtils.getEnumItems(OutComeSupervisor.class,true);

	}

	@Override
	public void onLayoutBinding(final FragmentTriagingEditLayoutBinding contentBinding) {

		contentBinding.setData(record);
		contentBinding.setTriagingSignalCategoryClass(SignalCategory.class);
		contentBinding.setTriagingCategoryDetailsLevelClass(CategoryDetailsLevel.class);
		contentBinding.setTriagingHumanCommunityCategoryDetailsClass(HumanCommunityCategoryDetails.class);
		contentBinding.setTriagingHumanFaclityCategoryDetailsClass(HumanFaclityCategoryDetails.class);
		contentBinding.setTriagingHumanLaboratoryCategoryDetailsClass(HumanLaboratoryCategoryDetails.class);

		contentBinding.setTriagingAnimalCommunityCategoryDetailsClass(AnimalCommunityCategoryDetails.class);
		contentBinding.setTriagingAnimalFacilityCategoryDetailsClass(AnimalFacilityCategoryDetails.class);
		contentBinding.setTriagingAnimalLaboratoryCategoryDetailsClass(AnimalLaboratoryCategoryDetails.class);

		contentBinding.setTriagingEnvironmentalCategoryDetailsClass(EnvironmentalCategoryDetails.class);
		contentBinding.setTriagingPOEClass(POE.class);

		contentBinding.setYesNoClass(YesNo.class);
		contentBinding.setTriagingDecisionClass(EbsTriagingDecision.class);
		contentBinding.setTriagingOutComeSupervisorClass(OutComeSupervisor.class);
	}

	@Override
	protected void onAfterLayoutBinding(FragmentTriagingEditLayoutBinding contentBinding) {
		super.onAfterLayoutBinding(contentBinding);
		contentBinding.triagingDecisionDate.initializeDateField(getFragmentManager());
		contentBinding.triagingOutcomeSupervisor.initializeSpinner(triagingOutComeSupervisor);
		if (contentBinding.triagingSpecificSignal.getValue() != null && contentBinding.triagingSpecificSignal.getValue() == YesNo.YES){
			contentBinding.triagingOccurrencePreviously.setVisibility(View.VISIBLE);
		} else if (contentBinding.triagingSpecificSignal.getValue() != null && contentBinding.triagingSpecificSignal.getValue() == YesNo.NO) {
			contentBinding.triagingOccurrencePreviously.setVisibility(GONE);
		}
		if (contentBinding.triagingSignalCategory.getValue() == null){
			contentBinding.triagingCategoryDetailsLevel.setVisibility(GONE);
		}
		contentBinding.triagingSpecificSignal.addValueChangedListener(e->{
			contentBinding.triagingDecisionDate.setVisibility(View.VISIBLE);
			contentBinding.triagingTriagingDecision.setVisibility(View.VISIBLE);
			var value = e.getValue();
			if(value == YesNo.YES){
				contentBinding.triagingOccurrencePreviously.setVisibility(View.VISIBLE);
			}else {
				contentBinding.triagingOccurrencePreviously.setVisibility(GONE);
				contentBinding.triagingDecisionDate.setVisibility(View.VISIBLE);
				contentBinding.triagingTriagingDecision.setVisibility(View.VISIBLE);
				reviewSignal(R.string.message_review_signal);
			}
		});
		contentBinding.triagingSupervisorReview.addValueChangedListener(e->{
			var value = e.getValue();
			if(value == YesNo.NO){
				reviewSignal(R.string.message_review_signal);
			}
		});
		if (contentBinding.triagingSpecificSignal.getValue() != null) {
			contentBinding.triagingOccurrencePreviously.setVisibility(View.VISIBLE);
			contentBinding.triagingDecisionDate.setVisibility(View.VISIBLE);
			contentBinding.triagingTriagingDecision.setVisibility(View.VISIBLE);
		}else if (contentBinding.triagingSpecificSignal.getValue() == null){
			contentBinding.triagingOccurrencePreviously.setVisibility(View.VISIBLE);
			contentBinding.triagingDecisionDate.setVisibility(GONE);
			contentBinding.triagingTriagingDecision.setVisibility(GONE);
		}


		contentBinding.triagingSignalCategory.addValueChangedListener(e->{
			final Set<String> validCategories = new HashSet<>(Set.of("Human", "Environment", "Animal", "POE"));
			String propertyValue = (e.getValue() != null) ? e.getValue().toString() : null;
			if (validCategories.contains(propertyValue)) {
				contentBinding.triagingCategoryDetailsLevel.setVisibility(View.VISIBLE);
			} else {
				contentBinding.triagingCategoryDetailsLevel.setVisibility(GONE);
			}
			if (contentBinding.triagingCategoryDetailsLevel.getValue() != null) {
				try {
					contentBinding.triagingCategoryDetailsLevel.setValue(CategoryDetailsLevel.COMMUNITY);
					SignalCategory category = getSignalCategory(propertyValue);
					setVisibility(contentBinding.triagingCategoryDetailsLevel.getValue().toString(), category,contentBinding);
				}catch (Exception exception){
					System.out.println(exception.getMessage());
				}

			}
			try {
				displayCategories(e.getValue().toString(),contentBinding);
			}catch (Exception exception){
				System.out.println(exception.getMessage());
			}
		});

		contentBinding.triagingOutcomeSupervisor.addValueChangedListener(e->{
			if (e.getValue() == OutComeSupervisor.ISSIGNAL){
				contentBinding.triagingOccurrencePreviously.setVisibility(View.VISIBLE);
			}
		});

		contentBinding.triagingHealthConcern.addValueChangedListener(e->{
			if (e.getValue() == YesNo.YES){
				reviewSignal(R.string.message_relevant_focal);
			}
		});
		contentBinding.triagingCategoryDetailsLevel.addValueChangedListener(e -> {
			var level = (e.getValue() != null) ? e.getValue().toString() : "Community";
			var category =  contentBinding.triagingSignalCategory.getValue();
			setVisibility(level, (SignalCategory) category,contentBinding);
		});

		Ebs selectedEbs = EbsEditActivity.getParentEbs();
		Triaging selectedTriaging = selectedEbs.getTriaging();
		if(selectedTriaging.getCategoryDetailsLevel() != null  ) {
			setVisibility(selectedTriaging.getCategoryDetailsLevel().toString(), selectedTriaging.getSignalCategory(),contentBinding);
		}
		contentBinding.triagingOccurrencePreviously.addValueChangedListener(e->{
			var value = e.getValue();
			if(value == YesNo.YES || value == null){
				contentBinding.triagingTriagingDecision.setValue(EbsTriagingDecision.DISCARD);
			}else {
				contentBinding.triagingTriagingDecision.setValue(EbsTriagingDecision.VERIFY);
			}
		});

	}

	@Override
	public int getEditLayout() {
		return R.layout.fragment_triaging_edit_layout;
	}

	public void setVisibility(String level, SignalCategory category, FragmentTriagingEditLayoutBinding contentBinding) {
		// Hide all category details by default
		contentBinding.triagingHumanCommunityCategoryDetails.setVisibility(View.GONE);
		contentBinding.triagingHumanFacilityCategoryDetails.setVisibility(View.GONE);
		contentBinding.triagingHumanLaboratoryCategoryDetails.setVisibility(View.GONE);
		contentBinding.triagingAnimalCommunityCategoryDetails.setVisibility(View.GONE);
		contentBinding.triagingAnimalFacilityCategoryDetails.setVisibility(View.GONE);
		contentBinding.triagingAnimalLaboratoryCategoryDetails.setVisibility(View.GONE);
		contentBinding.triagingEnvironmentalCategoryDetails.setVisibility(View.GONE);
		contentBinding.triagingPoeCategoryDetails.setVisibility(View.GONE);

		if (level == null || category == null) {
			return;
		}

		boolean isCommunityLevel = "Community".equals(level);
		boolean isFacilityLevel = "Facility".equals(level);
		boolean isLaboratoryLevel = "Laboratory".equals(level);

		switch (category) {
			case HUMAN:
				contentBinding.triagingHumanCommunityCategoryDetails.setVisibility(isCommunityLevel ? View.VISIBLE : View.GONE);
				contentBinding.triagingHumanFacilityCategoryDetails.setVisibility(isFacilityLevel ? View.VISIBLE : View.GONE);
				contentBinding.triagingHumanLaboratoryCategoryDetails.setVisibility(isLaboratoryLevel ? View.VISIBLE : View.GONE);
				break;
			case ANIMAL:
				contentBinding.triagingAnimalCommunityCategoryDetails.setVisibility(isCommunityLevel ? View.VISIBLE : View.GONE);
				contentBinding.triagingAnimalFacilityCategoryDetails.setVisibility(isFacilityLevel ? View.VISIBLE : View.GONE);
				contentBinding.triagingAnimalLaboratoryCategoryDetails.setVisibility(isLaboratoryLevel ? View.VISIBLE : View.GONE);
				break;
			case ENVIRONMENT:
				contentBinding.triagingEnvironmentalCategoryDetails.setVisibility(View.VISIBLE);
				break;
			case POE:
				contentBinding.triagingPoeCategoryDetails.setVisibility(View.VISIBLE);
				break;
		}
	}

	private static @Nullable SignalCategory getSignalCategory(String propertyValue) {
		SignalCategory category = null;
		switch (propertyValue) {
			case "Human":
				category = SignalCategory.HUMAN;
				break;
			case "Environment":
				category = SignalCategory.ENVIRONMENT;
				break;
			case "Animal":
				category = SignalCategory.ANIMAL;
				break;
			case "POE":
				category = SignalCategory.POE;
				break;
		}
		return category;
	}

	public void displayCategories(String property,FragmentTriagingEditLayoutBinding contentBinding){
		List<CategoryDetailsLevel> categories;
		contentBinding.triagingCategoryDetailsLevel.setVisibility(View.VISIBLE);
		switch (property) {
			case "Environment":
			case "POE":
				categories = Arrays.asList();
				contentBinding.triagingCategoryDetailsLevel.setVisibility(GONE);
				break;
			case "Animal":
			case "Human":
				categories = Arrays.asList(CategoryDetailsLevel.COMMUNITY, CategoryDetailsLevel.FACILITY,CategoryDetailsLevel.LABORATORY);
				break;
			default:
				categories = Collections.emptyList();
				contentBinding.triagingCategoryDetailsLevel.setCaption("");
				break;
		}
		try {
			contentBinding.triagingCategoryDetailsLevel.setValue(categories);
//			FieldHelper.updateEnumData(categoryLevel, categories);
		}catch (Exception exception){
			System.out.println(exception.getMessage());
		}
	}

	private void reviewSignal(int message) {
			final ConfirmationDialog signalReviewDialog = new ConfirmationDialog(
					getActivity(),
					R.string.heading_general_notice,
                    message);
					signalReviewDialog.show();
	}

}
