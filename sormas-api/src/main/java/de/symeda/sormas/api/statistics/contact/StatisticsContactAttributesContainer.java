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

import java.util.List;
import java.util.stream.Stream;

import de.symeda.sormas.api.statistics.StatisticsAttribute;
import de.symeda.sormas.api.statistics.StatisticsAttributeEnum;
import de.symeda.sormas.api.statistics.StatisticsAttributeGroup;
import de.symeda.sormas.api.statistics.StatisticsSubAttribute;
import de.symeda.sormas.api.statistics.StatisticsSubAttributeEnum;

import java.util.function.Function;
import java.util.function.Predicate;

public class StatisticsContactAttributesContainer {
	
	private final List<StatisticsContactAttributeGroup> groups;
	
	public StatisticsContactAttributesContainer (List<StatisticsContactAttributeGroup> groups2) {
		this.groups = groups2;
	}
	
	public List<StatisticsContactAttributeGroup> values () {
		return groups;
	}
	
	public StatisticsContactAttribute get (final StatisticsContactAttributeEnum _enum) {
		return groups.stream()
					.flatMap(new Function<StatisticsContactAttributeGroup, Stream<StatisticsContactAttribute>>() {
					    @Override
					    public Stream<StatisticsContactAttribute> apply(StatisticsContactAttributeGroup n) {
					        return n.getAttributes().stream();
					    }
					})
					.filter(new Predicate<StatisticsContactAttribute>() {
					    @Override
					    public boolean test(StatisticsContactAttribute n) {
					        return n.getBaseEnum() == _enum;
					    }
					})
					.findFirst().orElse(null);
	}
	
	public StatisticsContactSubAttribute get (final StatisticsContactSubAttributeEnum _enum) {
		return groups.stream()
					.flatMap(new Function<StatisticsContactAttributeGroup, Stream<StatisticsContactAttribute>>() {
					    @Override
					    public Stream<StatisticsContactAttribute> apply(StatisticsContactAttributeGroup n) {
					        return n.getAttributes().stream();
					    }
					})
					.flatMap(new Function<StatisticsContactAttribute, Stream<StatisticsContactSubAttribute>>() {
					    @Override
					    public Stream<StatisticsContactSubAttribute> apply(StatisticsContactAttribute n) {
					        return n.getSubAttributes().stream();
					    }
					})
					.filter(new Predicate<StatisticsContactSubAttribute>() {
					    @Override
					    public boolean test(StatisticsContactSubAttribute n) {
					        return n.getBaseEnum() == _enum;
					    }
					})
					.findFirst().orElse(null);
	}
}
