package de.symeda.sormas.ui.configuration.infrastructure;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.data.sort.SortDirection;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.infrastructure.forms.FormBuilderCriteria;
import de.symeda.sormas.api.infrastructure.forms.FormBuilderIndexDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.utils.FilteredGrid;
import de.symeda.sormas.ui.utils.ViewConfiguration;

import java.util.stream.Collectors;

public class FormBuilderGrid extends FilteredGrid<FormBuilderIndexDto, FormBuilderCriteria> {
    private static final long serialVersionUID = 4488941182432777837L;


    public FormBuilderGrid(FormBuilderCriteria criteria) {
        super(FormBuilderIndexDto.class);
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
                FormBuilderIndexDto.UUID,
                FormBuilderIndexDto.DISEASE,
                FormBuilderIndexDto.FORM_TYPE

        );

        if (UserProvider.getCurrent().hasUserRight(UserRight.INFRASTRUCTURE_EDIT)) {

            addEditColumn(e -> {
                ControllerProvider.getInfrastructureController().editForm(e.getUuid());
            });
		}

		for (Column<?, ?> column : getColumns()) {
			column.setCaption(I18nProperties.getPrefixCaption(FormBuilderIndexDto.I18N_PREFIX, column.getId(), column.getCaption()));
		}
    }

    public void setLazyDataProvider() {
        DataProvider<FormBuilderIndexDto, FormBuilderCriteria> dataProvider = DataProvider.fromFilteringCallbacks(
                query -> FacadeProvider.getFormBuilderFacade()
                        .getIndexList(
                                query.getFilter().orElse(null),
                                query.getOffset(),
                                query.getLimit(),
                                query.getSortOrders()
                                        .stream()
                                        .map(sortOrder -> new SortProperty(sortOrder.getSorted(), sortOrder.getDirection() == SortDirection.ASCENDING))
                                        .collect(Collectors.toList()))
                        .stream(),
                query -> (int) FacadeProvider.getFormBuilderFacade().count(query.getFilter().orElse(null)));
        setDataProvider(dataProvider);
        setSelectionMode(SelectionMode.NONE);
    }

    public void setEagerDataProvider() {

        ListDataProvider<FormBuilderIndexDto> dataProvider =
                DataProvider.fromStream(FacadeProvider.getFormBuilderFacade().getIndexList(getCriteria(), null, null, null).stream());
        setDataProvider(dataProvider);
    }

    public void reload() {
        getDataProvider().refreshAll();
    }
}
