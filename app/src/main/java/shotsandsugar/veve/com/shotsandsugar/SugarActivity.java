package shotsandsugar.veve.com.shotsandsugar;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class SugarActivity extends AppCompatActivity {

    private static final int PADDING = 5;
    private static final int COLUMNS_NUM = 4;
    private static final int ROWS_NUM = 5;
    private static final int MAX_SUGAR_LEVEL = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOne = new Intent(SugarActivity.this, MainActivity.class);
                intentOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentOne);
            }
        });

        final GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        gridLayout.setColumnCount(COLUMNS_NUM);
        gridLayout.setPadding(PADDING, PADDING, PADDING, PADDING);

        gridLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i0, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {

                gridLayout.setBackgroundColor(Color.GREEN);

            }
        });

//        int width = 240; //gridLayout.getWidth();
//        int height = 260; //gridLayout.getHeight();

        int width = gridLayout.getWidth();
        int height = gridLayout.getHeight();

        for (int i=0; i < MAX_SUGAR_LEVEL; i++) {

            Button button = new Button(getApplicationContext());
            button.setBackgroundColor(Color.GRAY);
            button.setTextColor(Color.WHITE);
            Log.d(getClass().getName(), "Grid width is " + width);
            Log.d(getClass().getName(), "Button width is " + (width - PADDING * (COLUMNS_NUM + 1))/ COLUMNS_NUM);
            int buttonWidth = (width - PADDING * (COLUMNS_NUM + 1))/ COLUMNS_NUM;
            int buttonHeight = (height - PADDING * (ROWS_NUM + 1))/ ROWS_NUM;
            button.setWidth(buttonWidth);
            button.setHeight(buttonHeight);

            button.setText(String.valueOf(i));

            ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(buttonWidth, buttonHeight);
            marginParams.setMargins(PADDING/2, PADDING/2, PADDING/2, PADDING/2);
            button.setLayoutParams(marginParams);

            Log.d(getClass().getName(), String.format(
                    "Adding button #%d with size %dx%d labelled %s", i,
                    button.getWidth(), button.getHeight(), button.getText()));

            gridLayout.addView(button);

        }


    }

}
