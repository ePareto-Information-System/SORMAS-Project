package de.symeda.sormas.app.backend.persontravelhistory;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import de.symeda.sormas.api.activityascase.ActivityAsCaseType;
import de.symeda.sormas.api.epidata.TravelPeriodType;
import de.symeda.sormas.api.event.MeansOfTransport;
import de.symeda.sormas.api.event.TypeOfPlace;
import de.symeda.sormas.api.exposure.ExposureRole;
import de.symeda.sormas.api.exposure.GatheringType;
import de.symeda.sormas.api.exposure.HabitationType;
import de.symeda.sormas.api.exposure.WorkEnvironment;
import de.symeda.sormas.app.backend.common.EmbeddedAdo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.epidata.EpiData;
import de.symeda.sormas.app.backend.location.Location;
import de.symeda.sormas.app.backend.region.Community;
import de.symeda.sormas.app.backend.region.District;
import de.symeda.sormas.app.backend.region.Region;
import de.symeda.sormas.app.backend.user.User;

@Entity(name = PersonTravelHistory.TABLE_NAME)
@DatabaseTable(tableName = PersonTravelHistory.TABLE_NAME)
@EmbeddedAdo(parentAccessor = PersonTravelHistory.EPI_DATA)
public class PersonTravelHistory extends PseudonymizableAdo {

	private static final long serialVersionUID = -5570515874416024605L;

	public static final String TABLE_NAME = "persontravelhistory";
	public static final String I18N_PREFIX = "PersonTravelHistory";

	public static final String EPI_DATA = "epiData";

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private EpiData epiData;
	@Enumerated(EnumType.STRING)
	private TravelPeriodType  travelPeriodType;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateFrom;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateTo;
	@Column(columnDefinition = "text")
	private String village;
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Community subDistrict;
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private District district;
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Region region;


	@Override
	public String getI18nPrefix() {
		return I18N_PREFIX;
	}

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

	public Community getSubDistrict() {
		return subDistrict;
	}

	public void setSubDistrict(Community subDistrict) {
		this.subDistrict = subDistrict;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}
}
