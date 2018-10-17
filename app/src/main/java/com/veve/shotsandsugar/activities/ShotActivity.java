package com.veve.shotsandsugar.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

    int selectedInsulinId;

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

        final GridLayout gridLayout = (GridLayout)findViewById(R.id.gridLayout);
        gridLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                gridLayout.setColumnCount(
                        ((LinearLayout)gridLayout.getParent()).getWidth()/
                                (Constants.BUTTON_WIDTH + 2 * Constants.PADDING));
                for (int i=1; i<30; i++) {
                    Button button = new Button(getApplicationContext());
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                            new ViewGroup.MarginLayoutParams(
                                    Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT));
                    params.setMargins(0, Constants.PADDING, Constants.PADDING, 0);
                    button.setLayoutParams(params);
                    button.setTextColor(Color.WHITE);
                    button.setOnClickListener(new NumberListener(i));
                    button.setText(String.valueOf(i));
                    if (selectedInsulinId == 0) {
                        button.setBackgroundColor(Color.LTGRAY);
                        button.setEnabled(false);
                    } else if (selectedInsulinId == 0) {
                        button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        button.setEnabled(true);
                    } else {
                        button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        button.setEnabled(true);
                    }
                    gridLayout.addView(button);
                }
                gridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.insulinsList);
        for (Insulin insulin : insulinsList) {
            RadioButton insulinRadioButton = new RadioButton(getApplicationContext());
            int id = getResources()
                    .getIdentifier(insulin.getCode(), Constants.STRING_RES_TYPE, getPackageName());
            insulinRadioButton.setText(getResources().getString(id));
            insulinRadioButton.setId(insulin.getId());
            insulinRadioButton.setTextColor(Color.BLACK);
            radioGroup.addView(insulinRadioButton);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedInsulinId = checkedId;
                for (int i=0; i<gridLayout.getChildCount(); i++) {
                    gridLayout.getChildAt(i).setEnabled(true);
                    if (selectedInsulinId == 1)
                        gridLayout.getChildAt(i)
                                .setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    else
                        gridLayout.getChildAt(i)
                                .setBackgroundColor(getResources().getColor(R.color.colorPrimary));

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

    class NumberListener implements View.OnClickListener {

        int number;

        public NumberListener(int number) {
            this.number = number;
        }

        @Override
        public void onClick(View v) {
            new AddShotTask(daoAccess).execute(number, selectedInsulinId);
            Intent intentOne = new Intent(getApplicationContext(), MainActivity.class);
            intentOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intentOne);
        }

    }

}
