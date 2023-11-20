package de.symeda.sormas.api.infrastructure.diseasecon;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;

import java.io.Serializable;
import java.util.List;

public class DiseaseIndexDto implements Serializable {

    public static final String I18N_PREFIX = "Disease";

    public static final String UUID = "uuid";
    public static final String DISEASE = "disease";
    public static final String ACTIVE = "active";
    public static final String PRIMARY_DISEASE = "primaryDisease";
    public static final String FOLLOW_UP_ENABLED = "followUpEnabled";
    public static final String FOLLOW_UP_DURATION = "followUpDuration";
    public static final String EXTENDED_CLASSIFICATION = "extendedClassification";
    public static final String AGE_GROUPS = "ageGroups";



    private String uuid;
    private Disease disease;
	private String active;
	private String primaryDisease;
	private String followUpEnabled;
	private Integer followUpDuration;
	private String extendedClassification;
	private String extendedClassificationMulti;
	private List<String> ageGroups;
    private String diseaseName;

    public DiseaseConIndexDto(String uuid, Disease in_disease) {

            Disease disease = Disease.valueOf(in_disease.getName());
            this.uuid = uuid;
            this.disease = disease;
            this.active = disease.isDefaultActive() ? I18nProperties.getString(Strings.active) : I18nProperties.getString(Strings.inactive);
            this.primaryDisease = disease.isDefaultPrimary() ? I18nProperties.getString(Strings.yes) : I18nProperties.getString(Strings.no);
            this.followUpEnabled = disease.isDefaultFollowUpEnabled() ? I18nProperties.getString(Strings.yes) : I18nProperties.getString(Strings.no);
            this.followUpDuration = disease.getDefaultFollowUpDuration();
            this.extendedClassification = disease.isDefaultExtendedClassification() ? I18nProperties.getString(Strings.yes) : I18nProperties.getString(Strings.no);
    }
    


    public String getUuid() {
        return uuid;
    }

    public Disease getDisease() {
        return disease;
    }


    public String getActive() {
        return active;
    }

    public String getPrimaryDisease() {
        return primaryDisease;
    }

    public String getFollowUpEnabled() {
        return followUpEnabled;
    }

    public Integer getFollowUpDuration() {
        return followUpDuration;
    }

    public String getExtendedClassification() {
        return extendedClassification;
    }

    public String getExtendedClassificationMulti() {
        return extendedClassificationMulti;
    }

    public List<String> getAgeGroups() {
        return ageGroups;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    //create setters

    public void setActive(String active) {
        this.active = active;
    }

    public void setPrimaryDisease(String primaryDisease) {
        this.primaryDisease = primaryDisease;
    }

    public void setFollowUpEnabled(String followUpEnabled) {
        this.followUpEnabled = followUpEnabled;
    }

    public void setFollowUpDuration(Integer followUpDuration) {
        this.followUpDuration = followUpDuration;
    }



    public void setExtendedClassification(String extendedClassification) {
        this.extendedClassification = extendedClassification;
    }

    public void setExtendedClassificationMulti(String extendedClassificationMulti) {
        this.extendedClassificationMulti = extendedClassificationMulti;
    }

    public void setAgeGroups(List<String> ageGroups) {
        this.ageGroups = ageGroups;
    }

    public void  setDiseaseName (String diseaseName) {
        this.diseaseName = diseaseName;
    }
}
