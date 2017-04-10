package com.dbs.volley.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dbs.volley.R;
import com.dbs.volley.application.Volley;

public class SplashActivity extends AppCompatActivity {
    private static final int VOL_LOGIN = 0;
    private static final int ORG_LOGIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView orgButton = (TextView)findViewById(R.id.organization_button);
        TextView volButton = (TextView)findViewById(R.id.volunteer_button);
        LinearLayout splashLayout = (LinearLayout)findViewById(R.id.splash_linear_layout);

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        SharedPreferences sp = getSharedPreferences(Volley.VOL_DATA, MODE_PRIVATE);

        if (sp.getBoolean("loggedIn", true)){
            Intent intent = null;
            if (sp.getString("loggedInAs", "").equals("volunteer")){
                intent = new Intent(this, VolunteerMainActivity.class);
                startActivity(intent);
                finish();
            }
            else if(sp.getString("loggedInAs", "").equals("organization")){
                intent = new Intent(this, OrganizationMainActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                splashLayout.setVisibility(View.VISIBLE);
            }
        }
        else{
            splashLayout.setVisibility(View.VISIBLE);
        }

        volButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.putExtra("loginType", VOL_LOGIN);
                startActivity(intent);
                finish();
            }
        });

        orgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.putExtra("loginType", ORG_LOGIN);
                startActivity(intent);
                finish();
            }
        });
    }
}
