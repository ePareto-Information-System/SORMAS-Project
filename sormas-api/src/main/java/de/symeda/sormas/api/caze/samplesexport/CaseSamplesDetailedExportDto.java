package de.symeda.sormas.api.caze.samplesexport;

import java.util.Date;

import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.user.UserReferenceDto;

public class CaseSamplesDetailedExportDto extends EntityDto {

	private static final long serialVersionUID = 1L;

	public static final String I18N_PREFIX = "CaseSamplesDetailedExport";

	public static final String REQUESTING_USER = "requestingUser";
	public static final String REQUESTED_DATE = "requestedDate";
	public static final String RESULT = "result";
	public static final String FILTER_SUMMARY = "filterSummary";
	public static final String EXPORTED_ROW_COUNT = "exportedRowCount";
	public static final String FILE_NAME = "fileName";
	public static final String EXPIRES_AT = "expiresAt";
	public static final String EMAIL_SENT_DATE = "emailSentDate";
	public static final String FAILURE_MESSAGE = "failureMessage";
	public static final String DOWNLOAD_COUNT = "downloadCount";

	private UserReferenceDto requestingUser;
	private Date requestedDate;
	private CaseSamplesDetailedExportResult result;
	private String filterSummary;
	private Integer exportedRowCount;
	private String fileName;
	private Date expiresAt;
	private Date emailSentDate;
	private String failureMessage;
	private int downloadCount;

	public UserReferenceDto getRequestingUser() {
		return requestingUser;
	}

	public void setRequestingUser(UserReferenceDto requestingUser) {
		this.requestingUser = requestingUser;
	}

	public Date getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	public CaseSamplesDetailedExportResult getResult() {
		return result;
	}

	public void setResult(CaseSamplesDetailedExportResult result) {
		this.result = result;
	}

	public String getFilterSummary() {
		return filterSummary;
	}

	public void setFilterSummary(String filterSummary) {
		this.filterSummary = filterSummary;
	}

	public Integer getExportedRowCount() {
		return exportedRowCount;
	}

	public void setExportedRowCount(Integer exportedRowCount) {
		this.exportedRowCount = exportedRowCount;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}

	public Date getEmailSentDate() {
		return emailSentDate;
	}

	public void setEmailSentDate(Date emailSentDate) {
		this.emailSentDate = emailSentDate;
	}

	public String getFailureMessage() {
		return failureMessage;
	}

	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}

	public int getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}
}
