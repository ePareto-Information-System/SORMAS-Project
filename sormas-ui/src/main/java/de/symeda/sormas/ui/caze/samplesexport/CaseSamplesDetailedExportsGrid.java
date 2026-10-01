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
import com.vaadin.ui.TextArea;
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
			CaseSamplesDetailedExportDto.DOWNLOAD_COUNT);

		((Column<CaseSamplesDetailedExportDto, UserReferenceDto>) getColumn(CaseSamplesDetailedExportDto.REQUESTING_USER))
			.setRenderer(user -> user != null ? user.getCaption() : "", new TextRenderer());
		((Column<CaseSamplesDetailedExportDto, Date>) getColumn(CaseSamplesDetailedExportDto.REQUESTED_DATE))
			.setRenderer(new DateRenderer(DateHelper.getLocalDateTimeFormat(I18nProperties.getUserLanguage())));
		((Column<CaseSamplesDetailedExportDto, CaseSamplesDetailedExportResult>) getColumn(CaseSamplesDetailedExportDto.RESULT))
			.setRenderer(result -> result != null ? result.toString() : "", new TextRenderer());
		((Column<CaseSamplesDetailedExportDto, Date>) getColumn(CaseSamplesDetailedExportDto.EXPIRES_AT))
			.setRenderer(new DateRenderer(DateHelper.getLocalDateTimeFormat(I18nProperties.getUserLanguage())));

		getColumn(CaseSamplesDetailedExportDto.FILTER_SUMMARY).setMaximumWidth(320);

		addComponentColumn(this::buildErrorButton)
			.setId("errorLog")
			.setCaption(I18nProperties.getCaption(Captions.caseSamplesDetailedExportErrorLog))
			.setSortable(false)
			.setWidth(120);

		addColumn(this::buildProgressText)
			.setId("progress")
			.setCaption(I18nProperties.getCaption(Captions.caseSamplesDetailedExportProgress))
			.setSortable(false)
			.setWidth(150);

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

	private String buildProgressText(CaseSamplesDetailedExportDto export) {
		if (export.getResult() == CaseSamplesDetailedExportResult.IN_PROGRESS) {
			Integer rows = export.getProgressRowCount();
			if (rows == null || rows == 0) {
				return "—";
			}
			Long total = export.getTotalCaseCount();
			if (total != null && total > 0) {
				int pct = (int) Math.min(100L, rows * 100L / total);
				return String.format("%,d rows (%d%%)", rows, pct);
			}
			return String.format("%,d rows", rows);
		}
		return "";
	}

	private Button buildErrorButton(CaseSamplesDetailedExportDto export) {
		Button button = ButtonHelper.createIconButton(
			Captions.caseSamplesDetailedExportViewError,
			VaadinIcons.WARNING,
			e -> showErrorLog(export),
			ValoTheme.BUTTON_LINK);
		button.setEnabled(export.getFailureMessage() != null);
		return button;
	}

	private void showErrorLog(CaseSamplesDetailedExportDto export) {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();

		if (export.getFailureMessage() != null) {
			Label messageLabel = new Label(export.getFailureMessage());
			messageLabel.addStyleName(CssStyles.H3);
			messageLabel.setWidth(100, Unit.PERCENTAGE);
			layout.addComponent(messageLabel);
		}

		TextArea traceArea = new TextArea();
		traceArea.setWidth(100, Unit.PERCENTAGE);
		traceArea.setHeight(400, Unit.PIXELS);
		traceArea.setReadOnly(true);
		traceArea.setValue(
			export.getFailureStackTrace() != null ? export.getFailureStackTrace() : "No stack trace recorded.");
		layout.addComponent(traceArea);
		layout.setExpandRatio(traceArea, 1);

		Window window = VaadinUiUtil.showPopupWindow(layout);
		window.setCaption(I18nProperties.getCaption(Captions.caseSamplesDetailedExportErrorLog));
		window.setWidth(820, Unit.PIXELS);
		window.setHeight(560, Unit.PIXELS);
	}

	private Button buildDownloadsButton(CaseSamplesDetailedExportDto export) {
		Button button = ButtonHelper.createIconButton(
			Captions.caseSamplesDetailedExportViewDownloads,
			VaadinIcons.DOWNLOAD,
			e -> showDownloads(export),
			ValoTheme.BUTTON_LINK);
		boolean enabled = export.getDownloadCount() > 0
			|| export.getResult() == CaseSamplesDetailedExportResult.SUCCESS
			|| (export.getResult() == CaseSamplesDetailedExportResult.FAILED && export.getFileName() != null);
		button.setEnabled(enabled);
		return button;
	}

	private void showDownloads(CaseSamplesDetailedExportDto export) {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setWidth(100, Unit.PERCENTAGE);

		boolean fileDownloadable = (export.getResult() == CaseSamplesDetailedExportResult.SUCCESS
			|| export.getResult() == CaseSamplesDetailedExportResult.FAILED)
			&& export.getExpiresAt() != null
			&& export.getExpiresAt().after(new Date())
			&& export.getFileName() != null;
		if (fileDownloadable) {
			String contextPath = VaadinServletService.getCurrentServletRequest().getContextPath();
			String linkLabel = export.isPartial()
				? I18nProperties.getCaption(Captions.caseSamplesDetailedExportDownloadFile) + " (partial)"
				: I18nProperties.getCaption(Captions.caseSamplesDetailedExportDownloadFile);
			Link downloadLink = new Link(
				linkLabel,
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

		ListDataProvider<CaseSamplesDetailedExportDownloadDto> provider =
			DataProvider.ofCollection(FacadeProvider.getCaseSamplesDetailedExportFacade().getDownloads(export.getUuid()));
		downloadsGrid.setDataProvider(provider);
		layout.addComponent(downloadsGrid);

		Window window = VaadinUiUtil.showPopupWindow(layout);
		window.setCaption(I18nProperties.getString(Strings.headingCaseSamplesDetailedExportDownloads));
		window.setWidth(800, Unit.PIXELS);
	}
}
