/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.ui.configuration.infrastructure;

import java.util.stream.Collectors;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.data.sort.SortDirection;
import de.symeda.sormas.api.infrastructure.cadre.CadreCriteria;
import de.symeda.sormas.api.infrastructure.cadre.CadreDto;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.utils.*;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.ui.ControllerProvider;

public class CadreGrid extends FilteredGrid<CadreDto, CadreCriteria> {

	public CadreGrid(CadreCriteria criteria, CadreView cadreView) {

		super(CadreDto.class);
		setSizeFull();

		ViewConfiguration viewConfiguration = ViewModelProviders.of(AdditionalView.class).get(ViewConfiguration.class);
		setInEagerMode(viewConfiguration.isInEagerMode());

		setLazyDataProvider();
		setCriteria(criteria);
		initColumns();
		addItemClickListener(
				new ShowDetailsListener<>(CadreDto.UUID, e -> ControllerProvider.getInfrastructureController().editCadre(e.getUuid(), cadreView)));
	}


	private void initColumns() {

		setColumns(
				CadreDto.UUID,
				CadreDto.POSITION,
				CadreDto.EXTERNAL_ID);

		((Column<CadreDto, String>) getColumn(CadreDto.UUID)).setRenderer(new UuidRenderer());

		for (Column<CadreDto, ?> column : getColumns()) {
			column.setCaption(I18nProperties.getPrefixCaption(CadreDto.I18N_PREFIX, column.getId(), column.getCaption()));
		}
	}

	public void reload() {
		getDataProvider().refreshAll();
	}

	public void setLazyDataProvider() {
		DataProvider<CadreDto, CadreCriteria> dataProvider = DataProvider.fromFilteringCallbacks(
				query -> FacadeProvider.getCadreFacade()
						.getIndexList(
								query.getFilter().orElse(null),
								query.getOffset(),
								query.getLimit(),
								query.getSortOrders()
										.stream()
										.map(sortOrder -> new SortProperty(sortOrder.getSorted(), sortOrder.getDirection() == SortDirection.ASCENDING))
										.collect(Collectors.toList()))
						.stream(),
				query -> {
					return (int) FacadeProvider.getCadreFacade().count(query.getFilter().orElse(null));
				});
		setDataProvider(dataProvider);
		setSelectionMode(SelectionMode.NONE);
	}

	public void setEagerDataProvider() {
		ListDataProvider<CadreDto> dataProvider =
				DataProvider.fromStream(FacadeProvider.getCadreFacade().getIndexList(getCriteria(), null, null, null).stream());
		setDataProvider(dataProvider);
		setSelectionMode(SelectionMode.MULTI);
	}
}
