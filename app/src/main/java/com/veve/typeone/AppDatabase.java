package com.veve.typeone;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.veve.typeone.model.Activity;
import com.veve.typeone.model.ActivityPeriod;
import com.veve.typeone.model.Ingredient;
import com.veve.typeone.model.IngredientUnit;
import com.veve.typeone.model.Insulin;
import com.veve.typeone.model.InsulinShot;
import com.veve.typeone.model.Meal;
import com.veve.typeone.model.MealIngredient;
import com.veve.typeone.model.Other;
import com.veve.typeone.model.OtherRecord;
import com.veve.typeone.model.SelectedInsulin;
import com.veve.typeone.model.SugarLevel;
import com.veve.typeone.model.Unit;

import java.io.File;

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
        OtherRecord.class,
        IngredientUnit.class,
        Unit.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    protected static final String DATABASE_NAME = "db";

    public abstract DaoAccess daoAccess();

    private static AppDatabase appDatabase;

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            File dbFile = context.getDatabasePath(DATABASE_NAME);
            boolean databasePresent = (dbFile != null && dbFile.exists());
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
            if (!databasePresent) {
                DaoAccess daoAccess = appDatabase.daoAccess();
                daoAccess.deleteInsulins();
                daoAccess.insertInsulin(new Insulin(0, "apidra"));
                daoAccess.insertInsulin(new Insulin(1, "tujeo"));
                daoAccess.deleteIngredients(0);
                daoAccess.insertIngredient(new Ingredient(0, "sugar", 8, 70, 0, 100, 0, 400, 0, null));
                daoAccess.insertIngredient(new Ingredient(0, "milk", 0.5f,30, 3, 4, 2, 60, 0, null));
                daoAccess.insertIngredient(new Ingredient(0, "ray_bread", 4, 50, 1, 44, 4, 210, 0, null));
                daoAccess.insertIngredient(new Ingredient(0, "wheat_bread", 5, 80, 1, 49, 1, 238, 0, null));
                daoAccess.insertIngredient(new Ingredient(0, "potato_boiled", 1.5f, 65, 0, 18, 2, 79, 0, null));
                daoAccess.insertIngredient(new Ingredient(0, "potato_fried", 2.8f, 95, 9, 24, 1, 192, 0, null));
                daoAccess.insertIngredient(new Ingredient(0, "spaghetti", 2f, 55, 1, 27, 6, 140, 0, null));
                daoAccess.insertIngredient(new Ingredient(0, "buckwheat", 1.5f, 40, 2, 17, 4, 100, 0, null));
                daoAccess.insertIngredient(new Ingredient(0, "rice", 2, 80, 0, 25, 2, 113, 0, null));
                daoAccess.insertIngredient(new Ingredient(0, "butter", 0.1f, 51, 83, 1, 1, 748, 0, null)); //GI ?
                daoAccess.insertIngredient(new Ingredient(0, "cheese", 0, 15, 14, 3, 11, 183, 0, null));
                daoAccess.insertIngredient(new Ingredient(0, "sausage", 0, 28, 28, 2, 10, 300, 0, null));
                daoAccess.insertIngredient(new Ingredient(0, "burger", 0, 40, 20, 5, 16, 266, 0, null));
                daoAccess.insertIngredient(new Ingredient(0, "pork_boiled", 0, 0, 30, 0, 23, 376, 0, null));
                daoAccess.insertIngredient(new Ingredient(0, "pork_fried", 0, 0, 50, 0, 11, 489, 0, null));
                daoAccess.insertIngredient(new Ingredient(0, "beef_boiled", 0, 0, 17, 0, 26, 252, 0, null));
                daoAccess.insertIngredient(new Ingredient(0, "beef_fried", 0, 0, 28, 0, 33, 384, 0, null));
                daoAccess.insertIngredient(new Ingredient(0, "chicken_boiled", 0, 0, 7, 0, 25, 170, 0, null));
                daoAccess.insertIngredient(new Ingredient(0, "chicken_fried", 0, 0, 12, 0, 26, 210, 0, null));
                daoAccess.insertIngredient(new Ingredient(0, "egg", 0.1f, 0, 12, 1, 13, 160, 0, null));
                daoAccess.insertIngredient(new Ingredient(0, "omelette", 0.2f, 50, 20, 3, 14, 250, 0, null));

                daoAccess.deleteUnits();
                daoAccess.insertUnit(new Unit("Tea Spoon"));
                daoAccess.insertUnit(new Unit("Spoon"));


                daoAccess.deleteIngredientUnits();
                daoAccess.insertIngredientUnit(new IngredientUnit(1, 1, 10));
                daoAccess.insertIngredientUnit(new IngredientUnit(2, 1, 25));

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