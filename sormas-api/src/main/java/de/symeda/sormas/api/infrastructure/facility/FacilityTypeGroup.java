/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2020 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.api.infrastructure.facility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.symeda.sormas.api.event.TypeOfPlace;
import de.symeda.sormas.api.i18n.I18nProperties;

public enum FacilityTypeGroup {

	ACCOMMODATION,
	CARE_FACILITY,
	CATERING_OUTLET,
	EDUCATIONAL_FACILITY,
	LEISURE_FACILITY,
	MEDICAL_FACILITY,
	WORKING_PLACE,
	RESIDENCE,
	COMMERCE;

	private static List<FacilityTypeGroup> accomodationGroups = null;

	public static List<FacilityTypeGroup> getAccomodationGroups() {
		if (accomodationGroups == null) {
			accomodationGroups = new ArrayList<FacilityTypeGroup>();
			for (FacilityTypeGroup group : values()) {
				if (!FacilityType.getAccommodationTypes(group).isEmpty()) {
					accomodationGroups.add(group);
				}
			}
		}
		return accomodationGroups;
	}

	//public static final List<FacilityTypeGroup> FOR_DHIMS = Arrays.asList(CHPS_COMPOUND, MATERNITY_HOME, CLINIC, HEALTH_CENTRE, POLYCLINIC, HOSPITAL);

	@Override
	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
}
