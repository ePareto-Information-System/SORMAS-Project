package de.symeda.sormas.app.backend.patienttraveldetailsprior;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

import de.symeda.sormas.app.backend.common.EmbeddedAdo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.riskfactor.RiskFactor;
import de.symeda.sormas.api.utils.TravelLocation;

@Entity(name = PatientTravelDetailsPrior.TABLE_NAME)
@DatabaseTable(tableName = PatientTravelDetailsPrior.TABLE_NAME)
@EmbeddedAdo(parentAccessor = PatientTravelDetailsPrior.RISK_FACTOR)

public class PatientTravelDetailsPrior extends PseudonymizableAdo {

    public static final String I18N_PREFIX = "PatientTravelDetailsPrior";
    private static final long serialVersionUID = 6551672739041643942L;

    public static final String TABLE_NAME = "patienttraveldetailsprior";
    public static final String RISK_FACTOR = "riskFactor";

    @DatabaseField(dataType = DataType.DATE_LONG)
    private Date dateOfTravel;
    @Enumerated(EnumType.STRING)
    private TravelLocation placeOfTravel;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private RiskFactor riskFactor;

    @Override
    public String getI18nPrefix() {
        return I18N_PREFIX;
    }

    public RiskFactor getRiskFactor() {
        return riskFactor;
    }
    public void setRiskFactor(RiskFactor riskFactor) {
        this.riskFactor = riskFactor;
    }

    public Date getDateOfTravel() {
        return dateOfTravel;
    }

    public void setDateOfTravel(Date dateOfTravel) {
        this.dateOfTravel = dateOfTravel;
    }

    public TravelLocation getPlaceOfTravel() {
        return placeOfTravel;
    }

    public void setPlaceOfTravel(TravelLocation placeOfTravel) {
        this.placeOfTravel = placeOfTravel;
    }
}
