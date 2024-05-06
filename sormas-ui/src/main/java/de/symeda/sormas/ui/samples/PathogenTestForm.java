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

import com.vaadin.v7.ui.*;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseOutcome;
import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.contact.ContactReferenceDto;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.i18n.Descriptions;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.facility.DhimsFacility;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.person.Sex;
import de.symeda.sormas.api.utils.LabType;
import de.symeda.sormas.api.utils.LatexCulture;
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
import de.symeda.sormas.api.sample.PathogenTestDto;
import de.symeda.sormas.api.sample.PathogenTestResultType;
import de.symeda.sormas.api.sample.PathogenTestType;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.sample.SamplePurpose;
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

	//@formatter:off
	private static final String HTML_LAYOUT =
			loc(PATHOGEN_TEST_HEADING_LOC) +
			fluidRowLocs(PathogenTestDto.REPORT_DATE, PathogenTestDto.VIA_LIMS) +
			fluidRowLocs(PathogenTestDto.EXTERNAL_ID, PathogenTestDto.EXTERNAL_ORDER_ID) +
			fluidRowLocs(PathogenTestDto.TEST_TYPE, PathogenTestDto.TEST_TYPE_TEXT) +
			fluidRowLocs(PathogenTestDto.PCR_TEST_SPECIFICATION, "") +
			fluidRowLocs(PathogenTestDto.TESTED_DISEASE, PathogenTestDto.TESTED_DISEASE_DETAILS) +
			fluidRowLocs(PathogenTestDto.TESTED_DISEASE_VARIANT, PathogenTestDto.TESTED_DISEASE_VARIANT_DETAILS) +
			fluidRowLocs(PathogenTestDto.TYPING_ID, "") +
			fluidRowLocs(PathogenTestDto.TEST_DATE_TIME, PathogenTestDto.LAB) +
			fluidRowLocs("", PathogenTestDto.LAB_DETAILS) +
			fluidRowLocs(PathogenTestDto.TEST_RESULT, PathogenTestDto.TEST_RESULT_VERIFIED) +
			fluidRowLocs(PathogenTestDto.PRELIMINARY, "") +
			fluidRowLocs(PathogenTestDto.FOUR_FOLD_INCREASE_ANTIBODY_TITER, "") +
			fluidRowLocs(PathogenTestDto.SEROTYPE, "") + 
			fluidRowLocs(PathogenTestDto.CQ_VALUE, "") + 
			fluidRowLocs(PathogenTestDto.TEST_RESULT_TEXT) +
			fluidRowLocs(PathogenTestDto.DELETION_REASON) +
			fluidRowLocs(PathogenTestDto.OTHER_DELETION_REASON)+

			fluidRowLocs(6,PathogenTestDto.LABORATORY_TYPE) +
			fluidRowLocs(DISTRICT_LABORATORY_HEADLINE_LOC) +
			fluidRowLocs(REGIONAL_LABORATORY_HEADLINE_LOC) +
			fluidRowLocs(REFERENCE_LABORATORY_HEADLINE_LOC) +
			locCss(VSPACE_TOP_3, PathogenTestDto.LABORATORY_NAME) +

			//CSM:GENERAL
			fluidRowLocs(PathogenTestDto.LABORATORY_TEST_PERFORMED, PathogenTestDto.LABORATORY_TEST_PERFORMED_OTHER) +
			fluidRowLocs(PathogenTestDto.LABORATORY_CYTOLOGY, PathogenTestDto.LABORATORY_GRAM, PathogenTestDto.LABORATORY_GRAM_OTHER) +
			fluidRowLocs(PathogenTestDto.LABORATORY_RDT_PERFORMED, PathogenTestDto.LABORATORY_RDT_RESULTS) +
			fluidRowLocs(6,PathogenTestDto.LABORATORY_LATEX) +

			//DISTRICT
			fluidRowLocs(PathogenTestDto.OTHER_TEST) +
			fluidRowLocs(PathogenTestDto.DATE_SENT_REPORTING_HEALTH_FACILITY, PathogenTestDto.DATE_SAMPLE_SENT_REGREF_LAB) +

			//REGIONAL
			fluidRowLocs(PathogenTestDto.LABORATORY_CULTURE, PathogenTestDto.LABORATORY_CULTURE_OTHER) +
			fluidRowLocs(PathogenTestDto.LABORATORY_OTHER_TESTS, PathogenTestDto.LABORATORY_OTHER_TESTS_RESULTS) +
			loc(LABORATORY_ANTIBIOGRAM_HEADLINE_LOC) +
			fluidRowLocs(PathogenTestDto.LABORATORY_CEFTRIAXONE, PathogenTestDto.LABORATORY_PENICILLIN_G) +
			fluidRowLocs(PathogenTestDto.LABORATORY_AMOXYCILLIN, PathogenTestDto.LABORATORY_OXACILLIN) +
			fluidRowLocs(PathogenTestDto.LABORATORY_ANTIBIOGRAM_OTHER) +
			fluidRowLocs(PathogenTestDto.DATE_SAMPLE_SENT_REF_LAB) +

			//REFERENCE
			loc(LABORATORY_PCR_HEADLINE_LOC) +
			fluidRowLocs(PathogenTestDto.LABORATORY_DATE_PCR_PERFORMED, PathogenTestDto.LABORATORY_PCR_TYPE) +
			fluidRowLocs(PathogenTestDto.LABORATORY_PCR_OPTIONS) +
			fluidRowLocs(PathogenTestDto.LABORATORY_SEROTYPE, PathogenTestDto.LABORATORY_SEROTYPE_TYPE, PathogenTestDto.LABORATORY_SEROTYPE_RESULTS) +
			fluidRowLocs(6,PathogenTestDto.LABORATORY_FINAL_RESULTS) +
			fluidRowLocs(PathogenTestDto.LABORATORY_OBSERVATIONS) +
			fluidRowLocs(6, PathogenTestDto.LABORATORY_DATE_RESULTS_SENT_HEALTH_FACILITY) +
			fluidRowLocs(6, PathogenTestDto.LABORATORY_DATE_RESULTS_SENT_DSD) +
			fluidRowLocs(6, PathogenTestDto.LABORATORY_FINAL_CLASSIFICATION);

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
	private ComboBox diseaseFieldForAll;
	private Disease caseDisease;

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
		caseDisease =  caseDataDto.getDisease();

		pathogenTestHeadingLabel = new Label();
		pathogenTestHeadingLabel.addStyleName(H3);
		getContent().addComponent(pathogenTestHeadingLabel, PATHOGEN_TEST_HEADING_LOC);

		addDateField(PathogenTestDto.REPORT_DATE, DateField.class, 0);
		addField(PathogenTestDto.VIA_LIMS);
		addField(PathogenTestDto.EXTERNAL_ID);
		addField(PathogenTestDto.EXTERNAL_ORDER_ID);

		ComboBox testBox = new ComboBox("Sex");

		if(caseDisease == Disease.AHF){
			for (PathogenTestType test : PathogenTestType.values()) {
				if (test == PathogenTestType.IGG_SERUM_ANTIBODY || test == PathogenTestType.IGM_SERUM_ANTIBODY || test == PathogenTestType.PCR_RT_PCR) {
					testBox.addItem(test);
				}
			}
			addField(PathogenTestDto.TEST_TYPE, testBox);
		}
		else {
			testTypeField = addField(PathogenTestDto.TEST_TYPE, ComboBox.class);
			testTypeField.setItemCaptionMode(ItemCaptionMode.ID_TOSTRING);
			testTypeField.setImmediate(true);
		}

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
		lab.addItems(FacadeProvider.getFacilityFacade().getAllActiveLaboratories(true));
		TextField labDetails = addField(PathogenTestDto.LAB_DETAILS, TextField.class);
		labDetails.setVisible(false);
		typingIdField = addField(PathogenTestDto.TYPING_ID, TextField.class);
		typingIdField.setVisible(false);

		ComboBox diseaseBox = new ComboBox("Disease");

		if(caseDisease == Disease.CSM){
			for (Disease selection : Disease.values()) {
				if (selection == Disease.CSM) {
					diseaseBox.addItem(selection);
				}
			}
			diseaseFieldForAll = addField(PathogenTestDto.TESTED_DISEASE, diseaseBox);
		}
		else {
			diseaseFieldForAll = addField(PathogenTestDto.TESTED_DISEASE);
			diseaseFieldForAll.setItemCaptionMode(ItemCaptionMode.ID_TOSTRING);
			diseaseFieldForAll.setImmediate(true);

		}

		ComboBox diseaseVariantField = addField(PathogenTestDto.TESTED_DISEASE_VARIANT, ComboBox.class);
		diseaseVariantField.setNullSelectionAllowed(true);
		addField(PathogenTestDto.TESTED_DISEASE_DETAILS, TextField.class);
		TextField diseaseVariantDetailsField = addField(PathogenTestDto.TESTED_DISEASE_VARIANT_DETAILS, TextField.class);
		diseaseVariantDetailsField.setVisible(false);

		ComboBox testResultField = addField(PathogenTestDto.TEST_RESULT, ComboBox.class);
		testResultField.removeItem(PathogenTestResultType.NOT_DONE);
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
		addField(PathogenTestDto.PRELIMINARY).addStyleName(CssStyles.VSPACE_4);

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
			Arrays.asList(PathogenTestType.PCR_RT_PCR, PathogenTestType.OTHER),
			true);
		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			PathogenTestDto.TESTED_DISEASE_DETAILS,
			PathogenTestDto.TESTED_DISEASE,
			Arrays.asList(Disease.OTHER),
			true);
		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			PathogenTestDto.TYPING_ID,
			PathogenTestDto.TEST_TYPE,
			Arrays.asList(PathogenTestType.PCR_RT_PCR, PathogenTestType.DNA_MICROARRAY, PathogenTestType.SEQUENCING),
			true);
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
		updateDiseaseVariantField.accept((Disease) diseaseFieldForAll.getValue());

		diseaseFieldForAll.addValueChangeListener((ValueChangeListener) valueChangeEvent -> {
			Disease disease = (Disease) valueChangeEvent.getProperty().getValue();
			updateDiseaseVariantField.accept(disease);

			FieldHelper.updateItems(
				testBox,
				Arrays.asList(PathogenTestType.values()),
				FieldVisibilityCheckers.withDisease(disease),
				PathogenTestType.class);
		});
		diseaseVariantField.addValueChangeListener(e -> {
			DiseaseVariant diseaseVariant = (DiseaseVariant) e.getProperty().getValue();
			diseaseVariantDetailsField.setVisible(diseaseVariant != null && diseaseVariant.matchPropertyValue(DiseaseVariant.HAS_DETAILS, true));
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




		if(caseDisease == Disease.CSM){

			Label districtLaboratory = new Label(I18nProperties.getString(Strings.headingDistrictLaboratory));
			CssStyles.style(districtLaboratory, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_4);
			getContent().addComponent(districtLaboratory, DISTRICT_LABORATORY_HEADLINE_LOC);
			districtLaboratory.setVisible(false);

			Label regionalLaboratory = new Label(I18nProperties.getString(Strings.headingRegionalLaboratory));
			CssStyles.style(regionalLaboratory, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_4);
			getContent().addComponent(regionalLaboratory, REGIONAL_LABORATORY_HEADLINE_LOC);
			regionalLaboratory.setVisible(false);

			Label referenceLaboratory = new Label(I18nProperties.getString(Strings.headingReferenceLaboratory));
			CssStyles.style(referenceLaboratory, CssStyles.LABEL_BOLD, CssStyles.LABEL_SECONDARY, VSPACE_4);
			getContent().addComponent(referenceLaboratory, REFERENCE_LABORATORY_HEADLINE_LOC);
			referenceLaboratory.setVisible(false);

			ComboBox laboType = addField(PathogenTestDto.LABORATORY_TYPE, ComboBox.class);
			addField(PathogenTestDto.LABORATORY_NAME, TextField.class);

			//General
			NullableOptionGroup laboratoryTestPerformed = addField(PathogenTestDto.LABORATORY_TEST_PERFORMED, NullableOptionGroup.class);
			TextField laboratoryTestPerformedOther = addField(PathogenTestDto.LABORATORY_TEST_PERFORMED_OTHER, TextField.class);
			TextField laboratoryCytology = addField(PathogenTestDto.LABORATORY_CYTOLOGY, TextField.class);
			NullableOptionGroup laboratoryGram = addField(PathogenTestDto.LABORATORY_GRAM, NullableOptionGroup.class);
			TextField laboratoryGramOther = addField(PathogenTestDto.LABORATORY_GRAM_OTHER, TextField.class);
			OptionGroup laboratoryRdtPerformed = addField(PathogenTestDto.LABORATORY_RDT_PERFORMED, OptionGroup.class);
			TextField laboratoryRdtResults = addField(PathogenTestDto.LABORATORY_RDT_RESULTS, TextField.class);

			ComboBox laboratoryLatex = new ComboBox();
			for (LatexCulture latexCulture : LatexCulture.LATEX) {
				laboratoryLatex.addItem(latexCulture);
				laboratoryLatex.setItemCaption(latexCulture, latexCulture.toString());
			}
			addField(PathogenTestDto.LABORATORY_LATEX, laboratoryLatex);
			addField(PathogenTestDto.OTHER_TEST, TextArea.class);

			//District Section
			addField(PathogenTestDto.DATE_SENT_REPORTING_HEALTH_FACILITY, DateField.class);
			addField(PathogenTestDto.DATE_SAMPLE_SENT_REGREF_LAB, DateField.class);

			//Regional Section
			ComboBox laboratoryCulture = new ComboBox();
			for (LatexCulture latexCulture : LatexCulture.LAB_CULTURE) {
				laboratoryCulture.addItem(latexCulture);
				laboratoryCulture.setItemCaption(latexCulture, latexCulture.toString());
			}
			addField(PathogenTestDto.LABORATORY_CULTURE, laboratoryCulture);
			TextField laboratoryCultureOther = addField(PathogenTestDto.LABORATORY_CULTURE_OTHER, TextField.class);
			TextField laboratoryOtherTests = addField(PathogenTestDto.LABORATORY_OTHER_TESTS, TextField.class);
			TextField laboratoryOtherTestsResults = addField(PathogenTestDto.LABORATORY_OTHER_TESTS_RESULTS, TextField.class);
			ComboBox laboratoryCeftriaxone = addField(PathogenTestDto.LABORATORY_CEFTRIAXONE, ComboBox.class);
			ComboBox laboratoryPenicillinG = addField(PathogenTestDto.LABORATORY_PENICILLIN_G, ComboBox.class);
			ComboBox laboratoryAmoxycillin = addField(PathogenTestDto.LABORATORY_AMOXYCILLIN, ComboBox.class);
			ComboBox laboratoryOxacillin = addField(PathogenTestDto.LABORATORY_OXACILLIN, ComboBox.class);
			OptionGroup laboratoryAntibiogramOther = addField(PathogenTestDto.LABORATORY_ANTIBIOGRAM_OTHER, OptionGroup.class);
			addField(PathogenTestDto.DATE_SAMPLE_SENT_REF_LAB, DateField.class);

			//Reference Section
			DateField laboratoryDatePcrPerformed = addField(PathogenTestDto.LABORATORY_DATE_PCR_PERFORMED, DateField.class);
			TextField laboratoryPcrType = addField(PathogenTestDto.LABORATORY_PCR_TYPE, TextField.class);

			ComboBox labPcrOptions = new ComboBox();
			for (LatexCulture latexCulture : LatexCulture.LAB_CULTURE) {
				labPcrOptions.addItem(latexCulture);
				labPcrOptions.setItemCaption(latexCulture, latexCulture.toString());
			}
			addField(PathogenTestDto.LABORATORY_PCR_OPTIONS, labPcrOptions);
			TextField laboratorySerotype = addField(PathogenTestDto.LABORATORY_SEROTYPE, TextField.class);
			TextField laboratorySerotypeType = addField(PathogenTestDto.LABORATORY_SEROTYPE_TYPE, TextField.class);
			TextField laboratorySerotypeResults = addField(PathogenTestDto.LABORATORY_SEROTYPE_RESULTS, TextField.class);

			TextArea laboratoryObservations = addField(PathogenTestDto.LABORATORY_OBSERVATIONS, TextArea.class);
			laboratoryObservations.setRows(4);
			laboratoryObservations.setDescription(
					I18nProperties.getPrefixDescription(PathogenTestDto.I18N_PREFIX, PathogenTestDto.LABORATORY_OBSERVATIONS, "") + "\n"
							+ I18nProperties.getDescription(Descriptions.observation));

			DateField laboratoryDateResultsSentHealthFacility = addField(PathogenTestDto.LABORATORY_DATE_RESULTS_SENT_HEALTH_FACILITY, DateField.class);
			DateField laboratoryDateResultsSentDSD = addField(PathogenTestDto.LABORATORY_DATE_RESULTS_SENT_DSD, DateField.class);

			OptionGroup laboratoryFinalClassification = new OptionGroup();
			for (CaseClassification caseClassification : CaseClassification.CASE_CLASSIFY) {
				laboratoryFinalClassification.addItem(caseClassification);
				laboratoryFinalClassification.setItemCaption(caseClassification, caseClassification.toString());
			}
			addField(PathogenTestDto.LABORATORY_FINAL_CLASSIFICATION, laboratoryFinalClassification);
			addField(PathogenTestDto.LABORATORY_FINAL_RESULTS, TextField.class);

			//set properties to false for CSM Lab Section
			setVisible(false, PathogenTestDto.OTHER_TEST, PathogenTestDto.DATE_SENT_REPORTING_HEALTH_FACILITY, PathogenTestDto.DATE_SAMPLE_SENT_REGREF_LAB, PathogenTestDto.LABORATORY_CULTURE, PathogenTestDto.LABORATORY_CULTURE_OTHER,
					PathogenTestDto.LABORATORY_OTHER_TESTS, PathogenTestDto.LABORATORY_OTHER_TESTS_RESULTS, PathogenTestDto.LABORATORY_CEFTRIAXONE, PathogenTestDto.LABORATORY_PENICILLIN_G,
					PathogenTestDto.LABORATORY_AMOXYCILLIN, PathogenTestDto.LABORATORY_OXACILLIN, PathogenTestDto.LABORATORY_ANTIBIOGRAM_OTHER, PathogenTestDto.DATE_SAMPLE_SENT_REF_LAB,
					PathogenTestDto.LABORATORY_DATE_PCR_PERFORMED, PathogenTestDto.LABORATORY_PCR_TYPE, PathogenTestDto.LABORATORY_PCR_OPTIONS,
					PathogenTestDto.LABORATORY_SEROTYPE, PathogenTestDto.LABORATORY_SEROTYPE_TYPE, PathogenTestDto.LABORATORY_SEROTYPE_RESULTS,  PathogenTestDto.LABORATORY_FINAL_RESULTS,
					PathogenTestDto.LABORATORY_OBSERVATIONS,PathogenTestDto.LABORATORY_DATE_RESULTS_SENT_HEALTH_FACILITY,   PathogenTestDto.LABORATORY_DATE_RESULTS_SENT_DSD, PathogenTestDto.LABORATORY_FINAL_CLASSIFICATION);

			laboType.addValueChangeListener(e -> {
			LabType labType = (LabType) e.getProperty().getValue();

			districtLaboratory.setVisible(false);
			regionalLaboratory.setVisible(false);
			referenceLaboratory.setVisible(false);


			if (labType == LabType.DISTRICT_LAB) {
				districtLaboratory.setVisible(true);

				setVisible(true, PathogenTestDto.OTHER_TEST, PathogenTestDto.DATE_SENT_REPORTING_HEALTH_FACILITY, PathogenTestDto.DATE_SAMPLE_SENT_REGREF_LAB);
			}
			else if (labType == LabType.REGIONAL_LAB) {
				regionalLaboratory.setVisible(true);

				setVisible(true,PathogenTestDto.LABORATORY_CULTURE, PathogenTestDto.LABORATORY_CULTURE_OTHER, PathogenTestDto.LABORATORY_OTHER_TESTS, PathogenTestDto.LABORATORY_OTHER_TESTS_RESULTS, PathogenTestDto.LABORATORY_CEFTRIAXONE, PathogenTestDto.LABORATORY_PENICILLIN_G, PathogenTestDto.LABORATORY_AMOXYCILLIN, PathogenTestDto.LABORATORY_OXACILLIN, PathogenTestDto.LABORATORY_ANTIBIOGRAM_OTHER);
				setVisible(true, PathogenTestDto.DATE_SENT_REPORTING_HEALTH_FACILITY, PathogenTestDto.DATE_SAMPLE_SENT_REF_LAB);
			}
			else if (labType == LabType.REFERENCE_LAB) {
				referenceLaboratory.setVisible(true);

				setVisible(true, PathogenTestDto.LABORATORY_CULTURE, PathogenTestDto.LABORATORY_CULTURE_OTHER, PathogenTestDto.LABORATORY_OTHER_TESTS, PathogenTestDto.LABORATORY_OTHER_TESTS_RESULTS,PathogenTestDto.LABORATORY_CEFTRIAXONE, PathogenTestDto.LABORATORY_PENICILLIN_G, PathogenTestDto.LABORATORY_AMOXYCILLIN, PathogenTestDto.LABORATORY_OXACILLIN, PathogenTestDto.LABORATORY_ANTIBIOGRAM_OTHER);

				setVisible(true, PathogenTestDto.LABORATORY_DATE_PCR_PERFORMED, PathogenTestDto.LABORATORY_PCR_TYPE, PathogenTestDto.LABORATORY_PCR_OPTIONS, PathogenTestDto.LABORATORY_SEROTYPE, PathogenTestDto.LABORATORY_SEROTYPE_TYPE, PathogenTestDto.LABORATORY_SEROTYPE_RESULTS, PathogenTestDto.LABORATORY_FINAL_RESULTS, PathogenTestDto.LABORATORY_OBSERVATIONS, PathogenTestDto.DATE_SENT_REPORTING_HEALTH_FACILITY, PathogenTestDto.LABORATORY_DATE_RESULTS_SENT_DSD, PathogenTestDto.LABORATORY_FINAL_CLASSIFICATION);
			}

			});
		}
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


	public void getAssociatedDisease() {
		final ContactDto contactDto = FacadeProvider.getContactFacade().getByUuid(sample.getAssociatedContact().getUuid());
		contactDto.getDisease();

		final EventDto eventDto = FacadeProvider.getEventFacade().getEventByUuid(sample.getAssociatedEventParticipant().getUuid(), false);
		eventDto.getDisease();

	}
}
