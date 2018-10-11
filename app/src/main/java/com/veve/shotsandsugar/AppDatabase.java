package com.veve.shotsandsugar;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.veve.shotsandsugar.model.Insulin;
import com.veve.shotsandsugar.model.InsulinShot;
import com.veve.shotsandsugar.model.SelectedInsulin;
import com.veve.shotsandsugar.model.SugarLevel;

@android.arch.persistence.room.Database(entities = {
        SugarLevel.class,
        Insulin.class,
        SelectedInsulin.class,
        InsulinShot.class
}, version = 2, exportSchema = false)
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