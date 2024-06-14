package de.symeda.sormas.backend.ebs;

import de.symeda.sormas.api.ebs.ResponseStatus;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.backend.common.AbstractDomainObject;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity(name = "ebsAlert")
public class EbsAlert extends AbstractDomainObject {

    public static final String DELETED = "deleted";
    public final static long serialVersionUID = 1L;
    public static final String I18N_PREFIX = "EbsAlert";
    public static final String TABLE_NAME = "ebsAlert";
    public static final String ACTION_INITIATED = "actionInitiated";
    public static final String RESPONSE_STATUS = "responseStatus";
    public static final String RESPONSE_DATE = "responseDate";
    public static final String DETAILS_RESPONSE_ACTIVITIES = "detailsResponseActivities";
    public static final String ALERT_USED = "alertUsed";
    public static final String DETAILS_GIVEN = "detailsGiven";
    public static final String DETAILS_ALERT_USED = "detailsAlertUsed";
    public static final String ALERTDATE = "alertDate";

    public static final String EBS = "ebs";

    private YesNo actionInitiated;
    private ResponseStatus responseStatus;
    private Date responseDate;
    private String detailsResponseActivities;
    private String detailsGiven;
    private YesNo alertUsed;
    private String detailsAlertUsed;
    private Ebs ebs;
    private Date alertDate;

    public YesNo getActionInitiated() {
        return actionInitiated;
    }

    public void setActionInitiated(YesNo actionInitiated) {
        this.actionInitiated = actionInitiated;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public String getDetailsResponseActivities() {
        return detailsResponseActivities;
    }

    public void setDetailsResponseActivities(String detailsResponseActivities) {
        this.detailsResponseActivities = detailsResponseActivities;
    }

    public String getDetailsGiven() {
        return detailsGiven;
    }

    public void setDetailsGiven(String detailsGiven) {
        this.detailsGiven = detailsGiven;
    }

    public YesNo getAlertUsed() {
        return alertUsed;
    }

    public void setAlertUsed(YesNo alertUsed) {
        this.alertUsed = alertUsed;
    }

    public String getDetailsAlertUsed() {
        return detailsAlertUsed;
    }

    public void setDetailsAlertUsed(String detailsAlertUsed) {
        this.detailsAlertUsed = detailsAlertUsed;
    }

    public Date getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(Date alertDate) {
        this.alertDate = alertDate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    public Ebs getEbs() {
        return ebs;
    }

    public void setEbs(Ebs ebs) {
        this.ebs = ebs;
    }
}
