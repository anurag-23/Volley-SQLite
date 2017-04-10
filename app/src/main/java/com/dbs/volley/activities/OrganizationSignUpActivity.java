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
import com.dbs.volley.database.DatabaseAdapter;
import com.dbs.volley.models.Organization;
import com.dbs.volley.models.Volunteer;

public class OrganizationSignUpActivity extends AppCompatActivity {

    private static final String ORG_TYPE = "ORGANIZATION";
    private DatabaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_sign_up);
        Toolbar signUpToolbar = (Toolbar)findViewById(R.id.organization_sign_up_toolbar);
        setSupportActionBar(signUpToolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(getString(R.string.signup));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final CoordinatorLayout rootLayout = (CoordinatorLayout)findViewById(R.id.organization_signup_root_layout);

        adapter = new DatabaseAdapter(this);
        adapter.open();

        final TextInputEditText name = (TextInputEditText)findViewById(R.id.organization_signup_name_edit_text);
        final TextInputEditText email = (TextInputEditText)findViewById(R.id.organization_signup_email_edit_text);
        final TextInputEditText address = (TextInputEditText)findViewById(R.id.organization_signup_address_edit_text);
        final TextInputEditText phone = (TextInputEditText)findViewById(R.id.organization_signup_phone_edit_text);
        final TextInputEditText city = (TextInputEditText)findViewById(R.id.organization_signup_city_edit_text);
        final TextInputEditText state = (TextInputEditText)findViewById(R.id.organization_signup_state_edit_text);
        final TextInputEditText website = (TextInputEditText)findViewById(R.id.organization_signup_website_edit_text);
        final TextInputEditText password = (TextInputEditText)findViewById(R.id.organization_signup_password_edit_text);
        final TextInputEditText confirmPassword = (TextInputEditText)findViewById(R.id.organization_signup_confirm_password_edit_text);

        TextView signUp = (TextView)findViewById(R.id.organization_signup_submit_button);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Organization o = new Organization();
                o.setName(name.getText().toString());
                o.setEmail(email.getText().toString());
                o.setAddress(address.getText().toString());
                o.setPhone(phone.getText().toString());
                o.setWebsite(website.getText().toString());
                o.setCity(city.getText().toString());
                o.setState(state.getText().toString());

                final String p1 = password.getText().toString();
                String p2 = confirmPassword.getText().toString();

                if (o.getName().equals("") || o.getEmail().equals("") || o.getPhone().equals("") || o.getCity().equals("") || o.getWebsite().equals("") || o.getAddress().equals("") || o.getState().equals("") || p1.equals("") || p2.equals("")){
                    Snackbar.make(rootLayout, "Please fill in all the required details!", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    if (!p1.equals(p2)){
                        Snackbar.make(rootLayout, "Entered passwords don't match!", Snackbar.LENGTH_SHORT).show();
                    }
                    else{
                        try{
                            adapter.orgInsert(o);
                            adapter.loginInsert(o.getEmail(), p1, ORG_TYPE);
                            Snackbar.make(rootLayout, "Sign up successful!", Snackbar.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 500);
                        }catch(SQLiteConstraintException ce){
                            Snackbar.make(rootLayout, "Email already in use!", Snackbar.LENGTH_SHORT).show();
                        }
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
