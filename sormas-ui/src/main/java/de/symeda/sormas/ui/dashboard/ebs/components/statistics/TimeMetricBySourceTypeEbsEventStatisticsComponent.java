package de.symeda.sormas.ui.dashboard.ebs.components.statistics;

import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.ebs.EbsTimeMetric;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;

import java.util.*;

public class TimeMetricBySourceTypeEbsEventStatisticsComponent extends EbsEventSectionStatisticsComponent {

	private final Grid<SignalCountRow> grid;
	private final VerticalLayout mainLayout;

	public TimeMetricBySourceTypeEbsEventStatisticsComponent() {
		super(Captions.dashboardSignalCategorySourceType, null);

		mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		mainLayout.setWidth("100%");
		mainLayout.setStyleName("signal-main-layout");

		grid = new Grid<>();
		grid.setWidth("100%");
		grid.setHeightByRows(6);
		grid.addStyleName(ValoTheme.TABLE_COMPACT);
		grid.addStyleName(ValoTheme.TABLE_BORDERLESS);
		grid.setSelectionMode(Grid.SelectionMode.NONE);
		grid.addStyleName("signal-grid");

		grid.setStyleGenerator(row -> {
			if ("Total".equalsIgnoreCase(row.getSourceType())) {
				return "signal-total-row";
			}
			return "signal-data-row";
		});

		grid.addColumn(SignalCountRow::getSourceType)
				.setCaption("")
				.setWidth(180);

		grid.addColumn(SignalCountRow::getLessThan24Hours)
				.setCaption(I18nProperties.getEnumCaption(EbsTimeMetric.LESS_THAN_24_HOURS))
				.setWidth(100);

		grid.addColumn(SignalCountRow::getBetween24And48Hours)
				.setCaption(I18nProperties.getEnumCaption(EbsTimeMetric.BETWEEN_24_48_HOURS))
				.setWidth(120);

		grid.addColumn(SignalCountRow::getMoreThan48Hours)
				.setCaption(I18nProperties.getEnumCaption(EbsTimeMetric.MORE_THAN_48_HOURS))
				.setWidth(120);

		mainLayout.addComponent(grid);
		addComponent(mainLayout);
	}

	public Component getComponent() {
		return mainLayout;
	}

	public void update(Map<EbsSourceType, Map<EbsTimeMetric, Integer>> timeMetricData) {
		List<SignalCountRow> rows = new ArrayList<>();

		for (EbsSourceType sourceType : EbsSourceType.values()) {
			Map<EbsTimeMetric, Integer> metricMap = timeMetricData.getOrDefault(sourceType, Collections.emptyMap());
			SignalCountRow row = new SignalCountRow(
					getSafeCaption(sourceType, sourceType.name()),
					metricMap.getOrDefault(EbsTimeMetric.LESS_THAN_24_HOURS, 0),
					metricMap.getOrDefault(EbsTimeMetric.BETWEEN_24_48_HOURS, 0),
					metricMap.getOrDefault(EbsTimeMetric.MORE_THAN_48_HOURS, 0)
			);
			rows.add(row);
		}

		SignalCountRow totalRow = new SignalCountRow(
				"Total",
				countByTimeMetric(timeMetricData, EbsTimeMetric.LESS_THAN_24_HOURS),
				countByTimeMetric(timeMetricData, EbsTimeMetric.BETWEEN_24_48_HOURS),
				countByTimeMetric(timeMetricData, EbsTimeMetric.MORE_THAN_48_HOURS)
		);
		rows.add(totalRow);

		grid.setItems(rows);
	}

	private int countByTimeMetric(Map<EbsSourceType, Map<EbsTimeMetric, Integer>> data, EbsTimeMetric metric) {
		int total = 0;
		for (Map<EbsTimeMetric, Integer> map : data.values()) {
			total += map.getOrDefault(metric, 0);
		}
		return total;
	}

	private String getSafeCaption(Enum<?> value, String fallback) {
		String caption = I18nProperties.getEnumCaption(value);
		return caption != null ? caption : fallback;
	}

	public static class SignalCountRow {
		private final String sourceType;
		private final int lessThan24Hours;
		private final int between24And48Hours;
		private final int moreThan48Hours;

		public SignalCountRow(String sourceType, int lessThan24Hours, int between24And48Hours, int moreThan48Hours) {
			this.sourceType = sourceType;
			this.lessThan24Hours = lessThan24Hours;
			this.between24And48Hours = between24And48Hours;
			this.moreThan48Hours = moreThan48Hours;
		}

		public String getSourceType() { return sourceType; }
		public int getLessThan24Hours() { return lessThan24Hours; }
		public int getBetween24And48Hours() { return between24And48Hours; }
		public int getMoreThan48Hours() { return moreThan48Hours; }
	}
}
