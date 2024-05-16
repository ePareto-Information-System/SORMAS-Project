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

import com.vaadin.data.provider.DataProviderListener;
import com.vaadin.navigator.View;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import de.symeda.sormas.api.DiseaseHelper;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.customizableenum.CustomizableEnumType;
import de.symeda.sormas.api.ebs.*;
import de.symeda.sormas.api.event.*;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.events.groups.EventGroupsValueProvider;
import de.symeda.sormas.ui.utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class EbsGrid extends FilteredGrid<de.symeda.sormas.api.ebs.EbsIndexDto, de.symeda.sormas.api.ebs.EbsCriteria> {

	public static final String EBS_DATE = Captions.singleDayEventDate;
	public static final String EBS_EVOLUTION_DATE = Captions.Event_evolutionDate;
	public static final String INFORMATION_SOURCE = Captions.Event_informationSource;
	public static final String NUMBER_OF_PENDING_TASKS = Captions.columnNumberOfPendingTasks;
	public static final String DISEASE_SHORT = Captions.columnDiseaseShort;

	private DataProviderListener<EbsIndexDto> dataProviderListener;

	@SuppressWarnings("unchecked")
	public <V extends View> EbsGrid(EbsCriteria criteria, Class<V> viewClass) {

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
			ebs -> ebs.getSrcType() == EbsSourceType.HOTLINE_PERSON
				? buildSourcePersonText(ebs)
				: ebs.getSrcType() == EbsSourceType.MEDIA_NEWS ? buildSourceMediaText(ebs) : "");
		informationSourceColumn.setId(INFORMATION_SOURCE);
		informationSourceColumn.setSortable(false);

		Language userLanguage = I18nProperties.getUserLanguage();

		boolean showPendingTasks = FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.TASK_MANAGEMENT)
			&& UserProvider.getCurrent().hasUserRight(UserRight.TASK_VIEW);
		if (showPendingTasks) {
			Column<EbsIndexDto, String> pendingTasksColumn = addColumn(
				entry -> String.format(
					I18nProperties.getCaption(Captions.formatSimpleNumberFormat),
					FacadeProvider.getTaskFacade().getPendingTaskCountByEbs(entry.toReference())));
			pendingTasksColumn.setId(NUMBER_OF_PENDING_TASKS);
			pendingTasksColumn.setSortable(false);
		}

		boolean specificRiskEnabled = FacadeProvider.getCustomizableEnumFacade().hasEnumValues(CustomizableEnumType.SPECIFIC_EVENT_RISK, null);

		List<String> columnIds = new ArrayList<>(
			Arrays.asList(
				EbsIndexDto.UUID));

		columnIds.addAll(
			Arrays.asList(
				EbsIndexDto.SRC_TYPE,
				EbsIndexDto.PERSON_REPORTING,
				EbsIndexDto.REPORT_DATE_TIME,
				EbsIndexDto.CONTACT_NAME,
				EbsIndexDto.CONTACT_PHONE_NUMBER,
				EbsIndexDto.TRIAGING_DECISION,
				EbsIndexDto.TRIAGE_DATE,
				EbsIndexDto.SIGNAL_CATEGORY,
				EbsIndexDto.VERIFIED,
				EbsIndexDto.DEATH,
					EbsIndexDto.TRIAGING_DECISION_DATE));


		setColumns(columnIds.toArray(new String[columnIds.size()]));


		((Column<EbsIndexDto, String>) getColumn(EbsIndexDto.UUID)).setRenderer(new UuidRenderer());
		((Column<EbsIndexDto, Date>) getColumn(EbsIndexDto.REPORT_DATE_TIME))
			.setRenderer(new DateRenderer(DateHelper.getLocalDateTimeFormat(userLanguage)));
		addItemClickListener(new ShowDetailsListener<>(EbsIndexDto.UUID, e -> ControllerProvider.getEbsController().navigateToData(e.getUuid())));
	}

//		((Column<EbsIndexDto, TriagingDto>)) ebsTriagingColumn =
//				(Column<EbsIndexDto, TriagingDto>) getColumn(EbsIndexDto.TRIAGING);
//		ebsTriagingColumn.setSortable(false);
//		ebsTriagingColumn.setRenderer(new EventGroupsValueProvider(), new HtmlRenderer());

//	public static String createEbsDateColumn(FilteredGrid<EbsIndexDto, EbsCriteria> grid) {
//		Column<EbsIndexDto, String> ebsDateColumn =
//			grid.addColumn(ebs -> EbsHelper.buildEbsDateString(ebs.getTriageDate(), ebs.getEndDate()));
//		ebsDateColumn.setId(EBS_DATE);
//		ebsDateColumn.setSortProperty(EbsDto.TRIAGE_DATE);
//		ebsDateColumn.setSortable(true);
//
//		return EBS_DATE;
//	}

	private String buildSourcePersonText(EbsIndexDto ebs) {
		String srcFirstName = ebs.getContactName();
		String srcTelNo = ebs.getContactPhoneNumber();

		if (FieldAccessHelper.isAllInaccessible(srcFirstName, srcTelNo)) {
			return I18nProperties.getCaption(Captions.inaccessibleValue);
		}

		return (srcFirstName != null ? srcFirstName : "") + " "
			+ (srcTelNo != null && !srcTelNo.isEmpty() ? " (" + srcTelNo + ")" : "");
	}

	private String buildSourceMediaText(EbsIndexDto ebs) {
		String srcMediaWebsite = String.valueOf(ebs.getSrcType());

		if (FieldAccessHelper.isAllInaccessible(srcMediaWebsite)) {
			return I18nProperties.getCaption(Captions.inaccessibleValue);
		}

		return (srcMediaWebsite != null ? srcMediaWebsite : "");
	}

//	public void setContactCountMethod(EbsContactCountMethod method) {
//		getColumn(EbsIndexDto.CONTACT_COUNT_SOURCE_IN_EVENT).setHidden(method == EbsContactCountMethod.ALL);
//		getColumn(EbsIndexDto.CONTACT_COUNT).setHidden(method == EbsContactCountMethod.SOURCE_CASE_IN_EVENT);
//		if (method == EbsContactCountMethod.BOTH_METHODS) {
//			getColumn(EbsIndexDto.CONTACT_COUNT_SOURCE_IN_EVENT)
//				.setCaption(I18nProperties.getPrefixCaption(EbsIndexDto.I18N_PREFIX, EbsIndexDto.CONTACT_COUNT_SOURCE_IN_EVENT));
//		} else {
//			getColumn(EbsIndexDto.CONTACT_COUNT_SOURCE_IN_EVENT)
//				.setCaption(I18nProperties.getPrefixCaption(EbsIndexDto.I18N_PREFIX, EbsIndexDto.CONTACT_COUNT));
//		}
//	}

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

		setLazyDataProvider(FacadeProvider.getEbsFacade()::getIndexList, FacadeProvider.getEbsFacade()::count);
	}

	public void setEagerDataProvider() {

		setEagerDataProvider(FacadeProvider.getEbsFacade()::getIndexList);
	}
}