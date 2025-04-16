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
package de.symeda.sormas.ui.dashboard.ebs;

import com.vaadin.navigator.ViewChangeListener;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.NewCaseDateType;
import de.symeda.sormas.ui.dashboard.AbstractDashboardView;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.dashboard.DashboardType;
import de.symeda.sormas.ui.dashboard.ebs.components.EbsFilterLayout;

@SuppressWarnings("serial")
public class EbsDashboardView extends AbstractDashboardView {

	public static final String VIEW_NAME = ROOT_VIEW_NAME + "/ebs";

	protected DashboardDataProvider dashboardDataProvider;
	protected EbsFilterLayout filterLayout;

	protected EbsOverviewLayout ebsOverviewLayout;
	protected EbsEventCarouselLayout ebsEventCarouselLayout;

	public EbsDashboardView() {
		//super(VIEW_NAME);
		super(VIEW_NAME, DashboardType.EVENTS);

		//dashboardDataProvider = new DashboardDataProvider();
		
		dashboardDataProvider = new DashboardDataProvider(NewCaseDateType.class);
		if (dashboardDataProvider.getDashboardType() == null) {
			dashboardDataProvider.setDashboardType(DashboardType.EVENTS);
		}
		if (DashboardType.CONTACTS.equals(dashboardDataProvider.getDashboardType())) {
			dashboardDataProvider.setDisease(FacadeProvider.getDiseaseConfigurationFacade().getDefaultDisease());
		}
		dashboardDataProvider.setNewCaseDateType(NewCaseDateType.MOST_RELEVANT);
		filterLayout = new EbsFilterLayout(this, dashboardDataProvider);

		filterLayout.addDateTypeValueChangeListener(e -> {
			dashboardDataProvider.setNewCaseDateType((NewCaseDateType) e.getProperty().getValue());
		});
		dashboardLayout.addComponent(filterLayout);

		dashboardSwitcher.setValue(DashboardType.EVENTS);
		dashboardSwitcher.addValueChangeListener(e -> {
			dashboardDataProvider.setDashboardType((DashboardType) e.getProperty().getValue());
			navigateToDashboardView(e);
		});

		dashboardLayout.setSpacing(false);

		//add disease burden and cases
		ebsOverviewLayout = new EbsOverviewLayout(dashboardDataProvider);
		dashboardLayout.addComponent(ebsOverviewLayout);
		filterLayout.setDateFilterChangeCallback(() -> {
			ebsOverviewLayout.updateDifferenceComponentSubHeader();
		});

		//add diseaseCarousel and map
		ebsEventCarouselLayout = new EbsEventCarouselLayout(dashboardDataProvider);
		dashboardLayout.addComponent(ebsEventCarouselLayout);
		dashboardLayout.setExpandRatio(ebsEventCarouselLayout, 1);

		ebsEventCarouselLayout.setExpandListener(expanded -> {
			if (expanded) {
				dashboardLayout.removeComponent(ebsOverviewLayout);
			} else {
				dashboardLayout.addComponent(ebsOverviewLayout, 1);
			}
		});
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		filterLayout.reload(event);
		refreshDashboard();
	}

	public void refreshDashboard() {
		dashboardDataProvider.refreshEbsData();

		// Update disease burden
		if (ebsOverviewLayout != null) {
			ebsOverviewLayout.refresh();
		}

		//Update disease carousel
		if (ebsEventCarouselLayout != null)
			ebsEventCarouselLayout.refresh();
	}
}
