package de.symeda.sormas.app;

import java.util.List;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;

public class FieldOrderConfigurations {

    public static List<Integer> getConfigurationForDisease(Disease disease, FormType formType) {
        return DiseaseFieldConfigurations.getFields(disease, formType);
    }
}
