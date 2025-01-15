package de.symeda.sormas.backend.patienttraveldetailsprior;

import de.symeda.sormas.api.utils.TravelLocation;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.riskfactor.RiskFactor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;
@Entity(name = "patienttraveldetailsprior")
public class PatientTravelDetailsPrior extends AbstractDomainObject {
    public static final String I18N_PREFIX = "PatientTravelDetailsPrior";
    private static final long serialVersionUID = 6551672739041643942L;

    public static final String TABLE_NAME = "patienttraveldetailsprior";
    public static final String RISK_FACTOR = "riskFactor";
    public static final String DATE_OF_TRAVEL = "dateOfTravel";
    public static final String PLACE_OF_TRAVEL = "placeOfTravel";
    private String uuid;
    private Date dateOfTravel;
    private TravelLocation placeOfTravel;
    private RiskFactor riskFactor;

    @ManyToOne
    @JoinColumn(nullable = false)
    public RiskFactor getRiskFactor() {
        return riskFactor;
    }
    public void setRiskFactor(RiskFactor riskFactor) {
        this.riskFactor = riskFactor;
    }
    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
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
