/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.ui.dashboard.ebs;

import com.vaadin.ui.CustomLayout;
import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.SourceTypeStatisticsComponent;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.summary.CEBSSummaryComponent;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.summary.HEBSSummaryComponent;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.summary.HotlineSummaryComponent;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.summary.MediaScanSummaryComponent;
import de.symeda.sormas.ui.utils.LayoutUtil;

public class EbsEventStatisticsComponent extends CustomLayout {

	private static final long serialVersionUID = 6582975657305031105L;

	private final DashboardDataProvider dashboardDataProvider;

	private final SourceTypeStatisticsComponent sourceTypeStatisticsComponent;
	private final CEBSSummaryComponent cebsSummaryComponent;

	private static final String SOURCE_TYPE_LOC = "sourceType";
	private static final String CEBS_LOC = "cebs";
	private static final String HEBS_LOC = "hebs";
	private static final String HOTLINE_LOC = "hotline";
	private static final String MEDIA_SCAN_LOC = "mediaScan";

	private final HEBSSummaryComponent hebsSummaryComponent;
	private final MediaScanSummaryComponent mediaScanningSummaryComponent;
	private final HotlineSummaryComponent hotlineSummaryComponent;

	public EbsEventStatisticsComponent(DashboardDataProvider dashboardDataProvider) {
		this.dashboardDataProvider = dashboardDataProvider;

		setWidth(100, Unit.PERCENTAGE);

		setTemplateContents(
			LayoutUtil.fluidRow(
				LayoutUtil.fluidColumn(3, 0, 12, 0, LayoutUtil.fluidRowLocs(SOURCE_TYPE_LOC)),
				LayoutUtil.fluidColumn(3, 0, 12, 0, LayoutUtil.fluidRowLocs(CEBS_LOC)),
				LayoutUtil.fluidColumn(3, 0, 12, 0, LayoutUtil.fluidRowLocs(HEBS_LOC)),
				LayoutUtil.fluidColumn(3, 0, 12, 0, LayoutUtil.fluidRowLocs(MEDIA_SCAN_LOC)),
				LayoutUtil.fluidColumn(3, 0, 12, 0, LayoutUtil.fluidRowLocs(HOTLINE_LOC))
			));

		sourceTypeStatisticsComponent = new SourceTypeStatisticsComponent();

		cebsSummaryComponent = new CEBSSummaryComponent();

		hebsSummaryComponent = new HEBSSummaryComponent();

		mediaScanningSummaryComponent = new MediaScanSummaryComponent();

		hotlineSummaryComponent = new HotlineSummaryComponent();

		addComponent(sourceTypeStatisticsComponent, SOURCE_TYPE_LOC);

		addComponent(cebsSummaryComponent, CEBS_LOC);

		addComponent(hebsSummaryComponent, HEBS_LOC);

		addComponent(mediaScanningSummaryComponent, MEDIA_SCAN_LOC);

		addComponent(hotlineSummaryComponent, HOTLINE_LOC);
	}

	public void refresh() {
		sourceTypeStatisticsComponent.update(dashboardDataProvider.getSourceTypeCount());
		cebsSummaryComponent.update(dashboardDataProvider);
		hebsSummaryComponent.update(dashboardDataProvider);
		mediaScanningSummaryComponent.update(dashboardDataProvider);
		hotlineSummaryComponent.update(dashboardDataProvider);
	}

}
