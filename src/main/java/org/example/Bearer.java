package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Bearer {
    private String token = "";

    public Bearer(ClientSecrets clientSecrets) {
        // check file first
        String fileName = "bearerToken.txt";

        try {
            Path path = Path.of(fileName);

            if (Files.exists(path)) {
                token = Files.readString(path);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!token.isEmpty()) {
            return;
        }

        // if there is nothing in file, make request
        HttpClient client = HttpClient.newHttpClient();

        Map<String, String> formData = new HashMap<>();
        formData.put("client_id", clientSecrets.getClientID());
        formData.put("client_secret", clientSecrets.getClientSecret());
        formData.put("grant_type", "client_credentials");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://id.twitch.tv/oauth2/token"))
                .POST(HttpRequest.BodyPublishers.ofString(getFormDataAsString(formData)))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> jsonMap = objectMapper.readValue(body, new TypeReference<HashMap<String, String>>() {
            });
            token = jsonMap.get("access_token");

            // save the token, so we don't have to request it every time
            FileController.saveToFile(fileName, token);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getToken() {
        return token;
    }

    private static String getFormDataAsString(Map<String, String> formData) {
        StringBuilder formBodyBuilder = new StringBuilder();
        for (Map.Entry<String, String> singleEntry : formData.entrySet()) {
            if (formBodyBuilder.length() > 0) {
                formBodyBuilder.append("&");
            }
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getKey(), StandardCharsets.UTF_8));
            formBodyBuilder.append("=");
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getValue(), StandardCharsets.UTF_8));
        }
        return formBodyBuilder.toString();
    }
}
