package de.symeda.sormas.ui.dashboard.ebs.components.statistics;

import de.symeda.sormas.api.EbsEvent;
import de.symeda.sormas.api.dashboard.EbsCategoryOfInformantDto;
import de.symeda.sormas.api.dashboard.EbsEventOutcomeDto;
import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.summary.EbsEventSummaryElementComponent;
import de.symeda.sormas.ui.dashboard.statistics.CountElementStyle;
import de.symeda.sormas.ui.dashboard.statistics.DashboardStatisticsCountElement;
import de.symeda.sormas.ui.utils.CssStyles;
import java.util.List;
import java.util.Map;

public class OutcomeStatisticsComponent extends EbsEventSectionStatisticsComponent {


	EbsEventSummaryElementComponent ebsEventSummaryElementComponent;

	public OutcomeStatisticsComponent(EbsEvent eventType) {
		super(getDynamicCaption(eventType),null);

		ebsEventSummaryElementComponent =
				new EbsEventSummaryElementComponent(null);
		addComponent(ebsEventSummaryElementComponent);

		addStyleName(CssStyles.VSPACE_TOP_4);
	}




//	public void update(DashboardDataProvider dashboardDataProvider) {
////		updateTotalLabel(Integer.toString(ebsSourceTypeIntegerMap.values().stream().reduce(0, Integer::sum)));
////		discarded.updateCountLabel(ebsSourceTypeIntegerMap.getOrDefault(EbsSourceType.CEBS, 0));
////		referred.updateCountLabel(ebsSourceTypeIntegerMap.getOrDefault(EbsSourceType.HEBS, 0));
////		proceedingToVerification.updateCountLabel(ebsSourceTypeIntegerMap.getOrDefault(EbsSourceType.MEDIA_NEWS, 0));
//	}

	private static String getDynamicCaption(EbsEvent eventType) {
		switch (eventType) {
			case TRIAGING:
				return I18nProperties.getCaption(Captions.dashboardEbsOutcome);
			case SIGNAL_VERIFICATION:
				return I18nProperties.getCaption(Captions.dashboardSignalVerificationOutcome);
			default:
				return "Event Outcome";
		}
	}
	public void update(DashboardDataProvider dashboardDataProvider) {
		dashboardDataProvider.setEventSourceType(EbsSourceType.CEBS);

		updateTotalLabel(String.valueOf(dashboardDataProvider.getSourceTypeCount().getOrDefault(EbsSourceType.CEBS, 0)));

		List<EbsEventOutcomeDto> ebsCategoryOfInformantDtoList = dashboardDataProvider.getEbsEventOutcome();
		StringBuilder labelText = new StringBuilder();

		for (EbsEventOutcomeDto ebsEventOutcomeDto : ebsCategoryOfInformantDtoList) {
			// Append each item's caption to the label text, followed by a line break
			labelText.append(ebsEventOutcomeDto.getTriagingDecision()!=null? I18nProperties.getEnumCaption(ebsEventOutcomeDto.getTriagingDecision()) : "None"+ "  "+ ebsEventOutcomeDto.getCount()+"  >  "+ebsEventOutcomeDto.getPercentage()+"%")
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
