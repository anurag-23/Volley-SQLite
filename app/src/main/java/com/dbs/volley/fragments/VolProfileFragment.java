package com.dbs.volley.fragments;


import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.dbs.volley.models.Volunteer;

/**
 * A simple {@link Fragment} subclass.
 */
public class VolProfileFragment extends Fragment {

    private DatabaseAdapter adapter;
    
    public VolProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_vol_profile, container, false);
        
        adapter = new DatabaseAdapter(getActivity());
        adapter.open();

        final String volEmail = getActivity().getSharedPreferences(Volley.VOL_DATA, Context.MODE_PRIVATE).getString("volEmail", "");
        Volunteer v = adapter.volQuery(volEmail);

        final TextInputEditText name = (TextInputEditText)view.findViewById(R.id.volunteer_profile_name_edit_text);
        final TextInputEditText phone = (TextInputEditText)view.findViewById(R.id.volunteer_profile_phone_edit_text);
        final TextInputEditText city = (TextInputEditText)view.findViewById(R.id.volunteer_profile_city_edit_text);
        final TextInputEditText state = (TextInputEditText)view.findViewById(R.id.volunteer_profile_state_edit_text);
        final TextInputEditText password = (TextInputEditText)view.findViewById(R.id.volunteer_profile_password_edit_text);
        final TextInputEditText confirmPassword = (TextInputEditText)view.findViewById(R.id.volunteer_profile_confirm_password_edit_text);

        final TextView email = (TextView)view.findViewById(R.id.vol_profile_email_text_view);

        if (v != null){
            name.setText(v.getName());
            phone.setText(v.getPhone());
            city.setText(v.getCity());
            state.setText(v.getState());
        }

        password.setText(adapter.loginVolQuery(volEmail));
        confirmPassword.setText(adapter.loginVolQuery(volEmail));

        email.setText(volEmail);

        TextView saveButton = (TextView)view.findViewById(R.id.vol_profile_save_profile_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Volunteer v = new Volunteer();
                v.setName(name.getText().toString());
                v.setEmail(email.getText().toString());
                v.setPhone(phone.getText().toString());
                v.setCity(city.getText().toString());
                v.setState(state.getText().toString());
                
                final String p1 = password.getText().toString();
                String p2 = confirmPassword.getText().toString();

                if (v.getName().equals("") || v.getEmail().equals("") || v.getPhone().equals("") || v.getCity().equals("") || v.getState().equals("") || p1.equals("") || p2.equals("")){
                    Snackbar.make(view, "Please fill in all the required details!", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    if (!p1.equals(p2)){
                        Snackbar.make(view, "Entered passwords don't match!", Snackbar.LENGTH_SHORT).show();
                    }
                    else{
                        try{
                            adapter.volUpdate(v);
                            adapter.loginUpdate(volEmail, p1);
                            Snackbar.make(view, "Changes saved!", Snackbar.LENGTH_SHORT).show();
                        }catch(SQLiteConstraintException ce){
                            ce.printStackTrace();
                        }
                    }
                }
            }
        });

        return view;
    }

}
