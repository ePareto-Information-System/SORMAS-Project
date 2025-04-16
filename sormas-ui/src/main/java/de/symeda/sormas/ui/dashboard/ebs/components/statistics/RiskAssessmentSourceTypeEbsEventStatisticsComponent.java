package de.symeda.sormas.ui.dashboard.ebs.components.statistics;

import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.ebs.RiskAssesment;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class RiskAssessmentSourceTypeEbsEventStatisticsComponent extends EbsEventSectionStatisticsComponent {

	private final Grid<RiskAssessmentRow> grid;
	private final VerticalLayout mainLayout;

	public RiskAssessmentSourceTypeEbsEventStatisticsComponent() {
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

		grid.addColumn(RiskAssessmentRow::getSourceType)
				.setCaption("")
				.setWidth(180);

		grid.addColumn(RiskAssessmentRow::getVeryHigh)
				.setCaption(getSafeCaption(RiskAssesment.VERY_HIGH, "Very High"))
				.setWidth(100);

		grid.addColumn(RiskAssessmentRow::getHigh)
				.setCaption(getSafeCaption(RiskAssesment.HIGH, "High"))
				.setWidth(100);

		grid.addColumn(RiskAssessmentRow::getMedium)
				.setCaption(getSafeCaption(RiskAssesment.MEDIUM, "Moderate"))
				.setWidth(120);

		grid.addColumn(RiskAssessmentRow::getLow)
				.setCaption(getSafeCaption(RiskAssesment.LOW, "Low"))
				.setWidth(80);

		mainLayout.addComponent(grid);
		addComponent(mainLayout);
	}

	private String getSafeCaption(Enum<?> value, String fallback) {
		String caption = I18nProperties.getEnumCaption(value);
		return caption != null ? caption : fallback;
	}

	public Component getComponent() {
		return mainLayout;
	}

	public void update(Map<EbsSourceType, Map<RiskAssesment, Integer>> data) {
		List<RiskAssessmentRow> rows = new ArrayList<>();

		for (EbsSourceType sourceType : EbsSourceType.values()) {
			Map<RiskAssesment, Integer> riskMap = data.getOrDefault(sourceType, Collections.emptyMap());
			RiskAssessmentRow row = new RiskAssessmentRow(
					getSafeCaption(sourceType, sourceType.name()),
					riskMap.getOrDefault(RiskAssesment.VERY_HIGH, 0),
					riskMap.getOrDefault(RiskAssesment.HIGH, 0),
					riskMap.getOrDefault(RiskAssesment.MEDIUM, 0),
					riskMap.getOrDefault(RiskAssesment.LOW, 0)
			);
			rows.add(row);
		}

		RiskAssessmentRow totalRow = new RiskAssessmentRow(
				"Total",
				countByRisk(data, RiskAssesment.VERY_HIGH),
				countByRisk(data, RiskAssesment.HIGH),
				countByRisk(data, RiskAssesment.MEDIUM),
				countByRisk(data, RiskAssesment.LOW)
		);
		rows.add(totalRow);

		grid.setItems(rows);
	}

	private int countByRisk(Map<EbsSourceType, Map<RiskAssesment, Integer>> data, RiskAssesment risk) {
		int total = 0;
		for (Map<RiskAssesment, Integer> map : data.values()) {
			total += map.getOrDefault(risk, 0);
		}
		return total;
	}

	public static class RiskAssessmentRow {
		private final String sourceType;
		private final int veryHigh;
		private final int high;
		private final int medium;
		private final int low;

		public RiskAssessmentRow(String sourceType, int veryHigh, int high, int medium, int low) {
			this.sourceType = sourceType;
			this.veryHigh = veryHigh;
			this.high = high;
			this.medium = medium;
			this.low = low;
		}

		public String getSourceType() { return sourceType; }
		public int getVeryHigh() { return veryHigh; }
		public int getHigh() { return high; }
		public int getMedium() { return medium; }
		public int getLow() { return low; }
	}
}
