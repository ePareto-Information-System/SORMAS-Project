package de.symeda.sormas.ui.dashboard.samples;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.ComboBox;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.ui.dashboard.AbstractDashboardView;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.dashboard.DashboardType;
import de.symeda.sormas.ui.dashboard.components.DashboardFilterLayout;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.components.datetypeselector.DateTypeSelectorComponent;

public class SampleFilterLayout extends DashboardFilterLayout{
	public static final String DATE_TYPE_SELECTOR_FILTER = "dateTypeSelectorFilter";

	public static final String DISEASE_FILTER = "diseaseFilter";
	public static final String INFO_LABEL_FILTER = "infoLabelFilter";
	private DateTypeSelectorComponent dateTypeSelectorComponent;

	private final static String[] SAMPLE_FILTERS = new String[] {
		DATE_TYPE_SELECTOR_FILTER,
		REGION_FILTER,
		DISTRICT_FILTER };
	private ComboBox diseaseFilter;
	

	public SampleFilterLayout(AbstractDashboardView dashboardView, DashboardDataProvider dashboardDataProvider) {
		super(dashboardView, dashboardDataProvider, SAMPLE_FILTERS);
	}

	@Override
	public void populateLayout() {
		super.populateLayout();
		createDateFilters();
		createInfoLabel();
		createRegionAndDistrictFilter();
		if (dashboardDataProvider.getDashboardType() == DashboardType.SAMPLES) {
			createDiseaseFilter();
		}
		//createResetAndApplyButtons();		
	}
	
	public boolean hasDiseaseSelected() {
		return diseaseFilter.getValue() != null;
	}
	
	public void addDateTypeValueChangeListener(Property.ValueChangeListener listener) {
		dateTypeSelectorComponent.addValueChangeListener(listener);
	}

	private void createInfoLabel() {
		Label infoLabel = new Label(VaadinIcons.INFO_CIRCLE.getHtml(), ContentMode.HTML);
		infoLabel.setSizeUndefined();
		infoLabel.setDescription(I18nProperties.getString(Strings.infoContactDashboard));
		CssStyles.style(infoLabel, CssStyles.LABEL_XLARGE, CssStyles.LABEL_SECONDARY);
		//addComponent(infoLabel);
		//setComponentAlignment(infoLabel, Alignment.TOP_RIGHT);
		addCustomComponent(infoLabel,INFO_LABEL_FILTER);
	}


	public void createDiseaseFilter() {
		this.diseaseFilter = new ComboBox();
		diseaseFilter.setWidth(200, Unit.PIXELS);
		diseaseFilter.setInputPrompt(I18nProperties.getString(Strings.promptDisease));
		diseaseFilter.addItems(FacadeProvider.getDiseaseConfigurationFacade().getAllDiseasesWithFollowUp(true, true, true).toArray());
		diseaseFilter.setValue(dashboardDataProvider.getDisease());
		diseaseFilter.addValueChangeListener(e -> {
			dashboardDataProvider.setDisease((Disease) diseaseFilter.getValue());
		});
		//addComponent(diseaseFilter);
		addCustomComponent(diseaseFilter,DISEASE_FILTER);
	}
}
