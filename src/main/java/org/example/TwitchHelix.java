package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.models.UserIdData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TwitchHelix {
    private Bearer bearer;
    private String clientID;

    public TwitchHelix(Bearer bearer, String clientId) {
        this.bearer = bearer;
        this.clientID = clientId;
    }

    public String getUserID(String username) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.twitch.tv/helix/users?login=" + username + "&first=100"))
                .GET()
                .header("Authorization", "Bearer " + bearer.getToken())
                .header("Client-Id", clientID)
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            ObjectMapper objectMapper = new ObjectMapper();
            UserIdData userIdData = objectMapper.readValue(body, UserIdData.class);

            return userIdData.getData()[0].getId();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
