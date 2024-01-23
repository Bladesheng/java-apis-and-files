package org.example.models;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoData {
    private String id;
    private String stream_id;
    private String user_id;
    private String user_login;
    private String user_name;
    private String title;
    private String description;
    private String created_at;
    private String published_at;
    private String url;
    private String thumbnail_url;
    private String viewable;
    private int view_count;
    private String language;
    private String type;
    private String duration;
    private String muted_segments;

    public String getId() {
        return id;
    }

    public String getStream_id() {
        return stream_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_login() {
        return user_login;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getPublished_at() {
        return published_at;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public String getViewable() {
        return viewable;
    }

    public int getView_count() {
        return view_count;
    }

    public String getLanguage() {
        return language;
    }

    public String getType() {
        return type;
    }

    public String getDuration() {
        return duration;
    }

    public String getMuted_segments() {
        return muted_segments;
    }

    public Duration getDurationObj() {
        int hours = extractNumber(duration, "h");
        int minutes = extractNumber(duration, "m");
        int seconds = extractNumber(duration, "s");

        return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
    }

    private int extractNumber(String input, String unit) {
        String pattern = "(\\d+)" + unit;
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(input);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            return 0;
        }
    }
}
