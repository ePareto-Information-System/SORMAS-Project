package de.symeda.sormas.ui.dashboard.ebs.components.statistics;

import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.symeda.sormas.api.ebs.EbsTimeMetric;
import de.symeda.sormas.api.ebs.RiskAssesment;
import de.symeda.sormas.api.i18n.I18nProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class RiskAssessmentTimeMetricEventStatisticsComponent extends EbsEventSectionStatisticsComponent {

	private final Grid<TimeMetricRiskRow> grid;
	private final VerticalLayout mainLayout;

	public RiskAssessmentTimeMetricEventStatisticsComponent() {
		super("Risk Assessment Status", null);

		mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		mainLayout.setWidth("100%");
		mainLayout.setStyleName("risk-assessment-layout");

		grid = new Grid<>();
		grid.setWidth("100%");
		grid.setHeightByRows(4); // Header + 3 time buckets
		grid.addStyleName(ValoTheme.TABLE_COMPACT);
		grid.addStyleName(ValoTheme.TABLE_BORDERLESS);
		grid.setSelectionMode(Grid.SelectionMode.NONE);
		grid.addStyleName("risk-grid");

		// Define columns
		grid.addColumn(row -> I18nProperties.getEnumCaption(row.getTimeMetric()))
				.setCaption("")
				.setWidth(120);

		grid.addColumn(TimeMetricRiskRow::getVeryHigh)
				.setCaption(I18nProperties.getEnumCaption(RiskAssesment.VERY_HIGH))
				.setWidth(100)
				.setStyleGenerator(row -> "risk-very-high");

		grid.addColumn(TimeMetricRiskRow::getHigh)
				.setCaption(I18nProperties.getEnumCaption(RiskAssesment.HIGH))
				.setWidth(100)
				.setStyleGenerator(row -> "risk-high");

		grid.addColumn(TimeMetricRiskRow::getModerate)
				.setCaption(I18nProperties.getEnumCaption(RiskAssesment.MEDIUM))
				.setWidth(100)
				.setStyleGenerator(row -> "risk-moderate");

		grid.addColumn(TimeMetricRiskRow::getLow)
				.setCaption(I18nProperties.getEnumCaption(RiskAssesment.LOW))
				.setWidth(100)
				.setStyleGenerator(row -> "risk-low");

		mainLayout.addComponent(grid);
		addComponent(mainLayout);
	}

	public Component getComponent() {
		return mainLayout;
	}

	public void update(Map<EbsTimeMetric, Map<RiskAssesment, Integer>> data) {
		List<TimeMetricRiskRow> rows = new ArrayList<>();

		for (EbsTimeMetric timeMetric : EbsTimeMetric.values()) {
			Map<RiskAssesment, Integer> riskCounts = data.getOrDefault(timeMetric, Collections.emptyMap());

			TimeMetricRiskRow row = new TimeMetricRiskRow(
					timeMetric,
					riskCounts.getOrDefault(RiskAssesment.VERY_HIGH, 0),
					riskCounts.getOrDefault(RiskAssesment.HIGH, 0),
					riskCounts.getOrDefault(RiskAssesment.MEDIUM, 0),
					riskCounts.getOrDefault(RiskAssesment.LOW, 0)
			);
			rows.add(row);
		}

		grid.setItems(rows);
	}

	public static class TimeMetricRiskRow {
		private final EbsTimeMetric timeMetric;
		private final int veryHigh;
		private final int high;
		private final int moderate;
		private final int low;

		public TimeMetricRiskRow(EbsTimeMetric timeMetric, int veryHigh, int high, int moderate, int low) {
			this.timeMetric = timeMetric;
			this.veryHigh = veryHigh;
			this.high = high;
			this.moderate = moderate;
			this.low = low;
		}

		public EbsTimeMetric getTimeMetric() { return timeMetric; }
		public int getVeryHigh() { return veryHigh; }
		public int getHigh() { return high; }
		public int getModerate() { return moderate; }
		public int getLow() { return low; }
	}
}
