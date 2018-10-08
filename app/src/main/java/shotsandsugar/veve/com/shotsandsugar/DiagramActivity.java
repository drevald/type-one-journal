package shotsandsugar.veve.com.shotsandsugar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.PrecomputedText;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.Result;

import shotsandsugar.veve.com.shotsandsugar.model.SugarLevel;

public class DiagramActivity extends DatabaseActivity {

    static SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
    List<SugarLevel> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagram);
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
            records = new DiagramTask(daoAccess).execute().get();
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage(), e);
        }

        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return records.size();
            }

            @Override
            public Object getItem(int position) {
                return records.get(position);
            }

            @Override
            public long getItemId(int position) {
                return records.get(position).getId();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    TextView textView = new TextView(getApplicationContext());
                    SugarLevel record = records.get(position);
                    Date resultdate = new Date(record.getTimestamp());
                    textView.setText(String.format(Locale.getDefault(), "Date %S id:%d value:%f",
                            sdf.format(resultdate), record.getId(), record.getValue()));
                    return textView;
                }
                return convertView;
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        try {
            records = new DiagramTask(daoAccess).execute().get();
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getLocalizedMessage(), e);
        }
    }

    static class DiagramTask extends AsyncTask<Void, Void, List<SugarLevel>> {

        DaoAccess daoAccess;

        DiagramTask(DaoAccess daoAccess) {
            this.daoAccess = daoAccess;
        }

        @Override
        protected List<SugarLevel> doInBackground(Void... voids) {
            List<SugarLevel> records = daoAccess.fetchSugarLevels();
            for (SugarLevel record : records) {
                Date resultdate = new Date(record.getTimestamp());
                Log.d(getClass().getName(),
                        String.format("Date %S id:%d value:%f",
                                sdf.format(resultdate), record.getId(), record.getValue()));
            }
            return records;
        }
    }

}
