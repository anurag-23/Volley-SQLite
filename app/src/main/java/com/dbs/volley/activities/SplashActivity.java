package com.dbs.volley.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dbs.volley.R;

public class SplashActivity extends AppCompatActivity {
    private static final int VOL_LOGIN = 0;
    private static final int ORG_LOGIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView orgButton = (TextView)findViewById(R.id.organization_button);
        TextView volButton = (TextView)findViewById(R.id.volunteer_button);

        volButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.putExtra("loginType", VOL_LOGIN);
                startActivity(intent);
            }
        });

        orgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.putExtra("loginType", ORG_LOGIN);
                startActivity(intent);
            }
        });
    }
}
