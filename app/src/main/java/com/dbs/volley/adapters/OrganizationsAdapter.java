package com.dbs.volley.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dbs.volley.R;
import com.dbs.volley.activities.OrgDescriptionActivity;
import com.dbs.volley.models.Organization;

import java.util.List;

/**
 * Created by anurag on 6/4/17.
 */
public class OrganizationsAdapter extends RecyclerView.Adapter<OrganizationsAdapter.OrganizationViewHolder> {

    private List<Organization> orgList;
    private Activity activity;

    public OrganizationsAdapter(List<Organization> orgList, Activity activity) {
        this.orgList = orgList;
        this.activity = activity;
    }

    @Override
    public OrganizationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrganizationViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_organization, parent, false));
    }

    @Override
    public void onBindViewHolder(OrganizationViewHolder holder, int position) {
        Organization o = orgList.get(position);

        holder.orgName.setText(o.getName());
        holder.orgCity.setText(o.getCity());
    }

    @Override
    public int getItemCount() {
        return orgList.size();
    }

    class OrganizationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView orgName;
        TextView orgCity;

        public OrganizationViewHolder(View itemView) {
            super(itemView);

            orgName = (TextView)itemView.findViewById(R.id.org_name_text_view);
            orgCity = (TextView)itemView.findViewById(R.id.org_city_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, OrgDescriptionActivity.class);
            intent.putExtra("orgName", orgList.get(getAdapterPosition()).getName());
            intent.putExtra("orgEmail", orgList.get(getAdapterPosition()).getEmail());
            intent.putExtra("orgPhone", orgList.get(getAdapterPosition()).getPhone());
            intent.putExtra("orgAddress", orgList.get(getAdapterPosition()).getAddress());
            intent.putExtra("orgCity", orgList.get(getAdapterPosition()).getCity());
            intent.putExtra("orgState", orgList.get(getAdapterPosition()).getState());
            intent.putExtra("orgWebsite", orgList.get(getAdapterPosition()).getWebsite());

            activity.startActivity(intent);
        }
    }
}
