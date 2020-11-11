package de.symeda.sormas.ui.dashboard.diseasedetails;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.utils.AbstractView;

public abstract class AbstractDiseaseView extends AbstractView {

	private static final long serialVersionUID = 1L;

	public static final String ROOT_VIEW_NAME = "dashboard";

	protected DashboardDataProvider dashboardDataProvider;

	protected VerticalLayout diseaseDashboardLayout;
	protected VerticalLayout topLayout;
//	protected DashboardFilterLayout filterLayout;

	public AbstractDiseaseView(String viewName) {
		super(viewName);
		addStyleName("Disease");
		dashboardDataProvider = new DashboardDataProvider();
//		dashboardDataProvider.setDisease(disease);
//		dashboardDataProvider.setDisease(FacadeProvider.getDiseaseConfigurationFacade().getDefaultDisease());
		dashboardDataProvider.setDisease(Disease.ANTHRAX);
		topLayout = new VerticalLayout();
		topLayout.setMargin(false);
		topLayout.setSpacing(false);

		// Dashboard layout
		diseaseDashboardLayout = new VerticalLayout();
		diseaseDashboardLayout.setMargin(false);
		diseaseDashboardLayout.setSpacing(false);
		diseaseDashboardLayout.setSizeFull();
		diseaseDashboardLayout.setStyleName("crud-main-layout");

		diseaseDashboardLayout.addComponent(topLayout);
		addComponent(diseaseDashboardLayout);
	}

	public void refreshDashboard() {
		dashboardDataProvider.refreshDiseaseData();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		refreshDashboard();
	}

}
