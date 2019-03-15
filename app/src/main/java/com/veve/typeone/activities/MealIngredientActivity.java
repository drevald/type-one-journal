package com.veve.typeone.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.veve.typeone.R;
import com.veve.typeone.model.Ingredient;
import com.veve.typeone.model.IngredientUnit;
import com.veve.typeone.model.MealIngredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MealIngredientActivity extends DatabaseActivity {

    MealIngredient mealIngredient;

    List<Ingredient> ingredientsList;

    List<IngredientUnit> ingredientUnitList;

    List<String> ingredientsNamesList = Arrays.asList(new String[] {"Uno", "Duos", "Tres"});

    String[] ingredientsNamesArray = new String[] {"Uno", "Duos", "Tres"};

    int mealIngredientPosition;

    Spinner productSelection;

    Spinner unitSelection;

    EditText weightInput;

    TextView productLabel;

    @Override
    protected void onResume() {
        super.onResume();
        updateIngredients();
        updateView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onPostResume();
        mealIngredientPosition = intent.getIntExtra("mealIngredientPosition", -1);
        if (intent.getSerializableExtra("mealIngredient") != null) {
            mealIngredient = (MealIngredient) intent.getSerializableExtra("mealIngredient");
            productLabel.setText(productLabel.getText().toString() + "_" + mealIngredientPosition);
            Log.d(getClass().getName(),
                    "Received: " + mealIngredient.toString() + " at " + mealIngredientPosition);
        }
        updateView();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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

        updateIngredients();

        productSelection = findViewById(R.id.productSelection);
        productSelection.setAdapter(new ArrayAdapter<Ingredient>(
                getApplicationContext(),
                R.layout.list_item,
                ingredientsList){

            @Override
            public long getItemId(int position) {
                Log.d("adapter", "getItemId("+position+")");
                return ingredientsList.get(position).getId();
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                Log.d("adapter", "getView("+position+")");
                convertView = (TextView)getLayoutInflater().inflate(R.layout.list_item, null);
                ((TextView)(convertView)).setText(ingredientsNamesList.get(position));
                return convertView;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                Log.d("adapter", "getDropDownView("+position+")");
                convertView = (TextView)getLayoutInflater().inflate(R.layout.list_item, null);
                ((TextView)(convertView)).setText(ingredientsNamesList.get(position));
                return convertView;
            }

        });

        productSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IngredienUnitTask ingredienUnitTask = new IngredienUnitTask();
                ingredienUnitTask.execute(id);
                try {
                    ingredientUnitList = ingredienUnitTask.get();
                } catch (Exception e) {
                    Log.e(getClass().getName(), e.getLocalizedMessage());
                }
                updateView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        unitSelection = findViewById(R.id.unitsList);
//        unitSelection.setAdapter(new ArrayAdapter(
//                getApplicationContext(),
//                R.layout.list_item,
//                ingredientsNamesArray));
        unitSelection.setAdapter(new CursorAdapter(){
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return null;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {

            }
        });


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

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MealActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        ImageButton newIngredientButton = findViewById(R.id.newIngredient);
        newIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewIngredientActivity.class);
                intent.putExtra("mealIngredient", mealIngredient);
                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                startActivity(intent);
            }
        });

        updateView();

    }

    private void updateIngredients() {
        try {
            MealActivity.ProductFinderTask task = new MealActivity.ProductFinderTask();
            task.execute();
            ingredientsList = task.get();
            ingredientsNamesList = new ArrayList<String>();
            for (Ingredient ingredient : ingredientsList) {
                ingredientsNamesList.add(
                        getLocalizedStringFromCode(ingredient.getIngredientCode(),
                                ingredient.getIngredientName()));
            }
            Log.d(getClass().getName(), "Ingredients list " + ingredientsList.hashCode()
                    + " has " + ingredientsList.size() + " items added");

            IngredienUnitTask ingredienUnitTask = new IngredienUnitTask();
            ingredienUnitTask.execute((long)ingredientsList.get(1).getId());
            try {
                ingredientUnitList = ingredienUnitTask.get();
            } catch (Exception e) {
                Log.e(getClass().getName(), e.getLocalizedMessage());
            }

        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage());
        }
    }

    private void updateView() {
        ((ArrayAdapter)(productSelection.getAdapter())).notifyDataSetChanged();
        for (Ingredient listedIngredient : ingredientsList) {
            if (mealIngredient.getIngredientId() == listedIngredient.getId())
                productSelection.setSelection(ingredientsList.indexOf(listedIngredient));
        }
        weightInput.setText(String.valueOf(mealIngredient.getIngredientWeightGramms()));
        if (ingredientUnitList != null && ingredientUnitList.size() > 0) {
            findViewById(R.id.unitLabel).setVisibility(View.VISIBLE);
            findViewById(R.id.unitsList).setVisibility(View.VISIBLE);
            findViewById(R.id.unitQuantityLabel).setVisibility(View.VISIBLE);
            findViewById(R.id.unitQuantityInput).setVisibility(View.VISIBLE);
            ((ArrayAdapter)(unitSelection.getAdapter())).notifyDataSetChanged();
        } else {
            findViewById(R.id.unitLabel).setVisibility(View.GONE);
            findViewById(R.id.unitsList).setVisibility(View.GONE);
            findViewById(R.id.unitQuantityLabel).setVisibility(View.GONE);
            findViewById(R.id.unitQuantityInput).setVisibility(View.GONE);
        }
    }

    //////////////////  DB TASK  ///////////////////////////////////////////////////

    static class IngredienUnitTask extends AsyncTask<Long, Void, List<IngredientUnit>> {

        @Override
        protected List<IngredientUnit> doInBackground(Long... longs) {
            List<IngredientUnit> result = daoAccess.fetchIngredientUnits(longs[0].longValue());
            return result;
        }

    }

}
