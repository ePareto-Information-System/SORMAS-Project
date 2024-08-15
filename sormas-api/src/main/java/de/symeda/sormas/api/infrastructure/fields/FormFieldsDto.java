package de.symeda.sormas.api.infrastructure.fields;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.infrastructure.InfrastructureDto;
import de.symeda.sormas.api.utils.DataHelper;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class FormFieldsDto extends InfrastructureDto {

	private static final long serialVersionUID = -7653585175036656542L;
	public static final String I18N_PREFIX = "FormFields";

	public static final String UUID = "uuid";
	public static final String FORM_TYPE = "formType";
	public static final String FIELD_NAME = "fieldName";
	public static final String DESCRIPTION = "description";
	public static final String ACTIVE = "active";

	private FormType formType;
	private String fieldName;
	private String description;
	private Boolean active;


	@Override
	public String getUuid() {
		return super.getUuid();
	}

	@Override
	public void setUuid(String uuid) {
		super.setUuid(uuid);
	}

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

	public static FormFieldsDto build() {
		FormFieldsDto fieldsDto = new FormFieldsDto();
		fieldsDto.setUuid(DataHelper.createUuid());
		return fieldsDto;
	}
}
