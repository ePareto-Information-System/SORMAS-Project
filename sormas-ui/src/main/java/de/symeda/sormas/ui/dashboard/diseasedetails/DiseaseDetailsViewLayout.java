package de.symeda.sormas.ui.dashboard.diseasedetails;

import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.dashboard.map.DashboardMapComponent;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.LayoutUtil;

public class DiseaseDetailsViewLayout extends CustomLayout {

	private static final long serialVersionUID = 6582975657305031105L;
	private static final String EXTEND_BUTTONS_LOC = "extendButtons";

	private static final String CARD = "card";
	private static final String GRID_TABLE = "table";
	private static final String MAP = "map";

	private DashboardDataProvider dashboardDataProvider;

	public DiseaseDetailsViewLayout(DashboardDataProvider dashboardDataProvider) {
		this.dashboardDataProvider = dashboardDataProvider;

		setTemplateContents(
			LayoutUtil.fluidRow(LayoutUtil.fluidColumnLoc(3, 1, 6, 0, CARD), LayoutUtil.fluidColumnLoc(6, 0, 12, 0, GRID_TABLE))
				+ LayoutUtil.loc(MAP));

		reload();
	}

	public void reload() {
		//Disease Card layout
//		List<DiseaseBurdenDto> diseasesBurden = dashboardDataProvider.getDiseasesBurden();
//		DiseaseDetailsComponent diseaseCardLayout = new DiseaseDetailsComponent(diseasesBurden.get(0));
//		Label label = new Label("Outbreak..");
//		diseaseCardLayout.addComponent(label);
//		addComponent(diseaseCardLayout, CARD);

//		Grid card layout
		HorizontalLayout gridLayout = new HorizontalLayout();
		gridLayout.addComponent(gridTableLayout());
		addComponent(gridLayout, GRID_TABLE);

//		Map layout 
		HorizontalLayout mapLayout = new HorizontalLayout();
		mapLayout.addComponent(mapVerticalLayout());
		addComponent(mapLayout, MAP);
	}

	private HorizontalLayout gridTableLayout() {
		HorizontalLayout tableLayout = new HorizontalLayout();
		tableLayout.setMargin(false);
		tableLayout.setSpacing(false);

		Label labelGrid = new Label("Grid Table here");

		tableLayout.addComponent(labelGrid);
		addComponent(tableLayout);
		return tableLayout;
	}

	private HorizontalLayout mapVerticalLayout() {
		HorizontalLayout mapLayout = new HorizontalLayout();
		mapLayout.setMargin(false);
		mapLayout.setSpacing(false);
		DashboardMapComponent dashboardMapComponent = new DashboardMapComponent(dashboardDataProvider);
		dashboardMapComponent.addStyleName(CssStyles.SIDE_COMPONENT);

		mapLayout.addComponent(dashboardMapComponent);

		addComponent(mapLayout);
		return mapLayout;
	}

}
