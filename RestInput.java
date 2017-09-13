package org.act.rest.client;

import javax.ws.rs.client.Entity;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Karthick_S19 on 6/3/2016.
 */
public class RestInput {

    private String httpURL;
    private Map<String, String> params;
    private Map<String, String> headers;
    private Entity<String> payload;

    public RestInput() {
        params = new HashMap<String,String>();
        headers = new HashMap<String,String>();;
    }

    public String getHttpURL() {
        return httpURL;
    }

    public void setHttpURL(String httpURL) {
        this.httpURL = httpURL;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Entity<String> getPayload() {
        return payload;
    }

    public void setPayload(Entity<String> payload) {
        this.payload = payload;
    }

    public void addQueryParam(String key,String value){
        params.put(key, value);
    }

    public void addRequestHeader(String key,String value){
        headers.put(key,value);
    }


}
