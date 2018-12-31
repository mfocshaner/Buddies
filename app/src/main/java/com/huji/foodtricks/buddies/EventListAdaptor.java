package com.huji.foodtricks.buddies;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huji.foodtricks.buddies.Models.EventModel;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

public class EventListAdaptor extends BaseAdapter {
    Context context;
    HashMap<String, EventModel> eventModels;
    ArrayList<String> keys;

    public EventListAdaptor(Context context, HashMap<String, EventModel> eventModels) {
        this.context = context;
        this.eventModels = eventModels;
        this.keys = new ArrayList<>(eventModels.keySet());
    }

    public void addItems(HashMap<String, EventModel> events) {
        eventModels.putAll(events);
        this.keys.addAll(events.keySet());
    }

    public void removeItems(HashMap<String, EventModel> events) {
        eventModels.keySet().removeAll(events.keySet());
        this.keys.removeAll(events.keySet());
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


        TextView eventName = view.findViewById(R.id.eventCardName);
        TextView eventDate = view.findViewById(R.id.eventCardDate);
        ImageView eventImage = view.findViewById(R.id.eventCardImage);

        final String name = event.getTitle();
        Calendar dateTime = new GregorianCalendar();
        dateTime.setTime(event.getTime());
        String dateTimeString = MessageFormat.format("{4} {0}/{1} {2}:{3}",
                dateTime.get(Calendar.DATE),
                dateTime.get(Calendar.MONTH) + 1,
                dateTime.get(Calendar.HOUR_OF_DAY),
                String.format(Locale.getDefault(),"%02d",dateTime.get(Calendar.MINUTE)),
                dateTime.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));

        eventName.setText(name);
        eventDate.setText(dateTimeString);
        GlideApp.with(context)
                .load(event.getImageUrl())
                .override(300, 300)
                .into(eventImage);



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent viewEventIntent = new Intent(context, ViewSingleEventActivity.class);
                viewEventIntent.putExtra(context.getResources()
                        .getString(R.string.extra_current_event_model), event);
                viewEventIntent.putExtra(context.getResources()
                        .getString(R.string.extra_current_event_id), key);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context);
                context.startActivity(viewEventIntent, options.toBundle());
            }
        });

        return view;
    }
}