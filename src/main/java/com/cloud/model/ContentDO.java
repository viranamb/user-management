package com.cloud.model;

import java.util.Objects;
import java.util.Set;

/**
 * Data object representing Content information
 */
public class ContentDO {

    private String name;

    // Assuming that pre-rolls cannot be repeated for a given ContentDO
    private Set<PreRollDO> preroll;

    // Assuming that videos cannot be repeated for a given ContentDO
    private Set<VideoDO> videos;

    public ContentDO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<PreRollDO> getPreroll() {
        return preroll;
    }

    public Set<VideoDO> getVideos() {
        return videos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContentDO contentDO = (ContentDO) o;
        return Objects.equals(name, contentDO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
