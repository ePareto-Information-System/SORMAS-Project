package de.symeda.sormas.api.infrastructure.forms;

import de.symeda.sormas.api.AgeGroup;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.utils.IgnoreForUrl;
import de.symeda.sormas.api.utils.criteria.BaseCriteria;

import java.io.Serializable;

public class FormBuilderCriteria extends BaseCriteria implements Serializable, Cloneable{

    private static final long serialVersionUID = 3958619224286048978L;

    private Disease disease;
//    private FormType formType;

    public FormBuilderCriteria() {
    }

//    @IgnoreForUrl
//    public FormType getFormType() {
//        return formType;
//    }

    @IgnoreForUrl
    public Disease getDisease() {
        return disease;
    }

    public FormBuilderCriteria disease(Disease disease) {
        this.disease = disease;
        return this;
    }
}
