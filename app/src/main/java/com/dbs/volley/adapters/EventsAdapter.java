package com.dbs.volley.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dbs.volley.R;
import com.dbs.volley.activities.EventDetailsActivity;
import com.dbs.volley.database.DatabaseAdapter;
import com.dbs.volley.models.Event;
import com.dbs.volley.models.Organization;

import java.util.List;

/**
 * Created by anurag on 8/4/17.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.VolEventViewHolder> {

    private List<Event> eventsList;
    private Activity activity;
    private DatabaseAdapter dbAdapter;
    private Fragment fragment;

    public EventsAdapter(List<Event> eventsList, Activity activity, DatabaseAdapter dbAdapter, Fragment fragment) {
        this.eventsList = eventsList;
        this.activity = activity;
        this.dbAdapter = dbAdapter;
        this.fragment = fragment;
    }

    @Override
    public VolEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VolEventViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_event, parent, false));
    }

    @Override
    public void onBindViewHolder(VolEventViewHolder holder, int position) {
        Event event = eventsList.get(position);
        holder.eventName.setText(event.getName());

        Organization o = dbAdapter.getOrgFromEvent(event.getName());
        if (o != null) holder.eventOrg.setText(o.getName());
        holder.eventTime.setText(event.getEventTime());
        holder.eventDate.setText(event.getEventDate());
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class VolEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView eventName;
        TextView eventOrg;
        TextView eventDate;
        TextView eventTime;

        public VolEventViewHolder(View itemView) {
            super(itemView);
            eventName = (TextView)itemView.findViewById(R.id.event_name_text_view);
            eventOrg = (TextView)itemView.findViewById(R.id.event_org_name_text_view);
            eventDate = (TextView)itemView.findViewById(R.id.event_date_text_view);
            eventTime = (TextView)itemView.findViewById(R.id.event_time_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, EventDetailsActivity.class);
            intent.putExtra("eventName", eventsList.get(getAdapterPosition()).getName());
            intent.putExtra("eventAddress", eventsList.get(getAdapterPosition()).getAddress());
            intent.putExtra("eventCity", eventsList.get(getAdapterPosition()).getCity());
            intent.putExtra("orgEmail", eventsList.get(getAdapterPosition()).getOrgEmail());
            intent.putExtra("eventDate", eventsList.get(getAdapterPosition()).getEventDate());
            intent.putExtra("eventTime", eventsList.get(getAdapterPosition()).getEventTime());
            fragment.startActivityForResult(intent, 1);
        }
    }
}
