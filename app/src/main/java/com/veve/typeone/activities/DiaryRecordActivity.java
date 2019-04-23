package com.veve.typeone.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_record);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText glucoseLevel = findViewById(R.id.glucoseLevel);
        EditText insulineShot = findViewById(R.id.insulineShot);
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
                Float glucoseLevelValue = Float.parseFloat(glucoseLevel.getText().toString());
                Float insulineShotValie = Float.parseFloat(insulineShot.getText().toString());
                recordSaverTask.execute(glucoseLevelValue, insulineShotValie);
                Intent intent = new Intent(DiaryRecordActivity.this, DiaryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mealId = intent.getLongExtra("mealId", -1);
        if ( mealId > 0) {
            mealDetails = findViewById(R.id.mealDetails);
            mealDetails.setVisibility(View.VISIBLE);
            MealIngredientTask mealIngredientTask = new MealIngredientTask();
            mealIngredientTask.execute(mealId);
        }
    }



    /////////////////////    DB TASKS   //////////////////////////////////////////

    class RecordSaverTask extends AsyncTask<Float, Void, Void> {
        @Override
        protected Void doInBackground(Float... params) {
            if (diaryRecordId == 0) {
                ThreeColumnRecord diaryRecord = new ThreeColumnRecord();
                diaryRecord.setTime(System.currentTimeMillis());
                diaryRecord.setGlucoseLevel(params[0]);
                diaryRecord.setInsulinShot(params[1]);
                diaryRecordId = daoAccess.insertThreeColumnRecord(diaryRecord);
            }
            return null;
        }
    }

    class MealIngredientTask extends AsyncTask<Long, Void, List<MealIngredient>> {

        List<Ingredient> ingredients;
        @Override
        protected List<MealIngredient> doInBackground(Long... longs) {
            ingredients = daoAccess.fetchIngredients();
            return daoAccess.fetchMealIngredients(longs[0]);
        }

        @Override
        protected void onPostExecute(List<MealIngredient> mealIngredients) {
            super.onPostExecute(mealIngredients);
            String details = "";
            for (MealIngredient mealIngredient : mealIngredients) {
                details.concat(String.format("%s %d gramm;",
                getLocalizedStringFromCode(getIngredientById(mealIngredient.getIngredientId()).getIngredientCode(),""),
                mealIngredient.getIngredientWeightGramms()));
            }
            mealDetails.setText(details);
        }

        private Ingredient getIngredientById(long id) {
            for (Ingredient ingredient : ingredients) {
                if (ingredient.getId() == id) {
                    return ingredient;
                }
            }
            return null;
        }
    }

}
