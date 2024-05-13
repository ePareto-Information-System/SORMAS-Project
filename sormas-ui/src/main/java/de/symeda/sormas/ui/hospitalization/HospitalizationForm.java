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

import java.util.*;
import java.util.stream.Collectors;

import com.vaadin.v7.ui.*;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.hospitalization.SymptomsList;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.sample.SampleMaterial;
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
import de.symeda.sormas.ui.caze.CaseFormConfiguration;
import de.symeda.sormas.ui.utils.*;

public class HospitalizationForm extends AbstractEditForm<HospitalizationDto> {

	private static final long serialVersionUID = 1L;

	private static final String HOSPITALIZATION_HEADING_LOC = "hospitalizationHeadingLoc";
	private static final String PREVIOUS_HOSPITALIZATIONS_HEADING_LOC = "previousHospitalizationsHeadingLoc";
	private static final String FILL_SECTION_HEADING_LOC = "fillSectionHeadingLoc";
	private static final String SEEK_HELP_HEADING_LOC = "seekHelpHeadingLoc";
	public static final String HEALTH_FACILITY = Captions.CaseHospitalization_healthFacility;
	public static final String HEALTH_FACILITY_DISTRICT = Captions.CaseHospitalization_healthFacilityDistrict;
	private static final String HOSPITAL_NAME_DETAIL = " ( %s )";
	private static final String SYMPTOMS_HEADING_LOC = " symptomsHeading";
	OptionGroup tickSymptomField;
	//@formatter:off
	private static final String HTML_LAYOUT =
			loc(HOSPITALIZATION_HEADING_LOC) +
			fluidRowLocs(HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY) +
					fluidRowLocs(HEALTH_FACILITY, HEALTH_FACILITY_DISTRICT, HospitalizationDto.HOSPITAL_RECORD_NUMBER) +
					fluidRowLocs(6,HospitalizationDto.SELECT_INPATIENT_OUTPATIENT) +
			fluidRowLocs(HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY_NEW, HospitalizationDto.PHYSICIAN_NAME) +
					fluidRowLocs(HospitalizationDto.DATE_FIRST_SEEN_HOSPITAL_FOR_DISEASE, HospitalizationDto.TERMINATION_DATE_HOSPITAL_STAY) +
			fluidRowLocs(HospitalizationDto.ADMISSION_DATE, HospitalizationDto.DISCHARGE_DATE, HospitalizationDto.LEFT_AGAINST_ADVICE, "") +
			fluidRowLocs(HospitalizationDto.NOTIFY_DISTRICT_DATE, HospitalizationDto.DATE_FORM_SENT_TO_DISTRICT) +
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

			loc(SYMPTOMS_HEADING_LOC) +
			fluidRowLocs(HospitalizationDto.SYMPTOMS_SELECTED)+
			fluidRowLocs(6,HospitalizationDto.OTHER_SYMPTOM_SELECTED)+
			fluidRowLocs(HospitalizationDto.ONSET_OF_SYMPTOM_DATETIME, HospitalizationDto.SYMPTOMS_ONGOING)+
			fluidRowLocs(6, HospitalizationDto.DURATION_HOURS)+
			fluidRowLocs(HospitalizationDto.SOUGHT_MEDICAL_ATTENTION, HospitalizationDto.NAME_OF_FACILITY)+
			fluidRowLocs(HospitalizationDto.LOCATION_ADDRESS, HospitalizationDto.DATE_OF_VISIT_HOSPITAL)+
			fluidRowLocs(6, HospitalizationDto.PHYSICIAN_NUMBER)+
			fluidRowLocs(HospitalizationDto.LAB_TEST_CONDUCTED, HospitalizationDto.TYPE_OF_SAMPLE, HospitalizationDto.AGENT_IDENTIFIED);
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

		Label symptomsHeadingLabel = new Label(I18nProperties.getString(Strings.headingSymptoms));
		symptomsHeadingLabel.addStyleName(H3);
		getContent().addComponent(symptomsHeadingLabel, SYMPTOMS_HEADING_LOC);
		symptomsHeadingLabel.setVisible(false);

		TextField districtField = addCustomField(HEALTH_FACILITY_DISTRICT, DistrictReferenceDto.class, TextField.class);
		DistrictReferenceDto districtReferenceDto = caze.getResponsibleDistrict();

		if(districtReferenceDto!= null){
			districtField.setValue(districtReferenceDto.getCaption());
		}
		districtField.setReadOnly(true);
		districtField.setCaption("District where Health Facility located");
		districtField.setVisible(false);

		TextField facilityField = addCustomField(HEALTH_FACILITY, FacilityReferenceDto.class, TextField.class);
		FacilityReferenceDto healthFacility1 = caze.getHealthFacility();

		String healthFacility = caze.getHospitalName();
		FacilityType facility = caze.getFacilityType();
		final boolean noneFacility = healthFacility == null || healthFacility.equalsIgnoreCase(FacilityDto.NONE_FACILITY_UUID);

		if(healthFacility != null ){
			facilityField.setValue(healthFacility);
		}
		else if (facility != null && facility != FacilityType.HOSPITAL){
			facilityField.setValue(String.valueOf(facility));
		}
		else if (healthFacility1 != null){
			facilityField.setValue(healthFacility1.getCaption());
		}
		else {
			facilityField.setValue(noneFacility || !FacilityType.HOSPITAL.equals(caze.getFacilityType()) ? null : healthFacility1.getCaption());
		}
		facilityField.setReadOnly(true);

		final NullableOptionGroup admittedToHealthFacilityField = addField(HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY, NullableOptionGroup.class);
		admittedToHealthFacilityField.setVisible(false);

		final NullableOptionGroup admittedToHealthFacilityFieldNew = addField(HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY_NEW, NullableOptionGroup.class);
		admittedToHealthFacilityFieldNew.setVisible(false);
		admittedToHealthFacilityFieldNew.setCaption("Was the Patient Admitted at the Facility (in-patient)?");
		final DateField admissionDateField = addField(HospitalizationDto.ADMISSION_DATE, DateField.class);
		final DateField dischargeDateField = addDateField(HospitalizationDto.DISCHARGE_DATE, DateField.class, 7);
		intensiveCareUnit = addField(HospitalizationDto.INTENSIVE_CARE_UNIT, NullableOptionGroup.class);
		intensiveCareUnitStart = addField(HospitalizationDto.INTENSIVE_CARE_UNIT_START, DateField.class);
		DateField notifyDistrictDate = addField(HospitalizationDto.NOTIFY_DISTRICT_DATE, DateField.class);
		DateField dateFormSentToDistrict = addField(HospitalizationDto.DATE_FORM_SENT_TO_DISTRICT, DateField.class);
		DateField dateFirstSeen = addField(HospitalizationDto.DATE_FIRST_SEEN_HOSPITAL_FOR_DISEASE, DateField.class);
		DateField terminationDateHospitalStay = addField(HospitalizationDto.TERMINATION_DATE_HOSPITAL_STAY, DateField.class);
		TextField hospitalRecordNumber = addField(HospitalizationDto.HOSPITAL_RECORD_NUMBER, TextField.class);

		intensiveCareUnitEnd = addField(HospitalizationDto.INTENSIVE_CARE_UNIT_END, DateField.class);
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

		NullableOptionGroup soughtMedicalAttentionField = addField(HospitalizationDto.SOUGHT_MEDICAL_ATTENTION, NullableOptionGroup.class);
		TextField nameOfFacilityField = addField(HospitalizationDto.NAME_OF_FACILITY, TextField.class);
		TextField locationAddressField = addField(HospitalizationDto.LOCATION_ADDRESS, TextField.class);
		DateField dateOfVisitHospitalField = addField(HospitalizationDto.DATE_OF_VISIT_HOSPITAL, DateField.class);
		TextField physicianNameField = addField(HospitalizationDto.PHYSICIAN_NAME, TextField.class);
		TextField physicianNumberField = addField(HospitalizationDto.PHYSICIAN_NUMBER, TextField.class);
		NullableOptionGroup labTestConductedField = addField(HospitalizationDto.LAB_TEST_CONDUCTED, NullableOptionGroup.class);
		TextField typeOfSampleField = addField(HospitalizationDto.TYPE_OF_SAMPLE, TextField.class);
		TextField agentIdentifiedField = addField(HospitalizationDto.AGENT_IDENTIFIED, TextField.class);

		tickSymptomField = addField(HospitalizationDto.SYMPTOMS_SELECTED, OptionGroup.class);
		CssStyles.style(tickSymptomField, CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
		tickSymptomField.setMultiSelect(true);
		addField(HospitalizationDto.OTHER_SYMPTOM_SELECTED, TextField.class);
		addField(HospitalizationDto.ONSET_OF_SYMPTOM_DATETIME, DateTimeField.class);
		NullableOptionGroup ongoing = addField(HospitalizationDto.SYMPTOMS_ONGOING, NullableOptionGroup.class);
		ComboBox durationHours = addField(HospitalizationDto.DURATION_HOURS, ComboBox.class);

		tickSymptomField.addItems(
				Arrays.stream(SymptomsList.FoodBorne())
						.filter( c -> fieldVisibilityCheckers.isVisible(SymptomsList.class, c.name()))
						.collect(Collectors.toList()));

		tickSymptomField.setVisible(false);

		setVisible(false, HospitalizationDto.SOUGHT_MEDICAL_ATTENTION, HospitalizationDto.NAME_OF_FACILITY, HospitalizationDto.LOCATION_ADDRESS, HospitalizationDto.DATE_OF_VISIT_HOSPITAL,
				HospitalizationDto.PHYSICIAN_NAME, HospitalizationDto.PHYSICIAN_NUMBER, HospitalizationDto.LAB_TEST_CONDUCTED, HospitalizationDto.TYPE_OF_SAMPLE, HospitalizationDto.AGENT_IDENTIFIED, HospitalizationDto.OTHER_SYMPTOM_SELECTED, HospitalizationDto.ONSET_OF_SYMPTOM_DATETIME, HospitalizationDto.SYMPTOMS_ONGOING, HospitalizationDto.DURATION_HOURS);

		admissionDateField.setVisible(false);
		dischargeDateField.setVisible(false);
		hospitalRecordNumber.setVisible(false);
		dateFirstSeen.setVisible(false);
		intensiveCareUnit.setVisible(false);
		isolatedField.setVisible(false);
		descriptionField.setVisible(false);
		terminationDateHospitalStay.setVisible(false);
		leftAgainstAdviceField.setVisible(false);
		hospitalizationReason.setVisible(false);
		hospitalizedPreviouslyField.setVisible(false);
		previousHospitalizationsHeadingLabel.setVisible(false);

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

		hideFieldsForSelectedDisease(caze.getDisease());

		if(caze.getDisease() == Disease.CSM){
			addField(HospitalizationDto.DISEASE_ONSET_DATE, DateField.class);
			addField(HospitalizationDto.PATIENT_HOSPITALIZED_DETAINED, NullableOptionGroup.class);

/*			setVisible(false, HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY, HospitalizationDto.DISCHARGE_DATE, HospitalizationDto.LEFT_AGAINST_ADVICE,
					HospitalizationDto.INTENSIVE_CARE_UNIT, HospitalizationDto.ISOLATED, HospitalizationDto.DESCRIPTION, HospitalizationDto.DATE_FORM_SENT_TO_DISTRICT);

			notifyDistrictDate.setVisible(false);*/
			hospitalizationReason.setVisible(false);
			hospitalizedPreviouslyField.setVisible(false);
			previousHospitalizationsHeadingLabel.setVisible(false);
			hospitalRecordNumber.setVisible(true);
			dateFirstSeen.setVisible(true);
			dateFirstSeen.setCaption("Date seen at health facility:");

		}

		if(caze.getDisease() == Disease.AFP){
			hospitalizationReason.setVisible(false);
			hospitalizedPreviouslyField.setVisible(false);
			previousHospitalizationsHeadingLabel.setVisible(false);

			admittedToHealthFacilityFieldNew.setVisible(true);
			hospitalRecordNumber.setVisible(true);
			dateFirstSeen.setVisible(true);
			dateFirstSeen.setCaption("Date of admission to hospital, if applicable:");
		}

		if(caze.getDisease() == Disease.AHF || caze.getDisease() == Disease.DENGUE){
//			setVisible(false, HospitalizationDto.ADMISSION_DATE,HospitalizationDto.DISCHARGE_DATE, HospitalizationDto.LEFT_AGAINST_ADVICE, HospitalizationDto.DATE_FORM_SENT_TO_DISTRICT);
			hospitalizationReason.setVisible(false);
			hospitalizedPreviouslyField.setVisible(false);
			previousHospitalizationsHeadingLabel.setVisible(false);
//			notifyDistrictDate.setVisible(false);

			hospitalRecordNumber.setVisible(true);
			intensiveCareUnit.setVisible(true);
			isolatedField.setVisible(true);
			descriptionField.setVisible(true);

			setVisible(true, HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY_NEW);

		}

		if(caze.getDisease() == Disease.YELLOW_FEVER){
			hospitalizationReason.setVisible(false);
			hospitalizedPreviouslyField.setVisible(false);
			previousHospitalizationsHeadingLabel.setVisible(false);
		}

		if(caze.getDisease() == Disease.NEW_INFLUENZA || caze.getDisease() == Disease.SARI){

			hospitalizationReason.setVisible(false);
			hospitalizedPreviouslyField.setVisible(false);
			previousHospitalizationsHeadingLabel.setVisible(false);

			admittedToHealthFacilityFieldNew.setVisible(true);
			admissionDateField.setVisible(true);
			dischargeDateField.setVisible(true);
			dateFirstSeen.setVisible(true);
			terminationDateHospitalStay.setVisible(true);
			dischargeDateField.setCaption("Date person discharged from hospital");
			admissionDateField.setCaption("Date of admission (in-patient)");
			facilityField.setVisible(true);
			districtField.setVisible(true);
			intensiveCareUnit.setVisible(true);

		}

		if(caze.getDisease() == Disease.FOODBORNE_ILLNESS){
			hospitalizationHeadingLabel.setVisible(false);
			facilityField.setVisible(false);
			symptomsHeadingLabel.setVisible(true);
			tickSymptomField.setVisible(true);

			setVisible(true, HospitalizationDto.SOUGHT_MEDICAL_ATTENTION, HospitalizationDto.NAME_OF_FACILITY, HospitalizationDto.LOCATION_ADDRESS, HospitalizationDto.DATE_OF_VISIT_HOSPITAL,
					 HospitalizationDto.PHYSICIAN_NUMBER, HospitalizationDto.LAB_TEST_CONDUCTED, HospitalizationDto.TYPE_OF_SAMPLE, HospitalizationDto.AGENT_IDENTIFIED, HospitalizationDto.OTHER_SYMPTOM_SELECTED, HospitalizationDto.ONSET_OF_SYMPTOM_DATETIME, HospitalizationDto.SYMPTOMS_ONGOING, HospitalizationDto.DURATION_HOURS);

			FieldHelper.setVisibleWhen(ongoing, Arrays.asList(durationHours), Arrays.asList(YesNo.NO), true);
			FieldHelper.setVisibleWhen(soughtMedicalAttentionField, Arrays.asList(nameOfFacilityField), Arrays.asList(YesNo.YES), true);

		}
		if(caze.getDisease() == Disease.MONKEYPOX){
			setVisible(true, HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY_NEW, HospitalizationDto.ADMISSION_DATE, HospitalizationDto.HOSPITAL_RECORD_NUMBER);
		}

		if (caze.getDisease() == Disease.GUINEA_WORM) {
			hideAllFields();
			//show discharge date
			setVisible(true, HospitalizationDto.DISCHARGE_DATE, HospitalizationDto.ADMISSION_DATE);
		}

		//Cholera
		if (caze.getDisease() == Disease.CHOLERA){
			hideAllFields();
			setVisible(true, HospitalizationDto.HOSPITAL_RECORD_NUMBER);
		}

		if(caze.getDisease() == Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS){
			dateFirstSeen.setVisible(true);
			dateFirstSeen.setCaption("Date seen at health facility");
			addField(HospitalizationDto.SELECT_INPATIENT_OUTPATIENT ,ComboBox.class);
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
		return HTML_LAYOUT;
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

	public void hideFieldsForSelectedDisease(Disease disease) {
		Set<String> disabledFields = HospitalizationFormConfiguration.getDisabledFieldsForDisease(disease);
		for (String field : disabledFields) {
			disableField(field);
		}
	}

	private void disableField(String field) {
		setVisible(false, field);
	}
}
