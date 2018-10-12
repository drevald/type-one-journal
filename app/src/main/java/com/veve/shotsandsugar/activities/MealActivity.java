package com.veve.shotsandsugar.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.veve.shotsandsugar.R;
import com.veve.shotsandsugar.model.Meal;
import com.veve.shotsandsugar.model.MealIngredient;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class MealActivity extends DatabaseActivity {

    List<MealIngredient> ingredients = new ArrayList<MealIngredient>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ingredients.add(new MealIngredient());

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

        ListView listView = (ListView)findViewById(R.id.mealsList);
        listView.setAdapter(new BaseAdapter() {
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
                    convertView = getLayoutInflater().inflate(R.layout.meals_list, parent, false);
                }
                ((TextView)convertView.findViewById(R.id.product)).setText("Product");
                ((TextView)convertView.findViewById(R.id.weight)).setText("Weight");
//                ((Spinner)convertView.findViewById(R.id.productSelection)).
//                        setAdapter(new WeightSpinnerAdapter(getApplicationContext()));
                ((Spinner)convertView.findViewById(R.id.weightSelection)).
                        setAdapter(new WeightSpinnerAdapter(getApplicationContext()));
                return convertView;

            }
        });

    }

    static class WeightSpinnerAdapter extends BaseAdapter {

        Context context;

        public WeightSpinnerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = new TextView(context);
            }
            return convertView;
        }

    }

}
