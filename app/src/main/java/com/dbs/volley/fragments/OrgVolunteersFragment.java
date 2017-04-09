package com.dbs.volley.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dbs.volley.R;
import com.dbs.volley.adapters.VolunteersAdapter;
import com.dbs.volley.application.Volley;
import com.dbs.volley.database.DatabaseAdapter;
import com.dbs.volley.models.Volunteer;

import java.util.ArrayList;
import java.util.List;

public class OrgVolunteersFragment extends Fragment {

    private List<Volunteer> volList = new ArrayList<>();
    private DatabaseAdapter dbAdapter;

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

        volList = dbAdapter.getVolFromOrg(getActivity().getSharedPreferences(Volley.VOL_DATA, Context.MODE_PRIVATE).getString("orgEmail", ""));

        RecyclerView volRecyclerView = (RecyclerView)view.findViewById(R.id.org_volunteers_recycler_view);
        VolunteersAdapter adapter = new VolunteersAdapter(volList, getActivity());
        volRecyclerView.setAdapter(adapter);
        volRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }
}
