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
package de.symeda.sormas.api.person;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import de.symeda.sormas.api.customizableenum.CustomizableEnum;

public class OccupationType extends CustomizableEnum implements Serializable {

	public static final String I18N_PREFIX = "OccupationType";
	public static final String HAS_DETAILS = "hasDetails";

	public static final String HEALTHCARE_WORKER = "HEALTHCARE_WORKER";
	public static final String LABORATORY_STAFF = "LABORATORY_STAFF";
	public static final String OTHER = "OTHER";
	public static final String UNKNOWN = "UNKNOWN";
	//public static final String TRANSPORTER="TRANSPORTER";
	//public static final  String BUSINESSMAN_WOMAN="BUSINESSMAN_WOMAN";
	private boolean hasDetails;

	@Override
	public void setProperties(Map<String, Object> properties) {

		if (properties == null || CollectionUtils.isEmpty(properties.keySet())) {
			return;
		}

		Set<String> propertyKeys = properties.keySet();
		for (String propertyKey : propertyKeys) {
			if (propertyKey.equals(HAS_DETAILS)) {
				hasDetails = (boolean) properties.get(HAS_DETAILS);
			} else {
				throw new IllegalArgumentException("Property " + propertyKey + " is not a member of OccupationType");
			}
		}
	}

	@Override
	public boolean matchPropertyValue(String propertyKey, Object value) {

		if (propertyKey == null || value == null) {
			return false;
		}

		if (propertyKey.equals(HAS_DETAILS)) {
			return value.equals(isHasDetails());
		} else {
			throw new IllegalArgumentException("Property " + propertyKey + " is not a member of OccupationType");
		}
	}

	public static Map<String, Map<String, Object>> getDefaultValues() {

		Map<String, Map<String, Object>> defaultValues = new HashMap<>();
		Map<String, Object> propertiesDetailsTrue = new HashMap<>();
		propertiesDetailsTrue.put(HAS_DETAILS, true);
		Map<String, Object> propertiesDetailsFalse = new HashMap<>();
		propertiesDetailsFalse.put(HAS_DETAILS, false);
		defaultValues.put("HEALTHCARE_WORKER", propertiesDetailsTrue);
		defaultValues.put("LABORATORY_STAFF", propertiesDetailsFalse);
		defaultValues.put("OTHER", propertiesDetailsTrue);
		defaultValues.put("UNKNOWN", propertiesDetailsFalse);
		//defaultValues.put("TRANSPORTER", propertiesDetailsFalseForTransporter());
		//defaultValues.put("BUSINESSMAN_WOMAN", propertiesDetailsFalseForBusinessmanWoman());

		return defaultValues;
	}

	private static Map<String, Object> propertiesDetailsFalseForTransporter() {
		Map<String, Object> propertiesDetailsFalse = new HashMap<>();
		propertiesDetailsFalse.put(HAS_DETAILS, false);
		// Add other properties specific to TRANSPORTER if needed
		return propertiesDetailsFalse;
	}

	private static Map<String, Object> propertiesDetailsFalseForBusinessmanWoman() {
		Map<String, Object> propertiesDetailsFalse = new HashMap<>();
		propertiesDetailsFalse.put(HAS_DETAILS, false);
		// Add other properties specific to BUSINESSMAN_WOMAN if needed
		return propertiesDetailsFalse;
	}

	public boolean isHasDetails() {
		return hasDetails;
	}
}
