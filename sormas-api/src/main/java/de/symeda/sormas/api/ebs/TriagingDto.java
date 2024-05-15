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

import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.utils.*;


import java.util.Date;
import java.util.Set;

@DependingOnFeatureType(featureType = FeatureType.EVENT_SURVEILLANCE)
public class TriagingDto extends EntityDto {

	private static final long serialVersionUID = 2430932452606853497L;

	public static final long APPROXIMATE_JSON_SIZE_IN_BYTES = 13356;

	public static final String I18N_PREFIX = "Triaging";

	public static final String EARLY_WARNING = "earlyWarning";
	public static final String SPECIFIC_SIGNAL = "specificSignal";
	public static final String HEALTH_CONCERN = "healthConcern";
	public static final String SIGNAL_CATEGORY = "signalCategory";
	public static final String CATEGORY_DETAILS = "categoryDetails";
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
	private YesNo occurrencePreviously;
	private Set<EbsTriagingDecision> triagingDecision;
	private Date decisionDate;
	private String referredTo;
	private UserReferenceDto responsibleUser;
	private String categoryDetailsString;
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

	public Set<CategoryDetails> getCategoryDetails() {
		return categoryDetails;
	}

	public void setCategoryDetails(Set<CategoryDetails> categoryDetails) {
		this.categoryDetails = categoryDetails;
	}

	public YesNo getOccurrencePreviously() {
		return occurrencePreviously;
	}

	public void setOccurrencePreviously(YesNo occurrencePreviously) {
		this.occurrencePreviously = occurrencePreviously;
	}

	public Set<EbsTriagingDecision> getTriagingDecision() {
		return triagingDecision;
	}

	public void setTriagingDecision(Set<EbsTriagingDecision> triagingDecision) {
		this.triagingDecision = triagingDecision;
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

	public UserReferenceDto getResponsibleUser() {
		return responsibleUser;
	}

	public void setResponsibleUser(UserReferenceDto responsibleUser) {
		this.responsibleUser = responsibleUser;
	}

	public String getCategoryDetailsString() {
		return categoryDetailsString;
	}

	public void setCategoryDetailsString(String categoryDetailsString) {
		this.categoryDetailsString = categoryDetailsString;
	}

	public OutComeSupervisor getOutcomeSupervisor() {
		return outcomeSupervisor;
	}

	public void setOutcomeSupervisor(OutComeSupervisor outcomeSupervisor) {
		this.outcomeSupervisor = outcomeSupervisor;
	}
}
