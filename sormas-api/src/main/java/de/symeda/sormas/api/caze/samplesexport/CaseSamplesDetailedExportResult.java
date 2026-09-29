package de.symeda.sormas.api.caze.samplesexport;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum CaseSamplesDetailedExportResult {

	IN_PROGRESS,
	SUCCESS,
	FAILED;

	@Override
	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
