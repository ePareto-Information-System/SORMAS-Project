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

package de.symeda.sormas.app.backend.formbuilder;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.app.backend.common.AbstractAdoDao;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.formfield.FormField;

public class FormBuilderDao extends AbstractAdoDao<FormBuilder> {

	private Dao<FormBuilderFormField, Long> formBuilderFormFieldDao;

	public FormBuilderDao(Dao<FormBuilder, Long> innerDao, Dao<FormBuilderFormField, Long> formBuilderFormFieldDao) {
		super(innerDao);
		this.formBuilderFormFieldDao = formBuilderFormFieldDao;
	}

	public FormBuilderDao(Dao<FormBuilder, Long> innerDao) {
		super(innerDao);
	}

	@Override
	protected Class<FormBuilder> getAdoClass() {
		return FormBuilder.class;
	}

	@Override
	public String getTableName() {
		return FormBuilder.TABLE_NAME;
	}

	public FormBuilder getFormBuilder(FormType formType, Disease disease) {
		try {
			QueryBuilder builder = queryBuilder();
			Where where = builder.where();
			where.eq(FormBuilder.FORM_TYPE, formType);
			where.and();
			where.eq(FormBuilder.DISEASE, disease);
			return (FormBuilder) builder.queryForFirst();
		} catch (SQLException e) {
			Log.e(getTableName(), "Could not perform getFormBuilder");
			throw new RuntimeException(e);
		}
	}

	public List<FormField> getFormBuilderFormFields(FormBuilder formBuilder) {
		formBuilder.setFormFields(
				loadFormBuilderFormField(formBuilder.getId()).stream()
						.map(diseaseFormField -> DatabaseHelper.getFormFieldDao().queryForId(diseaseFormField.getFormField().getId()))
						.collect(Collectors.toList()));

		List<FormField> formFields = formBuilder.getFormFields();
		return formFields;
	}

	private List<FormBuilderFormField> loadFormBuilderFormField(Long formBuilderId) {
		try {
			QueryBuilder builder = formBuilderFormFieldDao.queryBuilder();
			Where where = builder.where();
			where.eq("form_id", formBuilderId);
			return (List<FormBuilderFormField>) builder.query();
		} catch (SQLException e) {
			Log.e(getTableName(), "Could not perform loadUserRoles");
			throw new RuntimeException(e);
		}
	}

	@Override
	public void create(FormBuilder data) throws SQLException {
		if (data == null)
			return;
		super.create(data);
		if (data.getFormFields() != null) {
			for (FormField formField : data.getFormFields()) {
				int resultRowCount = formBuilderFormFieldDao.create(new FormBuilderFormField(data, formField));
				if (resultRowCount < 1)
					throw new SQLException(
							"Database entry was not created - go back and try again.\n" + "Type: " + FormBuilderFormField.class.getSimpleName() + ", FormField-UUID: "
									+ data.getUuid());
			}
		}
//		FormBuilderCache.reset();
	}

	@Override
	protected void update(FormBuilder data) throws SQLException {
		if (data == null)
			return;
		super.update(data);

		// 1. Delete existing FormBuilderFormField
		DeleteBuilder<FormBuilderFormField, Long> diseaseFormFieldLongDeleteBuilder = formBuilderFormFieldDao.deleteBuilder();
		diseaseFormFieldLongDeleteBuilder.where().eq("form_id", data);
		diseaseFormFieldLongDeleteBuilder.delete();

		// 2. Create new FormBuilderFormField
		if (data.getFormFields() != null) {
			for (FormField formField : data.getFormFields()) {
				int resultRowCount = formBuilderFormFieldDao.create(new FormBuilderFormField(data, formField));
				if (resultRowCount < 1)
					throw new SQLException(
							"Database entry was not created - go back and try again.\n" + "Type: " + FormBuilderFormField.class.getSimpleName() + ", FormField-UUID: "
									+ data.getUuid());
			}
		}
	}
}
