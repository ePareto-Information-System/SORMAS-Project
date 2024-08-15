package de.symeda.sormas.api.infrastructure.fields;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;

import java.io.Serializable;

public class FormFieldIndexDto implements Serializable {

    public static final String I18N_PREFIX = "Disease";

    public static final String UUID = "uuid";
    public static final String FORM_TYPE = "formType";
    public static final String FIELD_NAME = "fieldName";
    public static final String DESCRIPTION = "description";
    public static final String ACTIVE = "active";
    private String uuid;
    private Disease disease;
	private FormType formType;
    private String fieldName;
    private String description;
    private Boolean active;

    public FormFieldIndexDto(
            String uuid,
            FormType formType,
            String fieldName,
            String description,
            Boolean active) {

            this.uuid = uuid;
            this.formType = formType;
            this.fieldName = fieldName;
            this.description = description;
            this.active = active;
    }
    


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
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

    //TODO: Write clone method

}
