package com.veve.typeone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.veve.typeone.R;
import com.veve.typeone.model.MealIngredient;

import java.util.ArrayList;
import java.util.List;

import static com.veve.typeone.Constants.CARBOHYDRATES_PER_BREAD_UNIT;
import static com.veve.typeone.activities.DatabaseActivity.RESOURCES;


public class NewIngredientActivity extends AppCompatActivity {

    MealIngredient mealIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ingredient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mealIngredient = (MealIngredient)getIntent().getSerializableExtra("mealIngredient");

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MealIngredientActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("mealIngredient", mealIngredient);
                startActivity(intent);
            }
        });

        EditText ingredientNameInput = (EditText)findViewById(R.id.ingredientName);
        ingredientNameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        EditText glycemicIndexInput = (EditText)findViewById(R.id.glycemicIndex);

        EditText fatPer100gInput = (EditText)findViewById(R.id.fatPer100g);

        EditText carbohydratePer100gInput = (EditText)findViewById(R.id.carbohydratePer100g);

        EditText proteinPer100gInput = (EditText)findViewById(R.id.proteinPer100g);

        EditText energyKkalPer100gInput = (EditText)findViewById(R.id.energyKkalPer100g);

        EditText defaultWeightGrammsInput = (EditText)findViewById(R.id.defaultWeightGramms);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.createMealIngredient);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<String> errorMessages = new ArrayList<String>();

//                EditText ingredientNameInput = (EditText)findViewById(R.id.ingredientName);
//                String ingredientName = ingredientNameInput.getText().toString();
//                if (ingredientName.length() == 0) {
//                    errorMessages.add("Product name could not be empty");
//                }
//
//                EditText glycemicIndexInput = (EditText)findViewById(R.id.glycemicIndex);
//                float glycemicIndex = Float.parseFloat(glycemicIndexInput.getText().toString());
//                if (glycemicIndex < 0) {
//                    errorMessages.add("Glycemic index could not be nagative");
//                    glycemicIndexInput.setBackgroundColor(RESOURCES.getColor(R.color.wrongValue));
//                }
//
//                EditText fatPer100gInput = (EditText)findViewById(R.id.ingredientName);
//                int fatPer100g = Integer.parseInt(fatPer100gInput.getText().toString());
//                if (fatPer100g < 0 || fatPer100g > 100) {
//                    errorMessages.add("Fat percentage should be witin 0 to 100 limit");
//                    fatPer100gInput.setBackgroundColor(RESOURCES.getColor(R.color.wrongValue));
//                }
//
//                EditText carbohydratePer100gInput = (EditText)findViewById(R.id.carbohydratePer100g);
//                int carbohydratePer100g = Integer.parseInt(
//                        (carbohydratePer100gInput).getText().toString());
//
//                float breadUnitsPer100g = carbohydratePer100g/CARBOHYDRATES_PER_BREAD_UNIT;
//
//                EditText proteinPer100gInput = (EditText)findViewById(R.id.ingredientName);
//                int proteinPer100g = Integer.parseInt(proteinPer100gInput.getText().toString());
//
//                EditText energyKkalPer100gInput = (EditText)findViewById(R.id.ingredientName);
//                int energyKkalPer100g = Integer.parseInt(energyKkalPer100gInput.getText().toString());
//
//                EditText defaultWeightGrammsInput = (EditText)findViewById(R.id.ingredientName);
//                int defaultWeightGramms = Integer.parseInt(defaultWeightGrammsInput.getText().toString());

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mealIngredient = (MealIngredient)getIntent().getSerializableExtra("mealIngredient");
    }

}
