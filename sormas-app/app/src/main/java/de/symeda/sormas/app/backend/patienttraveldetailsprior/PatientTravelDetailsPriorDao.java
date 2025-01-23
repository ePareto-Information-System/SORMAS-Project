package de.symeda.sormas.app.backend.patienttraveldetailsprior;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.riskfactor.RiskFactor;

public class PatientTravelDetailsPriorDao extends AbstractAdoDao<PatientTravelDetailsPrior> {

    public PatientTravelDetailsPriorDao(Dao<PatientTravelDetailsPrior, Long> innerDao) {
        super(innerDao);
    }

    @Override
    protected Class<PatientTravelDetailsPrior> getAdoClass() {
        return PatientTravelDetailsPrior.class;
    }

    @Override
    public PatientTravelDetailsPrior build() {
        PatientTravelDetailsPrior patientTravelDetailsPrior = super.build();
        return patientTravelDetailsPrior;
    }

    @Override
    public void deleteCascade(PatientTravelDetailsPrior patientTravelDetailsPrior) throws SQLException {
        super.delete(patientTravelDetailsPrior);
    }

    public List<PatientTravelDetailsPrior> getByRiskFactor(RiskFactor riskFactor) {
        if (riskFactor.isSnapshot()) {
            return querySnapshotsForEq(PatientTravelDetailsPrior.RISK_FACTOR + "_id", riskFactor, PatientTravelDetailsPrior.CHANGE_DATE, false);
        }
        return queryForEq(PatientTravelDetailsPrior.RISK_FACTOR + "_id", riskFactor, PatientTravelDetailsPrior.CHANGE_DATE, false);
    }

    @Override
    public Date getLatestChangeDate() {
        Date date = super.getLatestChangeDate();
        if (date == null) {
            return null;
        }
        return date;
    }

    @Override
    public String getTableName() {
        return PatientTravelDetailsPrior.TABLE_NAME;
    }
}
