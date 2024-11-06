package de.symeda.sormas.app.backend.ebs;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.symeda.sormas.api.ebs.EbsTriagingDecision;
import de.symeda.sormas.api.ebs.SignalCategory;
import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.common.AbstractDomainObject;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.ebs.signalVerification.SignalVerification;
import de.symeda.sormas.app.backend.ebs.triaging.Triaging;
import de.symeda.sormas.app.backend.location.Location;
import de.symeda.sormas.app.backend.region.Community;
import de.symeda.sormas.app.backend.region.District;
import de.symeda.sormas.app.backend.region.Region;

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
        QueryBuilder<SignalVerification, Long> signalVerificationLongQueryBuilder = DatabaseHelper.getSignalVerificationDao().queryBuilder();
        QueryBuilder<Triaging, Long> triagingLongQueryBuilder = DatabaseHelper.getTriagingDao().queryBuilder();
        QueryBuilder<Region, Long> regionLongQueryBuilder = DatabaseHelper.getRegionDao().queryBuilder();
        QueryBuilder<Location, Long> locationLongQueryBuilder = DatabaseHelper.getLocationDao().queryBuilder();
        QueryBuilder<District, Long> districtLongQueryBuilder = DatabaseHelper.getDistrictDao().queryBuilder();
        QueryBuilder<Community, Long> communityLongQueryBuilder = DatabaseHelper.getCommunityDao().queryBuilder();
        whereStatements.add(where.eq(AbstractDomainObject.SNAPSHOT, false));

        if (criteria.getSignalOutcome() != null) {
            if (criteria.getSignalOutcome() == SignalOutcome.EVENT) {
                signalVerificationLongQueryBuilder.where().eq(SignalVerification.VERIFIED, SignalOutcome.EVENT);
                queryBuilder.leftJoin(signalVerificationLongQueryBuilder);
            }
            else if (criteria.getSignalOutcome() == SignalOutcome.NON_EVENT){
                signalVerificationLongQueryBuilder.where().eq(SignalVerification.VERIFIED, SignalOutcome.NON_EVENT).or().isNull("verified");
                queryBuilder.leftJoin(signalVerificationLongQueryBuilder);
            }
        }

        if (criteria.getSignalOutcome() == null) {
            signalVerificationLongQueryBuilder
                    .where()
                    .in(SignalVerification.VERIFIED, SignalOutcome.NON_EVENT, SignalOutcome.EVENT)
                    .or()
                    .isNull(SignalVerification.VERIFIED);
            queryBuilder.leftJoin(signalVerificationLongQueryBuilder);
        }

        if (criteria.getSourceInformation() != null){
            whereStatements.add(where.eq(Ebs.SOURCE_INFORMATION, criteria.getSourceInformation()));
        }
        if (criteria.getReportDateTime() != null){
            whereStatements.add(where.between(Ebs.REPORT_DATE_TIME, DateHelper.getStartOfDay(criteria.getReportDateTime()),
                    DateHelper.getEndOfDay(criteria.getReportDateTime())));
        }
        if (criteria.getTriagingDecision() != null){
            if (criteria.getTriagingDecision() == EbsTriagingDecision.VERIFY) {
                triagingLongQueryBuilder.where().eq(Triaging.TRIAGING_DECISION, EbsTriagingDecision.VERIFY);
                queryBuilder.leftJoin(triagingLongQueryBuilder);
            }else if (criteria.getTriagingDecision() == EbsTriagingDecision.DISCARD) {
                triagingLongQueryBuilder.where().eq(Triaging.TRIAGING_DECISION, EbsTriagingDecision.DISCARD);
                queryBuilder.leftJoin(triagingLongQueryBuilder);
            }else if (criteria.getTriagingDecision() == EbsTriagingDecision.MORE_INFORMATION) {
                triagingLongQueryBuilder.where().eq(Triaging.TRIAGING_DECISION, EbsTriagingDecision.MORE_INFORMATION);
                queryBuilder.leftJoin(triagingLongQueryBuilder);
            }
        }
        if (criteria.getSignalCategory() != null) {
            if (criteria.getSignalCategory() == SignalCategory.HUMAN) {
                triagingLongQueryBuilder.where().eq(Triaging.SIGNAL_CATEGORY, SignalCategory.HUMAN);
                queryBuilder.leftJoin(triagingLongQueryBuilder);
            } if (criteria.getSignalCategory() == SignalCategory.ANIMAL) {
                triagingLongQueryBuilder.where().eq(Triaging.SIGNAL_CATEGORY, SignalCategory.ANIMAL);
                queryBuilder.leftJoin(triagingLongQueryBuilder);
            } if (criteria.getSignalCategory() == SignalCategory.ENVIRONMENT) {
                triagingLongQueryBuilder.where().eq(Triaging.SIGNAL_CATEGORY, SignalCategory.ENVIRONMENT);
                queryBuilder.leftJoin(triagingLongQueryBuilder);
            }if (criteria.getSignalCategory() == SignalCategory.POE) {
                triagingLongQueryBuilder.where().eq(Triaging.SIGNAL_CATEGORY, SignalCategory.POE);
                queryBuilder.leftJoin(triagingLongQueryBuilder);
            }
        }
        if (criteria.getTriageDate() != null){
            triagingLongQueryBuilder.where().between(Triaging.DATE_OF_DECISION,DateHelper.getStartOfDay(criteria.getTriageDate()),DateHelper.getEndOfDay(criteria.getTriageDate()));
        }

        if (criteria.getRegion() != null) {
            locationLongQueryBuilder.where().eq(Location.REGION + "_id", criteria.getRegion().getId());
            queryBuilder.leftJoin(locationLongQueryBuilder);
        }
        if (criteria.getDistrict() != null) {
            locationLongQueryBuilder.where().eq(Location.DISTRICT + "_id", criteria.getDistrict().getId());
        }
        if (criteria.getCommunity() != null) {
            locationLongQueryBuilder.where().eq(Location.COMMUNITY + "_id", criteria.getCommunity().getId());
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
    public Date getLatestChangeDate() {
        Date date = super.getLatestChangeDate();
        if (date == null) {
            return null;
        }

        Date locationDate = getLatestChangeDateJoin(Location.TABLE_NAME, Ebs.EBS_LOCATION);
        if (locationDate != null && locationDate.after(date)) {
            date = locationDate;
        }

        return date;
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
