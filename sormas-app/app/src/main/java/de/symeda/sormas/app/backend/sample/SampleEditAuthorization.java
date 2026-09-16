package de.symeda.sormas.app.backend.sample;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.app.backend.config.ConfigProvider;
import de.symeda.sormas.app.backend.user.User;
import de.symeda.sormas.app.util.JurisdictionHelper;

public class SampleEditAuthorization {

	public static boolean isSampleEditAllowed(Sample sample) {

		if (isDengueSample(sample)) {
			return false;
		}

		if (sample.getSormasToSormasOriginInfo() != null) {
			return sample.getSormasToSormasOriginInfo().isOwnershipHandedOver();
		}

		final User user = ConfigProvider.getUser();
		final SampleJurisdictionBooleanValidator validator =
				SampleJurisdictionBooleanValidator.of(JurisdictionHelper.createSampleJurisdictionDto(sample), JurisdictionHelper.createUserJurisdiction(user));
		return !sample.isOwnershipHandedOver() && validator.inJurisdictionOrOwned();
	}

	private static boolean isDengueSample(Sample sample) {
		if (sample.getAssociatedCase() != null && sample.getAssociatedCase().getDisease() == Disease.DENGUE) {
			return true;
		}
		if (sample.getAssociatedContact() != null && sample.getAssociatedContact().getDisease() == Disease.DENGUE) {
			return true;
		}
		if (sample.getAssociatedEventParticipant() != null
			&& sample.getAssociatedEventParticipant().getEvent() != null
			&& sample.getAssociatedEventParticipant().getEvent().getDisease() == Disease.DENGUE) {
			return true;
		}
		return false;
	}
}
