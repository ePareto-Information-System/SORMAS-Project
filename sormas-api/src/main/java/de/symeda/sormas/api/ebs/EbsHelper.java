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
package de.symeda.sormas.api.ebs;

import de.symeda.sormas.api.event.InstitutionalPartnerType;
import de.symeda.sormas.api.event.MeansOfTransport;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DateFormatHelper;

import java.util.Date;

public final class EbsHelper {

	private EbsHelper() {
		// Hide Utility Class Constructor
	}

	public static String buildInstitutionalPartnerTypeString(
		InstitutionalPartnerType institutionalPartnerType,
		String institutionalPartnerTypeDetails) {

		if (institutionalPartnerType == InstitutionalPartnerType.OTHER) {
			return DataHelper.toStringNullable(institutionalPartnerTypeDetails);
		}

		return DataHelper.toStringNullable(institutionalPartnerType);
	}

	public static String buildEbsDateString(Date eventStartDate, Date eventEndDate) {

		if (eventStartDate == null) {
			return "";
		} else if (eventEndDate == null) {
			return DateFormatHelper.formatLocalDateTime(eventStartDate);
		} else {
			return String.format("%s - %s", DateFormatHelper.formatLocalDateTime(eventStartDate), DateFormatHelper.formatLocalDateTime(eventEndDate));
		}
	}

	public static String buildMeansOfTransportString(MeansOfTransport meansOfTransport, String meansOfTransportDetails) {

		if (meansOfTransport == MeansOfTransport.OTHER) {
			return DataHelper.toStringNullable(meansOfTransportDetails);
		}

		return DataHelper.toStringNullable(meansOfTransport);
	}

	public static Date getStartOrEndDate(Date eventStartDate, Date eventEndDate) {
		return eventStartDate != null ? eventStartDate : eventEndDate;
	}

}
