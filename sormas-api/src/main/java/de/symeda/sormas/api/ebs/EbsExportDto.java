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

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.disease.DiseaseVariant;
import de.symeda.sormas.api.event.*;
import de.symeda.sormas.api.importexport.ExportEntity;
import de.symeda.sormas.api.importexport.ExportGroup;
import de.symeda.sormas.api.importexport.ExportGroupType;
import de.symeda.sormas.api.importexport.ExportProperty;
import de.symeda.sormas.api.importexport.format.ExportFormat;
import de.symeda.sormas.api.importexport.format.ImportExportFormat;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.utils.Order;
import de.symeda.sormas.api.utils.YesNoUnknown;
import de.symeda.sormas.api.uuid.AbstractUuidDto;

import java.util.Date;

@ExportEntity(EbsDto.class)
public class EbsExportDto extends AbstractUuidDto {

	public static final String I18N_PREFIX = "EbsExport";

	public static final String LATEST_EVENT_GROUP = "latestEventGroup";
	public static final String EVENT_GROUP_COUNT = "eventGroupCount";
	public static final String PARTICIPANT_COUNT = "participantCount";
	public static final String CASE_COUNT = "caseCount";
	public static final String DEATH_COUNT = "deathCount";
	public static final String CONTACT_COUNT = "contactCount";
	public static final String CONTACT_COUNT_SOURCE_IN_EVENT = "contactCountSourceInEvent";

	private String externalId;
	private String externalToken;
	private String internalToken;
	private RiskLevel riskLevel;
	private SpecificRisk specificRisk;
	private long caseCount;
	private long deathCount;
	private long contactCount;
	private long contactCountSourceInEvent;
	private Disease disease;
	private Date startDate;
	private Date endDate;
	private String ebsTitle;
	private String ebsDesc;
	private DiseaseTransmissionMode diseaseTransmissionMode;
	private String region;
	private String district;
	private String community;
	private String town;
	private String houseNumber;
	private String additionalInformation;
	private String srcInstitutionalPartnerType;
	private String srcFirstName;
	private String srcLastName;
	private String srcTelNo;
	private String srcEmail;
	private String srcMediaWebsite;
	private String srcMediaName;
	private String srcMediaDetails;
	private Date reportDateTime;
	private UserReferenceDto reportingUser;
	private UserReferenceDto responsibleUser;
	private EbsManagementStatus ebsManagementStatus;
	private DiseaseVariant diseaseVariant;
	private String diseaseVariantDetails;

	private boolean isInJurisdictionOrOwned;

	public EbsExportDto(
		String uuid,
		String externalId,
		String externalToken,
		String internalToken,
		RiskLevel riskLevel,
		SpecificRisk specificRisk,
		Disease disease,
		DiseaseVariant diseaseVariant,
		String diseaseDetails,
		String diseaseVariantDetails,
		Date startDate,
		Date endDate,
		Date evolutionDate,
		String evolutionComment,
		String ebsTitle,
		String ebsDesc,
		DiseaseTransmissionMode diseaseTransmissionMode,
		YesNoUnknown nosocomial,
		YesNoUnknown transregionalOutbreak,
		MeansOfTransport meansOfTransport,
		String meansOfTransportDetails,
		String regionUuid,
		String region,
		String districtUuid,
		String district,
		String communityUuid,
		String community,
		String city,
		String street,
		String houseNumber,
		String additionalInformation,
		InstitutionalPartnerType srcInstitutionalPartnerType,
		String srcInstitutionalPartnerTypeDetails,
		String srcFirstName,
		String srcLastName,
		String srcTelNo,
		String srcEmail,
		String srcMediaWebsite,
		String srcMediaName,
		String srcMediaDetails,
		Date reportDateTime,
		String reportingUserUuid,
		String reportingUserFirstName,
		String reportingUserLastName,
		String responsibleUserUuid,
		String responsibleUserFirstName,
		String responsibleUserLastName,
		boolean isInJurisdictionOrOwned,
		EbsManagementStatus ebsManagementStatus) {
		super(uuid);
		this.externalId = externalId;
		this.externalToken = externalToken;
		this.internalToken = internalToken;
		this.riskLevel = riskLevel;
		this.specificRisk = specificRisk;
		this.disease = disease;
		this.diseaseVariantDetails = diseaseVariantDetails;
		this.startDate = startDate;
		this.endDate = endDate;
		this.ebsTitle = ebsTitle;
		this.diseaseTransmissionMode = diseaseTransmissionMode;
		this.region = region;
		this.district = district;
		this.community = community;
		this.town = town;
		this.houseNumber = houseNumber;
		this.additionalInformation = additionalInformation;
		this.srcInstitutionalPartnerType =
			EbsHelper.buildInstitutionalPartnerTypeString(srcInstitutionalPartnerType, srcInstitutionalPartnerTypeDetails);
		this.srcFirstName = srcFirstName;
		this.srcLastName = srcLastName;
		this.srcTelNo = srcTelNo;
		this.srcMediaWebsite = srcMediaWebsite;
		this.srcMediaName = srcMediaName;
		this.srcMediaDetails = srcMediaDetails;
		this.reportDateTime = reportDateTime;
		this.reportingUser = new UserReferenceDto(reportingUserUuid, reportingUserFirstName, reportingUserLastName);
		this.responsibleUser = new UserReferenceDto(responsibleUserUuid, responsibleUserFirstName, responsibleUserLastName);
		this.isInJurisdictionOrOwned = isInJurisdictionOrOwned;
		this.ebsManagementStatus = ebsManagementStatus;
	}

	@Order(0)
	@ExportProperty(EbsDto.UUID)
	@ExportGroup(ExportGroupType.CORE)
	@Override
	public String getUuid() {
		return super.getUuid();
	}

	@Order(1)
	@ExportGroup(ExportGroupType.CORE)
	public String getExternalId() {
		return externalId;
	}
	
	public void setEbsManagementStatus(EbsManagementStatus ebsManagementStatus) {
		this.ebsManagementStatus = ebsManagementStatus;
	}
	
	@Order(5)
	@ExportProperty(EbsDto.RISK_LEVEL)
	@ExportGroup(ExportGroupType.CORE)
	public RiskLevel getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(RiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}
	@Order(14)
	@ExportProperty(EbsDto.TRIAGE_DATE)
	@ExportGroup(ExportGroupType.CORE)
	@ExportFormat(ImportExportFormat.DATE_TIME)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Order(15)
	@ExportProperty(EbsDto.END_DATE)
	@ExportGroup(ExportGroupType.CORE)
	@ExportFormat(ImportExportFormat.DATE_TIME)
	public Date getEndDate() {
		return endDate;
	}

	@Order(18)
	@ExportProperty(EbsDto.EBS_TITLE)
	@ExportGroup(ExportGroupType.CORE)
	public String getEbsTitle() {
		return ebsTitle;
	}

	public void setEbsTitle(String ebsTitle) {
		this.ebsTitle = ebsTitle;
	}

	@Order(19)
	@ExportProperty(EbsDto.EBS_DESC)
	@ExportGroup(ExportGroupType.CORE)
	public String getEbsDesc() {
		return ebsDesc;
	}

	public void setEbsDesc(String ebsDesc) {
		this.ebsDesc = ebsDesc;
	}
	@Order(26)
	@ExportEntity(LocationDto.class)
	@ExportProperty({
		EbsDto.EBS_LOCATION,
		LocationDto.REGION })
	@ExportGroup(ExportGroupType.LOCATION)
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Order(27)
	@ExportEntity(LocationDto.class)
	@ExportProperty({
		EbsDto.EBS_LOCATION,
		LocationDto.DISTRICT })
	@ExportGroup(ExportGroupType.LOCATION)
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Order(28)
	@ExportEntity(LocationDto.class)
	@ExportProperty({
		EbsDto.EBS_LOCATION,
		LocationDto.COMMUNITY })
	@ExportGroup(ExportGroupType.LOCATION)
	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	@Order(29)
	@ExportEntity(LocationDto.class)
	@ExportProperty({
		EbsDto.EBS_LOCATION,
		LocationDto.CITY })
	@ExportGroup(ExportGroupType.LOCATION)
	public String getCity() {
		return town;
	}

	public void setCity(String town) {
		this.town = town;
	}

	@Order(31)
	@ExportEntity(LocationDto.class)
	@ExportProperty({
		EbsDto.EBS_LOCATION,
		LocationDto.HOUSE_NUMBER })
	@ExportGroup(ExportGroupType.LOCATION)
	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	@Order(32)
	@ExportEntity(LocationDto.class)
	@ExportProperty({
		EbsDto.EBS_LOCATION,
		LocationDto.ADDITIONAL_INFORMATION })
	@ExportGroup(ExportGroupType.LOCATION)
	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	@Order(42)
	@ExportProperty(EbsDto.REPORT_DATE_TIME)
	@ExportGroup(ExportGroupType.CORE)
	public Date getReportDateTime() {
		return reportDateTime;
	}

	public void setReportDateTime(Date reportDateTime) {
		this.reportDateTime = reportDateTime;
	}

	@Order(43)
	@ExportProperty(EbsDto.REPORTING_USER)
	@ExportGroup(ExportGroupType.CORE)
	public UserReferenceDto getReportingUser() {
		return reportingUser;
	}

	public void setReportingUser(UserReferenceDto reportingUser) {
		this.reportingUser = reportingUser;
	}

	@Order(44)
	@ExportProperty(EbsDto.RESPONSIBLE_USER)
	@ExportGroup(ExportGroupType.CORE)
	public UserReferenceDto getResponsibleUser() {
		return responsibleUser;
	}

	public void setResponsibleUser(UserReferenceDto responsibleUser) {
		this.responsibleUser = responsibleUser;
	}

	@Order(46)
	@ExportProperty(CASE_COUNT)
	@ExportGroup(ExportGroupType.CORE)
	public long getCaseCount() {
		return caseCount;
	}

	public void setCaseCount(long caseCount) {
		this.caseCount = caseCount;
	}

	@Order(47)
	@ExportProperty(DEATH_COUNT)
	@ExportGroup(ExportGroupType.CORE)
	public long getDeathCount() {
		return deathCount;
	}

	public void setDeathCount(long deathCount) {
		this.deathCount = deathCount;
	}

	@Order(48)
	@ExportProperty(CONTACT_COUNT)
	@ExportGroup(ExportGroupType.CORE)
	public long getContactCount() {
		return contactCount;
	}

	public void setContactCount(long contactCount) {
		this.contactCount = contactCount;
	}

	@Order(50)
	@ExportProperty(EbsDto.CONTACT_PHONE_NUMBER)
	@ExportGroup(ExportGroupType.CORE)
	public String getExternalToken() {
		return externalToken;
	}

	@Order(51)
	@ExportProperty(EbsDto.INTERNAL_TOKEN)
	@ExportGroup(ExportGroupType.CORE)
	public String getInternalToken() {
		return internalToken;
	}

	public boolean getInJurisdictionOrOwned() {
		return isInJurisdictionOrOwned;
	}
}
