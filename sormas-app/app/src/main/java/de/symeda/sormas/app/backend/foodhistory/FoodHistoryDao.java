/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.app.backend.foodhistory;

import com.j256.ormlite.dao.Dao;
import java.util.Date;

import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.common.DaoException;
import de.symeda.sormas.app.backend.common.DatabaseHelper;

public class FoodHistoryDao extends AbstractAdoDao<FoodHistory> {

    public FoodHistoryDao(Dao<FoodHistory, Long> innerDao) {
        super(innerDao);
    }
    @Override
    protected Class<FoodHistory> getAdoClass() {
        return FoodHistory.class;
    }
    @Override
    public String getTableName() {
        return FoodHistory.TABLE_NAME;
    }
    @Override
    public FoodHistory queryUuid(String uuid) {
        FoodHistory data = super.queryUuid(uuid);
        if (data != null) {
            initLazyData(data);
        }
        return data;
    }
    @Override
    public FoodHistory querySnapshotByUuid(String uuid) {
        FoodHistory data = super.querySnapshotByUuid(uuid);
        if (data != null) {
            initLazyData(data);
        }
        return data;
    }
    @Override
    public FoodHistory queryForId(Long id) {
        FoodHistory data = super.queryForId(id);
        if (data != null) {
            initLazyData(data);
        }
        return data;
    }
    private FoodHistory initLazyData(FoodHistory foodHistory) {
        foodHistory.setAffectedPersons(DatabaseHelper.getAffectedPersonDao().getByFoodHistory(foodHistory));
        return foodHistory;
    }

    @Override
    public FoodHistory saveAndSnapshot(FoodHistory ado) throws DaoException {
        FoodHistory snapshot = super.saveAndSnapshot(ado);
        DatabaseHelper.getAffectedPersonDao().saveCollectionWithSnapshot(DatabaseHelper.getAffectedPersonDao().getByFoodHistory(ado),
                ado.getAffectedPersons(), ado);
        return snapshot;
    }

    @Override
    public Date getLatestChangeDate() {
        Date date = super.getLatestChangeDate();
        if (date == null) {
            return null;
        }
        date = DateHelper.getLatestDate(date, DatabaseHelper.getAffectedPersonDao().getLatestChangeDate());
        return date;
    }
}
