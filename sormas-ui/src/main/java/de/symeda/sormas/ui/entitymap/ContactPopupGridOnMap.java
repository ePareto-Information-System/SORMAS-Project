package de.symeda.sormas.ui.entitymap;

import java.util.List;

import com.vaadin.ui.Window;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.GeneratedPropertyContainer;
import com.vaadin.v7.data.util.PropertyValueGenerator;
import com.vaadin.v7.ui.Grid;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.contact.MapContactDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.utils.V7UuidRenderer;

public class ContactPopupGridOnMap extends Grid {

	public static final String DISEASE_SHORT = Captions.columnDiseaseShort;
	public static final String FIRST_NAME = PersonDto.FIRST_NAME;
	public static final String LAST_NAME = PersonDto.LAST_NAME;

	private final MapContactDto mapContactDto;
	private final DashboardMapComponent dashboardMapComponent;

	public ContactPopupGridOnMap(Window window, MapContactDto mapContactDto, DashboardMapComponent dashboardMapComponent) {
		this.mapContactDto = mapContactDto;
		this.dashboardMapComponent = dashboardMapComponent;
		setWidth(960, Unit.PIXELS);
		setHeightUndefined();

		setSelectionMode(SelectionMode.NONE);

		BeanItemContainer<ContactDto> container = new BeanItemContainer<ContactDto>(ContactDto.class);
		GeneratedPropertyContainer generatedContainer = new GeneratedPropertyContainer(container);
		setContainerDataSource(generatedContainer);

		generatedContainer.addGeneratedProperty(DISEASE_SHORT, new PropertyValueGenerator<String>() {

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				ContactDto contactDto = (ContactDto) itemId;
				String diseaseName = contactDto.getDisease().getName();
				return Disease.valueOf(diseaseName).toShortString();
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});

		generatedContainer.addGeneratedProperty(FIRST_NAME, new PropertyValueGenerator<String>() {

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				ContactDto caseDataDto = (ContactDto) itemId;
				PersonDto personDto = FacadeProvider.getPersonFacade().getPersonByUuid(caseDataDto.getPerson().getUuid());
				return personDto.getFirstName();
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});

		generatedContainer.addGeneratedProperty(LAST_NAME, new PropertyValueGenerator<String>() {

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				ContactDto caseDataDto = (ContactDto) itemId;
				PersonDto personDto = FacadeProvider.getPersonFacade().getPersonByUuid(caseDataDto.getPerson().getUuid());
				return personDto.getLastName();
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});

		setColumns(ContactDto.UUID, DISEASE_SHORT, ContactDto.CONTACT_CLASSIFICATION, FIRST_NAME, LAST_NAME,
//			ContactDto.CONTACT_CATEGORY,
			ContactDto.FOLLOW_UP_STATUS);

		getColumn(ContactDto.UUID).setRenderer(new V7UuidRenderer());
		Language userLanguage = I18nProperties.getUserLanguage();
//		getColumn(ContactDto.REPORT_DATE_TIME).setRenderer(new DateRenderer(DateHelper.getLocalDateTimeFormat(userLanguage)));
//		if (mapContactDto == null || !FacilityHelper.isOtherOrNoneHealthFacility(mapContactDto.getUuid())) {
		if (mapContactDto == null) {
			getColumn(ContactDto.FOLLOW_UP_STATUS).setHidden(true);
		}

		for (Column column : getColumns()) {
			column.setHeaderCaption(
				I18nProperties.getPrefixCaption(ContactDto.I18N_PREFIX, column.getPropertyId().toString(), column.getHeaderCaption()));
		}

		addItemClickListener(e -> {
			window.close();
			ControllerProvider.getContactController().navigateToData(((ContactDto) e.getItemId()).getUuid(), true);
		});

		reload();
	}

	@SuppressWarnings("unchecked")
	private BeanItemContainer<ContactDto> getContainer() {
		GeneratedPropertyContainer container = (GeneratedPropertyContainer) super.getContainerDataSource();
		return (BeanItemContainer<ContactDto>) container.getWrappedContainer();
	}

	public void reload() {
		getContainer().removeAllItems();

		List<ContactDto> contacts = dashboardMapComponent.getContactsForMap(mapContactDto);

		getContainer().addAll(contacts);
		this.setHeightByRows(contacts.size());
	}

}
