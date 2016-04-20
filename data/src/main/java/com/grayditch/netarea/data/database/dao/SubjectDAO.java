package com.grayditch.netarea.data.database.dao;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by gerard on 20/04/16.
 */
public class SubjectDAO extends RealmObject {

    @PrimaryKey
    private String name;
    private RealmList<MarkDAO> marks = new RealmList<>();

    public SubjectDAO() {
    }


    public SubjectDAO(String name, RealmList<MarkDAO> marks) {
        this.name = name;
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<MarkDAO> getMarks() {
        return marks;
    }

    public void setMarks(RealmList<MarkDAO> marks) {
        this.marks = marks;
    }
}
