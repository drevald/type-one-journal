package com.veve.shotsandsugar.activities;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.veve.shotsandsugar.Constants;
import com.veve.shotsandsugar.R;
import com.veve.shotsandsugar.model.Activity;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ActivityActivity extends DatabaseActivity {

    final static Calendar CALENDAR = Calendar.getInstance();

    static List<Activity> activityList;

    static TimePickerDialog timePickerDialog;

    EditText fromTime;

    EditText toTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);
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

        RadioGroup intervalGroup = findViewById(R.id.interval);
        intervalGroup.check(0);


        try {
            activityList = new GetActivitiesTask().execute().get();
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage());
        }

        Spinner spinner = findViewById(R.id.activities);
        spinner.setAdapter(new ActivitiesAdapter(getApplicationContext()));

        fromTime = findViewById(R.id.fromTime);

        toTime = findViewById(R.id.toTime);

    }




    public void setFrom(View view) {
        int hour = CALENDAR.get(Calendar.HOUR_OF_DAY);
        int minute = CALENDAR.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                fromTime.setText(String.format(Locale.getDefault(), "%d2:%d2", hourOfDay, minute));
                Log.i(getClass().getName(), String.format("%d2:%d2", hourOfDay, minute));
            }
        }, hour, minute, true );
        timePickerDialog.show();
    }

    public void setTo(View view) {
        int hour = CALENDAR.get(Calendar.HOUR_OF_DAY);
        int minute = CALENDAR.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                toTime.setText(String.format(Locale.getDefault(), "%2d:%2d", hourOfDay, minute));
                Log.i(getClass().getName(), String.format("%dd:%dd", hourOfDay, minute));
            }
        }, hour, minute, true );
        timePickerDialog.show();
    }

    public void checkOthers(View view) {
    }

    static class GetActivitiesTask extends AsyncTask<Void, Void, List<Activity>> {

        @Override
        protected List<Activity> doInBackground(Void... voids) {
            return daoAccess.listActivity();
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
                view.setTextSize(16);
                view.setTextColor(Color.BLACK);
                view.setBackgroundResource(R.drawable.drop_down);
                view.setGravity(Gravity.CENTER|Gravity.START);
                view.setWidth(300);
                convertView = view;
            }
            return convertView;
        }

    }

}
