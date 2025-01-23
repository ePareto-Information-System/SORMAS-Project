package de.symeda.sormas.app.riskfactor;

import static de.symeda.sormas.app.core.notification.NotificationType.ERROR;
import static de.symeda.sormas.app.epidata.EpiDataFragmentHelper.getDiseaseOfCaseOrContact;

import android.content.Context;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;

import de.symeda.sormas.api.utils.TravelLocation;
import de.symeda.sormas.api.utils.ValidationException;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.app.BR;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.config.ConfigProvider;
import de.symeda.sormas.app.backend.patienttraveldetailsprior.PatientTravelDetailsPrior;
import de.symeda.sormas.app.component.controls.ControlButtonType;
import de.symeda.sormas.app.component.dialog.FormDialog;
import de.symeda.sormas.app.component.validation.FragmentValidator;
import de.symeda.sormas.app.core.notification.NotificationHelper;
import de.symeda.sormas.app.databinding.DialogPatientTravelDetailsPriorEditLayoutBinding;

public class PatientTravelDetailsPriorDialog extends FormDialog {

    private final PatientTravelDetailsPrior data;
    private DialogPatientTravelDetailsPriorEditLayoutBinding contentBinding;
    private final boolean create;

    public PatientTravelDetailsPriorDialog(final FragmentActivity activity, PatientTravelDetailsPrior patientTravelDetailsPrior, PseudonymizableAdo activityRootData, boolean create) {
        super(
                activity,
                R.layout.dialog_root_layout,
                R.layout.dialog_patient_travel_details_prior_edit_layout,
                R.layout.dialog_root_three_button_panel_layout,
                R.string.heading_travel_information,
                -1,
                false,
                UiFieldAccessCheckers.forSensitiveData(patientTravelDetailsPrior.isPseudonymized()),
                FieldVisibilityCheckers.withDisease(getDiseaseOfCaseOrContact(activityRootData)).andWithCountry(ConfigProvider.getServerCountryCode()));
        this.data = patientTravelDetailsPrior;
        this.create = create;
    }

    private void setUpHeadingVisibilities() {
    }

    public PatientTravelDetailsPrior getData() {
        return data;
    }

    @Override
    protected void setContentBinding(Context context, ViewDataBinding binding, String layoutName) {
        contentBinding = (DialogPatientTravelDetailsPriorEditLayoutBinding) binding;
        binding.setVariable(BR.data, data);
    }

    @Override
    protected void initializeContentView(ViewDataBinding rootBinding, ViewDataBinding buttonPanelBinding) {
        contentBinding.patientTravelDetailsPriorDateOfTravel.initializeDateField(getFragmentManager());
        contentBinding.setTravelClass(TravelLocation.class);

        if (data.getId() == null) {
            setLiveValidationDisabled(true);
        }
    }

    @Override
    protected void onPositiveClick() {
        setLiveValidationDisabled(false);
        try {
            FragmentValidator.validate(getContext(), contentBinding);
            this.data.setDateOfTravel(contentBinding.patientTravelDetailsPriorDateOfTravel.getValue());

            Object placeOfTravelValue = contentBinding.patientTravelDetailsPriorPlaceOfTravel.getValue();
            if (placeOfTravelValue instanceof TravelLocation) {
                this.data.setPlaceOfTravel((TravelLocation) placeOfTravelValue);
            } else {
                throw new ValidationException("Invalid type for place of travel. Expected TravelLocation.");
            }
        } catch (ValidationException e) {
            NotificationHelper.showDialogNotification(PatientTravelDetailsPriorDialog.this, ERROR, e.getMessage());
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
