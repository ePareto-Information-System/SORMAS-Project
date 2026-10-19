package de.symeda.sormas.rest.exception.mappers;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;

import de.symeda.sormas.api.utils.AuthenticationThrottledException;

@Provider
public class AuthenticationThrottledExceptionMapper implements ExceptionMapper<AuthenticationThrottledException> {

	private static final int SC_TOO_MANY_REQUESTS = 429;

	@Override
	public Response toResponse(AuthenticationThrottledException exception) {

		String message = exception.getLocalizedMessage();
		return Response.status(SC_TOO_MANY_REQUESTS)
			.header(HttpHeaders.RETRY_AFTER, exception.getRetryAfterSeconds())
			.entity(StringUtils.isNotBlank(message) ? message : "Too many failed authentication attempts.")
			.build();
	}
}
