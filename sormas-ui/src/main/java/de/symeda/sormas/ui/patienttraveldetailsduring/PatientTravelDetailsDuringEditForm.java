package de.symeda.sormas.ui.patienttraveldetailsduring;

import com.vaadin.server.Sizeable;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import de.symeda.sormas.api.riskfactor.PatientTravelDetailsDuringDto;
import de.symeda.sormas.api.riskfactor.PatientTravelDetailsPriorDto;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.utils.AbstractEditForm;

import static de.symeda.sormas.ui.utils.LayoutUtil.*;

public class PatientTravelDetailsDuringEditForm extends AbstractEditForm<PatientTravelDetailsDuringDto> {

    public static final String PATIENT_TRAVEL_DETAILS_DURING_HEADING = "patientTravelDetailsDuringHeadingLoc";

    public static final String HTML_LAYOUT = divs(
            loc(PATIENT_TRAVEL_DETAILS_DURING_HEADING),
            fluidRowLocs(PatientTravelDetailsDuringDto.DATE_OF_TRAVEL, PatientTravelDetailsDuringDto.PLACE_OF_TRAVEL));

    public PatientTravelDetailsDuringEditForm(boolean create, FieldVisibilityCheckers fieldVisibilityCheckers, UiFieldAccessCheckers fieldAccessCheckers) {
        super(PatientTravelDetailsDuringDto.class, PatientTravelDetailsDuringDto.I18N_PREFIX, create, fieldVisibilityCheckers, fieldAccessCheckers);

        setWidth(960, Sizeable.Unit.PIXELS);
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
