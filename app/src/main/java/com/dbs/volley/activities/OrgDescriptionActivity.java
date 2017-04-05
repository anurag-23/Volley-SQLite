package com.dbs.volley.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.dbs.volley.R;

public class OrgDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_description);

        Toolbar toolbar = (Toolbar)findViewById(R.id.org_toolbar);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getStringExtra("orgName"));
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView address = (TextView)findViewById(R.id.org_address_text_view);
        TextView city = (TextView)findViewById(R.id.org_city_text_view);
        TextView state = (TextView)findViewById(R.id.org_state_text_view);
        TextView email = (TextView)findViewById(R.id.org_email_text_view);
        TextView phone = (TextView)findViewById(R.id.org_phone_text_view);
        TextView website = (TextView)findViewById(R.id.org_website_text_view);

        address.setText(getIntent().getStringExtra("orgAddress"));
        city.setText(getIntent().getStringExtra("orgCity"));
        state.setText(getIntent().getStringExtra("orgState"));
        email.setText(getIntent().getStringExtra("orgEmail"));
        phone.setText(getIntent().getStringExtra("orgPhone"));
        website.setText(getIntent().getStringExtra("orgWebsite"));

    }
}
