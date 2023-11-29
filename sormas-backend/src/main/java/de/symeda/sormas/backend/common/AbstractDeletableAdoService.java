package de.symeda.sormas.backend.common;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.criteria.*;

import de.symeda.sormas.api.common.DeletionDetails;

public abstract class AbstractDeletableAdoService<ADO extends DeletableAdo> extends AdoServiceWithUserFilterAndJurisdiction<ADO> {

	protected AbstractDeletableAdoService(Class<ADO> elementClass) {
		super(elementClass);
	}

	public void delete(ADO ado, DeletionDetails deletionDetails) {

		ado.setDeletionReason(deletionDetails.getDeletionReason());
		ado.setOtherDeletionReason(deletionDetails.getOtherDeletionReason());
		ado.setDeleted(true);
		em.persist(ado);
		em.flush();
	}
	

	public void delete(ADO deleteme) {

		deleteme.setDeleted(true);
		em.persist(deleteme);
		em.flush();
	}

	public List<ADO> getBatchedQueryResults(CriteriaBuilder cb, CriteriaQuery<ADO> cq, From<?, ADO> from, Integer batchSize) {

		// Ordering by UUID is relevant if a batch includes some, but not all objects with the same timestamp.
		// the next batch can then resume with the same timestamp and the next UUID in lexicographical order.y
		cq.orderBy(cb.asc(from.get(AbstractDomainObject.CHANGE_DATE)), cb.asc(from.get(AbstractDomainObject.UUID)));

		return createQuery(cq, 0, batchSize).getResultList();
	}

	public void restore(ADO ado) {

		ado.setDeletionReason(null);
		ado.setOtherDeletionReason(null);
		ado.setDeleted(false);
		em.persist(ado);
		em.flush();
	}

	protected <C> Predicate changeDateFilter(CriteriaBuilder cb, Timestamp date, From<?, C> path, String... joinFields) {
		From<?, ?> parent = path;
		for (String joinField : joinFields) {
			parent = parent.join(joinField, JoinType.LEFT);
		}
		return CriteriaBuilderHelper.greaterThanAndNotNull(cb, parent.get(AbstractDomainObject.CHANGE_DATE), date);
	}
}
