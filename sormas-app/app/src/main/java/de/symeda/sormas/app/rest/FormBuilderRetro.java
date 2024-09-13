package de.symeda.sormas.app.rest;

import java.util.List;

import de.symeda.sormas.api.infrastructure.forms.FormBuilderDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FormBuilderRetro {
    @GET("formBuilders/all/{since}")
    Call<List<FormBuilderDto>> pullAllSince(@Path("since") long since);

    @POST("formBuilders/query")
    Call<List<FormBuilderDto>> pullByUuids(@Body List<String> uuids);

    @GET("formBuilders/uuids")
    Call<List<String>> pullUuids();
}
