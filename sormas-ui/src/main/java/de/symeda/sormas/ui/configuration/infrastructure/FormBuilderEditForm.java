package de.symeda.sormas.ui.configuration.infrastructure;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.ui.*;
import com.vaadin.v7.ui.ComboBox;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.infrastructure.fields.FormFieldIndexDto;
import de.symeda.sormas.api.infrastructure.fields.FormFieldReferenceDto;
import de.symeda.sormas.api.infrastructure.fields.FormFieldsCriteria;
import de.symeda.sormas.api.infrastructure.forms.FormBuilderDto;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.ui.utils.AbstractEditForm;

import java.util.*;
import java.util.stream.Collectors;

import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;

import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.VerticalLayout;

public class FormBuilderEditForm extends AbstractEditForm<FormBuilderDto> {
    private boolean create;
    private static final String FIELDS_SELECTION_LOCATION = "searchFieldsLocation";
    FormFieldsCriteria formFieldsCriteria;
    ComboBox formType;
    private Set<String> lastSelectedIds = new HashSet<>();
    private static final String HTML_LAYOUT = fluidRowLocs(FormBuilderDto.DISEASE, FormBuilderDto.FORM_TYPE) +
            fluidRowLocs(FIELDS_SELECTION_LOCATION);

    private com.vaadin.v7.ui.ListSelect availableFields;
    private Table selectedFieldsTable;
    private IndexedContainer selectedFieldsContainer;
    private static final String PROPERTY_FIELD = "field";
    private static final String PROPERTY_ORDER = "order";
    private static final String PROPERTY_NAME = "name";
    private int currentOrder = 0;
    Boolean firstPageLoad = true;
    private FormFieldsCriteria criteria;

    public FormBuilderEditForm(boolean create) {
        super(FormBuilderDto.class, FormBuilderDto.I18N_PREFIX, false);
        this.create = create;
        this.criteria = new FormFieldsCriteria();

        setWidth(900, Unit.PIXELS);

        if (create) {
            hideValidationUntilNextCommit();
        }
        addFields();
    }

    @Override
    protected String createHtmlLayout() {
        return HTML_LAYOUT;
    }

    @Override
    protected void addFields() {
        addFields(FormBuilderDto.DISEASE);
        formType = addField(FormBuilderDto.FORM_TYPE, ComboBox.class);

        HorizontalLayout fieldSelectionLayout = new HorizontalLayout();
        fieldSelectionLayout.setWidth("100%");
        fieldSelectionLayout.setSpacing(true);

        availableFields = new com.vaadin.v7.ui.ListSelect();
        availableFields.setWidth("100%");
        availableFields.setRows(20);
        availableFields.setNullSelectionAllowed(false);
        availableFields.setMultiSelect(true);
        availableFields.setCaption(I18nProperties.getCaption(Captions.formFieldsAvailable));

        selectedFieldsTable = new Table();
        selectedFieldsTable.setWidth("100%");
        selectedFieldsTable.setHeight("400px");
        selectedFieldsTable.setCaption(I18nProperties.getCaption(Captions.formFieldsSelected));
        selectedFieldsTable.setSelectable(true);
        selectedFieldsTable.setMultiSelect(true);

        selectedFieldsTable.addValueChangeListener(event -> {
            @SuppressWarnings("unchecked")
            Set<String> selectedIds = (Set<String>) event.getProperty().getValue();
            if (selectedIds != null && !selectedIds.isEmpty()) {
                lastSelectedIds = new HashSet<>(selectedIds);

                Object lastSelectedItemId = selectedIds.iterator().next();
                selectedFieldsTable.setCurrentPageFirstItemId(lastSelectedItemId);
            }
        });

        selectedFieldsContainer = new IndexedContainer();
        selectedFieldsContainer.addContainerProperty(PROPERTY_FIELD, FormFieldIndexDto.class, null);
        selectedFieldsContainer.addContainerProperty(PROPERTY_ORDER, Integer.class, null);
        selectedFieldsContainer.addContainerProperty(PROPERTY_NAME, String.class, null);
        selectedFieldsTable.setContainerDataSource(selectedFieldsContainer);

        selectedFieldsTable.setColumnHeader(PROPERTY_ORDER, "Order");
        selectedFieldsTable.setColumnHeader(PROPERTY_NAME, "Field Name");
        selectedFieldsTable.setVisibleColumns(PROPERTY_ORDER, PROPERTY_NAME);
        selectedFieldsTable.setColumnAlignment(PROPERTY_ORDER, Table.Align.CENTER);

        Button moveUpButton = new Button(" ↑ ");
        Button moveDownButton = new Button(" ↓ ");
        Button addButton = new Button("→");
        Button removeButton = new Button("←");
        Button moveToTopButton = new Button("↑↑");

        moveUpButton.addStyleName(ValoTheme.BUTTON_SMALL);
        moveDownButton.addStyleName(ValoTheme.BUTTON_SMALL);
        addButton.addStyleName(ValoTheme.BUTTON_SMALL);
        removeButton.addStyleName(ValoTheme.BUTTON_SMALL);
        moveToTopButton.addStyleName(ValoTheme.BUTTON_SMALL);

        moveToTopButton.setDescription("Move selected items to top");

        VerticalLayout buttonLayout = new VerticalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.addComponents(addButton, removeButton, moveUpButton, moveDownButton, moveToTopButton);
        buttonLayout.setWidth("15%");

        addButton.addClickListener(event -> {
            @SuppressWarnings("unchecked")
            Set<FormFieldIndexDto> selected = (Set<FormFieldIndexDto>) availableFields.getValue();
            for (FormFieldIndexDto field : selected) {
                boolean isDuplicate = false;
                // Check for duplicate field names in selectedFieldsContainer
                for (Object itemId : selectedFieldsContainer.getItemIds()) {
                    Item existingItem = selectedFieldsContainer.getItem(itemId);
                    String existingName = (String) existingItem.getItemProperty(PROPERTY_NAME).getValue();
                    if (existingName != null && existingName.equals(field.getFieldName())) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (!isDuplicate && selectedFieldsContainer.getItem(field.getUuid()) == null) {
                    Item item = selectedFieldsContainer.addItem(field.getUuid());
                    if (item != null) {
                        int order = selectedFieldsContainer.size() + 1;
                        item.getItemProperty(PROPERTY_FIELD).setValue(field);
                        item.getItemProperty(PROPERTY_ORDER).setValue(order);
                        item.getItemProperty(PROPERTY_NAME).setValue(field.getFieldName());
                    }
                    availableFields.removeItem(field);
                }
            }
            availableFields.setValue(null);
            reorderItems();
            updateFormFieldsList();
        });


        removeButton.addClickListener(event -> {
            Set<String> selectedIds = (Set<String>) selectedFieldsTable.getValue();
            for (String id : selectedIds) {
                FormFieldIndexDto field = (FormFieldIndexDto) selectedFieldsContainer.getItem(id).getItemProperty(PROPERTY_FIELD).getValue();

                selectedFieldsContainer.removeItem(id);
                availableFields.addItem(field);
                availableFields.setItemCaption(field, field.getFieldName());
            }
            reorderItems();
            updateFormFieldsList();
        });


        moveUpButton.addClickListener(event -> {
            Set<String> selectedIds = (Set<String>) selectedFieldsTable.getValue();
            if (selectedIds != null && !selectedIds.isEmpty()) {
                lastSelectedIds = new HashSet<>(selectedIds);
                for (String id : selectedIds) {
                    moveItem(id, true);
                }
                updateFormFieldsList();
                selectedFieldsTable.setValue(lastSelectedIds);
            }
        });

        moveDownButton.addClickListener(event -> {
            Set<String> selectedIds = (Set<String>) selectedFieldsTable.getValue();
            if (selectedIds != null && !selectedIds.isEmpty()) {
                lastSelectedIds = new HashSet<>(selectedIds);
                Object[] selectedIdsArray = selectedIds.toArray();
                for (int i = selectedIdsArray.length - 1; i >= 0; i--) {
                    moveItem((String) selectedIdsArray[i], false);
                }
                updateFormFieldsList();
                selectedFieldsTable.setValue(lastSelectedIds);
            }
        });

        moveToTopButton.addClickListener(event -> {
            Set<String> selectedIds = (Set<String>) selectedFieldsTable.getValue();
            if (selectedIds != null && !selectedIds.isEmpty()) {
                lastSelectedIds = new HashSet<>(selectedIds);
                List<String> selectedIdsList = new ArrayList<>(selectedIds);
                Collections.reverse(selectedIdsList);
                for (String id : selectedIdsList) {
                    moveItemToTop(id);
                }
                updateFormFieldsList();
                selectedFieldsTable.setValue(lastSelectedIds);
            }
        });

        fieldSelectionLayout.addComponents(availableFields, buttonLayout, selectedFieldsTable);
        getContent().addComponent(fieldSelectionLayout, FIELDS_SELECTION_LOCATION);
    }

    private void moveItem(String itemId, boolean up) {
        int currentIndex = getCurrentItemIndex(itemId);
        if (up && currentIndex > 0) {
            swapItems(currentIndex, currentIndex - 1);
        } else if (!up && currentIndex < selectedFieldsContainer.size() - 1) {
            swapItems(currentIndex, currentIndex + 1);
        }
        reorderItems();
    }

    private int getCurrentItemIndex(String itemId) {
        Object[] itemIds = selectedFieldsContainer.getItemIds().toArray();
        for (int i = 0; i < itemIds.length; i++) {
            if (itemIds[i].equals(itemId)) {
                return i;
            }
        }
        return -1;
    }

    private void swapItems(int index1, int index2) {
        Object[] itemIds = selectedFieldsContainer.getItemIds().toArray();
        if (index1 >= 0 && index2 >= 0 && index1 < itemIds.length && index2 < itemIds.length) {
            IndexedContainer newContainer = new IndexedContainer();
            newContainer.addContainerProperty(PROPERTY_FIELD, FormFieldIndexDto.class, null);
            newContainer.addContainerProperty(PROPERTY_ORDER, Integer.class, null);
            newContainer.addContainerProperty(PROPERTY_NAME, String.class, null);

            List<Object> newOrder = new ArrayList<>(selectedFieldsContainer.getItemIds());
            Object temp = newOrder.get(index1);
            newOrder.set(index1, newOrder.get(index2));
            newOrder.set(index2, temp);

            for (Object itemId : newOrder) {
                Item oldItem = selectedFieldsContainer.getItem(itemId);
                Item newItem = newContainer.addItem(itemId);
                for (Object propertyId : selectedFieldsContainer.getContainerPropertyIds()) {
                    newItem.getItemProperty(propertyId).setValue(
                            oldItem.getItemProperty(propertyId).getValue()
                    );
                }
            }
            selectedFieldsTable.setContainerDataSource(newContainer);
            selectedFieldsContainer = newContainer;
            selectedFieldsTable.setVisibleColumns(PROPERTY_ORDER, PROPERTY_NAME);
            selectedFieldsTable.setColumnHeader(PROPERTY_ORDER, "Order");
            selectedFieldsTable.setColumnHeader(PROPERTY_NAME, "Field Name");

            if (!lastSelectedIds.isEmpty()) {
                selectedFieldsTable.setValue(lastSelectedIds);
            }
        }
    }

    private void moveItemToTop(String itemId) {
        int currentIndex = getCurrentItemIndex(itemId);
        if (currentIndex > 0) {
            IndexedContainer newContainer = new IndexedContainer();
            newContainer.addContainerProperty(PROPERTY_FIELD, FormFieldIndexDto.class, null);
            newContainer.addContainerProperty(PROPERTY_ORDER, Integer.class, null);
            newContainer.addContainerProperty(PROPERTY_NAME, String.class, null);

            List<Object> newOrder = new ArrayList<>(selectedFieldsContainer.getItemIds());
            Object movedItem = newOrder.remove(currentIndex);
            newOrder.add(0, movedItem);

            for (Object id : newOrder) {
                Item oldItem = selectedFieldsContainer.getItem(id);
                Item newItem = newContainer.addItem(id);
                for (Object propertyId : selectedFieldsContainer.getContainerPropertyIds()) {
                    newItem.getItemProperty(propertyId).setValue(
                            oldItem.getItemProperty(propertyId).getValue()
                    );
                }
            }
            selectedFieldsTable.setContainerDataSource(newContainer);
            selectedFieldsContainer = newContainer;
            selectedFieldsTable.setVisibleColumns(PROPERTY_ORDER, PROPERTY_NAME);
            selectedFieldsTable.setColumnHeader(PROPERTY_ORDER, "Order");
            selectedFieldsTable.setColumnHeader(PROPERTY_NAME, "Field Name");
            reorderItems();
        }
    }

    private void reorderItems() {
        currentOrder = 0;
        for (Object itemId : selectedFieldsContainer.getItemIds()) {
            Item item = selectedFieldsContainer.getItem(itemId);
            item.getItemProperty(PROPERTY_ORDER).setValue(++currentOrder);
            FormFieldIndexDto field = (FormFieldIndexDto) item.getItemProperty(PROPERTY_FIELD).getValue();
            FormFieldReferenceDto dto = new FormFieldReferenceDto();
            dto.setUuid(field.getUuid());
            dto.setCaption(field.getFieldName());
            dto.setDisplayOrder(currentOrder);
            getValue().getFormFields().add(dto);
        }
    }

    private void updateFormFieldsList() {
        List<FormFieldReferenceDto> selectedDtos = new ArrayList<>();

        for (Object itemId : selectedFieldsContainer.getItemIds()) {
            Item item = selectedFieldsContainer.getItem(itemId);
            FormFieldIndexDto field = (FormFieldIndexDto) item.getItemProperty(PROPERTY_FIELD).getValue();

            FormFieldReferenceDto dto = new FormFieldReferenceDto();
            dto.setUuid(field.getUuid());
            dto.setCaption(field.getFieldName());
            selectedDtos.add(dto);
        }

        getValue().setFormFields(selectedDtos);
    }

    @Override
    public void setValue(FormBuilderDto newFieldValue) throws ReadOnlyException, Converter.ConversionException {
        super.setValue(newFieldValue);
        formFieldsCriteria = new FormFieldsCriteria();

        if (newFieldValue != null) {
            criteria.setFormType(newFieldValue.getFormType());
            updateDataProvider();
        }

        formType.addValueChangeListener(event -> {
            FormType formType = (FormType) event.getProperty().getValue();
            criteria.setFormType(formType);
            updateDataProvider();
            getValue().setFormFields(null);
            selectedFieldsContainer.removeAllItems();
        });
    }

    public void updateDataProvider() {
        Set<FormFieldIndexDto> formFieldIndexDtos = FacadeProvider.getFormFieldFacade()
                .getIndexList(criteria, null, null, List.of(new SortProperty("displayOrder", true)))
                .stream()
                .collect(Collectors.toSet());

        availableFields.removeAllItems();

        Set<String> selectedFieldUuids = getValue().getFormFields() != null
                ? getValue().getFormFields().stream()
                .map(FormFieldReferenceDto::getUuid)
                .collect(Collectors.toSet())
                : Collections.emptySet();

        for (FormFieldIndexDto dto : formFieldIndexDtos) {
            if (!selectedFieldUuids.contains(dto.getUuid())) {
                availableFields.addItem(dto);
                availableFields.setItemCaption(dto, dto.getFieldName());
            }
        }

        selectedFieldsContainer.removeAllItems();
        currentOrder = 0;

        if (firstPageLoad && getValue().getFormFields() != null) {
            List<FormFieldReferenceDto> existingFormFields = getValue().getFormFields();
            Map<String, FormFieldIndexDto> fieldMap = formFieldIndexDtos.stream()
                    .collect(Collectors.toMap(FormFieldIndexDto::getUuid, f -> f));

            for (FormFieldReferenceDto ref : existingFormFields) {
                FormFieldIndexDto field = fieldMap.get(ref.getUuid());
                if (field != null) {
                    Item item = selectedFieldsContainer.addItem(field.getUuid());
                    if (item != null) {
                        item.getItemProperty(PROPERTY_FIELD).setValue(field);
                        item.getItemProperty(PROPERTY_ORDER).setValue(++currentOrder);
                        item.getItemProperty(PROPERTY_NAME).setValue(field.getFieldName());
                    }
                }
            }
            selectedFieldsTable.setVisibleColumns(PROPERTY_ORDER, PROPERTY_NAME);
            selectedFieldsTable.setColumnHeader(PROPERTY_ORDER, "Order");
            selectedFieldsTable.setColumnHeader(PROPERTY_NAME, "Field Name");
            firstPageLoad = false;
        }
    }


}