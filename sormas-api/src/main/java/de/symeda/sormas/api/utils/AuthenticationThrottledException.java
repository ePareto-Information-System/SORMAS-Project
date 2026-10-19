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

package de.symeda.sormas.api.utils;

import javax.ejb.ApplicationException;

/**
 * Thrown when too many failed authentication attempts have been made for a user name in a short period of time and further attempts are
 * being rejected without even looking at the submitted password.
 */
@ApplicationException(rollback = false)
public class AuthenticationThrottledException extends RuntimeException {

	private static final long serialVersionUID = 1215741825873291684L;

	private final int retryAfterSeconds;

	public AuthenticationThrottledException(String message, int retryAfterSeconds) {
		super(message);
		this.retryAfterSeconds = retryAfterSeconds;
	}

	public int getRetryAfterSeconds() {
		return retryAfterSeconds;
	}
}
