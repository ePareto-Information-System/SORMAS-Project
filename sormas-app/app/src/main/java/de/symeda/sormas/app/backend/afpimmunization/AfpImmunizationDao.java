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

package de.symeda.sormas.app.backend.afpimmunization;

import com.j256.ormlite.dao.Dao;

import java.util.Date;

import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.common.DaoException;

public class AfpImmunizationDao extends AbstractAdoDao<AfpImmunization> {

	public AfpImmunizationDao(Dao<AfpImmunization, Long> innerDao) {
		super(innerDao);
	}

	@Override
	protected Class<AfpImmunization> getAdoClass() {
		return AfpImmunization.class;
	}

	@Override
	public String getTableName() {
		return AfpImmunization.TABLE_NAME;
	}

	@Override
	public AfpImmunization queryUuid(String uuid) {
		AfpImmunization data = super.queryUuid(uuid);
		if (data != null) {
			initLazyData(data);
		}
		return data;
	}

	@Override
	public AfpImmunization querySnapshotByUuid(String uuid) {
		AfpImmunization data = super.querySnapshotByUuid(uuid);
		if (data != null) {
			initLazyData(data);
		}
		return data;
	}

	@Override
	public AfpImmunization queryForId(Long id) {
		AfpImmunization data = super.queryForId(id);
		if (data != null) {
			initLazyData(data);
		}
		return data;
	}

	private AfpImmunization initLazyData(AfpImmunization afpImmunization) {
		return afpImmunization;
	}

	@Override
	public AfpImmunization saveAndSnapshot(AfpImmunization ado) throws DaoException {

		AfpImmunization snapshot = super.saveAndSnapshot(ado);
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
