package com.veve.typeone.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class SelectedInsulin {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInsulinId() {
        return insulinId;
    }

    public void setInsulinId(long insulinId) {
        this.insulinId = insulinId;
    }

    @PrimaryKey(autoGenerate = true)
    long id;
    long insulinId;

    public SelectedInsulin(long insulinId) {
        this.insulinId = insulinId;
    }

}


