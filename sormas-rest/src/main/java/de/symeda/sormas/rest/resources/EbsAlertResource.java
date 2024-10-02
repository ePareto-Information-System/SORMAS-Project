/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2023 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
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

package de.symeda.sormas.rest.resources;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.ebs.EbsAlertDto;
import de.symeda.sormas.rest.resources.base.EntityDtoResource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.function.UnaryOperator;

@Path("/ebsAlert")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class EbsAlertResource extends EntityDtoResource<EbsAlertDto> {

	@GET
	@Path("/all/{since}/{size}/{lastSynchronizedUuid}")
	public List<EbsAlertDto> getAllAlerts(@PathParam("since") long since) {
		return FacadeProvider.getAlertFacade().getAllAfter(new Date(since));
	}

	@GET
	@Path("/{uuid}")
	public EbsAlertDto getByUuid(@PathParam("uuid") String uuid) {
		return FacadeProvider.getAlertFacade().getAlertByUuid(uuid, true);
	}

	@GET
	@Path("/uuids")
	public List<String> getAllActiveUuids() {
		return FacadeProvider.getAlertFacade().getAllActiveUuids();
	}

	@Override
	public UnaryOperator<EbsAlertDto> getSave() {
		return FacadeProvider.getAlertFacade()::saveAlert;
	}

	@Override
	public Response postEntityDtos(List<EbsAlertDto> ebaDtos) {
		return super.postEntityDtos(ebaDtos);
	}
}
