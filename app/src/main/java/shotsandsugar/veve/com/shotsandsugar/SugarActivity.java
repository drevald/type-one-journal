package shotsandsugar.veve.com.shotsandsugar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.List;

import shotsandsugar.veve.com.shotsandsugar.model.SugarLevel;

public class SugarActivity extends DatabaseActivity {

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

        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOne = new Intent(getApplicationContext(), MainActivity.class);
                intentOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentOne);
            }
        });

        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddSugarTask(daoAccess).execute();
            }
        });

    }

    static class AddSugarTask extends AsyncTask<Void, Void, Void> {

        DaoAccess daoAccess;

        AddSugarTask(DaoAccess daoAccess) {
            this.daoAccess = daoAccess;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            daoAccess.insertSugarLevel(new SugarLevel(2.0f));
            return null;
        }

    }

}
