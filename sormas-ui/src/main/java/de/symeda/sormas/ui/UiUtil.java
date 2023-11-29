package de.symeda.sormas.ui;

import com.vaadin.ui.JavaScript;
import java.util.Set;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.auditlog.AuditLogEntryDto;
import de.symeda.sormas.api.auditlog.ChangeType;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.uuid.HasUuid;

public class UiUtil {

	private UiUtil() {
	}

	public static boolean permitted(FeatureType feature, UserRight userRight) {
		return (feature == null || enabled(feature)) && (userRight == null || permitted(userRight));
	}

	public static boolean permitted(Set<FeatureType> features, UserRight userRight) {
		return enabled(features) && permitted(userRight);
	}

	public static boolean permitted(UserRight userRight) {
		return UserProvider.getCurrent().hasUserRight(userRight);
	}

	public static boolean enabled(FeatureType featureType) {
		return !FacadeProvider.getFeatureConfigurationFacade().isFeatureDisabled(featureType);
	}

	public static boolean enabled(Set<FeatureType> features) {
		return FacadeProvider.getFeatureConfigurationFacade().areAllFeatureEnabled(features.toArray(new FeatureType[] {}));
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
