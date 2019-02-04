package com.veve.typeone.activities;

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
import android.widget.TextView;

import com.veve.typeone.Constants;
import com.veve.typeone.R;
import com.veve.typeone.model.Ingredient;
import com.veve.typeone.model.Meal;
import com.veve.typeone.model.MealIngredient;

import java.util.ArrayList;
import java.util.List;

public class MealIngredientActivity extends DatabaseActivity {

    MealIngredient mealIngredient;

    List<Ingredient> ingredientsList;

    List<String> ingredientsNamesList;

    int mealIngredientPosition;

    Spinner productSelection;

    EditText weightInput;

    TextView productLabel;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onPostResume();
        mealIngredientPosition = intent.getIntExtra("mealIngredientPosition", -1);
        mealIngredient = (MealIngredient) intent.getSerializableExtra("mealIngredient");
        productLabel.setText(productLabel.getText().toString() + "_" + mealIngredientPosition);
        Log.d(getClass().getName(),
                "Received: " + mealIngredient.toString() + " at " + mealIngredientPosition);
        updateView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_ingredient);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mealIngredientPosition = getIntent().getIntExtra("mealIngredientPosition", -1);
        mealIngredient = (MealIngredient) getIntent().getSerializableExtra("mealIngredient");

        productLabel = findViewById(R.id.productLabel);

        try {
            MealActivity.ProductFinderTask task = new MealActivity.ProductFinderTask();
            task.execute();
            ingredientsList = task.get();
            ingredientsNamesList = new ArrayList<String>();
            for (Ingredient ingredient : ingredientsList) {
                ingredientsNamesList.add(getLocalizedStringFromCode(ingredient.getIngredientCode()));
            }
            Log.d(getClass().getName(), "Ingredients list " + ingredientsList.hashCode()
                    + " has " + ingredientsList.size() + " items added");
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage());
        }

        productSelection = findViewById(R.id.productSelection);
        productSelection.setAdapter(new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.activity_meal_ingredient_item,
                ingredientsNamesList));

        weightInput = findViewById(R.id.weightInput);

        FloatingActionButton fab = findViewById(R.id.saveMealIngredient);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOne = new Intent(getApplicationContext(), MealActivity.class);
                intentOne.putExtra("mealIngredientPosition", mealIngredientPosition);
                mealIngredient.setIngredientId(productSelection.getSelectedItemId());
                mealIngredient.setIngredientWeightGramms(Integer.parseInt(weightInput.getText().toString()));
                intentOne.putExtra("mealIngredient", mealIngredient);
                intentOne.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Log.d(getClass().getName(),
                        "Returning: " + mealIngredient.toString()
                                + " at " + mealIngredientPosition);
                startActivity(intentOne);
            }
        });

        updateView();

    }

    private void updateView() {
        productSelection.setSelection((int)mealIngredient.getIngredientId());
        weightInput.setText(String.valueOf(mealIngredient.getIngredientWeightGramms()));
    }

    //////////////////  DB TASK  ///////////////////////////////////////////////////

}
