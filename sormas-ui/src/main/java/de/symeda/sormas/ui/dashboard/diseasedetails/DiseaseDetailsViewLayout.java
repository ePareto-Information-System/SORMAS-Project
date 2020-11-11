package de.symeda.sormas.ui.dashboard.diseasedetails;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.utils.CssStyles;

public class DiseaseDetailsViewLayout extends CssLayout {

	private static final long serialVersionUID = 6582975657305031105L;

	private DashboardDataProvider dashboardDataProvider;

	public DiseaseDetailsViewLayout(DashboardDataProvider dashboardDataProvider) {
		this.dashboardDataProvider = dashboardDataProvider;
	}

	public DiseaseDetailsViewLayout() {

	}

	@Override
	protected String getCss(Component c) {
		c.setId("sample-card-wrapper");
		return "display: flex !important; flex-wrap: wrap !important; flex-direction: column; white-space: normal !important";
	}

//	private VerticalLayout addDiseaseCard(
//		Disease disease,
//		boolean outbreak,
//		Long countDisease,
//		Long deaths,
//		double percentage,
//		String reportedDistrict,
//		String outBreakDistrict) {
	private VerticalLayout addDiseaseCard(
		Disease disease,
		boolean outbreak,
		double d,
		double e,
		double percentage,
		String reportedDistrict,
		String outBreakDistrict) {
		VerticalLayout verticalLayout = new VerticalLayout();

//		Label title = new Label(I18nProperties.getCaption(disease.getName()));
		Label title = new Label("Some disease name here");
		CssStyles.style(title, CssStyles.H2, CssStyles.VSPACE_4, CssStyles.VSPACE_TOP_NONE);

		verticalLayout.addComponent(title);
		return verticalLayout;
	}

	public void refresh() {
//		addDiseaseCard(disease, outbreak, countDisease, deaths, percentage, reportedDistrict, outBreakDistrict)

		HorizontalLayout conditionHorizontalLayout = new HorizontalLayout();
//		conditionHorizontalLayout.addComponent(addDiseaseCard(Disease.AFP, true, 0, 0, 10.2, "Mi5", "007"));
		conditionHorizontalLayout.addComponent(addDiseaseCard(Disease.AFP, true, 9912240.0, 545124.0, 10.2, "Mi5", "007"));
		addComponent(conditionHorizontalLayout);

	}

}
