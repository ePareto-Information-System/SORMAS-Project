package de.symeda.sormas.ui.configuration.infrastructure;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.infrastructure.InfrastructureType;
import de.symeda.sormas.api.infrastructure.cadre.CadreCriteria;
import de.symeda.sormas.api.infrastructure.community.CommunityCriteria;
import de.symeda.sormas.api.person.PersonCriteria;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.configuration.AbstractConfigurationView;
import de.symeda.sormas.ui.person.PersonsView;
import de.symeda.sormas.ui.utils.*;

public class AdditionalView extends AbstractConfigurationView {

    public static final String VIEW_NAME = ROOT_VIEW_NAME + "/additional";

    private GridLayout gridLayout;
    private Component component;
    private HorizontalLayout layout;
    private CadreGrid cadreGrid;
    private CadreCriteria criteria;
    private ViewConfiguration viewConfiguration;
    public AdditionalView() {
        super(VIEW_NAME);

        viewConfiguration = ViewModelProviders.of(CommunitiesView.class).get(ViewConfiguration.class);
        criteria = ViewModelProviders.of(CadreView.class).get(CadreCriteria.class, new CadreCriteria());

        if (criteria.getRelevanceStatus() == null) {
            criteria.relevanceStatus(EntityRelevanceStatus.ACTIVE);
        }

        layout = new HorizontalLayout();
        cadreGrid = new CadreGrid(criteria);
//        component = new CadreEditForm(true);
//        layout.addComponent(gridImportExportLayout());

        addComponent(layout());

    }

    public HorizontalLayout layout(){
        layout.addComponent(createButtonsComponent());
        return layout;
    }
    
    public VerticalLayout createButtonsComponent(){
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(createActionBtn());

        return verticalLayout;
    }

    public Button createActionBtn(){
        Button btnAction = ButtonHelper.createIconButton(Captions.cadre, VaadinIcons.USERS, e -> {
            if (component == null) {
                component = new CadreEditForm(true);
                layout.addComponent(gridImportExportLayout());
            }
        }, CssStyles.VSPACE_4, ValoTheme.BUTTON_PRIMARY);

        return btnAction;
    }

    public VerticalLayout gridVerticalLayout(Component gridComponent){
        VerticalLayout verticalLayout = new VerticalLayout();
        Component  gridLayout = gridComponent;

        verticalLayout.addComponent(gridLayout);

        return verticalLayout;

    }

    public HorizontalLayout gridImportExportLayout(){
        HorizontalLayout  importGridLayout = new HorizontalLayout();
        importGridLayout.addComponent(addImportExportLayout(Captions.addCadre, Strings.cadre, component));
//        importGridLayout.addComponent(gridVerticalLayout(""));
        return importGridLayout;
    }

    public HorizontalLayout addImportExportLayout(String caption, String captionHeading, Component component){
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.setMargin(false);
//        CssStyles.style(layout, CssStyles.VSPACE_TOP_NONE);

        Button btnAdd = ButtonHelper.createIconButton(caption, VaadinIcons.UPLOAD, e -> {
            Window window = VaadinUiUtil.showPopupWindow(component);
            window.setCaption(I18nProperties.getString(captionHeading));
        }, CssStyles.VSPACE_4, ValoTheme.BUTTON_PRIMARY);

        layout.addComponent(btnAdd);
//        layout.setComponentAlignment(btnAdd, Alignment.BOTTOM_LEFT);

        Button btnImport = ButtonHelper.createIconButton(Captions.actionImport, VaadinIcons.UPLOAD, e -> {
            Window window = VaadinUiUtil.showPopupWindow(new InfrastructureImportLayout(InfrastructureType.POPULATION_DATA));
            window.setCaption(I18nProperties.getString(Strings.headingImportPopulationData));
        }, CssStyles.VSPACE_4, ValoTheme.BUTTON_PRIMARY);

        layout.addComponent(btnImport);
        layout.setComponentAlignment(btnImport, Alignment.BOTTOM_RIGHT);

        Button btnExport = ButtonHelper.createIconButton(Captions.export, VaadinIcons.DOWNLOAD, null, ValoTheme.BUTTON_PRIMARY);
        layout.addComponent(btnExport);
        layout.setComponentAlignment(btnExport, Alignment.BOTTOM_RIGHT);

        StreamResource populationDataExportResource = DownloadUtil.createPopulationDataExportResource();
        new FileDownloader(populationDataExportResource).extend(btnExport);

        return layout;
    }
}
