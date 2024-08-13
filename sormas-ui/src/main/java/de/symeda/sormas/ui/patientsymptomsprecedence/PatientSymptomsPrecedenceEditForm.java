package de.symeda.sormas.ui.patientsymptomsprecedence;

import de.symeda.sormas.api.riskfactor.PatientSymptomsPrecedenceDto;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.utils.AbstractEditForm;

import static de.symeda.sormas.ui.utils.LayoutUtil.*;
public class PatientSymptomsPrecedenceEditForm extends AbstractEditForm<PatientSymptomsPrecedenceDto> {
    public static final String PATIENT_SYMPTOMS_PRECEDENCE_HEADING = "patientSymptomsPrecedenceHeadingLoc";

    public static final String HTML_LAYOUT = divs(
            loc(PATIENT_SYMPTOMS_PRECEDENCE_HEADING),
            fluidRowLocs(PatientSymptomsPrecedenceDto.NAME, PatientSymptomsPrecedenceDto.CONTACT_ADDRESS, PatientSymptomsPrecedenceDto.PHONE));

    public PatientSymptomsPrecedenceEditForm(boolean create, FieldVisibilityCheckers fieldVisibilityCheckers, UiFieldAccessCheckers fieldAccessCheckers) {
        super(PatientSymptomsPrecedenceDto.class, PatientSymptomsPrecedenceDto.I18N_PREFIX, create, fieldVisibilityCheckers, fieldAccessCheckers);

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
        addFields(

                PatientSymptomsPrecedenceDto.NAME,
                PatientSymptomsPrecedenceDto.CONTACT_ADDRESS,
                PatientSymptomsPrecedenceDto.PHONE);
    }
}
