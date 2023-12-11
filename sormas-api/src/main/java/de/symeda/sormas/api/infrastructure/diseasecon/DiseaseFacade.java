package de.symeda.sormas.api.infrastructure.diseasecon;

import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.infrastructure.InfrastructureFacade;
import de.symeda.sormas.api.utils.SortProperty;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface DiseaseFacade extends InfrastructureFacade<DiseaseConDto, DiseaseConIndexDto, DiseaseConReferenceDto, DiseaseConCriteria> {
    Page<DiseaseConIndexDto> getIndexPage(DiseaseConCriteria criteria, Integer offset, Integer size, List<SortProperty> sortProperties);

    List<DiseaseConReferenceDto> getAllActiveAsReference();
}
