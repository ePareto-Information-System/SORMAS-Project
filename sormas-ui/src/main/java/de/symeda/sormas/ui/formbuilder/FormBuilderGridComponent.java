package de.symeda.sormas.ui.formbuilder;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.ComboBox;
import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.formbuilder.FormBuilderCriteria;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.task.TaskCriteria;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.task.TaskGrid;
import de.symeda.sormas.ui.utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

public class FormBuilderGridComponent extends VerticalLayout {

    private FormBuilderCriteria criteria;

    private FormBuilderGrid grid;
    private FormBuildersView formBuildersView;
    private Map<Button, String> statusButtons;
    private Button activeStatusButton;

    // Filter
    private FormBuilderGridFilterForm filterForm;
    private ComboBox relevanceStatusFilter;

    MenuBar bulkOperationsDropdown;

    private Label viewTitleLabel;
    private String originalViewTitle;

    public FormBuilderGridComponent(Label viewTitleLabel, FormBuildersView formBuildersView) {
        setSizeFull();
        setMargin(false);

        this.viewTitleLabel = viewTitleLabel;
        this.formBuildersView = formBuildersView;
        originalViewTitle = viewTitleLabel.getValue();

        criteria = ViewModelProviders.of(FormBuildersView.class).get(FormBuilderCriteria.class);
        if (criteria.getRelevanceStatus() == null) {
            criteria.relevanceStatus(EntityRelevanceStatus.ACTIVE);
        }

        grid = new FormBuilderGrid(criteria);
        VerticalLayout gridLayout = new VerticalLayout();
        gridLayout.addComponent(createFilterBar());
        gridLayout.addComponent(createAssigneeFilterBar());
        gridLayout.addComponent(grid);
        //grid.getDataProvider().addDataProviderListener(e -> updateAssigneeFilterButtons());
       // grid.setDataProviderListener(e -> updateAssigneeFilterButtons());

        gridLayout.setMargin(true);
        styleGridLayout(gridLayout);

        addComponent(gridLayout);
    }

    public HorizontalLayout createFilterBar() {
        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setMargin(false);
        filterLayout.setSpacing(true);
        filterLayout.setSizeUndefined();

        filterForm = new FormBuilderGridFilterForm();
        filterForm.addResetHandler(e -> {
            ViewModelProviders.of(FormBuildersView.class).remove(FormBuilderCriteria.class);
            formBuildersView.navigateTo(null, true);
        });
        filterForm.addApplyHandler(e -> grid.reload());

        filterLayout.addComponent(filterForm);

        return filterLayout;
    }

    public HorizontalLayout createAssigneeFilterBar() {
        HorizontalLayout assigneeFilterLayout = new HorizontalLayout();
        assigneeFilterLayout.setMargin(false);
        assigneeFilterLayout.setSpacing(true);
        assigneeFilterLayout.setWidth(100, Unit.PERCENTAGE);
        assigneeFilterLayout.addStyleName(CssStyles.VSPACE_3);

        statusButtons = new HashMap<>();

        HorizontalLayout buttonFilterLayout = new HorizontalLayout();
        buttonFilterLayout.setSpacing(true);
        {
           /* Button allTasks =
                    ButtonHelper.createButton(Captions.all, e -> processAssigneeFilterChange(null), ValoTheme.BUTTON_BORDERLESS, CssStyles.BUTTON_FILTER);
            allTasks.setCaptionAsHtml(true);

            buttonFilterLayout.addComponent(allTasks);
            statusButtons.put(allTasks, I18nProperties.getCaption(Captions.all));*/


        }
        assigneeFilterLayout.addComponent(buttonFilterLayout);

        HorizontalLayout actionButtonsLayout = new HorizontalLayout();
        actionButtonsLayout.setSpacing(true);

        assigneeFilterLayout.addComponent(actionButtonsLayout);
        assigneeFilterLayout.setComponentAlignment(actionButtonsLayout, Alignment.TOP_RIGHT);
        assigneeFilterLayout.setExpandRatio(actionButtonsLayout, 1);

        return assigneeFilterLayout;
    }

    private void styleGridLayout(VerticalLayout gridLayout) {
        gridLayout.setSpacing(false);
        gridLayout.setSizeFull();
        gridLayout.setExpandRatio(grid, 1);
        gridLayout.setStyleName("crud-main-layout");
    }





    public void reload(ViewChangeListener.ViewChangeEvent event) {
        String params = event.getParameters().trim();
        if (params.startsWith("?")) {
            params = params.substring(1);
            criteria.fromUrlParams(params);
        }
        //updateFilterComponents();
        grid.reload();
    }

    public FormBuilderGrid getGrid() {
        return grid;
    }

    public MenuBar getBulkOperationsDropdown() {
        return bulkOperationsDropdown;
    }

    public FormBuilderCriteria getCriteria() {
        return criteria;
    }
}
