package com.huji.foodtricks.buddies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huji.foodtricks.buddies.Models.EventModel;

import java.util.ArrayList;
import java.util.HashMap;

public class EventListAdaptor extends BaseAdapter {
    Context context;
    HashMap<String, EventModel> eventModels;
    ArrayList<String> keys;

    public EventListAdaptor(Context context, HashMap<String, EventModel> eventModels) {
        this.context = context;
        this.eventModels = eventModels;
        this.keys = new ArrayList<>(eventModels.keySet());
    }

    public void addItems(HashMap<String,EventModel> events) {
        eventModels.putAll(events);
    }

    @Override
    public int getCount() {
        return eventModels.size();
    }

    @Override
    public Object getItem(int i) {
        return eventModels.get(this.keys.get(i));
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.event_card, viewGroup, false);
        }

        final EventModel event = (EventModel) this.getItem(i);
        final String key = this.keys.get(i);


        TextView eventName = (TextView) view.findViewById(R.id.nameTxt);
        TextView eventDate = (TextView) view.findViewById(R.id.date);

        final String name = event.getTitle();
        String time = event.getTime().toString();
        eventName.setText(name);
        eventDate.setText(time);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent viewEventIntent = new Intent(context, ViewSingleEventActivity.class);
                viewEventIntent.putExtra(context.getResources()
                        .getString(R.string.extra_current_event_model), event);
                viewEventIntent.putExtra(context.getResources()
                        .getString(R.string.extra_current_event_id), key);

                context.startActivity(viewEventIntent);
            }
        });

        return view;
    }
}