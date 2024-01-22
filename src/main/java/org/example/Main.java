package org.example;

import org.example.models.VideosData;

public class Main {
    public static void main(String[] args) {
        ClientSecrets clientSecrets = new ClientSecrets();

        Bearer bearer = new Bearer(clientSecrets);

        TwitchHelix twitchHelix = new TwitchHelix(bearer, clientSecrets.getClientID());

        String username = "forsen";
        String userID = twitchHelix.fetchUserID(username);

        VideosData videosData = twitchHelix.fetchVideosData(userID);

        System.out.println(clientSecrets.getClientID());
        System.out.println(clientSecrets.getClientSecret());
        System.out.println(bearer.getToken());

        System.out.println(userID);

        System.out.println(videosData.getData()[0].getCreated_at());
        System.out.println(videosData.getData().length);
    }
}