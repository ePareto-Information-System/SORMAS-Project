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

package de.symeda.sormas.app.backend.disease;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.facility.Facility;
import de.symeda.sormas.app.util.DiseaseConfigurationCache;

public class DiseaseConfigurationDao extends AbstractAdoDao<DiseaseConfiguration> {

	private Dao<DiseaseFacility, Long> diseaseFacilityDao;

	public DiseaseConfigurationDao(Dao<DiseaseConfiguration, Long> innerDao, Dao<DiseaseFacility, Long> diseaseFacilityDao) {
		super(innerDao);
		this.diseaseFacilityDao = diseaseFacilityDao;
	}

	@Override
	protected Class<DiseaseConfiguration> getAdoClass() {
		return DiseaseConfiguration.class;
	}

	@Override
	public String getTableName() {
		return DiseaseConfiguration.TABLE_NAME;
	}

	public DiseaseConfiguration getDiseaseConfiguration(Disease disease) {
		try {
			QueryBuilder builder = queryBuilder();
			Where where = builder.where();
			where.eq(DiseaseConfiguration.DISEASE, disease);
			return (DiseaseConfiguration) builder.queryForFirst();
		} catch (SQLException e) {
			Log.e(getTableName(), "Could not perform getDiseaseConfiguration");
			throw new RuntimeException(e);
		}
	}

	public List<Facility> getDiseaseFacilities(DiseaseConfiguration diseaseConfiguration) {
		diseaseConfiguration.setFacilities(
				loadDiseaseFacility(diseaseConfiguration.getId()).stream()
						.map(diseaseFacility -> DatabaseHelper.getFacilityDao().queryForId(diseaseFacility.getFacility().getId()))
						.collect(Collectors.toList()));

		List<Facility> facilities = diseaseConfiguration.getFacilities();
		return facilities;
	}

	private List<DiseaseFacility> loadDiseaseFacility(Long diseaseId) {
		try {
			QueryBuilder builder = diseaseFacilityDao.queryBuilder();
			Where where = builder.where();
			where.eq(DiseaseConfiguration.TABLE_NAME + "_id", diseaseId);
			return (List<DiseaseFacility>) builder.query();
		} catch (SQLException e) {
			Log.e(getTableName(), "Could not perform loadUserRoles");
			throw new RuntimeException(e);
		}
	}

	@Override
	public void create(DiseaseConfiguration data) throws SQLException {
		if (data == null)
			return;
		super.create(data);
		DiseaseConfigurationCache.reset();
		if (data.getFacilities() != null) {
			for (Facility facility : data.getFacilities()) {
				int resultRowCount = diseaseFacilityDao.create(new DiseaseFacility(data, facility));
				if (resultRowCount < 1)
					throw new SQLException(
							"Database entry was not created - go back and try again.\n" + "Type: " + DiseaseFacility.class.getSimpleName() + ", Facility-UUID: "
									+ data.getUuid());
			}
		}

	}

	@Override
	protected void update(DiseaseConfiguration data) throws SQLException {
		if (data == null)
			return;

		super.update(data);
		DiseaseConfigurationCache.reset();

		// 1. Delete existing DiseaseFacility
		DeleteBuilder<DiseaseFacility, Long> diseaseFacilityLongDeleteBuilder = diseaseFacilityDao.deleteBuilder();
		diseaseFacilityLongDeleteBuilder.where().eq(DiseaseFacility.DISEASE_CONFIGURATION + "_id", data);
		diseaseFacilityLongDeleteBuilder.delete();

		// 2. Create new DiseaseFacility
		if (data.getFacilities() != null) {
			for (Facility facility : data.getFacilities()) {
				int resultRowCount = diseaseFacilityDao.create(new DiseaseFacility(data, facility));
				if (resultRowCount < 1)
					throw new SQLException(
							"Database entry was not created - go back and try again.\n" + "Type: " + DiseaseFacility.class.getSimpleName() + ", Facility-UUID: "
									+ data.getUuid());
			}
		}
	}
}
