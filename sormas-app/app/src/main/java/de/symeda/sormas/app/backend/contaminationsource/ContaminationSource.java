package de.symeda.sormas.app.backend.contaminationsource;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import de.symeda.sormas.api.epidata.TravelPeriodType;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.app.backend.common.EmbeddedAdo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.epidata.EpiData;
import de.symeda.sormas.app.backend.region.Community;
import de.symeda.sormas.app.backend.region.District;
import de.symeda.sormas.app.backend.region.Region;

@Entity(name = ContaminationSource.TABLE_NAME)
@DatabaseTable(tableName = ContaminationSource.TABLE_NAME)
@EmbeddedAdo(parentAccessor = ContaminationSource.EPI_DATA)
public class ContaminationSource extends PseudonymizableAdo {

	private static final long serialVersionUID = -5570515874416024605L;

	public static final String TABLE_NAME = "contaminationsources";
	public static final String I18N_PREFIX = "ContaminationSource";

	public static final String EPI_DATA = "epiData";

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private EpiData epiData;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String name;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String contaminationType;
	@Column
	private Double longitude;
	@Column
	private Double latitude;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String type;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String source;
	@Enumerated(EnumType.STRING)
	private YesNo treatedWithAbate;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date abateTreatmentDate;


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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContaminationType() {
		return contaminationType;
	}

	public void setContaminationType(String contaminationType) {
		this.contaminationType = contaminationType;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public YesNo getTreatedWithAbate() {
		return treatedWithAbate;
	}

	public void setTreatedWithAbate(YesNo treatedWithAbate) {
		this.treatedWithAbate = treatedWithAbate;
	}

	public Date getAbateTreatmentDate() {
		return abateTreatmentDate;
	}

	public void setAbateTreatmentDate(Date abateTreatmentDate) {
		this.abateTreatmentDate = abateTreatmentDate;
	}
}
