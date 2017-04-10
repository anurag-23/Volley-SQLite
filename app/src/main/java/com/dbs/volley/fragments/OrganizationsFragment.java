package com.dbs.volley.fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dbs.volley.R;
import com.dbs.volley.adapters.OrganizationsAdapter;
import com.dbs.volley.database.DatabaseAdapter;
import com.dbs.volley.models.Organization;

import java.util.ArrayList;
import java.util.List;

public class OrganizationsFragment extends Fragment {

    private List<Organization> orgList = new ArrayList<>();
    private DatabaseAdapter dbAdapter;
    private OrganizationsAdapter adapter;

    public OrganizationsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_organizations, container, false);
        getActivity().setTitle(R.string.organizations);
        dbAdapter = new DatabaseAdapter(getActivity());
        dbAdapter.open();

        orgList = dbAdapter.fetchOrg();
        RecyclerView orgRecyclerView = (RecyclerView)view.findViewById(R.id.organizations_recycler_view);

        adapter = new OrganizationsAdapter(orgList, getActivity(), dbAdapter);
        orgRecyclerView.setAdapter(adapter);
        orgRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                orgList.clear();
                List<Organization> tempList = dbAdapter.fetchOrg(query);
                orgList.addAll(tempList);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                orgList.clear();
                List<Organization> tempList = dbAdapter.fetchOrg(newText);
                orgList.addAll(tempList);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                orgList.clear();
                List<Organization> tempList = dbAdapter.fetchOrg();
                orgList.addAll(tempList);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

}
