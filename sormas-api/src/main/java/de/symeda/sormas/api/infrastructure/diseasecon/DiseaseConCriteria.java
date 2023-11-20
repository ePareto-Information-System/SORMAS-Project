package de.symeda.sormas.api.infrastructure.diseasecon;

import de.symeda.sormas.api.AgeGroup;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.utils.IgnoreForUrl;
import de.symeda.sormas.api.utils.criteria.BaseCriteria;

import java.io.Serializable;

public class DiseaseConCriteria extends BaseCriteria implements Serializable, Cloneable{

    private static final long serialVersionUID = 3958619224286048978L;

    private Disease disease;
    private AgeGroup ageGroup;

    public DiseaseConCriteria() {
    }


    public AgeGroup ageGroup() {
        return ageGroup;
    }

    public void setAgeGroup(AgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }

    @IgnoreForUrl
    public Disease getDisease() {
        return disease;
    }

    public DiseaseConCriteria disease(Disease disease) {
        this.disease = disease;
        return this;
    }
}
