/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2022 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.ui.hospitalization;

import static de.symeda.sormas.ui.utils.CssStyles.H3;
import static de.symeda.sormas.ui.utils.CssStyles.VSPACE_TOP_3;
import static de.symeda.sormas.ui.utils.LayoutUtil.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import com.vaadin.v7.ui.*;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.infrastructure.facility.DhimsFacility;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.utils.AFPFacilityOptions;
import de.symeda.sormas.api.utils.YesNo;

import com.vaadin.server.ErrorMessage;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.ErrorLevel;
import com.vaadin.ui.Label;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.hospitalization.HospitalizationDto;
import de.symeda.sormas.api.hospitalization.HospitalizationReasonType;
import de.symeda.sormas.api.hospitalization.PreviousHospitalizationDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.infrastructure.facility.FacilityDto;
import de.symeda.sormas.api.infrastructure.facility.FacilityReferenceDto;
import de.symeda.sormas.api.infrastructure.facility.FacilityType;
import de.symeda.sormas.api.symptoms.SymptomsDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.DateComparator;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.DateComparisonValidator;
import de.symeda.sormas.ui.utils.FieldHelper;
import de.symeda.sormas.ui.utils.NullableOptionGroup;
import de.symeda.sormas.ui.utils.OutbreakFieldVisibilityChecker;
import de.symeda.sormas.ui.utils.ViewMode;

public class HospitalizationForm extends AbstractEditForm<HospitalizationDto> {

	private static final long serialVersionUID = 1L;

	private static final String HOSPITALIZATION_HEADING_LOC = "hospitalizationHeadingLoc";
	private static final String PREVIOUS_HOSPITALIZATIONS_HEADING_LOC = "previousHospitalizationsHeadingLoc";
	private static final String FILL_SECTION_HEADING_LOC = "fillSectionHeadingLoc";
	private static final String SEEK_HELP_HEADING_LOC = "seekHelpHeadingLoc";
	private static final String HEALTH_FACILITY = Captions.CaseHospitalization_healthFacility;
	private static final String HOSPITAL_NAME_DETAIL = " ( %s )";
	//@formatter:off
	private static final String HTML_LAYOUT =
			loc(HOSPITALIZATION_HEADING_LOC) +
			fluidRowLocs(HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY, HospitalizationDto.SEEN_AT_A_HEALTH_FACILITY, HospitalizationDto.WAS_PATIENT_ADMITTED) +
			fluidRowLocs(HEALTH_FACILITY) +
			fluidRowLocs(HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY_NEW) +
					fluidRowLocs(4,HospitalizationDto.DATE_FIRST_SEEN_HOSPITAL_FOR_DISEASE, 4,HospitalizationDto.TERMINATION_DATE_HOSPITAL_STAY) +
			fluidRowLocs(HospitalizationDto.ADMISSION_DATE, HospitalizationDto.DISCHARGE_DATE, HospitalizationDto.LEFT_AGAINST_ADVICE, "") +
			fluidRowLocs(6,HospitalizationDto.NOTIFY_DISTRICT_DATE) +
			fluidRowLocs(HospitalizationDto.HOSPITALIZATION_REASON, HospitalizationDto.OTHER_HOSPITALIZATION_REASON) +
					fluidRowLocs(3, HospitalizationDto.INTENSIVE_CARE_UNIT, 3,
							HospitalizationDto.INTENSIVE_CARE_UNIT_START,
							3,
							HospitalizationDto.INTENSIVE_CARE_UNIT_END)
					+ fluidRowLocs(HospitalizationDto.ISOLATED, HospitalizationDto.ISOLATION_DATE, "")
					+ fluidRowLocs(HospitalizationDto.DESCRIPTION) +
			loc(PREVIOUS_HOSPITALIZATIONS_HEADING_LOC) +
			fluidRowLocs(HospitalizationDto.HOSPITALIZED_PREVIOUSLY) +
			fluidRowLocs(HospitalizationDto.PREVIOUS_HOSPITALIZATIONS) +
			fluidRowLocs(6, HospitalizationDto.DISEASE_ONSET_DATE) +
			fluidRowLocs(HospitalizationDto.PATIENT_HOSPITALIZED_DETAINED) +

					//AFP
					loc(FILL_SECTION_HEADING_LOC) +
					loc(SEEK_HELP_HEADING_LOC) +
					fluidRowLocs(HospitalizationDto.PLACE, HospitalizationDto.DURATION_MONTHS, HospitalizationDto.DURATION_DAYS) +
					fluidRowLocs(HospitalizationDto.PLACE2, HospitalizationDto.DURATION_MONTHS2, HospitalizationDto.DURATION_DAYS2) +
					locCss(VSPACE_TOP_3, HospitalizationDto.INVESTIGATOR_NAME) +
					fluidRowLocs(HospitalizationDto.INVESTIGATOR_TITLE, HospitalizationDto.INVESTIGATOR_UNIT) +
					fluidRowLocs(HospitalizationDto.INVESTIGATOR_ADDRESS, HospitalizationDto.INVESTIGATOR_TEL);

	public static final String MEASLES_LAYOUT = loc(HOSPITALIZATION_HEADING_LOC) +
			fluidRowLocs(HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY, HEALTH_FACILITY) +
			fluidRowLocs(4, HospitalizationDto.ADMISSION_DATE, 4, HospitalizationDto.DISCHARGE_DATE);

	private final CaseDataDto caze;
	private final ViewMode viewMode;
	private NullableOptionGroup intensiveCareUnit;
	private DateField intensiveCareUnitStart;
	private DateField intensiveCareUnitEnd;
	//@formatter:on

	public HospitalizationForm(CaseDataDto caze, ViewMode viewMode, boolean isPseudonymized, boolean inJurisdiction, boolean isEditAllowed) {

		super(
			HospitalizationDto.class,
			HospitalizationDto.I18N_PREFIX,
			false,
			FieldVisibilityCheckers.withCountry(FacadeProvider.getConfigFacade().getCountryLocale())
				.add(new OutbreakFieldVisibilityChecker(viewMode)),
			UiFieldAccessCheckers.forDataAccessLevel(UserProvider.getCurrent().getPseudonymizableDataAccessLevel(inJurisdiction), isPseudonymized), isEditAllowed);
		this.caze = caze;
		this.viewMode = viewMode;
		addFields();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void addFields() {

		if (caze == null || viewMode == null) {
			return;
		}

		Label hospitalizationHeadingLabel = new Label(I18nProperties.getString(Strings.headingHospitalization));
		hospitalizationHeadingLabel.addStyleName(H3);
		getContent().addComponent(hospitalizationHeadingLabel, HOSPITALIZATION_HEADING_LOC);

		Label previousHospitalizationsHeadingLabel = new Label(I18nProperties.getString(Strings.headingPreviousHospitalizations));
		previousHospitalizationsHeadingLabel.addStyleName(H3);
		getContent().addComponent(previousHospitalizationsHeadingLabel, PREVIOUS_HOSPITALIZATIONS_HEADING_LOC);

		TextField facilityField = addCustomField(HEALTH_FACILITY, FacilityReferenceDto.class, TextField.class);
//		FacilityReferenceDto healthFacility = caze.getHealthFacility();

		String healthFacility = caze.getHospitalName();
		DhimsFacility facilityType = caze.getDhimsFacilityType();
		AFPFacilityOptions afpFacilityOptions = caze.getAfpFacilityOptions();
		final boolean noneFacility = healthFacility == null || healthFacility.equalsIgnoreCase(FacilityDto.NONE_FACILITY_UUID);

		if (facilityType != null && facilityType != DhimsFacility.HOSPITAL){
			facilityField.setValue(String.valueOf(facilityType));
		}
		else if(afpFacilityOptions != null && afpFacilityOptions != AFPFacilityOptions.Hospital){
			facilityField.setValue(String.valueOf(afpFacilityOptions));
		}
		else if(healthFacility != null ){
			facilityField.setValue(healthFacility);
		}
		else {
			System.out.println("Is facility type HOSPITAL: " + DhimsFacility.HOSPITAL.equals(caze.getDhimsFacilityType()));
			facilityField.setValue(noneFacility || !DhimsFacility.HOSPITAL.equals(caze.getDhimsFacilityType()) ? null : healthFacility);
		}
		facilityField.setReadOnly(true);

		final NullableOptionGroup admittedToHealthFacilityField = addField(HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY, NullableOptionGroup.class);
		admittedToHealthFacilityField.setVisible(false);

		final NullableOptionGroup seenAtAHealthFacility = addField(HospitalizationDto.SEEN_AT_A_HEALTH_FACILITY, NullableOptionGroup.class);
		seenAtAHealthFacility.setVisible(false);

		final NullableOptionGroup wasPatientAdmitted = addField(HospitalizationDto.WAS_PATIENT_ADMITTED, NullableOptionGroup.class);
		wasPatientAdmitted.setVisible(false);

		final NullableOptionGroup admittedToHealthFacilityFieldNew = addField(HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY_NEW, NullableOptionGroup.class);
		admittedToHealthFacilityFieldNew.setVisible(false);
		admittedToHealthFacilityFieldNew.setCaption("Was the Patient Admitted at the Facility (in-patient)?");
		final DateField admissionDateField = addField(HospitalizationDto.ADMISSION_DATE, DateField.class);
		final DateField dischargeDateField = addDateField(HospitalizationDto.DISCHARGE_DATE, DateField.class, 7);
		intensiveCareUnit = addField(HospitalizationDto.INTENSIVE_CARE_UNIT, NullableOptionGroup.class);
		intensiveCareUnitStart = addField(HospitalizationDto.INTENSIVE_CARE_UNIT_START, DateField.class);
		DateField notifyDistrictDate = addField(HospitalizationDto.NOTIFY_DISTRICT_DATE, DateField.class);
		DateField dateFirstSeen = addField(HospitalizationDto.DATE_FIRST_SEEN_HOSPITAL_FOR_DISEASE, DateField.class);
		DateField terminationDateHospitalStay = addField(HospitalizationDto.TERMINATION_DATE_HOSPITAL_STAY, DateField.class);
		dateFirstSeen.setVisible(false);
		notifyDistrictDate.setVisible(false);
		intensiveCareUnitStart.setVisible(false);
		terminationDateHospitalStay.setVisible(false);
		intensiveCareUnitEnd = addField(HospitalizationDto.INTENSIVE_CARE_UNIT_END, DateField.class);
		intensiveCareUnitEnd.setVisible(false);
		FieldHelper
			.setVisibleWhen(intensiveCareUnit, Arrays.asList(intensiveCareUnitStart, intensiveCareUnitEnd), Arrays.asList(YesNo.YES), true);
		final Field isolationDateField = addField(HospitalizationDto.ISOLATION_DATE);
		final TextArea descriptionField = addField(HospitalizationDto.DESCRIPTION, TextArea.class);
		descriptionField.setRows(4);
		final NullableOptionGroup isolatedField = addField(HospitalizationDto.ISOLATED, NullableOptionGroup.class);
		final NullableOptionGroup leftAgainstAdviceField = addField(HospitalizationDto.LEFT_AGAINST_ADVICE, NullableOptionGroup.class);

		ComboBox hospitalizationReason = addField(HospitalizationDto.HOSPITALIZATION_REASON);
		TextField otherHospitalizationReason = addField(HospitalizationDto.OTHER_HOSPITALIZATION_REASON, TextField.class);
		NullableOptionGroup hospitalizedPreviouslyField = addField(HospitalizationDto.HOSPITALIZED_PREVIOUSLY, NullableOptionGroup.class);
		CssStyles.style(hospitalizedPreviouslyField, CssStyles.ERROR_COLOR_PRIMARY);
		PreviousHospitalizationsField previousHospitalizationsField =
			addField(HospitalizationDto.PREVIOUS_HOSPITALIZATIONS, PreviousHospitalizationsField.class);

		if (caze.getDisease() == Disease.MEASLES) {
			setVisible(true, HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY, HospitalizationDto.NOTIFY_DISTRICT_DATE, HospitalizationDto.SEEN_AT_A_HEALTH_FACILITY, HospitalizationDto.DATE_FIRST_SEEN_HOSPITAL_FOR_DISEASE);
		}

		if(caze.getDisease() == Disease.AFP){

			Label fillThisSectionHeadingLabel = new Label(I18nProperties.getString(Strings.headingFillThis));
			fillThisSectionHeadingLabel.addStyleName(H3);
			getContent().addComponent(fillThisSectionHeadingLabel, FILL_SECTION_HEADING_LOC);

			Label childSeekhelpHeadingLabel = new Label(I18nProperties.getString(Strings.headingChildSeek));
			childSeekhelpHeadingLabel.addStyleName(H3);
			getContent().addComponent(childSeekhelpHeadingLabel, SEEK_HELP_HEADING_LOC);

			TextField place = addField(HospitalizationDto.PLACE, TextField.class);
			TextField durationMonths = addField(HospitalizationDto.DURATION_MONTHS, TextField.class);
			TextField durationDays = addField(HospitalizationDto.DURATION_DAYS, TextField.class);
			TextField place2 = addField(HospitalizationDto.PLACE2, TextField.class);
			TextField durationMonths2 = addField(HospitalizationDto.DURATION_MONTHS2, TextField.class);
			TextField durationDays2 = addField(HospitalizationDto.DURATION_DAYS2, TextField.class);
			TextField investigatorName = addField(HospitalizationDto.INVESTIGATOR_NAME, TextField.class);
			TextField investigatorTitle = addField(HospitalizationDto.INVESTIGATOR_TITLE, TextField.class);
			TextField investigatorUnit = addField(HospitalizationDto.INVESTIGATOR_UNIT, TextField.class);
			TextField investigatorAddress = addField(HospitalizationDto.INVESTIGATOR_ADDRESS, TextField.class);
			TextField investigatorTel = addField(HospitalizationDto.INVESTIGATOR_TEL, TextField.class);

			admittedToHealthFacilityFieldNew.setVisible(true);
			setVisible(false, HospitalizationDto.LEFT_AGAINST_ADVICE, HospitalizationDto.INTENSIVE_CARE_UNIT, HospitalizationDto.ISOLATED, HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY);
			hospitalizationReason.setVisible(false);
			hospitalizedPreviouslyField.setVisible(false);
			previousHospitalizationsHeadingLabel.setVisible(false);
		}

		/*if(caze.getDisease() != Disease.AHF) {
			FieldHelper.setEnabledWhen(
					admittedToHealthFacilityField,
					Arrays.asList(YesNo.YES, YesNo.NO),
					Arrays.asList(
							facilityField,
							admissionDateField,
							dischargeDateField,
							intensiveCareUnit,
							intensiveCareUnitStart,
							intensiveCareUnitEnd,
							isolationDateField,
							descriptionField,
							isolatedField,
							leftAgainstAdviceField,
							hospitalizationReason,
							otherHospitalizationReason),
					false);
		}*/



		initializeVisibilitiesAndAllowedVisibilities();
		initializeAccessAndAllowedAccesses();

		if (isVisibleAllowed(HospitalizationDto.ISOLATION_DATE)) {
			FieldHelper.setVisibleWhen(
				getFieldGroup(),
				HospitalizationDto.ISOLATION_DATE,
				HospitalizationDto.ISOLATED,
				Arrays.asList(YesNo.YES),
				true);
		}
		if (isVisibleAllowed(HospitalizationDto.PREVIOUS_HOSPITALIZATIONS)) {
			FieldHelper.setVisibleWhen(
				getFieldGroup(),
				HospitalizationDto.PREVIOUS_HOSPITALIZATIONS,
				HospitalizationDto.HOSPITALIZED_PREVIOUSLY,
				Arrays.asList(YesNo.YES),
				true);
		}

		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			HospitalizationDto.OTHER_HOSPITALIZATION_REASON,
			HospitalizationDto.HOSPITALIZATION_REASON,
			Collections.singletonList(HospitalizationReasonType.OTHER),
			true);

		// Validations
		// Add a visual-only validator to check if symptomonsetdate<admissiondate, as saving should be possible either way
		admissionDateField.addValueChangeListener(event -> {
			if (caze.getSymptoms().getOnsetDate() != null
				&& DateComparator.getDateInstance().compare(admissionDateField.getValue(), caze.getSymptoms().getOnsetDate()) < 0) {
				admissionDateField.setComponentError(new ErrorMessage() {

					@Override
					public ErrorLevel getErrorLevel() {
						return ErrorLevel.INFO;
					}

					@Override
					public String getFormattedHtmlMessage() {
						return I18nProperties.getValidationError(
							Validations.afterDateSoft,
							admissionDateField.getCaption(),
							I18nProperties.getPrefixCaption(SymptomsDto.I18N_PREFIX, SymptomsDto.ONSET_DATE));
					}
				});
			} else {
				// remove all invalidity-indicators and re-evaluate field
				admissionDateField.setComponentError(null);
				admissionDateField.markAsDirty();
			}
			// re-evaluate validity of dischargeDate (necessary because discharge has to be after admission)
			dischargeDateField.markAsDirty();
		});
		admissionDateField.addValidator(
			new DateComparisonValidator(
				admissionDateField,
				dischargeDateField,
				true,
				false,
				I18nProperties.getValidationError(Validations.beforeDate, admissionDateField.getCaption(), dischargeDateField.getCaption())));
		dischargeDateField.addValidator(
			new DateComparisonValidator(
				dischargeDateField,
				admissionDateField,
				false,
				false,
				I18nProperties.getValidationError(Validations.afterDate, dischargeDateField.getCaption(), admissionDateField.getCaption())));
		dischargeDateField.addValueChangeListener(event -> admissionDateField.markAsDirty()); // re-evaluate admission date for consistent validation of all fields
		intensiveCareUnitStart.addValidator(
			new DateComparisonValidator(
				intensiveCareUnitStart,
				admissionDateField,
				false,
				false,
				I18nProperties.getValidationError(Validations.afterDate, intensiveCareUnitStart.getCaption(), admissionDateField.getCaption())));
		intensiveCareUnitStart.addValidator(
			new DateComparisonValidator(
				intensiveCareUnitStart,
				intensiveCareUnitEnd,
				true,
				false,
				I18nProperties.getValidationError(Validations.beforeDate, intensiveCareUnitStart.getCaption(), intensiveCareUnitEnd.getCaption())));
		intensiveCareUnitEnd.addValidator(
			new DateComparisonValidator(
				intensiveCareUnitEnd,
				intensiveCareUnitStart,
				false,
				false,
				I18nProperties.getValidationError(Validations.afterDate, intensiveCareUnitEnd.getCaption(), intensiveCareUnitStart.getCaption())));
		intensiveCareUnitEnd.addValidator(
			new DateComparisonValidator(
				intensiveCareUnitEnd,
				dischargeDateField,
				true,
				false,
				I18nProperties.getValidationError(Validations.beforeDate, intensiveCareUnitEnd.getCaption(), dischargeDateField.getCaption())));
		intensiveCareUnitStart.addValueChangeListener(event -> intensiveCareUnitEnd.markAsDirty());
		intensiveCareUnitEnd.addValueChangeListener(event -> intensiveCareUnitStart.markAsDirty());
		hospitalizedPreviouslyField.addValueChangeListener(e -> updatePrevHospHint(hospitalizedPreviouslyField, previousHospitalizationsField));
		previousHospitalizationsField.addValueChangeListener(e -> updatePrevHospHint(hospitalizedPreviouslyField, previousHospitalizationsField));

		if(caze.getDisease() == Disease.CSM){
			addField(HospitalizationDto.DISEASE_ONSET_DATE, DateField.class);
			addField(HospitalizationDto.PATIENT_HOSPITALIZED_DETAINED, NullableOptionGroup.class);

			setVisible(false, HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY, HEALTH_FACILITY, HospitalizationDto.DISCHARGE_DATE, HospitalizationDto.LEFT_AGAINST_ADVICE,
					HospitalizationDto.INTENSIVE_CARE_UNIT, HospitalizationDto.ISOLATED, HospitalizationDto.DESCRIPTION);

			hospitalizationReason.setVisible(false);
			hospitalizedPreviouslyField.setVisible(false);
			previousHospitalizationsHeadingLabel.setVisible(false);

		}

		if(caze.getDisease() == Disease.AHF){
			setVisible(false, HospitalizationDto.ADMISSION_DATE,HospitalizationDto.DISCHARGE_DATE, HospitalizationDto.LEFT_AGAINST_ADVICE, HospitalizationDto.ISOLATED);
			hospitalizationReason.setVisible(false);
			hospitalizedPreviouslyField.setVisible(false);
			previousHospitalizationsHeadingLabel.setVisible(false);

			admittedToHealthFacilityFieldNew.setVisible(true);

		}

		if(caze.getDisease() == Disease.YELLOW_FEVER){
			notifyDistrictDate.setVisible(true);
			hospitalizationReason.setVisible(false);
			hospitalizedPreviouslyField.setVisible(false);
			previousHospitalizationsHeadingLabel.setVisible(false);
		}

		if(caze.getDisease() == Disease.NEW_INFLUENZA){

			hospitalizationReason.setVisible(false);
			hospitalizedPreviouslyField.setVisible(false);
			previousHospitalizationsHeadingLabel.setVisible(false);
			leftAgainstAdviceField.setVisible(false);
			isolatedField.setVisible(false);
			descriptionField.setVisible(false);

			admittedToHealthFacilityFieldNew.setVisible(true);
			admissionDateField.setVisible(true);
			dischargeDateField.setVisible(true);
			dateFirstSeen.setVisible(true);
			terminationDateHospitalStay.setVisible(true);
			dischargeDateField.setCaption("Date person discharged from hospital");
			admissionDateField.setCaption("Date of admission (in-patient)");

		}
	}

	private void updatePrevHospHint(NullableOptionGroup hospitalizedPreviouslyField, PreviousHospitalizationsField previousHospitalizationsField) {

		YesNo value = (YesNo) hospitalizedPreviouslyField.getNullableValue();
		Collection<PreviousHospitalizationDto> previousHospitalizations = previousHospitalizationsField.getValue();
		if (UserProvider.getCurrent().hasUserRight(UserRight.CASE_EDIT)
			&& value == YesNo.YES
			&& (previousHospitalizations == null || previousHospitalizations.size() == 0)) {
			hospitalizedPreviouslyField.setComponentError(new UserError(I18nProperties.getValidationError(Validations.softAddEntryToList)));
		} else {
			hospitalizedPreviouslyField.setComponentError(null);
		}
		if (Objects.nonNull(previousHospitalizationsField.getValue())) {
			hospitalizedPreviouslyField.setEnabled(previousHospitalizationsField.isEmpty());
		} else {
			hospitalizedPreviouslyField.setEnabled(true);
		}

	}

	@Override
	protected String createHtmlLayout() {
		if (caze.getDisease() != null) {
			switch (caze.getDisease()) {
				case MEASLES:
					return MEASLES_LAYOUT;
				default:
					return HTML_LAYOUT;
			}
		} else {
			return HTML_LAYOUT;
		}
	}

	private String getHospitalName(FacilityReferenceDto healthFacility, CaseDataDto caze) {
		final boolean noneFacility = healthFacility == null || healthFacility.getUuid().equalsIgnoreCase(FacilityDto.NONE_FACILITY_UUID);
		if (noneFacility || !FacilityType.HOSPITAL.equals(caze.getFacilityType())) {
			return null;
		}
		StringBuilder hospitalName = new StringBuilder();
		hospitalName.append(healthFacility.buildCaption());
		if (caze.getHealthFacilityDetails() != null && caze.getHealthFacilityDetails().trim().length() > 0) {
			hospitalName.append(String.format(HOSPITAL_NAME_DETAIL, caze.getHealthFacilityDetails()));
		}
		return hospitalName.toString();
	}
}
