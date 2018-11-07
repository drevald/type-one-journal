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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.veve.shotsandsugar.Constants;
import com.veve.shotsandsugar.R;
import com.veve.shotsandsugar.model.Activity;

import java.util.List;

public class ActivityActivity extends DatabaseActivity {

    static List<Activity> activityList;

    static TimePickerDialog timePickerDialog;

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


        try {
            activityList = new GetActivitiesTask().execute().get();
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage());
        }

        Spinner spinner = findViewById(R.id.activities);
        spinner.setAdapter(new ActivitiesAdapter(getApplicationContext()));

    }

    public void setFrom(View view) {
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            }
        }, 1, 1, false );
        timePickerDialog.show();
    }

    static class GetActivitiesTask extends AsyncTask<Void, Void, List<Activity>> {

        @Override
        protected List<Activity> doInBackground(Void... voids) {
            return daoAccess.listActivity();
        }

    }

    class ActivitiesAdapter extends BaseAdapter {

        Context context;

        public ActivitiesAdapter(Context context) {
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
                view.setWidth(300);
                convertView = view;
            }
            return convertView;
        }

    }

}
