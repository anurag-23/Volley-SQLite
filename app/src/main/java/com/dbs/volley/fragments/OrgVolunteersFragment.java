package com.dbs.volley.fragments;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dbs.volley.R;
import com.dbs.volley.adapters.VolEventsAdapter;
import com.dbs.volley.adapters.VolunteersAdapter;
import com.dbs.volley.application.Volley;
import com.dbs.volley.database.DatabaseAdapter;
import com.dbs.volley.models.Organization;
import com.dbs.volley.models.Volunteer;

import java.util.ArrayList;
import java.util.List;

public class OrgVolunteersFragment extends Fragment {

    private List<Volunteer> volList = new ArrayList<>();
    private DatabaseAdapter dbAdapter;
    private VolunteersAdapter adapter;
    private String orgEmail = "";

    public OrgVolunteersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.volunteers);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_org_volunteers, container, false);

        dbAdapter = new DatabaseAdapter(getActivity());
        dbAdapter.open();

        setHasOptionsMenu(true);
        
        orgEmail = getActivity().getSharedPreferences(Volley.VOL_DATA, Context.MODE_PRIVATE).getString("orgEmail", "");
        volList = dbAdapter.getVolFromOrg(orgEmail);

        RecyclerView volRecyclerView = (RecyclerView)view.findViewById(R.id.org_volunteers_recycler_view);
        adapter = new VolunteersAdapter(volList, getActivity());
        volRecyclerView.setAdapter(adapter);
        volRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_vol_orgs, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_vol_orgs_search);
        SearchView searchView = (SearchView)searchItem.getActionView();

        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                volList.clear();
                List<Volunteer> tempList = dbAdapter.fetchVol(orgEmail, query);
                volList.addAll(tempList);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                volList.clear();
                List<Volunteer> tempList = dbAdapter.fetchVol(orgEmail, newText);
                volList.addAll(tempList);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                volList.clear();
                List<Volunteer> tempList = dbAdapter.getVolFromOrg(orgEmail);
                volList.addAll(tempList);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }
}
