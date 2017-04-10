package com.dbs.volley.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.dbs.volley.R;
import com.dbs.volley.application.Volley;
import com.dbs.volley.database.DatabaseAdapter;
import com.dbs.volley.fragments.OrganizationsFragment;
import com.dbs.volley.fragments.VolEventsFragment;
import com.dbs.volley.fragments.VolProfileFragment;
import com.dbs.volley.models.Volunteer;

public class VolunteerMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseAdapter dbAdapter;
    private String vEmail = "";
    private NavigationView nView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();

        drawerLayout = (DrawerLayout)findViewById(R.id.main_drawer_layout);
        nView = (NavigationView)findViewById(R.id.main_navigation_view);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        vEmail = getSharedPreferences(Volley.VOL_DATA, MODE_PRIVATE).getString("volEmail", "");

        if (dbAdapter.isVolunteer(vEmail)){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new VolEventsFragment()).commit();
            nView.getMenu().findItem(R.id.vol_menu_events).setChecked(true);
        }
        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new OrganizationsFragment()).commit();
            nView.getMenu().findItem(R.id.vol_menu_orgs).setChecked(true);
        }

        nView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers();
        switch (item.getItemId()){
            case R.id.vol_menu_events:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new VolEventsFragment()).commit();
                setCheckedItem(R.id.vol_menu_events);
                break;
            case R.id.vol_menu_orgs:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new OrganizationsFragment()).commit();
                setCheckedItem(R.id.vol_menu_orgs);
                break;
            case R.id.vol_menu_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new VolProfileFragment()).commit();
                setCheckedItem(R.id.vol_menu_profile);
                break;
            case R.id.vol_menu_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final View ccView = View.inflate(this, R.layout.blank, null);
                builder.setView(ccView);
                builder.setMessage("Are you sure you want to delete your account?");

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String volEmail = getSharedPreferences(Volley.VOL_DATA, MODE_PRIVATE).getString("volEmail", "");
                        dbAdapter.volDelete(volEmail);
                        dbAdapter.loginDelete(volEmail);
                        SharedPreferences.Editor editor = getSharedPreferences(Volley.VOL_DATA, MODE_PRIVATE).edit();
                        editor.remove("loggedIn");
                        editor.putBoolean("loggedIn", false);
                        editor.apply();
                        Intent intent = new Intent(VolunteerMainActivity.this, SplashActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                break;
            case R.id.vol_menu_logout:
                AlertDialog.Builder newBuilder = new AlertDialog.Builder(this);
                final View newView = View.inflate(this, R.layout.blank, null);
                newBuilder.setView(newView);
                newBuilder.setMessage("Are you sure you want to log out?");

                // Set up the buttons
                newBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SharedPreferences.Editor editor = getSharedPreferences(Volley.VOL_DATA, MODE_PRIVATE).edit();
                        editor.remove("loggedIn");
                        editor.putBoolean("loggedIn", false);
                        editor.apply();
                        Intent intent = new Intent(VolunteerMainActivity.this, SplashActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                newBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                newBuilder.show();
                break;
        }
        return false;
    }

    private void setCheckedItem(int itemID){
        for (int i=0; i<nView.getMenu().size(); i++){
            nView.getMenu().getItem(i).setChecked(false);
        }
        nView.getMenu().findItem(itemID).setChecked(true);
    }
}
