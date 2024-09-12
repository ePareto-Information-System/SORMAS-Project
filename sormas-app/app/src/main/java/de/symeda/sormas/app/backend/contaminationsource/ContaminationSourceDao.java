package de.symeda.sormas.app.backend.contaminationsource;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import de.symeda.sormas.api.activityascase.ActivityAsCaseType;
import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.epidata.EpiData;

public class ContaminationSourceDao extends AbstractAdoDao<ContaminationSource> {

	public ContaminationSourceDao(Dao<ContaminationSource, Long> innerDao) {
		super(innerDao);
	}

	@Override
	protected Class<ContaminationSource> getAdoClass() {
		return ContaminationSource.class;
	}

	@Override
	public ContaminationSource build() {
		ContaminationSource contaminationSource = super.build();
		contaminationSource.setContaminationType("WATER");
		return contaminationSource;
	}


	@Override
	public void deleteCascade(ContaminationSource contaminationSource) throws SQLException {
		super.delete(contaminationSource);
	}

	public List<ContaminationSource> getByEpiData(EpiData epiData) {
		if (epiData.isSnapshot()) {
			return querySnapshotsForEq(ContaminationSource.EPI_DATA + "_id", epiData, ContaminationSource.CHANGE_DATE, false);
		}

		return queryForEq(ContaminationSource.EPI_DATA + "_id", epiData, ContaminationSource.CHANGE_DATE, false);
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
		return ContaminationSource.TABLE_NAME;
	}
}
