package br.com.hitss.fieldservicemobile.rest;

import android.util.Base64;

import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import br.com.hitss.fieldservicemobile.model.UserFs;
import okhttp3.CertificatePinner;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class CustomTrust {
    private final OkHttpClient client;
    private Retrofit retrofit;

    public CustomTrust() {
        X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory;
        try {
            trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { trustManager }, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        client = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustManager)
                .build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .build();


    }

    public void run(String user, String pass) throws Exception {
        String base64CredsBytes = Base64.encodeToString("web.mobile:wm12345".getBytes(), Base64.NO_WRAP);

        retrofit.newBuilder()
                .baseUrl("https://fieldserviceshmg.embratel.com.br:8443/fieldservice/v1/users");


        Request request = new Request.Builder()
                .header(HttpHeaders.AUTHORIZATION, "Basic " + base64CredsBytes)
                .url("https://fieldserviceshmg.embratel.com.br:8443/fieldservice/v1/users")
                .build();

        client.newCall(request).execute();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }

            System.out.println(response.body().string());
        }
    }

    /**
     * Returns an input stream containing one or more certificate PEM files. This implementation just
     * embeds the PEM files in Java strings; most applications will instead read this from a resource
     * file that gets bundled with the application.
     */
    private InputStream trustedCertificatesInputStream() {
        // PEM files for root certificates of Comodo and Entrust. These two CAs are sufficient to view
        // https://publicobject.com (Comodo) and https://squareup.com (Entrust). But they aren't
        // sufficient to connect to most HTTPS sites including https://godaddy.com and https://visa.com.
        // Typically developers will need to get a PEM file from their organization's TLS administrator.
        String comodoRsaCertificationAuthority = "-----BEGIN CERTIFICATE-----\n" +
                "MIIFijCCBHKgAwIBAgIRAPcM6YF1PtsMeV8qbYdB4xUwDQYJKoZIhvcNAQELBQAw\n" +
                "gcUxCzAJBgNVBAYTAkJSMRMwEQYDVQQIDApTw6NvIFBhdWxvMR4wHAYDVQQHDBVT\n" +
                "w6NvIEpvc8OpIGRvcyBDYW1wb3MxSzBJBgNVBAoMQlRydXN0U2lnbiBDZXJ0aWZp\n" +
                "Y2Fkb3JhIERpZy4gJiBTb2x1w6fDtWVzIFNlZ3VyYW7Dp2EgZGEgSW5mLiBMdGRh\n" +
                "LjE0MDIGA1UEAxMrVHJ1c3RTaWduIEJSIENlcnRpZmljYXRpb24gQXV0aG9yaXR5\n" +
                "IChEVikgMjAeFw0xODAxMTcwMDAwMDBaFw0yMDAxMTcyMzU5NTlaMF8xITAfBgNV\n" +
                "BAsTGERvbWFpbiBDb250cm9sIFZhbGlkYXRlZDEeMBwGA1UECxMVRXNzZW50aWFs\n" +
                "U1NMIFdpbGRjYXJkMRowGAYDVQQDDBEqLmVtYnJhdGVsLmNvbS5icjCCASIwDQYJ\n" +
                "KoZIhvcNAQEBBQADggEPADCCAQoCggEBANVftTnaO0Q4ABmwXSHa33+xUsWLmAK/\n" +
                "xyhJj/V+XBFyeyokRiDz6+N8PorRjmfuE0e9BmuM95C5Te8g9/9fl3RWzzZR4bh7\n" +
                "Y7M5SFc4SaEYHSferd2e7Rwrt/Cm8Jf2H62uL9/k/EbHVA05WskX/WjlPEPm555Y\n" +
                "asanjzAMopp/SzqL/gUTaVpQHdUhJAABWCgkVl2wXRv7tHgqExnHaNqJ6sFCUOWU\n" +
                "JvI5qf1gw4OHmoM2C9TjbQw7hJsF2vtK6j1lQaUyVZSo8+75PdZLAMVU7pQTxEPF\n" +
                "QFSjGNMNrwIWcPeaf/Mj7k+DzMy01uWW8EdSZGsQY2Jlm+vIuiTON3kCAwEAAaOC\n" +
                "AdgwggHUMB8GA1UdIwQYMBaAFIGRtics8qdvjP/PGhp2JUt+dBUOMB0GA1UdDgQW\n" +
                "BBRg+b/lWZc2Bkx/+gR0wasrxdEvPDAOBgNVHQ8BAf8EBAMCBaAwDAYDVR0TAQH/\n" +
                "BAIwADAdBgNVHSUEFjAUBggrBgEFBQcDAQYIKwYBBQUHAwIwSwYDVR0gBEQwQjA2\n" +
                "BgsrBgEEAbIxAQICJjAnMCUGCCsGAQUFBwIBFhlodHRwczovL2Nwcy51c2VydHJ1\n" +
                "c3QuY29tMAgGBmeBDAECATBSBgNVHR8ESzBJMEegRaBDhkFodHRwOi8vY3JsLnVz\n" +
                "ZXJ0cnVzdC5jb20vVHJ1c3RTaWduQlJDZXJ0aWZpY2F0aW9uQXV0aG9yaXR5RFYy\n" +
                "LmNybDCBhAYIKwYBBQUHAQEEeDB2ME0GCCsGAQUFBzAChkFodHRwOi8vY3J0LnVz\n" +
                "ZXJ0cnVzdC5jb20vVHJ1c3RTaWduQlJDZXJ0aWZpY2F0aW9uQXV0aG9yaXR5RFYy\n" +
                "LmNydDAlBggrBgEFBQcwAYYZaHR0cDovL29jc3AudXNlcnRydXN0LmNvbTAtBgNV\n" +
                "HREEJjAkghEqLmVtYnJhdGVsLmNvbS5icoIPZW1icmF0ZWwuY29tLmJyMA0GCSqG\n" +
                "SIb3DQEBCwUAA4IBAQBKHxE0Ma7CHq0Z3tQD/hIXfNoWfH6glb3wVG7MIaxX73/T\n" +
                "8ai3EvgP/9KFzc5FkNtB+T4fQLp0/UJDwGjEND+Ko8i3sk59C7SAy5kG2HFunHfi\n" +
                "uoZoEMuL69wEvfh7nK+nk+krKMa13pViRHXwj3eCCymgXMbyyZd86qc7r8XkcURo\n" +
                "P3O0WvA/dKjjgAuThucGiybMcAum/faW+0fnTRuUifyvKIrz56nuuHmrY1FMre76\n" +
                "+t6Npk3ASMsuG2HLi1p4WORyUPX7MIKWnl2Fv4A4s6+MMaDf41HnJEmu9F7e4gh0\n" +
                "38aJoxSUIUHXCzgUcOQKB5gBsg06oGLOYVOp2Qkz\n" +
                "-----END CERTIFICATE-----";
        return new Buffer()
                .writeUtf8(comodoRsaCertificationAuthority)
                .inputStream();
    }

    /**
     * Returns a trust manager that trusts {@code certificates} and none other. HTTPS services whose
     * certificates have not been signed by these certificates will fail with a {@code
     * SSLHandshakeException}.
     *
     * <p>This can be used to replace the host platform's built-in trusted certificates with a custom
     * set. This is useful in development where certificate authority-trusted certificates aren't
     * available. Or in production, to avoid reliance on third-party certificate authorities.
     *
     * <p>See also {@link CertificatePinner}, which can limit trusted certificates while still using
     * the host platform's built-in trust store.
     *
     * <h3>Warning: Customizing Trusted Certificates is Dangerous!</h3>
     *
     * <p>Relying on your own trusted certificates limits your server team's ability to update their
     * TLS certificates. By installing a specific set of trusted certificates, you take on additional
     * operational complexity and limit your ability to migrate between certificate authorities. Do
     * not use custom trusted certificates in production without the blessing of your server's TLS
     * administrator.
     */
    private X509TrustManager trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        // Put the certificates a key store.
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public static void main(String... args) throws Exception {
        new CustomTrust().run(args[0],args[1]);
    }
}
