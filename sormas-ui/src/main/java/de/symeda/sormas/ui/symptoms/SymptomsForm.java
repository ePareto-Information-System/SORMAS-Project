/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.ui.symptoms;

import static de.symeda.sormas.api.symptoms.SymptomsDto.*;
import static de.symeda.sormas.ui.utils.CssStyles.H3;
import static de.symeda.sormas.ui.utils.CssStyles.H4;
import static de.symeda.sormas.ui.utils.CssStyles.VSPACE_3;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidColumn;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRow;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowCss;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocsCss;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;
import static de.symeda.sormas.ui.utils.LayoutUtil.locCss;
import static de.symeda.sormas.ui.utils.LayoutUtil.locsCss;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.v7.ui.*;
import de.symeda.sormas.api.caze.CaseOutcome;

import com.vaadin.server.ErrorMessage;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.ErrorLevel;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.converter.Converter.ConversionException;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.VaccinationStatus;
import de.symeda.sormas.api.event.TypeOfPlace;
import de.symeda.sormas.api.hospitalization.HospitalizationDto;
import de.symeda.sormas.api.hospitalization.SymptomsList;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.Descriptions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.person.ApproximateAgeType;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.person.Sex;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.sample.SampleMaterial;
import de.symeda.sormas.api.symptoms.CongenitalHeartDiseaseType;
import de.symeda.sormas.api.symptoms.SymptomState;
import de.symeda.sormas.api.symptoms.SymptomsContext;
import de.symeda.sormas.api.symptoms.SymptomsDto;
import de.symeda.sormas.api.symptoms.SymptomsHelper;
import de.symeda.sormas.api.utils.*;
import de.symeda.sormas.api.symptoms.*;
import de.symeda.sormas.api.utils.DateComparator;
import de.symeda.sormas.api.utils.InjectionSite;
import de.symeda.sormas.api.utils.SymptomGroup;
import de.symeda.sormas.api.utils.SymptomGrouping;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.api.utils.pseudonymization.SampleDispatchMode;
import de.symeda.sormas.api.visit.VisitStatus;
import de.symeda.sormas.ui.utils.*;

public class SymptomsForm extends AbstractEditForm<SymptomsDto> {

	private static final long serialVersionUID = 1L;

	private static final String CLINICAL_MEASUREMENTS_HEADING_LOC = "clinicalMeasurementsHeadingLoc";
	private static final String FOOD_HISTORY_HEADING_LOC = "foodHistoryHeadingLoc";
	private static final String CLINICAL_HISTORY_HEADING_LOC = "clinicalHistoryHeadingLoc";
	private static final String SIGNS_AND_SYMPTOMS_HEADING_LOC = "signsAndSymptomsHeadingLoc";
	private static final String GENERAL_SIGNS_AND_SYMPTOMS_HEADING_LOC = "generalSignsAndSymptomsHeadingLoc";
	private static final String RESPIRATORY_SIGNS_AND_SYMPTOMS_HEADING_LOC = "respiratorySignsAndSymptomsHeadingLoc";
	private static final String CARDIOVASCULAR_SIGNS_AND_SYMPTOMS_HEADING_LOC = "cardiovascularSignsAndSymptomsHeadingLoc";
	private static final String GASTROINTESTINAL_SIGNS_AND_SYMPTOMS_HEADING_LOC = "gastrointestinalSignsAndSymptomsHeadingLoc";
	private static final String URINARY_SIGNS_AND_SYMPTOMS_HEADING_LOC = "urinarySignsAndSymptomsHeadingLoc";
	private static final String NERVOUS_SYSTEM_SIGNS_AND_SYMPTOMS_HEADING_LOC = "nervousSystemSignsAndSymptomsHeadingLoc";
	private static final String RASH_AND_SYMPTOMS_HEADING_LOC = "rashAndSymptomsHeadingLoc";
	private static final String RASH_CHARACTERISTICS_AND_SYMPTOMS_HEADING_LOC = "rashCharacteristicsAndSymptomsHeadingLoc";
	private static final String RASH_TYPE_AND_SYMPTOMS_HEADING_LOC = "rashTypeAndSymptomsHeadingLoc";
	private static final String SKIN_SIGNS_AND_SYMPTOMS_HEADING_LOC = "skinSignsAndSymptomsHeadingLoc";
	private static final String OTHER_SIGNS_AND_SYMPTOMS_HEADING_LOC = "otherSignsAndSymptomsHeadingLoc";
	private static final String BUTTONS_LOC = "buttonsLoc";
	private static final String LESIONS_LOCATIONS_LOC = "lesionsLocationsLoc";
	private static final String MONKEYPOX_LESIONS_IMG1 = "monkeypoxLesionsImg1";
	private static final String MONKEYPOX_LESIONS_IMG2 = "monkeypoxLesionsImg2";
	private static final String MONKEYPOX_LESIONS_IMG3 = "monkeypoxLesionsImg3";
	private static final String MONKEYPOX_LESIONS_IMG4 = "monkeypoxLesionsImg4";
	private static final String SYMPTOMS_HINT_LOC = "symptomsHintLoc";
	private static final String COMPLICATIONS_HEADING = "complicationsHeading";
	private static final String MPOX_SYMPTOMS_HEADING_LOC = " symptomsHeading";
	private static final String MPOX_RASH_HEADING_LOC = " mpozRash";

	private static Map<String, List<String>> symptomGroupMap = new HashMap();

	//@formatter:off
	private static final String HTML_LAYOUT =
			loc(CLINICAL_MEASUREMENTS_HEADING_LOC) +
					fluidRowLocs(TEMPERATURE, TEMPERATURE_SOURCE) +
					fluidRowLocs(BLOOD_PRESSURE_SYSTOLIC, BLOOD_PRESSURE_DIASTOLIC, HEART_RATE, RESPIRATORY_RATE) +
					fluidRowLocs(GLASGOW_COMA_SCALE, WEIGHT, HEIGHT, MID_UPPER_ARM_CIRCUMFERENCE) +
					loc(SIGNS_AND_SYMPTOMS_HEADING_LOC) +
					fluidRowCss(VSPACE_3,
							//XXX #1620 fluidColumnLoc?
							fluidColumn(8, 0, loc(SYMPTOMS_HINT_LOC))) +
					loc(MPOX_SYMPTOMS_HEADING_LOC) +
					fluidRowLocs(SymptomsDto.SYMPTOMS_SELECTED)+
					fluidRowLocs(6,SymptomsDto.SYMPTOMS_SELECTED_OTHER)+
					loc(MPOX_RASH_HEADING_LOC) +
					fluidRowLocs(6, DATE_OF_ONSET_RASH)+
					fluidRowLocs(RASH_SYMPTOMS)+
					fluidRowLocs(6, RASH_SYMPTOMS_OTHER_AREAS)+
					fluidRowLocs(ARE_LESIONS_IN_SAME_STATE)+
					fluidRowLocs(ARE_LESIONS_SAME_SIZE)+
					fluidRowLocs(ARE_LESIONS_DEEP)+
					fluidRowLocs(ARE_ULCERS_AMONG_LESIONS)+
					fluidRowLocs(6, TYPE_OF_RASH)+
					fluidRowLocs(DATE_OF_ONSET, "") +
					fluidRowLocs(6,FEVER_BODY_TEMP_GREATER) +
					fluidRow(fluidColumn(8,4, locCss(CssStyles.ALIGN_RIGHT,BUTTONS_LOC)))+
					createSymptomGroupLayout(SymptomGroup.GENERAL, GENERAL_SIGNS_AND_SYMPTOMS_HEADING_LOC) +
					fluidRowLocs(FEVER_ONSET_PARALYSIS, PROGRESSIVE_PARALYSIS) +
					fluidRowLocs(DATE_ONSET_PARALYSIS, PROGRESSIVE_FLACID_ACUTE, ASSYMETRIC) +
					fluidRowLocs(SITE_OF_PARALYSIS) +
					fluidRowLocs(PARALYSED_LIMB_SENSITIVE_TO_PAIN, INJECTION_SITE_BEFORE_ONSET_PARALYSIS) +
					fluidRowLocs(RIGHT_INJECTION_SITE, LEFT_INJECTION_SITE) +
					fluidRowLocs(6, TRUEAFP) +
					fluidRowLocs(6,ALTERED_CONSCIOUSNESS) +
					fluidRowLocs(6,CONFUSED_DISORIENTED) +
					fluidRowLocs(6,HEMORRHAGIC_SYNDROME) +
					fluidRowLocs(HYPERGLYCEMIA, HYPOGLYCEMIA) +
					fluidRowLocs(6,MENINGEAL_SIGNS) +
					fluidRowLocs(6,SEIZURES) +
					fluidRowLocs(6,SEPSIS) +
					fluidRowLocs(6,SHOCK) +
					fluidRowLocs(AGE_AT_DEATH_DAYS, AGE_AT_ONSET_DAYS) +
					fluidRowLocs(6,OTHER_COMPLICATIONS) +
					fluidRowLocs(6,OTHER_COMPLICATIONS_TEXT) +
					createSymptomGroupLayout(SymptomGroup.RESPIRATORY, RESPIRATORY_SIGNS_AND_SYMPTOMS_HEADING_LOC) +
					createSymptomGroupLayout(SymptomGroup.CARDIOVASCULAR, CARDIOVASCULAR_SIGNS_AND_SYMPTOMS_HEADING_LOC) +
					//createSymptomGroupLayout(SymptomGroup.GASTROINTESTINAL, GASTROINTESTINAL_SIGNS_AND_SYMPTOMS_HEADING_LOC) +
					createSymptomGroupLayout(SymptomGroup.URINARY, URINARY_SIGNS_AND_SYMPTOMS_HEADING_LOC) +
					createSymptomGroupLayout(SymptomGroup.NERVOUS_SYSTEM, NERVOUS_SYSTEM_SIGNS_AND_SYMPTOMS_HEADING_LOC) +
					//createSymptomGroupLayout(SymptomGroup.RASH, RASH_AND_SYMPTOMS_HEADING_LOC) +
					//createSymptomGroupLayout(SymptomGroup.RASH_CHARACTERISTICS, RASH_CHARACTERISTICS_AND_SYMPTOMS_HEADING_LOC) +
					//createSymptomGroupLayout(SymptomGroup.RASH_TYPE, RASH_TYPE_AND_SYMPTOMS_HEADING_LOC) +
					createSymptomGroupLayout(SymptomGroup.SKIN, SKIN_SIGNS_AND_SYMPTOMS_HEADING_LOC) +
					createSymptomGroupLayout(SymptomGroup.OTHER, OTHER_SIGNS_AND_SYMPTOMS_HEADING_LOC) +

					fluidRowLocs(FEVER_ONSET_PARALYSIS, PROGRESSIVE_PARALYSIS) +
					fluidRowLocs(DATE_ONSET_PARALYSIS, PROGRESSIVE_FLACID_ACUTE, ASSYMETRIC) +
					fluidRowLocs(6,SITE_OF_PARALYSIS) +
					fluidRowLocs(PARALYSED_LIMB_SENSITIVE_TO_PAIN, INJECTION_SITE_BEFORE_ONSET_PARALYSIS) +
					fluidRowLocs(RIGHT_INJECTION_SITE, LEFT_INJECTION_SITE) +
					fluidRowLocs( PLACE_OF_EXPOSURE_MEASLES_RUBELLA) +
					fluidRowLocs(PATIENT_ILL_LOCATION, SYMPTOMS_COMMENTS) +
					fluidRowLocs(6, ONSET_SYMPTOM) +
					fluidRowLocs(6, ONSET_DATE) +
					fluidRowLocsCss(FIRST_WORM_THIS_YEAR, FIRST_SIGN_OR_SYMPTOMS_BEFORE_WORM_OTHERS, NUMBER_OF_WORMS, DATE_FIRST_WORM_EMERGENCE) +
					//loc(CLINICAL_HISTORY_HEADING_LOC) +
					fluidRowLocs(OUTCOME, OUTCOME_OTHER)+
					fluidRowLocs(PROVISONAL_DIAGNOSIS)+
					fluidRowLocs(6, TRUEAFP)+
					fluidRowLocs(SYMPTOMS_ONGOING, DURATION_HOURS, YES_NAME_OF_HEALTH_FACILITY)+

					loc(FOOD_HISTORY_HEADING_LOC);

	//@formatter:on

	private static String createSymptomGroupLayout(SymptomGroup symptomGroup, String loc) {

		final Predicate<java.lang.reflect.Field> groupSymptoms =
			field -> field.isAnnotationPresent(SymptomGrouping.class) && field.getAnnotation(SymptomGrouping.class).value() == symptomGroup;
		final List<String> symptomLocations = Arrays.stream(SymptomsDto.class.getDeclaredFields())
			.filter(groupSymptoms)
			.map(field -> field.getName())
			.sorted(Comparator.comparing(fieldName -> I18nProperties.getPrefixCaption(I18N_PREFIX, fieldName)))
			.collect(Collectors.toList());

		if (symptomGroup == SymptomGroup.SKIN) {
			symptomLocations.add(symptomLocations.indexOf(LESIONS_RESEMBLE_IMG1) + 1, MONKEYPOX_LESIONS_IMG1);
			symptomLocations.add(symptomLocations.indexOf(LESIONS_RESEMBLE_IMG2) + 1, MONKEYPOX_LESIONS_IMG2);
			symptomLocations.add(symptomLocations.indexOf(LESIONS_RESEMBLE_IMG3) + 1, MONKEYPOX_LESIONS_IMG3);
			symptomLocations.add(symptomLocations.indexOf(LESIONS_RESEMBLE_IMG4) + 1, MONKEYPOX_LESIONS_IMG4);
		}

		symptomGroupMap.put(loc, symptomLocations);

		return loc(loc)
			+ fluidRow(
				fluidColumn(6, -1, locsCss(VSPACE_3, new ArrayList<>(symptomLocations.subList(0, symptomLocations.size() / 2)))),
				fluidColumn(
					6,
					0,
					locsCss(VSPACE_3, new ArrayList<>(symptomLocations.subList(symptomLocations.size() / 2, symptomLocations.size())))));
	}

	private final CaseDataDto caze;
	private final Disease disease;
	private final PersonDto person;
	private final SymptomsContext symptomsContext;
	private final ViewMode viewMode;
	private transient List<String> unconditionalSymptomFieldIds;
	private List<String> conditionalBleedingSymptomFieldIds;
	private List<String> lesionsFieldIds;
	private List<String> lesionsTypeIds;
	private List<String> lesionsLocationFieldIds;
	private List<String> monkeypoxImageFieldIds;
	private Button clearAllButton;
	private Button setEmptyToNoButton;
	OptionGroup tickSymptomField;
	OptionGroup tickRashCharacteristicsField;

	public SymptomsForm(
		CaseDataDto caze,
		Disease disease,
		PersonDto person,
		SymptomsContext symptomsContext,
		ViewMode viewMode,
		UiFieldAccessCheckers fieldAccessCheckers) {

		// TODO add user right parameter
		super(
			SymptomsDto.class,
			I18N_PREFIX,
			false,
			FieldVisibilityCheckers.withDisease(disease)
				.andWithCountry(FacadeProvider.getConfigFacade().getCountryLocale())
				.add(new OutbreakFieldVisibilityChecker(viewMode)),
			fieldAccessCheckers);

		this.caze = caze;
		this.disease = disease;
		this.person = person;
		this.symptomsContext = symptomsContext;
		this.viewMode = viewMode;
		if (disease == null || symptomsContext == null) {
			throw new IllegalArgumentException("disease and symptoms context cannot be null");
		}
		if (symptomsContext == SymptomsContext.CASE && caze == null) {
			throw new IllegalArgumentException("case cannot be null when symptoms context is case");
		}
		addFields();
		hideValidationUntilNextCommit();
	}

	@Override
	protected void addFields() {
		if (disease == null || symptomsContext == null) {
			// workaround to stop initialization until disease is set
			return;
		}

		// Add fields
		Label clinicalMeasurementsHeadingLabel =
			createLabel(I18nProperties.getString(Strings.headingClinicalMeasurements), H3, CLINICAL_MEASUREMENTS_HEADING_LOC);

		Label headingClinicalHistoryHeadingLabel =
				createLabel(I18nProperties.getString(Strings.headingClinicalHistory), H3, CLINICAL_HISTORY_HEADING_LOC);
		Label signsAndSymptomsHeadingLabel =
			createLabel(I18nProperties.getString(Strings.headingSignsAndSymptoms), H3, SIGNS_AND_SYMPTOMS_HEADING_LOC);
		signsAndSymptomsHeadingLabel.setVisible(false);
		final Label generalSymptomsHeadingLabel = createLabel(SymptomGroup.GENERAL.toString(), H4, GENERAL_SIGNS_AND_SYMPTOMS_HEADING_LOC);
		generalSymptomsHeadingLabel.setVisible(false);
		final Label respiratorySymptomsHeadingLabel =
			createLabel(SymptomGroup.RESPIRATORY.toString(), H4, RESPIRATORY_SIGNS_AND_SYMPTOMS_HEADING_LOC);
		respiratorySymptomsHeadingLabel.setVisible(false);
		final Label cardiovascularSymptomsHeadingLabel =
			createLabel(SymptomGroup.CARDIOVASCULAR.toString(), H4, CARDIOVASCULAR_SIGNS_AND_SYMPTOMS_HEADING_LOC);
		cardiovascularSymptomsHeadingLabel.setVisible(false);
		final Label gastrointestinalSymptomsHeadingLabel =
			createLabel(SymptomGroup.GASTROINTESTINAL.toString(), H4, GASTROINTESTINAL_SIGNS_AND_SYMPTOMS_HEADING_LOC);
		gastrointestinalSymptomsHeadingLabel.setVisible(false);
		final Label urinarySymptomsHeadingLabel = createLabel(SymptomGroup.URINARY.toString(), H4, URINARY_SIGNS_AND_SYMPTOMS_HEADING_LOC);
		urinarySymptomsHeadingLabel.setVisible(false);
		final Label nervousSystemSymptomsHeadingLabel =
			createLabel(SymptomGroup.NERVOUS_SYSTEM.toString(), H4, NERVOUS_SYSTEM_SIGNS_AND_SYMPTOMS_HEADING_LOC);
		nervousSystemSymptomsHeadingLabel.setVisible(false);
		final Label rashSymptomsHeadingLabel = createLabel(SymptomGroup.RASH.toString(), H4, RASH_AND_SYMPTOMS_HEADING_LOC);
		rashSymptomsHeadingLabel.setVisible(false);
		final Label rashCharacteristicsSymptomsHeadingLabel =
			createLabel(SymptomGroup.RASH_CHARACTERISTICS.toString(), H4, RASH_CHARACTERISTICS_AND_SYMPTOMS_HEADING_LOC);
		final Label rashTypeSymptomsHeadingLabel = createLabel(SymptomGroup.RASH_TYPE.toString(), H4, RASH_TYPE_AND_SYMPTOMS_HEADING_LOC);
		rashTypeSymptomsHeadingLabel.setVisible(false);
		final Label skinSymptomsHeadingLabel = createLabel(SymptomGroup.SKIN.toString(), H4, SKIN_SIGNS_AND_SYMPTOMS_HEADING_LOC);
		skinSymptomsHeadingLabel.setVisible(false);
		final Label otherSymptomsHeadingLabel = createLabel(SymptomGroup.OTHER.toString(), H4, OTHER_SIGNS_AND_SYMPTOMS_HEADING_LOC);
		otherSymptomsHeadingLabel.setVisible(false);

		Label foodHistoryHeadingLabel =
				createLabel(I18nProperties.getString(Strings.headingFoodHistory), H3, FOOD_HISTORY_HEADING_LOC);
		foodHistoryHeadingLabel.setVisible(false);

		DateField onsetDateField = addField(ONSET_DATE, DateField.class);
		ComboBox onsetSymptom = addField(ONSET_SYMPTOM, ComboBox.class);
		if (symptomsContext == SymptomsContext.CASE) {
			// If the symptom onset date is after the hospital admission date, show a warning but don't prevent the user from saving
			onsetDateField.addValueChangeListener(event -> {
				if (caze.getHospitalization().getAdmissionDate() != null
					&& DateComparator.getDateInstance().compare(caze.getHospitalization().getAdmissionDate(), onsetDateField.getValue()) < 0) {
					onsetDateField.setComponentError(new ErrorMessage() {

						@Override
						public ErrorLevel getErrorLevel() {
							return ErrorLevel.INFO;
						}

						@Override
						public String getFormattedHtmlMessage() {
							return I18nProperties.getValidationError(
								Validations.beforeDateSoft,
								onsetDateField.getCaption(),
								I18nProperties.getPrefixCaption(HospitalizationDto.I18N_PREFIX, HospitalizationDto.ADMISSION_DATE));
						}
					});
				} else if (onsetDateField.isValid()) {
					onsetDateField.setComponentError(null);
				}
			});
		}

		ComboBox temperature = addField(TEMPERATURE, ComboBox.class);
		temperature.setImmediate(true);
		for (Float temperatureValue : SymptomsHelper.getTemperatureValues()) {
			temperature.addItem(temperatureValue);
			temperature.setItemCaption(temperatureValue, SymptomsHelper.getTemperatureString(temperatureValue));
		}
		if (symptomsContext == SymptomsContext.CASE) {
			if(disease != Disease.NEW_INFLUENZA){
				temperature.setCaption(I18nProperties.getCaption(Captions.symptomsMaxTemperature));
			} else{
				temperature.setCaption(I18nProperties.getCaption(Captions.bodyTemperature));
			}
		}
		addField(TEMPERATURE_SOURCE);

		ComboBox outcome = new ComboBox("Outcome");

		if(disease == Disease.YELLOW_FEVER){
			for (CaseOutcome caseOutcome : CaseOutcome.values()) {
				if (caseOutcome == CaseOutcome.DECEASED || caseOutcome == CaseOutcome.ALIVE) {
					outcome.addItem(caseOutcome);
				}
			}
			addField(CaseDataDto.OUTCOME, outcome);
		}
		else{
			outcome = addField(CaseDataDto.OUTCOME, ComboBox.class);
			outcome.removeItem(CaseOutcome.UNKNOWN);
		}

		TextField outcomeOther = addField(OUTCOME_OTHER, TextField.class);
		outcomeOther.setVisible(false);

		FieldHelper.setVisibleWhen(outcome, Arrays.asList(outcomeOther), Arrays.asList(CaseOutcome.OTHER), true);

		ComboBox bloodPressureSystolic = addField(BLOOD_PRESSURE_SYSTOLIC, ComboBox.class);
		bloodPressureSystolic.addItems(SymptomsHelper.getBloodPressureValues());
		ComboBox bloodPressureDiastolic = addField(BLOOD_PRESSURE_DIASTOLIC, ComboBox.class);
		bloodPressureDiastolic.addItems(SymptomsHelper.getBloodPressureValues());
		ComboBox heartRate = addField(HEART_RATE, ComboBox.class);
		heartRate.addItems(SymptomsHelper.getHeartRateValues());
		ComboBox respiratoryRate = addField(RESPIRATORY_RATE, ComboBox.class);
		respiratoryRate.addItems(SymptomsHelper.getRespiratoryRateValues());
		ComboBox weight = addField(WEIGHT, ComboBox.class);
		for (Integer weightValue : SymptomsHelper.getWeightValues()) {
			weight.addItem(weightValue);
			weight.setItemCaption(weightValue, SymptomsHelper.getDecimalString(weightValue));
		}
		ComboBox height = addField(HEIGHT, ComboBox.class);
		height.addItems(SymptomsHelper.getHeightValues());
		ComboBox midUpperArmCircumference = addField(MID_UPPER_ARM_CIRCUMFERENCE, ComboBox.class);
		for (Integer circumferenceValue : SymptomsHelper.getMidUpperArmCircumferenceValues()) {
			midUpperArmCircumference.addItem(circumferenceValue);
			midUpperArmCircumference.setItemCaption(circumferenceValue, SymptomsHelper.getDecimalString(circumferenceValue));
		}
		ComboBox glasgowComaScale = addField(GLASGOW_COMA_SCALE, ComboBox.class);
		glasgowComaScale.addItems(SymptomsHelper.getGlasgowComaScaleValues());

		NullableOptionGroup symptomsOngoing = addField(SYMPTOMS_ONGOING, NullableOptionGroup.class);
		ComboBox durationHours = addField(DURATION_HOURS, ComboBox.class);
		TextField nameOfHealthFacility = addField(YES_NAME_OF_HEALTH_FACILITY, TextField.class);

		symptomsOngoing.setVisible(false);
		durationHours.setVisible(false);
		nameOfHealthFacility.setVisible(false);

		addFields(
			VOMITING,
			DIARRHEA,
			BLOOD_IN_STOOL,
			NAUSEA,
			ABDOMINAL_PAIN,
			HEADACHE,
			MUSCLE_PAIN,
			CHILLS_SWEATS,
			FATIGUE_WEAKNESS,
			SKIN_RASH,
			NECK_STIFFNESS,
			SORE_THROAT,
			COUGH,
			COUGH_WITH_SPUTUM,
			COUGH_WITH_HEAMOPTYSIS,
			RUNNY_NOSE,
			DIFFICULTY_BREATHING,
			CHEST_PAIN,
			CONJUNCTIVITIS,
			EYE_PAIN_LIGHT_SENSITIVE,
			KOPLIKS_SPOTS,
			THROBOCYTOPENIA,
			OTITIS_MEDIA,
			HEARINGLOSS,
			DEHYDRATION,
			ANOREXIA_APPETITE_LOSS,
			REFUSAL_FEEDOR_DRINK,
			JOINT_PAIN,
			HICCUPS,
			BACKACHE,
			EYES_BLEEDING,
			JAUNDICE,
			DARK_URINE,
			STOMACH_BLEEDING,
			RAPID_BREATHING,
			SWOLLEN_GLANDS,
			UNEXPLAINED_BLEEDING,
			GUMS_BLEEDING,
			INJECTION_SITE_BLEEDING,
			NOSE_BLEEDING,
			BLOODY_BLACK_STOOL,
			RED_BLOOD_VOMIT,
			DIGESTED_BLOOD_VOMIT,
			COUGHING_BLOOD,
			BLEEDING_VAGINA,
			SKIN_BRUISING,
			BLOOD_URINE,
			OTHER_HEMORRHAGIC_SYMPTOMS,
			OTHER_HEMORRHAGIC_SYMPTOMS_TEXT,
			OTHER_NON_HEMORRHAGIC_SYMPTOMS,
			OTHER_NON_HEMORRHAGIC_SYMPTOMS_TEXT,
			LESIONS,
			RASHES,
			LESIONS_THAT_ITCH,
			LESIONS_SAME_STATE,
			LESIONS_SAME_SIZE,
			LESIONS_DEEP_PROFOUND,
			LESIONS_FACE,
			LESIONS_LEGS,
			LESIONS_SOLES_FEET,
			LESIONS_PALMS_HANDS,
			LESIONS_THORAX,
			LESIONS_ARMS,
			LESIONS_GENITALS,
			LESIONS_ALL_OVER_BODY,
			LYMPHADENOPATHY,
			LYMPHADENOPATHY_AXILLARY,
			LYMPHADENOPATHY_CERVICAL,
			LYMPHADENOPATHY_INGUINAL,
			BEDRIDDEN,
			ORAL_ULCERS,
			PAINFUL_LYMPHADENITIS,
			BLACKENING_DEATH_OF_TISSUE,
			BUBOES_GROIN_ARMPIT_NECK,
			BULGING_FONTANELLE,
			PHARYNGEAL_ERYTHEMA,
			PHARYNGEAL_EXUDATE,
			OEDEMA_FACE_NECK,
			OEDEMA_LOWER_EXTREMITY,
			LOSS_SKIN_TURGOR,
			PALPABLE_LIVER,
			PALPABLE_SPLEEN,
			MALAISE,
			SUNKEN_EYES_FONTANELLE,
			SIDE_PAIN,
			FLUID_IN_LUNG_CAVITY,
			TREMOR,
			BILATERAL_CATARACTS,
			UNILATERAL_CATARACTS,
			CONGENITAL_GLAUCOMA,
			CONGENITAL_HEART_DISEASE,
			PIGMENTARY_RETINOPATHY,
			RADIOLUCENT_BONE_DISEASE,
			SPLENOMEGALY,
			MICROCEPHALY,
			MENINGOENCEPHALITIS,
			PURPURIC_RASH,
			DEVELOPMENTAL_DELAY,
			CONGENITAL_HEART_DISEASE_TYPE,
			CONGENITAL_HEART_DISEASE_DETAILS,
			JAUNDICE_WITHIN_24_HOURS_OF_BIRTH,
			PATIENT_ILL_LOCATION,
			HYDROPHOBIA,
			OPISTHOTONUS,
			ANXIETY_STATES,
			DELIRIUM,
			UPROARIOUSNESS,
			PARASTHESIA_AROUND_WOUND,
			EXCESS_SALIVATION,
			INSOMNIA,
			PARALYSIS,
			EXCITATION,
			DYSPHAGIA,
			AEROPHOBIA,
			HYPERACTIVITY,
			PARESIS,
			AGITATION,
			ASCENDING_FLACCID_PARALYSIS,
			ERRATIC_BEHAVIOUR,
			COMA,
			CONVULSION,
			FLUID_IN_LUNG_CAVITY_AUSCULTATION,
			DYSPNEA,
			TACHYPNEA,
			FLUID_IN_LUNG_CAVITY_XRAY,
			ABNORMAL_LUNG_XRAY_FINDINGS,
			CONJUNCTIVAL_INJECTION,
			ACUTE_RESPIRATORY_DISTRESS_SYNDROME,
			PNEUMONIA_CLINICAL_OR_RADIOLOGIC,
			LOSS_OF_TASTE,
			LOSS_OF_SMELL,
			WHEEZING,
			SKIN_ULCERS,
			INABILITY_TO_WALK,
			IN_DRAWING_OF_CHEST_WALL,
			FEELING_ILL,
			SHIVERING,
			RESPIRATORY_DISEASE_VENTILATION,
			FAST_HEART_RATE,
			OXYGEN_SATURATION_LOWER_94,
			FEVERISHFEELING,
			WEAKNESS,
			FATIGUE,
			COUGH_WITHOUT_SPUTUM,
			BREATHLESSNESS,
			CHEST_PRESSURE,
			BLUE_LIPS,
			BLOOD_CIRCULATION_PROBLEMS,
			PALPITATIONS,
			DIZZINESS_STANDING_UP,
			HIGH_OR_LOW_BLOOD_PRESSURE,
			URINARY_RETENTION,
			FEVER,
			GENERAL_BODILY_PAINS,
			LESIONS_NECK,
			LESIONS_TRUNK,
			PAPULAR_RASH,
			MACULAR_RASH,
			VESICULAR_RASH,
			OTHER_LESION_AREAS,
			NON_VASCULAR,
			BODY_ACHE,
			BABY_DIED,
			BABY_NORMAL_AT_BIRTH,
			NORMAL_CRY_AND_SUCK,
			STOPPED_SUCKING_AFTER_TWO_DAYS,
			STIFFNESS,
			DIZZINESS,
			EXCESSIVE_SWEATING,
			NUMBNESS,
			EMERGENCE_OF_GUINEA_WORM,
			FIRST_WORM_THIS_YEAR,
			CASE_DETECTED_BEFORE_WORM_EMERGENCE,
			DIARRHOEA,
			ABDOMINAL_CRAMPS,
			HEADACHES,
			GENERALIZED_RASH,
			RED_EYES,
			SWOLLEN_LYMPH_NODES_BEHIND_EARS,
			HISTORY_OF_TRAVEL_OUTSIDE_THE_VILLAGE_TOWN_DISTRICT
			);

		TextField babyAgeAtDeath = addField(AGE_AT_DEATH_DAYS, TextField.class);
		TextField ageOfOnsetDays =  addField(AGE_AT_ONSET_DAYS, TextField.class);
		setVisible(false, AGE_AT_DEATH_DAYS, AGE_AT_ONSET_DAYS,
				VOMITING, DIARRHEA, BLOOD_IN_STOOL, NAUSEA, ABDOMINAL_PAIN, HEADACHE, MUSCLE_PAIN, CHILLS_SWEATS, FATIGUE_WEAKNESS, SKIN_RASH, NECK_STIFFNESS, SORE_THROAT, COUGH, COUGH_WITH_SPUTUM, COUGH_WITH_HEAMOPTYSIS, RUNNY_NOSE, DIFFICULTY_BREATHING, CHEST_PAIN, CONJUNCTIVITIS, EYE_PAIN_LIGHT_SENSITIVE, KOPLIKS_SPOTS, THROBOCYTOPENIA, OTITIS_MEDIA, HEARINGLOSS, DEHYDRATION, ANOREXIA_APPETITE_LOSS, REFUSAL_FEEDOR_DRINK, JOINT_PAIN, HICCUPS, BACKACHE, EYES_BLEEDING, JAUNDICE, DARK_URINE, STOMACH_BLEEDING, RAPID_BREATHING, SWOLLEN_GLANDS, UNEXPLAINED_BLEEDING, GUMS_BLEEDING, INJECTION_SITE_BLEEDING, NOSE_BLEEDING, BLOODY_BLACK_STOOL, RED_BLOOD_VOMIT, DIGESTED_BLOOD_VOMIT, COUGHING_BLOOD, BLEEDING_VAGINA, SKIN_BRUISING, BLOOD_URINE, OTHER_HEMORRHAGIC_SYMPTOMS, OTHER_HEMORRHAGIC_SYMPTOMS_TEXT, OTHER_NON_HEMORRHAGIC_SYMPTOMS, OTHER_NON_HEMORRHAGIC_SYMPTOMS_TEXT, LESIONS, LESIONS_THAT_ITCH, LESIONS_SAME_STATE, LESIONS_SAME_SIZE, LESIONS_DEEP_PROFOUND, LESIONS_FACE, LESIONS_LEGS, LESIONS_SOLES_FEET, LESIONS_PALMS_HANDS, LESIONS_THORAX, LESIONS_ARMS, LESIONS_GENITALS, LESIONS_ALL_OVER_BODY, LYMPHADENOPATHY, LYMPHADENOPATHY_AXILLARY, LYMPHADENOPATHY_CERVICAL, LYMPHADENOPATHY_INGUINAL, BEDRIDDEN, ORAL_ULCERS, PAINFUL_LYMPHADENITIS, BLACKENING_DEATH_OF_TISSUE, BUBOES_GROIN_ARMPIT_NECK, BULGING_FONTANELLE, PHARYNGEAL_ERYTHEMA, PHARYNGEAL_EXUDATE, OEDEMA_FACE_NECK, OEDEMA_LOWER_EXTREMITY, LOSS_SKIN_TURGOR, PALPABLE_LIVER, PALPABLE_SPLEEN, MALAISE, SUNKEN_EYES_FONTANELLE, SIDE_PAIN, FLUID_IN_LUNG_CAVITY, TREMOR, BILATERAL_CATARACTS, UNILATERAL_CATARACTS, CONGENITAL_GLAUCOMA, CONGENITAL_HEART_DISEASE, PIGMENTARY_RETINOPATHY, RADIOLUCENT_BONE_DISEASE, SPLENOMEGALY, MICROCEPHALY, MENINGOENCEPHALITIS, PURPURIC_RASH, DEVELOPMENTAL_DELAY, CONGENITAL_HEART_DISEASE_TYPE, CONGENITAL_HEART_DISEASE_DETAILS, JAUNDICE_WITHIN_24_HOURS_OF_BIRTH, PATIENT_ILL_LOCATION, HYDROPHOBIA, OPISTHOTONUS, ANXIETY_STATES, DELIRIUM, UPROARIOUSNESS, PARASTHESIA_AROUND_WOUND, EXCESS_SALIVATION, INSOMNIA, PARALYSIS, EXCITATION, DYSPHAGIA, AEROPHOBIA, HYPERACTIVITY, PARESIS, AGITATION, ASCENDING_FLACCID_PARALYSIS, ERRATIC_BEHAVIOUR, COMA, CONVULSION, FLUID_IN_LUNG_CAVITY_AUSCULTATION, FLUID_IN_LUNG_CAVITY_XRAY, ABNORMAL_LUNG_XRAY_FINDINGS, CONJUNCTIVAL_INJECTION, ACUTE_RESPIRATORY_DISTRESS_SYNDROME, PNEUMONIA_CLINICAL_OR_RADIOLOGIC, LOSS_OF_TASTE, LOSS_OF_SMELL, WHEEZING, SKIN_ULCERS, INABILITY_TO_WALK, IN_DRAWING_OF_CHEST_WALL, FEELING_ILL, SHIVERING, RESPIRATORY_DISEASE_VENTILATION, FAST_HEART_RATE, OXYGEN_SATURATION_LOWER_94, FEVERISHFEELING, WEAKNESS, FATIGUE, COUGH_WITHOUT_SPUTUM, BREATHLESSNESS, CHEST_PRESSURE, BLUE_LIPS, BLOOD_CIRCULATION_PROBLEMS, PALPITATIONS, DIZZINESS_STANDING_UP, HIGH_OR_LOW_BLOOD_PRESSURE, URINARY_RETENTION, FEVER, BODY_ACHE, DIZZINESS, EXCESSIVE_SWEATING, NUMBNESS);

		addField(SYMPTOMS_COMMENTS, TextField.class).setDescription(
			I18nProperties.getPrefixDescription(I18N_PREFIX, SYMPTOMS_COMMENTS, "") + "\n" + I18nProperties.getDescription(Descriptions.descGdpr));

		addField(LESIONS_ONSET_DATE, DateField.class);

		addField(SymptomsDto.PLACE_OF_EXPOSURE_MEASLES_RUBELLA, TextField.class).setVisible(false);


		// complications
		String[] complicationsFieldIds = {
			ALTERED_CONSCIOUSNESS,
			CONFUSED_DISORIENTED,
			OTHER_COMPLICATIONS,
			OTHER_COMPLICATIONS_TEXT,
			HEMORRHAGIC_SYNDROME,
			HYPERGLYCEMIA,
			HYPOGLYCEMIA,
			MENINGEAL_SIGNS,
			SEIZURES,
			SEPSIS,
			SHOCK };

		addFields(complicationsFieldIds);

		monkeypoxImageFieldIds = Arrays.asList(LESIONS_RESEMBLE_IMG1, LESIONS_RESEMBLE_IMG2, LESIONS_RESEMBLE_IMG3, LESIONS_RESEMBLE_IMG4);

		for (String propertyId : monkeypoxImageFieldIds) {
			//@SuppressWarnings("rawtypes")
			addField(propertyId);
		}
	

		// Set initial visibilities

		initializeVisibilitiesAndAllowedVisibilities();
		initializeAccessAndAllowedAccesses();

		if (symptomsContext != SymptomsContext.CLINICAL_VISIT) {
			setVisible(
				false,
				BLOOD_PRESSURE_SYSTOLIC,
				BLOOD_PRESSURE_DIASTOLIC,
				HEART_RATE,
				RESPIRATORY_RATE,
				WEIGHT,
				HEIGHT,
				MID_UPPER_ARM_CIRCUMFERENCE,
				GLASGOW_COMA_SCALE);
		}
		/*else {
			setVisible(false, ONSET_SYMPTOM, ONSET_DATE);
		}*/

		// Initialize lists

		conditionalBleedingSymptomFieldIds = Arrays.asList(
			BLOODY_BLACK_STOOL,
			DIGESTED_BLOOD_VOMIT,
			COUGHING_BLOOD,
			SKIN_BRUISING,
			STOMACH_BLEEDING,
			BLOOD_URINE,
			OTHER_HEMORRHAGIC_SYMPTOMS);

		lesionsFieldIds = Arrays.asList(LESIONS_SAME_STATE, LESIONS_SAME_SIZE, LESIONS_DEEP_PROFOUND, LESIONS_THAT_ITCH);

		lesionsTypeIds = Arrays.asList(PURPURIC_RASH, LESIONS, SKIN_RASH);

		lesionsLocationFieldIds = Arrays.asList(
			LESIONS_FACE,
			LESIONS_LEGS,
			LESIONS_SOLES_FEET,
			LESIONS_PALMS_HANDS,
			LESIONS_THORAX,
			LESIONS_ARMS,
			LESIONS_TRUNK,
			LESIONS_GENITALS,
			LESIONS_ALL_OVER_BODY,
			LESIONS_NECK,
			PAPULAR_RASH,
			MACULAR_RASH,
			VESICULAR_RASH,
			OTHER_LESION_AREAS);

		unconditionalSymptomFieldIds = Arrays.asList(
			FEVER,
			ABNORMAL_LUNG_XRAY_FINDINGS,
			CONJUNCTIVAL_INJECTION,
			ACUTE_RESPIRATORY_DISTRESS_SYNDROME,
			PNEUMONIA_CLINICAL_OR_RADIOLOGIC,
			VOMITING,
			DIARRHEA,
			BLOOD_IN_STOOL,
			NAUSEA,
			ABDOMINAL_PAIN,
			HEADACHE,
			MUSCLE_PAIN,
			FATIGUE_WEAKNESS,
			SKIN_RASH,
			NECK_STIFFNESS,
			SORE_THROAT,
			COUGH,
			COUGH_WITH_SPUTUM,
			COUGH_WITH_HEAMOPTYSIS,
			RUNNY_NOSE,
			DIFFICULTY_BREATHING,
			CHEST_PAIN,
			CONJUNCTIVITIS,
			EYE_PAIN_LIGHT_SENSITIVE,
			KOPLIKS_SPOTS,
			THROBOCYTOPENIA,
			OTITIS_MEDIA,
			HEARINGLOSS,
			DEHYDRATION,
			ANOREXIA_APPETITE_LOSS,
			REFUSAL_FEEDOR_DRINK,
			JOINT_PAIN,
			HICCUPS,
			BACKACHE,
			JAUNDICE,
			DARK_URINE,
			RAPID_BREATHING,
			SWOLLEN_GLANDS,
			UNEXPLAINED_BLEEDING,
			OTHER_NON_HEMORRHAGIC_SYMPTOMS,
			LESIONS,
			LYMPHADENOPATHY,
			LYMPHADENOPATHY_AXILLARY,
			LYMPHADENOPATHY_CERVICAL,
			LYMPHADENOPATHY_INGUINAL,
			CHILLS_SWEATS,
			BEDRIDDEN,
			ORAL_ULCERS,
			PAINFUL_LYMPHADENITIS,
			BLACKENING_DEATH_OF_TISSUE,
			BUBOES_GROIN_ARMPIT_NECK,
			BULGING_FONTANELLE,
			PHARYNGEAL_ERYTHEMA,
			PHARYNGEAL_EXUDATE,
			OEDEMA_FACE_NECK,
			OEDEMA_LOWER_EXTREMITY,
			LOSS_SKIN_TURGOR,
			PALPABLE_LIVER,
			PALPABLE_SPLEEN,
			MALAISE,
			SUNKEN_EYES_FONTANELLE,
			SIDE_PAIN,
			FLUID_IN_LUNG_CAVITY,
			FLUID_IN_LUNG_CAVITY_AUSCULTATION,
			DYSPNEA,
			TACHYPNEA,
			FLUID_IN_LUNG_CAVITY_XRAY,
			TREMOR,
			BILATERAL_CATARACTS,
			UNILATERAL_CATARACTS,
			CONGENITAL_GLAUCOMA,
			CONGENITAL_HEART_DISEASE,
			RADIOLUCENT_BONE_DISEASE,
			SPLENOMEGALY,
			MICROCEPHALY,
			MENINGOENCEPHALITIS,
			DEVELOPMENTAL_DELAY,
			PURPURIC_RASH,
			PIGMENTARY_RETINOPATHY,
			CONVULSION,
			AEROPHOBIA,
			AGITATION,
			ANXIETY_STATES,
			ASCENDING_FLACCID_PARALYSIS,
			COMA,
			DELIRIUM,
			DYSPHAGIA,
			ERRATIC_BEHAVIOUR,
			EXCESS_SALIVATION,
			EXCITATION,
			HYDROPHOBIA,
			HYPERACTIVITY,
			INSOMNIA,
			OPISTHOTONUS,
			PARALYSIS,
			PARASTHESIA_AROUND_WOUND,
			PARESIS,
			UPROARIOUSNESS,
			LOSS_OF_TASTE,
			LOSS_OF_SMELL,
			WHEEZING,
			SKIN_ULCERS,
			INABILITY_TO_WALK,
			IN_DRAWING_OF_CHEST_WALL,
			OTHER_COMPLICATIONS,
			FEELING_ILL,
			SHIVERING,
			RESPIRATORY_DISEASE_VENTILATION,
			FAST_HEART_RATE,
			OXYGEN_SATURATION_LOWER_94,
			FEVERISHFEELING,
			WEAKNESS,
			FATIGUE,
			COUGH_WITHOUT_SPUTUM,
			BREATHLESSNESS,
			CHEST_PRESSURE,
			BLUE_LIPS,
			BLOOD_CIRCULATION_PROBLEMS,
			PALPITATIONS,
			DIZZINESS_STANDING_UP,
			HIGH_OR_LOW_BLOOD_PRESSURE,
			URINARY_RETENTION,
			ALTERED_CONSCIOUSNESS,
			CONFUSED_DISORIENTED,
			HEMORRHAGIC_SYNDROME,
			HYPERGLYCEMIA,
			HYPOGLYCEMIA,
			MENINGEAL_SIGNS,
			SEIZURES,
			SEPSIS,
			SHOCK,
			GENERAL_BODILY_PAINS,
			NON_VASCULAR,
			BABY_DIED,
			BABY_NORMAL_AT_BIRTH,
			NORMAL_CRY_AND_SUCK,
			STOPPED_SUCKING_AFTER_TWO_DAYS,
			STIFFNESS,
			EMERGENCE_OF_GUINEA_WORM,
			FIRST_WORM_THIS_YEAR,
			CASE_DETECTED_BEFORE_WORM_EMERGENCE,
			DIARRHOEA,
			ABDOMINAL_CRAMPS,
			HEADACHES,
			GENERALIZED_RASH,
			HISTORY_OF_TRAVEL_OUTSIDE_THE_VILLAGE_TOWN_DISTRICT,
			RED_EYES,
			SWOLLEN_LYMPH_NODES_BEHIND_EARS,
			BODY_ACHE);

		Label symptomsHeadingLabel = new Label(I18nProperties.getString(Strings.headingSymptoms));
		symptomsHeadingLabel.addStyleName(H3);
		getContent().addComponent(symptomsHeadingLabel, MPOX_SYMPTOMS_HEADING_LOC);
		symptomsHeadingLabel.setVisible(false);

		Label rashHeadingLabel = new Label(I18nProperties.getString(Strings.headingRash));
		rashHeadingLabel.addStyleName(H3);
		getContent().addComponent(rashHeadingLabel, MPOX_RASH_HEADING_LOC);
		rashHeadingLabel.setVisible(false);

		tickSymptomField = addField(SymptomsDto.SYMPTOMS_SELECTED, OptionGroup.class);
		CssStyles.style(tickSymptomField, CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
		tickSymptomField.setMultiSelect(true);

		tickSymptomField.addItems(
				Arrays.stream(SymptomsList.MpoxList())
						.filter( c -> fieldVisibilityCheckers.isVisible(SymptomsList.class, c.name()))
						.collect(Collectors.toList()));

		tickSymptomField.setVisible(false);

		tickRashCharacteristicsField = addField(SymptomsDto.RASH_SYMPTOMS, OptionGroup.class);
		CssStyles.style(tickRashCharacteristicsField, CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
		tickRashCharacteristicsField.setMultiSelect(true);

		tickRashCharacteristicsField.addItems(
				Arrays.stream(MpoxRashArea.values())
						.filter( c -> fieldVisibilityCheckers.isVisible(MpoxRashArea.class, c.name()))
						.collect(Collectors.toList()));

		tickRashCharacteristicsField.setVisible(false);

		// Set visibilities

		NullableOptionGroup feverField = (NullableOptionGroup) getFieldGroup().getField(FEVER);
		feverField.setImmediate(true);

		FieldHelper.setVisibleWhen(getFieldGroup(), conditionalBleedingSymptomFieldIds, UNEXPLAINED_BLEEDING, Arrays.asList(SymptomState.YES), true);

		FieldHelper
			.setVisibleWhen(getFieldGroup(), OTHER_HEMORRHAGIC_SYMPTOMS_TEXT, OTHER_HEMORRHAGIC_SYMPTOMS, Arrays.asList(SymptomState.YES), true);

		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			OTHER_NON_HEMORRHAGIC_SYMPTOMS_TEXT,
			OTHER_NON_HEMORRHAGIC_SYMPTOMS,
			Arrays.asList(SymptomState.YES),
			true);

		FieldHelper.setVisibleWhen(getFieldGroup(), OTHER_COMPLICATIONS_TEXT, OTHER_COMPLICATIONS, Arrays.asList(SymptomState.YES), true);

		FieldHelper.setVisibleWhen(getFieldGroup(), lesionsFieldIds, LESIONS_ALL_OVER_BODY, Arrays.asList(Boolean.TRUE), true);

		FieldHelper.setVisibleWhen(getFieldGroup(), lesionsTypeIds, RASHES, Arrays.asList(SymptomState.YES), true);

		if (disease != Disease.MEASLES) {
			FieldHelper.setVisibleWhen(getFieldGroup(), lesionsLocationFieldIds, RASHES, Arrays.asList(SymptomState.YES), true);
		} else {
			FieldHelper.setVisibleWhen(getFieldGroup(), LESIONS_ONSET_DATE, GENERALIZED_RASH, Arrays.asList(SymptomState.YES), true);
		}

		FieldHelper.setVisibleWhen(getFieldGroup(), CONGENITAL_HEART_DISEASE_TYPE, CONGENITAL_HEART_DISEASE, Arrays.asList(SymptomState.YES), true);
		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			CONGENITAL_HEART_DISEASE_DETAILS,
			CONGENITAL_HEART_DISEASE_TYPE,
			Arrays.asList(CongenitalHeartDiseaseType.OTHER),
			true);
		if (isVisibleAllowed(getFieldGroup().getField(JAUNDICE_WITHIN_24_HOURS_OF_BIRTH))) {
			FieldHelper.setVisibleWhen(getFieldGroup(), JAUNDICE_WITHIN_24_HOURS_OF_BIRTH, JAUNDICE, Arrays.asList(SymptomState.YES), true);
		}

		FieldHelper.addSoftRequiredStyle(getField(LESIONS_ONSET_DATE));

		boolean isInfant = person != null
			&& person.getApproximateAge() != null
			&& ((person.getApproximateAge() <= 12 && person.getApproximateAgeType() == ApproximateAgeType.MONTHS) || person.getApproximateAge() <= 1);
		if (!isInfant) {
			getFieldGroup().getField(BULGING_FONTANELLE).setVisible(false);
		}

		// Handle visibility of lesions locations caption
		Label lesionsLocationsCaption = new Label(I18nProperties.getCaption(Captions.symptomsLesionsLocations));
		CssStyles.style(lesionsLocationsCaption, VSPACE_3);
		getContent().addComponent(lesionsLocationsCaption, LESIONS_LOCATIONS_LOC);
		getContent().getComponent(LESIONS_LOCATIONS_LOC)
			.setVisible(FieldHelper.getNullableSourceFieldValue(getFieldGroup().getField(LESIONS)) == SymptomState.YES);

		
		getFieldGroup().getField(LESIONS).addValueChangeListener(e -> {
			getContent().getComponent(LESIONS_LOCATIONS_LOC)
				.setVisible(FieldHelper.getNullableSourceFieldValue((Field) e.getProperty()) == SymptomState.YES);
		});

		/*getFieldGroup().getField(RASHES).addValueChangeListener(e -> {
			if (FieldHelper.getNullableSourceFieldValue(getFieldGroup().getField(RASHES)) == SymptomState.YES) {
				rashCharacteristicsSymptomsHeadingLabel.setVisible(true);
				rashTypeSymptomsHeadingLabel.setVisible(true);

			} else {
				rashCharacteristicsSymptomsHeadingLabel.setVisible(false);
				rashTypeSymptomsHeadingLabel.setVisible(false);
			}
		});*/

		// Symptoms hint text
		Label symptomsHint = new Label(
			I18nProperties.getString(symptomsContext == SymptomsContext.CASE ? Strings.messageSymptomsHint : Strings.messageSymptomsVisitHint),
			ContentMode.HTML);
		symptomsHint.setWidth(100, Unit.PERCENTAGE);
		getContent().addComponent(symptomsHint, SYMPTOMS_HINT_LOC);
		//turn off for IDSR
		symptomsHint.setVisible(false);

		setVisible(false, FEVER,
				ALTERED_CONSCIOUSNESS,
				SEIZURES,
				HEADACHE,
				NECK_STIFFNESS,
				INJECTION_SITE_BLEEDING);

		symptomsHide();
		setVisible(false, TEMPERATURE, TEMPERATURE_SOURCE);
		clinicalMeasurementsHeadingLabel.setVisible(false);

		if (disease == Disease.MONKEYPOX) {
			setUpMonkeypoxVisibilities();
		}

		if (disease == Disease.AFP) {
			NullableOptionGroup feverOnsetParalysis = addField(FEVER_ONSET_PARALYSIS, NullableOptionGroup.class);
			NullableOptionGroup progressiveParalysis = addField(PROGRESSIVE_PARALYSIS, NullableOptionGroup.class);
			DateField dateOnsetParalysis = addField(DATE_ONSET_PARALYSIS, DateField.class);
			NullableOptionGroup progressiveFlaccidAcute = addField(PROGRESSIVE_FLACID_ACUTE, NullableOptionGroup.class);
			NullableOptionGroup assymetric = addField(ASSYMETRIC, NullableOptionGroup.class);

			NullableOptionGroup siteParalysisBox = new NullableOptionGroup("Site of Paralysis");
			for (InjectionSite injectionSite : InjectionSite.ParalysisSite) {
				siteParalysisBox.addItem(injectionSite);
			}
			NullableOptionGroup siteOfParalysis = addField(SITE_OF_PARALYSIS, siteParalysisBox);
			OptionGroup paralysedLimbSensitiveToPain = addField(PARALYSED_LIMB_SENSITIVE_TO_PAIN, OptionGroup.class);
			OptionGroup injectionSiteBeforeOnsetParalysis = addField(INJECTION_SITE_BEFORE_ONSET_PARALYSIS, OptionGroup.class);

			NullableOptionGroup rightInjectionSiteBox = new NullableOptionGroup("Right Injection Site");
			for (InjectionSite injectionSiteRight : InjectionSite.InjectionSiteRight) {
				rightInjectionSiteBox.addItem(injectionSiteRight);
			}
			NullableOptionGroup rightInjectionSite = addField(RIGHT_INJECTION_SITE, rightInjectionSiteBox);

			NullableOptionGroup leftInjectionSiteBox = new NullableOptionGroup("Left Injection Site");
			for (InjectionSite injectionSiteLeft : InjectionSite.InjectionSiteLeft) {
				leftInjectionSiteBox.addItem(injectionSiteLeft);
			}
			NullableOptionGroup leftInjectionSite = addField(LEFT_INJECTION_SITE, leftInjectionSiteBox);
			OptionGroup trueAFP = addField(TRUEAFP, OptionGroup.class);
			TextArea provisionalDiagnosis = addField(PROVISONAL_DIAGNOSIS, TextArea.class);
			provisionalDiagnosis.setRows(4);

			addFields(
					MUSCLE_TONE,
					DEEP_TENDON_REFLEX,
					MUSCLE_VOLUME,
					SENSORY_LOSS);

			clinicalMeasurementsHeadingLabel.setVisible(false);
			setVisible(false, FEVER,
					ALTERED_CONSCIOUSNESS,
					SEIZURES,
					HEADACHE,
					NECK_STIFFNESS);

			symptomsHide();
			setVisible(true, OTHER_COMPLICATIONS, OTHER_COMPLICATIONS_TEXT);

		}
		if(disease == Disease.NEW_INFLUENZA){
			addField(DATE_OF_ONSET, DateField.class);
			OptionGroup feverBodytemp = addField(FEVER_BODY_TEMP_GREATER, OptionGroup.class);

			clinicalMeasurementsHeadingLabel.setVisible(false);
			setVisible(false, ONSET_DATE);
			symptomsHide();
			setVisible(false, FEVER, HEADACHE, ALTERED_CONSCIOUSNESS, CONVULSION, SEIZURES);
			setVisible(true, COUGH, SORE_THROAT, DIFFICULTY_BREATHING, OTHER_COMPLICATIONS, OTHER_COMPLICATIONS_TEXT, TEMPERATURE, TEMPERATURE_SOURCE);
		}

		if(disease == Disease.YELLOW_FEVER){
			setVisible(true, HEADACHE, BACKACHE, NAUSEA, VOMITING, FEVER, JAUNDICE, CHILLS_SWEATS, FATIGUE_WEAKNESS, FATIGUE, WEAKNESS, BODY_ACHE);
			setVisible(true, TEMPERATURE, TEMPERATURE_SOURCE, OTHER_COMPLICATIONS, OTHER_COMPLICATIONS_TEXT);
		}

		if(disease == Disease.CSM){
			symptomsHide();
			setVisible(true, BULGING_FONTANELLE, FEVER, NECK_STIFFNESS, HEADACHE, CONVULSION, ALTERED_CONSCIOUSNESS, OTHER_COMPLICATIONS, OTHER_COMPLICATIONS_TEXT);
			clinicalMeasurementsHeadingLabel.setVisible(false);
			setVisible(false, TEMPERATURE, TEMPERATURE_SOURCE);
			setVisible(false, ONSET_DATE);

		}
		if (disease == Disease.AHF || disease == Disease.DENGUE) {

			setVisible(false,
					SORE_THROAT, COUGH, COUGH_WITH_SPUTUM, COUGH_WITH_HEAMOPTYSIS, RUNNY_NOSE, CHEST_PAIN, CONJUNCTIVITIS, EYE_PAIN_LIGHT_SENSITIVE, KOPLIKS_SPOTS, THROBOCYTOPENIA, OTITIS_MEDIA, HEARINGLOSS, DEHYDRATION, REFUSAL_FEEDOR_DRINK, BACKACHE, JAUNDICE, DARK_URINE, STOMACH_BLEEDING, RAPID_BREATHING, SWOLLEN_GLANDS, UNEXPLAINED_BLEEDING, COUGHING_BLOOD, SKIN_BRUISING, BLOOD_URINE, OTHER_HEMORRHAGIC_SYMPTOMS, OTHER_HEMORRHAGIC_SYMPTOMS_TEXT, OTHER_NON_HEMORRHAGIC_SYMPTOMS, OTHER_NON_HEMORRHAGIC_SYMPTOMS_TEXT, LESIONS, LESIONS_THAT_ITCH, LESIONS_SAME_STATE, LESIONS_SAME_SIZE, LESIONS_DEEP_PROFOUND, LESIONS_FACE, LESIONS_LEGS, LESIONS_SOLES_FEET, LESIONS_PALMS_HANDS, LESIONS_THORAX, LESIONS_ARMS, LESIONS_GENITALS, LESIONS_ALL_OVER_BODY, LYMPHADENOPATHY, LYMPHADENOPATHY_AXILLARY, LYMPHADENOPATHY_CERVICAL, LYMPHADENOPATHY_INGUINAL, CHILLS_SWEATS, BEDRIDDEN, ORAL_ULCERS, PAINFUL_LYMPHADENITIS, BLACKENING_DEATH_OF_TISSUE, BUBOES_GROIN_ARMPIT_NECK, PHARYNGEAL_ERYTHEMA, PHARYNGEAL_EXUDATE, OEDEMA_FACE_NECK, OEDEMA_LOWER_EXTREMITY, LOSS_SKIN_TURGOR, PALPABLE_LIVER, PALPABLE_SPLEEN, MALAISE, SUNKEN_EYES_FONTANELLE, SIDE_PAIN, FLUID_IN_LUNG_CAVITY, TREMOR, BILATERAL_CATARACTS, UNILATERAL_CATARACTS, CONGENITAL_GLAUCOMA, CONGENITAL_HEART_DISEASE, PIGMENTARY_RETINOPATHY, RADIOLUCENT_BONE_DISEASE, SPLENOMEGALY, MICROCEPHALY, MENINGOENCEPHALITIS, DEVELOPMENTAL_DELAY, CONGENITAL_HEART_DISEASE_TYPE, CONGENITAL_HEART_DISEASE_DETAILS, JAUNDICE_WITHIN_24_HOURS_OF_BIRTH, PATIENT_ILL_LOCATION, HYDROPHOBIA, OPISTHOTONUS, ANXIETY_STATES, DELIRIUM, UPROARIOUSNESS, PARASTHESIA_AROUND_WOUND, EXCESS_SALIVATION, INSOMNIA, PARALYSIS, EXCITATION, DYSPHAGIA, AEROPHOBIA, HYPERACTIVITY, PARESIS, AGITATION, ASCENDING_FLACCID_PARALYSIS, ERRATIC_BEHAVIOUR, COMA, CONVULSION, FLUID_IN_LUNG_CAVITY_AUSCULTATION, FLUID_IN_LUNG_CAVITY_XRAY, ABNORMAL_LUNG_XRAY_FINDINGS, CONJUNCTIVAL_INJECTION, ACUTE_RESPIRATORY_DISTRESS_SYNDROME, PNEUMONIA_CLINICAL_OR_RADIOLOGIC, LOSS_OF_TASTE, LOSS_OF_SMELL, WHEEZING, SKIN_ULCERS, INABILITY_TO_WALK, IN_DRAWING_OF_CHEST_WALL, FEELING_ILL, SHIVERING, RESPIRATORY_DISEASE_VENTILATION, FAST_HEART_RATE, OXYGEN_SATURATION_LOWER_94, WEAKNESS, COUGH_WITHOUT_SPUTUM, CHEST_PRESSURE, BLUE_LIPS, BLOOD_CIRCULATION_PROBLEMS, PALPITATIONS, DIZZINESS_STANDING_UP, HIGH_OR_LOW_BLOOD_PRESSURE, URINARY_RETENTION, CONFUSED_DISORIENTED, HEMORRHAGIC_SYNDROME, HYPERGLYCEMIA, HYPOGLYCEMIA, MENINGEAL_SIGNS, SEPSIS, SHOCK
			);
			clinicalMeasurementsHeadingLabel.setVisible(false);
			setVisible(true, FEVER, HEADACHE, SKIN_RASH, VOMITING, NAUSEA, INJECTION_SITE_BLEEDING, ANOREXIA_APPETITE_LOSS, GUMS_BLEEDING, DIARRHEA, EYES_BLEEDING, FATIGUE, BLOOD_IN_STOOL, ABDOMINAL_PAIN, RED_BLOOD_VOMIT, JOINT_PAIN, NOSE_BLEEDING, BLEEDING_VAGINA, HICCUPS, BREATHLESSNESS);
		}

		if(disease == Disease.FOODBORNE_ILLNESS){
			setVisible(true,
					ABDOMINAL_PAIN,	BLOOD_IN_STOOL, CHILLS_SWEATS, CONVULSION, DEHYDRATION, DIARRHEA, DIZZINESS, FEVER, EXCESSIVE_SWEATING, HEADACHE, JAUNDICE, MUSCLE_PAIN, NAUSEA, NUMBNESS, VOMITING, WEAKNESS, OTHER_COMPLICATIONS, OTHER_COMPLICATIONS_TEXT, OTHER_SIGNS_AND_SYMPTOMS_HEADING_LOC);

			symptomsOngoing.setVisible(true);
			durationHours.setVisible(true);
			nameOfHealthFacility.setVisible(true);

			FieldHelper
					.setVisibleWhen(symptomsOngoing, Arrays.asList(nameOfHealthFacility), Arrays.asList(YesNo.YES), true);
			FieldHelper
					.setVisibleWhen(symptomsOngoing, Arrays.asList(durationHours), Arrays.asList(YesNo.NO), true);

			foodHistoryHeadingLabel.setVisible(true);

		}

		if(disease == Disease.MONKEYPOX){
			setVisible(false, SYMPTOMS_COMMENTS, GENERAL_BODILY_PAINS);

			symptomsHeadingLabel.setVisible(true);
			rashHeadingLabel.setVisible(true);
			tickSymptomField.setVisible(true);
			onsetSymptom.setVisible(true);
//			setVisible(true, OTHER_COMPLICATIONS, OTHER_COMPLICATIONS_TEXT);

			addField(SYMPTOMS_SELECTED_OTHER, TextField.class);

			tickRashCharacteristicsField.setVisible(true);
			addField(DATE_OF_ONSET_RASH, DateField.class);

			addField(RASH_SYMPTOMS_OTHER_AREAS, TextField.class);
			addField(ARE_LESIONS_IN_SAME_STATE, NullableOptionGroup.class);
			addField(ARE_LESIONS_SAME_SIZE, NullableOptionGroup.class);
			addField(ARE_LESIONS_DEEP, NullableOptionGroup.class);
			addField(ARE_ULCERS_AMONG_LESIONS, NullableOptionGroup.class);

			ComboBox typeOfRash = new ComboBox("Rash");
			for (SymptomsList list : SymptomsList.MpoxRashList()) {
				typeOfRash.addItem(list);
			}

			addField(TYPE_OF_RASH, typeOfRash);
		}
		
		if (disease == Disease.CORONAVIRUS) {
			symptomsHide();
			setVisible(true, FEVER, RAPID_BREATHING, MUSCLE_PAIN, CHEST_PAIN, ABDOMINAL_PAIN, JOINT_PAIN, FATIGUE_WEAKNESS, DIARRHEA, COUGH, NAUSEA,
					SORE_THROAT, HEADACHE, RUNNY_NOSE, HEADACHE, CONFUSED_DISORIENTED, OTHER_COMPLICATIONS, PHARYNGEAL_EXUDATE, COMA, ABNORMAL_LUNG_XRAY_FINDINGS,
					CONJUNCTIVAL_INJECTION, SEIZURES, FLUID_IN_LUNG_CAVITY_AUSCULTATION, DIFFICULTY_BREATHING, TACHYPNEA);

		} else if (disease == Disease.NEONATAL_TETANUS) {
			symptomsHide();
			setVisible(true,
					SYMPTOMS_COMMENTS,
					ONSET_DATE,
					AGE_AT_DEATH_DAYS,
					AGE_AT_ONSET_DAYS,
					SEIZURES,
					OTHER_COMPLICATIONS,
					BABY_DIED,
					BABY_NORMAL_AT_BIRTH,
					NORMAL_CRY_AND_SUCK,
					STOPPED_SUCKING_AFTER_TWO_DAYS,
					STIFFNESS,
					BACKACHE,
					OTHER_COMPLICATIONS);
			FieldHelper.setVisibleWhen(getFieldGroup(), Arrays.asList(AGE_AT_DEATH_DAYS, AGE_AT_ONSET_DAYS), BABY_DIED, Arrays.asList(SymptomState.YES), true);

		}

		if(disease == Disease.MEASLES) {
			addField(DATE_OF_ONSET, DateField.class);
			setVisible(true, FEVER, GENERALIZED_RASH, COUGH, RUNNY_NOSE, RED_EYES, SWOLLEN_LYMPH_NODES_BEHIND_EARS, JOINT_PAIN, PLACE_OF_EXPOSURE_MEASLES_RUBELLA);
			setVisible(false, SYMPTOMS_COMMENTS);
		} else if(disease == Disease.CHOLERA) {
			setVisible(true, DIARRHOEA, VOMITING, DEHYDRATION, ABDOMINAL_PAIN, ABDOMINAL_CRAMPS, FEVER, HEADACHES, FATIGUE);
		}else if (disease == Disease.GUINEA_WORM) {
			addField(FIRST_SIGN_OR_SYMPTOMS_BEFORE_WORM, NullableOptionGroup.class);
			addField(FIRST_SIGN_OR_SYMPTOMS_BEFORE_WORM_OTHERS, TextField.class);
			FieldHelper.setVisibleWhen(getFieldGroup(), FIRST_SIGN_OR_SYMPTOMS_BEFORE_WORM_OTHERS, FIRST_SIGN_OR_SYMPTOMS_BEFORE_WORM, Arrays.asList(GuineaWormFirstSymptom.OTHERS), true);

			addField(NUMBER_OF_WORMS, TextField.class);
			FieldHelper.setVisibleWhen(getFieldGroup(), NUMBER_OF_WORMS, EMERGENCE_OF_GUINEA_WORM, Arrays.asList(SymptomState.YES), true);

			addField(DATE_FIRST_WORM_EMERGENCE, DateField.class);
		}

		if (symptomsContext != SymptomsContext.CASE) {
			getFieldGroup().getField(PATIENT_ILL_LOCATION).setVisible(false);
		}

		/*symptomGroupMap.forEach((location, strings) -> {
			final Component groupLabel = getContent().getComponent(location);
			final Optional<String> groupHasVisibleSymptom =
				strings.stream().filter(s -> getFieldGroup().getField(s) != null && getFieldGroup().getField(s).isVisible()).findAny();
			if (!groupHasVisibleSymptom.isPresent()) {
				groupLabel.setVisible(false);
			}
		});*/

		if (isEditableAllowed(OTHER_HEMORRHAGIC_SYMPTOMS_TEXT)) {
			FieldHelper.setRequiredWhen(
				getFieldGroup(),
				getFieldGroup().getField(OTHER_HEMORRHAGIC_SYMPTOMS),
				Arrays.asList(OTHER_HEMORRHAGIC_SYMPTOMS_TEXT),
				Arrays.asList(SymptomState.YES),
				disease);
		}
		if (isEditableAllowed(OTHER_NON_HEMORRHAGIC_SYMPTOMS_TEXT)) {
			FieldHelper.setRequiredWhen(
				getFieldGroup(),
				getFieldGroup().getField(OTHER_NON_HEMORRHAGIC_SYMPTOMS),
				Arrays.asList(OTHER_NON_HEMORRHAGIC_SYMPTOMS_TEXT),
				Arrays.asList(SymptomState.YES),
				disease);
		}
		if (isEditableAllowed(OTHER_COMPLICATIONS_TEXT)) {
			FieldHelper.setRequiredWhen(
				getFieldGroup(),
				getFieldGroup().getField(OTHER_COMPLICATIONS),
				Arrays.asList(OTHER_COMPLICATIONS_TEXT),
				Arrays.asList(SymptomState.YES),
				disease);
		}

		FieldHelper
			.setRequiredWhen(getFieldGroup(), getFieldGroup().getField(LESIONS_ALL_OVER_BODY), lesionsFieldIds, Arrays.asList(Boolean.TRUE), disease);

		addListenerForOnsetFields(onsetSymptom, onsetDateField);

		clearAllButton = ButtonHelper.createButton(Captions.actionClearAll, event -> {
			for (Object symptomId : unconditionalSymptomFieldIds) {
				getFieldGroup().getField(symptomId).setValue(null);
			}
			for (Object symptomId : conditionalBleedingSymptomFieldIds) {
				getFieldGroup().getField(symptomId).setValue(null);
			}
			for (Object symptomId : lesionsFieldIds) {
				getFieldGroup().getField(symptomId).setValue(null);
			}
			for (Object symptomId : lesionsLocationFieldIds) {
				getFieldGroup().getField(symptomId).setValue(null);
			}
			for (Object symptomId : monkeypoxImageFieldIds) {
				getFieldGroup().getField(symptomId).setValue(null);
			}
		}, ValoTheme.BUTTON_LINK);

		setEmptyToNoButton = createButtonSetClearedToSymptomState(Captions.symptomsSetClearedToNo, SymptomState.NO);
		clearAllButton.setVisible(false);
		setEmptyToNoButton.setVisible(false);

		//Button setEmptyToUnknownButton = createButtonSetClearedToSymptomState(Captions.symptomsSetClearedToUnknown, SymptomState.UNKNOWN);

		Set<Disease> includedDiseases = new HashSet<>(Arrays.asList(Disease.YELLOW_FEVER, Disease.AHF, Disease.DENGUE, Disease.CSM, Disease.AFP, Disease.NEW_INFLUENZA, Disease.SARI));

		if (includedDiseases.contains(disease)) {
			clearAllButton.setVisible(true);
			setEmptyToNoButton.setVisible(true);
			symptomsHint.setVisible(true);
		}

		Label complicationsHeading = new Label(I18nProperties.getString(Strings.headingComplications));
		CssStyles.style(complicationsHeading, CssStyles.H3);

		if (disease != Disease.CONGENITAL_RUBELLA && disease != Disease.MONKEYPOX && !isConfiguredServer("de")) {
			getContent().addComponent(complicationsHeading, COMPLICATIONS_HEADING);
		}

		getContent().addComponent(complicationsHeading, COMPLICATIONS_HEADING);

		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.addComponent(clearAllButton);
		buttonsLayout.addComponent(setEmptyToNoButton);
		//buttonsLayout.addComponent(setEmptyToUnknownButton);
		buttonsLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		buttonsLayout.setMargin(new MarginInfo(true, true, true, false));

		getContent().addComponent(buttonsLayout, BUTTONS_LOC);

		if (feverField.isVisible()) {
			temperature.addValueChangeListener(e -> {
				toggleFeverComponentError(feverField, temperature);
			});
			feverField.addValueChangeListener(e -> {
				toggleFeverComponentError(feverField, temperature);
			});
		}

		boolean isComplicationsHeadingVisible = false;
		for (String complicationField : complicationsFieldIds) {
			if (getFieldGroup().getField(complicationField).isVisible()) {
				isComplicationsHeadingVisible = true;
			}
		}
		complicationsHeading.setVisible(isComplicationsHeadingVisible);
	}

	private void symptomsHide() {
		setVisible(false,
				VOMITING,
				BODY_ACHE,
				DIARRHEA,
				BLOOD_IN_STOOL,
				NAUSEA,
				ABDOMINAL_PAIN,
				MUSCLE_PAIN,
				FATIGUE_WEAKNESS,
				SKIN_RASH,
				SORE_THROAT,
				COUGH,
				COUGH_WITH_SPUTUM,
				COUGH_WITH_HEAMOPTYSIS,
				RUNNY_NOSE,
				DIFFICULTY_BREATHING,
				CHEST_PAIN,
				CONJUNCTIVITIS,
				EYE_PAIN_LIGHT_SENSITIVE,
				KOPLIKS_SPOTS,
				THROBOCYTOPENIA,
				OTITIS_MEDIA,
				HEARINGLOSS,
				DEHYDRATION,
				ANOREXIA_APPETITE_LOSS,
				REFUSAL_FEEDOR_DRINK,
				JOINT_PAIN,
				HICCUPS,
				BACKACHE,
				EYES_BLEEDING,
				JAUNDICE,
				DARK_URINE,
				STOMACH_BLEEDING,
				RAPID_BREATHING,
				SWOLLEN_GLANDS,
				UNEXPLAINED_BLEEDING,
				GUMS_BLEEDING,
				NOSE_BLEEDING,
				BLOODY_BLACK_STOOL,
				RED_BLOOD_VOMIT,
				DIGESTED_BLOOD_VOMIT,
				COUGHING_BLOOD,
				BLEEDING_VAGINA,
				SKIN_BRUISING,
				BLOOD_URINE,
				OTHER_HEMORRHAGIC_SYMPTOMS,
				OTHER_HEMORRHAGIC_SYMPTOMS_TEXT,
				OTHER_NON_HEMORRHAGIC_SYMPTOMS,
				OTHER_NON_HEMORRHAGIC_SYMPTOMS_TEXT,
				LESIONS,
				LESIONS_THAT_ITCH,
				LESIONS_SAME_STATE,
				LESIONS_SAME_SIZE,
				LESIONS_DEEP_PROFOUND,
				LESIONS_FACE,
				LESIONS_LEGS,
				LESIONS_SOLES_FEET,
				LESIONS_PALMS_HANDS,
				LESIONS_THORAX,
				LESIONS_ARMS,
				LESIONS_GENITALS,
				LESIONS_ALL_OVER_BODY,
				LYMPHADENOPATHY,
				LYMPHADENOPATHY_AXILLARY,
				LYMPHADENOPATHY_CERVICAL,
				LYMPHADENOPATHY_INGUINAL,
				CHILLS_SWEATS,
				BEDRIDDEN,
				ORAL_ULCERS,
				PAINFUL_LYMPHADENITIS,
				BLACKENING_DEATH_OF_TISSUE,
				BUBOES_GROIN_ARMPIT_NECK,
				PHARYNGEAL_ERYTHEMA,
				PHARYNGEAL_EXUDATE,
				OEDEMA_FACE_NECK,
				OEDEMA_LOWER_EXTREMITY,
				LOSS_SKIN_TURGOR,
				PALPABLE_LIVER,
				PALPABLE_SPLEEN,
				MALAISE,
				SUNKEN_EYES_FONTANELLE,
				SIDE_PAIN,
				FLUID_IN_LUNG_CAVITY,
				TREMOR,
				BILATERAL_CATARACTS,
				UNILATERAL_CATARACTS,
				CONGENITAL_GLAUCOMA,
				CONGENITAL_HEART_DISEASE,
				PIGMENTARY_RETINOPATHY,
				RADIOLUCENT_BONE_DISEASE,
				SPLENOMEGALY,
				MICROCEPHALY,
				MENINGOENCEPHALITIS,
				PURPURIC_RASH,
				DEVELOPMENTAL_DELAY,
				CONGENITAL_HEART_DISEASE_TYPE,
				CONGENITAL_HEART_DISEASE_DETAILS,
				JAUNDICE_WITHIN_24_HOURS_OF_BIRTH,
				PATIENT_ILL_LOCATION,
				HYDROPHOBIA,
				OPISTHOTONUS,
				ANXIETY_STATES,
				DELIRIUM,
				UPROARIOUSNESS,
				PARASTHESIA_AROUND_WOUND,
				EXCESS_SALIVATION,
				INSOMNIA,
				PARALYSIS,
				EXCITATION,
				DYSPHAGIA,
				AEROPHOBIA,
				HYPERACTIVITY,
				PARESIS,
				AGITATION,
				ASCENDING_FLACCID_PARALYSIS,
				ERRATIC_BEHAVIOUR,
				COMA,
				CONVULSION,
				FLUID_IN_LUNG_CAVITY_AUSCULTATION,
				FLUID_IN_LUNG_CAVITY_XRAY,
				ABNORMAL_LUNG_XRAY_FINDINGS,
				CONJUNCTIVAL_INJECTION,
				ACUTE_RESPIRATORY_DISTRESS_SYNDROME,
				PNEUMONIA_CLINICAL_OR_RADIOLOGIC,
				LOSS_OF_TASTE,
				LOSS_OF_SMELL,
				WHEEZING,
				SKIN_ULCERS,
				INABILITY_TO_WALK,
				IN_DRAWING_OF_CHEST_WALL,
				FEELING_ILL,
				SHIVERING,
				RESPIRATORY_DISEASE_VENTILATION,
				FAST_HEART_RATE,
				OXYGEN_SATURATION_LOWER_94,
				FEVERISHFEELING,
				WEAKNESS,
				FATIGUE,
				COUGH_WITHOUT_SPUTUM,
				BREATHLESSNESS,
				CHEST_PRESSURE,
				BLUE_LIPS,
				BLOOD_CIRCULATION_PROBLEMS,
				PALPITATIONS,
				DIZZINESS_STANDING_UP,
				HIGH_OR_LOW_BLOOD_PRESSURE,
				URINARY_RETENTION,
				CONFUSED_DISORIENTED,
				OTHER_COMPLICATIONS,
				OTHER_COMPLICATIONS_TEXT,
				HEMORRHAGIC_SYNDROME,
				HYPERGLYCEMIA,
				HYPOGLYCEMIA,
				MENINGEAL_SIGNS,
				SEPSIS,
				SHOCK,
				LESIONS_ONSET_DATE,
				NON_VASCULAR,
				BABY_DIED,
				BABY_NORMAL_AT_BIRTH,
				NORMAL_CRY_AND_SUCK,
				STOPPED_SUCKING_AFTER_TWO_DAYS,
				STIFFNESS,
				SHOCK, DIZZINESS,
				EXCESSIVE_SWEATING,
				NUMBNESS,
				DIARRHOEA,
				ABDOMINAL_CRAMPS,
				HEADACHES);
	}

	private void toggleFeverComponentError(NullableOptionGroup feverField, ComboBox temperatureField) {
		Float temperatureValue = (Float) temperatureField.getValue();
		SymptomState feverValue = (SymptomState) feverField.getNullableValue();

		if (temperatureValue != null && temperatureValue >= 38.0f && feverValue != SymptomState.YES) {
			setFeverComponentError(feverField, true);
		} else if (temperatureValue != null && temperatureValue < 38.0f && feverValue != SymptomState.NO) {
			setFeverComponentError(feverField, false);
		} else {
			feverField.setComponentError(null);
		}
	}

	private void setFeverComponentError(NullableOptionGroup feverField, boolean feverSuggested) {
		feverField.setComponentError(new ErrorMessage() {

			@Override
			public ErrorLevel getErrorLevel() {
				return ErrorLevel.INFO;
			}

			@Override
			public String getFormattedHtmlMessage() {
				return I18nProperties.getValidationError(
					feverSuggested ? Validations.feverTemperatureAboveThreshold : Validations.feverTemperatureBelowThreshold,
					I18nProperties.getPrefixCaption(SymptomsDto.I18N_PREFIX, FEVER));
			}
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

	public void initializeSymptomRequirementsForVisit(NullableOptionGroup visitStatus) {
		FieldHelper.addSoftRequiredStyleWhen(
			getFieldGroup(),
			visitStatus,
			Arrays.asList(TEMPERATURE, TEMPERATURE_SOURCE),
			Arrays.asList(VisitStatus.COOPERATIVE),
			disease);
		addSoftRequiredStyleWhenSymptomaticAndCooperative(
			getFieldGroup(),
			ONSET_DATE,
			unconditionalSymptomFieldIds,
			Arrays.asList(SymptomState.YES),
			visitStatus);
		addSoftRequiredStyleWhenSymptomaticAndCooperative(
			getFieldGroup(),
			ONSET_SYMPTOM,
			unconditionalSymptomFieldIds,
			Arrays.asList(SymptomState.YES),
			visitStatus);
	}

	@Override
	public void setValue(SymptomsDto newFieldValue) throws ReadOnlyException, ConversionException {
		super.setValue(newFieldValue);

		initializeSymptomRequirementsForCase();
	}

	private void initializeSymptomRequirementsForCase() {
		addSoftRequiredStyleWhenSymptomaticAndCooperative(
			getFieldGroup(),
			ONSET_DATE,
			unconditionalSymptomFieldIds,
			Arrays.asList(SymptomState.YES),
			null);
		addSoftRequiredStyleWhenSymptomaticAndCooperative(
			getFieldGroup(),
			ONSET_SYMPTOM,
			unconditionalSymptomFieldIds,
			Arrays.asList(SymptomState.YES),
			null);
		addSoftRequiredStyleWhenSymptomaticAndCooperative(
			getFieldGroup(),
			PATIENT_ILL_LOCATION,
			unconditionalSymptomFieldIds,
			Arrays.asList(SymptomState.YES),
			null);
	}

	/**
	 * Sets the fields defined by the ids contained in sourceValues to required when the person is symptomatic
	 * and - if a visit is processed - cooperative. When this method is called from within a case, it needs to
	 * be called with visitStatusField set to null in order to ignore the visit status requirement.
	 */
	@SuppressWarnings("rawtypes")
	private void addSoftRequiredStyleWhenSymptomaticAndCooperative(
		FieldGroup fieldGroup,
		Object targetPropertyId,
		List<String> sourcePropertyIds,
		List<Object> sourceValues,
		NullableOptionGroup visitStatusField) {

		for (Object sourcePropertyId : sourcePropertyIds) {
			Field sourceField = fieldGroup.getField(sourcePropertyId);
			if (sourceField instanceof AbstractField<?>) {
				((AbstractField) sourceField).setImmediate(true);
			}
		}

		// Initialize
		final Field targetField = fieldGroup.getField(targetPropertyId);
		if (!targetField.isVisible()) {
			return;
		}

		if (visitStatusField != null) {
			if (isAnySymptomSetToYes(fieldGroup, sourcePropertyIds, sourceValues) && visitStatusField.getNullableValue() == VisitStatus.COOPERATIVE) {
				FieldHelper.addSoftRequiredStyle(targetField);
			} else {
				FieldHelper.removeSoftRequiredStyle(targetField);
			}
		} else {
			if (isAnySymptomSetToYes(fieldGroup, sourcePropertyIds, sourceValues)) {
				FieldHelper.addSoftRequiredStyle(targetField);
			} else {
				FieldHelper.removeSoftRequiredStyle(targetField);
			}
		}

		// Add listeners
		for (Object sourcePropertyId : sourcePropertyIds) {
			Field sourceField = fieldGroup.getField(sourcePropertyId);
			sourceField.addValueChangeListener(event -> {
				if (visitStatusField != null) {
					if (isAnySymptomSetToYes(fieldGroup, sourcePropertyIds, sourceValues) && visitStatusField.getValue() == VisitStatus.COOPERATIVE) {
						FieldHelper.addSoftRequiredStyle(targetField);
					} else {
						FieldHelper.removeSoftRequiredStyle(targetField);
					}
				} else {
					if (isAnySymptomSetToYes(fieldGroup, sourcePropertyIds, sourceValues)) {
						FieldHelper.addSoftRequiredStyle(targetField);
					} else {
						FieldHelper.removeSoftRequiredStyle(targetField);
					}
				}
			});
		}

		if (visitStatusField != null) {
			visitStatusField.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(com.vaadin.v7.data.Property.ValueChangeEvent event) {
					if (isAnySymptomSetToYes(fieldGroup, sourcePropertyIds, sourceValues) && visitStatusField.getValue() == VisitStatus.COOPERATIVE) {
						FieldHelper.addSoftRequiredStyle(targetField);
					} else {
						FieldHelper.removeSoftRequiredStyle(targetField);
					}
				}
			});
		}
	}

	/**
	 * Returns true if the value of any field associated with the sourcePropertyIds
	 * is set to one of the values contained in sourceValues.
	 *
	 * @param fieldGroup
	 * @param sourcePropertyIds
	 * @param sourceValues
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean isAnySymptomSetToYes(FieldGroup fieldGroup, List<String> sourcePropertyIds, List<Object> sourceValues) {

		for (Object sourcePropertyId : sourcePropertyIds) {
			Field sourceField = fieldGroup.getField(sourcePropertyId);
			if (sourceValues.contains(FieldHelper.getNullableSourceFieldValue(sourceField))) {
				return true;
			}
		}

		return false;
	}

	@SuppressWarnings("rawtypes")
	private void addListenerForOnsetFields(ComboBox onsetSymptom, DateField onsetDateField) {
		List<String> allPropertyIds =
			Stream.concat(unconditionalSymptomFieldIds.stream(), conditionalBleedingSymptomFieldIds.stream()).collect(Collectors.toList());
		allPropertyIds.add(LESIONS_THAT_ITCH);

		for (Object sourcePropertyId : allPropertyIds) {
			Field sourceField = getFieldGroup().getField(sourcePropertyId);
			sourceField.addValueChangeListener(event -> {
				if (FieldHelper.getNullableSourceFieldValue(sourceField) == SymptomState.YES) {
					onsetSymptom.addItem(sourceField.getCaption());
					onsetDateField.setEnabled(true);
				} else {
					onsetSymptom.removeItem(sourceField.getCaption());
					boolean isOnsetDateFieldEnabled = isAnySymptomSetToYes(getFieldGroup(), allPropertyIds, Arrays.asList(SymptomState.YES));
					//onsetDateField.setEnabled(isOnsetDateFieldEnabled);
					onsetDateField.setEnabled(true);
					Date onsetDate = getValue().getOnsetDate();
					if (onsetDate != null) {
						onsetDateField.setValue(onsetDate);
					} else if (!isOnsetDateFieldEnabled) {
						onsetDateField.setValue(null);
					}
				}
				onsetSymptom.setVisible(!onsetSymptom.getItemIds().isEmpty());
			});
		}
		onsetSymptom.setEnabled(true); // will be updated by listener if needed
		onsetDateField.setEnabled(true); // will be updated by listener if needed
	}


	private void setUpMonkeypoxVisibilities() {
		// Monkeypox picture resemblance fields
		FieldHelper.setVisibleWhen(getFieldGroup(), monkeypoxImageFieldIds, RASHES, Arrays.asList(SymptomState.YES), true);

		// Set up images
		Image lesionsImg1 = new Image(null, new ThemeResource("img/monkeypox-lesions-1.png"));
		CssStyles.style(lesionsImg1, VSPACE_3);
		Image lesionsImg2 = new Image(null, new ThemeResource("img/monkeypox-lesions-2.png"));
		CssStyles.style(lesionsImg2, VSPACE_3);
		Image lesionsImg3 = new Image(null, new ThemeResource("img/monkeypox-lesions-3.png"));
		CssStyles.style(lesionsImg3, VSPACE_3);
		Image lesionsImg4 = new Image(null, new ThemeResource("img/monkeypox-lesions-4.png"));
		CssStyles.style(lesionsImg4, VSPACE_3);
		VerticalLayout hl = new VerticalLayout();

		hl.addComponents(getContent().getComponent(LESIONS_RESEMBLE_IMG1), lesionsImg1);
		getContent().addComponent(hl, LESIONS_RESEMBLE_IMG1);

		VerticalLayout hl1 = new VerticalLayout();

		hl1.addComponents(getContent().getComponent(LESIONS_RESEMBLE_IMG2), lesionsImg2);
		getContent().addComponent(hl1, LESIONS_RESEMBLE_IMG2);

		VerticalLayout hl3 = new VerticalLayout();

		hl3.addComponents(getContent().getComponent(LESIONS_RESEMBLE_IMG3), lesionsImg3);
		getContent().addComponent(hl3, LESIONS_RESEMBLE_IMG3);

		VerticalLayout hl4 = new VerticalLayout();

		hl4.addComponents(getContent().getComponent(LESIONS_RESEMBLE_IMG4), lesionsImg4);
		getContent().addComponent(hl4, LESIONS_RESEMBLE_IMG4);

		//List<String> monkeypoxImages = Arrays.asList(MONKEYPOX_LESIONS_IMG11, MONKEYPOX_LESIONS_IMG2, MONKEYPOX_LESIONS_IMG3, MONKEYPOX_LESIONS_IMG4);
		List<String> monkeypoxImages = Arrays.asList(LESIONS_RESEMBLE_IMG1, LESIONS_RESEMBLE_IMG2, LESIONS_RESEMBLE_IMG3, LESIONS_RESEMBLE_IMG4);

		// Set up initial visibility
		boolean lesionsSetToYes = FieldHelper.getNullableSourceFieldValue(getFieldGroup().getField(RASHES)) == SymptomState.YES;
		for (String monkeypoxImage : monkeypoxImages) {
			getContent().getComponent(monkeypoxImage).setVisible(lesionsSetToYes);
		}
		
		

		// Set up image visibility listener
		getFieldGroup().getField(RASHES).addValueChangeListener(e -> {
			for (String monkeypoxImage : monkeypoxImages) {
				getContent().getComponent(monkeypoxImage)
					.setVisible(FieldHelper.getNullableSourceFieldValue((Field) e.getProperty()) == SymptomState.YES);
			}
		});
	}

	public List<String> getUnconditionalSymptomFieldIds() {
		return unconditionalSymptomFieldIds;
	}

	public Button createButtonSetClearedToSymptomState(String caption, SymptomState symptomState) {

		Button button = ButtonHelper.createButton(caption, event -> {
			for (Object symptomId : unconditionalSymptomFieldIds) {
				Field<Object> symptom = (Field<Object>) getFieldGroup().getField(symptomId);
				if (symptom.isVisible() && (Set.class.isAssignableFrom(symptom.getValue().getClass()) && ((Set) symptom.getValue()).size() == 0)) {
					Set<SymptomState> value = (Set<SymptomState>) symptom.getValue();
					value.add(symptomState);
					symptom.setValue(value);
				}
			}
			for (Object symptomId : conditionalBleedingSymptomFieldIds) {
				Field<Object> symptom = (Field<Object>) getFieldGroup().getField(symptomId);
				if (symptom.isVisible() && (Set.class.isAssignableFrom(symptom.getValue().getClass()) && ((Set) symptom.getValue()).size() == 0)) {
					Set<SymptomState> value = (Set<SymptomState>) symptom.getValue();
					value.add(symptomState);
					symptom.setValue(value);
				}
			}
			for (Object symptomId : lesionsFieldIds) {
				Field<Object> symptom = (Field<Object>) getFieldGroup().getField(symptomId);
				if (symptom.isVisible()) {
					if (symptom.isRequired() && symptom.getValue() == null) {
						symptom.setValue(symptomState);
					} else if (Set.class.isAssignableFrom(symptom.getValue().getClass()) && ((Set) symptom.getValue()).size() == 0) {
						Set<SymptomState> value = (Set<SymptomState>) symptom.getValue();
						value.add(symptomState);
						symptom.setValue(value);
					}
				}
			}
			for (Object symptomId : monkeypoxImageFieldIds) {
				Field<Object> symptom = (Field<Object>) getFieldGroup().getField(symptomId);
				if (symptom.isVisible()) {
					if (symptom.isRequired() && symptom.getValue() == null) {
						symptom.setValue(symptomState);
					} else if (Set.class.isAssignableFrom(symptom.getValue().getClass()) && ((Set) symptom.getValue()).size() == 0) {
						Set<SymptomState> value = (Set<SymptomState>) symptom.getValue();
						value.add(symptomState);
						symptom.setValue(value);
					}
				}
			}
		}, ValoTheme.BUTTON_LINK);

		return button;
	}

}
