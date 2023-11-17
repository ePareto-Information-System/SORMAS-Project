package de.symeda.sormas.api.infrastructure.disease;

import de.symeda.sormas.api.AgeGroup;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.utils.IgnoreForUrl;
import de.symeda.sormas.api.utils.criteria.BaseCriteria;

import java.io.Serializable;

public class DiseaseCriteria extends BaseCriteria implements Serializable, Cloneable{

    private static final long serialVersionUID = 3958619224286048978L;

    private Disease disease;
    private AgeGroup ageGroup;

    public DiseaseCriteria() {
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

    public DiseaseCriteria disease(Disease disease) {
        this.disease = disease;
        return this;
    }
}
