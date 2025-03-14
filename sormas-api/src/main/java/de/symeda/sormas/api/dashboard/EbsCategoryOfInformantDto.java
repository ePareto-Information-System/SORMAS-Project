package de.symeda.sormas.api.dashboard;

import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.ebs.PersonReporting;
import java.io.Serializable;

public class EbsCategoryOfInformantDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private EbsSourceType sourceInformation;
    private PersonReporting categoryOfInformant;
    private Long informantCount;
    private Double percentage;

    public EbsCategoryOfInformantDto(EbsSourceType sourceInformation, PersonReporting categoryOfInformant, Long informantCount, Double percentage) {
        this.sourceInformation = sourceInformation;
        this.categoryOfInformant = categoryOfInformant;
        this.informantCount = informantCount;
        this.percentage = percentage;
    }

    public EbsSourceType getSourceInformation() {
        return sourceInformation;
    }

    public void setSourceInformation(EbsSourceType sourceInformation) {
        this.sourceInformation = sourceInformation;
    }

    public PersonReporting getCategoryOfInformant() {
        return categoryOfInformant;
    }

    public void setCategoryOfInformant(PersonReporting categoryOfInformant) {
        this.categoryOfInformant = categoryOfInformant;
    }

    public Long getInformantCount() {
        return informantCount;
    }

    public void setInformantCount(Long informantCount) {
        this.informantCount = informantCount;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

}