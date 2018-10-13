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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mealIngredients.add(new MealIngredient());
        mealIngredients.add(new MealIngredient());
        mealIngredients.add(new MealIngredient());
        mealIngredients.add(new MealIngredient());

        setContentView(R.layout.activity_meal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOne = new Intent(getApplicationContext(), MainActivity.class);
                intentOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentOne);
            }
        });

        try {
            ingredients = new ProductFinderTask(daoAccess).execute().get();
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage());
        }


        ListView listView = (ListView)findViewById(R.id.mealsList);
        listView.setAdapter(new BaseAdapter() {
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
                if(convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.meals_list, parent, false);
                }
                if(position % 2 == 0)
                    convertView.findViewById(R.id.ingredient).setBackgroundColor(Color.LTGRAY);
                ((TextView)convertView.findViewById(R.id.product))
                        .setText(RESOURCES.getString(R.string.product_name));
                ((TextView)convertView.findViewById(R.id.weight))
                        .setText(RESOURCES.getString(R.string.product_weight));
                Spinner productSelection = ((Spinner)convertView.findViewById(R.id.productSelection));
                productSelection.setAdapter(new WeightSpinnerAdapter(getApplicationContext()));
                productSelection.setOnItemSelectedListener(new ProductSelectionListener(position));

                EditText weightInput = convertView.findViewById(R.id.weightInput);
                Button add10gButton = convertView.findViewById(R.id.add10gButton);
                Button minus10gButton = convertView.findViewById(R.id.minus10gButton);
                Button add100gButton = convertView.findViewById(R.id.add100gButton);
                Button minus100gButton = convertView.findViewById(R.id.minus100gButton);
                Button resetButton = convertView.findViewById(R.id.resetButton);

                add10gButton.setOnClickListener(new WeightChangeListener(10));
                minus10gButton.setOnClickListener(new WeightChangeListener(-10));
                add100gButton.setOnClickListener(new WeightChangeListener(100));
                minus100gButton.setOnClickListener(new WeightChangeListener(-100));

                resetButton.setOnClickListener(new WeightResetListener());

                weightInput.setOnClickListener(new ProductWeightListener(position));

                return convertView;

            }
        });

    }

    public void ingredientSelected() {

    }

    static class WeightSpinnerAdapter extends BaseAdapter {

        Context context;

        public WeightSpinnerAdapter(Context context) {
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
            if(convertView == null) {
                TextView view = new TextView(context);
                int ingredientResourceId = RESOURCES.getIdentifier(
                        ingredients.get(position).getIngredientCode(),
                        Constants.STRING_RES_TYPE, context.getPackageName());
                view.setText(RESOURCES.getText(ingredientResourceId));
                view.setTextSize(16);
                view.setTextColor(Color.BLACK);
                convertView = view;
            }
            return convertView;
        }

    }

    static class ProductFinderTask extends AsyncTask<Void, Void, List<Ingredient>> {

        DaoAccess daoAccess;

        public ProductFinderTask(DaoAccess daoAccess) {
            this.daoAccess =daoAccess;
        }

        @Override
        protected List<Ingredient> doInBackground(Void... voids) {
            return daoAccess.fetchIngredients();
        }
    }

    class ProductSelectionListener  implements AdapterView.OnItemSelectedListener {

        int productNumber;

        public ProductSelectionListener(int productNumber) {
            this.productNumber = productNumber;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int productPosition, long id) {
            Log.d(getClass().getName(),
                    String.format("Meal no #%d selected at position %d", productNumber, productPosition));
            MealIngredient mealIngredient = mealIngredients.get(productPosition);
            mealIngredient.setIngredientId(id);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Log.d(getClass().getName(), "Nothing selected");
        }

    }

    class WeightChangeListener implements Button.OnClickListener {

        int amount;

        public WeightChangeListener(int amount) {
            this.amount = amount;
        }

        @Override
        public void onClick(View v) {
            EditText editText = ((LinearLayout)v.getParent()).findViewById(R.id.weightInput);
            Log.d(getClass().getName(),
                    String.format("Value before is %s", editText.getText().toString()));
            if(amount + Integer.valueOf(editText.getText().toString())>=0)
                editText.setText(
                        String.valueOf(amount + Integer.valueOf(editText.getText().toString())));
            Log.d(getClass().getName(),
                    String.format("Value after is %s", editText.getText().toString()));
        }
    }

    class WeightResetListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            EditText editText = ((LinearLayout)v.getParent()).findViewById(R.id.weightInput);
            editText.setText("0");
        }
    }

    class ProductWeightListener implements Button.OnClickListener {

        int productNumber;

        public ProductWeightListener(int productNumber) {
            this.productNumber = productNumber;
        }

        @Override
        public void onClick(View v) {
            EditText editText = ((LinearLayout)v.getParent()).findViewById(R.id.weightInput);
            Log.d(getClass().getName(),
                    String.format("Meal no #%d weight is %s", productNumber, editText.getText().toString()));
            MealIngredient mealIngredient = mealIngredients.get(productNumber);
            mealIngredient.setIngredientWeightGramms(Integer.valueOf(editText.getText().toString()));
        }

    }

}
