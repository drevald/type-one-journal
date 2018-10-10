package com.veve.shotsandsugar.activities;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.veve.shotsandsugar.AppDatabase;
import com.veve.shotsandsugar.DaoAccess;

public abstract class DatabaseActivity extends AppCompatActivity {

    protected static final String DATABASE_NAME = "db";
    protected AppDatabase appDatabase;
    protected DaoAccess daoAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appDatabase = Room.databaseBuilder(
                getApplicationContext(), AppDatabase.class, "db").build();

        daoAccess = appDatabase.daoAccess();

    }

}
