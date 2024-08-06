//hospitalizationform
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

import com.vaadin.ui.Component;
import com.vaadin.v7.ui.*;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.ebs.EbsDto;
import de.symeda.sormas.api.hospitalization.SymptomsList;
import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.sample.SampleMaterial;
import de.symeda.sormas.api.utils.InpatOutpat;
import de.symeda.sormas.api.utils.YesNo;

import com.vaadin.server.ErrorMessage;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.ErrorLevel;
import com.vaadin.ui.Label;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseOutcome;
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
import de.symeda.sormas.ui.location.LocationEditForm;
import de.symeda.sormas.ui.utils.*;

public class HospitalizationForm extends AbstractEditForm<HospitalizationDto> {

	private static final long serialVersionUID = 1L;

	private static final String HOSPITALIZATION_HEADING_LOC = "hospitalizationHeadingLoc";
	private static final String PREVIOUS_HOSPITALIZATIONS_HEADING_LOC = "previousHospitalizationsHeadingLoc";
	private static final String FILL_SECTION_HEADING_LOC = "fillSectionHeadingLoc";
	private static final String SEEK_HELP_HEADING_LOC = "seekHelpHeadingLoc";
	private static final String OUTCOME = Captions.CaseData_outcome;
	private static final String SEQUELAE = Captions.CaseData_sequelae;
	private static final String SEQUELAE_DETAILS = Captions.CaseData_sequelaeDetails;
	private static final String OTHERCASEOUTCOMEDETAIL = Captions.CaseData_specify_other_outcome;
	public static final String HEALTH_FACILITY = Captions.CaseHospitalization_healthFacility;
	public static final String HEALTH_FACILITY_DISTRICT = Captions.CaseHospitalization_healthFacilityDistrict;
	public static final String HEALTH_FACILITY_COMMUNITY = Captions.CaseHospitalization_healthFacilityCommunity;
	public static final String HEALTH_FACILITY_REGION = Captions.CaseHospitalization_healthFacilityRegion;
	private static final String HOSPITAL_NAME_DETAIL = " ( %s )";
	private static final String SYMPTOMS_HEADING_LOC = " symptomsHeading";


	private DateField dischargeDateField;

	private OptionGroup caseOutcome;
	private TextField specifyOtherOutcome;
	private NullableOptionGroup sequelae;
	private TextField sequelaeDetails;
	private NullableOptionGroup patientVentilated;
	private final CaseDataDto caze;
	private final ViewMode viewMode;
	private NullableOptionGroup intensiveCareUnit;
	private DateField intensiveCareUnitStart;
	private DateField intensiveCareUnitEnd;
	private ComboBox selectInpatientOutpatient;



	OptionGroup tickSymptomField;
	TextField physicianName;
	TextField physicianNumber;
	private LocationEditForm addressForm;
	//@formatter:off
	private static final String HTML_LAYOUT =
			loc(HOSPITALIZATION_HEADING_LOC) +
			fluidRowLocs(HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY, HospitalizationDto.SEEN_AT_A_HEALTH_FACILITY, HospitalizationDto.WAS_PATIENT_ADMITTED) +
					fluidRowLocs(HEALTH_FACILITY, HEALTH_FACILITY_REGION, HEALTH_FACILITY_DISTRICT, HEALTH_FACILITY_COMMUNITY) +
					fluidRowLocs(6,HospitalizationDto.HOSPITAL_RECORD_NUMBER) +
					fluidRowLocs(6,HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY_NEW) +
					fluidRowLocs(6,HospitalizationDto.MEMBER_FAMILY_HELPING_PATIENT) +
					fluidRowLocs(HospitalizationDto.ADMISSION_DATE, HospitalizationDto.DISCHARGE_DATE, HospitalizationDto.DATE_OF_DEATH) +
					fluidRowLocs(6,HospitalizationDto.TERMINATION_DATE_HOSPITAL_STAY) +
					fluidRowLocs(6,HospitalizationDto.SELECT_INPATIENT_OUTPATIENT) +
					fluidRowLocs(6,HospitalizationDto.DATE_FIRST_SEEN_HOSPITAL_FOR_DISEASE) +
			fluidRowLocs(HospitalizationDto.LEFT_AGAINST_ADVICE, "") +
			fluidRowLocs(HospitalizationDto.NOTIFY_DISTRICT_DATE, HospitalizationDto.DATE_FORM_SENT_TO_DISTRICT) +
			fluidRowLocs(HospitalizationDto.HOSPITALIZATION_REASON, HospitalizationDto.OTHER_HOSPITALIZATION_REASON) +
					fluidRowLocs(3, HospitalizationDto.INTENSIVE_CARE_UNIT, 3,
							HospitalizationDto.INTENSIVE_CARE_UNIT_START,
							3,
							HospitalizationDto.INTENSIVE_CARE_UNIT_END)
					+ fluidRowLocs(HospitalizationDto.ISOLATED, HospitalizationDto.ISOLATION_DATE, "")
					+ fluidRowLocs(HospitalizationDto.PATIENT_VENTILATED)
					+ fluidRowLocs(HospitalizationDto.DESCRIPTION) +
					loc(PREVIOUS_HOSPITALIZATIONS_HEADING_LOC) +
					fluidRowLocs(HospitalizationDto.HOSPITALIZED_PREVIOUSLY) +
					fluidRowLocs(HospitalizationDto.PREVIOUS_HOSPITALIZATIONS) +
					fluidRowLocs(4, HospitalizationDto.DISEASE_ONSET_DATE) +
					fluidRowLocs(HospitalizationDto.PATIENT_HOSPITALIZED_DETAINED) +

					//AFP
					loc(FILL_SECTION_HEADING_LOC) +
					loc(SEEK_HELP_HEADING_LOC) +
					fluidRowLocs(HospitalizationDto.PLACE, HospitalizationDto.DURATION_MONTHS, HospitalizationDto.DURATION_DAYS) +
					fluidRowLocs(HospitalizationDto.PLACE2, HospitalizationDto.DURATION_MONTHS2, HospitalizationDto.DURATION_DAYS2) +
					locCss(VSPACE_TOP_3, HospitalizationDto.INVESTIGATOR_NAME) +
					fluidRowLocs(HospitalizationDto.INVESTIGATOR_TITLE, HospitalizationDto.INVESTIGATOR_UNIT) +
					fluidRowLocs(HospitalizationDto.INVESTIGATOR_ADDRESS, HospitalizationDto.INVESTIGATOR_TEL)+

					loc(SYMPTOMS_HEADING_LOC) +
					fluidRowLocs(HospitalizationDto.SYMPTOMS_SELECTED)+
					fluidRowLocs(6,HospitalizationDto.OTHER_SYMPTOM_SELECTED)+
					fluidRowLocs(HospitalizationDto.ONSET_OF_SYMPTOM_DATETIME, HospitalizationDto.SYMPTOMS_ONGOING)+
					fluidRowLocs(6, HospitalizationDto.DURATION_HOURS)+
					fluidRowLocs(6, HospitalizationDto.SOUGHT_MEDICAL_ATTENTION)+
					fluidRowLocs(HospitalizationDto.LOCATION_TYPE)+
					fluidRowLocs(6, HospitalizationDto.NAME_OF_FACILITY)+
					fluidRowLocs(6,HospitalizationDto.DATE_OF_VISIT_HOSPITAL)+
					fluidRowLocs(6,HospitalizationDto.HOSPITALIZATION_YES_NO) +
					fluidRowLocs(HospitalizationDto.PHYSICIAN_NAME, HospitalizationDto.PHYSICIAN_NUMBER)+
					fluidRowLocs(HospitalizationDto.LAB_TEST_CONDUCTED, HospitalizationDto.TYPE_OF_SAMPLE, HospitalizationDto.AGENT_IDENTIFIED);

	public static final String GUINEA_WORK_LAYOUT = loc(HOSPITALIZATION_HEADING_LOC) +
			fluidRowLocs(HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY, HEALTH_FACILITY) +
			fluidRowLocs(HospitalizationDto.HOSPITAL_RECORD_NUMBER, HospitalizationDto.ADMISSION_DATE, HospitalizationDto.DISCHARGE_DATE);

	public static final String MEASLES_LAYOUT = loc(HOSPITALIZATION_HEADING_LOC) +
			fluidRowLocs(5, HospitalizationDto.SELECT_INPATIENT_OUTPATIENT, 2, "",5, HospitalizationDto.SEEN_AT_A_HEALTH_FACILITY) +
			fluidRowLocs(5, HospitalizationDto.ADMISSION_DATE,2, "", 5, HospitalizationDto.DATE_FIRST_SEEN_HOSPITAL_FOR_DISEASE) +
	fluidRowLocs(5, HospitalizationDto.DISCHARGE_DATE );

	public static final String MPOX_LAYOUT = loc(HOSPITALIZATION_HEADING_LOC) +
			fluidRowLocs(HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY_NEW) +
			fluidRowLocs(HospitalizationDto.LOCATION_TYPE) +
			fluidRowLocs(6,HospitalizationDto.NAME_OF_FACILITY)+
			fluidRowLocs(HospitalizationDto.HOSPITAL_RECORD_NUMBER, HospitalizationDto.ADMISSION_DATE);

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

		caseOutcome = addCustomField(OUTCOME, CaseOutcome.class, OptionGroup.class);
		caseOutcome.setVisible(false);

		specifyOtherOutcome = addCustomField(OTHERCASEOUTCOMEDETAIL, CaseDataDto.class, TextField.class);
		specifyOtherOutcome.setVisible(false);

		/*sequelae = addCustomField(SEQUELAE, CaseDataDto.class, NullableOptionGroup.class);
		sequelae.setVisible(false);

		sequelaeDetails = addCustomField(SEQUELAE_DETAILS, CaseDataDto.class, TextField.class);
		sequelae.setVisible(false);*/

		Label previousHospitalizationsHeadingLabel = new Label(I18nProperties.getString(Strings.headingPreviousHospitalizations));
		previousHospitalizationsHeadingLabel.addStyleName(H3);
		getContent().addComponent(previousHospitalizationsHeadingLabel, PREVIOUS_HOSPITALIZATIONS_HEADING_LOC);

		Label symptomsHeadingLabel = new Label(I18nProperties.getString(Strings.headingSymptoms));
		symptomsHeadingLabel.addStyleName(H3);
		getContent().addComponent(symptomsHeadingLabel, SYMPTOMS_HEADING_LOC);
		symptomsHeadingLabel.setVisible(false);

		TextField regionField = addCustomField(HEALTH_FACILITY_REGION, RegionReferenceDto.class, TextField.class);
		RegionReferenceDto regionReferenceDto = caze.getResponsibleRegion();

		TextField districtField = addCustomField(HEALTH_FACILITY_DISTRICT, DistrictReferenceDto.class, TextField.class);
		DistrictReferenceDto districtReferenceDto = caze.getResponsibleDistrict();

		TextField subDistrictField = addCustomField(HEALTH_FACILITY_COMMUNITY, CommunityReferenceDto.class, TextField.class);
		CommunityReferenceDto communityReferenceDto = caze.getResponsibleCommunity();

		if(regionReferenceDto!= null){
			regionField.setValue(regionReferenceDto.getCaption());
		}
		if(districtReferenceDto!= null){
			districtField.setValue(districtReferenceDto.getCaption());
		}
		if(communityReferenceDto!= null){
			subDistrictField.setValue(communityReferenceDto.getCaption());
		}

		districtField.setReadOnly(true);
		setFieldsVisible(false, regionField, districtField, subDistrictField);

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

		TextField facilityRecord = addField(HospitalizationDto.HEALTH_FACILITY_RECORD_NUMBER, TextField.class);
		facilityRecord.setVisible(false);

		final NullableOptionGroup admittedToHealthFacilityField = addField(HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY, NullableOptionGroup.class);
		// final OptionGroup admittedToHealthFacilityField = addField(HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY, OptionGroup.class);
		final OptionGroup patienConditionOnAdmission = addField(HospitalizationDto.PATIENT_CONDITION_ON_ADMISSION, OptionGroup.class);
		admittedToHealthFacilityField.setVisible(false);

		final NullableOptionGroup seenAtAHealthFacility = addField(HospitalizationDto.SEEN_AT_A_HEALTH_FACILITY, NullableOptionGroup.class);
		seenAtAHealthFacility.setVisible(false);

		final NullableOptionGroup wasPatientAdmitted = addField(HospitalizationDto.WAS_PATIENT_ADMITTED, NullableOptionGroup.class);
		wasPatientAdmitted.setVisible(false);

		NullableOptionGroup admittedToHealthFacilityFieldNew = addField(HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY_NEW, NullableOptionGroup.class);
		NullableOptionGroup hospitalizedYesNo = addField(HospitalizationDto.HOSPITALIZATION_YES_NO, NullableOptionGroup.class);
		addField(HospitalizationDto.RECEPTION_DATE, DateField.class);
		addField(HospitalizationDto.MEMBER_FAMILY_HELPING_PATIENT, TextField.class);
		hospitalizedYesNo.setVisible(false);
		admittedToHealthFacilityFieldNew.setVisible(false);
		admittedToHealthFacilityFieldNew.setCaption("Was the Patient Admitted at the Facility (in-patient)?");
		final DateField admissionDateField = addField(HospitalizationDto.ADMISSION_DATE, DateField.class);
		dischargeDateField = addDateField(HospitalizationDto.DISCHARGE_DATE, DateField.class, 7);
		DateField dateOfDeath = addDateField(HospitalizationDto.DATE_OF_DEATH, DateField.class, 7);
		intensiveCareUnit = addField(HospitalizationDto.INTENSIVE_CARE_UNIT, NullableOptionGroup.class);
		intensiveCareUnitStart = addField(HospitalizationDto.INTENSIVE_CARE_UNIT_START, DateField.class);
		addField(HospitalizationDto.NOTIFY_DISTRICT_DATE, DateField.class);
		addField(HospitalizationDto.DATE_FORM_SENT_TO_DISTRICT, DateField.class);
		DateField dateFirstSeen = addField(HospitalizationDto.DATE_FIRST_SEEN_HOSPITAL_FOR_DISEASE, DateField.class);
		DateField terminationDateHospitalStay = addField(HospitalizationDto.TERMINATION_DATE_HOSPITAL_STAY, DateField.class);
		TextField hospitalRecordNumber = addField(HospitalizationDto.HOSPITAL_RECORD_NUMBER, TextField.class);
		patientVentilated = addField(HospitalizationDto.PATIENT_VENTILATED, NullableOptionGroup.class);
		patientVentilated.setVisible(false);

		selectInpatientOutpatient = addField(HospitalizationDto.SELECT_INPATIENT_OUTPATIENT ,ComboBox.class);
		selectInpatientOutpatient.setVisible(false);

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

		if(caze.getDisease() == Disease.MONKEYPOX){
			addressForm = new LocationEditForm(
					FieldVisibilityCheckers.withCountry(FacadeProvider.getConfigFacade().getCountryLocale()),
					UiFieldAccessCheckers.getNoop(), disease, caze);
		} else{
			addressForm = new LocationEditForm(
					FieldVisibilityCheckers.withCountry(FacadeProvider.getConfigFacade().getCountryLocale()),
					UiFieldAccessCheckers.getNoop(), disease);
		}

		addField(HospitalizationDto.LOCATION_TYPE, addressForm);
		addressForm.setCaption(null);

		ComboBox nameOfFacilityField = addInfrastructureField(HospitalizationDto.NAME_OF_FACILITY);
		nameOfFacilityField.setImmediate(true);

		addressForm.setNameOfFacilityField(nameOfFacilityField);
		addressForm.hideForHospitalizationForm();
		nameOfFacilityField.setVisible(false);
		setVisible(false, HospitalizationDto.LOCATION_TYPE);

		DateField dateOfVisitHospital = addField(HospitalizationDto.DATE_OF_VISIT_HOSPITAL, DateField.class);
		physicianName = addField(HospitalizationDto.PHYSICIAN_NAME, TextField.class);
		physicianNumber = addField(HospitalizationDto.PHYSICIAN_NUMBER, TextField.class);
		NullableOptionGroup labTestConducted = addField(HospitalizationDto.LAB_TEST_CONDUCTED, NullableOptionGroup.class);
		TextField typeOfSample = addField(HospitalizationDto.TYPE_OF_SAMPLE, TextField.class);
		TextField agentIdentified = addField(HospitalizationDto.AGENT_IDENTIFIED, TextField.class);

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

		setVisible(false, HospitalizationDto.SOUGHT_MEDICAL_ATTENTION, HospitalizationDto.DATE_OF_VISIT_HOSPITAL,
				HospitalizationDto.PHYSICIAN_NAME, HospitalizationDto.PHYSICIAN_NUMBER, HospitalizationDto.LAB_TEST_CONDUCTED, HospitalizationDto.TYPE_OF_SAMPLE, HospitalizationDto.AGENT_IDENTIFIED, HospitalizationDto.OTHER_SYMPTOM_SELECTED, HospitalizationDto.ONSET_OF_SYMPTOM_DATETIME, HospitalizationDto.SYMPTOMS_ONGOING, HospitalizationDto.DURATION_HOURS);

		previousHospitalizationsHeadingLabel.setVisible(false);

		setFieldsVisible(false, admissionDateField, dischargeDateField, hospitalRecordNumber, dateFirstSeen, intensiveCareUnit, isolatedField, descriptionField, terminationDateHospitalStay, leftAgainstAdviceField, hospitalizationReason, hospitalizedPreviouslyField);

		if (caze.getDisease() == Disease.MEASLES) {

			selectInpatientOutpatient.setVisible(true);
			seenAtAHealthFacility.setVisible(true);
			FieldHelper.setVisibleWhen(
					seenAtAHealthFacility,
					Arrays.asList(dateFirstSeen),
					Arrays.asList(YesNoUnknown.YES),
					true);

			FieldHelper.setVisibleWhen(
					selectInpatientOutpatient,
					Arrays.asList(admissionDateField, dischargeDateField),
					Arrays.asList(InpatOutpat.INPATIENT),
					true);

		}

		if(caze.getDisease() == Disease.AFP){

			setVisible(false, HospitalizationDto.LEFT_AGAINST_ADVICE, HospitalizationDto.INTENSIVE_CARE_UNIT, HospitalizationDto.ISOLATED, HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY, HospitalizationDto.DISCHARGE_DATE, HospitalizationDto.DATE_FORM_SENT_TO_DISTRICT, HospitalizationDto.NOTIFY_DISTRICT_DATE);
			hospitalizationReason.setVisible(false);
			hospitalizedPreviouslyField.setVisible(false);
			previousHospitalizationsHeadingLabel.setVisible(false);

			admittedToHealthFacilityFieldNew.setVisible(true);
			hospitalRecordNumber.setVisible(true);
			dateFirstSeen.setVisible(true);
			dateFirstSeen.setCaption("Date of admission to hospital, if applicable:");
		}

		initializeVisibilitiesAndAllowedVisibilities();
		initializeAccessAndAllowedAccesses();
//		addressForm.hideForHospitalizationForm();

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
		dischargeDateField.addValueChangeListener(e -> showCaseOutcome());
		//caseOutcome.addValueChangeListener(e -> addOtherOutcomeValue());
		/*caseOutcome.addValueChangeListener(e -> addSequelaeValue());
		sequelae.addValueChangeListener(e -> addSequelaeDetailsValue());*/

		setVisible(false, HospitalizationDto.RECEPTION_DATE, HospitalizationDto.DATE_OF_DEATH, HospitalizationDto.MEMBER_FAMILY_HELPING_PATIENT);

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

		if(caze.getDisease() == Disease.AHF){
			hospitalizationReason.setVisible(false);
			hospitalizedPreviouslyField.setVisible(false);
			previousHospitalizationsHeadingLabel.setVisible(false);

			hospitalRecordNumber.setVisible(true);
			isolatedField.setVisible(true);

			setVisible(true, HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY_NEW, HospitalizationDto.DATE_OF_DEATH, HospitalizationDto.MEMBER_FAMILY_HELPING_PATIENT);
			FieldHelper.setVisibleWhen(admittedToHealthFacilityFieldNew, Arrays.asList(admissionDateField, dischargeDateField, dateOfDeath), Arrays.asList(YesNo.YES), true);
		}

		if(caze.getDisease() == Disease.YELLOW_FEVER){
			hospitalizationReason.setVisible(false);
			hospitalizedPreviouslyField.setVisible(false);
			previousHospitalizationsHeadingLabel.setVisible(false);

			dateFirstSeen.setVisible(true);
			dateFirstSeen.setCaption("Date seen at health facility");
			selectInpatientOutpatient.setVisible(true);
		}

		if(caze.getDisease() == Disease.NEW_INFLUENZA){

			hospitalizationReason.setVisible(false);
			hospitalizedPreviouslyField.setVisible(false);
			previousHospitalizationsHeadingLabel.setVisible(false);

			admittedToHealthFacilityFieldNew.setVisible(true);
			admissionDateField.setVisible(true);
			dischargeDateField.setVisible(true);
			terminationDateHospitalStay.setVisible(true);
			dischargeDateField.setCaption("Date person discharged from hospital");
			admissionDateField.setCaption("Date of admission (in-patient)");
			facilityField.setVisible(true);
			intensiveCareUnit.setVisible(true);

			FieldHelper
					.setVisibleWhen(admittedToHealthFacilityFieldNew, Arrays.asList(admissionDateField, dischargeDateField, terminationDateHospitalStay, intensiveCareUnit), Arrays.asList(YesNo.YES), true);

		}

		if (caze.getDisease() == Disease.CORONAVIRUS){
			hideFields();
			hospitalRecordNumber.setVisible(true);
			patientVentilated.setVisible(true);
			intensiveCareUnit.setVisible(true);
			hospitalizedPreviouslyField.setVisible(true);
			setVisible(true, HospitalizationDto.ISOLATED, HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY,HospitalizationDto.ADMISSION_DATE, HospitalizationDto.DISCHARGE_DATE, HospitalizationDto.LEFT_AGAINST_ADVICE);
		}

		if (caze.getDisease() == Disease.NEONATAL_TETANUS) {
			hideFields();
			hospitalRecordNumber.setVisible(true);
			admittedToHealthFacilityField.setVisible(true);
			setVisible(false, HospitalizationDto.LEFT_AGAINST_ADVICE, HospitalizationDto.INTENSIVE_CARE_UNIT, HospitalizationDto.ISOLATED, HospitalizationDto.DISCHARGE_DATE, HospitalizationDto.PREVIOUS_HOSPITALIZATIONS);
		}

		if(caze.getDisease() == Disease.FOODBORNE_ILLNESS){
			addressForm.hideForFoodBorne();
			hospitalizationHeadingLabel.setVisible(false);
			facilityField.setVisible(false);
			symptomsHeadingLabel.setVisible(true);
			tickSymptomField.setVisible(true);

			setVisible(true, HospitalizationDto.SOUGHT_MEDICAL_ATTENTION, HospitalizationDto.AGENT_IDENTIFIED, HospitalizationDto.OTHER_SYMPTOM_SELECTED, HospitalizationDto.ONSET_OF_SYMPTOM_DATETIME, HospitalizationDto.SYMPTOMS_ONGOING, HospitalizationDto.DURATION_HOURS);

			FieldHelper.setVisibleWhen(ongoing, Arrays.asList(durationHours), Arrays.asList(YesNo.NO), true);
			FieldHelper.setVisibleWhen(soughtMedicalAttentionField, Arrays.asList(dateOfVisitHospital, hospitalizedYesNo, labTestConducted), Arrays.asList(YesNo.YES), true);
			FieldHelper.setVisibleWhen(hospitalizedYesNo, Arrays.asList(physicianName, physicianNumber), Arrays.asList(YesNo.YES), true);
			FieldHelper.setVisibleWhen(labTestConducted, Arrays.asList(typeOfSample, agentIdentified), Arrays.asList(YesNo.YES), true);

			FieldHelper.setVisibleWhen(
					fieldGroup,
					Arrays.asList(HospitalizationDto.LOCATION_TYPE),
					(Field) soughtMedicalAttentionField,
					Arrays.asList(YesNo.YES),
					true
			);

		}
		
		if(caze.getDisease() == Disease.MONKEYPOX){
			setFieldsVisible(true, admittedToHealthFacilityFieldNew);
//			setVisible(true, HospitalizationDto.HOSPITAL_RECORD_NUMBER, HospitalizationDto.LOCATION_TYPE, HospitalizationDto.NAME_OF_FACILITY);

			FieldHelper.setVisibleWhen(
					getFieldGroup(),
					Arrays.asList(HospitalizationDto.ADMISSION_DATE, HospitalizationDto.HOSPITAL_RECORD_NUMBER, HospitalizationDto.LOCATION_TYPE, HospitalizationDto.NAME_OF_FACILITY),
					HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY_NEW,
					Arrays.asList(YesNo.YES),
					false
			);
		}

		if (caze.getDisease() == Disease.GUINEA_WORM) {
			hideAllFields();
//			HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY make visble for HospitalizationDto.DISCHARGE_DATE, HospitalizationDto.ADMISSION_DATE, HospitalizationDto.HOSPITAL_RECORD_NUMBER
			setVisible(true, HospitalizationDto.ADMITTED_TO_HEALTH_FACILITY);
			FieldHelper.setVisibleWhen(
				admittedToHealthFacilityField,
				Arrays.asList(admissionDateField, dischargeDateField, hospitalRecordNumber),
				Arrays.asList(YesNo.YES),
				true);

		}

		//Cholera
		if (caze.getDisease() == Disease.CHOLERA){
			hideAllFields();
			setVisible(true, HospitalizationDto.HOSPITAL_RECORD_NUMBER);
		}

		if(caze.getDisease() == Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS){
			dateFirstSeen.setVisible(true);
			dateFirstSeen.setCaption("Date seen at health facility");
			selectInpatientOutpatient.setVisible(true);
		}
	}

	private void setDateFieldVisibilties() {

		boolean visible = YesNoUnknown.YES.equals(intensiveCareUnit.getNullableValue());
		intensiveCareUnitStart.setVisible(visible);
		intensiveCareUnitEnd.setVisible(visible);
	}

//	private void showCaseOutcome() {
//		if ((dischargeDateField.isModified() || !dischargeDateField.equals(null)) /* && caze.getOutcome() == null */) {
//			CaseOutcome outcome = caze.getOutcome();
//			caseOutcome.setRequired(true);
//			caseOutcome.setValue(outcome == null ? null : outcome);
//			caseOutcome.setVisible(true);
//		}
//	}
//	private void addOtherOutcomeValue() {
//		if (caseOutcome.getValue() == CaseOutcome.OTHER) {
////			caseOutcome.setValue(outcome == null ? null : outcome);
//			otherCaseOutcomeDetails.setVisible(true);
//			System.err.println(caseOutcome.getValue());
//		}
//	}

	private void showCaseOutcome() {
		if ((dischargeDateField.isModified() || !dischargeDateField.equals(null)) /* && caze.getOutcome() == null */) {
			CaseOutcome outcome = caze.getOutcome();
			caseOutcome.setRequired(true);
			caseOutcome.setValue(outcome == null ? null : outcome);
			caseOutcome.setVisible(true);
		}
	}

	//Commented temporarily: will create a static filter list later
	/*private void addOtherOutcomeValue() {
		if (caseOutcome.getValue() == CaseOutcome.OTHER) {
			specifyOtherOutcome.setValue(caze.getSpecifyOtherOutcome());
			specifyOtherOutcome.setVisible(true);
		} else {
			specifyOtherOutcome.setVisible(false);
			specifyOtherOutcome.setValue(null);
		}
	}*/

	private void addSequelaeValue() {
		if (caseOutcome.getValue() == CaseOutcome.RECOVERED || caseOutcome.getValue() == CaseOutcome.UNKNOWN) {
			sequelae.setValue(caze.getSequelae());
			sequelae.setVisible(true);
		} else {
			sequelae.setVisible(false);
			sequelae.setValue(null);
		}
	}

	private void addSequelaeDetailsValue() {
		if (sequelae.getValue() == YesNoUnknown.YES) {
			sequelaeDetails.setValue(caze.getSequelaeDetails());
			sequelaeDetails.setVisible(true);
		} else {
			sequelaeDetails.setVisible(false);
			sequelaeDetails.setValue(null);
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
				case GUINEA_WORM:
					return GUINEA_WORK_LAYOUT;
				case MEASLES:
					return MEASLES_LAYOUT;
				case MONKEYPOX:
					return MPOX_LAYOUT;
				default:
					return HTML_LAYOUT;
			}
		} else {
			return HTML_LAYOUT;
		}
	}

	public OptionGroup getCaseOutcome() {
		return caseOutcome;
	}

	public void setCaseOutcome(OptionGroup caseOutcome) {
		this.caseOutcome = caseOutcome;
	}

	public TextField getSpecifyOtherOutcome() {
		return specifyOtherOutcome;
	}

	public void setSpecifyOtherOutcome(TextField specifyOtherOutcome) {
		this.specifyOtherOutcome = specifyOtherOutcome;
	}

	public NullableOptionGroup getSequelae() {
		return sequelae;
	}

	public void setSequelae(NullableOptionGroup sequelae) {
		this.sequelae = sequelae;
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

	//hide all fields
	public void hideFields() {
		//get field group
		for (Object propertyId : getFieldGroup().getUnboundPropertyIds()) {
			setVisible(false, propertyId.toString());
		}
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

	public void hideAllFields () {
		for (Field<?> field : getFieldGroup().getFields()) {
			//check if field is not null
			if (field != null)
				//hide the field
				field.setVisible(false);

		}
	}

	@Override
	public void setValue(HospitalizationDto newFieldValue) {
		super.setValue(newFieldValue);

		// HACK: Binding to the fields will call field listeners that may clear/modify the values of other fields.
		// this hopefully resets everything to its correct value
		addressForm.discard();
	}
}
