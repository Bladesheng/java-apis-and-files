package org.example;

import org.example.models.VideoData;
import org.example.models.VideosData;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ClientSecrets clientSecrets = new ClientSecrets();

        Bearer bearer = new Bearer(clientSecrets);

        TwitchHelix twitchHelix = new TwitchHelix(bearer, clientSecrets.getClientID());

        String username = "forsen";
        String userID = twitchHelix.fetchUserID(username);

        VideosData videosData = twitchHelix.fetchVideosData(userID);

        List<VOD> vods = new ArrayList<>();

        for (VideoData videoData : videosData.getData()) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            ZonedDateTime time = ZonedDateTime.parse(videoData.getCreated_at(), formatter.withZone(ZoneId.of("UTC")));

            Duration duration = videoData.getDurationObj();

            VOD vod = new VOD(videoData.getTitle(), time, videoData.getUrl(), videoData.getView_count(), duration);

            vods.add(vod);
        }

        Duration avg = vods.stream()
                .map(VOD::getDuration)
                .reduce(Duration.ZERO, Duration::plus)
                .dividedBy(vods.size());

        System.out.println("Number of vods: " + videosData.getData().length);
        System.out.printf("Avg stream duration: %d hours %d minutes%n", avg.toHoursPart(), avg.toMinutesPart());
    }
}