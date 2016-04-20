package com.grayditch.netarea.domain;

/**
 * Created by gerard on 9/04/16.
 */
public class Mark {
    private boolean isNew = false;
    private String description;
    private String percentage;
    private String mark;

    public Mark() {

    }

    public Mark(boolean isNew, String description) {
        this.isNew = isNew;
        this.description = description;
    }

    public Mark(boolean isNew, String description, String percentage, String mark) {
        this.isNew = isNew;
        this.description = description;
        this.percentage = percentage;
        this.mark = mark;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
