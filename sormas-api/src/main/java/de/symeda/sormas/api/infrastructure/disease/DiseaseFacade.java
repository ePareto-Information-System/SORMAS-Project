package de.symeda.sormas.api.infrastructure.disease;

import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.infrastructure.InfrastructureFacade;
import de.symeda.sormas.api.utils.SortProperty;

import javax.ejb.Remote;
import java.util.List;
@Remote
public interface DiseaseFacade extends InfrastructureFacade<DiseaseDto, DiseaseIndexDto, DiseaseReferenceDto, DiseaseCriteria> {
    Page<DiseaseIndexDto> getIndexPage(DiseaseCriteria criteria, Integer offset, Integer size, List<SortProperty> sortProperties);

    List<DiseaseReferenceDto> getAllActiveAsReference();

}
