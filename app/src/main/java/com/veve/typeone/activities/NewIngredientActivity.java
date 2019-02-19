package com.veve.typeone.activities;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.veve.typeone.model.Ingredient;
import com.veve.typeone.model.MealIngredient;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.veve.typeone.Constants.CARBOHYDRATES_PER_BREAD_UNIT;
import static com.veve.typeone.activities.DatabaseActivity.RESOURCES;
import static com.veve.typeone.activities.DatabaseActivity.daoAccess;
import static java.lang.Float.parseFloat;


public class NewIngredientActivity extends AppCompatActivity {

    MealIngredient mealIngredient;

    boolean isDataValid = false;

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
        ingredientNameInput.setOnFocusChangeListener(new InputTextChecker());

        EditText glycemicIndexInput = (EditText)findViewById(R.id.glycemicIndex);
        glycemicIndexInput.setOnFocusChangeListener(new InputFloatChecker(0, 200));

        EditText fatPer100gInput = (EditText)findViewById(R.id.fatPer100g);
        fatPer100gInput.setOnFocusChangeListener(new InputFloatChecker(0, 100));

        EditText carbohydratePer100gInput = (EditText)findViewById(R.id.carbohydratePer100g);
        carbohydratePer100gInput.setOnFocusChangeListener(new InputFloatChecker(0, 100));

        EditText proteinPer100gInput = (EditText)findViewById(R.id.proteinPer100g);
        proteinPer100gInput.setOnFocusChangeListener(new InputFloatChecker(0, 100));

        EditText energyKkalPer100gInput = (EditText)findViewById(R.id.energyKkalPer100g);
        energyKkalPer100gInput.setOnFocusChangeListener(new InputFloatChecker(0, 1000));

        EditText defaultWeightGrammsInput = (EditText)findViewById(R.id.defaultWeightGramms);
        defaultWeightGrammsInput.setOnFocusChangeListener(new InputFloatChecker(0, 1000));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.createMealIngredient);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<InputValidationResponse> results = new ArrayList<InputValidationResponse>();
                results.add(InputChecker.checkText(ingredientNameInput));
                results.add(InputChecker.checkNumber(glycemicIndexInput, 0, 200));
                results.add(InputChecker.checkNumber(fatPer100gInput, 0, 100));
                results.add(InputChecker.checkNumber(carbohydratePer100gInput, 0, 100));
                results.add(InputChecker.checkNumber(proteinPer100gInput, 0, 100));
                results.add(InputChecker.checkNumber(energyKkalPer100gInput, 0, 1000));
                results.add(InputChecker.checkNumber(defaultWeightGrammsInput, 0, 2000));

                boolean isValid = true;

                for (InputValidationResponse result : results) {
                    isValid &= result.isValid();
                }

                if (isValid) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setCarbohydratePer100g(
                            Integer.parseInt(carbohydratePer100gInput.getText().toString()));
                    ingredient.setBreadUnitsPer100g(ingredient.getCarbohydratePer100g()
                            /CARBOHYDRATES_PER_BREAD_UNIT);
                    ingredient.setDefaultWeightGramms(Integer.parseInt(
                            defaultWeightGrammsInput.getText().toString()));
                    ingredient.setEnergyKkalPer100g(Integer.parseInt(
                            energyKkalPer100gInput.getText().toString()));
                    ingredient.setFatPer100g(Integer.parseInt(
                            fatPer100gInput.getText().toString()));
                    ingredient.setProteinPer100g(Integer.parseInt(
                            proteinPer100gInput.getText().toString()));
                    ingredient.setGlycemicIndex(Integer.parseInt(
                            glycemicIndexInput.getText().toString()));
                    ingredient.setIngredientName(
                            ingredientNameInput.getText().toString());
                    ingredient.setIngredientCode(null);
                    ingredient.setTypeId(1);
                    new NewProductTask().execute(ingredient);
                } else {
                    StringBuilder message = new StringBuilder();
                    for (InputValidationResponse result : results) {
                        if (!result.isValid()) {
                            message.append(result.getErrorMessage());
                            message.append("\n");
                        }
                    }
                    Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mealIngredient = (MealIngredient)getIntent().getSerializableExtra("mealIngredient");
    }

    public static class NewProductTask extends AsyncTask<Ingredient, Void, Void> {
        @Override
        protected Void doInBackground(Ingredient... ingredients) {
            daoAccess.insertIngredient(ingredients[0]);
            return null;
        }
    }

}

class InputChecker {

    public static InputValidationResponse checkText(EditText view) {
        if (view.getText().toString().length() < 1)
            return new InputValidationResponse(false, "Name should not be empty");
        return new InputValidationResponse(true, null);
    }

    public static InputValidationResponse checkNumber(EditText view, int min, int max) {
        try {
            float value = Float.parseFloat(view.getText().toString());
            if (value < min || value > max) {
                return new InputValidationResponse(false,
                        String.format(Locale.getDefault(),
                                "Value should be between %d and %d", min, max));
            } else {
                return new InputValidationResponse(true, null);
            }
        } catch (NumberFormatException e) {
            return new InputValidationResponse(false, "");
        }
    }

}

class InputValidationResponse {

    private boolean isValid;

    private String errorMessage;

    public InputValidationResponse(boolean isValid, String errorMessage) {
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

class InputFloatChecker implements View.OnFocusChangeListener {

    private int min;

    private int max;

    public InputFloatChecker(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus) {
            try {
                InputValidationResponse response = InputChecker.checkNumber((EditText)v, min, max);
                if (!response.isValid()) {
                    v.setBackgroundColor(RESOURCES.getColor(R.color.wrongValue));
                    Snackbar.make(v, response.getErrorMessage(),Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                 } else {
                    v.setBackgroundColor(RESOURCES.getColor(R.color.colorBackground));
                }
            } catch (NumberFormatException e) {
                v.setBackgroundColor(RESOURCES.getColor(R.color.wrongValue));
                Snackbar.make(v, "Invalid value", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }
}

class InputTextChecker implements View.OnFocusChangeListener {

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus) {
            InputValidationResponse response = InputChecker.checkText((EditText)v);
            if (!response.isValid()) {
                v.setBackgroundColor(RESOURCES.getColor(R.color.wrongValue));
                Snackbar.make(v, "Name should not be empty",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                v.setBackgroundColor(RESOURCES.getColor(R.color.colorBackground));
            }
        }
    }

}

