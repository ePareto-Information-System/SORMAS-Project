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
package de.symeda.sormas.backend.hospitalization;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_BIG;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import de.symeda.sormas.api.hospitalization.HospitalizationReasonType;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.utils.FieldConstraints;
import de.symeda.sormas.api.utils.HospOut;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.backend.common.AbstractDomainObject;

@Entity
public class Hospitalization extends AbstractDomainObject {

	private static final long serialVersionUID = -8576270649634034244L;

	public static final String TABLE_NAME = "hospitalization";

	public static final String ADMITTED_TO_HEALTH_FACILITY = "admittedToHealthFacility";
	private YesNoUnknown seenAtAHealthFacility;
	private YesNoUnknown wasPatientAdmitted;
	public static final String ADMISSION_DATE = "admissionDate";
	public static final String DISCHARGE_DATE = "dischargeDate";
	public static final String ISOLATED = "isolated";
	public static final String ISOLATION_DATE = "isolationDate";
	public static final String LEFT_AGAINST_ADVICE = "leftAgainstAdvice";
	public static final String HOSPITALIZED_PREVIOUSLY = "hospitalizedPreviously";
	public static final String PREVIOUS_HOSPITALIZATIONS = "previousHospitalizations";
	public static final String INTENSIVE_CARE_UNIT = "intensiveCareUnit";
	public static final String INTENSIVE_CARE_UNIT_START = "intensiveCareUnitStart";
	public static final String INTENSIVE_CARE_UNIT_END = "intensiveCareUnitEnd";
	public static final String DESCRIPTION = "description";
	public static final String DISEASE_ONSET_DATE = "diseaseOnsetDate";
	public static final String PATIENT_HOSPITALIZED_DETAINED = "patientHospitalizedOrDetained";

	private YesNo admittedToHealthFacility;
	private YesNo admittedToHealthFacilityNew;
	private Date admissionDate;
	private Date dischargeDate;
	private YesNo isolated;
	private Date isolationDate;
	private YesNo leftAgainstAdvice;

	private YesNo hospitalizedPreviously;
	private Date changeDateOfEmbeddedLists;
	private List<PreviousHospitalization> previousHospitalizations = new ArrayList<PreviousHospitalization>();
	private YesNo intensiveCareUnit;
	private Date intensiveCareUnitStart;
	private Date intensiveCareUnitEnd;
	private HospitalizationReasonType hospitalizationReason;
	private String otherHospitalizationReason;
	private String description;
	private Date diseaseOnsetDate;
	private HospOut patientHospitalizedOrDetained;

	private String place;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_UUID_MAX, message = Validations.onlyNumbersAllowed)
	private String durationMonths;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_UUID_MAX, message = Validations.onlyNumbersAllowed)
	private String durationDays;
	private String place2;
	private String durationMonths2;
	private String durationDays2;
	private String investigatorName;
	private String investigatorTitle;
	private String investigatorUnit;
	private String investigatorAddress;
	private String investigatorTel;
	private Date notifyDistrictDate;
	private Date dateFirstSeen;
	private Date terminationDateHospitalStay;

	@Temporal(TemporalType.TIMESTAMP)
	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getIsolated() {
		return isolated;
	}

	public void setIsolated(YesNo isolated) {
		this.isolated = isolated;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getIsolationDate() {
		return isolationDate;
	}

	public void setIsolationDate(Date isolationDate) {
		this.isolationDate = isolationDate;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getHospitalizedPreviously() {
		return hospitalizedPreviously;
	}

	public void setHospitalizedPreviously(YesNo hospitalizedPreviously) {
		this.hospitalizedPreviously = hospitalizedPreviously;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = PreviousHospitalization.HOSPITALIZATION)
	public List<PreviousHospitalization> getPreviousHospitalizations() {
		return previousHospitalizations;
	}

	public void setPreviousHospitalizations(List<PreviousHospitalization> previousHospitalizations) {
		this.previousHospitalizations = previousHospitalizations;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getAdmittedToHealthFacility() {
		return admittedToHealthFacility;
	}

	@Enumerated(EnumType.STRING)
	public YesNoUnknown getSeenAtAHealthFacility() {
		return seenAtAHealthFacility;
	}

	public void setSeenAtAHealthFacility(YesNoUnknown seenAtAHealthFacility) {
		this.seenAtAHealthFacility = seenAtAHealthFacility;
	}

	public void setAdmittedToHealthFacility(YesNo admittedToHealthFacility) {
		this.admittedToHealthFacility = admittedToHealthFacility;
	}

	public YesNo getAdmittedToHealthFacilityNew() {
		return admittedToHealthFacilityNew;
	}

	public void setAdmittedToHealthFacilityNew(YesNo admittedToHealthFacilityNew) {
		this.admittedToHealthFacilityNew = admittedToHealthFacilityNew;
	}
	/**
	 * This change date has to be set whenever one of the embedded lists is modified: !oldList.equals(newList)
	 * 
	 * @return
	 */
	public Date getChangeDateOfEmbeddedLists() {
		return changeDateOfEmbeddedLists;
	}

	public void setChangeDateOfEmbeddedLists(Date changeDateOfEmbeddedLists) {
		this.changeDateOfEmbeddedLists = changeDateOfEmbeddedLists;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getLeftAgainstAdvice() {
		return leftAgainstAdvice;
	}

	public void setLeftAgainstAdvice(YesNo leftAgainstAdvice) {
		this.leftAgainstAdvice = leftAgainstAdvice;
	}

	@Enumerated(EnumType.STRING)
	public YesNo getIntensiveCareUnit() {
		return intensiveCareUnit;
	}

	public void setIntensiveCareUnit(YesNo intensiveCareUnit) {
		this.intensiveCareUnit = intensiveCareUnit;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getIntensiveCareUnitStart() {
		return intensiveCareUnitStart;
	}

	public void setIntensiveCareUnitStart(Date intensiveCareUnitStart) {
		this.intensiveCareUnitStart = intensiveCareUnitStart;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getIntensiveCareUnitEnd() {
		return intensiveCareUnitEnd;
	}

	public void setIntensiveCareUnitEnd(Date intensiveCareUnitEnd) {
		this.intensiveCareUnitEnd = intensiveCareUnitEnd;
	}

	@Enumerated(EnumType.STRING)
	public HospitalizationReasonType getHospitalizationReason() {
		return hospitalizationReason;
	}

	public void setHospitalizationReason(HospitalizationReasonType reasonForHospitalization) {
		this.hospitalizationReason = reasonForHospitalization;
	}

	public String getOtherHospitalizationReason() {
		return otherHospitalizationReason;
	}

	public void setOtherHospitalizationReason(String otherReasonForHospitalization) {
		this.otherHospitalizationReason = otherReasonForHospitalization;
	}

	@Column(length = CHARACTER_LIMIT_BIG)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDiseaseOnsetDate() {
		return diseaseOnsetDate;
	}

	public void setDiseaseOnsetDate(Date diseaseOnsetDate) {
		this.diseaseOnsetDate = diseaseOnsetDate;
	}
	@Enumerated(EnumType.STRING)
	public HospOut getPatientHospitalizedOrDetained() {
		return patientHospitalizedOrDetained;
	}

	public void setPatientHospitalizedOrDetained(HospOut patientHospitalizedOrDetained) {
		this.patientHospitalizedOrDetained = patientHospitalizedOrDetained;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getDurationMonths() {
		return durationMonths;
	}

	public void setDurationMonths(String durationMonths) {
		this.durationMonths = durationMonths;
	}

	public String getDurationDays() {
		return durationDays;
	}
	public String getPlace2() {
		return place2;
	}

	public void setPlace2(String place2) {
		this.place2 = place2;
	}

	public String getDurationMonths2() {
		return durationMonths2;
	}

	public void setDurationMonths2(String durationMonths2) {
		this.durationMonths2 = durationMonths2;
	}

	public String getDurationDays2() {
		return durationDays2;
	}

	public void setDurationDays2(String durationDays2) {
		this.durationDays2 = durationDays2;
	}

	public void setDurationDays(String durationDays) {
		this.durationDays = durationDays;
	}

	public String getInvestigatorName() {
		return investigatorName;
	}

	public void setInvestigatorName(String investigatorName) {
		this.investigatorName = investigatorName;
	}

	public String getInvestigatorTitle() {
		return investigatorTitle;
	}

	public void setInvestigatorTitle(String investigatorTitle) {
		this.investigatorTitle = investigatorTitle;
	}

	public String getInvestigatorUnit() {
		return investigatorUnit;
	}

	public void setInvestigatorUnit(String investigatorUnit) {
		this.investigatorUnit = investigatorUnit;
	}

	public String getInvestigatorAddress() {
		return investigatorAddress;
	}

	public void setInvestigatorAddress(String investigatorAddress) {
		this.investigatorAddress = investigatorAddress;
	}

	public String getInvestigatorTel() {
		return investigatorTel;
	}

	public void setInvestigatorTel(String investigatorTel) {
		this.investigatorTel = investigatorTel;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getNotifyDistrictDate() {
		return notifyDistrictDate;
	}

	public void setNotifyDistrictDate(Date notifyDistrictDate) {
		this.notifyDistrictDate = notifyDistrictDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateFirstSeen() {
		return dateFirstSeen;
	}
	public void setDateFirstSeen(Date dateFirstSeen) {
		this.dateFirstSeen = dateFirstSeen;
	}
	@Temporal(TemporalType.TIMESTAMP)
	public Date getTerminationDateHospitalStay() {
		return terminationDateHospitalStay;
	}

	public void setTerminationDateHospitalStay(Date terminationDateHospitalStay) {
		this.terminationDateHospitalStay = terminationDateHospitalStay;
	}

	@Enumerated(EnumType.STRING)
	public YesNoUnknown getWasPatientAdmitted() {
		return wasPatientAdmitted;
	}

	public void setWasPatientAdmitted(YesNoUnknown wasPatientAdmitted) {
		this.wasPatientAdmitted = wasPatientAdmitted;
	}


}
