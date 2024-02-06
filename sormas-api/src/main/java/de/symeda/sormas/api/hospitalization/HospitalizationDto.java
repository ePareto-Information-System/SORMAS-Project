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
package de.symeda.sormas.api.hospitalization;

import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.utils.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.ImportIgnore;
import de.symeda.sormas.api.i18n.Validations;

@DependingOnFeatureType(featureType = FeatureType.CASE_SURVEILANCE)
public class HospitalizationDto extends EntityDto {

	private static final long serialVersionUID = 4846215199480684369L;

	public static final String I18N_PREFIX = "CaseHospitalization";

	public static final String ADMITTED_TO_HEALTH_FACILITY = "admittedToHealthFacility";
	public static final String ADMITTED_TO_HEALTH_FACILITY_NEW = "admittedToHealthFacilityNew";
	public static final String ADMISSION_DATE = "admissionDate";
	public static final String NOTIFY_DISTRICT_DATE = "notifyDistrictDate";
	public static final String DATE_FIRST_SEEN_HOSPITAL_FOR_DISEASE = "dateFirstSeen";
	public static final String TERMINATION_DATE_HOSPITAL_STAY = "terminationDateHospitalStay";
	public static final String DISEASE_ONSET_DATE = "diseaseOnsetDate";
	public static final String PATIENT_HOSPITALIZED_DETAINED = "patientHospitalizedOrDetained";
	public static final String PLACE = "place";
	public static final String DURATION_MONTHS = "durationMonths";
	public static final String DURATION_DAYS = "durationDays";
	public static final String PLACE2 = "place2";
	public static final String DURATION_MONTHS2 = "durationMonths2";
	public static final String DURATION_DAYS2 = "durationDays2";
	public static final String INVESTIGATOR_NAME = "investigatorName";
	public static final String INVESTIGATOR_TITLE = "investigatorTitle";
	public static final String INVESTIGATOR_UNIT = "investigatorUnit";
	public static final String INVESTIGATOR_ADDRESS = "investigatorAddress";
	public static final String INVESTIGATOR_TEL = "investigatorTel";
	public static final String DISCHARGE_DATE = "dischargeDate";
	public static final String ISOLATED = "isolated";
	public static final String ISOLATION_DATE = "isolationDate";
	public static final String LEFT_AGAINST_ADVICE = "leftAgainstAdvice";
	public static final String HOSPITALIZED_PREVIOUSLY = "hospitalizedPreviously";
	public static final String PREVIOUS_HOSPITALIZATIONS = "previousHospitalizations";
	public static final String INTENSIVE_CARE_UNIT = "intensiveCareUnit";
	public static final String INTENSIVE_CARE_UNIT_START = "intensiveCareUnitStart";
	public static final String INTENSIVE_CARE_UNIT_END = "intensiveCareUnitEnd";
	public static final String HOSPITALIZATION_REASON = "hospitalizationReason";
	public static final String OTHER_HOSPITALIZATION_REASON = "otherHospitalizationReason";
	public static final String DESCRIPTION = "description";

	// Fields are declared in the order they should appear in the import template

	@Outbreaks
	private YesNo admittedToHealthFacility;
	private YesNo admittedToHealthFacilityNew;
	private Date admissionDate;
	private Date notifyDistrictDate;
	private Date dateFirstSeen;
	private Date terminationDateHospitalStay;
	private Date diseaseOnsetDate;
	private HospOut patientHospitalizedOrDetained;
	private Date dischargeDate;
	private YesNo isolated;
	private Date isolationDate;
	private YesNo leftAgainstAdvice;

	private YesNo hospitalizedPreviously;
	@Valid
	private List<PreviousHospitalizationDto> previousHospitalizations = new ArrayList<>();
	private YesNo intensiveCareUnit;
	private Date intensiveCareUnitStart;
	private Date intensiveCareUnitEnd;
	private HospitalizationReasonType hospitalizationReason;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_TEXT, message = Validations.textTooLong)
	private String otherHospitalizationReason;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_BIG, message = Validations.textTooLong)
	private String description;
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

	public static HospitalizationDto build() {
		HospitalizationDto hospitalization = new HospitalizationDto();
		hospitalization.setUuid(DataHelper.createUuid());
		return hospitalization;
	}

	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	public Date getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	public YesNo getIsolated() {
		return isolated;
	}

	public void setIsolated(YesNo isolated) {
		this.isolated = isolated;
	}

	public Date getIsolationDate() {
		return isolationDate;
	}

	public void setIsolationDate(Date isolationDate) {
		this.isolationDate = isolationDate;
	}

	@ImportIgnore
	public YesNo getHospitalizedPreviously() {
		return hospitalizedPreviously;
	}

	public void setHospitalizedPreviously(YesNo hospitalizedPreviously) {
		this.hospitalizedPreviously = hospitalizedPreviously;
	}

	@ImportIgnore
	public List<PreviousHospitalizationDto> getPreviousHospitalizations() {
		return previousHospitalizations;
	}

	public void setPreviousHospitalizations(List<PreviousHospitalizationDto> previousHospitalizations) {
		this.previousHospitalizations = previousHospitalizations;
	}

	public YesNo getAdmittedToHealthFacility() {
		return admittedToHealthFacility;
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

	public YesNo getIntensiveCareUnit() {
		return intensiveCareUnit;
	}

	public void setIntensiveCareUnit(YesNo intensiveCareUnit) {
		this.intensiveCareUnit = intensiveCareUnit;
	}

	public Date getIntensiveCareUnitStart() {
		return intensiveCareUnitStart;
	}

	public void setIntensiveCareUnitStart(Date intensiveCareUnitStart) {
		this.intensiveCareUnitStart = intensiveCareUnitStart;
	}

	public Date getIntensiveCareUnitEnd() {
		return intensiveCareUnitEnd;
	}

	public void setIntensiveCareUnitEnd(Date intensiveCareUnitEnd) {
		this.intensiveCareUnitEnd = intensiveCareUnitEnd;
	}

	public YesNo getLeftAgainstAdvice() {
		return leftAgainstAdvice;
	}

	public void setLeftAgainstAdvice(YesNo leftAgainstAdvice) {
		this.leftAgainstAdvice = leftAgainstAdvice;
	}

	public HospitalizationReasonType getHospitalizationReason() {
		return hospitalizationReason;
	}

	public void setHospitalizationReason(HospitalizationReasonType hospitalizationReason) {
		this.hospitalizationReason = hospitalizationReason;
	}

	public String getOtherHospitalizationReason() {
		return otherHospitalizationReason;
	}

	public void setOtherHospitalizationReason(String otherHospitalizationReason) {
		this.otherHospitalizationReason = otherHospitalizationReason;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDiseaseOnsetDate() {
		return diseaseOnsetDate;
	}

	public void setDiseaseOnsetDate(Date diseaseOnsetDate) {
		this.diseaseOnsetDate = diseaseOnsetDate;
	}

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

	public Date getNotifyDistrictDate() {
		return notifyDistrictDate;
	}

	public void setNotifyDistrictDate(Date notifyDistrictDate) {
		this.notifyDistrictDate = notifyDistrictDate;
	}

	public Date getDateFirstSeen() {
		return dateFirstSeen;
	}

	public void setDateFirstSeen(Date dateFirstSeen) {
		this.dateFirstSeen = dateFirstSeen;
	}

	public Date getTerminationDateHospitalStay() {
		return terminationDateHospitalStay;
	}
	public void setTerminationDateHospitalStay(Date terminationDateHospitalStay) {
		this.terminationDateHospitalStay = terminationDateHospitalStay;
	}
}
