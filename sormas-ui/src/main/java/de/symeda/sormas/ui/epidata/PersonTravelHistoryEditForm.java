package de.symeda.sormas.ui.epidata;

import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.epidata.PersonTravelHistoryDto;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.person.PersonContactDetailDto;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.FieldHelper;

import static de.symeda.sormas.ui.utils.CssStyles.H3;
import static de.symeda.sormas.ui.utils.LayoutUtil.*;

public class PersonTravelHistoryEditForm extends AbstractEditForm<PersonTravelHistoryDto>  {

    public static final String TRAVEL_HISTORY_HEADING = "travelHistoryHeadingLoc";

    public static String HTML_LAYOUT = divs(
            loc(TRAVEL_HISTORY_HEADING),
            fluidRowLocs(PersonTravelHistoryDto.DATE_FROM, PersonTravelHistoryDto.DATE_TO),
            fluidRowLocs(PersonTravelHistoryDto.REGION, PersonTravelHistoryDto.DISTRICT, PersonTravelHistoryDto.SUB_DISTRICT),
            fluidRowLocs(PersonTravelHistoryDto.VILLAGE)
    );

    private Disease disease;
    private ComboBox comboBoxTravelPeriodType;
    private DateField dateFieldDateFrom;
    private DateField dateFieldDateTo;
    private ComboBox comboBoxRegion;
    private ComboBox comboBoxDistrict;
    private ComboBox comboBoxSubDistrict;
    private TextField textFieldVillage;

    public PersonTravelHistoryEditForm(boolean create, FieldVisibilityCheckers fieldVisibilityCheckers, UiFieldAccessCheckers fieldAccessCheckers) {
        super(PersonTravelHistoryDto.class, PersonTravelHistoryDto.I18N_PREFIX, create, fieldVisibilityCheckers, fieldAccessCheckers);

        setWidth(960, Unit.PIXELS);
        setHeightUndefined();

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



        dateFieldDateFrom = addDateField(PersonTravelHistoryDto.DATE_FROM, DateField.class, -1);
        dateFieldDateTo = addDateField(PersonTravelHistoryDto.DATE_TO, DateField.class, -1);
        comboBoxRegion = addField(PersonTravelHistoryDto.REGION, ComboBox.class);
        comboBoxDistrict =  addField(PersonTravelHistoryDto.DISTRICT, ComboBox.class);
        comboBoxSubDistrict = addField(PersonTravelHistoryDto.SUB_DISTRICT, ComboBox.class);
        addListenersForLocation(comboBoxRegion, comboBoxDistrict, comboBoxSubDistrict);
        comboBoxRegion.addItems(FacadeProvider.getRegionFacade().getAllActiveByServerCountry());

        textFieldVillage = addField(PersonTravelHistoryDto.VILLAGE, TextField.class);

        addValidateDateField(dateFieldDateFrom, dateFieldDateTo);



    }

    private void addValidateDateField(DateField dateFieldDateFrom, DateField dateFieldDateTo) {
        dateFieldDateFrom.addValueChangeListener(e -> {
            if (dateFieldDateTo.getValue() != null && dateFieldDateFrom.getValue().after(dateFieldDateTo.getValue())) {
                dateFieldDateFrom.setValue(dateFieldDateTo.getValue());
            }
        });

        dateFieldDateTo.addValueChangeListener(e -> {
            if (dateFieldDateFrom.getValue() != null && dateFieldDateFrom.getValue().after(dateFieldDateTo.getValue())) {
                dateFieldDateTo.setValue(dateFieldDateFrom.getValue());
            }
        });
    }

    private void addListenersForLocation(
            ComboBox regionField,
            ComboBox districtField,
            ComboBox communityField) {

        regionField.addValueChangeListener(e -> {
            RegionReferenceDto regionDto = (RegionReferenceDto) e.getProperty().getValue();
            FieldHelper.updateItems(districtField, regionDto != null ? FacadeProvider.getDistrictFacade().getAllActiveByRegion(regionDto.getUuid()) : null);
        });
        districtField.addValueChangeListener(e -> {
            DistrictReferenceDto districtDto = (DistrictReferenceDto) e.getProperty().getValue();
            FieldHelper.updateItems(
                    communityField,
                    districtDto != null ? FacadeProvider.getCommunityFacade().getAllActiveByDistrict(districtDto.getUuid()) : null);
        });
    }


}
