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
import de.symeda.sormas.backend.user.UserService;
import de.symeda.sormas.backend.util.*;

import javax.ejb.*;

@Stateless(name = "TriagingFacade")
public class TriagingFacadeEjb implements TriagingFacade {
	@EJB
	private UserService userService;


	public Triaging fillOrBuildEntity(TriagingDto source, Triaging target, boolean checkChangeDate) {
		if (source == null) {
			return null;
		}

		target = DtoHelper.fillOrBuildEntity(source, target, Triaging::new, checkChangeDate);
		target.setEarlyWarning(source.getEarlyWarning());
        target.setDecisionDate(source.getDecisionDate());
        target.setHealthConcern(source.getHealthConcern());
        target.setReferredTo(source.getReferredTo());
        target.setOccurrencePreviously(source.getOccurrencePreviously());
        target.setSpecificSignal(source.getSpecificSignal());
		target.setSignalCategory(source.getSignalCategory());
		target.setHumanCommunityCategoryDetails(source.getHumanCommunityCategoryDetails());
		target.setHumanFacilityCategoryDetails(source.getHumanFacilityCategoryDetails());
		target.setHumanLaboratoryCategoryDetails(source.getHumanLaboratoryCategoryDetails());
		target.setAnimalCommunityCategoryDetails(source.getAnimalCommunityCategoryDetails());
		target.setAnimalFacilityCategoryDetails(source.getAnimalFacilityCategoryDetails());
		target.setEnvironmentalCategoryDetails(source.getEnvironmentalCategoryDetails());
		target.setPoeCategoryDetails(source.getPoeCategoryDetails());
		target.setTriagingDecision(source.getTriagingDecision());
		target.setResponsibleUser(userService.getByReferenceDto(source.getResponsibleUser()));
		target.setOutcomeSupervisor(source.getOutcomeSupervisor());
		target.setNotSignal(source.getNotSignal());
		target.setCategoryDetailsLevel(source.getCategoryDetailsLevel());
		target.setPotentialRisk(source.getPotentialRisk());
		return target;
	}

	public static TriagingDto toDto(Triaging triaging) {

		if (triaging == null) {
			return null;
		}
		TriagingDto target = new TriagingDto();
		Triaging source = triaging;
		DtoHelper.fillDto(target, source);

		target.setEarlyWarning(source.getEarlyWarning());
		target.setDecisionDate(source.getDecisionDate());
		target.setHealthConcern(source.getHealthConcern());
		target.setReferredTo(source.getReferredTo());
		target.setOccurrencePreviously(source.getOccurrencePreviously());
		target.setSpecificSignal(source.getSpecificSignal());
		target.setSignalCategory(source.getSignalCategory());
		target.setHumanCommunityCategoryDetails(source.getHumanCommunityCategoryDetails());
		target.setHumanFacilityCategoryDetails(source.getHumanFacilityCategoryDetails());
		target.setHumanLaboratoryCategoryDetails(source.getHumanLaboratoryCategoryDetails());
		target.setAnimalCommunityCategoryDetails(source.getAnimalCommunityCategoryDetails());
		target.setAnimalFacilityCategoryDetails(source.getAnimalFacilityCategoryDetails());
		target.setEnvironmentalCategoryDetails(source.getEnvironmentalCategoryDetails());
		target.setPoeCategoryDetails(source.getPoeCategoryDetails());
		target.setTriagingDecision(source.getTriagingDecision());
		target.setHumanCommunityCategoryDetailsString(source.getHumanCommunityCategoryDetailsString());
		target.setHumanFacilityCategoryDetailsString(source.getHumanFacilityCategoryDetailsString());
		target.setHumanLaboratoryCategoryDetailsString(source.getHumanLaboratoryCategoryDetailsString());
		target.setAnimalCommunityCategoryDetailsString(source.getAnimalCommunityCategoryDetailsString());
		target.setAnimalFacilityCategoryDetailsString(source.getAnimalFacilityCategoryDetailsString());
		target.setEnvironmentalCategoryDetailsString(source.getEnvironmentalCategoryDetailsString());
		target.setPoeCategoryDetailsString(source.getPoeCategoryDetailsString());
		target.setOutcomeSupervisor(source.getOutcomeSupervisor());
		target.setNotSignal(source.getNotSignal());
		target.setCategoryDetailsLevel(source.getCategoryDetailsLevel());
		target.setPotentialRisk(source.getPotentialRisk());
		return target;
	}

	@LocalBean
	@Stateless
	public static class TriagingFacadeEjbLocal extends TriagingFacadeEjb {

	}
}

