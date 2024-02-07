/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.api.contact;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.symeda.sormas.api.ReferenceDto;
import de.symeda.sormas.api.feature.FeatureType;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.DependingOnFeatureType;
import de.symeda.sormas.api.utils.EmbeddedPersonalData;
import de.symeda.sormas.api.utils.EmbeddedSensitiveData;
import de.symeda.sormas.api.utils.HasCaption;
import de.symeda.sormas.api.utils.PersonalData;
import de.symeda.sormas.api.utils.SensitiveData;

@DependingOnFeatureType(featureType = FeatureType.CONTACT_TRACING)
public class ContactReferenceDto extends ReferenceDto {

	private static final long serialVersionUID = -7764607075875188799L;

	@EmbeddedPersonalData
	@EmbeddedSensitiveData
	private PersonName contactName;
	@EmbeddedPersonalData
	@EmbeddedSensitiveData
	private PersonName caseName;

	public ContactReferenceDto() {

	}

	public ContactReferenceDto(String uuid) {
		setUuid(uuid);
	}

	public ContactReferenceDto(String uuid, String contactFirstName, String contactLastName, String contactOtherName, String caseFirstName, String caseLastName, String caseOtherName) {

		setUuid(uuid);
		this.contactName = new PersonName(contactFirstName, contactLastName, contactOtherName);

		if (caseFirstName != null && caseLastName != null) {
			this.caseName = new PersonName(caseFirstName, caseLastName, caseOtherName);
		}
	}


	@Override
	public String getCaption() {
		return buildCaption(
			contactName.firstName,
			contactName.lastName,
			contactName.otherName,
			caseName != null ? caseName.firstName : null,
			caseName != null ? caseName.lastName : null,
			caseName != null ? caseName.otherName : null,
			getUuid(),
			true);
	}

	@JsonIgnore
	public String getCaptionAlwaysWithUuid() {
		return buildCaption(
			contactName.firstName,
			contactName.lastName,
			contactName.otherName,
			caseName != null ? caseName.firstName : null,
			caseName != null ? caseName.lastName : null,
			caseName != null ? caseName.otherName : null,
			getUuid(),
			true);
	}

	public PersonName getContactName() {
		return contactName;
	}

	public PersonName getCaseName() {
		return caseName;
	}

	public static String buildCaption(
		String contactFirstName,
		String contactLastName,
		String contactOtherName,
		String caseFirstName,
		String caseLastName,
		String caseOtherName,
		String contactUuid) {
		return buildCaption(contactFirstName, contactLastName, contactOtherName, caseFirstName, caseLastName, caseOtherName, contactUuid, false);
	}

	public static String buildCaption(
		String contactFirstName,
		String contactLastName,
		String contactOtherName,
		String caseFirstName,
		String caseLastName,
		String caseOtherName,
		String contactUuid,
		boolean alwaysShowUuid) {

		StringBuilder builder = new StringBuilder();
		if (!DataHelper.isNullOrEmpty(contactFirstName) || !DataHelper.isNullOrEmpty(contactLastName) || !DataHelper.isNullOrEmpty(contactOtherName)) {
			builder.append(DataHelper.toStringNullable(contactFirstName))
				.append(" ")
				.append(DataHelper.toStringNullable(contactLastName).toUpperCase())
				.append(" ")
				.append(DataHelper.toStringNullable(contactOtherName));
		}

		if (!DataHelper.isNullOrEmpty(caseFirstName) || !DataHelper.isNullOrEmpty(caseLastName) || !DataHelper.isNullOrEmpty(caseOtherName)) {
			builder.append(StringUtils.wrap(I18nProperties.getString(Strings.toCase), " "))
				.append(DataHelper.toStringNullable(caseFirstName))
				.append(" ")
				.append(DataHelper.toStringNullable(caseLastName))
				.append(" ")
				.append(DataHelper.toStringNullable(caseOtherName));
		}

		if (alwaysShowUuid || builder.length() == 0) {
			builder.append(builder.length() > 0 ? " (" + DataHelper.getShortUuid(contactUuid) + ")" : DataHelper.getShortUuid(contactUuid));
		}

		return builder.toString();
	}

	public static class PersonName implements Serializable, HasCaption {

		private static final long serialVersionUID = 3655299579771996044L;

		@PersonalData
		@SensitiveData
		private String firstName;
		@PersonalData
		@SensitiveData
		private String lastName;

		@PersonalData
		@SensitiveData
		private String otherName;

		public PersonName() {
		}

		public PersonName(String firstName, String lastName, String otherName) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.otherName = otherName;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}
		public String getOtherName() {return otherName;}

		public String toString() {
			return firstName + " " + lastName  + " " + otherName ;
		}

		@Override
		public String buildCaption() {
			return null;
		}
	}

	public void setContactName(PersonName contactName) {
		this.contactName = contactName;
	}

	public void setCaseName(PersonName caseName) {
		this.caseName = caseName;
	}
}
