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
import de.symeda.sormas.api.externaldata.HasExternalData;
import de.symeda.sormas.backend.common.CoreAdo;
import de.symeda.sormas.backend.location.Location;
import de.symeda.sormas.backend.sormastosormas.entities.SormasToSormasShareable;
import de.symeda.sormas.backend.sormastosormas.origin.SormasToSormasOriginInfo;
import de.symeda.sormas.backend.sormastosormas.share.outgoing.SormasToSormasShareInfo;
import de.symeda.sormas.backend.user.User;

import javax.persistence.*;
import java.util.*;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

@Entity(name = "ebs")
public class Ebs extends CoreAdo implements SormasToSormasShareable, HasExternalData {

	private static final long serialVersionUID = 4964495716032049582L;

	public static final String TABLE_NAME = "ebs";

	public static final String EXTERNAL_ID = "externalId";
	public static final String INFORMANT_NAME = "informantName";
	public static final String INFORMANT_TEL = "informantTel";
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
	public static final String SOURCE_INFORMATION = "sourceInformation";
	public static final String TRIAGING_DECISION = "triagingDecision";
	public static final String RESPONSIBLE_USER = "responsibleUser";
	public static final String EBS_LONGITUDE = "ebsLongitude";
	public static final String EBS_LATITUDE = "ebsLatitude";
	public static final String EBS_LATLONG = "ebsLatLon";
	public static final String SORMAS_TO_SORMAS_ORIGIN_INFO = "sormasToSormasOriginInfo";
	public static final String SORMAS_TO_SORMAS_SHARES = "sormasToSormasShares";
	public static final String MANUAL_SCANNING_TYPE = "manualScanningType";
	public static final String AUTOMATIC_SCANNING_TYPE = "automaticScanningType";
	public static final String SCANNING_TYPE = "scanningType";
	public static final String OTHER = "other";
	public static final String EBS_LOCATION = "ebsLocation";
	public static final String SIGNAL_CATEGORY = "signalCategory";
	public static final String VERIFIED = "verified";
	public static final String CASES = "cases";
	public static final String DEATH = "death";
	public static final String CATEGORY_OF_INFORMANT = "categoryOfInformant";
	public static final String TRIAGING = "triaging";
	public static final String SIGNAL_VERIFICATION = "signalVerification";
	public static final String RISK_ASSESSMENT = "riskAssessment";
	public static final String EBS_ALERT = "ebsAlert";
	public static final String OTHER_INFORMANT = "otherInformant";

	private String informantName;
	private String informantTel;
	private Date triageDate;
	private Date endDate;
	private Date reportDateTime;
	private Date dateOnset;
	private User reportingUser;
	private EbsSourceType sourceInformation;;
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
	private SormasToSormasOriginInfo sormasToSormasOriginInfo;
	private List<SormasToSormasShareInfo> sormasToSormasShares = new ArrayList<>(0);
	private String internalToken;
	private AutomaticScanningType automaticScanningType;
	private ManualScanningType manualScanningType;
	private MediaScannningType scanningType;
	private String other;
	private PersonReporting categoryOfInformant;
	private Triaging triaging;
	private SignalVerification signalVerification;
	private Set<RiskAssessment> riskAssessment = new HashSet<>();
	private Set<EbsAlert> ebsAlert = new HashSet<>();
	private String otherInformant;

	@Column(columnDefinition = "text")
	public String getInformantName() {
		return informantName;
	}

	public void setInformantName(String contactName) {
		this.informantName = contactName;
	}

	@Column(length = CHARACTER_LIMIT_DEFAULT)
	public String getInformantTel() {
		return informantTel;
	}

	public void setInformantTel(String contactPhoneNumber) {
		this.informantTel = contactPhoneNumber;
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
	public EbsSourceType getSourceInformation() {
		return sourceInformation;
	}

	public void setSourceInformation(EbsSourceType srcType) {
		this.sourceInformation = srcType;
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

	public PersonReporting getCategoryOfInformant() {
		return categoryOfInformant;
	}

	public void setCategoryOfInformant(PersonReporting personReporting) {
		this.categoryOfInformant = personReporting;
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
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "triaging_id")
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
	@JoinColumn(name = "signalverification_id")
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

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = EbsAlert.EBS, fetch = FetchType.LAZY)
	public Set<EbsAlert> getEbsAlert() {
		return ebsAlert;
	}

	public void setEbsAlert(Set<EbsAlert> ebsAlert) {
		this.ebsAlert = ebsAlert;
	}

	public void setRiskAssessment(Set<RiskAssessment> riskAssessment) {
		this.riskAssessment = riskAssessment;
	}

	public String getOtherInformant() {
		return otherInformant;
	}

	public void setOtherInformant(String otherInformant) {
		this.otherInformant = otherInformant;
	}
}