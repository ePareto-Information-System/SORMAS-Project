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

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.security.PermitAll;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import org.apache.commons.lang3.StringUtils;

import de.symeda.sormas.api.utils.AuthenticationThrottledException;

/**
 * Lockout for repeated failed credential validations, keyed on the submitted user name.
 * <p/>
 * SORMAS has no brute force protection of its own: the container's Basic authentication accepts an unlimited number of guesses. That is
 * tolerable as long as every caller must already hold valid credentials, but {@link UserFacadeEjb#authenticateUser} lets one caller test
 * credentials belonging to arbitrary other users, so attempts against it have to be bounded.
 * <p/>
 * State is per server instance and in memory only; it is not shared across a cluster and is lost on restart.
 */
@Singleton
@LocalBean
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@PermitAll
public class AuthenticationThrottle {

	/**
	 * Number of failures within {@link #FAILURE_WINDOW_MINUTES} that triggers a lockout.
	 */
	static final int MAX_FAILED_ATTEMPTS = 10;

	/**
	 * Sliding window in which failures are counted. Failures older than this do not count towards a lockout.
	 */
	static final int FAILURE_WINDOW_MINUTES = 15;

	/**
	 * How long a user name is locked out once {@link #MAX_FAILED_ATTEMPTS} has been reached.
	 */
	static final int LOCKOUT_MINUTES = 15;

	/**
	 * Upper bound on tracked user names, so that a flood of made up user names cannot grow the map without limit.
	 */
	private static final int MAX_TRACKED_USER_NAMES = 10_000;

	private final ConcurrentHashMap<String, FailureState> failuresByUserName = new ConcurrentHashMap<>();

	/**
	 * @throws AuthenticationThrottledException
	 *             if the user name is currently locked out, in which case the password must not even be looked at
	 */
	public void verifyNotLockedOut(String userName) {

		FailureState state = failuresByUserName.get(key(userName));
		if (state == null) {
			return;
		}

		long remainingNanos = state.lockedUntilNanos - System.nanoTime();
		if (remainingNanos > 0) {
			int retryAfterSeconds = (int) Math.max(1, TimeUnit.NANOSECONDS.toSeconds(remainingNanos));
			throw new AuthenticationThrottledException(
				"Too many failed authentication attempts. Try again in " + retryAfterSeconds + " seconds.",
				retryAfterSeconds);
		}
	}

	public void registerFailure(String userName) {

		long now = System.nanoTime();
		pruneIfNeeded(now);

		failuresByUserName.compute(key(userName), (k, state) -> {
			FailureState current = state;
			if (current == null || now - current.windowStartNanos > TimeUnit.MINUTES.toNanos(FAILURE_WINDOW_MINUTES)) {
				current = new FailureState(now);
			}

			current.failures++;
			if (current.failures >= MAX_FAILED_ATTEMPTS) {
				current.lockedUntilNanos = now + TimeUnit.MINUTES.toNanos(LOCKOUT_MINUTES);
				current.failures = 0;
				current.windowStartNanos = now;
			}

			return current;
		});
	}

	public void registerSuccess(String userName) {
		failuresByUserName.remove(key(userName));
	}

	private static String key(String userName) {
		// lower cased so that varying the capitalization cannot be used to get a fresh set of attempts
		return StringUtils.lowerCase(StringUtils.trimToEmpty(userName), Locale.ROOT);
	}

	private void pruneIfNeeded(long now) {

		if (failuresByUserName.size() < MAX_TRACKED_USER_NAMES) {
			return;
		}

		Iterator<Map.Entry<String, FailureState>> iterator = failuresByUserName.entrySet().iterator();
		while (iterator.hasNext()) {
			FailureState state = iterator.next().getValue();
			boolean lockoutOver = now - state.lockedUntilNanos >= 0;
			boolean windowOver = now - state.windowStartNanos > TimeUnit.MINUTES.toNanos(FAILURE_WINDOW_MINUTES);
			if (lockoutOver && windowOver) {
				iterator.remove();
			}
		}
	}

	private static final class FailureState {

		private long windowStartNanos;
		private long lockedUntilNanos;
		private int failures;

		private FailureState(long windowStartNanos) {
			this.windowStartNanos = windowStartNanos;
			this.lockedUntilNanos = windowStartNanos;
		}
	}
}
