package org.example;

import org.example.models.VideoData;
import org.example.models.VideosData;

import java.text.NumberFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

        List<LocalTime> startTimes = vods.stream()
                .map(VOD::getTime)
                .map(ZonedDateTime::toLocalTime)
                .toList();

        LocalTime averageLocalTime = calculateAverageLocalTime(startTimes);
        LocalTime deviation = calculateStdDeviation(startTimes, averageLocalTime);
        System.out.printf("Average stream start time: %s (deviation: %s)%n", averageLocalTime, deviation);


        Duration avgDuration = vods.stream()
                .map(VOD::getDuration)
                .reduce(Duration.ZERO, Duration::plus)
                .dividedBy(vods.size());

        List<VOD> vodsSortViews = vods.stream()
                .sorted(Comparator.comparingInt(VOD::getViewCount).reversed())
                .limit(5) // top 5 streams
                .toList();

        // how many days between first and last stream
        long daysFirstLast = Duration.between(vods.getLast().getTime(), vods.getFirst().getTime()).toDaysPart();
        float streamsPerWeek = (((float) vods.size() / 7) / ((float) daysFirstLast / 7)) * 7;

        System.out.println("Number of vods: " + vods.size());
        System.out.printf("Avg stream duration: %d hours %d minutes%n", avgDuration.toHoursPart(), avgDuration.toMinutesPart());
        System.out.println("Avg number of streams per week: " + streamsPerWeek);

        Map<DayOfWeek, Integer> dayOfWeekCount = new HashMap<>();

        for (VOD vod : vods) {
            DayOfWeek dayOfWeek = vod.getTime().getDayOfWeek();
            dayOfWeekCount.put(dayOfWeek, dayOfWeekCount.getOrDefault(dayOfWeek, 0) + 1);
        }

        System.out.println("Streams per day of week:");
        // print the counts starting with monday
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if (dayOfWeekCount.containsKey(dayOfWeek)) {
                System.out.println(dayOfWeek + ": " + dayOfWeekCount.get(dayOfWeek) + " streams");
            }
        }

        System.out.println("Top 3 streams:");
        for (VOD vod : vodsSortViews) {
            String viewCount = NumberFormat.getInstance().format(vod.getViewCount());
            System.out.printf("%s (%s views) (%s) (%s)%n", vod.getTitle(), viewCount, vod.getUrl(), vod.getTime());
        }
    }

    private static LocalTime calculateAverageLocalTime(List<LocalTime> localTimeList) {
        int totalSeconds = localTimeList.stream()
                .mapToInt(LocalTime::toSecondOfDay)
                .sum();

        return LocalTime.ofSecondOfDay(totalSeconds / localTimeList.size());
    }

    private static LocalTime calculateStdDeviation(List<LocalTime> localTimeList, LocalTime averageLocalTime) {
        double sumOfSquares = localTimeList.stream()
                .mapToDouble(time -> Math.pow(time.toSecondOfDay() - averageLocalTime.toSecondOfDay(), 2))
                .sum();

        double variance = sumOfSquares / localTimeList.size();
        double stdDeviation = Math.sqrt(variance);

        return LocalTime.ofSecondOfDay((long) stdDeviation);
    }
}