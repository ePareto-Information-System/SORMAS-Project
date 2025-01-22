package de.symeda.sormas.app.backend.patienttraveldetailsduring;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.riskfactor.RiskFactor;

public class PatientTravelDetailsDuringDao extends AbstractAdoDao<PatientTravelDetailsDuring> {

    public PatientTravelDetailsDuringDao(Dao<PatientTravelDetailsDuring, Long> innerDao) {
        super(innerDao);
    }

    @Override
    protected Class<PatientTravelDetailsDuring> getAdoClass() {
        return PatientTravelDetailsDuring.class;
    }

    @Override
    public PatientTravelDetailsDuring build() {
        PatientTravelDetailsDuring patientTravelDetailsDuring = super.build();
        return patientTravelDetailsDuring;
    }

    @Override
    public void deleteCascade(PatientTravelDetailsDuring patientTravelDetailsDuring) throws SQLException {
        super.delete(patientTravelDetailsDuring);
    }

    public List<PatientTravelDetailsDuring> getByRiskFactor(RiskFactor riskFactor) {
        if (riskFactor.isSnapshot()) {
            return querySnapshotsForEq(PatientTravelDetailsDuring.RISK_FACTOR + "_id", riskFactor, PatientTravelDetailsDuring.CHANGE_DATE, false);
        }
        return queryForEq(PatientTravelDetailsDuring.RISK_FACTOR + "_id", riskFactor, PatientTravelDetailsDuring.CHANGE_DATE, false);
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
        return PatientTravelDetailsDuring.TABLE_NAME;
    }
}
