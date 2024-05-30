package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.event.*;
import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.share.ExternalShareCriteria;
import de.symeda.sormas.api.user.UserRoleReferenceDto;
import de.symeda.sormas.api.utils.IgnoreForUrl;
import de.symeda.sormas.api.utils.criteria.CriteriaWithDateType;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class EbsCriteria extends CriteriaWithDateType implements ExternalShareCriteria, Serializable {
    private static final long serialVersionUID = 2194071120732246594L;

    public static final String REPORTING_USER_ROLE = "reportingUserRole";
    public static final String RISK_LEVEL = "riskLevel";
    public static final String DISTRICT = "district";
    public static final String REGION = "region";
    public static final String COMMUNITY = "community";

    private RiskLevel riskLevel;
    private UserRoleReferenceDto reportingUserRole;
    private Boolean deleted = Boolean.FALSE;
    private RegionReferenceDto region;
    private DistrictReferenceDto district;
    private CommunityReferenceDto community;
    private EbsSourceType sourceInformation;
    private EbsTriagingDecision triagingDecision;
    private Set<String> excludedUuids;
    private Date reportDateTime;
    private Date triageDate;
    private Boolean userFilterIncluded = true;
    private TriagingDto triagingDto;
    private SignalVerificationDto signalVerificationDto;
    private String personRegistering;
    private String personDesignation;

    public EbsCriteria() {
        super(EbsCriteriaDateType.class);
    }


    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public EbsCriteria riskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
        return this;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }



    public Boolean getUserFilterIncluded() {
        return userFilterIncluded;
    }

    public void setUserFilterIncluded(Boolean userFilterIncluded) {
        this.userFilterIncluded = userFilterIncluded;
    }

    public UserRoleReferenceDto getReportingUserRole() {
        return reportingUserRole;
    }

    public void setReportingUserRole(UserRoleReferenceDto reportingUserRole) {
        this.reportingUserRole = reportingUserRole;
    }

    public EbsCriteria deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    @IgnoreForUrl
    public Boolean getDeleted() {
        return deleted;
    }

    public EbsCriteria region(RegionReferenceDto region) {
        this.region = region;
        return this;
    }

    public void setRegion(RegionReferenceDto region) {
        this.region = region;
    }

    public RegionReferenceDto getRegion() {
        return this.region;
    }

    public EbsCriteria district(DistrictReferenceDto district) {
        this.district = district;
        return this;
    }

    public void setDistrict(DistrictReferenceDto district) {
        this.district = district;
    }

    public DistrictReferenceDto getDistrict() {
        return this.district;
    }

    public CommunityReferenceDto getCommunity() {
        return community;
    }

    public void setCommunity(CommunityReferenceDto community) {
        this.community = community;
    }

    public EbsCriteria ebsCommunity(CommunityReferenceDto ebsCommunity) {
        this.community = ebsCommunity;
        return this;
    }

    public EbsSourceType getSourceInformation() {
        return sourceInformation;
    }

    public void setSourceInformation(EbsSourceType sourceInformation) {
        this.sourceInformation = sourceInformation;
    }

    public EbsTriagingDecision getTriagingDecision() {
        return triagingDecision;
    }

    public void setTriagingDecision(EbsTriagingDecision triagingDecision) {
        this.triagingDecision = triagingDecision;
    }


    @IgnoreForUrl
    public Set<String> getExcludedUuids() {
        return excludedUuids;
    }

    public void setExcludedUuids(Set<String> excludedUuids) {
        this.excludedUuids = excludedUuids;
    }

    public EbsCriteria excludedUuids(Set<String> excludedUuids) {
        this.excludedUuids = excludedUuids;
        return this;
    }

    public Date getReportDateTime() {
        return reportDateTime;
    }

    public void setReportDateTime(Date reportDateTime) {
        this.reportDateTime = reportDateTime;
    }
    public Date getTriageDate() {
        return triageDate;
    }

    public void setTriageDate(Date triageDate) {
        this.triageDate = triageDate;
    }

    public TriagingDto getTriagingDto() {
        return triagingDto;
    }

    public void setTriagingDto(TriagingDto triagingDto) {
        this.triagingDto = triagingDto;
    }

    public SignalVerificationDto getSignalVerificationDto() {
        return signalVerificationDto;
    }

    public void setSignalVerificationDto(SignalVerificationDto signalVerificationDto) {
        this.signalVerificationDto = signalVerificationDto;
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

    @Override
    public Boolean getOnlyEntitiesNotSharedWithExternalSurvTool() {
        return null;
    }

    @Override
    public Boolean getOnlyEntitiesSharedWithExternalSurvTool() {
        return null;
    }

    @Override
    public Boolean getOnlyEntitiesChangedSinceLastSharedWithExternalSurvTool() {
        return null;
    }
}
