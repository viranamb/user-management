package com.capitalone.migration.model;

import java.util.Map;
import java.util.Objects;

/**
 * Created by msz519 on 11/9/17.
 */
public class Video {

    private String name;

    private Map<String, Object> attributes;

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(name, video.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;

    }
}
