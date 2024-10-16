package de.symeda.sormas.api.infrastructure.forms;

import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.infrastructure.InfrastructureFacade;
import de.symeda.sormas.api.utils.SortProperty;

import javax.ejb.Remote;
import java.util.List;
@Remote
public interface FormBuilderFacade extends InfrastructureFacade<FormBuilderDto, FormBuilderIndexDto, FormBuilderReferenceDto, FormBuilderCriteria> {
    Page<FormBuilderIndexDto> getIndexPage(FormBuilderCriteria criteria, Integer offset, Integer size, List<SortProperty> sortProperties);

}