package de.symeda.sormas.ui.ebs;


import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.Field;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.ebs.EbsCriteria;
import de.symeda.sormas.api.ebs.EbsIndexDto;
import de.symeda.sormas.api.ebs.NewEbsDateType;
import de.symeda.sormas.api.i18n.Descriptions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.api.utils.DateFilterOption;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.EpiWeek;
import de.symeda.sormas.api.utils.criteria.CriteriaDateType;
import de.symeda.sormas.api.utils.criteria.CriteriaDateTypeHelper;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.utils.*;

import java.util.Date;

import static de.symeda.sormas.ui.utils.LayoutUtil.filterLocs;

///*
// * SORMAS® - Surveillance Outbreak Response Management & Analysis System
// * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU General Public License for more details.
// * You should have received a copy of the GNU General Public License
// * along with this program. If not, see <https://www.gnu.org/licenses/>.
// */


public class EbsFilterForm extends AbstractFilterForm<EbsCriteria> {

	private static final long serialVersionUID = -1366745065032487009L;
	private static final String WEEK_AND_DATE_FILTER = "moreFilters";
	protected EbsFilterForm() {
		super(EbsCriteria.class,
				EbsIndexDto.I18N_PREFIX,
				FieldVisibilityCheckers.withCountry(FacadeProvider.getConfigFacade().getCountryLocale()));
	}
	private static final String MORE_FILTERS_HTML_LAYOUT = filterLocs(
			WEEK_AND_DATE_FILTER
	);
	@Override
	protected String[] getMainFilterLocators() {

		return new String[] {
				EbsIndexDto.SOURCE_INFORMATION,
				EbsIndexDto.REGION,
				EbsIndexDto.DISTRICT,
				EbsIndexDto.COMMUNITY,
				EbsIndexDto.TRIAGING_DECISION,
				EbsIndexDto.SIGNAL_CATEGORY,
				WEEK_AND_DATE_FILTER
		};
	}

	@Override
	protected void addFields() {
		final ComboBox srcField = addField(FieldConfiguration.pixelSized(EbsIndexDto.SOURCE_INFORMATION, 140));
		final ComboBox signalCategory = addField(FieldConfiguration.pixelSized(EbsIndexDto.SIGNAL_CATEGORY, 140));
		ComboBox regionField = addField(FieldConfiguration.withCaptionAndPixelSized(LocationDto.REGION, I18nProperties.getPrefixCaption(LocationDto.I18N_PREFIX, LocationDto.REGION), 140));
		regionField.addItems(FacadeProvider.getRegionFacade().getAllActiveAsReference());

		ComboBox districtField = addField(FieldConfiguration.withCaptionAndPixelSized(LocationDto.DISTRICT, I18nProperties.getPrefixCaption(LocationDto.I18N_PREFIX, LocationDto.DISTRICT), 140));
		districtField.setDescription(I18nProperties.getDescription(Descriptions.descDistrictFilter));
		districtField.setEnabled(false);

		ComboBox communityField = addField(FieldConfiguration.withCaptionAndPixelSized(LocationDto.COMMUNITY, I18nProperties.getPrefixCaption(LocationDto.I18N_PREFIX, LocationDto.COMMUNITY), 140));
		communityField.setDescription(I18nProperties.getDescription(Descriptions.descCommunityFilter));
		communityField.setEnabled(false);
		Field<?> reportDate = addField(FieldConfiguration.pixelSized(EbsIndexDto.REPORT_DATE_TIME, 200));
		reportDate.removeAllValidators();
		final ComboBox triagingField = addField(FieldConfiguration.pixelSized(EbsIndexDto.TRIAGING_DECISION, 140));
		Field<?> triageDate = addField(FieldConfiguration.pixelSized(EbsIndexDto.TRIAGE_DATE, 200));
		triageDate.removeAllValidators();
	}

	@Override
	protected String createMoreFiltersHtmlLayout() {
		return MORE_FILTERS_HTML_LAYOUT;
	}

	@Override
	public void addMoreFilters(CustomLayout moreFiltersContainer) {
		moreFiltersContainer.addComponent(buildWeekAndDateFilter(false),WEEK_AND_DATE_FILTER);

	}


	@Override
	protected void applyDependenciesOnFieldChange(String propertyId, Property.ValueChangeEvent event) {
		switch (propertyId) {
		case LocationDto.REGION:
			RegionReferenceDto region = (RegionReferenceDto) event.getProperty().getValue();
			if (region != null) {
				applyRegionFilterDependency(region, LocationDto.DISTRICT);
				clearAndDisableFields(LocationDto.COMMUNITY);
			} else {
				clearAndDisableFields(LocationDto.DISTRICT, LocationDto.COMMUNITY);
			}
			break;
		case LocationDto.DISTRICT:
			DistrictReferenceDto district = (DistrictReferenceDto) event.getProperty().getValue();
			if (district != null) {
				applyDistrictDependency(district, LocationDto.COMMUNITY);
			} else {
				clearAndDisableFields(LocationDto.COMMUNITY);
			}
			break;
		}
	}

	@Override
	protected void applyRegionFilterDependency(RegionReferenceDto region, String districtFieldId) {
		final ComboBox districtField = getField(districtFieldId);
		if (region != null) {
			FieldHelper.updateItems(districtField, FacadeProvider.getDistrictFacade().getAllActiveByRegion(region.getUuid()));
			districtField.setEnabled(true);
		} else {
			districtField.setEnabled(false);
		}
	}

	@Override
	protected void applyDistrictDependency(DistrictReferenceDto district, String communityFieldId) {
		final ComboBox communityField = getField(communityFieldId);
		if (district != null) {
			FieldHelper.updateItems(communityField, FacadeProvider.getCommunityFacade().getAllActiveByDistrict(district.getUuid()));
			communityField.setEnabled(true);
		} else {
			communityField.setEnabled(false);
		}
	}

	@Override
	protected void applyDependenciesOnNewValue(EbsCriteria criteria) {

		RegionReferenceDto region = criteria.getRegion();
		DistrictReferenceDto district = criteria.getDistrict();
		applyRegionAndDistrictFilterDependency(region, LocationDto.DISTRICT, district, LocationDto.COMMUNITY);
		// Date/Epi week filter
		HorizontalLayout dateFilterLayout = (HorizontalLayout) getMoreFiltersContainer().getComponent(WEEK_AND_DATE_FILTER);
		@SuppressWarnings("unchecked")
		EpiWeekAndDateFilterComponent<NewEbsDateType> weekAndDateFilter =
				(EpiWeekAndDateFilterComponent<NewEbsDateType>) dateFilterLayout.getComponent(0);

		weekAndDateFilter.getDateTypeSelector().setValue(criteria.getNewEbsDateType());
		weekAndDateFilter.getDateFilterOptionFilter().setValue(criteria.getDateFilterOption());
		Date newEbsDateFrom = criteria.getNewEbsDateFrom();
		Date newEbsDateTo = criteria.getNewEbsDateTo();
		if (newEbsDateFrom != null && newEbsDateTo != null) {
			if (DateFilterOption.EPI_WEEK.equals(criteria.getDateFilterOption())) {
				weekAndDateFilter.getWeekFromFilter().setValue(DateHelper.getEpiWeek(newEbsDateFrom));
				weekAndDateFilter.getWeekToFilter().setValue(DateHelper.getEpiWeek(newEbsDateTo));
			} else {
				weekAndDateFilter.getDateFromFilter().setValue(criteria.getNewEbsDateFrom());
				weekAndDateFilter.getDateToFilter().setValue(criteria.getNewEbsDateTo());
			}
		}
	}

	private HorizontalLayout buildWeekAndDateFilter(boolean isExternalShareEnabled) {

		EpiWeekAndDateFilterComponent<CriteriaDateType> weekAndDateFilter = new EpiWeekAndDateFilterComponent<>(
				false,
				false,
				I18nProperties.getString(Strings.infoEbsDate),
				CriteriaDateTypeHelper.getTypes(NewEbsDateType.class, isExternalShareEnabled),
				I18nProperties.getString(Strings.promptNewEbsDateType),
				null,
				this);
		weekAndDateFilter.getWeekFromFilter().setInputPrompt(I18nProperties.getString(Strings.promptEbsEpiWeekFrom));
		weekAndDateFilter.getWeekToFilter().setInputPrompt(I18nProperties.getString(Strings.promptEbsEpiWeekTo));
		weekAndDateFilter.getDateFromFilter().setInputPrompt(I18nProperties.getString(Strings.promptEbsDateFrom));
		weekAndDateFilter.getDateToFilter().setInputPrompt(I18nProperties.getString(Strings.promptDateTo));

		addApplyHandler(e -> onApplyClick(weekAndDateFilter));

		HorizontalLayout dateFilterRowLayout = new HorizontalLayout();
		dateFilterRowLayout.setSpacing(true);
		dateFilterRowLayout.setSizeUndefined();

		dateFilterRowLayout.addComponent(weekAndDateFilter);

		return dateFilterRowLayout;
	}

	private void onApplyClick(EpiWeekAndDateFilterComponent<CriteriaDateType> weekAndDateFilter) {
		DateFilterOption dateFilterOption = (DateFilterOption) weekAndDateFilter.getDateFilterOptionFilter().getValue();
		Date fromDate, toDate;
		if (dateFilterOption == DateFilterOption.DATE) {
			Date dateFrom = weekAndDateFilter.getDateFromFilter().getValue();
			fromDate = dateFrom != null ? DateHelper.getStartOfDay(dateFrom) : null;
			Date dateTo = weekAndDateFilter.getDateToFilter().getValue();
			toDate = dateFrom != null ? DateHelper.getEndOfDay(dateTo) : null;
		} else {
			fromDate = DateHelper.getEpiWeekStart((EpiWeek) weekAndDateFilter.getWeekFromFilter().getValue());
			toDate = DateHelper.getEpiWeekEnd((EpiWeek) weekAndDateFilter.getWeekToFilter().getValue());
		}
		if ((fromDate != null && toDate != null) || (fromDate == null && toDate == null)) {
			EbsCriteria criteria = getValue();
			CriteriaDateType newEbsDateType = (CriteriaDateType) weekAndDateFilter.getDateTypeSelector().getValue();

			criteria.newEbsDateBetween(fromDate, toDate, newEbsDateType != null ? newEbsDateType : NewEbsDateType.REPORT);
			criteria.dateFilterOption(dateFilterOption);
		} else {
			weekAndDateFilter.setNotificationsForMissingFilters();
		}
	}
}
