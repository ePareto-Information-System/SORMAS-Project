/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.ui.dashboard;

import com.vaadin.navigator.Navigator;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.SormasUI;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.dashboard.contacts.ContactsDashboardView;
import de.symeda.sormas.ui.dashboard.diseasedetails.DiseaseDetailsView;
import de.symeda.sormas.ui.dashboard.surveillance.SurveillanceDashboardView;

public class DashboardController {

	public DashboardController() {

	}

	public void registerViews(Navigator navigator) {
		if (UserProvider.getCurrent().hasUserRight(UserRight.DASHBOARD_SURVEILLANCE_ACCESS)) {
			navigator.addView(SurveillanceDashboardView.VIEW_NAME, SurveillanceDashboardView.class);
		}
		if (UserProvider.getCurrent().hasUserRight(UserRight.DASHBOARD_CONTACT_ACCESS)) {
			navigator.addView(ContactsDashboardView.VIEW_NAME, ContactsDashboardView.class);
		}
	}

	public void navigateToDisease(Disease disease) {
		String navigationState = DiseaseDetailsView.VIEW_NAME + "/" + disease.getName();
		SormasUI.get().getNavigator().navigateTo(navigationState);
//		DiseaseDetailsView.this.setDisease(disease);
	}

	private CaseDataDto findCase(String uuid) {
		return FacadeProvider.getCaseFacade().getCaseDataByUuid(uuid);
	}

}
