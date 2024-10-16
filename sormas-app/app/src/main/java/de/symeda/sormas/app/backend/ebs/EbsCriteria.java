package de.symeda.sormas.app.backend.ebs;

import java.io.Serializable;
import java.util.Date;

import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.ebs.EbsTriagingDecision;
import de.symeda.sormas.api.ebs.SignalCategory;
import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.app.backend.ebs.signalVerification.SignalVerification;
import de.symeda.sormas.app.backend.location.Location;
import de.symeda.sormas.app.backend.region.Community;
import de.symeda.sormas.app.backend.region.District;
import de.symeda.sormas.app.backend.region.Region;

public class EbsCriteria implements Serializable {
    private SignalOutcome signalOutcome;
    private EbsSourceType sourceInformation;
    private SignalVerification signalVerification;
    private EbsTriagingDecision triagingDecision;
    private SignalCategory signalCategory;
    private Region region;
    private District district;
    private Community community;
    private Location location;
    private Date reportDateTime;
    private Date triageDate;

    public SignalOutcome getSignalOutcome() {
        return signalOutcome;
    }

    public void setSignalOutcome(SignalOutcome signalOutcome) {
        this.signalOutcome = signalOutcome;
    }

    public EbsCriteria signalOutcome(SignalOutcome signalOutcome){
        this.signalOutcome = signalOutcome;
        return this;
    }

    public SignalVerification getSignalVerification() {
        return signalVerification;
    }

    public void setSignalVerification(SignalVerification signalVerification) {
        this.signalVerification = signalVerification;
    }

    public EbsCriteria signalVerification(SignalVerification signalVerification) {
        this.signalVerification = signalVerification;
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

    public void setRegion(Region region) {
        this.region = region;
    }

    public Region getRegion() {
        return region;
    }


    public EbsCriteria region(Region region) {
        this.region = region;
        return this;
    }

    public District getDistrict() {
        return this.district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public EbsCriteria district(District district) {
        this.district = district;
        return this;
    }
    public Community getCommunity() {
        return community;
    }

    public Location getLocation(){
        return location;
    }
    public void setLocation(Location location){
        this.location = location;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public EbsCriteria ebsCommunity(Community ebsCommunity) {
        this.community = ebsCommunity;
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
}
