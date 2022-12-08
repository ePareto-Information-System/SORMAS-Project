package de.symeda.sormas.api.statistics;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.symeda.sormas.api.statistics.StatisticsAttribute;
import de.symeda.sormas.api.statistics.StatisticsSubAttribute;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseAttribute;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseAttributeEnum;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseSubAttribute;
import de.symeda.sormas.api.statistics.caze.StatisticsCaseSubAttributeEnum;
import de.symeda.sormas.api.statistics.caze.StatisticsHelper;
import de.symeda.sormas.api.Month;
import de.symeda.sormas.api.MonthOfYear;
import de.symeda.sormas.api.QuarterOfYear;
import de.symeda.sormas.api.utils.EpiWeek;

public class StatisticsHelperTest {

	@Test
//	public void testBuildGroupingKey() throws Exception {
//		StatisticsHelper.buildGroupingKey(1803, StatisticsAttributeEnum.TIME, StatisticsSubAttributeEnum.EPI_WEEK_OF_YEAR, null, null);
//		StatisticsHelper.buildGroupingKey(1811, StatisticsAttributeEnum.TIME, StatisticsSubAttributeEnum.MONTH_OF_YEAR, null, null);
//		StatisticsHelper.buildGroupingKey(182, StatisticsAttributeEnum.TIME, StatisticsSubAttributeEnum.QUARTER_OF_YEAR, null, null);
//	}
	public void testBuildGroupingKey() {

		EpiWeek epiWeek = (EpiWeek) StatisticsHelper
			.buildGroupingKey(1803, StatisticsCaseAttributeEnum.REPORT_TIME, StatisticsCaseSubAttributeEnum.EPI_WEEK_OF_YEAR, null, null, null, null, null);
		assertThat(epiWeek.getYear(), equalTo(18));
		assertThat(epiWeek.getWeek(), equalTo(3));

		MonthOfYear monthOfYear = (MonthOfYear) StatisticsHelper
			.buildGroupingKey(1811, StatisticsCaseAttributeEnum.REPORT_TIME, StatisticsCaseSubAttributeEnum.MONTH_OF_YEAR, null, null, null, null, null);
		assertThat(monthOfYear.getYear().getValue(), equalTo(18));
		assertThat(monthOfYear.getMonth(), equalTo(Month.NOVEMBER));
//
		QuarterOfYear quarterOfYear = (QuarterOfYear) StatisticsHelper
			.buildGroupingKey(182, StatisticsCaseAttributeEnum.REPORT_TIME, StatisticsCaseSubAttributeEnum.QUARTER_OF_YEAR, null, null, null, null, null);
		assertThat(quarterOfYear.getYear().getValue(), equalTo(18));
		assertThat(quarterOfYear.getQuarter().getValue(), equalTo(2));
	}
}
