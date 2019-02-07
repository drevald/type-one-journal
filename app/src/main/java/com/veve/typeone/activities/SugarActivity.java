package com.veve.typeone.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veve.typeone.Constants;
import com.veve.typeone.DaoAccess;
import com.veve.typeone.model.SugarLevel;

import com.veve.typeone.R;

public class SugarActivity extends DatabaseActivity {

    private static final int PADDING = 5;
    private static final int COLUMNS_NUM = 4;
    private static final int ROWS_NUM = 5;
    private static final int MAX_SUGAR_LEVEL = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOne = new Intent(getApplicationContext(), MainActivity.class);
                intentOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentOne);
            }
        });

        final EditText textView = findViewById(R.id.sugar_level);

        FloatingActionButton saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputString = textView.getEditableText().toString();
                float sugarLevel = Float.parseFloat(inputString);
                if (sugarLevel < 0) {
                    Snackbar.make(textView, "Could not be negative value",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else if (sugarLevel > 50) {
                    Snackbar.make(textView, "Could not be that much",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    new AddSugarTask(daoAccess).execute(
                            Float.parseFloat(textView.getEditableText().toString()));
                    Intent intentOne = new Intent(getApplicationContext(), MainActivity.class);
                    intentOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intentOne);
                }
            }
        });

    }

    static class AddSugarTask extends AsyncTask<Float, Void, Void> {

        DaoAccess daoAccess;

        AddSugarTask(DaoAccess daoAccess) {
            this.daoAccess = daoAccess;
        }

        @Override
        protected Void doInBackground(Float... floats) {
            daoAccess.insertSugarLevel(new SugarLevel(floats[0], System.currentTimeMillis()));
            return null;
        }

    }

}
