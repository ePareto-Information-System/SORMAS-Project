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


import de.symeda.sormas.api.location.LocationReferenceDto;
import de.symeda.sormas.api.utils.HasCaption;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.api.utils.pseudonymization.PseudonymizableIndexDto;

import java.io.Serializable;
import java.util.Date;

public class EbsIndexDto extends PseudonymizableIndexDto {

	private static final long serialVersionUID = 8322646404033924938L;

	public static final String I18N_PREFIX = "Ebs";

	public static final String UUID = "uuid";
	public static final String TRIAGE_DATE = "triageDate";
	public static final String EBS_LOCATION = "ebsLocation";
	public static final String SOURCE_INFORMATION = "sourceInformation";
	public static final String TRIAGING_DECISION = "triagingDecision";
	public static final String REPORT_DATE_TIME = "reportDateTime";
	public static final String CHANGE_DATE = "changeDate";
	public static final String REPORTING_USER = "reportingUser";
	public static final String INFORMANT_NAME = "informantName";
	public static final String INFORMANT_TEL = "informantTel";
	public static final String SIGNAL_CATEGORY = "signalCategory";
	public static final String VERIFIED = "verified";
	public static final String CASES = "cases";
	public static final String DEATH = "death";
	public static final String CATEGORY_OF_INFORMANT = "categoryOfInformant";
	public static final String PERSON_REGISTERING = "personRegistering";
	public static final String PERSON_DESIGNATION = "personDesignation";


	public static final String TRIAGING_DECISION_DATE = "triagingDecisionDate";
	public static final String VERIFICATION_SENT = "verificationSent";
	public static final String VERIFIED_DATE = "verifiedDate";
	public static final String RISK_STATUS = "riskStatus";
	public static final String ACTION_INITIATED = "actionInitiated";
	public static final String RESPONSE_STATUS = "responseStatus";
	public static final String REGION = "region";
	public static final String DISTRICT = "district";
	public static final String COMMUNITY = "community";
	public static final String TOWN = "address";

	private Long id;
	/**
	 * number of contacts whose person is involved in this event, and where the source case is also part of the event
	 */
	private Date triageDate;
	private EbsSourceType sourceInformation;
	private EbsTriagingDecision triagingDecision;
	private Date reportDateTime;
	private Date changeDate;
	private PersonReporting categoryOfInformant;
	private String informantName;
	private String informantTel;
	private SignalCategory signalCategory;
	private EbsVerified verified;
	private EbsDeath death;
	private EbsIndexTriagingDecisionDate triagingDecisionDate;
	private String personRegistering;
	private String personDesignation;
	private EbsVerificationSent verificationSent;
	private EbsVerifiedDate verifiedDate;
	private EbsRiskStatus riskStatus;
	private EbsActionInitiated actionInitiated;
	private EbsResponseStatus responseStatus;
	private EbsLocation ebsLocation;
	private String regionUuid;


	public EbsIndexDto(
			Long id,
			String uuid,
			Date triageDate,
			EbsSourceType sourceInformation,
			EbsTriagingDecision triagingDecision,
			Date reportDateTime,
			Date changeDate,
			PersonReporting categoryOfInformant,
			String informantName,
			String informantTel,
			SignalCategory signalCategory,
			SignalOutcome verified,
			String death,
			Date decisionDate,
			String personRegistering,
			String personDesignation,
			YesNo verificationSent,
			Date verifiedDate,
			RiskAssesment riskStatus,
			YesNo actionInitiated,
			ResponseStatus responseStatus,
			String regionUuid,
			String regionName,
			String communityUuid,
			String communityName,
			String city,
			String districtUuid,
			String districtName
	) {

		super(uuid);
		this.id = id;
		this.triageDate = triageDate;
		this.sourceInformation = sourceInformation;
		this.triagingDecision = triagingDecision;
		this.reportDateTime = reportDateTime;
		this.changeDate = changeDate;
		this.categoryOfInformant = categoryOfInformant;
		this.informantName = informantName;
		this.informantTel = informantTel;
		this.signalCategory = signalCategory;
		this.verified = new EbsVerified(verified);
		this.death = new EbsDeath(death);
		this.triagingDecisionDate = new EbsIndexTriagingDecisionDate(decisionDate);
		this.personDesignation = personDesignation;
		this.personRegistering = personRegistering;
		this.verificationSent = new EbsVerificationSent(verificationSent);
		this.verifiedDate = new EbsVerifiedDate(verifiedDate);
		this.riskStatus = new EbsRiskStatus(riskStatus);
		this.actionInitiated = new EbsActionInitiated(actionInitiated);
		this.responseStatus = new EbsResponseStatus(responseStatus);
		this.ebsLocation = new EbsLocation(regionName, communityName, city,districtName);
		this.regionUuid = regionUuid;
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

	public EbsSourceType getSourceInformation() {
		return sourceInformation;
	}

	public void setSourceInformation(EbsSourceType sourceInformation) {
		this.sourceInformation = sourceInformation;
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
	public Date getChangeDate() {
			return changeDate;
		}

	public void setChangeDate(Date changeDate) {
			this.changeDate = changeDate;
		}

	public Date getDecisionDate() {
		return getTriagingDecisionDate().getDecisionDate();
	}

	public YesNo getVerificationSent() {
		return getExtensionVerificationSent().getVerificationSent();
	}

	public Date getVerifiedDate() {
		return getEbsVerifiedDate().getVerifiedDate();
	}

	public RiskAssesment getRiskStatus() {
		return getEbsRiskStatus().getRiskStatus();
	}

	public YesNo getResponseAction() {
		return getEbsActionInitiated().getActionInitiated();
	}

	public ResponseStatus getResponseStatus() {
		return getEbsResponseStatus().getResponseStatus();
	}
	public String getDeath() {
		return getEbsDeath().getDeath();
	}

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

	public SignalCategory getSignalCategory() {
		return signalCategory;
	}

	public void setSignalCategory(SignalCategory signalCategory) {
		this.signalCategory = signalCategory;
	}

	public EbsReferenceDto toReference() {
		return new EbsReferenceDto(getUuid(), getTriageDate());
	}

	public SignalOutcome getVerified() {
		return getEbsVerified().getVerified();
	}


	public EbsVerified getEbsVerified() {
		return verified;
	}

	public EbsDeath getEbsDeath() {
		return death;
	}

	public PersonReporting getCategoryOfInformant() {
		return categoryOfInformant;
	}

	public void setCategoryOfInformant(PersonReporting categoryOfInformant) {
		this.categoryOfInformant = categoryOfInformant;
	}

	public EbsIndexTriagingDecisionDate getTriagingDecisionDate() {
		return triagingDecisionDate;
	}

	public EbsVerificationSent getExtensionVerificationSent() {
		return verificationSent;
	}

	public EbsVerifiedDate getEbsVerifiedDate() {
		return verifiedDate;
	}

	public EbsRiskStatus getEbsRiskStatus() {
		return riskStatus;
	}

	public EbsResponseStatus getEbsResponseStatus() {
		return responseStatus;
	}

	public EbsActionInitiated getEbsActionInitiated() {return actionInitiated;}

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

	public Date getDecisionDateCaption() {
		return getTriagingDecisionDate().getDecisionDate();
	}

	public void setDeath(EbsDeath death) {
		this.death = death;
	}

	public String getRegion() {
		return getEbsLocation().getRegion();
	}

	public String getRegionUuid() {
		return regionUuid;
	}

	public EbsLocation getEbsLocation() {
		return ebsLocation;
	}

	public String getCommunity() {
		return getEbsLocation().getCommunity();
	}

	public String getAddress() {
		return getEbsLocation().getAddress();
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

		private final Date decisionDate;

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

	public static class EbsVerificationSent implements Serializable, HasCaption {

		private final YesNo verificationSent;

		public EbsVerificationSent(YesNo verificationSent) {
			this.verificationSent = verificationSent;
		}

		public YesNo getVerificationSent() {
			return verificationSent;
		}

		public String buildCaption() {
			return SignalVerificationReferenceDto.buildCaption(verificationSent);
		}
	}

	public static class EbsVerified implements Serializable, HasCaption {

		private final SignalOutcome verified;

		public EbsVerified(SignalOutcome verified) {
			this.verified = verified;
		}

		public SignalOutcome getVerified() {
			return verified;
		}

		public String buildCaption() {
			return SignalVerificationReferenceDto.buildCaption(verified);
		}
	}

	public static class EbsVerifiedDate implements Serializable, HasCaption {
		private final Date verfiedDate;

		public EbsVerifiedDate(Date verifiedDate) {
			this.verfiedDate = verifiedDate;
		}

		public Date getVerifiedDate() {
			return verfiedDate;
		}

		public String buildCaption() {
			return SignalVerificationReferenceDto.buildCaption(verfiedDate);
		}
	}

	public static class EbsRiskStatus implements Serializable, HasCaption {
		private final RiskAssesment riskStatus;

		public EbsRiskStatus(RiskAssesment riskStatus) {
			this.riskStatus = riskStatus;
		}

		public RiskAssesment getRiskStatus() {
			return riskStatus;
		}

		public String buildCaption() {
			return RiskAssessmentReferenceDto.buildCaption(riskStatus);
		}
	}

	public static class EbsActionInitiated implements Serializable, HasCaption {

		private final YesNo actionInitiated;

		public EbsActionInitiated(YesNo actionInitiated) {
			this.actionInitiated = actionInitiated;
		}

		public YesNo getActionInitiated() {
			return actionInitiated;
		}

		public String buildCaption() {
			return AlertReferenceDto.buildCaption(actionInitiated);
		}
	}

	public static class EbsResponseStatus implements Serializable, HasCaption {
		private final ResponseStatus responseStatus;

		public EbsResponseStatus(ResponseStatus responseStatus) {
			this.responseStatus = responseStatus;
		}

		public ResponseStatus getResponseStatus() {
			return responseStatus;
		}

		public String buildCaption() {
			return AlertReferenceDto.buildCaption(responseStatus);
		}
	}

	public static class EbsDeath implements Serializable, HasCaption {
		private final String death;

		public EbsDeath(String death) {
			this.death = death;
		}

		public String getDeath() {
			return death;
		}

		public String buildCaption() {
			return SignalVerificationReferenceDto.buildCaption(death);
		}
	}
	public static class EbsLocation implements Serializable, HasCaption {

		private String regionName;
		private String communityName;
		private String city;
		private String districtName;

		public EbsLocation(
				String regionName,
				String communityName,
				String city,
				String districtName
				) {
			this.regionName = regionName;
			this.communityName = communityName;
			this.city = city;
			this.districtName = districtName;
		}

		public String getRegion() {
			return regionName;
		}

		public String getDistrict() {
			return districtName;
		}

		public String getCommunity() {
			return communityName;
		}

		public String getAddress() {
			return LocationReferenceDto.buildCaption(city);
		}

		public String buildCaption() {
			return LocationReferenceDto.buildCaption(regionName,districtName, communityName, city,null,null,null);
		}
	}
}
