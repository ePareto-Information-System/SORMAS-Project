package de.symeda.sormas.app;

import java.util.Objects;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;

public class DiseaseFormTypeKey {
    private final Disease disease;
    private final FormType formType;

    public DiseaseFormTypeKey(Disease disease, FormType formType) {
        this.disease = disease;
        this.formType = formType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiseaseFormTypeKey that = (DiseaseFormTypeKey) o;
        return disease == that.disease && formType == that.formType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(disease, formType);
    }
}
