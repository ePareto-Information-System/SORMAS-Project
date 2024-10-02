package de.symeda.sormas.backend.ebs;

import de.symeda.sormas.api.RequestContextHolder;
import de.symeda.sormas.api.ebs.RiskAssessmentCriteria;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.caze.Case;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.common.BaseAdoService;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;
import de.symeda.sormas.backend.sample.Sample;
import de.symeda.sormas.backend.user.User;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Stateless
@LocalBean
public class RiskAssessmentService extends BaseAdoService<RiskAssessment> {
        @EJB
        EbsService service;
        public RiskAssessmentService() {super(RiskAssessment.class);}

        public RiskAssessment createRiskAssessment() {

            RiskAssessment riskAssessment = new RiskAssessment();
            riskAssessment.setUuid(DataHelper.createUuid());
            return riskAssessment;
        }

    public Predicate createDefaultFilter(CriteriaBuilder cb, Root<RiskAssessment> root) {
        return cb.isFalse(root.get(RiskAssessment.DELETED));
    }

    public List<RiskAssessment> getByEbsUuids(List<String> ebsUuids) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RiskAssessment> cq = cb.createQuery(RiskAssessment.class);
        Root<RiskAssessment> riskRoot = cq.from(RiskAssessment.class);
        Join<RiskAssessment, Ebs> ebsJoin = riskRoot.join(RiskAssessment.EBS, JoinType.LEFT);

        Predicate filter = cb.and(createDefaultFilter(cb, riskRoot), ebsJoin.get(AbstractDomainObject.UUID).in(ebsUuids));

        cq.where(filter);
        return em.createQuery(cq).getResultList();
    }

    public List<RiskAssessment> findBy(RiskAssessmentCriteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("Criteria cannot be null");
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RiskAssessment> cq = cb.createQuery(RiskAssessment.class);
        Root<RiskAssessment> root = cq.from(RiskAssessment.class);
        cq.select(root);

        Predicate filter = cb.conjunction();  // Create a default true predicate

        if (criteria.getEbs() != null && criteria.getEbs().getUuid() != null) {
            filter = cb.and(filter, cb.equal(root.get("ebs").get("uuid"), criteria.getEbs().getUuid()));
        }

        cq.where(filter);

        List<RiskAssessment> result;
        try {
            result = em.createQuery(cq).getResultList();
        } catch (Exception e) {
            // Log the exception and handle it as needed
            e.printStackTrace();
            return Collections.emptyList();
        }

        return result;
    }

    public List<String> getAllActiveUuids() {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<RiskAssessment> from = cq.from(RiskAssessment.class);
        cq.select(from.get(RiskAssessment.UUID));
        cq.distinct(true);

        return em.createQuery(cq).getResultList();
    }

    public Predicate createActiveRiskFilter(CriteriaBuilder cb, Root<RiskAssessment> root) {
        return cb.and(cb.isFalse(root.get(RiskAssessment.DELETED)));
    }

}
