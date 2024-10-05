package com.punk_pozer;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class KVDSConnection {

    private static final Long DEFAULT_CONNECTION_TIMEOUT_MILLIS = 5000L;
    private final HttpClient client;

    final private String baseURI;

    public KVDSConnection(String uriString) throws URISyntaxException {
        baseURI = uriString;

        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(DEFAULT_CONNECTION_TIMEOUT_MILLIS))
                .build();
    }

    public HttpResponse<String> checkConnection() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseURI + "/api"))
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> saveDump() throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseURI + "/api/dump/save"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("mode=DEFAULT"))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> saveDumpSoft() throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseURI + "/api/dump/save"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("mode=SOFT"))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> loadDump() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseURI + "/api/dump/load"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public String getValueByKey(String key) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseURI + "/api/get?key=" + key))
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public HttpResponse<String> setOrUpdate(String key, String value) throws IOException, InterruptedException, URISyntaxException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseURI + "/api/set"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "key="+key
                                +"&value="+value))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());

    }

    public HttpResponse<String> setOrUpdate(String key, String value, Long ttl) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseURI + "/api/set"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "key=" + key
                                +"&value=" + value
                                +"&ttl=" + ttl))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        response.body();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> remove(String key) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseURI + "/api/remove"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "key="+key))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
