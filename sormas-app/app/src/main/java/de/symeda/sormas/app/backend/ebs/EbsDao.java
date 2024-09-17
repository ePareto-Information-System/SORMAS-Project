package de.symeda.sormas.app.backend.ebs;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.common.AbstractDomainObject;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.ebs.signalVerification.SignalVerification;
import de.symeda.sormas.app.backend.ebs.triaging.Triaging;

public class EbsDao extends AbstractAdoDao<Ebs> {
    public EbsDao(Dao<Ebs, Long> innerDao) {
        super(innerDao);
    }

    public Ebs queryUuid(String uuid) {
        return super.queryUuid(uuid);
    }

    public Ebs queryUuidBasic(String uuid) {
        return super.queryUuid(uuid);
    }

    @Override
    protected Class<Ebs> getAdoClass() {
        return Ebs.class;
    }

    @Override
    public String getTableName() {
        return Ebs.TABLE_NAME;
    }

    private QueryBuilder<Ebs, Long> buildQueryBuilder(EbsCriteria criteria) throws SQLException {
        QueryBuilder<Ebs, Long> queryBuilder = queryBuilder();
        List<Where<Ebs, Long>> whereStatements = new ArrayList<>();
        Where<Ebs, Long> where = queryBuilder.where();
        whereStatements.add(where.eq(AbstractDomainObject.SNAPSHOT, false));

        SignalVerification signalVerification = criteria.getSignalVerification();
        if (signalVerification != null && signalVerification.getVerified() != null) {
            queryBuilder.distinct();
            whereStatements.add(where.eq(Ebs.SIGNAL_VERIFICATION, signalVerification.getVerified()));
        }
    if (!whereStatements.isEmpty()) {
            Where<Ebs, Long> whereStatement = where.and(whereStatements.size());
            queryBuilder.setWhere(whereStatement);
        }
        return queryBuilder;
    }

    public List<Ebs> queryByCriteria(EbsCriteria criteria, long offset, long limit) {
        try {
            return buildQueryBuilder(criteria).orderBy(Ebs.LOCAL_CHANGE_DATE, false).offset(offset).limit(limit).query();
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

    @Override
    public Ebs build() {
        throw new UnsupportedOperationException("Use build(triaging) instead");
    }

    public Ebs build(Triaging triaging) {
        Ebs ebs = super.build();
        SignalVerification signalVerification = new SignalVerification();
        ebs.setTriaging(DatabaseHelper.getTriagingDao().build());
        ebs.setSignalVerification(DatabaseHelper.getSignalVerificationDao().build());
        return ebs;
    }

    public Ebs build(Triaging triaging, Ebs ebs) {
        Ebs newEbs = build(triaging);
        return newEbs;
    }
}
