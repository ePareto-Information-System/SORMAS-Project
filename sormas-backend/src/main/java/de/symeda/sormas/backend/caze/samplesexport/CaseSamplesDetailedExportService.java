package de.symeda.sormas.backend.caze.samplesexport;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import de.symeda.sormas.api.caze.samplesexport.CaseSamplesDetailedExportResult;
import de.symeda.sormas.backend.common.BaseAdoService;
import de.symeda.sormas.backend.util.QueryHelper;

@Stateless
@LocalBean
public class CaseSamplesDetailedExportService extends BaseAdoService<CaseSamplesDetailedExport> {

	public CaseSamplesDetailedExportService() {
		super(CaseSamplesDetailedExport.class);
	}

	/**
	 * Persists the export record in its own committed transaction so that the
	 * immediately following {@code @Asynchronous} call sees the row regardless of
	 * when the EJB thread pool picks it up.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void persistNewExport(CaseSamplesDetailedExport export) {
		ensurePersisted(export);
	}

	public List<CaseSamplesDetailedExport> getStaleInProgressExports(Date olderThan) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CaseSamplesDetailedExport> cq = cb.createQuery(getElementClass());
		Root<CaseSamplesDetailedExport> root = cq.from(getElementClass());
		cq.where(
			cb.and(
				cb.equal(root.get(CaseSamplesDetailedExport.RESULT), CaseSamplesDetailedExportResult.IN_PROGRESS),
				cb.lessThan(root.get(CaseSamplesDetailedExport.REQUESTED_DATE), olderThan)));
		return em.createQuery(cq).getResultList();
	}

	public CaseSamplesDetailedExport getByTokenHash(String tokenHash) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CaseSamplesDetailedExport> cq = cb.createQuery(getElementClass());
		Root<CaseSamplesDetailedExport> root = cq.from(getElementClass());
		cq.where(cb.equal(root.get(CaseSamplesDetailedExport.TOKEN_HASH), tokenHash));
		return QueryHelper.getFirstResult(em, cq);
	}

	public List<CaseSamplesDetailedExport> getExpiredWithFiles(Date now) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CaseSamplesDetailedExport> cq = cb.createQuery(getElementClass());
		Root<CaseSamplesDetailedExport> root = cq.from(getElementClass());
		cq.where(
			cb.and(
				cb.equal(root.get(CaseSamplesDetailedExport.RESULT), CaseSamplesDetailedExportResult.SUCCESS),
				cb.isNotNull(root.get(CaseSamplesDetailedExport.FILE_PATH)),
				cb.lessThan(root.get(CaseSamplesDetailedExport.EXPIRES_AT), now)));
		return em.createQuery(cq).getResultList();
	}
}
