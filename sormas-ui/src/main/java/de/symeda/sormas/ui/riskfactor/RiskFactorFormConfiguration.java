package de.symeda.sormas.ui.riskfactor;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.riskfactor.RiskFactorDto;

import java.util.*;

public class RiskFactorFormConfiguration {

    private static final Map<String, Set<Disease>> DISABLED_FIELDS_BY_DISEASE = new HashMap<>();

    static {
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.DRINKING_WATER_SOURCE_ONE, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.DRINKING_WATER_SOURCE_TWO, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.DRINKING_WATER_SOURCE_THREE, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.DRINKING_WATER_SOURCE_FOUR, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));

        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.NON_DRINKING_WATER_SOURCE_ONE, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.NON_DRINKING_WATER_SOURCE_TWO, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.NON_DRINKING_WATER_SOURCE_THREE, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.NON_DRINKING_WATER_SOURCE_FOUR, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));

        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.FOOD_ITEMS_ONE, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.FOOD_ITEMS_TWO, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.FOOD_ITEMS_THREE, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.FOOD_ITEMS_FOUR, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.FOOD_ITEMS_FIVE, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.FOOD_ITEMS_SIX, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.FOOD_ITEMS_SEVEN, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.FOOD_ITEMS_EIGHT, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));

        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.DRINKING_WATER_INFECTED_BY_VIBRIO, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.NON_DRINKING_WATER_INFECTED_BY_VIBRIO, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.FOOD_ITEMS_INFECTED_BY_VIBRIO, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.WATER_USED_FOR_DRINKING, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));

        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_ONE, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_TWO, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_THREE, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_FOUR, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_WATER_SOURCE_FIVE, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));

        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_ONE, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_TWO, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_THREE, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_FOUR, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_FOOD_ITEMS_FIVE, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));

        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_ATTEND_ANY_FUNERAL, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.THREE_DAYS_PRIOR_TO_DISEASE_ATTEND_ANY_SOCIAL_EVENT, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));
        DISABLED_FIELDS_BY_DISEASE.put(RiskFactorDto.OTHER_SOCIAL_EVENT_DETAILS, new HashSet<>(Arrays.asList(Disease.MONKEYPOX)));

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
