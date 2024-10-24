package de.symeda.sormas.api.caze;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum TransmissionClassification {

	KNOWN_CLUSTER,
	COMMUNITY_TRANSMISSION,
	UNKNOWN;
	
	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
