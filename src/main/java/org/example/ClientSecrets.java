package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ClientSecrets {
    private String clientID = "";
    private String clientSecret = "";

    public ClientSecrets() {
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get("clientSecrets.json"));

            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, String> jsonMap = objectMapper.readValue(jsonData, new TypeReference<HashMap<String, String>>() {
            });

            clientID = jsonMap.get("clientID");
            clientSecret = jsonMap.get("clientSecret");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getClientID() {
        return clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
