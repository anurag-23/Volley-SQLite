package com.dbs.volley.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CollapsingToolbarLayout;
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
import com.dbs.volley.models.Organization;

public class LoginActivity extends AppCompatActivity {
    private static final int VOL_LOGIN = 0;
    private static final int ORG_LOGIN = 1;
    private static final String VOL_TITLE = "Volunteer Login";
    private static final String ORG_TITLE = "Organization Login";
    private DatabaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(getSupportActionBar() !=null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CoordinatorLayout rootLayout = (CoordinatorLayout)findViewById(R.id.login_root_layout);

        CollapsingToolbarLayout loginCollapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.login_collapsing_toolbar);
        Toolbar loginToolbar = (Toolbar)findViewById(R.id.login_toolbar);

        adapter = new DatabaseAdapter(this);
        adapter.open();

        setSupportActionBar(loginToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getIntent().getIntExtra("loginType", 0) == VOL_LOGIN ? VOL_TITLE : ORG_TITLE);
        }

        final TextInputEditText email = (TextInputEditText)findViewById(R.id.login_email_edit_text);
        final TextInputEditText password = (TextInputEditText)findViewById(R.id.login_password_edit_text);

        TextView loginButton = (TextView)findViewById(R.id.login_button);
        TextView signUpButton = (TextView)findViewById(R.id.sign_up_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String i1 = email.getText().toString();
                String i2 = password.getText().toString();

                if (i1.equals("") || i2.equals("")){
                    Snackbar.make(rootLayout, "Please enter details to login!", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    String pwd = (getIntent().getIntExtra("loginType", 0) == VOL_LOGIN ? adapter.loginVolQuery(i1) : adapter.loginOrgQuery(i1));
                    if (pwd!=null && pwd.equals(i2)){
                        //Snackbar.make(rootLayout, "Login successful!", Snackbar.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = getSharedPreferences(Volley.VOL_DATA, MODE_PRIVATE).edit();
                        Intent intent = null;

                        if (getIntent().getIntExtra("loginType", 0) == VOL_LOGIN){
                            intent = new Intent(LoginActivity.this, VolunteerMainActivity.class);
                            editor.putString("volEmail", i1);
                        }
                        else{
                            intent = new Intent(LoginActivity.this, OrganizationMainActivity.class);
                            editor.putString("orgEmail", i1);
                        }
                        editor.apply();
                        startActivity(intent);
                    }
                    else{
                        Snackbar.make(rootLayout, "Login failed!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if (getIntent().getIntExtra("loginType", 0) == VOL_LOGIN){
                    intent = new Intent(LoginActivity.this, VolunteerSignUpActivity.class);
                }
                else{
                    intent = new Intent(LoginActivity.this, OrganizationSignUpActivity.class);
                }
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.close();
    }
}
