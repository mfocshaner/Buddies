package com.huji.foodtricks.buddies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huji.foodtricks.buddies.Models.EventModel;

import java.util.ArrayList;

public class EventListAdaptor extends BaseAdapter {
    Context context;
    ArrayList<EventModel> events;

    public EventListAdaptor(Context context, ArrayList<EventModel> spacecrafts) {
        this.context = context;
        this.events = spacecrafts;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int i) {
        return events.get(i);
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


        TextView eventName = (TextView) view.findViewById(R.id.nameTxt);
        TextView eventDate = (TextView) view.findViewById(R.id.date);

        final String name = event.getTitle();
        String time = event.getTime().toString();
        eventName.setText(name);
        eventDate.setText(time);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewEventIntent = new Intent(view.getContext(), ViewSingleEventActivity.class);
                viewEventIntent.putExtra("event", event);
                context.startActivity(viewEventIntent);
            }
        });

        return view;
    }
}