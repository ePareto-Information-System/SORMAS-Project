package de.symeda.sormas.api.infrastructure.fields;

import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.infrastructure.InfrastructureFacade;
import de.symeda.sormas.api.utils.SortProperty;

import javax.ejb.Remote;
import java.util.List;
@Remote
public interface FormFieldFacade extends InfrastructureFacade<FormFieldsDto, FormFieldIndexDto, FormFieldReferenceDto, FormFieldsCriteria> {
    Page<FormFieldIndexDto> getIndexPage(FormFieldsCriteria criteria, Integer offset, Integer size, List<SortProperty> sortProperties);

    List<FormFieldsDto> getFormFieldsByFormType(FormFieldsCriteria criteria);

}