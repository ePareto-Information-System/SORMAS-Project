package de.symeda.sormas.api.utils;

import org.apache.commons.lang3.StringUtils;

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
