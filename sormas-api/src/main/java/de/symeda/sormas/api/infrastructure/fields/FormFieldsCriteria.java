package de.symeda.sormas.api.infrastructure.fields;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.utils.IgnoreForUrl;
import de.symeda.sormas.api.utils.criteria.BaseCriteria;

import java.io.Serializable;

public class FormFieldsCriteria extends BaseCriteria implements Serializable, Cloneable{

    private static final long serialVersionUID = 3958619224286048978L;

    private FormType formType;

    public FormFieldsCriteria() {
    }

//    @IgnoreForUrl
//    public FormType getFormType() {
//        return formType;
//    }

    @IgnoreForUrl
    public FormType getFormType() {
        return formType;
    }


    public FormFieldsCriteria setFormType(FormType formType) {
        this.formType = formType;
        return this;
    }
}
