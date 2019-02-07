package com.veve.typeone.activities;

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

import com.veve.typeone.Constants;
import com.veve.typeone.DaoAccess;
import com.veve.typeone.model.Insulin;

import com.veve.typeone.R;
import com.veve.typeone.model.InsulinShot;

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

        final EditText insulinAmountInput = findViewById(R.id.insulinAmount);

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
                insulinAmount = Integer.parseInt(insulinAmountInput.getText().toString());
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
            RadioButton insulinRadioButton = (RadioButton)
                    getLayoutInflater().inflate(R.layout.insulin_radio_button, null);
            int id = getResources()
                    .getIdentifier(insulin.getCode(), Constants.STRING_RES_TYPE, getPackageName());
            insulinRadioButton.setText(getResources().getString(id));
            insulinRadioButton.setId(insulin.getId());
            radioGroup.addView(insulinRadioButton);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radioGroup.getCheckedRadioButtonId()!= -1) {
                    insulinAmountInput.setEnabled(true);
                }
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
