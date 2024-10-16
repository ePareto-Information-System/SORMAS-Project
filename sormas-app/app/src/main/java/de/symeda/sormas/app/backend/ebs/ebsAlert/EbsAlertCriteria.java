package de.symeda.sormas.app.backend.ebs.ebsAlert;

import java.io.Serializable;

public class EbsAlertCriteria implements Serializable {
    private Long ebsId;

    public void setEbsId(Long ebsId) {
        this.ebsId = ebsId;
    }

    public Long getEbsId() {
        return ebsId;
    }
}
