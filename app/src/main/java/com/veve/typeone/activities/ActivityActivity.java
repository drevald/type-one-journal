package com.veve.typeone.activities;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.veve.typeone.Constants;
import com.veve.typeone.R;
import com.veve.typeone.model.Activity;
import com.veve.typeone.model.ActivityPeriod;
import com.veve.typeone.model.Ingredient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ActivityActivity extends DatabaseActivity {

    final static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

    static List<Activity> activityList;

    static List<String> activityNamesList;

    static TimePickerDialog timePickerDialog;

    EditText fromTimeInput;

    EditText toTimeInput;

    LinearLayout otherInterval;

    long fromTime;

    long toTime;

    Spinner activities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activities = findViewById(R.id.activities);

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
        saveButton.setOnClickListener((view) -> {
            new ActivitySaverTask().execute((int)activities.getSelectedItemId(), fromTime, toTime);
            Intent intentOne = new Intent(getApplicationContext(), MainActivity.class);
            intentOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intentOne);
        });


        activityNamesList = new ArrayList<String>();
        try {
            activityList = new GetActivitiesTask().execute().get();
            for (Activity activity : activityList) {
                activityNamesList.add(getLocalizedStringFromCode(activity.getActivityCode()));
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage());
        }





        Spinner spinner = findViewById(R.id.activities);
        spinner.setAdapter(new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.activity_meal_ingredient_item,
                activityNamesList));

        fromTimeInput = findViewById(R.id.fromTime);

        toTimeInput = findViewById(R.id.toTime);

        otherInterval = findViewById(R.id.otherInterval);

    }

    public void setFrom(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                fromTimeInput.setText(sdf.format(calendar.getTime()));
                fromTime = calendar.getTime().getTime();
                checkDates(view);
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    public void setTo(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                toTimeInput.setText(sdf.format(calendar.getTime()));
                toTime = calendar.getTime().getTime();
                checkDates(view);
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void checkDates(View view) {
        if (fromTime > 0 && toTime > 0) {
            if (fromTime > toTime) {
                Snackbar.make(view, "Wrong time", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                fromTime = 0;
                toTime = 0;
                fromTimeInput.setText("");
                toTimeInput.setText("");
            }
        }
    }

    public void setInterval(View view) {
        Calendar calendar = Calendar.getInstance();
        switch (((RadioButton) view).getId()) {
            case 1: {
                otherInterval.setVisibility(View.INVISIBLE);
                toTime = calendar.getTime().getTime();
                calendar.add(Calendar.MINUTE, -15);
                fromTime = calendar.getTime().getTime();
                break;
            }
            case 2: {
                otherInterval.setVisibility(View.INVISIBLE);
                toTime = calendar.getTime().getTime();
                calendar.add(Calendar.MINUTE, -30);
                fromTime = calendar.getTime().getTime();
                break;
            }
            case 3: {
                otherInterval.setVisibility(View.INVISIBLE);
                toTime = calendar.getTime().getTime();
                calendar.add(Calendar.MINUTE, -60);
                fromTime = calendar.getTime().getTime();
                break;
            }
            case 4: {
                otherInterval.setVisibility(View.VISIBLE);
                toTime = calendar.getTime().getTime();
                toTimeInput.setText(sdf.format(calendar.getTime()));
                calendar.add(Calendar.MINUTE, -120);
                fromTimeInput.setText(sdf.format(calendar.getTime()));
                fromTime = calendar.getTime().getTime();
                break;
            }
        }
    }

    class ActivitiesAdapter extends BaseAdapter {

        Context context;

        ActivitiesAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return activityList.size();
        }

        @Override
        public Object getItem(int position) {
            return activityList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return activityList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                TextView view = new TextView(context);
                int ingredientResourceId = RESOURCES.getIdentifier(
                        activityList.get(position).getActivityCode(),
                        Constants.STRING_RES_TYPE, context.getPackageName());
                view.setText(RESOURCES.getText(ingredientResourceId));
                view.setBackgroundResource(R.drawable.drop_down);
                view.setGravity(Gravity.CENTER | Gravity.START);
                view.setWidth(300);
                convertView = view;
            }
            return convertView;
        }

    }

    static class GetActivitiesTask extends AsyncTask<Void, Void, List<Activity>> {

        @Override
        protected List<Activity> doInBackground(Void... voids) {
            return daoAccess.listActivity();
        }

    }

    static class ActivitySaverTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... objects) {
            ActivityPeriod activityPeriod = new ActivityPeriod(
                    (Integer) objects[0], (Long)objects[1], (Long)objects[2]);
            daoAccess.insertActivityPeriod(activityPeriod);
            return null;
        }

    }

}
