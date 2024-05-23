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
	public static final String HUMAN_COMMUNITY_CATEGORY_DETAILS = "humanCommunityCategoryDetails";
	public static final String HUMAN_FACILITY_CATEGORY_DETAILS = "humanFacilityCategoryDetails";
	public static final String HUMAN_LABORATORY_CATEGORY_DETAILS = "humanLaboratoryCategoryDetails";
	public static final String ANIMAL_COMMUNITY_CATEGORY_DETAILS = "animalCommunityCategoryDetails";
	public static final String ANIMAL_FACILITY_CATEGORY_DETAILS = "animalFacilityCategoryDetails";
	public static final String ENVIRONMENTAL_CATEGORY_DETAILS = "environmentalCategoryDetails";
	public static final String POE_CATEGORY_DETAILS = "poeCategoryDetails";
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
	public static final String NOT_SIGNAL = "notSignal";
	public static final String CATEGORY_DETAILS_LEVEL = "categoryDetailsLevel";



	private YesNo earlyWarning;
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
	private String humanCommunityCategoryDetailsString;
	private String humanFacilityCategoryDetailsString;
	private String humanLaboratoryCategoryDetailsString;
	private String animalCommunityCategoryDetailsString;
	private String animalFacilityCategoryDetailsString;
	private String environmentalCategoryDetailsString;
	private String poeCategoryDetailsString;
	private CategoryDetailsLevel categoryDetailsLevel;



	private YesNo occurrencePreviously;
	private EbsTriagingDecision triagingDecision;
	private String triagingDecisionString;
	private Date decisionDate;
	private String referredTo;
	private User responsibleUser;
	private OutComeSupervisor outcomeSupervisor;
	private boolean notSignal;

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
	public Set<HumanCommunityCategoryDetails> getHumanCommunityCategoryDetails() {
		if (humanCommunityCategoryDetails == null) {
			if (StringUtils.isEmpty(humanCommunityCategoryDetailsString)) {
				humanCommunityCategoryDetails = new HashSet<>();
			} else {
				humanCommunityCategoryDetails =
						Arrays.stream(humanCommunityCategoryDetailsString.split(",")).map(HumanCommunityCategoryDetails::valueOf).collect(Collectors.toSet());
			}
		}
		return humanCommunityCategoryDetails;
	}

	public void setHumanCommunityCategoryDetails(Set<HumanCommunityCategoryDetails> humanCommunityCategoryDetails) {
		this.humanCommunityCategoryDetails = humanCommunityCategoryDetails;
		if (this.humanCommunityCategoryDetails == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		humanCommunityCategoryDetails.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		humanCommunityCategoryDetailsString = sb.toString();
	}

	@Transient
	public Set<HumanFaclityCategoryDetails> getHumanFacilityCategoryDetails() {
		if (humanFacilityCategoryDetails == null) {
			if (StringUtils.isEmpty(humanFacilityCategoryDetailsString)) {
				humanFacilityCategoryDetails = new HashSet<>();
			} else {
				humanFacilityCategoryDetails =
						Arrays.stream(humanFacilityCategoryDetailsString.split(",")).map(HumanFaclityCategoryDetails::valueOf).collect(Collectors.toSet());
			}
		}
		return humanFacilityCategoryDetails;
	}

	public void setHumanFacilityCategoryDetails(Set<HumanFaclityCategoryDetails> humanFacilityCategoryDetails) {
		this.humanFacilityCategoryDetails = humanFacilityCategoryDetails;
		if (this.humanFacilityCategoryDetails == null){
			return;
		}
		StringBuilder sb = new StringBuilder();
		humanFacilityCategoryDetails.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		humanFacilityCategoryDetailsString = sb.toString();
	}

	@Transient
	public Set<HumanLaboratoryCategoryDetails> getHumanLaboratoryCategoryDetails() {
		if (humanLaboratoryCategoryDetails == null) {
			if (StringUtils.isEmpty(humanLaboratoryCategoryDetailsString)) {
				humanLaboratoryCategoryDetails = new HashSet<>();
			} else {
				humanLaboratoryCategoryDetails =
						Arrays.stream(humanLaboratoryCategoryDetailsString.split(",")).map(HumanLaboratoryCategoryDetails::valueOf).collect(Collectors.toSet());
			}
		}
		return humanLaboratoryCategoryDetails;
	}

	public void setHumanLaboratoryCategoryDetails(Set<HumanLaboratoryCategoryDetails> humanLaboratoryCategoryDetails) {
		this.humanLaboratoryCategoryDetails = humanLaboratoryCategoryDetails;
		if (this.humanLaboratoryCategoryDetails == null){
			return;
		}
		StringBuilder sb = new StringBuilder();
		humanLaboratoryCategoryDetails.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		humanLaboratoryCategoryDetailsString = sb.toString();
	}

	@Transient
	public Set<AnimalCommunityCategoryDetails> getAnimalCommunityCategoryDetails() {
		if (animalCommunityCategoryDetails == null) {
			if (StringUtils.isEmpty(animalCommunityCategoryDetailsString)) {
				animalCommunityCategoryDetails = new HashSet<>();
			} else {
				animalCommunityCategoryDetails =
						Arrays.stream(animalCommunityCategoryDetailsString.split(",")).map(AnimalCommunityCategoryDetails::valueOf).collect(Collectors.toSet());
			}
		}
		return animalCommunityCategoryDetails;
	}

	public void setAnimalCommunityCategoryDetails(Set<AnimalCommunityCategoryDetails> animalCommunityCategoryDetails) {
		this.animalCommunityCategoryDetails = animalCommunityCategoryDetails;
		if (this.animalCommunityCategoryDetails == null){
			return;
		}
		StringBuilder sb = new StringBuilder();
		animalCommunityCategoryDetails.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		animalCommunityCategoryDetailsString = sb.toString();
	}

	@Transient
	public Set<AnimalFacilityCategoryDetails> getAnimalFacilityCategoryDetails() {
		if (animalFacilityCategoryDetails == null) {
			if (StringUtils.isEmpty(animalFacilityCategoryDetailsString)) {
				animalFacilityCategoryDetails = new HashSet<>();
			} else {
				animalFacilityCategoryDetails =
						Arrays.stream(animalFacilityCategoryDetailsString.split(",")).map(AnimalFacilityCategoryDetails::valueOf).collect(Collectors.toSet());
			}
		}
		return animalFacilityCategoryDetails;
	}

	public void setAnimalFacilityCategoryDetails(Set<AnimalFacilityCategoryDetails> animalFacilityCategoryDetails) {
		this.animalFacilityCategoryDetails = animalFacilityCategoryDetails;
		if (this.animalFacilityCategoryDetails == null){
			return;
		}
		StringBuilder sb = new StringBuilder();
		animalFacilityCategoryDetails.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		animalFacilityCategoryDetailsString = sb.toString();
	}

	@Transient
	public Set<EnvironmentalCategoryDetails> getEnvironmentalCategoryDetails() {
		if (environmentalCategoryDetails == null) {
			if (StringUtils.isEmpty(environmentalCategoryDetailsString)) {
				environmentalCategoryDetails = new HashSet<>();
			} else {
				environmentalCategoryDetails =
						Arrays.stream(environmentalCategoryDetailsString.split(",")).map(EnvironmentalCategoryDetails::valueOf).collect(Collectors.toSet());
			}
		}
		return environmentalCategoryDetails;
	}

	public void setEnvironmentalCategoryDetails(Set<EnvironmentalCategoryDetails> environmentalCategoryDetails) {
		this.environmentalCategoryDetails = environmentalCategoryDetails;
		if (this.environmentalCategoryDetails == null){
			return;
		}
		StringBuilder sb = new StringBuilder();
		environmentalCategoryDetails.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		environmentalCategoryDetailsString = sb.toString();
	}

	@Transient
	public Set<POE> getPoeCategoryDetails() {
		if (poeCategoryDetails == null) {
			if (StringUtils.isEmpty(poeCategoryDetailsString)) {
				poeCategoryDetails = new HashSet<>();
			} else {
				poeCategoryDetails =
						Arrays.stream(poeCategoryDetailsString.split(",")).map(POE::valueOf).collect(Collectors.toSet());
			}
		}
		return poeCategoryDetails;
	}

	public void setPoeCategoryDetails(Set<POE> poeCategoryDetails) {
		this.poeCategoryDetails = poeCategoryDetails;
		if (this.poeCategoryDetails == null){
			return;
		}
		StringBuilder sb = new StringBuilder();
		poeCategoryDetails.stream().forEach(t -> {
			sb.append(t.name());
			sb.append(",");
		});
		if (sb.length() > 0) {
			sb.substring(0, sb.lastIndexOf(","));
		}
		poeCategoryDetailsString = sb.toString();

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

	public YesNo getOccurrencePreviously() {
		return occurrencePreviously;
	}

	public void setOccurrencePreviously(YesNo occurrencePreviously) {
		this.occurrencePreviously = occurrencePreviously;
	}

	public EbsTriagingDecision getTriagingDecision() {
//		if (triagingDecision == null) {
//			if (StringUtils.isEmpty(triagingDecisionString)) {
//				triagingDecision = new HashSet<>();
//			} else {
//				triagingDecision =
//						Arrays.stream(triagingDecisionString.split(",")).map(EbsTriagingDecision::valueOf).collect(Collectors.toSet());
//			}
//		}
		return triagingDecision;
	}

	public void setTriagingDecision(EbsTriagingDecision triagingDecision) {
		this.triagingDecision = triagingDecision;
//		if (this.triagingDecision == null) {
//			return;
//		}

//		StringBuilder sb = new StringBuilder();
//		triagingDecision.stream().forEach(t -> {
//			sb.append(t.name());
//			sb.append(",");
//		});
//		if (sb.length() > 0) {
//			sb.substring(0, sb.lastIndexOf(","));
//		}
//		triagingDecisionString = sb.toString();
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

	public String getHumanCommunityCategoryDetailsString() {
		return humanCommunityCategoryDetailsString;
	}

	public void setHumanCommunityCategoryDetailsStringString(String humanCommunityCategoryDetailsString) {
		this.humanCommunityCategoryDetailsString = humanCommunityCategoryDetailsString;
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
}
