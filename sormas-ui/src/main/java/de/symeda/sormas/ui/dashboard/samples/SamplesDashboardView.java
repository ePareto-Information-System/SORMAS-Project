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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.ui.dashboard.samples;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.symeda.sormas.api.ConfigFacade;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.NewCaseDateType;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.dashboard.AbstractDashboardView;
import de.symeda.sormas.ui.dashboard.DashboardCssStyles;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.dashboard.DashboardType;
import de.symeda.sormas.ui.dashboard.contacts.ContactsDashboardStatisticsComponent;
import de.symeda.sormas.ui.dashboard.contacts.ContactsDashboardView;
import de.symeda.sormas.ui.dashboard.contacts.ContactsEpiCurveComponent;
import de.symeda.sormas.ui.dashboard.diagram.AbstractEpiCurveComponent;
import de.symeda.sormas.ui.dashboard.map.DashboardMapComponent;
import de.symeda.sormas.ui.dashboard.statistics.AbstractDashboardStatisticsComponent;
import de.symeda.sormas.ui.dashboard.surveillance.SurveillanceDiseaseCarouselLayout;
import de.symeda.sormas.ui.dashboard.surveillance.SurveillanceOverviewLayout;
import de.symeda.sormas.ui.dashboard.visualisation.DashboardNetworkComponent;
import de.symeda.sormas.ui.utils.CssStyles;
//  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
//  * GNU General Public License for more details.
//  *
//  * You should have received a copy of the GNU General Public License
//  * along with this program. If not, see <https://www.gnu.org/licenses/>.
//  *******************************************************************************/
// package de.symeda.sormas.ui.dashboard.samples;

// import com.vaadin.navigator.ViewChangeListener;

// import de.symeda.sormas.api.FacadeProvider;
// import de.symeda.sormas.ui.dashboard.AbstractDashboardView;
// import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
// import de.symeda.sormas.ui.dashboard.DashboardType;

@SuppressWarnings("serial")
public class SamplesDashboardView extends AbstractDashboardView {

	public static final String VIEW_NAME = ROOT_VIEW_NAME + "/samples";

	protected SampleCountsTileViewLayout countsTileViewLayout;
	protected DashboardSampleFilterLayout filterLayout;
	protected DashboardDataProvider dashboardDataProvider;
	
	public SamplesDashboardView() {
		super(VIEW_NAME, DashboardType.SAMPLES);

		
	


		dashboardDataProvider = new DashboardDataProvider();
		if (dashboardDataProvider.getDashboardType() == null) {
			dashboardDataProvider.setDashboardType(DashboardType.SAMPLES);
		}
		if (DashboardType.SAMPLES.equals(dashboardDataProvider.getDashboardType())) {
			dashboardDataProvider.setDisease(FacadeProvider.getDiseaseConfigurationFacade().getDefaultDisease());
		}
		

		

		filterLayout = new DashboardSampleFilterLayout(this, dashboardDataProvider);
		dashboardLayout.addComponent(filterLayout);

		dashboardSwitcher.setValue(DashboardType.SAMPLES);
		dashboardSwitcher.addValueChangeListener(e -> {
			dashboardDataProvider.setDashboardType((DashboardType) e.getProperty().getValue());
			navigateToDashboardView(e);
		});
		


		//add samples
		countsTileViewLayout = new SampleCountsTileViewLayout(dashboardDataProvider);
		dashboardLayout.addComponent(countsTileViewLayout);
		dashboardLayout.setExpandRatio(countsTileViewLayout, 1);
		dashboardLayout.setSpacing(false);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		refreshDashboard();
	}

	public void refreshDashboard() {
		dashboardDataProvider.refreshData();

		// Update counts
		if (countsTileViewLayout != null)
			countsTileViewLayout.refresh();

	}

}
