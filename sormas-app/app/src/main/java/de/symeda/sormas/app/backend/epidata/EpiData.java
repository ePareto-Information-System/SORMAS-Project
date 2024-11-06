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
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.symeda.sormas.api.caze.CaseOutcome;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.app.backend.activityascase.ActivityAsCase;
import de.symeda.sormas.app.backend.common.EmbeddedAdo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.exposure.Exposure;

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
	private YesNo contactDeadAnimals;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String ifYesSpecifyDead;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String ifYesWildAnimalLocation;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date ifYesWildAnimalDate;
	@Enumerated(EnumType.STRING)
	private YesNo contactWithSourceCaseKnown;
	@Enumerated(EnumType.STRING)
	private YesNo highTransmissionRiskArea;
	@Enumerated(EnumType.STRING)
	private YesNo largeOutbreaksArea;
	@Enumerated(EnumType.STRING)
	private YesNo areaInfectedAnimals;

	private YesNo patientTravelDuringIllness;
	private String comm1;
	private String healthCenter1;
	private String country1;
	private String comm2;
	private String healthCenter2;
	private String country2;
	private YesNo wasPatientHospitalized;
	private String ifYesWhere;
	private Date hospitalizedDate1;
	private Date hospitalizedDate2;
	private YesNo didPatientConsultHealer;
	private String ifYesNameHealer;
	private String community;
	private String country;
	private String whenWhereContactTakePlace;
	private Date dateOfContact;
	private YesNo patientReceiveTraditionalMedicine;
	private String ifYesExplain;
	private YesNo patientAttendFuneralCeremonies;
	private YesNo patientTravelAnytimePeriodBeforeIll;
	private String ifTravelYesWhere;
	private Date ifYesStartDate;
	private Date ifYesEndDate;
	private YesNo patientContactKnownSuspect;
	private String suspectName;
	private String suspectLastName;
	private String idCase;
	private CaseOutcome duringContactSuspectCase;
	private Date dateOfDeath;
	private Date dateOfLastContactWithSuspectCase;

	private List<Exposure> exposures = new ArrayList<>();

	private List<ActivityAsCase> activitiesAsCase = new ArrayList<>();

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

	public YesNo getContactDeadAnimals() {
		return contactDeadAnimals;
	}

	public void setContactDeadAnimals(YesNo contactDeadAnimals) {
		this.contactDeadAnimals = contactDeadAnimals;
	}

	public String getIfYesSpecifyDead() {
		return ifYesSpecifyDead;
	}

	public void setIfYesSpecifyDead(String ifYesSpecifyDead) {
		this.ifYesSpecifyDead = ifYesSpecifyDead;
	}

	public String getIfYesWildAnimalLocation() {
		return ifYesWildAnimalLocation;
	}

	public void setIfYesWildAnimalLocation(String ifYesWildAnimalLocation) {
		this.ifYesWildAnimalLocation = ifYesWildAnimalLocation;
	}

	public Date getIfYesWildAnimalDate() {
		return ifYesWildAnimalDate;
	}

	public void setIfYesWildAnimalDate(Date ifYesWildAnimalDate) {
		this.ifYesWildAnimalDate = ifYesWildAnimalDate;
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

	public YesNo getPatientTravelDuringIllness() {
		return patientTravelDuringIllness;
	}

	public void setPatientTravelDuringIllness(YesNo patientTravelDuringIllness) {
		this.patientTravelDuringIllness = patientTravelDuringIllness;
	}

	public String getComm1() {
		return comm1;
	}

	public void setComm1(String comm1) {
		this.comm1 = comm1;
	}

	public String getHealthCenter1() {
		return healthCenter1;
	}

	public void setHealthCenter1(String healthCenter1) {
		this.healthCenter1 = healthCenter1;
	}

	public String getCountry1() {
		return country1;
	}

	public void setCountry1(String country1) {
		this.country1 = country1;
	}

	public String getComm2() {
		return comm2;
	}

	public void setComm2(String comm2) {
		this.comm2 = comm2;
	}

	public String getHealthCenter2() {
		return healthCenter2;
	}

	public void setHealthCenter2(String healthCenter2) {
		this.healthCenter2 = healthCenter2;
	}

	public String getCountry2() {
		return country2;
	}

	public void setCountry2(String country2) {
		this.country2 = country2;
	}

	public YesNo getWasPatientHospitalized() {
		return wasPatientHospitalized;
	}

	public void setWasPatientHospitalized(YesNo wasPatientHospitalized) {
		this.wasPatientHospitalized = wasPatientHospitalized;
	}

	public String getIfYesWhere() {
		return ifYesWhere;
	}

	public void setIfYesWhere(String ifYesWhere) {
		this.ifYesWhere = ifYesWhere;
	}

	public Date getHospitalizedDate1() {
		return hospitalizedDate1;
	}

	public void setHospitalizedDate1(Date hospitalizedDate1) {
		this.hospitalizedDate1 = hospitalizedDate1;
	}

	public Date getHospitalizedDate2() {
		return hospitalizedDate2;
	}

	public void setHospitalizedDate2(Date hospitalizedDate2) {
		this.hospitalizedDate2 = hospitalizedDate2;
	}

	public YesNo getDidPatientConsultHealer() {
		return didPatientConsultHealer;
	}

	public void setDidPatientConsultHealer(YesNo didPatientConsultHealer) {
		this.didPatientConsultHealer = didPatientConsultHealer;
	}

	public String getIfYesNameHealer() {
		return ifYesNameHealer;
	}

	public void setIfYesNameHealer(String ifYesNameHealer) {
		this.ifYesNameHealer = ifYesNameHealer;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getWhenWhereContactTakePlace() {
		return whenWhereContactTakePlace;
	}

	public void setWhenWhereContactTakePlace(String whenWhereContactTakePlace) {
		this.whenWhereContactTakePlace = whenWhereContactTakePlace;
	}

	public Date getDateOfContact() {
		return dateOfContact;
	}

	public void setDateOfContact(Date dateOfContact) {
		this.dateOfContact = dateOfContact;
	}

	public YesNo getPatientReceiveTraditionalMedicine() {
		return patientReceiveTraditionalMedicine;
	}

	public void setPatientReceiveTraditionalMedicine(YesNo patientReceiveTraditionalMedicine) {
		this.patientReceiveTraditionalMedicine = patientReceiveTraditionalMedicine;
	}

	public String getIfYesExplain() {
		return ifYesExplain;
	}

	public void setIfYesExplain(String ifYesExplain) {
		this.ifYesExplain = ifYesExplain;
	}

	public YesNo getPatientAttendFuneralCeremonies() {
		return patientAttendFuneralCeremonies;
	}

	public void setPatientAttendFuneralCeremonies(YesNo patientAttendFuneralCeremonies) {
		this.patientAttendFuneralCeremonies = patientAttendFuneralCeremonies;
	}

	public YesNo getPatientTravelAnytimePeriodBeforeIll() {
		return patientTravelAnytimePeriodBeforeIll;
	}

	public void setPatientTravelAnytimePeriodBeforeIll(YesNo patientTravelAnytimePeriodBeforeIll) {
		this.patientTravelAnytimePeriodBeforeIll = patientTravelAnytimePeriodBeforeIll;
	}

	public String getIfTravelYesWhere() {
		return ifTravelYesWhere;
	}

	public void setIfTravelYesWhere(String ifTravelYesWhere) {
		this.ifTravelYesWhere = ifTravelYesWhere;
	}

	public Date getIfYesStartDate() {
		return ifYesStartDate;
	}

	public void setIfYesStartDate(Date ifYesStartDate) {
		this.ifYesStartDate = ifYesStartDate;
	}

	public Date getIfYesEndDate() {
		return ifYesEndDate;
	}

	public void setIfYesEndDate(Date ifYesEndDate) {
		this.ifYesEndDate = ifYesEndDate;
	}
	public YesNo getPatientContactKnownSuspect() {
		return patientContactKnownSuspect;
	}

	public void setPatientContactKnownSuspect(YesNo patientContactKnownSuspect) {
		this.patientContactKnownSuspect = patientContactKnownSuspect;
	}

	public String getSuspectName() {
		return suspectName;
	}

	public void setSuspectName(String suspectName) {
		this.suspectName = suspectName;
	}

	public String getSuspectLastName() {
		return suspectLastName;
	}

	public void setSuspectLastName(String suspectLastName) {
		this.suspectLastName = suspectLastName;
	}

	public String getIdCase() {
		return idCase;
	}

	public void setIdCase(String idCase) {
		this.idCase = idCase;
	}

	public CaseOutcome getDuringContactSuspectCase() {
		return duringContactSuspectCase;
	}

	public void setDuringContactSuspectCase(CaseOutcome duringContactSuspectCase) {
		this.duringContactSuspectCase = duringContactSuspectCase;
	}

	public Date getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(Date dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

	public Date getDateOfLastContactWithSuspectCase() {
		return dateOfLastContactWithSuspectCase;
	}

	public void setDateOfLastContactWithSuspectCase(Date dateOfLastContactWithSuspectCase) {
		this.dateOfLastContactWithSuspectCase = dateOfLastContactWithSuspectCase;
	}





	@Override
	public String getI18nPrefix() {
		return I18N_PREFIX;
	}
}
