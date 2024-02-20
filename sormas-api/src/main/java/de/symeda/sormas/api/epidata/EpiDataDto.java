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
package de.symeda.sormas.api.epidata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.ImportIgnore;
import de.symeda.sormas.api.activityascase.ActivityAsCaseDto;
import de.symeda.sormas.api.exposure.ExposureDto;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.utils.*;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableDto;

@DependingOnFeatureType(featureType = {
	FeatureType.CASE_SURVEILANCE,
	FeatureType.CONTACT_TRACING })
public class EpiDataDto extends PseudonymizableDto {

	private static final long serialVersionUID = 6292411396563549093L;

	public static final String I18N_PREFIX = "EpiData";

	public static final String EXPOSURE_DETAILS_KNOWN = "exposureDetailsKnown";
	public static final String ACTIVITY_AS_CASE_DETAILS_KNOWN = "activityAsCaseDetailsKnown";
	public static final String RECENT_TRAVEL_OUTBREAK = "recentTravelOutbreak";
	public static final String CONTACT_SIMILAR_SYMPTOMS = "contactSimilarOutbreak";
	public static final String CONTACT_SICK_ANIMALS = "contactSickAnimals";
	public static final String CONTACT_WITH_SOURCE_CASE_KNOWN = "contactWithSourceCaseKnown";
	public static final String EXPOSURES = "exposures";
	public static final String ACTIVITIES_AS_CASE = "activitiesAsCase";
	public static final String AREA_INFECTED_ANIMALS = "areaInfectedAnimals";
	public static final String HIGH_TRANSMISSION_RISK_AREA = "highTransmissionRiskArea";
	public static final String LARGE_OUTBREAKS_AREA = "largeOutbreaksArea";
	public static final String CHILD_COME_IN_CONTACT_WITH_SYMPTOMS = "childComeInContactWithSymptoms";

	public static final String PREVIOUSLY_VACCINATED_AGAINST_INFLUENZA = "previouslyVaccinatedAgainstInfluenza";
	public static final String YEAR_OF_VACCINATION = "yearOfVaccination";
	public static final String PLACES_VISITED_PAST_7DAYS = "placesVisitedPastSevenDays";
	public static final String VISITED_PLACES_CONFIRMED_PANDEMIC = "vistedPlacesConfirmedPandemic";
	public static final String RISK_FACTORS_SEVERE_DISEASE = "riskFactorsSevereDisease";
	public static final String OTHER_SPECIFY = "otherSpecify";

	@Enumerated(EnumType.STRING)
	private Disease disease;
	private YesNo exposureDetailsKnown;
	private YesNo activityAsCaseDetailsKnown;
	private YesNo recentTravelOutbreak;
	private YesNo contactSimilarOutbreak;
	private YesNo contactSickAnimals;
	private YesNo contactWithSourceCaseKnown;
	private YesNo highTransmissionRiskArea;
	private YesNo largeOutbreaksArea;
	@Diseases({
		Disease.AFP,
		Disease.GUINEA_WORM,
		Disease.NEW_INFLUENZA,
		Disease.ANTHRAX,
		Disease.POLIO,
		Disease.UNDEFINED,
		Disease.OTHER })
	private YesNo areaInfectedAnimals;

	private YesNo previouslyVaccinatedAgainstInfluenza;
	private Date yearOfVaccination;
	private String placesVisitedPastSevenDays;
	private YesNo vistedPlacesConfirmedPandemic;
	private RiskFactorInfluenza riskFactorsSevereDisease;
	private String otherSpecify;
	@Valid
	private List<ExposureDto> exposures = new ArrayList<>();

	@Valid
	private List<ActivityAsCaseDto> activitiesAsCase = new ArrayList<>();

	public YesNo getExposureDetailsKnown() {
		return exposureDetailsKnown;
	}

	private YesNoUnknown childComeInContactWithSymptoms;

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

	@ImportIgnore
	public List<ExposureDto> getExposures() {
		return exposures;
	}

	public void setExposures(List<ExposureDto> exposures) {
		this.exposures = exposures;
	}

	@ImportIgnore
	public List<ActivityAsCaseDto> getActivitiesAsCase() {
		return activitiesAsCase;
	}

	public void setActivitiesAsCase(List<ActivityAsCaseDto> activitiesAsCase) {
		this.activitiesAsCase = activitiesAsCase;
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

	public Disease getDisease() {
		return disease;
	}
	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	public YesNo getPreviouslyVaccinatedAgainstInfluenza() {
		return previouslyVaccinatedAgainstInfluenza;
	}

	public void setPreviouslyVaccinatedAgainstInfluenza(YesNo previouslyVaccinatedAgainstInfluenza) {
		this.previouslyVaccinatedAgainstInfluenza = previouslyVaccinatedAgainstInfluenza;
	}

	public Date getYearOfVaccination() {
		return yearOfVaccination;
	}

	public void setYearOfVaccination(Date yearOfVaccination) {
		this.yearOfVaccination = yearOfVaccination;
	}

	public String getPlacesVisitedPastSevenDays() {
		return placesVisitedPastSevenDays;
	}

	public void setPlacesVisitedPastSevenDays(String placesVisitedPastSevenDays) {
		this.placesVisitedPastSevenDays = placesVisitedPastSevenDays;
	}

	public YesNo getVistedPlacesConfirmedPandemic() {
		return vistedPlacesConfirmedPandemic;
	}

	public void setVistedPlacesConfirmedPandemic(YesNo vistedPlacesConfirmedPandemic) {
		this.vistedPlacesConfirmedPandemic = vistedPlacesConfirmedPandemic;
	}

	public RiskFactorInfluenza getRiskFactorsSevereDisease() {
		return riskFactorsSevereDisease;
	}

	public void setRiskFactorsSevereDisease(RiskFactorInfluenza riskFactorsSevereDisease) {
		this.riskFactorsSevereDisease = riskFactorsSevereDisease;
	}

	public String getOtherSpecify() {
		return otherSpecify;
	}

	public void setOtherSpecify(String otherSpecify) {
		this.otherSpecify = otherSpecify;
	}

	public static EpiDataDto build() {

		EpiDataDto epiData = new EpiDataDto();
		epiData.setUuid(DataHelper.createUuid());
		return epiData;
	}

	@Override
	public EpiDataDto clone() throws CloneNotSupportedException {
		EpiDataDto clone = (EpiDataDto) super.clone();
		List<ActivityAsCaseDto> activityAsCaseDtos = new ArrayList<>();
		for (ActivityAsCaseDto activityAsCase : getActivitiesAsCase()) {
			activityAsCaseDtos.add(activityAsCase.clone());
		}
		clone.getActivitiesAsCase().clear();
		clone.getActivitiesAsCase().addAll(activityAsCaseDtos);

		List<ExposureDto> exposureDtos = new ArrayList<>();
		for (ExposureDto exposure : getExposures()) {
			exposureDtos.add(exposure.clone());
		}
		clone.getExposures().clear();
		clone.getExposures().addAll(exposureDtos);

		return clone;
	}

	public YesNoUnknown getChildComeInContactWithSymptoms() {
		return childComeInContactWithSymptoms;
	}

	public void setChildComeInContactWithSymptoms(YesNoUnknown childComeInContactWithSymptoms) {
		this.childComeInContactWithSymptoms = childComeInContactWithSymptoms;
	}
}
