package de.symeda.sormas.ui.dashboard.diseasedetails;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.dashboard.DashboardFilterLayout;
import de.symeda.sormas.ui.utils.AbstractView;

public abstract class AbstractDiseaseView extends AbstractView {

	private static final long serialVersionUID = 1L;

	public static final String ROOT_VIEW_NAME = "dashboard";

	protected DashboardDataProvider dashboardDataProvider;

	protected VerticalLayout diseaseDashboardLayout;
	protected VerticalLayout topLayout;
	protected DashboardFilterLayout filterLayout;
	protected Disease disease;

	public AbstractDiseaseView(String viewName, Disease disease) {
		super(viewName);
		addStyleName("Disease");
		this.disease = disease;
		dashboardDataProvider = new DashboardDataProvider();
		dashboardDataProvider.setDisease(disease);

		topLayout = new VerticalLayout();
		topLayout.setMargin(false);
		topLayout.setSpacing(false);

		// Dashboard layout
		diseaseDashboardLayout = new VerticalLayout();
		diseaseDashboardLayout.setMargin(false);
		diseaseDashboardLayout.setSpacing(false);
		diseaseDashboardLayout.setSizeFull();
		diseaseDashboardLayout.setStyleName("crud-main-layout");

		// Filter bar
//		filterLayout = new DashboardFilterLayout(this, dashboardDataProvider);
		diseaseDashboardLayout.addComponent(filterLayout);

		addComponent(diseaseDashboardLayout);
	}

	public void refreshDiseaseData() {
		dashboardDataProvider.refreshDiseaseData();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		refreshDiseaseData();
	}

	public Disease getDiseases(Disease disease) {
//		this.disease = disease;
//		getRefere.getUuid()
		return disease;
	}

	public String getDisseaseName() {
		return getData().toString();
	}

}
