package de.symeda.sormas.app.backend.ebs.riskAssessment;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.common.AbstractDomainObject;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.backend.ebs.EbsCriteria;
import de.symeda.sormas.app.backend.ebs.signalVerification.SignalVerification;


public class RiskAssessmentDao extends AbstractAdoDao<RiskAssessment> {
    public RiskAssessmentDao(Dao<RiskAssessment, Long> innerDao) {
        super(innerDao);
    }

    @Override
    protected Class<RiskAssessment> getAdoClass() {
        return RiskAssessment.class;
    }

    @Override
    public String getTableName() {
        return RiskAssessment.TABLE_NAME;
    }

    private QueryBuilder<RiskAssessment, Long> buildQueryBuilder(EbsCriteria criteria) throws SQLException {
        QueryBuilder<RiskAssessment, Long> queryBuilder = queryBuilder();
        List<Where<RiskAssessment, Long>> whereStatements = new ArrayList<>();
        Where<RiskAssessment, Long> where = queryBuilder.where();
        whereStatements.add(where.eq(AbstractDomainObject.SNAPSHOT, false));

        SignalVerification signalVerification = criteria.getSignalVerification();
        if (signalVerification != null && signalVerification.getVerified() != null) {
            queryBuilder.distinct();
            whereStatements.add(where.eq(Ebs.SIGNAL_VERIFICATION, signalVerification.getVerified()));
        }
        if (!whereStatements.isEmpty()) {
            Where<RiskAssessment, Long> whereStatement = where.and(whereStatements.size());
            queryBuilder.setWhere(whereStatement);
        }
        return queryBuilder;
    }

    public List<RiskAssessment> queryByCriteria(EbsCriteria criteria, long offset, long limit) {
        try {
            return buildQueryBuilder(criteria).orderBy(RiskAssessment.LOCAL_CHANGE_DATE, false).offset(offset).limit(limit).query();
        } catch (SQLException e) {
            Log.e(getTableName(), "Could not perform queryByCriteria on Ebs");
            throw new RuntimeException(e);
        }
    }

    public long countByCriteria(EbsCriteria criteria) {
        try {
            return buildQueryBuilder(criteria).countOf();
        } catch (SQLException e) {
            Log.e(getTableName(), "Could not perform countByCriteria on Event");
            throw new RuntimeException(e);
        }
    }
}
