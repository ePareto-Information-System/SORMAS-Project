package de.symeda.sormas.ui.configuration.infrastructure;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.InfrastructureType;
import de.symeda.sormas.api.infrastructure.diseasecon.DiseaseConCriteria;
import de.symeda.sormas.api.infrastructure.fields.FormFieldsCriteria;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.configuration.AbstractConfigurationView;
import de.symeda.sormas.ui.utils.*;


public class FormFieldsView extends AbstractConfigurationView {

    public static final String VIEW_NAME = ROOT_VIEW_NAME + "/formFields";
    private FormFieldsGrid grid;
    protected Button importButton;
    protected Button createButton;
    private FormFieldsCriteria criteria;
    private ViewConfiguration viewConfiguration;

    private VerticalLayout gridLayout;
    private HorizontalLayout filterLayout;

    ComboBox formTypeFilter;

    private Button resetButton;
    private RowCount rowCount;

    public FormFieldsView() {
        super(VIEW_NAME);

        viewConfiguration = ViewModelProviders.of(getClass()).get(ViewConfiguration.class);
        criteria = ViewModelProviders.of(FormFieldsView.class).get(FormFieldsCriteria.class);
//        if (criteria.getFormType() == null) {
//            criteria.getFormType();
//        }

        grid = new FormFieldsGrid(criteria);
        gridLayout = new VerticalLayout();
        gridLayout.addComponent(createFilterBar());
        rowCount = new RowCount(Strings.labelNumberOfDiseases, grid.getDataSize());
        grid.addDataSizeChangeListener(e -> rowCount.update(grid.getDataSize()));
//        gridLayout.addComponent(rowCount);
        gridLayout.addComponent(grid);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(false);
		gridLayout.setExpandRatio(grid, 1);
		gridLayout.setSizeFull();
		gridLayout.setStyleName("crud-main-layout");

        addComponent(gridLayout);

        importButton = ButtonHelper.createIconButton(Captions.actionImport, VaadinIcons.UPLOAD, e -> {
            Window window = VaadinUiUtil.showPopupWindow(new InfrastructureImportLayout(InfrastructureType.FORM_FIELD));
            window.setCaption(I18nProperties.getString(Strings.entityFormFields));
            window.addCloseListener(c -> grid.reload());
        }, ValoTheme.BUTTON_PRIMARY);

        addHeaderComponent(importButton);

        createButton = ButtonHelper.createIconButtonWithCaption(
                "create",
                I18nProperties.getCaption(Captions.actionNewEntry),
                VaadinIcons.PLUS_CIRCLE,
                e -> ControllerProvider.getInfrastructureController().createField(),
                ValoTheme.BUTTON_PRIMARY);
        addHeaderComponent(createButton);



    }

    private HorizontalLayout createFilterBar() {

        filterLayout = new HorizontalLayout();
        filterLayout.setMargin(false);
        filterLayout.setSpacing(true);
        filterLayout.setWidth(450, Unit.PIXELS);

        formTypeFilter = new ComboBox();
        formTypeFilter.setId("formTypeFilter");
        formTypeFilter.setCaption(I18nProperties.getCaption(Captions.Configuration_FormType));
        formTypeFilter.setWidth(300, Unit.PIXELS);
        formTypeFilter.setItems(FacadeProvider.getDiseaseConfigurationFacade().getAllActiveDiseases());
        formTypeFilter.setItemCaptionGenerator(new ItemCaptionGenerator<Disease>() {
            @Override
            public String apply(Disease item) {
                return Disease.valueOf(item.getName()).toShortString() + " (" + item.getName() + ")";
            }
        });
        formTypeFilter.addValueChangeListener(event -> {
            criteria.setFormType((FormType) event.getValue());
            grid.reload();
            rowCount.update(grid.getDataSize());
            updateFilterComponents();
        });

        filterLayout.addComponent(formTypeFilter);

        resetButton = ButtonHelper.createButton(Captions.actionResetFilters, event -> {
            ViewModelProviders.of(FormFieldsView.class).remove(FormFieldsCriteria.class);
            navigateTo(null);
        }, CssStyles.FORCE_CAPTION);
        resetButton.setVisible(false);

        filterLayout.addComponent(resetButton);

        return filterLayout;
    }

    @Override
    public void enter(ViewChangeEvent event) {
        super.enter(event);
        String params = event.getParameters().trim();
        if (params.startsWith("?")) {
            params = params.substring(1);
            criteria.fromUrlParams(params);
        }
        updateFilterComponents();
        grid.reload();
        rowCount.update(grid.getDataSize());
    }

    private void updateFilterComponents() {
        applyingCriteria = true;
        formTypeFilter.setValue(criteria.getFormType());
        resetButton.setVisible(criteria.hasAnyFilterActive());
        rowCount.update(grid.getDataSize());
        applyingCriteria = false;
    }
}
