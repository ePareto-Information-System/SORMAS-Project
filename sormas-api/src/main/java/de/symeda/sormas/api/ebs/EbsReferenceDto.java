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

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.Language;
import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.event.EventInvestigationStatus;
import de.symeda.sormas.api.event.EventStatus;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.api.utils.DependingOnFeatureType;

import java.util.Date;

@DependingOnFeatureType(featureType = FeatureType.EBS_SURVEILLANCE)
public class EbsReferenceDto extends ReferenceDto {

	private static final long serialVersionUID = 2430932452606853497L;

	public EbsReferenceDto() {

	}

	public EbsReferenceDto(String uuid) {
		setUuid(uuid);
	}

	public EbsReferenceDto(String uuid, String caption) {
		setUuid(uuid);
		setCaption(caption);
	}

	public EbsReferenceDto(
		String uuid,
		Date triageDate) {
		setUuid(uuid);
		setCaption(buildCaption(triageDate));
	}

	@Override
	public String getCaption() {
		return super.getCaption();
	}

	public static String buildCaption(
		Date TriageDate) {
		Language language = I18nProperties.getUserLanguage();
		if(TriageDate == null) {
			return "";
		}
		return DateHelper.formatLocalDate(TriageDate, language);
	}
}
