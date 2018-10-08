package shotsandsugar.veve.com.shotsandsugar;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public abstract class DatabaseActivity extends AppCompatActivity {

    protected static final String DATABASE_NAME = "db";
    protected AppDatabase appDatabase;
    protected DaoAccess daoAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appDatabase = Room.databaseBuilder(
                getApplicationContext(), AppDatabase.class, "db").build();
        daoAccess = appDatabase.daoAccess();

    }

}
