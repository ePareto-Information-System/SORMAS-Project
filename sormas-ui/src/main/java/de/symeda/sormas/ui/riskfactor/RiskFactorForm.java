package de.symeda.sormas.ui.riskfactor;

import com.vaadin.ui.Component;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseOutcome;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.riskfactor.RiskFactorDto;
import de.symeda.sormas.api.sample.SampleMaterial;
import de.symeda.sormas.api.sample.SpecimenCondition;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.patientsymptomsprecedence.PatientSymptomsPrecedenceField;
import de.symeda.sormas.ui.utils.*;
import com.vaadin.ui.Label;

import java.util.Arrays;
import java.util.Set;

import static de.symeda.sormas.ui.utils.CssStyles.*;
import static de.symeda.sormas.ui.utils.LayoutUtil.*;

public class RiskFactorForm extends AbstractEditForm<RiskFactorDto> {

    private static final long serialVersionUID = 1L;
    private static final String RISK_FACTOR_HEADING_LOC = "riskFactorHeadingLoc";
    private static final String POTENTIAL_VIBRIO_VEHICLES_DRINKING_WATER = "potentialVibrioVehiclesDrinkingWaterLoc";
    private static final String POTENTIAL_VIBRIO_VEHICLES_NON_DRINKING_WATER = "potentialVibrioVehiclesNonDrinkingWaterLoc";
    private static final String POTENTIAL_VIBRIO_VEHICLES_FOOD_ITEMS = "potentialVibrioVehiclesFoodItemsLoc";
    private static final String POTENTIAL_VIBRIO_VEHICLES_BACTERIOLOGY_LAB_FINDINGS = "potentialVibrioVehiclesBacteriologyLabFindingsLoc";
    private static final String LOOKING_OUT_FOR_EXPOSURE_TO_IDENTIFIED_HAZARDS = "lookingOutForExposureToIdentifiedHazardsLoc";
    private static final String THREE_DAYS_PRIOR_TO_ONSET_OF_DISEASE = "threeDaysPriorToOnsetOfDiseaseLoc";
    private static final String THREE_DAYS_PRIOR_TO_ONSET_OF_DISEASE_DRINKING_WATER = "threeDaysPriorToOnsetOfDiseaseDrinkingWaterLoc";
    private static final String THREE_DAYS_PRIOR_TO_ONSET_OF_DISEASE_EAT = "headingCaseRiskFactorThreeDaysPriorToOnsetOfDiseaseEatLoc";
    private static final String THREE_DAYS_PRIOR_TO_ONSET_OF_DISEASE_ATTEND_ANY = "threeDaysPriorToOnsetOfDiseaseAttendAnyLoc";
    private static final String PATIENT_STATUS = "patientStatusLoc";
    private static final String LABORATORY = "laboratoryLoc";
    private static final String INVESTIGATING_OFFICER_INFO = "investigatingOfficerInfoLoc";



    private static final String HTML_LAYOUT =
            loc(RISK_FACTOR_HEADING_LOC) +
                    fluidRowLocs(6, RiskFactorDto.WATER_USED_FOR_DRINKING) +
                    loc(POTENTIAL_VIBRIO_VEHICLES_DRINKING_WATER) +
                    fluidRowLocs(RiskFactorDto.DRINKING_WATER_SOURCE_ONE,RiskFactorDto.DRINKING_WATER_SOURCE_TWO) +
                    fluidRowLocs(RiskFactorDto.DRINKING_WATER_SOURCE_THREE,RiskFactorDto.DRINKING_WATER_SOURCE_FOUR) +
                    loc(POTENTIAL_VIBRIO_VEHICLES_NON_DRINKING_WATER) +
                    fluidRowLocs(RiskFactorDto.NON_DRINKING_WATER_SOURCE_ONE, RiskFactorDto.NON_DRINKING_WATER_SOURCE_TWO) +
                    fluidRowLocs(RiskFactorDto.NON_DRINKING_WATER_SOURCE_THREE, RiskFactorDto.NON_DRINKING_WATER_SOURCE_FOUR) +
                    loc(POTENTIAL_VIBRIO_VEHICLES_FOOD_ITEMS) +
                    fluidRowLocs(RiskFactorDto.FOOD_ITEMS_ONE, RiskFactorDto.FOOD_ITEMS_TWO) +
                    fluidRowLocs(RiskFactorDto.FOOD_ITEMS_THREE, RiskFactorDto.FOOD_ITEMS_FOUR) +
                    fluidRowLocs(RiskFactorDto.FOOD_ITEMS_FIVE, RiskFactorDto.FOOD_ITEMS_SIX) +
                    fluidRowLocs(RiskFactorDto.FOOD_ITEMS_SEVEN, RiskFactorDto.FOOD_ITEMS_EIGHT) +
                    loc(POTENTIAL_VIBRIO_VEHICLES_BACTERIOLOGY_LAB_FINDINGS) +
                    fluidRowLocs(RiskFactorDto.DRINKING_WATER_INFECTED_BY_VIBRIO, RiskFactorDto.NON_DRINKING_WATER_INFECTED_BY_VIBRIO) +
                    fluidRowLocs(RiskFactorDto.FOOD_ITEMS_INFECTED_BY_VIBRIO) +

                    loc(THREE_DAYS_PRIOR_TO_ONSET_OF_DISEASE_DRINKING_WATER) +
                    fluidRowLocs(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_ONE, RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_TWO) +
                    fluidRowLocs(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_THREE, RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_FOUR) +
                    fluidRowLocs(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_FIVE) +

                    loc(THREE_DAYS_PRIOR_TO_ONSET_OF_DISEASE_EAT) +
                    fluidRowLocs(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_ONE, RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_TWO) +
                    fluidRowLocs(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_THREE, RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_FOUR) +
                    fluidRowLocs(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_FIVE) +

                    loc(THREE_DAYS_PRIOR_TO_ONSET_OF_DISEASE_ATTEND_ANY) +
                    fluidRowLocs(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_ATTEND_ANY_FUNERAL, RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_ATTEND_ANY_SOCIAL_EVENT) +
                    fluidRowLocs(RiskFactorDto.OTHER_SOCIAL_EVENT_DETAILS)+

                    //MONKEYPOX
                    fluidRowLocs(RiskFactorDto.PATIENT_SPOX_VACCINATION_SCAR_PRESENT) +
                    fluidRowLocs(RiskFactorDto.PATIENT_TRAVELLED_ANYWHERE_3WEEKS_PRIOR) +
                    fluidRowLocs(RiskFactorDto.PATIENT_TRAVELLED_3WEEKS_IF_YES_INDICATE) +
                    fluidRowLocs(RiskFactorDto.PATIENT_TRAVELLED_PERIOD_OF_ILLNESS) +
                    fluidRowLocs(RiskFactorDto.PATIENT_TRAVELLED_ILLNESS_IF_YES_INDICATE) +
                    fluidRowLocs(RiskFactorDto.OTHER_PLACES) +
                    fluidRowLocs(RiskFactorDto.DURING_3WEEKS_PATIENT_CONTACT_WITH_SIMILAR_SYMPTOMS) +
                    fluidRowLocs(RiskFactorDto.PATIENT_SYMPTOMS_PRECEDENCE) +
                    fluidRowLocs(6,RiskFactorDto.DATE_OF_CONTACT_WITH_ILL_PERSON) +
                    fluidRowLocs(RiskFactorDto.PATIENT_TOUCH_DOMESTIC_WILD_ANIMAL) +
                    fluidRowLocs(RiskFactorDto.PATIENT_TOUCH_DOMESTIC_WILD_ANIMAL_IF_YES) +
                    loc(LABORATORY) +
                    fluidRowLocs(6,RiskFactorDto.DATE_OF_SPECIMEN_COLLECTION) +
                    fluidRowLocs(6,RiskFactorDto.TYPE_OF_SPECIMEN_COLLECTION);

    private final Disease disease;
    private final Class<? extends EntityDto> parentClass;
    private final boolean isPseudonymized;

    public RiskFactorForm(Disease disease, Class<? extends EntityDto> parentClass,
                        boolean isPseudonymized,
                        boolean inJurisdiction, boolean isEditAllowed) {
        super(
                RiskFactorDto.class,
                RiskFactorDto.I18N_PREFIX,
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
    protected String createHtmlLayout() {
        return HTML_LAYOUT;
    }

    @Override
    protected void addFields() {

        if (parentClass == CaseDataDto.class) {
            addPatientSymptomsPrecedenceFields();
        }

        Label riskFactorHeadingLabel = new Label(I18nProperties.getString(Strings.headingRiskFactor));
        riskFactorHeadingLabel.addStyleName(H3);
        getContent().addComponent(riskFactorHeadingLabel, RISK_FACTOR_HEADING_LOC);

        Label potentialVibrioVehiclesDrinkingWater = new Label(I18nProperties.getString(Strings.headingCaseRiskFactorPotentialVibrioVehiclesDrinkingWater));
        potentialVibrioVehiclesDrinkingWater.addStyleName(H3);
        getContent().addComponent(potentialVibrioVehiclesDrinkingWater, POTENTIAL_VIBRIO_VEHICLES_DRINKING_WATER);

        Label potentialVibrioVehiclesNonDrinkingWater = new Label(I18nProperties.getString(Strings.headingCaseRiskFactorPotentialVibrioVehiclesNonDrinkingWater));
        potentialVibrioVehiclesNonDrinkingWater.addStyleName(H3);
        getContent().addComponent(potentialVibrioVehiclesNonDrinkingWater, POTENTIAL_VIBRIO_VEHICLES_NON_DRINKING_WATER);

        Label potentialVibrioVehiclesFoodItems = new Label(I18nProperties.getString(Strings.headingCaseRiskFactorPotentialVibrioVehiclesFoodItems));
        potentialVibrioVehiclesFoodItems.addStyleName(H3);
        getContent().addComponent(potentialVibrioVehiclesFoodItems, POTENTIAL_VIBRIO_VEHICLES_FOOD_ITEMS);

        Label potentialVibrioVehiclesBacteriologyLabFindings = new Label(I18nProperties.getString(Strings.headingCaseRiskFactorPotentialVibrioVehiclesBacteriologyLabFindings));
        potentialVibrioVehiclesBacteriologyLabFindings.addStyleName(H3);
        getContent().addComponent(potentialVibrioVehiclesBacteriologyLabFindings, POTENTIAL_VIBRIO_VEHICLES_BACTERIOLOGY_LAB_FINDINGS);

        Label lookingOutForExposureToIdentifiedHazards = new Label(I18nProperties.getString(Strings.headingCaseRiskFactorLookingOutForExposureToIdentifiedHazards));
        lookingOutForExposureToIdentifiedHazards.addStyleName(H3);
        getContent().addComponent(lookingOutForExposureToIdentifiedHazards, LOOKING_OUT_FOR_EXPOSURE_TO_IDENTIFIED_HAZARDS);

        Label threeDaysPriorToOnsetOfDisease = new Label(I18nProperties.getString(Strings.headingCaseRiskFactorThreeDaysPriorToOnsetOfDisease));
        threeDaysPriorToOnsetOfDisease.addStyleName(H3);
        getContent().addComponent(threeDaysPriorToOnsetOfDisease, THREE_DAYS_PRIOR_TO_ONSET_OF_DISEASE);

        Label threeDaysPriorToOnsetOfDiseaseDrinkingWater = new Label(I18nProperties.getString(Strings.headingCaseRiskFactorThreeDaysPriorToOnsetOfDiseaseDrinkingWater));
        threeDaysPriorToOnsetOfDiseaseDrinkingWater.addStyleName(H3);
        getContent().addComponent(threeDaysPriorToOnsetOfDiseaseDrinkingWater, THREE_DAYS_PRIOR_TO_ONSET_OF_DISEASE_DRINKING_WATER);

        Label threeDaysPriorToOnsetOfDiseaseEat = new Label(I18nProperties.getString(Strings.headingCaseRiskFactorThreeDaysPriorToOnsetOfDiseaseEat));
        threeDaysPriorToOnsetOfDiseaseEat.addStyleName(H3);
        getContent().addComponent(threeDaysPriorToOnsetOfDiseaseEat, THREE_DAYS_PRIOR_TO_ONSET_OF_DISEASE_EAT);


        Label threeDaysPriorToOnsetOfDiseaseAttendAny = new Label(I18nProperties.getString(Strings.headingCaseRiskFactorThreeDaysPriorToOnsetOfDiseaseAttendAny));
        threeDaysPriorToOnsetOfDiseaseAttendAny.addStyleName(H3);
        getContent().addComponent(threeDaysPriorToOnsetOfDiseaseAttendAny, THREE_DAYS_PRIOR_TO_ONSET_OF_DISEASE_ATTEND_ANY);

        addFields(
                RiskFactorDto.DRINKING_WATER_SOURCE_ONE,
                RiskFactorDto.DRINKING_WATER_SOURCE_TWO,
                RiskFactorDto.DRINKING_WATER_SOURCE_THREE,
                RiskFactorDto.DRINKING_WATER_SOURCE_FOUR,
                RiskFactorDto.NON_DRINKING_WATER_SOURCE_ONE,
                RiskFactorDto.NON_DRINKING_WATER_SOURCE_TWO,
                RiskFactorDto.NON_DRINKING_WATER_SOURCE_THREE,
                RiskFactorDto.NON_DRINKING_WATER_SOURCE_FOUR,
                RiskFactorDto.FOOD_ITEMS_ONE,
                RiskFactorDto.FOOD_ITEMS_TWO,
                RiskFactorDto.FOOD_ITEMS_THREE,
                RiskFactorDto.FOOD_ITEMS_FOUR,
                RiskFactorDto.FOOD_ITEMS_FIVE,
                RiskFactorDto.FOOD_ITEMS_SIX,
                RiskFactorDto.FOOD_ITEMS_SEVEN,
                RiskFactorDto.FOOD_ITEMS_EIGHT,
                RiskFactorDto.OTHER_SOCIAL_EVENT_DETAILS
        );

        addField(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_ONE, NullableOptionGroup.class);
        addField(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_TWO, NullableOptionGroup.class);
        addField(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_THREE, NullableOptionGroup.class);
        addField(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_FOUR, NullableOptionGroup.class);
        addField(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_FIVE, NullableOptionGroup.class);

        addField(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_ONE, NullableOptionGroup.class);
        addField(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_TWO, NullableOptionGroup.class);
        addField(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_THREE, NullableOptionGroup.class);
        addField(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_FOUR, NullableOptionGroup.class);
        addField(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_FIVE, NullableOptionGroup.class);

        addField(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_ATTEND_ANY_FUNERAL, NullableOptionGroup.class);
        addField(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_ATTEND_ANY_SOCIAL_EVENT, NullableOptionGroup.class);

        FieldHelper.setVisibleWhen(
                getFieldGroup(),
                RiskFactorDto.OTHER_SOCIAL_EVENT_DETAILS,
                RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_ATTEND_ANY_SOCIAL_EVENT,
                YesNo.YES,
                true
        );

        addField(RiskFactorDto.DRINKING_WATER_INFECTED_BY_VIBRIO, NullableOptionGroup.class);
        addField(RiskFactorDto.NON_DRINKING_WATER_INFECTED_BY_VIBRIO, NullableOptionGroup.class);
        addField(RiskFactorDto.FOOD_ITEMS_INFECTED_BY_VIBRIO, NullableOptionGroup.class);
        addField(RiskFactorDto.WATER_USED_FOR_DRINKING, ComboBox.class);

        hideFieldsForSelectedDisease(disease);

        if(disease == Disease.MONKEYPOX){

            setComponentsVisibilityFalse(
                 potentialVibrioVehiclesDrinkingWater,
                 potentialVibrioVehiclesNonDrinkingWater,
                 potentialVibrioVehiclesFoodItems,
                 potentialVibrioVehiclesBacteriologyLabFindings,
                 lookingOutForExposureToIdentifiedHazards,
                 threeDaysPriorToOnsetOfDisease,
                 threeDaysPriorToOnsetOfDiseaseDrinkingWater,
                 threeDaysPriorToOnsetOfDiseaseEat,
                 threeDaysPriorToOnsetOfDiseaseAttendAny
            );

            addField(RiskFactorDto.PATIENT_SPOX_VACCINATION_SCAR_PRESENT, NullableOptionGroup.class);
            NullableOptionGroup travelled3weeks = addField(RiskFactorDto.PATIENT_TRAVELLED_ANYWHERE_3WEEKS_PRIOR, NullableOptionGroup.class);
            TextArea indicatePlaces = addField(RiskFactorDto.PATIENT_TRAVELLED_3WEEKS_IF_YES_INDICATE, TextArea.class);
            indicatePlaces.setRows(3);
            NullableOptionGroup travelledIllness = addField(RiskFactorDto.PATIENT_TRAVELLED_PERIOD_OF_ILLNESS, NullableOptionGroup.class);
            TextArea indicateTravel = addField(RiskFactorDto.PATIENT_TRAVELLED_ILLNESS_IF_YES_INDICATE, TextArea.class);
            indicateTravel.setRows(3);
            TextArea otherPlaces = addField(RiskFactorDto.OTHER_PLACES, TextArea.class);
            otherPlaces.setRows(3);
            NullableOptionGroup weeksSymptoms = addField(RiskFactorDto.DURING_3WEEKS_PATIENT_CONTACT_WITH_SIMILAR_SYMPTOMS, NullableOptionGroup.class);
            addField(RiskFactorDto.DATE_OF_CONTACT_WITH_ILL_PERSON, DateField.class);
            NullableOptionGroup patientTouch = addField(RiskFactorDto.PATIENT_TOUCH_DOMESTIC_WILD_ANIMAL, NullableOptionGroup.class);
            TextArea patientTouchYes = addField(RiskFactorDto.PATIENT_TOUCH_DOMESTIC_WILD_ANIMAL_IF_YES, TextArea.class);

            FieldHelper.setVisibleWhen(travelled3weeks, Arrays.asList(indicatePlaces), Arrays.asList(YesNo.YES), true);
            FieldHelper.setVisibleWhen(travelledIllness, Arrays.asList(indicateTravel), Arrays.asList(YesNo.YES), true);
            FieldHelper.setVisibleWhen(patientTouch, Arrays.asList(patientTouchYes), Arrays.asList(YesNo.YES), true);

        }

    }

    private void addPatientSymptomsPrecedenceFields() {
        PatientSymptomsPrecedenceField patientSymptomsPrecedenceField = addField(RiskFactorDto.PATIENT_SYMPTOMS_PRECEDENCE, PatientSymptomsPrecedenceField.class);
        patientSymptomsPrecedenceField.setWidthFull();
        patientSymptomsPrecedenceField.setPseudonymized(isPseudonymized);
    }

    private void setComponentsVisibilityFalse(Component... components) {
        for (Component component : components) {
            component.setVisible(false);
        }
    }

    public void hideFieldsForSelectedDisease(Disease disease) {
        Set<String> disabledFields = RiskFactorFormConfiguration.getDisabledFieldsForDisease(disease);
        for (String field : disabledFields) {
            disableField(field);
        }
    }

    private void disableField(String field) {
        setVisible(false, field);
    }

    private Label createLabel(String text, String h4, String location) {
        final Label label = new Label(text);
        label.setId(text);
        label.addStyleName(h4);
        getContent().addComponent(label, location);
        return label;
    }

}
