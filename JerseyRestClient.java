package org.act.rest.client;

/**
 * Created by Karthick_S19 on 6/2/2016.
 */

import java.util.Map;

import javax.ws.rs.client.*;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import javax.net.ssl.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Configuration;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.glassfish.jersey.client.ClientConfig;


public class JerseyRestClient {
    private final Client client;


    public JerseyRestClient() {
        ClientConfig config = new ClientConfig();

        client = initClient(config);
    }

    /**
     * Invoke Http GET REST client method
     *
     * @param restInput
     * @return
     */
    public Response doGet(RestInput restInput) {
        Response response = null;
        try {
            Builder builder = getBuilder(restInput);

            response = builder.get();
        } catch (Exception ex) {
            ex.printStackTrace();
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(stackTrace).build();
        }

        return response;
    }

    /**
     * Invoke Http POST REST client method
     *
     * @param restInput
     * @return
     */

    public Response doPost(RestInput restInput) {
        Response response = null;
        try {

            Builder builder = getBuilder(restInput);
            Entity<String> payload = restInput.getPayload();

            response = builder.post(payload);

        } catch (Exception ex) {
            ex.printStackTrace();
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(stackTrace).build();

        }

        return response;
    }

    /**
     * Prepare the REST client builder
     *
     * @param restInput
     * @return
     */

    private Builder getBuilder(RestInput restInput) {
        if (Objects.isNull(restInput)) {
            throw new RuntimeException("REST input is missing");
        }

        String httpURL = restInput.getHttpURL();
        Map<String, String> params = restInput.getParams();
        Map<String, String> headers = restInput.getHeaders();

        if (StringUtils.isEmpty(httpURL)) {
            throw new RuntimeException("REST httpURL is missing");
        }


        WebTarget webTarget = client.target(httpURL);


        for (Map.Entry<String, String> entry : params.entrySet()) {

            webTarget = webTarget.queryParam(entry.getKey(), entry.getValue());

        }

        Builder builder = webTarget.request();

        for (Map.Entry<String, String> entry : headers.entrySet()) {

            builder.header(entry.getKey(), entry.getValue());

        }
        return builder;
    }

    /**
     * Init REST client SSL certification
     *
     * @param config
     * @return
     */
    private Client initClient(Configuration config) {
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(null, certs, new SecureRandom());

            return ClientBuilder.newBuilder()
                    .withConfig(config)
                    .hostnameVerifier(new TrustAllHostNameVerifier())
                    .sslContext(ctx)
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

    TrustManager[] certs = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }
            }
    };

    private static class TrustAllHostNameVerifier implements HostnameVerifier {

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

    }

}
