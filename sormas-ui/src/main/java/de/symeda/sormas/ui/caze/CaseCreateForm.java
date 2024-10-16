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

package de.symeda.sormas.ui.caze;

import static de.symeda.sormas.ui.utils.CssStyles.*;
import static de.symeda.sormas.ui.utils.LayoutUtil.*;

import java.time.Month;
import java.util.*;

import com.vaadin.v7.ui.*;
import de.symeda.sormas.api.caze.IdsrType;
import de.symeda.sormas.api.infrastructure.facility.*;
import de.symeda.sormas.api.person.Sex;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.ui.ControllerProvider;
import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Sets;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseOrigin;
import de.symeda.sormas.api.customizableenum.CustomizableEnumType;
import de.symeda.sormas.api.disease.DiseaseVariant;
import de.symeda.sormas.api.event.TypeOfPlace;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.pointofentry.PointOfEntryReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.person.PersonReferenceDto;
import de.symeda.sormas.api.symptoms.SymptomsDto;
import de.symeda.sormas.api.travelentry.TravelEntryDto;
import de.symeda.sormas.api.user.JurisdictionLevel;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.location.LocationEditForm;
import de.symeda.sormas.ui.person.PersonCreateForm;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.ComboBoxHelper;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.FieldHelper;
import de.symeda.sormas.ui.utils.InfrastructureFieldsHelper;
import de.symeda.sormas.ui.utils.NullableOptionGroup;

public class CaseCreateForm extends AbstractEditForm<CaseDataDto> {

    private static final long serialVersionUID = 1L;

    private static final String FACILITY_OR_HOME_LOC = "facilityOrHomeLoc";
    private static final String FACILITY_TYPE_GROUP_LOC = "typeGroupLoc";
    private static final String DONT_SHARE_WARNING_LOC = "dontShareWithReportingToolWarnLoc";
    private static final String RESPONSIBLE_JURISDICTION_HEADING_LOC = "responsibleJurisdictionHeadingLoc";
    private static final String DIFFERENT_PLACE_OF_STAY_JURISDICTION = "differentPlaceOfStayJurisdiction";
    private static final String PLACE_OF_STAY_HEADING_LOC = "placeOfStayHeadingLoc";
    public static final String MPOX_COORDINATE_LABEL = "mpoxCoordinate";
    public static final String PATIENT_DOB_LABEL = "patientDob";
    public static final String DOB_NOT_KNOWN_LABEL = "dobNotKnown";
    private static final String DIFFERENT_POINT_OF_ENTRY_JURISDICTION = "differentPointOfEntryJurisdiction";
    private static final String POINT_OF_ENTRY_REGION = "pointOfEntryRegion";
    private static final String POINT_OF_ENTRY_DISTRICT = "pointOfEntryDistrict";

    private ComboBox diseaseVariantField;
    private TextField diseaseVariantDetailsField;
    private NullableOptionGroup facilityOrHome;
    private ComboBox facilityTypeGroup;
    private ComboBox facilityType;
    private ComboBox responsibleDistrictCombo;
    private ComboBox responsibleCommunityCombo;
    private CheckBox differentPlaceOfStayJurisdiction;
    private CheckBox differentPointOfEntryJurisdiction;
    private ComboBox districtCombo;
    private ComboBox communityCombo;
    private ComboBox facilityCombo;
    private ComboBox pointOfEntryDistrictCombo;
    private DateField investigated;
    private ComboBox patientDobDay;
    private PersonCreateForm personCreateForm;
    private DateField reportDate;

    private final boolean showHomeAddressForm;
    private final boolean showPersonSearchButton;
    // If a case is created form a TravelEntry, the variable convertedTravelEntry provides the
    // necessary extra data. This variable is expected to be replaced in the implementation of
    // issue #5910.
    private final TravelEntryDto convertedTravelEntry;

    //@formatter:off
	private static final String HTML_LAYOUT = fluidRow(
			fluidColumnLoc(6, 0, CaseDataDto.DISEASE),
			fluidColumn(6, 0,
					locs(CaseDataDto.DISEASE_DETAILS, CaseDataDto.PLAGUE_TYPE, CaseDataDto.DENGUE_FEVER_TYPE,
							CaseDataDto.RABIES_TYPE, CaseDataDto.IDSR_DIAGNOSIS)))
			+ fluidRowLocs(CaseDataDto.SPECIFY_EVENT_DIAGNOSIS)
			+ fluidRowLocs(CaseDataDto.CASE_TRANSMISSION_CLASSIFICATION) +
			fluidRowLocs(CaseDataDto.CASE_ORIGIN, CaseDataDto.EPID_NUMBER)
			+ fluidRowLocs(CaseDataDto.REPORT_DATE, CaseDataDto.INVESTIGATED_DATE)
			+ fluidRowLocs(6,CaseDataDto.EXTERNAL_ID)
			+ fluidRowLocs(CaseDataDto.DISEASE_VARIANT, CaseDataDto.DISEASE_VARIANT_DETAILS)
			+ fluidRowLocs(RESPONSIBLE_JURISDICTION_HEADING_LOC)
			+ fluidRowLocs(CaseDataDto.RESPONSIBLE_REGION, CaseDataDto.RESPONSIBLE_DISTRICT, CaseDataDto.RESPONSIBLE_COMMUNITY)
			+ fluidRowLocs(CaseDataDto.DONT_SHARE_WITH_REPORTING_TOOL)
			+ fluidRowLocs(DONT_SHARE_WARNING_LOC)
			+ fluidRowLocs(DIFFERENT_PLACE_OF_STAY_JURISDICTION)
			//+ fluidRowLocs(PLACE_OF_STAY_HEADING_LOC)
			+ fluidRowLocs(CaseDataDto.REGION, CaseDataDto.DISTRICT, CaseDataDto.COMMUNITY)
			+ fluidRowLocs(FACILITY_OR_HOME_LOC)
			+ fluidRowLocs(6,FACILITY_TYPE_GROUP_LOC)
//			+ fluidRowLocs(CaseDataDto.FACILITY_TYPE, CaseDataDto.HEALTH_FACILITY)
			+ fluidRowLocs(6, CaseDataDto.HEALTH_FACILITY, 6,CaseDataDto.HEALTH_FACILITY_DETAILS)
			+ fluidRowLocs(6, CaseDataDto.HOME_ADDRESS_RECREATIONAL)
			+ fluidRowLocs(CaseDataDto.ADDRESS_MPOX, CaseDataDto.VILLAGE, CaseDataDto.CITY)
			+ fluidRowLocs(MPOX_COORDINATE_LABEL)
			+ fluidRowLocs(CaseDataDto.REPORT_LON, CaseDataDto.REPORT_LAT)
			+ fluidRowLocs(CaseDataDto.PATIENT_FIRST_NAME, CaseDataDto.PATIENT_LAST_NAME, CaseDataDto.PATIENT_OTHER_NAMES)
			+ fluidRowLocs(CaseDataDto.PATIENT_DOB_YY, CaseDataDto.PATIENT_DOB_MM, CaseDataDto.PATIENT_DOB_DD)
			+loc(DOB_NOT_KNOWN_LABEL)
			+ fluidRowLocs(CaseDataDto.PATIENT_AGE_YEAR, CaseDataDto.PATIENT_AGE_MONTH)
			+ fluidRowLocs(6, CaseDataDto.PATIENT_SEX)
			+ fluidRowLocs(CaseDataDto.NATIONALITY, CaseDataDto.ETHNICITY)
			+ fluidRowLocs(CaseDataDto.OCCUPATION, CaseDataDto.DISTRICT_OF_RESIDENCE)
			+ fluidRowLocs(DIFFERENT_POINT_OF_ENTRY_JURISDICTION)
			+ fluidRowLocs(POINT_OF_ENTRY_REGION, POINT_OF_ENTRY_DISTRICT)
			+ fluidRowLocs(CaseDataDto.POINT_OF_ENTRY, CaseDataDto.POINT_OF_ENTRY_DETAILS)
			+ fluidRowLocs(CaseDataDto.PERSON);
	//@formatter:on

    public CaseCreateForm() {
        this(true, true, null);
    }

    public CaseCreateForm(TravelEntryDto convertedTravelEntry) {
        this(false, true, convertedTravelEntry);
    }

    public CaseCreateForm(Boolean showHomeAddressForm, Boolean showPersonSearchButton, TravelEntryDto convertedTravelEntry) {
        super(
                CaseDataDto.class,
                CaseDataDto.I18N_PREFIX,
                false,
                FieldVisibilityCheckers.withCountry(FacadeProvider.getConfigFacade().getCountryLocale()),
                UiFieldAccessCheckers.getNoop());
        this.convertedTravelEntry = convertedTravelEntry;
        this.showHomeAddressForm = showHomeAddressForm;
        this.showPersonSearchButton = showPersonSearchButton;
        addFields();
        setWidth(720, Unit.PIXELS);
        hideValidationUntilNextCommit();
    }

    @Override
    protected void addFields() {

        NullableOptionGroup ogCaseOrigin = addField(CaseDataDto.CASE_ORIGIN, NullableOptionGroup.class);
        ogCaseOrigin.setRequired(true);

        TextField epidField = addField(CaseDataDto.EPID_NUMBER, TextField.class);
        epidField.setInvalidCommitted(true);
        style(epidField, ERROR_COLOR_PRIMARY);

        if (!FacadeProvider.getExternalSurveillanceToolFacade().isFeatureEnabled()) {
            TextField externalIdField = addField(CaseDataDto.EXTERNAL_ID, TextField.class);
            style(externalIdField, ERROR_COLOR_PRIMARY);
        } else {
            CheckBox dontShareCheckbox = addField(CaseDataDto.DONT_SHARE_WITH_REPORTING_TOOL, CheckBox.class);
            CaseFormHelper.addDontShareWithReportingTool(getContent(), () -> dontShareCheckbox, DONT_SHARE_WARNING_LOC);
        }

        reportDate = addField(CaseDataDto.REPORT_DATE, DateField.class);
        ComboBox diseaseField = addDiseaseField(CaseDataDto.DISEASE, false, true);
        diseaseVariantField = addField(CaseDataDto.DISEASE_VARIANT, ComboBox.class);
        diseaseVariantDetailsField = addField(CaseDataDto.DISEASE_VARIANT_DETAILS, TextField.class);
        diseaseVariantDetailsField.setVisible(false);
        diseaseVariantField.setNullSelectionAllowed(true);
        diseaseVariantField.setVisible(false);
        addField(CaseDataDto.DISEASE_DETAILS, TextField.class);
        NullableOptionGroup plagueType = addField(CaseDataDto.PLAGUE_TYPE, NullableOptionGroup.class);
        addField(CaseDataDto.DENGUE_FEVER_TYPE, NullableOptionGroup.class);
        addField(CaseDataDto.RABIES_TYPE, NullableOptionGroup.class);
        TextField homeaddrecreational = addField(CaseDataDto.HOME_ADDRESS_RECREATIONAL, TextField.class);
        homeaddrecreational.setVisible(false);
        ComboBox idsrdiagnosis = addField(CaseDataDto.IDSR_DIAGNOSIS, ComboBox.class);
        TextField specifyEvent = addField(CaseDataDto.SPECIFY_EVENT_DIAGNOSIS, TextField.class);
        specifyEvent.setVisible(false);
        addField(CaseDataDto.RE_INFECTION, NullableOptionGroup.class);

        personCreateForm = new PersonCreateForm(false, true, true, false);
        personCreateForm.setWidth(100, Unit.PERCENTAGE);
        getContent().addComponent(personCreateForm, CaseDataDto.PERSON);

        differentPlaceOfStayJurisdiction = addCustomField(DIFFERENT_PLACE_OF_STAY_JURISDICTION, Boolean.class, CheckBox.class);
        differentPlaceOfStayJurisdiction.addStyleName(VSPACE_3);

        Label placeOfStayHeadingLabel = new Label(I18nProperties.getCaption(Captions.casePlaceOfStay));
        placeOfStayHeadingLabel.addStyleName(H3);
        getContent().addComponent(placeOfStayHeadingLabel, PLACE_OF_STAY_HEADING_LOC);

        //Place of Stay differs
        ComboBox region = addInfrastructureField(CaseDataDto.REGION);
        region.setCaption("Resident Region");
        districtCombo = addInfrastructureField(CaseDataDto.DISTRICT);
        districtCombo.setCaption("Resident District");
        communityCombo = addInfrastructureField(CaseDataDto.COMMUNITY);
        communityCombo.setCaption("Resident Sub-district");
        communityCombo.setNullSelectionAllowed(true);

        // jurisdictionfields
        Label jurisdictionHeadingLabel = new Label(I18nProperties.getString(Strings.headingCaseResponsibleJurisidction));
        jurisdictionHeadingLabel.addStyleName(H3);
        getContent().addComponent(jurisdictionHeadingLabel, RESPONSIBLE_JURISDICTION_HEADING_LOC);

        ComboBox responsibleRegion = addInfrastructureField(CaseDataDto.RESPONSIBLE_REGION);
        responsibleRegion.setRequired(true);
        responsibleDistrictCombo = addInfrastructureField(CaseDataDto.RESPONSIBLE_DISTRICT);
        responsibleDistrictCombo.setRequired(true);
        responsibleCommunityCombo = addInfrastructureField(CaseDataDto.RESPONSIBLE_COMMUNITY);
        responsibleCommunityCombo.setNullSelectionAllowed(true);
        responsibleCommunityCombo.addStyleName(SOFT_REQUIRED);

        InfrastructureFieldsHelper.initInfrastructureFields(responsibleRegion, responsibleDistrictCombo, responsibleCommunityCombo);

        differentPointOfEntryJurisdiction = addCustomField(DIFFERENT_POINT_OF_ENTRY_JURISDICTION, Boolean.class, CheckBox.class);
        differentPointOfEntryJurisdiction.addStyleName(VSPACE_3);

        ComboBox pointOfEntryRegionCombo = addCustomField(POINT_OF_ENTRY_REGION, RegionReferenceDto.class, ComboBox.class);
        pointOfEntryDistrictCombo = addCustomField(POINT_OF_ENTRY_DISTRICT, DistrictReferenceDto.class, ComboBox.class);
        InfrastructureFieldsHelper.initInfrastructureFields(pointOfEntryRegionCombo, pointOfEntryDistrictCombo, null);

        pointOfEntryDistrictCombo.addValueChangeListener(e -> updatePOEs());

        FieldHelper.setVisibleWhen(
                differentPlaceOfStayJurisdiction,
                Arrays.asList(region, districtCombo, communityCombo),
                Collections.singletonList(Boolean.TRUE),
                true);

        FieldHelper.setVisibleWhen(
                differentPointOfEntryJurisdiction,
                Arrays.asList(pointOfEntryRegionCombo, pointOfEntryDistrictCombo),
                Collections.singletonList(Boolean.TRUE),
                true);

        FieldHelper.setRequiredWhen(
                differentPlaceOfStayJurisdiction,
                Arrays.asList(region, districtCombo),
                Collections.singletonList(Boolean.TRUE),
                false,
                null);

        ogCaseOrigin.addValueChangeListener(e -> {
            CaseOrigin caseOrigin = (CaseOrigin) e.getProperty().getValue();

            boolean pointOfEntryRegionDistrictVisible =
                    CaseOrigin.POINT_OF_ENTRY.equals(ogCaseOrigin.getValue()) && Boolean.TRUE.equals(differentPointOfEntryJurisdiction.getValue());
            pointOfEntryRegionCombo.setVisible(pointOfEntryRegionDistrictVisible);
            pointOfEntryDistrictCombo.setVisible(pointOfEntryRegionDistrictVisible);

            if (caseOrigin == CaseOrigin.IN_COUNTRY) {
                personCreateForm.hidePassportNumber();
            } else {
                personCreateForm.showPassportNumber();
            }

        });
        facilityOrHome =
                addCustomField(FACILITY_OR_HOME_LOC, TypeOfPlace.class, NullableOptionGroup.class, I18nProperties.getCaption(Captions.casePlaceOfStay));
        facilityOrHome.setVisible(false);
        facilityOrHome.removeAllItems();
        for (TypeOfPlace place : TypeOfPlace.FOR_CASES) {
            facilityOrHome.addItem(place);
            facilityOrHome.setItemCaption(place, I18nProperties.getEnumCaption(place));
        }
        facilityOrHome.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
        facilityOrHome.setId("facilityOrHome");
        facilityOrHome.setWidth(100, Unit.PERCENTAGE);
        CssStyles.style(facilityOrHome, ValoTheme.OPTIONGROUP_HORIZONTAL);

        facilityTypeGroup = ComboBoxHelper.createComboBoxV7();
        facilityTypeGroup.setId("typeGroup");
        facilityTypeGroup.setCaption(I18nProperties.getCaption(Captions.Facility_typeGroup));
        facilityTypeGroup.setWidth(100, Unit.PERCENTAGE);
        facilityTypeGroup.addItems(FacilityTypeGroup.getAccomodationGroups());
        facilityTypeGroup.setVisible(false);
        getContent().addComponent(facilityTypeGroup, FACILITY_TYPE_GROUP_LOC);
        facilityType = ComboBoxHelper.createComboBoxV7();
        facilityType.setId("type");
        facilityType.setCaption(I18nProperties.getCaption(Captions.facilityType));
        facilityType.setWidth(100, Unit.PERCENTAGE);
        getContent().addComponent(facilityType, CaseDataDto.FACILITY_TYPE);
        facilityCombo = addInfrastructureField(CaseDataDto.HEALTH_FACILITY);
        facilityCombo.setImmediate(true);
        TextField facilityDetails = addField(CaseDataDto.HEALTH_FACILITY_DETAILS, TextField.class);
        facilityDetails.setVisible(false);
        ComboBox cbPointOfEntry = addInfrastructureField(CaseDataDto.POINT_OF_ENTRY);
        cbPointOfEntry.setImmediate(true);
        TextField tfPointOfEntryDetails = addField(CaseDataDto.POINT_OF_ENTRY_DETAILS, TextField.class);
        tfPointOfEntryDetails.setVisible(false);


        if (convertedTravelEntry != null) {
            RegionReferenceDto regionReferenceDto = convertedTravelEntry.getPointOfEntryRegion() != null
                    ? convertedTravelEntry.getPointOfEntryRegion()
                    : convertedTravelEntry.getResponsibleRegion();
            pointOfEntryRegionCombo.setValue(regionReferenceDto);
            DistrictReferenceDto districtReferenceDto = convertedTravelEntry.getPointOfEntryDistrict() != null
                    ? convertedTravelEntry.getPointOfEntryDistrict()
                    : convertedTravelEntry.getResponsibleDistrict();
            pointOfEntryDistrictCombo.setValue(districtReferenceDto);

            differentPointOfEntryJurisdiction.setReadOnly(true);
            pointOfEntryRegionCombo.setReadOnly(true);
            pointOfEntryDistrictCombo.setReadOnly(true);
            updatePOEs();
            cbPointOfEntry.setReadOnly(true);
            tfPointOfEntryDetails.setReadOnly(true);
            ogCaseOrigin.setReadOnly(true);
        }

        region.addValueChangeListener(e -> {
            RegionReferenceDto regionDto = (RegionReferenceDto) e.getProperty().getValue();
            FieldHelper
                    .updateItems(districtCombo, regionDto != null ? FacadeProvider.getDistrictFacade().getAllActiveByRegion(regionDto.getUuid()) : null);
        });
        districtCombo.addValueChangeListener(e -> {
            FieldHelper.removeItems(communityCombo);
            DistrictReferenceDto districtDto = (DistrictReferenceDto) e.getProperty().getValue();
            FieldHelper.updateItems(
                    communityCombo,
                    districtDto != null ? FacadeProvider.getCommunityFacade().getAllActiveByDistrict(districtDto.getUuid()) : null);

            updateFacility();
            if (!Boolean.TRUE.equals(differentPointOfEntryJurisdiction.getValue())) {
                updatePOEs();
            }
        });
        communityCombo.addValueChangeListener(e -> updateFacility());

        facilityOrHome.addValueChangeListener(e -> {
            FacilityReferenceDto healthFacility = UserProvider.getCurrent().getUser().getHealthFacility();
            boolean hasOptionalHealthFacility = UserProvider.getCurrent().hasOptionalHealthFacility();
            if (hasOptionalHealthFacility && healthFacility != null) {
                String facilityId = healthFacility.getUuid();
                FacilityDto facilityDto = FacadeProvider.getFacilityFacade().getByUuid(facilityId);
                FacilityType facilityUserType = facilityDto.getType();
                FacilityTypeGroup facilityUserTypeGroup = facilityDto.getType().getFacilityTypeGroup();
                facilityTypeGroup.addItems(facilityUserTypeGroup);
                facilityTypeGroup.setValue(facilityUserTypeGroup);
                facilityType.addItems(facilityUserType);
                facilityType.setValue(facilityUserType);
                String facilityName = facilityDto.getName();
                facilityCombo.setValue(facilityName);
                FieldHelper.removeItems(facilityCombo);
            }
            if (TypeOfPlace.FACILITY.equals(facilityOrHome.getValue())
                    || ((facilityOrHome.getValue() instanceof java.util.Set) && TypeOfPlace.FACILITY.equals(facilityOrHome.getNullableValue()))) {
                if (facilityTypeGroup.getValue() == null) {
                    facilityTypeGroup.setValue(FacilityTypeGroup.MEDICAL_FACILITY);
                    facilityType.setValue(FacilityType.HOSPITAL);
                }
                if (facilityType.getValue() == null && FacilityTypeGroup.MEDICAL_FACILITY.equals(facilityTypeGroup.getValue())) {
                    facilityType.setValue(FacilityType.HOSPITAL);
                }

                if (facilityType.getValue() != null) {
                    updateFacility();
                }

                updateFacilityFields(facilityCombo, facilityDetails);
            } else if (TypeOfPlace.HOME.equals(facilityOrHome.getValue())
                    || ((facilityOrHome.getValue() instanceof java.util.Set) && TypeOfPlace.HOME.equals(facilityOrHome.getNullableValue()))) {
                setNoneFacility();
            } else {
                FieldHelper.removeItems(facilityCombo);
                if (TypeOfPlace.FACILITY.equals(facilityOrHome.getValue())
                        || ((facilityOrHome.getValue() instanceof java.util.Set) && TypeOfPlace.FACILITY.equals(facilityOrHome.getNullableValue()))) {
                    if (facilityTypeGroup.getValue() == null) {
                        facilityTypeGroup.setValue(FacilityTypeGroup.MEDICAL_FACILITY);
                    }
                    if (facilityType.getValue() == null && FacilityTypeGroup.MEDICAL_FACILITY.equals(facilityTypeGroup.getValue())) {
                        facilityType.setValue(FacilityType.HOSPITAL);
                    }

                    if (facilityType.getValue() != null) {
                        updateFacility();
                    }

                    if (CaseOrigin.IN_COUNTRY.equals(ogCaseOrigin.getValue())) {
                        facilityCombo.setRequired(true);
                    }
                    updateFacilityFields(facilityCombo, facilityDetails);
                } else if (TypeOfPlace.HOME.equals(facilityOrHome.getValue())
                        || ((facilityOrHome.getValue() instanceof java.util.Set) && TypeOfPlace.HOME.equals(facilityOrHome.getNullableValue()))) {
                    setNoneFacility();
                } else {
                    facilityCombo.removeAllItems();
                    facilityCombo.setValue(null);
                    updateFacilityFields(facilityCombo, facilityDetails);
                }
            }
        });
        facilityTypeGroup.addValueChangeListener(e -> {
            FieldHelper.removeItems(facilityCombo);
            FieldHelper.updateEnumData(facilityType, FacilityType.getAccommodationTypes((FacilityTypeGroup) facilityTypeGroup.getValue()));
        });
        facilityType.addValueChangeListener(e -> updateFacility());
        region.addItems(FacadeProvider.getRegionFacade().getAllActiveByServerCountry());

        OptionGroup caseTransmissionClassification = addField(CaseDataDto.CASE_TRANSMISSION_CLASSIFICATION, OptionGroup.class);
        caseTransmissionClassification.setVisible(false);

        JurisdictionLevel userJurisdictionLevel = UserProvider.getCurrent().getJurisdictionLevel();
        if (userJurisdictionLevel == JurisdictionLevel.HEALTH_FACILITY) {
            region.setReadOnly(true);
            responsibleRegion.setReadOnly(true);
            districtCombo.setReadOnly(true);
            responsibleDistrictCombo.setReadOnly(true);
            communityCombo.setReadOnly(true);
            responsibleCommunityCombo.setReadOnly(true);
            differentPlaceOfStayJurisdiction.setVisible(false);
            differentPlaceOfStayJurisdiction.setEnabled(false);

            facilityOrHome.setImmediate(true);
            facilityOrHome.setValue(Sets.newHashSet(TypeOfPlace.FACILITY)); // [FACILITY]
            facilityOrHome.setReadOnly(true);

            facilityTypeGroup.setValue(FacilityTypeGroup.MEDICAL_FACILITY);
            facilityTypeGroup.setReadOnly(true);
            facilityType.setValue(FacilityType.HOSPITAL);
            facilityType.setReadOnly(true);
            facilityCombo.setValue(UserProvider.getCurrent().getUser().getHealthFacility());
            facilityCombo.setReadOnly(true);
        }

        if (!UserProvider.getCurrent().isPortHealthUser()) {
            ogCaseOrigin.addValueChangeListener(ev -> {
                if (ev.getProperty().getValue() == CaseOrigin.IN_COUNTRY) {
                    setVisible(false, CaseDataDto.POINT_OF_ENTRY, CaseDataDto.POINT_OF_ENTRY_DETAILS);
                    differentPointOfEntryJurisdiction.setVisible(false);
                    setRequired(false, CaseDataDto.POINT_OF_ENTRY);
                    updateFacilityFields(facilityCombo, facilityDetails);
                } else {
                    setVisible(true, CaseDataDto.POINT_OF_ENTRY);
                    differentPointOfEntryJurisdiction.setVisible(true);
                    setRequired(true, CaseDataDto.POINT_OF_ENTRY);
                    setVisible(diseaseField.getValue() == Disease.CORONAVIRUS, DIFFERENT_POINT_OF_ENTRY_JURISDICTION, CaseDataDto.POINT_OF_ENTRY);
                    if (userJurisdictionLevel != JurisdictionLevel.HEALTH_FACILITY) {
                        facilityOrHome.clear();
                        setRequired(false, FACILITY_OR_HOME_LOC, FACILITY_TYPE_GROUP_LOC, CaseDataDto.FACILITY_TYPE, CaseDataDto.HEALTH_FACILITY);
                    }
                    updatePointOfEntryFields(cbPointOfEntry, tfPointOfEntryDetails);
                }
            });

        }

        // jurisdiction field valuechangelisteners
        responsibleDistrictCombo.addValueChangeListener(e -> {
            Boolean differentPlaceOfStay = differentPlaceOfStayJurisdiction.getValue();
            if (!Boolean.TRUE.equals(differentPlaceOfStay)) {
                updateFacility();
                if (!Boolean.TRUE.equals(differentPointOfEntryJurisdiction.getValue())) {
                    updatePOEs();
                }
            }
        });
        responsibleCommunityCombo.addValueChangeListener((e) -> {
            Boolean differentPlaceOfStay = differentPlaceOfStayJurisdiction.getValue();
            if (differentPlaceOfStay == null || Boolean.FALSE.equals(differentPlaceOfStay)) {
                updateFacility();
            }
        });

        differentPlaceOfStayJurisdiction.addValueChangeListener(e -> {
            updateFacility();
            if (!Boolean.TRUE.equals(differentPointOfEntryJurisdiction.getValue())) {
                updatePOEs();
            }
        });

        // Set initial visibilities & accesses
        initializeVisibilitiesAndAllowedVisibilities();

        setRequired(true, CaseDataDto.REPORT_DATE, CaseDataDto.DISEASE);
        FieldHelper.addSoftRequiredStyle(plagueType, communityCombo, facilityDetails);

        FieldHelper
                .setVisibleWhen(getFieldGroup(), Arrays.asList(CaseDataDto.DISEASE_DETAILS), CaseDataDto.DISEASE, Arrays.asList(Disease.OTHER), true);
        FieldHelper.setRequiredWhen(getFieldGroup(), CaseDataDto.DISEASE, Arrays.asList(CaseDataDto.DISEASE_DETAILS), Arrays.asList(Disease.OTHER));
		/*FieldHelper.setRequiredWhen(
				getFieldGroup(),
				CaseDataDto.CASE_ORIGIN,
				Arrays.asList(CaseDataDto.HEALTH_FACILITY),
				Arrays.asList(CaseOrigin.IN_COUNTRY));*/
        FieldHelper.setRequiredWhen(
                getFieldGroup(),
                CaseDataDto.CASE_ORIGIN,
                Arrays.asList(CaseDataDto.POINT_OF_ENTRY),
                Arrays.asList(CaseOrigin.POINT_OF_ENTRY));
        FieldHelper.setVisibleWhen(getFieldGroup(), Arrays.asList(CaseDataDto.PLAGUE_TYPE), CaseDataDto.DISEASE, Arrays.asList(Disease.PLAGUE), true);
        FieldHelper
                .setVisibleWhen(getFieldGroup(), Arrays.asList(CaseDataDto.DENGUE_FEVER_TYPE), CaseDataDto.DISEASE, Arrays.asList(Disease.DENGUE), true);
        FieldHelper.setVisibleWhen(getFieldGroup(), Arrays.asList(CaseDataDto.RABIES_TYPE), CaseDataDto.DISEASE, Arrays.asList(Disease.RABIES), true);
        FieldHelper
                .setVisibleWhen(getFieldGroup(), Arrays.asList(CaseDataDto.IDSR_DIAGNOSIS), CaseDataDto.DISEASE, Arrays.asList(Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS), true);
//		FieldHelper.setVisibleWhen(
//				facilityOrHome,
//				Arrays.asList(facilityType, facilityCombo),
//				Collections.singletonList(TypeOfPlace.FACILITY),
//				false);

        facilityCombo.addValueChangeListener(e -> {
            updateFacilityFields(facilityCombo, facilityDetails);
            this.getValue().setFacilityType((FacilityType) facilityType.getValue());
        });

        cbPointOfEntry.addValueChangeListener(e -> {
            updatePointOfEntryFields(cbPointOfEntry, tfPointOfEntryDetails);
        });

        addValueChangeListener(e -> {
            if (UserProvider.getCurrent().isPortHealthUser()) {
                setVisible(false, CaseDataDto.CASE_ORIGIN, CaseDataDto.DISEASE, CaseDataDto.COMMUNITY, CaseDataDto.HEALTH_FACILITY);
                setVisible(true, CaseDataDto.POINT_OF_ENTRY);
            }
        });


        diseaseField.addValueChangeListener((ValueChangeListener) valueChangeEvent -> {
            Disease disease = (Disease) valueChangeEvent.getProperty().getValue();

            caseTransmissionClassification.setVisible(disease != Disease.YELLOW_FEVER && disease != Disease.CSM && disease != Disease.AHF && disease != Disease.MEASLES);
            updateDiseaseVariant(disease);
            if (investigated == null) {
                investigated = addField(CaseDataDto.INVESTIGATED_DATE, DateField.class);
            }
            investigated.setVisible(false);
            ogCaseOrigin.setVisible(disease != Disease.CSM);
            caseTransmissionClassification.setVisible(false);

            facilityTypeGroup.setVisible(false);
//			FieldHelper.removeItems(facilityCombo);

            if (disease == Disease.CORONAVIRUS) {
                facilityOrHome.setVisible(true);
                caseTransmissionClassification.setVisible(true);
                personCreateForm.setSymptomsOnsetDateVisible(true);
            } else {
                facilityOrHome.setVisible(false);
                caseTransmissionClassification.setVisible(false);
                personCreateForm.setSymptomsOnsetDateVisible(false);
            }

            investigated.setVisible(disease == Disease.NEW_INFLUENZA);
            personCreateForm.updatePresentConditionEnum((Disease) valueChangeEvent.getProperty().getValue());

            if (diseaseField.getValue() != null && diseaseField.getValue() == Disease.CORONAVIRUS) {
                personCreateForm.hideFieldsForCovid19();
            } else {
                personCreateForm.showPresentCondition();
            }

            if (diseaseField.getValue() != null) {
                switch ((Disease) diseaseField.getValue()) {
                    case MEASLES:
                        personCreateForm.handleMeasles();
                        break;
                    case CHOLERA:
                        personCreateForm.hideFieldsForCholera();
                    case NEONATAL_TETANUS:
                        personCreateForm.handleVisibilityForNNT();
                        break;
                    case GUINEA_WORM:
                        personCreateForm.hideFieldsForGuineaWorm();
                        break;
                    case FOODBORNE_ILLNESS:
                        personCreateForm.hideFields();
                        placeOfStayHeadingLabel.setVisible(false);
                        setVisible(false, FACILITY_OR_HOME_LOC);
                        break;
                    case IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS:
                        personCreateForm.hideFields();
                        break;
                    case NEW_INFLUENZA:
                    case AFP:
                    case AHF:
                        personCreateForm.hidePresentCondition();
                        break;
                    case YELLOW_FEVER:
                        personCreateForm.hidePresentCondition();
                        personCreateForm.showPersonalEmail();
                        break;
                    case MONKEYPOX:
                        handleMPox();
                        break;
                    default:
                        personCreateForm.showPresentCondition();
                        break;

                }
            }


            setVisible(diseaseField.getValue() == Disease.CORONAVIRUS, DIFFERENT_PLACE_OF_STAY_JURISDICTION);
        });

//		FieldHelper.setVisibleWhen(
//				diseaseField,
//				Arrays.asList(facilityOrHome),
//				Arrays.asList(Disease.CORONAVIRUS),
//				true);

        idsrdiagnosis.addValueChangeListener((ValueChangeListener) valueChangeEvent -> {
            specifyEvent.setVisible(idsrdiagnosis.getValue() != null && idsrdiagnosis.getValue() == IdsrType.OTHER);
        });

        diseaseVariantField.addValueChangeListener(e -> {
            DiseaseVariant diseaseVariant = (DiseaseVariant) e.getProperty().getValue();
            diseaseVariantDetailsField.setVisible(diseaseVariant != null && diseaseVariant.matchPropertyValue(DiseaseVariant.HAS_DETAILS, true));
        });

        if (diseaseField.getValue() != null) {
            Disease disease = (Disease) diseaseField.getValue();
            updateDiseaseVariant(disease);
            personCreateForm.updatePresentConditionEnum(disease);
        }

        facilityOrHome.setValue(Sets.newHashSet(TypeOfPlace.FACILITY));


    }

    private void handleMPox() {
        personCreateForm.hideFields();
        reportDate.setVisible(false);
        reportDate.setRequired(false);
        personCreateForm.hideFieldsForMpox();

        addFields(CaseDataDto.ADDRESS_MPOX, CaseDataDto.VILLAGE, CaseDataDto.CITY);

        Label coorLabel = new Label(I18nProperties.getCaption(Captions.coorLabel));
        coorLabel.addStyleName(H4);
        getContent().addComponent(coorLabel, MPOX_COORDINATE_LABEL);

        Label patientDob = new Label(I18nProperties.getCaption(Captions.patientDob));
        patientDob.addStyleName(H4);
        getContent().addComponent(patientDob, PATIENT_DOB_LABEL);

        addFields(CaseDataDto.REPORT_LON, CaseDataDto.REPORT_LAT);

        patientDobDay = addField(CaseDataDto.PATIENT_DOB_DD, ComboBox.class);
        // @TODO: Done for nullselection Bug, fixed in Vaadin 7.7.3
        patientDobDay.setNullSelectionAllowed(true);
        patientDobDay.setInputPrompt(I18nProperties.getString(Strings.day));
        patientDobDay.setCaption("");

        ComboBox patientDobMonth = addField(CaseDataDto.PATIENT_DOB_MM, ComboBox.class);
        // @TODO: Done for nullselection Bug, fixed in Vaadin 7.7.3
        patientDobMonth.setNullSelectionAllowed(true);
        patientDobMonth.addItems(DateHelper.getMonthsInYear());
        patientDobMonth.setPageLength(12);
        patientDobMonth.setInputPrompt(I18nProperties.getString(Strings.month));
        patientDobMonth.setCaption("");
        DateHelper.getMonthsInYear()
                .forEach(month -> patientDobMonth.setItemCaption(month, de.symeda.sormas.api.Month.values()[month - 1].toString()));
        setItemCaptionsForMonths(patientDobMonth);

        ComboBox patientDobYear = addField(CaseDataDto.PATIENT_DOB_YY, ComboBox.class);
        patientDobYear.setCaption(I18nProperties.getPrefixCaption(PersonDto.I18N_PREFIX, PersonDto.BIRTH_DATE));
        // @TODO: Done for nullselection Bug, fixed in Vaadin 7.7.3
        patientDobYear.setNullSelectionAllowed(true);
        patientDobYear.addItems(DateHelper.getYearsToNow());
        patientDobYear.setItemCaptionMode(ItemCaptionMode.ID_TOSTRING);
        patientDobYear.setInputPrompt(I18nProperties.getString(Strings.year));

        patientDobDay.addValidator(
                e -> ControllerProvider.getPersonController()
                        .validateBirthDate((Integer) patientDobYear.getValue(), (Integer) patientDobMonth.getValue(), (Integer) e));
        patientDobMonth.addValidator(
                e -> ControllerProvider.getPersonController()
                        .validateBirthDate((Integer) patientDobYear.getValue(), (Integer) e, (Integer) patientDobDay.getValue()));
        patientDobYear.addValidator(
                e -> ControllerProvider.getPersonController()
                        .validateBirthDate((Integer) e, (Integer) patientDobMonth.getValue(), (Integer) patientDobDay.getValue()));

        Label dobNot = new Label(I18nProperties.getCaption(Captions.dobNot));
        dobNot.addStyleName(H4);
        getContent().addComponent(dobNot, DOB_NOT_KNOWN_LABEL);

        TextField patientAgeYear = addField(CaseDataDto.PATIENT_AGE_YEAR);
        addField(CaseDataDto.PATIENT_AGE_MONTH);

        ComboBox patientSex = addField(CaseDataDto.PATIENT_SEX, ComboBox.class);
        patientSex.removeItem(Sex.OTHER);
        patientSex.removeItem(Sex.UNKNOWN);
        addFields(CaseDataDto.PATIENT_FIRST_NAME, CaseDataDto.PATIENT_LAST_NAME, CaseDataDto.PATIENT_OTHER_NAMES);

        addFields(CaseDataDto.NATIONALITY, CaseDataDto.ETHNICITY);
        addFields(CaseDataDto.OCCUPATION, CaseDataDto.DISTRICT_OF_RESIDENCE);

        patientDobYear.addValueChangeListener(e -> {
            updateListOfDays((Integer) e.getProperty().getValue(), (Integer) patientDobMonth.getValue());
            patientDobMonth.markAsDirty();
            patientDobDay.markAsDirty();
        });
        patientDobMonth.addValueChangeListener(e -> {
            updateListOfDays((Integer) patientDobYear.getValue(), (Integer) e.getProperty().getValue());
            patientDobYear.markAsDirty();
            patientDobDay.markAsDirty();
        });
        patientDobDay.addValueChangeListener(e -> {
            patientDobYear.markAsDirty();
            patientDobMonth.markAsDirty();
        });

    }

    private void setItemCaptionsForMonths(AbstractSelect months) {
        months.setItemCaption(1, I18nProperties.getEnumCaption(Month.JANUARY));
        months.setItemCaption(2, I18nProperties.getEnumCaption(Month.FEBRUARY));
        months.setItemCaption(3, I18nProperties.getEnumCaption(Month.MARCH));
        months.setItemCaption(4, I18nProperties.getEnumCaption(Month.APRIL));
        months.setItemCaption(5, I18nProperties.getEnumCaption(Month.MAY));
        months.setItemCaption(6, I18nProperties.getEnumCaption(Month.JUNE));
        months.setItemCaption(7, I18nProperties.getEnumCaption(Month.JULY));
        months.setItemCaption(8, I18nProperties.getEnumCaption(Month.AUGUST));
        months.setItemCaption(9, I18nProperties.getEnumCaption(Month.SEPTEMBER));
        months.setItemCaption(10, I18nProperties.getEnumCaption(Month.OCTOBER));
        months.setItemCaption(11, I18nProperties.getEnumCaption(Month.NOVEMBER));
        months.setItemCaption(12, I18nProperties.getEnumCaption(Month.DECEMBER));
    }

    private void updateListOfDays(Integer selectedYear, Integer selectedMonth) {
        if (!patientDobDay.isReadOnly()) {
            Integer currentlySelected = (Integer) patientDobDay.getValue();
            patientDobDay.removeAllItems();
            patientDobDay.addItems(DateHelper.getDaysInMonth(selectedMonth, selectedYear));
            if (patientDobDay.containsId(currentlySelected)) {
                patientDobDay.setValue(currentlySelected);
            }
        }
    }

    private void updateDiseaseVariant(Disease disease) {
        List<DiseaseVariant> diseaseVariants =
                FacadeProvider.getCustomizableEnumFacade().getEnumValues(CustomizableEnumType.DISEASE_VARIANT, disease);
        FieldHelper.updateItems(diseaseVariantField, diseaseVariants);
        diseaseVariantField
                .setVisible(disease != null && isVisibleAllowed(CaseDataDto.DISEASE_VARIANT) && CollectionUtils.isNotEmpty(diseaseVariants));
    }

    private void setNoneFacility() {
        FacilityReferenceDto noFacilityRef = FacadeProvider.getFacilityFacade().getByUuid(FacilityDto.NONE_FACILITY_UUID).toReference();
        facilityCombo.addItem(noFacilityRef);
        facilityCombo.setValue(noFacilityRef);
    }

    private void updateFacility() {

        if (UserProvider.getCurrent().getJurisdictionLevel() == JurisdictionLevel.HEALTH_FACILITY) {
            return;
        }

        Object facilityOrHomeValue = facilityOrHome.isRequired() ? facilityOrHome.getValue() : facilityOrHome.getNullableValue();
        if (TypeOfPlace.HOME.equals(facilityOrHomeValue)) {
            setNoneFacility();
            return;
        }

        FieldHelper.removeItems(facilityCombo);

        final DistrictReferenceDto district;
        final CommunityReferenceDto community;

        if (Boolean.TRUE.equals(differentPlaceOfStayJurisdiction.getValue())) {
            district = (DistrictReferenceDto) districtCombo.getValue();
            community = (CommunityReferenceDto) communityCombo.getValue();
        } else {
            district = (DistrictReferenceDto) responsibleDistrictCombo.getValue();
            community = (CommunityReferenceDto) responsibleCommunityCombo.getValue();
        }


        if (facilityType.getValue() != null && district != null) {
            if (community != null) {
                FieldHelper.updateItems(
                        facilityCombo,
                        FacadeProvider.getFacilityFacade()
                                .getActiveFacilitiesByCommunityAndType(community, (FacilityType) facilityType.getValue(), true, false, true));
            } else {
                FieldHelper.updateItems(
                        facilityCombo,
                        FacadeProvider.getFacilityFacade()
                                .getActiveFacilitiesByDistrictAndType(district, (FacilityType) facilityType.getValue(), true, false, true));
            }
        }
    }

    private void updatePOEs() {

        ComboBox comboBoxPOE = getField(CaseDataDto.POINT_OF_ENTRY);
        if (!comboBoxPOE.isReadOnly()) {
            DistrictReferenceDto districtDto;

            if (Boolean.TRUE.equals(differentPointOfEntryJurisdiction.getValue())) {
                districtDto = (DistrictReferenceDto) pointOfEntryDistrictCombo.getValue();
            } else if (Boolean.TRUE.equals(differentPlaceOfStayJurisdiction.getValue())) {
                districtDto = (DistrictReferenceDto) districtCombo.getValue();
            } else {
                districtDto = (DistrictReferenceDto) responsibleDistrictCombo.getValue();
            }

            List<PointOfEntryReferenceDto> POEs = districtDto == null
                    ? Collections.emptyList()
                    : FacadeProvider.getPointOfEntryFacade().getAllActiveByDistrict(districtDto.getUuid(), true);
            FieldHelper.updateItems(comboBoxPOE, POEs);
        }
    }

    private void updateFacilityFields(ComboBox cbFacility, TextField tfFacilityDetails) {

        if (cbFacility.getValue() != null) {
            boolean otherHealthFacility = ((FacilityReferenceDto) cbFacility.getValue()).getUuid().equals(FacilityDto.OTHER_FACILITY_UUID);
            boolean noneHealthFacility = ((FacilityReferenceDto) cbFacility.getValue()).getUuid().equals(FacilityDto.NONE_FACILITY_UUID);
            boolean visibleAndRequired = otherHealthFacility || noneHealthFacility;

            tfFacilityDetails.setVisible(visibleAndRequired);
            tfFacilityDetails.setRequired(false);

            if (otherHealthFacility) {
                tfFacilityDetails.setCaption(I18nProperties.getPrefixCaption(CaseDataDto.I18N_PREFIX, CaseDataDto.HEALTH_FACILITY_DETAILS));
            }
            if (noneHealthFacility) {
                tfFacilityDetails.setCaption(I18nProperties.getCaption(Captions.CaseData_noneHealthFacilityDetails));
            }
            if (!visibleAndRequired) {
                tfFacilityDetails.clear();
            }
        } else if (((facilityOrHome.getValue() instanceof java.util.Set)
                && (facilityOrHome.getNullableValue() == null || TypeOfPlace.FACILITY.equals(facilityOrHome.getNullableValue())))
                || TypeOfPlace.FACILITY.equals(facilityOrHome.getValue())) {
            tfFacilityDetails.setVisible(false);
            tfFacilityDetails.setRequired(false);
            tfFacilityDetails.clear();

        }
    }

    private void updatePointOfEntryFields(ComboBox cbPointOfEntry, TextField tfPointOfEntryDetails) {

        if (cbPointOfEntry.getValue() != null) {
            boolean isOtherPointOfEntry = ((PointOfEntryReferenceDto) cbPointOfEntry.getValue()).isOtherPointOfEntry();
            setVisible(isOtherPointOfEntry, CaseDataDto.POINT_OF_ENTRY_DETAILS);
            setRequired(isOtherPointOfEntry, CaseDataDto.POINT_OF_ENTRY_DETAILS);
            if (!isOtherPointOfEntry) {
                tfPointOfEntryDetails.clear();
            }
        } else {
            tfPointOfEntryDetails.setVisible(false);
            tfPointOfEntryDetails.setRequired(false);
            tfPointOfEntryDetails.clear();
        }
    }

    public Date getOnsetDate() {
        return personCreateForm.getOnsetDate();
    }

    public void setSymptoms(SymptomsDto symptoms) {
        personCreateForm.setSymptoms(symptoms);
    }

    public void setPersonalDetailsReadOnlyIfNotEmpty(boolean readOnly) {
        personCreateForm.setPersonalDetailsReadOnlyIfNotEmpty(readOnly);
    }

    public void setDiseaseReadOnly(boolean readOnly) {
        getField(CaseDataDto.DISEASE).setEnabled(!readOnly);
    }

    public PersonCreateForm getPersonCreateForm() {
        return personCreateForm;
    }

    public LocationEditForm getHomeAddressForm() {
        return personCreateForm.getHomeAddressForm();
    }

    public PersonDto getSearchedPerson() {
        return personCreateForm.getSearchedPerson();
    }

    public void setPerson(PersonDto person) {
        personCreateForm.setPerson(person);
    }

    @Override
    protected String createHtmlLayout() {
        return HTML_LAYOUT;
    }

    @Override
    public void setValue(CaseDataDto caseDataDto) throws com.vaadin.v7.data.Property.ReadOnlyException, Converter.ConversionException {
        super.setValue(caseDataDto);
        if (convertedTravelEntry != null) {
            diseaseVariantDetailsField.setValue(convertedTravelEntry.getDiseaseVariantDetails());
        }

        PersonReferenceDto casePersonReference = caseDataDto.getPerson();
        String personUuid = casePersonReference == null ? null : casePersonReference.getUuid();
        PersonDto personByUuid = personUuid == null ? null : FacadeProvider.getPersonFacade().getByUuid(personUuid);
        personCreateForm.setPerson(personByUuid);
    }

    public void setSearchedPerson(PersonDto searchedPerson) {
        personCreateForm.setSearchedPerson(searchedPerson);
    }

    public void transferDataToPerson(PersonDto person) {
        if (Objects.equals(disease, Disease.MONKEYPOX.toString())) {
            person.setFirstName((String) getField(CaseDataDto.PATIENT_FIRST_NAME).getValue());
            person.setLastName((String) getField(CaseDataDto.PATIENT_LAST_NAME).getValue());
            person.setOtherName((String) getField(CaseDataDto.PATIENT_OTHER_NAMES).getValue());
            person.setSex((Sex) getField(CaseDataDto.PATIENT_SEX).getValue());
        } else {
            personCreateForm.transferDataToPerson(person);
        }
    }

    public String getPatientFirstName() {
        return (String) getField(CaseDataDto.PATIENT_FIRST_NAME).getValue();
    }

    public String getPatientLastName() {
        return (String) getField(CaseDataDto.PATIENT_LAST_NAME).getValue();
    }

    public String getPatientOtherNames() {
        return (String) getField(CaseDataDto.PATIENT_OTHER_NAMES).getValue();
    }

    public Sex getPatientSex() {
        return (Sex) getField(CaseDataDto.PATIENT_SEX).getValue();
    }

}

