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
		target.setCategoryDetails(source.getCategoryDetails());
		target.setTriagingDecision(source.getTriagingDecision());
		target.setResponsibleUser(userService.getByReferenceDto(source.getResponsibleUser()));
		target.setOutcomeSupervisor(source.getOutcomeSupervisor());
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
		target.setCategoryDetails(source.getCategoryDetails());
		target.setTriagingDecision(source.getTriagingDecision());
		target.setCategoryDetailsString(source.getCategoryDetailsString());
		target.setCategoryDetailsString(source.getCategoryDetailsString());
		target.setOutcomeSupervisor(source.getOutcomeSupervisor());
		return target;
	}

	@LocalBean
	@Stateless
	public static class TriagingFacadeEjbLocal extends TriagingFacadeEjb {

	}
}


