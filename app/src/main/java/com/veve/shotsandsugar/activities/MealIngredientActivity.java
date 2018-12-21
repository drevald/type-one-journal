package com.veve.shotsandsugar.activities;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.veve.shotsandsugar.Constants;
import com.veve.shotsandsugar.R;
import com.veve.shotsandsugar.model.Ingredient;

import java.util.List;

public class MealIngredientActivity extends DatabaseActivity {

    List<Ingredient> ingredientsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_ingredient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.saveMealIngredient);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        try {
            ingredientsList = new MealActivity.ProductFinderTask(daoAccess).execute().get();
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage());
        }

        Spinner productSelection = findViewById(R.id.productSelection);
        productSelection.setAdapter(new ProductSpinnerAdapter(getApplicationContext()));

    }

    class ProductSpinnerAdapter extends BaseAdapter {

        Context context;

        ProductSpinnerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 1000;
            //return ingredients.size();
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

}
