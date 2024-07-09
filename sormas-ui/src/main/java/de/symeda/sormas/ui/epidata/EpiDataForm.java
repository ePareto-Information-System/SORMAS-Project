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
package de.symeda.sormas.ui.epidata;

import static de.symeda.sormas.ui.utils.CssStyles.*;
import static de.symeda.sormas.ui.utils.CssStyles.VSPACE_3;
import static de.symeda.sormas.ui.utils.CssStyles.VSPACE_TOP_3;
import static de.symeda.sormas.ui.utils.LayoutUtil.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.vaadin.ui.Label;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.*;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import de.symeda.sormas.api.epidata.ContactSetting;
import de.symeda.sormas.api.caze.CaseOutcome;
import de.symeda.sormas.api.utils.RiskFactorCondition;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.ui.containmentmeasure.ContainmentMeasureField;
import de.symeda.sormas.ui.contaminationsource.ContaminationSourcesField;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.api.utils.*;
import de.symeda.sormas.ui.utils.*;
import org.apache.commons.collections.CollectionUtils;

import com.vaadin.shared.ui.ContentMode;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.contact.ContactReferenceDto;
import de.symeda.sormas.api.epidata.EpiDataDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.ActivityAsCase.ActivityAsCaseField;
import de.symeda.sormas.ui.exposure.ExposuresField;
import de.symeda.sormas.ui.utils.components.MultilineLabel;

public class EpiDataForm extends AbstractEditForm<EpiDataDto> {

	private static final long serialVersionUID = 1L;

	private static final String LOC_EXPOSURE_INVESTIGATION_HEADING = "locExposureInvestigationHeading";
	private static final String LOC_EXPOSURE_TRAVEL_HISTORY_HEADING = "locExposureTravelHistoryHeading";
	private static final String LOC_ACTIVITY_AS_CASE_INVESTIGATION_HEADING = "locActivityAsCaseInvestigationHeading";
	private static final String LOC_SOURCE_CASE_CONTACTS_HEADING = "locSourceCaseContactsHeading";
	private static final String LOC_EPI_DATA_FIELDS_HINT = "locEpiDataFieldsHint";
	private static final String EXPOSURE_HISTORY_HEADING = "exposureHistoryHeading";
	private static final String EXPOSITION_RISKS_HEADING = "expositionRisksHeading";
	private static final String INDICATE_PLACES_COUNTRY_HEADING = "indicate";
	private static final String NAME_OF_PLACE_OF_TRAVEL_HEADING = "headingNameOfPlaceOfTravel";

	private static final String TRAVEL_HISTORY_HEADING = "travelHistoryHeadingLoc";
	private static final String CONTAMINATION_SOURCE_HEADING = "contaminationSourceHeadingLoc";
	private static final String CONTAINMENT_MEASURES_HEADING = "containmentMeasuresHeadingLoc";
	//@formatter:off
	private static final String MAIN_HTML_LAYOUT =
			loc(LOC_EXPOSURE_TRAVEL_HISTORY_HEADING) +
					loc(EpiDataDto.PREVIOUSLY_VACCINATED_AGAINST_INFLUENZA)+
					fluidRowLocs(EpiDataDto.NAME_OF_VACCINE, EpiDataDto.YEAR_OF_VACCINATION)+
					loc(EpiDataDto.PREVIOUSLY_VACCINATED_AGAINST_COVID)+
					fluidRowLocs(EpiDataDto.NAME_OF_VACCINE_FOR_COVID, EpiDataDto.YEAR_OF_VACCINATION_FOR_COVID)+
					loc(EpiDataDto.VISITED_PLACES_CONFIRMED_PANDEMIC)+
					loc(EpiDataDto.PLACES_VISITED_PAST_7DAYS)+
					fluidRowLocs(EpiDataDto.RISK_FACTORS_SEVERE_DISEASE, EpiDataDto.OTHER_SPECIFY)+
					loc(EpiDataDto.PATIENT_TRAVELLED_TWO_WEEKS_PRIOR)+
					locCss(VSPACE_TOP_3, "") +
					fluidRowLocs(EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_ONE, EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_TWO) +
					fluidRowLocs(EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_THREE, EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_FOUR) +
					locCss(VSPACE_TOP_3, "") +
					fluidRowLocs(EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_ONE, EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_TWO) +
					fluidRowLocs(EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_THREE, EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_FOUR) +
					locCss(VSPACE_TOP_3, "") +
					fluidRowLocs(EpiDataDto.RECEIVED_HEALTH_EDUCATION, EpiDataDto.PATIENT_ENTERED_WATER_SOURCE, EpiDataDto.PLACE_MANAGED) +
					fluidRowLocs(EpiDataDto.EXPOSED_TO_RISK_FACTOR, EpiDataDto.WATER_USED_BY_PATIENT_AFTER_EXPOSURE)+
					fluidRowLocs(EpiDataDto.WATER_USED_FOR_DRINKING, EpiDataDto.WATER_USED_NOT_FOR_DRINKING, EpiDataDto.FOOD_ITEMS)+
					loc(EpiDataDto.PATIENT_VISITED_HEALTH_CARE_FACILITY)+
					loc(EpiDataDto.PATIENT_CLOSE_CONTACT_WITH_ARI)+
					loc(EpiDataDto.PATIENT_CLOSE_CONTACT_WITH_ARI_CONTACT_SETTINGS)+
					loc(EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE)+
					loc(EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE_EXPOSURE_LOCATIONS)+
					loc(EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE_EXPOSURE_LOCATION_CITY_COUNTRY)+
					loc(EpiDataDto.RECENT_TRAVEL_OUTBREAK)+
					loc(EpiDataDto.CONTACT_SIMILAR_SYMPTOMS)+
					loc(EpiDataDto.CONTACT_SICK_ANIMALS)+
					loc(EpiDataDto.IF_YES_SPECIFY_SICK_ANIMAL)+
					loc(EpiDataDto.CONTACT_DEAD_WILD_ANIMALS)+
					fluidRowLocs(EpiDataDto.IF_YES_SPECIFY_DEAD_WILD_ANIMAL, EpiDataDto.IF_YES_WILD_ANIMAL_LOCATION, EpiDataDto.IF_YES_WILD_ANIMAL_DATE)+
					loc(LOC_EXPOSURE_INVESTIGATION_HEADING) +
					loc(EpiDataDto.EXPOSURE_DETAILS_KNOWN) +
					loc(EpiDataDto.EXPOSURES) +
					loc(LOC_ACTIVITY_AS_CASE_INVESTIGATION_HEADING) +
					loc(EpiDataDto.ACTIVITY_AS_CASE_DETAILS_KNOWN)+

					loc(EpiDataDto.ACTIVITIES_AS_CASE) +
					locCss(VSPACE_TOP_3, LOC_EPI_DATA_FIELDS_HINT) +
					loc(TRAVEL_HISTORY_HEADING) +
					loc(EpiDataDto.PERSON_TRAVEL_HISTORY)+
					loc(CONTAMINATION_SOURCE_HEADING) +
					loc(EpiDataDto.CONTAMINATION_SOURCES) +
					loc(CONTAINMENT_MEASURES_HEADING) +
					fluidRowLocs(EpiDataDto.RECEIVED_HEALTH_EDUCATION, EpiDataDto.PATIENT_ENTERED_WATER_SOURCE, EpiDataDto.PLACE_MANAGED) +
					loc(EpiDataDto.CONTAINMENT_MEASURES) +
					loc(EpiDataDto.HIGH_TRANSMISSION_RISK_AREA) +
					loc(EpiDataDto.LARGE_OUTBREAKS_AREA) +
					loc(EpiDataDto.AREA_INFECTED_ANIMALS) +

					loc(EXPOSURE_HISTORY_HEADING) +
					fluidRowLocs(EpiDataDto.INTL_TRAVEL, EpiDataDto.SPECIFY_COUNTRIES, EpiDataDto.DATE_OF_DEPARTURE, EpiDataDto.DATE_OF_ARRIVAL) +
					fluidRowLocs(EpiDataDto.DOMESTIC_TRAVEL, EpiDataDto.SPECIFY_LOCATION, EpiDataDto.DATE_OF_DEPARTURE2, EpiDataDto.DATE_OF_ARRIVAL2) +
					fluidRowLocs(EpiDataDto.CONTACT_ILL_PERSON, EpiDataDto.CONTACT_DATE, EpiDataDto.SPECIFY_ILLNESS) +
					fluidRowLocs(EpiDataDto.AREA_INFECTED_ANIMALS) +

					fluidRowLocs(EpiDataDto.PATIENT_TRAVEL_DURING_ILLNESS) +
					loc(INDICATE_PLACES_COUNTRY_HEADING) +
					fluidRowLocs(EpiDataDto.COMM1, EpiDataDto.HEALTH_CENTER1, EpiDataDto.COUNTRY1) +
					fluidRowLocs(EpiDataDto.COMM2, EpiDataDto.HEALTH_CENTER2, EpiDataDto.COUNTRY2) +
					loc(EXPOSITION_RISKS_HEADING) +
					fluidRowLocs(EpiDataDto.WAS_PATIENT_HOSPITALIZED) +
					fluidRowLocs(EpiDataDto.IF_YES_WHERE, EpiDataDto.HOSPITALIZED_DATE1, EpiDataDto.HOSPITALIZED_DATE2) +
					fluidRowLocs(EpiDataDto.DID_PATIENT_CONSULT_HEALER) +
					fluidRowLocs(EpiDataDto.IF_YES_NAME_HEALER, EpiDataDto.COMMUNITY, EpiDataDto.COUNTRY) +
					fluidRowLocs(EpiDataDto.WHEN_WHERE_CONTACT_TAKE_PLACE, EpiDataDto.DATE_OF_CONTACT) +
					fluidRowLocs(6,EpiDataDto.PATIENT_RECEIVE_TRADITIONAL_MEDICINE) +
					fluidRowLocs(6,EpiDataDto.IF_YES_EXPLAIN) +
					fluidRowLocs(EpiDataDto.PATIENT_ATTEND_FUNERAL_CEREMONIES) +
					fluidRowLocs(EpiDataDto.PATIENT_TRAVEL_ANYTIME_PERIOD_BEFORE_ILL, EpiDataDto.IF_TRAVEL_YES_WHERE) +
					fluidRowLocs(EpiDataDto.IF_TRAVEL_START_DATE, EpiDataDto.IF_YES_END_DATE) +
					fluidRowLocs(6,EpiDataDto.PATIENT_CONTACT_KNOWN_SUSPECT) +
					fluidRowLocs(EpiDataDto.SUSPECT_LAST_NAME, EpiDataDto.SUSPECT_NAME, EpiDataDto.ID_CASE) +
					fluidRowLocs(EpiDataDto.DURING_CONTACT_SUSPECT_CASE, EpiDataDto.DATE_OF_DEATH) +
					fluidRowLocs(6,EpiDataDto.DATE_OF_LAST_CONTACT_WITH_SUSPECT_CASE);

	private static final String SOURCE_CONTACTS_HTML_LAYOUT =
			locCss(VSPACE_TOP_3, LOC_SOURCE_CASE_CONTACTS_HEADING) +
			loc(EpiDataDto.CONTACT_WITH_SOURCE_CASE_KNOWN) + loc(EpiDataDto.CHILD_COME_IN_CONTACT_WITH_SYMPTOMS);

	private static final String MEASLES_LAYOUT =
			fluidRowLocs(EpiDataDto.HISTORY_OF_TRAVEL_OUTSIDE_THE_VILLAGE_TOWN_DISTRICT) +
			locCss(VSPACE_TOP_3, NAME_OF_PLACE_OF_TRAVEL_HEADING) +
			fluidRowLocs(EpiDataDto.HISTORY_OF_TRAVEL_REGION, EpiDataDto.HISTORY_OF_TRAVEL_DISTRICT) +
			fluidRowLocs(EpiDataDto.HISTORY_OF_TRAVEL_SUB_DISTRICT, EpiDataDto.HISTORY_OF_TRAVEL_VILLAGE);

	//@formatter:on

	private final Disease disease;
	private final Class<? extends EntityDto> parentClass;
	private final Consumer<Boolean> sourceContactsToggleCallback;
	private final boolean isPseudonymized;

	private  NullableOptionGroup patientTravelledTwoWeeksPriorYesNoUknownField;
	private TextField textFieldPatientTravelledInCountryOne;
	private TextField textFieldPatientTravelledInCountryTwo;
	private TextField textFieldPatientTravelledInCountryThree;
	private TextField textFieldPatientTravelledInCountryFour;
	private TextField textFieldPatientTravelledInternationalOne;
	private TextField textFieldPatientTravelledInternationalTwo;
	private TextField textFieldPatientTravelledInternationalThree;
	private TextField textFieldPatientTravelledInternationalFour;
	private NullableOptionGroup patientVisitedHealthCareFacilityYesNoUnkownField;
	private NullableOptionGroup patientCloseContactWithARIYesNoUnkownField;
	private OptionGroup patientCloseContactWithARIContactSettingsField;
	private NullableOptionGroup patientContactWithConfirmedCaseYesNoField;
	private OptionGroup patientContactWithConfirmedCaseExposureLocationsField;
	private TextField patientContactWithConfirmedCaseExposureLocationCityCountryField;

	public EpiDataForm(
			Disease disease,
			Class<? extends EntityDto> parentClass,
			boolean isPseudonymized,
			boolean inJurisdiction,
			Consumer<Boolean> sourceContactsToggleCallback,
			boolean isEditAllowed) {
		super(
			EpiDataDto.class,
			EpiDataDto.I18N_PREFIX,
			false,
			FieldVisibilityCheckers.withDisease(disease).andWithCountry(FacadeProvider.getConfigFacade().getCountryLocale()),
			UiFieldAccessCheckers.forDataAccessLevel(UserProvider.getCurrent().getPseudonymizableDataAccessLevel(inJurisdiction), isPseudonymized),
			isEditAllowed,
				disease);
		this.disease = disease;
		this.parentClass = parentClass;
		this.sourceContactsToggleCallback = sourceContactsToggleCallback;
		this.isPseudonymized = isPseudonymized;
		addFields();
	}

	@Override
	protected void addFields() {


		if (disease != null && !excludedDiseases.contains(disease)) {
			addHeadingsAndInfoTexts();
		}

		addField(EpiDataDto.RECENT_TRAVEL_OUTBREAK, NullableOptionGroup.class);
		addField(EpiDataDto.CONTACT_SIMILAR_SYMPTOMS, NullableOptionGroup.class);

		NullableOptionGroup contactSickDomesticAnimals = addField(EpiDataDto.CONTACT_SICK_ANIMALS, NullableOptionGroup.class);
		TextField ifYesSpecifySickDomestic = addField(EpiDataDto.IF_YES_SPECIFY_SICK_ANIMAL, TextField.class);
		NullableOptionGroup contactDeadWildAnimals = addField(EpiDataDto.CONTACT_DEAD_WILD_ANIMALS, NullableOptionGroup.class);
		TextField ifYesSpecifyDeadWild = addField(EpiDataDto.IF_YES_SPECIFY_DEAD_WILD_ANIMAL, TextField.class);
		TextField ifYesWildAnimalLocation = addField(EpiDataDto.IF_YES_WILD_ANIMAL_LOCATION, TextField.class);
		DateField ifYesWildAnimalDate = addField(EpiDataDto.IF_YES_WILD_ANIMAL_DATE, DateField.class);

		contactDeadWildAnimals.setVisible(false);
		ifYesSpecifySickDomestic.setVisible(false);
		ifYesSpecifyDeadWild.setVisible(false);
		ifYesWildAnimalLocation.setVisible(false);
		ifYesWildAnimalDate.setVisible(false);

		NullableOptionGroup ogExposureDetailsKnown = addField(EpiDataDto.EXPOSURE_DETAILS_KNOWN, NullableOptionGroup.class);
		ExposuresField exposuresField = addField(EpiDataDto.EXPOSURES, ExposuresField.class);
		exposuresField.setEpiDataParentClass(parentClass);
		exposuresField.setWidthFull();
		exposuresField.setPseudonymized(isPseudonymized);

		if (parentClass == CaseDataDto.class) {
			addActivityAsCaseFields();
			addPersonTravelHistoryFields();
			addContaminationSourcesFields();
			addContainmentMeasuresFields();
		}

		addField(EpiDataDto.RECEIVED_HEALTH_EDUCATION, NullableOptionGroup.class);
		addField(EpiDataDto.PATIENT_ENTERED_WATER_SOURCE, NullableOptionGroup.class);
		addField(EpiDataDto.PLACE_MANAGED, ComboBox.class);
		setVisible(false, EpiDataDto.RECEIVED_HEALTH_EDUCATION, EpiDataDto.PATIENT_ENTERED_WATER_SOURCE, EpiDataDto.PLACE_MANAGED);

		NullableOptionGroup nullableOptionGroupHTVTD = addField(EpiDataDto.HISTORY_OF_TRAVEL_OUTSIDE_THE_VILLAGE_TOWN_DISTRICT, NullableOptionGroup.class);
		addField(EpiDataDto.HISTORY_OF_TRAVEL_OUTSIDE_THE_VILLAGE_TOWN_DISTRICT_DETAILS, TextField.class);

		ComboBox cmbRegion = addInfrastructureField(EpiDataDto.HISTORY_OF_TRAVEL_REGION);
		cmbRegion.addItems(FacadeProvider.getRegionFacade().getAllActiveByServerCountry());
		ComboBox cmbDistrict = addInfrastructureField(EpiDataDto.HISTORY_OF_TRAVEL_DISTRICT);
		ComboBox cmbCommunity = addInfrastructureField(EpiDataDto.HISTORY_OF_TRAVEL_SUB_DISTRICT);
		TextField txtVillage = addField(EpiDataDto.HISTORY_OF_TRAVEL_VILLAGE);

		Label nameOfPlaceOfTravelLabel = new Label(I18nProperties.getString(Strings.headingNameOfPlaceOfTravel));
		nameOfPlaceOfTravelLabel.setStyleName("h3");
		getContent().addComponent(nameOfPlaceOfTravelLabel, NAME_OF_PLACE_OF_TRAVEL_HEADING);

		cmbRegion.addValueChangeListener(e -> {
			RegionReferenceDto regionDto = (RegionReferenceDto) e.getProperty().getValue();
			FieldHelper
					.updateItems(cmbDistrict, regionDto != null ? FacadeProvider.getDistrictFacade().getAllActiveByRegion(regionDto.getUuid()) : null);
		});
		cmbDistrict.addValueChangeListener(e -> {
			DistrictReferenceDto districtDto = (DistrictReferenceDto) e.getProperty().getValue();
			FieldHelper.updateItems(
					cmbCommunity,
					districtDto != null ? FacadeProvider.getCommunityFacade().getAllActiveByDistrict(districtDto.getUuid()) : null);
		});

		FieldHelper.setVisibleWhen(
				nullableOptionGroupHTVTD,
				Arrays.asList(cmbRegion, cmbDistrict, cmbCommunity, txtVillage),
				Collections.singletonList(YesNo.YES),
				true);

		nullableOptionGroupHTVTD.addValueChangeListener(e -> {
			YesNo yesNo = (YesNo) FieldHelper.getNullableSourceFieldValue((Field) e.getProperty());
            nameOfPlaceOfTravelLabel.setVisible(yesNo == YesNo.YES);
		});


		addField(EpiDataDto.HIGH_TRANSMISSION_RISK_AREA, NullableOptionGroup.class);
		addField(EpiDataDto.LARGE_OUTBREAKS_AREA, NullableOptionGroup.class);
		addField(EpiDataDto.AREA_INFECTED_ANIMALS, NullableOptionGroup.class);
		NullableOptionGroup ogContactWithSourceCaseKnown = addField(EpiDataDto.CONTACT_WITH_SOURCE_CASE_KNOWN, NullableOptionGroup.class);
		NullableOptionGroup ogchildComeInContactWithSymptoms = addField(EpiDataDto.CHILD_COME_IN_CONTACT_WITH_SYMPTOMS, NullableOptionGroup.class);
		ogchildComeInContactWithSymptoms.setVisible(false);

		addField(EpiDataDto.EXPOSED_TO_RISK_FACTOR, OptionGroup.class);
		addField(EpiDataDto.WATER_USED_BY_PATIENT_AFTER_EXPOSURE, ComboBox.class);
		setVisible(false, EpiDataDto.WATER_USED_BY_PATIENT_AFTER_EXPOSURE, EpiDataDto.EXPOSED_TO_RISK_FACTOR);

		if (sourceContactsToggleCallback != null) {
			ogContactWithSourceCaseKnown.addValueChangeListener(e -> {
				YesNo sourceContactsKnown = (YesNo) FieldHelper.getNullableSourceFieldValue((Field) e.getProperty());
				sourceContactsToggleCallback.accept(YesNo.YES == sourceContactsKnown);
			});
		}

		//patientTravelledTwoWeeksPrior
		patientTravelledTwoWeeksPriorYesNoUknownField = addField(EpiDataDto.PATIENT_TRAVELLED_TWO_WEEKS_PRIOR, NullableOptionGroup.class);
		textFieldPatientTravelledInCountryOne = addField(EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_ONE, TextField.class);
		textFieldPatientTravelledInCountryTwo = addField(EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_TWO, TextField.class);
		textFieldPatientTravelledInCountryThree = addField(EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_THREE, TextField.class);
		textFieldPatientTravelledInCountryFour = addField(EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_FOUR, TextField.class);
		textFieldPatientTravelledInternationalOne = addField(EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_ONE, TextField.class);
		textFieldPatientTravelledInternationalTwo = addField(EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_TWO, TextField.class);
		textFieldPatientTravelledInternationalThree = addField(EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_THREE, TextField.class);
		textFieldPatientTravelledInternationalFour = addField(EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_FOUR, TextField.class);

		patientVisitedHealthCareFacilityYesNoUnkownField = addField(EpiDataDto.PATIENT_VISITED_HEALTH_CARE_FACILITY, NullableOptionGroup.class);
		patientCloseContactWithARIYesNoUnkownField = addField(EpiDataDto.PATIENT_CLOSE_CONTACT_WITH_ARI, NullableOptionGroup.class);
		patientCloseContactWithARIContactSettingsField = addField(EpiDataDto.PATIENT_CLOSE_CONTACT_WITH_ARI_CONTACT_SETTINGS, OptionGroup.class);
		CssStyles.style(patientCloseContactWithARIContactSettingsField, CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
		patientCloseContactWithARIContactSettingsField.setMultiSelect(true);
		patientCloseContactWithARIContactSettingsField.addItems((Object[]) ContactSetting.values());
		patientCloseContactWithARIContactSettingsField.setCaption(null);
		patientContactWithConfirmedCaseYesNoField = addField(EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE, NullableOptionGroup.class);
		patientContactWithConfirmedCaseExposureLocationsField = addField(EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE_EXPOSURE_LOCATIONS, OptionGroup.class);
		CssStyles.style(patientContactWithConfirmedCaseExposureLocationsField, CssStyles.OPTIONGROUP_CHECKBOXES_HORIZONTAL);
		patientContactWithConfirmedCaseExposureLocationsField.setMultiSelect(true);
		patientContactWithConfirmedCaseExposureLocationsField.addItems((Object[]) ContactSetting.values());
		patientContactWithConfirmedCaseExposureLocationsField.setCaption(null);
		patientContactWithConfirmedCaseExposureLocationCityCountryField = addField(EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE_EXPOSURE_LOCATION_CITY_COUNTRY, TextField.class);
		setVisible(false, EpiDataDto.PATIENT_TRAVELLED_TWO_WEEKS_PRIOR, EpiDataDto.PATIENT_VISITED_HEALTH_CARE_FACILITY, EpiDataDto.PATIENT_CLOSE_CONTACT_WITH_ARI, EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE);

		setVisible(false,
				EpiDataDto.PATIENT_TRAVELLED_TWO_WEEKS_PRIOR,
				EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_ONE,
				EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_TWO,
				EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_THREE,
				EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_FOUR,
				EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_ONE,
				EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_TWO,
				EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_THREE,
				EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_FOUR,
				EpiDataDto.PATIENT_VISITED_HEALTH_CARE_FACILITY,
				EpiDataDto.PATIENT_CLOSE_CONTACT_WITH_ARI,
				EpiDataDto.PATIENT_CLOSE_CONTACT_WITH_ARI_CONTACT_SETTINGS,
				EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE,
				EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE_EXPOSURE_LOCATIONS,
				EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE_EXPOSURE_LOCATION_CITY_COUNTRY
		);

		FieldHelper.setVisibleWhen(
				getFieldGroup(),
				EpiDataDto.EXPOSURES,
				EpiDataDto.EXPOSURE_DETAILS_KNOWN,
				Collections.singletonList(YesNo.YES),
				true);

		//Food Borne


		NullableOptionGroup intlTravel = addField(EpiDataDto.INTL_TRAVEL, NullableOptionGroup.class);
		TextField specifyCountries = addField(EpiDataDto.SPECIFY_COUNTRIES, TextField.class);
		DateField dateDeparture = addField(EpiDataDto.DATE_OF_DEPARTURE, DateField.class);
		DateField dateArrival = addField(EpiDataDto.DATE_OF_ARRIVAL, DateField.class);
		NullableOptionGroup domTravel = addField(EpiDataDto.DOMESTIC_TRAVEL, NullableOptionGroup.class);
		TextField specifyLocation= addField(EpiDataDto.SPECIFY_LOCATION, TextField.class);
		DateField dateDepart2= addField(EpiDataDto.DATE_OF_DEPARTURE2, DateField.class);
		DateField dateArrival2= addField(EpiDataDto.DATE_OF_ARRIVAL2, DateField.class);
		NullableOptionGroup illPerson = addField(EpiDataDto.CONTACT_ILL_PERSON, NullableOptionGroup.class);
		DateField contactDate = addField(EpiDataDto.CONTACT_DATE, DateField.class);
		TextField specifyIllnessKnown = addField(EpiDataDto.SPECIFY_ILLNESS, TextField.class);

		//AHF
		NullableOptionGroup patientTravelDuringIllness = addField(EpiDataDto.PATIENT_TRAVEL_DURING_ILLNESS, NullableOptionGroup.class);
		TextField comm1 = addField(EpiDataDto.COMM1, TextField.class);
		TextField comm2 = addField(EpiDataDto.COMM2, TextField.class);
		TextField healthCenter1 = addField(EpiDataDto.HEALTH_CENTER1, TextField.class);
		TextField healthCenter2 = addField(EpiDataDto.HEALTH_CENTER2, TextField.class);
		TextField country1 = addField(EpiDataDto.COUNTRY1, TextField.class);
		TextField country2 = addField(EpiDataDto.COUNTRY2, TextField.class);
		NullableOptionGroup wasPatientHospitalized = addField(EpiDataDto.WAS_PATIENT_HOSPITALIZED, NullableOptionGroup.class);
		TextField ifYesWhere = addField(EpiDataDto.IF_YES_WHERE, TextField.class);
		DateField hospitalizedDate1 = addField(EpiDataDto.HOSPITALIZED_DATE1, DateField.class);
		DateField hospitalizedDate2 = addField(EpiDataDto.HOSPITALIZED_DATE2, DateField.class);
		NullableOptionGroup didPatientConsultHealer = addField(EpiDataDto.DID_PATIENT_CONSULT_HEALER, NullableOptionGroup.class);
		TextField ifYesNameHealer = addField(EpiDataDto.IF_YES_NAME_HEALER, TextField.class);
		TextField community = addField(EpiDataDto.COMMUNITY, TextField.class);
		TextField country = addField(EpiDataDto.COUNTRY, TextField.class);
		TextField whenWhereContactTakePlace = addField(EpiDataDto.WHEN_WHERE_CONTACT_TAKE_PLACE, TextField.class);
		DateField dateOfContact = addField(EpiDataDto.DATE_OF_CONTACT, DateField.class);
		NullableOptionGroup patientReceiveTraditionalMedicine = addField(EpiDataDto.PATIENT_RECEIVE_TRADITIONAL_MEDICINE, NullableOptionGroup.class);
		TextField ifYesExplain = addField(EpiDataDto.IF_YES_EXPLAIN, TextField.class);
		addField(EpiDataDto.PATIENT_ATTEND_FUNERAL_CEREMONIES, NullableOptionGroup.class);
		NullableOptionGroup patientTravelAnytimePeriodBeforeIll = addField(EpiDataDto.PATIENT_TRAVEL_ANYTIME_PERIOD_BEFORE_ILL, NullableOptionGroup.class);
		TextField ifTravelYesWhere = addField(EpiDataDto.IF_TRAVEL_YES_WHERE, TextField.class);
		DateField ifTravelStartDate = addField(EpiDataDto.IF_TRAVEL_START_DATE, DateField.class);
		DateField ifYesEndDate = addField(EpiDataDto.IF_YES_END_DATE, DateField.class);
		NullableOptionGroup patientContactKnownSuspect = addField(EpiDataDto.PATIENT_CONTACT_KNOWN_SUSPECT, NullableOptionGroup.class);
		TextField suspectLastName = addField(EpiDataDto.SUSPECT_LAST_NAME, TextField.class);
		TextField suspectName = addField(EpiDataDto.SUSPECT_NAME, TextField.class);
		TextField idCase = addField(EpiDataDto.ID_CASE, TextField.class);

		ComboBox duringContactSuspectCase = new ComboBox("Outcome");

		for (CaseOutcome caseOutcome : CaseOutcome.values()) {
			if (caseOutcome == CaseOutcome.DECEASED || caseOutcome == CaseOutcome.ALIVE) {
				duringContactSuspectCase.addItem(caseOutcome);
			}
		}
		 addField(EpiDataDto.DURING_CONTACT_SUSPECT_CASE, duringContactSuspectCase);


		DateField dateOfDeath = addField(EpiDataDto.DATE_OF_DEATH, DateField.class);
		DateField dateOfLastContactWithSuspectCase = addField(EpiDataDto.DATE_OF_LAST_CONTACT_WITH_SUSPECT_CASE, DateField.class);

		setVisible(false, EpiDataDto.INTL_TRAVEL, EpiDataDto.SPECIFY_COUNTRIES, EpiDataDto.DATE_OF_DEPARTURE, EpiDataDto.DATE_OF_ARRIVAL, EpiDataDto.DOMESTIC_TRAVEL, EpiDataDto.SPECIFY_LOCATION, EpiDataDto.DATE_OF_DEPARTURE2, EpiDataDto.DATE_OF_ARRIVAL2, EpiDataDto.CONTACT_ILL_PERSON, EpiDataDto.CONTACT_DATE, EpiDataDto.SPECIFY_ILLNESS);


		setVisible(false,
				EpiDataDto.PATIENT_TRAVEL_DURING_ILLNESS, EpiDataDto.COMM1, EpiDataDto.COMM2,
				EpiDataDto.HEALTH_CENTER1, EpiDataDto.HEALTH_CENTER2, EpiDataDto.COUNTRY1,
				EpiDataDto.COUNTRY2, EpiDataDto.WAS_PATIENT_HOSPITALIZED, EpiDataDto.IF_YES_WHERE,
				EpiDataDto.HOSPITALIZED_DATE1, EpiDataDto.HOSPITALIZED_DATE2, EpiDataDto.DID_PATIENT_CONSULT_HEALER,
				EpiDataDto.IF_YES_NAME_HEALER, EpiDataDto.COMMUNITY, EpiDataDto.COUNTRY,
				EpiDataDto.WHEN_WHERE_CONTACT_TAKE_PLACE, EpiDataDto.DATE_OF_CONTACT,
				EpiDataDto.PATIENT_RECEIVE_TRADITIONAL_MEDICINE, EpiDataDto.IF_YES_EXPLAIN,
				EpiDataDto.PATIENT_ATTEND_FUNERAL_CEREMONIES, EpiDataDto.PATIENT_TRAVEL_ANYTIME_PERIOD_BEFORE_ILL,
				EpiDataDto.IF_TRAVEL_YES_WHERE, EpiDataDto.IF_TRAVEL_START_DATE, EpiDataDto.IF_YES_END_DATE,
				EpiDataDto.PATIENT_CONTACT_KNOWN_SUSPECT, EpiDataDto.SUSPECT_LAST_NAME, EpiDataDto.SUSPECT_NAME, EpiDataDto.ID_CASE,
				EpiDataDto.DURING_CONTACT_SUSPECT_CASE, EpiDataDto.DATE_OF_DEATH,
				EpiDataDto.DATE_OF_LAST_CONTACT_WITH_SUSPECT_CASE
		);


		initializeVisibilitiesAndAllowedVisibilities();
		initializeAccessAndAllowedAccesses();

		NullableOptionGroup waterUsedForDrinkingField = addField(EpiDataDto.WATER_USED_FOR_DRINKING, NullableOptionGroup.class);
		NullableOptionGroup waterUsedNotForDrinkingField = addField(EpiDataDto.WATER_USED_NOT_FOR_DRINKING, NullableOptionGroup.class);
		NullableOptionGroup foodItemsField = addField(EpiDataDto.FOOD_ITEMS, NullableOptionGroup.class);
		setVisible(false, EpiDataDto.WATER_USED_FOR_DRINKING, EpiDataDto.WATER_USED_NOT_FOR_DRINKING, EpiDataDto.FOOD_ITEMS);

		exposuresField.addValueChangeListener(e -> ogExposureDetailsKnown.setEnabled(CollectionUtils.isEmpty(exposuresField.getValue())));

		hideFieldsForSelectedDisease(disease);

		if (disease == Disease.NEW_INFLUENZA) {

			NullableOptionGroup previously = addField(EpiDataDto.PREVIOUSLY_VACCINATED_AGAINST_INFLUENZA, NullableOptionGroup.class);
			TextField vaccineName = addField(EpiDataDto.NAME_OF_VACCINE, TextField.class);
			ComboBox year = addField(EpiDataDto.YEAR_OF_VACCINATION, ComboBox.class);
			// @TODO: Done for nullselection Bug, fixed in Vaadin 7.7.3
			year.setNullSelectionAllowed(true);
			year.addItems(DateHelper.getYearsToNow());
			year.setItemCaptionMode(AbstractSelect.ItemCaptionMode.ID_TOSTRING);
			year.setInputPrompt(I18nProperties.getString(Strings.year));

			NullableOptionGroup previouslyCovid = addField(EpiDataDto.PREVIOUSLY_VACCINATED_AGAINST_COVID, NullableOptionGroup.class);
			TextField vaccineNameCovid = addField(EpiDataDto.NAME_OF_VACCINE_FOR_COVID, TextField.class);
			ComboBox yearCovid = addField(EpiDataDto.YEAR_OF_VACCINATION_FOR_COVID, ComboBox.class);
			yearCovid.setNullSelectionAllowed(true);
			yearCovid.addItems(DateHelper.getYearsToNow());
			yearCovid.setItemCaptionMode(AbstractSelect.ItemCaptionMode.ID_TOSTRING);
			yearCovid.setInputPrompt(I18nProperties.getString(Strings.year));

			vaccineName.setVisible(false);
			year.setVisible(false);
			vaccineNameCovid.setVisible(false);
			yearCovid.setVisible(false);
			contactDeadWildAnimals.setVisible(true);

			TextArea placesVisited = addField(EpiDataDto.PLACES_VISITED_PAST_7DAYS, TextArea.class);
			OptionGroup visitedPlace = addField(EpiDataDto.VISITED_PLACES_CONFIRMED_PANDEMIC, OptionGroup.class);
			ComboBox riskFactor = addField(EpiDataDto.RISK_FACTORS_SEVERE_DISEASE, ComboBox.class);
			TextField other = addField(EpiDataDto.OTHER_SPECIFY, com.vaadin.v7.ui.TextField.class);
			other.setVisible(false);

			FieldHelper.setVisibleWhen(riskFactor, Arrays.asList(other), Arrays.asList(RiskFactorCondition.OTHER), true);
			FieldHelper.setVisibleWhen(previously, Arrays.asList(year, vaccineName), Arrays.asList(YesNo.YES), true);
			FieldHelper.setVisibleWhen(previouslyCovid, Arrays.asList(yearCovid, vaccineNameCovid), Arrays.asList(YesNo.YES), true);

			FieldHelper.setVisibleWhen(contactSickDomesticAnimals, Arrays.asList(ifYesSpecifySickDomestic), Arrays.asList(YesNo.YES), true);
			FieldHelper.setVisibleWhen(contactDeadWildAnimals, Arrays.asList(ifYesSpecifyDeadWild), Arrays.asList(YesNo.YES), true);
			FieldHelper.setVisibleWhen(visitedPlace, Arrays.asList(placesVisited), Arrays.asList(YesNo.YES), true);

		}

//		PATIENT_TRAVELLED_IN_COUNTRY_ONE, PATIENT_TRAVELLED_IN_COUNTRY_TWO, PATIENT_TRAVELLED_IN_COUNTRY_THREE, PATIENT_TRAVELLED_IN_COUNTRY_FOUR, visible if PATIENT_TRAVELLED_TWO_WEEKS_PRIOR = YES
		FieldHelper.setVisibleWhen(
				patientTravelledTwoWeeksPriorYesNoUknownField,
				Arrays.asList(
						textFieldPatientTravelledInCountryOne,
						textFieldPatientTravelledInCountryTwo,
						textFieldPatientTravelledInCountryThree,
						textFieldPatientTravelledInCountryFour,
						textFieldPatientTravelledInternationalOne,
						textFieldPatientTravelledInternationalTwo,
						textFieldPatientTravelledInternationalThree,
						textFieldPatientTravelledInternationalFour),
				Arrays.asList(YesNoUnknown.YES),
				true);

		FieldHelper.setVisibleWhen(
				patientCloseContactWithARIYesNoUnkownField,
				Arrays.asList(patientCloseContactWithARIContactSettingsField),
				Arrays.asList(YesNoUnknown.YES),
				true);

		FieldHelper.setVisibleWhen(
				patientContactWithConfirmedCaseYesNoField,
				Arrays.asList(patientContactWithConfirmedCaseExposureLocationsField, patientContactWithConfirmedCaseExposureLocationCityCountryField),
				Arrays.asList(YesNoUnknown.YES),
				true);


		if (disease == Disease.CORONAVIRUS) {
			hideAllFields();
			hideLabels();
			setVisible(true, EpiDataDto.PATIENT_TRAVELLED_TWO_WEEKS_PRIOR);
			setVisible(true, EpiDataDto.PATIENT_VISITED_HEALTH_CARE_FACILITY);
			setVisible(true, EpiDataDto.PATIENT_CLOSE_CONTACT_WITH_ARI);
			setVisible(true, EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE);
		}

		if (disease == Disease.CHOLERA) {
			hideAllFields();
			hideLabels();
			setVisible(true, EpiDataDto.EXPOSED_TO_RISK_FACTOR);
			waterUsedForDrinkingField.setVisible(true);
			waterUsedNotForDrinkingField.setVisible(true);
			foodItemsField.setVisible(true);
			FieldHelper.setVisibleWhen(
					getFieldGroup(),
					EpiDataDto.WATER_USED_BY_PATIENT_AFTER_EXPOSURE,
					EpiDataDto.EXPOSED_TO_RISK_FACTOR,
					Collections.singletonList(YesNo.YES),
					true);

		}

		if(disease == Disease.FOODBORNE_ILLNESS){

			createLabel(I18nProperties.getString(Strings.headingExposureHistory), H3, EXPOSURE_HISTORY_HEADING);

			setVisible(true, EpiDataDto.INTL_TRAVEL, EpiDataDto.SPECIFY_COUNTRIES, EpiDataDto.DATE_OF_DEPARTURE, EpiDataDto.DATE_OF_ARRIVAL, EpiDataDto.DOMESTIC_TRAVEL, EpiDataDto.SPECIFY_LOCATION, EpiDataDto.DATE_OF_DEPARTURE2, EpiDataDto.DATE_OF_ARRIVAL2, EpiDataDto.CONTACT_ILL_PERSON, EpiDataDto.CONTACT_DATE, EpiDataDto.SPECIFY_ILLNESS);

			FieldHelper.setVisibleWhen(intlTravel, Arrays.asList(specifyCountries,dateDeparture,dateArrival ), Arrays.asList(YesNo.YES), true);
			FieldHelper.setVisibleWhen(domTravel, Arrays.asList(specifyLocation, dateDepart2, dateArrival2 ), Arrays.asList(YesNo.YES), true);
			FieldHelper.setVisibleWhen(illPerson, Arrays.asList(contactDate, specifyIllnessKnown), Arrays.asList(YesNo.YES), true);
		}

		if (disease == Disease.GUINEA_WORM) {
			hideAllFields();
			setVisible(true, EpiDataDto.PERSON_TRAVEL_HISTORY, EpiDataDto.CONTAMINATION_SOURCES, EpiDataDto.CONTAINMENT_MEASURES);
			Label travelHistoryHeading = new Label(I18nProperties.getString(Strings.headingTravelHistoryOfPatientTenToFourteenMonth));
			travelHistoryHeading.setStyleName(H3);
			getContent().addComponent(travelHistoryHeading, TRAVEL_HISTORY_HEADING);

			Label contaminationSourceHeading = new Label(I18nProperties.getString(Strings.headingContaminationSources));
			contaminationSourceHeading.setStyleName(H3);
			getContent().addComponent(contaminationSourceHeading, CONTAMINATION_SOURCE_HEADING);

			Label containmentMeasuresHeading = new Label(I18nProperties.getString(Strings.headingContainmentMeasures));
			containmentMeasuresHeading.setStyleName(H3);
			getContent().addComponent(containmentMeasuresHeading, CONTAINMENT_MEASURES_HEADING);

			setVisible(true, EpiDataDto.RECEIVED_HEALTH_EDUCATION, EpiDataDto.PATIENT_ENTERED_WATER_SOURCE, EpiDataDto.PLACE_MANAGED);
		}

		if (disease == Disease.NEONATAL_TETANUS) {
			hideAllFields();
			hideLabels();
		}


		if (disease == Disease.AHF) {
			createLabel(I18nProperties.getString(Strings.headingExpositionRisks), H3, EXPOSITION_RISKS_HEADING);

			Label indicatePlacesheading = new Label(I18nProperties.getString(Strings.indicatePlace));
			indicatePlacesheading.setStyleName(H3);
			getContent().addComponent(indicatePlacesheading, INDICATE_PLACES_COUNTRY_HEADING);
			indicatePlacesheading.setVisible(false);


			setVisible(true,
					EpiDataDto.PATIENT_TRAVEL_DURING_ILLNESS, EpiDataDto.WAS_PATIENT_HOSPITALIZED, EpiDataDto.IF_YES_WHERE,
					EpiDataDto.HOSPITALIZED_DATE1, EpiDataDto.HOSPITALIZED_DATE2, EpiDataDto.DID_PATIENT_CONSULT_HEALER,
					EpiDataDto.IF_YES_NAME_HEALER, EpiDataDto.COMMUNITY, EpiDataDto.COUNTRY,
					EpiDataDto.WHEN_WHERE_CONTACT_TAKE_PLACE, EpiDataDto.DATE_OF_CONTACT,
					EpiDataDto.PATIENT_RECEIVE_TRADITIONAL_MEDICINE, EpiDataDto.IF_YES_EXPLAIN,
					EpiDataDto.PATIENT_ATTEND_FUNERAL_CEREMONIES, EpiDataDto.PATIENT_TRAVEL_ANYTIME_PERIOD_BEFORE_ILL,
					EpiDataDto.IF_TRAVEL_YES_WHERE, EpiDataDto.IF_TRAVEL_START_DATE, EpiDataDto.IF_YES_END_DATE,
					EpiDataDto.PATIENT_CONTACT_KNOWN_SUSPECT, EpiDataDto.SUSPECT_LAST_NAME, EpiDataDto.SUSPECT_NAME, EpiDataDto.ID_CASE,
					EpiDataDto.DURING_CONTACT_SUSPECT_CASE, EpiDataDto.DATE_OF_DEATH,
					EpiDataDto.DATE_OF_LAST_CONTACT_WITH_SUSPECT_CASE);

			contactDeadWildAnimals.setVisible(true);

			FieldHelper.setVisibleWhen(patientContactKnownSuspect, Arrays.asList(suspectName, suspectLastName, idCase, duringContactSuspectCase, dateOfLastContactWithSuspectCase), Arrays.asList(YesNo.YES), true);
			FieldHelper.setVisibleWhen(contactDeadWildAnimals, Arrays.asList(ifYesSpecifyDeadWild, ifYesWildAnimalLocation, ifYesWildAnimalDate), Arrays.asList(YesNo.YES), true);
			FieldHelper.setVisibleWhen(duringContactSuspectCase, Arrays.asList(dateOfDeath), Arrays.asList(CaseOutcome.DECEASED), true);
			FieldHelper.setVisibleWhen(wasPatientHospitalized, Arrays.asList(ifYesWhere, hospitalizedDate1, hospitalizedDate2), Arrays.asList(YesNo.YES), true);
			FieldHelper.setVisibleWhen(didPatientConsultHealer, Arrays.asList(ifYesNameHealer, community, country, whenWhereContactTakePlace, dateOfContact), Arrays.asList(YesNo.YES), true);
			FieldHelper.setVisibleWhen(patientTravelAnytimePeriodBeforeIll, Arrays.asList(ifTravelYesWhere, ifTravelStartDate, ifYesEndDate), Arrays.asList(YesNo.YES), true);
			FieldHelper.setVisibleWhen(patientReceiveTraditionalMedicine, Arrays.asList(ifYesExplain), Arrays.asList(YesNo.YES), true);

			FieldHelper.setVisibleWhen(patientTravelDuringIllness, Arrays.asList(comm1, comm2, healthCenter1, healthCenter2, country1, country2), Arrays.asList(YesNo.YES), true);

			patientTravelDuringIllness.addValueChangeListener(event -> {
				YesNo varField = (YesNo) FieldHelper.getNullableSourceFieldValue((Field) event.getProperty());
                indicatePlacesheading.setVisible(varField == YesNo.YES);
			});

		}
	}

	private void addPersonTravelHistoryFields() {
		PersonTravelHistoryField personTravelHistoryField = addField(EpiDataDto.PERSON_TRAVEL_HISTORY, PersonTravelHistoryField.class);
		personTravelHistoryField.setWidthFull();
		personTravelHistoryField.setPseudonymized(isPseudonymized);
	}

	private void addContaminationSourcesFields() {
		ContaminationSourcesField contaminationSourcesField = addField(EpiDataDto.CONTAMINATION_SOURCES, ContaminationSourcesField.class);
		contaminationSourcesField.setWidthFull();
		contaminationSourcesField.setPseudonymized(isPseudonymized);
	}

	private void addContainmentMeasuresFields() {
		ContainmentMeasureField containmentMeasureField = addField(EpiDataDto.CONTAINMENT_MEASURES, ContainmentMeasureField.class);
		containmentMeasureField.setWidthFull();
		containmentMeasureField.setPseudonymized(isPseudonymized);
	}

	private void addActivityAsCaseFields() {

		if(disease != Disease.CSM && disease != Disease.FOODBORNE_ILLNESS && disease != Disease.AFP && disease != Disease.AHF && disease != Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS){
			getContent().addComponent(
					new MultilineLabel(
							h3(I18nProperties.getString(Strings.headingActivityAsCase))
									+ divsCss(VSPACE_3, I18nProperties.getString(Strings.infoActivityAsCaseInvestigation)),
							ContentMode.HTML),
					LOC_ACTIVITY_AS_CASE_INVESTIGATION_HEADING);
		}

		NullableOptionGroup ogActivityAsCaseDetailsKnown = addField(EpiDataDto.ACTIVITY_AS_CASE_DETAILS_KNOWN, NullableOptionGroup.class);
		ActivityAsCaseField activityAsCaseField = addField(EpiDataDto.ACTIVITIES_AS_CASE, ActivityAsCaseField.class);
		activityAsCaseField.setWidthFull();
		activityAsCaseField.setPseudonymized(isPseudonymized);

		FieldHelper.setVisibleWhen(
				getFieldGroup(),
				EpiDataDto.ACTIVITIES_AS_CASE,
				EpiDataDto.ACTIVITY_AS_CASE_DETAILS_KNOWN,
				Collections.singletonList(YesNo.YES),
				true);

		activityAsCaseField.addValueChangeListener(e -> ogActivityAsCaseDetailsKnown.setEnabled(CollectionUtils.isEmpty(activityAsCaseField.getValue())));
	}

	private void addHeadingsAndInfoTexts() {
		getContent().addComponent(
				new MultilineLabel(
						h3(I18nProperties.getString(Strings.headingExposureInvestigation))
								+ divsCss(
								VSPACE_3,
								I18nProperties.getString(
										parentClass == ContactDto.class ? Strings.infoExposureInvestigationContacts : Strings.infoExposureInvestigation)),
						ContentMode.HTML),
				LOC_EXPOSURE_INVESTIGATION_HEADING);

		getContent().addComponent(
				new MultilineLabel(divsCss(VSPACE_3, I18nProperties.getString(Strings.infoEpiDataFieldsHint)), ContentMode.HTML),
				LOC_EPI_DATA_FIELDS_HINT);

		getContent().addComponent(
				new MultilineLabel(
						h3(I18nProperties.getString(Strings.headingEpiDataSourceCaseContacts))
								+ divsCss(VSPACE_3, I18nProperties.getString(Strings.infoEpiDataSourceCaseContacts)),
						ContentMode.HTML),
				LOC_SOURCE_CASE_CONTACTS_HEADING);
	}

	public void disableContactWithSourceCaseKnownField() {
		setEnabled(false, EpiDataDto.CONTACT_WITH_SOURCE_CASE_KNOWN);
	}

	public void setGetSourceContactsCallback(Supplier<List<ContactReferenceDto>> callback) {
		((ExposuresField) getField(EpiDataDto.EXPOSURES)).setGetSourceContactsCallback(callback);
	}

	@Override
	protected String createHtmlLayout() {
		Disease caseDisease = getCaseDisease();
        if (Objects.requireNonNull(caseDisease) == Disease.MEASLES) {
            return MEASLES_LAYOUT;
        }
        return parentClass == CaseDataDto.class ? MAIN_HTML_LAYOUT + SOURCE_CONTACTS_HTML_LAYOUT : MAIN_HTML_LAYOUT;
    }

	public void hideFieldsForSelectedDisease(Disease disease) {
		Set<String> disabledFields = EpiFormConfiguration.getDisabledFieldsForDisease(disease);
		for (String field : disabledFields) {
			disableField(field);
		}

		//get headings if not null hide create a list of headings
		Arrays.asList(LOC_EXPOSURE_INVESTIGATION_HEADING, LOC_EXPOSURE_TRAVEL_HISTORY_HEADING, LOC_ACTIVITY_AS_CASE_INVESTIGATION_HEADING, LOC_SOURCE_CASE_CONTACTS_HEADING, LOC_EPI_DATA_FIELDS_HINT)
				.stream()
				.filter(heading -> getContent().getComponent(heading) != null)
				.forEach(heading -> getContent().getComponent(heading).setVisible(false));

	}

	Set<Disease> excludedDiseases = EnumSet.of(
			Disease.CSM,
			Disease.FOODBORNE_ILLNESS,
			Disease.AFP,
			Disease.AHF,
			Disease.IMMEDIATE_CASE_BASED_FORM_OTHER_CONDITIONS
	);

	private Label createLabel(String text, String h4, String location) {
		final Label label = new Label(text);
		label.setId(text);
		label.addStyleName(h4);
		getContent().addComponent(label, location);
		return label;
	}

	private void disableField(String field) {
		setVisible(false, field);
	}


	public void hideLabels () {
		Arrays.asList(LOC_EXPOSURE_INVESTIGATION_HEADING, LOC_ACTIVITY_AS_CASE_INVESTIGATION_HEADING, LOC_SOURCE_CASE_CONTACTS_HEADING).stream().map(
				//check if not null then hide
				label -> getContent().getComponent(label)).filter(
				label -> label != null).forEach(
				label -> label.setVisible(false));
	}
}
