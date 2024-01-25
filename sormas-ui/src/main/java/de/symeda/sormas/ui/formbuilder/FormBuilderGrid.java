package de.symeda.sormas.ui.formbuilder;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.DataProviderListener;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.TextRenderer;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.formbuilder.FormBuilderCriteria;
import de.symeda.sormas.api.formbuilder.FormBuilderIndexDto;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.task.TaskCriteria;
import de.symeda.sormas.api.task.TaskIndexDto;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.task.TasksView;
import de.symeda.sormas.ui.utils.*;

import java.util.Date;
import java.util.stream.Collectors;

public class FormBuilderGrid extends FilteredGrid<FormBuilderIndexDto, FormBuilderCriteria> {

    private DataProviderListener<FormBuilderIndexDto> dataProviderListener;

    public FormBuilderGrid(FormBuilderCriteria criteria) {
        super(FormBuilderIndexDto.class);
        setSizeFull();

        ViewConfiguration viewConfiguration = ViewModelProviders.of(TasksView.class).get(ViewConfiguration.class);
        setInEagerMode(viewConfiguration.isInEagerMode());


           // setLazyDataProvider();
            //setCriteria(criteria);



       /* Grid.Column<FormBuilderIndexDto, ReferenceDto> contextColumn = (Grid.Column<FormBuilderIndexDto, ReferenceDto>) getColumn(FormBuilderIndexDto.CONTEXT_REFERENCE);
        contextColumn.setRenderer(new ReferenceDtoHtmlProvider(), new HtmlRenderer());
        contextColumn.setSortable(false);

        Grid.Column<FormBuilderIndexDto, UserReferenceDto> assigneeUserColumn = (Grid.Column<FormBuilderIndexDto, UserReferenceDto>) getColumn(FormBuilderIndexDto.ASSIGNEE_USER);
        assigneeUserColumn.setRenderer(user -> {
            String text;
            if (user != null) {
                text = user.getCaption();
            } else {
                text = "";
            }
            return text;
        }, new TextRenderer());*/


    }

    public void reload() {
        if (getSelectionModel().isUserSelectionAllowed()) {
            deselectAll();
        }

        if (ViewModelProviders.of(TasksView.class).get(ViewConfiguration.class).isInEagerMode()) {
           // setEagerDataProvider();
        }

        getDataProvider().refreshAll();
    }



  /*  public void setLazyDataProvider() {
        DataProvider<FormBuilderIndexDto, FormBuilderCriteria> dataProvider = DataProvider.fromFilteringCallbacks(
                query -> FacadeProvider.getTaskFacade()
                        .getIndexList(
                                query.getFilter().orElse(null),
                                query.getOffset(),
                                query.getLimit(),
                                query.getSortOrders()
                                        .stream()
                                        .map(sortOrder -> new SortProperty(sortOrder.getSorted(), sortOrder.getDirection() == SortDirection.ASCENDING))
                                        .collect(Collectors.toList()))
                        .stream(),
                query -> (int) FacadeProvider.getTaskFacade().count(query.getFilter().orElse(null)));
        setDataProvider(dataProvider);
        setSelectionMode(SelectionMode.NONE);
    }*/

   /* public void setEagerDataProvider() {
        ListDataProvider<TaskIndexDto> dataProvider =
                DataProvider.fromStream(FacadeProvider.getTaskFacade().getIndexList(getCriteria(), null, null, null).stream());
        setDataProvider(dataProvider);
        setSelectionMode(SelectionMode.MULTI);

        if (dataProviderListener != null) {
            dataProvider.addDataProviderListener(dataProviderListener);
        }
    }*/

    /*public void setDataProviderListener(DataProviderListener<TaskIndexDto> dataProviderListener) {
        this.dataProviderListener = dataProviderListener;
    }*/
}
