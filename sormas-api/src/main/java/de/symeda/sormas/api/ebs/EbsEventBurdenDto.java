/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.EbsEvent;
import de.symeda.sormas.api.infrastructure.region.RegionDto;
import java.io.Serializable;
import java.util.Date;

public class EbsEventBurdenDto implements Serializable {
	private static final long serialVersionUID = 2430932452606853497L;

	public static final String I18N_PREFIX = "EbsEventBurden";

	public static final String EBSEVENT = "ebsEvent";
	public static final String EBSEVENT_COUNT = "ebsEventCount";
	public static final String PREVIOUS_EBSEVENT_COUNT = "previousEbsEventCount";
	public static final String EBSEVENTS_DIFFERENCE = "ebsEventsDifference";
	public static final String EBSEVENTS_DIFFERENCE_PERCENTAGE = "ebsEventsDifferencePercentage";
	public static final String EVENT_COUNT = "eventCount";
	public static final String OUTBREAK_LASTREPORTEDDISTRICTNAME_COUNT = "outbreakLastReportedDistrictNameCount";
	public static final String EBSEVENT_SOURCE = "ebsEventSource";
	public static final String EBSEVENT_SOURCE_RATE = "ebsEventSourceRate" ;
	public static final String LAST_REPORTED_DISTRICT_NAME = "lastReportedDistrictName";

	private EbsEvent ebsEvent;
	private String total;
	private String totalCount;

	private Long ebsEventCount;
	private Long previousEbsEventCount;
	private Date lastReportedDate;
	private Long outbreakLastReportedDistrictNameCount;
	private EbsSourceType ebsEventSource;
	private String lastReportedLastReportedDistrictNameName;

	private Integer cfr;
	private String LastReportedDistrictName;
	private String outbreakLastReportedDistrictName;

	private String deaths;
	private String source;


	private RegionDto region;

	private String recovered;
	private String recoveredCount;

	private String activeEbsEvents;
	private String activeCount;

	private String other;
	private String otherCount;
	private Date from;
	private Date to;

	private String  lastReportedDistrictName;

	public EbsEventBurdenDto(
			EbsEvent ebsEvent,
			Long ebsEventCount,
			Long previousEbsEventCount,
			Date lastReportedDate,
			EbsSourceType ebsEventSource,
			String  lastReportedDistrictName
) {

		this.ebsEvent = ebsEvent;
		this.ebsEventCount = ebsEventCount;
		this.previousEbsEventCount = previousEbsEventCount;
		this.lastReportedDate = lastReportedDate;
		this.ebsEventSource = ebsEventSource;
		this.lastReportedDistrictName=lastReportedDistrictName;

	}

	public String getLastReportedDistrictName() {
		return lastReportedDistrictName;
	}

	public void setLastReportedDistrictName(String lastReportedDistrictName) {
		this.lastReportedDistrictName = lastReportedDistrictName;
	}

	public EbsEvent getEbsEvent() {
		return ebsEvent;
	}

	public void setEbsEvent(EbsEvent ebsEvent) {
		this.ebsEvent = ebsEvent;
	}


	public Long getEbsEventCount() {
		return ebsEventCount;
	}

	public void setEbsEventCount(Long ebsEventCount) {
		this.ebsEventCount = ebsEventCount;
	}

	public Long getPreviousEbsEventCount() {
		return previousEbsEventCount;
	}

	public void setPreviousEbsEventCount(Long previousEbsEventCount) {
		this.previousEbsEventCount = previousEbsEventCount;
	}

	public Long getEbsEventsDifference() {
		return getEbsEventCount() - getPreviousEbsEventCount();
	}

	public Float getEbsEventsDifferencePercentage() {
		float percentage = 0f;

		if (getPreviousEbsEventCount() == 0 && getEbsEventCount() > 0)
			percentage = 100f;
		else if (getEbsEventCount() == 0 && getPreviousEbsEventCount() > 0)
			percentage = -100f;
		else
			percentage = (float) getEbsEventsDifference() / (float) (getPreviousEbsEventCount() == 0 ? 1 : getPreviousEbsEventCount()) * 100;

		return Math.round(percentage * 10) / 10.0f;
	}

	public Date getLastReportedDate() {
		return lastReportedDate;
	}

	public void setLastReportedDate(Date lastReportedDate) {
		this.lastReportedDate = lastReportedDate;
	}

	public Long getOutbreakLastReportedDistrictNameCount() {
		return outbreakLastReportedDistrictNameCount;
	}

	public void setOutbreakLastReportedDistrictNameCount(Long outbreakLastReportedDistrictNameCount) {
		this.outbreakLastReportedDistrictNameCount = outbreakLastReportedDistrictNameCount;
	}

	public EbsSourceType getEbsEventSource() {
		return ebsEventSource;
	}

	public void setEbsEventSource(EbsSourceType ebsEventSource) {
		this.ebsEventSource = ebsEventSource;
	}

	public float getEbsEventFatalityRate() {

//		float cfrPercentage = 100f * ((float) getEbsEventSource() / (float) (getEbsEventCount() == 0 ? 1 : getEbsEventCount()));
//		cfrPercentage = Math.round(cfrPercentage * 100) / 100f;
//		return cfrPercentage;
		return 0L;
	}

	public String getLastReportedLastReportedDistrictNameName() {
		return lastReportedLastReportedDistrictNameName;
	}

	public void setLastReportedLastReportedDistrictNameName(String name) {
		this.lastReportedLastReportedDistrictNameName = name;
	}

	public Boolean hasCount() {
		return (ebsEventCount + previousEbsEventCount ) > 0;
	}

	public Integer getCfr() {
		return cfr;
	}

	public void setCfr(Integer cfr) {
		this.cfr = cfr;
	}

	public String getOutbreakLastReportedDistrictName() {
		return outbreakLastReportedDistrictName;
	}

	public void setOutbreakLastReportedDistrictName(String outbreakLastReportedDistrictName) {
		this.outbreakLastReportedDistrictName = outbreakLastReportedDistrictName;
	}

	public String getDeaths() {
		return deaths;
	}

	public void setDeaths(String deaths) {
		this.deaths = deaths;
	}

	public RegionDto getRegion() {
		return region;
	}

	public void setRegion(RegionDto region) {
		this.region = region;
	}

	public String getRecovered() {
		return recovered;
	}

	public void setRecovered(String recovered) {
		this.recovered = recovered;
	}

	public String getActiveEbsEvents() {
		return activeEbsEvents;
	}

	public void setActiveEbsEvents(String activeEbsEvents) {
		this.activeEbsEvents = activeEbsEvents;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}


	public Date getTo() {
		return to;
	}


	public void setTo(Date to) {
		this.to = to;
	}


	public Date getFrom() {
		return from;
	}


	public void setFrom(Date from) {
		this.from = from;
	}



	public String getTotalCount() {
		return totalCount;
	}


	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}


	public String getSource() {
		return source;
	}


	public void setSource(String source) {
		this.source = source;
	}


	public String getRecoveredCount() {
		return recoveredCount;
	}


	public void setRecoveredCount(String recoveredCount) {
		this.recoveredCount = recoveredCount;
	}





	public String getActiveCount() {
		return activeCount;
	}


	public void setActiveCount(String activeCount) {
		this.activeCount = activeCount;
	}


	public String getOther() {
		return other;
	}


	public void setOther(String other) {
		this.other = other;
	}
	
	
	


	public String getOtherCount() {
		return otherCount;
	}


	public void setOtherCount(String otherCount) {
		this.otherCount = otherCount;
	}


	@Override
	public String toString() {
		return "EbsEventBurdenDto [ebsEvent=" + ebsEvent + ", total=" + total + ", ebsEventCount=" + ebsEventCount
				+ ", previousEbsEventCount=" + previousEbsEventCount + ", eventCount=" + lastReportedDate + ", outbreakLastReportedDistrictNameCount="
				+ outbreakLastReportedDistrictNameCount + ", ebsEventSource=" + ebsEventSource + ", lastReportedLastReportedDistrictNameName="
				+ lastReportedLastReportedDistrictNameName  + ", cfr=" + cfr
				+ ", lastReportedDistrictName=" + lastReportedDistrictName + ", outbreakLastReportedDistrictName=" + outbreakLastReportedDistrictName
				+ ", deaths=" + deaths + ", region=" + region + ", recovered=" + recovered + ", activeEbsEvents="
				+ activeEbsEvents + ", to=" + to + ", from=" + from + "]";
	}




//	public EbsEventBurdenDto getEbsEventGridForDashboard(RegionReferenceDto reference, Object object, EbsEvent ebsEvent2,
//			Date fromDate, Date toDate, Date previousFromDate, Date previousToDate) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
