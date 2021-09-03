package de.symeda.sormas.app.backend.sample;

import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.NonUniqueResultException;

import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.common.AbstractDomainObject;
import de.symeda.sormas.app.backend.common.DaoException;
import de.symeda.sormas.app.backend.common.DatabaseHelper;

public class FieldSampleIdDao extends AbstractAdoDao<FieldSampleId> {


    public FieldSampleIdDao(Dao<FieldSampleId, Long> innerDao) {
        super(innerDao);
    }

    @Override
    protected Class<FieldSampleId> getAdoClass() {
        return FieldSampleId.class;
    }

    @Override
    public FieldSampleId build() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTableName() {
        return FieldSampleId.TABLE_NAME;
    }

    public void deleteFieldSampleIdAndAllDependingEntities(String fieldSampleId) throws SQLException {
        List<FieldSampleId> fieldSampleIds = DatabaseHelper.getFieldSampleIdtDao().queryByFieldSampleIds(fieldSampleId);

        for (FieldSampleId newFieldSampleId : fieldSampleIds) {
            DatabaseHelper.getFieldSampleIdtDao().deleteCascade(newFieldSampleId);
        }

    }

    public List<FieldSampleId> queryByFieldSampleIds(String fieldSampleId) {

        try {

            return queryBuilder().where().eq(FieldSampleId.FIELD_SAMPLE_ID, fieldSampleId).and().eq(AbstractDomainObject.SNAPSHOT, false).query();
        } catch (SQLException e) {
            Log.e(getTableName(), "Could not perform queryByFieldSampleId");
            throw new RuntimeException(e);
        }
    }

    public FieldSampleId queryByFieldSampleId(String fieldSampleId) {

        try {

            List<FieldSampleId> results =
                    queryBuilder().where().eq(FieldSampleId.FIELD_SAMPLE_ID, fieldSampleId).and().eq(AbstractDomainObject.SNAPSHOT, false).query();
            if (results.size() == 0) {
                return null;
            } else if (results.size() == 1) {
                return results.get(0);
            } else {
                Log.e(getTableName(), "Found multiple results for fieldSampleId: " + fieldSampleId);
                throw new NonUniqueResultException("Found multiple results for fieldSampleId: " + fieldSampleId);
            }
        } catch (SQLException e) {
            Log.e(getTableName(), "Could not perform queryByFieldSampleId");
            throw new RuntimeException(e);
        }
    }

    public void saveFieldSampleId(String fieldSampleId) {

        try {
            Log.e("Saving data...:", "Saved");
            FieldSampleId f = new FieldSampleId();
            f.setFieldSampleId(fieldSampleId);
//            super.saveAndSnapshot(f);
            create(f);
//        } catch (DaoException | SQLException e) {
        } catch (SQLException e) {
            Log.e(getTableName(), "Could not perform saveFieldSampleId");
            throw new RuntimeException(e);
        }
    }

    public boolean isFieldSampleId(String fieldSampleId) {

        try {
            List<FieldSampleId> fieldSampleIdList = queryBuilder().query();
            Log.d("fieldSampleIdList size is: ", String.valueOf(fieldSampleIdList));

            List<FieldSampleId> fieldSampleIds = queryBuilder().where().eq(FieldSampleId.FIELD_SAMPLE_ID, fieldSampleId).and().eq(AbstractDomainObject.SNAPSHOT, false).query();
            Log.d("fieldSampleIds value is: ", String.valueOf(fieldSampleIds));
            Log.d("fieldSampleIds size is: ", String.valueOf(fieldSampleIds.size()));
            return fieldSampleIds.size() != 0;

//            return queryBuilder().where().eq(FieldSampleId.FIELD_SAMPLE_ID, fieldSampleId).and().eq(AbstractDomainObject.SNAPSHOT, false).query().size() == 0;
        } catch (SQLException e) {
            Log.e(getTableName(), "Could not perform isFieldSampleId");
            throw new RuntimeException(e);
        }
    }
}