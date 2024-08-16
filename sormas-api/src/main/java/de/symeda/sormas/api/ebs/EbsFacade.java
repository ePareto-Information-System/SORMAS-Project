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

import de.symeda.sormas.api.CoreFacade;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.event.*;
import de.symeda.sormas.api.externaldata.ExternalDataDto;
import de.symeda.sormas.api.externaldata.ExternalDataUpdateException;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.task.TaskDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.SortProperty;

import javax.ejb.Remote;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Remote
public interface EbsFacade extends CoreFacade<EbsDto, EbsIndexDto, EbsReferenceDto, EbsCriteria> {

	EbsDto getEbsByUuid(String uuid, boolean detailedReferences);

	List<String> getAllActiveUuids();

	List<EbsDto> getAllByCase(CaseDataDto caseDataDto);

	Page<EbsIndexDto> getIndexPage(@NotNull EbsCriteria ebsCriteria, Integer offset, Integer size, List<SortProperty> sortProperties);

	List<EbsExportDto> getExportList(EbsCriteria ebsCriteria, Collection<String> selectedRows, Integer first, Integer max);

	List<String> getDeletedUuidsSince(Date since);

	void archiveAllArchivableEbss(int daysAfterEbsGetsArchived, @NotNull LocalDate referenceDate);

	boolean doesExternalTokenExist(String externalToken, String ebsUuid);


	void updateExternalData(@Valid List<ExternalDataDto> externalData) throws ExternalDataUpdateException;


	Integer saveBulkEbs(
		List<String> ebsUuidList,
		EbsDto updatedTempEbs);

	boolean isInJurisdictionOrOwned(String uuid);

	void archiveAllArchivableEbss(int daysAfterEbsGetsArchived);

	Set<RegionReferenceDto> getAllRegionsRelatedToEbsUuids(List<String> uuids);

	public boolean hasRegionAndDistrict(String ebsUuid);

	String getUuidByCaseUuidOrPersonUuid(String value);

	void setRiskAssessmentAssociations(EbsReferenceDto ebsRef);

	void setEbsAlertAssociations(EbsReferenceDto ebsRef);

	public List<EbsIndexDto> getEventIndexList(EbsCriteria ebsCriteria, Integer first, Integer max, List<SortProperty> sortProperties);

	public long eventCount(EbsCriteria ebsCriteria);

}
