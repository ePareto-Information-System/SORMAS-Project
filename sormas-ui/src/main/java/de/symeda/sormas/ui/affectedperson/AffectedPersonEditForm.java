package de.symeda.sormas.ui.affectedperson;

import de.symeda.sormas.api.foodhistory.AffectedPersonDto;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.utils.AbstractEditForm;

import static de.symeda.sormas.ui.utils.LayoutUtil.*;

public class AffectedPersonEditForm extends AbstractEditForm<AffectedPersonDto>  {

    public static final String AFFECTED_PERSON_HEADING = "affectedPersonHeadingLoc";

    public static final String HTML_LAYOUT = divs(
            loc(AFFECTED_PERSON_HEADING),
            fluidRowLocs(AffectedPersonDto.NAME_OF_AFFECTED_PERSON, AffectedPersonDto.TEL_NO) +
            fluidRowLocs(AffectedPersonDto.DATE_TIME, AffectedPersonDto.AGE));


    public AffectedPersonEditForm(boolean create, FieldVisibilityCheckers fieldVisibilityCheckers, UiFieldAccessCheckers fieldAccessCheckers) {
        super(AffectedPersonDto.class, AffectedPersonDto.I18N_PREFIX, create, fieldVisibilityCheckers, fieldAccessCheckers);

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
                AffectedPersonDto.NAME_OF_AFFECTED_PERSON,
                AffectedPersonDto.TEL_NO,
                AffectedPersonDto.DATE_TIME,
                AffectedPersonDto.AGE);
    }

}
