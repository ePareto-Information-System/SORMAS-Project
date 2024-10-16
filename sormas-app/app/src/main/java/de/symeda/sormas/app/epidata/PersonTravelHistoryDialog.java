package de.symeda.sormas.app.epidata;

import static de.symeda.sormas.app.core.notification.NotificationType.ERROR;
import static de.symeda.sormas.app.epidata.EpiDataFragmentHelper.getDiseaseOfCaseOrContact;

import android.content.Context;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;

import java.util.List;

import de.symeda.sormas.api.utils.ValidationException;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.app.BR;
import de.symeda.sormas.app.BaseEditFragment;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.config.ConfigProvider;
import de.symeda.sormas.app.backend.person.Person;
import de.symeda.sormas.app.backend.persontravelhistory.PersonTravelHistory;
import de.symeda.sormas.app.component.Item;
import de.symeda.sormas.app.component.controls.ControlButtonType;
import de.symeda.sormas.app.component.dialog.FormDialog;
import de.symeda.sormas.app.component.validation.FragmentValidator;
import de.symeda.sormas.app.core.notification.NotificationHelper;
import de.symeda.sormas.app.databinding.DialogPersonTravelHistoryEditLayoutBinding;
import de.symeda.sormas.app.databinding.FragmentPersonEditLayoutBinding;
import de.symeda.sormas.app.util.InfrastructureDaoHelper;
import de.symeda.sormas.app.util.InfrastructureFieldsDependencyHandler;

public class PersonTravelHistoryDialog extends FormDialog {

	private final PersonTravelHistory data;
	private DialogPersonTravelHistoryEditLayoutBinding contentBinding;
	private final boolean create;

	PersonTravelHistoryDialog(final FragmentActivity activity, PersonTravelHistory personTravelHistory, PseudonymizableAdo activityRootData, boolean create) {
		super(
			activity,
			R.layout.dialog_root_layout,
			R.layout.dialog_person_travel_history_edit_layout,
			R.layout.dialog_root_three_button_panel_layout,
			R.string.heading_travel,
			-1,
			false,
			UiFieldAccessCheckers.forSensitiveData(personTravelHistory.isPseudonymized()),
			FieldVisibilityCheckers.withDisease(getDiseaseOfCaseOrContact(activityRootData)).andWithCountry(ConfigProvider.getServerCountryCode()));

		this.data = personTravelHistory;
		this.create = create;
	}

	private void setUpHeadingVisibilities() {
	}

	@Override
	protected void setContentBinding(Context context, ViewDataBinding binding, String layoutName) {
		contentBinding = (DialogPersonTravelHistoryEditLayoutBinding) binding;
		binding.setVariable(BR.data, data);
	}

	@Override
	protected void initializeContentView(ViewDataBinding rootBinding, ViewDataBinding buttonPanelBinding) {
		contentBinding.personTravelHistoryDateFrom.initializeDateField(getFragmentManager());
		contentBinding.personTravelHistoryDateTo.initializeDateField(getFragmentManager());

		if (data.getId() == null) {
			setLiveValidationDisabled(true);
		}

		List<Item>
				initialRegions = InfrastructureDaoHelper.loadRegionsByServerCountry();
		List<Item> districts = InfrastructureDaoHelper.loadDistricts(data.getRegion());
		List<Item> communities = InfrastructureDaoHelper.loadCommunities(data.getDistrict());
		InfrastructureFieldsDependencyHandler.instance.initializeRegionFields(
				contentBinding.personTravelHistoryRegion,
				initialRegions,
				data.getRegion(),
				contentBinding.personTravelHistoryDistrict,
				districts,
				data.getDistrict(),
				contentBinding.personTravelHistorySubDistrict,
				communities,
				data.getSubDistrict());

	}


	@Override
	protected void onPositiveClick() {
		setLiveValidationDisabled(false);
		try {
			FragmentValidator.validate(getContext(), contentBinding);
		} catch (ValidationException e) {
			NotificationHelper.showDialogNotification(PersonTravelHistoryDialog.this, ERROR, e.getMessage());
			return;
		}

		super.setCloseOnPositiveButtonClick(true);
		super.onPositiveClick();
	}

	@Override
	public boolean isDeleteButtonVisible() {
		return !create;
	}

	@Override
	public boolean isRounded() {
		return true;
	}

	@Override
	public ControlButtonType getNegativeButtonType() {
		return ControlButtonType.LINE_SECONDARY;
	}

	@Override
	public ControlButtonType getPositiveButtonType() {
		return ControlButtonType.LINE_PRIMARY;
	}

	@Override
	public ControlButtonType getDeleteButtonType() {
		return ControlButtonType.LINE_DANGER;
	}

}
