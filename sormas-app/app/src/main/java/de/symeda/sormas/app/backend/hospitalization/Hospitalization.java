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

package de.symeda.sormas.app.backend.hospitalization;

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

import de.symeda.sormas.api.hospitalization.AccommodationType;
import de.symeda.sormas.api.utils.InpatOutpat;
import de.symeda.sormas.api.utils.MildModerateSevereCritical;
import de.symeda.sormas.api.hospitalization.HospitalizationReasonType;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.app.backend.common.AbstractDomainObject;
import de.symeda.sormas.app.backend.common.EmbeddedAdo;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_BIG;

@Entity(name = Hospitalization.TABLE_NAME)
@DatabaseTable(tableName = Hospitalization.TABLE_NAME)
@EmbeddedAdo
public class Hospitalization extends AbstractDomainObject {

	private static final long serialVersionUID = -8576270649634034244L;

	public static final String TABLE_NAME = "hospitalizations";
	public static final String I18N_PREFIX = "CaseHospitalization";

	@Enumerated(EnumType.STRING)
	private YesNo admittedToHealthFacility;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date admissionDate;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dischargeDate;
	@Enumerated(EnumType.STRING)
	@Deprecated
	private AccommodationType accommodation;
	@Enumerated(EnumType.STRING)
	private YesNo isolated;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date isolationDate;
	@Column(length = CHARACTER_LIMIT_BIG)
	private String description;

	@Column(length = CHARACTER_LIMIT_BIG)
	private String healthFacilityRecordNumber;

	@Enumerated(EnumType.STRING)
	private YesNo leftAgainstAdvice;

	@Enumerated(EnumType.STRING)
	private YesNo hospitalizedPreviously;
	@Enumerated(EnumType.STRING)
	private YesNo intensiveCareUnit;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date intensiveCareUnitStart;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date intensiveCareUnitEnd;

	private MildModerateSevereCritical patientConditionOnAdmission;
	@Enumerated(EnumType.STRING)
	private HospitalizationReasonType hospitalizationReason;

	@Column(columnDefinition = "text")
	private String otherHospitalizationReason;
	@Enumerated(EnumType.STRING)
	private InpatOutpat selectInpatientOutpatient;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateFirstSeen;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date notifyDistrictDate;

	@Column(length = CHARACTER_LIMIT_BIG)
	private String hospitalRecordNumber;
	// just for reference, not persisted in DB
	private List<PreviousHospitalization> previousHospitalizations = new ArrayList<>();
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateFormSentToDistrict;
	@Enumerated(EnumType.STRING)
	private YesNoUnknown patientVentilated;
	@Enumerated(EnumType.STRING)
	private YesNoUnknown seenAtAHealthFacility;


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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public YesNo getHospitalizedPreviously() {
		return hospitalizedPreviously;
	}

	public void setHospitalizedPreviously(YesNo hospitalizedPreviously) {
		this.hospitalizedPreviously = hospitalizedPreviously;
	}

	public YesNo getAdmittedToHealthFacility() {
		return admittedToHealthFacility;
	}

	public void setAdmittedToHealthFacility(YesNo admittedToHealthFacility) {
		this.admittedToHealthFacility = admittedToHealthFacility;
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

	public InpatOutpat getSelectInpatientOutpatient() {
		return selectInpatientOutpatient;}
	public void setSelectInpatientOutpatient(InpatOutpat selectInpatientOutpatient) {
		this.selectInpatientOutpatient = selectInpatientOutpatient;}

	public Date getDateFirstSeen() {
		return dateFirstSeen;
	}
	public Date getNotifyDistrictDate() {
		return notifyDistrictDate;
	}

	public void setNotifyDistrictDate(Date notifyDistrictDate) {
		this.notifyDistrictDate = notifyDistrictDate;
	}
	public void setDateFirstSeen(Date dateFirstSeen) {
		this.dateFirstSeen = dateFirstSeen;
	}

	/**
	 * NOTE: This is only initialized when the hospitalization is retrieved using {@link HospitalizationDao}
	 * 
	 * @return
	 */
	public List<PreviousHospitalization> getPreviousHospitalizations() {
		return previousHospitalizations;
	}

	public void setPreviousHospitalizations(List<PreviousHospitalization> previousHospitalizations) {
		this.previousHospitalizations = previousHospitalizations;
	}

	@Override
	public String getI18nPrefix() {
		return I18N_PREFIX;
	}

	@Deprecated
	public AccommodationType getAccommodation() {
		return accommodation;
	}

	@Deprecated
	public void setAccommodation(AccommodationType accommodation) {
		this.accommodation = accommodation;
	}

	public YesNo getLeftAgainstAdvice() {
		return leftAgainstAdvice;
	}

	public void setLeftAgainstAdvice(YesNo leftAgainstAdvice) {
		this.leftAgainstAdvice = leftAgainstAdvice;
	}

	public MildModerateSevereCritical getPatientConditionOnAdmission() {
		return patientConditionOnAdmission;
	}

	public void setPatientConditionOnAdmission(MildModerateSevereCritical patientConditionOnAdmission) {
		this.patientConditionOnAdmission = patientConditionOnAdmission;
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

	public String getHealthFacilityRecordNumber() {
		return healthFacilityRecordNumber;
	}

	public void setHealthFacilityRecordNumber(String healthFacilityRecordNumber) {
		this.healthFacilityRecordNumber = healthFacilityRecordNumber;
	}

	public String getHospitalRecordNumber() {
		return hospitalRecordNumber;
	}

	public void setHospitalRecordNumber(String hospitalRecordNumber) {
		this.hospitalRecordNumber = hospitalRecordNumber;
	}

	public Date getDateFormSentToDistrict() {
		return dateFormSentToDistrict;
	}

	public void setDateFormSentToDistrict(Date dateFormSentToDistrict) {
		this.dateFormSentToDistrict = dateFormSentToDistrict;
	}

	public YesNoUnknown getPatientVentilated() {
		return patientVentilated;
	}

	public void setPatientVentilated(YesNoUnknown patientVentilated) {
		this.patientVentilated = patientVentilated;
	}

	public YesNoUnknown getSeenAtAHealthFacility() {
		return seenAtAHealthFacility;
	}

	public void setSeenAtAHealthFacility(YesNoUnknown seenAtAHealthFacility) {
		this.seenAtAHealthFacility = seenAtAHealthFacility;
	}
}
