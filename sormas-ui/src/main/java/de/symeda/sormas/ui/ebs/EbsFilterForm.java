package de.symeda.sormas.ui.ebs;


import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.TextField;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.ebs.EbsCriteria;
import de.symeda.sormas.api.ebs.EbsIndexDto;
import de.symeda.sormas.api.i18n.Descriptions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.ui.utils.*;

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
	protected EbsFilterForm() {
		super(EbsCriteria.class,
				EbsIndexDto.I18N_PREFIX,
				true);
	}
	@Override
	protected String[] getMainFilterLocators() {

		return new String[] {
				EbsIndexDto.SRC_TYPE,
				EbsIndexDto.REPORT_DATE_TIME,
				EbsIndexDto.TRIAGING_DECISION,
				EbsIndexDto.TRIAGE_DATE,
		};
	}

	@Override
	protected void addFields() {
		final ComboBox srcField = addField(FieldConfiguration.pixelSized(EbsIndexDto.SRC_TYPE, 140));
		ComboBox regionField = addField(
				FieldConfiguration
						.withCaptionAndPixelSized(LocationDto.REGION, I18nProperties.getPrefixCaption(LocationDto.I18N_PREFIX, LocationDto.REGION), 140));
		regionField.addItems(FacadeProvider.getRegionFacade().getAllActiveAsReference());

		ComboBox districtField = addField(
				FieldConfiguration
						.withCaptionAndPixelSized(LocationDto.DISTRICT, I18nProperties.getPrefixCaption(LocationDto.I18N_PREFIX, LocationDto.DISTRICT), 140));
		districtField.setDescription(I18nProperties.getDescription(Descriptions.descDistrictFilter));
		districtField.setEnabled(false);

		ComboBox communityField = addField(
				FieldConfiguration.withCaptionAndPixelSized(
						LocationDto.COMMUNITY,
						I18nProperties.getPrefixCaption(LocationDto.I18N_PREFIX, LocationDto.COMMUNITY),
						140));
		communityField.setDescription(I18nProperties.getDescription(Descriptions.descCommunityFilter));
		communityField.setEnabled(false);
		Field<?> reportDate = addField(FieldConfiguration.pixelSized(EbsIndexDto.REPORT_DATE_TIME, 200));
		reportDate.removeAllValidators();
		final ComboBox triagingField = addField(FieldConfiguration.pixelSized(EbsIndexDto.TRIAGING_DECISION, 140));
		Field<?> triageDate = addField(FieldConfiguration.pixelSized(EbsIndexDto.TRIAGE_DATE, 200));
		triageDate.removeAllValidators();
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
	}
}
