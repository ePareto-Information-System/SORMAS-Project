/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2022 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.ImportIgnore;
import de.symeda.sormas.api.activityascase.ActivityAsCaseDto;
import de.symeda.sormas.api.common.DeletionReason;
import de.symeda.sormas.api.epidata.EpiDataDto;
import de.symeda.sormas.api.event.*;
import de.symeda.sormas.api.exposure.ExposureDto;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.importexport.format.ImportExportFormat;
import de.symeda.sormas.api.importexport.format.ImportFormat;
import de.symeda.sormas.api.infrastructure.country.CountryReferenceDto;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.api.sormastosormas.S2SIgnoreProperty;
import de.symeda.sormas.api.sormastosormas.SormasToSormasConfig;
import de.symeda.sormas.api.sormastosormas.SormasToSormasShareableDto;
import de.symeda.sormas.api.user.UserDto;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.utils.*;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableDto;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@DependingOnFeatureType(featureType = FeatureType.EVENT_SURVEILLANCE)
public class EbsDto extends PseudonymizableDto {

	private static final long serialVersionUID = 2430932452606853497L;

	public static final long APPROXIMATE_JSON_SIZE_IN_BYTES = 13356;

	public static final String I18N_PREFIX = "Ebs";

	public static final String RISK_LEVEL = "riskLevel";
	public static final String CONTACT_NAME = "contactName";
	public static final String SOURCE_NAME = "sourceName";
	public static final String SOURCE_URL = "sourceUrl";
	public static final String CONTACT_PHONE_NUMBER = "contactPhoneNumber";
	public static final String EBS_TITLE = "ebsTitle";
	public static final String EBS_DESC = "ebsDesc";
	public static final String TRIAGE_DATE = "triageDate";
	public static final String END_DATE = "endDate";
	public static final String REPORT_DATE_TIME = "reportDateTime";
	public static final String DATE_ONSET = "dateOnset";
	public static final String TIME_ONSET = "timeOnset";
	public static final String DESCRIPTION_OCCURRENCE = "descriptionOccurrence";
	public static final String PERSON_REGISTERING = "personRegistering";
	public static final String PERSON_DESIGNATION = "personDesignation";
	public static final String PERSON_PHONE = "personPhone";
	public static final String REPORTING_USER = "reportingUser";
	public static final String EBS_LOCATION = "ebsLocation";
	public static final String EBS_LONGITUDE = "ebsLongitude";
	public static final String EBS_LATITUDE = "ebsLatitude";
	public static final String EBS_LATLONG = "ebsLatLon";
	public static final String SRC_TYPE = "srcType";
	public static final String SCANNING_TYPE = "scanningType";
	public static final String AUTOMATIC_SCANNING_TYPE = "automaticScanningType";
	public static final String MANUAL_SCANNING_TYPE = "manualScanningType";
	public static final String OTHER = "other";
	public static final String RESPONSIBLE_USER = "responsibleUser";
	public static final String REPORT_LAT = "reportLat";
	public static final String REPORT_LON = "reportLon";
	public static final String INTERNAL_TOKEN = "internalToken";
	public static final String DELETION_REASON = "deletionReason";
	public static final String OTHER_DELETION_REASON = "otherDeletionReason";
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
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String contactName;
	private String sourceName;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String sourceUrl;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String contactPhoneNumber;
	private String descriptionOccurrence;
	private String personRegistering;
	private String personDesignation;
	private String personPhone;
	private Date triageDate;
	private Date endDate;
	@NotNull(message = Validations.validReportDateTime)
	private Date reportDateTime;
	@NotNull(message = Validations.validDateOnset)
	private Date dateOnset;
	private Date timeOnset;
	private UserReferenceDto reportingUser;
	private LocationDto ebsLocation;
	@NotNull(message = Validations.validLocation)
	private Double ebsLongitude;
	@NotNull(message = Validations.validLocation)
	private Double ebsLatitude;
	private Double ebsLatLon;

	private EbsSourceType srcType;
	private MediaScannningType scanningType;
	private AutomaticScanningType automaticScanningType;
	private ManualScanningType manualScanningType;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String other;
	private UserReferenceDto responsibleUser;
	@HideForCountriesExcept
	private Map<EpidemiologicalEvidenceDetail, Boolean> epidemiologicalEvidenceDetails;
	@HideForCountriesExcept
	private YesNoUnknown laboratoryDiagnosticEvidence;
	@HideForCountriesExcept
	private Map<LaboratoryDiagnosticEvidenceDetail, Boolean> laboratoryDiagnosticEvidenceDetails;

	@HideForCountriesExcept
	@S2SIgnoreProperty(configProperty = SormasToSormasConfig.SORMAS2SORMAS_IGNORE_INTERNAL_TOKEN)
	@Size(max = FieldConstraints.CHARACTER_LIMIT_TEXT, message = Validations.textTooLong)
	private String internalToken;

	private boolean deleted;
	private DeletionReason deletionReason;
	@Size(max = FieldConstraints.CHARACTER_LIMIT_TEXT, message = Validations.textTooLong)
	private String otherDeletionReason;

	private SignalCategory signalCategory;
	private String verified;
	private String cases;
	private String death;
	private PersonReporting personReporting;
	private TriagingDto triaging;
	private SignalVerificationDto signalVerification;
	private RiskAssessmentDto riskAssessment;


	public static EbsDto build() {
		EbsDto ebs = new EbsDto();
		ebs.setUuid(DataHelper.createUuid());


		ebs.setEbsLocation(LocationDto.build());
		ebs.setReportDateTime(new Date());
		ebs.setDateOnset(new Date());

		return ebs;
	}

	public static EbsDto build(CountryReferenceDto country, UserDto user) {
		EbsDto ebs = build();

		ebs.getEbsLocation().setCountry(country);
		ebs.getEbsLocation().setRegion(user.getRegion());
		ebs.setReportingUser(user.toReference());

		return ebs;
	}

	public RiskLevel getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(RiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}

	public SpecificRisk getSpecificRisk() {
		return specificRisk;
	}

	public void setSpecificRisk(SpecificRisk specificRisk) {
		this.specificRisk = specificRisk;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
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

	public String getContactPhoneNumber() {
		return contactPhoneNumber;
	}

	public void setContactPhoneNumber(String contactPhoneNumber) {
		this.contactPhoneNumber = contactPhoneNumber;
	}

	public String getDescriptionOccurrence() {
		return this.descriptionOccurrence;
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

	public Date getTriageDate() {
		return triageDate;
	}

	@ImportFormat(ImportExportFormat.DATE_TIME)
	public void setTriageDate(Date triageDate) {
		this.triageDate = triageDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	@ImportFormat(ImportExportFormat.DATE_TIME)
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public Date getReportDateTime() {
		return reportDateTime;
	}

	public void setReportDateTime(Date reportDateTime) {
		this.reportDateTime = reportDateTime;
	}

	public Date getDateOnset() {
		return dateOnset;
	}

	public void setDateOnset(Date dateOnset) {
		this.dateOnset = dateOnset;
	}
	public Date getTimeOnset() {
		return timeOnset;
	}

	public void setTimeOnset(Date timeOnset) {
		this.timeOnset = timeOnset;
	}

	public UserReferenceDto getReportingUser() {
		return reportingUser;
	}


	public void setReportingUser(UserReferenceDto reportingUser) {
		this.reportingUser = reportingUser;
	}

	public PersonReporting getPersonReporting() {
		return personReporting;
	}

	public void setPersonReporting(PersonReporting personReporting) {
		this.personReporting = personReporting;
	}

	public EbsSourceType getSrcType() {
		return srcType;
	}

	public void setSrcType(EbsSourceType srcType) {
		this.srcType = srcType;
	}
public MediaScannningType getScanningType() {
		return scanningType;
	}

	public void setScanningType(MediaScannningType scanningType) {
		this.scanningType = scanningType;
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
	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public LocationDto getEbsLocation() {
		return ebsLocation;
	}

	public void setEbsLocation(LocationDto ebsLocation) {
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
		return ebsLatLon;
	}

	public void setEbsLatLon(Double ebsLatLon) {
		this.ebsLatLon = ebsLatLon;
	}

	public UserReferenceDto getResponsibleUser() {
		return responsibleUser;
	}

	public void setResponsibleUser(UserReferenceDto responsibleUser) {
		this.responsibleUser = responsibleUser;
	}

	public YesNoUnknown getLaboratoryDiagnosticEvidence() {
		return laboratoryDiagnosticEvidence;
	}

	public void setLaboratoryDiagnosticEvidence(YesNoUnknown laboratoryDiagnosticEvidence) {
		this.laboratoryDiagnosticEvidence = laboratoryDiagnosticEvidence;
	}

	public Map<EpidemiologicalEvidenceDetail, Boolean> getEpidemiologicalEvidenceDetails() {
		return epidemiologicalEvidenceDetails;
	}

	public void setEpidemiologicalEvidenceDetails(Map<EpidemiologicalEvidenceDetail, Boolean> epidemiologicalEvidenceDetails) {
		this.epidemiologicalEvidenceDetails = epidemiologicalEvidenceDetails;
	}

	public Map<LaboratoryDiagnosticEvidenceDetail, Boolean> getLaboratoryDiagnosticEvidenceDetails() {
		return laboratoryDiagnosticEvidenceDetails;
	}

	public void setLaboratoryDiagnosticEvidenceDetails(Map<LaboratoryDiagnosticEvidenceDetail, Boolean> laboratoryDiagnosticEvidenceDetails) {
		this.laboratoryDiagnosticEvidenceDetails = laboratoryDiagnosticEvidenceDetails;
	}

	public String getInternalToken() {
		return internalToken;
	}

	public void setInternalToken(String internalToken) {
		this.internalToken = internalToken;
	}


	public EbsReferenceDto toReference() {
		return new EbsReferenceDto(getUuid());
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public DeletionReason getDeletionReason() {
		return deletionReason;
	}

	public void setDeletionReason(DeletionReason deletionReason) {
		this.deletionReason = deletionReason;
	}

	public String getOtherDeletionReason() {
		return otherDeletionReason;
	}

	public void setOtherDeletionReason(String otherDeletionReason) {
		this.otherDeletionReason = otherDeletionReason;
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


	public TriagingDto getTriaging() {
		return triaging;
	}

	public void setTriaging(TriagingDto triaging) {
		this.triaging = triaging;
	}

	public SignalVerificationDto getSignalVerification() {
		return signalVerification;
	}

	public void setSignalVerification(SignalVerificationDto signalVerification) {
		this.signalVerification = signalVerification;
	}

	public RiskAssessmentDto getRiskAssessment() {
		return riskAssessment;
	}

	public void setRiskAssessment(RiskAssessmentDto riskAssessment) {
		this.riskAssessment = riskAssessment;
	}

}