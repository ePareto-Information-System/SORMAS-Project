package de.symeda.sormas.ui.dashboard.ebs.components.statistics;

import de.symeda.sormas.api.EbsEvent;
import de.symeda.sormas.api.ebs.EbsTimeMetric;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.ui.dashboard.statistics.CountElementStyle;
import de.symeda.sormas.ui.dashboard.statistics.DashboardStatisticsCountElement;
import java.util.Map;

public class PendingEbsEventStatisticsComponent extends EbsEventSectionStatisticsComponent {

	private final DashboardStatisticsCountElement lessThanTwentyFourHrs;
	private final DashboardStatisticsCountElement twentyFourToFortyEightHrs;
	private final DashboardStatisticsCountElement fortyEightHrs;

	public PendingEbsEventStatisticsComponent(EbsEvent eventType) {

		super(getDynamicCaption(eventType), null);

		// Count layout
		lessThanTwentyFourHrs =
			new DashboardStatisticsCountElement(I18nProperties.getCaption(Captions.dashboadLessThanTwentyFourHrs), CountElementStyle.CRITICAL);
		twentyFourToFortyEightHrs =
			new DashboardStatisticsCountElement(I18nProperties.getCaption(Captions.dashboardTwentyFourToFortyEightHrs), CountElementStyle.IMPORTANT);
		fortyEightHrs =
			new DashboardStatisticsCountElement(I18nProperties.getCaption(Captions.dashboardFortyEightHrs), CountElementStyle.RELEVANT);

		buildCountLayout(
				lessThanTwentyFourHrs,
				twentyFourToFortyEightHrs,
				fortyEightHrs
		);

	}
	private static String getDynamicCaption(EbsEvent eventType) {
		switch (eventType) {
			case TRIAGING:
				return I18nProperties.getCaption(Captions.dashboardPendingTriage);
			case SIGNAL_VERIFICATION:
				return I18nProperties.getCaption(Captions.dashboardPendingSignalVerification);
			default:
				return "Pending Event";
		}
	}

	public void update(Map<EbsTimeMetric, Integer> ebsSourceTypeIntegerMap) {
		updateTotalLabel(Integer.toString(ebsSourceTypeIntegerMap.values().stream().reduce(0, Integer::sum)));
		lessThanTwentyFourHrs.updateCountLabel(ebsSourceTypeIntegerMap.getOrDefault(EbsTimeMetric.LESS_THAN_24_HOURS, 0));
		twentyFourToFortyEightHrs.updateCountLabel(ebsSourceTypeIntegerMap.getOrDefault(EbsTimeMetric.BETWEEN_24_48_HOURS, 0));
		fortyEightHrs.updateCountLabel(ebsSourceTypeIntegerMap.getOrDefault(EbsTimeMetric.MORE_THAN_48_HOURS, 0));
	}
}
