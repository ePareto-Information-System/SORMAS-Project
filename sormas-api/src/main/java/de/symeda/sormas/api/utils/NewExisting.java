package de.symeda.sormas.api.utils;

public enum NewExisting {
    NEW_CASE,
    EXISTING_CASE;

    public static NewExisting valueOf(Boolean value) {
        if (Boolean.FALSE.equals(value)) {
            return EXISTING_CASE;
        } else {
            return NEW_CASE;
        }
    }
}
