package de.symeda.sormas.ui.samples;


import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.sample.SampleDto;

import java.util.*;

public class SampleFormConfiguration {
    private static final Map<String, Set<Disease>> DISABLED_FIELDS_BY_DISEASE = new HashMap<>();
    static {
        DISABLED_FIELDS_BY_DISEASE.put(SampleDto.PATHOGEN_TESTING_REQUESTED, new HashSet<>(Arrays.asList(Disease.NEW_INFLUENZA, Disease.CSM)));
    }

    public static Set<String> getDisabledFieldsForDisease(Disease disease) {
        Set<String> disabledFields = new HashSet<>();
        for (Map.Entry<String, Set<Disease>> entry : DISABLED_FIELDS_BY_DISEASE.entrySet()) {
            if (entry.getValue().contains(disease)) {
                disabledFields.add(entry.getKey());
            }
        }
        return disabledFields;
    }
}
