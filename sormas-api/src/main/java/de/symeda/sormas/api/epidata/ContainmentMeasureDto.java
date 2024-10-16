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
public class ContainmentMeasureDto extends PseudonymizableDto  {

    public static String I18N_PREFIX = "ContainmentMeasure";
    private static final long serialVersionUID = 6551672739041643942L;

    public static final String TABLE_NAME = "containmentMeasures";
    public static final String LOCATION_OF_WORM = "locationOfWorm";
    public static final String DATE_WORM_DETECTED_EMERGENCE = "dateWormDetectedEmergence";
    public static final String DATE_WORM_DETECT_BY_SUPERVISOR = "dateWormDetectBySupervisor";
    public static final String DATE_CONFIRMED = "dateConfirmed";
    public static final String DATE_OF_GUINEA_WORM_EXPULLED = "dateOfGuineaWormExpelled";
    public static final String REGULAR_BANDAGING = "regularBandaging";
    public static final String COMPLETELY_EXTRACTED = "completelyExtracted";
//    private String uuid;
    private String locationOfWorm;
    private Date dateWormDetectedEmergence;
    private Date dateWormDetectBySupervisor;
    private Date dateConfirmed;
    private Date dateOfGuineaWormExpelled;
    private YesNo regularBandaging;
    private YesNo completelyExtracted;

    public static ContainmentMeasureDto build(String contaminationType) {
        ContainmentMeasureDto contaminationSourceDto = new ContainmentMeasureDto();
        contaminationSourceDto.setUuid(DataHelper.createUuid());
        return contaminationSourceDto;
    }

    public static ContainmentMeasureDto build(
            String uuid,
            String locationOfWorm,
            Date dateWormDetectedEmergence,
            Date dateWormDetectBySupervisor,
            Date dateConfirmed,
            Date dateOfGuineaWormExpelled,
            YesNo regularBandaging,
            YesNo completelyExtracted) {
        ContainmentMeasureDto contaminationSourceDto = new ContainmentMeasureDto();
        contaminationSourceDto.setUuid(uuid);
        contaminationSourceDto.setLocationOfWorm(locationOfWorm);
        contaminationSourceDto.setDateWormDetectedEmergence(dateWormDetectedEmergence);
        contaminationSourceDto.setDateWormDetectBySupervisor(dateWormDetectBySupervisor);
        contaminationSourceDto.setDateConfirmed(dateConfirmed);
        contaminationSourceDto.setDateOfGuineaWormExpelled(dateOfGuineaWormExpelled);
        contaminationSourceDto.setRegularBandaging(regularBandaging);
        contaminationSourceDto.setCompletelyExtracted(completelyExtracted);
        return contaminationSourceDto;
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

    public String getLocationOfWorm() {
        return locationOfWorm;
    }

    public void setLocationOfWorm(String locationOfWorm) {
        this.locationOfWorm = locationOfWorm;
    }

    public Date getDateWormDetectedEmergence() {
        return dateWormDetectedEmergence;
    }

    public void setDateWormDetectedEmergence(Date dateWormDetectedEmergence) {
        this.dateWormDetectedEmergence = dateWormDetectedEmergence;
    }

    public Date getDateWormDetectBySupervisor() {
        return dateWormDetectBySupervisor;
    }

    public void setDateWormDetectBySupervisor(Date dateWormDetectBySupervisor) {
        this.dateWormDetectBySupervisor = dateWormDetectBySupervisor;
    }

    public Date getDateConfirmed() {
        return dateConfirmed;
    }

    public void setDateConfirmed(Date dateConfirmed) {
        this.dateConfirmed = dateConfirmed;
    }

    public Date getDateOfGuineaWormExpelled() {
        return dateOfGuineaWormExpelled;
    }

    public void setDateOfGuineaWormExpelled(Date dateOfGuineaWormExpelled) {
        this.dateOfGuineaWormExpelled = dateOfGuineaWormExpelled;
    }

    public YesNo getRegularBandaging() {
        return regularBandaging;
    }

    public void setRegularBandaging(YesNo regularBandaging) {
        this.regularBandaging = regularBandaging;
    }

    public YesNo getCompletelyExtracted() {
        return completelyExtracted;
    }

    public void setCompletelyExtracted(YesNo completelyExtracted) {
        this.completelyExtracted = completelyExtracted;
    }

    @Override
    public ContainmentMeasureDto clone() throws CloneNotSupportedException {
        ContainmentMeasureDto clone = (ContainmentMeasureDto) super.clone();
        return clone;
    }
}
