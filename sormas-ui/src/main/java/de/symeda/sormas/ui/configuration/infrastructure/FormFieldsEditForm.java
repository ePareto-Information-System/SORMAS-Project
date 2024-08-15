package de.symeda.sormas.ui.configuration.infrastructure;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.ui.ComboBox;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.facility.*;
import de.symeda.sormas.api.infrastructure.fields.FormFieldsDto;
import de.symeda.sormas.api.infrastructure.forms.FormBuilderCriteria;
import de.symeda.sormas.api.infrastructure.forms.FormBuilderDto;
import de.symeda.sormas.api.infrastructure.forms.FormBuilderIndexDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.configuration.infrastructure.components.SearchField;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.ComboBoxHelper;
import de.symeda.sormas.ui.utils.FieldHelper;
import de.symeda.sormas.ui.utils.ViewConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;

public class FormFieldsEditForm extends AbstractEditForm<FormFieldsDto> {


    private boolean create;
    private ViewConfiguration viewConfiguration;

    private static final String HTML_LAYOUT = fluidRowLocs(FormFieldsDto.FORM_TYPE, FormFieldsDto.FIELD_NAME)
            + fluidRowLocs(FormFieldsDto.DESCRIPTION);

    public FormFieldsEditForm(boolean create) {
        super(FormFieldsDto.class, FormFieldsDto.I18N_PREFIX, false);
        this.create = create;

        viewConfiguration = ViewModelProviders.of(FormFieldsView.class).get(ViewConfiguration.class);

        setWidth(1024, Unit.PIXELS);

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
        addFields(FormFieldsDto.FIELD_NAME, FormFieldsDto.FORM_TYPE, FormFieldsDto.DESCRIPTION);
    }

    @Override
    public void setValue(FormFieldsDto newFieldValue) throws ReadOnlyException, Converter.ConversionException {
        super.setValue(newFieldValue);
    }
}
