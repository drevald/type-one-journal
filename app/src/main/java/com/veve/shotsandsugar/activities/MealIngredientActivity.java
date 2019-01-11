package com.veve.shotsandsugar.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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

    int mealIngredientPosition;


    @Override
    protected void onPostResume() {
        super.onPostResume();
        mealIngredientPosition = getIntent().getIntExtra("mealIngredientPosition", -1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_ingredient);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getIntent().getLongExtra("mealIngredientPosition", -1);

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
        productSelection.setAdapter(new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.activity_meal_ingredient_item,
                ingredientsNamesList));
        productSelection.setSelection(getIntent().getIntExtra("ingredientId", 0));

        EditText weightInput = findViewById(R.id.weightInput);
        if (getIntent().getStringExtra("weightGramms") != null)
            weightInput.setText(getIntent().getStringExtra("weightGramms"));

        FloatingActionButton fab = findViewById(R.id.saveMealIngredient);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOne = new Intent(getApplicationContext(), MealActivity.class);
                intentOne.putExtra("mealIngredientPosition", mealIngredientPosition);
                intentOne.putExtra("ingredientId", (int)productSelection.getSelectedItemId());
                intentOne.putExtra("weightGramms",
                        Integer.parseInt(weightInput.getText().toString()));
                Log.d(getClass().getName(), "Setting ingredientId = "
                        +  productSelection.getSelectedItemId() + "weightGrams="+"weightGrams="+
                        Integer.parseInt(weightInput.getText().toString())+ "to intent "
                        + getIntent().hashCode());
                startActivity(intentOne);
            }
        });

    }

    //////////////////  DB TASK  ///////////////////////////////////////////////////

}
