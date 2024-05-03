package de.symeda.sormas.ui.contaminationsource;

import de.symeda.sormas.api.epidata.ContaminationSourceDto;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.utils.AbstractEditForm;

import static de.symeda.sormas.ui.utils.LayoutUtil.*;

public class ContaminationSourcesEditForm extends AbstractEditForm<ContaminationSourceDto>  {

    public static final String CONTAMINATION_SOURCES_HEADING = "contaminationSourcesHeadingLoc";

    public static String HTML_LAYOUT = divs(
            loc(CONTAMINATION_SOURCES_HEADING),
            fluidRowLocs(ContaminationSourceDto.NAME),
            fluidRowLocs(ContaminationSourceDto.LONGITUDE, ContaminationSourceDto.LATITUDE),
            fluidRowLocs(ContaminationSourceDto.TYPE, ContaminationSourceDto.SOURCE),
            fluidRowLocs(ContaminationSourceDto.TREATED_WITH_ABATE, ContaminationSourceDto.ABATE_TREATMENT_DATE)
    );


    public ContaminationSourcesEditForm(boolean create, FieldVisibilityCheckers fieldVisibilityCheckers, UiFieldAccessCheckers fieldAccessCheckers) {
        super(ContaminationSourceDto.class, ContaminationSourceDto.I18N_PREFIX, create, fieldVisibilityCheckers, fieldAccessCheckers);

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
                ContaminationSourceDto.NAME,
                ContaminationSourceDto.LONGITUDE,
                ContaminationSourceDto.LATITUDE,
                ContaminationSourceDto.TYPE,
                ContaminationSourceDto.SOURCE,
                ContaminationSourceDto.TREATED_WITH_ABATE,
                ContaminationSourceDto.ABATE_TREATMENT_DATE);
    }
}
