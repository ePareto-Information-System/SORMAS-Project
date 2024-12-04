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
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.symeda.sormas.api.hospitalization.AccommodationType;
import de.symeda.sormas.api.hospitalization.SymptomsList;
import de.symeda.sormas.api.infrastructure.facility.FacilityReferenceDto;
import de.symeda.sormas.api.utils.DurationHours;
import de.symeda.sormas.api.utils.InpatOutpat;
import de.symeda.sormas.api.utils.MildModerateSevereCritical;
import de.symeda.sormas.api.hospitalization.HospitalizationReasonType;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.app.backend.common.AbstractDomainObject;
import de.symeda.sormas.app.backend.common.EmbeddedAdo;
import de.symeda.sormas.app.backend.facility.Facility;
import de.symeda.sormas.app.backend.region.Community;
import de.symeda.sormas.app.backend.region.District;
import de.symeda.sormas.app.backend.region.Region;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_BIG;
import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import org.apache.commons.lang3.StringUtils;

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
	@Enumerated(EnumType.STRING)
	private YesNo admittedToHealthFacilityNew;
	@Column(columnDefinition = "text")
	private String memberFamilyHelpingPatient;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateOfDeath;

	private MildModerateSevereCritical patientConditionOnAdmission;
	@Enumerated(EnumType.STRING)
	private HospitalizationReasonType hospitalizationReason;

	@Column(columnDefinition = "text")
	private String otherHospitalizationReason;
	@Enumerated(EnumType.STRING)
	private InpatOutpat selectInpatientOutpatient;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date diseaseOnsetDate;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateFirstSeen;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date notifyDistrictDate;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateFormSentToDistrict;

	@Column(length = CHARACTER_LIMIT_BIG)
	private String hospitalRecordNumber;
	// just for reference, not persisted in DB
	private List<PreviousHospitalization> previousHospitalizations = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	private YesNoUnknown patientVentilated;
	@Enumerated(EnumType.STRING)
	private YesNoUnknown seenAtAHealthFacility;
	@Enumerated(EnumType.STRING)
	private YesNoUnknown wasPatientAdmitted;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date terminationDateHospitalStay;
	@Transient
	private Set<SymptomsList> symptomsSelected;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String requestedSymptomsSelectedString;
	@Column(columnDefinition = "text")
	private String otherSymptomSelected;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date onsetOfSymptomDatetime;
	@Enumerated(EnumType.STRING)
	private YesNo symptomsOngoing;
	@Enumerated(EnumType.STRING)
	private DurationHours durationHours;
	@Enumerated(EnumType.STRING)
	private YesNo soughtMedicalAttention;
	@DatabaseField(foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 3)
	private Region soughtRegion;
	@DatabaseField(foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 3)
	private District soughtDistrict;
	@DatabaseField(foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 3)
	private Community soughtCommunity;
	@DatabaseField(foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 3)
	private Facility nameOfFacility;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateOfVisitHospital;
	@Enumerated(EnumType.STRING)
	private YesNo hospitalizationYesNo;
	@Column(columnDefinition = "text")
	private String physicianName;
	@Column(columnDefinition = "text")
	private String physicianNumber;
	@Enumerated(EnumType.STRING)
	private YesNo labTestConducted;
	@Column(columnDefinition = "text")
	private String typeOfSample;
	@Column(columnDefinition = "text")
	private String agentIdentified;


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

	public YesNo getAdmittedToHealthFacilityNew() {
		return admittedToHealthFacilityNew;
	}

	public void setAdmittedToHealthFacilityNew(YesNo admittedToHealthFacilityNew) {
		this.admittedToHealthFacilityNew = admittedToHealthFacilityNew;
	}

	public String getMemberFamilyHelpingPatient() {
		return memberFamilyHelpingPatient;
	}

	public void setMemberFamilyHelpingPatient(String memberFamilyHelpingPatient) {
		this.memberFamilyHelpingPatient = memberFamilyHelpingPatient;
	}

	public Date getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(Date dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
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

	public Date getDiseaseOnsetDate() {
		return diseaseOnsetDate;
	}

	public void setDiseaseOnsetDate(Date diseaseOnsetDate) {
		this.diseaseOnsetDate = diseaseOnsetDate;
	}

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

	public YesNoUnknown getWasPatientAdmitted() {
		return wasPatientAdmitted;
	}

	public void setWasPatientAdmitted(YesNoUnknown wasPatientAdmitted) {
		this.wasPatientAdmitted = wasPatientAdmitted;
	}

	public Date getTerminationDateHospitalStay() {
		return terminationDateHospitalStay;
	}
	public void setTerminationDateHospitalStay(Date terminationDateHospitalStay) {
		this.terminationDateHospitalStay = terminationDateHospitalStay;
	}

	@Transient
	public Set<SymptomsList> getSymptomsSelected() {
		if (symptomsSelected == null) {
			if (StringUtils.isEmpty(requestedSymptomsSelectedString)) {
				symptomsSelected = new HashSet<>();
			} else {
				symptomsSelected =
						Arrays.stream(requestedSymptomsSelectedString.split(",")).map(SymptomsList::valueOf).collect(Collectors.toSet());
			}
		}
		return symptomsSelected;
	}

	public void setSymptomsSelected(Set<SymptomsList> symptomsSelected) {
		this.symptomsSelected = symptomsSelected;

		if (this.symptomsSelected == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		symptomsSelected.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		requestedSymptomsSelectedString = sb.toString();
	}
	public String getRequestedSymptomsSelectedString() {
		return requestedSymptomsSelectedString;
	}

	public void setRequestedSymptomsSelectedString(String requestedSymptomsSelectedString) {
		this.requestedSymptomsSelectedString = requestedSymptomsSelectedString;
		symptomsSelected = null;
	}

	public String getOtherSymptomSelected() {
		return otherSymptomSelected;
	}

	public void setOtherSymptomSelected(String otherSymptomSelected) {
		this.otherSymptomSelected = otherSymptomSelected;
	}

	public Date getOnsetOfSymptomDatetime() {
		return onsetOfSymptomDatetime;
	}

	public void setOnsetOfSymptomDatetime(Date onsetOfSymptomDatetime) {
		this.onsetOfSymptomDatetime = onsetOfSymptomDatetime;
	}
	public YesNo getSymptomsOngoing() {
		return symptomsOngoing;
	}

	public void setSymptomsOngoing(YesNo symptomsOngoing) {
		this.symptomsOngoing = symptomsOngoing;
	}

	public DurationHours getDurationHours() {
		return durationHours;
	}

	public void setDurationHours(DurationHours durationHours) {
		this.durationHours = durationHours;
	}

	public YesNo getSoughtMedicalAttention() {
		return soughtMedicalAttention;
	}

	public void setSoughtMedicalAttention(YesNo soughtMedicalAttention) {
		this.soughtMedicalAttention = soughtMedicalAttention;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public Region getSoughtRegion() {
		return soughtRegion;
	}

	public void setSoughtRegion(Region soughtRegion) {
		this.soughtRegion = soughtRegion;
	}

	public void setSoughtDistrict(District soughtDistrict) {
		this.soughtDistrict = soughtDistrict;
	}
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public District getSoughtDistrict() {
		return soughtDistrict;
	}

	@OneToOne(cascade = CascadeType.ALL)

	public Community getSoughtCommunity() {
		return soughtCommunity;
	}

	public void setSoughtCommunity(Community soughtCommunity) {
		this.soughtCommunity = soughtCommunity;
	}

	public Facility getNameOfFacility() {
		return nameOfFacility;
	}

	public void setNameOfFacility(Facility nameOfFacility) {
		this.nameOfFacility = nameOfFacility;
	}

	public Date getDateOfVisitHospital() {
		return dateOfVisitHospital;
	}

	public void setDateOfVisitHospital(Date dateOfVisitHospital) {
		this.dateOfVisitHospital = dateOfVisitHospital;
	}

	public YesNo getHospitalizationYesNo() {
		return hospitalizationYesNo;
	}

	public void setHospitalizationYesNo(YesNo hospitalizationYesNo) {
		this.hospitalizationYesNo = hospitalizationYesNo;
	}

	public String getPhysicianName() {
		return physicianName;
	}

	public void setPhysicianName(String physicianName) {
		this.physicianName = physicianName;
	}

	public String getPhysicianNumber() {
		return physicianNumber;
	}

	public void setPhysicianNumber(String physicianNumber) {
		this.physicianNumber = physicianNumber;
	}

	public YesNo getLabTestConducted() {
		return labTestConducted;
	}

	public void setLabTestConducted(YesNo labTestConducted) {
		this.labTestConducted = labTestConducted;
	}

	public String getTypeOfSample() {
		return typeOfSample;
	}

	public void setTypeOfSample(String typeOfSample) {
		this.typeOfSample = typeOfSample;
	}

	public String getAgentIdentified() {
		return agentIdentified;
	}

	public void setAgentIdentified(String agentIdentified) {
		this.agentIdentified = agentIdentified;
	}
}
