package com.veve.shotsandsugar.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.veve.shotsandsugar.Constants;
import com.veve.shotsandsugar.DaoAccess;
import com.veve.shotsandsugar.model.Activity;
import com.veve.shotsandsugar.model.ActivityPeriod;
import com.veve.shotsandsugar.model.Ingredient;
import com.veve.shotsandsugar.model.Insulin;
import com.veve.shotsandsugar.model.InsulinShot;
import com.veve.shotsandsugar.model.Meal;
import com.veve.shotsandsugar.model.MealIngredient;
import com.veve.shotsandsugar.model.Other;
import com.veve.shotsandsugar.model.OtherRecord;
import com.veve.shotsandsugar.model.Record;
import com.veve.shotsandsugar.model.SugarLevel;

import com.veve.shotsandsugar.R;

public class DiagramActivity extends DatabaseActivity {

    static SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());

    List<Record> records = new ArrayList<Record>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_diagram);
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
            records = new DiagramTask(daoAccess).execute().get();
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage(), e);
        }

        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return records.size();
            }

            @Override
            public Object getItem(int position) {
                return records.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.records_list, parent, false);
                }
                TextView dateTextView = convertView.findViewById(R.id.date);
                TextView recordTextView = convertView.findViewById(R.id.record);
                Record record = records.get(position);
                Date resultdate = new Date(record.getTimestamp());
                dateTextView.setText(String.format(Locale.getDefault(), "%s",
                        sdf.format(resultdate)));
                recordTextView.setText(record.getText());
                return convertView;
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        try {
            records = new DiagramTask(daoAccess).execute().get();

        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage(), e);
        }
    }

    static class DiagramTask extends AsyncTask<Void, Void, List<Record>> {

        DaoAccess daoAccess;

        DiagramTask(DaoAccess daoAccess) {
            this.daoAccess = daoAccess;
        }

        @Override
        protected List<Record> doInBackground(Void... voids) {

            List<Record> records = new ArrayList<Record>();

            List<SugarLevel> sugarRecords = daoAccess.fetchSugarLevels();
            for (SugarLevel sugarRecord : sugarRecords) {
                String text = String.format(Locale.getDefault(),
                        RESOURCES.getString(R.string.glucose_level_record), sugarRecord.getValue());
                Record record = new Record(sugarRecord, sugarRecord.getTimestamp(), text);
                records.add(record);
            }

            List<InsulinShot> shotRecords = daoAccess.fetchInsulinShots();
            for (InsulinShot insulinShot : shotRecords) {
                Insulin insulin = daoAccess.fetchInsulin(insulinShot.getInsulinId());
                int insulinNameId = RESOURCES.getIdentifier(insulin.getCode(),
                        Constants.STRING_RES_TYPE,"com.veve.shotsandsugar");
                String insulinName = RESOURCES.getString(insulinNameId);
                String text = String.format(Locale.getDefault(),
                        RESOURCES.getString(R.string.shot_record),
                        insulinShot.getAmount(),insulinName);
                records.add(new Record(insulinShot, insulinShot.getTime(), text));
            }

            List<Meal> mealRecords = daoAccess.fetchMeals();
            for (Meal meal: mealRecords) {
                StringBuilder sb = new StringBuilder();
                List<MealIngredient> mealIngredients = daoAccess.fetchMealIngredients(meal.getId());
                for (MealIngredient mealIngredient : mealIngredients) {
                    Ingredient ingredient = daoAccess.fetchIngredient(
                            mealIngredient.getIngredientId());
                    int ingredientResourceId = RESOURCES.getIdentifier(
                            ingredient.getIngredientCode(),
                            Constants.STRING_RES_TYPE, "com.veve.shotsandsugar");
                    sb.append(RESOURCES.getText(ingredientResourceId));
                    sb.append(", ");
                    sb.append(mealIngredient.getIngredientWeightGramms());
                    sb.append("g");
                    sb.append("; ");
                }
                records.add(new Record(meal, meal.getTime(), sb.toString()));
            }

            List<ActivityPeriod> activityRecords = daoAccess.fetchActivityPeriods();
            for (ActivityPeriod activityPeriod : activityRecords) {
                Activity activity = daoAccess.fetchActivity(activityPeriod.getActivityId());
                    int activityNameId = RESOURCES.getIdentifier(activity.getActivityCode(),
                            Constants.STRING_RES_TYPE,"com.veve.shotsandsugar");
                    String activityName = RESOURCES.getString(activityNameId);
                    int durationInMin =
                            (int)(activityPeriod.getEndTime()-activityPeriod.getStartTime())
                                    /Constants.MS_IN_MINUTE;
                    String text;
                    if (durationInMin < Constants.MIN_IN_HOUR) {
                        text = String.format(Locale.getDefault(),
                                RESOURCES.getString(R.string.physical_activity_duration_minutes),
                                activityName, durationInMin);
                    } else {
                        int durationInHours = (int)(durationInMin / Constants.MIN_IN_HOUR);
                        durationInMin = durationInMin % Constants.MIN_IN_HOUR;
                        text = String.format(Locale.getDefault(),
                                RESOURCES.getString(R.string.physical_activity_duration_hours_minutes),
                                activityName, durationInHours, durationInMin);
                    }
                    records.add(new Record(activityPeriod, activityPeriod.getEndTime(), text));
                }

            List<OtherRecord> otherRecords = daoAccess.fetchOtherRecords();
            for (OtherRecord otherRecord : otherRecords) {
                Other other = daoAccess.fetchOther(otherRecord.getOtherId());
                int otherNameId = RESOURCES.getIdentifier(other.getCode(),
                        Constants.STRING_RES_TYPE,"com.veve.shotsandsugar");
                String otherName = RESOURCES.getString(otherNameId);
                String text = String.format(Locale.getDefault(),
                        RESOURCES.getString(R.string.other_record),
                        otherName);
                records.add(new Record(otherRecord, otherRecord.getStartTime(), text));
            }

            Collections.sort(records);
            return records;

        }
    }

}

