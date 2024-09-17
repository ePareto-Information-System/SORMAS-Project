package de.symeda.sormas.app.backend.ebs.signalVerification;

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


public class SignalVerificationDao extends AbstractAdoDao<SignalVerification> {
    public SignalVerificationDao(Dao<SignalVerification, Long> innerDao) {
        super(innerDao);
    }

    @Override
    protected Class<SignalVerification> getAdoClass() {
        return SignalVerification.class;
    }

    @Override
    public String getTableName() {
        return SignalVerification.TABLE_NAME;
    }

    private QueryBuilder<SignalVerification, Long> buildQueryBuilder(EbsCriteria criteria) throws SQLException {
        QueryBuilder<SignalVerification, Long> queryBuilder = queryBuilder();
        List<Where<SignalVerification, Long>> whereStatements = new ArrayList<>();
        Where<SignalVerification, Long> where = queryBuilder.where();
        whereStatements.add(where.eq(AbstractDomainObject.SNAPSHOT, false));

        SignalVerification signalVerification = criteria.getSignalVerification();
        if (signalVerification != null && signalVerification.getVerified() != null) {
            queryBuilder.distinct();
            whereStatements.add(where.eq(Ebs.SIGNAL_VERIFICATION, signalVerification.getVerified()));
        }
        if (!whereStatements.isEmpty()) {
            Where<SignalVerification, Long> whereStatement = where.and(whereStatements.size());
            queryBuilder.setWhere(whereStatement);
        }
        return queryBuilder;
    }

    public List<SignalVerification> queryByCriteria(EbsCriteria criteria, long offset, long limit) {
        try {
            return buildQueryBuilder(criteria).orderBy(SignalVerification.LOCAL_CHANGE_DATE, false).offset(offset).limit(limit).query();
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
