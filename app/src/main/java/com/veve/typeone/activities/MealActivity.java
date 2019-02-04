package com.veve.typeone.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
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
    protected void onStop() {
        super.onStop();
        Log.d(getClass().getName(),"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(getClass().getName(),"onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mealIngredients", (Serializable) mealIngredients);
        Log.d(getClass().getName(), "onSaveInstanceState List of " + mealIngredients.size() + " saved");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        Log.d(getClass().getName(),"onRestoreInstanceState");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getClass().getName(),"onCreate");

        setContentView(R.layout.activity_meal);

        if (savedInstanceState != null && savedInstanceState.get("mealIngredients") != null) {
            mealIngredients = (List<MealIngredient>) savedInstanceState.get("mealIngredients");
            Log.d(getClass().getName(),"List of " + mealIngredients.size() + " restored");
        } else {
            mealIngredients = new ArrayList<MealIngredient>();
            mealIngredients.add(new MealIngredient(1, 2, 3));
            mealIngredients.add(new MealIngredient(1, 3, 3));
            mealIngredients.add(new MealIngredient(1, 4, 3));
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
            Log.d(getClass().getName(), "Removing ingredient at position " + productNumber + " ingredient:" + mealIngredients.toString());
            mealIngredients.remove(mealIngredient);
            updateActivity();
        }

    }

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





