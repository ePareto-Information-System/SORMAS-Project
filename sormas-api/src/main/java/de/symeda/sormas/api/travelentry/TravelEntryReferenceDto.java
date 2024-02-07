package de.symeda.sormas.api.travelentry;

import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.utils.DependingOnFeatureType;

@DependingOnFeatureType(featureType = FeatureType.TRAVEL_ENTRIES)
public class TravelEntryReferenceDto extends ReferenceDto {

	private String externalId;

	public TravelEntryReferenceDto(String uuid, String externalId, String firstName, String lastName, String otherName) {
		super(uuid, PersonDto.buildCaption(firstName, lastName, otherName));
		this.externalId = externalId;
	}

	public TravelEntryReferenceDto(String uuid) {
		super(uuid);
	}

	public String getExternalId() {
		return externalId;
	}
}
