package com.dbs.volley.activities;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dbs.volley.R;
import com.dbs.volley.application.Volley;
import com.dbs.volley.database.DatabaseAdapter;

public class VolunteerDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_details);

        Toolbar toolbar = (Toolbar)findViewById(R.id.vol_toolbar);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getStringExtra("volName"));
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView city = (TextView)findViewById(R.id.vol_city_text_view);
        TextView state = (TextView)findViewById(R.id.vol_state_text_view);
        TextView phone = (TextView)findViewById(R.id.vol_phone_text_view);
        final TextView email = (TextView)findViewById(R.id.vol_email_text_view);

        city.setText(getIntent().getStringExtra("volCity"));
        state.setText(getIntent().getStringExtra("volState"));
        email.setText(getIntent().getStringExtra("volEmail"));
        phone.setText(getIntent().getStringExtra("volPhone"));
    }
}
