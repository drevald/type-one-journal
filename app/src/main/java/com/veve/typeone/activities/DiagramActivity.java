package com.veve.typeone.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.veve.typeone.Constants;
import com.veve.typeone.DaoAccess;
import com.veve.typeone.model.Activity;
import com.veve.typeone.model.ActivityPeriod;
import com.veve.typeone.model.Ingredient;
import com.veve.typeone.model.Insulin;
import com.veve.typeone.model.InsulinShot;
import com.veve.typeone.model.Meal;
import com.veve.typeone.model.MealIngredient;
import com.veve.typeone.model.Other;
import com.veve.typeone.model.OtherRecord;
import com.veve.typeone.model.Record;
import com.veve.typeone.model.SugarLevel;

import com.veve.typeone.R;

public class DiagramActivity extends DatabaseActivity {

    static SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,\nHH:mm", Locale.getDefault());

    List<Record> records = new ArrayList<Record>();

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_diagram);
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

        FloatingActionButton sendButton = findViewById(R.id.send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer sb = new StringBuffer();
                Date resultdate;
                for (Record record : records) {
                    resultdate = new Date(record.getTimestamp());
                    sb.append(String.format(Locale.getDefault(), "%s",
                            sdf.format(resultdate)));
                    sb.append("\t\t");
                    sb.append(record.getText());
                    sb.append("\n");
                }

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"ddreval@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Report");
                i.putExtra(Intent.EXTRA_TEXT   , sb.toString());
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DiagramActivity.this,
                            "There are no email clients installed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView = (ListView)findViewById(R.id.listView);
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
                ImageButton removeButton = convertView.findViewById(R.id.removeButton);
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new RecordDeletionTask().execute(record);
                        update();
                    }
                });
                return convertView;
            }
        });

        update();

    }

    private void update() {
        records.clear();
        try {
            records = new DiagramTask(daoAccess).execute().get();
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage(), e);
        }
        ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }


    /////////////////  DB TASKS //////////////////////////////////////////////


    static class RecordDeletionTask extends AsyncTask<Record, Void, Void> {
        @Override
        protected Void doInBackground(Record... records) {
            Object originalRecord = records[0].getOriginalRecord();
            if (originalRecord instanceof Meal) {
                Meal meal = (Meal) originalRecord;
                daoAccess.deleteMealIngredients(meal.getId());
                daoAccess.deleteMeal(meal);
            } else if (originalRecord instanceof SugarLevel)  {
                SugarLevel sugarLevel = (SugarLevel) originalRecord;
                daoAccess.deleteSugarLevel(sugarLevel);
            } else if (originalRecord instanceof InsulinShot)  {
                InsulinShot insulinShot = (InsulinShot) originalRecord;
                daoAccess.deleteInsulitShot(insulinShot);
            } else if (originalRecord instanceof ActivityPeriod) {
                ActivityPeriod activityPeriod = (ActivityPeriod) originalRecord;
                daoAccess.deleteActivityPeriod(activityPeriod);
            } else if (originalRecord instanceof  OtherRecord) {
                OtherRecord otherRecord = (OtherRecord)originalRecord;
                daoAccess.deleteOtherRecord(otherRecord);
            }
            return null;
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


