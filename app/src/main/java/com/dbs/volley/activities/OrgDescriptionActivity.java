package com.dbs.volley.activities;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dbs.volley.R;
import com.dbs.volley.application.Volley;
import com.dbs.volley.database.DatabaseAdapter;

public class OrgDescriptionActivity extends AppCompatActivity {

    private boolean canVol = true;
    private String vEmail = "";
    private DatabaseAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_description);

        Toolbar toolbar = (Toolbar)findViewById(R.id.org_toolbar);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getStringExtra("orgName"));
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();

        final CoordinatorLayout rootLayout = (CoordinatorLayout)findViewById(R.id.org_description_root_layout);

        TextView address = (TextView)findViewById(R.id.org_address_text_view);
        TextView city = (TextView)findViewById(R.id.org_city_text_view);
        TextView state = (TextView)findViewById(R.id.org_state_text_view);
        final TextView email = (TextView)findViewById(R.id.org_email_text_view);
        TextView phone = (TextView)findViewById(R.id.org_phone_text_view);
        TextView website = (TextView)findViewById(R.id.org_website_text_view);

        address.setText(getIntent().getStringExtra("orgAddress"));
        city.setText(getIntent().getStringExtra("orgCity"));
        state.setText(getIntent().getStringExtra("orgState"));
        email.setText(getIntent().getStringExtra("orgEmail"));
        phone.setText(getIntent().getStringExtra("orgPhone"));
        website.setText(getIntent().getStringExtra("orgWebsite"));

        vEmail = getSharedPreferences(Volley.VOL_DATA, MODE_PRIVATE).getString("volEmail", "");

        canVol = dbAdapter.canVolunteerForOrg(vEmail, email.getText().toString());

        final TextView volButton = (TextView)findViewById(R.id.org_volunteer_submit_button);

        if (!canVol){
            volButton.setText(R.string.leave_organization);
        }
        else{
            volButton.setText(R.string.volunteer);
        }

        volButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (volButton.getText().toString().equals(getString(R.string.volunteer))){
                    Snackbar.make(rootLayout, "You are now a volunteer for this organization!", Snackbar.LENGTH_SHORT).show();
                    dbAdapter.volForDelete(vEmail);
                    dbAdapter.volForInsert(vEmail, email.getText().toString(), 4);
                    volButton.setText(R.string.leave_organization);
                }
                else{
                    Snackbar.make(rootLayout, "You are no longer a volunteer for this organization!", Snackbar.LENGTH_SHORT).show();
                    dbAdapter.volForDelete(vEmail);
                    volButton.setText(R.string.volunteer);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }
}
