/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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
package de.symeda.sormas.ui.ebs;

import static de.symeda.sormas.ui.utils.CssStyles.H3;
import static de.symeda.sormas.ui.utils.CssStyles.LABEL_WHITE_SPACE_NORMAL;
import static de.symeda.sormas.ui.utils.CssStyles.VSPACE_3;
import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.ebs.EbsDto;
import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.ebs.ManualScanningType;
import de.symeda.sormas.api.ebs.MediaScannningType;
import de.symeda.sormas.api.ebs.PersonReporting;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.location.LocationEditForm;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.DateTimeField;
import de.symeda.sormas.ui.utils.EbsDateValidator;
import de.symeda.sormas.ui.utils.EbsPhoneNumberValidator;
import de.symeda.sormas.ui.utils.FieldHelper;
import de.symeda.sormas.ui.utils.NullableOptionGroup;

@SuppressWarnings("deprecation")
public class EbsDataForm extends AbstractEditForm<EbsDto> {

	private static final long serialVersionUID = 1L;

	private static final String INFORMATION_SOURCE_HEADING_LOC = "informationSourceHeadingLoc";
	private static final String PLACE_DETECTION_HEADING_LOC = "placeOfDetectionHeadingLoc";
	private static final String CONTACT_PHONE_NUMBER_WARNING_LOC = "contactPhoneNumberWarningLoc";
	public final String THE_DATE_OF_REPORT_CANNOT_BE_EARLIER_THAN_THE_DATE_OF_OCCURRENCE =
		"The Date of Report cannot be earlier than the Date of Occurrence.";

	private static final String STATUS_CHANGE = "statusChange";

	//@formatter:off
    private static final String HTML_LAYOUT =
            loc(INFORMATION_SOURCE_HEADING_LOC) +
                    fluidRowLocs(EbsDto.SOURCE_INFORMATION, EbsDto.CATEGORY_OF_INFORMANT, EbsDto.REPORT_DATE_TIME) +
                    fluidRowLocs(EbsDto.INFORMANT_TEL,EbsDto.INFORMANT_NAME, EbsDto.OTHER_INFORMANT) +
                    fluidRowLocs(EbsDto.SCANNING_TYPE, EbsDto.AUTOMATIC_SCANNING_TYPE) +
                    fluidRowLocs(EbsDto.SOURCE_NAME,EbsDto.MANUAL_SCANNING_TYPE,EbsDto.SOURCE_URL,EbsDto.OTHER) +

                    loc(PLACE_DETECTION_HEADING_LOC) +
                    fluidRowLocs(EbsDto.EBS_LOCATION) +
                    fluidRowLocs(EbsDto.EBS_LATITUDE, EbsDto.EBS_LONGITUDE, EbsDto.EBS_LATLONG) +
                    fluidRowLocs(EbsDto.DATE_ONSET, EbsDto.TIME_ONSET) +
                    fluidRowLocs(EbsDto.DESCRIPTION_OCCURRENCE) +
                    fluidRowLocs(EbsDto.PERSON_DESIGNATION,EbsDto.PERSON_REGISTERING,EbsDto.PERSON_PHONE);
    //@formatter:on

	private final Boolean isCreateForm;
	private final boolean isPseudonymized;
	private final boolean inJurisdiction;
	private List<UserReferenceDto> regionEventResponsibles = new ArrayList<>();
	private List<UserReferenceDto> districtEventResponsibles = new ArrayList<>();
	private LocationEditForm locationForm;
	private final EbsDto ebs;
	DateField reportDate;
	DateTimeField occurrenceDate;
	EbsDateValidator validator = new EbsDateValidator(THE_DATE_OF_REPORT_CANNOT_BE_EARLIER_THAN_THE_DATE_OF_OCCURRENCE, false);

	public EbsDataForm(EbsDto ebsDto, boolean create, boolean isPseudonymized, boolean inJurisdiction) {
		super(
			EbsDto.class,
			EbsDto.I18N_PREFIX,
			false,
			FieldVisibilityCheckers.withCountry(FacadeProvider.getConfigFacade().getCountryLocale()),
			createFieldAccessCheckers(isPseudonymized, true),
			ebsDto);

		isCreateForm = create;
		this.ebs = ebsDto;
		this.isPseudonymized = isPseudonymized;
		this.inJurisdiction = inJurisdiction;

		if (create) {
			hideValidationUntilNextCommit();
		}
		VerticalLayout statusChangeLayout = new VerticalLayout();
		statusChangeLayout.setSpacing(false);
		statusChangeLayout.setMargin(false);
		getContent().addComponent(statusChangeLayout, STATUS_CHANGE);

		addFields();
	}

	private static UiFieldAccessCheckers createFieldAccessCheckers(boolean isPseudonymized, boolean withPersonalAndSensitive) {

		if (withPersonalAndSensitive) {
			return UiFieldAccessCheckers.getDefault(isPseudonymized);
		}

		return UiFieldAccessCheckers.getNoop();
	}

	@Override
	protected void addFields() {
		if (isCreateForm == null) {
			return;
		}
		Label informationSourceHeadingLabel = new Label(I18nProperties.getString(Strings.headingInformationSource));
		informationSourceHeadingLabel.addStyleName(H3);
		getContent().addComponent(informationSourceHeadingLabel, INFORMATION_SOURCE_HEADING_LOC);

		Label locationHeadingLabel = new Label(I18nProperties.getString(Strings.headingLocation));
		locationHeadingLabel.addStyleName(H3);
		getContent().addComponent(locationHeadingLabel, PLACE_DETECTION_HEADING_LOC);
		addField(EbsDto.INFORMANT_NAME, TextField.class);
		TextField contactPhone = addField(EbsDto.INFORMANT_TEL, TextField.class);
		addField(EbsDto.OTHER_INFORMANT, TextField.class);
		contactPhone
			.addValidator(new EbsPhoneNumberValidator(I18nProperties.getValidationError(Validations.validPhoneNumber, contactPhone.getCaption())));
		Label contactPhoneLabel = new Label(I18nProperties.getString(Strings.messageEventExternalTokenWarning));
		contactPhoneLabel.addStyleNames(VSPACE_3, LABEL_WHITE_SPACE_NORMAL);
		getContent().addComponent(contactPhoneLabel, CONTACT_PHONE_NUMBER_WARNING_LOC);

		addField(EbsDto.INTERNAL_TOKEN);

		reportDate = addField(EbsDto.REPORT_DATE_TIME, DateField.class);
		ComboBox categoryInformant = addField(EbsDto.CATEGORY_OF_INFORMANT, ComboBox.class);

		ComboBox srcType = addField(EbsDto.SOURCE_INFORMATION);

		NullableOptionGroup scanningType = addField(EbsDto.SCANNING_TYPE, NullableOptionGroup.class);
		ComboBox automaticScanningType = addField(EbsDto.AUTOMATIC_SCANNING_TYPE, ComboBox.class);
		ComboBox manualScanningType = addField(EbsDto.MANUAL_SCANNING_TYPE, ComboBox.class);
		addField(EbsDto.OTHER, TextField.class);
		TextField sourceName = addField(EbsDto.SOURCE_NAME);
		addField(EbsDto.SOURCE_URL);
		occurrenceDate = addField(EbsDto.DATE_ONSET, DateTimeField.class);
		TextArea descriptionOccurrence = addField(EbsDto.DESCRIPTION_OCCURRENCE, TextArea.class);
		descriptionOccurrence.setRows(4);
		TextField personDesignation = addField(EbsDto.PERSON_DESIGNATION, TextField.class);
		addField(EbsDto.PERSON_REGISTERING, TextField.class);
		TextField personPhone = addField(EbsDto.PERSON_PHONE, TextField.class);
		personPhone
			.addValidator(new EbsPhoneNumberValidator(I18nProperties.getValidationError(Validations.validPhoneNumber, personPhone.getCaption())));
		addField(EbsDto.EBS_LOCATION, new LocationEditForm(fieldVisibilityCheckers, createFieldAccessCheckers(isPseudonymized, false)))
			.setCaption(null);

		locationForm = (LocationEditForm) getFieldGroup().getField(EbsDto.EBS_LOCATION);
		locationForm.setDistrictRequiredOnDefaultCountry(true);

		ComboBox regionField = (ComboBox) locationForm.getFieldGroup().getField(LocationDto.REGION);
		ComboBox districtField = (ComboBox) locationForm.getFieldGroup().getField(LocationDto.DISTRICT);
		addField(EbsDto.EBS_LATITUDE, TextField.class);
		addField(EbsDto.EBS_LONGITUDE, TextField.class);
		addField(EbsDto.EBS_LATLONG, TextField.class);
		ComboBox responsibleUserField = addField(EbsDto.RESPONSIBLE_USER, ComboBox.class);
		responsibleUserField.setNullSelectionAllowed(true);

		addField(EbsDto.DELETION_REASON);
		addField(EbsDto.OTHER_DELETION_REASON, TextArea.class).setRows(3);
		setVisible(false, EbsDto.DELETION_REASON, EbsDto.OTHER_DELETION_REASON);

		if (isCreateForm) {
			locationForm.hideValidationUntilNextCommit();
		}

		initializeVisibilitiesAndAllowedVisibilities();
		initializeAccessAndAllowedAccesses();
		locationForm.hideForEbsForm();
		setRequired(
			true,
			EbsDto.SOURCE_INFORMATION,
			EbsDto.REPORT_DATE_TIME,
			EbsDto.END_DATE,
			EbsDto.CATEGORY_OF_INFORMANT,
			EbsDto.INFORMANT_TEL,
			EbsDto.DESCRIPTION_OCCURRENCE,
			EbsDto.PERSON_DESIGNATION,
			EbsDto.PERSON_REGISTERING,
			EbsDto.PERSON_PHONE);

		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			Arrays.asList(EbsDto.INFORMANT_NAME),
			EbsDto.SOURCE_INFORMATION,
			Arrays.asList(EbsSourceType.CEBS, EbsSourceType.HEBS, EbsSourceType.HOTLINE_PERSON),
			true);

		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			Arrays.asList(EbsDto.SCANNING_TYPE),
			EbsDto.SOURCE_INFORMATION,
			Arrays.asList(EbsSourceType.MEDIA_NEWS),
			true);
		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			Arrays.asList(EbsDto.AUTOMATIC_SCANNING_TYPE),
			EbsDto.SCANNING_TYPE,
			Arrays.asList(MediaScannningType.AUTOMATIC),
			true);
		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			Arrays.asList(EbsDto.MANUAL_SCANNING_TYPE),
			EbsDto.SCANNING_TYPE,
			Arrays.asList(MediaScannningType.MANUAL),
			true);
		FieldHelper
			.setVisibleWhen(getFieldGroup(), Arrays.asList(EbsDto.OTHER), EbsDto.MANUAL_SCANNING_TYPE, Arrays.asList(ManualScanningType.OTHER), true);
		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			Arrays.asList(EbsDto.SOURCE_NAME),
			EbsDto.SOURCE_INFORMATION,
			Arrays.asList(EbsSourceType.MEDIA_NEWS),
			true);
		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			Arrays.asList(EbsDto.SOURCE_URL),
			EbsDto.MANUAL_SCANNING_TYPE,
			Arrays.asList(ManualScanningType.ONLINE),
			true);
		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			Arrays.asList(EbsDto.OTHER_INFORMANT),
			EbsDto.CATEGORY_OF_INFORMANT,
			Arrays.asList(PersonReporting.OTHER),
			true);

		regionField.addValueChangeListener(e -> {
			RegionReferenceDto region = (RegionReferenceDto) regionField.getValue();
			if (region != null) {
				regionEventResponsibles = FacadeProvider.getUserFacade().getUsersByRegionAndRights(region, null, UserRight.EVENT_RESPONSIBLE);
			} else {
				regionEventResponsibles.clear();
			}
			addRegionAndDistrict(responsibleUserField);
		});

		districtField.addValueChangeListener(e -> {
			DistrictReferenceDto district = (DistrictReferenceDto) districtField.getValue();
			if (district != null) {
				districtEventResponsibles = FacadeProvider.getUserFacade().getUserRefsByDistrict(district, null, UserRight.EVENT_RESPONSIBLE);
			} else {
				districtEventResponsibles.clear();
			}
			addRegionAndDistrict(responsibleUserField);
		});

		FieldHelper.addSoftRequiredStyle(
			responsibleUserField,
			srcType,
			scanningType,
			automaticScanningType,
			categoryInformant,
			contactPhone,
			descriptionOccurrence,
			personDesignation);

		srcType.addValueChangeListener(valueChangeEvent -> {
			List<PersonReporting> itemsToAdd;

			Object value = srcType.getValue();
			if (value == null)
				return;
			if (value.equals(EbsSourceType.CEBS)) {
				itemsToAdd = Arrays.asList(
					PersonReporting.COMMUNITY_SURVEILLANCE_VOLUNTEER,
					PersonReporting.COMMUNITY_ANIMAL_HEALTH_WORKER,
					PersonReporting.OTC_CHEMICAL_WORKER,
					PersonReporting.COMMUNITY_MEMBER,
					PersonReporting.OTHER);
			} else if (value.equals(EbsSourceType.HEBS)) {
				itemsToAdd = Arrays.asList(
					PersonReporting.PUBLIC_HEALTHCARE,
					PersonReporting.PRIVATE_HEALTH,
					PersonReporting.REFERENCE_LABORATORY,
					PersonReporting.OTHER);
			} else if (value.equals(EbsSourceType.MEDIA_NEWS)) {
				itemsToAdd = Arrays
					.asList(PersonReporting.PERSON_DISTRICT, PersonReporting.PERSON_REGION, PersonReporting.PERSON_NATIONAL, PersonReporting.OTHER);
			} else if (value.equals(EbsSourceType.HOTLINE_PERSON)) {
				itemsToAdd = Arrays.asList(PersonReporting.GENERAL_PUBLIC_INFORMANT, PersonReporting.INSTITUTIONAL_INFORMANT, PersonReporting.OTHER);
			} else {
				itemsToAdd = Collections.emptyList();
			}

			Arrays.stream(PersonReporting.values())
				.filter(personReporting -> !itemsToAdd.contains(personReporting))
				.forEach(personReporting -> categoryInformant.removeItem(personReporting));

			FieldHelper.updateEnumData(categoryInformant, itemsToAdd);

		});

		categoryInformant.addValueChangeListener(valueChangeEvent -> {
			List<MediaScannningType> itemsToAdd;
			Object value = categoryInformant.getValue();
			if (value != PersonReporting.PERSON_NATIONAL) {
				itemsToAdd = Arrays.asList(MediaScannningType.MANUAL);
			} else {
				itemsToAdd = Arrays.asList(MediaScannningType.MANUAL, MediaScannningType.AUTOMATIC);
			}
			Arrays.stream(MediaScannningType.values())
				.filter(scanType -> !itemsToAdd.contains(scanType))
				.forEach(scanType -> scanningType.removeItem(scanType));

			FieldHelper.updateEnumData(scanningType, itemsToAdd);
		});

		manualScanningType.addValueChangeListener(valueChangeEvent -> {
			Object value = manualScanningType.getValue();
			if (value == null)
				return;
			if (value.equals(ManualScanningType.ONLINE)) {
				sourceName.setCaption("NAME OF WEBSITE");
				contactPhone.setVisible(false);
			} else {
				sourceName.setCaption("NAME OF MEDIA");
				contactPhone.setVisible(false);
			}
		});
		reportDate.addValueChangeListener(valueChangeEvent -> validateDateFields());
		occurrenceDate.addValueChangeListener(valueChangeEvent -> validateDateFields());

		validateDateFields();

	}

	private void addRegionAndDistrict(ComboBox responsibleUserField) {
		List<UserReferenceDto> responsibleUsers = new ArrayList<>();
		responsibleUsers.addAll(regionEventResponsibles);
		responsibleUsers.addAll(districtEventResponsibles);

		FieldHelper.updateItems(responsibleUserField, responsibleUsers);
	}

	public void validateDateFields() {
		DateField dateOfReport = reportDate;
		DateTimeField dateOfOccurrence = occurrenceDate;
		if (dateOfReport.getValue() != null && dateOfOccurrence.getValue() != null && dateOfReport.getValue().before(dateOfOccurrence.getValue())) {
			Date dateOfReportDate = clearTime(dateOfReport.getValue());
			Date dateOfOccurrenceDate = clearTime(dateOfOccurrence.getValue());
			if (!dateOfReportDate.toString().equals(dateOfOccurrenceDate.toString())) {
				dateOfReport.addValidator(validator);
				addDateValidator();
			} else {
				dateOfReport.removeAllValidators();
			}
		} else {
			dateOfReport.removeAllValidators();
		}
	}

	private Date clearTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	@Override
	protected String createHtmlLayout() {
		return HTML_LAYOUT;
	}

	@Override
	public EbsDto getValue() {
		final EbsDto EbsDto = super.getValue();
		return EbsDto;
	}

	@Override
	public void setValue(EbsDto newFieldValue) throws ReadOnlyException, Converter.ConversionException {
		if (!isCreateForm && FacadeProvider.getEventFacade().hasAnyEventParticipantWithoutJurisdiction(newFieldValue.getUuid())) {
			locationForm.setFieldsRequirement(true, LocationDto.REGION, LocationDto.DISTRICT);
			locationForm.setCountryDisabledWithHint(I18nProperties.getString(Strings.infoCountryNotEditableEventParticipantsWithoutJurisdiction));
		}

		super.setValue(newFieldValue);

		// HACK: Binding to the fields will call field listeners that may clear/modify the values of other fields.
		// this hopefully resets everything to its correct value
		locationForm.discard();
	}

	private void addDateValidator() {
		validator.setValidDate(false);
		reportDate.removeValidator(validator);
		reportDate.addValidator(validator);
	}

}
