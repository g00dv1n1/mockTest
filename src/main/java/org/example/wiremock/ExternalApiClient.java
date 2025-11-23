package org.example.wiremock;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

public class ExternalApiClient {

    private final String baseUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final int timeoutMillis;

    public ExternalApiClient(String baseUrl, int timeoutMillis) {
        this.baseUrl = baseUrl;
        this.timeoutMillis = timeoutMillis;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(timeoutMillis))
                .build();
    }

    public String fetchTitle() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/data"))
                .GET()
                .timeout(Duration.ofMillis(timeoutMillis))
                .build();

        try {
            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();
            String body = response.body();
            String contentType = response.headers()
                    .firstValue("Content-Type")
                    .orElse("");

            if (status == 404) {
                return "Not found";
            }

            if (status == 500) {
                return "Server error: " + body;
            }

            if (!contentType.contains("application/json")) {
                return "Unexpected format";
            }

            if (status == 200) {
                try {
                    JsonNode root = objectMapper.readTree(body);
                    String title = root.path("title").asText(null);
                    if (title == null) {
                        return "Parse error";
                    }
                    return "Title: " + title;
                } catch (IOException e) {
                    return "Parse error";
                }
            }

            return "Unexpected status: " + status;

        } catch (HttpTimeoutException e) {
            return "Timeout";
        } catch (IOException | InterruptedException e) {
            return "General error";
        }
    }
}
