/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.app.backend.ebs;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import de.symeda.sormas.api.ebs.AutomaticScanningType;
import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.ebs.ManualScanningType;
import de.symeda.sormas.api.ebs.MediaScannningType;
import de.symeda.sormas.api.ebs.PersonReporting;
import de.symeda.sormas.app.backend.common.EmbeddedAdo;
import de.symeda.sormas.app.backend.common.PseudonymizableAdo;
import de.symeda.sormas.app.backend.location.Location;
import de.symeda.sormas.app.backend.sormastosormas.SormasToSormasOriginInfo;
import de.symeda.sormas.app.backend.ebs.triaging.Triaging;
import de.symeda.sormas.app.backend.user.User;
import de.symeda.sormas.app.backend.ebs.signalVerification.SignalVerification;

@Entity(name = Ebs.TABLE_NAME)
@DatabaseTable(tableName = Ebs.TABLE_NAME)
public class Ebs extends PseudonymizableAdo {
    public static final String SIGNAL_VERIFICATION = "signalVerification";
    private static final long serialVersionUID = 4964495716032049582L;

    public static final String TABLE_NAME = "ebs";
    public static final String I18N_PREFIX = "Ebs";

    public static final String INFORMANT_NAME = "informantName";
    public static final String INFORMANT_TEL = "informantTel";
    public static final String EVENT_DESC = "eventDesc";
    public static final String TRIAGE_DATE = "triageDate";
    public static final String SOURCE_NAME = "sourceName";
    public static final String SOURCE_URL = "sourceUrl";
    public static final String DESCRIPTION_OCCURANCE = "descriptionOccurrence";
    public static final String PERSON_REGISTERING = "personRegistering";
    public static final String PERSON_DESIGNATION = "personDesignation";
    public static final String PERSON_PHONE = "personPhone";
    public static final String REPORT_DATE_TIME = "reportDateTime";
    public static final String REPORTING_USER = "reportingUser";
    public static final String SOURCE_INFORMATION = "sourceInformation";
    public static final String RESPONSIBLE_USER = "responsibleUser";
    public static final String EBS_LONGITUDE = "ebsLongitude";
    public static final String EBS_LATITUDE = "ebsLatitude";
    public static final String EBS_LATLONG = "ebsLatLon";
    public static final String SORMAS_TO_SORMAS_ORIGIN_INFO = "sormasToSormasOriginInfo";
    public static final String SORMAS_TO_SORMAS_SHARES = "sormasToSormasShares";
    public static final String MANUAL_SCANNING_TYPE = "manualScanningType";
    public static final String AUTOMATIC_SCANNING_TYPE = "automaticScanningType";
    public static final String SCANNING_TYPE = "scanningType";
    public static final String OTHER = "other";
    public static final String EBS_LOCATION = "ebsLocation";
    public static final String CASES = "cases";
    public static final String CATEGORY_OF_INFORMANT = "categoryOfInformant";
    public static final String TRIAGING = "triaging";
    public static final String OTHER_INFORMANT = "otherInformant";


    @DatabaseField
    private String informantName;

    @DatabaseField
    private String informantTel;

    @DatabaseField(dataType = DataType.DATE_LONG)
    private Date triageDate;

    @DatabaseField(dataType = DataType.DATE_LONG)
    private Date endDate;

    @DatabaseField(dataType = DataType.DATE_LONG)
    private Date reportDateTime;

    @DatabaseField(dataType = DataType.DATE_LONG)
    private Date dateOnset;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private User reportingUser;

    @DatabaseField
    @Enumerated(EnumType.STRING)
    private EbsSourceType sourceInformation;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private User responsibleUser;

    @DatabaseField
    private String sourceName;

    @DatabaseField
    private String sourceUrl;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "ebslocation_id")
    private Location ebsLocation;

    @DatabaseField
    private Double ebsLongitude;

    @DatabaseField
    private Double ebsLatitude;

    @DatabaseField
    private Double ebsLatLon;

    @DatabaseField
    private String descriptionOccurrence;

    @DatabaseField
    private String personRegistering;

    @DatabaseField
    private String personDesignation;

    @DatabaseField
    private String personPhone;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private SormasToSormasOriginInfo sormasToSormasOriginInfo;

    @DatabaseField
    private String internalToken;

    @DatabaseField
    @Enumerated(EnumType.STRING)
    private AutomaticScanningType automaticScanningType;

    @DatabaseField
    @Enumerated(EnumType.STRING)
    private ManualScanningType manualScanningType;

    @DatabaseField
    @Enumerated(EnumType.STRING)
    private MediaScannningType scanningType;

    @DatabaseField
    private String other;

    @Enumerated(EnumType.STRING)
    private PersonReporting categoryOfInformant;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private SignalVerification signalVerification;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Triaging triaging;

    @DatabaseField
    private String otherInformant;

    public String getInformantName() {
        return informantName;
    }

    public void setInformantName(String informantName) {
        this.informantName = informantName;
    }

    public String getInformantTel() {
        return informantTel;
    }

    public void setInformantTel(String informantTel) {
        this.informantTel = informantTel;
    }

    public Date getTriageDate() {
        return triageDate;
    }

    public void setTriageDate(Date triageDate) {
        this.triageDate = triageDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getReportDateTime() {
        return reportDateTime;
    }

    public void setReportDateTime(Date reportDateTime) {
        this.reportDateTime = reportDateTime;
    }

    public Date getDateOnset() {
        return dateOnset;
    }

    public void setDateOnset(Date dateOnset) {
        this.dateOnset = dateOnset;
    }

    public User getReportingUser() {
        return reportingUser;
    }

    public void setReportingUser(User reportingUser) {
        this.reportingUser = reportingUser;
    }

    public EbsSourceType getSourceInformation() {
        return sourceInformation;
    }

    public void setSourceInformation(EbsSourceType sourceInformation) {
        this.sourceInformation = sourceInformation;
    }

    public User getResponsibleUser() {
        return responsibleUser;
    }

    public void setResponsibleUser(User responsibleUser) {
        this.responsibleUser = responsibleUser;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Location getEbsLocation() {
        return ebsLocation;
    }

    public void setEbsLocation(Location ebsLocation) {
        this.ebsLocation = ebsLocation;
    }

    public Double getEbsLongitude() {
        return ebsLongitude;
    }

    public void setEbsLongitude(Double ebsLongitude) {
        this.ebsLongitude = ebsLongitude;
    }

    public Double getEbsLatitude() {
        return ebsLatitude;
    }

    public void setEbsLatitude(Double ebsLatitude) {
        this.ebsLatitude = ebsLatitude;
    }

    public Double getEbsLatLon() {
        return ebsLatLon;
    }

    public void setEbsLatLon(Double ebsLatLon) {
        this.ebsLatLon = ebsLatLon;
    }

    public String getDescriptionOccurrence() {
        return descriptionOccurrence;
    }

    public void setDescriptionOccurrence(String descriptionOccurrence) {
        this.descriptionOccurrence = descriptionOccurrence;
    }

    public String getPersonRegistering() {
        return personRegistering;
    }

    public void setPersonRegistering(String personRegistering) {
        this.personRegistering = personRegistering;
    }

    public String getPersonDesignation() {
        return personDesignation;
    }

    public void setPersonDesignation(String personDesignation) {
        this.personDesignation = personDesignation;
    }

    public String getPersonPhone() {
        return personPhone;
    }

    public void setPersonPhone(String personPhone) {
        this.personPhone = personPhone;
    }

    public SormasToSormasOriginInfo getSormasToSormasOriginInfo() {
        return sormasToSormasOriginInfo;
    }

    public void setSormasToSormasOriginInfo(SormasToSormasOriginInfo sormasToSormasOriginInfo) {
        this.sormasToSormasOriginInfo = sormasToSormasOriginInfo;
    }

    public String getInternalToken() {
        return internalToken;
    }

    public void setInternalToken(String internalToken) {
        this.internalToken = internalToken;
    }

    public AutomaticScanningType getAutomaticScanningType() {
        return automaticScanningType;
    }

    public void setAutomaticScanningType(AutomaticScanningType automaticScanningType) {
        this.automaticScanningType = automaticScanningType;
    }

    public ManualScanningType getManualScanningType() {
        return manualScanningType;
    }

    public void setManualScanningType(ManualScanningType manualScanningType) {
        this.manualScanningType = manualScanningType;
    }

    public MediaScannningType getScanningType() {
        return scanningType;
    }

    public void setScanningType(MediaScannningType scanningType) {
        this.scanningType = scanningType;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public PersonReporting getCategoryOfInformant() {
        return categoryOfInformant;
    }

    public void setCategoryOfInformant(PersonReporting categoryOfInformant) {
        this.categoryOfInformant = categoryOfInformant;
    }

    public String getOtherInformant() {
        return otherInformant;
    }

    public void setOtherInformant(String otherInformant) {
        this.otherInformant = otherInformant;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    public SignalVerification getSignalVerification() {
        return signalVerification;
    }

    public void setSignalVerification(SignalVerification signalVerification) {
        this.signalVerification = signalVerification;
    }
    @Override
    public String getI18nPrefix() {
        return I18N_PREFIX;
    }

    public Triaging getTriaging() {
        return triaging;
    }

    public void setTriaging(Triaging triaging) {
        this.triaging = triaging;
    }
}
