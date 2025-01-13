package ingu.flux.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class AbstractHttpClient_SEO {
    private static final String BASE_URL = "http://localhost:8080/demo/products/";
    private final HttpClient httpClient;
    private HttpRequest request;
    private HttpResponse<String> response;

    public AbstractHttpClient_SEO() {
        this.httpClient = HttpClient.newHttpClient();
    }
    protected String request() {
        this.request = HttpRequest.newBuilder(URI.create(BASE_URL)).build();
        return this.send();
    }
    protected String request(int id) {
        this.request = HttpRequest.newBuilder(URI.create(BASE_URL + id)).build();
        return this.send();
    }
    private String send() {
        try {
            response = this.httpClient.send(this.request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
