package com.dbs.volley.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
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
import com.dbs.volley.activities.CreateEventActivity;
import com.dbs.volley.adapters.VolEventsAdapter;
import com.dbs.volley.application.Volley;
import com.dbs.volley.database.DatabaseAdapter;
import com.dbs.volley.models.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OrgEventsFragment extends Fragment {

    private DatabaseAdapter dbAdapter;
    private List<Event> eventsList = new ArrayList<>();
    private static final int CREATE_EVENT = 0;
    private VolEventsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.events);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_org_events, container, false);

        dbAdapter = new DatabaseAdapter(getActivity());
        dbAdapter.open();

        eventsList = dbAdapter.fetchEvents(getActivity().getSharedPreferences(Volley.VOL_DATA, Context.MODE_PRIVATE).getString("orgEmail", ""));

        FloatingActionButton addFab = (FloatingActionButton)getActivity().findViewById(R.id.org_events_add_fab);

        RecyclerView eventsRecyclerView = (RecyclerView)view.findViewById(R.id.org_events_recycler_view);
        adapter = new VolEventsAdapter(eventsList, getActivity(), dbAdapter);
        eventsRecyclerView.setAdapter(adapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Event e = new Event();
                e.setName("Cloth Distribution Drive");
                e.setOrgEmail("tfi@gmail.com");
                e.setEventDate("10/04/2017");
                e.setEventTime("5:30 PM");
                e.setAddress("MIT");
                e.setCity("Manipal");
                try{
                    dbAdapter.eventInsert(e);
                    eventsList.add(e);
                    adapter.notifyDataSetChanged();
                }catch(SQLiteConstraintException ce){
                    Snackbar.make(view, "Event already exists!", Snackbar.LENGTH_SHORT).show();
                }*/
                Intent intent = new Intent(getActivity(), CreateEventActivity.class);
                startActivityForResult(intent, CREATE_EVENT);
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_EVENT && resultCode == Activity.RESULT_OK){
            Log.d("Reached", "heree");
            List<Event> tempList = dbAdapter.fetchEvents(getActivity().getSharedPreferences(Volley.VOL_DATA, Context.MODE_PRIVATE).getString("orgEmail", ""));
            eventsList.clear();
            eventsList.addAll(tempList);
            adapter.notifyDataSetChanged();
        }
    }
}
