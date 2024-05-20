package de.symeda.sormas.backend.ebs;

import de.symeda.sormas.api.ebs.RiskAssessmentCriteria;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.caze.Case;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.common.BaseAdoService;
import de.symeda.sormas.backend.sample.Sample;

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

//    public Collection<RiskAssessment> findBy(RiskAssessmentCriteria criteria) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<RiskAssessment> cq = cb.createQuery(RiskAssessment.class);
//        Root<RiskAssessment> root = cq.from(RiskAssessment.class);
//        cq.select(root);
//
//        Predicate filter = createDefaultFilter(cb, root);
//
//        if (criteria.getEbs() != null) {
//            filter = cb.and(filter, cb.equal(root.get(RiskAssessment.EBS).get(Ebs.ID), criteria.getEbs().getUuid()));
//        }
//
//        cq.where(filter);
//        return em.createQuery(cq).getResultList();
//    }


//    public List<RiskAssessment> findBy(RiskAssessmentCriteria criteria) {
//        if (criteria == null) {
//            throw new IllegalArgumentException("Criteria cannot be null");
//        }
//
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<RiskAssessment> cq = cb.createQuery(RiskAssessment.class);
//        Root<RiskAssessment> root = cq.from(RiskAssessment.class);
//        cq.select(root);
//
//        Predicate filter = createDefaultFilter(cb, root);
//
//        if (criteria.getEbs() != null) {
//            UUID ebsUuid = UUID.fromString(criteria.getEbs().getUuid());
//            if (ebsUuid != null) {
//                filter = cb.and(filter, cb.equal(root.get(RiskAssessment.EBS).get(Ebs.ID), ebsUuid));
//            }
//        }
//
//        cq.where(filter);
//
//        List<RiskAssessment> result = Collections.emptyList();
//        try {
//            result = em.createQuery(cq).getResultList();
//        } catch (Exception e) {
//            // Log the exception and handle it as needed
//            e.printStackTrace();
//        }
//
//        return result;
//    }

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


}
