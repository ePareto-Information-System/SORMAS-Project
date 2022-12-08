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
package de.symeda.sormas.api.caze;

import java.util.List;

import javax.ejb.Remote;

import de.symeda.sormas.api.statistics.caze.StatisticsCaseAttribute;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseAttributeEnum;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseCountDto;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseCriteria;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseSubAttribute;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseSubAttributeEnum;


@Remote
public interface CaseStatisticsFacade {

	

//	List<StatisticsCaseCountDto> queryCaseCount(
//		StatisticsCaseCriteria caseCriteria,
//		StatisticsCaseAttributeEnum groupingA,
//		StatisticsCaseSubAttributeEnum subGroupingA,
//		StatisticsCaseAttribute groupingB,
//		StatisticsCaseSubAttributeEnum subGroupingB,
//		boolean includePopulation,
//		boolean includeZeroValues,
//		Integer populationReferenceYear);

	List<StatisticsCaseCountDto> queryCaseCount(
			StatisticsCaseCriteria caseCriteria,
			StatisticsCaseAttributeEnum rowGrouping,
			StatisticsCaseSubAttributeEnum rowSubGrouping,
			StatisticsCaseAttributeEnum columnGrouping, 
			StatisticsCaseSubAttributeEnum columnSubGrouping,
			boolean includePopulation,
			boolean includeZeroValues, 
			Integer populationReferenceYear
			);

//	List<StatisticsCaseCountDto> queryCaseCount(StatisticsCaseCriteria criteria, StatisticsCaseAttributeEnum enum1,
//			StatisticsCaseAttributeEnum enum2, StatisticsCaseAttributeEnum enum3, StatisticsCaseAttributeEnum enum4,
//			boolean includePopulation, Boolean value, Integer populationReferenceYear);

//	List<StatisticsCaseCountDto> queryCaseCount(StatisticsCaseCriteria criteria, StatisticsCaseAttributeEnum enum1,
//			StatisticsCaseAttributeEnum enum2, StatisticsCaseAttributeEnum enum3, StatisticsCaseAttributeEnum enum4,
//			boolean includePopulation, Boolean value, Integer populationReferenceYear);



//	List<StatisticsCaseCountDto> queryCaseCount(StatisticsCaseCriteria caseCriteria,
//			StatisticsCaseAttribute rowGrouping, StatisticsCaseSubAttribute rowSubGrouping,
//			StatisticsCaseAttribute columnGrouping, StatisticsCaseSubAttribute columnSubGrouping,
//			boolean includePopulation, boolean includeZeroValues, Integer populationReferenceYear);

//	List<StatisticsCaseCountDto> queryCaseCount__(
//			StatisticsCaseCriteria caseCriteria,
//			StatisticsCaseAttribute rowGrouping, 
//			StatisticsCaseSubAttribute rowSubGrouping,
//			StatisticsCaseAttribute columnGrouping,
//			StatisticsCaseSubAttribute columnSubGrouping,
//			boolean includePopulation, boolean includeZeroValues, Integer populationReferenceYear);

//	List<StatisticsCaseCountDto> queryCaseCount(StatisticsCaseCriteria criteria, StatisticsCaseAttribute enum1,
//			StatisticsCaseAttribute enum2, StatisticsCaseAttribute enum3, StatisticsCaseAttribute enum4,
//			boolean includePopulation, Boolean value, Integer populationReferenceYear);
}
