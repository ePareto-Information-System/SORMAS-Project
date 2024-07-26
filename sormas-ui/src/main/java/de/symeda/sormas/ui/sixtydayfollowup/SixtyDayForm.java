/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.ui.sixtydayfollowup;

import com.vaadin.ui.Label;
import com.vaadin.v7.ui.*;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.sixtyday.SixtyDayDto;
import de.symeda.sormas.api.utils.PackagingType;
import de.symeda.sormas.api.utils.ParalysisSite;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.FieldHelper;
import de.symeda.sormas.ui.utils.NullableOptionGroup;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static de.symeda.sormas.ui.utils.CssStyles.H3;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;

public class SixtyDayForm extends AbstractEditForm<SixtyDayDto>{

    private static final long serialVersionUID = 1L;
    private static final String SIXTYDAY_HEADING_LOC = "hospitalizationHeadingLoc";
    private static final String OFFICIAL_HEADING_LOC = "officialHeadingLoc";
    private static final String PERSON_COMPLETING_HEADING_LOC = "personCompletingHeadingLoc";
    private static final String PROVIDE_HEADING_LOC = "provideHeadingLoc";

    private static final String HTML_LAYOUT =
            loc(SIXTYDAY_HEADING_LOC) +
                    fluidRowLocs(6,SixtyDayDto.PERSON_EXAMINE_CASE) +
                    fluidRowLocs(SixtyDayDto.DATE_OF_FOLLOWUP, SixtyDayDto.DATE_BIRTH) +
                    fluidRowLocs(6,SixtyDayDto.RESIDENTIAL_LOCATION) +
                    fluidRowLocs(SixtyDayDto.PATIENT_FOUND) +
                    fluidRowLocs(SixtyDayDto.PATIENT_FOUND_REASON) +
                    fluidRowLocs(SixtyDayDto.LOCATE_CHILD_ATTEMPT) +
                    fluidRowLocs(SixtyDayDto.PARALYSIS_WEAKNESS_PRESENT) +
                    fluidRowLocs(SixtyDayDto.PARALYSIS_WEAKNESS_PRESENT_SITE, SixtyDayDto.PARALYZED_PART_OTHER) +
                    fluidRowLocs(SixtyDayDto.PARALYSIS_WEAKNESS_FLOPPY) +
                    fluidRowLocs(SixtyDayDto.PARALYZED_PART, SixtyDayDto.OTHER_PART_BODY) +
                    fluidRowLocs(SixtyDayDto.DEEP_TENDON_REFLEX_SELECTION) +
                    fluidRowLocs(SixtyDayDto.MUSCLE_VOLUME_SELECTION) +
                    fluidRowLocs(SixtyDayDto.SENSORY_LOSS_SELECTION) +
                    fluidRowLocs(SixtyDayDto.PROVISIONAL_DIAGNOSIS) +
                    fluidRowLocs(SixtyDayDto.COMMENTS) +
                    fluidRowLocs(SixtyDayDto.CONTACT_DETAILS_NUMBER, SixtyDayDto.CONTACT_DETAILS_EMAIL) +
                    fluidRowLocs(SixtyDayDto.SIGNATURE, SixtyDayDto.DATE_SUBMISSION_FORMS)+

                    //FBI
                    fluidRowLocs(SixtyDayDto.FOOD_AVAILABLE_TESTING,SixtyDayDto.LAB_TEST_CONDUCTED) +
                    loc(PROVIDE_HEADING_LOC)+
                    fluidRowLocs(SixtyDayDto.SPECIFY_FOODS_SOURCES, SixtyDayDto.SPECIFY_SOURCES) +
                    fluidRowLocs(SixtyDayDto.PRODUCT_NAME, SixtyDayDto.BATCH_NUMBER) +
                    fluidRowLocs(SixtyDayDto.DATE_OF_MANUFACTURE, SixtyDayDto.EXPIRATION_DATE) +
                    fluidRowLocs(SixtyDayDto.PACKAGE_SIZE, SixtyDayDto.PACKAGING_TYPE, SixtyDayDto.PACKAGING_TYPE_OTHER) +
                    fluidRowLocs(SixtyDayDto.PLACE_OF_PURCHASE, SixtyDayDto.NAME_OF_MANUFACTURER) +
                    fluidRowLocs(SixtyDayDto.ADDRESS, SixtyDayDto.FOOD_TEL)+
                    loc(PERSON_COMPLETING_HEADING_LOC) +
            fluidRowLocs(SixtyDayDto.SURNAME, SixtyDayDto.FIRSTNAME, SixtyDayDto.MIDDLENAME) +
            fluidRowLocs(SixtyDayDto.TEL_NO, SixtyDayDto.DATE_OF_COMPLETION_OF_FORM, SixtyDayDto.NAME_OF_HEALTH_FACILITY);

    private final Disease disease;
    private final Class<? extends EntityDto> parentClass;
    private final boolean isPseudonymized;

    public SixtyDayForm(Disease disease, Class<? extends EntityDto> parentClass,
                        boolean isPseudonymized,
                        boolean inJurisdiction,boolean isEditAllowed) {
        super(
                SixtyDayDto.class,
                SixtyDayDto.I18N_PREFIX,
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

        Label sixtyDayHeadingLabel = new Label(I18nProperties.getString(Strings.headingSixtyDay));
        sixtyDayHeadingLabel.addStyleName(H3);
        getContent().addComponent(sixtyDayHeadingLabel, SIXTYDAY_HEADING_LOC);

        addField(SixtyDayDto.PERSON_EXAMINE_CASE, TextField.class);
        addField(SixtyDayDto.DATE_OF_FOLLOWUP, DateField.class);
        addField(SixtyDayDto.DATE_BIRTH, DateField.class);

        addField(SixtyDayDto.RESIDENTIAL_LOCATION, TextField.class);
        addField(SixtyDayDto.PATIENT_FOUND, NullableOptionGroup.class);
        addField(SixtyDayDto.PATIENT_FOUND_REASON, TextField.class);
        addField(SixtyDayDto.LOCATE_CHILD_ATTEMPT, TextField.class);

        OptionGroup paralysisWeaknessPresent = addField(SixtyDayDto.PARALYSIS_WEAKNESS_PRESENT, OptionGroup.class);

        OptionGroup paralysisWeaknessPresentSiteField = addField(SixtyDayDto.PARALYSIS_WEAKNESS_PRESENT_SITE, OptionGroup.class);
        CssStyles.style(paralysisWeaknessPresentSiteField, CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
        paralysisWeaknessPresentSiteField.setMultiSelect(true);

        paralysisWeaknessPresentSiteField.addItems(Arrays.stream(ParalysisSite.values())
                .filter(c -> fieldVisibilityCheckers.isVisible(ParalysisSite.class, c.name()))
                .collect(Collectors.toList()));

        TextField paralysisPartOther = addField(SixtyDayDto.PARALYZED_PART_OTHER, TextField.class);

        paralysisWeaknessPresentSiteField.setVisible(false);
        paralysisPartOther.setVisible(false);

        FieldHelper.setVisibleWhen(paralysisWeaknessPresent, Arrays.asList(paralysisWeaknessPresentSiteField), Arrays.asList(YesNo.YES), true);

        paralysisWeaknessPresentSiteField.addValueChangeListener( event ->{
            Set<ParalysisSite> selectedSites = (Set<ParalysisSite>) event.getProperty().getValue();
            paralysisPartOther.setVisible(selectedSites.contains(ParalysisSite.OTHER));
        });

        OptionGroup paralysisWeaknessFloppyField = addField(SixtyDayDto.PARALYSIS_WEAKNESS_FLOPPY, OptionGroup.class);

        NullableOptionGroup paralyzedPartField = addField(SixtyDayDto.PARALYZED_PART, NullableOptionGroup.class);
        NullableOptionGroup otherPartBodyField = addField(SixtyDayDto.OTHER_PART_BODY, NullableOptionGroup.class);
        NullableOptionGroup deepTendonReflexSelectionField = addField(SixtyDayDto.DEEP_TENDON_REFLEX_SELECTION, NullableOptionGroup.class);
        NullableOptionGroup muscleVolumeSelectionField = addField(SixtyDayDto.MUSCLE_VOLUME_SELECTION, NullableOptionGroup.class);
        NullableOptionGroup sensoryLossSelectionField = addField(SixtyDayDto.SENSORY_LOSS_SELECTION, NullableOptionGroup.class);

        TextArea provisionalDiagnosisField = addField(SixtyDayDto.PROVISIONAL_DIAGNOSIS, TextArea.class);
        provisionalDiagnosisField.setRows(4);
        TextArea commentsField = addField(SixtyDayDto.COMMENTS, TextArea.class);
        TextField contactDetailsNumberField = addField(SixtyDayDto.CONTACT_DETAILS_NUMBER, TextField.class);
        TextField contactDetailsEmailField = addField(SixtyDayDto.CONTACT_DETAILS_EMAIL, TextField.class);
        TextField signatureField = addField(SixtyDayDto.SIGNATURE, TextField.class);
        DateField dateSubmissionFormsField = addField(SixtyDayDto.DATE_SUBMISSION_FORMS, DateField.class);

        NullableOptionGroup foodAvailable = addField(SixtyDayDto.FOOD_AVAILABLE_TESTING, NullableOptionGroup.class);
        addField(SixtyDayDto.LAB_TEST_CONDUCTED, NullableOptionGroup.class);
        TextField food =  addField(SixtyDayDto.SPECIFY_FOODS_SOURCES, TextField.class);
        TextField sources =  addField(SixtyDayDto.SPECIFY_SOURCES, TextField.class);
        TextField productName = addField(SixtyDayDto.PRODUCT_NAME, TextField.class);
        TextField batchNumber = addField(SixtyDayDto.BATCH_NUMBER, TextField.class);
        DateField dateOfManufacture = addField(SixtyDayDto.DATE_OF_MANUFACTURE, DateField.class);
        DateField expirationDate = addField(SixtyDayDto.EXPIRATION_DATE, DateField.class);
        TextField packageSize = addField(SixtyDayDto.PACKAGE_SIZE, TextField.class);
        ComboBox packagingType = addField(SixtyDayDto.PACKAGING_TYPE, ComboBox.class);
        TextField packagingTypeOther = addField(SixtyDayDto.PACKAGING_TYPE_OTHER, TextField.class);
        TextField placeOfPurchase = addField(SixtyDayDto.PLACE_OF_PURCHASE, TextField.class);
        TextField nameOfManufacturer = addField(SixtyDayDto.NAME_OF_MANUFACTURER, TextField.class);
        TextField address = addField(SixtyDayDto.ADDRESS, TextField.class);
        TextField foodTel = addField(SixtyDayDto.FOOD_TEL, TextField.class);
        addField(SixtyDayDto.SURNAME, TextField.class);
        addField(SixtyDayDto.FIRSTNAME, TextField.class);
        addField(SixtyDayDto.MIDDLENAME, TextField.class);
        addField(SixtyDayDto.TEL_NO, TextField.class);
        addField(SixtyDayDto.DATE_OF_COMPLETION_OF_FORM, DateField.class);
        addField(SixtyDayDto.NAME_OF_HEALTH_FACILITY, TextField.class);

        setVisible(false,
                SixtyDayDto.FOOD_AVAILABLE_TESTING, SixtyDayDto.LAB_TEST_CONDUCTED, SixtyDayDto.SPECIFY_FOODS_SOURCES, SixtyDayDto.SPECIFY_SOURCES, SixtyDayDto.PRODUCT_NAME, SixtyDayDto.BATCH_NUMBER, SixtyDayDto.DATE_OF_MANUFACTURE, SixtyDayDto.EXPIRATION_DATE, SixtyDayDto.PACKAGE_SIZE, SixtyDayDto.PACKAGING_TYPE, SixtyDayDto.PACKAGING_TYPE_OTHER, SixtyDayDto.PLACE_OF_PURCHASE, SixtyDayDto.NAME_OF_MANUFACTURER, SixtyDayDto.ADDRESS, SixtyDayDto.FOOD_TEL, SixtyDayDto.SURNAME, SixtyDayDto.FIRSTNAME, SixtyDayDto.MIDDLENAME, SixtyDayDto.TEL_NO, SixtyDayDto.DATE_OF_COMPLETION_OF_FORM, SixtyDayDto.NAME_OF_HEALTH_FACILITY);


        initializeVisibilitiesAndAllowedVisibilities();
        initializeAccessAndAllowedAccesses();


        if(disease == Disease.FOODBORNE_ILLNESS){

            setVisible(false,
                    SixtyDayDto.PERSON_EXAMINE_CASE, SixtyDayDto.DATE_OF_FOLLOWUP, SixtyDayDto.DATE_BIRTH ,SixtyDayDto.RESIDENTIAL_LOCATION ,SixtyDayDto.PATIENT_FOUND,
                   SixtyDayDto.PATIENT_FOUND_REASON ,SixtyDayDto.LOCATE_CHILD_ATTEMPT ,SixtyDayDto.PARALYSIS_WEAKNESS_PRESENT ,SixtyDayDto.PARALYSIS_WEAKNESS_PRESENT_SITE, SixtyDayDto.PARALYZED_PART_OTHER,
            SixtyDayDto.PARALYSIS_WEAKNESS_FLOPPY ,SixtyDayDto.PARALYZED_PART ,SixtyDayDto.OTHER_PART_BODY ,SixtyDayDto.DEEP_TENDON_REFLEX_SELECTION ,SixtyDayDto.MUSCLE_VOLUME_SELECTION,
                    SixtyDayDto.SENSORY_LOSS_SELECTION , SixtyDayDto.PROVISIONAL_DIAGNOSIS ,SixtyDayDto.COMMENTS , SixtyDayDto.CONTACT_DETAILS_NUMBER, SixtyDayDto.CONTACT_DETAILS_EMAIL,
                    SixtyDayDto.SIGNATURE, SixtyDayDto.DATE_SUBMISSION_FORMS);
            sixtyDayHeadingLabel.setVisible(false);

            createLabel(I18nProperties.getString(Strings.headingProvide), H3, PROVIDE_HEADING_LOC);
            createLabel(I18nProperties.getString(Strings.headingofficialUse), H3, OFFICIAL_HEADING_LOC);
            createLabel(I18nProperties.getString(Strings.headingPersonCompleting), H3, PERSON_COMPLETING_HEADING_LOC);

            setVisible(true,
                    SixtyDayDto.FOOD_AVAILABLE_TESTING, SixtyDayDto.LAB_TEST_CONDUCTED, SixtyDayDto.SURNAME, SixtyDayDto.FIRSTNAME, SixtyDayDto.MIDDLENAME, SixtyDayDto.TEL_NO, SixtyDayDto.DATE_OF_COMPLETION_OF_FORM, SixtyDayDto.NAME_OF_HEALTH_FACILITY);
        }

        FieldHelper.setVisibleWhen(
                foodAvailable,
                Arrays.asList(food, sources, productName, batchNumber, dateOfManufacture, expirationDate, packageSize, packagingType, placeOfPurchase, nameOfManufacturer,
                        address, foodTel),Arrays.asList(YesNo.YES),true);
        packagingType.addValueChangeListener(event -> {
            boolean isOther = event.getProperty().getValue() == PackagingType.OTHER;
            packagingTypeOther.setVisible(isOther);
        });
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