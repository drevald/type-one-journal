package com.veve.typeone.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Activity {

    @PrimaryKey(autoGenerate = false)
    int id;
    String activityCode;

    public Activity(int id, String activityCode) {
        this.id = id;
        this.activityCode = activityCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }
}

