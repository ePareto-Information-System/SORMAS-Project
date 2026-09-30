package de.symeda.sormas.backend.caze.samplesexport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.symeda.sormas.api.caze.samplesexport.CaseSamplesDetailedExportResult;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.user.User;

@Entity(name = CaseSamplesDetailedExport.TABLE_NAME)
public class CaseSamplesDetailedExport extends AbstractDomainObject {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "casesamplesdetailedexport";

	public static final String REQUESTING_USER = "requestingUser";
	public static final String REQUESTED_DATE = "requestedDate";
	public static final String RESULT = "result";
	public static final String FILTER_SUMMARY = "filterSummary";
	public static final String EXPORTED_ROW_COUNT = "exportedRowCount";
	public static final String FILE_NAME = "fileName";
	public static final String FILE_PATH = "filePath";
	public static final String TOKEN_HASH = "tokenHash";
	public static final String EXPIRES_AT = "expiresAt";
	public static final String EMAIL_SENT_DATE = "emailSentDate";
	public static final String FAILURE_MESSAGE = "failureMessage";
	public static final String DOWNLOAD_COUNT = "downloadCount";
	public static final String DOWNLOADS = "downloads";
	public static final String PROGRESS_ROW_COUNT = "progressRowCount";
	public static final String TOTAL_CASE_COUNT = "totalCaseCount";
	public static final String FAILURE_STACK_TRACE = "failureStackTrace";
	public static final String IS_PARTIAL = "isPartial";
	public static final String LAST_PROGRESS_AT = "lastProgressAt";

	private User requestingUser;
	private Date requestedDate;
	private CaseSamplesDetailedExportResult result;
	private String filterSummary;
	private Integer exportedRowCount;
	private Integer progressRowCount;
	private Long totalCaseCount;
	private String fileName;
	private String filePath;
	private String tokenHash;
	private Date expiresAt;
	private Date emailSentDate;
	private String failureMessage;
	private String failureStackTrace;
	private boolean isPartial;
	private Date lastProgressAt;
	private int downloadCount;
	private List<CaseSamplesDetailedExportDownload> downloads = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public User getRequestingUser() {
		return requestingUser;
	}

	public void setRequestingUser(User requestingUser) {
		this.requestingUser = requestingUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	public CaseSamplesDetailedExportResult getResult() {
		return result;
	}

	public void setResult(CaseSamplesDetailedExportResult result) {
		this.result = result;
	}

	@Column(columnDefinition = "text")
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

	public Integer getProgressRowCount() {
		return progressRowCount;
	}

	public void setProgressRowCount(Integer progressRowCount) {
		this.progressRowCount = progressRowCount;
	}

	public Long getTotalCaseCount() {
		return totalCaseCount;
	}

	public void setTotalCaseCount(Long totalCaseCount) {
		this.totalCaseCount = totalCaseCount;
	}

	@Column(length = 512)
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(length = 1024)
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(length = 128)
	public String getTokenHash() {
		return tokenHash;
	}

	public void setTokenHash(String tokenHash) {
		this.tokenHash = tokenHash;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getEmailSentDate() {
		return emailSentDate;
	}

	public void setEmailSentDate(Date emailSentDate) {
		this.emailSentDate = emailSentDate;
	}

	@Column(columnDefinition = "text")
	public String getFailureMessage() {
		return failureMessage;
	}

	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}

	@Column(columnDefinition = "text")
	public String getFailureStackTrace() {
		return failureStackTrace;
	}

	public void setFailureStackTrace(String failureStackTrace) {
		this.failureStackTrace = failureStackTrace;
	}

	@Column(name = "ispartial", nullable = false)
	public boolean isPartial() {
		return isPartial;
	}

	public void setPartial(boolean isPartial) {
		this.isPartial = isPartial;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastProgressAt() {
		return lastProgressAt;
	}

	public void setLastProgressAt(Date lastProgressAt) {
		this.lastProgressAt = lastProgressAt;
	}

	@Column(nullable = false)
	public int getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}

	@OneToMany(mappedBy = CaseSamplesDetailedExportDownload.EXPORT, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CaseSamplesDetailedExportDownload> getDownloads() {
		return downloads;
	}

	public void setDownloads(List<CaseSamplesDetailedExportDownload> downloads) {
		this.downloads = downloads;
	}
}
