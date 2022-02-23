package de.symeda.sormas.ui;

import com.vaadin.ui.JavaScript;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.HasUuid;
import de.symeda.sormas.api.auditlog.AuditLogEntryDto;
import de.symeda.sormas.api.auditlog.ChangeType;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.user.UserRight;

public class UiUtil {

	private UiUtil() {
	}

	public static boolean permitted(FeatureType feature, UserRight userRight) {
		return (feature == null || !FacadeProvider.getFeatureConfigurationFacade().isFeatureDisabled(feature))
			&& (userRight == null || UserProvider.getCurrent().hasUserRight(userRight));
	}

	public static boolean permitted(UserRight userRight) {
		return permitted(null, userRight);
	}

	public static boolean enabled(FeatureType featureType) {
		return permitted(featureType, null);
	}
	
	public static void logActivity (HasUuid entity) {
		logActivity(ChangeType.VIEW, entity);
	}
	
	public static void logActivity (ChangeType activityType, HasUuid entity) {
		JavaScript.getCurrent().execute(
			"setTimeout(function () {" +
				"de.symeda.sormas.ui.auditlog.logActivity({" +
					"activityType: '" + activityType.name() + "'," +
					"clazz: '" + AuditLogEntryDto.getEntityClazz(entity.getClass()) + "'," +
					"uuid: '" + entity.getUuid() + "'," +
				"});" +
			"}, 2000)"
		);
	}
}
