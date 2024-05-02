package de.symeda.sormas.api.epidata;

import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DependingOnFeatureType;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableDto;

import java.util.Date;


@DependingOnFeatureType(featureType = {
        FeatureType.CASE_SURVEILANCE,
        FeatureType.CONTACT_TRACING })
public class PersonTravelHistoryDto  extends PseudonymizableDto  {

    public static String I18N_PREFIX = "PersonTravelHistory";

    private static final long serialVersionUID = 6551672739041643942L;

    public static final String TABLE_NAME = "persontravelhistory";
    public static final String TRAVEL_PERIOD_TYPE = "travelPeriodType";
    public static final String DATE_FROM = "dateFrom";
    public static final String DATE_TO = "dateTo";
    public static final String VILLAGE = "village";
    public static final String SUB_DISTRICT = "subDistrict";

    public static final String DISTRICT = "district";
    public static final String REGION = "region";

    private TravelPeriodType travelPeriodType;
    private Date dateFrom;
    private Date dateTo;
    private String village;
    private CommunityReferenceDto subDistrict;
    private DistrictReferenceDto district;
    private RegionReferenceDto region;

    public static PersonTravelHistoryDto build(TravelPeriodType travelPeriodType) {
        PersonTravelHistoryDto personTravelHistoryDto = new PersonTravelHistoryDto();
        personTravelHistoryDto.setUuid(DataHelper.createUuid());
        personTravelHistoryDto.setTravelPeriodType(TravelPeriodType.TEN_TO_FOURTEEN_MONTHS);
        personTravelHistoryDto.setTravelPeriodType(travelPeriodType);
        return personTravelHistoryDto;
    }

    public static PersonTravelHistoryDto build(
            TravelPeriodType travelPeriodType,
            Date dateFrom,
            Date dateTo,
            String village,
            CommunityReferenceDto subDistrict,
            DistrictReferenceDto district,
            RegionReferenceDto region) {
        PersonTravelHistoryDto personTravelHistoryDto = new PersonTravelHistoryDto();
        personTravelHistoryDto.setTravelPeriodType(travelPeriodType);
        personTravelHistoryDto.setDateFrom(dateFrom);
        personTravelHistoryDto.setDateTo(dateTo);
        personTravelHistoryDto.setVillage(village);
        personTravelHistoryDto.setSubDistrict(subDistrict);
        personTravelHistoryDto.setDistrict(district);
        personTravelHistoryDto.setRegion(region);
        return personTravelHistoryDto;
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

    public CommunityReferenceDto getSubDistrict() {
        return subDistrict;
    }

    public void setSubDistrict(CommunityReferenceDto subDistrict) {
        this.subDistrict = subDistrict;
    }

    public DistrictReferenceDto getDistrict() {
        return district;
    }

    public void setDistrict(DistrictReferenceDto district) {
        this.district = district;
    }

    public RegionReferenceDto getRegion() {
        return region;
    }

    public void setRegion(RegionReferenceDto region) {
        this.region = region;
    }

    @Override
    public PersonTravelHistoryDto clone() throws CloneNotSupportedException {
        PersonTravelHistoryDto clone = (PersonTravelHistoryDto) super.clone();
//        clone.setRegion((RegionReferenceDto) clone.getRegion().clone());
        return clone;
    }
}
