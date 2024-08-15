package de.symeda.sormas.api.importexport;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum ExportGroupType {

	CORE,
	SENSITIVE,
	PERSON,
	HOSPITALIZATION,
	EPIDEMIOLOGICAL,
	VACCINATION,
	CLINICAL_COURSE,
	THERAPY,
	FOLLOW_UP,
	ADDITIONAL,
	LOCATION,
	EVENT,
	EVENT_GROUP,
	EVENT_SOURCE,
	CASE_MANAGEMENT,
	EBS,
	TRIAGING,
	SIGNAL_VERIFICATION,
	RISK_ASSESSMENT,
	EBS_ALERT;

	@Override
	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
