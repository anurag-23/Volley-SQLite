package com.dbs.volley.activities;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dbs.volley.R;
import com.dbs.volley.application.Volley;
import com.dbs.volley.database.DatabaseAdapter;

public class EventDetailsActivity extends AppCompatActivity {

    private DatabaseAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Toolbar toolbar = (Toolbar)findViewById(R.id.event_toolbar);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getStringExtra("eventName"));
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();

        final CoordinatorLayout rootLayout = (CoordinatorLayout)findViewById(R.id.event_details_root_layout);

        TextView address = (TextView)findViewById(R.id.event_address_text_view);
        TextView city = (TextView)findViewById(R.id.event_city_text_view);
        final TextView email = (TextView)findViewById(R.id.event_org_email_text_view);
        TextView date = (TextView)findViewById(R.id.event_date_text_view);
        TextView time = (TextView)findViewById(R.id.event_time_text_view);

        address.setText(getIntent().getStringExtra("eventAddress"));
        city.setText(getIntent().getStringExtra("eventCity"));
        email.setText(getIntent().getStringExtra("orgEmail"));
        date.setText(getIntent().getStringExtra("eventDate"));
        time.setText(getIntent().getStringExtra("eventTime"));

        final TextView delButton = (TextView)findViewById(R.id.event_delete_button);

        if (getSharedPreferences(Volley.VOL_DATA, MODE_PRIVATE).getString("loggedInAs", "volunteer").equals("volunteer")){
            delButton.setVisibility(View.GONE);
        }
        else{
            delButton.setVisibility(View.VISIBLE);
        }

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbAdapter.eventDelete(getIntent().getStringExtra("eventName"));
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }
}
