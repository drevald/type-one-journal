package com.veve.shotsandsugar;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.veve.shotsandsugar.model.Ingredient;
import com.veve.shotsandsugar.model.Insulin;
import com.veve.shotsandsugar.model.InsulinShot;
import com.veve.shotsandsugar.model.Meal;
import com.veve.shotsandsugar.model.MealIngredient;
import com.veve.shotsandsugar.model.SelectedInsulin;
import com.veve.shotsandsugar.model.SugarLevel;

@android.arch.persistence.room.Database(entities = {
        SugarLevel.class,
        Insulin.class,
        SelectedInsulin.class,
        InsulinShot.class,
        Meal.class,
        MealIngredient.class,
        Ingredient.class
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
            daoAccess.insertInsulin(new Insulin(1, "apidra"));
            daoAccess.insertInsulin(new Insulin(2, "tujeo"));
            daoAccess.deleteIngredients();
            daoAccess.insertIngredient(new Ingredient(1, 0, "water", 5, 55, 1, 1, 1, 1));
            daoAccess.insertIngredient(new Ingredient(2, 0, "ray_bread", 5, 55, 1, 1, 1, 1));
            daoAccess.insertIngredient(new Ingredient(3, 0, "caviar", 5, 55, 1, 1, 1, 1));
            daoAccess.insertIngredient(new Ingredient(4, 0, "champagne", 5, 55, 1, 1, 1, 1));

//            @PrimaryKey
//            int id;
//            int typeId;
//            String ingredientCode;
//            float breadUnitsPer100g;
//            float glycemicIndex;

//            float fatPer100g;
//            float carbohydratePer100g;
//            float proteinPer100g;

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