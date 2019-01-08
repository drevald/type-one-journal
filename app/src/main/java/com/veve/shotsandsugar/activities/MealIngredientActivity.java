package com.veve.shotsandsugar.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.veve.shotsandsugar.Constants;
import com.veve.shotsandsugar.R;
import com.veve.shotsandsugar.model.Ingredient;
import com.veve.shotsandsugar.model.Meal;
import com.veve.shotsandsugar.model.MealIngredient;

import java.util.ArrayList;
import java.util.List;

public class MealIngredientActivity extends DatabaseActivity {

    List<Ingredient> ingredientsList;

    List<String> ingredientsNamesList;

    static MealIngredient mealIngredient;

    static Long mealId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_ingredient);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            MealActivity.ProductFinderTask task = new MealActivity.ProductFinderTask();
            task.execute();
            ingredientsList = task.get();
            ingredientsNamesList = new ArrayList<String>();
            for (Ingredient ingredient : ingredientsList) {
                int ingredientResourceId = RESOURCES.getIdentifier(
                        ingredient.getIngredientCode(),
                        Constants.STRING_RES_TYPE, getApplicationContext().getPackageName());
                ingredientsNamesList.add(RESOURCES.getText(ingredientResourceId).toString());
            }
            Log.d(getClass().getName(), "Ingredients list " + ingredientsList.hashCode()
                    + " has " + ingredientsList.size() + " items added");
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage());
        }

        Spinner productSelection = findViewById(R.id.productSelection);
        //productSelection.setAdapter(new ProductSpinnerAdapter(getApplicationContext()));
        productSelection.setAdapter(new ArrayAdapter(
                        getApplicationContext(),
                        R.layout.activity_meal_ingredient_item,
                        ingredientsNamesList));


        EditText weightInput = findViewById(R.id.weightInput);

        FloatingActionButton fab = findViewById(R.id.saveMealIngredient);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MealIngredientSaverTask task = new MealIngredientSaverTask();
                mealIngredient = new MealIngredient();
                mealIngredient.setIngredientId((int)productSelection.getSelectedItemId());
                mealIngredient.setIngredientWeightGramms(
                        Integer.parseInt(weightInput.getEditableText().toString()));
                task.execute(mealIngredient);
                try{
                    mealId = task.get();
                    Intent intentOne = new Intent(getApplicationContext(), MealActivity.class);
                    intentOne.putExtra("mealId", mealId);
                    Log.d(getClass().getName(), "Setting mealId="+mealId + " to intent "
                            + getIntent().hashCode());
                    startActivity(intentOne);
                } catch (Exception e) {
                    Log.e(getClass().getName(), e.getLocalizedMessage());
                }
            }
        });

    }




        //////////////////  DB TASK  ///////////////////////////////////////////////////

    static class MealIngredientSaverTask extends AsyncTask<MealIngredient, Void, Long> {
        @Override
        protected Long doInBackground(MealIngredient... mealIngredients) {
            if (mealId == null || mealId == 0) {
                Meal meal = new Meal(System.currentTimeMillis());
                mealId = daoAccess.insertMeal(meal);
            }
            mealIngredients[0].setMealId(mealId);
            daoAccess.insertMealIngredient(mealIngredients[0]);
            return mealId;
        }
    }

}
