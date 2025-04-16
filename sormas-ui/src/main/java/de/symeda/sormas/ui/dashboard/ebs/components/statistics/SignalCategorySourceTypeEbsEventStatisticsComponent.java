package de.symeda.sormas.ui.dashboard.ebs.components.statistics;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.ebs.SignalCategory;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;

import java.util.*;

public class SignalCategorySourceTypeEbsEventStatisticsComponent extends EbsEventSectionStatisticsComponent {

	private final Grid<SignalCountRow> grid;
	private final VerticalLayout mainLayout;

	public SignalCategorySourceTypeEbsEventStatisticsComponent() {
		super(Captions.dashboardSignalCategorySourceType, null);

		mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		mainLayout.setWidth("100%");
		mainLayout.setStyleName("signal-main-layout");  // Add this style name

		grid = new Grid<>();
		grid.setWidth("100%");
		grid.setHeightByRows(6); // Set to any number of rows you want to show
		grid.addStyleName(ValoTheme.TABLE_COMPACT);
		grid.addStyleName(ValoTheme.TABLE_BORDERLESS);
		grid.setSelectionMode(Grid.SelectionMode.NONE);
		grid.addStyleName("signal-grid");

		// Style generator for row-level styling
		grid.setStyleGenerator(row -> {
			if ("Total".equalsIgnoreCase(row.getSourceType())) {
				return "signal-total-row";
			}
			return "signal-data-row";
		});
		// Define columns with safe fallback captions
		grid.addColumn(SignalCountRow::getSourceType)
				.setCaption("") // No header for this column
				.setWidth(180);

		grid.addColumn(SignalCountRow::getHuman)
				.setCaption(getSafeCaption(SignalCategory.HUMAN, "Human"))
				.setWidth(100);

		grid.addColumn(SignalCountRow::getAnimal)
				.setCaption(getSafeCaption(SignalCategory.ANIMAL, "Animal"))
				.setWidth(100);

		grid.addColumn(SignalCountRow::getEnvironment)
				.setCaption(getSafeCaption(SignalCategory.ENVIRONMENT, "Environment"))
				.setWidth(120);

		grid.addColumn(SignalCountRow::getPoe)
				.setCaption(getSafeCaption(SignalCategory.POE, "POE"))
				.setWidth(80);

		mainLayout.addComponent(grid);
		addComponent(mainLayout); // Ensure it's part of this component
	}

	// Helper method to ensure captions are never null
	private String getSafeCaption(Enum<?> value, String fallback) {
		String caption = I18nProperties.getEnumCaption(value);
		return caption != null ? caption : fallback;
	}

	public Component getComponent() {
		return mainLayout;
	}

	public void update(Map<EbsSourceType, Map<SignalCategory, Integer>> signalCategoryData) {
		List<SignalCountRow> rows = new ArrayList<>();

		// Populate the grid rows
		for (EbsSourceType sourceType : EbsSourceType.values()) {
			Map<SignalCategory, Integer> catMap = signalCategoryData.getOrDefault(sourceType, Collections.emptyMap());
			SignalCountRow row = new SignalCountRow(
					getSafeCaption(sourceType, sourceType.name()),
					catMap.getOrDefault(SignalCategory.HUMAN, 0),
					catMap.getOrDefault(SignalCategory.ANIMAL, 0),
					catMap.getOrDefault(SignalCategory.ENVIRONMENT, 0),
					catMap.getOrDefault(SignalCategory.POE, 0)
			);
			rows.add(row);
		}

		// Add totals row
		SignalCountRow totalRow = new SignalCountRow(
				"Total",
				countByCategory(signalCategoryData, SignalCategory.HUMAN),
				countByCategory(signalCategoryData, SignalCategory.ANIMAL),
				countByCategory(signalCategoryData, SignalCategory.ENVIRONMENT),
				countByCategory(signalCategoryData, SignalCategory.POE)
		);
		rows.add(totalRow);

		grid.setItems(rows);
	}

	private int countByCategory(Map<EbsSourceType, Map<SignalCategory, Integer>> data, SignalCategory category) {
		int total = 0;
		for (Map<SignalCategory, Integer> map : data.values()) {
			total += map.getOrDefault(category, 0);
		}
		return total;
	}

	public static class SignalCountRow {
		private final String sourceType;
		private final int human;
		private final int animal;
		private final int environment;
		private final int poe;

		public SignalCountRow(String sourceType, int human, int animal, int environment, int poe) {
			this.sourceType = sourceType;
			this.human = human;
			this.animal = animal;
			this.environment = environment;
			this.poe = poe;
		}

		public String getSourceType() { return sourceType; }
		public int getHuman() { return human; }
		public int getAnimal() { return animal; }
		public int getEnvironment() { return environment; }
		public int getPoe() { return poe; }
	}
}
