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
package de.symeda.sormas.api.statistics.caze;

import de.symeda.sormas.api.i18n.I18nProperties;


public enum StatisticsCaseAttributeEnum {

//	TIME(StatisticsCaseAttributeGroupEnum.TIME, false, false, StatisticsCaseSubAttributeEnum.YEAR,StatisticsCaseSubAttributeEnum.QUARTER,StatisticsCaseSubAttributeEnum.MONTH,StatisticsCaseSubAttributeEnum.EPI_WEEK,
//			StatisticsCaseSubAttributeEnum.QUARTER_OF_YEAR,StatisticsCaseSubAttributeEnum.MONTH_OF_YEAR,StatisticsCaseSubAttributeEnum.EPI_WEEK_OF_YEAR,StatisticsCaseSubAttributeEnum.DATE_RANGE),
//	REGION_DISTRICT(StatisticsCaseAttributeGroupEnum.PLACE, true, false, StatisticsCaseSubAttributeEnum.REGION,StatisticsCaseSubAttributeEnum.DISTRICT),
//	
//	USER_ROLE(StatisticsCaseAttributeGroupEnum.ENTITY, true, false),
	

	ONSET_TIME(StatisticsCaseAttributeGroupEnum.TIME,
		false,
		true,
		StatisticsCaseSubAttributeEnum.YEAR,
		StatisticsCaseSubAttributeEnum.QUARTER,
		StatisticsCaseSubAttributeEnum.MONTH,
		StatisticsCaseSubAttributeEnum.EPI_WEEK,
		StatisticsCaseSubAttributeEnum.QUARTER_OF_YEAR,
		StatisticsCaseSubAttributeEnum.MONTH_OF_YEAR,
		StatisticsCaseSubAttributeEnum.EPI_WEEK_OF_YEAR,
		StatisticsCaseSubAttributeEnum.DATE_RANGE),

	REPORT_TIME(StatisticsCaseAttributeGroupEnum.TIME,
		false,
		false,
		StatisticsCaseSubAttributeEnum.YEAR,
		StatisticsCaseSubAttributeEnum.QUARTER,
		StatisticsCaseSubAttributeEnum.MONTH,
		StatisticsCaseSubAttributeEnum.EPI_WEEK,
		StatisticsCaseSubAttributeEnum.QUARTER_OF_YEAR,
		StatisticsCaseSubAttributeEnum.MONTH_OF_YEAR,
		StatisticsCaseSubAttributeEnum.EPI_WEEK_OF_YEAR,
		StatisticsCaseSubAttributeEnum.DATE_RANGE),

	OUTCOME_TIME(StatisticsCaseAttributeGroupEnum.TIME,
		false,
		true,
		StatisticsCaseSubAttributeEnum.YEAR,
		StatisticsCaseSubAttributeEnum.QUARTER,
		StatisticsCaseSubAttributeEnum.MONTH,
		StatisticsCaseSubAttributeEnum.EPI_WEEK,
		StatisticsCaseSubAttributeEnum.QUARTER_OF_YEAR,
		StatisticsCaseSubAttributeEnum.MONTH_OF_YEAR,
		StatisticsCaseSubAttributeEnum.EPI_WEEK_OF_YEAR,
		StatisticsCaseSubAttributeEnum.DATE_RANGE),

	JURISDICTION(StatisticsCaseAttributeGroupEnum.PLACE,
		true,
		true,
		StatisticsCaseSubAttributeEnum.REGION,
		StatisticsCaseSubAttributeEnum.DISTRICT,
		StatisticsCaseSubAttributeEnum.COMMUNITY,
		StatisticsCaseSubAttributeEnum.FACILITY),

	PLACE_OF_RESIDENCE(
		StatisticsCaseAttributeGroupEnum.PERSON,
		true,
		true,
		false,
		StatisticsCaseSubAttributeEnum.PERSON_REGION,
		StatisticsCaseSubAttributeEnum.PERSON_DISTRICT,
		StatisticsCaseSubAttributeEnum.PERSON_COMMUNITY,
		StatisticsCaseSubAttributeEnum.PERSON_CITY,
		StatisticsCaseSubAttributeEnum.PERSON_POSTCODE),

	SEX(StatisticsCaseAttributeGroupEnum.PERSON, true, true),
	AGE_INTERVAL_1_YEAR(StatisticsCaseAttributeGroupEnum.PERSON, false, true),
	AGE_INTERVAL_5_YEARS(StatisticsCaseAttributeGroupEnum.PERSON, false, true),
	AGE_INTERVAL_CHILDREN_COARSE(StatisticsCaseAttributeGroupEnum.PERSON, false, true),
	AGE_INTERVAL_CHILDREN_FINE(StatisticsCaseAttributeGroupEnum.PERSON, false, true),
	AGE_INTERVAL_CHILDREN_MEDIUM(StatisticsCaseAttributeGroupEnum.PERSON, false, true),
	AGE_INTERVAL_BASIC(StatisticsCaseAttributeGroupEnum.PERSON, false, true),
	DISEASE(StatisticsCaseAttributeGroupEnum.ENTITY, true, false),

	CLASSIFICATION(StatisticsCaseAttributeGroupEnum.CASE, true, false),
	
	OUTCOME(StatisticsCaseAttributeGroupEnum.CASE, true, false),
	REPORTING_USER_ROLE(StatisticsCaseAttributeGroupEnum.CASE, true, false);
	

	
	private final StatisticsCaseAttributeGroupEnum attributeGroup;
	private final boolean sortByCaption;
	private final boolean unknownValueAllowed;
	private boolean usedForVisualisation = true;
	private final StatisticsCaseSubAttributeEnum[] subAttributes;
	
	StatisticsCaseAttributeEnum(StatisticsCaseAttributeGroupEnum attributeGroup, boolean sortByCaption, boolean unknownValueAllowed, StatisticsCaseSubAttributeEnum ...subAttributes) {
		this.attributeGroup = attributeGroup;
		this.sortByCaption = sortByCaption;
		this.unknownValueAllowed = unknownValueAllowed;
		this.subAttributes = subAttributes;
	}
	
	StatisticsCaseAttributeEnum(
			StatisticsCaseAttributeGroupEnum attributeGroup,
			boolean sortByCaption,
			boolean unknownValueAllowed,
			boolean usedForVisualisation,
			StatisticsCaseSubAttributeEnum... subAttributes) {

			this.usedForVisualisation = usedForVisualisation;
			this.attributeGroup = attributeGroup;
			this.sortByCaption = sortByCaption;
			this.unknownValueAllowed = unknownValueAllowed;
			this.subAttributes = subAttributes;
		}


	



	public StatisticsCaseAttributeGroupEnum getAttributeGroup() {
		return attributeGroup;
	}
	
	public boolean isSortByCaption() {
		return sortByCaption;
	}

	public boolean isUnknownValueAllowed() {
		return unknownValueAllowed;
	}
	
	public StatisticsCaseSubAttributeEnum[] getSubAttributes() {
		return subAttributes;
	}
	
	public boolean isUsedForVisualisation() {
		return usedForVisualisation;
	}
	
	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}
	
	public boolean isAgeGroup() {
		return this == AGE_INTERVAL_1_YEAR || this == AGE_INTERVAL_5_YEARS || this == AGE_INTERVAL_BASIC || this == AGE_INTERVAL_CHILDREN_COARSE
				|| this == AGE_INTERVAL_CHILDREN_FINE || this == AGE_INTERVAL_CHILDREN_MEDIUM;
	}
	
//	public boolean isPopulationData() {
//		return this == REGION_DISTRICT || this == SEX || this.isAgeGroup();
//	}

	public boolean isPopulationData() {
		return this == JURISDICTION || this == SEX || this.isAgeGroup();
	}
	


}
