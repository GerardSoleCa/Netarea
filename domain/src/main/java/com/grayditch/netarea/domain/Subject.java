package com.grayditch.netarea.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerard on 9/04/16.
 */
public class Subject {
    private String name;
    private List<Mark> marks = new ArrayList<>();

    public Subject() {
    }

    public Subject(String name) {
        this.name = name;
    }

    public Subject(String name, List<Mark> marks) {
        this.name = name;
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Subject && this.getName().equals(((Subject) obj).getName());
    }
}
