package de.symeda.sormas.api.caze.samplesexport;

import java.util.Collection;
import java.util.List;

import javax.ejb.Remote;

import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.caze.CaseCriteria;
import de.symeda.sormas.api.caze.CaseExportType;
import de.symeda.sormas.api.importexport.ExportConfigurationDto;
import de.symeda.sormas.api.utils.SortProperty;

@Remote
public interface CaseSamplesDetailedExportFacade {

	/**
	 * Starts an asynchronous detailed sample export. Returns immediately after the job is accepted.
	 *
	 * @return uuid of the created export audit row
	 */
	String startDetailedSampleExport(
		CaseCriteria criteria,
		Collection<String> selectedRows,
		CaseExportType exportType,
		ExportConfigurationDto exportConfiguration,
		Language userLanguage);

	List<CaseSamplesDetailedExportDto> getIndexList(Integer first, Integer max, List<SortProperty> sortProperties);

	long count();

	List<CaseSamplesDetailedExportDownloadDto> getDownloads(String exportUuid);

	/**
	 * Resolves a raw download token to a ready file. Returns null when the token is invalid or expired.
	 */
	CaseSamplesDetailedExportFileDto resolveDownloadToken(String rawToken);

	/**
	 * Returns the file for an authenticated user who has CASE_EXPORT right.
	 * Returns null when the export is not ready, expired, or the file is missing.
	 */
	CaseSamplesDetailedExportFileDto getFileForAuthenticatedUser(String exportUuid);

	/**
	 * Records a successful download for the given export.
	 *
	 * @param downloadingUserName
	 *            login name of the logged-in user, or null when anonymous
	 */
	void recordDownload(String exportUuid, String downloadingUserName);

	/**
	 * Marks IN_PROGRESS exports older than 30 minutes as FAILED. Called from cron.
	 */
	void cleanupStaleInProgressExports();

	/**
	 * Deletes files (complete or partial) whose expiry has passed. Called hourly from cron.
	 */
	void cleanupExpiredExports();
}
