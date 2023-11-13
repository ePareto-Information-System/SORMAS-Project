/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2023 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.ui.configuration.customizableenums;

import java.util.Arrays;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.customizableenum.CustomizableEnumCriteria;
import de.symeda.sormas.api.customizableenum.CustomizableEnumType;
import de.symeda.sormas.api.customizableenum.CustomizableEnumValueIndexDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.configuration.AbstractConfigurationView;
import de.symeda.sormas.ui.configuration.infrastructure.components.SearchField;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;

public class CustomizableEnumsView extends AbstractConfigurationView {

	private static final long serialVersionUID = 6496373389997511056L;

	public static final String VIEW_NAME = ROOT_VIEW_NAME + "/customizableEnums";

	// Filters
	private SearchField searchField;
	private ComboBox<CustomizableEnumType> dataTypeFilter;
	private ComboBox<Disease> diseaseFilter;
	private Button resetButton;

	private final CustomizableEnumCriteria criteria;
	private final CustomizableEnumsGrid grid;

	public CustomizableEnumsView() {

		super(VIEW_NAME);

		criteria = ViewModelProviders.of(CustomizableEnumsView.class).get(CustomizableEnumCriteria.class, new CustomizableEnumCriteria());
		grid = new CustomizableEnumsGrid(criteria);
		VerticalLayout gridLayout = new VerticalLayout();
		gridLayout.addComponent(createFilterBar());
		gridLayout.addComponent(grid);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(false);
		gridLayout.setExpandRatio(grid, 1);
		gridLayout.setSizeFull();
		gridLayout.setStyleName("crud-main-layout");

		addComponent(gridLayout);
	}

	private HorizontalLayout createFilterBar() {

		HorizontalLayout filterLayout = new HorizontalLayout();
		filterLayout.setMargin(false);
		filterLayout.setSpacing(true);

		searchField = new SearchField();
		searchField.addTextChangeListener(e -> {
			criteria.freeTextFilter(e.getText());
			grid.reload();
		});
		filterLayout.addComponent(searchField);

		dataTypeFilter = new ComboBox<>(
			I18nProperties.getPrefixCaption(CustomizableEnumValueIndexDto.I18N_PREFIX, CustomizableEnumValueIndexDto.DATA_TYPE),
			Arrays.asList(CustomizableEnumType.values()));
		dataTypeFilter.addValueChangeListener(e -> {
			criteria.dataType(e.getValue());
			grid.reload();
		});
		filterLayout.addComponent(dataTypeFilter);

		diseaseFilter = new ComboBox<>(
			I18nProperties.getPrefixCaption(CustomizableEnumValueIndexDto.I18N_PREFIX, CustomizableEnumValueIndexDto.DISEASES),
			FacadeProvider.getDiseaseConfigurationFacade().getAllDiseases(true, true, true));
		diseaseFilter.addValueChangeListener(e -> {
			criteria.disease(e.getValue());
			grid.reload();
		});
		filterLayout.addComponent(diseaseFilter);

		resetButton = ButtonHelper.createButton(Captions.actionResetFilters, event -> {
			ViewModelProviders.of(CustomizableEnumsView.class).remove(CustomizableEnumCriteria.class);
			navigateTo(null);
		}, CssStyles.FORCE_CAPTION);
		filterLayout.addComponent(resetButton);

		return filterLayout;
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {

		super.enter(event);
		String params = event.getParameters().trim();
		if (params.startsWith("?")) {
			params = params.substring(1);
			criteria.fromUrlParams(params);
		}
		updateFilterComponents();
		grid.reload();
	}

	public void updateFilterComponents() {

		applyingCriteria = true;

		searchField.setValue(criteria.getFreeTextFilter());
		dataTypeFilter.setValue(criteria.getDataType());
		diseaseFilter.setValue(criteria.getDisease());

		applyingCriteria = false;
	}
}
