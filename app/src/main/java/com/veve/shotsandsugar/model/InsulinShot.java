package com.veve.shotsandsugar.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class InsulinShot {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private int amount;

    private long insulinId;

    private long time;

    public InsulinShot(int amount, long insulinId, long time) {
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

    public long getInsulinId() {
        return insulinId;
    }

    public void setInsulinId(long insulinId) {
        this.insulinId = insulinId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
