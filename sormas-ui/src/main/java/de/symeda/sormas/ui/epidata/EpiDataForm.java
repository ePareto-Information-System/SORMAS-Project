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

import static de.symeda.sormas.ui.utils.CssStyles.VSPACE_3;
import static de.symeda.sormas.ui.utils.CssStyles.VSPACE_TOP_3;
import static de.symeda.sormas.ui.utils.LayoutUtil.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextField;
import de.symeda.sormas.api.epidata.ContactSetting;
import de.symeda.sormas.api.utils.RiskFactorInfluenza;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.ui.utils.CssStyles;
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
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.FieldHelper;
import de.symeda.sormas.ui.utils.NullableOptionGroup;
import de.symeda.sormas.ui.utils.components.MultilineLabel;

public class EpiDataForm extends AbstractEditForm<EpiDataDto> {

	private static final long serialVersionUID = 1L;

	private static final String LOC_EXPOSURE_INVESTIGATION_HEADING = "locExposureInvestigationHeading";
	private static final String LOC_EXPOSURE_TRAVEL_HISTORY_HEADING = "locExposureTravelHistoryHeading";
	private static final String LOC_ACTIVITY_AS_CASE_INVESTIGATION_HEADING = "locActivityAsCaseInvestigationHeading";
	private static final String LOC_SOURCE_CASE_CONTACTS_HEADING = "locSourceCaseContactsHeading";
	private static final String LOC_EPI_DATA_FIELDS_HINT = "locEpiDataFieldsHint";

	//@formatter:off
	private static final String MAIN_HTML_LAYOUT = 
			loc(LOC_EXPOSURE_TRAVEL_HISTORY_HEADING) +

					loc(EpiDataDto.PREVIOUSLY_VACCINATED_AGAINST_INFLUENZA)+
					fluidRowLocs(6, EpiDataDto.YEAR_OF_VACCINATION)+
					loc(EpiDataDto.PLACES_VISITED_PAST_7DAYS)+
					loc(EpiDataDto.VISITED_PLACES_CONFIRMED_PANDEMIC)+
					fluidRowLocs(EpiDataDto.RISK_FACTORS_SEVERE_DISEASE, EpiDataDto.OTHER_SPECIFY)+
					loc(EpiDataDto.PATIENT_TRAVELLED_TWO_WEEKS_PRIOR)+
					locCss(VSPACE_TOP_3, "") +
					fluidRowLocs(EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_ONE, EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_TWO) +
					fluidRowLocs(EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_THREE, EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_FOUR) +
					locCss(VSPACE_TOP_3, "") +
					fluidRowLocs(EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_ONE, EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_TWO) +
					fluidRowLocs(EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_THREE, EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_FOUR) +
					fluidRowLocs(EpiDataDto.EXPOSED_TO_RISK_FACTOR, EpiDataDto.WATER_USED_BY_PATIENT_AFTER_EXPOSURE)+
					loc(EpiDataDto.PATIENT_VISITED_HEALTH_CARE_FACILITY)+
					loc(EpiDataDto.PATIENT_CLOSE_CONTACT_WITH_ARI)+
					loc(EpiDataDto.PATIENT_CLOSE_CONTACT_WITH_ARI_CONTACT_SETTINGS)+
					loc(EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE)+
					loc(EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE_EXPOSURE_LOCATIONS)+
					loc(EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE_EXPOSURE_LOCATION_CITY_COUNTRY)+
					loc(EpiDataDto.RECENT_TRAVEL_OUTBREAK)+
					loc(EpiDataDto.CONTACT_SIMILAR_SYMPTOMS)+
					loc(EpiDataDto.CONTACT_SICK_ANIMALS)+
			loc(LOC_EXPOSURE_INVESTIGATION_HEADING) +
			loc(EpiDataDto.EXPOSURE_DETAILS_KNOWN) +
			loc(EpiDataDto.EXPOSURES) +
			loc(LOC_ACTIVITY_AS_CASE_INVESTIGATION_HEADING) +
			loc(EpiDataDto.ACTIVITY_AS_CASE_DETAILS_KNOWN)+

			loc(EpiDataDto.ACTIVITIES_AS_CASE) +
			locCss(VSPACE_TOP_3, LOC_EPI_DATA_FIELDS_HINT) +
			loc(EpiDataDto.HIGH_TRANSMISSION_RISK_AREA) +
			loc(EpiDataDto.LARGE_OUTBREAKS_AREA) +
			loc(EpiDataDto.AREA_INFECTED_ANIMALS);
	
	private static final String SOURCE_CONTACTS_HTML_LAYOUT =
			locCss(VSPACE_TOP_3, LOC_SOURCE_CASE_CONTACTS_HEADING) +
			loc(EpiDataDto.CONTACT_WITH_SOURCE_CASE_KNOWN);
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
			isEditAllowed);
		this.disease = disease;
		this.parentClass = parentClass;
		this.sourceContactsToggleCallback = sourceContactsToggleCallback;
		this.isPseudonymized = isPseudonymized;
		addFields();
	}

	@Override
	protected void addFields() {

		if (disease != null && !diseaseCheck() && !diseaseInfluenzaCheck()) {
			addHeadingsAndInfoTexts();
		}

		NullableOptionGroup recentTravelOutbreak = addField(EpiDataDto.RECENT_TRAVEL_OUTBREAK, NullableOptionGroup.class);
		recentTravelOutbreak.setCaption("Did the patient travel to a place with a recent history of outbreak?");
		NullableOptionGroup contactSimilarOutbreak = addField(EpiDataDto.CONTACT_SIMILAR_SYMPTOMS, NullableOptionGroup.class);
		contactSimilarOutbreak.setCaption("Did the patient come in contact with someone with similar symptoms");
		NullableOptionGroup contactSickAnimals = addField(EpiDataDto.CONTACT_SICK_ANIMALS, NullableOptionGroup.class);
		contactSickAnimals.setCaption("Did the patient come in contact with sick animal(s)?");

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

		FieldHelper.setVisibleWhen(
			getFieldGroup(),
			EpiDataDto.EXPOSURES,
			EpiDataDto.EXPOSURE_DETAILS_KNOWN,
			Collections.singletonList(YesNo.YES),
			true);

		initializeVisibilitiesAndAllowedVisibilities();
		initializeAccessAndAllowedAccesses();

		exposuresField.addValueChangeListener(e -> {
			ogExposureDetailsKnown.setEnabled(CollectionUtils.isEmpty(exposuresField.getValue()));
		});

		if (diseaseCheck()) {
			setVisible(false, EpiDataDto.EXPOSURES, EpiDataDto.EXPOSURE_DETAILS_KNOWN, EpiDataDto.CONTACT_WITH_SOURCE_CASE_KNOWN);
		}

		if (diseaseCSMCheck()) {
			recentTravelOutbreak.setVisible(false);
			contactSickAnimals.setVisible(false);

			setVisible(false, EpiDataDto.HIGH_TRANSMISSION_RISK_AREA, EpiDataDto.LARGE_OUTBREAKS_AREA, EpiDataDto.ACTIVITY_AS_CASE_DETAILS_KNOWN);

		}

		if(diseaseInfluenzaCheck()){
			setVisible(false, EpiDataDto.HIGH_TRANSMISSION_RISK_AREA, EpiDataDto.LARGE_OUTBREAKS_AREA, EpiDataDto.AREA_INFECTED_ANIMALS);
			setVisible(false, EpiDataDto.CONTACT_WITH_SOURCE_CASE_KNOWN, EpiDataDto.EXPOSURES, EpiDataDto.EXPOSURE_DETAILS_KNOWN, EpiDataDto.ACTIVITY_AS_CASE_DETAILS_KNOWN);

			NullableOptionGroup previously = addField(EpiDataDto.PREVIOUSLY_VACCINATED_AGAINST_INFLUENZA, NullableOptionGroup.class);
			DateField year = addField(EpiDataDto.YEAR_OF_VACCINATION, DateField.class);
			year.setVisible(false);

			addField(EpiDataDto.PLACES_VISITED_PAST_7DAYS, com.vaadin.v7.ui.TextArea.class);
			addField(EpiDataDto.VISITED_PLACES_CONFIRMED_PANDEMIC, OptionGroup.class);
			ComboBox riskFactor = addField(EpiDataDto.RISK_FACTORS_SEVERE_DISEASE, ComboBox.class);
			TextField other = addField(EpiDataDto.OTHER_SPECIFY, com.vaadin.v7.ui.TextField.class);
			other.setVisible(false);

			FieldHelper
					.setVisibleWhen(riskFactor, Arrays.asList(other), Arrays.asList(RiskFactorInfluenza.OTHER), true);

			FieldHelper
					.setVisibleWhen(previously, Arrays.asList(year), Arrays.asList(YesNo.YES), true);

		}

		if (disease == Disease.CORONAVIRUS) {
			hideAllFields();
			setVisible(true, EpiDataDto.PATIENT_TRAVELLED_TWO_WEEKS_PRIOR);
			setVisible(true, EpiDataDto.PATIENT_VISITED_HEALTH_CARE_FACILITY);
			setVisible(true, EpiDataDto.PATIENT_CLOSE_CONTACT_WITH_ARI);
			setVisible(true, EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE);



			//check if patient travelled two weeks prior and set the visibility of the fields
			patientTravelledTwoWeeksPriorYesNoUknownField.addValueChangeListener(e -> {
				YesNoUnknown patientTravelledTwoWeeksPrior = (YesNoUnknown) FieldHelper.getNullableSourceFieldValue((Field) e.getProperty());
				if (patientTravelledTwoWeeksPrior == YesNoUnknown.YES) {
					setVisible(true, EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_ONE, EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_TWO, EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_THREE, EpiDataDto.PATIENT_TRAVELLED_IN_COUNTRY_FOUR);
					setVisible(true, EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_ONE, EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_TWO, EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_THREE, EpiDataDto.PATIENT_TRAVELLED_INTERNATIONAL_FOUR);
				}
			});

			patientCloseContactWithARIYesNoUnkownField.addValueChangeListener(e -> {
				YesNoUnknown patientCloseContactWithARI = (YesNoUnknown) FieldHelper.getNullableSourceFieldValue((Field) e.getProperty());
				if (patientCloseContactWithARI == YesNoUnknown.YES) {
					setVisible(true, EpiDataDto.PATIENT_CLOSE_CONTACT_WITH_ARI_CONTACT_SETTINGS);
				}
			});

			patientContactWithConfirmedCaseYesNoField.addValueChangeListener(e -> {
				YesNoUnknown patientContactWithConfirmedCase = (YesNoUnknown) FieldHelper.getNullableSourceFieldValue((Field) e.getProperty());
				if (patientContactWithConfirmedCase == YesNoUnknown.YES) {
					setVisible(true, EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE_EXPOSURE_LOCATIONS, EpiDataDto.PATIENT_CONTACT_WITH_CONFIRMED_CASE_EXPOSURE_LOCATION_CITY_COUNTRY);
				}
			});

		}

		if (disease == Disease.CHOLERA) {
			hideAllFields();
			hideLabels();
			setVisible(true, EpiDataDto.EXPOSED_TO_RISK_FACTOR, EpiDataDto.WATER_USED_BY_PATIENT_AFTER_EXPOSURE);
		}

	}

	private void addActivityAsCaseFields() {

		if(!diseaseCSMCheck()){
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
	private boolean diseaseInfluenzaCheck(){return disease == Disease.NEW_INFLUENZA; }

	//hide all fields
	public void hideAllFields () {
		for (Field<?> field : getFieldGroup().getFields()) {
			//check if field is not null
			if (field != null)
				//hide the field
				field.setVisible(false);

		}
	}

	public void hideLabels () {
		Arrays.asList(LOC_EXPOSURE_INVESTIGATION_HEADING, LOC_ACTIVITY_AS_CASE_INVESTIGATION_HEADING, LOC_SOURCE_CASE_CONTACTS_HEADING).stream().map(
				//check if not null then hide
				label -> getContent().getComponent(label)).filter(
				label -> label != null).forEach(
				label -> label.setVisible(false));
	}
}
