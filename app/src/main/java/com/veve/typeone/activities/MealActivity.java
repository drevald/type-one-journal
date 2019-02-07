package com.veve.typeone.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.veve.typeone.Constants;
import com.veve.typeone.R;
import com.veve.typeone.model.Ingredient;
import com.veve.typeone.model.Meal;
import com.veve.typeone.model.MealIngredient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class MealActivity extends DatabaseActivity {

    static List<MealIngredient> mealIngredients;

    static List<Ingredient> ingredients;

    static ArrayAdapter ingredientsListAdapter;

    static TextView counterText;

    static ImageButton saveMealButton;

    static long mealId;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onPostResume();
        Log.d(getClass().getName(),"onPostResume");
        int mealIngredientPosition = intent.getIntExtra("mealIngredientPosition", -1);
        int ingredientId = intent.getIntExtra("ingredientId", -1);
        MealIngredient mealIngredient = (MealIngredient)intent.getSerializableExtra("mealIngredient");
        Log.d(getClass().getName(),
                "Received: " + mealIngredient + " at " + mealIngredientPosition);
        if (mealIngredientPosition < 0 && mealIngredient == null) {
            return;
        } else if (mealIngredientPosition < 0) {
            Log.d(getClass().getName(), "Received new: " + mealIngredient.toString());
            mealIngredients.add(mealIngredient);
        } else {
            mealIngredients.set(mealIngredientPosition, mealIngredient);
            Log.d(getClass().getName(), "Received update " + mealIngredientPosition + " " + mealIngredient.toString()+" at " + mealIngredientPosition);
        }
        getIntent().removeExtra("mealIngredientPosition");
        getIntent().removeExtra("mealIngredient");
        updateActivity();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mealIngredients", (Serializable) mealIngredients);
        Log.d(getClass().getName(), "onSaveInstanceState List of " + mealIngredients.size() + " saved");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getClass().getName(),"onCreate");

        setContentView(R.layout.activity_meal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null && savedInstanceState.get("mealIngredients") != null) {
            mealIngredients = (List<MealIngredient>) savedInstanceState.get("mealIngredients");
            Log.d(getClass().getName(),"List of " + mealIngredients.size() + " restored");
        } else {
            mealIngredients = new ArrayList<MealIngredient>();
            Log.d(getClass().getName(),"new meals list created");
        }

        try {
            ingredients = new ProductFinderTask().execute().get();
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage());
        }

        // List
        ListView list  = findViewById(R.id.mealIngredientsList);
        ingredientsListAdapter = new ArrayAdapter<MealIngredient>(getApplicationContext(), R.layout.meal_ingredient_record_view){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                    convertView = getLayoutInflater().inflate(R.layout.meal_ingredient_record, null);
                    TextView mealIngredientText = convertView.findViewById(R.id.mealIngredient);
                    MealIngredient mealIngredient = mealIngredients.get(position);
                    Ingredient ingredient = ingredients.get((int)mealIngredient.getIngredientId());
                    int ingredientResourceId = RESOURCES.getIdentifier(
                    ingredient.getIngredientCode(),
                    Constants.STRING_RES_TYPE, "com.veve.shotsandsugar");
                    String mealRecordText = RESOURCES.getString(R.string.ingredient_record,
                    RESOURCES.getText(ingredientResourceId),
                    mealIngredient.getIngredientWeightGramms());
                    mealIngredientText.setText(mealRecordText);

                    mealIngredientText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), MealIngredientActivity.class);
                            intent.putExtra("mealIngredientPosition", position);
                            intent.putExtra("mealIngredient", mealIngredients.get(position));
                            Log.d(getClass().getName(),"Sending " + mealIngredient.toString() + " at " + position);
                            startActivity(intent);
                        }
                    });

                    ImageButton removeButton = convertView.findViewById(R.id.removeButton);
                    removeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mealIngredients.remove(position);
                            updateActivity();
                        }
                    });

                return convertView;
            }
        };



        list.setAdapter(ingredientsListAdapter);

        // Counter text
        counterText = findViewById(R.id.counterText);

        // Save
        saveMealButton = findViewById(R.id.saveMeal);
        saveMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(getClass().getSimpleName(), "before storing " + mealIngredients.size() + " ingredients");
                AddMealTask addMealTask = new AddMealTask();
                addMealTask.execute();
                Log.d(getClass().getSimpleName(), "after storing " + mealIngredients.size() + " ingredients");
                Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
                backIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(backIntent);
                Log.d(getClass().getSimpleName(), "clean ingredients " + mealIngredients.size() + " left");
            }
        });

        // Back
        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOne = new Intent(getApplicationContext(), MainActivity.class);
                intentOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentOne);
            }
        });


        // Add
        ImageButton addIngredientButton = findViewById(R.id.addIngredient);
        addIngredientButton.setOnClickListener(new AddIngredientListener());

        updateActivity();

    }

    static void updateActivity() {
        ingredientsListAdapter.clear();
        ingredientsListAdapter.addAll(mealIngredients);
        if (ingredientsListAdapter.getCount() == 0) {
            saveMealButton.setEnabled(false);
            counterText.clearComposingText();
        } else {
            saveMealButton.setEnabled(true);
            float breadUnitsTotal = 0f;
            float kKalTotal = 0f;
            for (MealIngredient mealIngredient : mealIngredients) {
                Ingredient ingredient = ingredients.get((int)mealIngredient.getIngredientId());
                breadUnitsTotal += ingredient.getBreadUnitsPer100g()
                        * mealIngredient.getIngredientWeightGramms() * 0.01;
                kKalTotal += ingredient.getEnergyKkalPer100g()
                        * mealIngredient.getIngredientWeightGramms() * 0.01;
            }
            String counterTextValue = String.format(Locale.getDefault(),
                    RESOURCES.getString(R.string.bread_units_counter), breadUnitsTotal, kKalTotal);
            counterText.setText(counterTextValue);
            ingredientsListAdapter.notifyDataSetChanged();
        }
    }

////////////////    DATABASE TASKS    //////////////////////////////////////////////////////////////

    static class ProductFinderTask extends AsyncTask<Void, Void, List<Ingredient>> {

        @Override
        protected List<Ingredient> doInBackground(Void... voids) {
            return daoAccess.fetchIngredients();
        }

    }

    static class AddMealTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(getClass().getSimpleName(), "AddMealTask pre-execute");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(getClass().getSimpleName(), "AddMealTask");
            if(mealId == 0) {
                mealId = daoAccess.insertMeal(new Meal(System.currentTimeMillis()));
            }
            Log.d(getClass().getSimpleName(), "iterating " + mealIngredients.size() + "ingredients");
            for (MealIngredient mealIngredient : mealIngredients){
                Log.d(getClass().getSimpleName(), "storing  " + mealIngredient.toString());
                mealIngredient.setMealId(mealId);
            }
            daoAccess.insertMealIngredients(mealIngredients);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mealIngredients.clear();
            mealId = 0;
            updateActivity();
        }
    }

/////////////////     LISTENERS     ////////////////////////////////////////////////////////////////

    class AddIngredientListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intentOne = new Intent(getApplicationContext(), MealIngredientActivity.class);
            MealIngredient mealIngredient = new MealIngredient();
            intentOne.putExtra("mealIngredient", mealIngredient);
            Log.d(getClass().getName(), "Adding " + mealIngredient);
            startActivity(intentOne);
        }

    }
}





