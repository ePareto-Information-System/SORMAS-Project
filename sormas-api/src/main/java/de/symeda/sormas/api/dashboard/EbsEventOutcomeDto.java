package de.symeda.sormas.api.dashboard;

import de.symeda.sormas.api.EbsEvent;
import de.symeda.sormas.api.ebs.EbsTriagingDecision;
import de.symeda.sormas.api.ebs.SignalOutcome;
import java.io.Serializable;

public class EbsEventOutcomeDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long outComeCount;
    private EbsTriagingDecision triagingDecision;
    private SignalOutcome verificationStatus;
    private Long count;
    private Double percentage;

    public EbsEventOutcomeDto(Long outComeCount, SignalOutcome status, Long count, Double percentage) {
        this.outComeCount = outComeCount;
        this.verificationStatus = status;
        this.count = count;
        this.percentage = percentage;
    }

    public EbsEventOutcomeDto() {

    }

    public Long getOutComeCount() {
        return outComeCount;
    }

    public void setOutComeCount(Long outComeCount) {
        this.outComeCount = outComeCount;
    }

    public EbsTriagingDecision getTriagingDecision() {
        return triagingDecision;
    }

    public void setTriagingDecision(EbsTriagingDecision triagingDecision) {
        this.triagingDecision = triagingDecision;
    }

    public SignalOutcome getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(SignalOutcome verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
