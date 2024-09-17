package de.symeda.sormas.app.backend.ebs.ebsAlert;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import de.symeda.sormas.api.ebs.ResponseStatus;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.ebs.Ebs;

@Entity(name = EbsAlert.TABLE_NAME)
@DatabaseTable(tableName = EbsAlert.TABLE_NAME)
public class EbsAlert extends PseudonymizableAdo {
    private static final long serialVersionUID = 1L;
    public static final long APPROXIMATE_JSON_SIZE_IN_BYTES = 13356;

    public static final String I18N_PREFIX = "EbsAlert";
    public static final String TABLE_NAME = "EbsAlert";
    @DatabaseField
    private YesNo actionInitiated;
    @DatabaseField
    private ResponseStatus responseStatus;
    @DatabaseField(dataType = DataType.DATE_LONG)
    private Date responseDate;
    @DatabaseField
    private String detailsResponseActivities;
    @DatabaseField
    private String detailsGiven;
    @DatabaseField
    private YesNo alertIssued;
    @DatabaseField
    private String detailsAlertUsed;
    private Ebs ebs;
    @DatabaseField(dataType = DataType.DATE_LONG)
    private Date alertDate;

    @Override
    public String getI18nPrefix() {
        return I18N_PREFIX;
    }


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

    public YesNo getAlertIssued() {
        return alertIssued;
    }

    public void setAlertIssued(YesNo alertUsed) {
        this.alertIssued = alertUsed;
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