package de.symeda.sormas.backend.contaminationsource;

import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.epidata.EpiData;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity(name = "contaminationsources")
public class ContaminationSource extends AbstractDomainObject {
    private static final long serialVersionUID = -5570515874416024602L;

    public static final String TABLE_NAME = "contaminationsources";
    public static final String EPI_DATA = "epiData";
    public static final String UUID = "uuid";
    public static final String NAME = "name";
    private static final String CONTAMINATION_TYPE = "contaminationType";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private static final String TYPE = "type";
    private static final String SOURCE = "source";
    private static final String TREATED_WITH_ABATE = "treatedWithAbate";
    private static final String ABATE_TREATMENT_DATE = "abateTreatmentDate";
    private EpiData epiData;
    private String uuid;
    private String name;
    private String contaminationType;
    private Double longitude;
    private Double latitude;
    private String type;
    private String source;
    private YesNo treatedWithAbate;
    private Date abateTreatmentDate;

    public ContaminationSource() {
    }

    public ContaminationSource(
            String name,
            String contaminationType,
            Double longitude,
            Double latitude,
            String type,
            String source,
            YesNo treatedWithAbate,
            Date abateTreatmentDate) {
        this.name = name;
        this.contaminationType = contaminationType;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
        this.source = source;
        this.treatedWithAbate = treatedWithAbate;
        this.abateTreatmentDate = abateTreatmentDate;
    }


    @ManyToOne
    @JoinColumn(nullable = false)
    public EpiData getEpiData() {
        return epiData;
    }

    public void setEpiData(EpiData epiData) {
        this.epiData = epiData;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContaminationType() {
        return contaminationType;
    }

    public void setContaminationType(String contaminationType) {
        this.contaminationType = contaminationType;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public YesNo getTreatedWithAbate() {
        return treatedWithAbate;
    }

    public void setTreatedWithAbate(YesNo treatedWithAbate) {
        this.treatedWithAbate = treatedWithAbate;
    }

    public Date getAbateTreatmentDate() {
        return abateTreatmentDate;
    }

    public void setAbateTreatmentDate(Date abateTreatmentDate) {
        this.abateTreatmentDate = abateTreatmentDate;
    }
}
