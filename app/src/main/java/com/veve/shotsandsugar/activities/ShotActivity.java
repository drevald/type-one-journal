package com.veve.shotsandsugar.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.veve.shotsandsugar.Constants;
import com.veve.shotsandsugar.DaoAccess;
import com.veve.shotsandsugar.model.Insulin;

import com.veve.shotsandsugar.R;
import com.veve.shotsandsugar.model.InsulinShot;

public class ShotActivity extends DatabaseActivity {

    List<Insulin> insulinsList;

    int insulinAmount;

    int selectedInsulinId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shot);
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

        FloatingActionButton saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddShotTask(daoAccess).execute(insulinAmount, selectedInsulinId);
                Intent intentOne = new Intent(getApplicationContext(), MainActivity.class);
                intentOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentOne);
            }
        });

        try {
            insulinsList = new ListInsulinTask(daoAccess).execute().get();
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage(), e);
        }

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.insulinsList);
        for (Insulin insulin : insulinsList) {
            RadioButton insulinRadioButton = new RadioButton(getApplicationContext());
            int id = getResources()
                    .getIdentifier(insulin.getCode(), Constants.STRING_RES_TYPE, getPackageName());
            insulinRadioButton.setText(getResources().getString(id));
            insulinRadioButton.setTextColor(Color.BLACK);
            insulinRadioButton.setId(insulin.getId());
            radioGroup.addView(insulinRadioButton);
        }

        final EditText insulinAmountInput = findViewById(R.id.insulinAmount);
        insulinAmountInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    insulinAmount = Integer.parseInt(s.toString());
                } catch (Exception e) {
                    Snackbar.make(insulinAmountInput, e.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    static class ListInsulinTask extends AsyncTask<Void, Void, List<Insulin>> {

        DaoAccess daoAccess;

        ListInsulinTask(DaoAccess daoAccess) {
            this.daoAccess = daoAccess;
        }

        @Override
        protected List<Insulin> doInBackground(Void... voids) {
            return daoAccess.listInsulins();
        }

    }

    static class AddShotTask extends AsyncTask<Integer, Void, Void> {

        DaoAccess daoAccess;

        AddShotTask(DaoAccess daoAccess) {
            this.daoAccess = daoAccess;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            daoAccess.insertShot(new InsulinShot(params[0], params[1], System.currentTimeMillis()));
            return null;
        }

    }

}
