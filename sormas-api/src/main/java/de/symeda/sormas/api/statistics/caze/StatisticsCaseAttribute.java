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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.statistics.StatisticsGroupingKey;
import de.symeda.sormas.api.statistics.StatisticsSubAttribute;
import de.symeda.sormas.api.statistics.caze.StatisticsHelper.getAttributeValues;

@SuppressWarnings("hiding")
public class StatisticsCaseAttribute {

	@SuppressWarnings("rawtypes")
	private final Enum _enum;	
	StatisticsCaseAttributeEnum baseEnum;
	
	StatisticsGroupingKey[] groupingKeys;
	private IValuesGetter valuesGetter;
	
	private final boolean sortByCaption;
	private final boolean unknownValueAllowed;
	private List<StatisticsCaseSubAttribute> subAttributes = new ArrayList<StatisticsCaseSubAttribute>();
	
	
	@SuppressWarnings("rawtypes")
	public StatisticsCaseAttribute (Enum _enum, StatisticsCaseAttributeEnum baseEnum, boolean sortByCaption, boolean unknownValueAllowed, List<StatisticsCaseSubAttribute> subAttributes, StatisticsGroupingKey[] groupingKeys) {
		this._enum = _enum;
		this.baseEnum = baseEnum;
		this.groupingKeys = groupingKeys;

		this.sortByCaption = sortByCaption;
		this.unknownValueAllowed = unknownValueAllowed;
		this.subAttributes = subAttributes;
	}	
	
	@SuppressWarnings("rawtypes")
	public StatisticsCaseAttribute (Enum _enum, StatisticsCaseAttributeEnum baseEnum, boolean sortByCaption, boolean unknownValueAllowed, List<StatisticsCaseSubAttribute> subAttributes, IValuesGetter valuesGetter) {
		this._enum = _enum;
		this.baseEnum = baseEnum;
		this.valuesGetter = valuesGetter;

		this.sortByCaption = sortByCaption;
		this.unknownValueAllowed = unknownValueAllowed;
		this.subAttributes = subAttributes;
	}	

	public StatisticsCaseAttribute(StatisticsCaseAttributeEnum attribute, Object baseEnum2, boolean sortByCaption2,
			boolean unknownValueAllowed2, List<StatisticsSubAttribute> subs, StatisticsGroupingKey[] groupingKeys2) {
				this._enum = null;
				this.sortByCaption = sortByCaption2;
				this.unknownValueAllowed = unknownValueAllowed2;
	}

	public StatisticsCaseAttribute(StatisticsCaseAttributeEnum attribute, StatisticsCaseAttributeEnum enum1,
			boolean sortByCaption2, boolean unknownValueAllowed2, List<StatisticsSubAttribute> subs,
			getAttributeValues getAttributeValues) {
		this._enum = null;
		this.sortByCaption = sortByCaption2;
		this.unknownValueAllowed = unknownValueAllowed2;
	}

	public boolean isSortByCaption() {
		return sortByCaption;
	}

	public boolean isUnknownValueAllowed() {
		return unknownValueAllowed;
	}
	
	public List<StatisticsCaseSubAttribute> getSubAttributes() {
		return subAttributes;
	}
	
	public String toString() {
		return I18nProperties.getEnumCaption(_enum != null ? _enum : baseEnum);
	}
	
	public boolean isAgeGroup() {
		return baseEnum == StatisticsCaseAttributeEnum.AGE_INTERVAL_1_YEAR 
				|| baseEnum == StatisticsCaseAttributeEnum.AGE_INTERVAL_5_YEARS 
				|| baseEnum == StatisticsCaseAttributeEnum.AGE_INTERVAL_BASIC 
				|| baseEnum == StatisticsCaseAttributeEnum.AGE_INTERVAL_CHILDREN_COARSE
				|| baseEnum == StatisticsCaseAttributeEnum.AGE_INTERVAL_CHILDREN_FINE 
				|| baseEnum == StatisticsCaseAttributeEnum.AGE_INTERVAL_CHILDREN_MEDIUM;
	}
	
//	public boolean isPopulationData() {
//		return baseEnum == StatisticsCaseAttributeEnum.REGION_DISTRICT 
//				|| baseEnum == StatisticsCaseAttributeEnum.SEX 
//				|| isAgeGroup();
//	}
	
	public boolean isPopulationData() {
		return  baseEnum == StatisticsCaseAttributeEnum.JURISDICTION 
				|| baseEnum == StatisticsCaseAttributeEnum.SEX 
				|| isAgeGroup();
	}
	
	public StatisticsCaseAttributeEnum getBaseEnum () {
		return baseEnum;
	}
	
	@SuppressWarnings("rawtypes")
	public static Enum getEnum (StatisticsCaseAttribute attribute) {
		return attribute == null ? null : attribute._enum;
	}
	
	
	
	public static StatisticsCaseAttributeEnum getBaseEnum (StatisticsCaseAttribute attribute) {
		return attribute == null ? null : attribute.baseEnum;
	}
	
	public Collection<? extends StatisticsGroupingKey> getValues () {
		if (valuesGetter != null)
			return valuesGetter.get(this);
		
		return Arrays.asList(groupingKeys);
	}

	public interface IValuesGetter {
		Collection<? extends StatisticsGroupingKey> get(StatisticsCaseAttribute attribute);
	}


}
