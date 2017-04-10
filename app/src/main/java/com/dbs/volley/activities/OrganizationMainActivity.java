package com.dbs.volley.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import com.dbs.volley.fragments.OrgEventsFragment;
import com.dbs.volley.fragments.OrgProfileFragment;
import com.dbs.volley.fragments.OrgVolunteersFragment;
import com.dbs.volley.fragments.OrganizationsFragment;
import com.dbs.volley.fragments.VolEventsFragment;

public class OrganizationMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseAdapter dbAdapter;
    private String vEmail = "";
    private NavigationView nView;
    private DrawerLayout drawerLayout;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.main_org_toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        fab = (FloatingActionButton)findViewById(R.id.org_events_add_fab);

        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();

        drawerLayout = (DrawerLayout)findViewById(R.id.main_org_drawer_layout);
        nView = (NavigationView)findViewById(R.id.main_org_navigation_view);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        nView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_org_container, new OrgEventsFragment()).commit();
        setCheckedItem(R.id.org_menu_events);
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
            case R.id.org_menu_events:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_org_container, new OrgEventsFragment()).commit();
                setCheckedItem(R.id.org_menu_events);
                fab.setVisibility(View.VISIBLE);
                break;
            case R.id.org_menu_volunteers:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_org_container, new OrgVolunteersFragment()).commit();
                setCheckedItem(R.id.org_menu_volunteers);
                fab.setVisibility(View.GONE);
                break;
            case R.id.org_menu_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_org_container, new OrgProfileFragment()).commit();
                setCheckedItem(R.id.org_menu_profile);
                fab.setVisibility(View.GONE);
                break;
            case R.id.org_menu_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final View ccView = View.inflate(this, R.layout.blank, null);
                builder.setView(ccView);
                builder.setMessage("Are you sure you want to delete your account?");

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String orgEmail = getSharedPreferences(Volley.VOL_DATA, MODE_PRIVATE).getString("orgEmail", "");
                        dbAdapter.orgDelete(orgEmail);
                        dbAdapter.loginDelete(orgEmail);
                        SharedPreferences.Editor editor = getSharedPreferences(Volley.VOL_DATA, MODE_PRIVATE).edit();
                        editor.remove("loggedIn");
                        editor.putBoolean("loggedIn", false);
                        editor.apply();
                        Intent intent = new Intent(OrganizationMainActivity.this, SplashActivity.class);
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
            case R.id.org_menu_logout:
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
                        Intent intent = new Intent(OrganizationMainActivity.this, SplashActivity.class);
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
