package de.symeda.sormas.api;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum EntityRelevanceStatus {

	ACTIVE,
	ARCHIVED,
	ALL;

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}

	public boolean getValue(EntityRelevanceStatus entityRelevanceStatus) {
		if (entityRelevanceStatus == EntityRelevanceStatus.ARCHIVED)
			return true;
		return false;
	}
}
