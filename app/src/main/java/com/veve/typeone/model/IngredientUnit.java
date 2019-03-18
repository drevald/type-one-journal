package com.veve.typeone.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class IngredientUnit {

    @PrimaryKey(autoGenerate = true)
    long id;
    private long unitId;
    private long ingredientId;
    private float gramsInUnit;

    public IngredientUnit(long unitId, long ingredientId, float gramsInUnit) {
        this.unitId = unitId;
        this.ingredientId = ingredientId;
        this.gramsInUnit = gramsInUnit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUnitId() {
        return unitId;
    }

    public void setUnitId(long unitId) {
        this.unitId = unitId;
    }

    public long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public float getGramsInUnit() {
        return gramsInUnit;
    }

    public void setGramsInUnit(float gramsInUnit) {
        this.gramsInUnit = gramsInUnit;
    }

}
