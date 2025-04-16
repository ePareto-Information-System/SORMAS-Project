package de.symeda.sormas.ui.dashboard.ebs.components;

import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.ComboBox;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.NewCaseDateType;
import de.symeda.sormas.api.dashboard.DashboardCriteria;
import de.symeda.sormas.api.ebs.EbsIndexDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.community.CommunityReferenceDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.dashboard.components.DashboardFilterLayout;
import de.symeda.sormas.ui.dashboard.ebs.EbsDashboardView;
import de.symeda.sormas.ui.utils.components.datetypeselector.DateTypeSelectorComponent;

public class EbsFilterLayout extends DashboardFilterLayout<DashboardDataProvider> {
	public static final String DATE_TYPE_SELECTOR_FILTER = "dateTypeSelectorFilter";
	public static final String INFO_LABEL = "infoLabel";

	private final static String[] EBS_FILTERS = new String[] {
		DATE_TYPE_SELECTOR_FILTER,
		REGION_FILTER,
		DISTRICT_FILTER,
		COMMUNITY_FILTER,
		SIGNAL_CATEGORY_FILTER,
		SOURCE_INFORMATION_FILTER,
		RISK_LEVEL_FILTER
	};
	private DateTypeSelectorComponent dateTypeSelectorComponent;

	public EbsFilterLayout(EbsDashboardView dashboardView, DashboardDataProvider dashboardDataProvider) {
		super(dashboardView, dashboardDataProvider, EBS_FILTERS);
	}

	@Override
	public void populateLayout() {
		super.populateLayout();
		createDateTypeSelectorFilter();
		createRegionFilter2(null);
		createDistrictFilter2(null);
		createCommunityFilter(null);
		createSourceInformationFilter(null);
		createRiskLevelFilter(null);
		createSignalCategoryFilter(null);
	}

	public void addDateTypeValueChangeListener(Property.ValueChangeListener listener) {
		dateTypeSelectorComponent.addValueChangeListener(listener);
	}

	private void createDateTypeSelectorFilter() {
		dateTypeSelectorComponent =
			new DateTypeSelectorComponent.Builder<>(NewCaseDateType.class).dateTypePrompt(I18nProperties.getString(Strings.promptNewCaseDateType))
				.build();
		dateTypeSelectorComponent.setValue(dashboardDataProvider.getNewCaseDateType());
		addCustomComponent(dateTypeSelectorComponent, DATE_TYPE_SELECTOR_FILTER);
	}

	public void setCriteria(DashboardCriteria criteria) {
		super.setCriteria(criteria);
		dateTypeSelectorComponent.setValue(criteria.getNewCaseDateType());
	}

}
