package com.veve.shotsandsugar.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.veve.shotsandsugar.Constants;
import com.veve.shotsandsugar.DaoAccess;
import com.veve.shotsandsugar.R;
import com.veve.shotsandsugar.model.Ingredient;
import com.veve.shotsandsugar.model.Meal;
import com.veve.shotsandsugar.model.MealIngredient;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class MealActivity extends DatabaseActivity {

    List<MealIngredient> mealIngredients = new ArrayList<MealIngredient>();

    static List<Ingredient> ingredients = new ArrayList<Ingredient>();

    IngredientsListAdapter ingredientsListAdapter;

    Button saveMealButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_meal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOne = new Intent(getApplicationContext(), MainActivity.class);
                intentOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentOne);
            }
        });

        Button addIngredientButton = (Button) findViewById(R.id.addIngredient);
        addIngredientButton.setOnClickListener(new AddProductListener());

        saveMealButton = (Button) findViewById(R.id.saveMeal);
        saveMealButton.setOnClickListener(new SaveMealListener());

        try {
            ingredients = new ProductFinderTask(daoAccess).execute().get();
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage());
        }

        ListView listView = findViewById(R.id.mealsList);
        ingredientsListAdapter = new IngredientsListAdapter();
        listView.setAdapter(ingredientsListAdapter);

        updateActivity();

    }
    
    void updateActivity() {
        ingredientsListAdapter.notifyDataSetChanged();
        if (ingredientsListAdapter.getCount() == 0) {
            saveMealButton.setVisibility(View.INVISIBLE);
        } else {
            saveMealButton.setVisibility(View.VISIBLE);
        }
    }


//////////////    DATABASE TASKS    ////////////////////////////////////////////////////////////////

    static class ProductFinderTask extends AsyncTask<Void, Void, List<Ingredient>> {

        DaoAccess daoAccess;

        ProductFinderTask(DaoAccess daoAccess) {
            this.daoAccess = daoAccess;
        }

        @Override
        protected List<Ingredient> doInBackground(Void... voids) {
            return daoAccess.fetchIngredients();
        }
    }

    static class MealSaverTask extends AsyncTask<List<MealIngredient>, Void, Void> {

        @Override
        protected Void doInBackground(List<MealIngredient>... lists) {
            Long mealId = daoAccess.insertMeal(new Meal(System.currentTimeMillis()));
            for (MealIngredient mealIngredient : lists[0]) {
                mealIngredient.setMealId(mealId);
            }
            daoAccess.insertMealIngredients(lists[0]);
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

    class WeightChangeListener implements Button.OnClickListener {

        MealIngredient mealIngredient;

        int amount;

        WeightChangeListener(MealIngredient mealIngredient, int amount) {
            this.mealIngredient = mealIngredient;
            this.amount = amount;
        }

        @Override
        public void onClick(View v) {
            EditText editText = ((LinearLayout) v.getParent()).findViewById(R.id.weightInput);
            Log.d(getClass().getName(),
                    String.format("Value before is %s", editText.getText().toString()));
            if (amount + Integer.valueOf(editText.getText().toString()) >= 0)
                editText.setText(
                        String.valueOf(amount + Integer.valueOf(editText.getText().toString())));
            Log.d(getClass().getName(),
                    String.format("Value after is %s", editText.getText().toString()));
            mealIngredient.setIngredientWeightGramms(Integer.valueOf(editText.getText().toString()));
        }
    }

    class WeightResetListener implements Button.OnClickListener {

        MealIngredient mealIngredient;

        WeightResetListener(MealIngredient mealIngredient) {
            this.mealIngredient = mealIngredient;
        }

        @Override
        public void onClick(View v) {
            EditText editText = ((LinearLayout) v.getParent()).findViewById(R.id.weightInput);
            editText.setText("0");
            mealIngredient.setIngredientWeightGramms(0);
        }
    }

    class ProductWeightListener implements Button.OnClickListener {

        MealIngredient mealIngredient;

        ProductWeightListener(MealIngredient mealIngredient) {
            this.mealIngredient = mealIngredient;
        }

        @Override
        public void onClick(View v) {
            EditText editText = ((LinearLayout) v.getParent()).findViewById(R.id.weightInput);
            Log.d(getClass().getName(),
                    String.format("Meal no #%d weight is %s", mealIngredient.getId(), editText.getText().toString()));
            mealIngredient.setIngredientWeightGramms(Integer.valueOf(editText.getText().toString()));
        }

    }

    class RemoveProductListener implements Button.OnClickListener {

        int productNumber;

        RemoveProductListener(int productNumber) {
            this.productNumber = productNumber;
        }

        @Override
        public void onClick(View v) {
            mealIngredients.remove(productNumber);
            updateActivity();
        }

    }

    class AddProductListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            mealIngredients.add(new MealIngredient());
            updateActivity();
        }

    }

    class SaveMealListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            new MealSaverTask().execute(mealIngredients);
            Intent intentOne = new Intent(getApplicationContext(), MainActivity.class);
            intentOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intentOne);
            mealIngredients.clear();
            updateActivity();
        }

    }


//////////////   ADAPTERS     //////////////////////////////////////////////////////////////////////

    class ProductSpinnerAdapter extends BaseAdapter {

        Context context;

        ProductSpinnerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return ingredients.size();
        }

        @Override
        public Object getItem(int position) {
            return ingredients.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ingredients.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                TextView view = new TextView(context);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(params);
                int ingredientResourceId = RESOURCES.getIdentifier(
                        ingredients.get(position).getIngredientCode(),
                        Constants.STRING_RES_TYPE, context.getPackageName());
                view.setText(RESOURCES.getText(ingredientResourceId));
                view.setTextSize(16);
                view.setTextColor(Color.BLACK);
                view.setWidth(300);
                convertView = view;
            }
            return convertView;
        }

    }


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
            MealIngredient mealIngredient = mealIngredients.get(position);
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.meals_list, parent, false);
            }
            if (position % 2 == 0)
                convertView.findViewById(R.id.ingredient).setBackgroundColor(Color.WHITE);
            else
                convertView.findViewById(R.id.ingredient).setBackgroundColor(Color.LTGRAY);

            ((TextView) convertView.findViewById(R.id.product))
                    .setText(RESOURCES.getString(R.string.product_name));
            ((TextView) convertView.findViewById(R.id.weight))
                    .setText(RESOURCES.getString(R.string.product_weight));
            Spinner productSelection = ((Spinner) convertView.findViewById(R.id.productSelection));
            productSelection.setAdapter(new ProductSpinnerAdapter(getApplicationContext()));
            productSelection.setOnItemSelectedListener(
                    new ProductSelectionListener(mealIngredients.get(position)));
            productSelection.setSelection(mealIngredients.get(position).getIngredientId());

            EditText weightInput = convertView.findViewById(R.id.weightInput);
            weightInput.setText(String.valueOf(mealIngredient.getIngredientWeightGramms()));
            Button add10gButton = convertView.findViewById(R.id.add10gButton);
            Button minus10gButton = convertView.findViewById(R.id.minus10gButton);
            Button add100gButton = convertView.findViewById(R.id.add100gButton);
            Button minus100gButton = convertView.findViewById(R.id.minus100gButton);
            Button resetButton = convertView.findViewById(R.id.resetButton);
            Button removeButton = convertView.findViewById(R.id.removeButton);

            add10gButton.setOnClickListener(new WeightChangeListener(mealIngredient, 10));
            minus10gButton.setOnClickListener(new WeightChangeListener(mealIngredient, -10));
            add100gButton.setOnClickListener(new WeightChangeListener(mealIngredient, 100));
            minus100gButton.setOnClickListener(new WeightChangeListener(mealIngredient, -100));

            resetButton.setOnClickListener(new WeightResetListener(mealIngredient));

            weightInput.setOnClickListener(
                    new ProductWeightListener(mealIngredients.get(position)));

            removeButton.setOnClickListener(new RemoveProductListener(position));

            return convertView;

        }
    }
}
