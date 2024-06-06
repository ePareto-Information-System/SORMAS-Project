package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.share.ExternalShareCriteria;
import de.symeda.sormas.api.user.UserRoleReferenceDto;
import de.symeda.sormas.api.utils.IgnoreForUrl;
import de.symeda.sormas.api.utils.YesNo;
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

    private Date triageDate;
    private UserRoleReferenceDto reportingUserRole;
    private EbsSourceType sourceInformation;
    private EbsTriagingDecision triagingDecision;
    private Date reportDateTime;
    private PersonReporting categoryOfInformant;
    private String informantName;
    private String informantTel;
    private SignalCategory signalCategory;
    private YesNo verified;
    private String death;
    private Date triagingDecisionDate;
    private String personRegistering;
    private String personDesignation;
    private YesNo verificationSent;
    private Date verificationSentDate;
    private Date verifiedDate;
    private RiskAssesment riskAssessment;
    private YesNo actionInitiated;
    private ResponseStatus responseStatus;
    private Boolean deleted = Boolean.FALSE;
    private RegionReferenceDto region;
    private DistrictReferenceDto district;
    private CommunityReferenceDto community;
    private TriagingDto triagingDto;
    private SignalVerificationDto signalVerificationDto;


    private Boolean userFilterIncluded = true;
    private Set<String> excludedUuids;
    public EbsCriteria() {
        super(EbsCriteriaDateType.class);
    }


    public RiskAssesment getRiskAssessment() {
        return riskAssessment;
    }

    public EbsCriteria riskAssessment(RiskAssesment riskAssessment) {
        this.riskAssessment = riskAssessment;
        return this;
    }

    public void setRiskAssessment(RiskAssesment riskAssessment) {
        this.riskAssessment = riskAssessment;
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

    public EbsCriteria sourceInformation(EbsSourceType sourceInformation) {
        this.sourceInformation = sourceInformation;
        return this;
    }

    public TriagingDto getTriagingDto() {
        return triagingDto;
    }

    public void setTriagingDto(TriagingDto triagingDto) {
        this.triagingDto = triagingDto;
    }


    public EbsCriteria triagingDto(TriagingDto triagingDto) {
        this.triagingDto = triagingDto;
        return this;
    }

    public SignalVerificationDto getSignalVerificationDto() {
        return signalVerificationDto;
    }

    public void setSignalVerificationDto(SignalVerificationDto signalVerificationDto) {
        this.signalVerificationDto = signalVerificationDto;
    }


    public EbsCriteria signalVerificationDto(SignalVerificationDto signalVerificationDto) {
        this.signalVerificationDto = signalVerificationDto;
        return this;
    }


    //Work on this
    public EbsTriagingDecision getTriagingDecision() {
        return triagingDecision;
    }

    public void setTriagingDecision(EbsTriagingDecision triagingDecision) {
        this.triagingDecision = triagingDecision;
    }

    public EbsCriteria triagingDecision(EbsTriagingDecision triagingDecision) {
        this.triagingDecision = triagingDecision;
        return this;
    }

    public Date getReportDateTime() {
        return reportDateTime;
    }

    public void setReportDateTime(Date reportDateTime) {
        this.reportDateTime = reportDateTime;
    }

    public EbsCriteria reportDateTime(Date reportDateTime) {
        this.reportDateTime = reportDateTime;
        return this;
    }

    public Date getTriageDate() {
        return triageDate;
    }

    public void setTriageDate(Date triageDate) {
        this.triageDate = triageDate;
    }

    public EbsCriteria triageDate(Date triageDate) {
        this.triageDate = triageDate;
        return this;
    }

    public String getPersonRegistering() {
        return personRegistering;
    }

    public void setPersonRegistering(String personRegistering) {
        this.personRegistering = personRegistering;
    }

    public EbsCriteria personRegistering(String personRegistering) {
        this.personRegistering = personRegistering;
        return this;
    }

    public String getPersonDesignation() {
        return personDesignation;
    }

    public void setPersonDesignation(String personDesignation) {
        this.personDesignation = personDesignation;
    }

    public EbsCriteria personDesignation(String personDesignation) {
        this.personDesignation = personDesignation;
        return this;
    }

    public PersonReporting getCategoryOfInformant() {
        return categoryOfInformant;
    }

    public void setCategoryOfInformant(PersonReporting categoryOfInformant) {
        this.categoryOfInformant = categoryOfInformant;
    }

    public EbsCriteria categoryOfInformant(PersonReporting categoryOfInformant) {
        this.categoryOfInformant = categoryOfInformant;
        return this;
    }

    public String getInformantName() {
        return informantName;
    }

    public void setInformantName(String informantName) {
        this.informantName = informantName;
    }

    public EbsCriteria informantName(String informantName) {
        this.informantName = informantName;
        return this;
    }

    public String getInformantTel() {
        return informantTel;
    }

    public void setInformantTel(String informantTel) {
        this.informantTel = informantTel;
    }

    public EbsCriteria informantTel(String informantTel) {
        this.informantTel = informantTel;
        return this;
    }

    public SignalCategory getSignalCategory() {
        return signalCategory;
    }

    public void setSignalCategory(SignalCategory signalCategory) {
        this.signalCategory = signalCategory;
    }

    public EbsCriteria signalCategory(SignalCategory signalCategory) {
        this.signalCategory = signalCategory;
        return this;
    }

    public YesNo getVerified() {
        return verified;
    }

    public void setVerified(YesNo verified) {
        this.verified = verified;
    }

    public EbsCriteria verified(YesNo verified) {
        this.verified = verified;
        return this;
    }

    public String getDeath() {
        return death;
    }

    public void setDeath(String death) {
        this.death = death;
    }

    public EbsCriteria death(String death) {
        this.death = death;
        return this;
    }

    public Date getTriagingDecisionDate() {
        return triagingDecisionDate;
    }

    public void setTriagingDecisionDate(Date triagingDecisionDate) {
        this.triagingDecisionDate = triagingDecisionDate;
    }

    public EbsCriteria triagingDecisionDate(Date triagingDecisionDate) {
        this.triagingDecisionDate = triagingDecisionDate;
        return this;
    }

    public YesNo getVerificationSent() {
        return verificationSent;
    }

    public void setVerificationSent(YesNo verificationSent) {
        this.verificationSent = verificationSent;
    }

    public EbsCriteria verificationSent(YesNo verificationSent) {
        this.verificationSent = verificationSent;
        return this;
    }

    public Date getVerificationSentDate() {
        return verificationSentDate;
    }

    public void setVerificationSentDate(Date verificationSentDate) {
        this.verificationSentDate = verificationSentDate;
    }

    public EbsCriteria verificationSentDate(Date verificationSentDate) {
        this.verificationSentDate = verificationSentDate;
        return this;
    }

    public Date getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(Date verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public EbsCriteria verifiedDate(Date verifiedDate) {
        this.verifiedDate = verifiedDate;
        return this;
    }

    public YesNo getActionInitiated() {
        return actionInitiated;
    }

    public void setActionInitiated(YesNo actionInitiated) {
        this.actionInitiated = actionInitiated;
    }

    public EbsCriteria actionInitiated(YesNo actionInitiated) {
        this.actionInitiated = actionInitiated;
        return this;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public EbsCriteria responseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
        return this;
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
