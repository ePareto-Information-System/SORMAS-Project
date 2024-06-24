package de.symeda.sormas.backend.afpimmunization;

import de.symeda.sormas.api.utils.CardRecall;
import de.symeda.sormas.backend.common.AbstractDomainObject;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class AfpImmunization extends AbstractDomainObject {
    private static final long serialVersionUID = -8294812479501735786L;

    public static final String TABLE_NAME = "afpimmunization";

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
