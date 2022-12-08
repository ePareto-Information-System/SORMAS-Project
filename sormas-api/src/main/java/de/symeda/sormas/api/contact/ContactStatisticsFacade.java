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
package de.symeda.sormas.api.contact;

import java.util.List;

import javax.ejb.Remote;

import de.symeda.sormas.api.statistics.caze.StatisticsCaseAttribute;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseAttributeEnum;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseCountDto;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseCriteria;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseSubAttribute;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseSubAttributeEnum;
import de.symeda.sormas.api.statistics.contact.StatisticsContactAttribute;
import de.symeda.sormas.api.statistics.contact.StatisticsContactAttributeEnum;
import de.symeda.sormas.api.statistics.contact.StatisticsContactCountDto;
import de.symeda.sormas.api.statistics.contact.StatisticsContactCriteria;
import de.symeda.sormas.api.statistics.contact.StatisticsContactSubAttribute;
import de.symeda.sormas.api.statistics.contact.StatisticsContactSubAttributeEnum;

@Remote
public interface ContactStatisticsFacade {

//	List<StatisticsCaseCountDto> queryContactCount(StatisticsContactCriteria caseCriteria, StatisticsContactAttribute groupingA, StatisticsCaseSubAttribute subGroupingA,
//			StatisticsContactAttribute groupingB, StatisticsCaseSubAttribute subGroupingB, boolean includePopulation, boolean includeZeroValues, Integer populationReferenceYear);
	
//	public List<StatisticsContactCountDto> queryContactCount(StatisticsContactCriteria caseCriteria, 
//			StatisticsContactAttribute rowGrouping, StatisticsContactSubAttribute rowSubGrouping,
//			StatisticsContactAttribute columnGrouping, StatisticsContactSubAttribute columnSubGrouping,
//			boolean includePopulation, boolean includeZeroValues, Integer populationReferenceYear) ;

	List<StatisticsContactCountDto> queryContactCount(StatisticsContactCriteria caseCriteria,
			StatisticsContactAttributeEnum rowGrouping, StatisticsContactAttributeEnum statisticsContactAttributeEnum,
			StatisticsContactAttributeEnum columnGrouping, StatisticsContactAttributeEnum statisticsContactAttributeEnum2,
			boolean includePopulation, boolean includeZeroValues, Integer populationReferenceYear);

	List<StatisticsContactCountDto> queryContactCount(StatisticsContactCriteria caseCriteria,
			StatisticsContactAttributeEnum rowGrouping, StatisticsContactSubAttributeEnum rowSubGrouping,
			StatisticsContactAttributeEnum columnGrouping, StatisticsContactSubAttributeEnum columnSubGrouping,
			boolean includePopulation, boolean includeZeroValues, Integer populationReferenceYear);

	

	

}
