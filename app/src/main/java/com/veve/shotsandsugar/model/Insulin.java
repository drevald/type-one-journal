package com.veve.shotsandsugar.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Insulin {

    @PrimaryKey(autoGenerate = false)
    private long id;
    private String code;

    public Insulin() {
    }

    public Insulin(long id, String code) {
        this.id = id;
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static Insulin[] populateData() {
        return new Insulin[] {
                new Insulin(1, "tudjeo"),
                new Insulin(2, "apidra"),
                new Insulin(3, "humulin"),
        };
    }


}
