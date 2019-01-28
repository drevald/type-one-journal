package com.veve.typeone;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import com.veve.typeone.model.Activity;
import com.veve.typeone.model.ActivityPeriod;
import com.veve.typeone.model.Ingredient;
import com.veve.typeone.model.Insulin;
import com.veve.typeone.model.InsulinShot;
import com.veve.typeone.model.Meal;
import com.veve.typeone.model.MealIngredient;
import com.veve.typeone.model.Other;
import com.veve.typeone.model.OtherRecord;
import com.veve.typeone.model.SelectedInsulin;
import com.veve.typeone.model.SugarLevel;

@Dao
public interface DaoAccess {

    @Insert
    void insertSugarLevel (SugarLevel record);

    @Insert
    void insertSugarLevels (List<SugarLevel> records);

    @Query("SELECT * FROM SugarLevel WHERE Id = :id")
    SugarLevel fetchSugarLevel (int id);

    @Query("SELECT * FROM SugarLevel")
    List<SugarLevel> fetchSugarLevels ();

    @Update
    void updateMovie (SugarLevel record);

    @Delete
    void deleteSugarLevel(SugarLevel record);

    @Insert
    void insertSugarLevel (Insulin record);

    @Insert
    void insertInsulin(Insulin insulin);

    @Query("DELETE FROM Insulin")
    void deleteInsulins();

    @Insert
    void insertSelectedInsulin(SelectedInsulin insulin);

    @Query("SELECT * FROM Insulin")
    List<Insulin> listInsulins();

    @Query("SELECT Insulin.* FROM Insulin, SelectedInsulin WHERE Insulin.id = SelectedInsulin.insulinId")
    List<Insulin> listSelectedInsulins();

    @Query("SELECT * FROM Insulin WHERE Id = :id")
    Insulin fetchInsulin (int id);

    @Insert
    void insertShot(InsulinShot insulinShot);

    @Query("SELECT * FROM InsulinShot")
    List<InsulinShot> fetchInsulinShots();

    @Query("DELETE FROM Ingredient")
    void deleteIngredients();

    @Insert
    void insertIngredient(Ingredient ingredient);

    @Query("SELECT * FROM Ingredient")
    List<Ingredient> fetchIngredients ();

    @Insert
    long insertMeal(Meal meal);

    @Insert
    void insertMealIngredients(List<MealIngredient> mealIngredients);

    @Query("SELECT * FROM Meal")
    List<Meal> fetchMeals();

    @Query("SELECT * FROM MealIngredient WHERE mealId = :mealId")
    List<MealIngredient> fetchMealIngredients(long mealId);

    @Query("SELECT * FROM Ingredient WHERE id = :id")
    Ingredient fetchIngredient(long id);

    @Insert
    long insertMealIngredient(MealIngredient mealIngredient);

    @Insert
    void insertActivity(Activity activity);

    @Query("DELETE FROM Activity")
    void deleteActivities();

    @Insert
    void insertOther(Other other);

    @Query("DELETE FROM Other")
    void deleteOthers();

    @Query("SELECT * FROM Activity")
    List<Activity> listActivity();

    @Insert
    void insertActivityPeriod(ActivityPeriod activityPeriod);

    @Query("SELECT * FROM ActivityPeriod")
    List<ActivityPeriod> fetchActivityPeriods();

    @Query("SELECT * FROM Activity WHERE Id = :id")
    Activity fetchActivity (int id);

    @Query("SELECT * FROM Other")
    List<Other> fetchOthers();

    @Insert
    void insertOtherRecord(OtherRecord otherRecord);

    @Query("SELECT * FROM OtherRecord")
    List<OtherRecord> fetchOtherRecords();

    @Query("SELECT * FROM Other WHERE Id = :id")
    Other fetchOther(int id);

    @Query("DELETE FROM MealIngredient WHERE Id = :id")
    void deleteMealIngredient(long id);

}