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
package de.symeda.sormas.ui.location;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.server.Sizeable;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.InfrastructureDataReferenceDto;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.infrastructure.area.AreaType;
import de.symeda.sormas.api.infrastructure.facility.*;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.ui.caze.CaseDataForm;
import de.symeda.sormas.ui.caze.CasePersonView;
import de.symeda.sormas.ui.person.PersonEditForm;
import de.symeda.sormas.ui.person.PersonFormConfiguration;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.ErrorLevel;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.data.validator.EmailValidator;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.TextField;

import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.geo.GeoLatLon;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.api.infrastructure.continent.ContinentCriteria;
import de.symeda.sormas.api.infrastructure.continent.ContinentReferenceDto;
import de.symeda.sormas.api.infrastructure.country.CountryReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.infrastructure.subcontinent.SubcontinentCriteria;
import de.symeda.sormas.api.infrastructure.subcontinent.SubcontinentReferenceDto;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.api.person.PersonAddressType;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.map.LeafletMap;
import de.symeda.sormas.ui.map.LeafletMarker;
import de.symeda.sormas.ui.map.MarkerIcon;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.ComboBoxHelper;
import de.symeda.sormas.ui.utils.ComboBoxWithPlaceholder;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.FieldHelper;
import de.symeda.sormas.ui.utils.InfrastructureFieldsHelper;
import de.symeda.sormas.ui.utils.PhoneNumberValidator;
import de.symeda.sormas.ui.utils.StringToAngularLocationConverter;
import de.symeda.sormas.ui.utils.VaadinUiUtil;

import static de.symeda.sormas.ui.utils.CssStyles.H3;
import static de.symeda.sormas.ui.utils.LayoutUtil.*;

public class LocationEditForm extends AbstractEditForm<LocationDto> {

	private static final long serialVersionUID = 1L;

	private static final String FACILITY_TYPE_GROUP_LOC = "typeGroupLoc";
	private static final String GEO_BUTTONS_LOC = "geoButtons";
	private static final String COUNTRY_HINT_LOC = "countryHintLoc";
	private static final String HOME_ADDRESS_HEADING = "homeAddress";

	private static final String HTML_LAYOUT =
		//XXX #1620 are the divs needed?
		divs(
			fluidRowLocs(LocationDto.COUNTRY, COUNTRY_HINT_LOC, ""),
			fluidRowLocs(LocationDto.ADDRESS_TYPE, LocationDto.ADDRESS_TYPE_DETAILS, ""),
			//fluidRowLocs(LocationDto.CONTINENT, LocationDto.SUB_CONTINENT, ""),
			fluidRowLocs(LocationDto.REGION, LocationDto.DISTRICT, LocationDto.COMMUNITY),
			//fluidRowLocs(FACILITY_TYPE_GROUP_LOC, LocationDto.FACILITY_TYPE),
			//fluidRowLocs(LocationDto.FACILITY, LocationDto.FACILITY_DETAILS),
			fluidRowLocs(6,LocationDto.STREET),
			loc(HOME_ADDRESS_HEADING),
			fluidRowLocs(6,LocationDto.ADDITIONAL_INFORMATION),
			fluidRowLocs(6, LocationDto.RESIDENTIAL_ADDRESS),
			fluidRowLocs(LocationDto.HOUSE_NUMBER, LocationDto.LOCALITY),
			fluidRowLocs(LocationDto.CITY, LocationDto.AREA_TYPE),
			fluidRowLocs(LocationDto.VILLAGE, LocationDto.ZONE),
			fluidRowLocs(6,LocationDto.POSTAL_CODE),
			fluidRowLocs(6,LocationDto.LAND_MARK),
			fluidRowLocs(6, LocationDto.NEAREST_HEALTH_FACILITY_TO_VILLAGE),
			fluidRowLocs(LocationDto.CONTACT_PERSON_FIRST_NAME, LocationDto.CONTACT_PERSON_LAST_NAME),
			fluidRowLocs(LocationDto.CONTACT_PERSON_PHONE, LocationDto.CONTACT_PERSON_EMAIL),
			fluidRow(
				//fluidColumnLoc(4, 0, LocationDto.DETAILS),
				fluidColumnLoc(2, 0, GEO_BUTTONS_LOC),
				fluidColumnLoc(2, 0, LocationDto.LATITUDE),
				fluidColumnLoc(2, 0, LocationDto.LONGITUDE),
				fluidColumnLoc(2, 0, LocationDto.LAT_LON_ACCURACY)));
	private static final String HTML_LAYOUT_GUINEA_WORM = fluidRowLocs(LocationDto.VILLAGE, LocationDto.ZONE) +
			fluidRowLocs(LocationDto.REGION, LocationDto.DISTRICT, LocationDto.COMMUNITY) +
			fluidRowLocs(LocationDto.AREA_TYPE, LocationDto.LAND_MARK);

	private static final String EBS_LAYOUT = fluidRowLocs(LocationDto.REGION, LocationDto.DISTRICT, LocationDto.COMMUNITY) +
			fluidRowLocs(4,LocationDto.CITY);

	private static final String IDSR_LAYOUT =
			fluidRowLocs(LocationDto.REGION, LocationDto.DISTRICT, LocationDto.COMMUNITY) +
			fluidRowLocs(LocationDto.HOUSE_NUMBER, LocationDto.AREA_TYPE);

	private static final String CSM_LAYOUT =
			fluidRowLocs(LocationDto.REGION, LocationDto.DISTRICT, LocationDto.COMMUNITY) +
					fluidRowLocs(LocationDto.HOUSE_NUMBER, LocationDto.POSTAL_CODE, LocationDto.LAND_MARK) +
					fluidRowLocs(LocationDto.LATITUDE, LocationDto.LONGITUDE, LocationDto.LAT_LON_ACCURACY);

	private static final String YELLOW_FEVER_LAYOUT =
			fluidRowLocs(LocationDto.REGION, LocationDto.DISTRICT, LocationDto.COMMUNITY) +
					fluidRowLocs(LocationDto.HOUSE_NUMBER, LocationDto.CITY, LocationDto.AREA_TYPE) +
					fluidRowLocs(LocationDto.POSTAL_CODE, LocationDto.LAND_MARK) +
					fluidRowLocs(LocationDto.LATITUDE, LocationDto.LONGITUDE);

	private MapPopupView leafletMapPopup;
	private ComboBox addressType;
	private ComboBoxWithPlaceholder facilityTypeGroup;
	private ComboBox facilityType;
	private ComboBox facility;
	private TextField facilityDetails;
	private ComboBox continent;
	private ComboBox subcontinent;
	private ComboBox country;
	private ComboBox region;
	private ComboBox district;
	private ComboBox community;
	private ComboBox nameOfFacilityField;
	private ComboBox areaType;
	private TextField contactPersonFirstName;
	private TextField contactPersonLastName;
	private TextField contactPersonPhone;
	private TextField contactPersonEmail;
	private TextField additionalInformationField;
	private TextField  postalCodeField;
	private TextField localityField;
	private boolean districtRequiredOnDefaultCountry;
	private boolean skipCountryValueChange;
	private boolean skipFacilityTypeUpdate;
	private boolean disableFacilityAddressCheck;
	private boolean hasEventParticipantsWithoutJurisdiction;
	private TextField zoneTextField;
	private TextField villageTextField;
	private TextField nearestHealthFacilityToVillage;
	private TextField  houseNumberField;
	private TextField residentialAddress;
	private Disease caseDisease;
	private ComboBox facilityName;

	public LocationEditForm(FieldVisibilityCheckers fieldVisibilityCheckers, UiFieldAccessCheckers fieldAccessCheckers) {
		super(LocationDto.class, LocationDto.I18N_PREFIX, true, fieldVisibilityCheckers, fieldAccessCheckers);

		if (FacadeProvider.getGeocodingFacade().isEnabled() && isEditableAllowed(LocationDto.LATITUDE) && isEditableAllowed(LocationDto.LONGITUDE)) {
			getContent().addComponent(createGeoButton(), GEO_BUTTONS_LOC);
		}
	}

	public LocationEditForm(FieldVisibilityCheckers fieldVisibilityCheckers, UiFieldAccessCheckers fieldAccessCheckers, Disease disease) {
		super(LocationDto.class, LocationDto.I18N_PREFIX, true, fieldVisibilityCheckers, fieldAccessCheckers, disease);
		this.caseDisease = disease;


		if (FacadeProvider.getGeocodingFacade().isEnabled() && isEditableAllowed(LocationDto.LATITUDE) && isEditableAllowed(LocationDto.LONGITUDE)) {
			getContent().addComponent(createGeoButton(), GEO_BUTTONS_LOC);
		}
	}

	public ComboBox getFacilityTypeGroup() {
		return facilityTypeGroup;
	}
	public ComboBox getFacilityType() {
		return facilityType;
	}

	private void setConvertedValue(String propertyId, Object value) {
		((AbstractField<?>) getField(propertyId)).setConvertedValue(value);
	}

	@SuppressWarnings("unchecked")
	private <T> T getConvertedValue(String propertyId) {
		return (T) ((AbstractField<?>) getField(propertyId)).getConvertedValue();
	}

	public void setFieldsRequirement(boolean required, String... fieldIds) {
		setRequired(required, fieldIds);
	}

	public void setSkipFacilityTypeUpdate(boolean skipFacilityTypeUpdate) {
		this.skipFacilityTypeUpdate = skipFacilityTypeUpdate;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void addFields() {

		caseDisease = getCaseDisease();


		addressType = addField(LocationDto.ADDRESS_TYPE, ComboBox.class);
		addressType.setVisible(false);
		final PersonAddressType[] personAddressTypeValues = PersonAddressType.getValues(FacadeProvider.getConfigFacade().getCountryCode());
		if (!isConfiguredServer("ch")) {
			addressType.removeAllItems();
			addressType.setItemCaptionMode(AbstractSelect.ItemCaptionMode.ID);
			addressType.addItems(personAddressTypeValues);
		}
		TextField addressTypeDetails = addField(LocationDto.ADDRESS_TYPE_DETAILS, TextField.class);
		addressTypeDetails.setVisible(false);
		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			LocationDto.ADDRESS_TYPE_DETAILS,
			addressType,
			Arrays.stream(personAddressTypeValues).filter(pat -> !pat.equals(PersonAddressType.HOME)).collect(Collectors.toList()),
			true);
		FieldHelper.setRequiredWhen(
			getFieldGroup(),
			addressType,
			Arrays.asList(LocationDto.ADDRESS_TYPE_DETAILS),
			Arrays.asList(PersonAddressType.OTHER_ADDRESS));

		facilityTypeGroup = ComboBoxHelper.createComboBoxV7();;
		facilityTypeGroup.setId("typeGroup");
		facilityTypeGroup.setCaption(I18nProperties.getCaption(Captions.Facility_typeGroup));
		facilityTypeGroup.setWidth(100, Unit.PERCENTAGE);
		facilityTypeGroup.addItems(FacilityTypeGroup.values());
		getContent().addComponent(facilityTypeGroup, FACILITY_TYPE_GROUP_LOC);
		facilityType = addField(LocationDto.FACILITY_TYPE, ComboBoxWithPlaceholder.class);
		facility = addInfrastructureField(LocationDto.FACILITY);
		facility.setImmediate(true);
		facilityDetails = addField(LocationDto.FACILITY_DETAILS, TextField.class);
		facilityDetails.setVisible(false);

		addressType.addValueChangeListener(e -> {
			FacilityTypeGroup oldGroup = (FacilityTypeGroup) facilityTypeGroup.getValue();
			FacilityType oldType = (FacilityType) facilityType.getValue();
			FacilityReferenceDto oldFacility = (FacilityReferenceDto) facility.getValue();
			String oldDetails = facilityDetails.getValue();
			if (PersonAddressType.HOME.equals(addressType.getValue())) {
				facilityTypeGroup.removeAllItems();
				facilityTypeGroup.addItems(FacilityTypeGroup.getAccomodationGroups());
				setOldFacilityValuesIfPossible(oldGroup, oldType, oldFacility, oldDetails);
			} else {
				facilityTypeGroup.removeAllItems();
				facilityTypeGroup.addItems(FacilityTypeGroup.values());
				setOldFacilityValuesIfPossible(oldGroup, oldType, oldFacility, oldDetails);
			}
		});

		TextField streetField = addField(LocationDto.STREET, TextField.class);
		streetField.setVisible(false);
		localityField = addField(LocationDto.LOCALITY, TextField.class);
		localityField.setVisible(false);
		houseNumberField = addField(LocationDto.HOUSE_NUMBER, TextField.class);
		TextField landMark = addField(LocationDto.LAND_MARK, TextField.class);
		landMark.setVisible(false);
		additionalInformationField = addField(LocationDto.ADDITIONAL_INFORMATION, TextField.class);
		additionalInformationField.setVisible(false);
		addField(LocationDto.DETAILS, TextField.class);
		TextField cityField = addField(LocationDto.CITY, TextField.class);
		postalCodeField = addField(LocationDto.POSTAL_CODE, TextField.class);

		residentialAddress = addField(LocationDto.RESIDENTIAL_ADDRESS, TextField.class);
		residentialAddress.setVisible(false);

		areaType = addField(LocationDto.AREA_TYPE, ComboBox.class);
		areaType.removeItem(AreaType.UNKNOWN);
		areaType.setVisible(true);
		areaType.setDescription(I18nProperties.getDescription(getPropertyI18nPrefix() + "." + LocationDto.AREA_TYPE));

		zoneTextField = addField(LocationDto.ZONE, TextField.class);
		villageTextField = addField(LocationDto.VILLAGE, TextField.class);
		setVisible(false, LocationDto.ZONE, LocationDto.VILLAGE);

		contactPersonFirstName = addField(LocationDto.CONTACT_PERSON_FIRST_NAME, TextField.class);
		contactPersonLastName = addField(LocationDto.CONTACT_PERSON_LAST_NAME, TextField.class);
		contactPersonPhone = addField(LocationDto.CONTACT_PERSON_PHONE, TextField.class);
		contactPersonPhone
			.addValidator(new PhoneNumberValidator(I18nProperties.getValidationError(Validations.validPhoneNumber, contactPersonPhone.getCaption())));
		contactPersonEmail = addField(LocationDto.CONTACT_PERSON_EMAIL, TextField.class);
		contactPersonEmail
			.addValidator(new EmailValidator(I18nProperties.getValidationError(Validations.validEmailAddress, contactPersonEmail.getCaption())));

		final AccessibleTextField tfLatitude = addField(LocationDto.LATITUDE, AccessibleTextField.class);
		final AccessibleTextField tfLongitude = addField(LocationDto.LONGITUDE, AccessibleTextField.class);
		final AccessibleTextField tfAccuracy = addField(LocationDto.LAT_LON_ACCURACY, AccessibleTextField.class);
		final StringToAngularLocationConverter stringToAngularLocationConverter = new StringToAngularLocationConverter();
		tfLatitude.setConverter(stringToAngularLocationConverter);
		tfLongitude.setConverter(stringToAngularLocationConverter);
		tfAccuracy.setConverter(stringToAngularLocationConverter);

		tfLatitude.setVisible(false);
		tfLongitude.setVisible(false);
		tfAccuracy.setVisible(false);

		continent = addInfrastructureField(LocationDto.CONTINENT);
		subcontinent = addInfrastructureField(LocationDto.SUB_CONTINENT);
		country = addInfrastructureField(LocationDto.COUNTRY);
		country.setVisible(false);
		region = addInfrastructureField(LocationDto.REGION);
		district = addInfrastructureField(LocationDto.DISTRICT);
		community = addInfrastructureField(LocationDto.COMMUNITY);
		community.setCaption("Sub-District");
		facilityName = new ComboBox("Facility");

		continent.setVisible(false);
		subcontinent.setVisible(false);

		initializeVisibilitiesAndAllowedVisibilities();
		initializeAccessAndAllowedAccesses();

		nearestHealthFacilityToVillage = addField(LocationDto.NEAREST_HEALTH_FACILITY_TO_VILLAGE, TextField.class);
		nearestHealthFacilityToVillage.setVisible(false);

		if (!isEditableAllowed(LocationDto.COMMUNITY)) {
			setEnabled(false, LocationDto.COUNTRY, LocationDto.REGION, LocationDto.DISTRICT);
		}
		if (!isEditableAllowed(LocationDto.FACILITY)) {
			setEnabled(false, LocationDto.FACILITY_TYPE, LocationDto.FACILITY_DETAILS);
			FieldHelper.setComboInaccessible(facilityTypeGroup);
		}

		ValueChangeListener continentValueListener = e -> {
			if (continent.isVisible()) {
				ContinentReferenceDto continentReferenceDto = (ContinentReferenceDto) e.getProperty().getValue();
				if (subcontinent.getValue() == null) {
					FieldHelper.updateItems(
						country,
						continentReferenceDto != null
							? FacadeProvider.getCountryFacade().getAllActiveByContinent(continentReferenceDto.getUuid())
							: FacadeProvider.getCountryFacade().getAllActiveAsReference());
					country.setValue(null);
				}
				subcontinent.setValue(null);
				FieldHelper.updateItems(
					subcontinent,
					continentReferenceDto != null
						? FacadeProvider.getSubcontinentFacade().getAllActiveByContinent(continentReferenceDto.getUuid())
						: FacadeProvider.getSubcontinentFacade().getAllActiveAsReference());
			}
		};

		ValueChangeListener subContinentValueListener = e -> {
			if (subcontinent.isVisible()) {
				SubcontinentReferenceDto subcontinentReferenceDto = (SubcontinentReferenceDto) e.getProperty().getValue();

				if (subcontinentReferenceDto != null) {
					continent.removeValueChangeListener(continentValueListener);
					continent.setValue(FacadeProvider.getContinentFacade().getBySubcontinent(subcontinentReferenceDto));
					continent.addValueChangeListener(continentValueListener);
				}

				country.setValue(null);

				ContinentReferenceDto continentValue = (ContinentReferenceDto) continent.getValue();
				FieldHelper.updateItems(
					country,
					subcontinentReferenceDto != null
						? FacadeProvider.getCountryFacade().getAllActiveBySubcontinent(subcontinentReferenceDto.getUuid())
						: continentValue == null
							? FacadeProvider.getCountryFacade().getAllActiveAsReference()
							: FacadeProvider.getCountryFacade().getAllActiveByContinent(continentValue.getUuid()));
			}
		};

		continent.addValueChangeListener(continentValueListener);
		subcontinent.addValueChangeListener(subContinentValueListener);
		skipCountryValueChange = false;
		country.addValueChangeListener(e -> {
			if (!skipCountryValueChange) {
				CountryReferenceDto countryDto = (CountryReferenceDto) e.getProperty().getValue();
				if (countryDto != null) {
					final ContinentReferenceDto countryContinent = FacadeProvider.getContinentFacade().getByCountry(countryDto);
					final SubcontinentReferenceDto countrySubcontinent = FacadeProvider.getSubcontinentFacade().getByCountry(countryDto);

                    setVisible("Ghana".equals(countryDto.getCaption()), LocationDto.REGION, LocationDto.DISTRICT, LocationDto.COMMUNITY, LocationDto.STREET, LocationDto.HOUSE_NUMBER, LocationDto.ADDITIONAL_INFORMATION,
                            LocationDto.POSTAL_CODE, LocationDto.CITY, LocationDto.AREA_TYPE, LocationDto.DETAILS);

					if (countryContinent != null) {
						continent.removeValueChangeListener(continentValueListener);
						if (continent.isVisible()) {
							skipCountryValueChange = true;
							FieldHelper.updateItems(country, FacadeProvider.getCountryFacade().getAllActiveByContinent(countryContinent.getUuid()));
							skipCountryValueChange = false;
						}
						continent.setValue(countryContinent);
						continent.addValueChangeListener(continentValueListener);
					}
					if (countrySubcontinent != null) {
						subcontinent.removeValueChangeListener(subContinentValueListener);
						if (subcontinent.isVisible()) {
							skipCountryValueChange = true;
							if (countryContinent != null) {
								FieldHelper.updateItems(
									subcontinent,
									FacadeProvider.getSubcontinentFacade().getAllActiveByContinent(countryContinent.getUuid()));
							}
							FieldHelper
								.updateItems(country, FacadeProvider.getCountryFacade().getAllActiveBySubcontinent(countrySubcontinent.getUuid()));
							skipCountryValueChange = false;
						}
						subcontinent.setValue(countrySubcontinent);
						subcontinent.addValueChangeListener(subContinentValueListener);
					}
				}
			}
		});

		region.addValueChangeListener(e -> {
			RegionReferenceDto regionDto = (RegionReferenceDto) e.getProperty().getValue();
			FieldHelper
				.updateItems(district, regionDto != null ? FacadeProvider.getDistrictFacade().getAllActiveByRegion(regionDto.getUuid()) : null);
			updateFacilities();
		});
		district.addValueChangeListener(e -> {
			DistrictReferenceDto districtDto = (DistrictReferenceDto) e.getProperty().getValue();
			FieldHelper.updateItems(
				community,
				districtDto != null ? FacadeProvider.getCommunityFacade().getAllActiveByDistrict(districtDto.getUuid()) : null);
			if (districtDto == null) {
				FieldHelper.removeItems(facility);
				// Add a visual indictator reminding the user to select a district
				facility.setComponentError(new ErrorMessage() {

					@Override
					public ErrorLevel getErrorLevel() {
						return ErrorLevel.INFO;
					}

					@Override
					public String getFormattedHtmlMessage() {
						return I18nProperties.getString(Strings.infoFacilityNeedsDistrict);
					}
				});
			} else if (facilityType.getValue() != null) {
				facility.setComponentError(null);
				facility.markAsDirty();
				FieldHelper.updateItems(
					facility,
					FacadeProvider.getFacilityFacade()
						.getActiveFacilitiesByDistrictAndType(districtDto, (FacilityType) facilityType.getValue(), true, false, true, true));
			}
			updateFacilities();
		});
		community.addValueChangeListener(e -> {
			CommunityReferenceDto communityDto = (CommunityReferenceDto) e.getProperty().getValue();
			if (facilityType.getValue() != null) {
				FieldHelper.updateItems(
					facility,
					communityDto != null
						? FacadeProvider.getFacilityFacade()
							.getActiveFacilitiesByCommunityAndType(communityDto, (FacilityType) facilityType.getValue(), true, true, true, true)
						: district.getValue() != null
							? FacadeProvider.getFacilityFacade()
								.getActiveFacilitiesByDistrictAndType(
									(DistrictReferenceDto) district.getValue(),
									(FacilityType) facilityType.getValue(),
									true,
									false, true, true)
							: null);
			}
			updateFacilities();
		});
		skipFacilityTypeUpdate = false;
		facilityTypeGroup.addValueChangeListener(e -> {
			if (!skipFacilityTypeUpdate) {
				FieldHelper.removeItems(facility);
				FieldHelper.updateEnumData(facilityType, FacilityType.getTypes((FacilityTypeGroup) facilityTypeGroup.getValue()));
				facilityType.setRequired(facilityTypeGroup.getValue() != null);
			}
		});
		facilityType.addValueChangeListener(e -> {
			FieldHelper.removeItems(facility);
			facility.setComponentError(null);
			facility.markAsDirty();
			if (facilityType.getValue() != null && facilityTypeGroup.getValue() == null) {
				facilityTypeGroup.setValue(((FacilityType) facilityType.getValue()).getFacilityTypeGroup());
			}
			if (facilityType.getValue() != null && district.getValue() != null) {
				if (community.getValue() != null) {
					FieldHelper.updateItems(
						facility,
						FacadeProvider.getFacilityFacade()
							.getActiveFacilitiesByCommunityAndType(
								(CommunityReferenceDto) community.getValue(),
								(FacilityType) facilityType.getValue(),
								true,
								false, true, true));
				} else {
					FieldHelper.updateItems(
						facility,
						FacadeProvider.getFacilityFacade()
							.getActiveFacilitiesByDistrictAndType(
								(DistrictReferenceDto) district.getValue(),
								(FacilityType) facilityType.getValue(),
								true,
								false, true, true));
				}
			} else if (facilityType.getValue() != null && district.getValue() == null) {
				// Add a visual indictator reminding the user to select a district
				facility.setComponentError(new ErrorMessage() {

					@Override
					public ErrorLevel getErrorLevel() {
						return ErrorLevel.INFO;
					}

					@Override
					public String getFormattedHtmlMessage() {
						return I18nProperties.getString(Strings.infoFacilityNeedsDistrict);
					}
				});
			}

			// Only show contactperson-details if at least a faciltytype has been set
			if (facilityType.getValue() != null) {
				setFacilityContactPersonFieldsVisible(true, true);
			} else {
				setFacilityContactPersonFieldsVisible(false, true);
			}
		});
		facility.addValueChangeListener(e -> {
			if (facility.getValue() != null) {
				boolean visibleAndRequired = areFacilityDetailsRequired();

				facilityDetails.setVisible(visibleAndRequired);
				facilityDetails.setRequired(visibleAndRequired);

				if (!visibleAndRequired) {
					facilityDetails.clear();
				} else if (!facility.isAttached()) {
					String facilityDetailsValue = getValue() != null ? getValue().getFacilityDetails() : null;
					facilityDetails.setValue(facilityDetailsValue);
				}
			} else {
				facilityDetails.setVisible(false);
				facilityDetails.setRequired(false);
				facilityDetails.clear();
			}

			// Fill in the address fields based on the selected facility
			// We don't want the location form to automatically change even if the facility's address is updated later
			// on, so we only trigger it upon a manual change of the facility field
			// We use isAttached() to avoid the fuss when initializing the form, it may seems a bit hacky, but it is
			// necessary because isModified() will still return true for a short duration even if we keep the very same
			// value because of this field dependencies to other fields and the way updateEnumValues works
			if (facility.isAttached() && !disableFacilityAddressCheck) {
				if (facility.getValue() != null) {
					FacilityDto facilityDto =
						FacadeProvider.getFacilityFacade().getByUuid(((FacilityReferenceDto) getField(LocationDto.FACILITY).getValue()).getUuid());

					// Only if the facility's address is set
					if (StringUtils.isNotEmpty(facilityDto.getCity())
						|| StringUtils.isNotEmpty(facilityDto.getPostalCode())
						|| StringUtils.isNotEmpty(facilityDto.getStreet())
						|| StringUtils.isNotEmpty(facilityDto.getHouseNumber())
						|| StringUtils.isNotEmpty(facilityDto.getAdditionalInformation())
						|| facilityDto.getAreaType() != null
						|| facilityDto.getLatitude() != null
						|| facilityDto.getLongitude() != null
						|| (StringUtils.isNotEmpty(facilityDto.getContactPersonFirstName())
							&& StringUtils.isNotEmpty(facilityDto.getContactPersonLastName()))) {

						// Show a confirmation popup if the location's address is already set and different from the facility one
						if ((StringUtils.isNotEmpty(cityField.getValue()) && !cityField.getValue().equals(facilityDto.getCity()))
							|| (StringUtils.isNotEmpty(postalCodeField.getValue()) && !postalCodeField.getValue().equals(facilityDto.getPostalCode()))
							|| (StringUtils.isNotEmpty(streetField.getValue()) && !streetField.getValue().equals(facilityDto.getStreet()))
							|| (StringUtils.isNotEmpty(houseNumberField.getValue())
								&& !houseNumberField.getValue().equals(facilityDto.getHouseNumber()))
							|| (StringUtils.isNotEmpty(additionalInformationField.getValue())
								&& !additionalInformationField.getValue().equals(facilityDto.getAdditionalInformation()))
							|| (areaType.getValue() != null && areaType.getValue() != facilityDto.getAreaType())
							|| (StringUtils.isNotEmpty(contactPersonFirstName.getValue()) && StringUtils.isNotEmpty(contactPersonLastName.getValue()))
							|| (tfLatitude.getConvertedValue() != null
								&& Double.compare((Double) tfLatitude.getConvertedValue(), facilityDto.getLatitude()) != 0)
							|| (tfLongitude.getConvertedValue() != null
								&& Double.compare((Double) tfLongitude.getConvertedValue(), facilityDto.getLongitude()) != 0)) {

							VaadinUiUtil.showConfirmationPopup(
								I18nProperties.getString(Strings.headingLocation),
								new Label(I18nProperties.getString(Strings.confirmationLocationFacilityAddressOverride)),
								I18nProperties.getString(Strings.yes),
								I18nProperties.getString(Strings.no),
								640,
								confirmationEvent -> {
									if (confirmationEvent) {
										overrideLocationDetailsWithFacilityOnes(facilityDto);
									}
								});
						} else {
							overrideLocationDetailsWithFacilityOnes(facilityDto);
						}
					}
				}
			}
		});
		final List<ContinentReferenceDto> continents = FacadeProvider.getContinentFacade().getAllActiveAsReference();
		if (continents.isEmpty()) {
			continent.setVisible(false);
			continent.clear();
		} else {
			continent.addItems(continents);
		}
		final List<SubcontinentReferenceDto> subcontinents = FacadeProvider.getSubcontinentFacade().getAllActiveAsReference();
		if (subcontinents.isEmpty()) {
			subcontinent.setVisible(false);
			subcontinent.clear();
		} else {
			subcontinent.addItems(subcontinents);
		}
		country.addItems(FacadeProvider.getCountryFacade().getAllActiveAsReference());
		updateRegionCombo(region, country);
		country.addValueChangeListener(e -> {
			updateRegionCombo(region, country);
			region.setValue(null);
		});

		Stream.of(LocationDto.LATITUDE, LocationDto.LONGITUDE)
			.<Field<?>> map(this::getField)
			.forEach(f -> f.addValueChangeListener(e -> this.updateLeafletMapContent()));

		// Set initial visiblity of facility-contactperson-details (should only be visible if at least a facilityType has been selected)
		setFacilityContactPersonFieldsVisible(facilityType.getValue() != null, true);
	}

	private void updateFacilities() {
		DistrictReferenceDto selectedDistrict = (DistrictReferenceDto) district.getValue();
		CommunityReferenceDto selectedCommunity = (CommunityReferenceDto) community.getValue();

		List<FacilityReferenceDto> facilities = null;
		if (selectedCommunity != null) {
			facilities = FacadeProvider.getFacilityFacade().getActiveHospitalsByCommunity(selectedCommunity, false, false, false);
		} else if (selectedDistrict != null) {
			facilities = FacadeProvider.getFacilityFacade().getActiveHospitalsByDistrict(selectedDistrict, false, false, false);
		}

		if (nameOfFacilityField != null) {
			FieldHelper.updateItems(nameOfFacilityField, facilities);
		}


	}
	public void setNameOfFacilityField(ComboBox nameOfFacilityField) {
		this.nameOfFacilityField = nameOfFacilityField;
	}

	@Override
	public void setValue(LocationDto newFieldValue) throws ReadOnlyException, Converter.ConversionException {
		super.setValue(newFieldValue);

		// HACK: Binding to the fields will call field listeners that may clear/modify the values of other fields.
		// this hopefully resets everything to its correct value
		discard();
	}

	@Override
	public void discard() throws SourceException {
		super.discard();
		LocationDto locationDto = getValue();
		if (locationDto != null) {
			FacilityType facilityType = locationDto.getFacilityType();
			if (facilityType != null) {
				facilityTypeGroup.setValue(facilityType.getFacilityTypeGroup());
			} else {
				facilityTypeGroup.setValue(null);
			}
			facility.setValue(locationDto.getFacility());
			facility.setComponentError(null);
			facilityDetails.setValue(locationDto.getFacilityDetails());
		}
	}

	private void updateRegionCombo(ComboBox region, ComboBox country) {
		InfrastructureFieldsHelper.updateRegionBasedOnCountry(country, region, (isServerCountry) -> {
			if (districtRequiredOnDefaultCountry) {
				setFieldsRequirement(hasEventParticipantsWithoutJurisdiction || isServerCountry, LocationDto.REGION, LocationDto.DISTRICT);
			}
		});
	}

	private void overrideLocationDetailsWithFacilityOnes(FacilityDto facilityDto) {
		((TextField) getField(LocationDto.CITY)).setValue(facilityDto.getCity());
		((TextField) getField(LocationDto.POSTAL_CODE)).setValue(facilityDto.getPostalCode());
		((TextField) getField(LocationDto.STREET)).setValue(facilityDto.getStreet());
		((TextField) getField(LocationDto.HOUSE_NUMBER)).setValue(facilityDto.getHouseNumber());
		((TextField) getField(LocationDto.ADDITIONAL_INFORMATION)).setValue(facilityDto.getAdditionalInformation());
		((ComboBox) getField(LocationDto.AREA_TYPE)).setValue(facilityDto.getAreaType());
		((TextField) getField(LocationDto.CONTACT_PERSON_FIRST_NAME)).setValue(facilityDto.getContactPersonFirstName());
		((TextField) getField(LocationDto.CONTACT_PERSON_LAST_NAME)).setValue(facilityDto.getContactPersonLastName());
		((TextField) getField(LocationDto.CONTACT_PERSON_PHONE)).setValue(facilityDto.getContactPersonPhone());
		((TextField) getField(LocationDto.CONTACT_PERSON_EMAIL)).setValue(facilityDto.getContactPersonEmail());
		((AccessibleTextField) getField(LocationDto.LATITUDE)).setConvertedValue(facilityDto.getLatitude());
		((AccessibleTextField) getField(LocationDto.LONGITUDE)).setConvertedValue(facilityDto.getLongitude());
	}

	private void setOldFacilityValuesIfPossible(
		FacilityTypeGroup oldGroup,
		FacilityType oldType,
		FacilityReferenceDto oldFacility,
		String oldDetails) {
		facilityTypeGroup.setValue(oldGroup);
		facilityType.setValue(oldType);
		facility.setValue(oldFacility);
		facilityDetails.setValue(oldDetails);
	}

	private HorizontalLayout createGeoButton() {
		HorizontalLayout geoButtonLayout = new HorizontalLayout();
		geoButtonLayout.setMargin(false);
		geoButtonLayout.setSpacing(false);

		Button geocodeButton = ButtonHelper.createIconButtonWithCaption("geocodeButton", null, VaadinIcons.MAP_MARKER, e -> {
			triggerGeocoding();
			e.getButton().removeStyleName(CssStyles.GEOCODE_BUTTON_HIGHLIGHT);
		}, ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_LARGE);

		Field[] locationGeoFields = Stream.of(LocationDto.STREET, LocationDto.POSTAL_CODE, LocationDto.CITY, LocationDto.HOUSE_NUMBER)
			.map(field -> (Field) getField(field))
			.toArray(Field[]::new);

		// Highlight geocode-button when the address changes
		Stream.of(locationGeoFields).forEach(field -> field.addValueChangeListener(e -> {
			if (isAllFieldsEmpty(locationGeoFields)) {
				geocodeButton.removeStyleName(CssStyles.GEOCODE_BUTTON_HIGHLIGHT);
			} else if (field.isModified()) {
				geocodeButton.addStyleName(CssStyles.GEOCODE_BUTTON_HIGHLIGHT);
			}
		}));

		geoButtonLayout.addComponent(geocodeButton);
		geoButtonLayout.setComponentAlignment(geocodeButton, Alignment.BOTTOM_RIGHT);

		leafletMapPopup = new MapPopupView();
		leafletMapPopup.setCaption(" ");
		leafletMapPopup.setEnabled(false);
		leafletMapPopup.setStyleName(ValoTheme.BUTTON_LARGE);
		leafletMapPopup.addStyleName(ValoTheme.BUTTON_ICON_ONLY);

		geoButtonLayout.addComponent(leafletMapPopup);
		geoButtonLayout.setComponentAlignment(leafletMapPopup, Alignment.BOTTOM_RIGHT);

		return geoButtonLayout;
	}

	private boolean isAllFieldsEmpty(Field[] fields) {
		return Stream.of(fields).allMatch(Field::isEmpty);
	}

	private void updateLeafletMapContent() {

		if (leafletMapPopup == null) {
			return;
		}

		if (areFieldsValid(LocationDto.LATITUDE, LocationDto.LONGITUDE)) {
			Double lat = getConvertedValue(LocationDto.LATITUDE);
			Double lon = getConvertedValue(LocationDto.LONGITUDE);
			GeoLatLon coordinates;
			if (ObjectUtils.allNotNull(lat, lon)) {
				coordinates = new GeoLatLon(lat, lon);
			} else {
				coordinates = null;
			}
			leafletMapPopup.setEnabled(coordinates != null);
			leafletMapPopup.setCoordinates(coordinates);
		} else {
			leafletMapPopup.setEnabled(false);
		}
	}

	private void triggerGeocoding() {

		String street = getConvertedValue(LocationDto.STREET);
		String houseNumber = getConvertedValue(LocationDto.HOUSE_NUMBER);
		String postalCode = getConvertedValue(LocationDto.POSTAL_CODE);
		String city = getConvertedValue(LocationDto.CITY);

		GeoLatLon latLon = FacadeProvider.getGeocodingFacade().getLatLon(street, houseNumber, postalCode, city);

		if (latLon != null) {
			setConvertedValue(LocationDto.LATITUDE, latLon.getLat());
			setConvertedValue(LocationDto.LONGITUDE, latLon.getLon());
		}
	}

	public void showAddressType() {
		addressType.setVisible(true);
		addressType.setRequired(true);
	}

	public void setDistrictRequiredOnDefaultCountry(boolean required) {
		this.districtRequiredOnDefaultCountry = required;
		if (required) {
			updateRegionCombo(region, country);
		}
	}

	//create a function to hide fields in ebs
	public void hideForEbsForm() {
		setVisible(false, LocationDto.STREET, LocationDto.ADDITIONAL_INFORMATION, LocationDto.LOCALITY,LocationDto.HOUSE_NUMBER,LocationDto.AREA_TYPE,LocationDto.POSTAL_CODE);
//		getField(LocationDto.STREET).setVisible(false);
//		getField(LocationDto.ADDITIONAL_INFORMATION).setVisible(false);
//		getField(LocationDto.LOCALITY).setVisible(false);
//		getField(LocationDto.HOUSE_NUMBER).setVisible(false);
//		getField(LocationDto.AREA_TYPE).setVisible(false);
//		getField(LocationDto.POSTAL_CODE).setVisible(false);
	}

	public void hideForHospitalizationForm(){
		setVisible(false, LocationDto.COMMUNITY);
	}

	public void setCountryDisabledWithHint(String hint) {
		country.setEnabled(false);
		Label infoLabel = new Label(VaadinIcons.INFO_CIRCLE.getHtml(), ContentMode.HTML);
		infoLabel.setSizeUndefined();
		infoLabel.setDescription(hint, ContentMode.HTML);
		CssStyles.style(infoLabel, CssStyles.LABEL_XLARGE, CssStyles.LABEL_SECONDARY, CssStyles.VSPACE_TOP_3);
		getContent().addComponent(infoLabel, COUNTRY_HINT_LOC);
	}

	@Override
	protected String createHtmlLayout() {
		if (caseDisease !=  null) {
			switch (caseDisease) {
				case GUINEA_WORM:
					return HTML_LAYOUT_GUINEA_WORM;
				case IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS:
					return IDSR_LAYOUT;
				case CSM:
					return CSM_LAYOUT;
				case YELLOW_FEVER:
					return YELLOW_FEVER_LAYOUT;
				default:
					return HTML_LAYOUT;
			}

		} else {
			return EBS_LAYOUT;
		}
	}

	@Override
	protected <F extends Field> F addFieldToLayout(CustomLayout layout, String propertyId, F field) {
		field.addValueChangeListener(e -> fireValueChange(false));

		return super.addFieldToLayout(layout, propertyId, field);
	}

	public void setFacilityFieldsVisible(boolean visible, boolean clearOnHidden) {
		facility.setVisible(visible);
		facilityDetails.setVisible(visible && areFacilityDetailsRequired());
		facilityType.setVisible(visible);
		facilityTypeGroup.setVisible(visible);

		setFacilityContactPersonFieldsVisible(visible && (facilityType.getValue() != null), clearOnHidden);

		if (!visible && clearOnHidden) {
			facility.clear();
			facilityDetails.clear();
			facilityType.clear();
			facilityTypeGroup.clear();
			facility.setComponentError(null);
		}
	}

	public void setFacilityFieldsVisibleExceptTypeGroupField(boolean visible, boolean clearOnHidden) {
		facility.setVisible(visible);
		facilityDetails.setVisible(visible && areFacilityDetailsRequired());
		facilityType.setVisible(visible);
		facilityTypeGroup.setVisible(false);

		setFacilityContactPersonFieldsVisible(visible && (facilityType.getValue() != null), clearOnHidden);

		if (!visible && clearOnHidden) {
			facility.clear();
			facilityDetails.clear();
			facilityType.clear();
		}
	}

	private void setFacilityContactPersonFieldsVisible(boolean visible, boolean clearOnHidden) {
		contactPersonFirstName.setVisible(visible);
		contactPersonLastName.setVisible(visible);
		contactPersonPhone.setVisible(visible);
		contactPersonEmail.setVisible(visible);

		if (!visible && clearOnHidden) {
			contactPersonFirstName.clear();
			contactPersonLastName.clear();
			contactPersonPhone.clear();
			contactPersonEmail.clear();
		}
	}

	public void setContinentFieldsVisibility() {
		if (FacadeProvider.getContinentFacade().count(new ContinentCriteria().relevanceStatus(EntityRelevanceStatus.ACTIVE)) == 0) {
			continent.setVisible(false);
			continent.clear();
		} else {
			continent.setVisible(true);
		}
		if (FacadeProvider.getSubcontinentFacade().count(new SubcontinentCriteria().relevanceStatus(EntityRelevanceStatus.ACTIVE)) == 0) {
			subcontinent.setVisible(false);
			subcontinent.clear();
		} else {
			subcontinent.setVisible(true);
		}
	}

	private boolean areFacilityDetailsRequired() {
		return facility.getValue() != null && ((FacilityReferenceDto) facility.getValue()).getUuid().equals(FacilityDto.OTHER_FACILITY_UUID);
	}

	public void setDisableFacilityAddressCheck(boolean disableFacilityAddressCheck) {
		this.disableFacilityAddressCheck = disableFacilityAddressCheck;
	}

	public void setHasEventParticipantsWithoutJurisdiction(boolean hasEventParticipantsWithoutJurisdiction) {
		this.hasEventParticipantsWithoutJurisdiction = hasEventParticipantsWithoutJurisdiction;
	}

	private static class MapPopupView extends PopupView {

		private static final long serialVersionUID = 6119339732442336000L;

		//eye-icon styled as button
		private static final String MINNIMIZED_HTML =
			"<div tabindex=\"0\" role=\"button\" class=\"v-button v-widget icon-only v-button-icon-only borderless v-button-borderless large v-button-large\"><span class=\"v-button-wrap\">"
				+ VaadinIcons.EYE.getHtml() + "<span class=\"v-button-caption\"></span></span></div>";

		private GeoLatLon coordinates = null;

		public MapPopupView() {
			setContent(new Content() {

				private static final long serialVersionUID = -1709597624862512304L;

				@Override
				public String getMinimizedValueAsHTML() {
					return MINNIMIZED_HTML;
				}

				@Override
				public Component getPopupComponent() {
					return createLeafletMap();
				}
			});
		}

		private LeafletMap createLeafletMap() {

			LeafletMap map = new LeafletMap();
			map.setWidth(420, Unit.PIXELS);
			map.setHeight(420, Unit.PIXELS);
			map.setZoom(12);

			map.setCenter(coordinates);

			LeafletMarker marker = new LeafletMarker();
			marker.setLatLon(coordinates);
			marker.setIcon(MarkerIcon.CASE_UNCLASSIFIED);
			marker.setMarkerCount(1);

			map.addMarkerGroup("cases", Collections.singletonList(marker));

			return map;
		}

		public void setCoordinates(GeoLatLon coordinates) {
			this.coordinates = coordinates;
		}
	}
	public void getCurrentDisease(Disease currentDisease){
		if (currentDisease == Disease.FOODBORNE_ILLNESS){

		}
	}

	public void getIncomingDisease(Disease incomingDisease){
		if (incomingDisease == null) {
			return;
		}

		switch (incomingDisease){

			case YELLOW_FEVER:
				setVisible(true, LocationDto.LONGITUDE, LocationDto.LATITUDE, LocationDto.LAND_MARK, LocationDto.CITY);
				break;
			case AHF:
				setVisible(true, LocationDto.LONGITUDE, LocationDto.LATITUDE);
				break;
			case CSM:
				setVisible(true, LocationDto.LONGITUDE, LocationDto.LATITUDE, LocationDto.LAT_LON_ACCURACY, LocationDto.LAND_MARK);
				break;
			case NEW_INFLUENZA:
				localityField.setVisible(true);
				break;
			case AFP:
				additionalInformationField.setCaption("Village");
				setVisible(true, LocationDto.LATITUDE, LocationDto.LONGITUDE);
				break;
			case MEASLES:
				handleMeasles();
				break;
			case CORONAVIRUS:
				handleCoronavirus();
				break;
			case FOODBORNE_ILLNESS:
				createLabel(I18nProperties.getString(Strings.homeAddressheading), H3, HOME_ADDRESS_HEADING);
				localityField.setVisible(true);
				break;
			case IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS:
				houseNumberField.setCaption("House Number/Location");
				break;
			default:
				break;
			}

		hideFieldsForSelectedDisease(incomingDisease);
	}

	public void handleCoronavirus() {
			setVisible(true, LocationDto.POSTAL_CODE, LocationDto.LONGITUDE, LocationDto.LATITUDE, LocationDto.LAND_MARK);
			setVisible(false, LocationDto.ADDITIONAL_INFORMATION, LocationDto.STREET, LocationDto.HOUSE_NUMBER, LocationDto.CITY, LocationDto.AREA_TYPE, LocationDto.DETAILS, LocationDto.LAND_MARK);
	}

	//handle CHOLERA
	public void handleForCholera() {
		setVisible(true, LocationDto.POSTAL_CODE, LocationDto.VILLAGE);
		setVisible(false, LocationDto.STREET, LocationDto.ADDITIONAL_INFORMATION, LocationDto.CITY, LocationDto.HOUSE_NUMBER, LocationDto.AREA_TYPE);
	}
	
	public void handleMeasles() {
			setVisible(false, LocationDto.STREET, LocationDto.HOUSE_NUMBER, LocationDto.LOCALITY);
			setVisible(true, LocationDto.LATITUDE, LocationDto.LONGITUDE, LocationDto.LAT_LON_ACCURACY, LocationDto.LAND_MARK, LocationDto.COMMUNITY, LocationDto.DISTRICT, LocationDto.REGION, LocationDto.CITY);
			additionalInformationField.setCaption("Address (Location)");
			residentialAddress.setVisible(true);
			areaType.setVisible(true);
	}

	//handleNnt
	public void handleVisibilityForNNT() {
		nearestHealthFacilityToVillage.setVisible(true);
		setVisible(true, LocationDto.POSTAL_CODE, LocationDto.STREET, LocationDto.REGION, LocationDto.DISTRICT, LocationDto.COMMUNITY);
		setVisible(false, LocationDto.STREET, LocationDto.HOUSE_NUMBER, LocationDto.ADDITIONAL_INFORMATION);
	}

	public void setOnlyUnknownForGuineaWorm() {
		setVisible(false, LocationDto.POSTAL_CODE, LocationDto.STREET, LocationDto.ADDITIONAL_INFORMATION, LocationDto.CITY, LocationDto.HOUSE_NUMBER);
			setVisible(true, LocationDto.LAND_MARK, LocationDto.AREA_TYPE, LocationDto.ZONE, LocationDto.VILLAGE);
	}
	private Label createLabel(String text, String h4, String location) {
		final Label label = new Label(text);
		label.setId(text);
		label.addStyleName(h4);
		getContent().addComponent(label, location);
		return label;
	}

	public void hideFieldsForSelectedDisease(Disease disease) {
		Set<String> disabledFields = LocationFormConfiguration.getDisabledFieldsForDisease(disease);
		for (String field : disabledFields) {
			disableField(field);
		}
	}

	private void disableField(String field) {
		setVisible(false, field);
	}


}
