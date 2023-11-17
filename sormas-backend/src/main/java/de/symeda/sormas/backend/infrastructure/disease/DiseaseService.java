package de.symeda.sormas.backend.infrastructure.disease;

import de.symeda.sormas.api.infrastructure.disease.DiseaseCriteria;
import de.symeda.sormas.backend.common.AbstractInfrastructureAdoService;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;
import de.symeda.sormas.backend.disease.DiseaseConfiguration;
import de.symeda.sormas.backend.infrastructure.facility.Facility;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import java.util.List;

@Stateless
@LocalBean
public class DiseaseService extends AbstractInfrastructureAdoService<DiseaseConfiguration, DiseaseCriteria> {

    public DiseaseService() {super(DiseaseConfiguration.class);}

    @Override
    public List<DiseaseConfiguration> getByExternalId(String externalId, boolean includeArchived) {
        return null;
    }

    @Override
    public Predicate buildCriteriaFilter(DiseaseCriteria criteria, CriteriaBuilder cb, Root<DiseaseConfiguration> from) {
        Predicate filter = null;

        if(criteria.getDisease() != null) {
            filter = CriteriaBuilderHelper.and(cb, filter, cb.equal(from.get("disease"), criteria.getDisease()));
        }

        return filter;

    }

    @Override
    public Predicate createUserFilter(CriteriaBuilder cb, CriteriaQuery cq, From<?, DiseaseConfiguration> from) {
        return null;
    }

    public Facility getFacilityByUuid(String uuid) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Facility> query = cb.createQuery(Facility.class);
		Root<Facility> root = query.from(Facility.class);

        query.where(cb.equal(root.get("uuid"), uuid));

        TypedQuery<Facility> typedQuery = em.createQuery(query);

        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

}
