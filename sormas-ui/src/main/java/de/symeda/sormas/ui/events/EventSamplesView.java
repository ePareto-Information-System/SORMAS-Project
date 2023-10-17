/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package de.symeda.sormas.ui.events;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.ComboBox;
import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.event.EventParticipantDto;
import de.symeda.sormas.api.event.EventParticipantReferenceDto;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.sample.SampleAssociationType;
import de.symeda.sormas.api.sample.SampleCriteria;
import de.symeda.sormas.api.sample.SampleDto;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.*;
import de.symeda.sormas.ui.externalmessage.ExternalMessagesView;
import de.symeda.sormas.ui.samples.CCESamplesGrid;
import de.symeda.sormas.ui.samples.SampleCreateForm;
import de.symeda.sormas.ui.utils.*;
import java.util.HashMap;

public class EventSamplesView extends AbstractDetailView<EventParticipantReferenceDto> {

    private static final long serialVersionUID = -1L;

    public static final String VIEW_NAME = EventParticipantsView.VIEW_NAME + "/samples";

    private CommitDiscardWrapperComponent<SampleCreateForm> editComponent;
    private SampleCriteria criteria;
    private final ViewConfiguration viewConfiguration;
    private HashMap<Button, String> statusButtons;
    private Button activeStatusButton;
    private ComboBox relevanceStatusFilter;
    private ComboBox sampleTypeFilter;
    private static final String NOT_SHIPPED = "notShipped";
    private static final String SHIPPED = "shipped";
    private static final String RECEIVED = "received";
    private static final String REFERRED = "referred";
    private CCESamplesGrid grid;
    private DetailSubComponentWrapper gridLayout;


    public EventSamplesView() {

        super(VIEW_NAME);

        setSizeFull();

        viewConfiguration = ViewModelProviders.of(getClass()).get(ViewConfiguration.class);
        viewConfiguration.setInEagerMode(true);
        criteria = ViewModelProviders.of(EventSamplesView.class).get(SampleCriteria.class);
    }

    @Override
    protected EventParticipantReferenceDto getReferenceByUuid(String uuid) {
        final EventParticipantReferenceDto reference;
        if (FacadeProvider.getEventParticipantFacade().exists(uuid)) {
            reference = FacadeProvider.getEventParticipantFacade().getReferenceByUuid(uuid);
        } else {
            reference = null;
        }
        return reference;
    }

    @Override
    protected String getRootViewName() {
        return EventParticipantsView.VIEW_NAME;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
        initOrRedirect(event);
    }

    @Override
    protected void initView(String params) {
        criteria.eventParticipant(getReference());
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
            setSubComponent(gridLayout);
        }

        if (params.startsWith("?")) {
            params = params.substring(1);
            criteria.fromUrlParams(params);
        }
        updateFilterComponents();
        grid.reload();
    }

    @Override
    public void refreshMenu(SubMenu menu, String params) {

        if (!findReferenceByParams(params)) {
            return;
        }

        EventParticipantDto eventParticipantDto = FacadeProvider.getEventParticipantFacade().getEventParticipantByUuid(getReference().getUuid());

        menu.removeAllViews();
        menu.addView(
                EventParticipantsView.VIEW_NAME,
                I18nProperties.getCaption(Captions.eventEventParticipants),
                eventParticipantDto.getEvent().getUuid(),
                true);


        if (FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.EXTERNAL_MESSAGES)
                && UserProvider.getCurrent().hasUserRight(UserRight.EXTERNAL_MESSAGE_VIEW)
                && FacadeProvider.getExternalMessageFacade().existsExternalMessageForEntity(getReference())) {
            menu.addView(ExternalMessagesView.VIEW_NAME, I18nProperties.getCaption(Captions.externalMessagesList));
        }

        menu.addView(EventParticipantDataView.VIEW_NAME, I18nProperties.getCaption(EventParticipantDto.I18N_PREFIX), params);

        if (UserProvider.getCurrent().hasUserRight(UserRight.SAMPLE_VIEW)) {
            menu.addView(EventSamplesView.VIEW_NAME, I18nProperties.getCaption(Captions.eventEventParticipantsSamples), params);
        }
        setMainHeaderComponent(ControllerProvider.getEventParticipantController().getEventParticipantViewTitleLayout(eventParticipantDto));
    }

    @Override
    protected void setSubComponent(DirtyStateComponent newComponent) {
        super.setSubComponent(newComponent);

        EventParticipantDto eventParticipant = FacadeProvider.getEventParticipantFacade().getEventParticipantByUuid(getReference().getUuid());
        if (eventParticipant.isDeleted()) {
            newComponent.setEnabled(false);
        }

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

            // Bulk operation dropdown
            if (UserProvider.getCurrent().hasUserRight(UserRight.PERFORM_BULK_OPERATIONS_CASE_SAMPLES)) {
                shipmentFilterLayout.setWidth(100, Unit.PERCENTAGE);

                MenuBar bulkOperationsDropdown = MenuBarHelper.createDropDown(
                        Captions.bulkActions,
                        new MenuBarHelper.MenuBarItem(I18nProperties.getCaption(Captions.bulkDelete), VaadinIcons.TRASH, selectedItem -> {
                            ControllerProvider.getSampleController().deleteAllSelectedItems(grid.asMultiSelect().getSelectedItems(), new Runnable() {

                                public void run() {
                                    navigateTo(criteria);
                                }
                            });
                        }));

                bulkOperationsDropdown.setVisible(viewConfiguration.isInEagerMode());

                actionButtonsLayout.addComponent(bulkOperationsDropdown);
            }

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

        final Button newButton = ButtonHelper.createIconButtonWithCaption(
                Captions.sampleNewSample,
                I18nProperties.getPrefixCaption(SampleDto.I18N_PREFIX, Captions.sampleNewSample),
                VaadinIcons.PLUS_CIRCLE,
                e -> ControllerProvider.getSampleController().create(criteria.getContact(), criteria.getDisease(), SormasUI::refreshView),
                ValoTheme.BUTTON_PRIMARY);

        actionButtonsLayout.addComponent(newButton);

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
                    .setCaption(statusButtons.get(activeStatusButton) + LayoutUtil.spanCss(CssStyles.BADGE, String.valueOf(grid.getItemCount())));
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
}