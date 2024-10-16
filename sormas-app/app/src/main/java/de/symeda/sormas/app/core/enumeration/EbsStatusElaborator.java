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
import de.symeda.sormas.api.ebs.SignalOutcome;
import de.symeda.sormas.app.R;

public class EbsStatusElaborator implements StatusElaborator {

	private SignalOutcome signalOutcome = null;
	private CategoryDetailsLevel detailsLevel = null;

	public EbsStatusElaborator(SignalOutcome signalOutcome) {
		this.signalOutcome = signalOutcome;
	}
	public EbsStatusElaborator(CategoryDetailsLevel detailsLevel) {
		this.detailsLevel = detailsLevel;
	}

	@Override
	public String getFriendlyName(Context context) {
		if (signalOutcome != null) {
			return signalOutcome.toString();
		}
		if (detailsLevel != null) {
			return detailsLevel.toString();
		}

		return "";
	}

	@Override
	public int getColorIndicatorResource() {
		switch (signalOutcome) {
			case EVENT:
				return R.color.indicatorSignal;
			case NON_EVENT:
				return R.color.indicatorEvent;
		}

		return R.color.noColor;
	}

	@Override
	public Enum getValue() {
		return this.signalOutcome;
	}

	@Override
	public int getIconResourceId() {
		switch (signalOutcome) {
			case EVENT:
			return R.drawable.ic_lp_possible_alerts_192dp;
			case NON_EVENT:
			return R.drawable.ic_lp_confirmed_alerts_192dp;
		default:
			throw new IllegalArgumentException(signalOutcome.toString());
		}
	}
}
