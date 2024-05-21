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
import static de.symeda.sormas.ui.utils.LayoutUtil.*;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.vaadin.ui.Label;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.utils.CardOrHistory;
import de.symeda.sormas.api.utils.RiskFactorInfluenza;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.ui.utils.*;
import org.apache.commons.collections.CollectionUtils;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.v7.ui.Field;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.contact.ContactReferenceDto;
import de.symeda.sormas.api.epidata.EpiDataDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.YesNoUnknown;
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
	private static final String OBTAIN_HISTORY_HEADING = "obtainHistoryHeading";
	private static final String DAY_1_HEADING = "day1Heading";
	private static final String DAY_2_HEADING = "day2Heading";
	private static final String DAY_3_HEADING = "day3Heading";
	private static final String LOC_SOURCE_CASE_CONTACTS_HEADING = "locSourceCaseContactsHeading";
	private static final String LOC_EPI_DATA_FIELDS_HINT = "locEpiDataFieldsHint";
	private static final String EXPOSURE_HISTORY_HEADING = "exposureHistoryHeading";
	private static final String OTHER_PERSONS_HEADING = "otherPersonsHeading";
	private static final String NUMBER_OF_PERSONS_NO_AFFECTED = "numberOfPersonsNoAffected";

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

					loc(EpiDataDto.RECENT_TRAVEL_OUTBREAK)+
					loc(EpiDataDto.CONTACT_SIMILAR_SYMPTOMS)+
					loc(EpiDataDto.CONTACT_SICK_ANIMALS)+
					loc(EpiDataDto.IF_YES_SPECIFY_SICK_ANIMAL)+
					loc(EpiDataDto.CONTACT_DEAD_WILD_ANIMALS)+
					loc(EpiDataDto.IF_YES_SPECIFY_DEAD_WILD_ANIMAL)+
					loc(LOC_EXPOSURE_INVESTIGATION_HEADING) +
					loc(EpiDataDto.EXPOSURE_DETAILS_KNOWN) +
					loc(EpiDataDto.EXPOSURES) +
					loc(LOC_ACTIVITY_AS_CASE_INVESTIGATION_HEADING) +
					loc(EpiDataDto.ACTIVITY_AS_CASE_DETAILS_KNOWN)+

					loc(EpiDataDto.ACTIVITIES_AS_CASE) +
					locCss(VSPACE_TOP_3, LOC_EPI_DATA_FIELDS_HINT) +
					loc(EpiDataDto.HIGH_TRANSMISSION_RISK_AREA) +
					loc(EpiDataDto.LARGE_OUTBREAKS_AREA) +
					loc(EpiDataDto.AREA_INFECTED_ANIMALS) +

					fluidRowLocs(EpiDataDto.SUSPECTED_FOOD, EpiDataDto.DATE_CONSUMED) +
					fluidRowLocs(EpiDataDto.FOOD_SOURCE, EpiDataDto.EVENT_TYPE, EpiDataDto.EVENT_OTHER_SPECIFY) +
					loc(OBTAIN_HISTORY_HEADING) +
					loc(DAY_1_HEADING) +
					fluidRowLocs(EpiDataDto.BREAKFAST, EpiDataDto.TOTAL_NO_PERSONS, EpiDataDto.FOOD_CONSUMED, EpiDataDto.SOURCE_OF_FOOD, EpiDataDto.CONSUMED_AT_PLACE) +
					fluidRowLocs(EpiDataDto.LUNCH, EpiDataDto.TOTAL_NO_PERSONS_L1, EpiDataDto.FOOD_CONSUMED_L1, EpiDataDto.SOURCE_OF_FOOD_L1, EpiDataDto.CONSUMED_AT_PLACE_L1) +
					fluidRowLocs(EpiDataDto.SUPPER, EpiDataDto.TOTAL_NO_PERSONS_S1, EpiDataDto.FOOD_CONSUMED_S1, EpiDataDto.SOURCE_OF_FOODS_S1, EpiDataDto.CONSUMED_AT_PLACE_S1) +

					loc(DAY_2_HEADING) +
					fluidRowLocs(EpiDataDto.BREAKFAST2, EpiDataDto.TOTAL_NO_PERSONS2, EpiDataDto.FOOD_CONSUMED2, EpiDataDto.SOURCE_OF_FOOD2, EpiDataDto.CONSUMED_AT_PLACE2) +
					fluidRowLocs(EpiDataDto.LUNCH_L2, EpiDataDto.TOTAL_NO_PERSONS_L2, EpiDataDto.FOOD_CONSUMED_L2, EpiDataDto.SOURCE_OF_FOOD_L2, EpiDataDto.CONSUMED_AT_PLACE_L2) +
					fluidRowLocs(EpiDataDto.SUPPER_S2, EpiDataDto.TOTAL_NO_PERSONS_S2, EpiDataDto.FOOD_CONSUMED_S2, EpiDataDto.SOURCE_OF_FOOD_S2, EpiDataDto.CONSUMED_AT_PLACE_S2) +

					loc(DAY_3_HEADING) +
					fluidRowLocs(EpiDataDto.BREAKFAST3, EpiDataDto.TOTAL_NO_PERSONS3, EpiDataDto.FOOD_CONSUMED3, EpiDataDto.SOURCE_OF_FOOD3, EpiDataDto.CONSUMED_AT_PLACE3) +
					fluidRowLocs(EpiDataDto.LUNCH_L3, EpiDataDto.TOTAL_NO_PERSONS_L3, EpiDataDto.FOOD_CONSUMED_L3, EpiDataDto.SOURCE_OF_FOOD_L3, EpiDataDto.CONSUMED_AT_PLACE_L3) +
					fluidRowLocs(EpiDataDto.SUPPER_S3, EpiDataDto.TOTAL_NO_PERSONS_S3, EpiDataDto.FOOD_CONSUMED_S3, EpiDataDto.SOURCE_OF_FOOD_S3, EpiDataDto.CONSUMED_AT_PLACE_S3) +

					loc(OTHER_PERSONS_HEADING) +
					loc(NUMBER_OF_PERSONS_NO_AFFECTED) +
					fluidRowLocs(EpiDataDto.NAME_OF_AFFECTED_PERSON, EpiDataDto.TEL_NO, EpiDataDto.DATE_TIME, EpiDataDto.AGE) +
					fluidRowLocs(EpiDataDto.NAME_OF_AFFECTED_PERSON2, EpiDataDto.TEL_NO2, EpiDataDto.DATE_TIME2, EpiDataDto.AGE2) +
					fluidRowLocs(EpiDataDto.NAME_OF_AFFECTED_PERSON3, EpiDataDto.TEL_NO3, EpiDataDto.DATE_TIME3, EpiDataDto.AGE3) +
					fluidRowLocs(EpiDataDto.NAME_OF_AFFECTED_PERSON4, EpiDataDto.TEL_NO4, EpiDataDto.DATE_TIME4, EpiDataDto.AGE4) +
					loc(EXPOSURE_HISTORY_HEADING) +
					fluidRowLocs(EpiDataDto.INTL_TRAVEL, EpiDataDto.SPECIFY_COUNTRIES, EpiDataDto.DATE_OF_DEPARTURE, EpiDataDto.DATE_OF_ARRIVAL) +
					fluidRowLocs(EpiDataDto.DOMESTIC_TRAVEL, EpiDataDto.SPECIFY_LOCATION, EpiDataDto.DATE_OF_DEPARTURE2, EpiDataDto.DATE_OF_ARRIVAL2) +
					fluidRowLocs(EpiDataDto.CONTACT_ILL_PERSON, EpiDataDto.CONTACT_DATE, EpiDataDto.SPECIFY_ILLNESS) +
					fluidRowLocs(EpiDataDto.AREA_INFECTED_ANIMALS);

	private static final String SOURCE_CONTACTS_HTML_LAYOUT =
			locCss(VSPACE_TOP_3, LOC_SOURCE_CASE_CONTACTS_HEADING) +
					loc(EpiDataDto.CONTACT_WITH_SOURCE_CASE_KNOWN);
	//@formatter:on

	private final Disease disease;
	private final Class<? extends EntityDto> parentClass;
	private final Consumer<Boolean> sourceContactsToggleCallback;
	private final boolean isPseudonymized;

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
				isEditAllowed);
		this.disease = disease;
		this.parentClass = parentClass;
		this.sourceContactsToggleCallback = sourceContactsToggleCallback;
		this.isPseudonymized = isPseudonymized;
		addFields();
	}

	@Override
	protected void addFields() {


		if (disease != null && !diseaseCheck() && !diseaseInfluenzaCheck() && disease!= Disease.FOODBORNE_ILLNESS) {
			addHeadingsAndInfoTexts();
		}

		NullableOptionGroup recentTravelOutbreak = addField(EpiDataDto.RECENT_TRAVEL_OUTBREAK, NullableOptionGroup.class);
		NullableOptionGroup contactSimilarOutbreak = addField(EpiDataDto.CONTACT_SIMILAR_SYMPTOMS, NullableOptionGroup.class);

		NullableOptionGroup contactSickDomesticAnimals = addField(EpiDataDto.CONTACT_SICK_ANIMALS, NullableOptionGroup.class);
		TextField ifYesSpecifySickDomestic = addField(EpiDataDto.IF_YES_SPECIFY_SICK_ANIMAL, TextField.class);
		NullableOptionGroup contactDeadWildAnimals = addField(EpiDataDto.CONTACT_DEAD_WILD_ANIMALS, NullableOptionGroup.class);
		TextField ifYesSpecifyDeadWild = addField(EpiDataDto.IF_YES_SPECIFY_DEAD_WILD_ANIMAL, TextField.class);

		contactDeadWildAnimals.setVisible(false);
		ifYesSpecifySickDomestic.setVisible(false);
		ifYesSpecifyDeadWild.setVisible(false);

		NullableOptionGroup ogExposureDetailsKnown = addField(EpiDataDto.EXPOSURE_DETAILS_KNOWN, NullableOptionGroup.class);
		ExposuresField exposuresField = addField(EpiDataDto.EXPOSURES, ExposuresField.class);
		exposuresField.setEpiDataParentClass(parentClass);
		exposuresField.setWidthFull();
		exposuresField.setPseudonymized(isPseudonymized);

		if (parentClass == CaseDataDto.class) {
			addActivityAsCaseFields();
		}

		addField(EpiDataDto.HIGH_TRANSMISSION_RISK_AREA, NullableOptionGroup.class);
		addField(EpiDataDto.LARGE_OUTBREAKS_AREA, NullableOptionGroup.class);
		addField(EpiDataDto.AREA_INFECTED_ANIMALS, NullableOptionGroup.class);
		NullableOptionGroup ogContactWithSourceCaseKnown = addField(EpiDataDto.CONTACT_WITH_SOURCE_CASE_KNOWN, NullableOptionGroup.class);

		if (sourceContactsToggleCallback != null) {
			ogContactWithSourceCaseKnown.addValueChangeListener(e -> {
				YesNo sourceContactsKnown = (YesNo) FieldHelper.getNullableSourceFieldValue((Field) e.getProperty());
				sourceContactsToggleCallback.accept(YesNo.YES == sourceContactsKnown);
			});
		}

		FieldHelper.setVisibleWhen(
				getFieldGroup(),
				EpiDataDto.EXPOSURES,
				EpiDataDto.EXPOSURE_DETAILS_KNOWN,
				Collections.singletonList(YesNo.YES),
				true);

		//Food Borne
		addFields(EpiDataDto.NAME_OF_AFFECTED_PERSON, EpiDataDto.NAME_OF_AFFECTED_PERSON2, EpiDataDto.NAME_OF_AFFECTED_PERSON3, EpiDataDto.NAME_OF_AFFECTED_PERSON4);
		addFields(EpiDataDto.TEL_NO, EpiDataDto.TEL_NO2, EpiDataDto.TEL_NO3, EpiDataDto.TEL_NO4);
		addField(EpiDataDto.DATE_TIME, DateTimeField.class);
		addField(EpiDataDto.DATE_TIME2, DateTimeField.class);
		addField(EpiDataDto.DATE_TIME3, DateTimeField.class);
		addField(EpiDataDto.DATE_TIME4, DateTimeField.class);
		addFields(EpiDataDto.AGE, EpiDataDto.AGE2, EpiDataDto.AGE3, EpiDataDto.AGE4);

		addField(EpiDataDto.INTL_TRAVEL, NullableOptionGroup.class);
		addField(EpiDataDto.SPECIFY_COUNTRIES, TextField.class);
		addField(EpiDataDto.DATE_OF_DEPARTURE, DateField.class);
		addField(EpiDataDto.DATE_OF_ARRIVAL, DateField.class);
		addField(EpiDataDto.DOMESTIC_TRAVEL, NullableOptionGroup.class);
		addField(EpiDataDto.SPECIFY_LOCATION, TextField.class);
		addField(EpiDataDto.DATE_OF_DEPARTURE2, DateField.class);
		addField(EpiDataDto.DATE_OF_ARRIVAL2, DateField.class);
		NullableOptionGroup illPerson = addField(EpiDataDto.CONTACT_ILL_PERSON, NullableOptionGroup.class);
		DateField contactDate = addField(EpiDataDto.CONTACT_DATE, DateField.class);
		addField(EpiDataDto.SPECIFY_ILLNESS, TextField.class);

		addField(EpiDataDto.SUSPECTED_FOOD, TextField.class);
		addField(EpiDataDto.DATE_CONSUMED, DateTimeField.class);
		addField(EpiDataDto.FOOD_SOURCE, ComboBox.class);
		addField(EpiDataDto.EVENT_TYPE, ComboBox.class);
		addField(EpiDataDto.EVENT_OTHER_SPECIFY, TextField.class);

		addField(EpiDataDto.BREAKFAST, NullableOptionGroup.class);
		TextField totalNoPerson = addField(EpiDataDto.TOTAL_NO_PERSONS, TextField.class);
		totalNoPerson.addStyleName("v-captiontext-foodborne");
		addField(EpiDataDto.FOOD_CONSUMED, TextField.class);
		addField(EpiDataDto.SOURCE_OF_FOOD, TextField.class);
		addField(EpiDataDto.CONSUMED_AT_PLACE, TextField.class);
		addField(EpiDataDto.LUNCH, NullableOptionGroup.class);
		TextField totalNoPersonl1 = addField(EpiDataDto.TOTAL_NO_PERSONS_L1, TextField.class);
		totalNoPersonl1.addStyleName("v-captiontext-foodborne");
		addField(EpiDataDto.FOOD_CONSUMED_L1, TextField.class);
		addField(EpiDataDto.SOURCE_OF_FOOD_L1, TextField.class);
		addField(EpiDataDto.CONSUMED_AT_PLACE_L1, TextField.class);
		addField(EpiDataDto.SUPPER, NullableOptionGroup.class);
		TextField totalNoPersons1 = addField(EpiDataDto.TOTAL_NO_PERSONS_S1, TextField.class);
		totalNoPersons1.addStyleName("v-captiontext-foodborne");
		addField(EpiDataDto.FOOD_CONSUMED_S1, TextField.class);
		addField(EpiDataDto.SOURCE_OF_FOODS_S1, TextField.class);
		addField(EpiDataDto.CONSUMED_AT_PLACE_S1, TextField.class);

		addField(EpiDataDto.BREAKFAST2, NullableOptionGroup.class);
		TextField totalNoPerson2 = addField(EpiDataDto.TOTAL_NO_PERSONS2, TextField.class);
		totalNoPerson2.addStyleName("v-captiontext-foodborne");
		addField(EpiDataDto.FOOD_CONSUMED2, TextField.class);
		addField(EpiDataDto.SOURCE_OF_FOOD2, TextField.class);
		addField(EpiDataDto.CONSUMED_AT_PLACE2, TextField.class);
		addField(EpiDataDto.LUNCH_L2, NullableOptionGroup.class);
		TextField totalNoPersonl2 = addField(EpiDataDto.TOTAL_NO_PERSONS_L2, TextField.class);
		totalNoPersonl2.addStyleName("v-captiontext-foodborne");
		addField(EpiDataDto.FOOD_CONSUMED_L2, TextField.class);
		addField(EpiDataDto.SOURCE_OF_FOOD_L2, TextField.class);
		addField(EpiDataDto.CONSUMED_AT_PLACE_L2, TextField.class);
		addField(EpiDataDto.SUPPER_S2, NullableOptionGroup.class);
		TextField totalNoPersonls2 =  addField(EpiDataDto.TOTAL_NO_PERSONS_S2, TextField.class);
		totalNoPersonls2.addStyleName("v-captiontext-foodborne");
		addField(EpiDataDto.FOOD_CONSUMED_S2, TextField.class);
		addField(EpiDataDto.SOURCE_OF_FOOD_S2, TextField.class);
		addField(EpiDataDto.CONSUMED_AT_PLACE_S2, TextField.class);

		addField(EpiDataDto.BREAKFAST3, NullableOptionGroup.class);
		TextField totalNoPerson3 = addField(EpiDataDto.TOTAL_NO_PERSONS3, TextField.class);
		totalNoPerson3.addStyleName("v-captiontext-foodborne");
		addField(EpiDataDto.FOOD_CONSUMED3, TextField.class);
		addField(EpiDataDto.SOURCE_OF_FOOD3, TextField.class);
		addField(EpiDataDto.CONSUMED_AT_PLACE3, TextField.class);
		addField(EpiDataDto.LUNCH_L3, NullableOptionGroup.class);
		TextField totalNoPersonl3 = addField(EpiDataDto.TOTAL_NO_PERSONS_L3, TextField.class);
		totalNoPersonl3.addStyleName("v-captiontext-foodborne");
		addField(EpiDataDto.FOOD_CONSUMED_L3, TextField.class);
		addField(EpiDataDto.SOURCE_OF_FOOD_L3, TextField.class);
		addField(EpiDataDto.CONSUMED_AT_PLACE_L3, TextField.class);
		addField(EpiDataDto.SUPPER_S3, NullableOptionGroup.class);
		TextField totalNoPersons3 = addField(EpiDataDto.TOTAL_NO_PERSONS_S3, TextField.class);
		totalNoPersons3.addStyleName("v-captiontext-foodborne");
		addField(EpiDataDto.FOOD_CONSUMED_S3, TextField.class);
		addField(EpiDataDto.SOURCE_OF_FOOD_S3, TextField.class);
		addField(EpiDataDto.CONSUMED_AT_PLACE_S3, TextField.class);


		setVisible(false, EpiDataDto.INTL_TRAVEL, EpiDataDto.SPECIFY_COUNTRIES, EpiDataDto.DATE_OF_DEPARTURE, EpiDataDto.DATE_OF_ARRIVAL, EpiDataDto.DOMESTIC_TRAVEL, EpiDataDto.SPECIFY_LOCATION, EpiDataDto.DATE_OF_DEPARTURE2, EpiDataDto.DATE_OF_ARRIVAL2, EpiDataDto.CONTACT_ILL_PERSON, EpiDataDto.CONTACT_DATE, EpiDataDto.SPECIFY_ILLNESS, EpiDataDto.NAME_OF_AFFECTED_PERSON, EpiDataDto.NAME_OF_AFFECTED_PERSON2, EpiDataDto.NAME_OF_AFFECTED_PERSON3, EpiDataDto.NAME_OF_AFFECTED_PERSON4, EpiDataDto.TEL_NO, EpiDataDto.TEL_NO2, EpiDataDto.TEL_NO3, EpiDataDto.TEL_NO4, EpiDataDto.DATE_TIME, EpiDataDto.DATE_TIME2, EpiDataDto.DATE_TIME3, EpiDataDto.DATE_TIME4, EpiDataDto.AGE,  EpiDataDto.AGE2, EpiDataDto.AGE3, EpiDataDto.AGE4);

		setVisible(false, EpiDataDto.SUSPECTED_FOOD, EpiDataDto.DATE_CONSUMED,
				EpiDataDto.FOOD_SOURCE, EpiDataDto.EVENT_TYPE, EpiDataDto.EVENT_OTHER_SPECIFY,
				EpiDataDto.BREAKFAST, EpiDataDto.TOTAL_NO_PERSONS, EpiDataDto.FOOD_CONSUMED, EpiDataDto.SOURCE_OF_FOOD, EpiDataDto.CONSUMED_AT_PLACE,
				EpiDataDto.LUNCH, EpiDataDto.TOTAL_NO_PERSONS_L1, EpiDataDto.FOOD_CONSUMED_L1, EpiDataDto.SOURCE_OF_FOOD_L1, EpiDataDto.CONSUMED_AT_PLACE_L1,
				EpiDataDto.SUPPER, EpiDataDto.TOTAL_NO_PERSONS_S1, EpiDataDto.FOOD_CONSUMED_S1, EpiDataDto.SOURCE_OF_FOODS_S1, EpiDataDto.CONSUMED_AT_PLACE_S1,
				EpiDataDto.BREAKFAST2, EpiDataDto.TOTAL_NO_PERSONS2, EpiDataDto.FOOD_CONSUMED2, EpiDataDto.SOURCE_OF_FOOD2, EpiDataDto.CONSUMED_AT_PLACE2,
				EpiDataDto.LUNCH_L2, EpiDataDto.TOTAL_NO_PERSONS_L2, EpiDataDto.FOOD_CONSUMED_L2, EpiDataDto.SOURCE_OF_FOOD_L2, EpiDataDto.CONSUMED_AT_PLACE_L2,
				EpiDataDto.SUPPER_S2, EpiDataDto.TOTAL_NO_PERSONS_S2, EpiDataDto.FOOD_CONSUMED_S2, EpiDataDto.SOURCE_OF_FOOD_S2, EpiDataDto.CONSUMED_AT_PLACE_S2,
				EpiDataDto.BREAKFAST3, EpiDataDto.TOTAL_NO_PERSONS3, EpiDataDto.FOOD_CONSUMED3, EpiDataDto.SOURCE_OF_FOOD3, EpiDataDto.CONSUMED_AT_PLACE3,
				EpiDataDto.LUNCH_L3, EpiDataDto.TOTAL_NO_PERSONS_L3, EpiDataDto.FOOD_CONSUMED_L3, EpiDataDto.SOURCE_OF_FOOD_L3, EpiDataDto.CONSUMED_AT_PLACE_L3,
				EpiDataDto.SUPPER_S3, EpiDataDto.TOTAL_NO_PERSONS_S3, EpiDataDto.FOOD_CONSUMED_S3, EpiDataDto.SOURCE_OF_FOOD_S3, EpiDataDto.CONSUMED_AT_PLACE_S3
		);

		initializeVisibilitiesAndAllowedVisibilities();
		initializeAccessAndAllowedAccesses();

		exposuresField.addValueChangeListener(e -> {
			ogExposureDetailsKnown.setEnabled(CollectionUtils.isEmpty(exposuresField.getValue()));
		});

		hideFieldsForSelectedDisease(disease);

		if (diseaseCheck()) {
			setVisible(false, EpiDataDto.EXPOSURES, EpiDataDto.EXPOSURE_DETAILS_KNOWN, EpiDataDto.CONTACT_WITH_SOURCE_CASE_KNOWN);
		}

		if (diseaseCSMCheck()) {
			recentTravelOutbreak.setVisible(false);
			contactSickDomesticAnimals.setVisible(false);

			setVisible(false, EpiDataDto.HIGH_TRANSMISSION_RISK_AREA, EpiDataDto.LARGE_OUTBREAKS_AREA, EpiDataDto.ACTIVITY_AS_CASE_DETAILS_KNOWN);

		}

		if(disease == Disease.NEW_INFLUENZA){
			setVisible(false, EpiDataDto.HIGH_TRANSMISSION_RISK_AREA, EpiDataDto.LARGE_OUTBREAKS_AREA, EpiDataDto.AREA_INFECTED_ANIMALS);
			setVisible(false, EpiDataDto.CONTACT_WITH_SOURCE_CASE_KNOWN, EpiDataDto.EXPOSURES, EpiDataDto.EXPOSURE_DETAILS_KNOWN, EpiDataDto.ACTIVITY_AS_CASE_DETAILS_KNOWN);

			NullableOptionGroup previously = addField(EpiDataDto.PREVIOUSLY_VACCINATED_AGAINST_INFLUENZA, NullableOptionGroup.class);
			TextField vaccineName = addField(EpiDataDto.NAME_OF_VACCINE, TextField.class);
			DateField year = addField(EpiDataDto.YEAR_OF_VACCINATION, DateField.class);

			NullableOptionGroup previouslyCovid = addField(EpiDataDto.PREVIOUSLY_VACCINATED_AGAINST_COVID, NullableOptionGroup.class);
			TextField vaccineNameCovid = addField(EpiDataDto.NAME_OF_VACCINE_FOR_COVID, TextField.class);
			DateField yearCovid = addField(EpiDataDto.YEAR_OF_VACCINATION_FOR_COVID, DateField.class);

			vaccineName.setVisible(false);
			year.setVisible(false);
			vaccineNameCovid.setVisible(false);
			yearCovid.setVisible(false);
			contactDeadWildAnimals.setVisible(true);

			TextArea placesVisited = addField(EpiDataDto.PLACES_VISITED_PAST_7DAYS, com.vaadin.v7.ui.TextArea.class);
			OptionGroup visitedPlace = addField(EpiDataDto.VISITED_PLACES_CONFIRMED_PANDEMIC, OptionGroup.class);
			ComboBox riskFactor = addField(EpiDataDto.RISK_FACTORS_SEVERE_DISEASE, ComboBox.class);
			TextField other = addField(EpiDataDto.OTHER_SPECIFY, com.vaadin.v7.ui.TextField.class);
			other.setVisible(false);

			FieldHelper.setVisibleWhen(riskFactor, Arrays.asList(other), Arrays.asList(RiskFactorInfluenza.OTHER), true);
			FieldHelper.setVisibleWhen(previously, Arrays.asList(year, vaccineName), Arrays.asList(YesNo.YES), true);
			FieldHelper.setVisibleWhen(previouslyCovid, Arrays.asList(yearCovid, vaccineNameCovid), Arrays.asList(YesNo.YES), true);

			FieldHelper.setVisibleWhen(contactSickDomesticAnimals, Arrays.asList(ifYesSpecifySickDomestic), Arrays.asList(YesNo.YES), true);
			FieldHelper.setVisibleWhen(contactDeadWildAnimals, Arrays.asList(ifYesSpecifyDeadWild), Arrays.asList(YesNo.YES), true);
			FieldHelper.setVisibleWhen(visitedPlace, Arrays.asList(placesVisited), Arrays.asList(YesNo.YES), true);

		}

		if(disease == Disease.FOODBORNE_ILLNESS){
			Label otherPersonsHeadingLabel =
					createLabel(I18nProperties.getString(Strings.headingOtherPersons), H3, OTHER_PERSONS_HEADING);

			Label numberOfPersonsAffected =
					createLabel(I18nProperties.getString(Strings.headingNumberOfPersonsAffected), H4, NUMBER_OF_PERSONS_NO_AFFECTED);
			setVisible(true, EpiDataDto.NAME_OF_AFFECTED_PERSON, EpiDataDto.TEL_NO,  EpiDataDto.AGE, EpiDataDto.DATE_TIME, EpiDataDto.DATE_TIME2, EpiDataDto.DATE_TIME3, EpiDataDto.DATE_TIME4);

			createLabel(I18nProperties.getString(Strings.headingExposureHistory), H3, EXPOSURE_HISTORY_HEADING);
			createLabel(I18nProperties.getString(Strings.headingObtainHistory), H3, OBTAIN_HISTORY_HEADING);
			createLabel(I18nProperties.getString(Strings.headingDay1), H3, DAY_1_HEADING);
			createLabel(I18nProperties.getString(Strings.headingDay2), H3, DAY_2_HEADING);
			createLabel(I18nProperties.getString(Strings.headingDay3), H3, DAY_3_HEADING);

			setVisible(false, EpiDataDto.HIGH_TRANSMISSION_RISK_AREA, EpiDataDto.LARGE_OUTBREAKS_AREA, EpiDataDto.AREA_INFECTED_ANIMALS, EpiDataDto.CONTACT_WITH_SOURCE_CASE_KNOWN, EpiDataDto.EXPOSURES, EpiDataDto.EXPOSURE_DETAILS_KNOWN, EpiDataDto.ACTIVITY_AS_CASE_DETAILS_KNOWN, EpiDataDto.RECENT_TRAVEL_OUTBREAK, EpiDataDto.CONTACT_SIMILAR_SYMPTOMS, EpiDataDto.CONTACT_SICK_ANIMALS, EpiDataDto.EXPOSURE_DETAILS_KNOWN, EpiDataDto.ACTIVITY_AS_CASE_DETAILS_KNOWN, EpiDataDto.CONTACT_WITH_SOURCE_CASE_KNOWN, EpiDataDto.ACTIVITIES_AS_CASE, EpiDataDto.ACTIVITY_AS_CASE_DETAILS_KNOWN);

			setVisible(true, EpiDataDto.INTL_TRAVEL, EpiDataDto.SPECIFY_COUNTRIES, EpiDataDto.DATE_OF_DEPARTURE, EpiDataDto.DATE_OF_ARRIVAL, EpiDataDto.DOMESTIC_TRAVEL, EpiDataDto.SPECIFY_LOCATION, EpiDataDto.DATE_OF_DEPARTURE2, EpiDataDto.DATE_OF_ARRIVAL2, EpiDataDto.CONTACT_ILL_PERSON, EpiDataDto.CONTACT_DATE, EpiDataDto.SPECIFY_ILLNESS, EpiDataDto.NAME_OF_AFFECTED_PERSON, EpiDataDto.NAME_OF_AFFECTED_PERSON2, EpiDataDto.NAME_OF_AFFECTED_PERSON3, EpiDataDto.NAME_OF_AFFECTED_PERSON4, EpiDataDto.TEL_NO, EpiDataDto.TEL_NO2, EpiDataDto.TEL_NO3, EpiDataDto.TEL_NO4, EpiDataDto.DATE_TIME, EpiDataDto.DATE_TIME2, EpiDataDto.DATE_TIME3, EpiDataDto.DATE_TIME4, EpiDataDto.AGE,  EpiDataDto.AGE2, EpiDataDto.AGE3, EpiDataDto.AGE4);

			setVisible(true, EpiDataDto.SUSPECTED_FOOD, EpiDataDto.DATE_CONSUMED,
					EpiDataDto.FOOD_SOURCE, EpiDataDto.EVENT_TYPE, EpiDataDto.EVENT_OTHER_SPECIFY,
					EpiDataDto.BREAKFAST, EpiDataDto.TOTAL_NO_PERSONS, EpiDataDto.FOOD_CONSUMED, EpiDataDto.SOURCE_OF_FOOD, EpiDataDto.CONSUMED_AT_PLACE,
					EpiDataDto.LUNCH, EpiDataDto.TOTAL_NO_PERSONS_L1, EpiDataDto.FOOD_CONSUMED_L1, EpiDataDto.SOURCE_OF_FOOD_L1, EpiDataDto.CONSUMED_AT_PLACE_L1,
					EpiDataDto.SUPPER, EpiDataDto.TOTAL_NO_PERSONS_S1, EpiDataDto.FOOD_CONSUMED_S1, EpiDataDto.SOURCE_OF_FOODS_S1, EpiDataDto.CONSUMED_AT_PLACE_S1,
					EpiDataDto.BREAKFAST2, EpiDataDto.TOTAL_NO_PERSONS2, EpiDataDto.FOOD_CONSUMED2, EpiDataDto.SOURCE_OF_FOOD2, EpiDataDto.CONSUMED_AT_PLACE2,
					EpiDataDto.LUNCH_L2, EpiDataDto.TOTAL_NO_PERSONS_L2, EpiDataDto.FOOD_CONSUMED_L2, EpiDataDto.SOURCE_OF_FOOD_L2, EpiDataDto.CONSUMED_AT_PLACE_L2,
					EpiDataDto.SUPPER_S2, EpiDataDto.TOTAL_NO_PERSONS_S2, EpiDataDto.FOOD_CONSUMED_S2, EpiDataDto.SOURCE_OF_FOOD_S2, EpiDataDto.CONSUMED_AT_PLACE_S2,
					EpiDataDto.BREAKFAST3, EpiDataDto.TOTAL_NO_PERSONS3, EpiDataDto.FOOD_CONSUMED3, EpiDataDto.SOURCE_OF_FOOD3, EpiDataDto.CONSUMED_AT_PLACE3,
					EpiDataDto.LUNCH_L3, EpiDataDto.TOTAL_NO_PERSONS_L3, EpiDataDto.FOOD_CONSUMED_L3, EpiDataDto.SOURCE_OF_FOOD_L3, EpiDataDto.CONSUMED_AT_PLACE_L3,
					EpiDataDto.SUPPER_S3, EpiDataDto.TOTAL_NO_PERSONS_S3, EpiDataDto.FOOD_CONSUMED_S3, EpiDataDto.SOURCE_OF_FOOD_S3, EpiDataDto.CONSUMED_AT_PLACE_S3
			);

			FieldHelper.setVisibleWhen(illPerson, Arrays.asList(contactDate), Arrays.asList(YesNo.YES), true);
		}

	}

	private void addActivityAsCaseFields() {

		if(!diseaseCSMCheck() && disease != Disease.FOODBORNE_ILLNESS){
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

		activityAsCaseField.addValueChangeListener(e -> {
			ogActivityAsCaseDetailsKnown.setEnabled(CollectionUtils.isEmpty(activityAsCaseField.getValue()));
		});
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
		return parentClass == CaseDataDto.class ? MAIN_HTML_LAYOUT + SOURCE_CONTACTS_HTML_LAYOUT : MAIN_HTML_LAYOUT;
	}

	private boolean diseaseCheck(){
		return disease == Disease.YELLOW_FEVER;
	}
	private boolean diseaseCSMCheck(){return disease == Disease.CSM; }
	private boolean diseaseInfluenzaCheck(){return disease == Disease.NEW_INFLUENZA || disease == Disease.SARI; }


	public void hideFieldsForSelectedDisease(Disease disease) {
		Set<String> disabledFields = EpiFormConfiguration.getDisabledFieldsForDisease(disease);
		for (String field : disabledFields) {
			disableField(field);
		}
	}

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
}
