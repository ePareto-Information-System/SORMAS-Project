package de.symeda.sormas.ui.containmentmeasure;

import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.TextField;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.epidata.ContainmentMeasureDto;
import de.symeda.sormas.api.epidata.PersonTravelHistoryDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.person.PersonContactDetailDto;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.FieldHelper;

import static de.symeda.sormas.ui.utils.LayoutUtil.*;

public class ContainmentMeasureEditForm extends AbstractEditForm<ContainmentMeasureDto>  {

    public static final String CONTAIMENT_MEASURES_HEADING = "containmentMeasuresHeadingLoc";

    public static String HTML_LAYOUT = divs(
            loc(CONTAIMENT_MEASURES_HEADING),
            fluidRowLocs(ContainmentMeasureDto.LOCATION_OF_WORM),
            fluidRowLocs(ContainmentMeasureDto.DATE_WORM_DETECTED_EMERGENCE, ContainmentMeasureDto.DATE_WORM_DETECT_BY_SUPERVISOR),
            fluidRowLocs(ContainmentMeasureDto.DATE_CONFIRMED, ContainmentMeasureDto.DATE_OF_GUINEA_WORM_EXPULLED),
            fluidRowLocs(ContainmentMeasureDto.REGULAR_BANDAGING, ContainmentMeasureDto.COMPLETELY_EXTRACTED)
    );


    public ContainmentMeasureEditForm(boolean create, FieldVisibilityCheckers fieldVisibilityCheckers, UiFieldAccessCheckers fieldAccessCheckers) {
        super(ContainmentMeasureDto.class, PersonContactDetailDto.I18N_PREFIX, create, fieldVisibilityCheckers, fieldAccessCheckers);

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
                ContainmentMeasureDto.LOCATION_OF_WORM,
                ContainmentMeasureDto.DATE_WORM_DETECTED_EMERGENCE,
                ContainmentMeasureDto.DATE_WORM_DETECT_BY_SUPERVISOR,
                ContainmentMeasureDto.DATE_CONFIRMED,
                ContainmentMeasureDto.DATE_OF_GUINEA_WORM_EXPULLED,
                ContainmentMeasureDto.REGULAR_BANDAGING,
                ContainmentMeasureDto.COMPLETELY_EXTRACTED);
    }

}
