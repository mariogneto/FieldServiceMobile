package br.com.hitss.fieldservicemobile.rest;

import java.util.List;
import java.util.Map;

import br.com.hitss.fieldservicemobile.model.Ticket;
import br.com.hitss.fieldservicemobile.model.UserFs;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface FieldserviceAPI {

    @Headers({
            "Accept: application/json",
            "User-Agent: Fieldservice Mobile"
    })
    @GET("users/login/")
    Call<UserFs> login(@HeaderMap Map<String, String> headers);

    @Headers({
            "Accept: application/json",
            "User-Agent: Fieldservice Mobile"
    })
    @GET("tickets?idUserTechnician={idUserFs}&idsTicketStatus=2,3,4")
    Call<List<Ticket>> findByidUserLogged(@Path("idUserFs") Long idUserFs);


    @GET("tickets")
    Call<List<Ticket>> findAll();

}
