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

package de.symeda.sormas.app.ebs;

import android.content.Context;

import de.symeda.sormas.app.R;
import de.symeda.sormas.app.core.enumeration.StatusElaborator;

public enum EbsSection
	implements
	StatusElaborator {

	SIGNAL_INFORMATION(R.string.caption_ebs_information, R.drawable.ic_group_black_24dp),
	TRIAGING(R.string.caption_triaging_information, R.drawable.ic_alert_24dp),
	SIGNAL_VERIFICATION(R.string.caption_signal_verification, R.drawable.ic_alert_24dp);

	private int friendlyNameResourceId;
	private int iconResourceId;

	EbsSection(int friendlyNameResourceId, int iconResourceId) {
		this.friendlyNameResourceId = friendlyNameResourceId;
		this.iconResourceId = iconResourceId;
	}

	public static EbsSection fromOrdinal(int ordinal) {
		return EbsSection.values()[ordinal];
	}

	@Override
	public String getFriendlyName(Context context) {
		return context.getResources().getString(friendlyNameResourceId);
	}

	@Override
	public int getColorIndicatorResource() {
		return 0;
	}

	@Override
	public Enum getValue() {
		return this;
	}

	@Override
	public int getIconResourceId() {
		return iconResourceId;
	}
}
