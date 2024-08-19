package de.symeda.sormas.rest.resources;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.infrastructure.fields.FormFieldsDto;
import de.symeda.sormas.api.infrastructure.forms.FormBuilderDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

@Path("/formFields")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class FormFieldResource {

    @GET
    @Path("/all/{since}")
    public List<FormFieldsDto> getAllFormBuilders(@PathParam("since") long since) {
        return FacadeProvider.getFormFieldFacade().getAllAfter(new Date(since));
    }

    @POST
    @Path("/query")
    public List<FormFieldsDto> getByUuids(List<String> uuids) {
        List<FormFieldsDto> listOfFormDts =  FacadeProvider.getFormFieldFacade().getByUuids(uuids);
        return listOfFormDts;
    }

    @GET
    @Path("/uuids")
    public List<String> getAllUuids() {
        return FacadeProvider.getFormFieldFacade().getAllUuids();
    }

}
