package de.symeda.sormas.ui.foodhistory;

import com.vaadin.ui.Label;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.TextField;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.epidata.EpiDataDto;
import de.symeda.sormas.api.foodhistory.FoodHistoryDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.DateTimeField;
import de.symeda.sormas.ui.utils.NullableOptionGroup;

import static de.symeda.sormas.ui.utils.CssStyles.H3;
import static de.symeda.sormas.ui.utils.CssStyles.H4;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;

public class FoodHistoryForm extends AbstractEditForm<FoodHistoryDto> {
    private static final long serialVersionUID = 1L;
    private static final String FOOD_HISTORY_HEADING_LOC = "foodHistoryHeadingLoc";
    private static final String OBTAIN_HISTORY_HEADING = "obtainHistoryHeading";
    private static final String DAY_1_HEADING = "day1Heading";
    private static final String DAY_2_HEADING = "day2Heading";
    private static final String DAY_3_HEADING = "day3Heading";
    private static final String OTHER_PERSONS_HEADING = "otherPersonsHeading";
    private static final String NUMBER_OF_PERSONS_NO_AFFECTED = "numberOfPersonsNoAffected";

    private static final String HTML_LAYOUT =
               fluidRowLocs(FoodHistoryDto.SUSPECTED_FOOD, FoodHistoryDto.DATE_CONSUMED) +
               fluidRowLocs(FoodHistoryDto.FOOD_SOURCE, FoodHistoryDto.EVENT_TYPE, FoodHistoryDto.EVENT_OTHER_SPECIFY) +
               loc(OBTAIN_HISTORY_HEADING) +
               loc(DAY_1_HEADING) +
               fluidRowLocs(FoodHistoryDto.BREAKFAST, FoodHistoryDto.TOTAL_NO_PERSONS, FoodHistoryDto.FOOD_CONSUMED, FoodHistoryDto.SOURCE_OF_FOOD, FoodHistoryDto.CONSUMED_AT_PLACE) +
               fluidRowLocs(FoodHistoryDto.LUNCH, FoodHistoryDto.TOTAL_NO_PERSONS_L1, FoodHistoryDto.FOOD_CONSUMED_L1, FoodHistoryDto.SOURCE_OF_FOOD_L1, FoodHistoryDto.CONSUMED_AT_PLACE_L1) +
               fluidRowLocs(FoodHistoryDto.SUPPER, FoodHistoryDto.TOTAL_NO_PERSONS_S1, FoodHistoryDto.FOOD_CONSUMED_S1, FoodHistoryDto.SOURCE_OF_FOODS_S1, FoodHistoryDto.CONSUMED_AT_PLACE_S1) +
           loc(DAY_2_HEADING) +
               fluidRowLocs(FoodHistoryDto.BREAKFAST2, FoodHistoryDto.TOTAL_NO_PERSONS2, FoodHistoryDto.FOOD_CONSUMED2, FoodHistoryDto.SOURCE_OF_FOOD2, FoodHistoryDto.CONSUMED_AT_PLACE2) +
               fluidRowLocs(FoodHistoryDto.LUNCH_L2, FoodHistoryDto.TOTAL_NO_PERSONS_L2, FoodHistoryDto.FOOD_CONSUMED_L2, FoodHistoryDto.SOURCE_OF_FOOD_L2, FoodHistoryDto.CONSUMED_AT_PLACE_L2) +
               fluidRowLocs(FoodHistoryDto.SUPPER_S2, FoodHistoryDto.TOTAL_NO_PERSONS_S2, FoodHistoryDto.FOOD_CONSUMED_S2, FoodHistoryDto.SOURCE_OF_FOOD_S2, FoodHistoryDto.CONSUMED_AT_PLACE_S2) +
           loc(DAY_3_HEADING) +
               fluidRowLocs(FoodHistoryDto.BREAKFAST3, FoodHistoryDto.TOTAL_NO_PERSONS3, FoodHistoryDto.FOOD_CONSUMED3, FoodHistoryDto.SOURCE_OF_FOOD3, FoodHistoryDto.CONSUMED_AT_PLACE3) +
               fluidRowLocs(FoodHistoryDto.LUNCH_L3, FoodHistoryDto.TOTAL_NO_PERSONS_L3, FoodHistoryDto.FOOD_CONSUMED_L3, FoodHistoryDto.SOURCE_OF_FOOD_L3, FoodHistoryDto.CONSUMED_AT_PLACE_L3) +
               fluidRowLocs(FoodHistoryDto.SUPPER_S3, FoodHistoryDto.TOTAL_NO_PERSONS_S3, FoodHistoryDto.FOOD_CONSUMED_S3, FoodHistoryDto.SOURCE_OF_FOOD_S3, FoodHistoryDto.CONSUMED_AT_PLACE_S3) +
           loc(OTHER_PERSONS_HEADING) +
           loc(NUMBER_OF_PERSONS_NO_AFFECTED) +
               fluidRowLocs(FoodHistoryDto.NAME_OF_AFFECTED_PERSON, FoodHistoryDto.TEL_NO, FoodHistoryDto.DATE_TIME, FoodHistoryDto.AGE) +
               fluidRowLocs(FoodHistoryDto.NAME_OF_AFFECTED_PERSON2, FoodHistoryDto.TEL_NO2, FoodHistoryDto.DATE_TIME2, FoodHistoryDto.AGE2) +
               fluidRowLocs(FoodHistoryDto.NAME_OF_AFFECTED_PERSON3, FoodHistoryDto.TEL_NO3, FoodHistoryDto.DATE_TIME3, FoodHistoryDto.AGE3) +
               fluidRowLocs(FoodHistoryDto.NAME_OF_AFFECTED_PERSON4, FoodHistoryDto.TEL_NO4, FoodHistoryDto.DATE_TIME4, FoodHistoryDto.AGE4);


    private final Disease disease;
    private final Class<? extends EntityDto> parentClass;
    private final boolean isPseudonymized;

    public FoodHistoryForm(Disease disease, Class<? extends EntityDto> parentClass,
                               boolean isPseudonymized,
                               boolean inJurisdiction,boolean isEditAllowed) {
        super(
                FoodHistoryDto.class,
                FoodHistoryDto.I18N_PREFIX,
                false,
                FieldVisibilityCheckers.withDisease(disease).andWithCountry(FacadeProvider.getConfigFacade().getCountryLocale()),
                UiFieldAccessCheckers.forDataAccessLevel(UserProvider.getCurrent().getPseudonymizableDataAccessLevel(inJurisdiction), isPseudonymized),
                isEditAllowed);
        this.disease = disease;
        this.parentClass = parentClass;
        this.isPseudonymized = isPseudonymized;
        addFields();
    }

    @Override
    protected void addFields() {

        Label foodHistoryHeadingLabel = new Label(I18nProperties.getString(Strings.foodHistoryHeading));
        foodHistoryHeadingLabel.addStyleName(H3);
        getContent().addComponent(foodHistoryHeadingLabel, FOOD_HISTORY_HEADING_LOC);

        Label numberOfPersonsAffected =
                createLabel(I18nProperties.getString(Strings.headingNumberOfPersonsAffected), H4, NUMBER_OF_PERSONS_NO_AFFECTED);

        createLabel(I18nProperties.getString(Strings.headingObtainHistory), H3, OBTAIN_HISTORY_HEADING);
        createLabel(I18nProperties.getString(Strings.headingDay1), H3, DAY_1_HEADING);
        createLabel(I18nProperties.getString(Strings.headingDay2), H3, DAY_2_HEADING);
        createLabel(I18nProperties.getString(Strings.headingDay3), H3, DAY_3_HEADING);

        addFields(FoodHistoryDto.NAME_OF_AFFECTED_PERSON, FoodHistoryDto.NAME_OF_AFFECTED_PERSON2, FoodHistoryDto.NAME_OF_AFFECTED_PERSON3, FoodHistoryDto.NAME_OF_AFFECTED_PERSON4);
        addFields(FoodHistoryDto.TEL_NO, FoodHistoryDto.TEL_NO2, FoodHistoryDto.TEL_NO3, FoodHistoryDto.TEL_NO4);
        addField(FoodHistoryDto.DATE_TIME, DateTimeField.class);
        addField(FoodHistoryDto.DATE_TIME2, DateTimeField.class);
        addField(FoodHistoryDto.DATE_TIME3, DateTimeField.class);
        addField(FoodHistoryDto.DATE_TIME4, DateTimeField.class);
        addFields(FoodHistoryDto.AGE, FoodHistoryDto.AGE2, FoodHistoryDto.AGE3, FoodHistoryDto.AGE4);

        addField(FoodHistoryDto.SUSPECTED_FOOD, TextField.class);
        addField(FoodHistoryDto.DATE_CONSUMED, DateTimeField.class);
        addField(FoodHistoryDto.FOOD_SOURCE, ComboBox.class);
        addField(FoodHistoryDto.EVENT_TYPE, ComboBox.class);
        addField(FoodHistoryDto.EVENT_OTHER_SPECIFY, TextField.class);

        addField(FoodHistoryDto.BREAKFAST, NullableOptionGroup.class);
        TextField totalNoPerson = addField(FoodHistoryDto.TOTAL_NO_PERSONS, TextField.class);
        totalNoPerson.addStyleName("v-captiontext-foodborne");
        addField(FoodHistoryDto.FOOD_CONSUMED, TextField.class);
        addField(FoodHistoryDto.SOURCE_OF_FOOD, TextField.class);
        addField(FoodHistoryDto.CONSUMED_AT_PLACE, TextField.class);
        addField(FoodHistoryDto.LUNCH, NullableOptionGroup.class);
        TextField totalNoPersonl1 = addField(FoodHistoryDto.TOTAL_NO_PERSONS_L1, TextField.class);
        totalNoPersonl1.addStyleName("v-captiontext-foodborne");
        addField(FoodHistoryDto.FOOD_CONSUMED_L1, TextField.class);
        addField(FoodHistoryDto.SOURCE_OF_FOOD_L1, TextField.class);
        addField(FoodHistoryDto.CONSUMED_AT_PLACE_L1, TextField.class);
        addField(FoodHistoryDto.SUPPER, NullableOptionGroup.class);
        TextField totalNoPersons1 = addField(FoodHistoryDto.TOTAL_NO_PERSONS_S1, TextField.class);
        totalNoPersons1.addStyleName("v-captiontext-foodborne");
        addField(FoodHistoryDto.FOOD_CONSUMED_S1, TextField.class);
        addField(FoodHistoryDto.SOURCE_OF_FOODS_S1, TextField.class);
        addField(FoodHistoryDto.CONSUMED_AT_PLACE_S1, TextField.class);

        addField(FoodHistoryDto.BREAKFAST2, NullableOptionGroup.class);
        TextField totalNoPerson2 = addField(FoodHistoryDto.TOTAL_NO_PERSONS2, TextField.class);
        totalNoPerson2.addStyleName("v-captiontext-foodborne");
        addField(FoodHistoryDto.FOOD_CONSUMED2, TextField.class);
        addField(FoodHistoryDto.SOURCE_OF_FOOD2, TextField.class);
        addField(FoodHistoryDto.CONSUMED_AT_PLACE2, TextField.class);
        addField(FoodHistoryDto.LUNCH_L2, NullableOptionGroup.class);
        TextField totalNoPersonl2 = addField(FoodHistoryDto.TOTAL_NO_PERSONS_L2, TextField.class);
        totalNoPersonl2.addStyleName("v-captiontext-foodborne");
        addField(FoodHistoryDto.FOOD_CONSUMED_L2, TextField.class);
        addField(FoodHistoryDto.SOURCE_OF_FOOD_L2, TextField.class);
        addField(FoodHistoryDto.CONSUMED_AT_PLACE_L2, TextField.class);
        addField(FoodHistoryDto.SUPPER_S2, NullableOptionGroup.class);
        TextField totalNoPersonls2 = addField(FoodHistoryDto.TOTAL_NO_PERSONS_S2, TextField.class);
        totalNoPersonls2.addStyleName("v-captiontext-foodborne");
        addField(FoodHistoryDto.FOOD_CONSUMED_S2, TextField.class);
        addField(FoodHistoryDto.SOURCE_OF_FOOD_S2, TextField.class);
        addField(FoodHistoryDto.CONSUMED_AT_PLACE_S2, TextField.class);

        addField(FoodHistoryDto.BREAKFAST3, NullableOptionGroup.class);
        TextField totalNoPerson3 = addField(FoodHistoryDto.TOTAL_NO_PERSONS3, TextField.class);
        totalNoPerson3.addStyleName("v-captiontext-foodborne");
        addField(FoodHistoryDto.FOOD_CONSUMED3, TextField.class);
        addField(FoodHistoryDto.SOURCE_OF_FOOD3, TextField.class);
        addField(FoodHistoryDto.CONSUMED_AT_PLACE3, TextField.class);
        addField(FoodHistoryDto.LUNCH_L3, NullableOptionGroup.class);
        TextField totalNoPersonl3 = addField(FoodHistoryDto.TOTAL_NO_PERSONS_L3, TextField.class);
        totalNoPersonl3.addStyleName("v-captiontext-foodborne");
        addField(FoodHistoryDto.FOOD_CONSUMED_L3, TextField.class);
        addField(FoodHistoryDto.SOURCE_OF_FOOD_L3, TextField.class);
        addField(FoodHistoryDto.CONSUMED_AT_PLACE_L3, TextField.class);
        addField(FoodHistoryDto.SUPPER_S3, NullableOptionGroup.class);
        TextField totalNoPersons3 = addField(FoodHistoryDto.TOTAL_NO_PERSONS_S3, TextField.class);
        totalNoPersons3.addStyleName("v-captiontext-foodborne");
        addField(FoodHistoryDto.FOOD_CONSUMED_S3, TextField.class);
        addField(FoodHistoryDto.SOURCE_OF_FOOD_S3, TextField.class);
        addField(FoodHistoryDto.CONSUMED_AT_PLACE_S3, TextField.class);

        initializeVisibilitiesAndAllowedVisibilities();
        initializeAccessAndAllowedAccesses();

    }

    private Label createLabel(String text, String h4, String location) {
        final Label label = new Label(text);
        label.setId(text);
        label.addStyleName(h4);
        getContent().addComponent(label, location);
        return label;
    }

    @Override
    protected String createHtmlLayout() {
        return HTML_LAYOUT;
    }


}
