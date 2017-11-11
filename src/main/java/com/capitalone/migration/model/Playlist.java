package com.capitalone.migration.model;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by msz519 on 11/9/17.
 */
public class Playlist {

    private Set<Video> videos;

    public Set<Video> getVideos() {
        return videos;
    }

    public void addVideos(Video video){
        if (videos == null) {
            // Using LinkedHashSet to maintain insertion order and remove duplicates
            videos = new LinkedHashSet<>();
        }
        if (video != null) {
            videos.add(video);
        }
    }

    @Override
    public String toString() {
        return "Playlist{" + videos + "}";
    }
}
