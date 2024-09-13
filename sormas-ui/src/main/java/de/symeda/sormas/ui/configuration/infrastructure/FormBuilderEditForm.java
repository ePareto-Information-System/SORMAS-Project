package de.symeda.sormas.ui.configuration.infrastructure;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.OptionGroup;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.facility.*;
import de.symeda.sormas.api.infrastructure.fields.FormFieldIndexDto;
import de.symeda.sormas.api.infrastructure.fields.FormFieldReferenceDto;
import de.symeda.sormas.api.infrastructure.fields.FormFieldsCriteria;
import de.symeda.sormas.api.infrastructure.fields.FormFieldsDto;
import de.symeda.sormas.api.infrastructure.forms.FormBuilderCriteria;
import de.symeda.sormas.api.infrastructure.forms.FormBuilderDto;
import de.symeda.sormas.api.infrastructure.forms.FormBuilderIndexDto;
import de.symeda.sormas.api.infrastructure.forms.FormBuilderReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.configuration.infrastructure.components.SearchField;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.ComboBoxHelper;
import de.symeda.sormas.ui.utils.FieldHelper;
import de.symeda.sormas.ui.utils.ViewConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;

public class FormBuilderEditForm extends AbstractEditForm<FormBuilderDto> {

    private boolean create;
    private ViewConfiguration viewConfiguration;
    private static final String FIELDS_SELECTION_LOCATION = "searchFieldsLocation";
    FormFieldsCriteria formFieldsCriteria;
    ComboBox formType;

    private static final String HTML_LAYOUT = fluidRowLocs(FormBuilderDto.DISEASE, FormBuilderDto.FORM_TYPE) +
            fluidRowLocs(FIELDS_SELECTION_LOCATION);

    TwinColSelect selectFormFields = new TwinColSelect();
    Boolean firstPageLoad = true;
    private int selectedFieldsCount = 0;
    private FormFieldsCriteria criteria;
    private OptionGroup formFieldsOptionGroup;

    public FormBuilderEditForm(boolean create) {
        super(FormBuilderDto.class, FormBuilderDto.I18N_PREFIX, false);
        this.create = create;
        criteria = new FormFieldsCriteria();

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


//        formFieldsOptionGroup = addField(FormBuilderDto.FORM_FIELDS, OptionGroup.class);
//        formFieldsOptionGroup.setMultiSelect(true);
//        formFieldsOptionGroup.addItems(FacadeProvider.getFormFieldFacade().getFormFieldsByFormType(criteria));




        selectFormFields.setLeftColumnCaption(I18nProperties.getCaption(Captions.facilitiesAvailable));
        selectFormFields.setRightColumnCaption(I18nProperties.getCaption(Captions.facilitiesSelected) + " (" + selectedFieldsCount + ")");
        selectFormFields.setRows(20);
        selectFormFields.setWidth("100%");

        selectFormFields.addValueChangeListener(event -> {
            //create a list of FacilityDto
            List<FormFieldReferenceDto> selectedDtos = new ArrayList<>();
            for (FormFieldIndexDto formFieldIndexDto : (Set<FormFieldIndexDto>) event.getValue()) {
                FormFieldReferenceDto formFieldReferenceDto = new FormFieldReferenceDto();
                formFieldReferenceDto.setUuid(formFieldIndexDto.getUuid());
                formFieldReferenceDto.setCaption(formFieldIndexDto.getFieldName());
                selectedDtos.add(formFieldReferenceDto);
            }
            getValue().setFormFields(selectedDtos);
            selectFormFields.setRightColumnCaption(I18nProperties.getCaption(Captions.facilitiesSelected) + " (" + selectedDtos.size() + ")");
        });

        getContent().addComponent(selectFormFields, FIELDS_SELECTION_LOCATION);
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
            selectFormFields.setValue(new HashSet<>());
        });


        selectFormFields.setItemCaptionGenerator(new ItemCaptionGenerator<FormFieldIndexDto>() {
            @Override
            public String apply(FormFieldIndexDto fieldIndexDto) {
                return fieldIndexDto.getFieldName() + " " + fieldIndexDto.getDescription();
            }
        });

    }

    public void updateDataProvider() {

        Set<FormFieldIndexDto> selectedFormFieldIndexDtos;

        Set<FormFieldIndexDto> formFielIndexDtos = FacadeProvider.getFormFieldFacade().getIndexList(criteria, null, null, null).stream().collect(Collectors.toSet());

        if (firstPageLoad && getValue().getFormFields() != null) {
            List<FormFieldReferenceDto> existingForFields = getValue().getFormFields();
            Set<String> existingFieldUuids = existingForFields.stream()
                    .map(FormFieldReferenceDto::getUuid)
                    .collect(Collectors.toSet());

            selectedFormFieldIndexDtos = formFielIndexDtos.stream()
                    .filter(formFieldIndexDto -> existingFieldUuids.contains(formFieldIndexDto.getUuid()))
                    .collect(Collectors.toSet());

            firstPageLoad = false;
        } else if (firstPageLoad && getValue().getFormFields() == null) {
            selectedFormFieldIndexDtos = new HashSet<>();
        } else {
            selectedFormFieldIndexDtos = new HashSet<>(selectFormFields.getSelectedItems());
        }

        selectFormFields.setItems(formFielIndexDtos);
        selectFormFields.setValue(new HashSet<>(
                selectedFormFieldIndexDtos
        ));
        selectFormFields.setRightColumnCaption(I18nProperties.getCaption(Captions.facilitiesSelected) + " (" + selectedFormFieldIndexDtos.size() + ")");
    }
}
