package com.veve.shotsandsugar.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class OtherRecord {

    @PrimaryKey(autoGenerate = true)
    long id;
    int otherId;
    long startTims;
    long endTime;

    public OtherRecord(int otherId, long startTims, long endTime) {
        this.otherId = otherId;
        this.startTims = startTims;
        this.endTime = endTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOtherId() {
        return otherId;
    }

    public void setOtherId(int otherId) {
        this.otherId = otherId;
    }

    public long getStartTims() {
        return startTims;
    }

    public void setStartTims(long startTims) {
        this.startTims = startTims;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
