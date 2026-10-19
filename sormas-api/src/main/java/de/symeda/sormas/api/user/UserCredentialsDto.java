/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2025 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.api.user;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.utils.FieldConstraints;

/**
 * Credentials of an end user, submitted by a trusted external system that wants SORMAS to validate them on the user's behalf.
 * <p/>
 * Only ever used as a request payload; the password is never persisted and never returned. {@link #toString()} deliberately omits it so
 * that the credentials cannot leak into audit entries, log statements or stack traces.
 *
 * @see UserFacade#authenticateUser(UserCredentialsDto)
 */
public class UserCredentialsDto implements Serializable {

	private static final long serialVersionUID = 7193491882940116521L;

	@NotBlank(message = Validations.required)
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String userName;

	@NotBlank(message = Validations.required)
	@Size(max = FieldConstraints.CHARACTER_LIMIT_DEFAULT, message = Validations.textTooLong)
	private String password;

	public UserCredentialsDto() {
	}

	public UserCredentialsDto(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		// the password must never be part of the string representation
		return "UserCredentialsDto(userName=" + userName + ")";
	}
}
