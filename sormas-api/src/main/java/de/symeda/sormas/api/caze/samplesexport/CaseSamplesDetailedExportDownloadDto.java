package de.symeda.sormas.api.caze.samplesexport;

import java.io.Serializable;
import java.util.Date;

import de.symeda.sormas.api.user.UserReferenceDto;

public class CaseSamplesDetailedExportDownloadDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String I18N_PREFIX = "CaseSamplesDetailedExportDownload";

	public static final String DOWNLOADED_AT = "downloadedAt";
	public static final String DOWNLOADING_USER = "downloadingUser";
	public static final String NOT_LOGGED_IN_LABEL = "notLoggedInLabel";
	public static final String CLIENT_ADDRESS = "clientAddress";

	private Date downloadedAt;
	private UserReferenceDto downloadingUser;
	private String notLoggedInLabel;
	private String clientAddress;

	public Date getDownloadedAt() {
		return downloadedAt;
	}

	public void setDownloadedAt(Date downloadedAt) {
		this.downloadedAt = downloadedAt;
	}

	public UserReferenceDto getDownloadingUser() {
		return downloadingUser;
	}

	public void setDownloadingUser(UserReferenceDto downloadingUser) {
		this.downloadingUser = downloadingUser;
	}

	public String getNotLoggedInLabel() {
		return notLoggedInLabel;
	}

	public void setNotLoggedInLabel(String notLoggedInLabel) {
		this.notLoggedInLabel = notLoggedInLabel;
	}

	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public String getDownloaderCaption() {
		if (downloadingUser != null) {
			return downloadingUser.getCaption();
		}
		return notLoggedInLabel != null ? notLoggedInLabel : "";
	}
}
