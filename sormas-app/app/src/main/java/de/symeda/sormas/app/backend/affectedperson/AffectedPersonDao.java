package de.symeda.sormas.app.backend.affectedperson;

import com.j256.ormlite.dao.Dao;

import java.util.List;

import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.foodhistory.FoodHistory;
import java.sql.SQLException;
import java.util.Date;

public class AffectedPersonDao extends AbstractAdoDao<AffectedPerson> {

    public AffectedPersonDao(Dao<AffectedPerson, Long> innerDao) {
        super(innerDao);
    }

    @Override
    protected Class<AffectedPerson> getAdoClass() {
        return AffectedPerson.class;
    }

    @Override
    public AffectedPerson build() {
        AffectedPerson affectedPerson = super.build();
        return affectedPerson;
    }

    @Override
    public void deleteCascade(AffectedPerson affectedPerson) throws SQLException {
        super.delete(affectedPerson);
    }

    public List<AffectedPerson> getByFoodHistory(FoodHistory foodHistory) {
        if (foodHistory.isSnapshot()) {
            return querySnapshotsForEq(AffectedPerson.FOOD_HISTORY + "_id", foodHistory, AffectedPerson.CHANGE_DATE, false);
        }
        return queryForEq(AffectedPerson.FOOD_HISTORY + "_id", foodHistory, AffectedPerson.CHANGE_DATE, false);
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
        return AffectedPerson.TABLE_NAME;
    }
}
