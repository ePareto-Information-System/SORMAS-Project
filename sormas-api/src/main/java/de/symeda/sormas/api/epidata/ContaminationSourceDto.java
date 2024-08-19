package de.symeda.sormas.api.epidata;

import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DependingOnFeatureType;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableDto;

import java.util.Date;


@DependingOnFeatureType(featureType = {
        FeatureType.CASE_SURVEILANCE,
        FeatureType.CONTACT_TRACING })
public class ContaminationSourceDto extends PseudonymizableDto  {

    public static String I18N_PREFIX = "ContaminationSource";

    private static final long serialVersionUID = 6551672739041643942L;

    public static final String TABLE_NAME = "contaminationSources";
    public static final String NAME = "name";
    public static final String CONTAMINATION_TYPE = "contaminationType";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String TYPE = "type";
    public static final String SOURCE = "source";
    public static final String TREATED_WITH_ABATE = "treatedWithAbate";
    public static final String ABATE_TREATMENT_DATE = "abateTreatmentDate";

//    private String uuid;
    private String name;
    private String contaminationType;
    private Double longitude;
    private Double latitude;
    private String type;
    private String source;
    private YesNo treatedWithAbate;
    private Date abateTreatmentDate;


    public static ContaminationSourceDto build(String contaminationType) {
        ContaminationSourceDto personTravelHistoryDto = new ContaminationSourceDto();
        personTravelHistoryDto.setUuid(DataHelper.createUuid());
        personTravelHistoryDto.setContaminationType(contaminationType);
        return personTravelHistoryDto;
    }

    public static ContaminationSourceDto build(
            String name,
            String contaminationType,
            Double longitude,
            Double latitude,
            String type,
            String source,
            YesNo treatedWithAbate,
            Date abateTreatmentDate) {

        ContaminationSourceDto contaminationSourcesDto = new ContaminationSourceDto();
        contaminationSourcesDto.setUuid(DataHelper.createUuid());
        contaminationSourcesDto.setName(name);
        contaminationSourcesDto.setContaminationType(contaminationType);
        contaminationSourcesDto.setLatitude(longitude);
        contaminationSourcesDto.setLongitude(latitude);
        contaminationSourcesDto.setType(type);
        contaminationSourcesDto.setSource(source);
        contaminationSourcesDto.setTreatedWithAbate(treatedWithAbate);
        contaminationSourcesDto.setAbateTreatmentDate(abateTreatmentDate);
        return contaminationSourcesDto;
    }


//    @Override
//    public String getUuid() {
//        return uuid;
//    }
//
//    @Override
//    public void setUuid(String uuid) {
//        this.uuid = uuid;
//    }

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setType(String type) {
        this.type = type;
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



    @Override
    public ContaminationSourceDto clone() throws CloneNotSupportedException {
        ContaminationSourceDto clone = (ContaminationSourceDto) super.clone();
        return clone;
    }

}
