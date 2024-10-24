/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.api.event;

import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DependingOnFeatureType;
import de.symeda.sormas.api.utils.PersonalData;

@DependingOnFeatureType(featureType = FeatureType.EVENT_SURVEILLANCE)
public class EventParticipantReferenceDto extends ReferenceDto {

	private static final long serialVersionUID = -8725734604520880084L;

	@PersonalData
	private String firstName;
	@PersonalData
	private String lastName;
	@PersonalData
	private String otherName;

	public EventParticipantReferenceDto() {
	}

	public EventParticipantReferenceDto(String uuid) {
		setUuid(uuid);
	}

	public EventParticipantReferenceDto(String uuid, String firstName, String lastName, String otherName) {
		super(uuid);
		this.firstName = firstName;
		this.lastName = lastName;
		this.otherName = lastName;
	}

	public EventParticipantReferenceDto(String uuid, String firstName, String lastName) {
		super(uuid);
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public String getCaption() {
		return buildCaption(getUuid(), firstName, lastName, otherName);
	}

	public static String buildCaption(String uuid, String firstName, String lastName, String otherName) {

		String personName = PersonDto.buildCaption(firstName, lastName, otherName);
		String shortUuid = DataHelper.getShortUuid(uuid);

		if (personName.trim().length() > 0) {
			return personName + " (" + shortUuid + ")";
		}

		return shortUuid;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	public String getOtherName() {return otherName;}
}
