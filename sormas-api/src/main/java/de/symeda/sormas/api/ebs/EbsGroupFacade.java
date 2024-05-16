/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.event.*;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.utils.SortProperty;

import javax.ejb.Remote;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Remote
public interface EbsGroupFacade {

	EbsGroupReferenceDto getReferenceByUuid(String uuid);

	boolean exists(String uuid);

	boolean isArchived(String uuid);

	EbsGroupDto getEbsGroupByUuid(String uuid);

	List<EbsGroupReferenceDto> getCommonEbsGroupsByEbs(List<EbsReferenceDto> ebsReferences);

	List<EbsGroupIndexDto> getIndexList(EbsGroupCriteria ebsGroupCriteria, Integer first, Integer max, List<SortProperty> sortProperties);

	long count(EbsGroupCriteria ebsGroupCriteria);

	Page<EbsGroupIndexDto> getIndexPage(
		@NotNull EbsGroupCriteria ebsGroupCriteria,
		Integer offset,
		Integer size,
		List<SortProperty> sortProperties);

	EbsGroupDto saveEbsGroup(@Valid @NotNull EbsGroupDto ebsGroup);

	void linkEbsToGroup(EbsReferenceDto ebsReference, EbsGroupReferenceDto ebsGroupReference);

	void linkEventToGroups(EbsReferenceDto ebsReference, List<EbsGroupReferenceDto> ebsGroupReferences);

	void linkEbsToGroup(List<EbsReferenceDto> ebsReferences, EbsGroupReferenceDto ebsGroupReference);

	void linkEbsToGroups(List<String> ebsUuids, List<String> ebsGroupReferences);

	void unlinkEbsGroup(EbsReferenceDto ebsReference, EbsGroupReferenceDto ebsGroupReference);

	void deleteEbsGroup(String uuid);

	void archiveOrDearchiveEbsGroup(String uuid, boolean archive);

	List<RegionReferenceDto> getEbsGroupRelatedRegions(String uuid);

	void notifyEbsEbsGroupCreated(EbsGroupReferenceDto ebsGroupReference);

	void notifyEbsAddedToEventGroup(EbsGroupReferenceDto ebsGroupReference, List<EbsReferenceDto> ebsReferences);

	void notifyEbsAddedToEbsGroup(String ebsGroupUuid, Set<String> ebsUuids);

	void notifyEbsRemovedFromEbsGroup(EbsGroupReferenceDto ebsGroupReference, List<EbsReferenceDto> ebsReferences);
}
