/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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
package de.symeda.sormas.ui.ebs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.DataProviderListener;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.renderers.DateRenderer;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.ebs.EbsCriteria;
import de.symeda.sormas.api.ebs.EbsIndexDto;
import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.utils.FieldAccessHelper;
import de.symeda.sormas.ui.utils.FilteredGrid;
import de.symeda.sormas.ui.utils.ShowDetailsListener;
import de.symeda.sormas.ui.utils.UuidRenderer;

@SuppressWarnings("serial")
public class EbsSignalGrid extends FilteredGrid<de.symeda.sormas.api.ebs.EbsIndexDto, de.symeda.sormas.api.ebs.EbsCriteria> {

	public static final String EBS_DATE = Captions.singleDayEventDate;
	public static final String EBS_EVOLUTION_DATE = Captions.Event_evolutionDate;
	public static final String INFORMATION_SOURCE = Captions.Event_informationSource;
	public static final String NUMBER_OF_PENDING_TASKS = Captions.columnNumberOfPendingTasks;
	public static final String DISEASE_SHORT = Captions.columnDiseaseShort;

	private DataProviderListener<EbsIndexDto> dataProviderListener;

	@SuppressWarnings("unchecked")
	public <V extends View> EbsSignalGrid(EbsCriteria criteria, Class<V> viewClass) {

		super(EbsIndexDto.class);
		setSizeFull();

		EbsViewConfiguration viewConfiguration = ViewModelProviders.of(viewClass).get(EbsViewConfiguration.class);
		setInEagerMode(viewConfiguration.isInEagerMode());

		boolean externalSurveillanceToolShareEnabled = FacadeProvider.getExternalSurveillanceToolFacade().isFeatureEnabled();

		if (isInEagerMode() && UserProvider.getCurrent().hasUserRight(UserRight.PERFORM_BULK_OPERATIONS_EVENT)) {
			setCriteria(criteria);
			setEagerDataProvider();
		} else {
			setLazyDataProvider();
			setCriteria(criteria);
		}

		Column<EbsIndexDto, String> informationSourceColumn = addColumn(
			ebs -> ebs.getSourceInformation() == EbsSourceType.HOTLINE_PERSON
				? buildSourcePersonText(ebs)
				: ebs.getSourceInformation() == EbsSourceType.MEDIA_NEWS ? buildSourceMediaText(ebs) : "");
		informationSourceColumn.setId(INFORMATION_SOURCE);
		informationSourceColumn.setSortable(false);

		Language userLanguage = I18nProperties.getUserLanguage();

		List<String> columnIds = new ArrayList<>(Arrays.asList(EbsIndexDto.UUID));

		columnIds.addAll(
			Arrays.asList(
				EbsIndexDto.SOURCE_INFORMATION,
				EbsIndexDto.CATEGORY_OF_INFORMANT,
				EbsIndexDto.REPORT_DATE_TIME,
				EbsIndexDto.INFORMANT_NAME,
				EbsIndexDto.INFORMANT_TEL,
				EbsIndexDto.REGION,
				EbsIndexDto.DISTRICT,
				EbsIndexDto.COMMUNITY,
				EbsIndexDto.TOWN,
				EbsIndexDto.PERSON_REGISTERING,
				EbsIndexDto.PERSON_DESIGNATION));

		setColumns(columnIds.toArray(new String[columnIds.size()]));

		((Column<EbsIndexDto, String>) getColumn(EbsIndexDto.UUID)).setRenderer(new UuidRenderer());
		((Column<EbsIndexDto, Date>) getColumn(EbsIndexDto.REPORT_DATE_TIME))
			.setRenderer(new DateRenderer(DateHelper.getLocalDateTimeFormat(userLanguage)));
		((Column<EbsIndexDto, String>) getColumn(EbsIndexDto.COMMUNITY)).setCaption(I18nProperties.getCaption(Captions.Ebs_community));
		addItemClickListener(new ShowDetailsListener<>(EbsIndexDto.UUID, e -> ControllerProvider.getEbsController().navigateToData(e.getUuid())));
	}

	private String buildSourcePersonText(EbsIndexDto ebs) {
		String srcFirstName = ebs.getInformantName();
		String srcTelNo = ebs.getInformantTel();

		if (FieldAccessHelper.isAllInaccessible(srcFirstName, srcTelNo)) {
			return I18nProperties.getCaption(Captions.inaccessibleValue);
		}

		return (srcFirstName != null ? srcFirstName : "") + " " + (srcTelNo != null && !srcTelNo.isEmpty() ? " (" + srcTelNo + ")" : "");
	}

	private String buildSourceMediaText(EbsIndexDto ebs) {
		String srcMediaWebsite = String.valueOf(ebs.getSourceInformation());

		if (FieldAccessHelper.isAllInaccessible(srcMediaWebsite)) {
			return I18nProperties.getCaption(Captions.inaccessibleValue);
		}

		return (srcMediaWebsite != null ? srcMediaWebsite : "");
	}

	public void reload() {

		if (getSelectionModel().isUserSelectionAllowed()) {
			deselectAll();
		}

		EbsViewConfiguration viewConfiguration = ViewModelProviders.of(EBSView.class).get(EbsViewConfiguration.class);
		if (viewConfiguration.isInEagerMode()) {
			setEagerDataProvider();
		}

		getDataProvider().refreshAll();
	}

	public void setLazyDataProvider() {

		DataProvider<EbsIndexDto, EbsCriteria> dataProvider = DataProvider.fromFilteringCallbacks(
			query -> FacadeProvider.getEbsFacade()
				.getIndexList(
					query.getFilter().orElse(null),
					query.getOffset(),
					query.getLimit(),
					query.getSortOrders()
						.stream()
						.map(sortOrder -> new SortProperty(sortOrder.getSorted(), sortOrder.getDirection() == SortDirection.ASCENDING))
						.collect(Collectors.toList()))
				.stream(),
			query -> (int) FacadeProvider.getEbsFacade().count(query.getFilter().orElse(null)));
		setDataProvider(dataProvider);
		setSelectionMode(SelectionMode.NONE);
	}

	public void setEagerDataProvider() {
		ListDataProvider<EbsIndexDto> dataProvider =
			DataProvider.fromStream(FacadeProvider.getEbsFacade().getIndexList(getCriteria(), null, null, null).stream());
		setDataProvider(dataProvider);
		setSelectionMode(SelectionMode.MULTI);

		if (dataProviderListener != null) {
			dataProvider.addDataProviderListener(dataProviderListener);
		}
	}

	public void setDataProviderListener(DataProviderListener<EbsIndexDto> dataProviderListener) {
		this.dataProviderListener = dataProviderListener;
	}
}
