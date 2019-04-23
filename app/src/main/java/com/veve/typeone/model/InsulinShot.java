package com.veve.typeone.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class InsulinShot {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long recordId;

    private int amount;

    private int insulinId;

    private long time;

    public InsulinShot(int amount, int insulinId, long time) {
        this.amount = amount;
        this.insulinId = insulinId;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getInsulinId() {
        return insulinId;
    }

    public void setInsulinId(int insulinId) {
        this.insulinId = insulinId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }
}
