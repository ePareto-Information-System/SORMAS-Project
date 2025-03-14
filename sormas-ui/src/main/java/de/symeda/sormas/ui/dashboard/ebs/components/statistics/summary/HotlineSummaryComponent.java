package de.symeda.sormas.ui.dashboard.ebs.components.statistics.summary;

import de.symeda.sormas.api.dashboard.EbsCategoryOfInformantDto;
import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.EbsEventSectionStatisticsComponent;
import de.symeda.sormas.ui.utils.CssStyles;
import java.util.List;

public class HotlineSummaryComponent extends EbsEventSectionStatisticsComponent {

	private final EbsEventSummaryElementComponent ebsEventSummaryElementComponent;

	public HotlineSummaryComponent() {
		super(Captions.dashboardHotline, null);


		ebsEventSummaryElementComponent =
			new EbsEventSummaryElementComponent(null);
		addComponent(ebsEventSummaryElementComponent);

		addStyleName(CssStyles.VSPACE_TOP_4);
	}

	public void update(DashboardDataProvider dashboardDataProvider) {
		dashboardDataProvider.setEventSourceType(EbsSourceType.HOTLINE_PERSON);
		updateTotalLabel(String.valueOf(dashboardDataProvider.getSourceTypeCount().getOrDefault(EbsSourceType.HOTLINE_PERSON, 0)));

		List<EbsCategoryOfInformantDto> ebsCategoryOfInformantDtoList = dashboardDataProvider.getEbsCategoryOfInformantDtoBySourceTypeForHotline();
		StringBuilder labelText = new StringBuilder();

		for (EbsCategoryOfInformantDto ebsCategoryOfInformantDto : ebsCategoryOfInformantDtoList) {
			// Append each item's caption to the label text, followed by a line break
			labelText.append(I18nProperties.getEnumCaption(ebsCategoryOfInformantDto.getCategoryOfInformant())+ "  "+ ebsCategoryOfInformantDto.getInformantCount()+"  >  "+ebsCategoryOfInformantDto.getPercentage()+"%")
					.append("<br>");
		}

		// Set the label's content mode to HTML and update the label
		ebsEventSummaryElementComponent.setCaptionAsHtml(true);
		ebsEventSummaryElementComponent.setMargin(true);
		ebsEventSummaryElementComponent.setResponsive(true);
		ebsEventSummaryElementComponent.setCaption(labelText.toString());
		ebsEventSummaryElementComponent.addStyleName("v-caption-caption-inline");
	}
}
