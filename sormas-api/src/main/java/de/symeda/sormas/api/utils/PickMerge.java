package de.symeda.sormas.api.utils;

public enum PickMerge {
    CANCEL,
    PICK,
    MERGE;

    public static PickMerge valueOf(Boolean value) {
        if (Boolean.FALSE.equals(value)) {
            return CANCEL;
        } else {
            return PICK;
        }
    }
}
