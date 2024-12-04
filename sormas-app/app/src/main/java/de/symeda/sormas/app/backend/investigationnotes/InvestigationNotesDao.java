package de.symeda.sormas.app.backend.investigationnotes;

import com.j256.ormlite.dao.Dao;

import java.util.Date;

import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.common.DaoException;

public class InvestigationNotesDao extends AbstractAdoDao<InvestigationNotes> {

    public InvestigationNotesDao(Dao<InvestigationNotes, Long> innerDao) {
        super(innerDao);
    }

    @Override
    protected Class<InvestigationNotes> getAdoClass() {
        return InvestigationNotes.class;
    }

    @Override
    public String getTableName() {
        return InvestigationNotes.TABLE_NAME;
    }

    @Override
    public InvestigationNotes queryUuid(String uuid) {
        InvestigationNotes data = super.queryUuid(uuid);
        if (data != null) {
            initLazyData(data);
        }
        return data;
    }

    @Override
    public InvestigationNotes querySnapshotByUuid(String uuid) {
        InvestigationNotes data = super.querySnapshotByUuid(uuid);
        if (data != null) {
            initLazyData(data);
        }
        return data;
    }

    @Override
    public InvestigationNotes queryForId(Long id) {
        InvestigationNotes data = super.queryForId(id);
        if (data != null) {
            initLazyData(data);
        }
        return data;
    }

    private InvestigationNotes initLazyData(InvestigationNotes foodHistory) {
        return foodHistory;
    }

    @Override
    public InvestigationNotes saveAndSnapshot(InvestigationNotes ado) throws DaoException {
        InvestigationNotes snapshot = super.saveAndSnapshot(ado);
        return snapshot;
    }

    @Override
    public Date getLatestChangeDate() {
        Date date = super.getLatestChangeDate();
        if (date == null) {
            return null;
        }
        return date;
    }
}
