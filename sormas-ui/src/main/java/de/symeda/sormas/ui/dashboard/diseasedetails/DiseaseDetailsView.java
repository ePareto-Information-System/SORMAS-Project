package de.symeda.sormas.ui.dashboard.diseasedetails;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.ui.dashboard.AbstractDashboardView;
import de.symeda.sormas.ui.dashboard.DashboardType;
import de.symeda.sormas.ui.utils.ViewConfiguration;

@SuppressWarnings("serial")
public class DiseaseDetailsView extends AbstractDashboardView {

	private static final long serialVersionUID = -1L;
//	public static final String VIEW_NAME = "dashboard";
	public static final String VIEW_NAME = ROOT_VIEW_NAME + "/disease";

	protected DiseaseDetailsViewLayout diseaseDetailsViewLayout;
	private DiseaseDetailsComponent diseaseDetailsComponent;
	private ViewConfiguration viewConfiguration;
	private Disease disease;

	public DiseaseDetailsView() {

		super(VIEW_NAME, DashboardType.DISEASE);

		filterLayout.setInfoLabelText(I18nProperties.getString(Strings.classificationForDisease));
		dashboardLayout.setSpacing(false);

		dashboardDataProvider.setDisease(getDiseases());

		//Added Component
		diseaseDetailsViewLayout = new DiseaseDetailsViewLayout(dashboardDataProvider);
		dashboardLayout.addComponent(diseaseDetailsViewLayout);
		dashboardLayout.setExpandRatio(diseaseDetailsViewLayout, 1);
	}

	public void refreshDiseaseData() {
		dashboardDataProvider.refreshDiseaseData();
	}

}
