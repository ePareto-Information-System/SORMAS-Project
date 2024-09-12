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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.j256.ormlite.table.DatabaseTable;

import de.symeda.sormas.api.epidata.PlaceManaged;
import de.symeda.sormas.api.utils.YesNo;
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

	public YesNo getExposureDetailsKnown() {
		return exposureDetailsKnown;
	}

	public void setExposureDetailsKnown(YesNo exposureDetailsKnown) {
		this.exposureDetailsKnown = exposureDetailsKnown;
	}

	public YesNo getActivityAsCaseDetailsKnown() {
		return activityAsCaseDetailsKnown;
	}

	public void setActivityAsCaseDetailsKnown(YesNo activityAsCaseDetailsKnown) {
		this.activityAsCaseDetailsKnown = activityAsCaseDetailsKnown;
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

	@Override
	public String getI18nPrefix() {
		return I18N_PREFIX;
	}
}
