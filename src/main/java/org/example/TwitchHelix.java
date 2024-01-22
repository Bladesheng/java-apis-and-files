package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.models.UserIdData;
import org.example.models.VideosData;

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

    public String fetchUserID(String username) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.twitch.tv/helix/users?login=" + username))
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

    public VideosData fetchVideosData(String userID) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.twitch.tv/helix/videos?&first=100&user_id=" + userID))
                .GET()
                .header("Authorization", "Bearer " + bearer.getToken())
                .header("Client-Id", clientID)
                .build();


        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(body, VideosData.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
