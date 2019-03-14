package br.com.hitss.fieldservicemobile.rest;

import java.util.List;
import java.util.Map;

import br.com.hitss.fieldservicemobile.model.Ticket;
import br.com.hitss.fieldservicemobile.model.TicketHistory;
import br.com.hitss.fieldservicemobile.model.UserFs;
import br.com.hitss.fieldservicemobile.model.UserLocationHistory;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FieldserviceAPI {

    /*headers.add(HttpHeaders.CONTENT_LENGTH, "0");
            headers.add("X-User-Agent", "FIELD_SERVICE_MOBILE");*/

    @Headers({
            "Accept: application/json",
            "X-User-Agent: FIELD_SERVICE_MOBILE"
    })
    @GET("users/login")
    Call<UserFs> login(@HeaderMap Map<String, String> headers);

    @Headers({
            "Accept: application/json",
            "X-User-Agent: FIELD_SERVICE_MOBILE"
    })
    @GET("tickets")
    Call<List<Ticket>> findByidUserLogged(@Query("idUserTechnician") Long idUserTechnician,
                                          @Query("idsTicketStatus") String idsTicketStatus);

    @Headers({
            "Accept: application/json",
            "X-User-Agent: FIELD_SERVICE_MOBILE"
    })
    @GET("tickets")
    Call<List<Ticket>> findAll();

    @Headers({
            "Accept: application/json",
            "X-User-Agent: FIELD_SERVICE_MOBILE"
    })
    @GET("tickets/{idTicket}")
    Call<Ticket> findById(@Path("idTicket")Long idTicket);

    @Headers({
            "Accept: application/json",
            "X-User-Agent: FIELD_SERVICE_MOBILE"
    })
    @POST("tickets/{idTicket}/history")
    Call<Void> postHistoryByIdTicket(@Header("Authentication") String jwt, @Path("idTicket")Long idTicket, @Body TicketHistory ticketHistory);

    @Headers({
            "Accept: application/json",
            "X-User-Agent: FIELD_SERVICE_MOBILE"
    })
    @POST("users/{idUserFs}/logout")
    Call<Void> postLogoff(@Path("idUserFs") Long idUserFs);

    @Headers({
            "Accept: application/json",
            "X-User-Agent: FIELD_SERVICE_MOBILE"
    })
    @POST("users/{idUserFs}/locationhistory")
    Call<Void> postUserLocationHistory(@Path("idUserFs") Long idUserFs, @Body UserLocationHistory userLocationHistory);

    @Headers({
            "Accept: application/json",
            "X-User-Agent: FIELD_SERVICE_MOBILE"
    })
    @POST("users/{idUserFs}/locationhistories")
    Call<Void> postUserLocationHistories(@Path("idUserFs") Long idUserFs, @Body List<UserLocationHistory> userLocationHistories);


}
