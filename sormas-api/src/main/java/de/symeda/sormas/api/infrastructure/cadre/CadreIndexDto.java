package de.symeda.sormas.api.infrastructure.cadre;

public class CadreIndexDto extends CadreDto {

    public static final String DISPLAY_NAME = "displayName";

    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
