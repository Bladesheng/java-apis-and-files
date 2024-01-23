package org.example;

import java.time.Duration;
import java.time.ZonedDateTime;

public class VOD {
    final private String title;
    final private ZonedDateTime time;
    final private String url;
    final private int viewCount;
    final private Duration duration;

    public VOD(String title, ZonedDateTime time, String url, int viewCount, Duration duration) {
        this.title = title;
        this.time = time;
        this.url = url;
        this.viewCount = viewCount;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }

    public int getViewCount() {
        return viewCount;
    }

    public Duration getDuration() {
        return duration;
    }
}
