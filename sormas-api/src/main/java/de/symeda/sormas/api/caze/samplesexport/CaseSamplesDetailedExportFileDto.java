package de.symeda.sormas.api.caze.samplesexport;

import java.io.Serializable;

/**
 * Payload returned to the public download servlet when a token is valid.
 */
public class CaseSamplesDetailedExportFileDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String exportUuid;
	private String absoluteFilePath;
	private String fileName;

	public String getExportUuid() {
		return exportUuid;
	}

	public void setExportUuid(String exportUuid) {
		this.exportUuid = exportUuid;
	}

	public String getAbsoluteFilePath() {
		return absoluteFilePath;
	}

	public void setAbsoluteFilePath(String absoluteFilePath) {
		this.absoluteFilePath = absoluteFilePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
