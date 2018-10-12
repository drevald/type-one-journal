package com.veve.shotsandsugar.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class MealIngredient {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long mealId;
    private long ingredientId;
    private int ingredientWeightGramms;

    public MealIngredient() {
    }

    public MealIngredient(long mealId, long ingredientId, int ingredientWeightGramms) {
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

    public long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public int getIngredientWeightGramms() {
        return ingredientWeightGramms;
    }

    public void setIngredientWeightGramms(int ingredientWeightGramms) {
        this.ingredientWeightGramms = ingredientWeightGramms;
    }
}
