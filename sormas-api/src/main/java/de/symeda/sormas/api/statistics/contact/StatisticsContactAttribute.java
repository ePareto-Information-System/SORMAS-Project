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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.statistics.StatisticsGroupingKey;

@SuppressWarnings("hiding")
public class StatisticsContactAttribute {

	@SuppressWarnings("rawtypes")
	private final Enum _enum;	
	StatisticsContactAttributeEnum baseEnum;
	StatisticsGroupingKey[] groupingKeys;
	private IValuesGetter valuesGetter;
	
	private final boolean sortByCaption;
	private final boolean unknownValueAllowed;
	private List<StatisticsContactSubAttribute> subAttributes = new ArrayList<StatisticsContactSubAttribute>();
	
	
	@SuppressWarnings("rawtypes")
	public StatisticsContactAttribute (Enum _enum, StatisticsContactAttributeEnum baseEnum, boolean sortByCaption, boolean unknownValueAllowed, List<StatisticsContactSubAttribute> subAttributes, StatisticsGroupingKey[] groupingKeys) {
		this._enum = _enum;
		this.baseEnum = baseEnum;
		this.groupingKeys = groupingKeys;

		this.sortByCaption = sortByCaption;
		this.unknownValueAllowed = unknownValueAllowed;
		this.subAttributes = subAttributes;
	}	
	
	@SuppressWarnings("rawtypes")
	public StatisticsContactAttribute (Enum _enum, StatisticsContactAttributeEnum baseEnum, boolean sortByCaption, boolean unknownValueAllowed, List<StatisticsContactSubAttribute> subAttributes, IValuesGetter valuesGetter) {
		this._enum = _enum;
		this.baseEnum = baseEnum;
		this.valuesGetter = valuesGetter;

		this.sortByCaption = sortByCaption;
		this.unknownValueAllowed = unknownValueAllowed;
		this.subAttributes = subAttributes;
	}	

	public StatisticsContactAttribute(StatisticsContactAttribute attribute, Object baseEnum2, boolean sortByCaption2,
			boolean unknownValueAllowed2, List<StatisticsContactSubAttribute> subs,
			StatisticsGroupingKey[] groupingKeys2) {
				this._enum = null;
				this.sortByCaption =sortByCaption2 ;
				this.unknownValueAllowed = unknownValueAllowed2;
	}

	public boolean isSortByCaption() {
		return sortByCaption;
	}

	public boolean isUnknownValueAllowed() {
		return unknownValueAllowed;
	}
	
	public List<StatisticsContactSubAttribute> getSubAttributes() {
		return subAttributes;
	}
	
	public String toString() {
		return I18nProperties.getEnumCaption(_enum != null ? _enum : baseEnum);
	}
	
	public boolean isAgeGroup() {
		return baseEnum == StatisticsContactAttributeEnum.AGE_INTERVAL_1_YEAR 
				|| baseEnum == StatisticsContactAttributeEnum.AGE_INTERVAL_5_YEARS 
				|| baseEnum == StatisticsContactAttributeEnum.AGE_INTERVAL_BASIC 
				|| baseEnum == StatisticsContactAttributeEnum.AGE_INTERVAL_CHILDREN_COARSE
				|| baseEnum == StatisticsContactAttributeEnum.AGE_INTERVAL_CHILDREN_FINE 
				|| baseEnum == StatisticsContactAttributeEnum.AGE_INTERVAL_CHILDREN_MEDIUM;
	}
	
	public boolean isPopulationData() {
		return baseEnum == StatisticsContactAttributeEnum.JURISDICTION 
				|| baseEnum == StatisticsContactAttributeEnum.SEX 
				|| isAgeGroup();
	}
	
	public StatisticsContactAttributeEnum getBaseEnum () {
		return baseEnum;
	}
	
	@SuppressWarnings("rawtypes")
	public static Enum getEnum (StatisticsContactAttribute attribute) {
		return attribute == null ? null : attribute._enum;
	}
	
	
	
	public static StatisticsContactAttributeEnum getBaseEnum (StatisticsContactAttribute attribute) {
		return attribute == null ? null : attribute.baseEnum;
	}
	
	public Collection<? extends StatisticsGroupingKey> getValues () {
		if (valuesGetter != null)
			return valuesGetter.get(this);
		
		return Arrays.asList(groupingKeys);
	}

	public interface IValuesGetter {
		Collection<? extends StatisticsGroupingKey> get(StatisticsContactAttribute attribute);
	}
}
