package com.dbs.volley.activities;

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
import com.dbs.volley.models.Volunteer;

public class VolunteerSignUpActivity extends AppCompatActivity {

    private static final String VOL_TYPE = "VOLUNTEER";
    private DatabaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_sign_up);

        Toolbar signUpToolbar = (Toolbar)findViewById(R.id.volunteer_sign_up_toolbar);
        setSupportActionBar(signUpToolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(getString(R.string.signup));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final CoordinatorLayout rootLayout = (CoordinatorLayout)findViewById(R.id.volunteer_signup_root_layout);

        adapter = new DatabaseAdapter(this);
        adapter.open();

        final TextInputEditText name = (TextInputEditText)findViewById(R.id.volunteer_signup_name_edit_text);
        final TextInputEditText email = (TextInputEditText)findViewById(R.id.volunteer_signup_email_edit_text);
        final TextInputEditText phone = (TextInputEditText)findViewById(R.id.volunteer_signup_phone_edit_text);
        final TextInputEditText city = (TextInputEditText)findViewById(R.id.volunteer_signup_city_edit_text);
        final TextInputEditText state = (TextInputEditText)findViewById(R.id.volunteer_signup_state_edit_text);
        final TextInputEditText password = (TextInputEditText)findViewById(R.id.volunteer_signup_password_edit_text);
        final TextInputEditText confirmPassword = (TextInputEditText)findViewById(R.id.volunteer_signup_confirm_password_edit_text);

        TextView signUp = (TextView)findViewById(R.id.volunteer_signup_submit_button);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Volunteer v = new Volunteer();
                v.setName(name.getText().toString());
                v.setEmail(email.getText().toString());
                v.setPhone(phone.getText().toString());
                v.setCity(city.getText().toString());
                v.setState(state.getText().toString());

                final String p1 = password.getText().toString();
                String p2 = confirmPassword.getText().toString();

                if (v.getName().equals("") || v.getEmail().equals("") || v.getPhone().equals("") || v.getCity().equals("") || v.getState().equals("") || p1.equals("") || p2.equals("")){
                    Snackbar.make(rootLayout, "Please fill in all the required details!", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    if (!p1.equals(p2)){
                        Snackbar.make(rootLayout, "Entered passwords don't match!", Snackbar.LENGTH_SHORT).show();
                    }
                    else{
                        Snackbar.make(rootLayout, "Sign up successful!", Snackbar.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.volInsert(v);
                                adapter.loginInsert(v.getEmail(), p1, VOL_TYPE);
                                finish();
                            }
                        }, 500);

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
