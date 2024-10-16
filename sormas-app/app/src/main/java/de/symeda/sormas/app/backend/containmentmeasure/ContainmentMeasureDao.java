package de.symeda.sormas.app.backend.containmentmeasure;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.epidata.EpiData;

public class ContainmentMeasureDao extends AbstractAdoDao<ContainmentMeasure> {

	public ContainmentMeasureDao(Dao<ContainmentMeasure, Long> innerDao) {
		super(innerDao);
	}

	@Override
	protected Class<ContainmentMeasure> getAdoClass() {
		return ContainmentMeasure.class;
	}

	@Override
	public ContainmentMeasure build() {
		ContainmentMeasure contaminationSource = super.build();
		return contaminationSource;
	}


	@Override
	public void deleteCascade(ContainmentMeasure contaminationSource) throws SQLException {
		super.delete(contaminationSource);
	}

	public List<ContainmentMeasure> getByEpiData(EpiData epiData) {
		if (epiData.isSnapshot()) {
			return querySnapshotsForEq(ContainmentMeasure.EPI_DATA + "_id", epiData, ContainmentMeasure.CHANGE_DATE, false);
		}

		return queryForEq(ContainmentMeasure.EPI_DATA + "_id", epiData, ContainmentMeasure.CHANGE_DATE, false);
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
		return ContainmentMeasure.TABLE_NAME;
	}
}
