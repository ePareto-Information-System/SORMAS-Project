package de.symeda.sormas.api.hospitalization;

import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.DataHelper;

public enum SymptomsList {
    ABDOMINAL, BLOODY_STOOL, CHILLS, CONVULSION, DEHYDRATION, DIARRHOEA, DIZZINESS, FEVER, EXCESSIVE_SWEATING, HEADACHE, JAUNDICE, NAUSEA, MUSCLE_ACHES, NUMBNESS, VOMITING, WEAKNESS, OTHER;

    @Override
    public String toString() {
        return I18nProperties.getEnumCaption(this);
    }

    public static String toString(SymptomsList value, String details) {

        if (value == null) {
            return "";
        }

        if (value == SymptomsList.OTHER) {
            return DataHelper.toStringNullable(details);
        }

        return value.toString();
    }
}
