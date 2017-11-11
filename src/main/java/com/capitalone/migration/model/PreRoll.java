package com.capitalone.migration.model;

import java.util.Objects;
import java.util.Set;

/**
 * Created by msz519 on 11/9/17.
 */
public class PreRoll {

    private String name;

    private Set<Video> videos;

    public String getName() {
        return name;
    }

    public Set<Video> getVideos() {
        return videos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PreRoll preRoll = (PreRoll) o;
        return Objects.equals(name, preRoll.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
