package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VideosData {
    private VideoData[] data;

    public VideoData[] getData() {
        return data;
    }
}
