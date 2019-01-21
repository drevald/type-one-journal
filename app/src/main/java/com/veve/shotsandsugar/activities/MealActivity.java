package com.veve.shotsandsugar.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.veve.shotsandsugar.Constants;
import com.veve.shotsandsugar.R;
import com.veve.shotsandsugar.model.Ingredient;
import com.veve.shotsandsugar.model.MealIngredient;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;



public class MealActivity extends DatabaseActivity {

    static List<MealIngredient> mealIngredients;

    static List<Ingredient> ingredients;

    static ArrayAdapter ingredientsListAdapter;

    static TextView counterText;

    static ImageButton saveMealButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_meal);

        mealIngredients = new ArrayList<MealIngredient>();
        mealIngredients.add(new MealIngredient(1, 2, 3));
        mealIngredients.add(new MealIngredient(1, 3, 3));
        mealIngredients.add(new MealIngredient(1, 4, 3));

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
                    Ingredient ingredient = ingredients.get(mealIngredient.getIngredientId());
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
                            intent.putExtra("mealIngredient", new MealIngredient(1, 1, 1));
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
        addIngredientButton.setOnClickListener(new AddingredientListener());

        updateActivity();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
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
            float kkalTotal = 0f;
            for (MealIngredient mealIngredient : mealIngredients) {
                Ingredient ingredient = ingredients.get(mealIngredient.getIngredientId());
                breadUnitsTotal += ingredient.getBreadUnitsPer100g()
                        * mealIngredient.getIngredientWeightGramms() * 0.01;
                kkalTotal += ingredient.getEnergyKkalPer100g()
                        * mealIngredient.getIngredientWeightGramms() * 0.01;
            }
            String counterTextValue = String.format(Locale.getDefault(),
                    RESOURCES.getString(R.string.bread_units_counter), breadUnitsTotal, kkalTotal);
            counterText.setText(counterTextValue);
        }
    }


////////////////    DATABASE TASKS    //////////////////////////////////////////////////////////////

    static class ProductFinderTask extends AsyncTask<Void, Void, List<Ingredient>> {
        @Override
        protected List<Ingredient> doInBackground(Void... voids) {
            return daoAccess.fetchIngredients();
        }
    }

    class RemoveProductListener implements Button.OnClickListener {

        int productNumber;

        RemoveProductListener(int productNumber) {
            this.productNumber = productNumber;
        }

        @Override
        public void onClick(View v) {
            MealIngredient mealIngredient = mealIngredients.get(productNumber);
            Log.d(getClass().getName(), "Removing ingredient at position " + productNumber + " " + mealIngredients.get(productNumber).getId());
            mealIngredients.remove(mealIngredient);
            updateActivity();
        }

    }

    class AddingredientListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intentOne = new Intent(getApplicationContext(), MealIngredientActivity.class);
            intentOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intentOne.putExtra("mealIngredient", new MealIngredient());
            startActivity(intentOne);
        }

    }

}





