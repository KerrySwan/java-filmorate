package ru.yandex.practicum.filmorate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpFlimorateClient {

    public static HttpResponse<String> sendPostRequest(String endpoint, String jsonBody){
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(endpoint);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .setHeader("Content-Type", "application/json")
                .setHeader("Accept", "*/*")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .uri(uri)
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("json POST-request had not been sent");
        }
        return null;
    }

    public static HttpResponse<String> sendPutRequest(String endpoint, String jsonBody){
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(endpoint);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .setHeader("Content-Type", "application/json")
                .setHeader("Accept", "*/*")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .uri(uri)
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("json POST-request had not been sent");
        }
        return null;
    }

    public static HttpResponse<String> sendGetRequest(String endpoint){
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(endpoint);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .setHeader("Content-Type", "application/json")
                .setHeader("Accept", "*/*")
                .GET()
                .uri(uri)
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("json POST-request had not been sent");
        }
        return null;
    }

}
