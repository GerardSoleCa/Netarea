package com.grayditch.netarea.data.entity;

/**
 * Created by gerard on 9/04/16.
 */
public class MarkEntity {
    private boolean isNew = false;
    private String description;
    private String percentage;
    private String qualification;

    public MarkEntity(boolean isNew, String description) {
        this.isNew = isNew;
        this.description = description;
    }

    public MarkEntity(boolean isNew, String description, String percentage, String qualification) {
        this.isNew = isNew;
        this.description = description;
        this.percentage = percentage;
        this.qualification = qualification;
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

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
}
