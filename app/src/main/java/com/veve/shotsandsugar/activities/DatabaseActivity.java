package com.veve.shotsandsugar.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.veve.shotsandsugar.AppDatabase;
import com.veve.shotsandsugar.DaoAccess;

public abstract class DatabaseActivity extends AppCompatActivity {

    protected static AppDatabase appDatabase;
    protected static DaoAccess daoAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            daoAccess = new GetDatabaseTask().execute(getApplicationContext()).get();
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage(), e);
        }

    }

    static class GetDatabaseTask extends AsyncTask<Context, Void, DaoAccess> {

        @Override
        protected DaoAccess doInBackground(Context... contexts) {
            appDatabase = AppDatabase.getInstance(contexts[0]);
            return appDatabase.daoAccess();
        }
    }

}
