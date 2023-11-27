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
package de.symeda.sormas.ui.events;

import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;

import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.event.EventDto;
import de.symeda.sormas.api.event.EventParticipantDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.location.LocationDto;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.FieldHelper;

public class EventParticipantEditForm extends AbstractEditForm<EventParticipantDto> {

	private static final long serialVersionUID = 1L;

	private static final String MEDICAL_INFORMATION_LOC = "medicalInformationLoc";

	private static final String HTML_LAYOUT = fluidRowLocs(EventParticipantDto.REGION, EventParticipantDto.DISTRICT)
		+ fluidRowLocs(EventParticipantDto.UUID, EventParticipantDto.REPORTING_USER)
		+ fluidRowLocs(EventParticipantDto.INVOLVEMENT_DESCRIPTION)
		+ loc(MEDICAL_INFORMATION_LOC)
		+ fluidRowLocs(EventParticipantDto.VACCINATION_STATUS)
		+ fluidRowLocs(EventParticipantDto.DELETION_REASON)
		+ fluidRowLocs(EventParticipantDto.OTHER_DELETION_REASON);

	private final EventDto event;

	public EventParticipantEditForm(EventDto event, boolean isPseudonymized, boolean inJurisdiction) {
		super(
			EventParticipantDto.class,
			EventParticipantDto.I18N_PREFIX,
			false,
			FieldVisibilityCheckers.withDisease(event.getDisease()),
			UiFieldAccessCheckers.forDataAccessLevel(UserProvider.getCurrent().getPseudonymizableDataAccessLevel(inJurisdiction), isPseudonymized));
		this.event = event;

		addFields();
	}

	@Override
	protected void addFields() {

		if (event == null) {
			// workaround to stop initialization until event is set
			return;
		}

		if (searchPerson) {
			searchPersonButton = createPersonSearchButton(PERSON_SEARCH_LOC);
			searchPersonButton.setCaption(I18nProperties.getString(Strings.infoSearchPersonOnDependentForm));
			getContent().addComponent(searchPersonButton, PERSON_SEARCH_LOC);
		}

		pef = new PersonEditForm(PersonContext.EVENT_PARTICIPANT, event.getDisease(), event.getDiseaseDetails(), null, isPersonPseudonymized, event.getCaseOrigin());
		pef.setWidth(100, Unit.PERCENTAGE);
		pef.setImmediate(true);
		getFieldGroup().bind(pef, EventParticipantDto.PERSON);
		getContent().addComponent(pef, EventParticipantDto.PERSON);

		addField(EventParticipantDto.INVOLVEMENT_DESCRIPTION, TextField.class);
		ComboBox region = addInfrastructureField(EventParticipantDto.REGION);
		region.setDescription(I18nProperties.getPrefixDescription(EventParticipantDto.I18N_PREFIX, EventParticipantDto.REGION));
		ComboBox district = addInfrastructureField(EventParticipantDto.DISTRICT);
		district.setDescription(I18nProperties.getPrefixDescription(EventParticipantDto.I18N_PREFIX, EventParticipantDto.DISTRICT));

		region.addValueChangeListener(e -> {
			RegionReferenceDto regionDto = (RegionReferenceDto) e.getProperty().getValue();

			FieldHelper
				.updateItems(district, regionDto != null ? FacadeProvider.getDistrictFacade().getAllActiveByRegion(regionDto.getUuid()) : null);
		});
		region.addItems(FacadeProvider.getRegionFacade().getAllActiveByServerCountry());

		LocationDto locationDto = event.getEventLocation();
		boolean shouldBeRequired = locationDto.getRegion() == null || locationDto.getDistrict() == null;
		region.setRequired(shouldBeRequired);
		district.setRequired(shouldBeRequired);

		addField(EventParticipantDto.UUID, TextField.class);
		addField(EventParticipantDto.REPORTING_USER, ComboBox.class);
		setReadOnly(true, EventParticipantDto.UUID, EventParticipantDto.REPORTING_USER);

		initializeVisibilitiesAndAllowedVisibilities();
		initializeAccessAndAllowedAccesses();

		addField(EventParticipantDto.VACCINATION_STATUS);

		addField(EventParticipantDto.DELETION_REASON);
		addField(EventParticipantDto.OTHER_DELETION_REASON, TextArea.class).setRows(3);
		setVisible(false, EventParticipantDto.DELETION_REASON, EventParticipantDto.OTHER_DELETION_REASON);
	}

	@Override
	protected String createHtmlLayout() {
		return HTML_LAYOUT;
	}

	@Override
	public void setValue(EventParticipantDto newFieldValue) throws ReadOnlyException, Converter.ConversionException {
		super.setValue(newFieldValue);
	}

	@Override
	public void setPerson(PersonDto person) {
		if (person != null) {
			this.getValue().setPerson(person);
		} else {
			this.getValue().setPerson(originalPerson);
		}
		getFieldGroup().unbind(pef);
		pef = new PersonEditForm(
			PersonContext.EVENT_PARTICIPANT,
			event.getDisease(),
			event.getDiseaseDetails(),
			null,
			person != null ? person.isPseudonymized() : isPersonPseudonymized, event.getCaseOrigin());
		pef.setWidth(100, Unit.PERCENTAGE);
		pef.setImmediate(true);
		getFieldGroup().bind(pef, EventParticipantDto.PERSON);
		getContent().addComponent(pef, EventParticipantDto.PERSON);
	}

	@Override
	protected void enablePersonFields(Boolean enable) {
		pef.getFieldGroup().setEnabled(enable);
	}
}
