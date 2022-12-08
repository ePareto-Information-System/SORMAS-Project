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

import java.util.List;
import java.util.stream.Stream;

import de.symeda.sormas.api.i18n.I18nProperties;

@SuppressWarnings("rawtypes")
public class StatisticsCaseAttributeGroup {
	
	private Enum _enum;
	
	private List<StatisticsCaseAttributeEnum>  attributes_;
	
	private List<StatisticsCaseAttribute> attributes;
	
	private List<StatisticsCaseSubAttributeEnum> attributeEnum;

	
	public StatisticsCaseAttributeGroup (Enum _enum, List<StatisticsCaseAttributeEnum> attributes) {
		this._enum = _enum;
		this.attributes_ = attributes;
	}
	
	public StatisticsCaseAttributeGroup(StatisticsCaseAttributeGroupEnum person,
			List<StatisticsCaseAttribute> attributes2) {
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return I18nProperties.getEnumCaption(_enum);
	}
	
	public List<StatisticsCaseAttributeEnum> getAttributes_ () {
		return attributes_;
	}
	
	public List<StatisticsCaseAttribute>  getAttributes() {
		return attributes;
	}
	
	public List<StatisticsCaseSubAttributeEnum> getAttributesEnum() {
		return attributeEnum;
	}
}
