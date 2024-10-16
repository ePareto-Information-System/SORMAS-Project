package de.symeda.sormas.api.infrastructure.forms;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.disease.DiseaseConfigurationDto;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.infrastructure.InfrastructureDto;
import de.symeda.sormas.api.infrastructure.facility.FacilityReferenceDto;
import de.symeda.sormas.api.infrastructure.fields.FormFieldReferenceDto;
import de.symeda.sormas.api.infrastructure.fields.FormFieldsDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DependingOnFeatureType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;
import java.util.Set;

public class FormBuilderDto extends InfrastructureDto {

	private static final long serialVersionUID = -7653585175036656542L;
	public static final String I18N_PREFIX = "FormBuilder";

	public static final String TABLE_NAME = "forms";

	public static final String UUID = "uuid";
	public static final String FORM_TYPE = "formType";
	public static final String DISEASE = "disease";
	public static final String ACTIVE = "active";
//	formFields
	public static final String FORM_FIELDS = "formFields";
	private List<FormFieldReferenceDto> formFields;
	private Boolean active;
	private Disease disease;
	private FormType formType;
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


	@Enumerated(EnumType.STRING)
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

	public List<FormFieldReferenceDto> getFormFields() {
		return formFields;
	}

	public void setFormFields(List<FormFieldReferenceDto> formFields) {
		this.formFields = formFields;
	}

	public static FormBuilderDto build() {
		FormBuilderDto formBuilderDto = new FormBuilderDto();
		formBuilderDto.setUuid(DataHelper.createUuid());
		return formBuilderDto;
	}


}
