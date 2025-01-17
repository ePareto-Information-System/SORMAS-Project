package de.symeda.sormas.ui.patienttraveldetailsprior;

import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import de.symeda.sormas.api.riskfactor.PatientTravelDetailsPriorDto;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.utils.AbstractEditForm;

import static de.symeda.sormas.ui.utils.LayoutUtil.*;

public class PatientTravelDetailsPriorEditForm extends AbstractEditForm<PatientTravelDetailsPriorDto> {

    public static final String PATIENT_TRAVEL_DETAILS_PRIOR_HEADING = "patientTravelDetailsPriorHeadingLoc";

    public static final String HTML_LAYOUT = divs(
            loc(PATIENT_TRAVEL_DETAILS_PRIOR_HEADING),
            fluidRowLocs(PatientTravelDetailsPriorDto.DATE_OF_TRAVEL, PatientTravelDetailsPriorDto.PLACE_OF_TRAVEL));

    public PatientTravelDetailsPriorEditForm(boolean create, FieldVisibilityCheckers fieldVisibilityCheckers, UiFieldAccessCheckers fieldAccessCheckers) {
        super(PatientTravelDetailsPriorDto.class, PatientTravelDetailsPriorDto.I18N_PREFIX, create, fieldVisibilityCheckers, fieldAccessCheckers);

        setWidth(960, Unit.PIXELS);
        if (create) {
            hideValidationUntilNextCommit();
        } else {
            addFields();
        }

    }

    @Override
    protected String createHtmlLayout() {
        return HTML_LAYOUT;
    }

    @Override
    protected void addFields() {
        addField(PatientTravelDetailsPriorDto.DATE_OF_TRAVEL, DateField.class);
        addField(PatientTravelDetailsPriorDto.PLACE_OF_TRAVEL, ComboBox.class);

    }
}
