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
package de.symeda.sormas.ui.dashboard.ebs;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import de.symeda.sormas.api.disease.DiseaseBurdenDto;
import de.symeda.sormas.api.ebs.EbsEventBurdenDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.highcharts.HighChart;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.DateFormatHelper;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EbsEventCountDifferenceComponent extends VerticalLayout {

	private static final long serialVersionUID = 6582975657305031105L;

	private DashboardDataProvider dashboardDataProvider;
	private HighChart chart;
	private Label subtitleLabel;

	public EbsEventCountDifferenceComponent(DashboardDataProvider dashboardDataProvider) {

		this.dashboardDataProvider = dashboardDataProvider;

		Label title = new Label(I18nProperties.getCaption(Captions.dashboardEbsEventDifference));
		CssStyles.style(title, CssStyles.H2, CssStyles.VSPACE_4, CssStyles.VSPACE_TOP_NONE);

		subtitleLabel = new Label();
		updateSubHeader();

		chart = new HighChart();
		chart.setSizeFull();

		// layout
		setWidth(100, Unit.PERCENTAGE);

		addComponent(title);
		addComponent(subtitleLabel);
		addComponent(chart);
		setExpandRatio(chart, 1);

		setMargin(new MarginInfo(true, true, false, true));
		setSpacing(false);
		setSizeFull();
	}

	public void refresh(int limitDiseasesCount) {

		// Fetch the list of EbsEvent
		List<EbsEventBurdenDto> ebsEvents = dashboardDataProvider.getEbsEventsBurden();

		// Create a stream and sort it based on the desired attribute in EbsEvent
		Stream<EbsEventBurdenDto> ebsEventsStream = ebsEvents.stream().sorted((event1, event2) -> {
			// Assuming EbsEvent has a method getCaseDifference() similar to DiseaseBurdenDto
			long caseDifference1 = event1.getEbsEventsDifference();
			long caseDifference2 = event2.getEbsEventsDifference();

			// Handle zero values by setting them to Long.MIN_VALUE
			if (caseDifference1 == 0)
				caseDifference1 = Long.MIN_VALUE;
			if (caseDifference2 == 0)
				caseDifference2 = Long.MIN_VALUE;

			// Compare in descending order
			return Long.compare(caseDifference2, caseDifference1);
		});

		// Limit the stream if a limit is specified
		if (limitDiseasesCount > 0) {
			ebsEventsStream = ebsEventsStream.limit(limitDiseasesCount);
		}

		// Collect the sorted and limited stream back into a list
		ebsEvents = ebsEventsStream.collect(Collectors.toList());

		// Refresh the chart with the sorted list
		refreshChart(ebsEvents);

		// Adjust the chart height based on the number of items in the list
		if (limitDiseasesCount > 0) {
			chart.setHeight(ebsEvents.size() * 20 + 70, Unit.PIXELS); // compact mode
		} else {
			chart.setHeight(ebsEvents.size() * 40 + 70, Unit.PIXELS);
		}
	}

	private void refreshChart(List<EbsEventBurdenDto> data) {
		int maxCasesDifference = data.stream().map(d -> Math.abs(d.getEbsEventsDifference())).max(Long::compare).orElse(5L).intValue();
		maxCasesDifference = Math.max(5, maxCasesDifference);

		StringBuilder hcjs = new StringBuilder();

		//@formatter:off
		hcjs.append(
			"var options = {" + 
				"plotOptions: {" + 
					"bar: {" + 
						"colorByPoint: true," +
						"groupPadding: 0.05" + 
					"}" + 
				"}," + 
				 
				"chart: {" + 
					"type: 'bar'," + 
				"}," + 
					
				"series: [" +
					"{" +
						"name: ''," + 
						"data: [" +
							data.stream().map((d) -> 
							"{" +
								"y: " + d.getEbsEventsDifference() + "," +
								"className: '" + CssStyles.getEbsEventColor(d.getEbsEvent()) + " " + CssStyles.BACKGROUND_DARKER + "'," +
							"},")
							.reduce((fullText, nextText) -> fullText + nextText).orElse("") + 
						"]," +
					"}" +
				"]," +
					
				"xAxis: {" +
					"categories: [" + 
						data.stream().map((d) -> "'" + I18nProperties.getEnumCaption(d.getEbsEvent()) + "'").reduce((fullText, nextText) -> fullText + ", " + nextText).orElse("") +
					"]" +
				"}," + 
					
				"yAxis: {" + 
					"title: { text: '" + I18nProperties.getCaption(Captions.dashboardDiseaseDifferenceYAxisLabel) + "' }," + 
					"allowDecimals: false," + 
					"max: " + maxCasesDifference + "," + 
					"min: " + -maxCasesDifference + "," + 
				"}," + 
					
				"tooltip: { " + 
					"headerFormat: '<b>{point.x}: </b>{point.y}<br/>'," + 
					"pointFormat: ' '" + 
				"}," + 
				
				"title: { text: '' }, " + 
				"legend: { enabled: false }," + 
				"credits: { enabled: false }," + 
				"exporting: { enabled: false }," + 
				"" + 
			"}"
		);
		//@formatter:on

		chart.setHcjs(hcjs.toString());
	}

	public void updateSubHeader() {

		subtitleLabel.setValue(
			String.format(
				I18nProperties.getCaption(Captions.dashboardComparedToPreviousPeriod),
				DateFormatHelper.buildPeriodString(dashboardDataProvider.getFromDate(), dashboardDataProvider.getToDate()),
				DateFormatHelper.buildPeriodString(dashboardDataProvider.getPreviousFromDate(), dashboardDataProvider.getPreviousToDate())));
	}
}
