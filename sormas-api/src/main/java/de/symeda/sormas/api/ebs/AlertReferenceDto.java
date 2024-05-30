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
import de.symeda.sormas.api.utils.YesNo;

public class AlertReferenceDto extends ReferenceDto {

	private static final long serialVersionUID = -1399197327930368752L;

	public AlertReferenceDto() {

	}

	public AlertReferenceDto(String uuid) {
		this.setUuid(uuid);
	}

	public AlertReferenceDto(String uuid, String caption) {
		this.setUuid(uuid);
		this.setCaption(caption);
	}

	public AlertReferenceDto(
		String uuid,
		YesNo actionInitiated) {
		this.setUuid(uuid);
		this.setCaption(buildCaption(actionInitiated));
	}

	public AlertReferenceDto(
		String uuid,
		ResponseStatus responseStatus) {
		this.setUuid(uuid);
		this.setCaption(buildCaption(responseStatus));
	}

	public AlertReferenceDto(
			ResponseStatus responseStatus) {
		this.setCaption(buildCaption(responseStatus));
	}

	public AlertReferenceDto(
			YesNo actionInitiated) {
		this.setCaption(buildCaption(actionInitiated));
	}

	public static String buildCaption(
			ResponseStatus responseStatus) {
		if(responseStatus == null){
			return "";
		}
		return responseStatus.toString();
	}

	public static String buildCaption(
			YesNo actionInitiated) {
		if(actionInitiated == null){
			return "";
		}
		return actionInitiated.toString();
	}
}
