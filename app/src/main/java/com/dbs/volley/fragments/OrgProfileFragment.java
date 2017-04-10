package com.dbs.volley.fragments;


import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dbs.volley.R;
import com.dbs.volley.application.Volley;
import com.dbs.volley.database.DatabaseAdapter;
import com.dbs.volley.models.Organization;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrgProfileFragment extends Fragment {
    
    private DatabaseAdapter adapter;

    public OrgProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.edit_profile);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_org_profile, container, false);

        adapter = new DatabaseAdapter(getActivity());
        adapter.open();

        final String orgEmail = getActivity().getSharedPreferences(Volley.VOL_DATA, Context.MODE_PRIVATE).getString("orgEmail", "");
        Organization o = adapter.orgQuery(orgEmail);

        Log.d("Org Email", orgEmail);


        final TextInputEditText name = (TextInputEditText)view.findViewById(R.id.organization_profile_name_edit_text);
        final TextInputEditText address = (TextInputEditText)view.findViewById(R.id.organization_profile_address_edit_text);
        final TextInputEditText phone = (TextInputEditText)view.findViewById(R.id.organization_profile_phone_edit_text);
        final TextInputEditText city = (TextInputEditText)view.findViewById(R.id.organization_profile_city_edit_text);
        final TextInputEditText state = (TextInputEditText)view.findViewById(R.id.organization_profile_state_edit_text);
        final TextInputEditText website = (TextInputEditText)view.findViewById(R.id.organization_profile_website_edit_text);
        final TextInputEditText password = (TextInputEditText)view.findViewById(R.id.organization_profile_password_edit_text);
        final TextInputEditText confirmPassword = (TextInputEditText)view.findViewById(R.id.organization_profile_confirm_password_edit_text);

        TextView email = (TextView)view.findViewById(R.id.org_profile_email_text_view);

        if (o != null){
            name.setText(o.getName());
            address.setText(o.getAddress());
            phone.setText(o.getPhone());
            city.setText(o.getCity());
            state.setText(o.getState());
            website.setText(o.getWebsite());
        }

        password.setText(adapter.loginOrgQuery(orgEmail));
        confirmPassword.setText(adapter.loginOrgQuery(orgEmail));

        email.setText(orgEmail);

        TextView saveButton = (TextView)view.findViewById(R.id.org_profile_save_profile_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Organization o = new Organization();
                o.setName(name.getText().toString());
                o.setEmail(orgEmail);
                o.setAddress(address.getText().toString());
                o.setPhone(phone.getText().toString());
                o.setWebsite(website.getText().toString());
                o.setCity(city.getText().toString());
                o.setState(state.getText().toString());

                final String p1 = password.getText().toString();
                String p2 = confirmPassword.getText().toString();

                if (o.getName().equals("") || o.getEmail().equals("") || o.getPhone().equals("") || o.getCity().equals("") || o.getState().equals("") || p1.equals("") || p2.equals("")){
                    Snackbar.make(view, "Please fill in all the required details!", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    if (!p1.equals(p2)){
                        Snackbar.make(view, "Entered passwords don't match!", Snackbar.LENGTH_SHORT).show();
                    }
                    else{
                        try{
                            adapter.orgUpdate(o);
                            adapter.loginUpdate(orgEmail, p1);
                            Snackbar.make(view, "Changes saved!", Snackbar.LENGTH_SHORT).show();
                        }catch(SQLiteConstraintException ce){
                            Snackbar.make(view, "", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.close();
    }
}
