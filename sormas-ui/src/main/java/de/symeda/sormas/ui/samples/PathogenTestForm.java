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

import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseReferenceDto;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.contact.ContactReferenceDto;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.event.EventParticipantReferenceDto;
import de.symeda.sormas.api.event.EventReferenceDto;
import de.symeda.sormas.api.user.DefaultUserRole;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.user.UserRoleReferenceDto;
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
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.DateComparisonValidator;
import de.symeda.sormas.ui.utils.DateFormatHelper;
import de.symeda.sormas.ui.utils.DateTimeField;
import de.symeda.sormas.ui.utils.FieldHelper;
import de.symeda.sormas.ui.utils.NullableOptionGroup;

public class PathogenTestForm extends AbstractEditForm<PathogenTestDto> {

	private static final long serialVersionUID = -1218707278398543154L;

	private static final String PATHOGEN_TEST_HEADING_LOC = "pathogenTestHeadingLoc";

	private List<FacilityReferenceDto> allActiveLabs;

	private Disease associatedDisease;

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
					fluidRowLocs(PathogenTestDto.OTHER_DELETION_REASON);
	//@formatter:on

	private final SampleDto sample;
	private final int caseSampleCount;
	private final boolean create;

	private Label pathogenTestHeadingLabel;

	private TextField testTypeTextField;
	private ComboBox pcrTestSpecification;
	private TextField typingIdField;



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

	@Override
	protected void addFields() {
		if (sample == null) {
			return;
		}
		pathogenTestHeadingLabel = new Label();
		pathogenTestHeadingLabel.addStyleName(H3);
		getContent().addComponent(pathogenTestHeadingLabel, PATHOGEN_TEST_HEADING_LOC);

		addDateField(PathogenTestDto.REPORT_DATE, DateField.class, 0);
		addField(PathogenTestDto.VIA_LIMS);
		addField(PathogenTestDto.EXTERNAL_ID);
		addField(PathogenTestDto.EXTERNAL_ORDER_ID);
		ComboBox testTypeField = addField(PathogenTestDto.TEST_TYPE, ComboBox.class);
		testTypeField.setItemCaptionMode(ItemCaptionMode.ID_TOSTRING);
		testTypeField.setImmediate(true);
		pcrTestSpecification = addField(PathogenTestDto.PCR_TEST_SPECIFICATION, ComboBox.class);
		testTypeTextField = addField(PathogenTestDto.TEST_TYPE_TEXT, TextField.class);
		FieldHelper.addSoftRequiredStyle(testTypeTextField);
		DateTimeField sampleTestDateField = addField(PathogenTestDto.TEST_DATE_TIME, DateTimeField.class);
		sampleTestDateField.addValidator(
				new DateComparisonValidator(
						sampleTestDateField,
						sample.getReceivedDate(),
						false,
						false,
						I18nProperties.getValidationError(
								Validations.afterDateWithDate,
								sampleTestDateField.getCaption(),
								I18nProperties.getPrefixCaption(SampleDto.I18N_PREFIX, SampleDto.RECEIVED_DATE),
								DateFormatHelper.formatDate(sample.getReceivedDate()))));
		ComboBox lab = addInfrastructureField(PathogenTestDto.LAB);
		allActiveLabs = FacadeProvider.getFacilityFacade().getAllActiveLaboratories(true);
		TextField labDetails = addField(PathogenTestDto.LAB_DETAILS, TextField.class);
		labDetails.setVisible(false);
		typingIdField = addField(PathogenTestDto.TYPING_ID, TextField.class);
		typingIdField.setVisible(false);
		ComboBox diseaseField = addDiseaseField(PathogenTestDto.TESTED_DISEASE, true, create);
		diseaseField.setValue(Disease.INFLUENZA_A);
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
		String valLabSupervisor =I18nProperties.getEnumCaption(DefaultUserRole.LAB_SUPERVISOR);
		UserRoleReferenceDto userRoleToFind = new UserRoleReferenceDto(null, valLabUser);

		if (userRoles.contains(userRoleToFind) && !userRoles.contains(new UserRoleReferenceDto(null, valLabSupervisor))) {
			//TODO Uncomment the line below to enable the testResultVerifiedField once Ghana health service agrees to enable it
//			testResultVerifiedField.setRequired(false);
//			testResultVerifiedField.setEnabled(false);

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


		Map<Object, List<Object>> pcrTestSpecificationVisibilityDependencies = new HashMap<Object, List<Object>>() {

			{
				put(PathogenTestDto.TESTED_DISEASE, Arrays.asList(Disease.CORONAVIRUS));
				put(PathogenTestDto.TEST_TYPE, Arrays.asList(PathogenTestType.PCR_RT_PCR));
			}
		};
		FieldHelper.setVisibleWhen(getFieldGroup(), PathogenTestDto.PCR_TEST_SPECIFICATION, pcrTestSpecificationVisibilityDependencies, true);
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

//		lab.addItems(FacadeProvider.getFacilityFacade().getAllActiveLaboratories(true));
		//diseaseField.setValue(associatedDisease);




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
		});

		diseaseVariantField.addValueChangeListener(e -> {
			DiseaseVariant diseaseVariant = (DiseaseVariant) e.getProperty().getValue();
			diseaseVariantDetailsField.setVisible(diseaseVariant != null && diseaseVariant.matchPropertyValue(DiseaseVariant.HAS_DETAILS, true));
		});

		testTypeField.addValueChangeListener(e -> {
			PathogenTestType testType = (PathogenTestType) e.getProperty().getValue();
			if (testType == PathogenTestType.IGM_SERUM_ANTIBODY || testType == PathogenTestType.IGG_SERUM_ANTIBODY) {
				fourFoldIncrease.setVisible(true);
				fourFoldIncrease.setEnabled(caseSampleCount >= 2);
			} else {
				fourFoldIncrease.setVisible(false);
				fourFoldIncrease.setEnabled(false);
			}
		});

		testTypeField.addValueChangeListener(e -> {
			PathogenTestType testType = (PathogenTestType) e.getProperty().getValue();
			if ((testType == PathogenTestType.PCR_RT_PCR && testResultField.getValue() == PathogenTestResultType.POSITIVE)
					|| testType == PathogenTestType.CQ_VALUE_DETECTION) {
				cqValueField.setVisible(true);
			} else {
				cqValueField.setVisible(false);
				cqValueField.clear();
			}
		});

		testResultField.addValueChangeListener(e -> {
			PathogenTestResultType testResult = (PathogenTestResultType) e.getProperty().getValue();
			if ((testTypeField.getValue() == PathogenTestType.PCR_RT_PCR && testResult == PathogenTestResultType.POSITIVE)
					|| testTypeField.getValue() == PathogenTestType.CQ_VALUE_DETECTION) {
				cqValueField.setVisible(true);
			} else {
				cqValueField.setVisible(false);
				cqValueField.clear();
			}
		});


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
}