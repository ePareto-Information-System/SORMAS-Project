package de.symeda.sormas.api.afpimmunization;

import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.utils.CardRecall;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DependingOnFeatureType;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableDto;

import java.util.Date;

@DependingOnFeatureType(featureType = FeatureType.CASE_SURVEILANCE)
public class AfpImmunizationDto extends PseudonymizableDto {

    private static final long serialVersionUID = 4846215199480684367L;
    public static final String I18N_PREFIX = "CaseAfpImmunization";

    public static final String TOTAL_NUMBER_DOSES = "totalNumberDoses";
    public static final String OPV_DOSE_AT_BIRTH = "opvDoseAtBirth";
    public static final String SECOND = "secondDose";
    public static final String FOURTH = "fourthDose";
    public static final String FIRST = "firstDose";
    public static final String THIRD = "thirdDose";
    public static final String LAST_DOSE = "lastDose";
    public static final String TOTAL_OPV_DOSES_RECEIVED_THROUGH_SIA = "totalOpvDosesReceivedThroughSia";
    public static final String TOTAL_OPV_DOSES_RECEIVED_THROUGH_RI = "totalOpvDosesReceivedThroughRi";
    public static final String DATE_LAST_OPV_DOSES_RECEIVED_THROUGH_SIA = "dateLastOpvDosesReceivedThroughSia";
    public static final String TOTAL_IPV_DOSES_RECEIVED_THROUGH_SIA = "totalIpvDosesReceivedThroughSia";
    public static final String TOTAL_IPV_DOSES_RECEIVED_THROUGH_RI = "totalIpvDosesReceivedThroughRi";
    public static final String DATE_LAST_IPV_DOSES_RECEIVED_THROUGH_SIA = "dateLastIpvDosesReceivedThroughSia";
    public static final String SOURCE_RI_VACCINATION_INFORMATION = "sourceRiVaccinationInformation";



    private Integer totalNumberDoses;
    private Date opvDoseAtBirth;
    private Date secondDose;
    private Date fourthDose;
    private Date firstDose;
    private Date thirdDose;
    private Date lastDose;
    private String totalOpvDosesReceivedThroughSia;
    private String totalOpvDosesReceivedThroughRi;
    private Date dateLastOpvDosesReceivedThroughSia;
    private String totalIpvDosesReceivedThroughSia;
    private String totalIpvDosesReceivedThroughRi;
    private Date dateLastIpvDosesReceivedThroughSia;
    private CardRecall sourceRiVaccinationInformation;


    public static AfpImmunizationDto build() {
        AfpImmunizationDto afpImmunizationDto  = new AfpImmunizationDto();
        afpImmunizationDto .setUuid(DataHelper.createUuid());
        return afpImmunizationDto ;
    }

    public Integer getTotalNumberDoses() {
        return totalNumberDoses;
    }

    public void setTotalNumberDoses(Integer totalNumberDoses) {
        this.totalNumberDoses = totalNumberDoses;
    }

    public Date getOpvDoseAtBirth() {
        return opvDoseAtBirth;
    }

    public void setOpvDoseAtBirth(Date opvDoseAtBirth) {
        this.opvDoseAtBirth = opvDoseAtBirth;
    }

    public Date getSecondDose() {
        return secondDose;
    }

    public void setSecondDose(Date secondDose) {
        this.secondDose = secondDose;
    }

    public Date getFourthDose() {
        return fourthDose;
    }

    public void setFourthDose(Date fourthDose) {
        this.fourthDose = fourthDose;
    }

    public Date getFirstDose() {
        return firstDose;
    }

    public void setFirstDose(Date firstDose) {
        this.firstDose = firstDose;
    }

    public Date getThirdDose() {
        return thirdDose;
    }

    public void setThirdDose(Date thirdDose) {
        this.thirdDose = thirdDose;
    }

    public Date getLastDose() {
        return lastDose;
    }

    public void setLastDose(Date lastDose) {
        this.lastDose = lastDose;
    }

    public String getTotalOpvDosesReceivedThroughSia() {
        return totalOpvDosesReceivedThroughSia;
    }

    public void setTotalOpvDosesReceivedThroughSia(String totalOpvDosesReceivedThroughSia) {
        this.totalOpvDosesReceivedThroughSia = totalOpvDosesReceivedThroughSia;
    }

    public String getTotalOpvDosesReceivedThroughRi() {
        return totalOpvDosesReceivedThroughRi;
    }

    public void setTotalOpvDosesReceivedThroughRi(String totalOpvDosesReceivedThroughRi) {
        this.totalOpvDosesReceivedThroughRi = totalOpvDosesReceivedThroughRi;
    }

    public Date getDateLastOpvDosesReceivedThroughSia() {
        return dateLastOpvDosesReceivedThroughSia;
    }

    public void setDateLastOpvDosesReceivedThroughSia(Date dateLastOpvDosesReceivedThroughSia) {
        this.dateLastOpvDosesReceivedThroughSia = dateLastOpvDosesReceivedThroughSia;
    }

    public String getTotalIpvDosesReceivedThroughSia() {
        return totalIpvDosesReceivedThroughSia;
    }

    public void setTotalIpvDosesReceivedThroughSia(String totalIpvDosesReceivedThroughSia) {
        this.totalIpvDosesReceivedThroughSia = totalIpvDosesReceivedThroughSia;
    }

    public String getTotalIpvDosesReceivedThroughRi() {
        return totalIpvDosesReceivedThroughRi;
    }

    public void setTotalIpvDosesReceivedThroughRi(String totalIpvDosesReceivedThroughRi) {
        this.totalIpvDosesReceivedThroughRi = totalIpvDosesReceivedThroughRi;
    }

    public Date getDateLastIpvDosesReceivedThroughSia() {
        return dateLastIpvDosesReceivedThroughSia;
    }

    public void setDateLastIpvDosesReceivedThroughSia(Date dateLastIpvDosesReceivedThroughSia) {
        this.dateLastIpvDosesReceivedThroughSia = dateLastIpvDosesReceivedThroughSia;
    }

    public CardRecall getSourceRiVaccinationInformation() {
        return sourceRiVaccinationInformation;
    }

    public void setSourceRiVaccinationInformation(CardRecall sourceRiVaccinationInformation) {
        this.sourceRiVaccinationInformation = sourceRiVaccinationInformation;
    }

}
