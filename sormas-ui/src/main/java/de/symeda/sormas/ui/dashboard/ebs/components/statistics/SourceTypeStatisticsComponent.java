package de.symeda.sormas.ui.dashboard.ebs.components.statistics;

import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.ui.dashboard.statistics.CountElementStyle;
import de.symeda.sormas.ui.dashboard.statistics.DashboardStatisticsCountElement;
import java.util.Map;

public class SourceTypeStatisticsComponent extends EbsEventSectionStatisticsComponent {

	private final DashboardStatisticsCountElement cebs;
	private final DashboardStatisticsCountElement hebs;
	private final DashboardStatisticsCountElement mediaScanning;
	private final DashboardStatisticsCountElement hotline;

	public SourceTypeStatisticsComponent() {
		super(Captions.dashboardNewSources, null);

		// Count layout
		cebs =
			new DashboardStatisticsCountElement(I18nProperties.getCaption(Captions.dashboardCebs), CountElementStyle.CRITICAL);
		hebs =
			new DashboardStatisticsCountElement(I18nProperties.getCaption(Captions.dashboardHebs), CountElementStyle.IMPORTANT);
		mediaScanning =
			new DashboardStatisticsCountElement(I18nProperties.getCaption(Captions.dashboardMediaScanning), CountElementStyle.RELEVANT);
		hotline =
			new DashboardStatisticsCountElement(I18nProperties.getCaption(Captions.dashboardHotline), CountElementStyle.POSITIVE);

		buildCountLayout(
				cebs,
				hebs,
				mediaScanning,
				hotline
		);

	}

	public void update(Map<EbsSourceType, Integer> ebsSourceTypeIntegerMap) {
		updateTotalLabel(Integer.toString(ebsSourceTypeIntegerMap.values().stream().reduce(0, Integer::sum)));
		cebs.updateCountLabel(ebsSourceTypeIntegerMap.getOrDefault(EbsSourceType.CEBS, 0));
		hebs.updateCountLabel(ebsSourceTypeIntegerMap.getOrDefault(EbsSourceType.HEBS, 0));
		mediaScanning.updateCountLabel(ebsSourceTypeIntegerMap.getOrDefault(EbsSourceType.MEDIA_NEWS, 0));
		hotline.updateCountLabel(ebsSourceTypeIntegerMap.getOrDefault(EbsSourceType.HOTLINE_PERSON, 0));
	}
}
