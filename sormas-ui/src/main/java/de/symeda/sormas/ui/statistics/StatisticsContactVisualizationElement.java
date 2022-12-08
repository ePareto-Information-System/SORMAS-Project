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

import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.statistics.StatisticsAttribute;
import de.symeda.sormas.api.statistics.StatisticsAttributeEnum;
import de.symeda.sormas.api.statistics.StatisticsAttributeGroup;
import de.symeda.sormas.api.statistics.StatisticsSubAttribute;
import de.symeda.sormas.api.statistics.contact.StatisticsContactAttributeEnum;
import de.symeda.sormas.api.statistics.contact.StatisticsContactAttributeGroupEnum;
import de.symeda.sormas.api.statistics.contact.StatisticsContactSubAttributeEnum;
import de.symeda.sormas.ui.utils.CssStyles;

@SuppressWarnings("serial")
public class StatisticsContactVisualizationElement extends HorizontalLayout {

	private MenuBar displayedAttributeDropdown;
	private MenuBar displayedSubAttributeDropdown;
	private MenuItem displayedAttributeItem;
	private MenuItem emptySelectionItem;
	private MenuItem displayedSubAttributeItem;

	private StatisticsVisualizationElementType type;
	private StatisticsVisualizationType visualizationType;
	// private StatisticsAttribute attribute;
	// private StatisticsSubAttribute subAttribute;
	
	
	// public StatisticsVisualizationElement(StatisticsAttributesContainer statisticsAttributes, StatisticsVisualizationElementType type, StatisticsVisualizationType visualizationType) {
	// 	this.statisticsAttributes = statisticsAttributes;
	private StatisticsContactAttributeEnum attribute;
	private StatisticsContactSubAttributeEnum subAttribute;



	public StatisticsContactVisualizationElement(StatisticsVisualizationElementType type, StatisticsVisualizationType visualizationType) {
		this.type = type;
		this.visualizationType = visualizationType;

		CssStyles.style(this, CssStyles.LAYOUT_MINIMAL);
		setSpacing(true);
		setWidthUndefined();

		createAndAddComponents();
	}


	private void createAndAddComponents() {
		displayedAttributeDropdown = new MenuBar();
		displayedAttributeDropdown.setId("visualizationType");
		displayedAttributeDropdown.setCaption(type.toString(visualizationType));
		displayedAttributeItem = displayedAttributeDropdown.addItem(type.getEmptySelectionString(visualizationType), null);

		displayedSubAttributeDropdown = new MenuBar();
		displayedSubAttributeDropdown.setId("displayedSubAttribute");
		CssStyles.style(displayedSubAttributeDropdown, CssStyles.FORCE_CAPTION);
		displayedSubAttributeItem = displayedSubAttributeDropdown.addItem(I18nProperties.getCaption(Captions.statisticsSpecifySelection), null);

		// Empty selections
		Command emptyItemCommand = selectedItem -> {
			attribute = null;
			subAttribute = null;
			resetSubAttributeDropdown();
			displayedAttributeItem.setText(type.getEmptySelectionString(visualizationType));
			removeSelections(displayedAttributeItem);
		};
		emptySelectionItem = displayedAttributeItem.addItem(type.getEmptySelectionString(visualizationType), emptyItemCommand);

		// Add attribute groups
		
		for (StatisticsContactAttributeGroupEnum attributeGroup : StatisticsContactAttributeGroupEnum.values()) {
			MenuItem attributeGroupItem = displayedAttributeItem.addItem(attributeGroup.toString(), null);
			attributeGroupItem.setEnabled(false);

			// Add attributes belonging to the current group
			// for (StatisticsAttribute attribute : attributeGroup.getAttributes()) {
			// 	Command attributeCommand = selectedItem -> {
			// 		resetSubAttributeDropdown();
			// 		this.attribute = attribute;
			// 		this.subAttribute = null;
			// 		displayedAttributeItem.setText(attribute.toString());
			// 		removeSelections(displayedAttributeItem);
			// 		selectedItem.setStyleName("selected-filter");
					
			// 		// Build sub attribute dropdown
			// 		if (attribute.getSubAttributes().size() > 0) {
			// 			for (StatisticsSubAttribute subAttribute : attribute.getSubAttributes()) {
			// 				if (subAttribute.isUsedForGrouping()) {
			// 					Command subAttributeCommand = selectedSubItem -> {
			// 						this.subAttribute = subAttribute;
			// 						displayedSubAttributeItem.setText(subAttribute.toString());
			// 						removeSelections(displayedSubAttributeItem);
			// 						selectedSubItem.setStyleName("selected-filter");
			// 					};
								
			// 					displayedSubAttributeItem.addItem(subAttribute.toString(), subAttributeCommand);
			for (StatisticsContactAttributeEnum attribute : attributeGroup.getAttributes()) {
				if (attribute.isUsedForVisualisation()) {
					Command attributeCommand = selectedItem -> {
						resetSubAttributeDropdown();
						this.attribute = attribute;
						this.subAttribute = null;
						displayedAttributeItem.setText(attribute.toString());
						removeSelections(displayedAttributeItem);
						selectedItem.setStyleName("selected-filter");

						// Build sub attribute dropdown
						if (attribute.getSubAttributes().length > 0) {
							for (StatisticsContactSubAttributeEnum subAttribute : attribute.getSubAttributes()) {
								if (subAttribute.isUsedForGrouping()) {
									Command subAttributeCommand = selectedSubItem -> {
										this.subAttribute = subAttribute;
										displayedSubAttributeItem.setText(subAttribute.toString());
										removeSelections(displayedSubAttributeItem);
										selectedSubItem.setStyleName("selected-filter");
									};

									displayedSubAttributeItem.addItem(subAttribute.toString(), subAttributeCommand);
								}
							}

							addComponent(displayedSubAttributeDropdown);
						}
					};

					displayedAttributeItem.addItem(attribute.toString(), attributeCommand);
				}
			}
		}

		
		addComponent(displayedAttributeDropdown);
	}

	private void removeSelections(MenuItem parentItem) {
		for (MenuItem childItem : parentItem.getChildren()) {
			childItem.setStyleName(null);
		}
	}

	private void resetSubAttributeDropdown() {
		displayedSubAttributeItem.removeChildren();
		displayedSubAttributeItem.setText(I18nProperties.getCaption(Captions.statisticsSpecifySelection));
		removeComponent(displayedSubAttributeDropdown);
	}

	public StatisticsContactAttributeEnum getAttribute() {
		return attribute;
	}

//	public StatisticsContactAttributeEnum getAttributeEnum() {
//		return attribute == null ? null : attribute.getBaseEnum();
//	}

	public StatisticsContactSubAttributeEnum getSubAttribute() {
		return subAttribute;
	}

	
//	public StatisticsContactSubAttributeEnum getSubAttributeEnum() {
//		return subAttribute == null ? null : subAttribute.getBaseEnum();
//	}

	public StatisticsVisualizationElementType getType() {
		return type;
	}

	public void setType(StatisticsVisualizationElementType type, StatisticsVisualizationType visualizationType) {
		this.type = type;
		this.visualizationType = visualizationType;
		displayedAttributeDropdown.setCaption(type.toString(visualizationType));
		emptySelectionItem.setText(type.getEmptySelectionString(visualizationType));
		if (attribute == null && subAttribute == null) {
			displayedAttributeItem.setText(type.getEmptySelectionString(visualizationType));
		}
	}

	
}
