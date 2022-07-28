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
package de.symeda.sormas.api.statistics;

import de.symeda.sormas.api.i18n.I18nProperties;

public enum StatisticsCaseAttribute {

	ONSET_TIME(StatisticsCaseAttributeGroup.TIME,
		false,
		true,
		StatisticsSubAttribute.YEAR,
		StatisticsSubAttribute.QUARTER,
		StatisticsSubAttribute.MONTH,
		StatisticsSubAttribute.EPI_WEEK,
		StatisticsSubAttribute.QUARTER_OF_YEAR,
		StatisticsSubAttribute.MONTH_OF_YEAR,
		StatisticsSubAttribute.EPI_WEEK_OF_YEAR,
		StatisticsSubAttribute.DATE_RANGE),

	REPORT_TIME(StatisticsCaseAttributeGroup.TIME,
		false,
		false,
		StatisticsSubAttribute.YEAR,
		StatisticsSubAttribute.QUARTER,
		StatisticsSubAttribute.MONTH,
		StatisticsSubAttribute.EPI_WEEK,
		StatisticsSubAttribute.QUARTER_OF_YEAR,
		StatisticsSubAttribute.MONTH_OF_YEAR,
		StatisticsSubAttribute.EPI_WEEK_OF_YEAR,
		StatisticsSubAttribute.DATE_RANGE),

	OUTCOME_TIME(StatisticsCaseAttributeGroup.TIME,
		false,
		true,
		StatisticsSubAttribute.YEAR,
		StatisticsSubAttribute.QUARTER,
		StatisticsSubAttribute.MONTH,
		StatisticsSubAttribute.EPI_WEEK,
		StatisticsSubAttribute.QUARTER_OF_YEAR,
		StatisticsSubAttribute.MONTH_OF_YEAR,
		StatisticsSubAttribute.EPI_WEEK_OF_YEAR,
		StatisticsSubAttribute.DATE_RANGE),

	JURISDICTION(StatisticsCaseAttributeGroup.PLACE,
		true,
		true,
		StatisticsSubAttribute.REGION,
		StatisticsSubAttribute.DISTRICT,
		StatisticsSubAttribute.COMMUNITY,
		StatisticsSubAttribute.FACILITY),

	PLACE_OF_RESIDENCE(StatisticsCaseAttributeGroup.PERSON,
		true,
		true,
		false,
		StatisticsSubAttribute.PERSON_REGION,
		StatisticsSubAttribute.PERSON_DISTRICT,
		StatisticsSubAttribute.PERSON_COMMUNITY,
		StatisticsSubAttribute.PERSON_CITY,
		StatisticsSubAttribute.PERSON_POSTCODE),

	SEX(StatisticsCaseAttributeGroup.PERSON, true, false),
	AGE_INTERVAL_1_YEAR(StatisticsCaseAttributeGroup.PERSON, false, true),
	AGE_INTERVAL_5_YEARS(StatisticsCaseAttributeGroup.PERSON, false, true),
	AGE_INTERVAL_CHILDREN_COARSE(StatisticsCaseAttributeGroup.PERSON, false, true),
	AGE_INTERVAL_CHILDREN_FINE(StatisticsCaseAttributeGroup.PERSON, false, true),
	AGE_INTERVAL_CHILDREN_MEDIUM(StatisticsCaseAttributeGroup.PERSON, false, true),
	AGE_INTERVAL_BASIC(StatisticsCaseAttributeGroup.PERSON, false, true),
	DISEASE(StatisticsCaseAttributeGroup.CASE, true, false),
	CLASSIFICATION(StatisticsCaseAttributeGroup.CASE, true, false),
	OUTCOME(StatisticsCaseAttributeGroup.CASE, true, false),
	REPORTING_USER_ROLE(StatisticsCaseAttributeGroup.CASE, true, false);

	private final StatisticsCaseAttributeGroup attributeGroup;
	private final boolean sortByCaption;
	private final boolean unknownValueAllowed;
	private boolean usedForVisualisation = true;
	private final StatisticsSubAttribute[] subAttributes;

	StatisticsCaseAttribute(
		StatisticsCaseAttributeGroup attributeGroup,
		boolean sortByCaption,
		boolean unknownValueAllowed,
		StatisticsSubAttribute... subAttributes) {

		this.attributeGroup = attributeGroup;
		this.sortByCaption = sortByCaption;
		this.unknownValueAllowed = unknownValueAllowed;
		this.subAttributes = subAttributes;
	}

	StatisticsCaseAttribute(
		StatisticsCaseAttributeGroup attributeGroup,
		boolean sortByCaption,
		boolean unknownValueAllowed,
		boolean usedForVisualisation,
		StatisticsSubAttribute... subAttributes) {

		this.usedForVisualisation = usedForVisualisation;
		this.attributeGroup = attributeGroup;
		this.sortByCaption = sortByCaption;
		this.unknownValueAllowed = unknownValueAllowed;
		this.subAttributes = subAttributes;
	}

	public StatisticsCaseAttributeGroup getAttributeGroup() {
		return attributeGroup;
	}

	public boolean isSortByCaption() {
		return sortByCaption;
	}

	public boolean isUsedForVisualisation() {
		return usedForVisualisation;
	}

	public boolean isUnknownValueAllowed() {
		return unknownValueAllowed;
	}

	public StatisticsSubAttribute[] getSubAttributes() {
		return subAttributes;
	}

	public String toString() {
		return I18nProperties.getEnumCaption(this);
	}

	public boolean isAgeGroup() {

		return this == AGE_INTERVAL_1_YEAR
			|| this == AGE_INTERVAL_5_YEARS
			|| this == AGE_INTERVAL_BASIC
			|| this == AGE_INTERVAL_CHILDREN_COARSE
			|| this == AGE_INTERVAL_CHILDREN_FINE
			|| this == AGE_INTERVAL_CHILDREN_MEDIUM;
	}

	public boolean isPopulationData() {
		return this == JURISDICTION || this == SEX || this.isAgeGroup();
	}
}
