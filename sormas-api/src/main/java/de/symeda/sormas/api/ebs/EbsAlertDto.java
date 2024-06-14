package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.ImportIgnore;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableDto;

import java.util.Date;

public class EbsAlertDto extends PseudonymizableDto {

    private final static long serialVersionUID = 1L;

    public static final String I18N_PREFIX = "EbsAlert";

    public static final String ACTION_INITIATED = "actionInitiated";
    public static final String RESPONSE_STATUS = "responseStatus";
    public static final String RESPONSE_DATE = "responseDate";
    public static final String DETAILS_RESPONSE_ACTIVITIES = "detailsResponseActivities";
    public static final String DETAILS_GIVEN = "detailsGiven";
    public static final String ALERT_USED = "alertUsed";
    public static final String DETAILS_ALERT_USED = "detailsAlertUsed";
    public static final String ALERT_DATE = "alertDate";
    public static final String EBS = "ebs";

    private YesNo actionInitiated;
    private ResponseStatus responseStatus;
    private Date responseDate;
    private String detailsResponseActivities;
    private String detailsGiven;
    private YesNo alertUsed;
    private String detailsAlertUsed;
    private EbsReferenceDto ebs;
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

    @ImportIgnore
    public EbsReferenceDto getEbs() {
        return ebs;
    }

    public void setEbs(EbsReferenceDto ebs) {
        this.ebs = ebs;
    }

    private static EbsAlertDto createEbsAlertDto() {
        EbsAlertDto ebsAlertDto = new EbsAlertDto();
        ebsAlertDto.setUuid(DataHelper.createUuid());
        return ebsAlertDto;
    }

    public static EbsAlertDto build(EbsReferenceDto ebsRef) {
        final EbsAlertDto ebsAlertDto = createEbsAlertDto();
        ebsAlertDto.setEbs(ebsRef);
        return ebsAlertDto;
    }
}
