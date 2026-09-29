package de.symeda.sormas.ui.caze.samplesexport;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.ui.caze.CasesView;
import de.symeda.sormas.ui.utils.AbstractView;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;

public class CaseSamplesDetailedExportsView extends AbstractView {

	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = CasesView.VIEW_NAME + "/sampleExports";

	private final CaseSamplesDetailedExportsGrid grid;

	public CaseSamplesDetailedExportsView() {
		super(VIEW_NAME);

		grid = new CaseSamplesDetailedExportsGrid();


		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(true);
		layout.setSpacing(true);

		HorizontalLayout header = new HorizontalLayout();
		header.setWidth(100, Unit.PERCENTAGE);
		header.setSpacing(true);

		Label title = new Label(I18nProperties.getCaption(Captions.caseSamplesDetailedExportHistory));
		title.addStyleName(CssStyles.H2);
		header.addComponent(title);
		header.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
		header.setExpandRatio(title, 1);

		Button refreshButton = ButtonHelper.createIconButton(Captions.actionRefresh, VaadinIcons.REFRESH, e -> grid.reload(), ValoTheme.BUTTON_PRIMARY);
		header.addComponent(refreshButton);
		header.setComponentAlignment(refreshButton, Alignment.MIDDLE_RIGHT);

		Button backButton = ButtonHelper.createButton(Captions.caseSamplesDetailedExportBackToCases, e -> {
			getUI().getNavigator().navigateTo(CasesView.VIEW_NAME);
		});
		header.addComponent(backButton);
		header.setComponentAlignment(backButton, Alignment.MIDDLE_RIGHT);

		layout.addComponent(header);

		Label info = new Label(I18nProperties.getString(Strings.infoCaseSamplesDetailedExportHistory));
		info.setWidth(100, Unit.PERCENTAGE);
		layout.addComponent(info);

		layout.addComponent(grid);
		layout.setExpandRatio(grid, 1);

		addComponent(layout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		grid.reload();
	}
}
