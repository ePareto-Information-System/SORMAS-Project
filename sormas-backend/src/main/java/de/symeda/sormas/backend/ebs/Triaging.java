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

package de.symeda.sormas.backend.ebs;


import de.symeda.sormas.api.ebs.*;
import de.symeda.sormas.api.utils.YesNo;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.user.User;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "triaging")
public class Triaging extends AbstractDomainObject {

	public static final String TABLE_NAME = "triaging";

	private static final long serialVersionUID = 2430932452606853497L;

	public static final long APPROXIMATE_JSON_SIZE_IN_BYTES = 13356;

	public static final String I18N_PREFIX = "Triaging";

	public static final String EARLY_WARNING = "earlyWarning";
	public static final String SPECIFIC_SIGNAL = "specificSignal";
	public static final String HEALTH_CONCERN = "healthConcern";
	public static final String SIGNAL_CATEGORY = "signalCategory";
	public static final String RESPIRATORY_SYMPTOMS = "respiratorySymptoms";
	public static final String SEVERE_ILLNESS = "severeIllness";
	public static final String SEVERE_DIARRHOEA = "severeDiarrhoea";
	public static final String ORIFICES = "orifices";
	public static final String UNABLE_TO_WALK = "unableToWalk";
	public static final String OTC = "otc";
	public static final String PREGNANT_WOMAN = "pregnantWoman";
	public static final String HANGING_WORM = "hangingWorm";
	public static final String OCCURRENCE_PREVIOUSLY = "occurrencePreviously";
	public static final String TRIAGING_DECISION = "triagingDecision";
	public static final String DATE_OF_DECISION = "decisionDate";
	public static final String REFERRED_TO = "referredTo";
	public static final String RESPONSIBLE_USER = "responsibleUser";
	public static final String OUTCOME_SUPERVISOR = "outcomeSupervisor";



	private YesNo earlyWarning;
	private YesNo specificSignal;
	private SignalCategory signalCategory;
	private YesNo healthConcern;
	private Set<CategoryDetails> categoryDetails;
	private String categoryDetailsString;



	private YesNo occurrencePreviously;
	private Set<EbsTriagingDecision> triagingDecision;
	private String triagingDecisionString;
	private Date decisionDate;
	private String referredTo;
	private User responsibleUser;
	private OutComeSupervisor outcomeSupervisor;

	public EbsReferenceDto toReference() {
		return new EbsReferenceDto(getUuid());
	}


	public YesNo getEarlyWarning() {
		return earlyWarning;
	}

	public void setEarlyWarning(YesNo earlyWarning) {
		this.earlyWarning = earlyWarning;
	}

	public YesNo getSpecificSignal() {
		return specificSignal;
	}

	public void setSpecificSignal(YesNo specificSignal) {
		this.specificSignal = specificSignal;
	}

	public SignalCategory getSignalCategory() {
		return signalCategory;
	}

	public void setSignalCategory(SignalCategory signalCategory) {
		this.signalCategory = signalCategory;
	}

	public YesNo getHealthConcern() {
		return healthConcern;
	}

	public void setHealthConcern(YesNo healthConcern) {
		this.healthConcern = healthConcern;
	}

	@Transient
	public Set<CategoryDetails> getCategoryDetails() {
		if (categoryDetails == null) {
			if (StringUtils.isEmpty(categoryDetailsString)) {
				categoryDetails = new HashSet<>();
			} else {
				categoryDetails =
						Arrays.stream(categoryDetailsString.split(",")).map(CategoryDetails::valueOf).collect(Collectors.toSet());
			}
		}
		return categoryDetails;
	}

	public void setCategoryDetails(Set<CategoryDetails> categoryDetails) {
		this.categoryDetails = categoryDetails;
		if (this.categoryDetails == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		categoryDetails.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		categoryDetailsString = sb.toString();
	}

	public YesNo getOccurrencePreviously() {
		return occurrencePreviously;
	}

	public void setOccurrencePreviously(YesNo occurrencePreviously) {
		this.occurrencePreviously = occurrencePreviously;
	}

	@Transient
	public Set<EbsTriagingDecision> getTriagingDecision() {
		if (triagingDecision == null) {
			if (StringUtils.isEmpty(triagingDecisionString)) {
				triagingDecision = new HashSet<>();
			} else {
				triagingDecision =
						Arrays.stream(triagingDecisionString.split(",")).map(EbsTriagingDecision::valueOf).collect(Collectors.toSet());
			}
		}
		return triagingDecision;
	}

	public void setTriagingDecision(Set<EbsTriagingDecision> triagingDecision) {
		this.triagingDecision = triagingDecision;
		if (this.triagingDecision == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		triagingDecision.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		triagingDecisionString = sb.toString();
	}

	public Date getDecisionDate() {
		return decisionDate;
	}

	public void setDecisionDate(Date decisionDate) {
		this.decisionDate = decisionDate;
	}

	public String getReferredTo() {
		return referredTo;
	}

	public void setReferredTo(String referredTo) {
		this.referredTo = referredTo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getResponsibleUser() {
		return responsibleUser;
	}

	public void setResponsibleUser(User responsibleUser) {
		this.responsibleUser = responsibleUser;
	}

	public String getCategoryDetailsString() {
		return categoryDetailsString;
	}

	public void setCategoryDetailsString(String categoryDetailsString) {
		this.categoryDetailsString = categoryDetailsString;
	}
	public String getTriagingDecisionString() {
		return triagingDecisionString;
	}

	public void setTriagingDecisionString(String triagingDecisionString) {
		this.triagingDecisionString = triagingDecisionString;
	}

	public OutComeSupervisor getOutcomeSupervisor() {
		return outcomeSupervisor;
	}

	public void setOutcomeSupervisor(OutComeSupervisor outcomeSupervisor) {
		this.outcomeSupervisor = outcomeSupervisor;
	}
}
