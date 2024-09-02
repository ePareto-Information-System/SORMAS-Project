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

package de.symeda.sormas.app.backend.formfield;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.util.DiseaseConfigurationCache;

public class FormFieldDao extends AbstractAdoDao<FormField> {

	public FormFieldDao(Dao<FormField, Long> innerDao) {
		super(innerDao);
	}

	@Override
	protected Class<FormField> getAdoClass() {
		return FormField.class;
	}

	@Override
	public String getTableName() {
		return FormField.TABLE_NAME;
	}

	public FormField getDiseaseConfiguration(FormType formType) {
		try {
			QueryBuilder builder = queryBuilder();
			Where where = builder.where();
			where.eq(FormField.FORM_TYPE, formType);
			return (FormField) builder.queryForFirst();
		} catch (SQLException e) {
			Log.e(getTableName(), "Could not perform getDiseaseConfiguration");
			throw new RuntimeException(e);
		}
	}

	@Override
	public void create(FormField data) throws SQLException {
		super.create(data);
//		DiseaseConfigurationCache.reset();
	}

	@Override
	protected void update(FormField data) throws SQLException {
		super.update(data);
//		DiseaseConfigurationCache.reset();
	}
}