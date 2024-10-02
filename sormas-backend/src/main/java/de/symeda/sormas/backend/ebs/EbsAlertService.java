package de.symeda.sormas.backend.ebs;

import de.symeda.sormas.api.ebs.RiskAssessmentCriteria;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.common.BaseAdoService;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;

@Stateless
@LocalBean
public class EbsAlertService extends BaseAdoService<EbsAlert> {

    public EbsAlertService() {super(EbsAlert.class);}

    public EbsAlert createAlert(){
        EbsAlert ebsAlert = new EbsAlert();
        ebsAlert.setUuid(DataHelper.createUuid());
        return ebsAlert;
    }

    public Predicate createDefaultFilter(CriteriaBuilder cb, Root<EbsAlert> root) {
        return cb.isFalse(root.get(EbsAlert.DELETED));
    }

    public List<EbsAlert> getByEbsUuids(List<String> ebsUuids) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EbsAlert> cq = cb.createQuery(EbsAlert.class);
        Root<EbsAlert> riskRoot = cq.from(EbsAlert.class);
        Join<EbsAlert, Ebs> ebsJoin = riskRoot.join(EbsAlert.EBS, JoinType.LEFT);

        Predicate filter = cb.and(createDefaultFilter(cb, riskRoot), ebsJoin.get(AbstractDomainObject.UUID).in(ebsUuids));

        cq.where(filter);
        return em.createQuery(cq).getResultList();
    }

    public List<EbsAlert> findBy(RiskAssessmentCriteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("Criteria cannot be null");
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EbsAlert> cq = cb.createQuery(EbsAlert.class);
        Root<EbsAlert> root = cq.from(EbsAlert.class);
        cq.select(root);

        Predicate filter = cb.conjunction();  // Create a default true predicate
        if (criteria.getEbs() != null && criteria.getEbs().getUuid() != null) {
            filter = cb.and(filter, cb.equal(root.get("ebs").get("uuid"), criteria.getEbs().getUuid()));
        }

        cq.where(filter);
        List<EbsAlert> result;
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
        Root<EbsAlert> from = cq.from(EbsAlert.class);
        cq.select(from.get(EbsAlert.UUID));
        cq.distinct(true);

        return em.createQuery(cq).getResultList();
    }
}
