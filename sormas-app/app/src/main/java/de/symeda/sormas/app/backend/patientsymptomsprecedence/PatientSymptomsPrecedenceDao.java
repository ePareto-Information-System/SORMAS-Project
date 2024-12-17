package de.symeda.sormas.app.backend.patientsymptomsprecedence;
import com.j256.ormlite.dao.Dao;

import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.riskfactor.RiskFactor;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class PatientSymptomsPrecedenceDao  extends AbstractAdoDao<PatientSymptomsPrecedence> {

    public PatientSymptomsPrecedenceDao(Dao<PatientSymptomsPrecedence, Long> innerDao) {
        super(innerDao);
    }

    @Override
    protected Class<PatientSymptomsPrecedence> getAdoClass() {
        return PatientSymptomsPrecedence.class;
    }

    @Override
    public PatientSymptomsPrecedence build() {
        PatientSymptomsPrecedence patientSymptomsPrecedence = super.build();
        return patientSymptomsPrecedence;
    }

    @Override
    public void deleteCascade(PatientSymptomsPrecedence patientSymptomsPrecedence) throws SQLException {
        super.delete(patientSymptomsPrecedence);
    }

    public List<PatientSymptomsPrecedence> getByRiskFactor(RiskFactor riskFactor) {
        if (riskFactor.isSnapshot()) {
            return querySnapshotsForEq(PatientSymptomsPrecedence.RISK_FACTOR + "_id", riskFactor, PatientSymptomsPrecedence.CHANGE_DATE, false);
        }
        return queryForEq(PatientSymptomsPrecedence.RISK_FACTOR + "_id", riskFactor, PatientSymptomsPrecedence.CHANGE_DATE, false);
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
        return PatientSymptomsPrecedence.TABLE_NAME;
    }

}
