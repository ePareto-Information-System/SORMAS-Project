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

import de.symeda.sormas.api.ebs.ResponseStatus;
import de.symeda.sormas.api.ebs.RiskAssesment;
import de.symeda.sormas.app.R;

public class EbsAlertStatusElaborator implements StatusElaborator {

	private ResponseStatus responseStatus = null;

	public EbsAlertStatusElaborator(ResponseStatus responseStatus) {
		this.responseStatus = responseStatus;
	}

	@Override
	public String getFriendlyName(Context context) {
		if (responseStatus != null) {
			return responseStatus.toString();
		}

		return "";
	}

	@Override
	public int getColorIndicatorResource() {
		switch (responseStatus) {
			case NOT_STARTED:
				return R.color.indicatorConfirmedContact;
			case ON_GOING:
				return R.color.indicatorSignal;
			case COMPLETED:
				return R.color.indicatorNoContact;
		}

		return R.color.noColor;
	}

	@Override
	public Enum getValue() {
		return this.responseStatus;
	}

	@Override
	public int getIconResourceId() {
		switch (responseStatus) {
			case NOT_STARTED:
				return R.drawable.ic_lp_possible_alerts_192dp;
			case ON_GOING:
			case COMPLETED:
				return R.drawable.ic_lp_confirmed_alerts_192dp;
			default:
				throw new IllegalArgumentException("Unknown response Status: " + responseStatus);
		}
	}
}
