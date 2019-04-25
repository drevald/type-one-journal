package com.veve.typeone.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.veve.typeone.R;
import com.veve.typeone.model.Ingredient;
import com.veve.typeone.model.MealIngredient;
import com.veve.typeone.model.ThreeColumnRecord;

import java.util.List;

import static com.veve.typeone.activities.MealActivity.mealId;

public class DiaryRecordActivity extends DatabaseActivity {

    long diaryRecordId;

    long mealRecordId;

    TextView mealDetails;

    EditText glucoseLevel;

    EditText insulineShot;

    Button setMealButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_record);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        glucoseLevel = findViewById(R.id.glucoseLevel);
        insulineShot = findViewById(R.id.insulineShot);
        mealDetails = findViewById(R.id.mealDetails);
        Button setMealButton = findViewById(R.id.setMeal);

        setMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryRecordActivity.this, MealActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        FloatingActionButton saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordSaverTask recordSaverTask = new RecordSaverTask();
                Float glucoseLevelValue, insulineShotValue;
                try {
                    glucoseLevelValue = Float.parseFloat(glucoseLevel.getText().toString());
                } catch (Exception e) {
                    glucoseLevelValue = -1f;
                }
                try {
                    insulineShotValue = Float.parseFloat(insulineShot.getText().toString());
                } catch (Exception e) {
                    insulineShotValue = -1f;
                }
                recordSaverTask.execute(glucoseLevelValue, insulineShotValue, mealDetails.getText().toString(), mealRecordId);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(getClass().getName(), "Recieved meal info id:"+mealId+" mealDetails:"+intent.getLongExtra("mealId", -1)
                + "  details " + intent.getStringExtra("mealString"));
        mealId = intent.getLongExtra("mealId", -1);
        if ( mealId > 0) {
            mealDetails = findViewById(R.id.mealDetails);
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                  mealDetails.setVisibility(View.VISIBLE);
                  mealDetails.setText(intent.getStringExtra("mealString"));
                  mealRecordId=intent.getLongExtra("mealId", -1);
              }
            });
        }
    }

    /////////////////////    DB TASKS   //////////////////////////////////////////

    class RecordSaverTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            if (diaryRecordId == 0) {
                ThreeColumnRecord diaryRecord = new ThreeColumnRecord();
                diaryRecord.setTime(System.currentTimeMillis());
                diaryRecord.setGlucoseLevel((Float)params[0]);
                diaryRecord.setInsulinShot((Float)params[1]);
                diaryRecord.setMealDetails((String)params[2]);
                diaryRecord.setMealId((Long)params[3]);
                diaryRecordId = daoAccess.insertThreeColumnRecord(diaryRecord);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mealId = 0;
            insulineShot.clearComposingText();
            glucoseLevel.clearComposingText();
            mealDetails.clearComposingText();
            mealDetails.setVisibility(View.GONE);
            Intent intent = new Intent(DiaryRecordActivity.this, DiaryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

    }

}
