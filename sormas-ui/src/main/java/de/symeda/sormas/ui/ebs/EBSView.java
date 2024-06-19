package de.symeda.sormas.ui.ebs;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.OptionGroup;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.ebs.EbsCriteria;
import de.symeda.sormas.api.ebs.EbsGroupCriteria;
import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.*;
import de.symeda.sormas.ui.events.*;
import de.symeda.sormas.ui.events.importer.EventImportLayout;
import de.symeda.sormas.ui.utils.*;
import de.symeda.sormas.ui.utils.components.popupmenu.PopupMenu;
import org.vaadin.hene.popupbutton.PopupButton;

import java.util.*;

public class EBSView extends AbstractView {
    private static final long serialVersionUID = -3048977745713631500L;
    public static final String VIEW_NAME = "ebs";
    private EbsCriteria ebsCriteria;
    private EbsGroupCriteria ebsGroupCriteria;
    private EbsViewConfiguration viewConfiguration;
    public static String currentview = "signallist";

    private FilteredGrid<?, ?> grid;
    private Button createButton;
    private HashMap<Button, String> srcButtons;
    private Button activeStatusButton;
    // Filter
    private EbsFilterForm ebsFilterForm;
    private ComboBox contactCountMethod;

    private VerticalLayout gridLayout;

    public EBSView() {
        super(VIEW_NAME);

        viewConfiguration = ViewModelProviders.of(getClass()).get(EbsViewConfiguration.class);
        if (viewConfiguration.getViewType() == null) {
            viewConfiguration.setViewType(EbsViewType.DEFAULT);
        }

        ebsCriteria = ViewModelProviders.of(EBSView.class).get(EbsCriteria.class);
        if (isDefaultViewType()) {
            grid = new EbsSignalGrid(ebsCriteria, getClass());
            currentview = "signallist";
        } else {
            grid = new EbsGrid(ebsCriteria, getClass());
            currentview = "eventlist";
        }
        gridLayout = new VerticalLayout();
        gridLayout.addComponent(createFilterBar());
        gridLayout.addComponent(grid);
        gridLayout.setMargin(true);
        gridLayout.setSpacing(false);
        gridLayout.setSizeFull();
        gridLayout.setExpandRatio(grid, 1);
//        gridLayout.setStyleName("crud-main-layout");
        addComponent(gridLayout);

        OptionGroup ebsViewSwitcher = new OptionGroup();
        ebsViewSwitcher.setId("ebsViewSwitcher");
        CssStyles.style(
                ebsViewSwitcher,
                CssStyles.FORCE_CAPTION,
                ValoTheme.OPTIONGROUP_HORIZONTAL,
                CssStyles.OPTIONGROUP_HORIZONTAL_PRIMARY,
                CssStyles.VSPACE_TOP_3);
        ebsViewSwitcher.addItem(EbsViewType.DEFAULT);
        ebsViewSwitcher.setItemCaption(EbsViewType.DEFAULT, I18nProperties.getCaption(Captions.ebsSignalView));

        ebsViewSwitcher.addItem(EbsViewType.EVENT);
        ebsViewSwitcher.setItemCaption(EbsViewType.EVENT, I18nProperties.getCaption(Captions.ebsEventView));

        ebsViewSwitcher.setValue(viewConfiguration.getViewType());
        ebsViewSwitcher.addValueChangeListener(e -> {
            EbsViewType viewType = (EbsViewType) e.getProperty().getValue();

            viewConfiguration.setViewType(viewType);
            SormasUI.get().getNavigator().navigateTo(EBSView.VIEW_NAME);
        });
        addHeaderComponent(ebsViewSwitcher);


            Button importButton = ButtonHelper.createIconButton(Captions.actionImport, VaadinIcons.UPLOAD, e -> {
                Window popupWindow = VaadinUiUtil.showPopupWindow(new EventImportLayout());
                popupWindow.setCaption(I18nProperties.getString(Strings.headingImportEvent));
                popupWindow.addCloseListener(c -> ((EventGrid) grid).reload());
            }, ValoTheme.BUTTON_PRIMARY);

            addHeaderComponent(importButton);


        if (UserProvider.getCurrent().hasUserRight(UserRight.EVENT_EXPORT)) {
            VerticalLayout exportLayout = new VerticalLayout();
            {
                exportLayout.setSpacing(true);
                exportLayout.setMargin(true);
                exportLayout.addStyleName(CssStyles.LAYOUT_MINIMAL);
                exportLayout.setWidth(250, Unit.PIXELS);
            }

            PopupButton exportPopupButton = ButtonHelper.createIconPopupButton(Captions.export, VaadinIcons.DOWNLOAD, exportLayout);
            addHeaderComponent(exportPopupButton);

            {
                StreamResource streamResource = GridExportStreamResource.createStreamResourceWithSelectedItems(
                        grid,
                        () -> isDefaultViewType() && this.viewConfiguration.isInEagerMode()
                                ? this.grid.asMultiSelect().getSelectedItems()
                                : Collections.emptySet(),
                        ExportEntityName.EBS);
                addExportButton(streamResource, exportPopupButton, exportLayout, VaadinIcons.TABLE, Captions.exportBasic, Strings.infoBasicExport);
            }
        }


            createButton = ButtonHelper.createIconButton(
                    Captions.ebsCreatEbs,
                    VaadinIcons.PLUS_CIRCLE,
                    e -> ControllerProvider.getEbsController().create(),
                    ValoTheme.BUTTON_PRIMARY);

            addHeaderComponent(createButton);

        final PopupMenu moreButton = new PopupMenu(I18nProperties.getCaption(Captions.moreActions));

        if (UserProvider.getCurrent().hasUserRight(UserRight.PERFORM_BULK_OPERATIONS_EVENT)) {
            Button btnEnterBulkEditMode = ButtonHelper.createIconButton(Captions.actionEnterBulkEditMode, VaadinIcons.CHECK_SQUARE_O, null);
            {
                btnEnterBulkEditMode.setVisible(!viewConfiguration.isInEagerMode());
                btnEnterBulkEditMode.addStyleName(ValoTheme.BUTTON_PRIMARY);
                btnEnterBulkEditMode.setWidth(100, Unit.PERCENTAGE);
                moreButton.addMenuEntry(btnEnterBulkEditMode);
            }

            Button btnLeaveBulkEditMode =
                    ButtonHelper.createIconButton(Captions.actionLeaveBulkEditMode, VaadinIcons.CLOSE, null, ValoTheme.BUTTON_PRIMARY);
            {
                btnLeaveBulkEditMode.setVisible(viewConfiguration.isInEagerMode());
                btnLeaveBulkEditMode.setWidth(100, Unit.PERCENTAGE);
                moreButton.addMenuEntry(btnLeaveBulkEditMode);
            }
            if (isDefaultViewType()) {
                btnEnterBulkEditMode.addClickListener(e -> {
                    ViewModelProviders.of(EBSView.class).get(EbsViewConfiguration.class).setInEagerMode(true);
                    btnEnterBulkEditMode.setVisible(false);
                    btnLeaveBulkEditMode.setVisible(true);
                    ((EbsSignalGrid) grid).reload();
                });
            }else {
                btnEnterBulkEditMode.addClickListener(e -> {
                            ViewModelProviders.of(EBSView.class).get(EbsViewConfiguration.class).setInEagerMode(true);
                            btnEnterBulkEditMode.setVisible(false);
                            btnLeaveBulkEditMode.setVisible(true);
                            ((EbsGrid) grid).reload();
            });
            }
            btnLeaveBulkEditMode.addClickListener(e -> {
                ViewModelProviders.of(EBSView.class).get(EbsViewConfiguration.class).setInEagerMode(false);
                btnLeaveBulkEditMode.setVisible(false);
                btnEnterBulkEditMode.setVisible(true);
                navigateTo(ebsCriteria);
            });
        }
            Button searchSpecificEbsButton = ButtonHelper.createIconButton(Captions.eventSearchSpecificEvent, VaadinIcons.SEARCH, e -> {
                buildAndOpenSearchSpecificEbsWindow();
                moreButton.setPopupVisible(false);
            }, ValoTheme.BUTTON_PRIMARY);
            searchSpecificEbsButton.setWidth(100, Unit.PERCENTAGE);
            moreButton.addMenuEntry(searchSpecificEbsButton);


        if (moreButton.hasMenuEntries()) {
            addHeaderComponent(moreButton);
        }
    }

    private boolean isDefaultViewType() {
        return viewConfiguration.getViewType() == EbsViewType.DEFAULT;
    }

    private void buildAndOpenSearchSpecificEbsWindow() {
        Window window = VaadinUiUtil.createPopupWindow();
        window.setCaption(I18nProperties.getCaption(Captions.eventSearchSpecificEvent));
        window.setWidth(768, Unit.PIXELS);

        SearchSpecificLayout layout = buildSearchSpecificLayout(window);
        window.setContent(layout);
        UI.getCurrent().addWindow(window);
    }

    private SearchSpecificLayout buildSearchSpecificLayout(Window window) {

        String description = I18nProperties.getString(Strings.infoSpecificEventSearch);
        String confirmCaption = I18nProperties.getCaption(Captions.eventSearchEvent);

        TextField searchField = new TextField();
        Runnable confirmCallback = () -> {
            String foundEbsUuid = FacadeProvider.getEbsFacade().getUuidByCaseUuidOrPersonUuid(searchField.getValue());

            if (foundEbsUuid != null) {
                ControllerProvider.getEbsController().navigateToData(foundEbsUuid);
                window.close();
            } else {
                VaadinUiUtil.showSimplePopupWindow(
                        I18nProperties.getString(Strings.headingNoEventFound),
                        I18nProperties.getString(Strings.messageNoEventFound));
            }
        };

        return new SearchSpecificLayout(confirmCallback, () -> window.close(), searchField, description, confirmCaption);
    }

    public void updateFilterComponents() {

        // TODO replace with Vaadin 8 databinding
        applyingCriteria = true;

        ebsFilterForm.setValue(ebsCriteria);

        applyingCriteria = false;
    }

    public HorizontalLayout createFilterBar() {
        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setSpacing(true);
        filterLayout.setMargin(false);
        filterLayout.setSizeUndefined();

        ebsFilterForm = new EbsFilterForm();
        ebsFilterForm.addValueChangeListener(e -> {
            if (!ebsFilterForm.hasFilter()) {
                navigateTo(null);
            }
        });
        ebsFilterForm.addResetHandler(e -> {
            ViewModelProviders.of(EBSView.class).remove(EbsCriteria.class);
            navigateTo(null);
        });

            ebsFilterForm.addApplyHandler(e -> {
                if (isDefaultViewType()) {
                    navigateTo(ebsCriteria);
                ((EbsSignalGrid) grid).reload();
                }else {
                    ((EbsGrid) grid).reload();
                    navigateTo(ebsCriteria);
                }
            });
        filterLayout.addComponent(ebsFilterForm);

        return filterLayout;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        String params = event.getParameters().trim();
        if (params.startsWith("?")) {
            params = params.substring(1);
        }
            if (isDefaultViewType()) {
                ((EbsSignalGrid) grid).setEagerDataProvider();

                updateFilterComponents();
                ((EbsSignalGrid) grid).reload();
            }else {
                ((EbsGrid) grid).setEagerDataProvider();

                updateFilterComponents();
                ((EbsGrid) grid).reload();
            }
    }

    public HorizontalLayout createStatusFilterBar() {

        HorizontalLayout srcFilterLayout = new HorizontalLayout();
        srcFilterLayout.setSpacing(true);
        srcFilterLayout.setMargin(false);
        srcFilterLayout.setWidth(100, Unit.PERCENTAGE);
        srcFilterLayout.addStyleName(CssStyles.VSPACE_3);
        srcButtons = new HashMap<>();

        if (isDefaultViewType()) {
            Button srcAll = ButtonHelper.createButton(Captions.all, e -> {
                ebsCriteria.setSourceInformation(null);
                ebsCriteria.setTriagingDecision(null);
                navigateTo(ebsCriteria);
            }, ValoTheme.BUTTON_BORDERLESS, CssStyles.BUTTON_FILTER);
            srcAll.setCaptionAsHtml(true);

            srcFilterLayout.addComponent(srcAll);

            srcButtons.put(srcAll, I18nProperties.getCaption(Captions.all));
            activeStatusButton = srcAll;

            for (EbsSourceType src : EbsSourceType.values()) {
                Button srcButton = ButtonHelper.createButton("status-" + src, src.toString(), e -> {
                    ebsCriteria.setSourceInformation(src);
                    navigateTo(ebsCriteria);
                }, ValoTheme.BUTTON_BORDERLESS, CssStyles.BUTTON_FILTER, CssStyles.BUTTON_FILTER_LIGHT);
                srcButton.setCaptionAsHtml(true);
                srcButton.setData(src);

                srcFilterLayout.addComponent(srcButton);

                srcButtons.put(srcButton, src.toString());
            }
        }

        return srcFilterLayout;

    }
}
