package de.symeda.sormas.app.backend.ebs.ebsAlert;

import java.io.Serializable;

import de.symeda.sormas.api.ebs.ResponseStatus;

public class EbsAlertCriteria implements Serializable {
    private Long ebsId;
    private ResponseStatus responseStatus;

    public void setEbsId(Long ebsId) {
        this.ebsId = ebsId;
    }

    public Long getEbsId() {
        return ebsId;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public EbsAlertCriteria responseStatus(ResponseStatus responseStatus){
        this.responseStatus = responseStatus;
        return this;
    }
}
