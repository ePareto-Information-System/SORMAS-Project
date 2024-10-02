package de.symeda.sormas.app.backend.ebs.ebsAlert;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.common.AbstractDomainObject;


public class EbsAlertDao extends AbstractAdoDao<EbsAlert> {
    public EbsAlertDao(Dao<EbsAlert, Long> innerDao) {
        super(innerDao);
    }

    @Override
    protected Class<EbsAlert> getAdoClass() {
        return EbsAlert.class;
    }

    @Override
    public String getTableName() {
        return EbsAlert.TABLE_NAME;
    }

    private QueryBuilder<EbsAlert, Long> buildQueryBuilder(EbsAlertCriteria criteria) throws SQLException {
        QueryBuilder<EbsAlert, Long> queryBuilder = queryBuilder();
        List<Where<EbsAlert, Long>> whereStatements = new ArrayList<>();
        Where<EbsAlert, Long> where = queryBuilder.where();
        whereStatements.add(where.eq(AbstractDomainObject.SNAPSHOT, false));
        if (criteria.getEbsId() != 0) {
            whereStatements.add(where.eq("ebs_id", criteria.getEbsId()));
        }
        if (!whereStatements.isEmpty()) {
            Where<EbsAlert, Long> whereStatement = where.and(whereStatements.size());
            queryBuilder.setWhere(whereStatement);
        }
        return queryBuilder;
    }

    public List<EbsAlert> queryByCriteria(EbsAlertCriteria criteria, long offset, long limit) {
        try {
            return buildQueryBuilder(criteria).orderBy(EbsAlert.LOCAL_CHANGE_DATE, false).offset(offset).limit(limit).query();
        } catch (SQLException e) {
            Log.e(getTableName(), "Could not perform queryByCriteria on Ebs");
            throw new RuntimeException(e);
        }
    }

    public long countByCriteria(EbsAlertCriteria criteria) {
        try {
            return buildQueryBuilder(criteria).countOf();
        } catch (SQLException e) {
            Log.e(getTableName(), "Could not perform countByCriteria on Event");
            throw new RuntimeException(e);
        }
    }
}
