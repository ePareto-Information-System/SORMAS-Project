package de.symeda.sormas.ui.statistics;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum ContactCountOrIncidence {

	CONTACT_COUNT,
	CONTACT_INCIDENCE;
	
	@Override
	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
