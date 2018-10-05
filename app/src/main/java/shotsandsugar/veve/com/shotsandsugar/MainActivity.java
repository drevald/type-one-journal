package shotsandsugar.veve.com.shotsandsugar;

import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sugarButton = findViewById(R.id.sugarButton);
        sugarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intentOne = new Intent(MainActivity.this, SugarActivity.class);
            intentOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intentOne);
            }
        });
    }

}
