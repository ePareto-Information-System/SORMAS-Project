package de.symeda.sormas.backend.containmentmeasure;

import de.symeda.sormas.api.epidata.TravelPeriodType;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.epidata.EpiData;
import de.symeda.sormas.backend.infrastructure.community.Community;
import de.symeda.sormas.backend.infrastructure.district.District;
import de.symeda.sormas.backend.infrastructure.region.Region;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity(name = "containmentMeasures")
public class ContainmentMeasure extends AbstractDomainObject {
    private static final long serialVersionUID = -5570515874416024602L;

    public static final String TABLE_NAME = "containmentMeasures";
    public static final String EPI_DATA = "epiData";
    public static final String LOCATION_OF_WORM = "locationOfWorm";
    public static final String DATE_WORM_DETECTED_EMERGENCE = "dateWormDetectedEmergence";
    public static final String DATE_WORM_DETECT_BY_SUPERVISOR = "dateWormDetectBySupervisor";
    public static final String DATE_CONFIRMED = "dateConfirmed";
    public static final String DATE_OF_GUINEA_WORM_EXPULLED = "dateOfGuineaWormExpelled";
    public static final String REGULAR_BANDAGING = "regularBandaging";
    public static final String COMPLETELY_EXTRACTED = "completelyExtracted";

    private EpiData epiData;
    private String uuid;
    private String locationOfWorm;
    private Date dateWormDetectedEmergence;
    private Date dateWormDetectBySupervisor;
    private Date dateConfirmed;
    private Date dateOfGuineaWormExpelled;
    private YesNo regularBandaging;
    private YesNo completelyExtracted;

    public ContainmentMeasure() {
    }

    public ContainmentMeasure(
            String uuid,
            String locationOfWorm,
            Date dateWormDetectedEmergence,
            Date dateWormDetectBySupervisor,
            Date dateConfirmed,
            Date dateOfGuineaWormExpelled,
            YesNo regularBandaging,
            YesNo completelyExtracted) {
        this.uuid = uuid;
        this.locationOfWorm = locationOfWorm;
        this.dateWormDetectedEmergence = dateWormDetectedEmergence;
        this.dateWormDetectBySupervisor = dateWormDetectBySupervisor;
        this.dateConfirmed = dateConfirmed;
        this.dateOfGuineaWormExpelled = dateOfGuineaWormExpelled;
        this.regularBandaging = regularBandaging;
        this.completelyExtracted = completelyExtracted;
    }

    @ManyToOne
    @JoinColumn(nullable = false)
    public EpiData getEpiData() {
        return epiData;
    }

    public void setEpiData(EpiData epiData) {
        this.epiData = epiData;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLocationOfWorm() {
        return locationOfWorm;
    }

    public void setLocationOfWorm(String locationOfWorm) {
        this.locationOfWorm = locationOfWorm;
    }

    public Date getDateWormDetectedEmergence() {
        return dateWormDetectedEmergence;
    }

    public void setDateWormDetectedEmergence(Date dateWormDetectedEmergence) {
        this.dateWormDetectedEmergence = dateWormDetectedEmergence;
    }

    public Date getDateWormDetectBySupervisor() {
        return dateWormDetectBySupervisor;
    }

    public void setDateWormDetectBySupervisor(Date dateWormDetectBySupervisor) {
        this.dateWormDetectBySupervisor = dateWormDetectBySupervisor;
    }

    public Date getDateConfirmed() {
        return dateConfirmed;
    }

    public void setDateConfirmed(Date dateConfirmed) {
        this.dateConfirmed = dateConfirmed;
    }

    public Date getDateOfGuineaWormExpelled() {
        return dateOfGuineaWormExpelled;
    }

    public void setDateOfGuineaWormExpelled(Date dateOfGuineaWormExpelled) {
        this.dateOfGuineaWormExpelled = dateOfGuineaWormExpelled;
    }

    public YesNo getRegularBandaging() {
        return regularBandaging;
    }

    public void setRegularBandaging(YesNo regularBandaging) {
        this.regularBandaging = regularBandaging;
    }

    public YesNo getCompletelyExtracted() {
        return completelyExtracted;
    }

    public void setCompletelyExtracted(YesNo completelyExtracted) {
        this.completelyExtracted = completelyExtracted;
    }
}
