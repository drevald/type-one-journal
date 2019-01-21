package com.veve.shotsandsugar.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class MealIngredient implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long mealId;
    private int ingredientId;
    private int ingredientWeightGramms;

    public MealIngredient() {
    }

    public MealIngredient(long mealId, int ingredientId, int ingredientWeightGramms) {
        this.mealId = mealId;
        this.ingredientId = ingredientId;
        this.ingredientWeightGramms = ingredientWeightGramms;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMealId() {
        return mealId;
    }

    public void setMealId(long mealId) {
        this.mealId = mealId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public int getIngredientWeightGramms() {
        return ingredientWeightGramms;
    }

    public void setIngredientWeightGramms(int ingredientWeightGramms) {
        this.ingredientWeightGramms = ingredientWeightGramms;
    }
}
