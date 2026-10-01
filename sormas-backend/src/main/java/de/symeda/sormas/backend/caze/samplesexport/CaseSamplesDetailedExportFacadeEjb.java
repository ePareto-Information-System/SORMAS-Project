package de.symeda.sormas.backend.caze.samplesexport;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.caze.CaseCriteria;
import de.symeda.sormas.api.caze.CaseExportType;
import de.symeda.sormas.api.caze.samplesexport.CaseSamplesDetailedExportDownloadDto;
import de.symeda.sormas.api.caze.samplesexport.CaseSamplesDetailedExportDto;
import de.symeda.sormas.api.caze.samplesexport.CaseSamplesDetailedExportFacade;
import de.symeda.sormas.api.caze.samplesexport.CaseSamplesDetailedExportFileDto;
import de.symeda.sormas.api.caze.samplesexport.CaseSamplesDetailedExportResult;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.importexport.ExportConfigurationDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.AccessDeniedException;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.user.User;
import de.symeda.sormas.backend.user.UserFacadeEjb;
import de.symeda.sormas.backend.user.UserService;
import de.symeda.sormas.backend.util.ModelConstants;
import de.symeda.sormas.backend.util.QueryHelper;
import de.symeda.sormas.backend.util.RightsAllowed;

@Stateless(name = "CaseSamplesDetailedExportFacade")
public class CaseSamplesDetailedExportFacadeEjb implements CaseSamplesDetailedExportFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(CaseSamplesDetailedExportFacadeEjb.class);

	@PersistenceContext(unitName = ModelConstants.PERSISTENCE_UNIT_NAME)
	private EntityManager em;

	@EJB
	private CaseSamplesDetailedExportService exportService;
	@EJB
	private CaseSamplesDetailedExportAsyncService asyncService;
	@EJB
	private UserService userService;

	@Override
	@RightsAllowed(UserRight._CASE_EXPORT)
	public String startDetailedSampleExport(
		CaseCriteria criteria,
		Collection<String> selectedRows,
		CaseExportType exportType,
		ExportConfigurationDto exportConfiguration,
		Language userLanguage) {

		User currentUser = userService.getCurrentUser();
		if (currentUser == null) {
			throw new AccessDeniedException(I18nProperties.getString(Strings.errorForbidden));
		}

		String rawToken = CaseSamplesDetailedExportAsyncService.generateRawToken();

		CaseSamplesDetailedExport export = new CaseSamplesDetailedExport();
		export.setUuid(DataHelper.createUuid());
		export.setRequestingUser(currentUser);
		export.setRequestedDate(new Date());
		export.setResult(CaseSamplesDetailedExportResult.IN_PROGRESS);
		export.setFilterSummary(buildFilterSummary(criteria, selectedRows));
		export.setTokenHash(CaseSamplesDetailedExportAsyncService.hashToken(rawToken));
		export.setDownloadCount(0);
		// Use REQUIRES_NEW so the row is committed before the async thread reads it.
		exportService.persistNewExport(export);

		asyncService.generateAndNotify(
			export.getUuid(),
			criteria,
			selectedRows,
			exportType != null ? exportType : CaseExportType.CASE_SURVEILLANCE,
			exportConfiguration,
			userLanguage,
			rawToken);

		return export.getUuid();
	}

	@Override
	@RightsAllowed(UserRight._CASE_EXPORT)
	public List<CaseSamplesDetailedExportDto> getIndexList(Integer first, Integer max, List<SortProperty> sortProperties) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CaseSamplesDetailedExport> cq = cb.createQuery(CaseSamplesDetailedExport.class);
		Root<CaseSamplesDetailedExport> root = cq.from(CaseSamplesDetailedExport.class);
		root.fetch(CaseSamplesDetailedExport.REQUESTING_USER, JoinType.LEFT);

		cq.select(root);
		cq.orderBy(buildOrder(cb, root, sortProperties));

		return QueryHelper.getResultList(em, cq, first, max, this::toDto);
	}

	@Override
	@RightsAllowed(UserRight._CASE_EXPORT)
	public long count() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<CaseSamplesDetailedExport> root = cq.from(CaseSamplesDetailedExport.class);
		cq.select(cb.count(root));
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	@RightsAllowed(UserRight._CASE_EXPORT)
	public List<CaseSamplesDetailedExportDownloadDto> getDownloads(String exportUuid) {
		CaseSamplesDetailedExport export = exportService.getByUuid(exportUuid);
		if (export == null) {
			return new ArrayList<>();
		}

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CaseSamplesDetailedExportDownload> cq = cb.createQuery(CaseSamplesDetailedExportDownload.class);
		Root<CaseSamplesDetailedExportDownload> root = cq.from(CaseSamplesDetailedExportDownload.class);
		root.fetch(CaseSamplesDetailedExportDownload.DOWNLOADING_USER, JoinType.LEFT);
		cq.where(cb.equal(root.get(CaseSamplesDetailedExportDownload.EXPORT).get(AbstractDomainObject.ID), export.getId()));
		cq.orderBy(cb.desc(root.get(CaseSamplesDetailedExportDownload.DOWNLOADED_AT)));

		return em.createQuery(cq).getResultList().stream().map(this::toDownloadDto).collect(Collectors.toList());
	}

	@Override
	@PermitAll
	public CaseSamplesDetailedExportFileDto resolveDownloadToken(String rawToken) {
		if (StringUtils.isBlank(rawToken)) {
			return null;
		}

		CaseSamplesDetailedExport export = exportService.getByTokenHash(CaseSamplesDetailedExportAsyncService.hashToken(rawToken));
		if (export == null) {
			return null;
		}
		if (export.getResult() != CaseSamplesDetailedExportResult.SUCCESS) {
			return null;
		}
		if (export.getExpiresAt() == null || export.getExpiresAt().before(new Date())) {
			return null;
		}
		if (StringUtils.isBlank(export.getFilePath()) || !new File(export.getFilePath()).isFile()) {
			return null;
		}

		CaseSamplesDetailedExportFileDto dto = new CaseSamplesDetailedExportFileDto();
		dto.setExportUuid(export.getUuid());
		dto.setAbsoluteFilePath(export.getFilePath());
		dto.setFileName(export.getFileName());
		return dto;
	}

	@Override
	@RightsAllowed(UserRight._CASE_EXPORT)
	public CaseSamplesDetailedExportFileDto getFileForAuthenticatedUser(String exportUuid) {
		if (StringUtils.isBlank(exportUuid)) {
			return null;
		}
		CaseSamplesDetailedExport export = exportService.getByUuid(exportUuid);
		if (export == null) {
			return null;
		}
		boolean fileAvailable = (export.getResult() == CaseSamplesDetailedExportResult.SUCCESS
			|| export.getResult() == CaseSamplesDetailedExportResult.FAILED)
			&& export.getExpiresAt() != null
			&& export.getExpiresAt().after(new Date())
			&& StringUtils.isNotBlank(export.getFilePath())
			&& new File(export.getFilePath()).isFile();
		if (!fileAvailable) {
			return null;
		}
		CaseSamplesDetailedExportFileDto dto = new CaseSamplesDetailedExportFileDto();
		dto.setExportUuid(export.getUuid());
		dto.setAbsoluteFilePath(export.getFilePath());
		dto.setFileName(export.getFileName());
		return dto;
	}

	@Override
	@PermitAll
	public void recordDownload(String exportUuid, String downloadingUserName) {
		CaseSamplesDetailedExport export = exportService.getByUuid(exportUuid);
		if (export == null) {
			return;
		}
		if (export.getResult() != CaseSamplesDetailedExportResult.SUCCESS
			&& export.getResult() != CaseSamplesDetailedExportResult.FAILED) {
			return;
		}

		CaseSamplesDetailedExportDownload download = new CaseSamplesDetailedExportDownload();
		download.setUuid(DataHelper.createUuid());
		download.setExport(export);
		download.setDownloadedAt(new Date());
		if (StringUtils.isNotBlank(downloadingUserName)
			&& !"ANONYMOUS".equalsIgnoreCase(downloadingUserName)
			&& !"SYSTEM".equalsIgnoreCase(downloadingUserName)) {
			download.setDownloadingUser(userService.getByUserName(downloadingUserName));
		}

		export.getDownloads().add(download);
		export.setDownloadCount(export.getDownloadCount() + 1);
		exportService.ensurePersisted(export);
	}

	@Override
	@RightsAllowed(UserRight._SYSTEM)
	public void cleanupStaleInProgressExports() {
		// Never-started exports: 3-hour threshold from requestedDate.
		// Stalled exports that made progress: 30-minute threshold from lastProgressAt.
		Date neverStartedThreshold = DateHelper.addSeconds(new Date(), -3 * 3600);
		Date stalledThreshold = DateHelper.addSeconds(new Date(), -30 * 60);
		List<CaseSamplesDetailedExport> stale = exportService.getStaleInProgressExports(neverStartedThreshold, stalledThreshold);
		for (CaseSamplesDetailedExport export : stale) {
			export.setResult(CaseSamplesDetailedExportResult.FAILED);
			export.setFailureMessage("Export did not complete within the expected time. Please try again.");
			exportService.ensurePersisted(export);
		}
		if (!stale.isEmpty()) {
			LOGGER.info("Marked {} stale IN_PROGRESS detailed sample export(s) as FAILED", stale.size());
		}
	}

	@Override
	@RightsAllowed(UserRight._SYSTEM)
	public void cleanupExpiredExports() {
		List<CaseSamplesDetailedExport> expired = exportService.getExpiredWithFiles(new Date());
		int deleted = 0;
		for (CaseSamplesDetailedExport export : expired) {
			if (StringUtils.isNotBlank(export.getFilePath())) {
				try {
					Files.deleteIfExists(new File(export.getFilePath()).toPath());
					deleted++;
				} catch (Exception e) {
					LOGGER.warn("Could not delete expired export file {}", export.getFilePath(), e);
				}
				export.setFilePath(null);
				exportService.ensurePersisted(export);
			}
		}
		LOGGER.info("Cleaned up {} expired detailed sample export file(s)", deleted);
	}

	private List<Order> buildOrder(CriteriaBuilder cb, Root<CaseSamplesDetailedExport> root, List<SortProperty> sortProperties) {
		List<Order> order = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(sortProperties)) {
			for (SortProperty sortProperty : sortProperties) {
				javax.persistence.criteria.Expression<?> path = root.get(sortProperty.propertyName);
				order.add(sortProperty.ascending ? cb.asc(path) : cb.desc(path));
			}
		}
		if (order.isEmpty()) {
			order.add(cb.desc(root.get(CaseSamplesDetailedExport.REQUESTED_DATE)));
		}
		return order;
	}

	private String buildFilterSummary(CaseCriteria criteria, Collection<String> selectedRows) {
		List<String> parts = new ArrayList<>();
		if (criteria != null) {
			if (criteria.getDisease() != null) {
				parts.add("Disease: " + criteria.getDisease().toString());
			}
			if (criteria.getRegion() != null) {
				parts.add("Region: " + criteria.getRegion().getCaption());
			}
			if (criteria.getDistrict() != null) {
				parts.add("District: " + criteria.getDistrict().getCaption());
			}
			if (criteria.getNewCaseDateFrom() != null || criteria.getNewCaseDateTo() != null) {
				parts.add(
					"Dates: " + DateHelper.formatLocalDate(criteria.getNewCaseDateFrom(), Language.EN) + " - "
						+ DateHelper.formatLocalDate(criteria.getNewCaseDateTo(), Language.EN));
			}
		}
		int selectedCount = selectedRows != null ? selectedRows.size() : 0;
		if (selectedCount > 0) {
			parts.add("Selected cases: " + selectedCount);
		} else {
			parts.add("All matching cases");
		}
		return String.join("; ", parts);
	}

	private CaseSamplesDetailedExportDto toDto(@NotNull CaseSamplesDetailedExport source) {
		CaseSamplesDetailedExportDto dto = new CaseSamplesDetailedExportDto();
		dto.setUuid(source.getUuid());
		dto.setCreationDate(source.getCreationDate());
		dto.setChangeDate(source.getChangeDate());
		dto.setRequestingUser(UserFacadeEjb.toReferenceDto(source.getRequestingUser()));
		dto.setRequestedDate(source.getRequestedDate());
		dto.setResult(source.getResult());
		dto.setFilterSummary(source.getFilterSummary());
		dto.setExportedRowCount(source.getExportedRowCount());
		dto.setProgressRowCount(source.getProgressRowCount());
		dto.setTotalCaseCount(source.getTotalCaseCount());
		dto.setFileName(source.getFileName());
		dto.setExpiresAt(source.getExpiresAt());
		dto.setEmailSentDate(source.getEmailSentDate());
		dto.setFailureMessage(source.getFailureMessage());
		dto.setFailureStackTrace(source.getFailureStackTrace());
		dto.setPartial(source.isPartial());
		dto.setDownloadCount(source.getDownloadCount());
		return dto;
	}

	private CaseSamplesDetailedExportDownloadDto toDownloadDto(CaseSamplesDetailedExportDownload source) {
		CaseSamplesDetailedExportDownloadDto dto = new CaseSamplesDetailedExportDownloadDto();
		dto.setDownloadedAt(source.getDownloadedAt());
		dto.setDownloadingUser(UserFacadeEjb.toReferenceDto(source.getDownloadingUser()));
		if (source.getDownloadingUser() == null) {
			dto.setNotLoggedInLabel(I18nProperties.getCaption(Captions.caseSamplesDetailedExportNotLoggedIn));
		}
		return dto;
	}

	@LocalBean
	@Stateless
	public static class CaseSamplesDetailedExportFacadeEjbLocal extends CaseSamplesDetailedExportFacadeEjb {
	}
}
