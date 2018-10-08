package shotsandsugar.veve.com.shotsandsugar;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import shotsandsugar.veve.com.shotsandsugar.model.SugarLevel;

public class ShotActivity extends DatabaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shot);
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

        Button sugarButton = (Button) findViewById(R.id.sugarButton);
        sugarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SugarLevel sugarLevel = new SugarLevel(1.0f, System.currentTimeMillis());
                appDatabase.daoAccess().insertSugarLevel(sugarLevel);
            }
        });


    }

}
