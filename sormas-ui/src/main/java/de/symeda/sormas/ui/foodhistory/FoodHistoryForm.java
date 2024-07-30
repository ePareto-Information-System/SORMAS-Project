package de.symeda.sormas.ui.foodhistory;

import com.vaadin.ui.Label;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.foodhistory.FoodHistoryDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.EventType;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.affectedperson.AffectedPersonField;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.DateTimeField;
import de.symeda.sormas.ui.utils.FieldHelper;
import de.symeda.sormas.ui.utils.NullableOptionGroup;

import java.util.Arrays;

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
    //@formatter:off

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
               fluidRowLocs(OTHER_PERSONS_HEADING) +
               fluidRowLocs(FoodHistoryDto.NUMBER_OF_PEOPLE_ATE_IMPLICATED_FOOD, FoodHistoryDto.NUMBER_AFFECTED) +
               loc(FoodHistoryDto.AFFECTED_PERSONS);

    private final Class<? extends EntityDto> parentClass;
    private final boolean isPseudonymized;
    public AffectedPersonField affectedPersonField;
    private int maxAffectedPersons = 0;
    public FoodHistoryForm(Disease disease,
                           Class<? extends EntityDto> parentClass,
                           boolean isPseudonymized,
                               boolean inJurisdiction,boolean isEditAllowed) {
        super(
                FoodHistoryDto.class,
                FoodHistoryDto.I18N_PREFIX,
                false,
                FieldVisibilityCheckers.withDisease(disease).andWithCountry(FacadeProvider.getConfigFacade().getCountryLocale()),
                UiFieldAccessCheckers.forDataAccessLevel(UserProvider.getCurrent().getPseudonymizableDataAccessLevel(inJurisdiction), isPseudonymized),
                isEditAllowed, disease);
        this.isPseudonymized = isPseudonymized;
        this.disease = disease;
        this.parentClass = parentClass;
        addFields();
    }

    @Override
    protected void addFields() {

        if (parentClass == CaseDataDto.class) {
            addAffectedPersonFields();
        }

        Label foodHistoryHeadingLabel = new Label(I18nProperties.getString(Strings.foodHistoryHeading));
        foodHistoryHeadingLabel.addStyleName(H3);
        getContent().addComponent(foodHistoryHeadingLabel, FOOD_HISTORY_HEADING_LOC);

        createLabel(I18nProperties.getString(Strings.headingNumberOfPersonsAffected), H4, NUMBER_OF_PERSONS_NO_AFFECTED);
        createLabel(I18nProperties.getString(Strings.headingObtainHistory), H3, OBTAIN_HISTORY_HEADING);
        createLabel(I18nProperties.getString(Strings.headingDay1), H3, DAY_1_HEADING);
        createLabel(I18nProperties.getString(Strings.headingDay2), H3, DAY_2_HEADING);
        createLabel(I18nProperties.getString(Strings.headingDay3), H3, DAY_3_HEADING);
        Label otherPersonHeading = createLabel(I18nProperties.getString(Strings.headingOtherPersons), H3, OTHER_PERSONS_HEADING);
        otherPersonHeading.addStyleName("otherPersonHeading-middle");

        addField(FoodHistoryDto.NUMBER_OF_PEOPLE_ATE_IMPLICATED_FOOD);

        TextField numberAffectedField = addField(FoodHistoryDto.NUMBER_AFFECTED, TextField.class);
        numberAffectedField.addValueChangeListener(event -> {
            String value = (String) event.getProperty().getValue();
            if (value != null && !value.isEmpty()) {
                int numberAffected = Integer.parseInt(value);
                maxAffectedPersons = numberAffected;
                affectedPersonField.setMaxAffectedPersons(maxAffectedPersons);
            }
        });

        addField(FoodHistoryDto.SUSPECTED_FOOD, TextField.class);
        addField(FoodHistoryDto.DATE_CONSUMED, DateTimeField.class);
        addField(FoodHistoryDto.FOOD_SOURCE, ComboBox.class);
        ComboBox eventType = addField(FoodHistoryDto.EVENT_TYPE, ComboBox.class);
        TextField eventOtherSpecify = addField(FoodHistoryDto.EVENT_OTHER_SPECIFY, TextField.class);

        NullableOptionGroup breakFast = addField(FoodHistoryDto.BREAKFAST, NullableOptionGroup.class);
        TextField totalNoPerson = addField(FoodHistoryDto.TOTAL_NO_PERSONS, TextField.class);
        totalNoPerson.addStyleName("v-captiontext-foodborne");
        TextField foodConsumed = addField(FoodHistoryDto.FOOD_CONSUMED, TextField.class);
        TextField sourceOfFood = addField(FoodHistoryDto.SOURCE_OF_FOOD, TextField.class);
        NullableOptionGroup consumedAtPlace = addField(FoodHistoryDto.CONSUMED_AT_PLACE, NullableOptionGroup.class);

        NullableOptionGroup lunch = addField(FoodHistoryDto.LUNCH, NullableOptionGroup.class);
        TextField totalNoPersonl1 = addField(FoodHistoryDto.TOTAL_NO_PERSONS_L1, TextField.class);
        totalNoPersonl1.addStyleName("v-captiontext-foodborne");
        TextField foodConsumedL1 = addField(FoodHistoryDto.FOOD_CONSUMED_L1, TextField.class);
        TextField sourceOfFoodL1 = addField(FoodHistoryDto.SOURCE_OF_FOOD_L1, TextField.class);
        NullableOptionGroup consumedAtPlaceL1 = addField(FoodHistoryDto.CONSUMED_AT_PLACE_L1, NullableOptionGroup.class);

        NullableOptionGroup supper = addField(FoodHistoryDto.SUPPER, NullableOptionGroup.class);
        TextField totalNoPersons1 = addField(FoodHistoryDto.TOTAL_NO_PERSONS_S1, TextField.class);
        totalNoPersons1.addStyleName("v-captiontext-foodborne");
        TextField foodConsumedS1 = addField(FoodHistoryDto.FOOD_CONSUMED_S1, TextField.class);
        TextField sourceOfFoodS1 = addField(FoodHistoryDto.SOURCE_OF_FOODS_S1, TextField.class);
        NullableOptionGroup consumedAtPlaceS1 = addField(FoodHistoryDto.CONSUMED_AT_PLACE_S1, NullableOptionGroup.class);

        setFieldsVisible(false, totalNoPerson, foodConsumed, sourceOfFood, consumedAtPlace, totalNoPersonl1, foodConsumedL1, sourceOfFoodL1, consumedAtPlaceL1, totalNoPersons1, foodConsumedS1, sourceOfFoodS1, consumedAtPlaceS1);
        FieldHelper.setVisibleWhen(breakFast, Arrays.asList(totalNoPerson, foodConsumed, sourceOfFood, consumedAtPlace), Arrays.asList(YesNo.YES), true);
        FieldHelper.setVisibleWhen(lunch, Arrays.asList(totalNoPersonl1, foodConsumedL1, sourceOfFoodL1, consumedAtPlaceL1), Arrays.asList(YesNo.YES), true);
        FieldHelper.setVisibleWhen(supper, Arrays.asList(totalNoPersons1, foodConsumedS1, sourceOfFoodS1, consumedAtPlaceS1), Arrays.asList(YesNo.YES), true);

        NullableOptionGroup breakfast2 = addField(FoodHistoryDto.BREAKFAST2, NullableOptionGroup.class);
        TextField totalNoPerson2 = addField(FoodHistoryDto.TOTAL_NO_PERSONS2, TextField.class);
        totalNoPerson2.addStyleName("v-captiontext-foodborne");
        TextField foodConsumedS2 = addField(FoodHistoryDto.FOOD_CONSUMED2, TextField.class);
        TextField sourceOfFoodS2 = addField(FoodHistoryDto.SOURCE_OF_FOOD2, TextField.class);
        NullableOptionGroup consumedAtPlaceS2 = addField(FoodHistoryDto.CONSUMED_AT_PLACE2, NullableOptionGroup.class);

        NullableOptionGroup lunchl2 = addField(FoodHistoryDto.LUNCH_L2, NullableOptionGroup.class);
        TextField totalNoPersonl2 = addField(FoodHistoryDto.TOTAL_NO_PERSONS_L2, TextField.class);
        totalNoPersonl2.addStyleName("v-captiontext-foodborne");
        TextField foodConsumedl2 = addField(FoodHistoryDto.FOOD_CONSUMED_L2, TextField.class);
        TextField sourceOfFoodl2 = addField(FoodHistoryDto.SOURCE_OF_FOOD_L2, TextField.class);
        NullableOptionGroup consumedAtPlacel2 = addField(FoodHistoryDto.CONSUMED_AT_PLACE_L2, NullableOptionGroup.class);

        NullableOptionGroup suppers2 = addField(FoodHistoryDto.SUPPER_S2, NullableOptionGroup.class);
        TextField totalNoPersonls2 = addField(FoodHistoryDto.TOTAL_NO_PERSONS_S2, TextField.class);
        totalNoPersonls2.addStyleName("v-captiontext-foodborne");
        TextField foodConsumeds2  = addField(FoodHistoryDto.FOOD_CONSUMED_S2, TextField.class);
        TextField sourceOfFoods2 = addField(FoodHistoryDto.SOURCE_OF_FOOD_S2, TextField.class);
        NullableOptionGroup consumedAtPlaces2 = addField(FoodHistoryDto.CONSUMED_AT_PLACE_S2, NullableOptionGroup.class);

        setFieldsVisible(false, totalNoPerson2, foodConsumedS2, sourceOfFoodS2, consumedAtPlaceS2, totalNoPersonl2, foodConsumedl2, sourceOfFoodl2, consumedAtPlacel2, totalNoPersonls2, foodConsumeds2, sourceOfFoods2, consumedAtPlaces2);
        FieldHelper.setVisibleWhen(breakfast2, Arrays.asList(totalNoPerson2, foodConsumedS2, sourceOfFoodS2, consumedAtPlaceS2), Arrays.asList(YesNo.YES), true);
        FieldHelper.setVisibleWhen(lunchl2, Arrays.asList(totalNoPersonl2, foodConsumedl2, sourceOfFoodl2, consumedAtPlacel2), Arrays.asList(YesNo.YES), true);
        FieldHelper.setVisibleWhen(suppers2, Arrays.asList(totalNoPersonls2, foodConsumeds2, sourceOfFoods2, consumedAtPlaces2), Arrays.asList(YesNo.YES), true);

        NullableOptionGroup breakFast3 = addField(FoodHistoryDto.BREAKFAST3, NullableOptionGroup.class);
        TextField totalNoPerson3 = addField(FoodHistoryDto.TOTAL_NO_PERSONS3, TextField.class);
        totalNoPerson3.addStyleName("v-captiontext-foodborne");
        TextField foodConsumed3 = addField(FoodHistoryDto.FOOD_CONSUMED3, TextField.class);
        TextField sourceOfFood3 = addField(FoodHistoryDto.SOURCE_OF_FOOD3, TextField.class);
        NullableOptionGroup consumedAtPlaces3 = addField(FoodHistoryDto.CONSUMED_AT_PLACE3, NullableOptionGroup.class);

        NullableOptionGroup lunchl3 = addField(FoodHistoryDto.LUNCH_L3, NullableOptionGroup.class);
        TextField totalNoPersonl3 = addField(FoodHistoryDto.TOTAL_NO_PERSONS_L3, TextField.class);
        totalNoPersonl3.addStyleName("v-captiontext-foodborne");
        TextField foodConsumedl3 = addField(FoodHistoryDto.FOOD_CONSUMED_L3, TextField.class);
        TextField sourceOfFoodl3 = addField(FoodHistoryDto.SOURCE_OF_FOOD_L3, TextField.class);
        NullableOptionGroup consumedAtPlacesl3 = addField(FoodHistoryDto.CONSUMED_AT_PLACE_L3, NullableOptionGroup.class);

        NullableOptionGroup suppers3 = addField(FoodHistoryDto.SUPPER_S3, NullableOptionGroup.class);
        TextField totalNoPersons3 = addField(FoodHistoryDto.TOTAL_NO_PERSONS_S3, TextField.class);
        totalNoPersons3.addStyleName("v-captiontext-foodborne");
        TextField foodConsumeds3 = addField(FoodHistoryDto.FOOD_CONSUMED_S3, TextField.class);
        TextField sourceOfFoods3 = addField(FoodHistoryDto.SOURCE_OF_FOOD_S3, TextField.class);
        NullableOptionGroup consumedAtPlacess3 = addField(FoodHistoryDto.CONSUMED_AT_PLACE_S3, NullableOptionGroup.class);

        setFieldsVisible(false, totalNoPerson3, foodConsumed3, sourceOfFood3, consumedAtPlaces3, totalNoPersonl3, foodConsumedl3, sourceOfFoodl3, consumedAtPlacesl3, totalNoPersons3, foodConsumeds3, sourceOfFoods3, consumedAtPlacess3);
        FieldHelper.setVisibleWhen(breakFast3, Arrays.asList(totalNoPerson3, foodConsumed3, sourceOfFood3, consumedAtPlaces3), Arrays.asList(YesNo.YES), true);
        FieldHelper.setVisibleWhen(lunchl3, Arrays.asList(totalNoPersonl3, foodConsumedl3, sourceOfFoodl3, consumedAtPlacesl3), Arrays.asList(YesNo.YES), true);
        FieldHelper.setVisibleWhen(suppers3, Arrays.asList(totalNoPersons3, foodConsumeds3, sourceOfFoods3, consumedAtPlacess3), Arrays.asList(YesNo.YES), true);

        initializeVisibilitiesAndAllowedVisibilities();
        initializeAccessAndAllowedAccesses();

        eventOtherSpecify.setVisible(false);
        eventType.addValueChangeListener(valueChangeEvent -> eventOtherSpecify.setVisible(eventType.getValue() == EventType.OTHER));
    }

    private Label createLabel(String text, String h4, String location) {
        final Label label = new Label(text);
        label.setId(text);
        label.addStyleName(h4);
        getContent().addComponent(label, location);
        return label;
    }

    private void addAffectedPersonFields() {
        affectedPersonField = addField(FoodHistoryDto.AFFECTED_PERSONS, AffectedPersonField.class);
        affectedPersonField.setWidthFull();
        affectedPersonField.setPseudonymized(isPseudonymized);
        affectedPersonField.setMaxAffectedPersons(maxAffectedPersons);

        affectedPersonField.addDataLoadedListener(() -> {
            int rowCount = affectedPersonField.getTableRowCount();
        });
    }

    public void validateNumberOfAffectedPersons() {
        if (affectedPersonField != null) {
            affectedPersonField.setMaxAffectedPersons(maxAffectedPersons);
        }
    }

    @Override
    protected String createHtmlLayout() {
        return HTML_LAYOUT;
    }


}
