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

import javax.persistence.*;
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

	public static final String SUPERVISOR_REVIEW = "supervisorReview";
	public static final String SPECIFIC_SIGNAL = "specificSignal";
	public static final String HEALTH_CONCERN = "healthConcern";
	public static final String SIGNAL_CATEGORY = "signalCategory";
	public static final String HUMAN_COMMUNITY_CATEGORY_DETAILS = "humanCommunityCategoryDetails";
	public static final String HUMAN_FACILITY_CATEGORY_DETAILS = "humanFacilityCategoryDetails";
	public static final String HUMAN_LABORATORY_CATEGORY_DETAILS = "humanLaboratoryCategoryDetails";
	public static final String ANIMAL_COMMUNITY_CATEGORY_DETAILS = "animalCommunityCategoryDetails";
	public static final String ANIMAL_FACILITY_CATEGORY_DETAILS = "animalFacilityCategoryDetails";
	public static final String ANIMAL_LABORATORY_CATEGORY_DETAILS = "animalLaboratoryCategoryDetails";
	public static final String ENVIRONMENTAL_CATEGORY_DETAILS = "environmentalCategoryDetails";
	public static final String POE_CATEGORY_DETAILS = "poeCategoryDetails";
	public static final String OCCURRENCE_PREVIOUSLY = "occurrencePreviously";
	public static final String TRIAGING_DECISION = "triagingDecision";
	public static final String DATE_OF_DECISION = "decisionDate";
	public static final String REFERRED_TO = "referredTo";
	public static final String RESPONSIBLE_USER = "responsibleUser";
	public static final String OUTCOME_SUPERVISOR = "outcomeSupervisor";
	public static final String NOT_SIGNAL = "notSignal";
	public static final String CATEGORY_DETAILS_LEVEL = "categoryDetailsLevel";
	public static final String POTENTIAL_RISK = "potentialRisk";
	public static final String REFERRED = "referred";



	private YesNo supervisorReview;
	private YesNo referred;
	private YesNo specificSignal;
	private SignalCategory signalCategory;
	private YesNo healthConcern;
	private HumanCommunityCategoryDetails humanCommunityCategoryDetails;
	private HumanFaclityCategoryDetails humanFacilityCategoryDetails;
	private HumanLaboratoryCategoryDetails humanLaboratoryCategoryDetails;
	private AnimalCommunityCategoryDetails animalCommunityCategoryDetails;
	private AnimalFacilityCategoryDetails animalFacilityCategoryDetails;
	private AnimalLaboratoryCategoryDetails animalLaboratoryCategoryDetails;
	private EnvironmentalCategoryDetails environmentalCategoryDetails;
	private POE poeCategoryDetails;
	private CategoryDetailsLevel categoryDetailsLevel;




	private YesNo occurrencePreviously;
	private EbsTriagingDecision triagingDecision;
	private String triagingDecisionString;
	private Date decisionDate;
	private String referredTo;
	private User responsibleUser;
	private OutComeSupervisor outcomeSupervisor;
	private boolean notSignal;
	private YesNo potentialRisk;

	public EbsReferenceDto toReference() {
		return new EbsReferenceDto(getUuid());
	}


	public YesNo getSupervisorReview() {
		return supervisorReview;
	}

	public void setSupervisorReview(YesNo earlyWarning) {
		this.supervisorReview = earlyWarning;
	}

	public YesNo getSpecificSignal() {
		return specificSignal;
	}

	public void setSpecificSignal(YesNo specificSignal) {
		this.specificSignal = specificSignal;
	}

	@Enumerated(EnumType.STRING)
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


	public HumanCommunityCategoryDetails getHumanCommunityCategoryDetails() {
		return humanCommunityCategoryDetails;
	}

	public void setHumanCommunityCategoryDetails(HumanCommunityCategoryDetails humanCommunityCategoryDetails) {
		this.humanCommunityCategoryDetails = humanCommunityCategoryDetails;
	}

	public HumanFaclityCategoryDetails getHumanFacilityCategoryDetails() {
		return humanFacilityCategoryDetails;
	}

	public void setHumanFacilityCategoryDetails(HumanFaclityCategoryDetails humanFacilityCategoryDetails) {
		this.humanFacilityCategoryDetails = humanFacilityCategoryDetails;
	}

	public HumanLaboratoryCategoryDetails getHumanLaboratoryCategoryDetails() {
		return humanLaboratoryCategoryDetails;
	}

	public void setHumanLaboratoryCategoryDetails(HumanLaboratoryCategoryDetails humanLaboratoryCategoryDetails) {
		this.humanLaboratoryCategoryDetails = humanLaboratoryCategoryDetails;
	}

	public AnimalCommunityCategoryDetails getAnimalCommunityCategoryDetails() {
		return animalCommunityCategoryDetails;
	}

	public void setAnimalCommunityCategoryDetails(AnimalCommunityCategoryDetails animalCommunityCategoryDetails) {
		this.animalCommunityCategoryDetails = animalCommunityCategoryDetails;
	}

	public AnimalFacilityCategoryDetails getAnimalFacilityCategoryDetails() {
		return animalFacilityCategoryDetails;
	}

	public void setAnimalFacilityCategoryDetails(AnimalFacilityCategoryDetails animalFacilityCategoryDetails) {
		this.animalFacilityCategoryDetails = animalFacilityCategoryDetails;

	}

	public AnimalLaboratoryCategoryDetails getAnimalLaboratoryCategoryDetails() {
		return animalLaboratoryCategoryDetails;
	}

	public void setAnimalLaboratoryCategoryDetails(AnimalLaboratoryCategoryDetails animalLaboratoryCategoryDetails) {
		this.animalLaboratoryCategoryDetails = animalLaboratoryCategoryDetails;
	}

	public EnvironmentalCategoryDetails getEnvironmentalCategoryDetails() {
		return environmentalCategoryDetails;
	}

	public void setEnvironmentalCategoryDetails(EnvironmentalCategoryDetails environmentalCategoryDetails) {
		this.environmentalCategoryDetails = environmentalCategoryDetails;
	}

	public POE getPoeCategoryDetails() {
		return poeCategoryDetails;
	}

	public void setPoeCategoryDetails(POE poeCategoryDetails) {
		this.poeCategoryDetails = poeCategoryDetails;

	}

	public YesNo getOccurrencePreviously() {
		return occurrencePreviously;
	}

	public void setOccurrencePreviously(YesNo occurrencePreviously) {
		this.occurrencePreviously = occurrencePreviously;
	}

	@Enumerated(EnumType.STRING)
	public EbsTriagingDecision getTriagingDecision() {
		return triagingDecision;
	}

	public void setTriagingDecision(EbsTriagingDecision triagingDecision) {
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

	@ManyToOne(fetch = FetchType.LAZY)
	public User getResponsibleUser() {
		return responsibleUser;
	}

	public void setResponsibleUser(User responsibleUser) {
		this.responsibleUser = responsibleUser;
	}

	public String getTriagingDecisionString() {
		return triagingDecisionString;
	}

	public void setTriagingDecisionString(String triagingDecisionString) {
		this.triagingDecisionString = triagingDecisionString;
	}
	@Enumerated(EnumType.STRING)
	public OutComeSupervisor getOutcomeSupervisor() {
		return outcomeSupervisor;
	}

	public void setOutcomeSupervisor(OutComeSupervisor outcomeSupervisor) {
		this.outcomeSupervisor = outcomeSupervisor;
	}


	public boolean getNotSignal() {
		return notSignal;
	}

	public void setNotSignal(boolean notSignal) {
		this.notSignal = notSignal;
	}
	@Enumerated(EnumType.STRING)
	public CategoryDetailsLevel getCategoryDetailsLevel() {
		return categoryDetailsLevel;
	}

	public void setCategoryDetailsLevel(CategoryDetailsLevel categoryDetailsLevel) {
		this.categoryDetailsLevel = categoryDetailsLevel;
	}

	public YesNo getPotentialRisk() {
		return potentialRisk;
	}

	public void setPotentialRisk(YesNo potentialRisk) {
		this.potentialRisk = potentialRisk;
	}

	public YesNo getReferred() {
		return referred;
	}

	public void setReferred(YesNo referred) {
		this.referred = referred;
	}
}
