package de.symeda.sormas.rest.resources;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.infrastructure.forms.FormBuilderDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

@Path("/formBuilders")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class FormBuilderResource {

    @GET
    @Path("/all/{since}")
    public List<FormBuilderDto> getAllFormBuilders(@PathParam("since") long since) {
         return FacadeProvider.getFormBuilderFacade().getAllAfter(new Date(since));
    }

    @POST
    @Path("/query")
    public List<FormBuilderDto> getByUuids(List<String> uuids) {
        return FacadeProvider.getFormBuilderFacade().getByUuids(uuids);
    }

    @GET
    @Path("/uuids")
    public List<String> getAllUuids() {
        return FacadeProvider.getFormBuilderFacade().getAllUuids();
    }

}