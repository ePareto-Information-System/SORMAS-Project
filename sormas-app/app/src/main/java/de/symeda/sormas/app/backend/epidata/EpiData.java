/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.app.backend.epidata;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import com.j256.ormlite.table.DatabaseTable;

import org.apache.commons.lang3.StringUtils;

import de.symeda.sormas.api.epidata.ContactSetting;
import de.symeda.sormas.api.epidata.PlaceManaged;
import de.symeda.sormas.api.sample.PathogenTestType;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.app.backend.activityascase.ActivityAsCase;
import de.symeda.sormas.app.backend.common.EmbeddedAdo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.containmentmeasure.ContainmentMeasure;
import de.symeda.sormas.app.backend.contaminationsource.ContaminationSource;
import de.symeda.sormas.app.backend.exposure.Exposure;
import de.symeda.sormas.app.backend.persontravelhistory.PersonTravelHistory;

@Entity(name = EpiData.TABLE_NAME)
@DatabaseTable(tableName = EpiData.TABLE_NAME)
@EmbeddedAdo
public class EpiData extends PseudonymizableAdo {

	private static final long serialVersionUID = -8294812479501735785L;

	public static final String TABLE_NAME = "epidata";
	public static final String I18N_PREFIX = "EpiData";

	@Enumerated(EnumType.STRING)
	private YesNo exposureDetailsKnown;
	@Enumerated(EnumType.STRING)
	private YesNo activityAsCaseDetailsKnown;
	@Enumerated(EnumType.STRING)
	private YesNo recentTravelOutbreak;
	@Enumerated(EnumType.STRING)
	private YesNo contactSimilarOutbreak;
	@Enumerated(EnumType.STRING)
	private YesNo contactSickAnimals;
	@Enumerated(EnumType.STRING)
	private YesNo contactWithSourceCaseKnown;
	@Enumerated(EnumType.STRING)
	private YesNo highTransmissionRiskArea;
	@Enumerated(EnumType.STRING)
	private YesNo largeOutbreaksArea;
	@Enumerated(EnumType.STRING)
	private YesNo areaInfectedAnimals;

	private List<Exposure> exposures = new ArrayList<>();

	private List<ActivityAsCase> activitiesAsCase = new ArrayList<>();
	private List<PersonTravelHistory> personTravelHistories = new ArrayList<>();
	private List<ContainmentMeasure> containmentMeasures = new ArrayList<>();
	private List<ContaminationSource> contaminationSources = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	private YesNo receivedHealthEducation;

	@Enumerated(EnumType.STRING)
	private YesNo patientEnteredWaterSource;

	@Enumerated(EnumType.STRING)
	private PlaceManaged placeManaged;

	@Enumerated(EnumType.STRING)
	private YesNoUnknown patientTravelledTwoWeeksPrior;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientTravelledInCountryOne;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientTravelledInCountryTwo;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientTravelledInCountryThree;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientTravelledInCountryFour;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientTravelledInternationalOne;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientTravelledInternationalTwo;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientTravelledInternationalThree;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientTravelledInternationalFour;

	@Enumerated(EnumType.STRING)
	private YesNoUnknown patientVisitedHealthCareFacility;

	@Enumerated(EnumType.STRING)
	private YesNoUnknown patientCloseContactWithARI;

	@Enumerated(EnumType.STRING)
	private YesNoUnknown patientContactWithConfirmedCase;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientContactWithConfirmedCaseExposureLocationCityCountry;
	public YesNo getExposureDetailsKnown() {
		return exposureDetailsKnown;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientCloseContactWithARIContactSettingsString;

	@Transient
	private Set<ContactSetting> patientCloseContactWithARIContactSettings;

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String patientContactWithConfirmedCaseExposureLocationsString;

	@Transient
	private Set<ContactSetting> patientContactWithConfirmedCaseExposureLocations;




	public void setExposureDetailsKnown(YesNo exposureDetailsKnown) {
		this.exposureDetailsKnown = exposureDetailsKnown;
	}

	public YesNo getActivityAsCaseDetailsKnown() {
		return activityAsCaseDetailsKnown;
	}

	public void setActivityAsCaseDetailsKnown(YesNo activityAsCaseDetailsKnown) {
		this.activityAsCaseDetailsKnown = activityAsCaseDetailsKnown;
	}

	public YesNo getRecentTravelOutbreak() {
		return recentTravelOutbreak;
	}

	public void setRecentTravelOutbreak(YesNo recentTravelOutbreak) {
		this.recentTravelOutbreak = recentTravelOutbreak;
	}

	public YesNo getContactSimilarOutbreak() {
		return contactSimilarOutbreak;
	}

	public void setContactSimilarOutbreak(YesNo contactSimilarOutbreak) {
		this.contactSimilarOutbreak = contactSimilarOutbreak;
	}

	public YesNo getContactSickAnimals() {
		return contactSickAnimals;
	}

	public void setContactSickAnimals(YesNo contactSickAnimals) {
		this.contactSickAnimals = contactSickAnimals;
	}

	public YesNo getContactWithSourceCaseKnown() {
		return contactWithSourceCaseKnown;
	}

	public void setContactWithSourceCaseKnown(YesNo contactWithSourceCaseKnown) {
		this.contactWithSourceCaseKnown = contactWithSourceCaseKnown;
	}

	public YesNo getHighTransmissionRiskArea() {
		return highTransmissionRiskArea;
	}

	public void setHighTransmissionRiskArea(YesNo highTransmissionRiskArea) {
		this.highTransmissionRiskArea = highTransmissionRiskArea;
	}

	public YesNo getLargeOutbreaksArea() {
		return largeOutbreaksArea;
	}

	public void setLargeOutbreaksArea(YesNo largeOutbreaksArea) {
		this.largeOutbreaksArea = largeOutbreaksArea;
	}

	public YesNo getAreaInfectedAnimals() {
		return areaInfectedAnimals;
	}

	public void setAreaInfectedAnimals(YesNo areaInfectedAnimals) {
		this.areaInfectedAnimals = areaInfectedAnimals;
	}

	public List<Exposure> getExposures() {
		return exposures;
	}

	public void setExposures(List<Exposure> exposures) {
		this.exposures = exposures;
	}

	public List<ActivityAsCase> getActivitiesAsCase() {
		return activitiesAsCase;
	}

	public void setActivitiesAsCase(List<ActivityAsCase> activitiesAsCase) {
		this.activitiesAsCase = activitiesAsCase;
	}

	public List<PersonTravelHistory> getPersonTravelHistories() {
		return personTravelHistories;
	}

	public void setPersonTravelHistories(List<PersonTravelHistory> personTravelHistories) {
		this.personTravelHistories = personTravelHistories;
	}

	public List<ContainmentMeasure> getContainmentMeasures() {
		return containmentMeasures;
	}

	public void setContainmentMeasures(List<ContainmentMeasure> containmentMeasures) {
		this.containmentMeasures = containmentMeasures;
	}

	public List<ContaminationSource> getContaminationSources() {
		return contaminationSources;
	}

	public void setContaminationSources(List<ContaminationSource> contaminationSources) {
		this.contaminationSources = contaminationSources;
	}

	public YesNo getReceivedHealthEducation() {
		return receivedHealthEducation;
	}

	public void setReceivedHealthEducation(YesNo receivedHealthEducation) {
		this.receivedHealthEducation = receivedHealthEducation;
	}

	public YesNo getPatientEnteredWaterSource() {
		return patientEnteredWaterSource;
	}

	public void setPatientEnteredWaterSource(YesNo patientEnteredWaterSource) {
		this.patientEnteredWaterSource = patientEnteredWaterSource;
	}

	public PlaceManaged getPlaceManaged() {
		return placeManaged;
	}

	public void setPlaceManaged(PlaceManaged placeManaged) {
		this.placeManaged = placeManaged;
	}

	public YesNoUnknown getPatientTravelledTwoWeeksPrior() {
		return patientTravelledTwoWeeksPrior;
	}

	public void setPatientTravelledTwoWeeksPrior(YesNoUnknown patientTravelledTwoWeeksPrior) {
		this.patientTravelledTwoWeeksPrior = patientTravelledTwoWeeksPrior;
	}

	public String getPatientTravelledInCountryOne() {
		return patientTravelledInCountryOne;
	}

	public void setPatientTravelledInCountryOne(String patientTravelledInCountryOne) {
		this.patientTravelledInCountryOne = patientTravelledInCountryOne;
	}

	public String getPatientTravelledInCountryTwo() {
		return patientTravelledInCountryTwo;
	}

	public void setPatientTravelledInCountryTwo(String patientTravelledInCountryTwo) {
		this.patientTravelledInCountryTwo = patientTravelledInCountryTwo;
	}

	public String getPatientTravelledInCountryThree() {
		return patientTravelledInCountryThree;
	}

	public void setPatientTravelledInCountryThree(String patientTravelledInCountryThree) {
		this.patientTravelledInCountryThree = patientTravelledInCountryThree;
	}

	public String getPatientTravelledInCountryFour() {
		return patientTravelledInCountryFour;
	}

	public void setPatientTravelledInCountryFour(String patientTravelledInCountryFour) {
		this.patientTravelledInCountryFour = patientTravelledInCountryFour;
	}

	public String getPatientTravelledInternationalOne() {
		return patientTravelledInternationalOne;
	}

	public void setPatientTravelledInternationalOne(String patientTravelledInternationalOne) {
		this.patientTravelledInternationalOne = patientTravelledInternationalOne;
	}

	public String getPatientTravelledInternationalTwo() {
		return patientTravelledInternationalTwo;
	}

	public void setPatientTravelledInternationalTwo(String patientTravelledInternationalTwo) {
		this.patientTravelledInternationalTwo = patientTravelledInternationalTwo;
	}

	public String getPatientTravelledInternationalThree() {
		return patientTravelledInternationalThree;
	}

	public void setPatientTravelledInternationalThree(String patientTravelledInternationalThree) {
		this.patientTravelledInternationalThree = patientTravelledInternationalThree;
	}

	public String getPatientTravelledInternationalFour() {
		return patientTravelledInternationalFour;
	}

	public void setPatientTravelledInternationalFour(String patientTravelledInternationalFour) {
		this.patientTravelledInternationalFour = patientTravelledInternationalFour;
	}

	public YesNoUnknown getPatientVisitedHealthCareFacility() {
		return patientVisitedHealthCareFacility;
	}

	public void setPatientVisitedHealthCareFacility(YesNoUnknown patientVisitedHealthCareFacility) {
		this.patientVisitedHealthCareFacility = patientVisitedHealthCareFacility;
	}

	public YesNoUnknown getPatientCloseContactWithARI() {
		return patientCloseContactWithARI;
	}

	public void setPatientCloseContactWithARI(YesNoUnknown patientCloseContactWithARI) {
		this.patientCloseContactWithARI = patientCloseContactWithARI;
	}

	public YesNoUnknown getPatientContactWithConfirmedCase() {
		return patientContactWithConfirmedCase;
	}

	public void setPatientContactWithConfirmedCase(YesNoUnknown patientContactWithConfirmedCase) {
		this.patientContactWithConfirmedCase = patientContactWithConfirmedCase;
	}

	public String getPatientContactWithConfirmedCaseExposureLocationCityCountry() {
		return patientContactWithConfirmedCaseExposureLocationCityCountry;
	}

	public void setPatientContactWithConfirmedCaseExposureLocationCityCountry(String patientContactWithConfirmedCaseExposureLocationCityCountry) {
		this.patientContactWithConfirmedCaseExposureLocationCityCountry = patientContactWithConfirmedCaseExposureLocationCityCountry;
	}

	public String getPatientCloseContactWithARIContactSettingsString() {
		return patientCloseContactWithARIContactSettingsString;
	}

	public void setPatientCloseContactWithARIContactSettingsString(String patientCloseContactWithARIContactSettingsString) {
		this.patientCloseContactWithARIContactSettingsString = patientCloseContactWithARIContactSettingsString;
		patientCloseContactWithARIContactSettings = null;
	}

	public String getPatientContactWithConfirmedCaseExposureLocationsString() {
		return patientContactWithConfirmedCaseExposureLocationsString;
	}

	public void setPatientContactWithConfirmedCaseExposureLocationsString(String patientContactWithConfirmedCaseExposureLocationsString) {
		this.patientContactWithConfirmedCaseExposureLocationsString = patientContactWithConfirmedCaseExposureLocationsString;
		patientContactWithConfirmedCaseExposureLocations = null;
	}

	public void setPatientCloseContactWithARIContactSettings(Set<ContactSetting> patientCloseContactWithARIContactSettings) {
		this.patientCloseContactWithARIContactSettings = patientCloseContactWithARIContactSettings;

		if (this.patientCloseContactWithARIContactSettings == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		for (ContactSetting contactSetting : patientCloseContactWithARIContactSettings) {
			sb.append(contactSetting.name());
			sb.append(",");
		}
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		patientCloseContactWithARIContactSettingsString = sb.toString();
	}

	@Transient
	public Set<ContactSetting> getPatientCloseContactWithARIContactSettings() {
		if (patientCloseContactWithARIContactSettings == null) {
			patientCloseContactWithARIContactSettings = new HashSet<>();
			if (!StringUtils.isEmpty(patientCloseContactWithARIContactSettingsString)) {
				String[] contactSettingsTypes = patientCloseContactWithARIContactSettingsString.split(",");
				for (String contactSettingsType : contactSettingsTypes) {
					patientCloseContactWithARIContactSettings.add(ContactSetting.valueOf(contactSettingsType));
				}
			}
		}
		return patientCloseContactWithARIContactSettings;
	}

	public void setPatientContactWithConfirmedCaseExposureLocations(Set<ContactSetting> patientContactWithConfirmedCaseExposureLocations) {
		this.patientContactWithConfirmedCaseExposureLocations = patientContactWithConfirmedCaseExposureLocations;

		if (this.patientContactWithConfirmedCaseExposureLocations == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		for (ContactSetting contactSetting : patientContactWithConfirmedCaseExposureLocations) {
			sb.append(contactSetting.name());
			sb.append(",");
		}
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		patientContactWithConfirmedCaseExposureLocationsString = sb.toString();
	}

	@Transient
	public Set<ContactSetting> getPatientContactWithConfirmedCaseExposureLocations() {
		if (patientContactWithConfirmedCaseExposureLocations == null) {
			patientContactWithConfirmedCaseExposureLocations = new HashSet<>();
			if (!StringUtils.isEmpty(patientContactWithConfirmedCaseExposureLocationsString)) {
				String[] contactSettingsTypes = patientContactWithConfirmedCaseExposureLocationsString.split(",");
				for (String contactSettingsType : contactSettingsTypes) {
					patientContactWithConfirmedCaseExposureLocations.add(ContactSetting.valueOf(contactSettingsType));
				}
			}
		}
		return patientContactWithConfirmedCaseExposureLocations;
	}



	@Override
	public String getI18nPrefix() {
		return I18N_PREFIX;
	}



}
