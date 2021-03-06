package com.dbs.volley.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dbs.volley.R;
import com.dbs.volley.activities.VolunteerDetailsActivity;
import com.dbs.volley.models.Volunteer;

import java.util.List;

public class VolunteersAdapter extends RecyclerView.Adapter<VolunteersAdapter.VolunteerViewHolder> {

    private List<Volunteer> volList;
    private Activity activity;

    public VolunteersAdapter(List<Volunteer> volList, Activity activity) {
        this.volList = volList;
        this.activity = activity;
    }

    @Override
    public VolunteerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VolunteerViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_volunteer, parent, false));
    }

    @Override
    public void onBindViewHolder(VolunteerViewHolder holder, int position) {
        Volunteer v = volList.get(position);
        holder.volName.setText(v.getName());
        holder.volCity.setText(v.getCity());
    }

    @Override
    public int getItemCount() {
        return volList.size();
    }

    public class VolunteerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView volName;
        TextView volCity;

        public VolunteerViewHolder(View itemView) {
            super(itemView);
            volName = (TextView)itemView.findViewById(R.id.vol_name_text_view);
            volCity = (TextView)itemView.findViewById(R.id.vol_city_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, VolunteerDetailsActivity.class);
            intent.putExtra("volName", volList.get(getAdapterPosition()).getName());
            intent.putExtra("volCity", volList.get(getAdapterPosition()).getCity());
            intent.putExtra("volState", volList.get(getAdapterPosition()).getState());
            intent.putExtra("volPhone", volList.get(getAdapterPosition()).getPhone());
            intent.putExtra("volEmail", volList.get(getAdapterPosition()).getEmail());
            activity.startActivity(intent);
        }
    }
}
