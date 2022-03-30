package de.symeda.sormas.api.infrastructure.cadre;

import java.util.Collection;
import java.util.List;

import javax.ejb.Remote;
import javax.validation.Valid;

import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.caze.*;
import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.externalsurveillancetool.ExternalSurveillanceToolException;
import de.symeda.sormas.api.importexport.ExportConfigurationDto;
import de.symeda.sormas.api.infrastructure.GeoLocationFacade;
import de.symeda.sormas.api.infrastructure.area.AreaCriteria;
import de.symeda.sormas.api.infrastructure.area.AreaDto;
import de.symeda.sormas.api.infrastructure.country.CountryReferenceDto;
import de.symeda.sormas.api.infrastructure.subcontinent.SubcontinentReferenceDto;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.api.utils.ValidationRuntimeException;

@Remote
public interface CadreFacade {
	List<CadreReferenceDto> getAllActiveAsReference();

	List<CadreDto> getByUuids(List<String> uuids);

	CadreDto getByUuid(String uuid);

	List<CadreReferenceDto> getByPosition(String name, boolean includeArchivedEntities);

	Page<CadreDto> getIndexPage(CadreCriteria criteria, Integer offset, Integer size, List<SortProperty> sortProperties);

	List<CadreDto> getIndexList(CadreCriteria criteria, Integer first, Integer max, List<SortProperty> sortProperties);

	CadreDto saveCadre(@Valid CadreDto dto) throws ValidationRuntimeException;

	CadreDto saveCadre(@Valid CadreDto dto, Boolean systemSave) throws ValidationRuntimeException;

//	List<CaseExportDto> getExportList(
//			CaseCriteria caseCriteria,
//			Collection<String> selectedRows,
//			CaseExportType exportType,
//			int first,
//			int max,
//			ExportConfigurationDto exportConfiguration,
//			Language userLanguage);


	long count(CadreCriteria cadreCriteria);

	long count(CadreCriteria cadreCriteria, boolean ignoreUserFilter);

	List<CadreDto> getByExternalId(String externalId);

//	boolean hasArchivedParentInfrastructure(Collection<String> cadreUuids);

	void archive(String cadreUuid);

	void dearchive(String cadreUuid);
}
