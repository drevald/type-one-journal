package com.veve.shotsandsugar.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import com.veve.shotsandsugar.DaoAccess;
import com.veve.shotsandsugar.model.Insulin;

import com.veve.shotsandsugar.R;

public class ShotActivity extends DatabaseActivity {

    List<Insulin> insulinsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            int id = getResources().getIdentifier(insulin.getCode(), null, null);
            insulinRadioButton.setText(getResources().getString(id));
            radioGroup.addView(insulinRadioButton);
        }

    }

    static class ListInsulinTask extends AsyncTask<Void, Void, List<Insulin>> {

        DaoAccess daoAccess;

        ListInsulinTask(DaoAccess daoAccess) {
            this.daoAccess = daoAccess;
        }

        @Override
        protected List<Insulin> doInBackground(Void... voids) {
            //return daoAccess.listSelectedInsulins();
            List<Insulin> insulins = new ArrayList<Insulin>();
            insulins.add(new Insulin(1, "tujeo"));
            insulins.add(new Insulin(2, "apidra"));
            insulins.add(new Insulin(3, "humulin"));
            return insulins;
        }

    }

}
