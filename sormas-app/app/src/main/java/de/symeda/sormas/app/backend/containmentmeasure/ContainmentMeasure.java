package de.symeda.sormas.app.backend.containmentmeasure;

import static de.symeda.sormas.api.utils.FieldConstraints.CHARACTER_LIMIT_DEFAULT;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.app.backend.common.EmbeddedAdo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.epidata.EpiData;

@Entity(name = ContainmentMeasure.TABLE_NAME)
@DatabaseTable(tableName = ContainmentMeasure.TABLE_NAME)
@EmbeddedAdo(parentAccessor = ContainmentMeasure.EPI_DATA)
public class ContainmentMeasure extends PseudonymizableAdo {

	private static final long serialVersionUID = -5570515874416024605L;

	public static final String TABLE_NAME = "containmentmeasures";
	public static final String I18N_PREFIX = "ContaminationSource";
	public static final String EPI_DATA = "epiData";

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private EpiData epiData;
	@Column(length = CHARACTER_LIMIT_DEFAULT)
	private String locationOfWorm;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateWormDetectedEmergence;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateWormDetectBySupervisor;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateConfirmed;
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date dateOfGuineaWormExpelled;
	@Enumerated(EnumType.STRING)
	private YesNo regularBandaging;
	@Enumerated(EnumType.STRING)
	private YesNo completelyExtracted;

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
