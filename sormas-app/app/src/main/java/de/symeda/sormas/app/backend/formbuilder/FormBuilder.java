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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.audit.AuditIgnore;
import de.symeda.sormas.app.backend.common.AbstractDomainObject;
import de.symeda.sormas.app.backend.facility.Facility;
import de.symeda.sormas.app.backend.formfield.FormField;
import de.symeda.sormas.app.util.MetaProperty;

@Entity(name = FormBuilder.TABLE_NAME)
@DatabaseTable(tableName = FormBuilder.TABLE_NAME)
public class FormBuilder extends AbstractDomainObject {

	private static final long serialVersionUID = -7653585175036656526L;


	public static final String I18N_PREFIX = "FormBuilder";

	public static final String TABLE_NAME = "forms";

	public static final String UUID = "uuid";
	public static final String FORM_TYPE = "formType";
	public static final String DISEASE = "disease";
	public static final String ACTIVE = "active";

//	@DatabaseField
//	private String uuid;

	@Enumerated(EnumType.STRING)
	private FormType formType;

	@DatabaseField
	private Disease disease;

	private Boolean active;
	private List<FormField> formFields = null;


//	@Override
//	public String getUuid() {
//		return uuid;
//	}
//
//	@Override
//	public void setUuid(String uuid) {
//		this.uuid = uuid;
//	}

	public FormType getFormType() {
		return formType;
	}

	public void setFormType(FormType formType) {
		this.formType = formType;
	}

	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
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

	@MetaProperty
	public List<FormField> getFormFields() {
		return formFields;
	}

	public void setFormFields(List<FormField> formFields) {
		this.formFields = formFields;
	}
}
