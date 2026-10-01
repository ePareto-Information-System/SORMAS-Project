package de.symeda.sormas.backend.caze.samplesexport;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.user.User;

@Entity(name = CaseSamplesDetailedExportDownload.TABLE_NAME)
public class CaseSamplesDetailedExportDownload extends AbstractDomainObject {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "casesamplesdetailedexportdownload";

	public static final String EXPORT = "export";
	public static final String DOWNLOADED_AT = "downloadedAt";
	public static final String DOWNLOADING_USER = "downloadingUser";

	private CaseSamplesDetailedExport export;
	private Date downloadedAt;
	private User downloadingUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public CaseSamplesDetailedExport getExport() {
		return export;
	}

	public void setExport(CaseSamplesDetailedExport export) {
		this.export = export;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getDownloadedAt() {
		return downloadedAt;
	}

	public void setDownloadedAt(Date downloadedAt) {
		this.downloadedAt = downloadedAt;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getDownloadingUser() {
		return downloadingUser;
	}

	public void setDownloadingUser(User downloadingUser) {
		this.downloadingUser = downloadingUser;
	}
}
