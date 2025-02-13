package de.symeda.sormas.ui.dashboard.ebs.components.epicurve.builders;

import de.symeda.sormas.api.dashboard.EpiCurveGrouping;
import de.symeda.sormas.ui.dashboard.ebs.components.epicurve.EbsEpiCurveMode;

public class EbsEpiCurveBuilders {

	public static EbsEpiCurveBuilder getEpiCurveBuilder(
		EbsEpiCurveMode epiCurveSurveillanceMode,
		EpiCurveGrouping epiCurveGrouping) {
		if (epiCurveSurveillanceMode == EbsEpiCurveMode.CASE_STATUS) {
			return new CaseStatusCurveBuilder(epiCurveGrouping);
		} else if (epiCurveSurveillanceMode == EbsEpiCurveMode.ALIVE_OR_DEAD) {
			return new AliveOrDeadCurveBuilder(epiCurveGrouping);
		}
		return null;
	}
}
