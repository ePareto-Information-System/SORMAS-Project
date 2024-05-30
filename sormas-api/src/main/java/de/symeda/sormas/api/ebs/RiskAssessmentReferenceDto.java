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

import de.symeda.sormas.api.ReferenceDto;

public class RiskAssessmentReferenceDto extends ReferenceDto {

	private static final long serialVersionUID = -1399197327930368752L;

	public RiskAssessmentReferenceDto() {

	}

	public RiskAssessmentReferenceDto(String uuid) {
		this.setUuid(uuid);
	}

	public RiskAssessmentReferenceDto(String uuid, String caption) {
		this.setUuid(uuid);
		this.setCaption(caption);
	}

	public RiskAssessmentReferenceDto(
		String uuid,
		RiskAssesment riskStatus) {
		this.setUuid(uuid);
		this.setCaption(buildCaption(riskStatus));
	}

	public RiskAssessmentReferenceDto(
			RiskAssesment riskStatus) {
		this.setCaption(buildCaption(riskStatus));
	}

	public static String buildCaption(
			RiskAssesment riskStatus) {
		if(riskStatus == null){
			return "";
		}
		return riskStatus.toString();
	}
}
