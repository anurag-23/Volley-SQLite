package com.dbs.volley.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.dbs.volley.R;
import com.dbs.volley.application.Volley;
import com.dbs.volley.database.DatabaseAdapter;
import com.dbs.volley.fragments.OrgEventsFragment;
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
                setCheckedItem(R.id.org_menu_profile);
                fab.setVisibility(View.GONE);
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
