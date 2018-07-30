package br.com.hitss.fieldservicemobile.rest;

import java.util.Map;

import br.com.hitss.fieldservicemobile.model.UserFs;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;

public interface FieldserviceAPI {

    @Headers({
            "Accept: application/json",
            "User-Agent: Fieldservice Mobile"
    })
    @GET("login/")
    Call<UserFs> login(@HeaderMap Map<String, String> headers);
}
