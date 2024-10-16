package de.symeda.sormas.app.epidata;

import static de.symeda.sormas.app.core.notification.NotificationType.ERROR;
import static de.symeda.sormas.app.epidata.EpiDataFragmentHelper.getDiseaseOfCaseOrContact;

import android.content.Context;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;

import de.symeda.sormas.api.utils.ValidationException;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.app.BR;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.config.ConfigProvider;
import de.symeda.sormas.app.backend.contaminationsource.ContaminationSource;
import de.symeda.sormas.app.component.controls.ControlButtonType;
import de.symeda.sormas.app.component.dialog.FormDialog;
import de.symeda.sormas.app.component.validation.FragmentValidator;
import de.symeda.sormas.app.core.notification.NotificationHelper;
import de.symeda.sormas.app.databinding.DialogContaminationSourceEditLayoutBinding;

public class ContaminationSourceDialog extends FormDialog {

	private final ContaminationSource data;
	private DialogContaminationSourceEditLayoutBinding contentBinding;
	private final boolean create;

	ContaminationSourceDialog(final FragmentActivity activity, ContaminationSource contaminationSource, PseudonymizableAdo activityRootData, boolean create) {
		super(
			activity,
			R.layout.dialog_root_layout,
			R.layout.dialog_contamination_source_edit_layout,
			R.layout.dialog_root_three_button_panel_layout,
			R.string.heading_travel,
			-1,
			false,
			UiFieldAccessCheckers.forSensitiveData(contaminationSource.isPseudonymized()),
			FieldVisibilityCheckers.withDisease(getDiseaseOfCaseOrContact(activityRootData)).andWithCountry(ConfigProvider.getServerCountryCode()));

		this.data = contaminationSource;
		this.create = create;
	}

	private void setUpHeadingVisibilities() {
	}

	@Override
	protected void setContentBinding(Context context, ViewDataBinding binding, String layoutName) {
		contentBinding = (DialogContaminationSourceEditLayoutBinding) binding;
		binding.setVariable(BR.data, data);
	}

	@Override
	protected void initializeContentView(ViewDataBinding rootBinding, ViewDataBinding buttonPanelBinding) {
		contentBinding.contaminationSourceAbateTreatmentDate.initializeDateField(getFragmentManager());
		contentBinding.setYesNoClass(YesNo.class);

		if (data.getId() == null) {
			setLiveValidationDisabled(true);
		}



	}


	@Override
	protected void onPositiveClick() {
		setLiveValidationDisabled(false);
		try {
			FragmentValidator.validate(getContext(), contentBinding);
		} catch (ValidationException e) {
			NotificationHelper.showDialogNotification(ContaminationSourceDialog.this, ERROR, e.getMessage());
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
