package de.symeda.sormas.ui.dashboard.ebs.components.statistics.summary;

import de.symeda.sormas.api.dashboard.EbsCategoryOfInformantDto;
import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.EbsEventSectionStatisticsComponent;
import de.symeda.sormas.ui.utils.CssStyles;
import java.util.List;

public class HEBSSummaryComponent extends EbsEventSectionStatisticsComponent {

	private final EbsEventSummaryElementComponent hebsEventSummaryElementComponent;

	public HEBSSummaryComponent() {
		super(Captions.dashboardHebs, null);
		hebsEventSummaryElementComponent =
			new EbsEventSummaryElementComponent(null);
		addComponent(hebsEventSummaryElementComponent);

		addStyleName(CssStyles.VSPACE_TOP_4);
	}

	public void update(DashboardDataProvider dashboardDataProvider) {
		dashboardDataProvider.setEventSourceType(EbsSourceType.HEBS);
		updateTotalLabel(String.valueOf(dashboardDataProvider.getSourceTypeCount().getOrDefault(EbsSourceType.HEBS, 0)));

		List<EbsCategoryOfInformantDto> ebsCategoryOfInformantDtoList = dashboardDataProvider.getEbsCategoryOfInformantDtoBySourceTypeForHeb();
		StringBuilder labelText = new StringBuilder();

		for (EbsCategoryOfInformantDto ebsCategoryOfInformantDto : ebsCategoryOfInformantDtoList) {
			// Append each item's caption to the label text, followed by a line break
			labelText.append(I18nProperties.getEnumCaption(ebsCategoryOfInformantDto.getCategoryOfInformant())+ "  "+ ebsCategoryOfInformantDto.getInformantCount()+"  >  "+ebsCategoryOfInformantDto.getPercentage()+"%")
					.append("<br>");
		}

		// Set the label's content mode to HTML and update the label
		hebsEventSummaryElementComponent.setCaptionAsHtml(true);
		hebsEventSummaryElementComponent.setMargin(true);
		hebsEventSummaryElementComponent.setResponsive(true);
		hebsEventSummaryElementComponent.setCaption(labelText.toString());
		hebsEventSummaryElementComponent.addStyleName("v-caption-caption-inline");
	}

}
