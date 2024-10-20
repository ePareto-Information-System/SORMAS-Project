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
package de.symeda.sormas.rest;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.auditlog.ChangeType;
import de.symeda.sormas.rest.resources.base.EntityDtoResource;

import java.util.function.UnaryOperator;

@Path("/auditlogentry")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@RolesAllowed({
	"USER",
	"REST_USER" })
public class AuditLogEntryResource extends EntityDtoResource {

	@POST
	@Path("/logactivity/{activityType}/{clazz}/{uuid}")
	public void logActivity(
		@PathParam("activityType") String activityType,
		@PathParam("clazz") String clazz,
		@PathParam("uuid") String uuid) {
		
		ChangeType changeType = ChangeType.valueOf(activityType);
		
		FacadeProvider.getAuditLogEntryFacade().logActivity(changeType, clazz, uuid);
	}

	@Override
	public UnaryOperator getSave() {
		return null;
	}
}
