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

import com.vaadin.icons.VaadinIcons;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.GeneratedPropertyContainer;
import com.vaadin.v7.data.util.converter.StringToFloatConverter;
import com.vaadin.v7.ui.Grid;
import com.vaadin.v7.ui.renderers.HtmlRenderer;
import de.symeda.sormas.api.ebs.EbsEventBurdenDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.ui.utils.CssStyles;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("serial")
public class EbsEventBurdenGrid extends Grid {

	private static final String VIEW_DETAILS_BTN_ID = "viewDetails";

	public EbsEventBurdenGrid() {

		setSizeFull();

		BeanItemContainer<EbsEventBurdenDto> container = new BeanItemContainer<EbsEventBurdenDto>(EbsEventBurdenDto.class);
		GeneratedPropertyContainer generatedContainer = new GeneratedPropertyContainer(container);
		setContainerDataSource(generatedContainer);

		setColumns(/* VIEW_DETAILS_BTN_ID, */
			EbsEventBurdenDto.EBSEVENT,
			EbsEventBurdenDto.EBSEVENT_COUNT,
			EbsEventBurdenDto.PREVIOUS_EBSEVENT_COUNT,
			EbsEventBurdenDto.EBSEVENTS_DIFFERENCE_PERCENTAGE,
			EbsEventBurdenDto.EBSEVENT_SOURCE,
			EbsEventBurdenDto.LAST_REPORTED_DISTRICT_NAME
	);

		for (Column column : getColumns()) {
			if (column.getPropertyId().equals(VIEW_DETAILS_BTN_ID)) {
				column.setHeaderCaption("");
			} else {
				column.setHeaderCaption(
					I18nProperties.getPrefixCaption(EbsEventBurdenDto.I18N_PREFIX, column.getPropertyId().toString(), column.getHeaderCaption()));
			}
		}

		getColumn(EbsEventBurdenDto.EBSEVENTS_DIFFERENCE_PERCENTAGE)
			.setHeaderCaption(I18nProperties.getPrefixCaption(EbsEventBurdenDto.I18N_PREFIX, EbsEventBurdenDto.EBSEVENTS_DIFFERENCE));

		// format columns
		//getColumn(EbsEventBurdenDto.EBSEVENT_SOURCE_RATE).setRenderer(new PercentageRenderer());

		// format casesGrowth column with chevrons
		getColumn(EbsEventBurdenDto.EBSEVENTS_DIFFERENCE_PERCENTAGE).setConverter(new StringToFloatConverter() {

			@Override
			public String convertToPresentation(Float value, Class<? extends String> targetType, Locale locale) throws ConversionException {

				String stringRepresentation = super.convertToPresentation(value, targetType, locale);
				String chevronType = "";
				String criticalLevel = "";

				if (value > 0) {
					chevronType = VaadinIcons.CHEVRON_UP.getHtml();
					criticalLevel = CssStyles.LABEL_CRITICAL;
				} else if (value < 0) {
					chevronType = VaadinIcons.CHEVRON_DOWN.getHtml();
					criticalLevel = CssStyles.LABEL_POSITIVE;
				} else {
					chevronType = VaadinIcons.CHEVRON_RIGHT.getHtml();
					criticalLevel = CssStyles.LABEL_IMPORTANT;
				}

				String strValue = "" + Math.abs(value);
				if (strValue.equals("100.0"))
					strValue = "100";

				//@formatter:off
				stringRepresentation =
					  "<div style=\"width:100%\">"
					+	"<div class=\"\" style=\"display: inline-block;margin-top: 2px;width: 70%;text-align:left;\">" + strValue + "%" + "</div>"
					+	"<div class=\"v-label v-widget " + criticalLevel + " v-label-" + criticalLevel
					+		" align-center v-label-align-center bold v-label-bold large v-label-large v-has-width\" "
					+		" style=\"width: 15px;width: 30%;text-align: left;\">"
					+		"<span class=\"v-icon\" style=\"font-family: VaadinIcons;\">" + chevronType + "</span>"
					+ 	"</div>"
					+ "</div>";
				//@formatter:on

				return stringRepresentation;
			}
		}).setRenderer(new HtmlRenderer());

		setSelectionMode(SelectionMode.NONE);
	}

	@SuppressWarnings("unchecked")
	private BeanItemContainer<EbsEventBurdenDto> getContainer() {
		GeneratedPropertyContainer container = (GeneratedPropertyContainer) super.getContainerDataSource();
		return (BeanItemContainer<EbsEventBurdenDto>) container.getWrappedContainer();
	}

	public void reload(List<EbsEventBurdenDto> items) {
		getContainer().removeAllItems();
		getContainer().addAll(items);
	}
}
