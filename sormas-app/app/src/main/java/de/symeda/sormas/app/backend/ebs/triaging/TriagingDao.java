package de.symeda.sormas.app.backend.ebs.triaging;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.symeda.sormas.api.ebs.SignalVerificationDto;
import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.common.AbstractDomainObject;
import de.symeda.sormas.app.backend.ebs.Ebs;
import de.symeda.sormas.app.backend.ebs.EbsCriteria;
import de.symeda.sormas.app.backend.ebs.signalVerification.SignalVerification;

public class TriagingDao extends AbstractAdoDao<Triaging> {
    public TriagingDao(Dao<Triaging, Long> innerDao) {
        super(innerDao);
    }

    @Override
    protected Class<Triaging> getAdoClass() {
        return Triaging.class;
    }

    @Override
    public String getTableName() {
        return Triaging.TABLE_NAME;
    }

    private QueryBuilder<Triaging, Long> buildQueryBuilder(EbsCriteria criteria) throws SQLException {
        QueryBuilder<Triaging, Long> queryBuilder = queryBuilder();
        List<Where<Triaging, Long>> whereStatements = new ArrayList<>();
        Where<Triaging, Long> where = queryBuilder.where();
        whereStatements.add(where.eq(AbstractDomainObject.SNAPSHOT, false));

        if (criteria.getSignalOutcome() != null) {
            queryBuilder.distinct();
            whereStatements.add(where.eq(Ebs.SIGNAL_VERIFICATION, criteria.getSignalVerification().getVerified()));
        }
    if (!whereStatements.isEmpty()) {
            Where<Triaging, Long> whereStatement = where.and(whereStatements.size());
            queryBuilder.setWhere(whereStatement);
        }
        return queryBuilder;
    }

    public List<Triaging> queryByCriteria(EbsCriteria criteria, long offset, long limit) {
        try {
            return buildQueryBuilder(criteria).orderBy(Triaging.LOCAL_CHANGE_DATE, false).offset(offset).limit(limit).query();
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
