/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2022 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.api.ebs;


import de.symeda.sormas.api.utils.HasCaption;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableIndexDto;

import java.io.Serializable;
import java.util.Date;

public class EbsIndexDto extends PseudonymizableIndexDto {

	private static final long serialVersionUID = 8322646404033924938L;

	public static final String I18N_PREFIX = "Ebs";

	public static final String UUID = "uuid";
	public static final String TRIAGE_DATE = "triageDate";
	public static final String EBS_LOCATION = "ebsLocation";
	public static final String SRC_TYPE = "srcType";
	public static final String TRIAGING_DECISION = "triagingDecision";
	public static final String REPORT_DATE_TIME = "reportDateTime";
	public static final String REPORTING_USER = "reportingUser";
	public static final String CONTACT_NAME = "contactName";
	public static final String CONTACT_PHONE_NUMBER = "contactPhoneNumber";
	public static final String SIGNAL_CATEGORY = "signalCategory";
	public static final String VERIFIED = "verified";
	public static final String CASES = "cases";
	public static final String DEATH = "death";
	public static final String PERSON_REPORTING = "personReporting";



	public static final String TRIAGING_DECISION_DATE = "triagingDecisionDate";

	private Long id;
	/**
	 * number of contacts whose person is involved in this event, and where the source case is also part of the event
	 */
	private Date triageDate;
	private EbsSourceType srcType;
	private EbsTriagingDecision triagingDecision;
	private Date reportDateTime;
	private PersonReporting personReporting;
	private String contactName;
	private String contactPhoneNumber;
	private SignalCategory signalCategory;
	private String verified;
	private String cases;
	private String death;
	private EbsIndexTriagingDecisionDate triagingDecisionDate;


	public EbsIndexDto(
		Long id,
		String uuid,
		Date triageDate,
		EbsSourceType srcType,
		EbsTriagingDecision triagingDecision,
		Date reportDateTime,
		PersonReporting personReporting,
		String contactName,
		String contactPhoneNumber,
		SignalCategory signalCategory,
		String verified,
		String cases,
		String death,
		Date decisionDate
		) {

		super(uuid);
		this.id = id;
		this.triageDate = triageDate;
		this.srcType = srcType;
		this.triagingDecision = triagingDecision;
		this.reportDateTime = reportDateTime;
		this.personReporting = personReporting;
		this.contactName = contactName;
		this.contactPhoneNumber = contactPhoneNumber;
		this.signalCategory = signalCategory;
		this.verified = verified;
		this.cases = cases;
		this.death = death;
		this.triagingDecisionDate = new EbsIndexTriagingDecisionDate(decisionDate);
	}

	public EbsIndexDto(String uuid) {
		super(uuid);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTriageDate() {
		return triageDate;
	}

	public void setTriageDate(Date triageDate) {
		this.triageDate = triageDate;
	}

	public EbsSourceType getSrcType() {
		return srcType;
	}

	public void setSrcType(EbsSourceType srcType) {
		this.srcType = srcType;
	}

	public EbsTriagingDecision getTriagingDecision() {
		return triagingDecision;
	}

	public void setTriagingDecision(EbsTriagingDecision triagingDecision) {
		this.triagingDecision = triagingDecision;
	}

	public Date getReportDateTime() {
		return reportDateTime;
	}

	public void setReportDateTime(Date reportDateTime) {
		this.reportDateTime = reportDateTime;
	}

//	public UserReferenceDto getReportingUser() {
//		return reportingUser;
//	}
//
//	public void setReportingUser(UserReferenceDto reportingUser) {
//		this.reportingUser = reportingUser;
//	}

	public Date getDecisionDate() {
		return getTriagingDecisionDate().getDecisionDate();
	}
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhoneNumber() {
		return contactPhoneNumber;
	}

	public void setContactPhoneNumber(String contactPhoneNumber) {
		this.contactPhoneNumber = contactPhoneNumber;
	}

	public SignalCategory getSignalCategory() {
		return signalCategory;
	}

	public void setSignalCategory(SignalCategory signalCategory) {
		this.signalCategory = signalCategory;
	}

	public EbsReferenceDto toReference() {
		return new EbsReferenceDto(getUuid(), getTriageDate());
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	public String getCases() {
		return cases;
	}

	public void setCases(String cases) {
		this.cases = cases;
	}

	public String getDeath() {
		return death;
	}

	public void setDeath(String death) {
		this.death = death;
	}

	public PersonReporting getPersonReporting() {
		return personReporting;
	}

	public void setPersonReporting(PersonReporting personReporting) {
		this.personReporting = personReporting;
	}

	public EbsIndexTriagingDecisionDate getTriagingDecisionDate() {
		return triagingDecisionDate;
	}

	public Date getDecisionDateCaption() {
		return getTriagingDecisionDate().getDecisionDate();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		EbsIndexDto that = (EbsIndexDto) o;

		return getUuid().equals(that.getUuid());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getUuid() == null) ? 0 : getUuid().hashCode());
		return result;
	}

	public static class EbsIndexTriagingDecisionDate implements Serializable, HasCaption {

		private Date decisionDate;

		public EbsIndexTriagingDecisionDate(
				Date decisionDate) {
			this.decisionDate = decisionDate;
		}

		public Date getDecisionDate() {
			return decisionDate;
		}

		public String buildCaption() {
			return TriagingReferenceDto.buildCaption(decisionDate);
		}
	}
}
