package com.veve.shotsandsugar.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.veve.shotsandsugar.Constants;
import com.veve.shotsandsugar.R;
import com.veve.shotsandsugar.model.Ingredient;
import com.veve.shotsandsugar.model.Meal;
import com.veve.shotsandsugar.model.MealIngredient;

import java.util.List;

public class MealIngredientActivity extends DatabaseActivity {

    List<Ingredient> ingredientsList;

    static MealIngredient mealIngredient;

    static Long mealId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_ingredient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            ingredientsList = new MealActivity.ProductFinderTask(daoAccess).execute().get();
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage());
        }

        Spinner productSelection = findViewById(R.id.productSelection);
        productSelection.setAdapter(new ProductSpinnerAdapter(getApplicationContext()));

        EditText weightInput = findViewById(R.id.weightInput);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.saveMealIngredient);
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
                    //intentOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
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

    class ProductSpinnerAdapter extends BaseAdapter {

        Context context;

        ProductSpinnerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return ingredientsList.size();
        }

        @Override
        public Object getItem(int position) {
            return ingredientsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ingredientsList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                TextView view = new TextView(context);
                int ingredientResourceId = RESOURCES.getIdentifier(
                        ingredientsList.get(position).getIngredientCode(),
                        Constants.STRING_RES_TYPE, context.getPackageName());
                view.setText(RESOURCES.getText(ingredientResourceId));
                view.setTextSize(16);
                view.setTextColor(Color.BLACK);
                view.setBackgroundResource(R.drawable.drop_down);
                view.setGravity(Gravity.START|Gravity.CENTER);
                view.setWidth(300);
                convertView = view;
            }
            return convertView;
        }

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
