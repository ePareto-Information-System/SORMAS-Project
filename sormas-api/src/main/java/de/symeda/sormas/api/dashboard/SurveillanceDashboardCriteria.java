package de.symeda.sormas.api.dashboard;

import de.symeda.sormas.api.caze.NewCaseDateType;

public class SurveillanceDashboardCriteria extends DashboardCriteria {

	private NewCaseDateType newCaseDateType;

	protected SurveillanceDashboardCriteria(Class<DashboardCriteria> dashboardCriteriaClass) {
		super(dashboardCriteriaClass);
	}

	@Override
	public NewCaseDateType getNewCaseDateType() {
		return newCaseDateType;
	}
}
