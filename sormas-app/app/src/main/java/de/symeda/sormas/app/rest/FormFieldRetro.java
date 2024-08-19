package de.symeda.sormas.app.rest;

import java.util.List;

import de.symeda.sormas.api.infrastructure.fields.FormFieldsDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FormFieldRetro {
    @GET("formFields/all/{since}")
    Call<List<FormFieldsDto>> pullAllSince(@Path("since") long since);

    @POST("formFields/query")
    Call<List<FormFieldsDto>> pullByUuids(@Body List<String> uuids);

    @GET("formFields/uuids")
    Call<List<String>> pullUuids();
}
