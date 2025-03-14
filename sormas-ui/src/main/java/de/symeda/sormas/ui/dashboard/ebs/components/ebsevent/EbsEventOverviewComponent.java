package de.symeda.sormas.ui.dashboard.ebs.components.ebsevent;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import de.symeda.sormas.api.ebs.EbsEventBurdenDto;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;

import de.symeda.sormas.ui.dashboard.ebs.components.ebsevent.burden.EbsEventBurdenComponent;
import de.symeda.sormas.ui.dashboard.ebs.components.ebsevent.tile.EbsEventTileViewLayout;
import de.symeda.sormas.ui.utils.ButtonHelper;
import de.symeda.sormas.ui.utils.CssStyles;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EbsEventOverviewComponent extends HorizontalLayout {

	private static final int NUMBER_OF_DISEASES_COLLAPSED = 6;

	private final EbsEventBurdenComponent ebsEventBurdenComponent;
	private final EbsEventTileViewLayout ebsEventTileViewLayout;
	private final Button showTableViewButton;

	public EbsEventOverviewComponent(DashboardDataProvider dashboardDataProvider) {
		setWidth(100, Unit.PERCENTAGE);
		setMargin(false);
		ebsEventBurdenComponent = new EbsEventBurdenComponent();
		ebsEventTileViewLayout = new EbsEventTileViewLayout(dashboardDataProvider);

		addComponent(ebsEventTileViewLayout);
		setExpandRatio(ebsEventTileViewLayout, 1);

		// "Expand" and "Collapse" buttons
		showTableViewButton =
			ButtonHelper.createIconButtonWithCaption("showTableView", "", VaadinIcons.TABLE, null, CssStyles.BUTTON_SUBTLE, CssStyles.VSPACE_NONE);
		Button showTileViewButton = ButtonHelper
			.createIconButtonWithCaption("showTileView", "", VaadinIcons.SQUARE_SHADOW, null, CssStyles.BUTTON_SUBTLE, CssStyles.VSPACE_NONE);

		showTableViewButton.addClickListener(e -> {
			removeComponent(ebsEventTileViewLayout);
			addComponent(ebsEventBurdenComponent);
			setExpandRatio(ebsEventBurdenComponent, 1);

			removeComponent(showTableViewButton);
			addComponent(showTileViewButton);
			setComponentAlignment(showTileViewButton, Alignment.TOP_RIGHT);
		});
		showTileViewButton.addClickListener(e -> {
			removeComponent(ebsEventBurdenComponent);
			addComponent(ebsEventTileViewLayout);
			setExpandRatio(ebsEventTileViewLayout, 1);

			removeComponent(showTileViewButton);
			addComponent(showTableViewButton);
			setComponentAlignment(showTableViewButton, Alignment.TOP_RIGHT);
		});

		addComponent(showTableViewButton);
		setComponentAlignment(showTableViewButton, Alignment.TOP_RIGHT);
	}

	public void refresh(List<EbsEventBurdenDto> ebsEventsBurden, boolean isShowingAllEvents) {
		// sort, limit and filter
		Stream<EbsEventBurdenDto> ebsEventsBurdenStream =
			ebsEventsBurden.stream()
					.sorted(Comparator.comparing(EbsEventBurdenDto::getEbsEvent));

		if (!isShowingAllEvents) {
			ebsEventsBurdenStream = ebsEventsBurdenStream.limit(NUMBER_OF_DISEASES_COLLAPSED);
		}
		ebsEventsBurden = ebsEventsBurdenStream.collect(Collectors.toList());

		ebsEventBurdenComponent.refresh(ebsEventsBurden);
		ebsEventTileViewLayout.refresh(ebsEventsBurden);
	}

	public Button getShowTableViewButton() {
		return showTableViewButton;
	}
}
