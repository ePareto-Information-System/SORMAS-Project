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

import static de.symeda.sormas.ui.UiUtil.permitted;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.OptionGroup;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.SormasUI;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.dashboard.campaigns.CampaignDashboardView;
import de.symeda.sormas.ui.dashboard.components.DashboardFilterLayout;
import de.symeda.sormas.ui.dashboard.contacts.ContactsDashboardView;
import de.symeda.sormas.ui.dashboard.samples.SamplesDashboardView;
import de.symeda.sormas.ui.dashboard.diseasedetails.DiseaseDetailsView;
import de.symeda.sormas.ui.dashboard.sample.SampleDashboardView;
import de.symeda.sormas.ui.dashboard.surveillance.SurveillanceDashboardView;
import de.symeda.sormas.ui.utils.AbstractView;
import de.symeda.sormas.ui.utils.CssStyles;

@SuppressWarnings("serial")
public abstract class AbstractDashboardView extends AbstractView {

	public static final String ROOT_VIEW_NAME = "dashboard";

	protected VerticalLayout dashboardLayout;
	protected DashboardFilterLayout filterLayout;
	
	protected DashboardDataProvider dashboardDataProvider;
	protected OptionGroup dashboardSwitcher = new OptionGroup();

	@SuppressWarnings("deprecation")
	protected Disease disease;

	protected AbstractDashboardView(String viewName, DashboardType dashboardType) {
		super(viewName);

//		addStyleName(DashboardCssStyles.DASHBOARD_SCREEN);

		dashboardDataProvider = new DashboardDataProvider();

		if (dashboardDataProvider.getDashboardType() == null) {
			dashboardDataProvider.setDashboardType(dashboardType);
		}
		if (DashboardType.CONTACTS.equals(dashboardDataProvider.getDashboardType())) {
			dashboardDataProvider.setDisease(FacadeProvider.getDiseaseConfigurationFacade().getDefaultDisease());
		}
		if (DashboardType.DISEASE.equals(dashboardDataProvider.getDashboardType())) {
			dashboardDataProvider.setDisease(getDiseases());
		}
		
	
		
		CssStyles.style(dashboardSwitcher, CssStyles.FORCE_CAPTION, ValoTheme.OPTIONGROUP_HORIZONTAL, CssStyles.OPTIONGROUP_HORIZONTAL_PRIMARY);
		if (permitted(FeatureType.CASE_SURVEILANCE, UserRight.DASHBOARD_SURVEILLANCE_VIEW)) {
			dashboardSwitcher.addItem(DashboardType.SURVEILLANCE);
			dashboardSwitcher.setItemCaption(DashboardType.SURVEILLANCE, I18nProperties.getEnumCaption(DashboardType.SURVEILLANCE));
		}
		if (permitted(FeatureType.CONTACT_TRACING, UserRight.DASHBOARD_CONTACT_VIEW)) {
			dashboardSwitcher.addItem(DashboardType.CONTACTS);
			dashboardSwitcher.setItemCaption(DashboardType.CONTACTS, I18nProperties.getEnumCaption(DashboardType.CONTACTS));
		}

		if (permitted(FeatureType.SAMPLES_LAB, UserRight.DASHBOARD_SAMPLES_VIEW)) {
			dashboardSwitcher.addItem(DashboardType.SAMPLES);
			dashboardSwitcher.setItemCaption(DashboardType.SAMPLES, I18nProperties.getEnumCaption(DashboardType.SAMPLES));
		}

		if (permitted(FeatureType.CAMPAIGNS, UserRight.DASHBOARD_CAMPAIGNS_VIEW)) {
			dashboardSwitcher.addItem(DashboardType.CAMPAIGNS);
			dashboardSwitcher.setItemCaption(DashboardType.CAMPAIGNS, I18nProperties.getEnumCaption(DashboardType.CAMPAIGNS));
		}

		if (permitted(FeatureType.DISEASE_DETAILS,UserRight.DASHBOARD_DISEASE_DETAILS_ACCESS)) {
		
			if(dashboardDataProvider.getDashboardType()==DashboardType.DISEASE) {
			dashboardSwitcher.addItem(DashboardType.DISEASE);
			dashboardSwitcher.setItemCaption(DashboardType.DISEASE, I18nProperties.getEnumCaption(DashboardType.DISEASE));	
			}
		}
		// if (UserProvider.getCurrent().hasUserRight(UserRight.DASHBOARD_SAMPLE_ACCESS)) {
		// 	dashboardSwitcher.addItem(DashboardType.SAMPLES);
		// 	dashboardSwitcher.setItemCaption(DashboardType.SAMPLES, I18nProperties.getEnumCaption(DashboardType.SAMPLES));
		// }
		
		// dashboardSwitcher.setValue(dashboardType);
		// dashboardSwitcher.addValueChangeListener(e -> {
		// 	dashboardDataProvider.setDashboardType((DashboardType) e.getProperty().getValue());
		// 	if (DashboardType.SURVEILLANCE.equals(e.getProperty().getValue())) {
		// 		SormasUI.get().getNavigator().navigateTo(SurveillanceDashboardView.VIEW_NAME);
		// 	} else if (DashboardType.CONTACTS.equals(e.getProperty().getValue())) {
		// 		SormasUI.get().getNavigator().navigateTo(ContactsDashboardView.VIEW_NAME);
		// 	} 
		// 	else if (DashboardType.SAMPLES.equals(e.getProperty().getValue())) {
		// 		SormasUI.get().getNavigator().navigateTo(SamplesDashboardView.VIEW_NAME);
		// 	}
		// 	else {
		// 		SormasUI.get().getNavigator().navigateTo(CampaignDashboardView.VIEW_NAME);
		// 	}
		// });
		
//		if (permitted(FeatureType.SAMPLES_LAB, UserRight.DASHBOARD_SAMPLE_ACCESS)) {
//			dashboardSwitcher.addItem(DashboardType.SAMPLES);
//			dashboardSwitcher.setItemCaption(DashboardType.SAMPLES, I18nProperties.getEnumCaption(DashboardType.SAMPLES));
//		}
		dashboardSwitcher.setValue(dashboardType);
		dashboardSwitcher.addValueChangeListener(e -> {
			dashboardDataProvider.setDashboardType((DashboardType) e.getProperty().getValue());
			if (DashboardType.SURVEILLANCE.equals(e.getProperty().getValue())) {
				SormasUI.get().getNavigator().navigateTo(SurveillanceDashboardView.VIEW_NAME);
			} else if (DashboardType.DISEASE.equals(e.getProperty().getValue())) {
				
				SormasUI.get().getNavigator().navigateTo(DiseaseDetailsView.VIEW_NAME);
			}
			
			else if (DashboardType.SAMPLES.equals(e.getProperty().getValue())) {
				
				SormasUI.get().getNavigator().navigateTo(SamplesDashboardView.VIEW_NAME);
			}else {
				SormasUI.get().getNavigator().navigateTo(ContactsDashboardView.VIEW_NAME);
			}
		});
		addHeaderComponent(dashboardSwitcher);

		// Hide the dashboard switcher if only one dashboard is accessible to the user
		if (dashboardSwitcher.size() <= 1) {
			dashboardSwitcher.setVisible(false);
		}

		// Dashboard layout
		dashboardLayout = new VerticalLayout();
		dashboardLayout.setMargin(false);
		dashboardLayout.setSpacing(false);
		dashboardLayout.setSizeFull();
		dashboardLayout.setStyleName("crud-main-layout");

		// Filter bar
//		filterLayout = new DashboardFilterLayout(this, dashboardDataProvider);
//		dashboardLayout.addComponent(filterLayout);
		
		addComponent(dashboardLayout);
		setExpandRatio(dashboardLayout, 1);
	}
	
	@SuppressWarnings("deprecation")
	protected AbstractDashboardView(String viewName) {

		super(viewName);

		addStyleName(DashboardCssStyles.DASHBOARD_SCREEN);

		CssStyles.style(dashboardSwitcher, CssStyles.FORCE_CAPTION, ValoTheme.OPTIONGROUP_HORIZONTAL, CssStyles.OPTIONGROUP_HORIZONTAL_PRIMARY);
		if (permitted(FeatureType.CASE_SURVEILANCE, UserRight.DASHBOARD_SURVEILLANCE_ACCESS)) {
			dashboardSwitcher.addItem(DashboardType.SURVEILLANCE);
			dashboardSwitcher.setItemCaption(DashboardType.SURVEILLANCE, I18nProperties.getEnumCaption(DashboardType.SURVEILLANCE));
		}
		if (permitted(FeatureType.CONTACT_TRACING, UserRight.DASHBOARD_CONTACT_ACCESS)) {
			dashboardSwitcher.addItem(DashboardType.CONTACTS);
			dashboardSwitcher.setItemCaption(DashboardType.CONTACTS, I18nProperties.getEnumCaption(DashboardType.CONTACTS));
		}
		if (permitted(FeatureType.CAMPAIGNS, UserRight.DASHBOARD_CAMPAIGNS_ACCESS)) {
			dashboardSwitcher.addItem(DashboardType.CAMPAIGNS);
			dashboardSwitcher.setItemCaption(DashboardType.CAMPAIGNS, I18nProperties.getEnumCaption(DashboardType.CAMPAIGNS));
		}

		if (permitted(FeatureType.DISEASE_DETAILS, UserRight.DASHBOARD_DISEASE_DETAILS_ACCESS)) {
			dashboardSwitcher.addItem(DashboardType.DISEASE);

			dashboardSwitcher.setItemCaption(DashboardType.DISEASE, I18nProperties.getEnumCaption(DashboardType.DISEASE));
		}
	}
	// 	addHeaderComponent(dashboardSwitcher);1.87.0

	// 	// Hide the dashboard switcher if only one dashboard is accessible to the user
	// 	if (dashboardSwitcher.size() <= 1) {
	// 		dashboardSwitcher.setVisible(false);
	// 	}

	// 	// Dashboard layout
	// 	dashboardLayout = new VerticalLayout();
	// 	dashboardLayout.setMargin(false);
	// 	dashboardLayout.setSpacing(false);
	// 	dashboardLayout.setSizeFull();
	// 	dashboardLayout.setStyleName("crud-main-layout");

	// 	addComponent(dashboardLayout);
	// 	setExpandRatio(dashboardLayout, 1);
	// }

	protected void navigateToDashboardView(Property.ValueChangeEvent e) {
		if (DashboardType.SURVEILLANCE.equals(e.getProperty().getValue())) {
			SormasUI.get().getNavigator().navigateTo(SurveillanceDashboardView.VIEW_NAME);
		} 
		else if (DashboardType.CONTACTS.equals(e.getProperty().getValue())) {
			SormasUI.get().getNavigator().navigateTo(ContactsDashboardView.VIEW_NAME);
		}
		else if (DashboardType.SAMPLES.equals(e.getProperty().getValue())) {
			SormasUI.get().getNavigator().navigateTo(SamplesDashboardView.VIEW_NAME);
		}
		else if (DashboardType.DISEASE.equals(e.getProperty().getValue())) {
		
			SormasUI.get().getNavigator().navigateTo(DiseaseDetailsView.VIEW_NAME);
		}
		else if (DashboardType.CAMPAIGNS.equals(e.getProperty().getValue()))  {
			SormasUI.get().getNavigator().navigateTo(CampaignDashboardView.VIEW_NAME);
		} else {
			SormasUI.get().getNavigator().navigateTo(SamplesDashboardView.VIEW_NAME);
		}
	}

	public void refreshDashboard() {
		dashboardDataProvider.refreshData();
	}

	public void refreshDiseaseData() {
		dashboardDataProvider.refreshDiseaseData();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (!DashboardType.DISEASE.equals(dashboardDataProvider.getDashboardType())) {
			refreshDashboard();
		}
//		else {
//			refreshDiseaseData();
//		}
			//
//		else
			
	}

	public void setDiseases(Disease disease) {
		this.disease = disease;
	}

	public Disease getDiseases() {
		return disease;
	}
	// @Override
	// public void enter(ViewChangeEvent event) {
	// 	refreshDashboard();
	// }

	//public abstract void refreshDashboard();
}
