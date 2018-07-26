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

import br.com.hitss.fieldservicemobile.model.UserFs;
import br.com.hitss.fieldservicemobile.model.UserLocationHistory;

public class UserRestClient {

    private static final String TAG = UserRestClient.class.getSimpleName();

    private final String BASE_URL = "http://10.172.16.78:7080/fieldservice/v1/users/";
    private final RestTemplate restTemplate = new RestTemplate();

    public void postUserLocationHistory(String idUserFs, Double latitude, Double longitude) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String plainCreds = "web.mobile:wm12345";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encode(plainCredsBytes, Base64.DEFAULT);
        String base64Creds = new String(base64CredsBytes);
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + base64Creds);

        HttpEntity<UserLocationHistory> request = new HttpEntity<>(new UserLocationHistory(latitude, longitude), headers);

        try {
            restTemplate.exchange(
                    BASE_URL + idUserFs + "/locationhistory", HttpMethod.POST, request, UserLocationHistory.class);
        } catch (Exception e) {
            Log.e(TAG, "erro ao fazer POST de UserLocationHistory.", e);
            throw e;
        }
    }

    public void postLogoff(String idUserFs) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String plainCreds = "web.mobile:wm12345";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encode(plainCredsBytes, Base64.DEFAULT);
        String base64Creds = new String(base64CredsBytes);
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + base64Creds);

        HttpEntity request = new HttpEntity(headers);

        try {
            restTemplate.exchange(
                    BASE_URL + idUserFs + "/logout", HttpMethod.POST, request, Object.class);
        } catch (Exception e) {
            Log.e(TAG, "erro ao fazer POST de UserLocationHistory.", e);
            throw e;
        }
    }

    public UserFs login(String mEmail, String mPassword) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String plainCreds = "web.mobile:wm12345";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encode(plainCredsBytes, Base64.DEFAULT);
        String base64Creds = new String(base64CredsBytes);
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + base64Creds);
        headers.add("login", mEmail);
        headers.add("password", mPassword);

        HttpEntity entity = new HttpEntity(headers);
        try {
            return restTemplate.exchange(
                    BASE_URL + "login", HttpMethod.GET, entity,
                    new ParameterizedTypeReference<UserFs>() {
                    }).getBody();
        } catch (Exception e) {
            Log.e(TAG, "erro ao fazer login", e);
            throw e;
        }
    }
}