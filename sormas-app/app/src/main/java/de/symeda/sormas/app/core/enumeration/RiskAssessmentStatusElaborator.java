/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.app.core.enumeration;

import android.content.Context;

import de.symeda.sormas.api.ebs.CategoryDetailsLevel;
import de.symeda.sormas.api.ebs.EbsSourceType;
import de.symeda.sormas.api.ebs.RiskAssesment;
import de.symeda.sormas.app.R;

public class RiskAssessmentStatusElaborator implements StatusElaborator {

	private RiskAssesment riskAssesment = null;

	public RiskAssessmentStatusElaborator(RiskAssesment riskAssesment) {
		this.riskAssesment = riskAssesment;
	}

	@Override
	public String getFriendlyName(Context context) {
		if (riskAssesment != null) {
			return riskAssesment.toString();
		}

		return "";
	}

	@Override
	public int getColorIndicatorResource() {
		switch (riskAssesment) {
			case VERY_HIGH:
				return R.color.indicatorConfirmedContact;
			case HIGH:
				return R.color.indicatorSignal;
			case MEDIUM:
				return R.color.indicatorCaseSuspect;
			case LOW:
				return R.color.indicatorNoContact;
		}

		return R.color.noColor;
	}

	@Override
	public Enum getValue() {
		return this.riskAssesment;
	}

	@Override
	public int getIconResourceId() {
		switch (riskAssesment) {
			case VERY_HIGH:
				return R.drawable.ic_lp_possible_alerts_192dp;
			case HIGH:
			case MEDIUM:
			case LOW:
				return R.drawable.ic_lp_confirmed_alerts_192dp;
			default:
				throw new IllegalArgumentException("Unknown risk assessment: " + riskAssesment);
		}
	}
}
