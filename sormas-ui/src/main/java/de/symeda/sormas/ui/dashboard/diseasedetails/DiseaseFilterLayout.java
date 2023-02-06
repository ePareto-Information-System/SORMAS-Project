package de.symeda.sormas.ui.dashboard.diseasedetails;



import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.ComboBox;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.NewCaseDateType;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.dashboard.DashboardType;
import de.symeda.sormas.ui.dashboard.components.DashboardFilterLayout;
import de.symeda.sormas.ui.dashboard.contacts.ContactsDashboardView;
import de.symeda.sormas.ui.utils.ComboBoxHelper;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.components.datetypeselector.DateTypeSelectorComponent;

public class DiseaseFilterLayout extends DashboardFilterLayout {
	public static final String DATE_TYPE_SELECTOR_FILTER = "dateTypeSelectorFilter";

	public static final String INFO_LABEL = "infoLabel";
	public static final String DISEASE_FILTER = "diseaseFilter";
	private DateTypeSelectorComponent dateTypeSelectorComponent;

	private final static String[] DISEASE_FILTERS = new String[]{
			INFO_LABEL,
			REGION_FILTER, 
			DISTRICT_FILTER,
			DISEASE_FILTER,
			DATE_TYPE_SELECTOR_FILTER,
			CASE_CLASSIFICATION_FILTER 
			};
	private ComboBox diseaseFilter;

	public DiseaseFilterLayout(DiseaseDetailsView dashboardView, DashboardDataProvider dashboardDataProvider) {
		super(dashboardView, dashboardDataProvider, DISEASE_FILTERS);
	}

	@Override
	public void populateLayout() {

		super.populateLayout();
		createInfoLabel();
		createRegionAndDistrictFilter();
		createDateTypeSelectorFilter();
		createCaseClassificationFilter();
		if (dashboardDataProvider.getDashboardType() == DashboardType.DISEASE) {
			createDiseaseFilter();
		}
	}

	public boolean hasDiseaseSelected() {
		return diseaseFilter.getValue() != null;
	}

	private void createInfoLabel() {
		Label infoLabel = new Label(VaadinIcons.INFO_CIRCLE.getHtml(), ContentMode.HTML);
		infoLabel.setSizeUndefined();
		infoLabel.setDescription(I18nProperties.getString(Strings.infoDiseaseDashboard));
		CssStyles.style(infoLabel, CssStyles.LABEL_XLARGE, CssStyles.LABEL_SECONDARY);

		addCustomComponent(infoLabel, INFO_LABEL);
	}

	private void createDiseaseFilter() {
		this.diseaseFilter = ComboBoxHelper.createComboBoxV7();
		diseaseFilter.setWidth(200, Unit.PIXELS);
		diseaseFilter.setInputPrompt(I18nProperties.getString(Strings.promptDisease));
		diseaseFilter.addItems(FacadeProvider.getDiseaseConfigurationFacade().getAllDiseasesWithFollowUp(true, true, true).toArray());
		diseaseFilter.setValue(dashboardDataProvider.getDisease());
		diseaseFilter.addValueChangeListener(e -> {
			dashboardDataProvider.setDisease((Disease) diseaseFilter.getValue());
		});
		addCustomComponent(diseaseFilter, DISEASE_FILTER);
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
	
}
