package de.symeda.sormas.ui.dashboard.ebs.components.statistics;

		import com.vaadin.ui.CssLayout;
		import de.symeda.sormas.api.dashboard.EbsCategoryOfInformantDto;
		import de.symeda.sormas.api.ebs.EbsSourceType;
		import de.symeda.sormas.api.i18n.I18nProperties;
		import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
		import de.symeda.sormas.ui.dashboard.components.DashboardHeadingComponent;
		import de.symeda.sormas.ui.dashboard.statistics.DashboardStatisticsCountElement;
		import de.symeda.sormas.ui.dashboard.statistics.DashboardStatisticsSubComponent;
		import java.util.List;

public class EbsEventSectionStatisticsComponent extends DashboardStatisticsSubComponent {

	private final DashboardHeadingComponent heading;

	public EbsEventSectionStatisticsComponent(String titleCaption, String infoIconText) {
		heading = new DashboardHeadingComponent(titleCaption, infoIconText);
		addComponent(heading);
	}

	public EbsEventSectionStatisticsComponent(String titleCaption, String description, String infoIconText) {
		this(titleCaption, infoIconText);
		heading.setTotalLabelDescription(description);
	}

	protected void updateTotalLabel(String value) {
		heading.updateTotalLabel(value);
	}

	public void hideHeading() {
		heading.setVisible(false);
	}

	protected void buildCountLayout(DashboardStatisticsCountElement... dashboardStatisticsCountElements) {
		CssLayout countLayout = createCountLayout(true);
		for (DashboardStatisticsCountElement dashboardStatisticsCountElement : dashboardStatisticsCountElements) {
			addComponentToCountLayout(countLayout, dashboardStatisticsCountElement);
		}
		addComponent(countLayout);
	}

	public DashboardHeadingComponent getHeading() {
		return heading;
	}

//	public void update(DashboardDataProvider dashboardDataProvider,EbsSourceType ebsSourceType) {
//		updateTotalLabel(String.valueOf(dashboardDataProvider.getSourceTypeCount().getOrDefault(ebsSourceType, 0)));
//
//		List<EbsCategoryOfInformantDto> ebsCategoryOfInformantDtoList = dashboardDataProvider.getEbsCategoryOfInformantDtoBySourceType();
//		StringBuilder labelText = new StringBuilder();
//
//		for (EbsCategoryOfInformantDto ebsCategoryOfInformantDto : ebsCategoryOfInformantDtoList) {
//			// Append each item's caption to the label text, followed by a line break
//			labelText.append(I18nProperties.getEnumCaption(ebsCategoryOfInformantDto.getCategoryOfInformant())+ "  "+ ebsCategoryOfInformantDto.getInformantCount()+"  >  "+ebsCategoryOfInformantDto.getPercentage()+"%")
//					.append("<br>");
//		}
//
//		// Remove the trailing <br> (if any)
////		if (labelText.length() > 0) {
////			labelText.setLength(labelText.length() - 4); // Remove the last "<br>"
////		}
//
//		// Set the label's content mode to HTML and update the label
//		this.setCaptionAsHtml(true);
//		this.setMargin(true);
//		this.setResponsive(true);
//		this.setCaption(labelText.toString());
//		this.addStyleName("v-caption-caption-inline");
//
//	}
//			this.update(dashboardDataProvider, EbsSourceType.CEBS)

}
