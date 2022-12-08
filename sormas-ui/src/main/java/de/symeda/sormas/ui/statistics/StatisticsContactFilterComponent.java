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
package de.symeda.sormas.ui.statistics;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;

import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.statistics.StatisticsAttribute;
import de.symeda.sormas.api.statistics.StatisticsAttributeEnum;
import de.symeda.sormas.api.statistics.StatisticsAttributeGroup;
//import de.symeda.sormas.api.statistics.StatisticsAttributesContainer;
import de.symeda.sormas.api.statistics.StatisticsSubAttribute;
//import de.symeda.sormas.api.statistics.caze.StatisticsContactAttributesContainer;
import de.symeda.sormas.api.statistics.contact.StatisticsContactAttributeEnum;
import de.symeda.sormas.api.statistics.contact.StatisticsContactAttributeGroupEnum;
import de.symeda.sormas.api.statistics.contact.StatisticsContactSubAttributeEnum;
import de.symeda.sormas.ui.utils.CssStyles;

@SuppressWarnings("serial")
public class StatisticsContactFilterComponent extends VerticalLayout {

	private static final String SPECIFY_YOUR_SELECTION = I18nProperties.getCaption(Captions.statisticsSpecifySelection);

	private StatisticsContactAttributeEnum selectedAttribute;
	private StatisticsContactSubAttributeEnum satisticsContactSubAttribute;
	
	private StatisticsContactAttributeEnum satisticsContactAttribute;


	private StatisticsFilterElement filterElement;
	
	//private final StatisticsContactAttributesContainer statisticsAttributes;

	// public StatisticsFilterComponent(StatisticsAttributesContainer statisticsAttributes) {
	// 	this.statisticsAttributes = statisticsAttributes;
		
	public StatisticsContactFilterComponent(int rowIndex) {
		//this.statisticsAttributes = null;
		setSpacing(true);
		setMargin(false);

		addStyleName(CssStyles.LAYOUT_MINIMAL);
		setWidth(100, Unit.PERCENTAGE);

		addComponent(createFilterAttributeElement(rowIndex));
	}

//	public StatisticsFilterComponent(StatisticsContactAttributesContainer statisticsAttributes2) {
//		this.statisticsAttributes = statisticsAttributes2;
//		// TODO Auto-generated constructor stub
//	}

	private HorizontalLayout createFilterAttributeElement(int rowIndex) {
		HorizontalLayout filterAttributeLayout = new HorizontalLayout();
		filterAttributeLayout.setSpacing(true);
		filterAttributeLayout.setWidth(100, Unit.PERCENTAGE);

		MenuBar filterAttributeDropdown = new MenuBar();
		filterAttributeDropdown.setId(Captions.statisticsAttribute + "-" + rowIndex);
		filterAttributeDropdown.setCaption(I18nProperties.getCaption(Captions.statisticsAttribute));
		MenuItem filterAttributeItem = filterAttributeDropdown.addItem(I18nProperties.getCaption(Captions.statisticsAttributeSelect), null);
		MenuBar filterSubAttributeDropdown = new MenuBar();
		filterSubAttributeDropdown.setId(Captions.statisticsAttributeSpecification + "-" + rowIndex);
		filterSubAttributeDropdown.setCaption(I18nProperties.getCaption(Captions.statisticsAttributeSpecification));
		MenuItem filterSubAttributeItem = filterSubAttributeDropdown.addItem(SPECIFY_YOUR_SELECTION, null);

		// Add attribute groups
		for (StatisticsContactAttributeGroupEnum attributeGroup : StatisticsContactAttributeGroupEnum.values()) {
			MenuItem attributeGroupItem = filterAttributeItem.addItem(attributeGroup.toString(), null);
			attributeGroupItem.setEnabled(false);

			// Add attributes belonging to the current group
			for (StatisticsContactAttributeEnum attribute : attributeGroup.getAttributes()) {
				Command attributeCommand = selectedItem -> {
					selectedAttribute = attribute;
					satisticsContactSubAttribute = null;
					filterAttributeItem.setText(attribute.toString());

					// Add style to keep chosen item selected and remove it from all other items
					for (MenuItem menuItem : filterAttributeItem.getChildren()) {
						menuItem.setStyleName(null);
					}
					selectedItem.setStyleName("selected-filter");

					// Reset the sub attribute dropdown
					filterSubAttributeItem.removeChildren();
					filterSubAttributeItem.setText(SPECIFY_YOUR_SELECTION);

					if (attribute.getSubAttributes().length > 0) {
						for (StatisticsContactSubAttributeEnum subAttribute : attribute.getSubAttributes()) {
							if (subAttribute.isUsedForFilters()) {
								Command subAttributeCommand = selectedSubItem -> {
									satisticsContactSubAttribute = subAttribute;
									filterSubAttributeItem.setText(subAttribute.toString());

									// Add style to keep chosen item selected and remove it from all other items
									for (MenuItem menuItem : filterSubAttributeItem.getChildren()) {
										menuItem.setStyleName(null);
									}
									selectedSubItem.setStyleName("selected-filter");

									updateFilterElement(rowIndex);
								};

								filterSubAttributeItem.addItem(subAttribute.toString(), subAttributeCommand);
							}
						}

						// Only add the sub attribute dropdown if there are any sub attributes that are relevant for the filters section
						if (filterSubAttributeItem.getChildren() != null && filterSubAttributeItem.getChildren().size() > 0) {
							filterAttributeLayout.addComponent(filterSubAttributeDropdown);
							filterAttributeLayout.setExpandRatio(filterSubAttributeDropdown, 1);
						} else {
							filterAttributeLayout.removeComponent(filterSubAttributeDropdown);
						}
					} else {
						filterAttributeLayout.removeComponent(filterSubAttributeDropdown);
					}
					updateFilterElement(rowIndex);
				};

				filterAttributeItem.addItem(attribute.toString(), attributeCommand);
			}
		}
		
		

		filterAttributeLayout.addComponent(filterAttributeDropdown);
		filterAttributeLayout.setExpandRatio(filterAttributeDropdown, 0);
		return filterAttributeLayout;
	}

	private void updateFilterElement(int rowIndex) {
		if (filterElement != null) {
			removeComponent(filterElement);
			filterElement = null;
		}
		
		// if (StatisticsSubAttribute.getBaseEnum(satisticsContactSubAttribute) == StatisticsSubAttributeEnum.DATE_RANGE) {
		// 	filterElement = new StatisticsFilterDateRangeElement();
		// } else if (StatisticsAttribute.getBaseEnum(selectedAttribute) == StatisticsAttributeEnum.REGION_DISTRICT) {
		// 	filterElement = new StatisticsFilterRegionDistrictElement(statisticsAttributes);
		// } else if (selectedAttribute.getSubAttributes().size() == 0 
		// 		|| satisticsContactSubAttribute != null) {

		if (satisticsContactSubAttribute == StatisticsContactSubAttributeEnum.DATE_RANGE) {
			filterElement = new StatisticsFilterDateRangeElement(rowIndex);
		} else if (selectedAttribute == StatisticsContactAttributeEnum.JURISDICTION) {
			filterElement = new StatisticsFilterJurisdictionElement(rowIndex);
		} else if (selectedAttribute == StatisticsContactAttributeEnum.PLACE_OF_RESIDENCE) {
			filterElement = new StatisticsFilterResidenceElement(rowIndex);
		} else if (selectedAttribute.getSubAttributes().length == 0 || satisticsContactSubAttribute != null) {
			filterElement = new StatisticsContactsFilterValuesElement(
				selectedAttribute.toString() + (satisticsContactSubAttribute != null ? " (" + satisticsContactSubAttribute.toString() + ")" : ""),
				selectedAttribute,
				satisticsContactSubAttribute,
				rowIndex);
		}

		if (filterElement != null) {
			addComponent(filterElement);
		}
	}

	public StatisticsContactAttributeEnum getSelectedAttribute() {
		return selectedAttribute;
	}

	public StatisticsContactSubAttributeEnum getSelectedSubAttribute() {
		return satisticsContactSubAttribute;
	}
	
	public StatisticsContactSubAttributeEnum getSelectedContactSubAttribute() {
		return satisticsContactSubAttribute;
	}
	
	public StatisticsContactAttributeEnum getSelectedContactAttribute() {
		return satisticsContactAttribute;
	}



	public StatisticsFilterElement getFilterElement() {
		return filterElement;
	}


}
