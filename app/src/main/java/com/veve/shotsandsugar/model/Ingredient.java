package com.veve.shotsandsugar.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Ingredient {

    @PrimaryKey(autoGenerate = false)
    private int id;
    private int typeId;
    private String ingredientCode;
    private float breadUnitsPer100g;
    private int glycemicIndex;
    private int fatPer100g;
    private int carbohydratePer100g;
    private int proteinPer100g;
    private int energyKkalPer100g;

    public Ingredient(int id,
                      int typeId,
                      String ingredientCode,
                      float breadUnitsPer100g,
                      int glycemicIndex,
                      int fatPer100g,
                      int carbohydratePer100g,
                      int proteinPer100g,
                      int energyKkalPer100g) {
        this.id = id;
        this.typeId = typeId;
        this.ingredientCode = ingredientCode;
        this.breadUnitsPer100g = breadUnitsPer100g;
        this.glycemicIndex = glycemicIndex;
        this.fatPer100g = fatPer100g;
        this.carbohydratePer100g = carbohydratePer100g;
        this.proteinPer100g = proteinPer100g;
        this.energyKkalPer100g = energyKkalPer100g;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getIngredientCode() {
        return ingredientCode;
    }

    public void setIngredientCode(String ingredientCode) {
        this.ingredientCode = ingredientCode;
    }

    public float getBreadUnitsPer100g() {
        return breadUnitsPer100g;
    }

    public void setBreadUnitsPer100g(float breadUnitsPer100g) {
        this.breadUnitsPer100g = breadUnitsPer100g;
    }

    public int getGlycemicIndex() {
        return glycemicIndex;
    }

    public void setGlycemicIndex(int glycemicIndex) {
        this.glycemicIndex = glycemicIndex;
    }

    public int getFatPer100g() {
        return fatPer100g;
    }

    public void setFatPer100g(int fatPer100g) {
        this.fatPer100g = fatPer100g;
    }

    public int getCarbohydratePer100g() {
        return carbohydratePer100g;
    }

    public void setCarbohydratePer100g(int carbohydratePer100g) {
        this.carbohydratePer100g = carbohydratePer100g;
    }

    public int getProteinPer100g() {
        return proteinPer100g;
    }

    public void setProteinPer100g(int proteinPer100g) {
        this.proteinPer100g = proteinPer100g;
    }

    public int getEnergyKkalPer100g() {
        return energyKkalPer100g;
    }

    public void setEnergyKkalPer100g(int energyKkalPer100g) {
        this.energyKkalPer100g = energyKkalPer100g;
    }
}
