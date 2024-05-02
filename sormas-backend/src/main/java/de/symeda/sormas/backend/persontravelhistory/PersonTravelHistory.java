package de.symeda.sormas.backend.persontravelhistory;

import de.symeda.sormas.api.epidata.TravelPeriodType;
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

@Entity(name = "persontravelhistory")
public class PersonTravelHistory extends AbstractDomainObject {
    private static final long serialVersionUID = -5570515874416024602L;

    public static final String TABLE_NAME = "persontravelhistory";
    public static final String EPI_DATA = "epiData";
    public static final String TRAVEL_PERIOD = "travelPeriod";
    public static final String DATE_FROM = "dateFrom";
    public static final String DATE_TO = "dateTo";
    public static final String VILLAGE = "village";
    public static final String SUB_DISTRICT = "subDistrict";
    public static final String DISTRICT = "district";
    public static final String REGION = "region";

    private EpiData epiData;
    private TravelPeriodType travelPeriodType;
    private Date dateFrom;
    private Date dateTo;
    private String village;
    private Community subDistrict;
    private District district;
    private Region region;

    public PersonTravelHistory() {
    }

    public PersonTravelHistory(
        TravelPeriodType travelPeriodType,
        Date dateFrom,
        Date dateTo,
        String village,
        Community subDistrict,
        District district,
        Region region) {
        this.travelPeriodType = travelPeriodType;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.village = village;
        this.subDistrict = subDistrict;
        this.district = district;
        this.region = region;
    }

    @ManyToOne
    @JoinColumn(nullable = false)
    public EpiData getEpiData() {
        return epiData;
    }

    public void setEpiData(EpiData epiData) {
        this.epiData = epiData;
    }

    public TravelPeriodType getTravelPeriodType() {
        return travelPeriodType;
    }

    public void setTravelPeriodType(TravelPeriodType travelPeriodType) {
        this.travelPeriodType = travelPeriodType;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public Community getSubDistrict() {
        return subDistrict;
    }
    public void setSubDistrict(Community subDistrict) {
        this.subDistrict = subDistrict;
    }
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
