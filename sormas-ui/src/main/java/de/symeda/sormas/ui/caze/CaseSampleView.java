package de.symeda.sormas.ui.caze;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.ComboBox;
import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.caze.CaseIndexDto;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.sample.SampleAssociationType;
import de.symeda.sormas.api.sample.SampleCriteria;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.sample.SampleIndexDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.SormasUI;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.samples.CCESamplesGrid;
import de.symeda.sormas.ui.samples.SampleCreateForm;
import de.symeda.sormas.ui.samples.SamplesView;
import de.symeda.sormas.ui.utils.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class CaseSampleView extends AbstractCaseView {
    public static final String VIEW_NAME = ROOT_VIEW_NAME + "/samples";
    private CommitDiscardWrapperComponent<SampleCreateForm> editComponent;
    private SampleCriteria criteria;
    private final ViewConfiguration viewConfiguration;
    private HashMap<Button, String> statusButtons;
    private Button activeStatusButton;
    private ComboBox relevanceStatusFilter;
    private ComboBox sampleTypeFilter;
    private static final String NOT_SHIPPED = "notShipped";
    private static final String SHIPPED = "shipped";
    private static final String RECEIVED = "receiveds";
    private static final String REFERRED = "referred";
    private CCESamplesGrid grid;
    private DetailSubComponentWrapper gridLayout;
    private Button btnEnterBulkEditMode;

    public CaseSampleView() {
        super(VIEW_NAME, false);
        setSizeFull();

        viewConfiguration = ViewModelProviders.of(getClass()).get(ViewConfiguration.class);
        viewConfiguration.setInEagerMode(true);
        criteria = ViewModelProviders.of(CaseSampleView.class).get(SampleCriteria.class);
    }

    @Override
    protected void initView(String params) {
        criteria.caze(getCaseRef());

        if (grid == null) {
            grid = new CCESamplesGrid(criteria);
            gridLayout = new DetailSubComponentWrapper(() -> null);
            gridLayout.addComponent(createFilterBar());
            gridLayout.addComponent(createShipmentFilterBar());
            gridLayout.addComponent(grid);
            gridLayout.setMargin(true);
            gridLayout.setSpacing(false);
            gridLayout.setSizeFull();
            gridLayout.setExpandRatio(grid, 1);

            if (viewConfiguration.isInEagerMode()) {
                grid.setEagerDataProvider();
            }

            grid.getDataProvider().addDataProviderListener(e -> updateStatusButtons());
            gridLayout.addComponent(getNextTabButton());
            setSubComponent(gridLayout);
        }

        if (params.startsWith("?")) {
            params = params.substring(1);
            criteria.fromUrlParams(params);
        }

        updateFilterComponents();

        grid.reload();
//        setCaseEditPermission(gridLayout);
    }

    public HorizontalLayout createFilterBar() {
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setSpacing(true);
        topLayout.setSizeUndefined();

        return topLayout;
    }

    public HorizontalLayout createShipmentFilterBar() {
        HorizontalLayout shipmentFilterLayout = new HorizontalLayout();
        shipmentFilterLayout.setMargin(false);
        shipmentFilterLayout.setSpacing(true);
        shipmentFilterLayout.setWidth(100, Unit.PERCENTAGE);
        shipmentFilterLayout.addStyleName(CssStyles.VSPACE_3);

        statusButtons = new HashMap<>();

        HorizontalLayout buttonFilterLayout = new HorizontalLayout();
        buttonFilterLayout.setSpacing(true);
        {
            Button statusAll =
                    ButtonHelper.createButton(Captions.all, e -> processStatusChange(null), ValoTheme.BUTTON_BORDERLESS, CssStyles.BUTTON_FILTER);
            statusAll.setCaptionAsHtml(true);

            buttonFilterLayout.addComponent(statusAll);

            statusButtons.put(statusAll, I18nProperties.getCaption(Captions.all));
            activeStatusButton = statusAll;

            createAndAddStatusButton(Captions.sampleNotShipped, NOT_SHIPPED, buttonFilterLayout);
            createAndAddStatusButton(Captions.sampleShipped, SHIPPED, buttonFilterLayout);
            createAndAddStatusButton(Captions.sampleReceived, RECEIVED, buttonFilterLayout);
            createAndAddStatusButton(Captions.sampleReferred, REFERRED, buttonFilterLayout);
        }

        shipmentFilterLayout.addComponent(buttonFilterLayout);

        HorizontalLayout actionButtonsLayout = new HorizontalLayout();
        actionButtonsLayout.setSpacing(true);
        {
            // Show active/archived/all dropdown
            if (UserProvider.getCurrent().hasUserRight(UserRight.SAMPLE_VIEW)) {
                relevanceStatusFilter = ComboBoxHelper.createComboBoxV7();
                relevanceStatusFilter.setId("relevanceStatusFilter");
                relevanceStatusFilter.setWidth(140, Unit.PERCENTAGE);
                relevanceStatusFilter.setNullSelectionAllowed(false);
                relevanceStatusFilter.addItems((Object[]) EntityRelevanceStatus.values());
                relevanceStatusFilter.setItemCaption(EntityRelevanceStatus.ACTIVE, I18nProperties.getCaption(Captions.sampleActiveSamples));
                relevanceStatusFilter.setItemCaption(EntityRelevanceStatus.ARCHIVED, I18nProperties.getCaption(Captions.sampleArchivedSamples));
                relevanceStatusFilter.setItemCaption(EntityRelevanceStatus.ALL, I18nProperties.getCaption(Captions.sampleAllSamples));
                relevanceStatusFilter.addValueChangeListener(e -> {
                    criteria.relevanceStatus((EntityRelevanceStatus) e.getProperty().getValue());
                    navigateTo(criteria);
                });
                actionButtonsLayout.addComponent(relevanceStatusFilter);
            }


            final Button newButton = ButtonHelper.createIconButtonWithCaption(
                    Captions.sampleNewSample,
                    I18nProperties.getPrefixCaption(SampleDto.I18N_PREFIX, Captions.sampleNewSample),
                    VaadinIcons.PLUS_CIRCLE,
                    e -> ControllerProvider.getSampleController().create(criteria.getCaze(), criteria.getDisease(), SormasUI::refreshView),
                    ValoTheme.BUTTON_PRIMARY);
            addHeaderComponentSample(newButton);

            sampleTypeFilter = ComboBoxHelper.createComboBoxV7();
            sampleTypeFilter.setWidth(140, Unit.PERCENTAGE);
            sampleTypeFilter.setId("sampleTypeFilter");
            sampleTypeFilter.setNullSelectionAllowed(false);
            sampleTypeFilter.addItems((Object[]) SampleAssociationType.values());
            sampleTypeFilter.setItemCaption(SampleAssociationType.ALL, I18nProperties.getEnumCaption(SampleAssociationType.ALL));
            sampleTypeFilter.setItemCaption(SampleAssociationType.CASE, I18nProperties.getEnumCaption(SampleAssociationType.CASE));
            sampleTypeFilter.setItemCaption(SampleAssociationType.CONTACT, I18nProperties.getEnumCaption(SampleAssociationType.CONTACT));
            sampleTypeFilter
                    .setItemCaption(SampleAssociationType.EVENT_PARTICIPANT, I18nProperties.getEnumCaption(SampleAssociationType.EVENT_PARTICIPANT));
            sampleTypeFilter.addValueChangeListener(e -> {
                criteria.sampleAssociationType(((SampleAssociationType) e.getProperty().getValue()));
                navigateTo(criteria);
            });
            actionButtonsLayout.addComponent(sampleTypeFilter);
        }



        shipmentFilterLayout.addComponent(actionButtonsLayout);
        shipmentFilterLayout.setComponentAlignment(actionButtonsLayout, Alignment.TOP_RIGHT);
        shipmentFilterLayout.setExpandRatio(actionButtonsLayout, 1);

        return shipmentFilterLayout;
    }

    private void processStatusChange(String status) {
        if (NOT_SHIPPED.equals(status)) {
            criteria.shipped(false);
            criteria.received(null);
            criteria.referred(null);
        } else if (SHIPPED.equals(status)) {
            criteria.shipped(true);
            criteria.received(null);
            criteria.referred(null);
        } else if (RECEIVED.equals(status)) {
            criteria.shipped(null);
            criteria.received(true);
            criteria.referred(null);
        } else if (REFERRED.equals(status)) {
            criteria.shipped(null);
            criteria.received(null);
            criteria.referred(true);
        } else {
            criteria.shipped(null);
            criteria.received(null);
            criteria.referred(null);
        }

        navigateTo(criteria);
    }

    private void createAndAddStatusButton(String captionKey, String status, HorizontalLayout filterLayout) {
        Button button = ButtonHelper.createButton(
                captionKey,
                e -> processStatusChange(status),
                ValoTheme.BUTTON_BORDERLESS,
                CssStyles.BUTTON_FILTER,
                CssStyles.BUTTON_FILTER_LIGHT);

        button.setData(status);
        button.setCaptionAsHtml(true);

        filterLayout.addComponent(button);

        statusButtons.put(button, button.getCaption());
    }

    private void updateStatusButtons() {
        statusButtons.keySet().forEach(b -> {
            CssStyles.style(b, CssStyles.BUTTON_FILTER_LIGHT);
            b.setCaption(statusButtons.get(b));
            if ((NOT_SHIPPED.equals(b.getData()) && criteria.getShipped() == Boolean.FALSE)
                    || (SHIPPED.equals(b.getData()) && criteria.getShipped() == Boolean.TRUE)
                    || (RECEIVED.equals(b.getData()) && criteria.getReceived() == Boolean.TRUE)
                    || (REFERRED.equals(b.getData()) && criteria.getReferred() == Boolean.TRUE)) {
                activeStatusButton = b;
            }
        });
        CssStyles.removeStyles(activeStatusButton, CssStyles.BUTTON_FILTER_LIGHT);
        if (activeStatusButton != null) {
            activeStatusButton
                    .setCaption(statusButtons.get(activeStatusButton) + LayoutUtil.spanCss(CssStyles.BADGE, String.valueOf(grid.getDataSize())));
        }
    }

    public void updateFilterComponents() {

        applyingCriteria = true;

        updateStatusButtons();

        if (sampleTypeFilter != null) {
            sampleTypeFilter.setValue(criteria.getSampleAssociationType());
        }

        if (relevanceStatusFilter != null) {
            relevanceStatusFilter.setValue(criteria.getRelevanceStatus());
        }

        applyingCriteria = false;
    }

    private Set<String> getSelectedRows() {
        CCESamplesGrid caseGrid = (CCESamplesGrid) this.grid;
        return this.viewConfiguration.isInEagerMode()
                ? caseGrid.asMultiSelect().getSelectedItems().stream().map(SampleIndexDto::getUuid).collect(Collectors.toSet())
                : Collections.emptySet();
    }
}