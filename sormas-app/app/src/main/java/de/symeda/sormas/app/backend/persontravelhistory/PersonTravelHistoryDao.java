package de.symeda.sormas.app.backend.persontravelhistory;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.epidata.EpiData;

public class PersonTravelHistoryDao extends AbstractAdoDao<PersonTravelHistory> {

	public PersonTravelHistoryDao(Dao<PersonTravelHistory, Long> innerDao) {
		super(innerDao);
	}

	@Override
	protected Class<PersonTravelHistory> getAdoClass() {
		return PersonTravelHistory.class;
	}

	@Override
	public PersonTravelHistory build() {
		PersonTravelHistory personTravelHistory = super.build();
		return personTravelHistory;
	}

	@Override
	public void deleteCascade(PersonTravelHistory personTravelHistory) throws SQLException {
		super.delete(personTravelHistory);
	}

	public List<PersonTravelHistory> getByEpiData(EpiData epiData) {
		if (epiData.isSnapshot()) {
			return querySnapshotsForEq(PersonTravelHistory.EPI_DATA + "_id", epiData, PersonTravelHistory.CHANGE_DATE, false);
		}

		return queryForEq(PersonTravelHistory.EPI_DATA + "_id", epiData, PersonTravelHistory.CHANGE_DATE, false);
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
		return PersonTravelHistory.TABLE_NAME;
	}
}
