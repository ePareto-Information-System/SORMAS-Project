package de.symeda.sormas.ui.configuration.infrastructure;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.InfrastructureType;
import de.symeda.sormas.api.infrastructure.diseasecon.DiseaseConCriteria;
import de.symeda.sormas.api.infrastructure.forms.FormBuilderCriteria;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.configuration.AbstractConfigurationView;
import de.symeda.sormas.ui.utils.*;


public class FormBuildersView extends AbstractConfigurationView {

    public static final String VIEW_NAME = ROOT_VIEW_NAME + "/forms";
    private FormBuilderGrid grid;
    protected Button createButton;
    protected Button importButton;
    private FormBuilderCriteria criteria;
    private ViewConfiguration viewConfiguration;

    private VerticalLayout gridLayout;
    private HorizontalLayout filterLayout;

    ComboBox disseaFilter;

    private Button resetButton;
    private RowCount rowCount;

    public FormBuildersView() {
        super(VIEW_NAME);

        viewConfiguration = ViewModelProviders.of(getClass()).get(ViewConfiguration.class);
        criteria = ViewModelProviders.of(FormBuildersView.class).get(FormBuilderCriteria.class);


        grid = new FormBuilderGrid(criteria);
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
            Window window = VaadinUiUtil.showPopupWindow(new InfrastructureImportLayout(InfrastructureType.FORM));
            window.setCaption(I18nProperties.getString(Strings.entityFormBuilder));
            window.addCloseListener(c -> grid.reload());
        }, ValoTheme.BUTTON_PRIMARY);

        // TODO:

//        addHeaderComponent(importButton);

        createButton = ButtonHelper.createIconButtonWithCaption(
                "create",
                I18nProperties.getCaption(Captions.actionNewEntry),
                VaadinIcons.PLUS_CIRCLE,
                e -> ControllerProvider.getInfrastructureController().createForm(),
                ValoTheme.BUTTON_PRIMARY);
        addHeaderComponent(createButton);

    }

    private HorizontalLayout createFilterBar() {

        filterLayout = new HorizontalLayout();
        filterLayout.setMargin(false);
        filterLayout.setSpacing(true);
        filterLayout.setWidth(450, Unit.PIXELS);

        disseaFilter = new ComboBox();
        disseaFilter.setId("diseaseFilter");
        disseaFilter.setCaption(I18nProperties.getCaption(Captions.Configuration_Disease));
        disseaFilter.setWidth(300, Unit.PIXELS);
        disseaFilter.setItems(FacadeProvider.getDiseaseConfigurationFacade().getAllActiveDiseases());
        disseaFilter.setItemCaptionGenerator(new ItemCaptionGenerator<Disease>() {
            @Override
            public String apply(Disease item) {
                return Disease.valueOf(item.getName()).toShortString() + " (" + item.getName() + ")";
            }
        });
        disseaFilter.addValueChangeListener(event -> {
            criteria.disease((Disease) event.getValue());
            grid.reload();
            rowCount.update(grid.getDataSize());
            updateFilterComponents();
        });

        filterLayout.addComponent(disseaFilter);

        resetButton = ButtonHelper.createButton(Captions.actionResetFilters, event -> {
            ViewModelProviders.of(FormBuildersView.class).remove(DiseaseConCriteria.class);
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
        disseaFilter.setValue(criteria.getDisease());
        resetButton.setVisible(criteria.hasAnyFilterActive());
        rowCount.update(grid.getDataSize());
        applyingCriteria = false;
    }
}
