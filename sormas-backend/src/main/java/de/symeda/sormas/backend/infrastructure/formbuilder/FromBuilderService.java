package de.symeda.sormas.backend.infrastructure.formbuilder;

import de.symeda.sormas.api.infrastructure.diseasecon.DiseaseConCriteria;
import de.symeda.sormas.api.infrastructure.forms.FormBuilderCriteria;
import de.symeda.sormas.backend.common.AbstractInfrastructureAdoService;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;
import de.symeda.sormas.backend.disease.DiseaseConfiguration;
import de.symeda.sormas.backend.infrastructure.facility.Facility;
import de.symeda.sormas.backend.infrastructure.formfiield.FormField;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Stateless
@LocalBean
public class FromBuilderService extends AbstractInfrastructureAdoService<FormBuilder, FormBuilderCriteria> {

    public FromBuilderService() {super(FormBuilder.class);}

    @Override
    public List<FormBuilder> getByExternalId(String externalId, boolean includeArchived) {
        return null;
    }

    @Override
    public Predicate buildCriteriaFilter(FormBuilderCriteria criteria, CriteriaBuilder cb, Root<FormBuilder> from) {
        Predicate filter = null;

        if(criteria.getDisease() != null) {
            filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get("disease"), criteria.getDisease()));
        }

        return filter;

    }

    @Override
    public Predicate createUserFilter(CriteriaBuilder cb, CriteriaQuery cq, From<?, FormBuilder> from) {
        return null;
    }

//    getFieldByUuid
    public FormField getFieldByUuid(String uuid) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<FormField> cq = cb.createQuery(FormField.class);
        Root<FormField> from = cq.from(FormField.class);

        Predicate filter = cb.equal(from.get("uuid"), uuid);
        cq.where(filter);

        TypedQuery<FormField> query = em.createQuery(cq);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public List<FormField> getFormFieldsByUuids(List<String> uuids) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<FormField> cq = cb.createQuery(FormField.class);
        Root<FormField> from = cq.from(FormField.class);

        Predicate filter = from.get("uuid").in(uuids);
        cq.where(filter);

        TypedQuery<FormField> query = em.createQuery(cq);
        return query.getResultList();
    }



}
