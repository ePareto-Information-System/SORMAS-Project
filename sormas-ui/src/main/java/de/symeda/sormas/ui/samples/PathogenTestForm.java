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
package de.symeda.sormas.ui.samples;

import static de.symeda.sormas.ui.utils.CssStyles.*;
import static de.symeda.sormas.ui.utils.LayoutUtil.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.vaadin.v7.ui.*;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseOutcome;
import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.contact.ContactReferenceDto;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.event.EventParticipantReferenceDto;
import de.symeda.sormas.api.event.EventReferenceDto;
import de.symeda.sormas.api.hospitalization.SymptomsList;
import de.symeda.sormas.api.i18n.Descriptions;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.facility.DhimsFacility;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.person.Profession;
import de.symeda.sormas.api.person.Sex;
import de.symeda.sormas.api.sample.*;
import de.symeda.sormas.api.symptoms.SymptomsDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.Gram;
import de.symeda.sormas.api.utils.LabType;
import de.symeda.sormas.api.utils.LatexCulture;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.ui.utils.*;
import org.apache.commons.collections.CollectionUtils;

import com.vaadin.ui.Label;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.customizableenum.CustomizableEnumType;
import de.symeda.sormas.api.disease.DiseaseVariant;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.infrastructure.facility.FacilityDto;
import de.symeda.sormas.api.infrastructure.facility.FacilityReferenceDto;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;

public class  PathogenTestForm extends AbstractEditForm<PathogenTestDto> {

	private static final long serialVersionUID = -1218707278398543154L;

	private static final String PATHOGEN_TEST_HEADING_LOC = "pathogenTestHeadingLoc";
	protected static final String LABORATORY_ANTIBIOGRAM_HEADLINE_LOC = "laboratoryAntibiogramHeadlineloc";
	protected static final String LABORATORY_PCR_HEADLINE_LOC = "laboratoryPcrHeadlineloc";
	protected static final String DISTRICT_LABORATORY_HEADLINE_LOC = "districtLaboratoryLoc";
	protected static final String REGIONAL_LABORATORY_HEADLINE_LOC = "regionalLaboratoryLoc";
	protected static final String REFERENCE_LABORATORY_HEADLINE_LOC = "referenceLaboratoryLoc";
	protected static final String CYTOLOGY_HEADING = "cytologyHeading";
	private List<FacilityReferenceDto> allActiveLabs;
	private Disease associatedDisease;

	//@formatter:off
	private static final String HTML_LAYOUT =
			loc(PATHOGEN_TEST_HEADING_LOC) +
			fluidRowLocs(PathogenTestDto.REPORT_DATE, PathogenTestDto.VIA_LIMS) +
			fluidRowLocs(PathogenTestDto.EXTERNAL_ID, PathogenTestDto.EXTERNAL_ORDER_ID) +
			fluidRowLocs(PathogenTestDto.PCR_TEST_SPECIFICATION, "") +
			fluidRowLocs(PathogenTestDto.TESTED_DISEASE, PathogenTestDto.TESTED_DISEASE_DETAILS) +
			fluidRowLocs(PathogenTestDto.TEST_TYPE, PathogenTestDto.TEST_TYPE_TEXT) +
			fluidRowLocs(PathogenTestDto.VIRUS_DETECTION_GENOTYPE) +
			fluidRowLocs(PathogenTestDto.TESTED_DISEASE_VARIANT, PathogenTestDto.TESTED_DISEASE_VARIANT_DETAILS) +
			fluidRowLocs(PathogenTestDto.TYPING_ID, "") +
			fluidRowLocs(6,PathogenTestDto.TEST_DATE_TIME) +
			fluidRowLocs(PathogenTestDto.LAB, PathogenTestDto.LAB_DETAILS) +
			fluidRowLocs(PathogenTestDto.SAMPLE_TESTS) +
			fluidRowLocs(PathogenTestDto.SAMPLE_TEST_RESULT_PCR, PathogenTestDto.SAMPLE_TEST_RESULT_PCR_DATE) +
			fluidRowLocs(PathogenTestDto.SAMPLE_TEST_RESULT_ANTIGEN, PathogenTestDto.SAMPLE_TEST_RESULT_ANTIGEN_DATE) +
			fluidRowLocs(PathogenTestDto.SAMPLE_TEST_RESULT_IGM, PathogenTestDto.SAMPLE_TEST_RESULT_IGM_DATE) +
			fluidRowLocs(PathogenTestDto.SAMPLE_TEST_RESULT_IGG, PathogenTestDto.SAMPLE_TEST_RESULT_IGG_DATE) +
			fluidRowLocs(PathogenTestDto.SAMPLE_TEST_RESULT_IMMUNO, PathogenTestDto.SAMPLE_TEST_RESULT_IMMUNO_DATE) +
			fluidRowLocs(6,PathogenTestDto.LAB_LOCATION) +
			fluidRowLocs(PathogenTestDto.DATE_LAB_RECEIVED_SPECIMEN, PathogenTestDto.SPECIMEN_CONDITION) +
			fluidRowLocs(PathogenTestDto.TEST_RESULT, PathogenTestDto.TEST_RESULT_VERIFIED) +
			fluidRowLocs(PathogenTestDto.TEST_RESULT_VARIANT, PathogenTestDto.VARIANT_OTHER_SPECIFY) +
			fluidRowLocs(PathogenTestDto.SECOND_TESTED_DISEASE, PathogenTestDto.TEST_RESULT_FOR_SECOND_DISEASE) +
			fluidRowLocs(PathogenTestDto.PRELIMINARY, "") +
			fluidRowLocs(PathogenTestDto.FOUR_FOLD_INCREASE_ANTIBODY_TITER, "") +
			fluidRowLocs(PathogenTestDto.SEROTYPE, "") + 
			fluidRowLocs(PathogenTestDto.CQ_VALUE, "") + 
			fluidRowLocs(PathogenTestDto.TEST_RESULT_TEXT) +
			fluidRowLocs(PathogenTestDto.DATE_LAB_RESULTS_SENT_DISTRICT, PathogenTestDto.DATE_LAB_RESULTS_SENT_CLINICIAN) +
			fluidRowLocs(6, PathogenTestDto.DATE_DISTRICT_RECEIVED_LAB_RESULTS) +
			fluidRowLocs(PathogenTestDto.DELETION_REASON) +
			fluidRowLocs(PathogenTestDto.OTHER_DELETION_REASON)+

			fluidRowLocs(6,PathogenTestDto.LABORATORY_TYPE) +
			fluidRowLocs(DISTRICT_LABORATORY_HEADLINE_LOC) +
			fluidRowLocs(REGIONAL_LABORATORY_HEADLINE_LOC) +
			fluidRowLocs(REFERENCE_LABORATORY_HEADLINE_LOC) +
//			fluidRowLocs(6, PathogenTestDto.LABORATORY_NAME) +

			//CSM:GENERAL
//			fluidRowLocs(PathogenTestDto.LABORATORY_TEST_PERFORMED, PathogenTestDto.LABORATORY_TEST_PERFORMED_OTHER) +
			loc(CYTOLOGY_HEADING) +
			fluidRowLocs(PathogenTestDto.LABORATORY_CYTOLOGY_LEUCOCYTES, PathogenTestDto.LABORATORY_CYTOLOGY_PMN, PathogenTestDto.LABORATORY_CYTOLOGY_LYMPH) +
			fluidRowLocs(PathogenTestDto.LABORATORY_GRAM, PathogenTestDto.LABORATORY_GRAM_OTHER) +
			fluidRowLocs(PathogenTestDto.LABORATORY_RDT_PERFORMED, PathogenTestDto.LABORATORY_RDT_RESULTS) +
			fluidRowLocs(6,PathogenTestDto.LABORATORY_LATEX) +

			//DISTRICT
			fluidRowLocs(PathogenTestDto.OTHER_TEST) +

			//REGIONAL
			fluidRowLocs(PathogenTestDto.LABORATORY_CULTURE, PathogenTestDto.LABORATORY_CULTURE_OTHER) +
			fluidRowLocs(PathogenTestDto.LABORATORY_OTHER_TESTS) +
			loc(LABORATORY_ANTIBIOGRAM_HEADLINE_LOC) +
			fluidRowLocs(PathogenTestDto.LABORATORY_CEFTRIAXONE, PathogenTestDto.LABORATORY_PENICILLIN_G) +
			fluidRowLocs(PathogenTestDto.LABORATORY_AMOXYCILLIN, PathogenTestDto.LABORATORY_OXACILLIN) +
			fluidRowLocs(PathogenTestDto.LABORATORY_ANTIBIOGRAM_OTHER) +

			//REFERENCE
			loc(LABORATORY_PCR_HEADLINE_LOC) +
			fluidRowLocs(PathogenTestDto.LABORATORY_DATE_PCR_PERFORMED, PathogenTestDto.LABORATORY_PCR_TYPE) +
			fluidRowLocs(6,PathogenTestDto.LABORATORY_PCR_OPTIONS) +
			fluidRowLocs(PathogenTestDto.LABORATORY_SEROTYPE) +
			fluidRowLocs(6,PathogenTestDto.LABORATORY_FINAL_RESULTS) +
			fluidRowLocs(PathogenTestDto.LABORATORY_OBSERVATIONS) +
			fluidRowLocs(6,PathogenTestDto.DATE_SAMPLE_SENT_REF_LAB) +
			fluidRowLocs(PathogenTestDto.DATE_SENT_REPORTING_HEALTH_FACILITY, PathogenTestDto.DATE_SAMPLE_SENT_REGREF_LAB) +
			fluidRowLocs(6, PathogenTestDto.LABORATORY_DATE_RESULTS_SENT_DSD) +
			fluidRowLocs(6, PathogenTestDto.LABORATORY_FINAL_CLASSIFICATION) +

			fluidRowLocs(PathogenTestDto.FINAL_CLASSIFICATION, "");

	//@formatter:on

	private SampleDto sample;
	private AbstractSampleForm sampleForm;
	private final int caseSampleCount;
	private final boolean create;

	private Label pathogenTestHeadingLabel;

	private TextField testTypeTextField;
	private ComboBox pcrTestSpecification;
	private TextField typingIdField;
	private ComboBox testTypeField;
	private Disease caseDisease;
	private TextField laboratoryTestPerformedOther;
	private TextField laboratoryCytology_LEUCOCYTES;
	private TextField laboratoryCytology_PMN;
	private TextField laboratoryCytology_LYMPH;
	private NullableOptionGroup laboratoryGram;
	private TextField laboratoryGramOther;
	private OptionGroup laboratoryRdtPerformed;
	private TextField laboratoryRdtResults;
	private TextField laboratoryCultureOther;
	private TextField laboratoryOtherTests;
	private ComboBox laboratoryCeftriaxone;
	private ComboBox laboratoryPenicillinG;
	private ComboBox laboratoryAmoxycillin;
	private ComboBox laboratoryOxacillin;
	private OptionGroup laboratoryAntibiogramOther;
	private DateField laboratoryDatePcrPerformed;
	private TextField laboratoryPcrType;
	private TextField laboratorySerotype;
	private TextField laboratoryFinalResults;
	private ComboBox laboratoryLatex;
	private TextArea laboratoryObservations;
	private ComboBox labPcrOptions;
	private OptionGroup laboratoryFinalClassification;
	private ComboBox laboratoryCulture;
	Label districtLaboratory;
	Label regionalLaboratory;
	Label referenceLaboratory;
	private DateField labDateResultsSentDSD;
	private DateField refLabDate;
	private DateField regrefLabDate;
	private DateField dateSentReportHealthFacility;
	private ComboBox diseaseBox;
	private ComboBox testResultField;
	private ComboBox testResultVariant;
	private ComboBox secondTestedDisease;
	private ComboBox TestResultForSecondDisease;
	OptionGroup tickTestField;

	private ComboBox diseaseField;
	private TextField virusDetectionGenotypeField;
	private ComboBox finalClassificationField;


	public PathogenTestForm(AbstractSampleForm sampleForm, boolean create, int caseSampleCount, boolean isPseudonymized, boolean inJurisdiction) {
		this(create, caseSampleCount, isPseudonymized, inJurisdiction);
		this.sampleForm = sampleForm;
		addFields();
		if (create) {
			hideValidationUntilNextCommit();
		}
	}

	public PathogenTestForm(SampleDto sample, boolean create, int caseSampleCount, boolean isPseudonymized, boolean inJurisdiction) {

		this(create, caseSampleCount, isPseudonymized, inJurisdiction);
		this.sample = sample;
		addFields();
		if (create) {
			hideValidationUntilNextCommit();
		}
	}
	private ComboBox diseaseField;

	public PathogenTestForm(boolean create, int caseSampleCount, boolean isPseudonymized, boolean inJurisdiction) {
		super(
			PathogenTestDto.class,
			PathogenTestDto.I18N_PREFIX,
			false,
			FieldVisibilityCheckers.withDisease(null).andWithCountry(FacadeProvider.getConfigFacade().getCountryLocale()),
			UiFieldAccessCheckers.forDataAccessLevel(
				UserProvider.getCurrent().getPseudonymizableDataAccessLevel(create || inJurisdiction), // Jurisdiction doesn't matter for creation forms
				!create && isPseudonymized)); // Pseudonymization doesn't matter for creation forms

		this.caseSampleCount = caseSampleCount;
		this.create = create;
		setWidth(900, Unit.PIXELS);
	}

	@Override
	protected void addFields() {

		CaseDataDto caseDataDto = FacadeProvider.getCaseFacade().getCaseDataByUuid(sample.getAssociatedCase().getUuid());
		caseDisease = caseDataDto.getDisease();

		pathogenTestHeadingLabel = new Label();
		pathogenTestHeadingLabel.addStyleName(H3);
		getContent().addComponent(pathogenTestHeadingLabel, PATHOGEN_TEST_HEADING_LOC);

		addDateField(PathogenTestDto.REPORT_DATE, DateField.class, 0);
		addField(PathogenTestDto.VIA_LIMS);
		addField(PathogenTestDto.EXTERNAL_ID);
		addField(PathogenTestDto.EXTERNAL_ORDER_ID);

		testTypeField = addField(PathogenTestDto.TEST_TYPE, ComboBox.class);
		testTypeField.setItemCaptionMode(ItemCaptionMode.ID_TOSTRING);
		testTypeField.setImmediate(true);

		pcrTestSpecification = addField(PathogenTestDto.PCR_TEST_SPECIFICATION, ComboBox.class);
		testTypeTextField = addField(PathogenTestDto.TEST_TYPE_TEXT, TextField.class);
		FieldHelper.addSoftRequiredStyle(testTypeTextField);
		DateTimeField sampleTestDateField = addField(PathogenTestDto.TEST_DATE_TIME, DateTimeField.class);
		sampleTestDateField.addValidator(
			new DateComparisonValidator(
				sampleTestDateField,
				this::getSampleDate,
				false,
				false,
				I18nProperties.getValidationError(
					Validations.afterDateWithDate,
					sampleTestDateField.getCaption(),
					I18nProperties.getPrefixCaption(SampleDto.I18N_PREFIX, SampleDto.SAMPLE_DATE_TIME),
					DateFormatHelper.formatDate(
						sample != null ? sample.getSampleDateTime() : (Date) sampleForm.getField(SampleDto.SAMPLE_DATE_TIME).getValue()))));
		ComboBox lab = addInfrastructureField(PathogenTestDto.LAB);
		allActiveLabs = FacadeProvider.getFacilityFacade().getAllActiveLaboratories(true);
		TextField labDetails = addField(PathogenTestDto.LAB_DETAILS, TextField.class);
		labDetails.setVisible(false);
		typingIdField = addField(PathogenTestDto.TYPING_ID, TextField.class);
		typingIdField.setVisible(false);

		diseaseField = addDiseaseField(PathogenTestDto.TESTED_DISEASE, true, create);

		if(caseDisease == Disease.AHF){
			diseaseField.removeAllItems();
			FieldHelper.updateEnumData(diseaseField, Disease.AHF_DISEASES);
		} else if (caseDisease == Disease.CSM) {
			diseaseField.removeAllItems();
			FieldHelper.updateEnumData(diseaseField, Disease.CSM_ONLY);
			diseaseField.setEnabled(false);
		} else if (caseDisease == Disease.NEW_INFLUENZA) {
			diseaseField.removeAllItems();
			FieldHelper.updateEnumData(diseaseField, Disease.NEW_ONLY);
			diseaseField.setEnabled(false);
		}
		else if (caseDisease == Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS) {
			for (Disease disease1 : Disease.values()) {
				if (disease1.getName().equals(sample.getSuspectedDisease().getName())) {
					diseaseField.removeAllItems();
					FieldHelper.updateEnumData(diseaseField, Collections.singleton(disease1));
					break;
				}
			}
		}


		ComboBox diseaseVariantField = addField(PathogenTestDto.TESTED_DISEASE_VARIANT, ComboBox.class);
		diseaseVariantField.setNullSelectionAllowed(true);
		addField(PathogenTestDto.TESTED_DISEASE_DETAILS, TextField.class);
		TextField diseaseVariantDetailsField = addField(PathogenTestDto.TESTED_DISEASE_VARIANT_DETAILS, TextField.class);
		diseaseVariantDetailsField.setVisible(false);

		testResultField = addField(PathogenTestDto.TEST_RESULT, ComboBox.class);
		testResultField.removeItem(PathogenTestResultType.NOT_DONE);
		
		testResultVariant = addField(PathogenTestDto.TEST_RESULT_VARIANT, ComboBox.class);
		testResultVariant.setVisible(false);
		TextField variantOther = addField(PathogenTestDto.VARIANT_OTHER_SPECIFY, TextField.class);
		secondTestedDisease = addField(PathogenTestDto.SECOND_TESTED_DISEASE, ComboBox.class);
		TestResultForSecondDisease = addField(PathogenTestDto.TEST_RESULT_FOR_SECOND_DISEASE, ComboBox.class);
		secondTestedDisease.setVisible(false);
		TestResultForSecondDisease.setVisible(false);
		variantOther.setVisible(false);

		virusDetectionGenotypeField = addField(PathogenTestDto.VIRUS_DETECTION_GENOTYPE, TextField.class);
		virusDetectionGenotypeField.setVisible(false);

		finalClassificationField = addField(PathogenTestDto.FINAL_CLASSIFICATION, ComboBox.class);
		finalClassificationField.setVisible(false);
		
		addField(PathogenTestDto.SEROTYPE, TextField.class);
		TextField cqValueField = addField(PathogenTestDto.CQ_VALUE, TextField.class);
		cqValueField.setConversionError(I18nProperties.getValidationError(Validations.onlyNumbersAllowed, cqValueField.getCaption()));
		NullableOptionGroup testResultVerifiedField = addField(PathogenTestDto.TEST_RESULT_VERIFIED, NullableOptionGroup.class);
		testResultVerifiedField.setRequired(true);
		CheckBox fourFoldIncrease = addField(PathogenTestDto.FOUR_FOLD_INCREASE_ANTIBODY_TITER, CheckBox.class);
		CssStyles.style(fourFoldIncrease, VSPACE_3, VSPACE_TOP_4);
		fourFoldIncrease.setVisible(false);
		fourFoldIncrease.setEnabled(false);

		addField(PathogenTestDto.TEST_RESULT_TEXT, TextArea.class).setRows(6);
		//addField(PathogenTestDto.PRELIMINARY).addStyleName(CssStyles.VSPACE_4);

		addField(PathogenTestDto.DELETION_REASON);
		addField(PathogenTestDto.OTHER_DELETION_REASON, TextArea.class).setRows(3);
		setVisible(false, PathogenTestDto.DELETION_REASON, PathogenTestDto.OTHER_DELETION_REASON);

		initializeAccessAndAllowedAccesses();
		initializeVisibilitiesAndAllowedVisibilities();

		pcrTestSpecification.setVisible(false);

		/*Map<Object, List<Object>> pcrTestSpecificationVisibilityDependencies = new HashMap<Object, List<Object>>() {

			{
				put(PathogenTestDto.TESTED_DISEASE, Arrays.asList(Disease.CORONAVIRUS));
				put(PathogenTestDto.TEST_TYPE, Arrays.asList(PathogenTestType.PCR_RT_PCR));
			}
		};
		FieldHelper.setVisibleWhen(getFieldGroup(), PathogenTestDto.PCR_TEST_SPECIFICATION, pcrTestSpecificationVisibilityDependencies, true);*/
		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			PathogenTestDto.TEST_TYPE_TEXT,
			PathogenTestDto.TEST_TYPE,
			Arrays.asList(PathogenTestType.OTHER),
			true);
		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			PathogenTestDto.TESTED_DISEASE_DETAILS,
			PathogenTestDto.TESTED_DISEASE,
			Arrays.asList(Disease.OTHER),
			true);
		/*FieldHelper.setVisibleWhen(
			getFieldGroup(),
			PathogenTestDto.TYPING_ID,
			PathogenTestDto.TEST_TYPE,
			Arrays.asList(PathogenTestType.PCR_RT_PCR, PathogenTestType.DNA_MICROARRAY, PathogenTestType.SEQUENCING),
			true);*/
		Map<Object, List<Object>> serotypeVisibilityDependencies = new HashMap<Object, List<Object>>() {

			private static final long serialVersionUID = 1967952323596082247L;

			{
				put(PathogenTestDto.TESTED_DISEASE, Arrays.asList(Disease.CSM));
				put(PathogenTestDto.TEST_RESULT, Arrays.asList(PathogenTestResultType.POSITIVE));
			}
		};
		FieldHelper.setVisibleWhen(getFieldGroup(), PathogenTestDto.SEROTYPE, serotypeVisibilityDependencies, true);

		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			PathogenTestDto.CQ_VALUE,
			PathogenTestDto.TEST_TYPE,
			Arrays.asList(PathogenTestType.CQ_VALUE_DETECTION),
			true);

		Consumer<Disease> updateDiseaseVariantField = disease -> {
			List<DiseaseVariant> diseaseVariants =
				FacadeProvider.getCustomizableEnumFacade().getEnumValues(CustomizableEnumType.DISEASE_VARIANT, disease);
			FieldHelper.updateItems(diseaseVariantField, diseaseVariants);
			diseaseVariantField.setVisible(
				disease != null && isVisibleAllowed(PathogenTestDto.TESTED_DISEASE_VARIANT) && CollectionUtils.isNotEmpty(diseaseVariants));
		};

			// trigger the update, as the disease may already be set
			updateDiseaseVariantField.accept((Disease) diseaseField.getValue());

		final CaseReferenceDto associatedCase = sample.getAssociatedCase();
		final ContactReferenceDto associatedContact = sample.getAssociatedContact();
		final EventParticipantReferenceDto associatedEventParticipant = sample.getAssociatedEventParticipant();

		if (associatedCase != null && UserProvider.getCurrent().hasAllUserRights(UserRight.CASE_VIEW)) {
			associatedDisease = getDiseaseFromCase(associatedCase.getUuid());
		} else if (associatedContact != null && UserProvider.getCurrent().hasAllUserRights(UserRight.CONTACT_VIEW)) {
			associatedDisease = getDiseaseFromContact(associatedContact.getUuid());
		} else if (associatedEventParticipant != null && UserProvider.getCurrent().hasAllUserRights(UserRight.EVENT_VIEW)) {
			EventReferenceDto eventReferenceDto = FacadeProvider.getEventParticipantFacade().getEventParticipantByUuid(associatedEventParticipant.getUuid()).getEvent();
			if (eventReferenceDto != null) {
				associatedDisease = getDiseaseFromEvent(eventReferenceDto.getUuid());
			}
		} else {
			associatedDisease = null;
		}

		diseaseField.addValueChangeListener((ValueChangeListener) valueChangeEvent -> {
			Disease disease = (Disease) valueChangeEvent.getProperty().getValue();

			if (disease == null && associatedDisease != null && create) {
				disease = associatedDisease;
				diseaseField.setValue(associatedDisease);
			}
			if (disease != null) {
				updateDiseaseVariantField.accept(disease);
				String diseaseName = disease.getName();
				FacilityReferenceDto selectedLab = (FacilityReferenceDto) lab.getValue();
				lab.removeAllItems();

				List<FacilityReferenceDto> facilities = FacadeProvider.getFacilityFacade().getAllActiveFacilityByDisease(diseaseName);
				if (facilities.isEmpty()) {
					facilities = allActiveLabs;
				}
				lab.addItems(facilities);
				if (selectedLab != null && facilities.contains(selectedLab)) {
					lab.setValue(selectedLab);
				} else {
					lab.setValue(facilities.size() > 0 ? facilities.get(0) : null);
				}

				if (disease.equals(associatedDisease) && create) {
					lab.setValue(sample.getLab());
				}
				FieldHelper.updateItems(
						testTypeField,
						Arrays.asList(PathogenTestType.values()),
						FieldVisibilityCheckers.withDisease(disease),
						PathogenTestType.class);
			}

			if (disease == Disease.MEASLES) {
				List<PathogenTestType> ahfMeaselesPathogenTests = PathogenTestType.getMeaslesTestTypes();
				Arrays.stream(PathogenTestType.values())
						.filter(pathogenTestType -> !ahfMeaselesPathogenTests.contains(pathogenTestType))
						.forEach(pathogenTestType -> testTypeField.removeItem(pathogenTestType));

			} else if(disease == Disease.CSM) {
				List<PathogenTestType> csmPathogenTests = PathogenTestType.getCSMTestTypes();
				Arrays.stream(PathogenTestType.values())
						.filter(pathogenTestType -> !csmPathogenTests.contains(pathogenTestType))
						.forEach(pathogenTestType -> testTypeField.removeItem(pathogenTestType));
			} else if(disease == Disease.NEW_INFLUENZA){
					List<PathogenTestType> iliPathogenTests = PathogenTestType.getILITestTypes();
					Arrays.stream(PathogenTestType.values())
							.filter(pathogenTestType -> !iliPathogenTests.contains(pathogenTestType))
							.forEach(pathogenTestType -> testTypeField.removeItem(pathogenTestType));

				testResultField.addValueChangeListener(e -> {
					PathogenTestResultType testResult = (PathogenTestResultType) e.getProperty().getValue();
					if(testResult == PathogenTestResultType.POSITIVE){
						testResultVariant.setVisible(true);
					}else {
						testResultVariant.setVisible(false);
						testResultVariant.clear();
					}
				});

				FieldHelper.setVisibleWhen(testResultVariant, Arrays.asList(variantOther), Arrays.asList(PathogenTestResultVariant.OTHER), true);
				testResultField.removeItem(PathogenTestResultType.PENDING);


				secondTestedDisease.setVisible(true);
				secondTestedDisease.setValue(Disease.CORONAVIRUS);
				secondTestedDisease.setEnabled(false);

				TestResultForSecondDisease.setVisible(true);
				TestResultForSecondDisease.removeItem(PathogenTestResultType.PENDING);
				TestResultForSecondDisease.removeItem(PathogenTestResultType.NOT_DONE);
			}
			else {
					testTypeField.addItems(PathogenTestType.values());
			}

			if(Arrays.asList(Disease.MEASLES, Disease.CHOLERA, Disease.YELLOW_FEVER).contains(disease)){
				finalClassificationField.setVisible(true);
			} else {
				finalClassificationField.setVisible(false);
			}
		});

	/*	testTypeField.addValueChangeListener(e -> {
			PathogenTestType testType = (PathogenTestType) e.getProperty().getValue();
			if (testType == PathogenTestType.IGM_SERUM_ANTIBODY || testType == PathogenTestType.IGG_SERUM_ANTIBODY) {
				fourFoldIncrease.setVisible(true);
				fourFoldIncrease.setEnabled(caseSampleCount >= 2);
			} else {
				fourFoldIncrease.setVisible(false);
				fourFoldIncrease.setEnabled(false);
			}
		});*/

		lab.addValueChangeListener(event -> {
			if (event.getProperty().getValue() != null
				&& ((FacilityReferenceDto) event.getProperty().getValue()).getUuid().equals(FacilityDto.OTHER_FACILITY_UUID)) {
				labDetails.setVisible(true);
				labDetails.setRequired(isEditableAllowed(labDetails));
			} else {
				labDetails.setVisible(false);
				labDetails.setRequired(false);
				labDetails.clear();
			}
		});

		/*testTypeField.addValueChangeListener(e -> {
			PathogenTestType testType = (PathogenTestType) e.getProperty().getValue();
			if ((testType == PathogenTestType.PCR_RT_PCR && testResultField.getValue() == PathogenTestResultType.POSITIVE)
				|| testType == PathogenTestType.CQ_VALUE_DETECTION) {
				cqValueField.setVisible(true);
			} else {
				cqValueField.setVisible(false);
				cqValueField.clear();
			}
		});*/

	/*	testResultField.addValueChangeListener(e -> {
			PathogenTestResultType testResult = (PathogenTestResultType) e.getProperty().getValue();
			if ((testTypeField.getValue() == PathogenTestType.PCR_RT_PCR && testResult == PathogenTestResultType.POSITIVE)
				|| testTypeField.getValue() == PathogenTestType.CQ_VALUE_DETECTION) {
				cqValueField.setVisible(true);
			} else {
				cqValueField.setVisible(false);
				cqValueField.clear();
			}
		});*/

		if (SamplePurpose.INTERNAL.equals(getSamplePurpose())) { // this only works for already saved samples
			setRequired(true, PathogenTestDto.LAB);
		}
		setRequired(true, PathogenTestDto.TEST_TYPE, PathogenTestDto.TESTED_DISEASE, PathogenTestDto.TEST_RESULT);

		DateField datelabResultsSentDistrict = addField(PathogenTestDto.DATE_LAB_RESULTS_SENT_DISTRICT, DateField.class);
		datelabResultsSentDistrict.setInvalidCommitted(false);
		DateField dateLabResultsSentClinician = addField(PathogenTestDto.DATE_LAB_RESULTS_SENT_CLINICIAN, DateField.class);
		dateLabResultsSentClinician.setInvalidCommitted(false);
		DateField dateDistrictReceivedLabResults = addField(PathogenTestDto.DATE_DISTRICT_RECEIVED_LAB_RESULTS, DateField.class);
		dateDistrictReceivedLabResults.setInvalidCommitted(false);

		datelabResultsSentDistrict.setVisible(false);
		dateLabResultsSentClinician.setVisible(false);
		dateDistrictReceivedLabResults.setVisible(false);


		if (caseDisease == Disease.CSM) {
			districtLaboratory = new Label(I18nProperties.getString(Strings.headingDistrictLaboratory));
			CssStyles.style(districtLaboratory, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_4);
			getContent().addComponent(districtLaboratory, DISTRICT_LABORATORY_HEADLINE_LOC);
			districtLaboratory.setVisible(false);

			regionalLaboratory = new Label(I18nProperties.getString(Strings.headingRegionalLaboratory));
			CssStyles.style(regionalLaboratory, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_4);
			getContent().addComponent(regionalLaboratory, REGIONAL_LABORATORY_HEADLINE_LOC);
			regionalLaboratory.setVisible(false);

			referenceLaboratory = new Label(I18nProperties.getString(Strings.headingReferenceLaboratory));
			CssStyles.style(referenceLaboratory, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_4);
			getContent().addComponent(referenceLaboratory, REFERENCE_LABORATORY_HEADLINE_LOC);
			referenceLaboratory.setVisible(false);

			ComboBox laboType = addField(PathogenTestDto.LABORATORY_TYPE, ComboBox.class);
			laboType.setRequired(true);
			//addField(PathogenTestDto.LABORATORY_NAME, TextField.class);

			// General fields
			Label cytologyHeadingLabel = createLabel(I18nProperties.getString(Strings.headingCytology), H3, CYTOLOGY_HEADING);
			laboratoryCytology_LEUCOCYTES = addField(PathogenTestDto.LABORATORY_CYTOLOGY_LEUCOCYTES, TextField.class);
			laboratoryCytology_PMN = addField(PathogenTestDto.LABORATORY_CYTOLOGY_PMN, TextField.class);
			laboratoryCytology_LYMPH = addField(PathogenTestDto.LABORATORY_CYTOLOGY_LYMPH, TextField.class);
			laboratoryGram = addField(PathogenTestDto.LABORATORY_GRAM, NullableOptionGroup.class);
			laboratoryGramOther = addField(PathogenTestDto.LABORATORY_GRAM_OTHER, TextField.class);
			laboratoryRdtPerformed = addField(PathogenTestDto.LABORATORY_RDT_PERFORMED, OptionGroup.class);
			laboratoryRdtResults = addField(PathogenTestDto.LABORATORY_RDT_RESULTS, TextField.class);
			laboratoryGramOther.setVisible(false);

			laboratoryLatex = new ComboBox();
			for (LatexCulture latexCulture : LatexCulture.LATEX) {
				laboratoryLatex.addItem(latexCulture);
				laboratoryLatex.setItemCaption(latexCulture, latexCulture.toString());
			}
			addField(PathogenTestDto.LABORATORY_LATEX, laboratoryLatex);
			addField(PathogenTestDto.OTHER_TEST, TextArea.class);

			FieldHelper.setVisibleWhen(laboratoryGram, Arrays.asList(laboratoryGramOther), Arrays.asList(Gram.OTHER_PATHOGENS), true);

			// District Section
			dateSentReportHealthFacility = addField(PathogenTestDto.DATE_SENT_REPORTING_HEALTH_FACILITY);
			regrefLabDate = addField(PathogenTestDto.DATE_SAMPLE_SENT_REGREF_LAB);

			// Regional Section
			laboratoryCulture = new ComboBox();
			for (LatexCulture latexCulture : LatexCulture.LAB_CULTURE) {
				laboratoryCulture.addItem(latexCulture);
				laboratoryCulture.setItemCaption(latexCulture, latexCulture.toString());
			}
			addField(PathogenTestDto.LABORATORY_CULTURE, laboratoryCulture);
			 laboratoryCultureOther = addField(PathogenTestDto.LABORATORY_CULTURE_OTHER, TextField.class);
			 laboratoryOtherTests = addField(PathogenTestDto.LABORATORY_OTHER_TESTS, TextField.class);
			laboratoryCeftriaxone = addField(PathogenTestDto.LABORATORY_CEFTRIAXONE, ComboBox.class);
			laboratoryPenicillinG = addField(PathogenTestDto.LABORATORY_PENICILLIN_G, ComboBox.class);
			laboratoryAmoxycillin = addField(PathogenTestDto.LABORATORY_AMOXYCILLIN, ComboBox.class);
			laboratoryOxacillin = addField(PathogenTestDto.LABORATORY_OXACILLIN, ComboBox.class);
			laboratoryAntibiogramOther = addField(PathogenTestDto.LABORATORY_ANTIBIOGRAM_OTHER, OptionGroup.class);
			refLabDate = addField(PathogenTestDto.DATE_SAMPLE_SENT_REF_LAB, DateField.class);

			// Reference Section
			laboratoryDatePcrPerformed = addField(PathogenTestDto.LABORATORY_DATE_PCR_PERFORMED, DateField.class);
			laboratoryPcrType = addField(PathogenTestDto.LABORATORY_PCR_TYPE, TextField.class);

			labPcrOptions = new ComboBox();
			for (LatexCulture latexCulture : LatexCulture.LAB_CULTURE) {
				labPcrOptions.addItem(latexCulture);
				labPcrOptions.setItemCaption(latexCulture, latexCulture.toString());
			}
			addField(PathogenTestDto.LABORATORY_PCR_OPTIONS, labPcrOptions);
			laboratorySerotype = addField(PathogenTestDto.LABORATORY_SEROTYPE, TextField.class);

			laboratoryObservations = addField(PathogenTestDto.LABORATORY_OBSERVATIONS, TextArea.class);
			laboratoryObservations.setRows(4);
			laboratoryObservations.setDescription(
					I18nProperties.getPrefixDescription(PathogenTestDto.I18N_PREFIX, PathogenTestDto.LABORATORY_OBSERVATIONS, "") + "\n"
							+ I18nProperties.getDescription(Descriptions.observation));

			labDateResultsSentDSD =  addField(PathogenTestDto.LABORATORY_DATE_RESULTS_SENT_DSD);


			laboratoryFinalClassification = new OptionGroup();
			for (CaseClassification caseClassification : CaseClassification.CASE_CLASSIFY) {
				laboratoryFinalClassification.addItem(caseClassification);
				laboratoryFinalClassification.setItemCaption(caseClassification, caseClassification.toString());
			}
			addField(PathogenTestDto.LABORATORY_FINAL_CLASSIFICATION, laboratoryFinalClassification);
			laboratoryFinalResults = addField(PathogenTestDto.LABORATORY_FINAL_RESULTS, TextField.class);

			// Listener
			laboType.addValueChangeListener(e -> {
				LabType labType = (LabType) e.getProperty().getValue();

				// Hide all components initially
				hideAllComponents();

				// Show components based on the selected lab type
				switch (labType) {
					case DISTRICT_LAB:
						districtLaboratory.setVisible(true);
						showComponentsForDistrictLab();
						break;
					case REGIONAL_LAB:
						regionalLaboratory.setVisible(true);
						showComponentsForRegionalLab();
						break;
					case REFERENCE_LAB:
						referenceLaboratory.setVisible(true);
						showComponentsForReferenceLab();
						break;
					default:
						break;
				}
			});
			setVisible(false, PathogenTestDto.FOUR_FOLD_INCREASE_ANTIBODY_TITER, PathogenTestDto.SEROTYPE, PathogenTestDto.CQ_VALUE, PathogenTestDto.TEST_RESULT_TEXT);
		}

		if(caseDisease == Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS){
			sampleTestDateField.setVisible(false);
			addField(PathogenTestDto.LAB_LOCATION, TextField.class);

			datelabResultsSentDistrict.setVisible(true);
			dateLabResultsSentClinician.setVisible(true);
			dateDistrictReceivedLabResults.setVisible(true);

		}

		if(caseDisease == Disease.YELLOW_FEVER){
			datelabResultsSentDistrict.setVisible(true);
			dateDistrictReceivedLabResults.setVisible(true);
			virusDetectionGenotypeField.setVisible(true);
		}

		if(caseDisease == Disease.AHF){
			testTypeField.setVisible(false);
			testTypeField.setRequired(false);
			testResultField.setVisible(false);
			testResultField.setRequired(false);

			tickTestField = addField(PathogenTestDto.SAMPLE_TESTS, OptionGroup.class);
			CssStyles.style(tickTestField, CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
			tickTestField.setMultiSelect(true);

			tickTestField.addItems(
					Arrays.stream(PathogenTestType.getAHFTestTypes())
							.filter(c -> fieldVisibilityCheckers.isVisible(PathogenTestType.class, c.name()))
							.collect(Collectors.toList()));

			NullableOptionGroup sampleTestResultPCR = addField(PathogenTestDto.SAMPLE_TEST_RESULT_PCR, NullableOptionGroup.class);
			DateField sampleTestResultPCRDate = addField(PathogenTestDto.SAMPLE_TEST_RESULT_PCR_DATE, DateField.class);
			NullableOptionGroup sampleTestResultAntigen = addField(PathogenTestDto.SAMPLE_TEST_RESULT_ANTIGEN, NullableOptionGroup.class);
			DateField sampleTestResultAntigenDate = addField(PathogenTestDto.SAMPLE_TEST_RESULT_ANTIGEN_DATE, DateField.class);
			NullableOptionGroup sampleTestResultIGM = addField(PathogenTestDto.SAMPLE_TEST_RESULT_IGM, NullableOptionGroup.class);
			DateField sampleTestResultIGMDate = addField(PathogenTestDto.SAMPLE_TEST_RESULT_IGM_DATE, DateField.class);
			NullableOptionGroup sampleTestResultIGG = addField(PathogenTestDto.SAMPLE_TEST_RESULT_IGG, NullableOptionGroup.class);
			DateField sampleTestResultIGGDate = addField(PathogenTestDto.SAMPLE_TEST_RESULT_IGG_DATE, DateField.class);
			NullableOptionGroup sampleTestResultImmuno = addField(PathogenTestDto.SAMPLE_TEST_RESULT_IMMUNO, NullableOptionGroup.class);
			DateField sampleTestResultImmunoDate = addField(PathogenTestDto.SAMPLE_TEST_RESULT_IMMUNO_DATE, DateField.class);

			setVisible(false,
					PathogenTestDto.SAMPLE_TEST_RESULT_PCR, PathogenTestDto.SAMPLE_TEST_RESULT_PCR_DATE, PathogenTestDto.SAMPLE_TEST_RESULT_ANTIGEN, PathogenTestDto.SAMPLE_TEST_RESULT_ANTIGEN_DATE, PathogenTestDto.SAMPLE_TEST_RESULT_IGM, PathogenTestDto.SAMPLE_TEST_RESULT_IGM_DATE, PathogenTestDto.SAMPLE_TEST_RESULT_IGG, PathogenTestDto.SAMPLE_TEST_RESULT_IGG_DATE, PathogenTestDto.SAMPLE_TEST_RESULT_IMMUNO, PathogenTestDto.SAMPLE_TEST_RESULT_IMMUNO_DATE, PathogenTestDto.TEST_RESULT_TEXT);

			tickTestField.addValueChangeListener(event -> {
				Set<PathogenTestType> selectedTests = (Set<PathogenTestType>) event.getProperty().getValue();

				boolean isPCRSelected = selectedTests != null && selectedTests.contains(PathogenTestType.PCR);
				sampleTestResultPCR.setVisible(isPCRSelected);
				sampleTestResultPCRDate.setVisible(isPCRSelected);

				boolean isAntigenSelected = selectedTests != null && selectedTests.contains(PathogenTestType.ANTIGEN_DETECTION);
				sampleTestResultAntigen.setVisible(isAntigenSelected);
				sampleTestResultAntigenDate.setVisible(isAntigenSelected);

				boolean isIGMSelected = selectedTests != null && selectedTests.contains(PathogenTestType.IGM_SERUM_ANTIBODY);
				sampleTestResultIGM.setVisible(isIGMSelected);
				sampleTestResultIGMDate.setVisible(isIGMSelected);

				boolean isIGGSelected = selectedTests != null && selectedTests.contains(PathogenTestType.IGG_SERUM_ANTIBODY);
				sampleTestResultIGG.setVisible(isIGGSelected);
				sampleTestResultIGGDate.setVisible(isIGGSelected);

				boolean isImmunoSelected = selectedTests != null && selectedTests.contains(PathogenTestType.IMMUNO);
				sampleTestResultImmuno.setVisible(isImmunoSelected);
				sampleTestResultImmunoDate.setVisible(isImmunoSelected);

			});

		}
	}

	// Method to hide all components initially
	private void hideAllComponents() {
		districtLaboratory.setVisible(false);
		regionalLaboratory.setVisible(false);
		referenceLaboratory.setVisible(false);

		laboratoryObservations.setVisible(false);
		laboratoryDatePcrPerformed.setVisible(false);
		laboratoryPcrType.setVisible(false);
		labPcrOptions.setVisible(false);
		laboratorySerotype.setVisible(false);
		laboratoryFinalClassification.setVisible(false);
		laboratoryFinalResults.setVisible(false);
		laboratoryAntibiogramOther.setVisible(false);
		laboratoryCulture.setVisible(false);
		laboratoryCultureOther.setVisible(false);
		laboratoryOtherTests.setVisible(false);
		refLabDate.setVisible(false);
		regrefLabDate.setVisible(false);
		labDateResultsSentDSD.setVisible(false);

		setVisible(false, PathogenTestDto.LABORATORY_CEFTRIAXONE, PathogenTestDto.LABORATORY_AMOXYCILLIN, PathogenTestDto.LABORATORY_PENICILLIN_G, PathogenTestDto.LABORATORY_OXACILLIN);

	}

	// Method to show components for district lab
	private void showComponentsForDistrictLab() {
		setVisible(true, PathogenTestDto.OTHER_TEST, PathogenTestDto.DATE_SENT_REPORTING_HEALTH_FACILITY);
		regrefLabDate.setVisible(true);
	}

	// Method to show components for regional lab
	private void showComponentsForRegionalLab() {
		laboratoryCulture.setVisible(true);
		laboratoryCultureOther.setVisible(true);
		laboratoryOtherTests.setVisible(true);
		laboratoryCeftriaxone.setVisible(true);
		laboratoryPenicillinG.setVisible(true);
		laboratoryAmoxycillin.setVisible(true);
		laboratoryOxacillin.setVisible(true);
		laboratoryAntibiogramOther.setVisible(true);
		refLabDate.setVisible(true);
	}

	// Method to show components for reference lab
	private void showComponentsForReferenceLab() {
		laboratoryCulture.setVisible(true);
		laboratoryCultureOther.setVisible(true);
		laboratoryOtherTests.setVisible(true);
		laboratoryCeftriaxone.setVisible(true);
		laboratoryPenicillinG.setVisible(true);
		laboratoryAmoxycillin.setVisible(true);
		laboratoryOxacillin.setVisible(true);
		laboratoryAntibiogramOther.setVisible(true);
		laboratoryDatePcrPerformed.setVisible(true);
		laboratoryPcrType.setVisible(true);
		labPcrOptions.setVisible(true);
		laboratorySerotype.setVisible(true);
		laboratoryFinalResults.setVisible(true);
		laboratoryObservations.setVisible(true);
		labDateResultsSentDSD.setVisible(true);
	}

	private Date getSampleDate() {
		return sample != null ? sample.getSampleDateTime() : (Date) sampleForm.getField(SampleDto.SAMPLE_DATE_TIME).getValue();
	}

	private SamplePurpose getSamplePurpose() {
		return sample != null ? sample.getSamplePurpose() : (SamplePurpose) sampleForm.getField(SampleDto.SAMPLE_PURPOSE).getValue();
	}

	@Override
	protected String createHtmlLayout() {
		return HTML_LAYOUT;
	}

	@Override
	public void setHeading(String heading) {
		pathogenTestHeadingLabel.setValue(heading);
	}

	@Override
	public void setValue(PathogenTestDto newFieldValue) throws ReadOnlyException, Converter.ConversionException {
		super.setValue(newFieldValue);
		pcrTestSpecification.setValue(newFieldValue.getPcrTestSpecification());
		testTypeTextField.setValue(newFieldValue.getTestTypeText());
		typingIdField.setValue(newFieldValue.getTypingId());
	}

	private Disease getDiseaseFromCase(String caseUuid) {
		CaseDataDto caseDataDto = FacadeProvider.getCaseFacade().getCaseDataByUuid(caseUuid);
		if (caseDataDto != null) {
			return caseDataDto.getDisease();
		}
		return null;
	}

	private Disease getDiseaseFromContact(String contactUuid) {
		ContactDto contactDto = FacadeProvider.getContactFacade().getByUuid(contactUuid);
		if (contactDto != null) {
			return contactDto.getDisease();
		}
		return null;
	}

	private Disease getDiseaseFromEvent(String eventUuid) {
		EventDto eventDto = FacadeProvider.getEventFacade().getByUuid(eventUuid);
		if (eventDto != null) {
			return eventDto.getDisease();
		}
		return null;
	}

	private Label createLabel(String text, String h4, String location) {
		final Label label = new Label(text);
		label.setId(text);
		label.addStyleName(h4);
		getContent().addComponent(label, location);
		return label;
	}

}
