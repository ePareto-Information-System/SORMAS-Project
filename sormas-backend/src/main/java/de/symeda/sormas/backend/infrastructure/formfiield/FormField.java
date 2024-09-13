package de.symeda.sormas.backend.infrastructure.formfiield;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.audit.AuditIgnore;
import de.symeda.sormas.backend.common.InfrastructureAdo;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity(name = FormField.TABLE_NAME)
@AuditIgnore(retainWrites = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FormField extends InfrastructureAdo {
    private static final long serialVersionUID = -7653585175036656542L;

    public static final String TABLE_NAME = "form_fields";

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

    @Enumerated(EnumType.STRING)
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



    public static FormField build(Disease disease, String formType) {
        FormField form = new FormField();
        return form;
    }

    public static FormField build(Disease disease) {
        return build(disease, null);
    }


}
