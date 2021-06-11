package de.symeda.sormas.api.utils;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum MildModerateSevereCritical {
	MILD, MODERATE, SEVERE, CRITICAL;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
