package de.symeda.sormas.ui.statistics;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.statistics.StatisticsContactAttribute;
import de.symeda.sormas.api.statistics.StatisticsContactAttributeGroup;
import de.symeda.sormas.api.statistics.StatisticsSubAttribute;
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
	private StatisticsContactAttribute attribute;
	private StatisticsSubAttribute subAttribute;
	
	//contact
	private StatisticsContactAttribute contactAttribute;
	private StatisticsSubAttribute contactSubAttribute;
	
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
		for (StatisticsContactAttributeGroup attributeGroup : StatisticsContactAttributeGroup.values()) {
			MenuItem attributeGroupItem = displayedAttributeItem.addItem(attributeGroup.toString(), null);
			attributeGroupItem.setEnabled(false);

			// Add attributes belonging to the current group
			for (StatisticsContactAttribute attribute : attributeGroup.getAttributes()) {
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
							for (StatisticsSubAttribute subAttribute : attribute.getSubAttributes()) {
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

	public StatisticsContactAttribute getAttribute() {
		return attribute;
	}

	public StatisticsSubAttribute getSubAttribute() {
		return subAttribute;
	}

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
	
	public StatisticsContactAttribute getContactAttribute() {
		return attribute;
	}
	
	public StatisticsSubAttribute getContactSubAttribute() {
		return subAttribute;
	}
}
