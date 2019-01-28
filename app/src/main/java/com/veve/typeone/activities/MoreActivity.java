package com.veve.typeone.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.veve.typeone.Constants;
import com.veve.typeone.R;
import com.veve.typeone.model.Other;
import com.veve.typeone.model.OtherRecord;

import java.util.Calendar;
import java.util.List;

public class MoreActivity extends DatabaseActivity {

    static List<Other> othersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
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

        try {
            othersList = new MoreActivity.ListOthersTask().execute().get();
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage());
        }

        Spinner other = findViewById(R.id.other);
        other.setAdapter(new OthersAdapter(getApplicationContext()));

        FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SaveOthersTask().execute((int)other.getSelectedItemId());
                Intent intentOne = new Intent(getApplicationContext(), MainActivity.class);
                intentOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentOne);
            }
        });

    }


    public static class ListOthersTask extends AsyncTask<Void, Void, List<Other>> {
        @Override
        protected List<Other> doInBackground(Void... voids) {
            return daoAccess.fetchOthers();
        }
    }

    public static class SaveOthersTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            long fromTime = calendar.getTime().getTime();
            calendar.add(Calendar.HOUR, 24);
            long toTime = calendar.getTime().getTime();
            OtherRecord otherRecord = new OtherRecord(integers[0], fromTime, toTime);
            daoAccess.insertOtherRecord(otherRecord);
            return null;
        }
    }

    class OthersAdapter extends BaseAdapter {

        Context context;

        OthersAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return othersList.size();
        }

        @Override
        public Object getItem(int position) {
            return othersList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return othersList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                TextView view = new TextView(context);
                int ingredientResourceId = RESOURCES.getIdentifier(
                        othersList.get(position).getCode(),
                        Constants.STRING_RES_TYPE, context.getPackageName());
                view.setText(RESOURCES.getText(ingredientResourceId));
                view.setTextSize(16);
                view.setTextColor(Color.BLACK);
                view.setBackgroundResource(R.drawable.drop_down);
                view.setGravity(Gravity.CENTER | Gravity.START);
                view.setWidth(300);
                convertView = view;
            }
            return convertView;
        }

    }

}
