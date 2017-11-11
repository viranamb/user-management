package com.capitalone.migration.model;

import java.util.Objects;
import java.util.Set;

/**
 * Created by msz519 on 11/9/17.
 */
public class Content {

    private String name;

    // Assuming that pre-rolls cannot be repeated for a given Content
    private Set<PreRoll> preroll;

    // Assuming that videos cannot be repeated for a given Content
    private Set<Video> videos;

    public Content(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<PreRoll> getPreroll() {
        return preroll;
    }

    public Set<Video> getVideos() {
        return videos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;
        return Objects.equals(name, content.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
