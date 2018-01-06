package com.cloud.model;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Data object representing Playlist information
 */
public class PlaylistDO {

    private Set<VideoDO> videos;

    public Set<VideoDO> getVideos() {
        return videos;
    }

    public void addVideos(VideoDO videoDO) {
        if (videos == null) {
            // Using LinkedHashSet to maintain insertion order and remove duplicates
            videos = new LinkedHashSet<>();
        }
        if (videoDO != null) {
            videos.add(videoDO);
        }
    }

    @Override
    public String toString() {
        return "Playlist{" + videos + "}";
    }
}
