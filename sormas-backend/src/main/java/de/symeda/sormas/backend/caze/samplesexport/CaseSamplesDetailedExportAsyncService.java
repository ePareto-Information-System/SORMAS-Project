package de.symeda.sormas.backend.caze.samplesexport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.MessagingException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.caze.CaseCriteria;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.caze.CaseExportDetailedSampleDto;
import de.symeda.sormas.api.caze.CaseExportType;
import de.symeda.sormas.api.caze.samplesexport.CaseSamplesDetailedExportResult;
import de.symeda.sormas.api.epidata.EpiDataDto;
import de.symeda.sormas.api.hospitalization.HospitalizationDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.immunization.ImmunizationDto;
import de.symeda.sormas.api.importexport.ExportConfigurationDto;
import de.symeda.sormas.api.importexport.ExportTarget;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.symptoms.SymptomsDto;
import de.symeda.sormas.api.utils.CsvStreamUtils;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.vaccination.VaccinationDto;
import de.symeda.sormas.api.visit.VisitExportType;
import de.symeda.sormas.backend.caze.CaseFacadeEjb.CaseFacadeEjbLocal;
import de.symeda.sormas.backend.common.ConfigFacadeEjb.ConfigFacadeEjbLocal;
import de.symeda.sormas.backend.common.messaging.EmailService;
import de.symeda.sormas.backend.user.User;

/**
 * Generates the detailed sample CSV asynchronously and emails a download link.
 * Invoked from {@link CaseSamplesDetailedExportFacadeEjb} so the caller's security
 * context is propagated for jurisdiction-aware export queries.
 */
@Stateless
@LocalBean
public class CaseSamplesDetailedExportAsyncService {

	public static final int EXPORT_LINK_VALIDITY_HOURS = 48;

	private static final Logger LOGGER = LoggerFactory.getLogger(CaseSamplesDetailedExportAsyncService.class);
	private static final char[] HEX = "0123456789abcdef".toCharArray();

	@EJB
	private CaseSamplesDetailedExportService exportService;
	@EJB
	private CaseFacadeEjbLocal caseFacade;
	@EJB
	private ConfigFacadeEjbLocal configFacade;
	@EJB
	private EmailService emailService;

	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void generateAndNotify(
		String exportUuid,
		CaseCriteria criteria,
		Collection<String> selectedRows,
		CaseExportType exportType,
		ExportConfigurationDto exportConfiguration,
		Language userLanguage,
		String rawToken) {

		CaseSamplesDetailedExport export = exportService.getByUuid(exportUuid);
		if (export == null) {
			LOGGER.error("Detailed sample export {} not found", exportUuid);
			return;
		}

		User requestingUser = export.getRequestingUser();
		String recipientEmail = requestingUser != null ? requestingUser.getUserEmail() : null;

		File exportFile = null;
		try {
			String instanceName = DataHelper.cleanStringForFileName(configFacade.getSormasInstanceName().toLowerCase());
			String exportDate = DateHelper.formatDateForExport(new Date());
			String fileName = String.join("_", instanceName, "cases_samples_detailed", exportDate, exportUuid.substring(0, 8) + ".csv");

			File exportDir = new File(configFacade.getGeneratedFilesPath(), "case_sample_exports");
			if (!exportDir.exists() && !exportDir.mkdirs()) {
				throw new IllegalStateException("Could not create export directory: " + exportDir.getAbsolutePath());
			}
			exportFile = new File(exportDir, fileName);

			AtomicInteger rowCount = new AtomicInteger(0);
			Collection<String> selected = selectedRows != null ? selectedRows : new ArrayList<>();

			try (OutputStream out = new FileOutputStream(exportFile)) {
				CsvStreamUtils.writeCsvContentToStream(
					CaseExportDetailedSampleDto.class,
					(start, max) -> {
						List<CaseExportDetailedSampleDto> rows = caseFacade
							.getExportListDetailed(criteria, selected, exportType, start, max, exportConfiguration, userLanguage);
						rowCount.addAndGet(rows.size());
						return rows;
					},
					CaseSamplesDetailedExportAsyncService::captionProvider,
					exportConfiguration,
					(o) -> exportType == null || hasExportTarget(exportType, (Method) o),
					configFacade,
					out);
			}

			Date expiresAt = DateHelper.addSeconds(new Date(), EXPORT_LINK_VALIDITY_HOURS * 3600);
			export.setResult(CaseSamplesDetailedExportResult.SUCCESS);
			export.setFileName(fileName);
			export.setFilePath(exportFile.getAbsolutePath());
			export.setExportedRowCount(rowCount.get());
			export.setExpiresAt(expiresAt);
			export.setFailureMessage(null);
			exportService.ensurePersisted(export);

			sendSuccessEmail(recipientEmail, rawToken, fileName, expiresAt);
			export.setEmailSentDate(new Date());
			exportService.ensurePersisted(export);

		} catch (Exception e) {
			LOGGER.error("Failed to generate detailed sample export {}", exportUuid, e);
			if (exportFile != null && exportFile.exists()) {
				try {
					Files.deleteIfExists(exportFile.toPath());
				} catch (Exception deleteEx) {
					LOGGER.warn("Could not delete failed export file {}", exportFile.getAbsolutePath(), deleteEx);
				}
			}
			export.setResult(CaseSamplesDetailedExportResult.FAILED);
			export.setFailureMessage(StringUtils.abbreviate(e.getMessage(), 2000));
			export.setFileName(null);
			export.setFilePath(null);
			export.setExportedRowCount(null);
			export.setExpiresAt(null);
			exportService.ensurePersisted(export);
			sendFailureEmail(recipientEmail, e.getMessage());
			export.setEmailSentDate(new Date());
			exportService.ensurePersisted(export);
		}
	}

	public static String generateRawToken() {
		byte[] bytes = new byte[32];
		new SecureRandom().nextBytes(bytes);
		return toHex(bytes);
	}

	public static String hashToken(String rawToken) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			return toHex(digest.digest(rawToken.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception e) {
			throw new IllegalStateException("SHA-256 not available", e);
		}
	}

	private static String toHex(byte[] bytes) {
		char[] out = new char[bytes.length * 2];
		for (int i = 0; i < bytes.length; i++) {
			int value = bytes[i] & 0xFF;
			out[i * 2] = HEX[value >>> 4];
			out[i * 2 + 1] = HEX[value & 0x0F];
		}
		return new String(out);
	}

	private void sendSuccessEmail(String recipientEmail, String rawToken, String fileName, Date expiresAt) {
		if (StringUtils.isBlank(recipientEmail)) {
			LOGGER.warn("Cannot send export success email: recipient has no email address");
			return;
		}
		String uiUrl = StringUtils.removeEnd(StringUtils.defaultString(configFacade.getUiUrl()), "/");
		String downloadUrl = uiUrl + "/export-downloads/" + rawToken;
		String subject = "SORMAS detailed sample export ready";
		String content = "Your detailed sample export is ready.\n\n"
			+ "File: " + fileName + "\n"
			+ "Download (valid for " + EXPORT_LINK_VALIDITY_HOURS + " hours, until "
			+ DateHelper.formatLocalDateTime(expiresAt, Language.EN) + "):\n"
			+ downloadUrl + "\n\n"
			+ "Anyone with this link can download the file until it expires.\n";
		try {
			emailService.sendEmail(recipientEmail, subject, content);
		} catch (MessagingException e) {
			LOGGER.error("Failed to send export success email to {}", recipientEmail, e);
		}
	}

	private void sendFailureEmail(String recipientEmail, String errorMessage) {
		if (StringUtils.isBlank(recipientEmail)) {
			return;
		}
		String subject = "SORMAS detailed sample export failed";
		String content = "Your detailed sample export could not be completed.\n\n"
			+ "Please try again or contact an administrator.\n"
			+ (StringUtils.isNotBlank(errorMessage) ? "\nDetails: " + StringUtils.abbreviate(errorMessage, 500) : "");
		try {
			emailService.sendEmail(recipientEmail, subject, content);
		} catch (MessagingException e) {
			LOGGER.error("Failed to send export failure email to {}", recipientEmail, e);
		}
	}

	private static String captionProvider(String propertyId, Class<?> type) {
		String caption = I18nProperties.findPrefixCaption(
			propertyId,
			CaseExportDetailedSampleDto.I18N_PREFIX,
			CaseDataDto.I18N_PREFIX,
			PersonDto.I18N_PREFIX,
			LocationDto.I18N_PREFIX,
			SymptomsDto.I18N_PREFIX,
			EpiDataDto.I18N_PREFIX,
			ImmunizationDto.I18N_PREFIX,
			VaccinationDto.I18N_PREFIX,
			HospitalizationDto.I18N_PREFIX);
		if (caption == null) {
			caption = propertyId;
		}
		if (Date.class.isAssignableFrom(type)) {
			caption += " (" + DateHelper.getLocalDateFormat(Language.EN).toPattern() + ")";
		}
		return caption;
	}

	@SuppressWarnings("rawtypes")
	private static boolean hasExportTarget(Enum<?> exportType, Method m) {
		if (m.isAnnotationPresent(ExportTarget.class)) {
			final Class<? extends Enum> exportTypeClass = exportType.getClass();
			final ExportTarget exportTarget = m.getAnnotation(ExportTarget.class);
			Supplier<Enum[]> exportTypeSupplier = null;
			if (exportTypeClass.isAssignableFrom(CaseExportType.class)) {
				exportTypeSupplier = exportTarget::caseExportTypes;
			}
			if (exportTypeClass.isAssignableFrom(VisitExportType.class)) {
				exportTypeSupplier = exportTarget::visitExportTypes;
			}
			return exportTypeSupplier != null && Arrays.asList(exportTypeSupplier.get()).contains(exportType);
		}
		return false;
	}
}
