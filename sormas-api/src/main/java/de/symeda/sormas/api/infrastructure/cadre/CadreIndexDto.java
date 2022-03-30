package de.symeda.sormas.api.infrastructure.cadre;

public class CadreIndexDto extends CadreDto {

    public static final String I18N_PREFIX = "Cadre";
    public static final String POSITION = "position";
    public static final String UUID = "uuid";
    public static final String EXTERNAL_ID = "externalId";

    private String uuid;
    private String position;
    private String externalId;

    public CadreIndexDto(){}

    public CadreIndexDto(String uuid, String position, String externalId){
        this.uuid = uuid;
        this.position = position;
        this.externalId = externalId;
    }
    
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
