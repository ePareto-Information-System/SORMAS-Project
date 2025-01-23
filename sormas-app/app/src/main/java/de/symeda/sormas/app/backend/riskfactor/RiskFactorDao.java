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

package de.symeda.sormas.app.backend.riskfactor;

import com.j256.ormlite.dao.Dao;

import java.util.Date;

import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.common.DaoException;
import de.symeda.sormas.app.backend.common.DatabaseHelper;

public class RiskFactorDao extends AbstractAdoDao<RiskFactor> {

	public RiskFactorDao(Dao<RiskFactor, Long> innerDao) {
		super(innerDao);
	}

	@Override
	protected Class<RiskFactor> getAdoClass() {
		return RiskFactor.class;
	}

	@Override
	public String getTableName() {
		return RiskFactor.TABLE_NAME;
	}

	@Override
	public RiskFactor queryUuid(String uuid) {
		RiskFactor data = super.queryUuid(uuid);
		if (data != null) {
			initLazyData(data);
		}
		return data;
	}

	@Override
	public RiskFactor querySnapshotByUuid(String uuid) {
		RiskFactor data = super.querySnapshotByUuid(uuid);
		if (data != null) {
			initLazyData(data);
		}
		return data;
	}

	@Override
	public RiskFactor queryForId(Long id) {
		RiskFactor data = super.queryForId(id);
		if (data != null) {
			initLazyData(data);
		}
		return data;
	}

	private RiskFactor initLazyData(RiskFactor riskFactor) {
		riskFactor.setPatientSymptomsPrecedences(DatabaseHelper.getPatientSymptomsPrecedenceDao().getByRiskFactor(riskFactor));
		riskFactor.setPatientTravelDetailsDurings(DatabaseHelper.getPatientTravelDetailsDuringDao().getByRiskFactor(riskFactor));
		riskFactor.setPatientTravelDetailsPriors(DatabaseHelper.getPatientTravelDetailsPriorDao().getByRiskFactor(riskFactor));
		return riskFactor;
	}

	@Override
	public RiskFactor saveAndSnapshot(RiskFactor ado) throws DaoException {

		RiskFactor snapshot = super.saveAndSnapshot(ado);
		DatabaseHelper.getPatientSymptomsPrecedenceDao().saveCollectionWithSnapshot(DatabaseHelper.getPatientSymptomsPrecedenceDao().getByRiskFactor(ado),
				ado.getPatientSymptomsPrecedences(), ado);

		DatabaseHelper.getPatientTravelDetailsDuringDao().saveCollectionWithSnapshot(DatabaseHelper.getPatientTravelDetailsDuringDao().getByRiskFactor(ado),
				ado.getPatientTravelDetailsDurings(), ado);

		DatabaseHelper.getPatientTravelDetailsPriorDao().saveCollectionWithSnapshot(DatabaseHelper.getPatientTravelDetailsPriorDao().getByRiskFactor(ado),
				ado.getPatientTravelDetailsPriors(), ado);

		return snapshot;
	}

	@Override
	public Date getLatestChangeDate() {
		Date date = super.getLatestChangeDate();
		if (date == null) {
			return null;
		}
		date = DateHelper.getLatestDate(date, DatabaseHelper.getPatientSymptomsPrecedenceDao().getLatestChangeDate());
		date = DateHelper.getLatestDate(date, DatabaseHelper.getPatientTravelDetailsDuringDao().getLatestChangeDate());
		date = DateHelper.getLatestDate(date, DatabaseHelper.getPatientTravelDetailsPriorDao().getLatestChangeDate());

		return date;
	}
}
