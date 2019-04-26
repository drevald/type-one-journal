package com.veve.typeone.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.Locale;

@Entity
public class ThreeColumnRecord extends DiaryRecord {


    @PrimaryKey(autoGenerate = true)
    long _id;
    long time;
    @Nullable
    float glucoseLevel;
    @Nullable
    float insulinShot;
    @Nullable
    long mealId;
    @Nullable
    String mealDetails;

    public ThreeColumnRecord() {

    }

    public String getMealDetails() { return mealDetails; }

    public void setMealDetails(String mealDetails) { this.mealDetails = mealDetails; }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getGlucoseLevel() {
        return glucoseLevel;
    }

    public void setGlucoseLevel(float glucoseLevel) {
        this.glucoseLevel = glucoseLevel;
    }

    public float getInsulinShot() {
        return insulinShot;
    }

    public void setInsulinShot(float insulinShot) {
        this.insulinShot = insulinShot;
    }

    public long getMealId() {
        return mealId;
    }

    public void setMealId(long mealId) {
        this.mealId = mealId;
    }

    public String toString() {
        return String.format(Locale.getDefault(),
                "Time:%s, Glucose:%f, Shot:%f, Meal%s",
                new Date(getTime()), getGlucoseLevel(), getInsulinShot(), getMealDetails());
    }

    public boolean equals(ThreeColumnRecord threeColumnRecord) {
        return _id == threeColumnRecord._id && glucoseLevel==threeColumnRecord.glucoseLevel
                && insulinShot==threeColumnRecord.insulinShot;
    }

}
