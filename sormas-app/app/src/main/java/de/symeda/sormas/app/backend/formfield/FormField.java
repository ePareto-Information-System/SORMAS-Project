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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.utils.AgeGroupUtils;
import de.symeda.sormas.app.backend.common.AbstractDomainObject;

@Entity(name = FormField.TABLE_NAME)
@DatabaseTable(tableName = FormField.TABLE_NAME)
public class FormField extends AbstractDomainObject {

	private static final long serialVersionUID = -7653585175036656526L;

	public static final String TABLE_NAME = "form_fields";
	public static final String I18N_PREFIX = "FORM_FIELD";

	public static final String FORM_TYPE = "formType";
	public static final String FIELD_NAME = "fieldName";
	public static final String DESCRIPTION = "description";
	public static final String ACTIVE = "active";

	@Enumerated(EnumType.STRING)
	private FormType formType;
	@Column
	private String fieldName;
	@Column
	private String description;

	@DatabaseField
	private Boolean active;

	public FormType getFormType() {
		return formType;
	}

	public void setFormType(FormType formType) {
		this.formType = formType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public String getI18nPrefix() {
		return I18N_PREFIX;
	}
}
