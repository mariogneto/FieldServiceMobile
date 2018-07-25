package br.com.hitss.fieldservicemobile.rest;

import android.util.Base64;
import android.util.Log;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import br.com.hitss.fieldservicemobile.model.Ticket;
import br.com.hitss.fieldservicemobile.model.TicketHistory;

public class TicketRestClient {

    private static final String TAG = TicketRestClient.class.getSimpleName();

    private String BASE_URL = "http://10.172.16.78:7080/fieldservice/v1/tickets/";
    private RestTemplate restTemplate;

    public TicketRestClient() {
        restTemplate = new RestTemplate();
    }

    public Ticket findById(Long id) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            String plainCreds = "web.mobile:wm12345";
            byte[] plainCredsBytes = plainCreds.getBytes();
            byte[] base64CredsBytes = Base64.encode(plainCredsBytes, Base64.DEFAULT);
            String base64Creds = new String(base64CredsBytes);
            headers.add(HttpHeaders.AUTHORIZATION, "Basic " + base64Creds);
            headers.add(HttpHeaders.CONTENT_LENGTH, "0");
            headers.add("X-User-Agent", "FIELD_SERVICE_MOBILE");

            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            return restTemplate.exchange(
                    BASE_URL + id,
                    HttpMethod.GET, entity,
                    new ParameterizedTypeReference<Ticket>() {
                    }).getBody();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao buscar ticket", e);
            throw e;
        }
    }

    public List<Ticket> findByidUserLogged(Long idUserFs) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            String plainCreds = "web.mobile:wm12345";
            byte[] plainCredsBytes = plainCreds.getBytes();
            byte[] base64CredsBytes = Base64.encode(plainCredsBytes, Base64.DEFAULT);
            String base64Creds = new String(base64CredsBytes);
            headers.add(HttpHeaders.AUTHORIZATION, "Basic " + base64Creds);
            headers.add(HttpHeaders.CONTENT_LENGTH, "0");
            headers.add("X-User-Agent", "FIELD_SERVICE_MOBILE");

            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            return restTemplate.exchange(
                    BASE_URL + "?idUserTechnician=" + idUserFs + "&idsTicketStatus=2,3,4",
                    HttpMethod.GET, entity,
                    new ParameterizedTypeReference<List<Ticket>>() {
                    }).getBody();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao buscar tickets", e);
            throw e;
        }
    }

    public void postHistoryByIdTicket(TicketHistory ticketHistory) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String plainCreds = "web.mobile:wm12345";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encode(plainCredsBytes, Base64.DEFAULT);
        String base64Creds = new String(base64CredsBytes);
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + base64Creds);

        HttpEntity<TicketHistory> request = new HttpEntity<>(new TicketHistory(ticketHistory), headers);

        try {
            restTemplate.exchange(
                    BASE_URL + ticketHistory.getIdTicket() + "/history", HttpMethod.POST, request, TicketHistory.class);
        } catch (Exception e) {
            Log.e(TAG, "erro ao fazer POST de TicketHistory.", e);
            throw e;
        }
    }
}
