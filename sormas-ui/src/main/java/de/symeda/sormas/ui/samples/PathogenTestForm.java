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

import static de.symeda.sormas.ui.utils.CssStyles.H3;
import static de.symeda.sormas.ui.utils.CssStyles.VSPACE_3;
import static de.symeda.sormas.ui.utils.CssStyles.VSPACE_TOP_4;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;

import java.util.*;
import java.util.function.Consumer;

import de.symeda.sormas.api.user.DefaultUserRole;
import de.symeda.sormas.api.user.UserRoleReferenceDto;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseOutcome;
import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.contact.ContactReferenceDto;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.infrastructure.facility.DhimsFacility;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.person.Sex;
import de.symeda.sormas.ui.utils.*;
import org.apache.commons.collections.CollectionUtils;

import com.vaadin.ui.Label;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;

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

	private List<FacilityReferenceDto> allActiveLabs;

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

			//CSM:DISTRICT
			fluidRowLocs(PathogenTestDto.LABORATORY_TEST_PERFORMED, PathogenTestDto.LABORATORY_TEST_PERFORMED_OTHER) +
			fluidRowLocs(PathogenTestDto.LABORATORY_CYTOLOGY, PathogenTestDto.LABORATORY_GRAM, PathogenTestDto.LABORATORY_GRAM_OTHER) +
			fluidRowLocs(PathogenTestDto.LABORATORY_RDT_PERFORMED, PathogenTestDto.LABORATORY_RDT_RESULTS) +
			fluidRowLocs(6,PathogenTestDto.LABORATORY_LATEX) +
			fluidRowLocs(PathogenTestDto.OTHER_TEST) +
			fluidRowLocs(PathogenTestDto.DATE_SENT_REPORTING_HEALTH_FACILITY, PathogenTestDto.DATE_SAMPLE_SENT_REGREF_LAB) +

			//REGIONAL
			fluidRowLocs(PathogenTestDto.LABORATORY_CULTURE, PathogenTestDto.LABORATORY_CULTURE_OTHER) +
			fluidRowLocs(PathogenTestDto.LABORATORY_OTHER_TESTS, PathogenTestDto.LABORATORY_OTHER_TESTS_RESULTS) +
			loc(LABORATORY_ANTIBIOGRAM_HEADLINE_LOC) +
			fluidRowLocs(PathogenTestDto.LABORATORY_CEFTRIAXONE, PathogenTestDto.LABORATORY_PENICILLIN_G) +
			fluidRowLocs(PathogenTestDto.LABORATORY_AMOXYCILLIN, PathogenTestDto.LABORATORY_OXACILLIN) +
			fluidRowLocs(PathogenTestDto.LABORATORY_ANTIBIOGRAM_OTHER) +
			fluidRowLocs( PathogenTestDto.DATE_SAMPLE_SENT_REF_LAB) +

			//REFERENCE
			loc(LABORATORY_PCR_HEADLINE_LOC) +
			fluidRowLocs(PathogenTestDto.LABORATORY_DATE_PCR_PERFORMED, PathogenTestDto.LABORATORY_PCR_TYPE) +
			fluidRowLocs(PathogenTestDto.LABORATORY_PCR_OPTIONS) +
			fluidRowLocs(PathogenTestDto.LABORATORY_SEROTYPE, PathogenTestDto.LABORATORY_SEROTYPE_TYPE, PathogenTestDto.LABORATORY_SEROTYPE_RESULTS) +
			fluidRowLocs(6,PathogenTestDto.LABORATORY_FINAL_RESULTS) +
			fluidRowLocs(PathogenTestDto.LABORATORY_OBSERVATIONS) +
			fluidRowLocs(6, PathogenTestDto.LABORATORY_DATE_RESULTS_SENT_HEALTH_FACILITY) +
			fluidRowLocs(6, PathogenTestDto.LABORATORY_DATE_RESULTS_SENT_DSD) +
			fluidRowLocs(6,PathogenTestDto.LABORATORY_FINAL_CLASSIFICATION);

	//@formatter:on

	private SampleDto sample;
	private Disease selectedDisease;
	private AbstractSampleForm sampleForm;
	private final int caseSampleCount;
	private final boolean create;

	private Label pathogenTestHeadingLabel;

	private TextField testTypeTextField;
	private ComboBox pcrTestSpecification;
	private TextField typingIdField;
	private ComboBox testTypeField;



	public PathogenTestForm(SampleDto sample, boolean create, int caseSampleCount, boolean isPseudonymized) {
		super(
				PathogenTestDto.class,
				PathogenTestDto.I18N_PREFIX,
				false,
				FieldVisibilityCheckers.withDisease(null).andWithCountry(FacadeProvider.getConfigFacade().getCountryLocale()),
				UiFieldAccessCheckers.forSensitiveData(!create && isPseudonymized));

		this.sample = sample;
		this.caseSampleCount = caseSampleCount;
		this.create = create;
		setWidth(900, Unit.PIXELS);


		addFields();
		if (create) {
			hideValidationUntilNextCommit();
		}
	}
	private ComboBox diseaseField;
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

	public PathogenTestForm(SampleDto sample, boolean create, int caseSampleCount, boolean isPseudonymized, boolean inJurisdiction, Disease disease) {

		this(create, caseSampleCount, isPseudonymized, inJurisdiction, disease);
		this.sample = sample;
		this.selectedDisease = disease;
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

	public PathogenTestForm(boolean create, int caseSampleCount, boolean isPseudonymized, boolean inJurisdiction, Disease disease) {
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
		this.selectedDisease = disease;
		setWidth(900, Unit.PIXELS);
	}

	@Override
	protected void addFields() {
		if (sample == null) {
			return;
		}

		CaseDataDto caseDataDto = FacadeProvider.getCaseFacade().getCaseDataByUuid(sample.getAssociatedCase().getUuid());
		caseDisease =  caseDataDto.getDisease();

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
						sample.getSampleDateTime(),
						false,
						false,
						I18nProperties.getValidationError(
								Validations.afterDateWithDate,
								sampleTestDateField.getCaption(),
								I18nProperties.getPrefixCaption(SampleDto.I18N_PREFIX, SampleDto.SAMPLE_DATE_TIME),
								DateFormatHelper.formatDate(sample.getSampleDateTime()))));
			// new DateComparisonValidator(1.87.0
			// 	sampleTestDateField,
			// 	this::getSampleDate,
			// 	false,
			// 	false,
			// 	I18nProperties.getValidationError(
			// 		Validations.afterDateWithDate,
			// 		sampleTestDateField.getCaption(),
			// 		I18nProperties.getPrefixCaption(SampleDto.I18N_PREFIX, SampleDto.SAMPLE_DATE_TIME),
			// 		DateFormatHelper.formatDate(
			// 			sample != null ? sample.getSampleDateTime() : (Date) sampleForm.getField(SampleDto.SAMPLE_DATE_TIME).getValue()))));
		ComboBox lab = addInfrastructureField(PathogenTestDto.LAB);
		allActiveLabs = FacadeProvider.getFacilityFacade().getAllActiveLaboratories(true);
		TextField labDetails = addField(PathogenTestDto.LAB_DETAILS, TextField.class);
		labDetails.setVisible(false);
		typingIdField = addField(PathogenTestDto.TYPING_ID, TextField.class);
		typingIdField.setVisible(false);
		//diseaseField = addDiseaseField(PathogenTestDto.TESTED_DISEASE, true, create);

		/*ComboBox diseaseBox = new ComboBox("Diseases");

		}

		ComboBox diseaseField = addField(PathogenTestDto.TESTED_DISEASE, diseaseBox);*/

		diseaseField = addDiseaseField(PathogenTestDto.TESTED_DISEASE, true, create);

		if(caseDisease == Disease.AHF){
			diseaseField.removeAllItems();
			FieldHelper.updateEnumData(diseaseField, Disease.AHF_DISEASES);
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
		Set<UserRoleReferenceDto> userRoles =UserProvider.getCurrent().getUser().getUserRoles();

		String valLabUser=I18nProperties.getEnumCaption(DefaultUserRole.LAB_USER);
		UserRoleReferenceDto userRoleToFind = new UserRoleReferenceDto(null, valLabUser);

		if (userRoles.contains(userRoleToFind)) {
			testResultVerifiedField.setRequired(false);
			testResultVerifiedField.setEnabled(false);

			//PathogenTestDto.TEST_RESULT_VERIFIED defaultValue = PathogenTestDto.TEST_RESULT_VERIFIED; // Replace with the desired default value
			//testResultVerifiedField.setValue(defaultValue);
		}
		//if (UserProvider.getCurrent().hasLaboratoryOrExternalLaboratoryJurisdictionLevel()==true){//  hasUserRole(DefaultUserRole.LAB_USER)) {
//			testResultVerifiedField.setRequired(false);
//			testResultVerifiedField.setVisible(false);

		//}
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
			String diseaseName = disease.getName();
			lab.removeAllItems();
			List<FacilityReferenceDto> facilities = FacadeProvider.getFacilityFacade().getAllActiveFacilityByDisease(diseaseName);
			if (facilities.isEmpty()) {
				facilities = allActiveLabs;
			}
			lab.addItems(facilities);
			FieldHelper.updateItems(
					testTypeField,
					Arrays.asList(PathogenTestType.values()),
					FieldVisibilityCheckers.withDisease(disease),
					PathogenTestType.class);

			if (disease == Disease.MEASLES || Disease.AHF_DISEASES.contains(disease)) {
				List<PathogenTestType> ahfMeaselesPathogenTests = PathogenTestType.getMeaslesTestTypes();
				Arrays.stream(PathogenTestType.values())
						.filter(pathogenTestType -> !ahfMeaselesPathogenTests.contains(pathogenTestType))
						.forEach(pathogenTestType -> testTypeField.removeItem(pathogenTestType));
			}else{
				testTypeField.addItems(PathogenTestType.values());
			}
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

		if (sample.getSamplePurpose() != SamplePurpose.INTERNAL) {
//			ComboBox lab = addField(PathogenTestDto.LAB, ComboBox.class);
			lab.addItems(FacadeProvider.getFacilityFacade().getAllActiveLaboratories(true));
//			TextField labDetails = addField(PathogenTestDto.LAB_DETAILS, TextField.class);
			labDetails.setVisible(false);

			lab.addValueChangeListener(event -> {
				if (event.getProperty().getValue() != null
					&& ((FacilityReferenceDto) event.getProperty().getValue()).getUuid().equals(FacilityDto.OTHER_LABORATORY_UUID)) {
					labDetails.setVisible(true);
					labDetails.setRequired(true);
				} else {
					labDetails.setVisible(false);
					labDetails.setRequired(false);
					labDetails.clear();
				}
			});
			
			setRequired(true, PathogenTestDto.LAB);
		}
		if (sample.getSamplePurpose() != SamplePurpose.INTERNAL) { // this only works for already saved samples
			setRequired(true, PathogenTestDto.LAB);
		}
		// setRequired(true, PathogenTestDto.TEST_TYPE, PathogenTestDto.TESTED_DISEASE, PathogenTestDto.TEST_RESULT);
		
		setRequired(true, PathogenTestDto.TEST_TYPE, PathogenTestDto.TESTED_DISEASE, PathogenTestDto.TEST_DATE_TIME, PathogenTestDto.TEST_RESULT);
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

	public void setValue(PathogenTestDto newFieldValue, Disease disease) throws ReadOnlyException, Converter.ConversionException {
		super.setValue(newFieldValue);
		pcrTestSpecification.setValue(newFieldValue.getPcrTestSpecification());
		testTypeTextField.setValue(newFieldValue.getTestTypeText());
		typingIdField.setValue(newFieldValue.getTypingId());
		diseaseField.setValue(disease);
	}

	//not implemented
	public void getAssociatedDisease() {
		final ContactDto contactDto = FacadeProvider.getContactFacade().getByUuid(sample.getAssociatedContact().getUuid());
		contactDto.getDisease();

		final EventDto eventDto = FacadeProvider.getEventFacade().getEventByUuid(sample.getAssociatedEventParticipant().getUuid(), false);
		eventDto.getDisease();

	}
}
