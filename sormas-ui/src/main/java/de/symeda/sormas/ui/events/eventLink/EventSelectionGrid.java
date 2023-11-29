/*
 * ******************************************************************************
 * * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * * Copyright © 2016-2020 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * *
 * * This program is free software: you can redistribute it and/or modify
 * * it under the terms of the GNU General Public License as published by
 * * the Free Software Foundation, either version 3 of the License, or
 * * (at your option) any later version.
 * *
 * * This program is distributed in the hope that it will be useful,
 * * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * * GNU General Public License for more details.
 * *
 * * You should have received a copy of the GNU General Public License
 * * along with this program. If not, see <https://www.gnu.org/licenses/>.
 * ******************************************************************************
 */

package de.symeda.sormas.ui.events.eventLink;

import java.util.Date;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.renderers.DateRenderer;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.event.EventCriteria;
import de.symeda.sormas.api.event.EventIndexDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.ui.events.EventGrid;
import de.symeda.sormas.ui.utils.FieldAccessColumnStyleGenerator;
import de.symeda.sormas.ui.utils.FilteredGrid;
import de.symeda.sormas.ui.utils.GridHeightResizer;

@SuppressWarnings("serial")
public class EventSelectionGrid extends FilteredGrid<EventIndexDto, EventCriteria> {

	public EventSelectionGrid(EventCriteria criteria) {

		super(EventIndexDto.class);

		setLazyDataProvider(FacadeProvider.getEventFacade()::getIndexList, FacadeProvider.getEventFacade()::count);
		addDataSizeChangeListener(new GridHeightResizer());

		setCriteria(criteria);
		buildGrid();
	}

	private void buildGrid() {
		setSizeFull();
		setSelectionMode(SelectionMode.SINGLE);
		setHeightMode(HeightMode.ROW);

		Language userLanguage = I18nProperties.getUserLanguage();

		setColumns(
			EventIndexDto.EVENT_LOCATION,
			EventIndexDto.EVENT_TITLE,
			EventGrid.createEventDateColumn(this),
			EventIndexDto.EVENT_STATUS,
			EventIndexDto.REPORT_DATE_TIME);

		for (Column<EventIndexDto, ?> column : getColumns()) {
			column.setCaption(I18nProperties.getPrefixCaption(EventIndexDto.I18N_PREFIX, column.getId(), column.getCaption()));
			column.setStyleGenerator(FieldAccessColumnStyleGenerator.forSensitiveData(EventIndexDto.class, column.getId()));
		}

		getColumn(EventIndexDto.EVENT_TITLE).setMaximumWidth(300);

		((Column<EventIndexDto, Date>) getColumn(EventIndexDto.REPORT_DATE_TIME))
			.setRenderer(new DateRenderer(DateHelper.getLocalDateTimeFormat(userLanguage)));
	}

	@Override
	public void setCriteria(EventCriteria criteria) {
		getFilteredDataProvider().setFilter(criteria);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ConfigurableFilterDataProvider<EventIndexDto, Void, EventCriteria> getFilteredDataProvider() {
		return (ConfigurableFilterDataProvider<EventIndexDto, Void, EventCriteria>) super.getDataProvider();
	}
}
