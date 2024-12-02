package de.symeda.sormas.app.foodhistory;

import static de.symeda.sormas.app.core.notification.NotificationType.ERROR;
import static de.symeda.sormas.app.epidata.EpiDataFragmentHelper.getDiseaseOfCaseOrContact;

import android.content.Context;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;

import de.symeda.sormas.api.utils.ValidationException;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.app.BR;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.affectedperson.AffectedPerson;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.config.ConfigProvider;
import de.symeda.sormas.app.component.controls.ControlButtonType;
import de.symeda.sormas.app.component.dialog.FormDialog;
import de.symeda.sormas.app.component.validation.FragmentValidator;
import de.symeda.sormas.app.core.notification.NotificationHelper;
import de.symeda.sormas.app.databinding.DialogAffectedPersonEditLayoutBinding;

public class AffectedPersonDialog extends FormDialog {

    private final AffectedPerson data;
    private DialogAffectedPersonEditLayoutBinding contentBinding;
    private final boolean create;

    public AffectedPersonDialog(final FragmentActivity activity, AffectedPerson affectedPerson, PseudonymizableAdo activityRootData, boolean create) {
        super(
                activity,
                R.layout.dialog_root_layout,
                R.layout.dialog_affected_person_edit_layout,
                R.layout.dialog_root_three_button_panel_layout,
                R.string.heading_affected_person_details,
                -1,
                false,
                UiFieldAccessCheckers.forSensitiveData(affectedPerson.isPseudonymized()),
                FieldVisibilityCheckers.withDisease(getDiseaseOfCaseOrContact(activityRootData)).andWithCountry(ConfigProvider.getServerCountryCode()));

        this.data = affectedPerson;
        this.create = create;
    }

    private void setUpHeadingVisibilities() {
    }

    @Override
    protected void setContentBinding(Context context, ViewDataBinding binding, String layoutName) {
        contentBinding = (DialogAffectedPersonEditLayoutBinding) binding;
        binding.setVariable(BR.data, data);
    }

    @Override
    protected void initializeContentView(ViewDataBinding rootBinding, ViewDataBinding buttonPanelBinding) {

        contentBinding.affectedPersonDateTime.initializeDateTimeField(getFragmentManager());

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
            NotificationHelper.showDialogNotification(AffectedPersonDialog.this, ERROR, e.getMessage());
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
