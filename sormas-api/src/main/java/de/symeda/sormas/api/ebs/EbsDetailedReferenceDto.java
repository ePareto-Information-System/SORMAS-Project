package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.event.EventReferenceDto;
import de.symeda.sormas.api.event.EventStatus;

import java.util.Date;

public class EbsDetailedReferenceDto extends EbsReferenceDto {

	private String ebsTitle;
	private Date reportDateTime;

	public EbsDetailedReferenceDto(String uuid, String caption, Date reportDateTime) {
		super(uuid, caption);
		this.reportDateTime = reportDateTime;
	}


	public String getEbsTitle() {
		return ebsTitle;
	}

	public void setEbsTitle(String ebsTitle) {
		this.ebsTitle = ebsTitle;
	}

	public Date getReportDateTime() {
		return reportDateTime;
	}

	public void setReportDateTime(Date reportDateTime) {
		this.reportDateTime = reportDateTime;
	}
}
