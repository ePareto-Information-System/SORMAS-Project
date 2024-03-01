package de.symeda.sormas.ui;

import java.util.Objects;
import java.util.Set;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.common.DeletableEntityType;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.user.UserRight;

public class UiUtil {

	private UiUtil() {
	}

	public static boolean permitted(FeatureType feature, UserRight userRight) {
		return (feature == null || enabled(feature)) && (userRight == null || permitted(userRight));
	}

	public static boolean permitted(Set<FeatureType> features, UserRight userRight) {
		return enabled(features) && permitted(userRight);
	}

	public static boolean permitted(FeatureType feature, UserRight... userRights) {
		return enabled(feature) && permitted(userRights);
	}

	public static boolean permitted(UserRight userRight) {
		return Objects.nonNull(UserProvider.getCurrent()) && UserProvider.getCurrent().hasUserRight(userRight);
	}

	public static boolean permitted(UserRight... userRights) {
		return Objects.nonNull(UserProvider.getCurrent()) && UserProvider.getCurrent().hasAllUserRights(userRights);
	}

	public static boolean enabled(FeatureType featureType) {
		return !disabled(featureType);
	}

	public static boolean enabled(FeatureType featureType, DeletableEntityType entityType) {
		return FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(featureType, entityType);
	}

	public static boolean disabled(FeatureType featureType) {
		return FacadeProvider.getFeatureConfigurationFacade().isFeatureDisabled(featureType);
	}

	public static boolean enabled(Set<FeatureType> features) {
		return FacadeProvider.getFeatureConfigurationFacade().areAllFeatureEnabled(features.toArray(new FeatureType[] {}));
	}
}
