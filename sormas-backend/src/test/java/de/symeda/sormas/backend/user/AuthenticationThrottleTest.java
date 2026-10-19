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

package de.symeda.sormas.backend.user;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.symeda.sormas.api.utils.AuthenticationThrottledException;

/**
 * Plain unit test; {@link AuthenticationThrottle} holds no injected state, so it needs no container.
 */
public class AuthenticationThrottleTest {

	@Test
	public void testUnknownUserNameIsNotLockedOut() {

		AuthenticationThrottle throttle = new AuthenticationThrottle();

		assertDoesNotThrow(() -> throttle.verifyNotLockedOut("j.mensah"));
	}

	@Test
	public void testLockoutAfterConfiguredNumberOfFailures() {

		AuthenticationThrottle throttle = new AuthenticationThrottle();

		for (int i = 0; i < AuthenticationThrottle.MAX_FAILED_ATTEMPTS - 1; i++) {
			throttle.registerFailure("j.mensah");
			assertDoesNotThrow(() -> throttle.verifyNotLockedOut("j.mensah"), "locked out after only " + (i + 1) + " failures");
		}

		throttle.registerFailure("j.mensah");

		AuthenticationThrottledException exception =
			assertThrows(AuthenticationThrottledException.class, () -> throttle.verifyNotLockedOut("j.mensah"));
		assertTrue(exception.getRetryAfterSeconds() > 0, "no retry-after hint given");
	}

	@Test
	public void testSuccessfulAuthenticationClearsFailures() {

		AuthenticationThrottle throttle = new AuthenticationThrottle();

		for (int i = 0; i < AuthenticationThrottle.MAX_FAILED_ATTEMPTS - 1; i++) {
			throttle.registerFailure("j.mensah");
		}
		throttle.registerSuccess("j.mensah");

		// the counter restarted, so the next failure must not lock the account out
		throttle.registerFailure("j.mensah");
		assertDoesNotThrow(() -> throttle.verifyNotLockedOut("j.mensah"));
	}

	@Test
	public void testLockoutIsNotBypassedByChangingCapitalization() {

		AuthenticationThrottle throttle = new AuthenticationThrottle();

		for (int i = 0; i < AuthenticationThrottle.MAX_FAILED_ATTEMPTS; i++) {
			throttle.registerFailure("j.mensah");
		}

		assertThrows(AuthenticationThrottledException.class, () -> throttle.verifyNotLockedOut("J.Mensah"));
	}

	@Test
	public void testLockoutIsPerUserName() {

		AuthenticationThrottle throttle = new AuthenticationThrottle();

		for (int i = 0; i < AuthenticationThrottle.MAX_FAILED_ATTEMPTS; i++) {
			throttle.registerFailure("j.mensah");
		}

		assertThrows(AuthenticationThrottledException.class, () -> throttle.verifyNotLockedOut("j.mensah"));
		assertDoesNotThrow(() -> throttle.verifyNotLockedOut("a.owusu"), "an unrelated user name was locked out too");
	}
}
