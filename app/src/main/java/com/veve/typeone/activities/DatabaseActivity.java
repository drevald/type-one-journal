package com.veve.typeone.activities;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.veve.typeone.AppDatabase;
import com.veve.typeone.Constants;
import com.veve.typeone.DaoAccess;

public abstract class DatabaseActivity extends AppCompatActivity {

    protected static AppDatabase appDatabase;
    protected static DaoAccess daoAccess;
    static Resources RESOURCES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            daoAccess = new GetDatabaseTask().execute(getApplicationContext()).get();
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage(), e);
        }
        RESOURCES = getResources();
    }

    static class GetDatabaseTask extends AsyncTask<Context, Void, DaoAccess> {

        @Override
        protected DaoAccess doInBackground(Context... contexts) {
            appDatabase = AppDatabase.getInstance(contexts[0]);
            return appDatabase.daoAccess();
        }
    }

    protected String getLocalizedStringFromCode(String code) {
        int resourceId = RESOURCES.getIdentifier(code,
                Constants.STRING_RES_TYPE, getApplicationContext().getPackageName());
        return RESOURCES.getText(resourceId).toString();
    }

}
