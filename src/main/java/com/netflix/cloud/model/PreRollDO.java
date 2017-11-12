package com.netflix.cloud.model;

import java.util.Objects;
import java.util.Set;

/**
 * Data object for Preroll information
 */
public class PreRollDO {

    private String name;

    private Set<VideoDO> videos;

    public void setName(String name) {
        this.name = name;
    }

    public void setVideos(Set<VideoDO> videos) {
        this.videos = videos;
    }

    public String getName() {
        return name;
    }

    public Set<VideoDO> getVideos() {
        return videos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PreRollDO preRollDO = (PreRollDO) o;
        return Objects.equals(name, preRollDO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
