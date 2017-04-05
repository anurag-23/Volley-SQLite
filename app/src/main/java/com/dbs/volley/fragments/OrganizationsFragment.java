package com.dbs.volley.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dbs.volley.R;
import com.dbs.volley.adapters.OrganizationsAdapter;
import com.dbs.volley.database.DatabaseAdapter;
import com.dbs.volley.models.Organization;

import java.util.ArrayList;
import java.util.List;

public class OrganizationsFragment extends Fragment {

    List<Organization> orgList = new ArrayList<>();

    public OrganizationsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_organizations, container, false);
        getActivity().setTitle(R.string.organizations);
        DatabaseAdapter dbAdapter = new DatabaseAdapter(getActivity());
        dbAdapter.open();

        RecyclerView orgRecyclerView = (RecyclerView)view.findViewById(R.id.organizations_recycler_view);
        OrganizationsAdapter adapter = new OrganizationsAdapter(dbAdapter.fetchOrg(), getActivity());
        orgRecyclerView.setAdapter(adapter);
        orgRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

}
