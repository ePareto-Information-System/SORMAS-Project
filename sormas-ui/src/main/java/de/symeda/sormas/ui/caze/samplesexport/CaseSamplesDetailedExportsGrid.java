package de.symeda.sormas.ui.caze.samplesexport;

import java.util.Date;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinServletService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.TextRenderer;
import com.vaadin.ui.themes.ValoTheme;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.samplesexport.CaseSamplesDetailedExportDownloadDto;
import de.symeda.sormas.api.caze.samplesexport.CaseSamplesDetailedExportDto;
import de.symeda.sormas.api.caze.samplesexport.CaseSamplesDetailedExportResult;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.VaadinUiUtil;

public class CaseSamplesDetailedExportsGrid extends Grid<CaseSamplesDetailedExportDto> {

	private static final long serialVersionUID = 1L;

	public CaseSamplesDetailedExportsGrid() {
		super(CaseSamplesDetailedExportDto.class);
		setSizeFull();
		setLazyDataProvider();

		setColumns(
			CaseSamplesDetailedExportDto.REQUESTING_USER,
			CaseSamplesDetailedExportDto.REQUESTED_DATE,
			CaseSamplesDetailedExportDto.RESULT,
			CaseSamplesDetailedExportDto.FILTER_SUMMARY,
			CaseSamplesDetailedExportDto.EXPORTED_ROW_COUNT,
			CaseSamplesDetailedExportDto.EXPIRES_AT,
			CaseSamplesDetailedExportDto.DOWNLOAD_COUNT,
			CaseSamplesDetailedExportDto.FAILURE_MESSAGE);

		((Column<CaseSamplesDetailedExportDto, UserReferenceDto>) getColumn(CaseSamplesDetailedExportDto.REQUESTING_USER))
			.setRenderer(user -> user != null ? user.getCaption() : "", new TextRenderer());
		((Column<CaseSamplesDetailedExportDto, Date>) getColumn(CaseSamplesDetailedExportDto.REQUESTED_DATE))
			.setRenderer(new DateRenderer(DateHelper.getLocalDateTimeFormat(I18nProperties.getUserLanguage())));
		((Column<CaseSamplesDetailedExportDto, CaseSamplesDetailedExportResult>) getColumn(CaseSamplesDetailedExportDto.RESULT))
			.setRenderer(result -> result != null ? result.toString() : "", new TextRenderer());
		((Column<CaseSamplesDetailedExportDto, Date>) getColumn(CaseSamplesDetailedExportDto.EXPIRES_AT))
			.setRenderer(new DateRenderer(DateHelper.getLocalDateTimeFormat(I18nProperties.getUserLanguage())));

		getColumn(CaseSamplesDetailedExportDto.FILTER_SUMMARY).setMaximumWidth(320);
		getColumn(CaseSamplesDetailedExportDto.FAILURE_MESSAGE).setMaximumWidth(240);

		addComponentColumn(this::buildDownloadsButton)
			.setId("downloads")
			.setCaption(I18nProperties.getCaption(Captions.caseSamplesDetailedExportDownloads))
			.setSortable(false)
			.setWidth(140);

		for (Column<?, ?> column : getColumns()) {
			column.setCaption(
				I18nProperties.getPrefixCaption(CaseSamplesDetailedExportDto.I18N_PREFIX, column.getId().toString(), column.getCaption()));
		}
	}

	public void reload() {
		getDataProvider().refreshAll();
	}

	private void setLazyDataProvider() {
		DataProvider<CaseSamplesDetailedExportDto, Void> dataProvider = DataProvider.fromFilteringCallbacks(
			query -> FacadeProvider.getCaseSamplesDetailedExportFacade()
				.getIndexList(query.getOffset(), query.getLimit(), null)
				.stream(),
			query -> (int) FacadeProvider.getCaseSamplesDetailedExportFacade().count());
		setDataProvider(dataProvider);
		setSelectionMode(SelectionMode.NONE);
	}

	private Button buildDownloadsButton(CaseSamplesDetailedExportDto export) {
		Button button = ButtonHelper.createIconButton(
			Captions.caseSamplesDetailedExportViewDownloads,
			VaadinIcons.DOWNLOAD,
			e -> showDownloads(export),
			ValoTheme.BUTTON_LINK);
		button.setEnabled(export.getDownloadCount() > 0 || export.getResult() == CaseSamplesDetailedExportResult.SUCCESS);
		return button;
	}

	private void showDownloads(CaseSamplesDetailedExportDto export) {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setWidth(100, Unit.PERCENTAGE);

		if (export.getResult() == CaseSamplesDetailedExportResult.SUCCESS
			&& export.getExpiresAt() != null
			&& export.getExpiresAt().after(new Date())) {
			String contextPath = VaadinServletService.getCurrentServletRequest().getContextPath();
			Link downloadLink = new Link(
				I18nProperties.getCaption(Captions.caseSamplesDetailedExportDownloadFile),
				new ExternalResource(contextPath + "/export-downloads/auth/" + export.getUuid()));
			downloadLink.setIcon(VaadinIcons.DOWNLOAD);
			downloadLink.setTargetName("_self");
			downloadLink.addStyleName(ValoTheme.BUTTON_PRIMARY);
			layout.addComponent(downloadLink);
		}

		Label summary = new Label(
			String.format(I18nProperties.getString(Strings.infoCaseSamplesDetailedExportDownloads), export.getDownloadCount()));
		summary.addStyleName(CssStyles.H3);
		layout.addComponent(summary);

		Grid<CaseSamplesDetailedExportDownloadDto> downloadsGrid = new Grid<>();
		downloadsGrid.setHeightByRows(Math.max(1, Math.min(10, Math.max(export.getDownloadCount(), 1))));
		downloadsGrid.setWidth(100, Unit.PERCENTAGE);
		downloadsGrid.addColumn(CaseSamplesDetailedExportDownloadDto::getDownloadedAt)
			.setCaption(
				I18nProperties
					.getPrefixCaption(CaseSamplesDetailedExportDownloadDto.I18N_PREFIX, CaseSamplesDetailedExportDownloadDto.DOWNLOADED_AT))
			.setRenderer(new DateRenderer(DateHelper.getLocalDateTimeFormat(I18nProperties.getUserLanguage())));
		downloadsGrid.addColumn(CaseSamplesDetailedExportDownloadDto::getDownloaderCaption)
			.setCaption(I18nProperties.getPrefixCaption(CaseSamplesDetailedExportDownloadDto.I18N_PREFIX, "who"));
		downloadsGrid.addColumn(CaseSamplesDetailedExportDownloadDto::getClientAddress)
			.setCaption(
				I18nProperties
					.getPrefixCaption(CaseSamplesDetailedExportDownloadDto.I18N_PREFIX, CaseSamplesDetailedExportDownloadDto.CLIENT_ADDRESS));

		ListDataProvider<CaseSamplesDetailedExportDownloadDto> provider =
			DataProvider.ofCollection(FacadeProvider.getCaseSamplesDetailedExportFacade().getDownloads(export.getUuid()));
		downloadsGrid.setDataProvider(provider);
		layout.addComponent(downloadsGrid);

		Window window = VaadinUiUtil.showPopupWindow(layout);
		window.setCaption(I18nProperties.getString(Strings.headingCaseSamplesDetailedExportDownloads));
		window.setWidth(800, Unit.PIXELS);
	}
}
