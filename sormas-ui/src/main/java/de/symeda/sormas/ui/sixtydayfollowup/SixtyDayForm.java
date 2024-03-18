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
import de.symeda.sormas.api.sixtyday.SixtyDayDto;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.*;

import static de.symeda.sormas.ui.utils.CssStyles.H3;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;

public class SixtyDayForm extends AbstractEditForm<SixtyDayDto>{

    private static final long serialVersionUID = 1L;
    private static final String SIXTYDAY_HEADING_LOC = "hospitalizationHeadingLoc";

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
                    fluidRowLocs(SixtyDayDto.SIGNATURE, SixtyDayDto.DATE_SUBMISSION_FORMS);

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


        initializeVisibilitiesAndAllowedVisibilities();
        initializeAccessAndAllowedAccesses();


        personExamineCase.setCaption("Name of Person Examining Case");
        admissionDateField.setCaption("Date of Follow-up");
        dateOfBirthField.setCaption("Date of Birth");
        residentialLocationField.setCaption("Residential Location");
        patientFoundField.setCaption("Was the Patient Found?");
        patientFoundReasonField.setCaption("If no, why?");
        locateChildAttemptField.setCaption("Describe attempt to Locate Child");
        paralysisWeaknessPresentField.setCaption("Is Paralysis or Weakness Present (Yes/No)");
        paralysisWeaknessPresentSiteField.setCaption("If yes, site of paralysis");
        paralysisWeaknessResponse.setCaption("Paralyzed Other Part");
        paralysisWeaknessFloppyField.setCaption("Paralysis or Weakness Floppy?");
        paralyzedPartField.setCaption("Muscle Tone - In Paralyzed Part");
        otherPartBodyField.setCaption("Muscle Tone - Other Part of Body");
        deepTendonReflexSelectionField.setCaption("Deep Tendon Reflex");
        muscleVolumeSelectionField.setCaption("Muscle Volume");
        sensoryLossSelectionField.setCaption("Sensory Loss");
        provisionalDiagnosisField.setCaption("Provisional Diagnosis");
        commentsField.setCaption("Comments");
        contactDetailsNumberField.setCaption("Contact Details of Person - Phone Number");
        contactDetailsEmailField.setCaption("Contact Details of Person - Email Address");
        signatureField.setCaption("Signature");
        dateSubmissionFormsField.setCaption("Date of Submission of Forms");
    }


    @Override
    protected String createHtmlLayout() {
        return HTML_LAYOUT;
    }


}