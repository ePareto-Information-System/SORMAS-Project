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

	public static final String SUPERVISOR_REVIEW = "supervisorReview";
	public static final String SPECIFIC_SIGNAL = "specificSignal";
	public static final String HEALTH_CONCERN = "healthConcern";
	public static final String SIGNAL_CATEGORY = "signalCategory";
	public static final String HUMAN_COMMUNITY_CATEGORY_DETAILS = "humanCommunityCategoryDetails";
	public static final String HUMAN_FACILITY_CATEGORY_DETAILS = "humanFacilityCategoryDetails";
	public static final String HUMAN_LABORATORY_CATEGORY_DETAILS = "humanLaboratoryCategoryDetails";
	public static final String ANIMAL_COMMUNITY_CATEGORY_DETAILS = "animalCommunityCategoryDetails";
	public static final String ANIMAL_FACILITY_CATEGORY_DETAILS = "animalFacilityCategoryDetails";
	public static final String ENVIRONMENTAL_CATEGORY_DETAILS = "environmentalCategoryDetails";
	public static final String POE_CATEGORY_DETAILS = "poeCategoryDetails";
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
	public static final String REFERRED = "referred";
	public static final String RESPONSIBLE_USER = "responsibleUser";
	public static final String OUTCOME_SUPERVISOR = "outcomeSupervisor";
	public static final String NOT_SIGNAL = "notSignal";
	public static final String CATEGORY_DETAILS_LEVEL = "categoryDetailsLevel";
	public static final String POTENTIAL_RISK = "potentialRisk";



	private YesNo supervisorReview;
	private YesNo referred;
	private YesNo specificSignal;
	private SignalCategory signalCategory;
	private YesNo healthConcern;
	private Set<HumanCommunityCategoryDetails> humanCommunityCategoryDetails;
	private Set<HumanFaclityCategoryDetails> humanFacilityCategoryDetails;
	private Set<HumanLaboratoryCategoryDetails> humanLaboratoryCategoryDetails;
	private Set<AnimalCommunityCategoryDetails> animalCommunityCategoryDetails;
	private Set<AnimalFacilityCategoryDetails> animalFacilityCategoryDetails;
	private Set<EnvironmentalCategoryDetails> environmentalCategoryDetails;
	private Set<POE> poeCategoryDetails;
	private YesNo occurrencePreviously;
	private EbsTriagingDecision triagingDecision;
	private Date decisionDate;
	private String referredTo;
	private UserReferenceDto responsibleUser;
	private String humanCommunityCategoryDetailsString;
	private String humanFacilityCategoryDetailsString;
	private String humanLaboratoryCategoryDetailsString;
	private String animalCommunityCategoryDetailsString;
	private String animalFacilityCategoryDetailsString;
	private String environmentalCategoryDetailsString;
	private String poeCategoryDetailsString;
	private OutComeSupervisor outcomeSupervisor;
	private boolean notSignal;
	private CategoryDetailsLevel categoryDetailsLevel;
	private YesNo potentialRisk;



	public EbsReferenceDto toReference() {
		return new EbsReferenceDto(getUuid());
	}


	public YesNo getSupervisorReview() {
		return supervisorReview;
	}

	public void setSupervisorReview(YesNo supervisorReview) {
		this.supervisorReview = supervisorReview;
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

	public Set<HumanCommunityCategoryDetails> getHumanCommunityCategoryDetails() {
		return humanCommunityCategoryDetails;
	}

	public void setHumanCommunityCategoryDetails(Set<HumanCommunityCategoryDetails> humanCommunityCategoryDetails) {
		this.humanCommunityCategoryDetails = humanCommunityCategoryDetails;
	}

	public Set<HumanFaclityCategoryDetails> getHumanFacilityCategoryDetails() {
		return humanFacilityCategoryDetails;
	}

	public void setHumanFacilityCategoryDetails(Set<HumanFaclityCategoryDetails> humanFacilityCategoryDetails) {
		this.humanFacilityCategoryDetails = humanFacilityCategoryDetails;
	}

	public Set<HumanLaboratoryCategoryDetails> getHumanLaboratoryCategoryDetails() {
		return humanLaboratoryCategoryDetails;
	}

	public void setHumanLaboratoryCategoryDetails(Set<HumanLaboratoryCategoryDetails> humanLaboratoryCategoryDetails) {
		this.humanLaboratoryCategoryDetails = humanLaboratoryCategoryDetails;
	}

	public Set<AnimalCommunityCategoryDetails> getAnimalCommunityCategoryDetails() {
		return animalCommunityCategoryDetails;
	}

	public void setAnimalCommunityCategoryDetails(Set<AnimalCommunityCategoryDetails> animalCommunityCategoryDetails) {
		this.animalCommunityCategoryDetails = animalCommunityCategoryDetails;
	}

	public Set<AnimalFacilityCategoryDetails> getAnimalFacilityCategoryDetails() {
		return animalFacilityCategoryDetails;
	}

	public void setAnimalFacilityCategoryDetails(Set<AnimalFacilityCategoryDetails> animalFacilityCategoryDetails) {
		this.animalFacilityCategoryDetails = animalFacilityCategoryDetails;
	}

	public Set<EnvironmentalCategoryDetails> getEnvironmentalCategoryDetails() {
		return environmentalCategoryDetails;
	}

	public void setEnvironmentalCategoryDetails(Set<EnvironmentalCategoryDetails> environmentalCategoryDetails) {
		this.environmentalCategoryDetails = environmentalCategoryDetails;
	}

	public Set<POE> getPoeCategoryDetails() {
		return poeCategoryDetails;
	}

	public void setPoeCategoryDetails(Set<POE> poeCategoryDetails) {
		this.poeCategoryDetails = poeCategoryDetails;
	}

	public YesNo getOccurrencePreviously() {
		return occurrencePreviously;
	}

	public void setOccurrencePreviously(YesNo occurrencePreviously) {
		this.occurrencePreviously = occurrencePreviously;
	}

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

	public UserReferenceDto getResponsibleUser() {
		return responsibleUser;
	}

	public void setResponsibleUser(UserReferenceDto responsibleUser) {
		this.responsibleUser = responsibleUser;
	}

	public String getHumanCommunityCategoryDetailsString() {
		return humanCommunityCategoryDetailsString;
	}

	public void setHumanCommunityCategoryDetailsString(String humanCommunityCategoryDetailsString) {
		this.humanCommunityCategoryDetailsString = humanCommunityCategoryDetailsString;
	}

	public String getHumanFacilityCategoryDetailsString() {
		return humanFacilityCategoryDetailsString;
	}

	public void setHumanFacilityCategoryDetailsString(String humanFacilityCategoryDetailsString) {
		this.humanFacilityCategoryDetailsString = humanFacilityCategoryDetailsString;
	}

	public String getHumanLaboratoryCategoryDetailsString() {
		return humanLaboratoryCategoryDetailsString;
	}

	public void setHumanLaboratoryCategoryDetailsString(String humanLaboratoryCategoryDetailsString) {
		this.humanLaboratoryCategoryDetailsString = humanLaboratoryCategoryDetailsString;
	}

	public String getAnimalCommunityCategoryDetailsString() {
		return animalCommunityCategoryDetailsString;
	}

	public void setAnimalCommunityCategoryDetailsString(String animalCommunityCategoryDetailsString) {
		this.animalCommunityCategoryDetailsString = animalCommunityCategoryDetailsString;
	}

	public String getAnimalFacilityCategoryDetailsString() {
		return animalFacilityCategoryDetailsString;
	}

	public void setAnimalFacilityCategoryDetailsString(String animalFacilityCategoryDetailsString) {
		this.animalFacilityCategoryDetailsString = animalFacilityCategoryDetailsString;
	}

	public String getEnvironmentalCategoryDetailsString() {
		return environmentalCategoryDetailsString;
	}

	public void setEnvironmentalCategoryDetailsString(String environmentalCategoryDetailsString) {
		this.environmentalCategoryDetailsString = environmentalCategoryDetailsString;
	}

	public String getPoeCategoryDetailsString() {
		return poeCategoryDetailsString;
	}

	public void setPoeCategoryDetailsString(String poeCategoryDetailsString) {
		this.poeCategoryDetailsString = poeCategoryDetailsString;
	}

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
