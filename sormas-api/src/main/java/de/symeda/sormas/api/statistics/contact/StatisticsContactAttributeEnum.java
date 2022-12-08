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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.api.statistics.contact;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum StatisticsContactAttributeEnum {

	//TIME(StatisticsContactAttributeGroupEnum.TIME, false, false, StatisticsContactSubAttributeEnum.YEAR,StatisticsContactSubAttributeEnum.QUARTER,StatisticsContactSubAttributeEnum.MONTH,StatisticsContactSubAttributeEnum.EPI_WEEK,
		//	StatisticsContactSubAttributeEnum.QUARTER_OF_YEAR,StatisticsContactSubAttributeEnum.MONTH_OF_YEAR,StatisticsContactSubAttributeEnum.EPI_WEEK_OF_YEAR,StatisticsContactSubAttributeEnum.DATE_RANGE),
	//REGION_DISTRICT(StatisticsContactAttributeGroupEnum.PLACE, true, false, StatisticsContactSubAttributeEnum.REGION,StatisticsContactSubAttributeEnum.DISTRICT),
	
	//USER_ROLE(StatisticsContactAttributeGroupEnum.ENTITY, true, false),
	ONSET_TIME(StatisticsContactAttributeGroupEnum.TIME,
			false,
			true,
			StatisticsContactSubAttributeEnum.YEAR,
			StatisticsContactSubAttributeEnum.QUARTER,
			StatisticsContactSubAttributeEnum.MONTH,
			StatisticsContactSubAttributeEnum.EPI_WEEK,
			StatisticsContactSubAttributeEnum.QUARTER_OF_YEAR,
			StatisticsContactSubAttributeEnum.MONTH_OF_YEAR,
			StatisticsContactSubAttributeEnum.EPI_WEEK_OF_YEAR,
			StatisticsContactSubAttributeEnum.DATE_RANGE),
	
	REPORT_TIME(StatisticsContactAttributeGroupEnum.TIME,
			false,
			false,
			StatisticsContactSubAttributeEnum.YEAR,
			StatisticsContactSubAttributeEnum.QUARTER,
			StatisticsContactSubAttributeEnum.MONTH,
			StatisticsContactSubAttributeEnum.EPI_WEEK,
			StatisticsContactSubAttributeEnum.QUARTER_OF_YEAR,
			StatisticsContactSubAttributeEnum.MONTH_OF_YEAR,
			StatisticsContactSubAttributeEnum.EPI_WEEK_OF_YEAR,
			StatisticsContactSubAttributeEnum.DATE_RANGE),
	
	OUTCOME_TIME(StatisticsContactAttributeGroupEnum.TIME,
			false,
			true,
			StatisticsContactSubAttributeEnum.YEAR,
			StatisticsContactSubAttributeEnum.QUARTER,
			StatisticsContactSubAttributeEnum.MONTH,
			StatisticsContactSubAttributeEnum.EPI_WEEK,
			StatisticsContactSubAttributeEnum.QUARTER_OF_YEAR,
			StatisticsContactSubAttributeEnum.MONTH_OF_YEAR,
			StatisticsContactSubAttributeEnum.EPI_WEEK_OF_YEAR,
			StatisticsContactSubAttributeEnum.DATE_RANGE),

	JURISDICTION(StatisticsContactAttributeGroupEnum.PLACE,
			true,
			true,
			StatisticsContactSubAttributeEnum.REGION,
			StatisticsContactSubAttributeEnum.DISTRICT,
			StatisticsContactSubAttributeEnum.COMMUNITY),

		PLACE_OF_RESIDENCE(StatisticsContactAttributeGroupEnum.PERSON,
			true,
			true,
			false,
			StatisticsContactSubAttributeEnum.PERSON_REGION,
			StatisticsContactSubAttributeEnum.PERSON_DISTRICT,
			StatisticsContactSubAttributeEnum.PERSON_COMMUNITY,
			StatisticsContactSubAttributeEnum.PERSON_CITY,
			StatisticsContactSubAttributeEnum.PERSON_POSTCODE),

	SEX(StatisticsContactAttributeGroupEnum.PERSON, true, true),
	AGE_INTERVAL_1_YEAR(StatisticsContactAttributeGroupEnum.PERSON, false, true),
	AGE_INTERVAL_5_YEARS(StatisticsContactAttributeGroupEnum.PERSON, false, true),
	AGE_INTERVAL_CHILDREN_COARSE(StatisticsContactAttributeGroupEnum.PERSON, false, true),
	AGE_INTERVAL_CHILDREN_FINE(StatisticsContactAttributeGroupEnum.PERSON, false, true),
	AGE_INTERVAL_CHILDREN_MEDIUM(StatisticsContactAttributeGroupEnum.PERSON, false, true),
	AGE_INTERVAL_BASIC(StatisticsContactAttributeGroupEnum.PERSON, false, true),
	DISEASE(StatisticsContactAttributeGroupEnum.ENTITY, true, false),
	CLASSIFICATION(StatisticsContactAttributeGroupEnum.CONTACT, true, false),
	FOLLOW_UP_STATUS(StatisticsContactAttributeGroupEnum.CONTACT, true, false), 
	STATUS(StatisticsContactAttributeGroupEnum.CONTACT, true, false),
	REPORTING_USER_ROLE(StatisticsContactAttributeGroupEnum.CONTACT, true, false);
	private final StatisticsContactAttributeGroupEnum attributeGroup;
	private final boolean sortByCaption;
	private final boolean unknownValueAllowed;
	private final StatisticsContactSubAttributeEnum[] subAttributes;
	private boolean usedForVisualisation = true;

	StatisticsContactAttributeEnum(StatisticsContactAttributeGroupEnum attributeGroup, boolean sortByCaption, boolean unknownValueAllowed, StatisticsContactSubAttributeEnum ...subAttributes) {
		this.attributeGroup = attributeGroup;
		this.sortByCaption = sortByCaption;
		this.unknownValueAllowed = unknownValueAllowed;
		this.subAttributes = subAttributes;
	}
	
	


	
	StatisticsContactAttributeEnum(
			StatisticsContactAttributeGroupEnum attributeGroup,
			boolean sortByCaption,
			boolean unknownValueAllowed,
			boolean usedForVisualisation,
			StatisticsContactSubAttributeEnum... subAttributes) {

			this.usedForVisualisation = usedForVisualisation;
			this.attributeGroup = attributeGroup;
			this.sortByCaption = sortByCaption;
			this.unknownValueAllowed = unknownValueAllowed;
			this.subAttributes = subAttributes;
		}

	public StatisticsContactAttributeGroupEnum getAttributeGroup() {
		return attributeGroup;
	}
	
	public boolean isSortByCaption() {
		return sortByCaption;
	}

	public boolean isUnknownValueAllowed() {
		return unknownValueAllowed;
	}
	
	public StatisticsContactSubAttributeEnum[] getSubAttributes() {
		return subAttributes;
	}
	
	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
	
	public boolean isAgeGroup() {
		return this == AGE_INTERVAL_1_YEAR || this == AGE_INTERVAL_5_YEARS || this == AGE_INTERVAL_BASIC || this == AGE_INTERVAL_CHILDREN_COARSE
				|| this == AGE_INTERVAL_CHILDREN_FINE || this == AGE_INTERVAL_CHILDREN_MEDIUM;
	}
	
	public boolean isPopulationData() {
		return this == JURISDICTION || this == SEX || this.isAgeGroup();
	}

	public boolean isUsedForVisualisation() {
		return usedForVisualisation;
	}

}
