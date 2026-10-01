package de.symeda.sormas.backend.caze.samplesexport;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
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

	/**
	 * Updates progress counters in their own committed transaction so the change is immediately
	 * visible to readers (e.g. the UI grid) without waiting for the outer export transaction to finish.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void updateProgress(String exportUuid, int processedRows, long totalCount) {
		CaseSamplesDetailedExport export = getByUuid(exportUuid);
		if (export != null && export.getResult() == CaseSamplesDetailedExportResult.IN_PROGRESS) {
			export.setProgressRowCount(processedRows);
			export.setTotalCaseCount(totalCount > 0 ? totalCount : null);
			export.setLastProgressAt(new Date());
			ensurePersisted(export);
		}
	}

	/**
	 * Saves SUCCESS state in its own transaction, re-reading the entity fresh to avoid
	 * OptimisticLockException from intermediate progress updates.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void saveExportSuccess(
		String exportUuid,
		String fileName,
		String filePath,
		Integer exportedRowCount,
		Date expiresAt,
		Date emailSentDate) {

		CaseSamplesDetailedExport export = getByUuid(exportUuid);
		if (export == null) {
			return;
		}
		export.setResult(CaseSamplesDetailedExportResult.SUCCESS);
		export.setFileName(fileName);
		export.setFilePath(filePath);
		export.setExportedRowCount(exportedRowCount);
		export.setExpiresAt(expiresAt);
		export.setFailureMessage(null);
		export.setFailureStackTrace(null);
		export.setPartial(false);
		export.setEmailSentDate(emailSentDate);
		ensurePersisted(export);
	}

	/**
	 * Saves FAILED state in its own transaction, re-reading the entity fresh to avoid
	 * OptimisticLockException from intermediate progress updates. Preserves the file
	 * path when a partial/complete file exists.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void saveExportFailed(
		String exportUuid,
		String failureMessage,
		String failureStackTrace,
		String fileName,
		String filePath,
		boolean isPartial,
		Date expiresAt,
		Date emailSentDate) {

		CaseSamplesDetailedExport export = getByUuid(exportUuid);
		if (export == null) {
			return;
		}
		export.setResult(CaseSamplesDetailedExportResult.FAILED);
		export.setFailureMessage(failureMessage);
		export.setFailureStackTrace(failureStackTrace);
		export.setFileName(fileName);
		export.setFilePath(filePath);
		export.setPartial(isPartial);
		export.setExpiresAt(expiresAt);
		export.setExportedRowCount(null);
		export.setEmailSentDate(emailSentDate);
		ensurePersisted(export);
	}

	/**
	 * Returns IN_PROGRESS exports that are stale. An export is stale if:
	 * - it never made progress and was requested more than {@code neverStartedThreshold} ago, or
	 * - it made progress but the last progress update was more than {@code stalledThreshold} ago.
	 */
	public List<CaseSamplesDetailedExport> getStaleInProgressExports(Date neverStartedThreshold, Date stalledThreshold) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CaseSamplesDetailedExport> cq = cb.createQuery(getElementClass());
		Root<CaseSamplesDetailedExport> root = cq.from(getElementClass());

		Predicate neverStarted = cb.and(
			cb.isNull(root.get(CaseSamplesDetailedExport.PROGRESS_ROW_COUNT)),
			cb.lessThan(root.get(CaseSamplesDetailedExport.REQUESTED_DATE), neverStartedThreshold));

		Predicate stalledWithProgress = cb.and(
			cb.isNotNull(root.get(CaseSamplesDetailedExport.LAST_PROGRESS_AT)),
			cb.lessThan(root.get(CaseSamplesDetailedExport.LAST_PROGRESS_AT), stalledThreshold));

		cq.where(
			cb.and(
				cb.equal(root.get(CaseSamplesDetailedExport.RESULT), CaseSamplesDetailedExportResult.IN_PROGRESS),
				cb.or(neverStarted, stalledWithProgress)));

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
				cb.isNotNull(root.get(CaseSamplesDetailedExport.FILE_PATH)),
				cb.lessThan(root.get(CaseSamplesDetailedExport.EXPIRES_AT), now)));
		return em.createQuery(cq).getResultList();
	}
}
