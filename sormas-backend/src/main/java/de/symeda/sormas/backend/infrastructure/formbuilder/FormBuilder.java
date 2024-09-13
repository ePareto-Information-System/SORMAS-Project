package de.symeda.sormas.backend.infrastructure.formbuilder;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.audit.AuditIgnore;
import de.symeda.sormas.backend.common.InfrastructureAdo;
import de.symeda.sormas.backend.disease.DiseaseConfiguration;
import de.symeda.sormas.backend.infrastructure.formfiield.FormField;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Set;

@Entity(name = FormBuilder.TABLE_NAME)
@AuditIgnore(retainWrites = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FormBuilder extends InfrastructureAdo {
    private static final long serialVersionUID = -7653585175036656542L;

    public static final String TABLE_NAME = "forms";

    public static final String UUID = "uuid";
    public static final String FORM_TYPE = "formType";
    public static final String DISEASE = "disease";
    public static final String ACTIVE = "active";
    private static final String FORMS_FORM_FIELDS_TABLE = "forms_form_fields";
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

    public static FormBuilder build(Disease disease, String formType) {
        FormBuilder form = new FormBuilder();
        return form;
    }

    public static FormBuilder build(Disease disease) {
        return build(disease, null);
    }

    private Set<FormField> formFields;
    @ManyToMany(cascade = {}, fetch = FetchType.LAZY)
    @JoinTable(name = FORMS_FORM_FIELDS_TABLE, joinColumns = @JoinColumn(name = "form_id"),
            inverseJoinColumns = @JoinColumn(name = "formField_id"))
    public Set<FormField> getFormFields() {return formFields;}
    public void setFormFields(Set<FormField> formFields) {
        this.formFields = formFields;
    }


}
