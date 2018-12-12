package com.veve.shotsandsugar;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.veve.shotsandsugar.model.Activity;
import com.veve.shotsandsugar.model.ActivityPeriod;
import com.veve.shotsandsugar.model.Ingredient;
import com.veve.shotsandsugar.model.Insulin;
import com.veve.shotsandsugar.model.InsulinShot;
import com.veve.shotsandsugar.model.Meal;
import com.veve.shotsandsugar.model.MealIngredient;
import com.veve.shotsandsugar.model.Other;
import com.veve.shotsandsugar.model.OtherRecord;
import com.veve.shotsandsugar.model.SelectedInsulin;
import com.veve.shotsandsugar.model.SugarLevel;

@android.arch.persistence.room.Database(entities = {
        SugarLevel.class,
        Insulin.class,
        SelectedInsulin.class,
        InsulinShot.class,
        Meal.class,
        MealIngredient.class,
        Ingredient.class,
        Activity.class,
        ActivityPeriod.class,
        Other.class,
        OtherRecord.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    protected static final String DATABASE_NAME = "db";

    public abstract DaoAccess daoAccess();

    private static AppDatabase appDatabase;

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
            DaoAccess daoAccess = appDatabase.daoAccess();
            daoAccess.deleteInsulins();
            daoAccess.insertInsulin(new Insulin(0, "apidra"));
            daoAccess.insertInsulin(new Insulin(1, "tujeo"));
            daoAccess.deleteIngredients();
            daoAccess.insertIngredient(new Ingredient(0, 0, "sugar", 8, 70, 0, 100, 0, 400));
            daoAccess.insertIngredient(new Ingredient(1, 0, "milk", 0.5f,30, 3, 4, 2, 60));
            daoAccess.insertIngredient(new Ingredient(2, 0, "ray_bread", 4, 50, 1, 44, 4, 210));
            daoAccess.insertIngredient(new Ingredient(3, 0, "wheat_bread", 5, 80, 1, 49, 1, 238));
            daoAccess.insertIngredient(new Ingredient(4, 0, "potato_boiled", 1.5f, 65, 0, 18, 2, 79));
            daoAccess.insertIngredient(new Ingredient(5, 0, "potato_fried", 2.8f, 95, 9, 24, 1, 192));
            daoAccess.insertIngredient(new Ingredient(6, 0, "spaghetti", 2f, 55, 1, 27, 6, 140));
            daoAccess.insertIngredient(new Ingredient(7, 0, "buckwheat", 1.5f, 40, 2, 17, 4, 100));
            daoAccess.insertIngredient(new Ingredient(8, 0, "rice", 2, 80, 0, 25, 2, 113));
            daoAccess.insertIngredient(new Ingredient(9, 0, "butter", 0.1f, 51, 83, 1, 1, 748)); //GI ?
            daoAccess.insertIngredient(new Ingredient(10, 0, "cheese", 0, 15, 14, 3, 11, 183));
            daoAccess.insertIngredient(new Ingredient(11, 0, "sausage", 0, 28, 28, 2, 10, 300));
            daoAccess.insertIngredient(new Ingredient(12, 0, "burger", 0, 40, 20, 5, 16, 266));
            daoAccess.insertIngredient(new Ingredient(13, 0, "pork_boiled", 0, 0, 30, 0, 23, 376));
            daoAccess.insertIngredient(new Ingredient(14, 0, "pork_fried", 0, 0, 50, 0, 11, 489));
            daoAccess.insertIngredient(new Ingredient(15, 0, "beef_boiled", 0, 0, 17, 0, 26, 252));
            daoAccess.insertIngredient(new Ingredient(16, 0, "beef_fried", 0, 0, 28, 0, 33, 384));
            daoAccess.insertIngredient(new Ingredient(17, 0, "chicken_boiled", 0, 0, 7, 0, 25, 170));
            daoAccess.insertIngredient(new Ingredient(18, 0, "chicken_fried", 0, 0, 12, 0, 26, 210));
            daoAccess.insertIngredient(new Ingredient(19, 0, "egg", 0.1f, 0, 12, 1, 13, 160));
            daoAccess.insertIngredient(new Ingredient(20, 0, "omelette", 0.2f, 50, 20, 3, 14, 250));
            daoAccess.deleteActivities();
            daoAccess.insertActivity(new Activity(0, "morning_exercise"));
            daoAccess.insertActivity(new Activity(1, "warm_up"));
            daoAccess.insertActivity(new Activity(2, "running"));
            daoAccess.insertActivity(new Activity(3, "swimming"));
            daoAccess.insertActivity(new Activity(4, "walking"));
            daoAccess.deleteOthers();
            daoAccess.insertOther(new Other(0, "hormons"));
            daoAccess.insertOther(new Other(1, "cold"));
        }
        return appDatabase;
    }


    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }

}