package de.symeda.sormas.ui.configuration.infrastructure;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.data.sort.SortDirection;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.infrastructure.fields.FormFieldIndexDto;
import de.symeda.sormas.api.infrastructure.fields.FormFieldsCriteria;
import de.symeda.sormas.api.infrastructure.forms.FormBuilderIndexDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.utils.FilteredGrid;
import de.symeda.sormas.ui.utils.ViewConfiguration;

import java.util.stream.Collectors;

public class FormFieldsGrid extends FilteredGrid<FormFieldIndexDto, FormFieldsCriteria> {
    private static final long serialVersionUID = 4488941182432777837L;


    public FormFieldsGrid(FormFieldsCriteria criteria) {
        super(FormFieldIndexDto.class);
        setSizeFull();

        ViewConfiguration viewConfiguration = ViewModelProviders.of(FormBuildersView.class).get(ViewConfiguration.class);
        setInEagerMode(viewConfiguration.isInEagerMode());

        if (isInEagerMode() && UserProvider.getCurrent().hasUserRight(UserRight.PERFORM_BULK_OPERATIONS)) {
            setCriteria(criteria);
            setEagerDataProvider();
        } else {
            setLazyDataProvider();
            setCriteria(criteria);
        }


        setColumns(
                FormFieldIndexDto.UUID,
                FormFieldIndexDto.FORM_TYPE,
                FormFieldIndexDto.FIELD_NAME,
                FormFieldIndexDto.DESCRIPTION,
                FormFieldIndexDto.ACTIVE
        );

        if (UserProvider.getCurrent().hasUserRight(UserRight.INFRASTRUCTURE_EDIT)) {

            addEditColumn(e -> {
                ControllerProvider.getInfrastructureController().editFormField(e.getUuid());
            });
		}

		for (Column<?, ?> column : getColumns()) {
			column.setCaption(I18nProperties.getPrefixCaption(FormBuilderIndexDto.I18N_PREFIX, column.getId(), column.getCaption()));
		}
    }

    public void setLazyDataProvider() {
        DataProvider<FormFieldIndexDto, FormFieldsCriteria> dataProvider = DataProvider.fromFilteringCallbacks(
                query -> FacadeProvider.getFormFieldFacade()
                        .getIndexList(
                                query.getFilter().orElse(null),
                                query.getOffset(),
                                query.getLimit(),
                                query.getSortOrders()
                                        .stream()
                                        .map(sortOrder -> new SortProperty(sortOrder.getSorted(), sortOrder.getDirection() == SortDirection.ASCENDING))
                                        .collect(Collectors.toList()))
                        .stream(),
                query -> (int) FacadeProvider.getFormFieldFacade().count(query.getFilter().orElse(null)));
        setDataProvider(dataProvider);
        setSelectionMode(SelectionMode.NONE);
    }

    public void setEagerDataProvider() {

        ListDataProvider<FormFieldIndexDto> dataProvider =
                DataProvider.fromStream(FacadeProvider.getFormFieldFacade().getIndexList(getCriteria(), null, null, null).stream());
        setDataProvider(dataProvider);
    }

    public void reload() {
        getDataProvider().refreshAll();
    }
}
