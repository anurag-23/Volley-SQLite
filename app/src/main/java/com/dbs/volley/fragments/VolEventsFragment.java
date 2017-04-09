package com.dbs.volley.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dbs.volley.R;
import com.dbs.volley.adapters.VolEventsAdapter;
import com.dbs.volley.application.Volley;
import com.dbs.volley.database.DatabaseAdapter;
import com.dbs.volley.models.Event;

import java.util.ArrayList;
import java.util.List;

public class VolEventsFragment extends Fragment {

    private DatabaseAdapter dbAdapter;
    private List<Event> eventsList = new ArrayList<>();
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

        SharedPreferences sp = getActivity().getSharedPreferences(Volley.VOL_DATA, Context.MODE_PRIVATE);
        if (dbAdapter.isVolunteer(sp.getString("volEmail", ""))){
            eventsList = dbAdapter.fetchEvents(dbAdapter.getOrg(sp.getString("volEmail", "")));
        }

        RecyclerView eventsRecyclerView = (RecyclerView)view.findViewById(R.id.vol_events_recycler_view);
        final VolEventsAdapter adapter = new VolEventsAdapter(eventsList, getActivity(), dbAdapter);
        eventsRecyclerView.setAdapter(adapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }

}
