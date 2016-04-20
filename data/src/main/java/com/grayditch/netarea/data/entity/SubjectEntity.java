package com.grayditch.netarea.data.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerard on 9/04/16.
 */
public class SubjectEntity {
    private String name;
    private List<MarkEntity> markEntities = new ArrayList<>();

    public SubjectEntity() {
    }

    public SubjectEntity(String name) {
        this.name = name;
    }

    public SubjectEntity(String name, List<MarkEntity> markEntities) {
        this.name = name;
        this.markEntities = markEntities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MarkEntity> getMarkEntities() {
        return markEntities;
    }

    public void setMarkEntities(List<MarkEntity> markEntities) {
        this.markEntities = markEntities;
    }
}
