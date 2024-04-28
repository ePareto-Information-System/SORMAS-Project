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
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.sixtyday.SixtyDayDto;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.*;

import java.util.Arrays;

import static de.symeda.sormas.ui.utils.CssStyles.H2;
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
                    fluidRowLocs(SixtyDayDto.PERSON_EXAMINE_CASE) +
                    fluidRowLocs(SixtyDayDto.DATE_OF_FOLLOWUP, SixtyDayDto.DATE_BIRTH) +
                    fluidRowLocs(SixtyDayDto.RESIDENTIAL_LOCATION) +
                    fluidRowLocs(SixtyDayDto.PATIENT_FOUND) +
                    fluidRowLocs(SixtyDayDto.PATIENT_FOUND_REASON) +
                    fluidRowLocs(SixtyDayDto.LOCATE_CHILD_ATTEMPT) +
                    fluidRowLocs(SixtyDayDto.PARALYSIS_WEAKNESS_PRESENT) +
                    fluidRowLocs(SixtyDayDto.PARALYSIS_WEAKNESS_PRESENT_SITE, SixtyDayDto.PARALYZED_PART_OTHER) +
                    fluidRowLocs(SixtyDayDto.PARALYSIS_WEAKNESS_FLOPPY) +
                    fluidRowLocs(SixtyDayDto.PARALYZED_PART) +
                    fluidRowLocs(SixtyDayDto.OTHER_PART_BODY) +
                    fluidRowLocs(SixtyDayDto.DEEP_TENDON_REFLEX_SELECTION) +
                    fluidRowLocs(SixtyDayDto.MUSCLE_VOLUME_SELECTION) +
                    fluidRowLocs(SixtyDayDto.SENSORY_LOSS_SELECTION) +
                    fluidRowLocs(SixtyDayDto.PROVISIONAL_DIAGNOSIS) +
                    fluidRowLocs(SixtyDayDto.COMMENTS) +
                    fluidRowLocs(SixtyDayDto.CONTACT_DETAILS_NUMBER, SixtyDayDto.CONTACT_DETAILS_EMAIL) +
                    fluidRowLocs(SixtyDayDto.SIGNATURE, SixtyDayDto.DATE_SUBMISSION_FORMS)+

                    //FBI
                    loc(PROVIDE_HEADING_LOC)+
                    fluidRowLocs(SixtyDayDto.FOOD_AVAILABLE_TESTING, SixtyDayDto.SPECIFY_FOODS_SOURCES, SixtyDayDto.LAB_TEST_CONDUCTED) +
                    fluidRowLocs(SixtyDayDto.PRODUCT_NAME, SixtyDayDto.BATCH_NUMBER) +
                    fluidRowLocs(SixtyDayDto.DATE_OF_MANUFACTURE, SixtyDayDto.EXPIRATION_DATE) +
                    fluidRowLocs(SixtyDayDto.PACKAGE_SIZE, SixtyDayDto.PACKAGING_TYPE, SixtyDayDto.PACKAGING_TYPE_OTHER) +
                    fluidRowLocs(SixtyDayDto.PLACE_OF_PURCHASE, SixtyDayDto.NAME_OF_MANUFACTURER) +
                    fluidRowLocs(SixtyDayDto.ADDRESS, SixtyDayDto.FOOD_TEL)+
                    loc(OFFICIAL_HEADING_LOC) +
            fluidRowLocs(SixtyDayDto.INVESTIGATION_NOTES) +
            fluidRowLocs(SixtyDayDto.SUSPECTED_DIAGNOSIS, SixtyDayDto.CONFIRMED_DIAGNOSIS) +
            fluidRowLocs(SixtyDayDto.INVESTIGATED_BY, SixtyDayDto.INVESTIGATOR_SIGNATURE, SixtyDayDto.INVESTIGATOR_DATE) +
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

        TextField personExamineCase = addField(SixtyDayDto.PERSON_EXAMINE_CASE, TextField.class);
        DateField admissionDateField = addField(SixtyDayDto.DATE_OF_FOLLOWUP, DateField.class);
        DateField dateOfBirthField = addField(SixtyDayDto.DATE_BIRTH, DateField.class);

        TextField residentialLocationField = addField(SixtyDayDto.RESIDENTIAL_LOCATION, TextField.class);
        NullableOptionGroup patientFoundField = addField(SixtyDayDto.PATIENT_FOUND, NullableOptionGroup.class);
        TextField patientFoundReasonField = addField(SixtyDayDto.PATIENT_FOUND_REASON, TextField.class);
        TextField locateChildAttemptField = addField(SixtyDayDto.LOCATE_CHILD_ATTEMPT, TextField.class);

        OptionGroup paralysisWeaknessPresentField = addField(SixtyDayDto.PARALYSIS_WEAKNESS_PRESENT, OptionGroup.class);
        ComboBox paralysisWeaknessPresentSiteField = addField(SixtyDayDto.PARALYSIS_WEAKNESS_PRESENT_SITE, ComboBox.class);
        TextField paralysisWeaknessResponse = addField(SixtyDayDto.PARALYZED_PART_OTHER, TextField.class);
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
        TextField foodSources =  addField(SixtyDayDto.SPECIFY_FOODS_SOURCES, TextField.class);
        addField(SixtyDayDto.PRODUCT_NAME, TextField.class);
        addField(SixtyDayDto.BATCH_NUMBER, TextField.class);
        addField(SixtyDayDto.DATE_OF_MANUFACTURE, DateField.class);
        addField(SixtyDayDto.EXPIRATION_DATE, DateField.class);
        addField(SixtyDayDto.PACKAGE_SIZE, TextField.class);
        addField(SixtyDayDto.PACKAGING_TYPE, ComboBox.class);
        addField(SixtyDayDto.PACKAGING_TYPE_OTHER, TextField.class);
        addField(SixtyDayDto.PLACE_OF_PURCHASE, TextField.class);
        addField(SixtyDayDto.NAME_OF_MANUFACTURER, TextField.class);
        addField(SixtyDayDto.ADDRESS, TextField.class);
        addField(SixtyDayDto.FOOD_TEL, TextField.class);
        addField(SixtyDayDto.INVESTIGATION_NOTES, TextArea.class);
        addField(SixtyDayDto.SUSPECTED_DIAGNOSIS, TextField.class);
        addField(SixtyDayDto.CONFIRMED_DIAGNOSIS, TextField.class);
        addField(SixtyDayDto.INVESTIGATED_BY, TextField.class);
        addField(SixtyDayDto.INVESTIGATOR_SIGNATURE, TextField.class);
        addField(SixtyDayDto.INVESTIGATOR_DATE, DateField.class);
        addField(SixtyDayDto.SURNAME, TextField.class);
        addField(SixtyDayDto.FIRSTNAME, TextField.class);
        addField(SixtyDayDto.MIDDLENAME, TextField.class);
        addField(SixtyDayDto.TEL_NO, TextField.class);
        addField(SixtyDayDto.DATE_OF_COMPLETION_OF_FORM, DateField.class);
        addField(SixtyDayDto.NAME_OF_HEALTH_FACILITY, TextField.class);

        setVisible(false,
                SixtyDayDto.FOOD_AVAILABLE_TESTING, SixtyDayDto.LAB_TEST_CONDUCTED, SixtyDayDto.SPECIFY_FOODS_SOURCES, SixtyDayDto.PRODUCT_NAME, SixtyDayDto.BATCH_NUMBER, SixtyDayDto.DATE_OF_MANUFACTURE, SixtyDayDto.EXPIRATION_DATE, SixtyDayDto.PACKAGE_SIZE, SixtyDayDto.PACKAGING_TYPE, SixtyDayDto.PACKAGING_TYPE_OTHER, SixtyDayDto.PLACE_OF_PURCHASE, SixtyDayDto.NAME_OF_MANUFACTURER,
                SixtyDayDto.ADDRESS, SixtyDayDto.FOOD_TEL, SixtyDayDto.INVESTIGATION_NOTES, SixtyDayDto.SUSPECTED_DIAGNOSIS, SixtyDayDto.CONFIRMED_DIAGNOSIS, SixtyDayDto.INVESTIGATED_BY,
                SixtyDayDto.INVESTIGATOR_SIGNATURE, SixtyDayDto.INVESTIGATOR_DATE, SixtyDayDto.SURNAME, SixtyDayDto.FIRSTNAME, SixtyDayDto.MIDDLENAME, SixtyDayDto.TEL_NO, SixtyDayDto.DATE_OF_COMPLETION_OF_FORM, SixtyDayDto.NAME_OF_HEALTH_FACILITY);


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
                    SixtyDayDto.FOOD_AVAILABLE_TESTING, SixtyDayDto.LAB_TEST_CONDUCTED, SixtyDayDto.SPECIFY_FOODS_SOURCES, SixtyDayDto.PRODUCT_NAME, SixtyDayDto.BATCH_NUMBER, SixtyDayDto.DATE_OF_MANUFACTURE, SixtyDayDto.EXPIRATION_DATE, SixtyDayDto.PACKAGE_SIZE, SixtyDayDto.PACKAGING_TYPE, SixtyDayDto.PACKAGING_TYPE_OTHER, SixtyDayDto.PLACE_OF_PURCHASE, SixtyDayDto.NAME_OF_MANUFACTURER,
                    SixtyDayDto.ADDRESS, SixtyDayDto.FOOD_TEL, SixtyDayDto.INVESTIGATION_NOTES, SixtyDayDto.SUSPECTED_DIAGNOSIS, SixtyDayDto.CONFIRMED_DIAGNOSIS, SixtyDayDto.INVESTIGATED_BY,
                    SixtyDayDto.INVESTIGATOR_SIGNATURE, SixtyDayDto.INVESTIGATOR_DATE, SixtyDayDto.SURNAME, SixtyDayDto.FIRSTNAME, SixtyDayDto.MIDDLENAME, SixtyDayDto.TEL_NO, SixtyDayDto.DATE_OF_COMPLETION_OF_FORM, SixtyDayDto.NAME_OF_HEALTH_FACILITY);
        }

        FieldHelper.setVisibleWhen(foodAvailable, Arrays.asList(foodSources), Arrays.asList(YesNo.YES), true);
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