package de.symeda.sormas.app.backend.ebs;

import java.io.Serializable;

import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.api.ebs.SignalVerificationDto;
import de.symeda.sormas.app.backend.ebs.signalVerification.SignalVerification;

public class EbsCriteria implements Serializable {
    private SignalOutcome signalOutcome;
    private EbsSourceType sourceType;
    private SignalVerification signalVerification;

    public EbsCriteria signalOutcome(SignalOutcome signalOutcome){
        this.signalOutcome = signalOutcome;
        return this;
    }


    public SignalOutcome getSignalOutcome() {
        return signalOutcome;
    }

    public void setSignalOutcome(SignalOutcome signalOutcome) {
        this.signalOutcome = signalOutcome;
    }

    public SignalVerification getSignalVerification() {
        return signalVerification;
    }

    public void setSignalVerification(SignalVerification signalVerification) {
        this.signalVerification = signalVerification;
    }


    public EbsCriteria signalVerification(SignalVerification signalVerificationDto) {
        this.signalVerification = signalVerification;
        return this;
    }

    public EbsSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(EbsSourceType sourceType) {
        this.sourceType = sourceType;
    }
}
