/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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
package de.symeda.sormas.backend.ebs;

import de.symeda.sormas.api.ebs.*;
import de.symeda.sormas.api.event.*;
import de.symeda.sormas.api.externaldata.HasExternalData;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.backend.action.Action;
import de.symeda.sormas.backend.common.CoreAdo;
import de.symeda.sormas.backend.event.SpecificRiskConverter;
import de.symeda.sormas.backend.hospitalization.PreviousHospitalization;
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.share.ExternalShareInfo;
import de.symeda.sormas.backend.sormastosormas.entities.SormasToSormasShareable;
import de.symeda.sormas.backend.sormastosormas.origin.SormasToSormasOriginInfo;
import de.symeda.sormas.backend.sormastosormas.share.outgoing.SormasToSormasShareInfo;
import de.symeda.sormas.backend.task.Task;
import de.symeda.sormas.backend.user.User;

import javax.persistence.*;
import java.util.*;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

@Entity(name = "ebs")
public class Ebs extends CoreAdo implements SormasToSormasShareable, HasExternalData {

	private static final long serialVersionUID = 4964495716032049582L;

	public static final String TABLE_NAME = "ebs";

	public static final String EXTERNAL_ID = "externalId";
	public static final String CONTACT_NAME = "contactName";
	public static final String CONTACT_PHONE_NUMBER = "contactPhoneNumber";
	public static final String RISK_LEVEL = "riskLevel";
	public static final String SPECIFIC_RISK = "specificRisk";
	public static final String EVENT_MANAGEMENT_STATUS = "eventManagementStatus";
	public static final String EVENT_DESC = "eventDesc";
	public static final String TRIAGE_DATE = "triageDate";
	public static final String SOURCE_NAME = "sourceName";
	public static final String SOURCE_URL = "sourceUrl";
	public static final String DATE_ONSET = "dateOnset";
	public static final String TIME_ONSET = "timeOnset";
	public static final String DESCRIPTION_OCCURANCE = "descriptionOccurrence";
	public static final String PERSON_REGISTERING = "personRegistering";
	public static final String PERSON_DESIGNATION = "personDesignation";
	public static final String PERSON_PHONE = "personPhone";
	public static final String REPORT_DATE_TIME = "reportDateTime";
	public static final String REPORTING_USER = "reportingUser";
	public static final String SRC_TYPE = "srcType";
	public static final String TRIAGING_DECISION = "triagingDecision";
	public static final String RESPONSIBLE_USER = "responsibleUser";
	public static final String REPORT_LAT = "reportLat";
	public static final String REPORT_LON = "reportLon";
	public static final String SORMAS_TO_SORMAS_ORIGIN_INFO = "sormasToSormasOriginInfo";
	public static final String SORMAS_TO_SORMAS_SHARES = "sormasToSormasShares";
	public static final String AUTOMATIC_SCANNING_TYPE = "automaticScanningType";
	public static final String MANUAL_SCANNING_TYPE = "manualScanningType";
	public static final String SCANNING_TYPE = "scanningType";
	public static final String OTHER = "other";
	public static final String EBS_LOCATION = "ebsLocation";
	public static final String SIGNAL_CATEGORY = "signalCategory";
	public static final String VERIFIED = "verified";
	public static final String CASES = "cases";
	public static final String DEATH = "death";
	public static final String PERSON_REPORTING = "personReporting";
	public static final String TRIAGING = "triaging";
	public static final String SIGNAL_VERIFICATION = "signalVerification";
	public static final String RISK_ASSESSMENT = "riskAssessment";


	private RiskLevel riskLevel;
	private SpecificRisk specificRisk;
	private String contactName;
	private String contactPhoneNumber;
	private Date triageDate;
	private Date endDate;
	private Date reportDateTime;
	private Date dateOnset;
	private User reportingUser;
	private EbsSourceType srcType;
	private EbsTriagingDecision triagingDecision;
	private User responsibleUser;
	private String sourceName;
	private String sourceUrl;
	private Location ebsLocation;
	private Double ebsLongitude;
	private Double ebsLatitude;
	private Double ebsLatLon;
	private String descriptionOccurrence;
	private String personRegistering;
	private String personDesignation;
	private String personPhone;
	private SignalCategory signalCategory;
	private String verified;
	private String cases;
	private String death;
	private SormasToSormasOriginInfo sormasToSormasOriginInfo;
	private List<SormasToSormasShareInfo> sormasToSormasShares = new ArrayList<>(0);

	private YesNoUnknown epidemiologicalEvidence;
	private Map<EpidemiologicalEvidenceDetail, Boolean> epidemiologicalEvidenceDetails;
	private YesNoUnknown laboratoryDiagnosticEvidence;
	private Map<LaboratoryDiagnosticEvidenceDetail, Boolean> laboratoryDiagnosticEvidenceDetails;

	private String internalToken;

	private EventIdentificationSource eventIdentificationSource;

	private List<ExternalShareInfo> externalShares = new ArrayList<>(0);
	private AutomaticScanningType automaticScanningType;
	private ManualScanningType manualScanningType;
	private MediaScannningType scanningType;
	private String other;
	private PersonReporting personReporting;
	private Triaging triaging;
	private SignalVerification signalVerification;
	private Set<RiskAssessment> riskAssessment = new HashSet<>();

	@Column
	@Enumerated(EnumType.STRING)
	public RiskLevel getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(RiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}

	@Column
	@Convert(converter = SpecificRiskConverter.class)
	public SpecificRisk getSpecificRisk() {
		return specificRisk;
	}

	public void setSpecificRisk(SpecificRisk specificRisk) {
		this.specificRisk = specificRisk;
	}

	@Column(columnDefinition = "text")
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getContactPhoneNumber() {
		return contactPhoneNumber;
	}

	public void setContactPhoneNumber(String contactPhoneNumber) {
		this.contactPhoneNumber = contactPhoneNumber;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getTriageDate() {
		return triageDate;
	}

	public void setTriageDate(Date triageDate) {
		this.triageDate = triageDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getReportDateTime() {
		return reportDateTime;
	}

	public void setReportDateTime(Date reportDateTime) {
		this.reportDateTime = reportDateTime;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public User getReportingUser() {
		return reportingUser;
	}

	public void setReportingUser(User reportingUser) {
		this.reportingUser = reportingUser;
	}

	@Enumerated(EnumType.STRING)
	public EbsSourceType getSrcType() {
		return srcType;
	}

	public void setSrcType(EbsSourceType srcType) {
		this.srcType = srcType;
	}

	@Enumerated(EnumType.STRING)
	public EbsTriagingDecision getTriagingDecision() {
		return triagingDecision;
	}

	public void setTriagingDecision(EbsTriagingDecision triagingDecision) {
		this.triagingDecision = triagingDecision;
	}

	public Date getDateOnset() {
		return dateOnset;
	}

	public void setDateOnset(Date dateOnset) {
		this.dateOnset = dateOnset;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getResponsibleUser() {
		return responsibleUser;
	}

	public void setResponsibleUser(User responsibleUser) {
		this.responsibleUser = responsibleUser;
	}
	@OneToOne(cascade = CascadeType.ALL)
	public Location getEbsLocation() {
		return ebsLocation;
	}

	public void setEbsLocation(Location ebsLocation) {
		this.ebsLocation = ebsLocation;
	}

	public Double getEbsLongitude() {
		return ebsLongitude;
	}

	public void setEbsLongitude(Double ebsLongitude) {
		this.ebsLongitude = ebsLongitude;
	}

	public Double getEbsLatitude() {
		return ebsLatitude;
	}

	public void setEbsLatitude(Double ebsLatitude) {
		this.ebsLatitude = ebsLatitude;
	}

	public Double getEbsLatLon() {
		return 	ebsLatLon;
	}

	public void setEbsLatLon(Double ebsLatlon) {
		this.ebsLatLon = ebsLatlon;
	}

	public String getDescriptionOccurrence() {
		return descriptionOccurrence;
	}

	public void setDescriptionOccurrence(String descriptionOccurrence) {
		this.descriptionOccurrence = descriptionOccurrence;
	}
	public String getPersonRegistering() {
		return personRegistering;
	}

	public void setPersonRegistering(String personRegistering) {
		this.personRegistering = personRegistering;
	}
	public String getPersonDesignation() {
		return personDesignation;
	}

	public void setPersonDesignation(String personDesignation) {
		this.personDesignation = personDesignation;
	}
	public String getPersonPhone() {
		return personPhone;
	}

	public void setPersonPhone(String personPhone) {
		this.personPhone = personPhone;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public PersonReporting getPersonReporting() {
		return personReporting;
	}

	public void setPersonReporting(PersonReporting personReporting) {
		this.personReporting = personReporting;
	}

	@ManyToOne(cascade = {
		CascadeType.PERSIST,
		CascadeType.MERGE,
		CascadeType.DETACH,
		CascadeType.REFRESH })
	public SormasToSormasOriginInfo getSormasToSormasOriginInfo() {
		return sormasToSormasOriginInfo;
	}

	public void setSormasToSormasOriginInfo(SormasToSormasOriginInfo originInfo) {
		this.sormasToSormasOriginInfo = originInfo;
	}

	@OneToMany(mappedBy = SormasToSormasShareInfo.EVENT, fetch = FetchType.LAZY)
	public List<SormasToSormasShareInfo> getSormasToSormasShares() {
		return sormasToSormasShares;
	}

	public void setSormasToSormasShares(List<SormasToSormasShareInfo> sormasToSormasShares) {
		this.sormasToSormasShares = sormasToSormasShares;
	}

	@Column(columnDefinition = "text")
	public String getInternalToken() {
		return internalToken;
	}

	public void setInternalToken(String internalToken) {
		this.internalToken = internalToken;
	}


	@Override
	public String getExternalId() {
		return "";
	}

	@Override
	public void setExternalId(String externalId) {

	}

	@Override
	public String getExternalToken() {
		return "";
	}

	@Override
	public void setExternalToken(String externalToken) {

	}

	public AutomaticScanningType getAutomaticScanningType() {
		return automaticScanningType;
	}

	public void setAutomaticScanningType(AutomaticScanningType automaticScanningType) {
		this.automaticScanningType = automaticScanningType;
	}

	public ManualScanningType getManualScanningType() {
		return manualScanningType;
	}

	public void setManualScanningType(ManualScanningType manualScanningType) {
		this.manualScanningType = manualScanningType;
	}

	public MediaScannningType getScanningType() {
		return scanningType;
	}

	public void setScanningType(MediaScannningType scanningType) {
		this.scanningType = scanningType;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public SignalCategory getSignalCategory() {
		return signalCategory;
	}

	public void setSignalCategory(SignalCategory signalCategory) {
		this.signalCategory = signalCategory;
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	public String getCases() {
		return cases;
	}

	public void setCases(String cases) {
		this.cases = cases;
	}

	public String getDeath() {
		return death;
	}

	public void setDeath(String death) {
		this.death = death;
	}
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Triaging getTriaging() {

		if (triaging == null) {
			triaging = new Triaging();
		}
		return triaging;
	}

	public void setTriaging(Triaging triaging) {
		this.triaging = triaging;
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public SignalVerification getSignalVerification() {
		if (signalVerification == null) {
			signalVerification = new SignalVerification();
		}
		return signalVerification;
	}

	public void setSignalVerification(SignalVerification signalVerification) {
		this.signalVerification = signalVerification;
	}
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = RiskAssessment.EBS, fetch = FetchType.LAZY)
	public Set<RiskAssessment> getRiskAssessment() {
		return riskAssessment;
	}

	public void setRiskAssessment(Set<RiskAssessment> riskAssessment) {
		this.riskAssessment = riskAssessment;
	}
}