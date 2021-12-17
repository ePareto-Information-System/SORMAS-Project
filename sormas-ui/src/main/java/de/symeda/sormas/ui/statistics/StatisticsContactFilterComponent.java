package de.symeda.sormas.ui.statistics;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.statistics.StatisticsContactAttribute;
import de.symeda.sormas.api.statistics.StatisticsContactAttributeGroup;
import de.symeda.sormas.api.statistics.StatisticsSubAttribute;
import de.symeda.sormas.ui.utils.CssStyles;

@SuppressWarnings("serial")
public class StatisticsContactFilterComponent extends VerticalLayout {

	private static final String SPECIFY_YOUR_SELECTION = I18nProperties.getCaption(Captions.statisticsSpecifySelection);

	private StatisticsContactAttribute selectedAttribute;
	private StatisticsSubAttribute selectedSubAttribute;
	private StatisticsFilterElement filterElement;

	public StatisticsContactFilterComponent(int rowIndex) {
		setSpacing(true);
		setMargin(false);

		addStyleName(CssStyles.LAYOUT_MINIMAL);
		setWidth(100, Unit.PERCENTAGE);

		addComponent(createFilterAttributeElement(rowIndex));
	}

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
		for (StatisticsContactAttributeGroup attributeGroup : StatisticsContactAttributeGroup.values()) {
			MenuItem attributeGroupItem = filterAttributeItem.addItem(attributeGroup.toString(), null);
			attributeGroupItem.setEnabled(false);

			// Add attributes belonging to the current group
			for (StatisticsContactAttribute attribute : attributeGroup.getAttributes()) {
				Command attributeCommand = selectedItem -> {
					selectedAttribute = attribute;
					selectedSubAttribute = null;
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
						for (StatisticsSubAttribute subAttribute : attribute.getSubAttributes()) {
							if (subAttribute.isUsedForFilters()) {
								Command subAttributeCommand = selectedSubItem -> {
									selectedSubAttribute = subAttribute;
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

		if (selectedSubAttribute == StatisticsSubAttribute.DATE_RANGE) {
			filterElement = new StatisticsFilterDateRangeElement(rowIndex);
		} else if (selectedAttribute == StatisticsContactAttribute.JURISDICTION) {
			filterElement = new StatisticsFilterJurisdictionElement(rowIndex);
		} else if (selectedAttribute == StatisticsContactAttribute.PLACE_OF_RESIDENCE) {
			filterElement = new StatisticsFilterResidenceElement(rowIndex);
		} else if (selectedAttribute.getSubAttributes().length == 0 || selectedSubAttribute != null) {
			filterElement = new StatisticsContactFilterValuesElement(
				selectedAttribute.toString() + (selectedSubAttribute != null ? " (" + selectedSubAttribute.toString() + ")" : ""),
				selectedAttribute,
				selectedSubAttribute,
				rowIndex);
		}

		if (filterElement != null) {
			addComponent(filterElement);
		}
	}

	public StatisticsContactAttribute getSelectedAttribute() {
		return selectedAttribute;
	}

	public StatisticsSubAttribute getSelectedSubAttribute() {
		return selectedSubAttribute;
	}

	public StatisticsFilterElement getFilterElement() {
		return filterElement;
	}
}
