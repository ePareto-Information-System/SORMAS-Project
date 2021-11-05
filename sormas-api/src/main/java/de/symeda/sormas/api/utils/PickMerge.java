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

    @Override
    public String toString() {
        return super.toString();
    }

    static public PickMerge checkValue(String value) {
        if (value == null)
            return PickMerge.CANCEL;

        if (value.equalsIgnoreCase(PickMerge.PICK.toString()))
            return PickMerge.PICK;

		if (value.equalsIgnoreCase(String.valueOf(PickMerge.MERGE)))
			return PickMerge.MERGE;

        if (value.equalsIgnoreCase("add"))
            return PickMerge.MERGE;

        if (value.equalsIgnoreCase("join"))
            return PickMerge.MERGE;

        if (value.equalsIgnoreCase("choose"))
            return PickMerge.MERGE;

        return null;
    }
}
