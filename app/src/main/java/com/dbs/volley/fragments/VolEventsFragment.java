package com.dbs.volley.fragments;


import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.dbs.volley.adapters.EventsAdapter;
import com.dbs.volley.application.Volley;
import com.dbs.volley.database.DatabaseAdapter;
import com.dbs.volley.models.Event;

import java.util.ArrayList;
import java.util.List;

public class VolEventsFragment extends Fragment {

    private DatabaseAdapter dbAdapter;
    private List<Event> eventsList = new ArrayList<>();
    private EventsAdapter adapter;

    public VolEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.events);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_vol_events, container, false);
        dbAdapter = new DatabaseAdapter(getActivity());
        dbAdapter.open();

        setHasOptionsMenu(true);

        SharedPreferences sp = getActivity().getSharedPreferences(Volley.VOL_DATA, Context.MODE_PRIVATE);
        if (dbAdapter.isVolunteer(sp.getString("volEmail", ""))){
            eventsList = dbAdapter.fetchEvents(dbAdapter.getOrg(sp.getString("volEmail", "")));
        }

        RecyclerView eventsRecyclerView = (RecyclerView)view.findViewById(R.id.vol_events_recycler_view);
        adapter = new EventsAdapter(eventsList, getActivity(), dbAdapter, this);
        eventsRecyclerView.setAdapter(adapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                eventsList.clear();
                List<Event> tempList = dbAdapter.fetchEvents(dbAdapter.getOrg(getActivity().getSharedPreferences(Volley.VOL_DATA, Context.MODE_PRIVATE).getString("volEmail", "")), query);
                eventsList.addAll(tempList);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                eventsList.clear();
                List<Event> tempList = dbAdapter.fetchEvents(dbAdapter.getOrg(getActivity().getSharedPreferences(Volley.VOL_DATA, Context.MODE_PRIVATE).getString("volEmail", "")), newText);
                eventsList.addAll(tempList);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                eventsList.clear();
                List<Event> tempList = dbAdapter.fetchEvents(dbAdapter.getOrg(getActivity().getSharedPreferences(Volley.VOL_DATA, Context.MODE_PRIVATE).getString("volEmail", "")));
                eventsList.addAll(tempList);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

}
