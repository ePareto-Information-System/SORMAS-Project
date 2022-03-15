package de.symeda.sormas.api.infrastructure.cadre;

import java.util.Collection;
import java.util.List;

import javax.ejb.Remote;

import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.infrastructure.GeoLocationFacade;
import de.symeda.sormas.api.infrastructure.country.CountryReferenceDto;
import de.symeda.sormas.api.infrastructure.subcontinent.SubcontinentReferenceDto;
import de.symeda.sormas.api.utils.SortProperty;

@Remote
public interface CadreFacade extends GeoLocationFacade<CadreDto, CadreIndexDto, CadreReferenceDto, CadreCriteria> {

	List<CadreReferenceDto> getByDefaultName(String name, boolean includeArchivedEntities);

	boolean isUsedInOtherInfrastructureData(Collection<String> cadreUuids);

//	CadreReferenceDto getByCadre(CountryReferenceDto countryReferenceDto);

	Page<CadreReferenceDto> getIndexPage(CadreCriteria criteria, Integer offset, Integer size, List<SortProperty> sortProperties);

	List<CadreReferenceDto> getAllActiveAsReference();
}
