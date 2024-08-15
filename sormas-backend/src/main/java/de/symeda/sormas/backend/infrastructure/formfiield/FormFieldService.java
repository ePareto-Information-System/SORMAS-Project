package de.symeda.sormas.backend.infrastructure.formfiield;

import de.symeda.sormas.api.infrastructure.fields.FormFieldsCriteria;
import de.symeda.sormas.backend.common.AbstractInfrastructureAdoService;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.*;
import java.util.List;

@Stateless
@LocalBean
public class FormFieldService extends AbstractInfrastructureAdoService<FormField, FormFieldsCriteria> {

    public FormFieldService() {super(FormField.class);}

    @Override
    public List<FormField> getByExternalId(String externalId, boolean includeArchived) {
        return null;
    }

    @Override
    public Predicate buildCriteriaFilter(FormFieldsCriteria criteria, CriteriaBuilder cb, Root<FormField> from) {
        Predicate filter = null;

        if(criteria.getFormType() != null) {
            filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get("formType"), criteria.getFormType()));
        }

        return filter;

    }

    @Override
    public Predicate createUserFilter(CriteriaBuilder cb, CriteriaQuery cq, From<?, FormField> from) {
        return null;
    }

//    getFormFieldsByFormType
    public List<FormField> getFormFieldsByFormType(FormFieldsCriteria criteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<FormField> cq = cb.createQuery(FormField.class);
        Root<FormField> from = cq.from(FormField.class);

        Predicate filter = buildCriteriaFilter(criteria, cb, from);
        if (filter != null) {
            cq.where(filter);
        }

        return em.createQuery(cq).getResultList();
    }


}
