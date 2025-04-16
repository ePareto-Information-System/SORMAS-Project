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
import de.symeda.sormas.api.EbsEvent;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.CompletedEbsEventStatisticsComponent;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.OutcomeStatisticsComponent;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.PendingEbsEventStatisticsComponent;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.RiskAssessmentSourceTypeEbsEventStatisticsComponent;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.RiskAssessmentTimeMetricEventStatisticsComponent;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.SignalCategorySourceTypeEbsEventStatisticsComponent;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.SourceTypeStatisticsComponent;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.TimeMetricBySourceTypeEbsEventStatisticsComponent;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.summary.CEBSSummaryComponent;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.summary.HEBSSummaryComponent;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.summary.HotlineSummaryComponent;
import de.symeda.sormas.ui.dashboard.ebs.components.statistics.summary.MediaScanSummaryComponent;
import de.symeda.sormas.ui.utils.LayoutUtil;

public class EbsEventStatisticsComponent extends CustomLayout {

	private static final long serialVersionUID = 6582975657305031105L;
	private final DashboardDataProvider dashboardDataProvider;

	private final SourceTypeStatisticsComponent sourceTypeStatisticsComponent;
	private final PendingEbsEventStatisticsComponent signalVerificationPendingEbsEventStatisticsComponent;

	private final PendingEbsEventStatisticsComponent triagingPendingEbsEventStatisticsComponent;


	private final CompletedEbsEventStatisticsComponent completedEbsEventStatisticsComponent;

	private final CEBSSummaryComponent cebsSummaryComponent;

	private final OutcomeStatisticsComponent outcomeStatisticsComponent;

	private final OutcomeStatisticsComponent signalVerificationOutcomeStatisticsComponent;

	private static final String SOURCE_TYPE_LOC = "sourceType";
	private static final String CEBS_LOC = "cebs";
	private static final String HEBS_LOC = "hebs";
	private static final String HOTLINE_LOC = "hotline";
	private static final String MEDIA_SCAN_LOC = "mediaScan";
	private static final String PENDING_EVENT_LOC = "pendingEvent";
	private static final String COMPLETED_EVENT_LOC = "completedEvent" ;
	private static final String OUTCOME_EVENT_LOC = "outcomeEvent";
	private static final String SIGNAL_CATEGORY_EVENT_LOC = "signalCategoryEvent" ;
	private static final String RISK_SOURCE_EVENT_LOC = "riskAssessmentSourceType";

	private static final String RISK_TIME_EVENT_LOC = "riskAssessmentTimeMetric";
	private static final String SOURCE_TIME_EVENT_LOC = "sourceTypeTimeMetric" ;
	private static final String SIGNAL_PENDING_EVENT_LOC = "signalVerificationPendingEvent";
	private static final String SIGNAL_OUTCOME_EVENT_LOC = "signalOutcomeEvent";

	private final HEBSSummaryComponent hebsSummaryComponent;
	private final MediaScanSummaryComponent mediaScanningSummaryComponent;
	private final HotlineSummaryComponent hotlineSummaryComponent;
	private final SignalCategorySourceTypeEbsEventStatisticsComponent signalCategorySourceTypeEbsEventStatisticsComponent;
	private final RiskAssessmentTimeMetricEventStatisticsComponent riskAssessmentTimeMetricEventStatisticsComponent;

	private final RiskAssessmentSourceTypeEbsEventStatisticsComponent riskAssessmentSourceTypeEbsEventStatisticsComponent;

	private final TimeMetricBySourceTypeEbsEventStatisticsComponent timeMetricBySourceTypeEbsEventStatisticsComponent;

	public EbsEventStatisticsComponent(DashboardDataProvider dashboardDataProvider) {
		this.dashboardDataProvider = dashboardDataProvider;

		setWidth(100, Unit.PERCENTAGE);

		setTemplateContents(
				LayoutUtil.fluidRow(
						LayoutUtil.fluidColumn(3, 0, 12, 0, LayoutUtil.fluidRowLocs(SOURCE_TYPE_LOC)),
						LayoutUtil.fluidColumn(3, 0, 12, 0, LayoutUtil.fluidRowLocs(CEBS_LOC)),
						LayoutUtil.fluidColumn(3, 0, 12, 0, LayoutUtil.fluidRowLocs(HEBS_LOC)),
						LayoutUtil.fluidColumn(3, 0, 12, 0, LayoutUtil.fluidRowLocs(MEDIA_SCAN_LOC)),
						LayoutUtil.fluidColumn(3, 0, 12, 0, LayoutUtil.fluidRowLocs(HOTLINE_LOC)),
						LayoutUtil.fluidColumn(3, 0, 12, 0, LayoutUtil.fluidRowLocs(PENDING_EVENT_LOC)),
						LayoutUtil.fluidColumn(3, 0, 12, 0, LayoutUtil.fluidRowLocs(SIGNAL_PENDING_EVENT_LOC)),
						LayoutUtil.fluidColumn(3, 0, 12, 0, LayoutUtil.fluidRowLocs(COMPLETED_EVENT_LOC)),
						LayoutUtil.fluidColumn(3, 0, 12, 0, LayoutUtil.fluidRowLocs(OUTCOME_EVENT_LOC)),
						LayoutUtil.fluidColumn(3, 0, 12, 0, LayoutUtil.fluidRowLocs(SIGNAL_OUTCOME_EVENT_LOC)),

						LayoutUtil.fluidColumn(6, 0, 12, 0, LayoutUtil.fluidRowLocs(SIGNAL_CATEGORY_EVENT_LOC)),
						LayoutUtil.fluidColumn(6, 0, 12, 0, LayoutUtil.fluidRowLocs(RISK_SOURCE_EVENT_LOC)),
						LayoutUtil.fluidColumn(6, 0, 12, 0, LayoutUtil.fluidRowLocs(RISK_TIME_EVENT_LOC)),
						LayoutUtil.fluidColumn(6, 0, 12, 0, LayoutUtil.fluidRowLocs(SOURCE_TIME_EVENT_LOC)),
						LayoutUtil.fluidColumn(6, 0, 12, 0, LayoutUtil.fluidRowLocs(SIGNAL_PENDING_EVENT_LOC))
				));

		sourceTypeStatisticsComponent = new SourceTypeStatisticsComponent();

		triagingPendingEbsEventStatisticsComponent = new PendingEbsEventStatisticsComponent(EbsEvent.TRIAGING);

		signalVerificationPendingEbsEventStatisticsComponent = new PendingEbsEventStatisticsComponent(EbsEvent.SIGNAL_VERIFICATION);

		completedEbsEventStatisticsComponent = new CompletedEbsEventStatisticsComponent();

		cebsSummaryComponent = new CEBSSummaryComponent();

		hebsSummaryComponent = new HEBSSummaryComponent();

		mediaScanningSummaryComponent = new MediaScanSummaryComponent();

		hotlineSummaryComponent = new HotlineSummaryComponent();

		outcomeStatisticsComponent = new OutcomeStatisticsComponent(EbsEvent.TRIAGING);

		signalVerificationOutcomeStatisticsComponent =new OutcomeStatisticsComponent(EbsEvent.SIGNAL_VERIFICATION);

		signalCategorySourceTypeEbsEventStatisticsComponent = new SignalCategorySourceTypeEbsEventStatisticsComponent();

		riskAssessmentSourceTypeEbsEventStatisticsComponent = new RiskAssessmentSourceTypeEbsEventStatisticsComponent();

		riskAssessmentTimeMetricEventStatisticsComponent = new RiskAssessmentTimeMetricEventStatisticsComponent();

		timeMetricBySourceTypeEbsEventStatisticsComponent = new TimeMetricBySourceTypeEbsEventStatisticsComponent();

		addComponent(sourceTypeStatisticsComponent, SOURCE_TYPE_LOC);

		addComponent(cebsSummaryComponent, CEBS_LOC);

		addComponent(hebsSummaryComponent, HEBS_LOC);

		addComponent(mediaScanningSummaryComponent, MEDIA_SCAN_LOC);

		addComponent(hotlineSummaryComponent, HOTLINE_LOC);

		addComponent(triagingPendingEbsEventStatisticsComponent, PENDING_EVENT_LOC);

		addComponent(signalVerificationPendingEbsEventStatisticsComponent, SIGNAL_PENDING_EVENT_LOC);

		addComponent(completedEbsEventStatisticsComponent, COMPLETED_EVENT_LOC);

		addComponent(outcomeStatisticsComponent, OUTCOME_EVENT_LOC);

		addComponent(signalCategorySourceTypeEbsEventStatisticsComponent, SIGNAL_CATEGORY_EVENT_LOC);

		addComponent(riskAssessmentSourceTypeEbsEventStatisticsComponent, RISK_SOURCE_EVENT_LOC);

		addComponent(riskAssessmentTimeMetricEventStatisticsComponent, RISK_TIME_EVENT_LOC);

		addComponent(timeMetricBySourceTypeEbsEventStatisticsComponent, SOURCE_TIME_EVENT_LOC);

		addComponent(signalVerificationOutcomeStatisticsComponent, SIGNAL_OUTCOME_EVENT_LOC);
	}

	public void refresh() {
		// Always update source type statistics
		sourceTypeStatisticsComponent.update(dashboardDataProvider.getSourceTypeCount());

		EbsEvent currentEvent = dashboardDataProvider.getEbsEvent();

		// Hide all components first (both visibility and content)
		cebsSummaryComponent.setVisible(false);
		hebsSummaryComponent.setVisible(false);
		mediaScanningSummaryComponent.setVisible(false);
		hotlineSummaryComponent.setVisible(false);
		triagingPendingEbsEventStatisticsComponent.setVisible(false);
		signalVerificationPendingEbsEventStatisticsComponent.setVisible(false);

		completedEbsEventStatisticsComponent.setVisible(false);
		outcomeStatisticsComponent.setVisible(false);
		signalCategorySourceTypeEbsEventStatisticsComponent.setVisible(false);
		riskAssessmentSourceTypeEbsEventStatisticsComponent.setVisible(false);
		riskAssessmentTimeMetricEventStatisticsComponent.setVisible(false);
		timeMetricBySourceTypeEbsEventStatisticsComponent.setVisible(false);

		removeComponent(CEBS_LOC);
		removeComponent(HEBS_LOC);
		removeComponent(PENDING_EVENT_LOC);
		removeComponent(SIGNAL_PENDING_EVENT_LOC);
		removeComponent(COMPLETED_EVENT_LOC);
		removeComponent(OUTCOME_EVENT_LOC);
		removeComponent(SIGNAL_CATEGORY_EVENT_LOC);
		removeComponent(RISK_SOURCE_EVENT_LOC);
		removeComponent(RISK_TIME_EVENT_LOC);
		removeComponent(SOURCE_TIME_EVENT_LOC);

		// Only show and update for null or SIGNAL_INFORMATION
		if (EbsEvent.SIGNAL_INFORMATION.equals(currentEvent)) {
			addComponent(cebsSummaryComponent, CEBS_LOC);
			addComponent(hebsSummaryComponent, HEBS_LOC);
			addComponent(triagingPendingEbsEventStatisticsComponent, PENDING_EVENT_LOC);
			addComponent(signalVerificationPendingEbsEventStatisticsComponent, PENDING_EVENT_LOC);

			addComponent(completedEbsEventStatisticsComponent, COMPLETED_EVENT_LOC);
			addComponent(outcomeStatisticsComponent,OUTCOME_EVENT_LOC);
			addComponent(signalCategorySourceTypeEbsEventStatisticsComponent,SIGNAL_CATEGORY_EVENT_LOC);

			// Show CEBS components in their normal location
			cebsSummaryComponent.setVisible(true);
			hebsSummaryComponent.setVisible(true);
			mediaScanningSummaryComponent.setVisible(true);
			hotlineSummaryComponent.setVisible(true);

			cebsSummaryComponent.update(dashboardDataProvider);
			hebsSummaryComponent.update(dashboardDataProvider);
			mediaScanningSummaryComponent.update(dashboardDataProvider);
			hotlineSummaryComponent.update(dashboardDataProvider);

		} else if (EbsEvent.TRIAGING.equals(currentEvent)) {
			addComponent(triagingPendingEbsEventStatisticsComponent, CEBS_LOC);
			addComponent(completedEbsEventStatisticsComponent, HEBS_LOC);
			addComponent(outcomeStatisticsComponent,OUTCOME_EVENT_LOC);
			addComponent(signalCategorySourceTypeEbsEventStatisticsComponent,SIGNAL_CATEGORY_EVENT_LOC);

			triagingPendingEbsEventStatisticsComponent.setVisible(true);
			triagingPendingEbsEventStatisticsComponent.update(dashboardDataProvider.getEbsPendingTimeMetricByEbsEvent());
			completedEbsEventStatisticsComponent.setVisible(true);
			completedEbsEventStatisticsComponent.update(dashboardDataProvider.getEbsCompletedTimeMetricByEbsEvent());
			outcomeStatisticsComponent.setVisible(true);
			outcomeStatisticsComponent.update(dashboardDataProvider);
			signalCategorySourceTypeEbsEventStatisticsComponent.setVisible(true);
			signalCategorySourceTypeEbsEventStatisticsComponent.update(dashboardDataProvider.getSignalCategoryBySourceType());
		}
		else if (EbsEvent.SIGNAL_VERIFICATION.equals(currentEvent)) {
			addComponent(signalVerificationPendingEbsEventStatisticsComponent, CEBS_LOC);
			addComponent(outcomeStatisticsComponent,OUTCOME_EVENT_LOC);
			addComponent(timeMetricBySourceTypeEbsEventStatisticsComponent,SIGNAL_CATEGORY_EVENT_LOC);

			signalVerificationPendingEbsEventStatisticsComponent.setVisible(true);
			signalVerificationPendingEbsEventStatisticsComponent.update(dashboardDataProvider.getEbsPendingTimeMetricByEbsEvent());

			outcomeStatisticsComponent.setVisible(true);
			outcomeStatisticsComponent.update(dashboardDataProvider);

			timeMetricBySourceTypeEbsEventStatisticsComponent.setVisible(true);
			timeMetricBySourceTypeEbsEventStatisticsComponent.update(dashboardDataProvider.getTimeMetricBySourceType());
		}
		else if (EbsEvent.RISK_ASSESSMENT.equals(currentEvent)) {
			addComponent(riskAssessmentTimeMetricEventStatisticsComponent,RISK_TIME_EVENT_LOC);
			addComponent(riskAssessmentSourceTypeEbsEventStatisticsComponent,RISK_SOURCE_EVENT_LOC);

			riskAssessmentTimeMetricEventStatisticsComponent.setVisible(true);
			riskAssessmentTimeMetricEventStatisticsComponent.update(dashboardDataProvider.getRiskAssessmentTimeMetric());

			riskAssessmentSourceTypeEbsEventStatisticsComponent.setVisible(true);
			riskAssessmentSourceTypeEbsEventStatisticsComponent.update(dashboardDataProvider.getRiskAssessmentSourceType());
		}
		else if (EbsEvent.ALERT.equals(currentEvent)) {
			addComponent(riskAssessmentSourceTypeEbsEventStatisticsComponent,RISK_SOURCE_EVENT_LOC);

			riskAssessmentSourceTypeEbsEventStatisticsComponent.setVisible(true);
			riskAssessmentSourceTypeEbsEventStatisticsComponent.update(dashboardDataProvider.getRiskAssessmentSourceType());
		}
	}

}
