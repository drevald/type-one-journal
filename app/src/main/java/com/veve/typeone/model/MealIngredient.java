package com.veve.typeone.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class MealIngredient implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long mealId;
    private long ingredientId;
    private int ingredientWeightGramms;

    @Ignore
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

    public String toString() {
        return "id:"+id+",mealId:"+mealId+",ingredientId:"+ingredientId+",weight:"+ingredientWeightGramms;
    }

}
