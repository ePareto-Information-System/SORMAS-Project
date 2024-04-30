package de.symeda.sormas.api.hospitalization;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum SymptomsList {
    ABDOMINAL, BLOODY_STOOL, CHILLS, CONVULSION, DEHYDRATION, DIARRHOEA, DIZZINESS, FEVER, EXCESSIVE_SWEATING, HEADACHE, JAUNDICE, NAUSEA, MUSCLE_ACHES, NUMBNESS, VOMITING, WEAKNESS, BODY_PAINS, LYMPH_NODES, RASH, SORE_THROAT, COUGH, FATIGUE;

    public static SymptomsList[] FoodBorne() {
        return new SymptomsList[] {
                ABDOMINAL, BLOODY_STOOL, CHILLS, CONVULSION, DEHYDRATION, DIARRHOEA, DIZZINESS, FEVER, EXCESSIVE_SWEATING, HEADACHE, JAUNDICE, NAUSEA, MUSCLE_ACHES, NUMBNESS, VOMITING, WEAKNESS
        };
    }

    public static SymptomsList[] MpoxList(){
        return new SymptomsList[] {
                FEVER, BODY_PAINS, HEADACHE, LYMPH_NODES, WEAKNESS, RASH, MUSCLE_ACHES, SORE_THROAT, COUGH, FATIGUE
        };
    }
    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }
}
