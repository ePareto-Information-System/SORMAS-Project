package de.symeda.sormas.app.riskfactor;

import static de.symeda.sormas.app.core.notification.NotificationType.ERROR;
import android.content.Context;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import de.symeda.sormas.api.utils.ValidationException;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.app.BR;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.config.ConfigProvider;
import de.symeda.sormas.app.backend.patientsymptomsprecedence.PatientSymptomsPrecedence;
import de.symeda.sormas.app.component.controls.ControlButtonType;
import de.symeda.sormas.app.component.dialog.FormDialog;
import de.symeda.sormas.app.component.validation.FragmentValidator;
import de.symeda.sormas.app.core.notification.NotificationHelper;
import de.symeda.sormas.app.databinding.DialogPatientSymptomsPrecedenceEditLayoutBinding;
import static de.symeda.sormas.app.epidata.EpiDataFragmentHelper.getDiseaseOfCaseOrContact;

public class PatientSymptomsPrecedenceDialog extends FormDialog {

    private final PatientSymptomsPrecedence data;
    private DialogPatientSymptomsPrecedenceEditLayoutBinding contentBinding;
    private final boolean create;

    public PatientSymptomsPrecedenceDialog(final FragmentActivity activity, PatientSymptomsPrecedence patientSymptomsPrecedence, PseudonymizableAdo activityRootData, boolean create) {
        super(
                activity,
                R.layout.dialog_root_layout,
                R.layout.dialog_patient_symptoms_precedence_edit_layout,
                R.layout.dialog_root_three_button_panel_layout,
                R.string.heading_patient_symptom,
                -1,
                false,
                UiFieldAccessCheckers.forSensitiveData(patientSymptomsPrecedence.isPseudonymized()),
                FieldVisibilityCheckers.withDisease(getDiseaseOfCaseOrContact(activityRootData)).andWithCountry(ConfigProvider.getServerCountryCode()));
        this.data = patientSymptomsPrecedence;
        this.create = create;
    }

    private void setUpHeadingVisibilities() {
    }

    public PatientSymptomsPrecedence getData() {
        return data;
    }

    @Override
    protected void setContentBinding(Context context, ViewDataBinding binding, String layoutName) {
        contentBinding = (DialogPatientSymptomsPrecedenceEditLayoutBinding) binding;
        binding.setVariable(BR.data, data);
    }

    @Override
    protected void initializeContentView(ViewDataBinding rootBinding, ViewDataBinding buttonPanelBinding) {

        if (data.getId() == null) {
            setLiveValidationDisabled(true);
        }
    }

    @Override
    protected void onPositiveClick() {
        setLiveValidationDisabled(false);
        try {
            FragmentValidator.validate(getContext(), contentBinding);
            this.data.setName(contentBinding.patientSymptomsPrecedenceName.getValue());
            this.data.setContactAddress(contentBinding.patientSymptomsPrecedenceContactAddress.getValue());
            this.data.setPhone(contentBinding.patientSymptomsPrecedencePhone.getValue());
        } catch (ValidationException e) {
            NotificationHelper.showDialogNotification(PatientSymptomsPrecedenceDialog.this, ERROR, e.getMessage());
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
