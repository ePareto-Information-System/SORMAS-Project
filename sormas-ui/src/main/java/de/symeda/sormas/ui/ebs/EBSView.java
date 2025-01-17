package de.symeda.sormas.ui.ebs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import de.symeda.sormas.api.EntityRelevanceStatus;
import de.symeda.sormas.api.common.CoreEntityType;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.feature.FeatureTypeProperty;
import de.symeda.sormas.ui.utils.ComboBoxHelper;
import de.symeda.sormas.ui.utils.LayoutUtil;
import org.vaadin.hene.popupbutton.PopupButton;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.OptionGroup;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.ebs.EbsCriteria;
import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.ui.ControllerProvider;
import de.symeda.sormas.ui.SearchSpecificLayout;
import de.symeda.sormas.ui.SormasUI;
import de.symeda.sormas.ui.UserProvider;
import de.symeda.sormas.ui.ViewModelProviders;
import de.symeda.sormas.ui.events.EventGrid;
import de.symeda.sormas.ui.events.importer.EventImportLayout;
import de.symeda.sormas.ui.utils.AbstractView;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.ExportEntityName;
import de.symeda.sormas.ui.utils.FilteredGrid;
import de.symeda.sormas.ui.utils.GridExportStreamResource;
import de.symeda.sormas.ui.utils.VaadinUiUtil;
import de.symeda.sormas.ui.utils.components.popupmenu.PopupMenu;

public class EBSView extends AbstractView {

	private static final long serialVersionUID = -3048977745713631500L;
	public static final String VIEW_NAME = "ebs";
	private final EbsCriteria ebsCriteria;
	private final EbsViewConfiguration viewConfiguration;
	public static String currentview = "signallist";

	private FilteredGrid<?, ?> grid;
	private Button createButton;
	private Button activeStatusButton;
	// Filter
	private EbsFilterForm ebsFilterForm;

	private VerticalLayout gridLayout;
	private HashMap<Button, String> statusButtons;
	private Label relevanceStatusInfoLabel;
	private ComboBox relevanceStatusFilter;

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
		gridLayout.addComponent(createStatusFilterBar());
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
					() -> this.viewConfiguration.isInEagerMode() ? this.grid.asMultiSelect().getSelectedItems() : Collections.emptySet(),
					ExportEntityName.EBS);
				addExportButton(streamResource, exportPopupButton, exportLayout, VaadinIcons.TABLE, Captions.exportBasic, Strings.infoBasicExport);
			}
			Button btnCustomExport = ButtonHelper.createIconButton(Captions.exportCustom, VaadinIcons.FILE_TEXT, e -> {
				ControllerProvider.getCustomExportController().openEbsExportWindow(ebsCriteria, this::getSelectedRowUuids);
				exportPopupButton.setPopupVisible(false);
			}, ValoTheme.BUTTON_PRIMARY);
			btnCustomExport.setDescription(I18nProperties.getString(Strings.infoCustomExport));
			btnCustomExport.setWidth(100, Unit.PERCENTAGE);
			exportLayout.addComponent(btnCustomExport);
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
			} else {
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

	public HorizontalLayout createStatusFilterBar() {
		HorizontalLayout statusFilterLayout = new HorizontalLayout();
		statusFilterLayout.setSpacing(true);
		statusFilterLayout.setMargin(false);
		statusFilterLayout.setWidth(100, Unit.PERCENTAGE);
		statusFilterLayout.addStyleName(CssStyles.VSPACE_3);

		statusButtons = new HashMap<>();

		Button statusAll = ButtonHelper.createButton(Captions.all, e -> {
			ebsCriteria.sourceInformation(null);
			navigateTo(ebsCriteria);
		}, ValoTheme.BUTTON_BORDERLESS, CssStyles.BUTTON_FILTER);
		statusAll.setCaptionAsHtml(true);

		statusFilterLayout.addComponent(statusAll);
		statusButtons.put(statusAll, I18nProperties.getCaption(Captions.all));
		activeStatusButton = statusAll;

		for (EbsSourceType source : EbsSourceType.values()) {
			Button statusButton = ButtonHelper.createButton(source.toString(), e -> {
				ebsCriteria.sourceInformation(source);
				navigateTo(ebsCriteria);
			}, ValoTheme.BUTTON_BORDERLESS, CssStyles.BUTTON_FILTER, CssStyles.BUTTON_FILTER_LIGHT);
			statusButton.setData(source);
			statusButton.setCaptionAsHtml(true);

			statusFilterLayout.addComponent(statusButton);
			statusButtons.put(statusButton, source.toString());
		}

		HorizontalLayout actionButtonsLayout = new HorizontalLayout();
		actionButtonsLayout.setSpacing(true);
		{

			// Show active/archived/all dropdown
			if (Objects.nonNull(UserProvider.getCurrent()) && UserProvider.getCurrent().hasUserRight(UserRight.EVENT_VIEW)) {

				if (FacadeProvider.getFeatureConfigurationFacade().isFeatureEnabled(FeatureType.AUTOMATIC_ARCHIVING, CoreEntityType.EVENT)) {
					int daysAfterCaseGetsArchived = FacadeProvider.getFeatureConfigurationFacade()
							.getProperty(FeatureType.AUTOMATIC_ARCHIVING, CoreEntityType.EBS, FeatureTypeProperty.THRESHOLD_IN_DAYS, Integer.class);
					if (daysAfterCaseGetsArchived > 0) {
						relevanceStatusInfoLabel = new Label(
								VaadinIcons.INFO_CIRCLE.getHtml() + " "
										+ String.format(I18nProperties.getString(Strings.infoArchivedEbs), daysAfterCaseGetsArchived),
								ContentMode.HTML);
						relevanceStatusInfoLabel.setVisible(false);
						relevanceStatusInfoLabel.addStyleName(CssStyles.LABEL_VERTICAL_ALIGN_SUPER);
						actionButtonsLayout.addComponent(relevanceStatusInfoLabel);
						actionButtonsLayout.setComponentAlignment(relevanceStatusInfoLabel, Alignment.MIDDLE_RIGHT);
					}
				}
				relevanceStatusFilter = ComboBoxHelper.createComboBoxV7();
				relevanceStatusFilter.setId("relevanceStatus");
				relevanceStatusFilter.setWidth(210, Unit.PIXELS);
				relevanceStatusFilter.setNullSelectionAllowed(false);
				relevanceStatusFilter.addItems((Object[]) EntityRelevanceStatus.values());
				relevanceStatusFilter.setItemCaption(EntityRelevanceStatus.ACTIVE, I18nProperties.getCaption(Captions.Ebs_Active_Signals));
				relevanceStatusFilter.setItemCaption(EntityRelevanceStatus.ARCHIVED, I18nProperties.getCaption(Captions.Ebs_Archived_Signals));
				relevanceStatusFilter
						.setItemCaption(EntityRelevanceStatus.ACTIVE_AND_ARCHIVED, I18nProperties.getCaption(Captions.Ebs_All_Active_And_Archived_Signals));

				if (UserProvider.getCurrent().hasUserRight(UserRight.CASE_DELETE)) {
					relevanceStatusFilter.setItemCaption(EntityRelevanceStatus.DELETED, I18nProperties.getCaption(Captions.Ebs_Deleted_Signals));
				} else {
					relevanceStatusFilter.removeItem(EntityRelevanceStatus.DELETED);
				}
				relevanceStatusFilter.addValueChangeListener(e -> {
					if (relevanceStatusInfoLabel != null) {
						relevanceStatusInfoLabel.setVisible(EntityRelevanceStatus.ARCHIVED.equals(e.getProperty().getValue()));
					}
					ebsCriteria.relevanceStatus((EntityRelevanceStatus) e.getProperty().getValue());
					navigateTo(ebsCriteria);
				});
				actionButtonsLayout.addComponent(relevanceStatusFilter);
			}
		}
		statusFilterLayout.addComponent(actionButtonsLayout);
		statusFilterLayout.setComponentAlignment(actionButtonsLayout, Alignment.TOP_RIGHT);
		statusFilterLayout.setExpandRatio(actionButtonsLayout, 1);

		return statusFilterLayout;
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
		updateStatusButtons();
		if (relevanceStatusFilter != null) {
			relevanceStatusFilter.setValue(ebsCriteria.getRelevanceStatus());
		} if ( ebsCriteria.getRelevanceStatus() == null) {
			relevanceStatusFilter.setValue(EntityRelevanceStatus.ACTIVE);
			ebsCriteria.relevanceStatus(EntityRelevanceStatus.ACTIVE);

		}
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
			} else {
				((EbsGrid) grid).reload();
				navigateTo(ebsCriteria);
			}
		});
		filterLayout.addComponent(ebsFilterForm);

		return filterLayout;
	}
	private void updateStatusButtons() {
		long dataCount;
		statusButtons.keySet().forEach(b -> {
			CssStyles.style(b, CssStyles.BUTTON_FILTER_LIGHT);
			b.setCaption(statusButtons.get(b));
			if (b.getData() == ebsCriteria.getSourceInformation()) {
				activeStatusButton = b;
			}
		});
		CssStyles.removeStyles(activeStatusButton, CssStyles.BUTTON_FILTER_LIGHT);

		if (isDefaultViewType()){
			dataCount = FacadeProvider.getEbsFacade().count(ebsCriteria);
		}else {
			dataCount = FacadeProvider.getEbsFacade().eventCount(ebsCriteria);
		}

		if (activeStatusButton != null) {
			activeStatusButton
					.setCaption(statusButtons.get(activeStatusButton) + LayoutUtil.spanCss(CssStyles.BADGE, String.valueOf(dataCount)));
		}
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		String params = event.getParameters().trim();
		if (params.startsWith("?")) {
			params = params.substring(1);
		}
		if (isDefaultViewType()) {
			updateFilterComponents();
			((EbsSignalGrid) grid).reload();
		} else {

			updateFilterComponents();
			((EbsGrid) grid).reload();
		}
	}

	private Set<String> getSelectedRowUuids() {
		return viewConfiguration.isInEagerMode() ? (Set<String>) grid.asMultiSelect().getSelectedItems() : Collections.emptySet();
	}
}
