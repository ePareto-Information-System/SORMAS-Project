package de.symeda.sormas.ui.formbuilder;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.symeda.sormas.api.formbuilder.FormBuilderContext;
import de.symeda.sormas.api.formbuilder.FormBuilderCriteria;
import de.symeda.sormas.api.formbuilder.FormBuilderIndexDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.task.TaskIndexDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.task.TasksView;
import de.symeda.sormas.ui.utils.AbstractView;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.ViewConfiguration;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class FormBuildersView extends AbstractView {

    public static final String VIEW_NAME = "formbuilder";
    private final FormBuilderGridComponent formBuilderComponent;

    private ViewConfiguration viewConfiguration;

    public FormBuildersView(){

        super(VIEW_NAME);

        if(!ViewModelProviders.of(FormBuildersView.class).has(FormBuilderCriteria.class)){

            FormBuilderCriteria formBuilderCriteria = new FormBuilderCriteria();
            ViewModelProviders.of(FormBuildersView.class).get(FormBuilderCriteria.class, formBuilderCriteria);
        }

        viewConfiguration = ViewModelProviders.of(getClass()).get(ViewConfiguration.class);
        formBuilderComponent = new FormBuilderGridComponent(getViewTitleLabel(), this);
        addComponent(formBuilderComponent);


        if (UserProvider.getCurrent().hasUserRight(UserRight.CASE_CREATE)) {
            VerticalLayout exportLayout = new VerticalLayout();
            exportLayout.setSpacing(true);
            exportLayout.setMargin(true);
            exportLayout.addStyleName(CssStyles.LAYOUT_MINIMAL);
            exportLayout.setWidth(200, Unit.PIXELS);

            Button createButton = ButtonHelper.createIconButton(
                    Captions.formBuilderNew,
                    VaadinIcons.PLUS_CIRCLE,
                    e -> ControllerProvider.getFormBuilderController().create(FormBuilderContext.GENERAL, null, null, formBuilderComponent.getGrid()::reload),
                    ValoTheme.BUTTON_PRIMARY);

            addHeaderComponent(createButton);
        }
    }

    private Set<String> getSelectedRowUuids() {
        return viewConfiguration.isInEagerMode()
                ? formBuilderComponent.getGrid().asMultiSelect().getSelectedItems().stream().map(FormBuilderIndexDto::getUuid).collect(Collectors.toSet())
                : Collections.emptySet();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        formBuilderComponent.reload(event);
    }

    public ViewConfiguration getViewConfiguration() {
        return viewConfiguration;
    }










}
