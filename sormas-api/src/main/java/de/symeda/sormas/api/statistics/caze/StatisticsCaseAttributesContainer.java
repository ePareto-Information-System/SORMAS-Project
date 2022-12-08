///*******************************************************************************
// * SORMAS® - Surveillance Outbreak Response Management & Analysis System
// * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
// *
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with this program.  If not, see <https://www.gnu.org/licenses/>.
// *******************************************************************************/
//package de.symeda.sormas.api.statistics.caze;
//
//import java.util.List;
//import java.util.stream.Stream;
//
//
//
//import java.util.function.Function;
//import java.util.function.Predicate;
//
//public class StatisticsCaseAttributesContainer {
//	
//	private final List<StatisticsCaseAttributeGroup> groups;
//	
//	public StatisticsCaseAttributesContainer (List<StatisticsCaseAttributeGroup> groups) {
//		this.groups = groups;
//	}
//	
//	public List<StatisticsCaseAttributeGroup> values () {
//		return groups;
//	}
//	
//	public StatisticsCaseAttribute get (final StatisticsCaseAttributeEnum _enum) {
//		return groups.stream()
//					.flatMap(new Function<StatisticsCaseAttributeGroup, Stream<StatisticsCaseAttribute>>() {
//					    @Override
//					    public Stream<StatisticsCaseAttribute> apply(StatisticsCaseAttributeGroup n) {
//					        return n.getAttributes().stream();
//					    }
//					})
//					.filter(new Predicate<StatisticsCaseAttribute>() {
//					    @Override
//					    public boolean test(StatisticsCaseAttribute n) {
//					        return n.getBaseEnum() == _enum;
//					    }
//					})
//					.findFirst().orElse(null);
//	}
//	
//	public StatisticsCaseSubAttributeEnum get (final StatisticsCaseSubAttributeEnum _enum) {
//		return groups.stream()
//					.flatMap(new Function<StatisticsCaseAttributeGroup, Stream<StatisticsCaseSubAttributeEnum>>() {
//					    @Override
//					    public Stream<StatisticsCaseSubAttributeEnum> apply(StatisticsCaseAttributeGroup n) {
//					        return n.getAttributesEnum().stream();
//					    }
//					})
//					.flatMap(new Function<StatisticsCaseSubAttributeEnum, Stream<StatisticsCaseSubAttributeEnum>>() {
//					    @Override
//					    public Stream<StatisticsCaseSubAttributeEnum> apply(StatisticsCaseSubAttributeEnum n) {
//					        return n.getSubAttributesEnum().stream();
//					    }
//					})
//					.filter(new Predicate<StatisticsCaseSubAttributeEnum>() {
//					    @Override
//					    public boolean test(StatisticsCaseSubAttributeEnum n) {
//					        return n.getBaseEnum() == _enum;
//					    }
//					})
//					.findFirst().orElse(null);
//	}
//}
