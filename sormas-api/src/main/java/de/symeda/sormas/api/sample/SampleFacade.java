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
package de.symeda.sormas.api.sample;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import javax.validation.Valid;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseCriteria;
import de.symeda.sormas.api.region.DistrictReferenceDto;
import de.symeda.sormas.api.region.RegionReferenceDto;
import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.api.utils.ValidationRuntimeException;

@Remote
public interface SampleFacade {

	List<SampleDto> getAllActiveSamplesAfter(Date date);

	List<SampleIndexDto> getIndexList(SampleCriteria sampleCriteria, Integer first, Integer max, List<SortProperty> sortProperties);

	Page<SampleIndexDto> getIndexPage(SampleCriteria sampleCriteria, Integer offset, Integer size, List<SortProperty> sortProperties);

	List<SampleExportDto> getExportList(SampleCriteria sampleCriteria, Collection<String> selectedRows, int first, int max);

	List<SampleExportDto> getExportList(CaseCriteria caseCriteria, Collection<String> selectedRows, int first, int max);

	long count(SampleCriteria sampleCriteria);

	SampleDto getSampleByUuid(String uuid);

	boolean isFieldSampleIdUnique(String uuid, String fieldSampleId);

	SampleDto saveSample(@Valid SampleDto dto);

	SampleReferenceDto getReferenceByUuid(String uuid);

	SampleReferenceDto getReferredFrom(String sampleUuid);

	List<String> getAllActiveUuids();

	List<SampleDto> getByUuids(List<String> uuids);
	
	List<SampleDto> findBy(SampleCriteria criteria);

	void deleteSample(SampleReferenceDto sampleRef);

	void deleteAllSamples(List<String> sampleUuids);

	void validate(SampleDto sample) throws ValidationRuntimeException;

	List<String> getDeletedUuidsSince(Date since);

	boolean isDeleted(String sampleUuid);

	Map<PathogenTestResultType, Long> getNewTestResultCountByResultType(List<Long> caseIds);

	List<SampleDto> getByCaseUuids(List<String> caseUuids);
	
	Map<SampleCountType, Long> getSampleCount(RegionReferenceDto regionRef, DistrictReferenceDto districtRef, Disease disease, Date from, Date to);

	Boolean isSampleEditAllowed(String sampleUuid);

	List<SampleDto> getByContactUuids(List<String> contactUuids);

	List<SampleDto> getSimilarSamples(SampleSimilarityCriteria criteria);

	boolean exists(String uuid);
	
	List<SampleDto> getByEventParticipantUuids(List<String> asList);
	
}
