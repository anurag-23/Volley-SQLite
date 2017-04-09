package com.dbs.volley.activities;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dbs.volley.R;
import com.dbs.volley.application.Volley;
import com.dbs.volley.database.DatabaseAdapter;
import com.dbs.volley.models.Event;
import com.dbs.volley.models.Organization;

public class CreateEventActivity extends AppCompatActivity {

    private DatabaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar)findViewById(R.id.create_event_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(getString(R.string.create_event));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final CoordinatorLayout rootLayout = (CoordinatorLayout)findViewById(R.id.create_event_root_layout);

        adapter = new DatabaseAdapter(this);
        adapter.open();

        final TextInputEditText name = (TextInputEditText)findViewById(R.id.event_name_edit_text);
        final TextInputEditText date = (TextInputEditText)findViewById(R.id.event_date_edit_text);
        final TextInputEditText time = (TextInputEditText)findViewById(R.id.event_time_edit_text);
        final TextInputEditText address = (TextInputEditText)findViewById(R.id.event_address_edit_text);
        final TextInputEditText city = (TextInputEditText)findViewById(R.id.event_city_edit_text);

        TextView createEvent = (TextView)findViewById(R.id.create_event_submit_button);

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Event e = new Event();
                e.setName(name.getText().toString());
                e.setOrgEmail(getSharedPreferences(Volley.VOL_DATA, MODE_PRIVATE).getString("orgEmail", ""));
                e.setEventDate(date.getText().toString());
                e.setEventTime(time.getText().toString());
                e.setAddress(address.getText().toString());
                e.setCity(city.getText().toString());


                if (e.getName().equals("") || e.getOrgEmail().equals("") || e.getEventDate().equals("") || e.getEventTime().equals("") || e.getAddress().equals("") || e.getCity().equals("")){
                    Snackbar.make(rootLayout, "Please fill in all the required details!", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    try{
                        adapter.eventInsert(e);
                        Snackbar.make(rootLayout, "Event successfully created!", Snackbar.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setResult(RESULT_OK);
                                finish();
                            }
                        }, 500);
                    }catch(SQLiteConstraintException ce){
                        Snackbar.make(rootLayout, "Event already exists!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.close();
    }
}
