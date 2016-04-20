package com.grayditch.netarea.data.database.dao;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by gerard on 20/04/16.
 */
public class MarkDAO extends RealmObject {
    @PrimaryKey
    private String description;
    private boolean isNew;
    private String mark;
    private String percentage;

    public MarkDAO() {

    }

    public MarkDAO(String description, boolean isNew, String mark, String percentage) {
        this.description = description;
        this.isNew = isNew;
        this.mark = mark;
        this.percentage = percentage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
