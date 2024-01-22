package org.example;

public class Main {
    public static void main(String[] args) {
        ClientSecrets clientSecrets = new ClientSecrets();

        Bearer bearer = new Bearer(clientSecrets);

        TwitchHelix twitchHelix = new TwitchHelix(bearer, clientSecrets.getClientID());

        String username = "forsen";
        String userID = twitchHelix.getUserID(username);

        System.out.println(clientSecrets.getClientID());
        System.out.println(clientSecrets.getClientSecret());
        System.out.println(bearer.getToken());

        System.out.println(userID);
    }
}