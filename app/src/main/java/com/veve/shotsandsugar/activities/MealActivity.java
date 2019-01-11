package com.veve.shotsandsugar.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.veve.shotsandsugar.Constants;
import com.veve.shotsandsugar.R;
import com.veve.shotsandsugar.model.Ingredient;
import com.veve.shotsandsugar.model.MealIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MealActivity extends DatabaseActivity {

    static Long mealId;

    static List<MealIngredient> mealIngredients = new ArrayList<MealIngredient>();

    static List<Ingredient> ingredients = new ArrayList<Ingredient>();

    static IngredientsListAdapter ingredientsListAdapter;

    static FloatingActionButton saveMealButton;

    static TextView counterText;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        int mealIngredientPosition = getIntent().getIntExtra("mealIngredientPosition", -1);
        if (mealIngredientPosition < 0){
            MealIngredient mealIngredient = new MealIngredient();
            mealIngredient.setIngredientId(getIntent().getIntExtra("ingredientId", 0));
            mealIngredient.setIngredientWeightGramms(getIntent().getIntExtra("weightGramms", 0));
            mealIngredients.add(mealIngredient);
        } else {
            MealIngredient mealIngredient = mealIngredients.get(mealIngredientPosition);
            mealIngredient.setIngredientId(getIntent().getIntExtra("ingredientId", 0));
            mealIngredient.setIngredientWeightGramms(getIntent().getIntExtra("weightGramms", 0));
        }
        updateActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_meal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOne = new Intent(getApplicationContext(), MainActivity.class);
                intentOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentOne);
            }
        });

        ImageButton addIngredientButton = findViewById(R.id.addIngredient);
        addIngredientButton.setOnClickListener(new AddProductListener());

        saveMealButton = findViewById(R.id.saveMeal);

        try {
            ingredients = new ProductFinderTask().execute().get();
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage());
        }

        ListView listView = findViewById(R.id.mealsList);
        ingredientsListAdapter = new IngredientsListAdapter();
        listView.setAdapter(ingredientsListAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d(getClass().getName(), "Selecting position " + position);
//                MealIngredient mealIngredient = mealIngredients.get(position);
//                Intent modifyIngredientIndent = new Intent(getApplicationContext(), MealIngredientActivity.class);
//                modifyIngredientIndent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                modifyIngredientIndent.putExtra("mealIngredientPosition", position);
//                modifyIngredientIndent.putExtra("ingredientId", mealIngredient.getIngredientId());
//                modifyIngredientIndent.putExtra("weightGramms", mealIngredient.getIngredientWeightGramms());
//                startActivity(modifyIngredientIndent);
//            }
//        });

        counterText = findViewById(R.id.counterText);

        updateActivity();

    }

    static void updateActivity() {
        ingredientsListAdapter.notifyDataSetChanged();
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

    @Override
    protected void onResume() {
        super.onResume();
        updateActivity();
    }

//////////////    DATABASE TASKS    ////////////////////////////////////////////////////////////////

    static class ProductFinderTask extends AsyncTask<Void, Void, List<Ingredient>> {
        @Override
        protected List<Ingredient> doInBackground(Void... voids) {
            return daoAccess.fetchIngredients();
        }
    }

    static class MealIngredientsFinderTask extends AsyncTask<Long, Void, List<MealIngredient>> {
        @Override
        protected List<MealIngredient> doInBackground(Long... ids) {
            List<MealIngredient> result = null;
            if (ids == null || ids[0] == null || ids[0] == 0) {
                result = new ArrayList<>();
            } else {
                result = daoAccess.fetchMealIngredients(ids[0]);
            }
            return result;
        }
    }

    static class MealIngredientRemoverTask extends AsyncTask<Long, Void, Void> {
        @Override
        protected Void doInBackground(Long... longs) {
            daoAccess.deleteMealIngredient(longs[0]);
            return null;
        }
    }



//////////////   LISTENERS    //////////////////////////////////////////////////////////////////////

    class ProductSelectionListener implements AdapterView.OnItemSelectedListener {

        MealIngredient mealIngredient;

        ProductSelectionListener(MealIngredient mealIngredient) {
            this.mealIngredient = mealIngredient;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int productPosition, long id) {
            Log.d(getClass().getName(),
                    String.format("Meal no #%d selected at position %d", id, productPosition));
            mealIngredient.setIngredientId(productPosition);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Log.d(getClass().getName(), "Nothing selected");
        }

    }

    class RemoveProductListener implements Button.OnClickListener {

        int productNumber;

        RemoveProductListener(int productNumber) {
            this.productNumber = productNumber;
        }

        @Override
        public void onClick(View v) {
            MealIngredientRemoverTask task = new MealIngredientRemoverTask();
            task.execute((long)mealIngredients.get(productNumber).getId());
            mealIngredients.remove(productNumber);
            updateActivity();
        }

    }

    class AddProductListener implements Button.OnClickListener {

//        @Override
//        public void onClick(View v) {
//            mealIngredients.add(new MealIngredient());
//            updateActivity();
//        }


        @Override
        public void onClick(View v) {
            Intent intentOne = new Intent(getApplicationContext(), MealIngredientActivity.class);
            intentOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intentOne.putExtra("mealId", mealId);
            startActivity(intentOne);
        }

    }

    class IngredientWeightTextWatcher implements TextWatcher {

        MealIngredient mealIngredient;

        IngredientWeightTextWatcher(MealIngredient mealIngredient) {
            this.mealIngredient = mealIngredient;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                mealIngredient.setIngredientWeightGramms(Integer.parseInt(s.toString()));
//                updateActivity();
            } catch (NumberFormatException nfe) {
                Log.d(getClass().getName(), nfe.getMessage());
            }
        }
    }

//////////////   ADAPTERS     //////////////////////////////////////////////////////////////////////




    class IngredientsListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mealIngredients.size();
        }

        @Override
        public Object getItem(int position) {
            return mealIngredients.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mealIngredients.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getLayoutInflater()
                        .inflate(R.layout.meal_ingredient_record, parent, false);
            }

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
                    Log.d(getClass().getName(), "List is clicked at position " + position);
                    MealIngredient mealIngredient = mealIngredients.get(position);
                    Intent modifyIngredientIndent = new Intent(getApplicationContext(), MealIngredientActivity.class);
                    modifyIngredientIndent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    modifyIngredientIndent.putExtra("mealIngredientPosition", position);
                    modifyIngredientIndent.putExtra("ingredientId", mealIngredient.getIngredientId());
                    modifyIngredientIndent.putExtra("weightGramms", mealIngredient.getIngredientWeightGramms());
                    startActivity(modifyIngredientIndent);
                }
            });

            ImageButton removeButton = convertView.findViewById(R.id.removeButton);
            removeButton.setOnClickListener(new RemoveProductListener(position));

            return convertView;
        }

    }

}





