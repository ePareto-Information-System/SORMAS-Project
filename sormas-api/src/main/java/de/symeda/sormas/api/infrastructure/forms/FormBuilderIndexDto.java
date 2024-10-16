package de.symeda.sormas.api.infrastructure.forms;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;

import java.io.Serializable;
import java.util.List;

public class FormBuilderIndexDto implements Serializable {

    public static final String I18N_PREFIX = "Disease";

    public static final String UUID = "uuid";
    public static final String DISEASE = "disease";
    public static final String FORM_TYPE = "formType";
    public static final String ACTIVE = "active";

    private String uuid;
    private Disease disease;
	private FormType formType;
    public FormBuilderIndexDto(String uuid, Disease in_disease, FormType formType) {

            Disease disease = Disease.valueOf(in_disease.getName());
            this.uuid = uuid;
            this.disease = disease;
            this.formType = formType;
    }
    


    public String getUuid() {
        return uuid;
    }

    public Disease getDisease() {
        return disease;
    }

    public FormType getFormType() {
        return formType;
    }

    public void setFormType(FormType formType) {
        this.formType = formType;
    }

    //TODO: Write clone method

}
